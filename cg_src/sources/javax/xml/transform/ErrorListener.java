package javax.xml.transform;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/ErrorListener.class */
public interface ErrorListener {
    void warning(TransformerException transformerException) throws TransformerException;

    void error(TransformerException transformerException) throws TransformerException;

    void fatalError(TransformerException transformerException) throws TransformerException;
}
