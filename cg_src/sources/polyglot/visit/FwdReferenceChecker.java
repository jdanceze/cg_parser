package polyglot.visit;

import java.util.HashSet;
import java.util.Set;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FwdReferenceChecker.class */
public class FwdReferenceChecker extends ContextVisitor {
    private boolean inInitialization;
    private boolean inStaticInit;
    private Field fieldAssignLHS;
    private Set declaredFields;

    public FwdReferenceChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
        this.inInitialization = false;
        this.inStaticInit = false;
        this.fieldAssignLHS = null;
        this.declaredFields = new HashSet();
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        if (n instanceof FieldDecl) {
            FieldDecl fd = (FieldDecl) n;
            this.declaredFields.add(fd.fieldInstance());
            FwdReferenceChecker frc = (FwdReferenceChecker) copy();
            frc.inInitialization = true;
            frc.inStaticInit = fd.flags().isStatic();
            return frc;
        } else if (n instanceof Initializer) {
            FwdReferenceChecker frc2 = (FwdReferenceChecker) copy();
            frc2.inInitialization = true;
            frc2.inStaticInit = ((Initializer) n).flags().isStatic();
            return frc2;
        } else if (n instanceof FieldAssign) {
            FwdReferenceChecker frc3 = (FwdReferenceChecker) copy();
            frc3.fieldAssignLHS = (Field) ((FieldAssign) n).left();
            return frc3;
        } else {
            if (n instanceof Field) {
                if (this.fieldAssignLHS == n) {
                    this.fieldAssignLHS = null;
                } else if (this.inInitialization) {
                    Field f = (Field) n;
                    if (this.inStaticInit == f.fieldInstance().flags().isStatic() && context().currentClass().equals(f.fieldInstance().container()) && !this.declaredFields.contains(f.fieldInstance()) && f.isTargetImplicit()) {
                        throw new SemanticException("Illegal forward reference", f.position());
                    }
                }
            }
            return this;
        }
    }
}
