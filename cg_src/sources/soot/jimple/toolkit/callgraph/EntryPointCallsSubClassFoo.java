package soot.jimple.toolkit.callgraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkit/callgraph/EntryPointCallsSubClassFoo.class */
public class EntryPointCallsSubClassFoo {
    public void main() {
        BaseClassFooCallsBar baseClassFooCallsBar = new SubClassImplementsBaz();
        baseClassFooCallsBar.foo();
    }
}
