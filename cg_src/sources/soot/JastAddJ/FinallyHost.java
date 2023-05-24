package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FinallyHost.class */
public interface FinallyHost {
    boolean isDUafterFinally(Variable variable);

    boolean isDAafterFinally(Variable variable);

    void emitFinallyCode(Body body);

    soot.jimple.Stmt label_finally_block();
}
