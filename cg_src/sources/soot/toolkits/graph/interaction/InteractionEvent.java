package soot.toolkits.graph.interaction;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/interaction/InteractionEvent.class */
public class InteractionEvent {
    private int type;
    private Object info;

    public InteractionEvent(int type) {
        type(type);
    }

    public InteractionEvent(int type, Object info) {
        type(type);
        info(info);
    }

    private void type(int t) {
        this.type = t;
    }

    private void info(Object i) {
        this.info = i;
    }

    public int type() {
        return this.type;
    }

    public Object info() {
        return this.info;
    }
}
