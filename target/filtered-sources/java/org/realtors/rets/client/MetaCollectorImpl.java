/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.client;

public class MetaCollectorImpl extends MetaCollectorAdapter {
	private final RetsTransport mTransport;

	public MetaCollectorImpl(RetsTransport transport) {
		this.mTransport = transport;
	}

	@Override
	protected GetMetadataResponse doRequest(GetMetadataRequest req) throws RetsException {
		return this.mTransport.getMetadata(req);
	}

}
