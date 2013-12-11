package org.realtors.rets.client;

/**
 * 
 * The search request sent from search() in RetsSession
 *
 */

public class SearchRequest extends RetsHttpRequest {
	
	public static final int COUNT_NONE = 1;
	public static final int COUNT_FIRST = 2;
	public static final int COUNT_ONLY = 3;
	public static final String FORMAT_STANDARD_XML = "STANDARD-XML";
	public static final String FORMAT_COMPACT = "COMPACT";
	public static final String FORMAT_COMPACT_DECODED = "COMPACT-DECODED";
	public static final String RETS_DMQL1 = "DMQL";
	public static final String RETS_DMQL2 = "DMQL2";
	public static final String KEY_TYPE = "SearchType";
	public static final String KEY_CLASS = "Class";
	public static final String KEY_DMQLVERSION = "QueryType";
	public static final String KEY_QUERY = "Query";
	public static final String KEY_COUNT = "Count";
	public static final String KEY_FORMAT = "Format";
	public static final String KEY_LIMIT = "Limit";
	public static final String KEY_OFFSET = "Offset";
	public static final String KEY_SELECT = "Select";
	public static final String KEY_RESTRICTEDINDICATOR = "RestrictedIndicator";
	public static final String KEY_STANDARDNAMES = "StandardNames";


	private String type;

	public SearchRequest(String stype, String sclass, String query) {
		setQueryParameter(KEY_TYPE, stype);
		this.type = stype;
		setQueryParameter(KEY_CLASS, sclass);
		setQueryParameter(KEY_QUERY, query);
		setQueryParameter(KEY_FORMAT, FORMAT_COMPACT);
		setQueryParameter(KEY_DMQLVERSION, RETS_DMQL2);
	}
	

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getSearchUrl());
	}

	public String getType() {
		return this.type;
	}

	public void setCountNone() {
		setQueryParameter(KEY_COUNT, null);
	}

	public void setCountFirst() {
		setQueryParameter(KEY_COUNT, "1");
	}

	public void setCountOnly() {
		setQueryParameter(KEY_COUNT, "2");
	}

	public void setFormatCompact() {
		setQueryParameter(KEY_FORMAT, FORMAT_COMPACT);
	}

	public void setFormatCompactDecoded() {
		setQueryParameter(KEY_FORMAT, FORMAT_COMPACT_DECODED);
	}

	public void setFormatStandardXml() {
		setQueryParameter(KEY_FORMAT, FORMAT_STANDARD_XML);
	}

	public void setFormatStandardXml(String dtdVersion) {
		setQueryParameter(KEY_FORMAT, FORMAT_STANDARD_XML + ":" + dtdVersion);
	}

	public void setLimit(int count) {
		setQueryParameter(KEY_LIMIT, Integer.toString(count));
	}

	public void setLimitNone() {
		setQueryParameter(KEY_LIMIT, null);
	}

	public void setSelect(String sel) {
		setQueryParameter(KEY_SELECT, sel);
	}

	public void setRestrictedIndicator(String rest) {
		setQueryParameter(KEY_RESTRICTEDINDICATOR, rest);
	}

	public void setStandardNames() {
		setQueryParameter(KEY_STANDARDNAMES, "1");
	}

	public void setSystemNames() {
		setQueryParameter(KEY_STANDARDNAMES, null);
	}

	public void setOffset(int offset) {
		setQueryParameter(KEY_OFFSET, Integer.toString(offset));
	}

	public void setOffsetNone() {
		setQueryParameter(KEY_OFFSET, null);
	}

	/** TODO should the search automatically handle this???  shouldn't this be setable by vendor is that predicatable? */
	@Override
	public void setVersion(RetsVersion ver) {
		if (RetsVersion.RETS_10.equals(ver)) {
			setQueryParameter(KEY_DMQLVERSION, RETS_DMQL1);
		} else {
			setQueryParameter(KEY_DMQLVERSION, RETS_DMQL2);
		}
	}
}
