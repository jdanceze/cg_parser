package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/nonassoc_action.class */
public class nonassoc_action extends parse_action {
    @Override // java_cup.parse_action
    public int kind() {
        return 3;
    }

    @Override // java_cup.parse_action
    public boolean equals(parse_action other) {
        return other != null && other.kind() == 3;
    }

    @Override // java_cup.parse_action
    public boolean equals(Object other) {
        if (other instanceof parse_action) {
            return equals((parse_action) other);
        }
        return false;
    }

    @Override // java_cup.parse_action
    public int hashCode() {
        return 212853537;
    }

    @Override // java_cup.parse_action
    public String toString() {
        return "NONASSOC";
    }
}
