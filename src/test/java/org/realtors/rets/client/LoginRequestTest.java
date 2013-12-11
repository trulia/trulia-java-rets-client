package org.realtors.rets.client;

public class LoginRequestTest extends RetsTestCase {
	public void testGetUrl() {
		LoginRequest req = new LoginRequest();
		req.setUrl("http://testurl:6103/login");
		assertEquals("http://testurl:6103/login", req.getUrl());
	}

	public void testSetBrokerCode() {
		LoginRequest req = new LoginRequest();
		req.setUrl("http://testurl:6103/login");
		req.setBrokerCode(null, "branch");
		assertEquals("http://testurl:6103/login", req.getUrl());
		req.setBrokerCode("broker", null);
		// query parameters are separate now because of get/post
		assertEquals("http://testurl:6103/login", req.getUrl());
		assertEquals("BrokerCode=broker", req.getHttpParameters());
		req.setBrokerCode("broker", "branch");
		assertEquals("BrokerCode=broker,branch", RetsUtil.urlDecode(req.getHttpParameters()));
	}
}
