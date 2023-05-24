package soot.toolkits.scalar;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/BoundedFlowSet.class */
public interface BoundedFlowSet<T> extends FlowSet<T> {
    void complement();

    void complement(FlowSet<T> flowSet);

    FlowSet<T> topSet();
}
