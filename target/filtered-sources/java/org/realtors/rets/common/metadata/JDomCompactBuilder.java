/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;
import org.xml.sax.InputSource;

public class JDomCompactBuilder extends MetadataBuilder {
	public static final String CONTAINER_PREFIX = "METADATA-";
	public static final String CONTAINER_ROOT = "RETS";
	public static final String CONTAINER_METADATA = "METADATA";
	public static final String CONTAINER_SYSTEM = "METADATA-SYSTEM";
	public static final String CONTAINER_RESOURCE = "METADATA-RESOURCE";
	public static final String CONTAINER_FOREIGNKEY = "METADATA-FOREIGN_KEY";
	public static final String CONTAINER_CLASS = "METADATA-CLASS";
	public static final String CONTAINER_TABLE = "METADATA-TABLE";
	public static final String CONTAINER_UPDATE = "METADATA-UPDATE";
	public static final String CONTAINER_UPDATETYPE = "METADATA-UPDATE_TYPE";
	public static final String CONTAINER_OBJECT = "METADATA-OBJECT";
	public static final String CONTAINER_SEARCHHELP = "METADATA-SEARCH_HELP";
	public static final String CONTAINER_EDITMASK = "METADATA-EDITMASK";
	public static final String CONTAINER_UPDATEHELP = "METADATA-UPDATE_HELP";
	public static final String CONTAINER_LOOKUP = "METADATA-LOOKUP";
	public static final String CONTAINER_LOOKUPTYPE = "METADATA-LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONLOOKUP = "METADATA-VALIDATION_LOOKUP";
	public static final String CONTAINER_VALIDATIONLOOKUPTYPE = "METADATA-VALIDATION_LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONEXPRESSION = "METADATA-VALIDATION_EXPRESSION";
	public static final String CONTAINER_VALIDATIONEXTERNAL = "METADATA-VALIDATION_EXTERNAL";
	public static final String CONTAINER_VALIDATIONEXTERNALTYPE = "METADATA-VALIDATION_EXTERNAL_TYPE";
	public static final String ELEMENT_SYSTEM = "SYSTEM";
	public static final String COLUMNS = "COLUMNS";
	public static final String DATA = "DATA";
	public static final String ATTRIBUTE_RESOURCE = "Resource";
	public static final String ATTRIBUTE_CLASS = "Class";
	public static final String ATTRIBUTE_UPDATE = "Update";
	public static final String ATTRIBUTE_LOOKUP = "Lookup";
	public static final String ATTRIBUTE_VALIDATIONEXTERNAL = "ValidationExternal";
	public static final String ATTRIBUTE_VALIDATIONLOOKUP = "ValidationLookup";
	private static final Log LOG = LogFactory.getLog(JDomCompactBuilder.class);

	@Override
	public Metadata doBuild(Object src) throws MetadataException {
		return build((Document) src);
	}

