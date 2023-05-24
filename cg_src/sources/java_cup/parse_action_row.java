package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/parse_action_row.class */
public class parse_action_row {
    protected static int _size = 0;
    protected static int[] reduction_count = null;
    public parse_action[] under_term;
    public int default_reduce;

    public parse_action_row() {
        if (_size <= 0) {
            _size = terminal.number();
        }
        this.under_term = new parse_action[size()];
        for (int i = 0; i < _size; i++) {
            this.under_term[i] = new parse_action();
        }
    }

    public static int size() {
        return _size;
    }

    public static void clear() {
        _size = 0;
        reduction_count = null;
    }

    public void compute_default() {
        if (reduction_count == null) {
            reduction_count = new int[production.number()];
        }
        for (int i = 0; i < production.number(); i++) {
            reduction_count[i] = 0;
        }
        int max_prod = -1;
        int max_red = 0;
        for (int i2 = 0; i2 < size(); i2++) {
            if (this.under_term[i2].kind() == 2) {
                int prod = ((reduce_action) this.under_term[i2]).reduce_with().index();
                int[] iArr = reduction_count;
                iArr[prod] = iArr[prod] + 1;
                if (reduction_count[prod] > max_red) {
                    max_red = reduction_count[prod];
                    max_prod = prod;
                }
            }
        }
        this.default_reduce = max_prod;
    }
}
