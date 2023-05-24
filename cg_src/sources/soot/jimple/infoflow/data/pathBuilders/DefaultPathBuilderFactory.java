package soot.jimple.infoflow.data.pathBuilders;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/DefaultPathBuilderFactory.class */
public class DefaultPathBuilderFactory implements IPathBuilderFactory {
    private final InfoflowConfiguration.PathConfiguration pathConfiguration;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowConfiguration.PathBuildingAlgorithm.valuesCustom().length];
        try {
            iArr2[InfoflowConfiguration.PathBuildingAlgorithm.ContextInsensitive.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowConfiguration.PathBuildingAlgorithm.ContextInsensitiveSourceFinder.ordinal()] = 4;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[InfoflowConfiguration.PathBuildingAlgorithm.ContextSensitive.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[InfoflowConfiguration.PathBuildingAlgorithm.None.ordinal()] = 5;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[InfoflowConfiguration.PathBuildingAlgorithm.Recursive.ordinal()] = 1;
        } catch (NoSuchFieldError unused5) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm = iArr2;
        return iArr2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/DefaultPathBuilderFactory$RepeatableContextSensitivePathBuilder.class */
    public static class RepeatableContextSensitivePathBuilder extends ContextSensitivePathBuilder {
        public RepeatableContextSensitivePathBuilder(InfoflowManager manager) {
            super(manager);
        }

        @Override // soot.jimple.infoflow.data.pathBuilders.ContextSensitivePathBuilder
        protected void cleanupExecutor() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/DefaultPathBuilderFactory$ShutdownBatchPathBuilder.class */
    public static class ShutdownBatchPathBuilder extends BatchPathBuilder {
        public ShutdownBatchPathBuilder(InfoflowManager manager, RepeatableContextSensitivePathBuilder innerBuilder) {
            super(manager, innerBuilder);
        }

        @Override // soot.jimple.infoflow.data.pathBuilders.BatchPathBuilder, soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
        public void computeTaintPaths(Set<AbstractionAtSink> res) {
            try {
                super.computeTaintPaths(res);
            } finally {
                ((RepeatableContextSensitivePathBuilder) this.innerBuilder).shutdown();
            }
        }
    }

    public DefaultPathBuilderFactory(InfoflowConfiguration.PathConfiguration config) {
        this.pathConfiguration = config;
    }

    private InterruptableExecutor createExecutor(int maxThreadNum) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        InterruptableExecutor executor = new InterruptableExecutor(maxThreadNum == -1 ? numThreads : Math.min(maxThreadNum, numThreads), Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        executor.setThreadFactory(new ThreadFactory() { // from class: soot.jimple.infoflow.data.pathBuilders.DefaultPathBuilderFactory.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                Thread thr = new Thread(r, "Path reconstruction");
                return thr;
            }
        });
        return executor;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory
    public IAbstractionPathBuilder createPathBuilder(InfoflowManager manager, int maxThreadNum) {
        return createPathBuilder(manager, createExecutor(maxThreadNum));
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory
    public IAbstractionPathBuilder createPathBuilder(InfoflowManager manager, InterruptableExecutor executor) {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm()[this.pathConfiguration.getPathBuildingAlgorithm().ordinal()]) {
            case 1:
                return new BatchPathBuilder(manager, new RecursivePathBuilder(manager, executor));
            case 2:
                return new ShutdownBatchPathBuilder(manager, new RepeatableContextSensitivePathBuilder(manager));
            case 3:
                return new BatchPathBuilder(manager, new ContextInsensitivePathBuilder(manager, executor));
            case 4:
                return new BatchPathBuilder(manager, new ContextInsensitiveSourceFinder(manager, executor));
            case 5:
                return new EmptyPathBuilder();
            default:
                throw new RuntimeException("Unsupported path building algorithm");
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory
    public boolean supportsPathReconstruction() {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathBuildingAlgorithm()[this.pathConfiguration.getPathBuildingAlgorithm().ordinal()]) {
            case 1:
            case 2:
            case 3:
                return true;
            case 4:
            case 5:
                return false;
            default:
                throw new RuntimeException("Unsupported path building algorithm");
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory
    public boolean isContextSensitive() {
        return this.pathConfiguration.getPathBuildingAlgorithm() == InfoflowConfiguration.PathBuildingAlgorithm.ContextSensitive;
    }
}
