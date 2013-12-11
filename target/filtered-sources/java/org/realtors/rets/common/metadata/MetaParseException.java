/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

public class MetaParseException extends MetadataException {
	public MetaParseException() {
		super();
	}

	public MetaParseException(String msg) {
		super(msg);
	}

	public MetaParseException(Throwable cause) {
		super(cause);
	}

	public MetaParseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
