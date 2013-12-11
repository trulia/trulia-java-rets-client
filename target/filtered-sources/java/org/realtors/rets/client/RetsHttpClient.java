package org.realtors.rets.client;


public abstract class RetsHttpClient {

	public static final String SESSION_ID_COOKIE = "RETS-Session-ID";
    public static final String LOGIN_SESSION_ID = "0";

    public abstract void setUserCredentials(String userName, String password);

	/**
	 * The protocol specific implementation happens here.
	 */
	public abstract RetsHttpResponse doRequest(String httpMethod, RetsHttpRequest request) throws RetsException;

	/**
	 * Add an HTTP header that should be included by default in all requests
	 * 
	 * @param name
	 *            header name, case should be preserved
	 * @param value
	 *            static header value, if <tt>null</tt> then implementation
	 *            should not include the header in requests
	 */
	public abstract void addDefaultHeader(String name, String value);

}
