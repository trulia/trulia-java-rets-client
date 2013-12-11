package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MSearchHelp extends MetaObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHHELPID = "SearchHelpID";
	public static final String VALUE = "Value";

	public MSearchHelp() {
		this(DEFAULT_PARSING);
	}

	public MSearchHelp(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSearchHelpID() {
		return getStringAttribute(SEARCHHELPID);
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
		return SEARCHHELPID;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(SEARCHHELPID, sAlphanum32);
		attributeMap.put(VALUE, sText1024);
	}

}
