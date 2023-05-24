package soot.dava.internal.SET;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETStatementSequenceNode.class */
public class SETStatementSequenceNode extends SETNode {
    private DavaBody davaBody;
    private boolean hasContinue;

    public SETStatementSequenceNode(IterableSet body, DavaBody davaBody) {
        super(body);
        add_SubBody(body);
        this.davaBody = davaBody;
        this.hasContinue = false;
    }

    public SETStatementSequenceNode(IterableSet body) {
        this(body, null);
    }

    public boolean has_Continue() {
        return this.hasContinue;
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        IterableSet c = new IterableSet();
        AugmentedStmt last = get_Body().getLast();
        if (last.csuccs != null && !last.csuccs.isEmpty()) {
            c.add(last);
        }
        return c;
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        List<AugmentedStmt> l = new LinkedList<>();
        boolean isStaticInitializer = this.davaBody.getMethod().getName().equals("<clinit>");
        Iterator it = get_Body().iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            Stmt s = as.get_Stmt();
            if (this.davaBody != null) {
                if (!(s instanceof ReturnVoidStmt) || !isStaticInitializer) {
                    if (!(s instanceof GotoStmt) && !(s instanceof MonitorStmt)) {
                        this.davaBody.get_ConstructorUnit();
                        if (s instanceof IdentityStmt) {
                            IdentityStmt ids = (IdentityStmt) s;
                            Value rightOp = ids.getRightOp();
                            Value leftOp = ids.getLeftOp();
                            if (!this.davaBody.get_ThisLocals().contains(leftOp) && !(rightOp instanceof ParameterRef) && !(rightOp instanceof CaughtExceptionRef)) {
                            }
                        }
                    }
                }
            }
            l.add(as);
        }
        if (l.isEmpty()) {
            return null;
        }
        return new ASTStatementSequenceNode(l);
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return get_Body().getFirst();
    }

    public void insert_AbruptStmt(DAbruptStmt stmt) {
        if (this.hasContinue) {
            return;
        }
        get_Body().addLast(new AugmentedStmt(stmt));
        this.hasContinue = stmt.is_Continue();
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        throw new RuntimeException("Attempting auto-nest a SETStatementSequenceNode.");
    }
}
