package org.xml.sax.helpers;

import org.xml.sax.Parser;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/ParserFactory.class */
public class ParserFactory {
    private ParserFactory() {
    }

    public static Parser makeParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException {
        String systemProperty = SecuritySupport.getInstance().getSystemProperty("org.xml.sax.parser");
        if (systemProperty == null) {
            throw new NullPointerException("No value for sax.parser property");
        }
        return makeParser(systemProperty);
    }

    public static Parser makeParser(String str) throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {
        return (Parser) NewInstance.newInstance(NewInstance.getClassLoader(), str);
    }
}
