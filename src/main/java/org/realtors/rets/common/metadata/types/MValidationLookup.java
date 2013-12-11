package org.realtors.rets.common.metadata.types;

//import java.util.Date;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationLookup extends MetaObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String PARENT1FIELD = "Parent1Field";
	public static final String PARENT2FIELD = "Parent2Field";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	private static final MetadataType[] sChildren = { MetadataType.VALIDATION_LOOKUP_TYPE };

	public MValidationLookup() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookup(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public String getParent1Field() {
		return getStringAttribute(PARENT1FIELD);
	}

	public String getParent2Field() {
		return getStringAttribute(PARENT2FIELD);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public String getDate() {
		return getDateAttribute(DATE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONLOOKUPNAME;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDATIONLOOKUPNAME, sAlphanum32);
		attributeMap.put(PARENT1FIELD, sAlphanum32);
		attributeMap.put(PARENT2FIELD, sAlphanum32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
	}

	public MValidationLookupType[] getMValidationLookupTypes() {
		MValidationLookupType[] tmpl = new MValidationLookupType[0];
		return (MValidationLookupType[]) getChildren(MetadataType.VALIDATION_LOOKUP_TYPE).toArray(tmpl);
	}

}
