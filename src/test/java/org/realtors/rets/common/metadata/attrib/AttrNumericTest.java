package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;

public class AttrNumericTest extends AttrTypeTest {
	public void testNumeric() throws Exception {
		AttrType parser = new AttrNumeric();
		assertEquals(Integer.class, parser.getType());
		int[] values = { 1, 100, 99999, 12345, 67890 };
		for (int i = 0; i < values.length; i++) {
			int expected = values[i];
			String input = Integer.toString(expected);
			Object o = parser.parse(input,true);
			int output = ((Integer) o).intValue();
			assertEquals(expected, output);
		}
		assertParseException(parser, "0x99");
		assertParseException(parser, "0AF");
		assertParseException(parser, "0L");
	}
}
