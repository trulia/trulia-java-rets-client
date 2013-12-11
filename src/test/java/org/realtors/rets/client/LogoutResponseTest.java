package org.realtors.rets.client;

public class LogoutResponseTest extends RetsTestCase {
	/*
	 * TODO: FIX THESE
	 *
	public void testValidLogoutResponse10() throws RetsException {
		LogoutResponse response = new LogoutResponse();
		response.parse(getResource("logout_valid10.xml"), RetsVersion.RETS_10);
		assertEquals("1000", response.getSeconds());
		assertEquals("$20.00", response.getBillingInfo());
		assertEquals("Good Bye", response.getLogoutMessage());
	}

	public void testValidLogoutResponse() throws RetsException {
		LogoutResponse response = new LogoutResponse();
		response.parse(getResource("logout_valid15.xml"), RetsVersion.RETS_15);
		assertEquals("1000", response.getSeconds());
		assertEquals("$20.00", response.getBillingInfo());
		assertEquals("Good Bye", response.getLogoutMessage());
	}

	public void testLowerCaseKeys() throws RetsException {
		LogoutResponse response = new LogoutResponse();
		response.parse(getResource("logout_lower_case.xml"), RetsVersion.RETS_15);
		assertEquals("1000", response.getSeconds());
		assertEquals("$20.00", response.getBillingInfo());
		assertEquals("Good Bye", response.getLogoutMessage());
	}
	 */
	public void testStrictLowerCaseKeys() {
		try {
			LogoutResponse response = new LogoutResponse();
			response.setStrict(true);
			response.parse(getResource("logout_lower_case.xml"), RetsVersion.RETS_15);
			fail("Should have thrown exception");
		} catch (RetsException e) {
			// Expected
		}
	}
	/*
	 * TODO: FIX THIS.
	 *
	public void testLogoutNoEquals() throws RetsException {
		LogoutResponse response = new LogoutResponse();
		response.parse(getResource("logout_no_equals.xml"), RetsVersion.RETS_15);
		assertNull(response.getSeconds());
		assertNull(response.getBillingInfo());
		assertNull(response.getLogoutMessage());
	}
	*/
}
