package soot.jimple.infoflow.threading;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.executors.SetPoolExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/threading/DefaultExecutorFactory.class */
public class DefaultExecutorFactory implements IExecutorFactory {
    @Override // soot.jimple.infoflow.threading.IExecutorFactory
    public InterruptableExecutor createExecutor(int numThreads, boolean allowSetSemantics, InfoflowConfiguration config) {
        if (allowSetSemantics) {
            return new SetPoolExecutor(config.getMaxThreadNum() == -1 ? numThreads : Math.min(config.getMaxThreadNum(), numThreads), Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        return new InterruptableExecutor(config.getMaxThreadNum() == -1 ? numThreads : Math.min(config.getMaxThreadNum(), numThreads), Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }
}
