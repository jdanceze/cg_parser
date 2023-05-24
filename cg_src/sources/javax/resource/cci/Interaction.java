package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/Interaction.class */
public interface Interaction {
    void close() throws ResourceException;

    Connection getConnection();

    boolean execute(InteractionSpec interactionSpec, Record record, Record record2) throws ResourceException;

    Record execute(InteractionSpec interactionSpec, Record record) throws ResourceException;

    ResourceWarning getWarnings() throws ResourceException;

    void clearWarnings() throws ResourceException;
}
