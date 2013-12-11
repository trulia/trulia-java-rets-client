/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import org.realtors.rets.common.metadata.MetaParseException;

public class AttrEnum extends AttrAbstractText {
	public AttrEnum(String[] values) {
		super(0, 0);
		this.map = new HashMap<String,String>();
		for (String value : values)  this.map.put(value, value);
		this.map = Collections.unmodifiableMap(this.map);
	}

	@Override
	protected void checkContent(String value) throws MetaParseException {
		if( !this.map.containsKey(value) ) 
			throw new MetaParseException("Invalid key: " + value);
	}

	private Map<String,String> map;
}
