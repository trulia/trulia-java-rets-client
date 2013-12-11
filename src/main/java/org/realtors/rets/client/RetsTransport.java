package org.realtors.rets.client;


import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.JDomStandardBuilder;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetadataBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the basic transport mechanism.  This class deals with the
 * very basic parts of sending the request, returning a response object,
 * and version negotiation.
 *
 */
public class RetsTransport {
	private static final String RETS_SESSION_ID_HEADER = "RETS-Session-ID"; // TODO spec says hyphen, Marketlinx uses an underscore

	private RetsHttpClient client;
	private CapabilityUrls capabilities;
	private String method = "GET";
	private RetsVersion version;
	private boolean strict;
	private NetworkEventMonitor monitor;
	
	private static final Log LOG = LogFactory.getLog(RetsTransport.class);

	private static Map MONITOR_MSGS = new HashMap(){{
		put(ChangePasswordRequest.class, "Transmitting change password request");
		put(GetObjectRequest.class, "Retrieving media object");
		put(LoginRequest.class, "Logging in");
		put(GetMetadataRequest.class, "Retrieving metadata");
		put(LogoutRequest.class, "Logging out");
		put(SearchRequest.class, "Executing search");
	}};


	/**
	 * Create a new transport instance.
	 * @param client An http client (make sure you call setUserCredentials
	 * on it before carrying out any transactions).
	 * @param capabilities the initial capabilities url list.  This can be
	 * replaced with a more up to date version at any time (for example,
	 * post-login()) with setCapabilities()
	 *
	 * @see RetsHttpClient#setUserCredentials
	 */
	public RetsTransport(RetsHttpClient client, CapabilityUrls capabilities) {
		this(client, capabilities, RetsVersion.DEFAULT, false);
	}

	/**
	 * Create a new transport instance to speak a specific RETS version.
	 * @param client an http client
	 * @param capabilities the initial capabilities url list
	 * @param version the RETS version to use during initial negotiation
	 * (RetsTransport will automatically switch to whatever version the
	 * server supports).
	 */
	public RetsTransport(RetsHttpClient client, CapabilityUrls capabilities, RetsVersion version, boolean strict) {
		this.client = client;
		this.capabilities = capabilities;
		this.doVersionHeader(version);
		this.strict = strict;
		this.client.addDefaultHeader("Accept", "*/*");
		this.monitor = new NullNetworkEventMonitor();
	}

	/**
	 * Query the current RetsVersion being used in this RetsTransport.
	 * 
	 * Initially, this will be the value with which this object was
	 * constructed.
	 *   
	 * However, this value may change after login.
	 * 
	 * @return the current RetsVersion value being used by the
	 * RetsTransport.
	 */
	public RetsVersion getRetsVersion() {
		return this.version;
	}

