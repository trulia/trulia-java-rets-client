package org.realtors.rets.client;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetadataException;

/**
 * RetsSession is the core class of the rets.client package.
 */
public class RetsSession {
	public static final String METADATA_TABLES = "metadata_tables.xml";
	public static final String RETS_CLIENT_VERSION = "1.5";//change default version

	private static final Log LOG = LogFactory.getLog(RetsSession.class);
	private static String sUserAgent = "crt-rets-client/" + RETS_CLIENT_VERSION;

	private CapabilityUrls capabilityUrls;
	private RetsHttpClient httpClient;
	private RetsTransport transport;
	private String sessionId;


	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 * 
	 * Uses a  default implementation of RetsHttpClient based on
	 * apache commons http client.
	 *
	 * Uses the RetsVersion.RETS_DEFAULT as the RetsVersion for
	 * this session.
	 * 
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl URL of the Login transaction.
	 */
	public RetsSession(String loginUrl) {
		this(loginUrl, new CommonsHttpClient());
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 * 
	 * Uses the RetsVersion.RETS_DEFAULT as the RetsVersion for
	 * this session.
	 * 
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl URL of the Login transaction
	 * @param httpClient a RetsHttpClient implementation.  The default
	 *   is CommonsHttpClient.
	 */
	public RetsSession(String loginUrl, RetsHttpClient httpClient) {
		this(loginUrl, httpClient, RetsVersion.DEFAULT);
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 *
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl URL of the Login transaction
	 * @param httpClient a RetsHttpClient implementation.  The default
	 *   is CommonsHttpClient.
	 * @param retsVersion The RetsVersion used by this RetsSession.
	 */
	public RetsSession(String loginUrl, RetsHttpClient httpClient, RetsVersion retsVersion) {
		this(loginUrl, httpClient, retsVersion, sUserAgent,false);
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 *
	 * @param loginUrl URL of the Login transaction
	 * @param httpClient a RetsHttpClient implementation.  The default
	 *   is CommonsHttpClient.
	 * @param retsVersion The RetsVersion used by this RetsSession.
	 * @param userAgent specific User-Agent to use for this session.
	 */
	public RetsSession(String loginUrl, RetsHttpClient httpClient, RetsVersion retsVersion, String userAgent, boolean strict) {
		this.capabilityUrls = new CapabilityUrls();
		this.capabilityUrls.setLoginUrl(loginUrl);

		this.httpClient = httpClient;
		this.transport = new RetsTransport(httpClient, this.capabilityUrls, retsVersion, strict);
		this.httpClient.addDefaultHeader("User-Agent", userAgent);
	}

	/**
	 * Query the current RetsVersion being used in this session.
	 * 
	 * Initially, this will be the value passed to the RetsTransport.  
	 * However, if during auto-negotiation the RetsTransport changes 
	 * the RetsSession, this value may change throughout the session.
	 * 
	 * @return the current RetsVersion value being used by the
	 * RetsTransport.
	 */
	public RetsVersion getRetsVersion() {
		return this.transport.getRetsVersion();
	}

	/**
	 * Get the current RETS Session ID
	 * 
	 * @return the current RETS Session ID or null is the server has
	 * not specified one
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		LOG.debug("setting Session-ID to: " + sessionId);
		this.sessionId = sessionId;
	}

	public void setMonitor(NetworkEventMonitor monitor) {
		this.transport.setMonitor(monitor);
	}

	public void setStrict(boolean strict) {
		this.transport.setStrict(strict);
	}
	public boolean isStrict() {
		return this.transport.isStrict();
	}

	/**
	 * Sets the default User-Agent value for RetsSessions created without
	 * a specified User-Agent value.
	 *  
	 * @param userAgent Default User-Agent value to use for all RetsSession
	 * objects created in the future.
	 */
	public static void setUserAgent(String userAgent) {
		sUserAgent = userAgent;
	}

	public String getLoginUrl() {
		return this.capabilityUrls.getLoginUrl();
	}

	public Metadata getIncrementalMetadata() throws RetsException {
		try {
			return new Metadata(new MetaCollectorImpl(this.transport));
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	/**
	 * Get the complete RETS metadata.
	 * 
	 * @return The RETS metadata object for these credentials.
	 * 
	 * @throws RetsException
	 */
	public Metadata getMetadata() throws RetsException {
		return this.transport.getMetadata("null");
	}
	/**
	 * Ability to download the raw metadata to a location
	 * @param location
	 * @return
	 * @throws RetsException
	 */
	public Metadata getMetadata(String location) throws RetsException {
		return this.transport.getMetadata(location);
	}

	/**
	 * Perform a low level GetMetadatRequest.  To retrieve
	 * structured metadata, 
	 * 
	 * @see #getMetadata()
	 * 
	 * @param req GetMetadataRequest
	 * @return GetMetadataResponse, containing all MetaObjects
	 * returned
	 * 
	 * @throws RetsException if an error occurs
	 */
	public GetMetadataResponse getMetadata(GetMetadataRequest req) throws RetsException {
		return this.transport.getMetadata(req);
	}

	/**
	 * Fetches the action (MOTD) from the server.
	 *
	 * @exception RetsException if an error occurs
	 */
	private void getAction() throws RetsException {
		String actionUrl = this.capabilityUrls.getActionUrl();
		if (actionUrl == null) {
			LOG.warn("No Action-URL available, skipping");
			return;
		}
		GenericHttpRequest actionRequest = new GenericHttpRequest(actionUrl){
			@Override
			public Map<String, String> getHeaders() {
				return null;
			}
		};
		RetsHttpResponse httpResponse = this.httpClient.doRequest("GET", actionRequest);
		try {
			httpResponse.getInputStream().close();
		} catch (Exception e) {
			LOG.error("Action URL weirdness", e);
		}
	}

	/**
	 * Implementation that allow for single or multi-part 
	 * GetObject requests.
	 *
	 * @param req
	 * @return
	 * @exception RetsException if an error occurs
	 */
	public GetObjectResponse getObject(GetObjectRequest req) throws RetsException {
		return this.transport.getObject(req);
	}

	/**
	 *
	 * @param resource
	 * @param type
	 * @param entity
	 * @param id
	 * @return response
	 * @exception RetsException if an error occurs
	 */
	public GetObjectResponse getObject(String resource, String type, String entity, String id) throws RetsException {
		GetObjectRequest req = new GetObjectRequest(resource, type);
		req.addObject(entity, id);
		return getObject(req);
	}

	/**
	 * Log into the RETS server (see RETS 1.5, section 4).  No other
	 * transactions will work until you have logged in.
	 *
	 * @param userName Username to authenticate
	 * @param password Password to authenticate with
	 * @return LoginResponse if success.
	 * @exception RetsException if authentication was denied
	 */
	public LoginResponse login(String userName, String password) throws RetsException {
		return login(userName, password, null, null);
	}

	/**
	 * Log into the RETS server (see RETS 1.5, section 4).  No other
	 * transactions will work until you have logged in.
	 *
	 * @param userName username to authenticate
	 * @param password password to authenticate with
	 * @param brokerCode broker code if the same user belongs to multiple
	 * brokerages.  May be null.
	 * @param brokerBranch branch code if the same user belongs to multiple
	 * branches.  May be null.  brokerCode is required if you want
	 * brokerBranch to work.
	 * @return LoginResponse if success.
	 * @exception RetsException if authentication was denied
	 */

	public LoginResponse login(String userName, String password, String brokerCode, String brokerBranch) throws RetsException {
		this.httpClient.setUserCredentials(userName, password);

		LoginRequest request = new LoginRequest();
		request.setBrokerCode(brokerCode, brokerBranch);

		LoginResponse response = this.transport.login(request);
		this.capabilityUrls = response.getCapabilityUrls();
		this.transport.setCapabilities(this.capabilityUrls);
		this.setSessionId(response.getSessionId());
		this.getAction();

		return response;
	}

	/**
	 * Log out of the current session.  Another login _may_ re-establish a new connection
	 * depending the the behavior of the {#link RetsHttpClient} and its' ability to 
	 * maintain and restablish a connection.  
	 * 
	 * @return a LogoutResponse
	 * @throws RetsException if the logout transaction failed
	 */
	public LogoutResponse logout() throws RetsException {
		try {
			return this.transport.logout();
		} finally {
			this.setSessionId(null);
		}
	}

	/**
	 * Will perform a search as requested and return a filled
	 * SearchResult object.  This method caches all result information
	 * in memory in the SearchResult object.
	 *
	 * @param req Contains parameters on which to search.
	 * @return a completed SearchResult
	 * @exception RetsException if an error occurs
	 */
	public SearchResult search(SearchRequest req) throws RetsException {
		SearchResultImpl res = new SearchResultImpl();
		search(req, res);
		return res;
	}

	/**
	 * Execute a RETS Search.  The collector object will be filled 
	 * when this method is returned.  See RETS 1.52d, Section 5.
	 *
	 * @param req Contains parameters on which to search.
	 * @param collector SearchResult object which will be informed of the results
	 * as they come in.  If you don't need live results, see the other
	 * search invocation.
	 * @exception RetsException if an error occurs
	 */
	public void search(SearchRequest req, SearchResultCollector collector) throws RetsException {
		this.transport.search(req, collector);
	}

	/**
	 * Search and process the Search using a given SearchResultProcessor.
	 *
	 * @param req the search request
	 * @param processor the result object that will process the data
	 */
	public SearchResultSet search(SearchRequest req, SearchResultProcessor processor) throws RetsException {
		return this.transport.search(req, processor);
	}

	/** 
	 * The lowest level integration.  This method is not recommened for general use.   
	 */
	public RetsHttpResponse request(RetsHttpRequest request) throws RetsException{
		return this.transport.doRequest(request);
	}

	/**
	 * switch to a specific HttpMethodName, POST/GET, where the 
	 * method is supported.  Where GET is not supported, POST 
	 * will be used.
	 * @param method the HttpMethodName to use
	 */
	public void setMethod(String method) {
		this.transport.setMethod(method);
	}

	/** Make sure GC'd sessions are logged out. */
	@Override
	protected void finalize() throws Throwable {
		try {
			if( this.sessionId != null ) this.logout();
		} finally {
			super.finalize();
		}
	}
	/**
	 * Performs a search returning only the number of records resulting from a query.
	 * 
	 * Convenience method to get number records from a query
	 * 
	 * @param req the search request
	 * @return the number of records that returned from the search request
	 * @throws RetsException
	 */
	public int getQueryCount(SearchRequest req) throws RetsException {
		req.setCountOnly();
		SearchResult res = this.search(req);
		return res.getCount();
	}

	/**
	 * Gives the URL's of an Object request instead of object themselves
	 *
	 * Convenience method to get the URL's of the requeseted object only
	 * 
	 * @param req
	 * @return
	 * @throws RetsException
	 */
	public GetObjectResponse getObjectUrl(GetObjectRequest req) throws RetsException {
		req.setLocationOnly(true);
		GetObjectResponse res = this.getObject(req);
		return res;
	}
}
