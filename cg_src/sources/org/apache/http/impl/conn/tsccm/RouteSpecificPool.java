package org.apache.http.impl.conn.tsccm;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/tsccm/RouteSpecificPool.class */
public class RouteSpecificPool {
    protected final HttpRoute route;
    protected final int maxEntries;
    private final Log log = LogFactory.getLog(getClass());
    protected final LinkedList<BasicPoolEntry> freeEntries = new LinkedList<>();
    protected final Queue<WaitingThread> waitingThreads = new LinkedList();
    protected int numEntries = 0;

    public RouteSpecificPool(HttpRoute route, int maxEntries) {
        this.route = route;
        this.maxEntries = maxEntries;
    }

    public final HttpRoute getRoute() {
        return this.route;
    }

    public final int getMaxEntries() {
        return this.maxEntries;
    }

    public boolean isUnused() {
        return this.numEntries < 1 && this.waitingThreads.isEmpty();
    }

    public int getCapacity() {
        return this.maxEntries - this.numEntries;
    }

    public final int getEntryCount() {
        return this.numEntries;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public org.apache.http.impl.conn.tsccm.BasicPoolEntry allocEntry(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r4
            java.util.LinkedList<org.apache.http.impl.conn.tsccm.BasicPoolEntry> r0 = r0.freeEntries
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L49
            r0 = r4
            java.util.LinkedList<org.apache.http.impl.conn.tsccm.BasicPoolEntry> r0 = r0.freeEntries
            r1 = r4
            java.util.LinkedList<org.apache.http.impl.conn.tsccm.BasicPoolEntry> r1 = r1.freeEntries
            int r1 = r1.size()
            java.util.ListIterator r0 = r0.listIterator(r1)
            r6 = r0
        L19:
            r0 = r6
            boolean r0 = r0.hasPrevious()
            if (r0 == 0) goto L49
            r0 = r6
            java.lang.Object r0 = r0.previous()
            org.apache.http.impl.conn.tsccm.BasicPoolEntry r0 = (org.apache.http.impl.conn.tsccm.BasicPoolEntry) r0
            r7 = r0
            r0 = r7
            java.lang.Object r0 = r0.getState()
            if (r0 == 0) goto L3e
            r0 = r5
            r1 = r7
            java.lang.Object r1 = r1.getState()
            boolean r0 = org.apache.http.util.LangUtils.equals(r0, r1)
            if (r0 == 0) goto L46
        L3e:
            r0 = r6
            r0.remove()
            r0 = r7
            return r0
        L46:
            goto L19
        L49:
            r0 = r4
            int r0 = r0.getCapacity()
            if (r0 != 0) goto L88
            r0 = r4
            java.util.LinkedList<org.apache.http.impl.conn.tsccm.BasicPoolEntry> r0 = r0.freeEntries
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L88
            r0 = r4
            java.util.LinkedList<org.apache.http.impl.conn.tsccm.BasicPoolEntry> r0 = r0.freeEntries
            java.lang.Object r0 = r0.remove()
            org.apache.http.impl.conn.tsccm.BasicPoolEntry r0 = (org.apache.http.impl.conn.tsccm.BasicPoolEntry) r0
            r6 = r0
            r0 = r6
            r0.shutdownEntry()
            r0 = r6
            org.apache.http.conn.OperatedClientConnection r0 = r0.getConnection()
            r7 = r0
            r0 = r7
            r0.close()     // Catch: java.io.IOException -> L77
            goto L86
        L77:
            r8 = move-exception
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log
            java.lang.String r1 = "I/O error closing connection"
            r2 = r8
            r0.debug(r1, r2)
        L86:
            r0 = r6
            return r0
        L88:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.tsccm.RouteSpecificPool.allocEntry(java.lang.Object):org.apache.http.impl.conn.tsccm.BasicPoolEntry");
    }

    public void freeEntry(BasicPoolEntry entry) {
        if (this.numEntries < 1) {
            throw new IllegalStateException("No entry created for this pool. " + this.route);
        }
        if (this.numEntries <= this.freeEntries.size()) {
            throw new IllegalStateException("No entry allocated from this pool. " + this.route);
        }
        this.freeEntries.add(entry);
    }

    public void createdEntry(BasicPoolEntry entry) {
        if (!this.route.equals(entry.getPlannedRoute())) {
            throw new IllegalArgumentException("Entry not planned for this pool.\npool: " + this.route + "\nplan: " + entry.getPlannedRoute());
        }
        this.numEntries++;
    }

    public boolean deleteEntry(BasicPoolEntry entry) {
        boolean found = this.freeEntries.remove(entry);
        if (found) {
            this.numEntries--;
        }
        return found;
    }

    public void dropEntry() {
        if (this.numEntries < 1) {
            throw new IllegalStateException("There is no entry that could be dropped.");
        }
        this.numEntries--;
    }

    public void queueThread(WaitingThread wt) {
        if (wt == null) {
            throw new IllegalArgumentException("Waiting thread must not be null.");
        }
        this.waitingThreads.add(wt);
    }

    public boolean hasThread() {
        return !this.waitingThreads.isEmpty();
    }

    public WaitingThread nextThread() {
        return this.waitingThreads.peek();
    }

    public void removeThread(WaitingThread wt) {
        if (wt == null) {
            return;
        }
        this.waitingThreads.remove(wt);
    }
}
