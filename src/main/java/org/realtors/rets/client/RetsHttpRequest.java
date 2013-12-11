package org.realtors.rets.client;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.realtors.rets.common.util.CaseInsensitiveTreeMap;

/** Base Http Request object */
public abstract class RetsHttpRequest implements Serializable {
	private final Map<String,String> mHeaders;
	private final SortedMap<String,String> mQueryParameters;
	protected String mUrl;

	public RetsHttpRequest() {
		this.mHeaders = new CaseInsensitiveTreeMap<String,String>();
		this.mQueryParameters = new TreeMap<String,String>();
	}

	public abstract void setUrl(CapabilityUrls urls);

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getUrl() {
		return this.mUrl;
	}

	public void setHeader(String key, String value) {
		this.mHeaders.put(key, value);
	}

	public Map<String,String> getHeaders() {
		return this.mHeaders;
	}

	public String getHttpParameters() {
		if (this.mQueryParameters.isEmpty()) 
			return null;

		List<String> params = new LinkedList<String>();
		for(Map.Entry<String,String> param : this.mQueryParameters.entrySet()){
			params.add(String.format("%s=%s",RetsUtil.urlEncode(param.getKey()),RetsUtil.urlEncode(param.getValue())));
		}
		return StringUtils.join(params.iterator(),"&");
	}

	protected void setQueryParameter(String name, String value) {
		if (value == null) {
			this.mQueryParameters.remove(name);
		} else {
			this.mQueryParameters.put(name, value);
		}
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		Iterator iterator = this.mQueryParameters.keySet().iterator();
		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			builder.append(s, this.mQueryParameters.get(s));
		}
		return builder.toString();
	}

	/**
	 * any request with version-specific handling should deal with this.
	 *
	 * @param version
	 */
	public abstract void setVersion(RetsVersion version);

}