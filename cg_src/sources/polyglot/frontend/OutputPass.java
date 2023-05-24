package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.util.InternalCompilerError;
import polyglot.visit.Translator;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/OutputPass.class */
public class OutputPass extends AbstractPass {
    protected Job job;
    protected Translator translator;

    public OutputPass(Pass.ID id, Job job, Translator translator) {
        super(id);
        this.job = job;
        this.translator = translator;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        Node ast = this.job.ast();
        if (ast == null) {
            throw new InternalCompilerError("AST is null");
        }
        return this.translator.translate(ast);
    }
}
