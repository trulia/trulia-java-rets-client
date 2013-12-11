package org.realtors.rets.client;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

public class StreamingSearchResultProcessorTest extends TestCase {
	protected SearchResultProcessor createProcessor(InvalidReplyCodeHandler invalidReplyCodeHandler) {
		StreamingSearchResultProcessor streamingSearchResultProcessor = new StreamingSearchResultProcessor(1, 0);
		if (invalidReplyCodeHandler != null)
			streamingSearchResultProcessor.setInvalidRelyCodeHandler(invalidReplyCodeHandler);
		return streamingSearchResultProcessor;
	}

	SearchResultSet runSearchTest(String input) throws RetsException {
		return runSearchTest(input, InvalidReplyCodeHandler.FAIL);
	}

	SearchResultSet runSearchTest(String input, InvalidReplyCodeHandler invalidReplyCodeHandler) throws RetsException {
		SearchResultProcessor processor = createProcessor(invalidReplyCodeHandler);
		Reader source = new StringReader(input);
		return processor.parse(source);
	}

	public void testSmallResult() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.GOOD_SMALL_TEST);
		String[] columns = result.getColumns();
		assertNotNull(columns);
		assertEquals("column headers count wrong", 1, columns.length);
		assertEquals("bad column header", "Column1", columns[0]);

		if (result.getCount() != -1)
			assertEquals("wrong row count", 1, result.getCount());

		assertTrue("iterator should have more", result.hasNext());
		String[] row = result.next();

		assertEquals("wrong row width", 1, row.length);
		assertEquals("wrong row data", "Data1", row[0]);

		assertFalse("rows should be exhausted", result.hasNext());
		assertFalse("max rows wrong", result.isMaxrows());
		assertTrue("search not complete", result.isComplete());
	}

	public void testEarlyCallToIsMaxRows() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.ALL_TAGS_TEST);
		try {
			result.isMaxrows();
			fail("Should throw illegal state exception");
		} catch (IllegalStateException e) {
			// "success"
		}
	}

	public void testAllTags() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.ALL_TAGS_TEST);
		assertEquals("extended count wrong", 100, result.getCount());

		assertTrue("iterator should have more", result.hasNext());
		String[] row = result.next();
		assertNotNull("row 0 is null", row);
		assertEquals("wrong number of row[0] elements", 1, row.length);
		assertEquals("wrong row[0] data", "Data1", row[0]);

		assertTrue("iterator should have more", result.hasNext());
		row = result.next();
		assertNotNull("row 1 is null", row);
		assertEquals("wrong number of row[1] elements", 1, row.length);
		assertEquals("wrong row[1] data", "Data2", row[0]);

		assertFalse("rows should be exhausted", result.hasNext());
		assertTrue("search not complete", result.isComplete());
		assertTrue("max rows not set", result.isMaxrows());
	}

	public void testReplyCode20208() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.MAXROWS_REPLYCODE);
		assertEquals("extended count wrong", 100, result.getCount());

		assertTrue("iterator should have more", result.hasNext());
		String[] row = result.next();
		assertNotNull("row 0 is null", row);
		assertEquals("wrong number of row[0] elements", 1, row.length);
		assertEquals("wrong row[0] data", "Data1", row[0]);

		assertTrue("iterator should have more", result.hasNext());
		row = result.next();
		assertNotNull("row 1 is null", row);
		assertEquals("wrong number of row[1] elements", 1, row.length);
		assertEquals("wrong row[1] data", "Data2", row[0]);

		assertFalse("rows should be exhausted", result.hasNext());
		assertTrue("search not complete", result.isComplete());
		assertTrue("max rows not set", result.isMaxrows());
	}

	public void testReplyCode20201WithColumns() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.EMPTY_REPLYCODE_WITH_COLUMNS_TAG);
		assertFalse("iterator should be empty", result.hasNext());
	}

	public void testReplyCode20201WithoutColumns() throws RetsException {
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.EMPTY_REPLYCODE);
		assertFalse("iterator should be empty", result.hasNext());
	}

	public void testEarlyException() throws RetsException {
		try {
			// Test now checks that the error is thrown at process 
			// or during the evaluation of the data rows, since the
			// result may be lazily evaluated (streaming)
			SearchResultSet result = runSearchTest(SearchResultHandlerTest.EARLY_ERROR_TEST);
			while (result.hasNext())
				result.next();
			fail("Expected an Invalid ReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testLateException() throws RetsException {
		try {
			// Test now checks that the error is thrown at process 
			// or during the evaluation of the data rows, since the
			// result may be lazily evaluated (streaming)
			SearchResultSet result = runSearchTest(SearchResultHandlerTest.LATE_ERROR_TEST);
			while (result.hasNext())
				result.next();
			fail("Expected an Invalid ReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testEarlyExceptionWithTrap() throws RetsException {
		try {
			// Test now checks that the error is thrown at process 
			// or during the evaluation of the data rows, since the
			// result may be lazily evaluated (streaming)
			SearchResultSet result = runSearchTest(SearchResultHandlerTest.EARLY_ERROR_TEST,
					new TestInvalidReplyCodeHandler());
			while (result.hasNext())
				result.next();
			fail("Expected an Invalid ReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testLateExceptionWithTrap() throws RetsException {
		// Test now checks that the error is thrown at process 
		// or during the evaluation of the data rows, since the
		// result may be lazily evaluated (streaming)
		TestInvalidReplyCodeHandler testInvalidReplyCodeHandler = new TestInvalidReplyCodeHandler();
		SearchResultSet result = runSearchTest(SearchResultHandlerTest.LATE_ERROR_TEST, testInvalidReplyCodeHandler);
		while (result.hasNext())
			result.next();

		assertEquals(SearchResultHandlerTest.LATE_ERROR_CODE, testInvalidReplyCodeHandler.getReplyCode());
	}

	public void testTimeout() throws Exception {
		int timeout = 100;
		SearchResultProcessor processor = new StreamingSearchResultProcessor(1, timeout);
		Reader source = new StringReader(SearchResultHandlerTest.ALL_TAGS_TEST);
		SearchResultSet result = processor.parse(source);

		try {
			// attempt to force timeout to occur
			Thread.sleep(timeout * 10);
			// hasNext should fail b/c timeout
			// will have occurred
			result.hasNext();
			fail("Should fail since timeout should have been reached");
		} catch (RetsException e) {
			// success
		}
	}

	public void testIONotEatenException() throws RetsException {
		SearchResultProcessor processor = new StreamingSearchResultProcessor(100);

		IOFailReader ioExceptionStream = new IOFailReader(new StringReader(SearchResultHandlerTest.ALL_TAGS_TEST));
		ioExceptionStream.setFailRead(true);

		SearchResultSet resultSet = processor.parse(ioExceptionStream);

		try {
			while (resultSet.hasNext())
				resultSet.next();
			fail("Expection an IOException to be thrown during stream reading.");
		} catch (RetsException e) {
			e.printStackTrace();
			assertNotNull(e);
		}
	}
}
