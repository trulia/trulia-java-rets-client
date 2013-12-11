package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrAlphanumTest extends AttrTypeTest {
	@Override
	protected void setUp() throws Exception {
		this.mShort = new AttrAlphanum(1, 10);
		this.mLong = new AttrAlphanum(10, 100);
	}

	public void testAlphanum() throws MetaParseException {
		String test1 = "1234567890";
		String test2 = "abcdefghijklmnopqrstuvwxyz";
		String test3 = test2.toUpperCase();
		// special exceptions for CRT metadata
		String test4 = "123-_ 456";
		this.mShort.parse(test1,true);
		this.mLong.parse(test2,true);
		this.mLong.parse(test3,true);
		this.mShort.parse(test4,true);
	}

	public void testFailures() throws Exception {
		String test1 = "abcdefg%";
		String test2 = "!abcdefg";
		String test3 = "___^___ ";

		assertParseException(this.mShort, test1);
		assertParseException(this.mShort, test2);
		assertParseException(this.mShort, test3);
	}

	public void testLength() throws Exception {
		String test1 = "abcdefghij12345";
		String test2 = "12345";
		assertParseException(this.mShort, test1);
		assertParseException(this.mLong, test2);
	}

	private AttrType mShort;

	private AttrType mLong;
}
