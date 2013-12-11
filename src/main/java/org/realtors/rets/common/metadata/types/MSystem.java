package org.realtors.rets.common.metadata.types;

//import java.util.Date;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MSystem extends MetaObject {
	public static final String SYSTEMID = "SystemID";
	public static final String SYSTEMDESCRIPTION = "SystemDescription";
	public static final String COMMENTS = "Comments";
	public static final String DATE = "Date";
	public static final String VERSION = "Version";
	public static final String TIMEZONEOFFSET = "TimeZoneOffset";

	public MSystem() {
		this(DEFAULT_PARSING);
	}

	public MSystem(boolean strictParsing) {
		super(strictParsing);
	}

	public String getSystemID() {
		return getStringAttribute(SYSTEMID);
	}

	public String getComment() {
		return getStringAttribute(COMMENTS);
	}

	public String getSystemDescription() {
		return getStringAttribute(SYSTEMDESCRIPTION);
	}

	public String getDate() {
		return getDateAttribute(DATE);
	}
	
	public String getTimeZoneOffset() {
		return getDateAttribute(TIMEZONEOFFSET);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public MResource getMResource(String resourceID) {
		return (MResource) getChild(MetadataType.RESOURCE, resourceID);
	}

	public MResource[] getMResources() {
		MResource[] tmpl = new MResource[0];
		return (MResource[]) getChildren(MetadataType.RESOURCE).toArray(tmpl);
	}

	public MForeignKey getMForeignKey(String foreignKeyID) {
		return (MForeignKey) getChild(MetadataType.FOREIGNKEYS, foreignKeyID);
	}

	public MForeignKey[] getMForeignKeys() {
		MForeignKey[] tmpl = new MForeignKey[0];
		return (MForeignKey[]) getChildren(MetadataType.FOREIGNKEYS).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	public static final MetadataType[] CHILDREN = { MetadataType.RESOURCE, MetadataType.FOREIGNKEYS };

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(SYSTEMID, sAlphanum10);
		attributeMap.put(SYSTEMDESCRIPTION, sPlaintext64);
		attributeMap.put(DATE, sAttrDate);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(COMMENTS, sText);
		attributeMap.put(TIMEZONEOFFSET, sAttrDate);
	}

}
