package javax.xml.transform;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/TransformerConfigurationException.class */
public class TransformerConfigurationException extends TransformerException {
    public TransformerConfigurationException() {
        super("Configuration Error");
    }

    public TransformerConfigurationException(String str) {
        super(str);
    }

    public TransformerConfigurationException(Throwable th) {
        super(th);
    }

    public TransformerConfigurationException(String str, Throwable th) {
        super(str, th);
    }

    public TransformerConfigurationException(String str, SourceLocator sourceLocator) {
        super(str, sourceLocator);
    }

    public TransformerConfigurationException(String str, SourceLocator sourceLocator, Throwable th) {
        super(str, sourceLocator, th);
    }
}
