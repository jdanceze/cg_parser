package org.xml.sax.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/XMLReaderFactory.class */
public final class XMLReaderFactory {
    private static final String property = "org.xml.sax.driver";
    static Class class$org$xml$sax$helpers$XMLReaderFactory;

    private XMLReaderFactory() {
    }

    public static XMLReader createXMLReader() throws SAXException {
        Class cls;
        InputStream resourceAsStream;
        BufferedReader bufferedReader;
        Class cls2;
        String str = null;
        SecuritySupport securitySupport = SecuritySupport.getInstance();
        ClassLoader classLoader = NewInstance.getClassLoader();
        try {
            str = securitySupport.getSystemProperty(property);
        } catch (Exception e) {
        }
        if (str == null) {
            ClassLoader contextClassLoader = securitySupport.getContextClassLoader();
            if (contextClassLoader != null) {
                resourceAsStream = securitySupport.getResourceAsStream(contextClassLoader, "META-INF/services/org.xml.sax.driver");
                if (resourceAsStream == null) {
                    if (class$org$xml$sax$helpers$XMLReaderFactory == null) {
                        cls2 = class$("org.xml.sax.helpers.XMLReaderFactory");
                        class$org$xml$sax$helpers$XMLReaderFactory = cls2;
                    } else {
                        cls2 = class$org$xml$sax$helpers$XMLReaderFactory;
                    }
                    resourceAsStream = securitySupport.getResourceAsStream(cls2.getClassLoader(), "META-INF/services/org.xml.sax.driver");
                }
            } else {
                if (class$org$xml$sax$helpers$XMLReaderFactory == null) {
                    cls = class$("org.xml.sax.helpers.XMLReaderFactory");
                    class$org$xml$sax$helpers$XMLReaderFactory = cls;
                } else {
                    cls = class$org$xml$sax$helpers$XMLReaderFactory;
                }
                resourceAsStream = securitySupport.getResourceAsStream(cls.getClassLoader(), "META-INF/services/org.xml.sax.driver");
            }
            if (resourceAsStream != null) {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
                } catch (UnsupportedEncodingException e2) {
                    bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                }
                try {
                    str = bufferedReader.readLine();
                    bufferedReader.close();
                } catch (Exception e3) {
                }
            }
        }
        if (str == null) {
            str = "org.apache.xerces.parsers.SAXParser";
        }
        if (str != null) {
            return loadClass(classLoader, str);
        }
        try {
            return new ParserAdapter(ParserFactory.makeParser());
        } catch (Exception e4) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
        }
    }

    public static XMLReader createXMLReader(String str) throws SAXException {
        return loadClass(NewInstance.getClassLoader(), str);
    }

    private static XMLReader loadClass(ClassLoader classLoader, String str) throws SAXException {
        try {
            return (XMLReader) NewInstance.newInstance(classLoader, str);
        } catch (ClassCastException e) {
            throw new SAXException(new StringBuffer().append("SAX2 driver class ").append(str).append(" does not implement XMLReader").toString(), e);
        } catch (ClassNotFoundException e2) {
            throw new SAXException(new StringBuffer().append("SAX2 driver class ").append(str).append(" not found").toString(), e2);
        } catch (IllegalAccessException e3) {
            throw new SAXException(new StringBuffer().append("SAX2 driver class ").append(str).append(" found but cannot be loaded").toString(), e3);
        } catch (InstantiationException e4) {
            throw new SAXException(new StringBuffer().append("SAX2 driver class ").append(str).append(" loaded but cannot be instantiated (no empty public constructor?)").toString(), e4);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
