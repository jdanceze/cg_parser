package soot.toolkits.graph.interaction;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/interaction/FlowInfo.class */
public class FlowInfo<I, U> {
    private I info;
    private U unit;
    private boolean before;

    public FlowInfo(I info, U unit, boolean b) {
        info(info);
        unit(unit);
        setBefore(b);
    }

    public U unit() {
        return this.unit;
    }

    public void unit(U u) {
        this.unit = u;
    }

    public I info() {
        return this.info;
    }

    public void info(I i) {
        this.info = i;
    }

    public boolean isBefore() {
        return this.before;
    }

    public void setBefore(boolean b) {
        this.before = b;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("unit: " + this.unit);
        sb.append(" info: " + this.info);
        sb.append(" before: " + this.before);
        return sb.toString();
    }
}
