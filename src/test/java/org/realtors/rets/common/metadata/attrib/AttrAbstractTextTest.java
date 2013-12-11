package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrAbstractTextTest extends AttrTypeTest {
	@Override
	protected void setUp() {
		this.mShort = new AttrAbstractText(1, 10) {
			@Override
			protected void checkContent(String value) throws MetaParseException {
				return;
			}
		};
		this.mLong = new AttrAbstractText(10, 20) {
			@Override
			protected void checkContent(String value) throws MetaParseException {
				return;
			}
		};
	}

	public void testAttrAbstractText() throws MetaParseException {
		assertEquals("type is not String", String.class, this.mShort.getType());
		String test = "short";
		assertEquals("return object not identical", this.mShort.parse(test,true), test);
	}

	public void testTooLong() throws Exception {
		assertParseException(this.mShort, "way too long");
	}

	public void testTooShort() throws Exception {
		assertParseException(this.mLong, "short");
	}

	AttrType mShort, mLong;
}
