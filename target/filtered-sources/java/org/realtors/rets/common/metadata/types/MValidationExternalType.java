package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternalType extends MetaObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHFIELD = "SearchField";
	public static final String DISPLAYFIELD = "DisplayField";
	public static final String RESULTFIELDS = "ResultFields";

	public MValidationExternalType() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternalType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSearchField() {
		return getStringAttribute(SEARCHFIELD);
	}

	public String getDisplayField() {
		return getStringAttribute(DISPLAYFIELD);
	}

	public String getResultFields() {
		return getStringAttribute(RESULTFIELDS);
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
		attributeMap.put(SEARCHFIELD, sPlaintext512);
		attributeMap.put(DISPLAYFIELD, sPlaintext512);
		attributeMap.put(RESULTFIELDS, sPlaintext1024);
	}

}
