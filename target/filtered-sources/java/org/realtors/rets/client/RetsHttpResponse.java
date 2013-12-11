package org.realtors.rets.client;

import java.util.Map;
import java.io.InputStream;

/**
 * Interface for retrieving useful header fields from a RETS HTTP response
 * 
 *
 */

public interface RetsHttpResponse {
	public int getResponseCode() throws RetsException;

	public Map getHeaders() throws RetsException;

	public String getHeader(String hdr) throws RetsException;

	public String getCookie(String cookie) throws RetsException;

	public String getCharset() throws RetsException;
	
	public InputStream getInputStream() throws RetsException;

	public Map getCookies() throws RetsException;

}