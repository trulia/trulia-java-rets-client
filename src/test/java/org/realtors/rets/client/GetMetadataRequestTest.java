package org.realtors.rets.client;

public class GetMetadataRequestTest extends RetsTestCase {
	public void testGetMetadataRequestSimple() throws RetsException {
		GetMetadataRequest request = new GetMetadataRequest("SYSTEM", "*");
		request.setUrl("http://rets.test:6103/getMetadata");
		assertFalse(request.isCompactFormat());
		assertTrue(request.isStandardXmlFormat());
		assertNull(request.getStandardXmlVersion());
		assertEquals("http://rets.test:6103/getMetadata", request.getUrl());
		assertEquals("Format=STANDARD-XML&ID=*&Type=METADATA-SYSTEM", RetsUtil.urlDecode(request.getHttpParameters()));
	}

	public void testGetMetadataRequestMultipleIds() throws RetsException {
		GetMetadataRequest request = new GetMetadataRequest("UPDATE_TYPE", new String[] { "ActiveAgent", "ACTAGT",
				"Change_ACTAGT" });
		request.setCompactFormat();

		assertTrue(request.isCompactFormat());
		assertFalse(request.isStandardXmlFormat());
		assertEquals("Format=COMPACT&ID=ActiveAgent:ACTAGT:Change_ACTAGT" + "&Type=METADATA-UPDATE_TYPE", RetsUtil
				.urlDecode(request.getHttpParameters()));
	}

	public void testInvalidGetMetadataRequests() throws RetsException {
		try {
			// ID for METADATA-SYSTEM can only be 0 or *
			new GetMetadataRequest("SYSTEM", "Blah");
			fail("Should have thrown an InvalidArgumentException");
		} catch (InvalidArgumentException e) {
			// Expected
		}

		try {
			// ID for METADATA-RESOURCE can only be 0 or *
			new GetMetadataRequest("RESOURCE", "Blah");
			fail("Should have thrown an InvalidArgumentException");
		} catch (InvalidArgumentException e) {
			// Expected
		}

		try {
			// Must have at least 1 ID
			new GetMetadataRequest("RESOURCE", new String[0]);
			fail("Should have thrown an InvalidArgumentException");
		} catch (InvalidArgumentException e) {
			// Expected
		}
	}
}
