/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public abstract class AttrAbstractText implements AttrType<String> {
	protected int min;
	protected int max;

	public AttrAbstractText(int min, int max) {
		this.min = min;
		this.max = max;
	}

	
	public String parse(String value, boolean strict) throws MetaParseException {
		if( !strict )
			return value;
		int l = value.length();
		if (this.min != 0 && l < this.min) {
			throw new MetaParseException("Value too short (min " + this.min + "): " + l);
		}
		if (this.max != 0 && l > this.max) {
			throw new MetaParseException("Value too long (max " + this.max + "): " + l);
		}
		checkContent(value);
		return value;
	}

	
	public Class<String> getType() {
		return String.class;
	}

	
	public String render(String value) {
		return value;
	}

	protected abstract void checkContent(String value) throws MetaParseException;

}
