package javax.resource.cci;

import java.io.Serializable;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/ConnectionFactory.class */
public interface ConnectionFactory extends Serializable, Referenceable {
    Connection getConnection() throws ResourceException;

    Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException;

    RecordFactory getRecordFactory() throws ResourceException;

    ResourceAdapterMetaData getMetaData() throws ResourceException;
}
