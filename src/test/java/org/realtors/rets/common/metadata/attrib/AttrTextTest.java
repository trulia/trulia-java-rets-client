package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;

public class AttrTextTest extends AttrTypeTest {
	public void testAttrText() throws Exception {
		AttrType parser = new AttrText(0, 10);
		String[] good = { "\r\n\t", "eabc\rdefg", };
		String[] bad = { (char) 7 + "", (char) 1 + "", "12345678901", };

		assertEquals(parser.getType(), String.class);

		for (int i = 0; i < good.length; i++) {
			String s = good[i];
			assertEquals(s, parser.parse(s,true));
		}

		for (int i = 0; i < bad.length; i++) {
			String s = bad[i];
			assertParseException(parser, s);
		}
	}
}
