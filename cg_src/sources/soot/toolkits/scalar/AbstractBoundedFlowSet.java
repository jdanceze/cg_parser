package soot.toolkits.scalar;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/AbstractBoundedFlowSet.class */
public abstract class AbstractBoundedFlowSet<T> extends AbstractFlowSet<T> implements BoundedFlowSet<T> {
    @Override // soot.toolkits.scalar.BoundedFlowSet
    public void complement() {
        complement(this);
    }

    @Override // soot.toolkits.scalar.BoundedFlowSet
    public void complement(FlowSet<T> dest) {
        if (this == dest) {
            complement();
            return;
        }
        BoundedFlowSet<T> tmp = (BoundedFlowSet) topSet();
        tmp.difference(this, dest);
    }

    @Override // soot.toolkits.scalar.BoundedFlowSet
    public FlowSet<T> topSet() {
        BoundedFlowSet<T> tmp = (BoundedFlowSet) emptySet();
        tmp.complement();
        return tmp;
    }
}
