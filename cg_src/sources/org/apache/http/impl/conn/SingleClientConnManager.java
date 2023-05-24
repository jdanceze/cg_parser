package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.resource.spi.work.WorkManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/SingleClientConnManager.class */
public class SingleClientConnManager implements ClientConnectionManager {
    private final Log log = LogFactory.getLog(getClass());
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final SchemeRegistry schemeRegistry;
    protected final ClientConnectionOperator connOperator;
    protected final boolean alwaysShutDown;
    @GuardedBy("this")
    protected PoolEntry uniquePoolEntry;
    @GuardedBy("this")
    protected ConnAdapter managedConn;
    @GuardedBy("this")
    protected long lastReleaseTime;
    @GuardedBy("this")
    protected long connectionExpiresTime;
    protected volatile boolean isShutDown;

    public SingleClientConnManager(HttpParams params, SchemeRegistry schreg) {
        if (schreg == null) {
            throw new IllegalArgumentException("Scheme registry must not be null.");
        }
        this.schemeRegistry = schreg;
        this.connOperator = createConnectionOperator(schreg);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1L;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    protected final void assertStillUp() throws IllegalStateException {
        if (this.isShutDown) {
            throw new IllegalStateException("Manager is shut down.");
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        return new ClientConnectionRequest() { // from class: org.apache.http.impl.conn.SingleClientConnManager.1
            @Override // org.apache.http.conn.ClientConnectionRequest
            public void abortRequest() {
            }

            @Override // org.apache.http.conn.ClientConnectionRequest
            public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
                return SingleClientConnManager.this.getConnection(route, state);
            }
        };
    }

    public synchronized ManagedClientConnection getConnection(HttpRoute route, Object state) {
        if (route == null) {
            throw new IllegalArgumentException("Route may not be null.");
        }
        assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Get connection for route " + route);
        }
        if (this.managedConn != null) {
            throw new IllegalStateException(MISUSE_MESSAGE);
        }
        boolean recreate = false;
        boolean shutdown = false;
        closeExpiredConnections();
        if (this.uniquePoolEntry.connection.isOpen()) {
            RouteTracker tracker = this.uniquePoolEntry.tracker;
            shutdown = tracker == null || !tracker.toRoute().equals(route);
        } else {
            recreate = true;
        }
        if (shutdown) {
            recreate = true;
            try {
                this.uniquePoolEntry.shutdown();
            } catch (IOException iox) {
                this.log.debug("Problem shutting down connection.", iox);
            }
        }
        if (recreate) {
            this.uniquePoolEntry = new PoolEntry();
        }
        this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
        return this.managedConn;
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public synchronized void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
        assertStillUp();
        if (!(conn instanceof ConnAdapter)) {
            throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection " + conn);
        }
        ConnAdapter sca = (ConnAdapter) conn;
        if (sca.poolEntry == null) {
            return;
        }
        ClientConnectionManager manager = sca.getManager();
        if (manager != null && manager != this) {
            throw new IllegalArgumentException("Connection not obtained from this manager.");
        }
        try {
            try {
                if (sca.isOpen() && (this.alwaysShutDown || !sca.isMarkedReusable())) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Released connection open but not reusable.");
                    }
                    sca.shutdown();
                }
                sca.detach();
                this.managedConn = null;
                this.lastReleaseTime = System.currentTimeMillis();
                if (validDuration > 0) {
                    this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
                } else {
                    this.connectionExpiresTime = WorkManager.INDEFINITE;
                }
            } catch (IOException iox) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Exception shutting down released connection.", iox);
                }
                sca.detach();
                this.managedConn = null;
                this.lastReleaseTime = System.currentTimeMillis();
                if (validDuration > 0) {
                    this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
                } else {
                    this.connectionExpiresTime = WorkManager.INDEFINITE;
                }
            }
        } catch (Throwable th) {
            sca.detach();
            this.managedConn = null;
            this.lastReleaseTime = System.currentTimeMillis();
            if (validDuration > 0) {
                this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
            } else {
                this.connectionExpiresTime = WorkManager.INDEFINITE;
            }
            throw th;
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public synchronized void closeExpiredConnections() {
        if (System.currentTimeMillis() >= this.connectionExpiresTime) {
            closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public synchronized void closeIdleConnections(long idletime, TimeUnit tunit) {
        assertStillUp();
        if (tunit == null) {
            throw new IllegalArgumentException("Time unit must not be null.");
        }
        if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
            long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
            if (this.lastReleaseTime <= cutoff) {
                try {
                    this.uniquePoolEntry.close();
                } catch (IOException iox) {
                    this.log.debug("Problem closing idle connection.", iox);
                }
            }
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public synchronized void shutdown() {
        this.isShutDown = true;
        if (this.managedConn != null) {
            this.managedConn.detach();
        }
        try {
            try {
                if (this.uniquePoolEntry != null) {
                    this.uniquePoolEntry.shutdown();
                }
            } catch (IOException iox) {
                this.log.debug("Problem while shutting down manager.", iox);
                this.uniquePoolEntry = null;
            }
        } finally {
            this.uniquePoolEntry = null;
        }
    }

    @Deprecated
    protected synchronized void revokeConnection() {
        if (this.managedConn == null) {
            return;
        }
        this.managedConn.detach();
        try {
            this.uniquePoolEntry.shutdown();
        } catch (IOException iox) {
            this.log.debug("Problem while shutting down connection.", iox);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/SingleClientConnManager$PoolEntry.class */
    public class PoolEntry extends AbstractPoolEntry {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, null);
        }

        protected void close() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }

        protected void shutdown() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter.class */
    public class ConnAdapter extends AbstractPooledConnAdapter {
        protected ConnAdapter(PoolEntry entry, HttpRoute route) {
            super(SingleClientConnManager.this, entry);
            markReusable();
            entry.route = route;
        }
    }
}
