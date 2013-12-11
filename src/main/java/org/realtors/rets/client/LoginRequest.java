package org.realtors.rets.client;

public class LoginRequest extends VersionInsensitiveRequest {

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getLoginUrl());
	}

	public void setBrokerCode(String code, String branch) {
		if (code == null) {
			setQueryParameter(KEY_BROKERCODE, null);
		} else {
			if (branch == null) {
				setQueryParameter(KEY_BROKERCODE, code);
			} else {
				setQueryParameter(KEY_BROKERCODE, code + "," + branch);
			}
		}
	}

	public static final String KEY_BROKERCODE = "BrokerCode";
}
