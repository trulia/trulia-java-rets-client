package org.realtors.rets.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CapabilityUrls {
	public static final String ACTION_URL = "Action";
	public static final String CHANGE_PASSWORD_URL = "ChangePassword";
	public static final String GET_OBJECT_URL = "GetObject";
	public static final String LOGIN_URL = "Login";
	public static final String LOGIN_COMPLETE_URL = "LoginComplete";
	public static final String LOGOUT_URL = "Logout";
	public static final String SEARCH_URL = "Search";
	public static final String GET_METADATA_URL = "GetMetadata";
	public static final String UPDATE_URL = "Update";
	public static final String SERVER_INFO_URL = "ServerInformation";// for rets 1.7
	private static final Log LOG = LogFactory.getLog(CapabilityUrls.class);
	
	private final Map mCapabilityUrls;
	private URL mUrl;

	public CapabilityUrls() {
		this(null);
	}

	public CapabilityUrls(URL baseurl) {
		this.mUrl = baseurl;
		this.mCapabilityUrls = new HashMap();
	}

	public void setCapabilityUrl(String capability, String url) {
		if (this.mUrl != null) {
			try {
				String newurl = new URL(this.mUrl, url).toString();
				if (!newurl.equals(url)) {
					LOG.info("qualified " + capability + "  URL different: "
							+ url + " -> " + newurl);
					url = newurl;
				}

			} catch (MalformedURLException e) {
				LOG.warn("Couldn't normalize URL", e);
			}
		}
		this.mCapabilityUrls.put(capability, url);

	}

	public String getCapabilityUrl(String capability) {
		return (String) this.mCapabilityUrls.get(capability);
	}

	public void setActionUrl(String url) {
		setCapabilityUrl(ACTION_URL, url);
	}

	public String getActionUrl() {
		return getCapabilityUrl(ACTION_URL);
	}

	public void setChangePasswordUrl(String url) {
		setCapabilityUrl(CHANGE_PASSWORD_URL, url);
	}

	public String getChangePasswordUrl() {
		return getCapabilityUrl(CHANGE_PASSWORD_URL);
	}

	public void setGetObjectUrl(String url) {
		setCapabilityUrl(GET_OBJECT_URL, url);
	}

	public String getGetObjectUrl() {
		return getCapabilityUrl(GET_OBJECT_URL);
	}

	public void setLoginUrl(String url) {
		if (this.mUrl == null) {
			try {
				this.mUrl = new URL(url);
			} catch (MalformedURLException e) {
				LOG.debug("java.net.URL can't parse login url: " + url);
				this.mUrl = null;
			}
		}
		setCapabilityUrl(LOGIN_URL, url);
	}

	public String getLoginUrl() {
		return getCapabilityUrl(LOGIN_URL);
	}

	public void setLoginCompleteUrl(String url) {
		setCapabilityUrl(LOGIN_COMPLETE_URL, url);
	}

	public String getLoginCompleteUrl() {
		return getCapabilityUrl(LOGIN_COMPLETE_URL);
	}

	public void setLogoutUrl(String url) {
		setCapabilityUrl(LOGOUT_URL, url);
	}

	public String getLogoutUrl() {
		return getCapabilityUrl(LOGOUT_URL);
	}

	public void setSearchUrl(String url) {
		setCapabilityUrl(SEARCH_URL, url);
	}

	public String getSearchUrl() {
		return getCapabilityUrl(SEARCH_URL);
	}

	public void setGetMetadataUrl(String url) {
		setCapabilityUrl(GET_METADATA_URL, url);
	}

	public String getGetMetadataUrl() {
		return getCapabilityUrl(GET_METADATA_URL);
	}

	public void setUpdateUrl(String url) {
		setCapabilityUrl(UPDATE_URL, url);
	}

	public String getUpdateUrl() {
		return getCapabilityUrl(UPDATE_URL);
	}
	/**
	 * This is for RETS 1.7 and later and will return an empty string if it is not implemented.
	 * @param url
	 */
	public void setServerInfo(String url) {
		setCapabilityUrl(SERVER_INFO_URL, url);
	}
	/**
	 * This is for RETS 1.7 and later and will return an empty string if it is not implemented.
	 * @return
	 */
	public String getServerInfo() {
		return getCapabilityUrl(SERVER_INFO_URL);
	}

}
