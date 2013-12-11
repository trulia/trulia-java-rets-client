/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import java.io.Serializable;

/** Interface for Metadata objects to collect their children. */
public interface MetaCollector extends Serializable {
	/**
	 * @param path path to the parent object.
	 */
	public MetaObject[] getMetadata(MetadataType type, String path) throws MetadataException;

	public MetaObject[] getMetadataRecursive(MetadataType type, String path) throws MetadataException;
}
