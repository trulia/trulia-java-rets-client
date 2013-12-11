package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationLookupType extends MetaObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDTEXT = "ValidText";
	public static final String PARENT1VALUE = "Parent1Value";
	public static final String PARENT2VALUE = "Parent2Value";

	public MValidationLookupType() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidText() {
		return getStringAttribute(VALIDTEXT);
	}

	public String getParent1Value() {
		return getStringAttribute(PARENT1VALUE);
	}

	public String getParent2Value() {
		return getStringAttribute(PARENT2VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDTEXT, sAlphanum32);
		attributeMap.put(PARENT1VALUE, sAlphanum32);
		attributeMap.put(PARENT2VALUE, sAlphanum32);
	}

}
