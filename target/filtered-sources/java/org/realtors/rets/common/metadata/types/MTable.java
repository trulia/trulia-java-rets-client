package org.realtors.rets.common.metadata.types;

import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.attrib.AttrEnum;

public class MTable extends MetaObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SYSTEMNAME = "SystemName";
	public static final String STANDARDNAME = "StandardName";
	public static final String LONGNAME = "LongName";
	public static final String DBNAME = "DBName";
	public static final String SHORTNAME = "ShortName";
	public static final String MAXIMUMLENGTH = "MaximumLength";
	public static final String DATATYPE = "DataType";
	public static final String PRECISION = "Precision";
	public static final String SEARCHABLE = "Searchable";
	public static final String INTERPRETATION = "Interpretation";
	public static final String ALIGNMENT = "Alignment";
	public static final String USESEPARATOR = "UseSeparator";
	public static final String EDITMASKID = "EditMaskID";
	public static final String LOOKUPNAME = "LookupName";
	public static final String MAXSELECT = "MaxSelect";
	public static final String UNITS = "Units";
	public static final String INDEX = "Index";
	public static final String MINIMUM = "Minimum";
	public static final String MAXIMUM = "Maximum";
	public static final String DEFAULT = "Default";
	public static final String REQUIRED = "Required";
	public static final String SEARCHHELPID = "SearchHelpID";
	public static final String UNIQUE = "Unique";
	public static final String MODTIMESTAMP = "ModTimeStamp";
	public static final String MODTIMESTAMPNAME = "ModTimeStampName";
	public static final String FOREIGNKEYNAME = "ForeignKeyName";
	public static final String FOREIGNFIELD = "ForeignField";
	public static final String INKEYINDEX = "InKeyIndex";
	public static final String KEYQUERY = "KeyQuery";
	public static final String KEYSELECT = "KeySelect";
	
	private static final String[] DATATYPES = "Boolean,Character,Date,DateTime,Time,Tiny,Small,Int,Long,Decimal".split(",");
	private static final AttrType sDataTypes = new AttrEnum(DATATYPES);
	private static final String[] INTERPRETATIONS = "Number,Currency,Lookup,LookupMulti,LookupBitstring,LookupBitmask".split(",");
	private static final AttrType sInterpretations = new AttrEnum(INTERPRETATIONS);
	private static final String[] ALIGNMENTS = "Left,Right,Center,Justify".split(",");
	private static final AttrType sAlignments = new AttrEnum(ALIGNMENTS);
	private static final String[] UNITSS = "Feet,Meters,SqFt,SqMeters,Acres,Hectares".split(",");
	private static final AttrType sUnits = new AttrEnum(UNITSS);

	public MTable() {
		this(DEFAULT_PARSING);
	}

	public MTable(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		
		String metadataEntryID = getStringAttribute(METADATAENTRYID);
		if (metadataEntryID == null){
			metadataEntryID = this.getSystemName();
		}
		return metadataEntryID;
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getLongName() {
		return getStringAttribute(LONGNAME);
	}

	public String getDBName() {
		return getStringAttribute(DBNAME);
	}

	public String getShortName() {
		return getStringAttribute(SHORTNAME);
	}

	public int getMaximumLength() {
		return getIntAttribute(MAXIMUMLENGTH);
	}

	public String getDataType() {
		return getStringAttribute(DATATYPE);
	}

	public int getPrecision() {
		return getIntAttribute(PRECISION);
	}

	public boolean getSearchable() {
		return getBooleanAttribute(SEARCHABLE);
	}

	public String getInterpretation() {
		return getStringAttribute(INTERPRETATION);
	}

	public boolean isLookup() {
		String interp = getInterpretation();
		if (interp != null && interp.startsWith("Lookup")) {
			return true;
		}
		if (getSystemName().equalsIgnoreCase("status")) {
			System.out.println("Field is " + getSystemName() + " and interp " + "is " + interp
					+ " but isLookup() is false");
		}
		return false;
	}

	public String getAlignment() {
		return getStringAttribute(ALIGNMENT);
	}

	public boolean getUseSeparator() {
		return getBooleanAttribute(USESEPARATOR);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
	}

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public int getMaxSelect() {
		return getIntAttribute(MAXSELECT);
	}

	public String getUnits() {
		return getStringAttribute(UNITS);
	}

	public int getIndex() {
		return getIntAttribute(INDEX);
	}

	public int getMinimum() {
		return getIntAttribute(MINIMUM);
	}

	public int getMaximum() {
		return getIntAttribute(MAXIMUM);
	}

	public int getDefault() {
		return getIntAttribute(DEFAULT);
	}

	public int getRequired() {
		return getIntAttribute(REQUIRED);
	}

	public String getSearchHelpID() {
		return getStringAttribute(SEARCHHELPID);
	}

	public boolean getUnique() {
		return getBooleanAttribute(UNIQUE);
	}

	public boolean getModTimestamp() {
		return getBooleanAttribute(MODTIMESTAMP);
	}
	
	public boolean getModTimestampName() {
		return getBooleanAttribute(MODTIMESTAMPNAME);
	}

	public boolean getInKeyIndex() {
		return getBooleanAttribute(INKEYINDEX);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return SYSTEMNAME;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, retsid);
		attributeMap.put(SYSTEMNAME, retsname);
		attributeMap.put(STANDARDNAME, retsname);
		attributeMap.put(LONGNAME, sText256);
		attributeMap.put(DBNAME, sAlphanum10);
		attributeMap.put(SHORTNAME, sText64);
		attributeMap.put(MAXIMUMLENGTH, sAttrNumericPositive);
		attributeMap.put(DATATYPE, sDataTypes);
		attributeMap.put(PRECISION, sAttrNumeric);
		attributeMap.put(SEARCHABLE, sAttrBoolean);
		attributeMap.put(INTERPRETATION, sInterpretations);
		attributeMap.put(ALIGNMENT, sAlignments);
		attributeMap.put(USESEPARATOR, sAttrBoolean);
		// XXX: but multiples are separated by commas
		attributeMap.put(EDITMASKID, retsname); 
		attributeMap.put(LOOKUPNAME, retsname);
		attributeMap.put(MAXSELECT, sAttrNumeric);
		attributeMap.put(UNITS, sUnits);
		attributeMap.put(INDEX, sAttrNumeric);
		attributeMap.put(MINIMUM, sAttrNumeric);
		attributeMap.put(MAXIMUM, sAttrNumeric);
		// XXX: serial
		attributeMap.put(DEFAULT, sAttrNumeric); 
		attributeMap.put(REQUIRED, sAttrNumeric);
		attributeMap.put(SEARCHHELPID, retsname);
		attributeMap.put(UNIQUE, sAttrBoolean);
		attributeMap.put(MODTIMESTAMP, sAttrBoolean);
		attributeMap.put(MODTIMESTAMPNAME, retsname);
		attributeMap.put(FOREIGNKEYNAME,retsid);
		attributeMap.put(FOREIGNFIELD,retsname);
		attributeMap.put(INKEYINDEX, sAttrBoolean);
		
		attributeMap.put(KEYQUERY, sAttrBoolean);
		attributeMap.put(KEYSELECT, sAttrBoolean);
	}
	
	public String getForeignKeyName() {
		return getStringAttribute(FOREIGNKEYNAME);
	}
	
	public String getForeignField() {
		return getStringAttribute(FOREIGNFIELD);
	}

}
