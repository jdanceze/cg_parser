package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/MessageListener.class */
public interface MessageListener {
    Record onMessage(Record record) throws ResourceException;
}
