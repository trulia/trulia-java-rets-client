package org.realtors.rets.common.metadata.attrib;

public class AttrDateTest extends AttrTypeTest {
	public void testAttrDate() throws Exception {
		/*
		 * AttrDate uses Strings now, not dates.
		 * bgutierrez Sep. 17, 2012
		 * 
		 * AttrType parser = new AttrDate();

		assertEquals(parser.getType(), Date.class);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.clear();
		cal.set(2003, Calendar.NOVEMBER, 5, 17, 50, 23);
		assertEquals(cal.getTime(), parser.parse("Wed, 5 Nov 2003 17:50:23 GMT",true));

		assertEquals(new Date(0), parser.parse("Thu, 1 Jan 1970 00:00:00 GMT",true));

		assertEquals("wrong day of week but still should parse!", new Date(0), parser.parse("Tue, 1 Jan 1970 00:00:00 GMT",true));

		assertParseException(parser, "2/12/70");
		assertParseException(parser, "12/2/70");
		assertParseException(parser, "2003-1-1");
		// month and date backwards
		assertParseException(parser, "Thu, Jan 1 1970 00:00:00 GMT");*/
	}
}
