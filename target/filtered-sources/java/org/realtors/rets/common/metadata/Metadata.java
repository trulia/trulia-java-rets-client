package org.realtors.rets.common.metadata;

import java.io.Serializable;

import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MSearchHelp;

public class Metadata implements Serializable {

	protected MSystem system;

	public Metadata(MetaCollector collector) throws MetadataException {
		MetaObject[] sys = collector.getMetadata(MetadataType.SYSTEM, null);
		if (sys != null && sys.length == 1) {
			try {
				this.system = (MSystem) sys[0];
			} catch (ClassCastException e) {
				throw new MetadataException(e);
			}
			this.system.setCollector(collector);
		}
	}

	public Metadata(MSystem system) {
		this.system = system;
	}

	public MSystem getSystem() {
		return this.system;
	}

	public MResource getResource(String resourceId) {
		return this.system.getMResource(resourceId);
	}

	public MForeignKey getForeignKey(String foreignKeyId) {
		return this.system.getMForeignKey(foreignKeyId);
	}

	public MClass getMClass(String resourceId, String className) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMClass(className);
	}

	public MTable getTable(String resourceId, String className, String systemName) {
		MClass clazz = getMClass(resourceId, className);
		if (clazz == null) {
			return null;
		}
		return clazz.getMTable(systemName);
	}

	public MUpdate getUpdate(String resourceId, String className, String updateName) {
		MClass clazz = getMClass(resourceId, className);
		if (clazz == null) {
			return null;
		}
		return clazz.getMUpdate(updateName);
	}

	public MUpdateType getUpdateType(String resourceId, String className, String updateName, String systemName) {
		MUpdate update = getUpdate(resourceId, className, updateName);
		if (update == null) {
			return null;
		}
		return update.getMUpdateType(systemName);
	}

	public MObject getObject(String resourceId, String objectType) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMObject(objectType);
	}

	public MLookup getLookup(String resourceId, String lookupName) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMLookup(lookupName);
	}

	public MSearchHelp getSearchHelp(String resourceId, String searchHelpId) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMSearchHelp(searchHelpId);
	}

	public MValidationExternal getValidationExternal(String resourceId, String validationExternalName) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMValidationExternal(validationExternalName);
	}

	public MValidationLookup getValidationLookup(String resourceId, String validationLookupName) {
		MResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getMValidationLookup(validationLookupName);
	}

	private String getResourceId(MetaObject obj) {
		String path = obj.getPath();
		int index = path.indexOf(':');
		if (index == -1) {
			return null;
		}
		String resource = path.substring(0, index);
		return resource;
	}

	public MResource getResource(MTable field) {
		String resource = getResourceId(field);
		return getResource(resource);
	}

	public MLookup getLookup(MTable field) {
		String resource = getResourceId(field);
		return getLookup(resource, field.getLookupName());
	}

	public MSearchHelp getSearchHelp(MTable field) {
		String searchHelpID = field.getSearchHelpID();
		if (searchHelpID == null) {
			return null;
		}
		String resource = getResourceId(field);
		return getSearchHelp(resource, searchHelpID);
	}

	public MResource getResource(MClass clazz) {
		return getResource(getResourceId(clazz));
	}
}
