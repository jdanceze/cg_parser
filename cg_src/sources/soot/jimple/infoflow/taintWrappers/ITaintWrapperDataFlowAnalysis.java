package soot.jimple.infoflow.taintWrappers;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/ITaintWrapperDataFlowAnalysis.class */
public interface ITaintWrapperDataFlowAnalysis {
    void setTaintWrapper(ITaintPropagationWrapper iTaintPropagationWrapper);

    ITaintPropagationWrapper getTaintWrapper();
}
