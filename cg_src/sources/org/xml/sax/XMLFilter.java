package org.xml.sax;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/XMLFilter.class */
public interface XMLFilter extends XMLReader {
    void setParent(XMLReader xMLReader);

    XMLReader getParent();
}
