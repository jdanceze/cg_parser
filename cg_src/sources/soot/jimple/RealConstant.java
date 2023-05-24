package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/RealConstant.class */
public abstract class RealConstant extends NumericConstant {
    private static final long serialVersionUID = -5624501180441017529L;

    public abstract IntConstant cmpl(RealConstant realConstant);

    public abstract IntConstant cmpg(RealConstant realConstant);
}
