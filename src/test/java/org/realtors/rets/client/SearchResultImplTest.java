/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.client;

import java.util.NoSuchElementException;

/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class SearchResultImplTest extends RetsTestCase {
	public void testSearchResult() {
		String[] cols = { "Column1", "Column2" };
		String[] row1 = { "Data1x1", "Data1x2" };
		String[] row2 = { "Data2x1", "Data2x2" };
		String[] row2alt = { "", "" };
		row2alt[0] = row2[0];
		row2alt[1] = row2[1];
		SearchResultImpl result = new SearchResultImpl();
		result.setCount(5);
		result.setColumns(cols);
		result.addRow(row1);
		result.addRow(row2);
		result.setMaxrows();
		result.setComplete();
		assertEquals("setCount wrong", result.getCount(), 5);
		assertTrue("isComplete not set", result.isComplete());
		assertTrue("isMaxrows not set", result.isMaxrows());
		assertEquals("columns mangled", cols, result.getColumns());
		assertEquals("row 1 mangled", row1, result.getRow(0));
		assertEquals("row 2 mangled", row2alt, result.getRow(1));
		try {
			result.getRow(2);
			fail("getting invalid row 2 should have thrown " + "NoSuchElementException");
		} catch (NoSuchElementException e) {
			// "success"
		}
	}

	public void testMinimumSearchResult() {
		String[] cols = { "col1" };
		String[] row = { "row1" };
		SearchResultImpl result = new SearchResultImpl();
		result.setColumns(cols);
		result.addRow(row);
		result.setComplete();
		assertEquals("row count wrong", 1, result.getCount());
		assertTrue("isComplete wrong", result.isComplete());
		assertFalse("isMaxrows wrong", result.isMaxrows());
	}
}
