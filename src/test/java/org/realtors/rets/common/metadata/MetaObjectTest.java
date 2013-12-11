package org.realtors.rets.common.metadata;

public class MetaObjectTest extends MetadataTestCase {
	public void testStrictAttributes() {
		MetaObject.clearAttributeMapCache();
		TestMetaObject metaObject = createTestMetaObject(MetaObject.STRICT_PARSING);
		assertEquals("SomeName", metaObject.getSystemName());
		assertEquals("Foo Bar", metaObject.getString1());
		assertEquals("somename", metaObject.getAttributeAsString("systemname"));
		assertEquals("foo bar", metaObject.getAttributeAsString("string1"));
	}

	public void testLooseAttributes() {
		MetaObject.clearAttributeMapCache();
		TestMetaObject metaObject = createTestMetaObject(MetaObject.LOOSE_PARSING);
		assertEquals("somename", metaObject.getSystemName());
		assertEquals("foo bar", metaObject.getString1());
	}

	public void testCache() {
		TestMetaObject.resetAddAttributeCount();
		MetaObject.clearAttributeMapCache();
		createTestMetaObject(MetaObject.STRICT_PARSING);
		createTestMetaObject(MetaObject.LOOSE_PARSING);
		createTestMetaObject(MetaObject.STRICT_PARSING);
		createTestMetaObject(MetaObject.LOOSE_PARSING);
		assertEquals(2, TestMetaObject.getAddAttributeCount());
		MetaObject.clearAttributeMapCache();
		createTestMetaObject(MetaObject.STRICT_PARSING);
		createTestMetaObject(MetaObject.LOOSE_PARSING);
		createTestMetaObject(MetaObject.STRICT_PARSING);
		createTestMetaObject(MetaObject.LOOSE_PARSING);
		assertEquals(4, TestMetaObject.getAddAttributeCount());
	}

	private TestMetaObject createTestMetaObject(boolean strictParsing) {
		TestMetaObject metaObject = new TestMetaObject(strictParsing);
		metaObject.setAttribute("SystemName", "SomeName");
		metaObject.setAttribute("systemname", "somename");
		metaObject.setAttribute("String1", "Foo Bar");
		metaObject.setAttribute("string1", "foo bar");
		return metaObject;
	}
}
