package javax.xml.registry.infomodel;

import java.util.Locale;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/LocalizedString.class */
public interface LocalizedString {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    String getCharsetName() throws JAXRException;

    Locale getLocale() throws JAXRException;

    String getValue() throws JAXRException;

    void setCharsetName(String str) throws JAXRException;

    void setLocale(Locale locale) throws JAXRException;

    void setValue(String str) throws JAXRException;
}
