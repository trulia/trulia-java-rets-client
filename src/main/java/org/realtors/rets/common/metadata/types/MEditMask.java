package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MEditMask extends MetaObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String EDITMASKID = "EditMaskID";
	public static final String VALUE = "Value";

	public MEditMask() {
		this(DEFAULT_PARSING);
	}

	public MEditMask(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
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
		return EDITMASKID;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(EDITMASKID, sAlphanum32);
		attributeMap.put(VALUE, sText256);
	}

}
