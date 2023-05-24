package ppg.atoms;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/Terminal.class */
public class Terminal extends GrammarSymbol {
    public Terminal(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public Terminal(String name) {
        this.name = name;
        this.label = null;
    }

    @Override // ppg.atoms.GrammarPart
    public Object clone() {
        return new Terminal(this.name, this.label);
    }

    @Override // ppg.util.Equatable
    public boolean equals(Object o) {
        if (o instanceof Terminal) {
            return this.name.equals(((Terminal) o).getName());
        }
        if (o instanceof String) {
            return this.name.equals(o);
        }
        return false;
    }
}
