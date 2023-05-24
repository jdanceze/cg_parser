package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/PrettyPrintPass.class */
public class PrettyPrintPass extends AbstractPass {
    protected Job job;
    protected PrettyPrinter pp;
    protected CodeWriter w;

    public PrettyPrintPass(Pass.ID id, Job job, CodeWriter w, PrettyPrinter pp) {
        super(id);
        this.job = job;
        this.pp = pp;
        this.w = w;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        Node ast = this.job.ast();
        if (ast == null) {
            this.w.write("<<<< null AST >>>>");
            return true;
        }
        this.pp.printAst(ast, this.w);
        return true;
    }
}
