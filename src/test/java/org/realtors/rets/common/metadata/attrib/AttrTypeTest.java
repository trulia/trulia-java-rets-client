/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.MetadataTestCase;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

/**
 * It's rare you can encapsulate the exception expecting tests, but I can,
 * so, I did.
 */
public abstract class AttrTypeTest extends MetadataTestCase {
	protected void assertParseException(AttrType attrib, String input) throws Exception {
		attrib.parse(input,false);
		try {
			attrib.parse(input,true);
			fail("Expected MetaParseException, got no exception for input " + '"' + input + '"');
		} catch (MetaParseException e) {
			// "success"
		}
	}
}
