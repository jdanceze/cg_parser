package soot.jimple.infoflow.memory;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.util.ThreadUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/MemoryWarningSystem.class */
public class MemoryWarningSystem {
    private final Set<OnMemoryThresholdReached> listeners = new HashSet();
    private boolean isClosed = false;
    private long threshold;
    private static Thread thrLowMemoryWarningThread;
    private static final Logger logger = LoggerFactory.getLogger(MemoryWarningSystem.class);
    private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();
    private static TreeSet<MemoryWarningSystem> warningSystems = new TreeSet<>(new Comparator<MemoryWarningSystem>() { // from class: soot.jimple.infoflow.memory.MemoryWarningSystem.1
        @Override // java.util.Comparator
        public int compare(MemoryWarningSystem o1, MemoryWarningSystem o2) {
            return Long.compare(o1.threshold, o2.threshold);
        }
    });
    private static final NotificationListener memoryListener = new NotificationListener() { // from class: soot.jimple.infoflow.memory.MemoryWarningSystem.2
        @Override // javax.management.NotificationListener
        public void handleNotification(Notification notification, Object handback) {
            if (!notification.getType().equals("java.management.memory.threshold.exceeded")) {
                return;
            }
            MemoryWarningSystem.triggerNotification();
        }
    };

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/MemoryWarningSystem$OnMemoryThresholdReached.class */
    public interface OnMemoryThresholdReached {
        void onThresholdReached(long j, long j2);
    }

