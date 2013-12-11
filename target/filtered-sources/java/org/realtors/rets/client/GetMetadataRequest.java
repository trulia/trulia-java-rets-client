package org.realtors.rets.client;

import org.apache.commons.lang.StringUtils;

public class GetMetadataRequest extends VersionInsensitiveRequest {
	private static final int COMPACT_FORMAT = 0;
	private static final int STANDARD_XML_FORMAT = 1;
	public static final String KEY_TYPE = "Type";
	public static final String KEY_ID = "ID";
	public static final String KEY_FORMAT = "Format";
	public static final String FORMAT_STANDARD = "STANDARD-XML";
	public static final String FORMAT_STANDARD_PREFIX = "STANDARD-XML:";
	public static final String FORMAT_COMPACT = "COMPACT";

	private int format;
	private String standardXmlVersion;

	public GetMetadataRequest(String type, String id) throws RetsException {
		this(type, new String[] { id });
	}

	public GetMetadataRequest(String type, String[] ids) throws RetsException {
		assertValidIds(ids);
		type = "METADATA-" + type;
		if (type.equals("METADATA-SYSTEM") || type.equals("METADATA-RESOURCE")) {
			assertIdZeroOrStar(ids);
		}

		setQueryParameter(KEY_TYPE, type);
		setQueryParameter(KEY_ID, StringUtils.join(ids, ":"));
		setQueryParameter(KEY_FORMAT, FORMAT_STANDARD);
		this.format = STANDARD_XML_FORMAT;
	}

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getGetMetadataUrl());
	}

	private void assertValidIds(String[] ids) throws InvalidArgumentException {
		if (ids.length == 0) {
			throw new InvalidArgumentException("Expecting at least one ID");
		}
	}

	private void assertIdZeroOrStar(String[] ids) throws InvalidArgumentException {
		if (ids.length != 1) {
			throw new InvalidArgumentException("Expecting 1 ID, but found, " + ids.length);
		}
		if (!ids[0].equals("0") && !ids[0].equals("*")) {
			throw new InvalidArgumentException("Expecting ID of 0 or *, but was " + ids[0]);
		}
	}

	public void setCompactFormat() {
		setQueryParameter(KEY_FORMAT, FORMAT_COMPACT);
		this.format = COMPACT_FORMAT;
		this.standardXmlVersion = null;
	}

	public boolean isCompactFormat() {
		return (this.format == COMPACT_FORMAT);
	}

	public boolean isStandardXmlFormat() {
		return (this.format == STANDARD_XML_FORMAT);
	}

	public String getStandardXmlVersion() {
		return this.standardXmlVersion;
	}

}
