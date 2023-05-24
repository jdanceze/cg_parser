package soot;

import soot.jimple.AnyNewExpr;
/* loaded from: gencallgraphv3.jar:soot/EscapeAnalysis.class */
public interface EscapeAnalysis {
    boolean mayEscapeMethod(AnyNewExpr anyNewExpr);

    boolean mayEscapeMethod(Context context, AnyNewExpr anyNewExpr);

    boolean mayEscapeThread(AnyNewExpr anyNewExpr);

    boolean mayEscapeThread(Context context, AnyNewExpr anyNewExpr);
}
