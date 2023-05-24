package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/parse_reduce_table.class */
public class parse_reduce_table {
    protected int _num_states = lalr_state.number();
    public parse_reduce_row[] under_state = new parse_reduce_row[this._num_states];

    public parse_reduce_table() {
        for (int i = 0; i < this._num_states; i++) {
            this.under_state[i] = new parse_reduce_row();
        }
    }

    public int num_states() {
        return this._num_states;
    }

    public String toString() {
        String result = "-------- REDUCE_TABLE --------\n";
        for (int row = 0; row < num_states(); row++) {
            result = String.valueOf(result) + "From state #" + row + "\n";
            int cnt = 0;
            for (int col = 0; col < parse_reduce_row.size(); col++) {
                lalr_state goto_st = this.under_state[row].under_non_term[col];
                if (goto_st != null) {
                    result = String.valueOf(String.valueOf(result) + " [non term " + col + "->") + "state " + goto_st.index() + "]";
                    cnt++;
                    if (cnt == 3) {
                        result = String.valueOf(result) + "\n";
                        cnt = 0;
                    }
                }
            }
            if (cnt != 0) {
                result = String.valueOf(result) + "\n";
            }
        }
        return String.valueOf(result) + "-----------------------------";
    }
}
