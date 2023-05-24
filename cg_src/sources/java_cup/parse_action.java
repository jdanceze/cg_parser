package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/parse_action.class */
public class parse_action {
    public static final int ERROR = 0;
    public static final int SHIFT = 1;
    public static final int REDUCE = 2;
    public static final int NONASSOC = 3;

    public int kind() {
        return 0;
    }

    public boolean equals(parse_action other) {
        return other != null && other.kind() == 0;
    }

    public boolean equals(Object other) {
        if (other instanceof parse_action) {
            return equals((parse_action) other);
        }
        return false;
    }

    public int hashCode() {
        return 212853027;
    }

    public String toString() {
        return "ERROR";
    }
}
