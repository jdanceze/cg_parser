package org.xml.sax;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/AttributeList.class */
public interface AttributeList {
    int getLength();

    String getName(int i);

    String getType(int i);

    String getValue(int i);

    String getType(String str);

    String getValue(String str);
}