    static {
        ManagementFactory.getMemoryMXBean().addNotificationListener(memoryListener, new NotificationFilter() { // from class: soot.jimple.infoflow.memory.MemoryWarningSystem.3
            private static final long serialVersionUID = -3755179266517545663L;

            @Override // javax.management.NotificationFilter
            public boolean isNotificationEnabled(Notification notification) {
                return notification.getType().equals("java.management.memory.threshold.exceeded");
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Throwable, java.util.TreeSet<soot.jimple.infoflow.memory.MemoryWarningSystem>] */
    public static long triggerNotification() {
        long maxMemory = tenuredGenPool.getUsage().getMax();
        long usedMemory = tenuredGenPool.getUsage().getUsed();
        Runtime runtime = Runtime.getRuntime();
        long usedMem = runtime.totalMemory() - runtime.freeMemory();
        synchronized (warningSystems) {
            Iterator<MemoryWarningSystem> it = warningSystems.iterator();
            while (it.hasNext()) {
                MemoryWarningSystem ws = it.next();
                if (ws.threshold <= usedMemory) {
                    logger.info("Triggering memory warning at " + ((usedMem / 1000) / 1000) + " MB (" + ((maxMemory / 1000) / 1000) + " MB max, " + ((usedMemory / 1000) / 1000) + " in watched memory pool)...");
                    for (OnMemoryThresholdReached listener : ws.listeners) {
                        listener.onThresholdReached(usedMemory, maxMemory);
                    }
                    it.remove();
                } else {
                    tenuredGenPool.setUsageThreshold(ws.threshold);
                    return ws.threshold;
                }
            }
            return -1L;
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void addListener(OnMemoryThresholdReached listener) {
        this.listeners.add(listener);
    }

    public static MemoryPoolMXBean findTenuredGenPool() {
        List<MemoryPoolMXBean> usablePools = new ArrayList<>();
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() == MemoryType.HEAP && pool.isUsageThresholdSupported()) {
                usablePools.add(pool);
                if (pool.getName().equals("Tenured Gen")) {
                    return pool;
                }
            }
        }
        if (!usablePools.isEmpty()) {
            return usablePools.get(0);
        }
        throw new AssertionError("Could not find tenured space");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [java.util.TreeSet<soot.jimple.infoflow.memory.MemoryWarningSystem>] */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v34 */
    public void setWarningThreshold(double percentage) {
        if (percentage <= Const.default_value_double || percentage > 1.0d) {
            throw new IllegalArgumentException("Percentage not in range");
        }
        long maxMemory = tenuredGenPool.getUsage().getMax();
        long warningThreshold = (long) (maxMemory * percentage);
        ?? r0 = warningSystems;
        synchronized (r0) {
            warningSystems.remove(this);
            this.threshold = warningThreshold;
            logger.info(MessageFormat.format("Registered a memory warning system for {0} MiB", Double.valueOf((this.threshold / 1024.0d) / 1024.0d)));
            warningSystems.add(this);
            MemoryUsage usage = tenuredGenPool.getUsage();
            long threshold = warningSystems.iterator().next().threshold;
            boolean useOwnImplementation = !tenuredGenPool.isUsageThresholdSupported();
            if (!useOwnImplementation && usage != null && usage.getUsed() > threshold) {
                tenuredGenPool.setUsageThreshold(threshold);
            } else {
                useOwnImplementation = true;
            }
            if (useOwnImplementation && thrLowMemoryWarningThread == null) {
                thrLowMemoryWarningThread = ThreadUtils.createGenericThread(new Runnable() { // from class: soot.jimple.infoflow.memory.MemoryWarningSystem.4
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.TreeSet] */
                    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable] */
                    /* JADX WARN: Type inference failed for: r0v36, types: [java.util.TreeSet] */
                    /* JADX WARN: Type inference failed for: r0v37, types: [java.lang.Throwable] */
                    /* JADX WARN: Type inference failed for: r0v41 */
                    /* JADX WARN: Type inference failed for: r0v5 */
                    @Override // java.lang.Runnable
                    public void run() {
                        MemoryWarningSystem l;
                        while (true) {
                            ?? r02 = MemoryWarningSystem.warningSystems;
                            synchronized (r02) {
                                if (MemoryWarningSystem.warningSystems.isEmpty()) {
                                    MemoryWarningSystem.thrLowMemoryWarningThread = null;
                                    r02 = r02;
                                    return;
                                }
                                l = (MemoryWarningSystem) MemoryWarningSystem.warningSystems.iterator().next();
                            }
                            long nextThreshold = l.threshold;
                            MemoryUsage usage2 = MemoryWarningSystem.tenuredGenPool.getUsage();
                            if (usage2 == null) {
                                MemoryWarningSystem.logger.warn(MessageFormat.format("Memory usage of {0} could not be estimated", MemoryWarningSystem.tenuredGenPool.getName()));
                                return;
                            }
                            long used = usage2.getUsed();
                            if (used >= l.threshold) {
                                nextThreshold = MemoryWarningSystem.triggerNotification();
                                if (nextThreshold == -1) {
                                    ?? r03 = MemoryWarningSystem.warningSystems;
                                    synchronized (r03) {
                                        if (MemoryWarningSystem.warningSystems.isEmpty()) {
                                            MemoryWarningSystem.thrLowMemoryWarningThread = null;
                                            r03 = r03;
                                            return;
                                        }
                                    }
                                }
                            }
                            long used2 = usage2.getUsed();
                            long missing = nextThreshold - used2;
                            if (missing > 0) {
                                try {
                                    long wait = (long) ((missing / MemoryWarningSystem.tenuredGenPool.getUsage().getMax()) * 500.0d);
                                    Thread.sleep(wait);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                }, "Low memory monitor", true);
                thrLowMemoryWarningThread.setPriority(1);
                thrLowMemoryWarningThread.start();
            }
            r0 = r0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.util.TreeSet<soot.jimple.infoflow.memory.MemoryWarningSystem>] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v7 */
    public void close() {
        if (this.isClosed) {
            return;
        }
        logger.info("Shutting down the memory warning system...");
        ?? r0 = warningSystems;
        synchronized (r0) {
            warningSystems.remove(this);
            r0 = r0;
            try {
                ManagementFactory.getMemoryMXBean().removeNotificationListener(memoryListener);
            } catch (ListenerNotFoundException e) {
            }
            this.isClosed = true;
        }
    }
}
