package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/RecordFactory.class */
public interface RecordFactory {
    MappedRecord createMappedRecord(String str) throws ResourceException;

    IndexedRecord createIndexedRecord(String str) throws ResourceException;
}
