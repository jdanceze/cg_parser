package ppg.atoms;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/Nonterminal.class */
public class Nonterminal extends GrammarSymbol {
    public Nonterminal(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public Nonterminal(String name) {
        this.name = name;
        this.label = null;
    }

    @Override // ppg.atoms.GrammarPart
    public Object clone() {
        return new Nonterminal(this.name, this.label);
    }

    @Override // ppg.util.Equatable
    public boolean equals(Object o) {
        if (o instanceof Nonterminal) {
            return this.name.equals(((Nonterminal) o).getName());
        }
        if (o instanceof String) {
            return this.name.equals(o);
        }
        return false;
    }
}
