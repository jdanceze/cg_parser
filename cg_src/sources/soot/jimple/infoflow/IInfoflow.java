package soot.jimple.infoflow;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import soot.jimple.Stmt;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory;
import soot.jimple.infoflow.entryPointCreators.IEntryPointCreator;
import soot.jimple.infoflow.handlers.PostAnalysisHandler;
import soot.jimple.infoflow.handlers.PreAnalysisHandler;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.ipc.IIPCManager;
import soot.jimple.infoflow.nativeCallHandler.INativeCallHandler;
import soot.jimple.infoflow.problems.rules.IPropagationRuleManagerFactory;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.memory.IMemoryManagerFactory;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis;
import soot.jimple.infoflow.threading.IExecutorFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/IInfoflow.class */
public interface IInfoflow extends ITaintWrapperDataFlowAnalysis {
    InfoflowConfiguration getConfig();

    void setConfig(InfoflowConfiguration infoflowConfiguration);

    void setNativeCallHandler(INativeCallHandler iNativeCallHandler);

    void setPreProcessors(Collection<? extends PreAnalysisHandler> collection);

    void setPostProcessors(Collection<? extends PostAnalysisHandler> collection);

    void computeInfoflow(String str, String str2, IEntryPointCreator iEntryPointCreator, List<String> list, List<String> list2);

    void computeInfoflow(String str, String str2, Collection<String> collection, Collection<String> collection2, Collection<String> collection3);

    void computeInfoflow(String str, String str2, String str3, Collection<String> collection, Collection<String> collection2);

    void computeInfoflow(String str, String str2, IEntryPointCreator iEntryPointCreator, ISourceSinkManager iSourceSinkManager);

    void computeInfoflow(String str, String str2, String str3, ISourceSinkManager iSourceSinkManager);

    InfoflowResults getResults();

    boolean isResultAvailable();

    void setIPCManager(IIPCManager iIPCManager);

    void setSootConfig(IInfoflowConfig iInfoflowConfig);

    void setPathBuilderFactory(IPathBuilderFactory iPathBuilderFactory);

    Set<Stmt> getCollectedSources();

    Set<Stmt> getCollectedSinks();

    void addResultsAvailableHandler(ResultsAvailableHandler resultsAvailableHandler);

    void removeResultsAvailableHandler(ResultsAvailableHandler resultsAvailableHandler);

    void abortAnalysis();

    void setTaintPropagationHandler(TaintPropagationHandler taintPropagationHandler);

    void setAliasPropagationHandler(TaintPropagationHandler taintPropagationHandler);

    void setMemoryManagerFactory(IMemoryManagerFactory iMemoryManagerFactory);

    void setExecutorFactory(IExecutorFactory iExecutorFactory);

    void setPropagationRuleManagerFactory(IPropagationRuleManagerFactory iPropagationRuleManagerFactory);
}
