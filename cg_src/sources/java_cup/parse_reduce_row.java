package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/parse_reduce_row.class */
public class parse_reduce_row {
    protected static int _size = 0;
    public lalr_state[] under_non_term;

    public parse_reduce_row() {
        if (_size <= 0) {
            _size = non_terminal.number();
        }
        this.under_non_term = new lalr_state[size()];
    }

    public static int size() {
        return _size;
    }

    public static void clear() {
        _size = 0;
    }
}
