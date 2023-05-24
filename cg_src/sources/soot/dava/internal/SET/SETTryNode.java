package soot.dava.internal.SET;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.toolkits.base.finders.ExceptionFinder;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETTryNode.class */
public class SETTryNode extends SETNode {
    private ExceptionNode en;
    private DavaBody davaBody;
    private AugmentedStmtGraph asg;
    private final HashMap<IterableSet, IterableSet> cb2clone;

    public SETTryNode(IterableSet body, ExceptionNode en, AugmentedStmtGraph asg, DavaBody davaBody) {
        super(body);
        this.en = en;
        this.asg = asg;
        this.davaBody = davaBody;
        add_SubBody(en.get_TryBody());
        this.cb2clone = new HashMap<>();
        for (IterableSet<AugmentedStmt> catchBody : en.get_CatchList()) {
            IterableSet clone = (IterableSet) catchBody.clone();
            this.cb2clone.put(catchBody, clone);
            add_SubBody(clone);
        }
        this.entryStmt = null;
        Iterator it = body.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = (AugmentedStmt) it.next();
            for (AugmentedStmt augmentedStmt : as.cpreds) {
                if (!body.contains(augmentedStmt)) {
                    this.entryStmt = as;
                    return;
                }
            }
        }
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        if (this.entryStmt != null) {
            return this.entryStmt;
        }
        return this.en.get_TryBody().getFirst();
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        IterableSet c = new IterableSet();
        Iterator<IterableSet> it = this.subBodies.iterator();
        while (it.hasNext()) {
            Iterator eit = ((SETNode) this.body2childChain.get(it.next()).getLast()).get_NaturalExits().iterator();
            while (eit.hasNext()) {
                Object o = eit.next();
                if (!c.contains(o)) {
                    c.add(o);
                }
            }
        }
        return c;
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        LinkedList<Object> catchList = new LinkedList<>();
        HashMap<Object, Object> exceptionMap = new HashMap<>();
        HashMap<Object, Object> paramMap = new HashMap<>();
        for (IterableSet<AugmentedStmt> originalCatchBody : this.en.get_CatchList()) {
            IterableSet catchBody = this.cb2clone.get(originalCatchBody);
            List<Object> astBody = emit_ASTBody(this.body2childChain.get(catchBody));
            exceptionMap.put(astBody, this.en.get_Exception(originalCatchBody));
            catchList.addLast(astBody);
            Iterator bit = catchBody.iterator();
            while (true) {
                if (!bit.hasNext()) {
                    break;
                }
                Stmt s = ((AugmentedStmt) bit.next()).get_Stmt();
                if (s instanceof GotoStmt) {
                    s = (Stmt) ((GotoStmt) s).getTarget();
                }
                if (s instanceof IdentityStmt) {
                    IdentityStmt ids = (IdentityStmt) s;
                    Value rightOp = ids.getRightOp();
                    Value leftOp = ids.getLeftOp();
                    if (rightOp instanceof CaughtExceptionRef) {
                        paramMap.put(astBody, leftOp);
                        break;
                    }
                }
            }
        }
        return new ASTTryNode(get_Label(), emit_ASTBody(this.body2childChain.get(this.en.get_TryBody())), catchList, exceptionMap, paramMap);
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        for (IterableSet subBody : parent.get_SubBodies()) {
            if (subBody.intersects(this.en.get_TryBody())) {
                IterableSet childChain = parent.get_Body2ChildChain().get(subBody);
                Iterator ccit = childChain.iterator();
                while (ccit.hasNext()) {
                    SETNode child = (SETNode) ccit.next();
                    IterableSet childBody = child.get_Body();
                    if (childBody.intersects(this.en.get_TryBody()) && !childBody.isSubsetOf(this.en.get_TryBody())) {
                        if (childBody.isSupersetOf(get_Body())) {
                            return true;
                        }
                        IterableSet newTryBody = childBody.intersection(this.en.get_TryBody());
                        if (newTryBody.isStrictSubsetOf(this.en.get_TryBody())) {
                            this.en.splitOff_ExceptionNode(newTryBody, this.asg, this.davaBody.get_ExceptionFacts());
                            Iterator enlit = this.davaBody.get_ExceptionFacts().iterator();
                            while (enlit.hasNext()) {
                                enlit.next().refresh_CatchBody(ExceptionFinder.v());
                            }
                            return false;
                        }
                        for (IterableSet<AugmentedStmt> iterableSet : this.en.get_CatchList()) {
                            Iterator bit = this.cb2clone.get(iterableSet).snapshotIterator();
                            while (bit.hasNext()) {
                                AugmentedStmt as = (AugmentedStmt) bit.next();
                                if (!childBody.contains(as)) {
                                    remove_AugmentedStmt(as);
                                } else if ((child instanceof SETControlFlowNode) && !(child instanceof SETUnconditionalWhileNode)) {
                                    SETControlFlowNode scfn = (SETControlFlowNode) child;
                                    if (scfn.get_CharacterizingStmt() == as || (as.cpreds.size() == 1 && (as.get_Stmt() instanceof GotoStmt) && scfn.get_CharacterizingStmt() == as.cpreds.get(0))) {
                                        remove_AugmentedStmt(as);
                                    }
                                }
                            }
                        }
                        return true;
                    }
                }
                continue;
            }
        }
        return true;
    }
}
