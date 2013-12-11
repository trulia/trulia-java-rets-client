/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

public abstract class MetadataBuilder {
	protected MetadataBuilder() {
		this.mStrict = false;
	}

	public boolean isStrict() {
		return this.mStrict;
	}

	public void setStrict(boolean strict) {
		this.mStrict = strict;
	}

	protected Metadata finish(MSystem system) {
		return new Metadata(system);
	}

	protected static void setAttribute(MetaObject obj, String key, String value) {
		obj.setAttribute(key, value);
	}

	protected MSystem buildSystem() {
		MSystem system = new MSystem(this.mStrict);
		return system;
	}

	protected MResource buildResource() {
		MResource resource = new MResource(this.mStrict);
		return resource;
	}

	protected MForeignKey buildForeignKey() {
		MForeignKey key = new MForeignKey(this.mStrict);
		return key;
	}

	protected MClass buildClass() {
		MClass clazz = new MClass(this.mStrict);
		return clazz;
	}

	protected MTable buildTable() {
		MTable table = new MTable(this.mStrict);
		return table;
	}

	protected MUpdate buildUpdate() {
		MUpdate update = new MUpdate(this.mStrict);
		return update;
	}

	protected MUpdateType buildUpdateType() {
		MUpdateType updatetype = new MUpdateType(this.mStrict);
		return updatetype;
	}

	protected MObject buildObject() {
		MObject obj = new MObject(this.mStrict);
		return obj;
	}

	protected MSearchHelp buildSearchHelp() {
		MSearchHelp help = new MSearchHelp(this.mStrict);
		return help;
	}

	protected MEditMask buildEditMask() {
		MEditMask mask = new MEditMask(this.mStrict);
		return mask;
	}

	protected MLookup buildLookup() {
		MLookup lookup = new MLookup(this.mStrict);
		return lookup;
	}

	protected MLookupType buildLookupType() {
		MLookupType type = new MLookupType(this.mStrict);
		return type;
	}

	protected MUpdateHelp buildUpdateHelp() {
		MUpdateHelp help = new MUpdateHelp(this.mStrict);
		return help;
	}

	protected MValidationLookup buildValidationLookup() {
		MValidationLookup lookup = new MValidationLookup(this.mStrict);
		return lookup;
	}

	protected MValidationExternalType buildValidationExternalType() {
		MValidationExternalType type = new MValidationExternalType(this.mStrict);
		return type;
	}

	protected MValidationExpression buildValidationExpression() {
		MValidationExpression expression = new MValidationExpression(this.mStrict);
		return expression;
	}

	protected MValidationExternal buildValidationExternal() {
		MValidationExternal external = new MValidationExternal(this.mStrict);
		return external;
	}

	protected MValidationLookupType buildValidationLookupType() {
		MValidationLookupType lookupType = new MValidationLookupType(this.mStrict);
		return lookupType;
	}

	public abstract Metadata doBuild(Object src) throws MetadataException;

	public abstract MetaObject[] parse(Object src) throws MetadataException;

	protected MetaObject newType(MetadataType type) {
		if (type == MetadataType.SYSTEM) {
			return buildSystem();
		}
		if (type == MetadataType.RESOURCE) {
			return buildResource();
		}
		if (type == MetadataType.FOREIGNKEYS) {
			return buildForeignKey();
		}
		if (type == MetadataType.CLASS) {
			return buildClass();
		}
		if (type == MetadataType.TABLE) {
			return buildTable();
		}
		if (type == MetadataType.UPDATE) {
			return buildUpdate();
		}
		if (type == MetadataType.UPDATE_TYPE) {
			return buildUpdateType();
		}
		if (type == MetadataType.OBJECT) {
			return buildObject();
		}
		if (type == MetadataType.SEARCH_HELP) {
			return buildSearchHelp();
		}
		if (type == MetadataType.EDITMASK) {
			return buildEditMask();
		}
		if (type == MetadataType.UPDATE_HELP) {
			return buildUpdateHelp();
		}
		if (type == MetadataType.LOOKUP) {
			return buildLookup();
		}
		if (type == MetadataType.LOOKUP_TYPE) {
			return buildLookupType();
		}
		if (type == MetadataType.VALIDATION_LOOKUP) {
			return buildValidationLookup();
		}
		if (type == MetadataType.VALIDATION_LOOKUP_TYPE) {
			return buildValidationLookupType();
		}
		if (type == MetadataType.VALIDATION_EXTERNAL) {
			return buildValidationExternal();
		}
		if (type == MetadataType.VALIDATION_EXTERNAL_TYPE) {
			return buildValidationExternalType();
		}
		if (type == MetadataType.VALIDATION_EXPRESSION) {
			return buildValidationExpression();
		}
		throw new RuntimeException("No metadata type class found for " + type.name());
	}

	private boolean mStrict;
}
