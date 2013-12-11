package org.realtors.rets.common.metadata;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
//import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.common.metadata.attrib.AttrAlphanum;
import org.realtors.rets.common.metadata.attrib.AttrBoolean;
import org.realtors.rets.common.metadata.attrib.AttrDate;
import org.realtors.rets.common.metadata.attrib.AttrNumeric;
import org.realtors.rets.common.metadata.attrib.AttrNumericPositive;
import org.realtors.rets.common.metadata.attrib.AttrPlaintext;
import org.realtors.rets.common.metadata.attrib.AttrText;
import org.realtors.rets.common.metadata.attrib.AttrVersion;
import org.realtors.rets.common.util.CaseInsensitiveTreeMap;

public abstract class MetaObject implements Serializable {
	private static final Log LOG = LogFactory.getLog(MetaObject.class);

	/** a standard parser used by different child types */
	protected static final AttrType sAlphanum = new AttrAlphanum(0, 0);
	protected static final AttrType sAlphanum64 = new AttrAlphanum(1, 64);
	protected static final AttrType sAlphanum32 = new AttrAlphanum(1, 32);
	protected static final AttrType sAlphanum24 = new AttrAlphanum(1, 24);
	protected static final AttrType sAlphanum10 = new AttrAlphanum(1, 10);
	protected static final AttrType sPlaintext = new AttrPlaintext(0, 0);
	protected static final AttrType sPlaintext1024 = new AttrPlaintext(1, 1024);
	protected static final AttrType sPlaintext512 = new AttrPlaintext(1, 512);
	protected static final AttrType sPlaintext128 = new AttrPlaintext(1, 128);
	protected static final AttrType sPlaintext64 = new AttrPlaintext(1, 64);
	protected static final AttrType sPlaintext32 = new AttrPlaintext(1, 32);
	protected static final AttrType sText = new AttrText(0, 0);
	protected static final AttrType sText1024 = new AttrText(1, 1024);
	protected static final AttrType sText512 = new AttrText(1, 512);
	protected static final AttrType sText256 = new AttrText(1, 256);
	protected static final AttrType sText128 = new AttrText(1, 128);
	protected static final AttrType sText64 = new AttrText(1, 64);
	protected static final AttrType sText32 = new AttrText(1, 32);
	protected static final AttrType sAttrBoolean = new AttrBoolean();
	protected static final AttrType sAttrDate = new AttrDate();
	protected static final AttrType sAttrNumeric = new AttrNumeric();
	protected static final AttrType sAttrNumericPositive = new AttrNumericPositive();
	protected static final AttrType sAttrVersion = new AttrVersion();
	protected static final AttrType sAttrMetadataEntryId = sAlphanum32;
	protected static final MetadataType[] sNoChildren = new MetadataType[0];

	protected static final AttrType retsid = sAlphanum32;
	protected static final AttrType retsname = sAlphanum64;
	
	public static final boolean STRICT_PARSING = true;
	public static final boolean LOOSE_PARSING = false;
	public static final boolean DEFAULT_PARSING = LOOSE_PARSING;

	/** the metdata path to this object */
	protected String path;
	/** map of child type to map of child id to child object */
	protected Map childTypes;
	/** map of attribute name to attribute object (as parsed by attrtype) */
	protected Map attributes;
	/** map of attribute name to AttrType parser */
	protected Map attrTypes;


	private static Map<CacheKey,Map> sAttributeMapCache = new HashMap<CacheKey,Map>();
	private MetaCollector mCollector;
	private boolean strict;

	public MetaObject(boolean strictParsing) {
		this.strict = strictParsing;
		if (strictParsing) {
			this.attributes = new HashMap();
		} else {
			this.attributes = new CaseInsensitiveTreeMap();
		}
		this.attrTypes = this.getAttributeMap(strictParsing);
		MetadataType[] types = getChildTypes();
		this.childTypes = new HashMap();
		for (int i = 0; i < types.length; i++) {
			this.childTypes.put(types[i], null);
		}
	}

