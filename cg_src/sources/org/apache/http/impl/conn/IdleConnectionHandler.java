package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.resource.spi.work.WorkManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.NotThreadSafe;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/IdleConnectionHandler.class */
public class IdleConnectionHandler {
    private final Log log = LogFactory.getLog(getClass());
    private final Map<HttpConnection, TimeValues> connectionToTimes = new HashMap();

    public void add(HttpConnection connection, long validDuration, TimeUnit unit) {
        long timeAdded = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Adding connection at: " + timeAdded);
        }
        this.connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
    }

    public boolean remove(HttpConnection connection) {
        TimeValues times = this.connectionToTimes.remove(connection);
        if (times != null) {
            return System.currentTimeMillis() <= times.timeExpires;
        }
        this.log.warn("Removing a connection that never existed!");
        return true;
    }

    public void removeAll() {
        this.connectionToTimes.clear();
    }

    public void closeIdleConnections(long idleTime) {
        long idleTimeout = System.currentTimeMillis() - idleTime;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Checking for connections, idle timeout: " + idleTimeout);
        }
        for (HttpConnection conn : this.connectionToTimes.keySet()) {
            TimeValues times = this.connectionToTimes.get(conn);
            long connectionTime = times.timeAdded;
            if (connectionTime <= idleTimeout) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing idle connection, connection time: " + connectionTime);
                }
                try {
                    conn.close();
                } catch (IOException ex) {
                    this.log.debug("I/O error closing connection", ex);
                }
            }
        }
    }

    public void closeExpiredConnections() {
        long now = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Checking for expired connections, now: " + now);
        }
        for (HttpConnection conn : this.connectionToTimes.keySet()) {
            TimeValues times = this.connectionToTimes.get(conn);
            if (times.timeExpires <= now) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection, expired @: " + times.timeExpires);
                }
                try {
                    conn.close();
                } catch (IOException ex) {
                    this.log.debug("I/O error closing connection", ex);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/IdleConnectionHandler$TimeValues.class */
    private static class TimeValues {
        private final long timeAdded;
        private final long timeExpires;

        TimeValues(long now, long validDuration, TimeUnit validUnit) {
            this.timeAdded = now;
            if (validDuration > 0) {
                this.timeExpires = now + validUnit.toMillis(validDuration);
            } else {
                this.timeExpires = WorkManager.INDEFINITE;
            }
        }
    }
}
