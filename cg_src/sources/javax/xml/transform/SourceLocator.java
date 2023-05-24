package javax.xml.transform;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/SourceLocator.class */
public interface SourceLocator {
    String getPublicId();

    String getSystemId();

    int getLineNumber();

    int getColumnNumber();
}
