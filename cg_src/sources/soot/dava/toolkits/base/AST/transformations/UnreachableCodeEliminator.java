package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.structuredAnalysis.UnreachableCodeFinder;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/UnreachableCodeEliminator.class */
public class UnreachableCodeEliminator extends DepthFirstAdapter {
    public boolean DUBUG;
    ASTNode AST;
    UnreachableCodeFinder codeFinder;

    public UnreachableCodeEliminator(ASTNode AST) {
        this.DUBUG = true;
        this.AST = AST;
        setup();
    }

    public UnreachableCodeEliminator(boolean verbose, ASTNode AST) {
        super(verbose);
        this.DUBUG = true;
        this.AST = AST;
        setup();
    }

    private void setup() {
        this.codeFinder = new UnreachableCodeFinder(this.AST);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        List<AugmentedStmt> toRemove = new ArrayList<>();
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            if (!this.codeFinder.isConstructReachable(s)) {
                toRemove.add(as);
            }
        }
        for (AugmentedStmt as2 : toRemove) {
            node.getStatements().remove(as2);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void normalRetrieving(ASTNode node) {
        if (node instanceof ASTSwitchNode) {
            dealWithSwitchNode((ASTSwitchNode) node);
            return;
        }
        List<ASTNode> toReturn = new ArrayList<>();
        for (Object subBody : node.get_SubBodies()) {
            for (ASTNode temp : (List) subBody) {
                if (!this.codeFinder.isConstructReachable(temp)) {
                    toReturn.add(temp);
                } else {
                    temp.apply(this);
                }
            }
            for (ASTNode aSTNode : toReturn) {
                ((List) subBody).remove(aSTNode);
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTTryNode(ASTTryNode node) {
        List<Object> tryBody = node.get_TryBody();
        Iterator<Object> it = tryBody.iterator();
        List<Object> toReturn = new ArrayList<>();
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (!this.codeFinder.isConstructReachable(temp)) {
                toReturn.add(temp);
            } else {
                temp.apply(this);
            }
        }
        for (Object obj : toReturn) {
            tryBody.remove(obj);
        }
        Map<Object, Object> exceptionMap = node.get_ExceptionMap();
        Map<Object, Object> paramMap = node.get_ParamMap();
        List<Object> catchList = node.get_CatchList();
        Iterator<Object> it2 = catchList.iterator();
        while (it2.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container) it2.next();
            SootClass sootClass = (SootClass) exceptionMap.get(catchBody);
            Type type = sootClass.getType();
            caseType(type);
            Local local = (Local) paramMap.get(catchBody);
            decideCaseExprOrRef(local);
            List<Object> body = (List) catchBody.o;
            List<Object> toReturn2 = new ArrayList<>();
            Iterator<Object> itBody = body.iterator();
            while (itBody.hasNext()) {
                ASTNode temp2 = (ASTNode) itBody.next();
                if (!this.codeFinder.isConstructReachable(temp2)) {
                    toReturn2.add(temp2);
                } else {
                    temp2.apply(this);
                }
            }
            for (Object obj2 : toReturn2) {
                body.remove(obj2);
            }
        }
    }

    private void dealWithSwitchNode(ASTSwitchNode node) {
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        for (Object currentIndex : indexList) {
            List<ASTNode> body = index2BodyList.get(currentIndex);
            if (body != null) {
                List<ASTNode> toReturn = new ArrayList<>();
                for (ASTNode temp : body) {
                    if (!this.codeFinder.isConstructReachable(temp)) {
                        toReturn.add(temp);
                    } else {
                        temp.apply(this);
                    }
                }
                for (ASTNode aSTNode : toReturn) {
                    body.remove(aSTNode);
                }
            }
        }
    }
}
