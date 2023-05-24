package polyglot.frontend;

import java.io.IOException;
import java.io.Reader;
import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.main.Report;
import polyglot.util.ErrorQueue;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/ParserPass.class */
public class ParserPass extends AbstractPass {
    Job job;
    Compiler compiler;

    public ParserPass(Pass.ID id, Compiler compiler, Job job) {
        super(id);
        this.compiler = compiler;
        this.job = job;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        ErrorQueue eq = this.compiler.errorQueue();
        FileSource source = (FileSource) this.job.source();
        try {
            Reader reader = source.open();
            Parser p = this.compiler.sourceExtension().parser(reader, source, eq);
            if (Report.should_report(Report.frontend, 2)) {
                Report.report(2, new StringBuffer().append("Using parser ").append(p).toString());
            }
            Node ast = p.parse();
            source.close();
            if (ast != null) {
                this.job.ast(ast);
                return true;
            }
            return false;
        } catch (IOException e) {
            eq.enqueue(2, e.getMessage());
            return false;
        }
    }

    @Override // polyglot.frontend.AbstractPass
    public String toString() {
        return new StringBuffer().append(this.id).append("(").append(this.job.source()).append(")").toString();
    }
}
