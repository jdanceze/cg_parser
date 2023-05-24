package soot.jimple.infoflow.util;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ThreadUtils.class */
public class ThreadUtils {
    public static IThreadFactory threadFactory = new IThreadFactory() { // from class: soot.jimple.infoflow.util.ThreadUtils.1
        @Override // soot.jimple.infoflow.util.ThreadUtils.IThreadFactory
        public Thread createGenericThread(Runnable r, String name, boolean daemon) {
            Thread thr = new Thread(r, name);
            thr.setDaemon(daemon);
            return thr;
        }
    };

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ThreadUtils$IThreadFactory.class */
    public interface IThreadFactory {
        Thread createGenericThread(Runnable runnable, String str, boolean z);
    }

    public static Thread createGenericThread(Runnable r, String name, boolean daemon) {
        return threadFactory.createGenericThread(r, name, daemon);
    }
}
