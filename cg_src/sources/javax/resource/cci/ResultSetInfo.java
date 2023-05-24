package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/ResultSetInfo.class */
public interface ResultSetInfo {
    boolean updatesAreDetected(int i) throws ResourceException;

    boolean insertsAreDetected(int i) throws ResourceException;

    boolean deletesAreDetected(int i) throws ResourceException;

    boolean supportsResultSetType(int i) throws ResourceException;

    boolean supportsResultTypeConcurrency(int i, int i2) throws ResourceException;

    boolean othersUpdatesAreVisible(int i) throws ResourceException;

    boolean othersDeletesAreVisible(int i) throws ResourceException;

    boolean othersInsertsAreVisible(int i) throws ResourceException;

    boolean ownUpdatesAreVisible(int i) throws ResourceException;

    boolean ownInsertsAreVisible(int i) throws ResourceException;

    boolean ownDeletesAreVisible(int i) throws ResourceException;
}
