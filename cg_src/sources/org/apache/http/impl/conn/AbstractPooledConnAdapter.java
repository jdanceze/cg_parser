package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/AbstractPooledConnAdapter.class */
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {
    protected volatile AbstractPoolEntry poolEntry;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractPooledConnAdapter(ClientConnectionManager manager, AbstractPoolEntry entry) {
        super(manager, entry.connection);
        this.poolEntry = entry;
    }

    @Deprecated
    protected final void assertAttached() {
        if (this.poolEntry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.impl.conn.AbstractClientConnAdapter
    public synchronized void detach() {
        super.detach();
        this.poolEntry = null;
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public HttpRoute getRoute() {
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        if (entry.tracker == null) {
            return null;
        }
        return entry.tracker.toRoute();
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
        assertNotAborted();
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        entry.open(route, context, params);
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
        assertNotAborted();
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        entry.tunnelTarget(secure, params);
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
        assertNotAborted();
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        entry.tunnelProxy(next, secure, params);
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
        assertNotAborted();
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        entry.layerProtocol(context, params);
    }

    @Override // org.apache.http.HttpConnection
    public void close() throws IOException {
        AbstractPoolEntry entry = this.poolEntry;
        if (entry != null) {
            entry.shutdownEntry();
        }
        OperatedClientConnection conn = getWrappedConnection();
        if (conn != null) {
            conn.close();
        }
    }

    @Override // org.apache.http.HttpConnection
    public void shutdown() throws IOException {
        AbstractPoolEntry entry = this.poolEntry;
        if (entry != null) {
            entry.shutdownEntry();
        }
        OperatedClientConnection conn = getWrappedConnection();
        if (conn != null) {
            conn.shutdown();
        }
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public Object getState() {
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        return entry.getState();
    }

    @Override // org.apache.http.conn.ManagedClientConnection
    public void setState(Object state) {
        AbstractPoolEntry entry = this.poolEntry;
        if (entry == null) {
            throw new IllegalStateException("Adapter is detached.");
        }
        entry.setState(state);
    }
}
