package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrBooleanTest
    extends AttrTypeTest
{
    public void testBoolean() throws Exception
    {
        String[] trues = {"true", "1", "TrUe", "Y"};
        String[] falses = {"false", "FALSE","0", "", "N"};
        String[] exceptions = {"weird", "#(*&", "2", "falze"};


        AttrType parser = new AttrBoolean();
        assertEquals("Wrong Class returned", Boolean.class, parser.getType());
        for (int i = 0; i < trues.length; i++)
        {
            String input = trues[i];
            boolean value = ((Boolean)parser.parse(input,true)).booleanValue();
            assertTrue("Expected true return for " + input, value);
        }
        for (int i = 0; i < falses.length; i++)
        {
            String input = falses[i];
            boolean value = ((Boolean)parser.parse(input,true)).booleanValue();
            assertFalse("Expected false return for " + input, value);

        }
        for (int i = 0; i < exceptions.length; i++)
        {
            String input = exceptions[i];
            assertParseException(parser, input);
        }
    }

    public void testBooleanOutput() throws MetaParseException
    {
        AttrBoolean parser = new AttrBoolean();
        Boolean output = parser.parse("true",true);
        assertEquals("1", parser.render(output));
        output = parser.parse("false",true);
        assertEquals("0", parser.render(output));
    }
}
