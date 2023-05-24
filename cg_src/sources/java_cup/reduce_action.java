package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/reduce_action.class */
public class reduce_action extends parse_action {
    protected production _reduce_with;

    public reduce_action(production prod) throws internal_error {
        if (prod == null) {
            throw new internal_error("Attempt to create a reduce_action with a null production");
        }
        this._reduce_with = prod;
    }

    public production reduce_with() {
        return this._reduce_with;
    }

    @Override // java_cup.parse_action
    public int kind() {
        return 2;
    }

    public boolean equals(reduce_action other) {
        return other != null && other.reduce_with() == reduce_with();
    }

    @Override // java_cup.parse_action
    public boolean equals(Object other) {
        if (other instanceof reduce_action) {
            return equals((reduce_action) other);
        }
        return false;
    }

    @Override // java_cup.parse_action
    public int hashCode() {
        return reduce_with().hashCode();
    }

    @Override // java_cup.parse_action
    public String toString() {
        return "REDUCE(with prod " + reduce_with().index() + ")";
    }
}
