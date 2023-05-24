package ppg.atoms;

import ppg.util.CodeWriter;
import ppg.util.Equatable;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/GrammarSymbol.class */
public abstract class GrammarSymbol extends GrammarPart implements Equatable {
    protected String name;
    protected String label;

    public String getName() {
        return this.name;
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.begin(0);
        cw.write(this.name);
        if (this.label != null) {
            cw.write(new StringBuffer().append(":").append(this.label).toString());
        }
        cw.end();
    }

    public String toString() {
        String result = this.name;
        if (this.label != null) {
            result = new StringBuffer().append(result).append(":").append(this.label).toString();
        }
        return result;
    }
}
