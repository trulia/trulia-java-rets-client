/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import org.apache.commons.lang.exception.NestableException;

public class MetadataException extends NestableException {
	public MetadataException() {
		super();
	}

	public MetadataException(String msg) {
		super(msg);
	}

	public MetadataException(Throwable cause) {
		super(cause);
	}

	public MetadataException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
