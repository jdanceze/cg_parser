package ppg.cmds;

import ppg.atoms.Production;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/cmds/ExtendCmd.class */
public class ExtendCmd implements Command {
    private Production prod;

    public ExtendCmd(Production p) {
        this.prod = p;
    }

    public Production getProduction() {
        return this.prod;
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.write("ExtendCmd");
        cw.allowBreak(2);
        this.prod.unparse(cw);
    }
}
