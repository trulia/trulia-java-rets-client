package org.realtors.rets.client;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class SingleObjectResponseTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCaseInsensitiveHeaders() throws Exception {
		Map headers = new HashMap();
		headers.put("Content-type", "1");
		headers.put("location", "2");
		headers.put("Object-Id", "3");
		headers.put("content-id", "4");
		headers.put("CONTENT-DESCRIPTION", "5");

		SingleObjectResponse res = new SingleObjectResponse(headers, null);
		assertEquals("1", res.getType());
		assertEquals("2", res.getLocation());
		assertEquals("3", res.getObjectID());
		assertEquals("4", res.getContentID());
		assertEquals("5", res.getDescription());
	}

}
