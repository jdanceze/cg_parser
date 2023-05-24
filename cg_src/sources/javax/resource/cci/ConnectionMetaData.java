package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/ConnectionMetaData.class */
public interface ConnectionMetaData {
    String getEISProductName() throws ResourceException;

    String getEISProductVersion() throws ResourceException;

    String getUserName() throws ResourceException;
}
