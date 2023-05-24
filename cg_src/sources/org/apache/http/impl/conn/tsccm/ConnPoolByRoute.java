package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/tsccm/ConnPoolByRoute.class */
public class ConnPoolByRoute extends AbstractConnPool {
    private final Log log = LogFactory.getLog(getClass());
    private final HttpParams params;
    protected final ClientConnectionOperator operator;
    protected final Queue<BasicPoolEntry> freeConnections;
    protected final Queue<WaitingThread> waitingThreads;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;

    public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params) {
        if (operator == null) {
            throw new IllegalArgumentException("Connection operator may not be null");
        }
        this.operator = operator;
        this.params = params;
        this.freeConnections = createFreeConnQueue();
        this.waitingThreads = createWaitingThreadQueue();
        this.routeToPool = createRouteToPoolMap();
    }

    protected Queue<BasicPoolEntry> createFreeConnQueue() {
        return new LinkedList();
    }

    protected Queue<WaitingThread> createWaitingThreadQueue() {
        return new LinkedList();
    }

    protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
        return new HashMap();
    }

    protected RouteSpecificPool newRouteSpecificPool(HttpRoute route) {
        ConnPerRoute connPerRoute = ConnManagerParams.getMaxConnectionsPerRoute(this.params);
        return new RouteSpecificPool(route, connPerRoute.getMaxForRoute(route));
    }

    protected WaitingThread newWaitingThread(Condition cond, RouteSpecificPool rospl) {
        return new WaitingThread(cond, rospl);
    }

    protected RouteSpecificPool getRoutePool(HttpRoute route, boolean create) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = this.routeToPool.get(route);
            if (rospl == null && create) {
                rospl = newRouteSpecificPool(route);
                this.routeToPool.put(route, rospl);
            }
            return rospl;
        } finally {
            this.poolLock.unlock();
        }
    }

    public int getConnectionsInPool(HttpRoute route) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = getRoutePool(route, false);
            return rospl != null ? rospl.getEntryCount() : 0;
        } finally {
            this.poolLock.unlock();
        }
    }

    @Override // org.apache.http.impl.conn.tsccm.AbstractConnPool
    public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state) {
        final WaitingThreadAborter aborter = new WaitingThreadAborter();
        return new PoolEntryRequest() { // from class: org.apache.http.impl.conn.tsccm.ConnPoolByRoute.1
            @Override // org.apache.http.impl.conn.tsccm.PoolEntryRequest
            public void abortRequest() {
                ConnPoolByRoute.this.poolLock.lock();
                try {
                    aborter.abort();
                    ConnPoolByRoute.this.poolLock.unlock();
                } catch (Throwable th) {
                    ConnPoolByRoute.this.poolLock.unlock();
                    throw th;
                }
            }

            @Override // org.apache.http.impl.conn.tsccm.PoolEntryRequest
            public BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                return ConnPoolByRoute.this.getEntryBlocking(route, state, timeout, tunit, aborter);
            }
        };
    }

    protected BasicPoolEntry getEntryBlocking(HttpRoute route, Object state, long timeout, TimeUnit tunit, WaitingThreadAborter aborter) throws ConnectionPoolTimeoutException, InterruptedException {
        int maxTotalConnections = ConnManagerParams.getMaxTotalConnections(this.params);
        Date deadline = null;
        if (timeout > 0) {
            deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
        }
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = getRoutePool(route, true);
            WaitingThread waitingThread = null;
            while (entry == null) {
                if (this.isShutDown) {
                    throw new IllegalStateException("Connection pool shut down.");
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Total connections kept alive: " + this.freeConnections.size());
                    this.log.debug("Total issued connections: " + this.leasedConnections.size());
                    this.log.debug("Total allocated connection: " + this.numConnections + " out of " + maxTotalConnections);
                }
                entry = getFreeEntry(rospl, state);
                if (entry != null) {
                    break;
                }
                boolean hasCapacity = rospl.getCapacity() > 0;
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
                }
                if (hasCapacity && this.numConnections < maxTotalConnections) {
                    entry = createEntry(rospl, this.operator);
                } else if (hasCapacity && !this.freeConnections.isEmpty()) {
                    deleteLeastUsedEntry();
                    entry = createEntry(rospl, this.operator);
                } else {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Need to wait for connection [" + route + "][" + state + "]");
                    }
                    if (waitingThread == null) {
                        waitingThread = newWaitingThread(this.poolLock.newCondition(), rospl);
                        aborter.setWaitingThread(waitingThread);
                    }
                    rospl.queueThread(waitingThread);
                    this.waitingThreads.add(waitingThread);
                    boolean success = waitingThread.await(deadline);
                    rospl.removeThread(waitingThread);
                    this.waitingThreads.remove(waitingThread);
                    if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis()) {
                        throw new ConnectionPoolTimeoutException("Timeout waiting for connection");
                    }
                }
            }
            return entry;
        } finally {
            this.poolLock.unlock();
        }
    }

    @Override // org.apache.http.impl.conn.tsccm.AbstractConnPool
    public void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) {
        HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            if (this.isShutDown) {
                closeConnection(entry.getConnection());
                this.poolLock.unlock();
                return;
            }
            this.leasedConnections.remove(entry);
            RouteSpecificPool rospl = getRoutePool(route, true);
            if (reusable) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive for " + validDuration + Instruction.argsep + timeUnit.toString());
                }
                rospl.freeEntry(entry);
                this.freeConnections.add(entry);
                this.idleConnHandler.add(entry.getConnection(), validDuration, timeUnit);
            } else {
                rospl.dropEntry();
                this.numConnections--;
            }
            notifyWaitingThread(rospl);
            this.poolLock.unlock();
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }

    protected BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state) {
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        boolean done = false;
        while (!done) {
            try {
                entry = rospl.allocEntry(state);
                if (entry != null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
                    }
                    this.freeConnections.remove(entry);
                    boolean valid = this.idleConnHandler.remove(entry.getConnection());
                    if (!valid) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
                        }
                        closeConnection(entry.getConnection());
                        rospl.dropEntry();
                        this.numConnections--;
                    } else {
                        this.leasedConnections.add(entry);
                        done = true;
                    }
                } else {
                    done = true;
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
                    }
                }
            } finally {
                this.poolLock.unlock();
            }
        }
        return entry;
    }

    protected BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
        }
        BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute());
        this.poolLock.lock();
        try {
            rospl.createdEntry(entry);
            this.numConnections++;
            this.leasedConnections.add(entry);
            this.poolLock.unlock();
            return entry;
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }

    protected void deleteEntry(BasicPoolEntry entry) {
        HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            closeConnection(entry.getConnection());
            RouteSpecificPool rospl = getRoutePool(route, true);
            rospl.deleteEntry(entry);
            this.numConnections--;
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
            this.idleConnHandler.remove(entry.getConnection());
            this.poolLock.unlock();
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }

    protected void deleteLeastUsedEntry() {
        try {
            this.poolLock.lock();
            BasicPoolEntry entry = this.freeConnections.remove();
            if (entry != null) {
                deleteEntry(entry);
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("No free connection to delete.");
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    @Override // org.apache.http.impl.conn.tsccm.AbstractConnPool
    protected void handleLostEntry(HttpRoute route) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = getRoutePool(route, true);
            rospl.dropEntry();
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
            this.numConnections--;
            notifyWaitingThread(rospl);
            this.poolLock.unlock();
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x009c A[Catch: all -> 0x00ac, TryCatch #0 {all -> 0x00ac, blocks: (B:5:0x000f, B:7:0x0016, B:9:0x0022, B:10:0x0046, B:22:0x009c, B:11:0x004e, B:13:0x005a, B:15:0x0066, B:16:0x0071, B:17:0x0081, B:19:0x008d), top: B:30:0x000f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void notifyWaitingThread(org.apache.http.impl.conn.tsccm.RouteSpecificPool r5) {
        /*
            r4 = this;
            r0 = 0
            r6 = r0
            r0 = r4
            java.util.concurrent.locks.Lock r0 = r0.poolLock
            r0.lock()
            r0 = r5
            if (r0 == 0) goto L4e
            r0 = r5
            boolean r0 = r0.hasThread()     // Catch: java.lang.Throwable -> Lac
            if (r0 == 0) goto L4e
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            boolean r0 = r0.isDebugEnabled()     // Catch: java.lang.Throwable -> Lac
            if (r0 == 0) goto L46
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lac
            r2 = r1
            r2.<init>()     // Catch: java.lang.Throwable -> Lac
            java.lang.String r2 = "Notifying thread waiting on pool ["
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Lac
            r2 = r5
            org.apache.http.conn.routing.HttpRoute r2 = r2.getRoute()     // Catch: java.lang.Throwable -> Lac
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Lac
            java.lang.String r2 = "]"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Lac
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Lac
            r0.debug(r1)     // Catch: java.lang.Throwable -> Lac
        L46:
            r0 = r5
            org.apache.http.impl.conn.tsccm.WaitingThread r0 = r0.nextThread()     // Catch: java.lang.Throwable -> Lac
            r6 = r0
            goto L98
        L4e:
            r0 = r4
            java.util.Queue<org.apache.http.impl.conn.tsccm.WaitingThread> r0 = r0.waitingThreads     // Catch: java.lang.Throwable -> Lac
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> Lac
            if (r0 != 0) goto L81
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            boolean r0 = r0.isDebugEnabled()     // Catch: java.lang.Throwable -> Lac
            if (r0 == 0) goto L71
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            java.lang.String r1 = "Notifying thread waiting on any pool"
            r0.debug(r1)     // Catch: java.lang.Throwable -> Lac
        L71:
            r0 = r4
            java.util.Queue<org.apache.http.impl.conn.tsccm.WaitingThread> r0 = r0.waitingThreads     // Catch: java.lang.Throwable -> Lac
            java.lang.Object r0 = r0.remove()     // Catch: java.lang.Throwable -> Lac
            org.apache.http.impl.conn.tsccm.WaitingThread r0 = (org.apache.http.impl.conn.tsccm.WaitingThread) r0     // Catch: java.lang.Throwable -> Lac
            r6 = r0
            goto L98
        L81:
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            boolean r0 = r0.isDebugEnabled()     // Catch: java.lang.Throwable -> Lac
            if (r0 == 0) goto L98
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> Lac
            java.lang.String r1 = "Notifying no-one, there are no waiting threads"
            r0.debug(r1)     // Catch: java.lang.Throwable -> Lac
        L98:
            r0 = r6
            if (r0 == 0) goto La0
            r0 = r6
            r0.wakeup()     // Catch: java.lang.Throwable -> Lac
        La0:
            r0 = r4
            java.util.concurrent.locks.Lock r0 = r0.poolLock
            r0.unlock()
            goto Lb8
        Lac:
            r7 = move-exception
            r0 = r4
            java.util.concurrent.locks.Lock r0 = r0.poolLock
            r0.unlock()
            r0 = r7
            throw r0
        Lb8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.tsccm.ConnPoolByRoute.notifyWaitingThread(org.apache.http.impl.conn.tsccm.RouteSpecificPool):void");
    }

    @Override // org.apache.http.impl.conn.tsccm.AbstractConnPool
    public void deleteClosedConnections() {
        this.poolLock.lock();
        try {
            Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = iter.next();
                if (!entry.getConnection().isOpen()) {
                    iter.remove();
                    deleteEntry(entry);
                }
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    @Override // org.apache.http.impl.conn.tsccm.AbstractConnPool
    public void shutdown() {
        this.poolLock.lock();
        try {
            super.shutdown();
            Iterator<BasicPoolEntry> ibpe = this.freeConnections.iterator();
            while (ibpe.hasNext()) {
                BasicPoolEntry entry = ibpe.next();
                ibpe.remove();
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection [" + entry.getPlannedRoute() + "][" + entry.getState() + "]");
                }
                closeConnection(entry.getConnection());
            }
            Iterator<WaitingThread> iwth = this.waitingThreads.iterator();
            while (iwth.hasNext()) {
                WaitingThread waiter = iwth.next();
                iwth.remove();
                waiter.wakeup();
            }
            this.routeToPool.clear();
            this.poolLock.unlock();
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }
}
