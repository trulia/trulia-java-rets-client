package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrVersionTest extends AttrTypeTest {
	@Override
	protected void setUp() throws Exception {
		this.mParser = new AttrVersion();
	}

	public void testAttrVersion() throws Exception {
		assertEquals(this.mParser.getType(), Integer.class);
		assertVersionEquals(10500005, "1.5.5");
		assertVersionEquals(123456789, "12.34.56789");
		assertVersionEquals(0, "0.0.0");
		assertParseException("1.1.1.1");
		assertParseException("1.1");
		assertParseException("123456789");
	}

	private void assertParseException(String input) throws Exception {
		assertParseException(this.mParser, input);
	}

	private void assertVersionEquals(int expected, String input) throws MetaParseException {
		Integer i = (Integer) this.mParser.parse(input,true);
		assertEquals(expected, i.intValue());
	}

	private AttrType mParser;
}
