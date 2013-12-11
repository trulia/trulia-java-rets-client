/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import java.io.Serializable;

public interface AttrType<T> extends Serializable {
	public T parse(String value, boolean strict) throws MetaParseException;
	public Class<T> getType();
	public String render(T value);
}
