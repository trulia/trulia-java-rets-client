package org.realtors.rets.client;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.realtors.rets.common.util.CaseInsensitiveTreeMap;

public class CommonsHttpClient extends RetsHttpClient {
	private static final int DEFAULT_TIMEOUT = 300000;
	private static final String RETS_VERSION = "RETS-Version";
	private static final String RETS_SESSION_ID = "RETS-Session-ID";
	private static final String RETS_REQUEST_ID = "RETS-Request-ID";
	private static final String USER_AGENT = "User-Agent";
	private static final String RETS_UA_AUTH_HEADER = "RETS-UA-Authorization";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String DEFLATE_ENCODINGS = "gzip,deflate";
	public static final String CONTENT_TYPE = "Content-Type";

	public static BasicHttpParams defaultParams(int timeout) {
		BasicHttpParams httpClientParams = new BasicHttpParams();
        // connection to server timeouts
        HttpConnectionParams.setConnectionTimeout(httpClientParams, timeout);
        HttpConnectionParams.setSoTimeout(httpClientParams, timeout);
        // set to rfc 2109 as it puts the ASP (IIS) cookie _FIRST_, this is critical for interealty
        httpClientParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        return httpClientParams;
	}
	public static ThreadSafeClientConnManager defaultConnectionManager(int maxConnectionsPerRoute, int maxConnectionsTotal) {
		// allows for multi threaded requests from a single client
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connectionManager.setMaxTotal(maxConnectionsTotal);
        return connectionManager;
	}

	private final ConcurrentHashMap<String, String> defaultHeaders;
	private final DefaultHttpClient httpClient;
	
	// method choice improvement
	private final String userAgentPassword;

	public CommonsHttpClient() {
		this(new DefaultHttpClient(defaultConnectionManager(Integer.MAX_VALUE, Integer.MAX_VALUE), defaultParams(DEFAULT_TIMEOUT)), null, true);
	}
	
	public CommonsHttpClient(int timeout, String userAgentPassword, boolean gzip) {
		this(new DefaultHttpClient(defaultConnectionManager(Integer.MAX_VALUE, Integer.MAX_VALUE), defaultParams(timeout)), userAgentPassword, gzip);
	}
	
	public CommonsHttpClient(DefaultHttpClient client, String userAgentPassword, boolean gzip) {
		this.defaultHeaders = new ConcurrentHashMap<String, String>();
		this.userAgentPassword = userAgentPassword;

        this.httpClient = client;
		// ask the server if we can use gzip
		if( gzip ) this.addDefaultHeader(ACCEPT_ENCODING, DEFLATE_ENCODINGS);
	}
	
	public DefaultHttpClient getHttpClient(){
		return this.httpClient;
	}

	//----------------------method implementations
 	@Override
	public void setUserCredentials(String userName, String password) {
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
		this.httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
	}
	@Override
	public RetsHttpResponse doRequest(String httpMethod, RetsHttpRequest request) throws RetsException {
		return "GET".equals(StringUtils.upperCase(httpMethod)) ? this.doGet(request) : this.doPost(request);
	}

	//----------------------method implementations
	public RetsHttpResponse doGet(RetsHttpRequest request) throws RetsException {
		String url = request.getUrl();
		String args = request.getHttpParameters();
		if (args != null) {
			url = url + "?" + args;
		}
		HttpGet method = new HttpGet(url);
		return execute(method, request.getHeaders());
	}

	public RetsHttpResponse doPost(RetsHttpRequest request) throws RetsException {
		String url = request.getUrl();
		String body = request.getHttpParameters();
		if (body == null) body = "";  // commons-httpclient 3.0 refuses to accept null entity (body)
		HttpPost method = new HttpPost(url);
		try {
			method.setEntity(new StringEntity(body, null, null));
		} catch (UnsupportedEncodingException e) {
			throw new RetsException(e);
		}
		//updated Content-Type, application/x-www-url-encoded no longer supported
		method.setHeader("Content-Type", "application/x-www-form-urlencoded");
		return execute(method, request.getHeaders());
	}

	protected RetsHttpResponse execute(final HttpRequestBase method, Map<String,String> headers) throws RetsException {
		try {
			// add the default headers
			if (this.defaultHeaders != null) {
				for (Map.Entry<String,String> entry : this.defaultHeaders.entrySet()) {
					method.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// add our request headers from rets
			if (headers != null) {
				for (Map.Entry<String,String> entry : headers.entrySet()) {
					method.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// optional ua-auth stuff here
			if( this.userAgentPassword != null ){
			    method.setHeader(RETS_UA_AUTH_HEADER, calculateUaAuthHeader(method,getCookies()));
			}
			// try to execute the request
			HttpResponse response = this.httpClient.execute(method);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new InvalidHttpStatusException(status);
			}
			return new CommonsHttpClientResponse(response, getCookies());
		} catch (Exception e) {
			throw new RetsException(e);
		}
	}

	@Override
	public synchronized void addDefaultHeader(String key, String value) {
		this.defaultHeaders.put(key, value);
		if( value == null ) this.defaultHeaders.remove(key);
	}
	
	protected Map<String,String> getCookies() {
		Map<String,String> cookieMap = new CaseInsensitiveTreeMap();
		for (Cookie cookie : this.httpClient.getCookieStore().getCookies()) {
			cookieMap.put(cookie.getName(), cookie.getValue());
		}
		return cookieMap;
	}

	protected String calculateUaAuthHeader(HttpRequestBase method, Map<String, String> cookies ) {
		final String userAgent = this.getHeaderValue(method, USER_AGENT);
		final String requestId = this.getHeaderValue(method, RETS_REQUEST_ID);
		final String sessionId = cookies.get(RETS_SESSION_ID);
		final String retsVersion = this.getHeaderValue(method, RETS_VERSION);
		String secretHash = DigestUtils.md5Hex(String.format("%s:%s",userAgent,this.userAgentPassword));
    	String pieces = String.format("%s:%s:%s:%s",secretHash,StringUtils.trimToEmpty(requestId),StringUtils.trimToEmpty(sessionId),retsVersion);
		return String.format("Digest %s", DigestUtils.md5Hex(pieces));
	}
	
	protected String getHeaderValue(HttpRequestBase method, String key){
    	Header requestHeader = method.getFirstHeader(key);
    	if( requestHeader == null ) return null;
		return requestHeader.getValue();
    }
}
