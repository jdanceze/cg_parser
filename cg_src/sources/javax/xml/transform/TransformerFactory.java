package javax.xml.transform;

import javax.xml.transform.FactoryFinder;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/TransformerFactory.class */
public abstract class TransformerFactory {
    public static TransformerFactory newInstance() throws TransformerFactoryConfigurationError {
        try {
            return (TransformerFactory) FactoryFinder.find("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");
        } catch (FactoryFinder.ConfigurationError e) {
            throw new TransformerFactoryConfigurationError(e.getException(), e.getMessage());
        }
    }

    public abstract Transformer newTransformer(Source source) throws TransformerConfigurationException;

    public abstract Transformer newTransformer() throws TransformerConfigurationException;

    public abstract Templates newTemplates(Source source) throws TransformerConfigurationException;

    public abstract Source getAssociatedStylesheet(Source source, String str, String str2, String str3) throws TransformerConfigurationException;

    public abstract void setURIResolver(URIResolver uRIResolver);

    public abstract URIResolver getURIResolver();

    public abstract boolean getFeature(String str);

    public abstract void setAttribute(String str, Object obj) throws IllegalArgumentException;

    public abstract Object getAttribute(String str) throws IllegalArgumentException;

    public abstract void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException;

    public abstract ErrorListener getErrorListener();
}
