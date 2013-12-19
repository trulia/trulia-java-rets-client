package org.realtors.rets.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.util.CaseInsensitiveTreeMap;

public class GetObjectResponse{
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	private final static GetObjectIterator<SingleObjectResponse> EMPTY_OBJECT_RESPONSE_ITERATOR = new GetObjectIterator<SingleObjectResponse>() {
		public boolean hasNext() {
			return false;
		}
		public SingleObjectResponse next() {
			throw new NoSuchElementException();
		}
		public void close() {
			/* no op */
		}
		public void remove() {
			/* no op */
		}
	};
    private final Map headers;
    private final InputStream inputStream;
	private final boolean isMultipart;
	/** Indicate whether the response was empty */
	private boolean emptyResponse;
	/** Indicate whether this GetObjectResponse is exhausted, i.e. has no objects */
	private boolean exhausted;

	public GetObjectResponse(Map headers, InputStream in) throws RetsException {
		this.emptyResponse = false;
		this.exhausted = false;
		this.headers = new CaseInsensitiveTreeMap(headers);
	    this.isMultipart = getType().contains("multipart");
	    this.inputStream = in;
	    
	    boolean isXml = getType().equals("text/xml");
		boolean containsContentId = headers.containsKey(SingleObjectResponse.CONTENT_ID);
		// non multipart request that returns text/xml and does NOT have a Context-ID header, must only be a non-zero response code
		boolean nonMultiPart_xml_withoutContentId = !this.isMultipart && isXml && !containsContentId;
		// multipart request that returns text/xml can only be a non-zero response code
		boolean multiPart_xml = this.isMultipart && isXml;

		if ( multiPart_xml || nonMultiPart_xml_withoutContentId ) {
	    	int replyCode = 0;
	        try {
	        	// GetObjectResponse is empty, because we have a Rets ReplyCode
	        	this.emptyResponse = true;
	            SAXBuilder builder = new SAXBuilder();
	            Document mDocument = builder.build(in);
	            Element root = mDocument.getRootElement();
	            if (root.getName().equals("RETS")) {
	                replyCode = NumberUtils.toInt(root.getAttributeValue("ReplyCode"));
	                
	                // success
					if (ReplyCode.SUCCESS.equals(replyCode)) return;
					
					// no object found - that's fine
					if (ReplyCode.NO_OBJECT_FOUND.equals(replyCode)) return;
					
					throw new InvalidReplyCodeException(replyCode);
	            } 
	            // no other possibilities
            	throw new RetsException("Malformed response [multipart="+this.isMultipart+", content-type=text/xml]. " +
            			"Content id did not exist in response and response was not valid rets response.");
	        } catch (JDOMException e) {
	            throw new RetsException(e);
	        } catch (IOException e) {
	            throw new RetsException(e);
	        }
	    }
	}

	public String getType() {
        return (String) this.headers.get("Content-Type");
    }

	public String getBoundary() {
		String contentTypeValue = getType();
		HeaderElement[] contentType = BasicHeaderValueParser.parseElements(contentTypeValue, new BasicHeaderValueParser());
		if (contentType.length != 1)
			throw new IllegalArgumentException("Multipart response appears to have a bad Content-Type: header value: "
					+ contentTypeValue);

		NameValuePair boundaryNV = contentType[0].getParameterByName("boundary");
		if (boundaryNV == null)
			return null;
		return unescapeBoundary(boundaryNV.getValue());
	}

	private static String unescapeBoundary(String boundaryValue) {
		if (boundaryValue.startsWith("\""))
			boundaryValue = boundaryValue.substring(1);
		if (boundaryValue.endsWith("\""))
			boundaryValue = boundaryValue.substring(0, boundaryValue.length() - 1);
		return boundaryValue;
	}


	/**
	 * @return GetObjectIterator, which iterates over SingleObjectResponse
	 * objects.
	 * 
	 * @throws RetsException
	 */
	public <T extends SingleObjectResponse> GetObjectIterator<T> iterator() throws RetsException {
    	return iterator(DEFAULT_BUFFER_SIZE);
    }
    
	/**
	 * @return GetObjectIterator, which iterates over SingleObjectResponse
	 * objects.
	 * 
	 * @param bufferSize How large a buffer should be used for underlying
	 * streams.
	 * 
	 * @throws RetsException
	 */
	public  <T extends SingleObjectResponse> GetObjectIterator<T> iterator(int bufferSize) throws RetsException {
		if(this.exhausted )
			throw new RetsException("response was exhausted - cannot request iterator a second time");
		
		if( this.emptyResponse ) 
			return (GetObjectIterator<T>) EMPTY_OBJECT_RESPONSE_ITERATOR;
		
		
    	if( this.isMultipart ){
			try {
				return GetObjectResponseIterator.createIterator(this, bufferSize);
			} catch (Exception e) {
				throw new RetsException("Error creating multipart GetObjectIterator", e);
			}
    	}
    	// no other possibilities
    	return new NonMultipartGetObjectResponseIterator(this.headers, this.inputStream);
    }
    
    public InputStream getInputStream() {
        return this.inputStream;
    }
    
}


/** Used to implement GetObjectIterator for a non multipart response. */
final class NonMultipartGetObjectResponseIterator implements GetObjectIterator {
	private boolean exhausted;
	private final Map headers;
	private final InputStream inputStream;
	
	public NonMultipartGetObjectResponseIterator(Map headers, InputStream in){
		this.exhausted = false;
		this.headers = headers;
		this.inputStream = in;
	}
	
	
	public void close() throws IOException {
		this.inputStream.close();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public boolean hasNext() {
		return !this.exhausted;
	}
	
	public SingleObjectResponse next() {
		if( this.exhausted ) 
			throw new NoSuchElementException("stream exhausted");
		
		this.exhausted = true;
		return new SingleObjectResponse(this.headers, this.inputStream);
	}
}