	private Map getAttributeMap(boolean strictParsing) {
		synchronized (sAttributeMapCache) {
			Map<CacheKey,Map> map = sAttributeMapCache.get(new CacheKey(this, strictParsing));
			if (map == null) {
				if (strictParsing) {
					map = new HashMap();
				} else {
					map = new CaseInsensitiveTreeMap();
				}
				addAttributesToMap(map);
				// Let's make sure no one mucks with the map later
				map = Collections.unmodifiableMap(map);
				sAttributeMapCache.put(new CacheKey(this, strictParsing), map);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding to attribute cache: " + this.getClass().getName() + ", " + strictParsing);
				}
			}
			return map;
		}
	}

	public static void clearAttributeMapCache() {
		synchronized (sAttributeMapCache) {
			sAttributeMapCache.clear();
		}
	}

	public Collection getChildren(MetadataType type) {
		if (!this.childTypes.containsKey(type)) {
			// throw new IllegalArgumentException?
			return null;
		}
		Object o = this.childTypes.get(type);
		if (o == null) {
			if (!fetchChildren(type)) {
				return Collections.EMPTY_SET;
			}
			o = this.childTypes.get(type);
		}
		if (o instanceof Map) {
			Map m = (Map) o;
			return m.values();
		}
		return (Collection) o;
	}

	private boolean fetchChildren(MetadataType type) {
		this.childTypes.put(type, new HashMap());
		try {
			MetaObject[] children = null;
			if (this.mCollector != null) {
				children = this.mCollector.getMetadata(type, getPath());
			}
			if (children == null) {
				return false;
			}
			for (int i = 0; i < children.length; i++) {
				MetaObject child = children[i];
				addChild(type, child);
			}
		} catch (MetadataException e) {
			LOG.error(toString() + " unable to fetch " + type.name() + " children");
			return false;
		}
		return true;
	}

	public MetaObject getChild(MetadataType type, String id) {
		if (id == null) {
			return null;
		}
		try {
			if (this.childTypes.get(type) == null && this.mCollector != null) {
				if (!fetchChildren(type)) {
					return null;
				}
			}
			Map m = (Map) this.childTypes.get(type);
			if (m == null) {
				return null;
			}
			return (MetaObject) m.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}

	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public Set getKnownAttributes() {
		return this.attrTypes.keySet();
	}

	public String getAttributeAsString(String key) {
		Object value = this.attributes.get(key);
		if (value == null) {
			return null;
		}
		if (this.attrTypes.containsKey(key)) {
			AttrType type = (AttrType) this.attrTypes.get(key);
			return type.render(value);
		}
		return value.toString();
	}

	protected Object getTypedAttribute(String key, Class type) {
		AttrType atype = (AttrType) this.attrTypes.get(key);
		if (atype == null) {
			return null;
		}
		if (atype.getType() == type) {
			return this.attributes.get(key);
		} 
		LOG.warn("type mismatch, expected " + type.getName() + " but" + " got " + atype.getType().getName());
		return null;
	}

	public String getDateAttribute(String key) {
		return (String) getTypedAttribute(key, String.class);
	}

	public String getStringAttribute(String key) {
		return (String) getTypedAttribute(key, String.class);
	}

	public int getIntAttribute(String key) {
		Integer i = (Integer) getTypedAttribute(key, Integer.class);
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}

	public boolean getBooleanAttribute(String key) {
		Boolean b = (Boolean) getTypedAttribute(key, Boolean.class);
		if (b == null) {
			return false;
		}
		return b.booleanValue();
	}

	public void setAttribute(String key, String value) {
		if (value == null) {
			// LOG.warning()
			return;
		}
		if (this.attrTypes.containsKey(key)) {
			AttrType type = (AttrType) this.attrTypes.get(key);
			try {
				this.attributes.put(key, type.parse(value,this.strict));
			} catch (MetaParseException e) {
				LOG.warn(toString() + " couldn't parse attribute " + key + ", value " + value + ": " + e.getMessage());
			}
		} else {
			this.attributes.put(key, value);
			LOG.warn("Unknown key (" + toString() + "): " + key);
		}
	}

	public void addChild(MetadataType type, MetaObject child) {
		if (this.childTypes.containsKey(type)) {
			Object obj = this.childTypes.get(type);
			Map map;
			if (obj == null) {
				map = new HashMap();
				this.childTypes.put(type, map);
			} else {
				map = (Map) obj;
			}
			if (child == null) {
				return;
			}
			String id = child.getId();

			child.setPath(this.getPath());
			child.setCollector(this.mCollector);
			if (id != null) {
				map.put(id, child);
			}
			return;
		}
	}

	public String getId() {
		String idAttr = getIdAttr();
		if (idAttr == null) {
			/** cheap hack so everything's a damn map */
			return Integer.toString(hashCode());
		}
		return getAttributeAsString(idAttr);
	}

	public String getPath() {
		return this.path;
	}

	protected void setPath(String parent) {
		if (parent == null || parent.equals("")) {
			this.path = getId();
		} else {
			this.path = parent + ":" + getId();
		}
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this);
		Iterator iter = getKnownAttributes().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			tsb.append(key, getAttributeAsString(key));
		}
		return tsb.toString();
	}

	public void setCollector(MetaCollector c) {
		this.mCollector = c;
		Iterator iterator = this.childTypes.keySet().iterator();
		while (iterator.hasNext()) {
			MetadataType type = (MetadataType) iterator.next();
			Map map = (Map) this.childTypes.get(type);
			if (map == null) {
				continue;
			}
			Collection children = map.values();
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				MetaObject object = (MetaObject) iter.next();
				object.setCollector(c);
			}
		}
	}

	public abstract MetadataType[] getChildTypes();

	protected abstract String getIdAttr();

	/**
	 * Adds attributes to an attribute map.  This is called by the MetaObject
	 * constructor to initialize a map of atributes.  This map may be cached,
	 * so this method may not be called for every object construction.
	 *
	 * @param attributeMap Map to add attributes to
	 */
	protected abstract void addAttributesToMap(Map attributeMap);

}

class CacheKey {
	private Class mClass;
	private boolean strictParsing;

	public CacheKey(MetaObject metaObject, boolean strictParsing) {
		this.mClass = metaObject.getClass();
		this.strictParsing = strictParsing;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CacheKey)) {
			return false;
		}
		CacheKey rhs = (CacheKey) obj;
		return new EqualsBuilder().append(this.mClass, rhs.mClass).append(this.strictParsing, rhs.strictParsing).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.mClass).append(this.strictParsing).toHashCode();
	}

}

