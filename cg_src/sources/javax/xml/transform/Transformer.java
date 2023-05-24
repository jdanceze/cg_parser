package javax.xml.transform;

import java.util.Properties;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/Transformer.class */
public abstract class Transformer {
    protected Transformer() {
    }

    public abstract void transform(Source source, Result result) throws TransformerException;

    public abstract void setParameter(String str, Object obj);

    public abstract Object getParameter(String str);

    public abstract void clearParameters();

    public abstract void setURIResolver(URIResolver uRIResolver);

    public abstract URIResolver getURIResolver();

    public abstract void setOutputProperties(Properties properties) throws IllegalArgumentException;

    public abstract Properties getOutputProperties();

    public abstract void setOutputProperty(String str, String str2) throws IllegalArgumentException;

    public abstract String getOutputProperty(String str) throws IllegalArgumentException;

    public abstract void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException;

    public abstract ErrorListener getErrorListener();
}
