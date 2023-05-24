package soot.jimple.toolkit.callgraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkit/callgraph/SubImplementation.class */
public class SubImplementation implements SubInterface {
    public void invokeTarget(String arg) {
    }

    public void doNotCall(A arg) {
    }

    public String getName() {
        return "foobar" + System.currentTimeMillis();
    }

    @Override // soot.jimple.toolkit.callgraph.Interface
    public Object[] args() {
        return new Object[]{"foo"};
    }
}
