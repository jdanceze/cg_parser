package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/action_part.class */
public class action_part extends production_part {
    protected String _code_string;

    public action_part(String code_str) {
        super(null);
        this._code_string = code_str;
    }

    public String code_string() {
        return this._code_string;
    }

    public void set_code_string(String new_str) {
        this._code_string = new_str;
    }

    @Override // java_cup.production_part
    public boolean is_action() {
        return true;
    }

    public boolean equals(action_part other) {
        return other != null && super.equals((production_part) other) && other.code_string().equals(code_string());
    }

    @Override // java_cup.production_part
    public boolean equals(Object other) {
        if (!(other instanceof action_part)) {
            return false;
        }
        return equals((action_part) other);
    }

    @Override // java_cup.production_part
    public int hashCode() {
        return super.hashCode() ^ (code_string() == null ? 0 : code_string().hashCode());
    }

    @Override // java_cup.production_part
    public String toString() {
        return String.valueOf(super.toString()) + "{" + code_string() + "}";
    }
}
