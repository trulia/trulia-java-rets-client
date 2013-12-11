package org.realtors.rets.common.metadata.types;

//import java.util.Date;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MUpdate extends MetaObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATENAME = "UpdateName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	public static final String UPDATETYPEVERSION = "UpdateTypeVersion";
	public static final String UPDATETYPEDATE = "UpdateTypeDate";

	public MUpdate() {
		this(DEFAULT_PARSING);
	}

	public MUpdate(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateName() {
		return getStringAttribute(UPDATENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getVersion() {
		int v = getIntAttribute(VERSION);
		if (v == 0){
			v = getIntAttribute(UPDATETYPEVERSION);
		}
		return v;
	}

	public String getDate() {
		String d = getDateAttribute(DATE);
		if (d == null ){
			d = getDateAttribute(UPDATETYPEDATE);
		}
		return d;
	}

	public MUpdateType getMUpdateType(String systemName) {
		return (MUpdateType) getChild(MetadataType.UPDATE_TYPE, systemName);
	}

	public MUpdateType[] getMUpdateTypes() {
		MUpdateType[] tmpl = new MUpdateType[0];
		return (MUpdateType[]) getChildren(MetadataType.UPDATE_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	@Override
	protected String getIdAttr() {
		return UPDATENAME;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(UPDATENAME, sAlphanum24);
		attributeMap.put(DESCRIPTION, sPlaintext64);
		attributeMap.put(KEYFIELD, sAlphanum32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
		attributeMap.put(UPDATETYPEVERSION, sAttrVersion);
		attributeMap.put(UPDATETYPEDATE, sAttrDate);
	}

	private static final MetadataType[] sTypes = { MetadataType.UPDATE_TYPE };
}
