package org.realtors.rets.common.metadata.types;

//import java.util.Date;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternal extends MetaObject {

	private static final MetadataType[] CHILDREN = { MetadataType.VALIDATION_EXTERNAL_TYPE };
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String SEARCHRESOURCE = "SearchResource";
	public static final String SEARCHCLASS = "SearchClass";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

	public MValidationExternal() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternal(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public String getSearchResource() {
		return getStringAttribute(SEARCHRESOURCE);
	}

	public String getSearchClass() {
		return getStringAttribute(SEARCHCLASS);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public String getDate() {
		return getDateAttribute(DATE);
	}

	public MValidationExternalType[] getMValidationExternalTypes() {
		MValidationExternalType[] tmpl = new MValidationExternalType[0];
		return (MValidationExternalType[]) getChildren(MetadataType.VALIDATION_EXTERNAL_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXTERNALNAME;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDATIONEXTERNALNAME, sAlphanum32);
		attributeMap.put(SEARCHRESOURCE, sAlphanum32);
		attributeMap.put(SEARCHCLASS, sAlphanum32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
	}

}
