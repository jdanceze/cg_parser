package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/action_production.class */
public class action_production extends production {
    private int indexOfIntermediateResult;
    protected production _base_production;

    public action_production(production base, non_terminal lhs_sym, production_part[] rhs_parts, int rhs_len, String action_str, int indexOfIntermediateResult) throws internal_error {
        super(lhs_sym, rhs_parts, rhs_len, action_str);
        this._base_production = base;
        this.indexOfIntermediateResult = indexOfIntermediateResult;
    }

    public int getIndexOfIntermediateResult() {
        return this.indexOfIntermediateResult;
    }

    public production base_production() {
        return this._base_production;
    }
}
