package org.realtors.rets.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class GetObjectResponseIteratorTest extends TestCase {
	private static final String BOUNDARY = "jack";

	private static final String BINARY_BLOB_1 = "1)dcg fa8 5 uiwjskdgsdfkg hdsfa bdf" + "  erkfjhwfewuhuh"
			+ "B\r\n\r\n";

	private static final String BINARY_BLOB_2 = "2)dcg fAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" + "\tAAAAAAAAAAAAhdsfa bdf";

	private static final String BINARY_BLOB_3 = "3)dcg fAAAAAAAAAAAAAAAAA\t\\!"
			+ "\r\n\r\nAAAAAAAAAAAAAAAAAAAAAAAAAhdsfa bdf";

	private static final String BINARY_BLOB_4 = "fgsdgsdfg";

	private static final String BINARY_BLOB_5 = "4)dcg fAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAhdsfa bdf";

	public static final byte[] MULTIPART_RESPONSE_BODY = ("--" + BOUNDARY + "\r\n" + "Content-Type: text\r\n"
			+ "Content-ID: one\r\n" + "Object-ID: 1\r\n" + "\r\n" + BINARY_BLOB_1 + "\r\n--" + BOUNDARY + "\r\n"
			+ "Content-Type: gray-matter\r\n" + "Content-ID: two\r\n" + "Object-ID: 2\r\n" + "\r\n" + BINARY_BLOB_2
			+ "\r\n--" + BOUNDARY + "\r\n" + "Content-Type: blue-matter\r\n" + "Content-ID: three\r\n"
			+ "Object-ID: 3\r\n" + "\r\n" + BINARY_BLOB_3 + "\r\n--" + BOUNDARY + "\r\n"
			+ "Content-Type: green-matter\r\n" + "Content-ID: four\r\n" + "Object-ID: 4\r\n" + "\r\n" + BINARY_BLOB_4
			+ "\r\n--" + BOUNDARY + "\r\n" + "Content-Type: yellow-matter-custard\r\n" + "Content-ID: five\r\n"
			+ "Object-ID: 5\r\n" + "\r\n" + BINARY_BLOB_5 + "\r\n--" + BOUNDARY + "--").getBytes();

	public void testIterationMultipart() throws Exception {
		GetObjectIterator<SingleObjectResponse> getObjectIterator = null;

		Map headers = new HashMap();
		headers.put("Content-Type", "multipart/parallel; boundary=\"" + BOUNDARY + "\"");
		headers.put("MIME-Version", "1.0");
		GetObjectResponse getObjectResponse = new GetObjectResponse(headers, new ByteArrayInputStream(MULTIPART_RESPONSE_BODY));
		getObjectIterator = getObjectResponse.iterator();

		SingleObjectResponse firstResponse = getObjectIterator.next();
		assertEquals("text", firstResponse.getType());
		assertEquals("one", firstResponse.getContentID());
		assertEquals("1", firstResponse.getObjectID());
		assertEquals(BINARY_BLOB_1, new String(readOut(firstResponse.getInputStream(), 1024)));

		assertTrue(getObjectIterator.hasNext());
		assertTrue(getObjectIterator.hasNext());

		SingleObjectResponse secondResponse = getObjectIterator.next();
		assertEquals("gray-matter", secondResponse.getType());
		assertEquals("two", secondResponse.getContentID());
		assertEquals("2", secondResponse.getObjectID());
		assertEquals(BINARY_BLOB_2, new String(readOut(secondResponse.getInputStream(), 1024)));

		getObjectIterator.next();
		getObjectIterator.next();
		getObjectIterator.next();

		assertFalse(getObjectIterator.hasNext());
		assertFalse(getObjectIterator.hasNext());
	}

	public void testIterationNonMultipart() throws Exception {
		GetObjectIterator<SingleObjectResponse> getObjectIterator = null;

		Map headers = new HashMap();
		headers.put("Content-Type", "image/jpeg");
		headers.put("MIME-Version", "1.0");
		headers.put("Content-ID", "one");
		headers.put("Object-ID", "1");
		GetObjectResponse getObjectResponse = new GetObjectResponse(headers, new ByteArrayInputStream(BINARY_BLOB_1
				.getBytes()));

		getObjectIterator = getObjectResponse.iterator();

		assertTrue(getObjectIterator.hasNext());
		assertTrue(getObjectIterator.hasNext());

		SingleObjectResponse firstResponse = getObjectIterator.next();

		assertEquals("image/jpeg", firstResponse.getType());
		assertEquals("one", firstResponse.getContentID());
		assertEquals("1", firstResponse.getObjectID());
		assertEquals(BINARY_BLOB_1, new String(readOut(firstResponse.getInputStream(), 1024)));

		assertFalse(getObjectIterator.hasNext());
		assertFalse(getObjectIterator.hasNext());
	}

	/*
	 * TODO: Fix these tests.
	 * 
	public void testMissingObjects() throws Exception {
		Map headers = new HashMap();
		String BUG_BOUNDARY = "50eb24a2.9354.35f3.be11.9cea9411a260";
		headers.put("Content-Type", "mutipart/parallel; boundary=\"" + BUG_BOUNDARY + "\"");
		headers.put("MIME-Version", "1.0");

		InputStream BUG_MULTIPART_RESPONSE_BODY = this.getClass().getResourceAsStream("objects-missing.multipart");

		GetObjectResponse getObjectResponse = new GetObjectResponse(headers, BUG_MULTIPART_RESPONSE_BODY);
		GetObjectIterator<SingleObjectResponse> bugObjectIterator = GetObjectResponseIterator.createIterator(getObjectResponse, 10000);

		String[] expectedContentIds = new String[] { "111285", "10037", "100084", "13710", "58946", };
		for (int i = 0; bugObjectIterator.hasNext(); i++) {
			SingleObjectResponse objectResponse = bugObjectIterator.next();
			String contentID = objectResponse.getContentID();
			assertEquals(expectedContentIds[i], contentID);
		}
	}

	public void testParsingObjects() throws Exception {
		Map headers = new HashMap();
		String BUG_BOUNDARY = "simple boundary";
		headers.put("Content-Type", "mutipart/parallel; boundary=\"" + BUG_BOUNDARY + "\"");
		headers.put("MIME-Version", "1.0");

		InputStream BUG_MULTIPART_RESPONSE_BODY = this.getClass().getResourceAsStream("2237858_0.jpg");

		GetObjectResponse getObjectResponse = new GetObjectResponse(headers, BUG_MULTIPART_RESPONSE_BODY);
		GetObjectIterator<SingleObjectResponse> bugObjectIterator = GetObjectResponseIterator.createIterator(getObjectResponse, 10000);

		String[] expectedContentIds = new String[] { "2237858", "2237858", "2237858", "2237858", "2236185", "2236185",
				"2236185", "2236185", "2236210", "2236210", "2236210", "2236210", "2236210" };
		String[] expectedObjectIds = new String[] { "1", "2", "3", "4", "0", "1", "2", "3", "0", "1", "2", "3", "4", };
		int i = 0;
		for (; bugObjectIterator.hasNext(); i++) {
			SingleObjectResponse objectResponse = bugObjectIterator.next();

			String contentID = objectResponse.getContentID();
			assertEquals(expectedContentIds[i], contentID);

			String objectID = objectResponse.getObjectID();
			assertEquals(expectedObjectIds[i], objectID);

			File tmp = File.createTempFile("embedded-image.", "." + contentID + "_" + objectID + ".jpg");
			byte[] image = GetObjectResponseIteratorTest.readOut(objectResponse.getInputStream(), 10000);

			FileOutputStream imageOut = new FileOutputStream(tmp);
			try {
				imageOut.write(image);
			} finally {
				imageOut.close();
			}
			System.out.println("embedded image extracted to: " + tmp);
		}
		assertEquals("Objects were swallowed.", expectedContentIds.length, i);
	}
	*/

	/**
	 * Some RETS servers send headers like "Content-type"
	 */
	public void testCaseInsensitiveHeaders() throws Exception {
		GetObjectIterator<SingleObjectResponse> getObjectIterator = null;

		Map headers = new HashMap();
		headers.put("Content-type", "image/jpeg");
		headers.put("MIME-version", "1.0");
		headers.put("content-id", "one");
		headers.put("Object-id", "1");
		GetObjectResponse getObjectResponse = new GetObjectResponse(headers, new ByteArrayInputStream(BINARY_BLOB_1.getBytes()));

		getObjectIterator = getObjectResponse.iterator();

		assertTrue(getObjectIterator.hasNext());
		SingleObjectResponse firstResponse = getObjectIterator.next();

		assertEquals("image/jpeg", firstResponse.getType());
		assertEquals("one", firstResponse.getContentID());
		assertEquals("1", firstResponse.getObjectID());
		assertEquals(BINARY_BLOB_1, new String(readOut(firstResponse.getInputStream(), 1024)));
	}

	public static byte[] readOut(InputStream in, int bufferSize) throws IOException {
		byte[] temp = new byte[bufferSize];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		do {
			bufferSize = in.read(temp, 0, bufferSize);
			if (bufferSize > 0)
				baos.write(temp, 0, bufferSize);
		} while (bufferSize != -1);
		return baos.toByteArray();
	}

}