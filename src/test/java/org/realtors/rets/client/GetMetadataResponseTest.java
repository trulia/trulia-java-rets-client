package org.realtors.rets.client;

//import java.util.List;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;

public class GetMetadataResponseTest extends RetsTestCase {
	public void testValidSystemMetadataResponse() {
		// GetMetadataResponse response = new GetMetadataResponse(
		// getResource("getMetadataResponse_system.xml"));

		// MetadataSegment[] segments = response.getMetadataSegments();
		// assertEquals(1, segments.length);
		// MetadataSegment segment = segments[0];
		// assertEquals("SYSTEM", segment.getName());
		// assertEquals("01.00.001", segment.getAttribute("Version"));
		// assertEquals("Tue, 27 May 2003 12:00:00 CDT",
		// segment.getAttribute("Date"));
		// assertEquals("CRT_RETS", segment.getSystemId());
		// assertEquals("Center for REALTOR Technology",
		// segment.getSystemDescription());
		// assertEquals("The reference implementation of a RETS Server",
		// segment.getSystemComments());
		// assertNull(segment.getColumns());
		// assertNull(segment.getData());
	}

	public void testSingleSegmentResponse() {
		// GetMetadataResponse response = new GetMetadataResponse(
		// getResource("getMetadataResponse_updateType.xml"));
		//
		// MetadataSegment[] segments = response.getMetadataSegments();
		// assertEquals(1, segments.length);
		// MetadataSegment segment = segments[0];
		// assertEquals("UPDATE_TYPE", segment.getName());
		//
		// assertEquals("ActiveAgent", segment.getAttribute("resource"));
		// assertEquals("ACTAGT", segment.getAttribute("CLASS"));
		// assertEquals("Change_ACTAGT", segment.getAttribute("update"));
		// assertEquals("1.00.000", segment.getAttribute("VERSION"));
		// assertEquals("Sat, 20 Mar 2002 12:03:38 GMT",
		// segment.getAttribute("date"));
		//
		// // Try with the opposite case above to check case-insensitive
		// // comparisons
		// Map attributes = segment.getAttributes();
		// assertEquals(5, attributes.size());
		// assertEquals("ActiveAgent", attributes.get("RESOURCE"));
		// assertEquals("ACTAGT", attributes.get("class"));
		// assertEquals("Change_ACTAGT", attributes.get("UPDATE"));
		// assertEquals("1.00.000", attributes.get("version"));
		// assertEquals("Sat, 20 Mar 2002 12:03:38 GMT",
		// attributes.get("DATE"));
		//
		// assertNull(segment.getSystemId());
		// assertNull(segment.getSystemDescription());
		// assertNull(segment.getSystemComments());
		//
		// List columns = segment.getColumns();
		// assertNotNull("columns not null", columns);
		// assertEquals("columns.size", 8, columns.size());
		//
		// List expectedColumns = new ArrayList();
		// expectedColumns.add("SystemName");
		// expectedColumns.add("Sequence");
		// expectedColumns.add("Attributes");
		// expectedColumns.add("Default");
		// expectedColumns.add("ValidationExpressionID");
		// expectedColumns.add("UpdateHelpID");
		// expectedColumns.add("ValidationLookupName");
		// expectedColumns.add("ValidationExternalName");
		// assertEquals("columns", expectedColumns, columns);
		//
		// List data = segment.getData();
		// assertNotNull(data);
		// assertEquals(2, data.size());
		// Iterator i = data.iterator();
		//
		// assertTrue(i.hasNext());
		// List dataRow = (List) i.next();
		// assertEquals(8, dataRow.size());
		// List expectedDataRow = new ArrayList();
		// expectedDataRow.add("AGENT_ID");
		// expectedDataRow.add("1");
		// expectedDataRow.add("1");
		// expectedDataRow.add("0");
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// assertEquals(expectedDataRow, dataRow);
		//
		// assertTrue(i.hasNext());
		// dataRow = (List) i.next();
		// assertEquals(8, dataRow.size());
		// expectedDataRow = new ArrayList();
		// expectedDataRow.add("OFFICE_ID");
		// expectedDataRow.add("2");
		// expectedDataRow.add("2");
		// expectedDataRow.add("0");
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// expectedDataRow.add(null);
		// assertEquals(expectedDataRow, dataRow);
	}

	public void testMultipleSegmentResponse() {
		// GetMetadataResponse response = new GetMetadataResponse(
		// getResource("getMetadataResponse_lookupZero.xml"));
		//
		// MetadataSegment[] segments = response.getMetadataSegments();
		// assertEquals(2, segments.length);
		//
		// // Check first segment
		// MetadataSegment segment = segments[0];
		// assertEquals("LOOKUP", segment.getName());
		// assertEquals("Property", segment.getAttribute("Resource"));
		// List columns = segment.getColumns();
		// assertNotNull(columns);
		// assertEquals(4, columns.size());
		//
		// List expectedColumns = new ArrayList();
		// expectedColumns.add("LookupName");
		// expectedColumns.add("VisibleName");
		// expectedColumns.add("Version");
		// expectedColumns.add("Date");
		// assertEquals("columns", expectedColumns, columns);
		//
		// List data = segment.getData();
		// assertNotNull(data);
		// assertEquals(9, data.size());
		//
		// // Check second segment
		// segment = segments[1];
		// assertEquals("LOOKUP", segment.getName());
		// assertEquals("Agent", segment.getAttribute("Resource"));
		// columns = segment.getColumns();
		// assertNotNull(columns);
		// assertEquals(4, columns.size());
		// assertEquals("columns", expectedColumns, columns);
		//
		// data = segment.getData();
		// assertNotNull(data);
		// assertEquals(1, data.size());
	}

	public void testNoRecordsMetadataResponse() {
		// GetMetadataResponse response = new GetMetadataResponse(
		//            getResource("getMetadataResponse_noRecords.xml"));
		//
		//        MetadataSegment[] segments = response.getMetadataSegments();
		//        assertEquals(0, segments.length);
	}
}