	public Metadata build(InputSource source) throws MetadataException {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(source);
		} catch (JDOMException e) {
			throw new MetadataException("Couldn't build document", e);
		} catch (IOException e) {
			throw new MetadataException("Couldn't build document", e);
		}
		return build(document);
	}

	@Override
	public MetaObject[] parse(Object src) throws MetadataException {
		return parse((Document) src);
	}

	public MetaObject[] parse(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element container = root.getChild(CONTAINER_SYSTEM);
		if (container != null) {
			MSystem sys = processSystem(container);
			if (root.getChild(CONTAINER_RESOURCE) != null) {
				Metadata m = new Metadata(sys);
				recurseAll(m, root);
			}
			return new MetaObject[] { sys };
		}
		container = root.getChild(CONTAINER_RESOURCE);
		if (container != null) {
			return processResource(container);
		}
		container = root.getChild(CONTAINER_CLASS);
		if (container != null) {
			return processClass(container);
		}
		container = root.getChild(CONTAINER_TABLE);
		if (container != null) {
			return processTable(container);
		}
		container = root.getChild(CONTAINER_UPDATE);
		if (container != null) {
			return processUpdate(container);
		}
		container = root.getChild(CONTAINER_UPDATETYPE);
		if (container != null) {
			return processUpdateType(container);
		}
		container = root.getChild(CONTAINER_OBJECT);
		if (container != null) {
			return processObject(container);
		}
		container = root.getChild(CONTAINER_SEARCHHELP);
		if (container != null) {
			return processSearchHelp(container);
		}
		container = root.getChild(CONTAINER_EDITMASK);
		if (container != null) {
			return processEditMask(container);
		}
		container = root.getChild(CONTAINER_LOOKUP);
		if (container != null) {
			return processLookup(container);
		}
		container = root.getChild(CONTAINER_LOOKUPTYPE);
		if (container != null) {
			return processLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUP);
		if (container != null) {
			return processValidationLookup(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUPTYPE);
		if (container != null) {
			return processValidationLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNAL);
		if (container != null) {
			return processValidationExternal(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNALTYPE);
		if (container != null) {
			return processValidationExternalType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXPRESSION);
		if (container != null) {
			return processValidationExpression(container);
		}
		return null;
	}

	public Metadata build(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element element = root.getChild(CONTAINER_SYSTEM);
		if (element == null) {
			throw new MetadataException("Missing element " + CONTAINER_SYSTEM);
		}
		MSystem sys = processSystem(element);
		Metadata metadata;
		metadata = new Metadata(sys);
		recurseAll(metadata, root);
		return metadata;
	}

	private void recurseAll(Metadata metadata, Element root) throws MetaParseException {
		attachResource(metadata, root);
		attachClass(metadata, root);
		attachTable(metadata, root);
		attachUpdate(metadata, root);
		attachUpdateType(metadata, root);
		attachObject(metadata, root);
		attachSearchHelp(metadata, root);
		attachEditMask(metadata, root);
		attachLookup(metadata, root);
		attachLookupType(metadata, root);
		attachValidationLookup(metadata, root);
		attachValidationLookupType(metadata, root);
		attachValidationExternal(metadata, root);
		attachValidationExternalType(metadata, root);
		attachValidationExpression(metadata, root);
	}

	private void setAttributes(MetaObject obj, String[] columns, String[] data) {
		int count = columns.length;
		if (count > data.length) {
			count = data.length;
		}
		for (int i = 0; i < count; i++) {
			String column = columns[i];
			String datum = data[i];
			if (!datum.equals("")) {
				setAttribute(obj, column, datum);
			}
		}
	}

	private String[] getColumns(Element el) {
		Element cols = el.getChild(COLUMNS);
		return split(cols);
	}

	/** do NOT use string.split() unless your prepared to deal with loss due to token boundary conditions */
	private String[] split(Element el) {
		if( el == null ) return null;
		final String delimiter = "\t";
		StringTokenizer tkn = new StringTokenizer(el.getText(), delimiter, true);
		List list = new LinkedList();
		tkn.nextToken(); // junk the first element
		String last = null;
		while (tkn.hasMoreTokens()) {
			String next = tkn.nextToken();
			if (next.equals(delimiter)) {
				if (last == null) {
					list.add("");
				} else {
					last = null;
				}
			} else {
				list.add(next);
				last = next;
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * Gets an attribute that is not expected to be null (i.e. an attribute that
	 * MUST exist).
	 *
	 * @param element Element
	 * @param name Attribute name
	 * @return value of attribute
	 * @throws MetaParseException if the value is null.
	 */
	private String getNonNullAttribute(Element element, String name) throws MetaParseException {
		String value = element.getAttributeValue(name);
		if (value == null) {
			throw new MetaParseException("Attribute '" + name + "' not found on tag " + toString(element));
		}
		return value;
	}

	private String toString(Element element) {
		StringBuffer buffer = new StringBuffer();
		List attributes = element.getAttributes();
		buffer.append("'").append(element.getName()).append("'");
		buffer.append(", attributes: ").append(attributes);
		return buffer.toString();
	}

	private MSystem processSystem(Element container) {
		Element element = container.getChild(ELEMENT_SYSTEM);
		MSystem system = buildSystem();
		// system metadata is such a hack.  the first one here is by far my favorite
		String comment = container.getChildText(MSystem.COMMENTS);
		String systemId = element.getAttributeValue(MSystem.SYSTEMID);
		String systemDescription = element.getAttributeValue(MSystem.SYSTEMDESCRIPTION);
		String version = container.getAttributeValue(MSystem.VERSION);
		String date = container.getAttributeValue(MSystem.DATE);
		setAttribute(system, MSystem.COMMENTS, comment);
		setAttribute(system, MSystem.SYSTEMID, systemId);
		setAttribute(system, MSystem.SYSTEMDESCRIPTION, systemDescription);
		setAttribute(system, MSystem.VERSION, version);
		setAttribute(system, MSystem.DATE, date);
		return system;
	}

	private void attachResource(Metadata metadata, Element root) {
		MSystem system = metadata.getSystem();
		List containers = root.getChildren(CONTAINER_RESOURCE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource[] resources = this.processResource(container);
			for (int j = 0; j < resources.length; j++) {
				system.addChild(MetadataType.RESOURCE, resources[j]);
			}
		}
	}

	private MResource[] processResource(Element resourceContainer) {
		String[] columns = getColumns(resourceContainer);
		List rows = resourceContainer.getChildren(DATA);
		MResource[] resources = new MResource[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MResource resource = buildResource();
			setAttributes(resource, columns, data);
			resources[i] = resource;
		}
		return resources;
	}

	private void attachClass(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_CLASS);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			MResource resource = metadata.getResource(resourceId);
			MClass[] classes = processClass(container);
			for (int j = 0; j < classes.length; j++) {
				resource.addChild(MetadataType.CLASS, classes[j]);
			}
		}
	}

	private MClass[] processClass(Element classContainer) throws MetaParseException {
		String name = classContainer.getName();
		String resourceId = getNonNullAttribute(classContainer, ATTRIBUTE_RESOURCE);
		LOG.debug("resource name: " + resourceId + " for container " + name);
		String[] columns = getColumns(classContainer);
		List rows = classContainer.getChildren(DATA);
		MClass[] classes = new MClass[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MClass clazz = buildClass();
			setAttributes(clazz, columns, data);
			classes[i] = clazz;
		}
		return classes;
	}

	private void attachTable(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_TABLE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String className = getNonNullAttribute(container, ATTRIBUTE_CLASS);
			MClass clazz = metadata.getMClass(resourceId, className);

			if (clazz == null) {
				//MarketLinx Strikes!!!
				LOG.warn("Found table metadata for resource class: " + resourceId + ":" + className
						+ " but there is no class metadata for " + resourceId + ":" + className);
				continue;
			}

			MTable[] fieldMetadata = processTable(container);
			for (int j = 0; j < fieldMetadata.length; j++) {
				clazz.addChild(MetadataType.TABLE, fieldMetadata[j]);
			}
		}
	}

	private MTable[] processTable(Element tableContainer) {
		String[] columns = getColumns(tableContainer);
		List rows = tableContainer.getChildren(DATA);
		MTable[] fieldMetadata = new MTable[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MTable mTable = buildTable();
			setAttributes(mTable, columns, data);
			fieldMetadata[i] = mTable;
		}
		return fieldMetadata;
	}

	private void attachUpdate(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_UPDATE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MClass parent = metadata.getMClass(getNonNullAttribute(container, ATTRIBUTE_RESOURCE), getNonNullAttribute(
					container, ATTRIBUTE_CLASS));
			MUpdate[] updates = processUpdate(container);
			for (int j = 0; j < updates.length; j++) {
				parent.addChild(MetadataType.UPDATE, updates[j]);
			}
		}
	}

	private MUpdate[] processUpdate(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MUpdate[] updates = new MUpdate[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MUpdate update = buildUpdate();
			setAttributes(update, columns, data);
			updates[i] = update;
		}
		return updates;
	}

	private void attachUpdateType(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_UPDATETYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MUpdate parent = metadata.getUpdate(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_CLASS), getNonNullAttribute(container, ATTRIBUTE_UPDATE));
			MUpdateType[] updateTypes = processUpdateType(container);
			for (int j = 0; j < updateTypes.length; j++) {
				parent.addChild(MetadataType.UPDATE_TYPE, updateTypes[j]);
			}
		}
	}

	private MUpdateType[] processUpdateType(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MUpdateType[] updateTypes = new MUpdateType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MUpdateType updateType = buildUpdateType();
			setAttributes(updateType, columns, data);
			updateTypes[i] = updateType;
		}
		return updateTypes;
	}

	private void attachObject(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_OBJECT);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MObject[] objects = processObject(container);
			for (int j = 0; j < objects.length; j++) {
				parent.addChild(MetadataType.OBJECT, objects[j]);
			}
		}
	}

	private MObject[] processObject(Element objectContainer) {
		String[] columns = getColumns(objectContainer);
		List rows = objectContainer.getChildren(DATA);
		MObject[] objects = new MObject[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MObject object = buildObject();
			setAttributes(object, columns, data);
			objects[i] = object;
		}
		return objects;
	}

	private void attachSearchHelp(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_SEARCHHELP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MSearchHelp[] searchHelps = processSearchHelp(container);
			for (int j = 0; j < searchHelps.length; j++) {
				parent.addChild(MetadataType.SEARCH_HELP, searchHelps[j]);
			}
		}
	}

	private MSearchHelp[] processSearchHelp(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MSearchHelp[] searchHelps = new MSearchHelp[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MSearchHelp searchHelp = buildSearchHelp();
			setAttributes(searchHelp, columns, data);
			searchHelps[i] = searchHelp;
		}
		return searchHelps;
	}

	private void attachEditMask(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_EDITMASK);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MEditMask[] editMasks = processEditMask(container);
			for (int j = 0; j < editMasks.length; j++) {
				parent.addChild(MetadataType.EDITMASK, editMasks[j]);
			}
		}
	}

	private MEditMask[] processEditMask(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MEditMask[] editMasks = new MEditMask[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MEditMask editMask = buildEditMask();
			setAttributes(editMask, columns, data);
			editMasks[i] = editMask;
		}
		return editMasks;
	}

	private void attachLookup(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_LOOKUP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MLookup[] lookups = processLookup(container);
			for (int j = 0; j < lookups.length; j++) {
				parent.addChild(MetadataType.LOOKUP, lookups[j]);
			}
		}
	}

	private MLookup[] processLookup(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MLookup[] lookups = new MLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MLookup lookup = buildLookup();
			setAttributes(lookup, columns, data);
			lookups[i] = lookup;
		}
		return lookups;
	}

	private void attachLookupType(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_LOOKUPTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MLookup parent = metadata.getLookup(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_LOOKUP));

			if (parent == null) {
				LOG.warn("Skipping lookup type: could not find lookup for tag " + toString(container));
				continue;
			}

			MLookupType[] lookupTypes = processLookupType(container);
			for (int j = 0; j < lookupTypes.length; j++) {
				parent.addChild(MetadataType.LOOKUP_TYPE, lookupTypes[j]);
			}
		}
	}

	private MLookupType[] processLookupType(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MLookupType[] lookupTypes = new MLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MLookupType lookupType = buildLookupType();
			setAttributes(lookupType, columns, data);
			lookupTypes[i] = lookupType;
		}
		return lookupTypes;
	}

	private void attachValidationLookup(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_VALIDATIONLOOKUP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MValidationLookup[] validationLookups = processValidationLookup(container);
			for (int j = 0; j < validationLookups.length; j++) {
				parent.addChild(MetadataType.VALIDATION_LOOKUP, validationLookups[j]);
			}
		}
	}

	private MValidationLookup[] processValidationLookup(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MValidationLookup[] validationLookups = new MValidationLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationLookup validationLookup = buildValidationLookup();
			setAttributes(validationLookup, columns, data);
			validationLookups[i] = validationLookup;
		}
		return validationLookups;
	}

	private void attachValidationLookupType(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_VALIDATIONLOOKUPTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MValidationLookup parent = metadata.getValidationLookup(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_VALIDATIONLOOKUP));
			MValidationLookupType[] validationLookupTypes = processValidationLookupType(container);
			for (int j = 0; j < validationLookupTypes.length; j++) {
				parent.addChild(MetadataType.VALIDATION_LOOKUP_TYPE, validationLookupTypes[j]);
			}
		}
	}

	private MValidationLookupType[] processValidationLookupType(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MValidationLookupType[] validationLookupTypes = new MValidationLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationLookupType validationLookupType = buildValidationLookupType();
			setAttributes(validationLookupType, columns, data);
			validationLookupTypes[i] = validationLookupType;
		}
		return validationLookupTypes;
	}

	private void attachValidationExternal(Metadata metadata, Element root) {
		List containers = root.getChildren(CONTAINER_VALIDATIONEXTERNAL);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(container.getAttributeValue(ATTRIBUTE_RESOURCE));
			MValidationExternal[] validationExternals = processValidationExternal(container);
			for (int j = 0; j < validationExternals.length; j++) {
				parent.addChild(MetadataType.VALIDATION_EXTERNAL, validationExternals[j]);
			}
		}
	}

	private MValidationExternal[] processValidationExternal(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MValidationExternal[] validationExternals = new MValidationExternal[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationExternal validationExternal = buildValidationExternal();
			setAttributes(validationExternal, columns, data);
			validationExternals[i] = validationExternal;
		}
		return validationExternals;
	}

	private void attachValidationExternalType(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_VALIDATIONEXTERNALTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MValidationExternal parent = metadata.getValidationExternal(getNonNullAttribute(container,
					ATTRIBUTE_RESOURCE), getNonNullAttribute(container, ATTRIBUTE_VALIDATIONEXTERNAL));
			MValidationExternalType[] validationExternalTypes = processValidationExternalType(container);
			for (int j = 0; j < validationExternalTypes.length; j++) {
				parent.addChild(MetadataType.VALIDATION_EXTERNAL_TYPE, validationExternalTypes[j]);
			}
		}
	}

	private MValidationExternalType[] processValidationExternalType(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MValidationExternalType[] validationExternalTypes = new MValidationExternalType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationExternalType validationExternalType = buildValidationExternalType();
			setAttributes(validationExternalType, columns, data);
			validationExternalTypes[i] = validationExternalType;
		}
		return validationExternalTypes;
	}

	private void attachValidationExpression(Metadata metadata, Element root) throws MetaParseException {
		List containers = root.getChildren(CONTAINER_VALIDATIONEXPRESSION);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			MValidationExpression[] expressions = processValidationExpression(container);
			for (int j = 0; j < expressions.length; j++) {
				parent.addChild(MetadataType.VALIDATION_EXPRESSION, expressions[j]);
			}
		}
	}

	private MValidationExpression[] processValidationExpression(Element container) {
		String[] columns = getColumns(container);
		List rows = container.getChildren(DATA);
		MValidationExpression[] expressions = new MValidationExpression[rows.size()];
		for (int i = 0; i < expressions.length; i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationExpression expression = buildValidationExpression();
			setAttributes(expression, columns, data);
			expressions[i] = expression;
		}
		return expressions;
	}

}
