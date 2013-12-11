package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MObject extends MetaObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String OBJECTTYPE = "ObjectType";
	public static final String MIMETYPE = "MimeType";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String OBJECTTIMESTAMPNAME = "ObjectTimeStamp";
	public static final String OBJECTCOUNT = "ObjectCount";
	public static final String STANDARDNAME = "StandardName";
	public MObject() {
		this(DEFAULT_PARSING);
	}

	public MObject(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getObjectType() {
		return getStringAttribute(OBJECTTYPE);
	}

	public String getMIMEType() {
		return getStringAttribute(MIMETYPE);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}
	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return OBJECTTYPE;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAlphanum24);
		attributeMap.put(OBJECTTYPE, sAlphanum24);
		attributeMap.put(MIMETYPE, sText64);
		attributeMap.put(VISIBLENAME, sPlaintext64);
		attributeMap.put(DESCRIPTION, sPlaintext128);
		attributeMap.put(OBJECTTIMESTAMPNAME, retsname);
		attributeMap.put(OBJECTCOUNT, retsname);
		attributeMap.put(STANDARDNAME, retsname);
	}

	public String getObjectTimeStampName() {
		return getStringAttribute(OBJECTTIMESTAMPNAME);
	}
	
	public String getObjectCount() {
		return getStringAttribute(OBJECTCOUNT);
	}

}
