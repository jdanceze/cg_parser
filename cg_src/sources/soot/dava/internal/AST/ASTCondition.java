package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTCondition.class */
public abstract class ASTCondition {
    public abstract void apply(Analysis analysis);

    public abstract void toString(UnitPrinter unitPrinter);

    public abstract void flip();

    public abstract boolean isNotted();
}
