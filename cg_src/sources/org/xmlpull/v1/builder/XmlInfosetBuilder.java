package org.xmlpull.v1.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.builder.impl.XmlInfosetBuilderImpl;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlInfosetBuilder.class */
public abstract class XmlInfosetBuilder {
    protected XmlPullParserFactory factory;

    public abstract XmlDocument newDocument(String str, Boolean bool, String str2) throws XmlBuilderException;

    public abstract XmlElement newFragment(String str) throws XmlBuilderException;

    public abstract XmlElement newFragment(String str, String str2) throws XmlBuilderException;

    public abstract XmlElement newFragment(XmlNamespace xmlNamespace, String str) throws XmlBuilderException;

    public abstract XmlNamespace newNamespace(String str) throws XmlBuilderException;

    public abstract XmlNamespace newNamespace(String str, String str2) throws XmlBuilderException;

    public abstract XmlDocument parse(XmlPullParser xmlPullParser) throws XmlBuilderException;

    public abstract Object parseItem(XmlPullParser xmlPullParser) throws XmlBuilderException;

    public abstract XmlElement parseStartTag(XmlPullParser xmlPullParser) throws XmlBuilderException;

    public abstract XmlDocument parseLocation(String str) throws XmlBuilderException;

    public abstract XmlElement parseFragment(XmlPullParser xmlPullParser) throws XmlBuilderException;

    public abstract void serializeStartTag(XmlElement xmlElement, XmlSerializer xmlSerializer) throws XmlBuilderException;

    public abstract void serializeEndTag(XmlElement xmlElement, XmlSerializer xmlSerializer) throws XmlBuilderException;

    public abstract void serialize(Object obj, XmlSerializer xmlSerializer) throws XmlBuilderException;

    public abstract void serializeItem(Object obj, XmlSerializer xmlSerializer) throws XmlBuilderException;

    public static XmlInfosetBuilder newInstance() throws XmlBuilderException {
        XmlInfosetBuilder impl = new XmlInfosetBuilderImpl();
        try {
            impl.factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
            impl.factory.setNamespaceAware(true);
            return impl;
        } catch (XmlPullParserException ex) {
            throw new XmlBuilderException(new StringBuffer().append("could not create XmlPull factory:").append(ex).toString(), ex);
        }
    }

    public static XmlInfosetBuilder newInstance(XmlPullParserFactory factory) throws XmlBuilderException {
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        XmlInfosetBuilder impl = new XmlInfosetBuilderImpl();
        impl.factory = factory;
        impl.factory.setNamespaceAware(true);
        return impl;
    }

    public XmlPullParserFactory getFactory() throws XmlBuilderException {
        return this.factory;
    }

    public XmlDocument newDocument() throws XmlBuilderException {
        return newDocument(null, null, null);
    }

    public XmlDocument parseInputStream(InputStream is) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(is, null);
            return parse(pp);
        } catch (XmlPullParserException e) {
            throw new XmlBuilderException("could not start parsing input stream", e);
        }
    }

    public XmlDocument parseInputStream(InputStream is, String encoding) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(is, encoding);
            return parse(pp);
        } catch (XmlPullParserException e) {
            throw new XmlBuilderException(new StringBuffer().append("could not start parsing input stream (encoding=").append(encoding).append(")").toString(), e);
        }
    }

    public XmlDocument parseReader(Reader reader) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(reader);
            return parse(pp);
        } catch (XmlPullParserException e) {
            throw new XmlBuilderException("could not start parsing input from reader", e);
        }
    }

    public XmlElement parseFragmentFromInputStream(InputStream is) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(is, null);
            try {
                pp.nextTag();
                return parseFragment(pp);
            } catch (IOException e) {
                throw new XmlBuilderException("IO error when starting to parse input stream", e);
            }
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException("could not start parsing input stream", e2);
        }
    }

    public XmlElement parseFragementFromInputStream(InputStream is, String encoding) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(is, encoding);
            try {
                pp.nextTag();
                return parseFragment(pp);
            } catch (IOException e) {
                throw new XmlBuilderException(new StringBuffer().append("IO error when starting to parse input stream (encoding=").append(encoding).append(")").toString(), e);
            }
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException(new StringBuffer().append("could not start parsing input stream (encoding=").append(encoding).append(")").toString(), e2);
        }
    }

    public XmlElement parseFragmentFromReader(Reader reader) throws XmlBuilderException {
        try {
            XmlPullParser pp = this.factory.newPullParser();
            pp.setInput(reader);
            try {
                pp.nextTag();
                return parseFragment(pp);
            } catch (IOException e) {
                throw new XmlBuilderException("IO error when starting to parse from reader", e);
            }
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException("could not start parsing input from reader", e2);
        }
    }

    public void skipSubTree(XmlPullParser pp) throws XmlBuilderException {
        try {
            pp.require(2, null, null);
            int level = 1;
            while (level > 0) {
                int eventType = pp.next();
                if (eventType == 3) {
                    level--;
                } else if (eventType == 2) {
                    level++;
                }
            }
        } catch (IOException e) {
            throw new XmlBuilderException("IO error when skipping subtree", e);
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException("could not skip subtree", e2);
        }
    }

    public void serializeToOutputStream(Object item, OutputStream os) throws XmlBuilderException {
        serializeToOutputStream(item, os, "UTF8");
    }

    public void serializeToOutputStream(Object item, OutputStream os, String encoding) throws XmlBuilderException {
        try {
            XmlSerializer ser = this.factory.newSerializer();
            ser.setOutput(os, encoding);
            serialize(item, ser);
            try {
                ser.flush();
            } catch (IOException e) {
                throw new XmlBuilderException("could not flush output", e);
            }
        } catch (Exception e2) {
            throw new XmlBuilderException(new StringBuffer().append("could not serialize node to output stream (encoding=").append(encoding).append(")").toString(), e2);
        }
    }

    public void serializeToWriter(Object item, Writer writer) throws XmlBuilderException {
        try {
            XmlSerializer ser = this.factory.newSerializer();
            ser.setOutput(writer);
            serialize(item, ser);
            try {
                ser.flush();
            } catch (IOException e) {
                throw new XmlBuilderException("could not flush output", e);
            }
        } catch (Exception e2) {
            throw new XmlBuilderException("could not serialize node to writer", e2);
        }
    }

    public String serializeToString(Object item) throws XmlBuilderException {
        StringWriter sw = new StringWriter();
        serializeToWriter(item, sw);
        return sw.toString();
    }
}
