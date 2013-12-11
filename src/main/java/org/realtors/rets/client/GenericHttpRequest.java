package org.realtors.rets.client;

/**
 * on the off chance you need an ad hoc request object...
 */
public class GenericHttpRequest extends VersionInsensitiveRequest {
	
	public GenericHttpRequest(){
		// noop
	}

	public GenericHttpRequest(String url){
		this.mUrl = url;
	}
	
	/**
	 * throws an exception.  GenericHttpRequest can't have a
	 * CapabilityUrl
	 * @param urls the CapabilityUrls object that has nothing we can use
	 */
	@Override
	public void setUrl(CapabilityUrls urls) {
		// do nothing
		return;
	}

	/**
	 * expose the queryParameter interface to build query arguments.
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	@Override
	public void setQueryParameter(String name, String value) {
		super.setQueryParameter(name, value);
	}
}