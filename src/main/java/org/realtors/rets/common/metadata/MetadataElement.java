package org.realtors.rets.common.metadata;

public enum MetadataElement {
	SYSTEM("System"),// might need to provide enumeration for different versions 1.5 vs 1.7
	RESOURCE("Resource"),
	FOREIGNKEY("ForeignKey"),
	CLASS("Class"),
	TABLE("Field"),
	UPDATE("UpdateType"),
	UPDATETYPE("UpdateField"),
	OBJECT("Object"),
	SEARCHHELP("SearchHelp"),
	EDITMASK("EditMask"),
	UPDATEHELP("UpdateHelp"),
	LOOKUP("Lookup"),
	LOOKUPTYPE("LookupType"),
	VALIDATIONLOOKUP("ValidationLookup"),
	VALIDATIONLOOKUPTYPE("ValidationLookupType"),
	VALIDATIONEXPRESSION("ValidationExpression"),
	VALIDATIONEXTERNAL("ValidationExternalType"),
	VALIDATIONEXTERNALTYPE("ValidationExternal");
	
	private final String elementName;
	
	MetadataElement(String elementName){
		this.elementName = elementName;
	}
	
	public String elementName(){ return  this.elementName;}

}
