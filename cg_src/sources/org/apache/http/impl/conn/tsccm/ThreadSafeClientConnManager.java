package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.params.HttpParams;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/tsccm/ThreadSafeClientConnManager.class */
public class ThreadSafeClientConnManager implements ClientConnectionManager {
    private final Log log = LogFactory.getLog(getClass());
    protected final SchemeRegistry schemeRegistry;
    protected final AbstractConnPool connectionPool;
    protected final ClientConnectionOperator connOperator;

    public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        if (schreg == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        }
        this.schemeRegistry = schreg;
        this.connOperator = createConnectionOperator(schreg);
        this.connectionPool = createConnectionPool(params);
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

    protected AbstractConnPool createConnectionPool(HttpParams params) {
        return new ConnPoolByRoute(this.connOperator, params);
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
        final PoolEntryRequest poolRequest = this.connectionPool.requestPoolEntry(route, state);
        return new ClientConnectionRequest() { // from class: org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager.1
            @Override // org.apache.http.conn.ClientConnectionRequest
            public void abortRequest() {
                poolRequest.abortRequest();
            }

            @Override // org.apache.http.conn.ClientConnectionRequest
            public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                if (route != null) {
                    if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
                        ThreadSafeClientConnManager.this.log.debug("ThreadSafeClientConnManager.getConnection: " + route + ", timeout = " + timeout);
                    }
                    BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
                    return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
                }
                throw new IllegalArgumentException("Route may not be null.");
            }
        };
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
        if (!(conn instanceof BasicPooledConnAdapter)) {
            throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
        }
        BasicPooledConnAdapter hca = (BasicPooledConnAdapter) conn;
        if (hca.getPoolEntry() != null && hca.getManager() != this) {
            throw new IllegalArgumentException("Connection not obtained from this manager.");
        }
        synchronized (hca) {
            BasicPoolEntry entry = (BasicPoolEntry) hca.getPoolEntry();
            if (entry == null) {
                return;
            }
            try {
                if (hca.isOpen() && !hca.isMarkedReusable()) {
                    hca.shutdown();
                }
                boolean reusable = hca.isMarkedReusable();
                if (this.log.isDebugEnabled()) {
                    if (reusable) {
                        this.log.debug("Released connection is reusable.");
                    } else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.connectionPool.freeEntry(entry, reusable, validDuration, timeUnit);
            } catch (IOException iox) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Exception shutting down released connection.", iox);
                }
                boolean reusable2 = hca.isMarkedReusable();
                if (this.log.isDebugEnabled()) {
                    if (reusable2) {
                        this.log.debug("Released connection is reusable.");
                    } else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.connectionPool.freeEntry(entry, reusable2, validDuration, timeUnit);
            }
        }
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public void shutdown() {
        this.log.debug("Shutting down");
        this.connectionPool.shutdown();
    }

    public int getConnectionsInPool(HttpRoute route) {
        return ((ConnPoolByRoute) this.connectionPool).getConnectionsInPool(route);
    }

    public int getConnectionsInPool() {
        this.connectionPool.poolLock.lock();
        int count = this.connectionPool.numConnections;
        this.connectionPool.poolLock.unlock();
        return count;
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle for " + idleTimeout + Instruction.argsep + tunit);
        }
        this.connectionPool.closeIdleConnections(idleTimeout, tunit);
        this.connectionPool.deleteClosedConnections();
    }

    @Override // org.apache.http.conn.ClientConnectionManager
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.connectionPool.closeExpiredConnections();
        this.connectionPool.deleteClosedConnections();
    }
}
