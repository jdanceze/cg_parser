package polyglot.visit;

import java.util.HashMap;
import java.util.Map;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ConstructorCallChecker.class */
public class ConstructorCallChecker extends ContextVisitor {
    protected Map constructorInvocations;

    public ConstructorCallChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
        this.constructorInvocations = new HashMap();
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        if (n instanceof ConstructorCall) {
            ConstructorCall cc = (ConstructorCall) n;
            if (cc.kind() == ConstructorCall.THIS) {
                Context ctxt = context();
                if (!(ctxt.currentCode() instanceof ConstructorInstance)) {
                    throw new InternalCompilerError("Constructor call occurring in a non-constructor.", cc.position());
                }
                ConstructorInstance srcCI = (ConstructorInstance) ctxt.currentCode();
                ConstructorInstance destCI = cc.constructorInstance();
                this.constructorInvocations.put(srcCI, destCI);
                while (destCI != null) {
                    destCI = (ConstructorInstance) this.constructorInvocations.get(destCI);
                    if (destCI != null && srcCI.equals(destCI)) {
                        throw new SemanticException("Recursive constructor invocation.", cc.position());
                    }
                }
            }
        }
        return this;
    }
}
