/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.MetaParseException;

public class AttrAlphanum extends AttrAbstractText {

	public AttrAlphanum(int min, int max) {
		super(min, max);
	}

	@Override
	protected void checkContent(String value) throws MetaParseException {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (!Character.isLetterOrDigit(c)) {
				// illegal but exist in CRT metadata
				if ("_- ".indexOf(c) == -1) {
					throw new MetaParseException("Invalid Alphanum character at position " + i + ": " + c);
				}
			}
		}
	}
}
