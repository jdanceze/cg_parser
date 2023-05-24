package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Versionable.class */
public interface Versionable {
    int getMajorVersion() throws JAXRException;

    void setMajorVersion(int i) throws JAXRException;

    int getMinorVersion() throws JAXRException;

    void setMinorVersion(int i) throws JAXRException;

    String getUserVersion() throws JAXRException;

    void setUserVersion(String str) throws JAXRException;
}
