package ppg.cmds;

import ppg.atoms.Production;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/cmds/NewProdCmd.class */
public class NewProdCmd implements Command {
    private Production prod;

    public NewProdCmd(Production p) {
        this.prod = p;
    }

    public Production getProduction() {
        return this.prod;
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.write("NewProdCmd");
        cw.allowBreak(0);
        this.prod.unparse(cw);
    }
}
