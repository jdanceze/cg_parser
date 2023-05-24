package org.jvnet.fastinfoset;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/FastInfosetParser.class */
public interface FastInfosetParser {
    public static final String STRING_INTERNING_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/string-interning";
    public static final String BUFFER_SIZE_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/buffer-size";
    public static final String REGISTERED_ENCODING_ALGORITHMS_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms";
    public static final String EXTERNAL_VOCABULARIES_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/external-vocabularies";
    public static final String FORCE_STREAM_CLOSE_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/force-stream-close";

    void setStringInterning(boolean z);

    boolean getStringInterning();

    void setBufferSize(int i);

    int getBufferSize();

    void setRegisteredEncodingAlgorithms(Map map);

    Map getRegisteredEncodingAlgorithms();

    void setExternalVocabularies(Map map);

    Map getExternalVocabularies();

    void setParseFragments(boolean z);

    boolean getParseFragments();

    void setForceStreamClose(boolean z);

    boolean getForceStreamClose();
}
