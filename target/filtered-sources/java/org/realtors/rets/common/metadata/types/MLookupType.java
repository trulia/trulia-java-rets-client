package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MLookupType extends MetaObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LONGVALUE = "LongValue";
	public static final String SHORTVALUE = "ShortValue";
	public static final String VALUE = "Value";

	public MLookupType() {
		this(DEFAULT_PARSING);
	}

	public MLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getLongValue() {
		return getStringAttribute(LONGVALUE);
	}

	public String getShortValue() {
		return getStringAttribute(SHORTVALUE);
	}

	public String getValue() {
		return getStringAttribute(VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALUE;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(LONGVALUE, sText128);
		attributeMap.put(SHORTVALUE, sText32);
		attributeMap.put(VALUE, sAlphanum32);
	}

}
