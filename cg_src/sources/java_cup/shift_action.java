package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/shift_action.class */
public class shift_action extends parse_action {
    protected lalr_state _shift_to;

    public shift_action(lalr_state shft_to) throws internal_error {
        if (shft_to == null) {
            throw new internal_error("Attempt to create a shift_action to a null state");
        }
        this._shift_to = shft_to;
    }

    public lalr_state shift_to() {
        return this._shift_to;
    }

    @Override // java_cup.parse_action
    public int kind() {
        return 1;
    }

    public boolean equals(shift_action other) {
        return other != null && other.shift_to() == shift_to();
    }

    @Override // java_cup.parse_action
    public boolean equals(Object other) {
        if (other instanceof shift_action) {
            return equals((shift_action) other);
        }
        return false;
    }

    @Override // java_cup.parse_action
    public int hashCode() {
        return shift_to().hashCode();
    }

    @Override // java_cup.parse_action
    public String toString() {
        return "SHIFT(to state " + shift_to().index() + ")";
    }
}
