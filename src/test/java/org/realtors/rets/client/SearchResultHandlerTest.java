/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.client;

import java.io.StringReader;

import org.xml.sax.InputSource;

/**
 * TODO refactor this and the StreamingSearchResultsProcessorTest.
 * 
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class SearchResultHandlerTest extends RetsTestCase {
	SearchResult runSearchTest(String input) throws RetsException {
		return runSearchTest(input, InvalidReplyCodeHandler.FAIL);
	}

	SearchResult runSearchTest(String input, InvalidReplyCodeHandler invalidReplyCodeHandler) throws RetsException {
		SearchResultImpl res = new SearchResultImpl();
		SearchResultHandler h = new SearchResultHandler(res, invalidReplyCodeHandler, CompactRowPolicy.DEFAULT);
		InputSource source = new InputSource(new StringReader(input));
		h.parse(source);
		return res;
	}

	public void testSmallResult() throws RetsException {
		SearchResult result = runSearchTest(GOOD_SMALL_TEST);
		assertTrue("search not complete", result.isComplete());
		String[] columns = result.getColumns();
		assertNotNull(columns);
		assertEquals("column headers count wrong", 1, columns.length);
		assertEquals("bad column header", "Column1", columns[0]);
		assertEquals("wrong row count", 1, result.getCount());
		String[] row = result.getRow(0);
		assertEquals("wrong row width", 1, row.length);
		assertEquals("wrong row data", "Data1", row[0]);
		assertFalse("max rows wrong", result.isMaxrows());
	}

	public void testAllTags() throws RetsException {
		SearchResult result = runSearchTest(ALL_TAGS_TEST);
		assertTrue("search not complete", result.isComplete());
		assertEquals("extended count wrong", 100, result.getCount());
		assertTrue("max rows not set", result.isMaxrows());
		String[] row = result.getRow(0);
		assertNotNull("row 0 is null", row);
		assertEquals("wrong number of row[0] elements", 1, row.length);
		assertEquals("wrong row[0] data", "Data1", row[0]);
		row = result.getRow(1);
		assertNotNull("row 1 is null", row);
		assertEquals("wrong number of row[1] elements", 1, row.length);
		assertEquals("wrong row[1] data", "Data2", row[0]);
	}

	public void testReplyCode20208() throws RetsException {
		SearchResult result = runSearchTest(MAXROWS_REPLYCODE);
		assertTrue("search not complete", result.isComplete());
		assertEquals("extended count wrong", 100, result.getCount());
		assertTrue("max rows not set", result.isMaxrows());
		String[] row = result.getRow(0);
		assertNotNull("row 0 is null", row);
		assertEquals("wrong number of row[0] elements", 1, row.length);
		assertEquals("wrong row[0] data", "Data1", row[0]);
		row = result.getRow(1);
		assertNotNull("row 1 is null", row);
		assertEquals("wrong number of row[1] elements", 1, row.length);
		assertEquals("wrong row[1] data", "Data2", row[0]);
	}

	public void testReplyCode20201WithColumns() throws RetsException {
		SearchResult result = runSearchTest(EMPTY_REPLYCODE_WITH_COLUMNS_TAG);
		assertFalse("iterator should be empty", result.iterator().hasNext());
	}

	public void testReplyCode20201WithoutColumns() throws RetsException {
		SearchResult result = runSearchTest(EMPTY_REPLYCODE);
		assertFalse("iterator should be empty", result.iterator().hasNext());
	}

	public void testEarlyException() throws RetsException {
		try {
			runSearchTest(EARLY_ERROR_TEST);
			fail("Expected an InvalidReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testLateException() throws RetsException {
		try {
			runSearchTest(LATE_ERROR_TEST);
			fail("Expected an Invalid ReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testEarlyExceptionWithTrap() throws RetsException {
		try {
			runSearchTest(EARLY_ERROR_TEST, new TestInvalidReplyCodeHandler());
			fail("Expected an InvalidReplyCodeException");
		} catch (InvalidReplyCodeException e) {
			// "success"
		}
	}

	public void testLateExceptionWithTrap() throws RetsException {
		TestInvalidReplyCodeHandler testInvalidReplyCodeHandler = new TestInvalidReplyCodeHandler();
		runSearchTest(LATE_ERROR_TEST, testInvalidReplyCodeHandler);
		assertEquals(LATE_ERROR_CODE, testInvalidReplyCodeHandler.getReplyCode());
	}

	public static final String CRLF = "\r\n";

	public static final String GOOD_SMALL_TEST = "<RETS ReplyCode=\"0\" " + "ReplyText=\"Success\">" + CRLF
			+ "<DELIMITER value=\"09\"/>" + CRLF + "<COLUMNS>\tColumn1\t</COLUMNS>" + CRLF + "<DATA>\tData1\t</DATA>"
			+ CRLF + "</RETS>" + CRLF;

	public static final String ALL_TAGS_TEST = "<RETS ReplyCode=\"0\" " + "ReplyText=\"Success\">" + CRLF
			+ "<COUNT Records=\"100\"/>" + CRLF + "<DELIMITER value=\"09\"/>" + CRLF + "<COLUMNS>\tColumn1\t</COLUMNS>"
			+ CRLF + "<DATA>\tData1\t</DATA>" + CRLF + "<DATA>\tData2\t</DATA>" + CRLF + "<MAXROWS/>" + "</RETS>"
			+ CRLF;

	public static final String EARLY_ERROR_TEST = "<RETS ReplyCode=\"20203\" " + "ReplyText=\"Misc search Error\">"
			+ CRLF + "</RETS>" + CRLF;

	public static final int LATE_ERROR_CODE = 20203;

	public static final String LATE_ERROR_TEST = "<RETS ReplyCode=\"0\" " + "ReplyText=\"Success\">" + CRLF
			+ "<COUNT Records=\"100\"/>" + CRLF + "<DELIMITER value=\"09\"/>" + CRLF + "<COLUMNS>\tColumn1\t</COLUMNS>"
			+ CRLF + "<DATA>\tData1\t</DATA>" + CRLF + "<DATA>\tData2\t</DATA>" + CRLF + "<RETS-STATUS ReplyCode=\""
			+ LATE_ERROR_CODE + "\" ReplyText=\"Misc Error\"/>" + "</RETS>" + CRLF;

	public static final String MAXROWS_REPLYCODE = "<RETS ReplyCode=\"20208\" " + "ReplyText=\"Success\">" + CRLF
			+ "<COUNT Records=\"100\"/>" + CRLF + "<DELIMITER value=\"09\"/>" + CRLF + "<COLUMNS>\tColumn1\t</COLUMNS>"
			+ CRLF + "<DATA>\tData1\t</DATA>" + CRLF + "<DATA>\tData2\t</DATA>" + CRLF + "<MAXROWS/>" + "</RETS>"
			+ CRLF;

	public static final String EMPTY_REPLYCODE = "<RETS ReplyCode=\"20201\" " + "ReplyText=\"No Records Found\">"
			+ CRLF + "</RETS>" + CRLF;

	public static final String EMPTY_REPLYCODE_WITH_COLUMNS_TAG = "<RETS ReplyCode=\"20201\" "
			+ "ReplyText=\"No Records Found\">" + CRLF + "<DELIMITER value=\"09\"/>" + CRLF
			+ "<COLUMNS>\tColumn1\t</COLUMNS>" + CRLF + "</RETS>" + CRLF;
}
