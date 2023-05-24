package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.ASTUsesAndDefs;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/LocalVariableCleaner.class */
public class LocalVariableCleaner extends DepthFirstAdapter {
    public final boolean DEBUG = false;
    ASTNode AST;
    ASTUsesAndDefs useDefs;
    ASTParentNodeFinder parentOf;

    public LocalVariableCleaner(ASTNode AST) {
        this.DEBUG = false;
        this.AST = AST;
        this.parentOf = new ASTParentNodeFinder();
        AST.apply(this.parentOf);
    }

    public LocalVariableCleaner(boolean verbose, ASTNode AST) {
        super(verbose);
        this.DEBUG = false;
        this.AST = AST;
        this.parentOf = new ASTParentNodeFinder();
        AST.apply(this.parentOf);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTMethodNode(ASTMethodNode node) {
        boolean redo = false;
        this.useDefs = new ASTUsesAndDefs(this.AST);
        this.AST.apply(this.useDefs);
        ArrayList<Local> removeList = new ArrayList<>();
        for (Local var : node.getDeclaredLocals()) {
            List<DefinitionStmt> defs = getDefs(var);
            if (defs.size() == 0) {
                removeList.add(var);
            } else {
                for (DefinitionStmt ds : defs) {
                    if (canRemoveDef(ds)) {
                        redo = removeStmt(ds);
                    }
                }
            }
        }
        Iterator<Local> remIt = removeList.iterator();
        while (remIt.hasNext()) {
            Local removeLocal = remIt.next();
            node.removeDeclaredLocal(removeLocal);
            if (this.AST instanceof ASTMethodNode) {
                DavaBody body = ((ASTMethodNode) this.AST).getDavaBody();
                Collection<Local> localChain = body.getLocals();
                if (removeLocal != null && localChain != null) {
                    localChain.remove(removeLocal);
                }
                redo = true;
            } else {
                throw new DecompilationException("found AST which is not a methodNode");
            }
        }
        if (redo) {
            outASTMethodNode(node);
        }
    }

    public boolean canRemoveDef(DefinitionStmt ds) {
        List uses = this.useDefs.getDUChain(ds);
        if (uses.size() != 0) {
            return false;
        }
        if ((ds.getRightOp() instanceof Local) || (ds.getRightOp() instanceof Constant)) {
            return true;
        }
        return false;
    }

    public List<DefinitionStmt> getDefs(Local var) {
        List<DefinitionStmt> toReturn = new ArrayList<>();
        HashMap<Object, List> dU = this.useDefs.getDUHashMap();
        Iterator<Object> it = dU.keySet().iterator();
        while (it.hasNext()) {
            DefinitionStmt s = (DefinitionStmt) it.next();
            Value left = s.getLeftOp();
            if ((left instanceof Local) && ((Local) left).getName().compareTo(var.getName()) == 0) {
                toReturn.add(s);
            }
        }
        return toReturn;
    }

    public boolean removeStmt(Stmt stmt) {
        Object tempParent = this.parentOf.getParentOf(stmt);
        if (tempParent == null) {
            return false;
        }
        ASTNode parent = (ASTNode) tempParent;
        if (!(parent instanceof ASTStatementSequenceNode)) {
            return false;
        }
        ASTStatementSequenceNode parentNode = (ASTStatementSequenceNode) parent;
        ArrayList<AugmentedStmt> newSequence = new ArrayList<>();
        int size = parentNode.getStatements().size();
        for (AugmentedStmt as : parentNode.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s.toString().compareTo(stmt.toString()) != 0) {
                newSequence.add(as);
            }
        }
        parentNode.setStatements(newSequence);
        if (newSequence.size() < size) {
            return true;
        }
        return false;
    }
}
