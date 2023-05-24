package javax.xml.registry.infomodel;

import java.util.Collection;
import java.util.Locale;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/InternationalString.class */
public interface InternationalString {
    String getValue() throws JAXRException;

    String getValue(Locale locale) throws JAXRException;

    void setValue(String str) throws JAXRException;

    void setValue(Locale locale, String str) throws JAXRException;

    void addLocalizedString(LocalizedString localizedString) throws JAXRException;

    void addLocalizedStrings(Collection collection) throws JAXRException;

    void removeLocalizedString(LocalizedString localizedString) throws JAXRException;

    void removeLocalizedStrings(Collection collection) throws JAXRException;

    LocalizedString getLocalizedString(Locale locale, String str) throws JAXRException;

    Collection getLocalizedStrings() throws JAXRException;
}
