package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;

public class AttrPlaintextTest extends AttrTypeTest {
	public void testPlaintext() throws Exception {
		AttrType parser = new AttrPlaintext(0, 10);

		assertEquals(String.class, parser.getType());
		String[] good = { "%17a", "!%@$", "90785", ")!(*%! ", "" };
		String[] bad = { "\r\n", "\t", new String(new char[] { (char) 7 }) };

		for (int i = 0; i < good.length; i++) {
			String s = good[i];
			assertEquals(s, parser.parse(s,true));
		}

		for (int i = 0; i < bad.length; i++) {
			String s = bad[i];
			assertParseException(parser, s);
		}

		AttrType parser2 = new AttrPlaintext(10, 20);
		assertParseException(parser2, "1");
		assertParseException(parser2, "123456789012345678901");
	}
}