	public boolean isStrict() {
		return this.strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public void setMonitor(NetworkEventMonitor monitor) {
		if (monitor == null) {
			monitor = new NullNetworkEventMonitor();
		}
		this.monitor = monitor;
	}

	/**
	 * Set our RetsHttpClient up with the correct default RETS version to use,
	 * default to RETS 1.5.
	 * @param retsVersion 
	 */
	private void doVersionHeader(RetsVersion retsVersion) {
		if (this.client == null)
			return;
		if (retsVersion == null)
			retsVersion = RetsVersion.DEFAULT; 
		this.version = retsVersion;
		this.client.addDefaultHeader(RetsVersion.RETS_VERSION_HEADER, this.version.toString());
	}

	/**
	 * replace the capabilities url list with a new one
	 * @param capabilities the new capabilities url list
	 */
	public void setCapabilities(CapabilityUrls capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * switch to a specific HttpMethodName, POST/GET, where the 
	 * method is supported.  Where GET is not supported, POST 
	 * will be used.
	 * @param method the HttpMethodName to use
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/** 
	 * Available as an integration last resort
	 */
	public RetsHttpResponse doRequest(RetsHttpRequest req) throws RetsException {
		Object monitorobj = null;
		String msg = getMonitorMessage(req);
		monitorobj = this.monitor.eventStart(msg);

		req.setVersion(this.version);
		req.setUrl(this.capabilities);

		RetsHttpResponse httpResponse;
		try {
			httpResponse = this.client.doRequest(this.method, req);
		} finally {
			this.monitor.eventFinish(monitorobj);
		}
		return httpResponse;
	}

	private String getMonitorMessage(RetsHttpRequest req) {
		String msg = (String) MONITOR_MSGS.get(req.getClass());
		if (msg == null) {
			msg = "communicating with network";
		}
		return msg;
	}

	/**
	 * Logs into the server.  This transaction gets a list of capability URLs
	 * encapsulated in the LoginResponse that should typically be given back
	 * to the transport object with setCapabilities().  RETS Specification,
	 * section 4.
	 *
	 * @param req The login request
	 * @return the LoginResponse object
	 * @throws RetsException if the login failed or something went wrong on the
	 * network
	 * @see #setCapabilities
	 */
	public LoginResponse login(LoginRequest req) throws RetsException {
		RetsHttpResponse retsHttpResponse = this.doRequest(req);

		String versionHeader = retsHttpResponse.getHeader(RetsVersion.RETS_VERSION_HEADER);
		// may be null, which is fine, return null, dont throw 
		RetsVersion retsVersion = RetsVersion.getVersion(versionHeader);
		if( retsVersion == null && this.strict ) 
			throw new RetsException(String.format("RETS Version is a required response header, version '%s' is unrecognized",versionHeader));
		// skip updating the client version if its not set (correctly) by the server
		if( retsVersion != null ) this.doVersionHeader(retsVersion);

		LoginResponse response = new LoginResponse(this.capabilities.getLoginUrl());

		String sessionId = retsHttpResponse.getCookie(RETS_SESSION_ID_HEADER);
		response.setSessionId(sessionId);
		response.setStrict(this.strict);
		response.parse(retsHttpResponse.getInputStream(), this.version);
		return response;
	}

	/**
	 * Logs out of the server.  No other transactions should be called until
	 * another login() succeeds.  RETS Specification, Section 6.  Logout is
	 * an optional transaction.  This method returns null if the server does
	 * not support the Logout transaction.
	 *
	 * @return LogoutResponse or null if logout is not supported
	 * @throws RetsException if there is a network or remote server error
	 */
	public LogoutResponse logout() throws RetsException {
		if (this.capabilities.getLogoutUrl() == null) {
			return null;
		}
		RetsHttpRequest req = new LogoutRequest();
		RetsHttpResponse httpResponse = doRequest(req);
		LogoutResponse response = new LogoutResponse();
		response.setStrict(this.strict);
		try {
			response.parse(httpResponse.getInputStream(), this.version);
		} catch(RetsException e) {
			if (e.getMessage().contains("Invalid number of children")){// most RETS servers have issues logging out for some reason.
				LOG.warn("unsual response for logout request, but log out successful.");
			}
			
		} 
		return response;
	}

	/**
	 * Perform a non-streaming search and pass all results from the 
	 * SearchRequest to the given collector.
	 *
	 * 12/06/20 Added charset, needed for sax parser
	 * @param req the search request
	 * @param collector the result object that will store the data
	 */
	public void search(SearchRequest req, SearchResultCollector collector) throws RetsException {
		RetsHttpResponse httpResponse = doRequest(req);
		new SearchResultHandler(collector).parse(httpResponse.getInputStream(), httpResponse.getCharset());
	}

	/**
	 * Override processing of the search completely by providing a 
	 * SearchResultProcessor to process the results of the Search.
	 *
	 * @param req the search request
	 * @param processor the result object that will process the data
	 */
	public SearchResultSet search(SearchRequest req, SearchResultProcessor processor) throws RetsException {
		RetsHttpResponse httpResponse = doRequest(req);
		return processor.parse(httpResponse.getInputStream());
	}

	/**
	 *
	 * @param req GetObject request
	 * @return a GetObjectResponse
	 * @throws RetsException if the request is not valid or a network error
	 * occurs
	 */
	public GetObjectResponse getObject(GetObjectRequest req) throws RetsException {
		if (this.capabilities.getGetObjectUrl() == null) {
			throw new RetsException("Server does not support GetObject transaction.");
		}
		req.setUrl(this.capabilities);
		RetsHttpResponse httpResponse = this.client.doRequest(this.method, req);
		GetObjectResponse result = new GetObjectResponse(httpResponse.getHeaders(), httpResponse.getInputStream());
		return result;
	}

	public Metadata getMetadata(String location) throws RetsException {
		boolean compact = Boolean.getBoolean("rets-client.metadata.compact");
		GetMetadataRequest req = new GetMetadataRequest("SYSTEM", "*");
		if (compact) {
			req.setCompactFormat();
		}
		try {
			RetsHttpResponse httpResponse = doRequest(req);
			Object monitorobj = null;
			monitorobj = this.monitor.eventStart("Parsing metadata");
			try {
				SAXBuilder xmlBuilder = new SAXBuilder();
				Document xmlDocument = xmlBuilder.build(httpResponse.getInputStream());
				if (!location.equals("null")){
					 XMLOutputter outputter = new XMLOutputter();
					 FileWriter writer = new FileWriter(location);
					 outputter.output(xmlDocument, writer); 
					 outputter.outputString(xmlDocument);
				}
				MetadataBuilder metadataBuilder;
				if (req.isCompactFormat()) {
					metadataBuilder = new JDomCompactBuilder();
				} else {
					metadataBuilder = new JDomStandardBuilder();
				}
				metadataBuilder.setStrict(this.strict);
			
		        
		        
				return metadataBuilder.doBuild(xmlDocument);
			} finally {
				this.monitor.eventFinish(monitorobj);
			}
		} catch (Exception e) {
			throw new RetsException(e);
		}
	}

	public GetMetadataResponse getMetadata(GetMetadataRequest req) throws RetsException {
		RetsHttpResponse httpResponse = doRequest(req);
		Object monitorobj = null;
		monitorobj = this.monitor.eventStart("Parsing metadata");
		try {
			try {
				return new GetMetadataResponse(httpResponse.getInputStream(), req.isCompactFormat(),this.strict);
			} catch (InvalidReplyCodeException e) {
				e.setRequestInfo(req.toString());
				throw e;
			}
		} finally {
			this.monitor.eventFinish(monitorobj);
		}
	}

	public boolean changePassword(ChangePasswordRequest req) throws RetsException {
		RetsHttpResponse httpResponse = doRequest(req);
		ChangePasswordResponse response = new ChangePasswordResponse(httpResponse.getInputStream());
		// response will throw an exception if there is an error code
		return (response != null);
	}

}
