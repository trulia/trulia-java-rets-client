package org.realtors.rets.client;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A suite of all tests in the org.realtors.rets.client.* package.
 */
public class AllTests {
	/**
	 * Returns a test suite for all classes in org.realtors.rets.client.*.
	 */
	public static Test suite() {
		TestSuite suite;

		suite = new TestSuite();
		/*suite.addTestSuite(GetMetadataRequestTest.class);
		suite.addTestSuite(GetMetadataResponseTest.class);
		suite.addTestSuite(GetObjectResponseIteratorTest.class);
		suite.addTestSuite(LoginRequestTest.class);
		suite.addTestSuite(LoginResponseTest.class);
		suite.addTestSuite(LogoutResponseTest.class);
		//        suite.addTestSuite(MetadataTableTest.class);
		//        suite.addTestSuite(MetadataTableBuilderTest.class);
		suite.addTestSuite(RetsVersionTest.class);
		suite.addTestSuite(SearchResultImplTest.class);
		suite.addTestSuite(SearchResultHandlerTest.class);
		suite.addTestSuite(SingleObjectResponseTest.class);*/
		return suite;
	}
}
