package org.jvnet.fastinfoset.sax;

import org.xml.sax.Attributes;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/EncodingAlgorithmAttributes.class */
public interface EncodingAlgorithmAttributes extends Attributes {
    String getAlgorithmURI(int i);

    int getAlgorithmIndex(int i);

    Object getAlgorithmData(int i);

    String getAlpababet(int i);

    boolean getToIndex(int i);
}
