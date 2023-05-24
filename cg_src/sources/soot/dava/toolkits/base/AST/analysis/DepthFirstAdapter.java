package soot.dava.toolkits.base.AST.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Immediate;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DInstanceFieldRef;
import soot.dava.internal.javaRep.DThisRef;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.jimple.internal.JimpleLocal;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/analysis/DepthFirstAdapter.class */
public class DepthFirstAdapter extends AnalysisAdapter {
    public boolean DEBUG;
    boolean verbose;

    public DepthFirstAdapter() {
        this.DEBUG = false;
        this.verbose = false;
    }

    public DepthFirstAdapter(boolean verbose) {
        this.DEBUG = false;
        this.verbose = false;
        this.verbose = verbose;
    }

    public void inASTMethodNode(ASTMethodNode node) {
        if (this.verbose) {
            System.out.println("inASTMethodNode");
        }
    }

    public void outASTMethodNode(ASTMethodNode node) {
        if (this.verbose) {
            System.out.println("outASTMethodNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTMethodNode(ASTMethodNode node) {
        inASTMethodNode(node);
        normalRetrieving(node);
        outASTMethodNode(node);
    }

    public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        if (this.verbose) {
            System.out.println("inASTSynchronizedBlockNode");
        }
    }

    public void outASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        if (this.verbose) {
            System.out.println("outASTSynchronizedBlockNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        inASTSynchronizedBlockNode(node);
        Value local = node.getLocal();
        decideCaseExprOrRef(local);
        normalRetrieving(node);
        outASTSynchronizedBlockNode(node);
    }

    public void inASTLabeledBlockNode(ASTLabeledBlockNode node) {
        if (this.verbose) {
            System.out.println("inASTLabeledBlockNode");
        }
    }

    public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
        if (this.verbose) {
            System.out.println("outASTLabeledBlockNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTLabeledBlockNode(ASTLabeledBlockNode node) {
        inASTLabeledBlockNode(node);
        normalRetrieving(node);
        outASTLabeledBlockNode(node);
    }

    public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        if (this.verbose) {
            System.out.println("inASTUnconditionalWhileNode");
        }
    }

    public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        if (this.verbose) {
            System.out.println("outASTUnconditionalWhileNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        inASTUnconditionalLoopNode(node);
        normalRetrieving(node);
        outASTUnconditionalLoopNode(node);
    }

    public void inASTSwitchNode(ASTSwitchNode node) {
        if (this.verbose) {
            System.out.println("inASTSwitchNode");
        }
    }

    public void outASTSwitchNode(ASTSwitchNode node) {
        if (this.verbose) {
            System.out.println("outASTSwitchNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTSwitchNode(ASTSwitchNode node) {
        inASTSwitchNode(node);
        caseExprOrRefValueBox(node.getKeyBox());
        normalRetrieving(node);
        outASTSwitchNode(node);
    }

    public void inASTIfNode(ASTIfNode node) {
        if (this.verbose) {
            System.out.println("inASTIfNode");
        }
    }

    public void outASTIfNode(ASTIfNode node) {
        if (this.verbose) {
            System.out.println("outASTIfNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTIfNode(ASTIfNode node) {
        inASTIfNode(node);
        ASTCondition condition = node.get_Condition();
        condition.apply(this);
        normalRetrieving(node);
        outASTIfNode(node);
    }

    public void inASTIfElseNode(ASTIfElseNode node) {
        if (this.verbose) {
            System.out.println("inASTIfElseNode");
        }
    }

    public void outASTIfElseNode(ASTIfElseNode node) {
        if (this.verbose) {
            System.out.println("outASTIfElseNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTIfElseNode(ASTIfElseNode node) {
        inASTIfElseNode(node);
        ASTCondition condition = node.get_Condition();
        condition.apply(this);
        normalRetrieving(node);
        outASTIfElseNode(node);
    }

    public void inASTWhileNode(ASTWhileNode node) {
        if (this.verbose) {
            System.out.println("inASTWhileNode");
        }
    }

    public void outASTWhileNode(ASTWhileNode node) {
        if (this.verbose) {
            System.out.println("outASTWhileNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTWhileNode(ASTWhileNode node) {
        inASTWhileNode(node);
        ASTCondition condition = node.get_Condition();
        condition.apply(this);
        normalRetrieving(node);
        outASTWhileNode(node);
    }

    public void inASTForLoopNode(ASTForLoopNode node) {
        if (this.verbose) {
            System.out.println("inASTForLoopNode");
        }
    }

    public void outASTForLoopNode(ASTForLoopNode node) {
        if (this.verbose) {
            System.out.println("outASTForLoopNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTForLoopNode(ASTForLoopNode node) {
        inASTForLoopNode(node);
        for (AugmentedStmt as : node.getInit()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                caseDefinitionStmt((DefinitionStmt) s);
            } else if (s instanceof ReturnStmt) {
                caseReturnStmt((ReturnStmt) s);
            } else if (s instanceof InvokeStmt) {
                caseInvokeStmt((InvokeStmt) s);
            } else if (s instanceof ThrowStmt) {
                caseThrowStmt((ThrowStmt) s);
            } else {
                caseStmt(s);
            }
        }
        ASTCondition condition = node.get_Condition();
        condition.apply(this);
        for (AugmentedStmt as2 : node.getUpdate()) {
            Stmt s2 = as2.get_Stmt();
            if (s2 instanceof DefinitionStmt) {
                caseDefinitionStmt((DefinitionStmt) s2);
            } else if (s2 instanceof ReturnStmt) {
                caseReturnStmt((ReturnStmt) s2);
            } else if (s2 instanceof InvokeStmt) {
                caseInvokeStmt((InvokeStmt) s2);
            } else if (s2 instanceof ThrowStmt) {
                caseThrowStmt((ThrowStmt) s2);
            } else {
                caseStmt(s2);
            }
        }
        normalRetrieving(node);
        outASTForLoopNode(node);
    }

    public void inASTDoWhileNode(ASTDoWhileNode node) {
        if (this.verbose) {
            System.out.println("inASTDoWhileNode");
        }
    }

    public void outASTDoWhileNode(ASTDoWhileNode node) {
        if (this.verbose) {
            System.out.println("outASTDoWhileNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTDoWhileNode(ASTDoWhileNode node) {
        inASTDoWhileNode(node);
        ASTCondition condition = node.get_Condition();
        condition.apply(this);
        normalRetrieving(node);
        outASTDoWhileNode(node);
    }

    public void inASTTryNode(ASTTryNode node) {
        if (this.verbose) {
            System.out.println("inASTTryNode");
        }
    }

    public void outASTTryNode(ASTTryNode node) {
        if (this.verbose) {
            System.out.println("outASTTryNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTTryNode(ASTTryNode node) {
        inASTTryNode(node);
        List<Object> tryBody = node.get_TryBody();
        Iterator<Object> it = tryBody.iterator();
        while (it.hasNext()) {
            ((ASTNode) it.next()).apply(this);
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
            List<ASTNode> body = (List) catchBody.o;
            for (ASTNode aSTNode : body) {
                aSTNode.apply(this);
            }
        }
        outASTTryNode(node);
    }

    public void inASTUnaryCondition(ASTUnaryCondition uc) {
        if (this.verbose) {
            System.out.println("inASTUnaryCondition");
        }
    }

    public void outASTUnaryCondition(ASTUnaryCondition uc) {
        if (this.verbose) {
            System.out.println("outASTUnaryCondition");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTUnaryCondition(ASTUnaryCondition uc) {
        inASTUnaryCondition(uc);
        decideCaseExprOrRef(uc.getValue());
        outASTUnaryCondition(uc);
    }

    public void inASTBinaryCondition(ASTBinaryCondition bc) {
        if (this.verbose) {
            System.out.println("inASTBinaryCondition");
        }
    }

    public void outASTBinaryCondition(ASTBinaryCondition bc) {
        if (this.verbose) {
            System.out.println("outASTBinaryCondition");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTBinaryCondition(ASTBinaryCondition bc) {
        inASTBinaryCondition(bc);
        ConditionExpr condition = bc.getConditionExpr();
        decideCaseExprOrRef(condition);
        outASTBinaryCondition(bc);
    }

    public void inASTAndCondition(ASTAndCondition ac) {
        if (this.verbose) {
            System.out.println("inASTAndCondition");
        }
    }

    public void outASTAndCondition(ASTAndCondition ac) {
        if (this.verbose) {
            System.out.println("outASTAndCondition");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTAndCondition(ASTAndCondition ac) {
        inASTAndCondition(ac);
        ac.getLeftOp().apply(this);
        ac.getRightOp().apply(this);
        outASTAndCondition(ac);
    }

    public void inASTOrCondition(ASTOrCondition oc) {
        if (this.verbose) {
            System.out.println("inASTOrCondition");
        }
    }

    public void outASTOrCondition(ASTOrCondition oc) {
        if (this.verbose) {
            System.out.println("outASTOrCondition");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTOrCondition(ASTOrCondition oc) {
        inASTOrCondition(oc);
        oc.getLeftOp().apply(this);
        oc.getRightOp().apply(this);
        outASTOrCondition(oc);
    }

    public void inType(Type t) {
        if (this.verbose) {
            System.out.println("inType");
        }
    }

    public void outType(Type t) {
        if (this.verbose) {
            System.out.println("outType");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseType(Type t) {
        inType(t);
        outType(t);
    }

    public void normalRetrieving(ASTNode node) {
        for (Object subBody : node.get_SubBodies()) {
            for (ASTNode temp : (List) subBody) {
                temp.apply(this);
            }
        }
    }

    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        if (this.verbose) {
            System.out.println("inASTStatementSequenceNode");
        }
    }

    public void outASTStatementSequenceNode(ASTStatementSequenceNode node) {
        if (this.verbose) {
            System.out.println("outASTStatementSequenceNode");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
        inASTStatementSequenceNode(node);
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                caseDefinitionStmt((DefinitionStmt) s);
            } else if (s instanceof ReturnStmt) {
                caseReturnStmt((ReturnStmt) s);
            } else if (s instanceof InvokeStmt) {
                caseInvokeStmt((InvokeStmt) s);
            } else if (s instanceof ThrowStmt) {
                caseThrowStmt((ThrowStmt) s);
            } else if (s instanceof DVariableDeclarationStmt) {
                caseDVariableDeclarationStmt((DVariableDeclarationStmt) s);
            } else {
                caseStmt(s);
            }
        }
        outASTStatementSequenceNode(node);
    }

    public void inDefinitionStmt(DefinitionStmt s) {
        if (this.verbose) {
            System.out.println("inDefinitionStmt" + s);
        }
    }

    public void outDefinitionStmt(DefinitionStmt s) {
        if (this.verbose) {
            System.out.println("outDefinitionStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseDefinitionStmt(DefinitionStmt s) {
        inDefinitionStmt(s);
        caseExprOrRefValueBox(s.getRightOpBox());
        caseExprOrRefValueBox(s.getLeftOpBox());
        outDefinitionStmt(s);
    }

    public void inReturnStmt(ReturnStmt s) {
        if (this.verbose) {
            System.out.println("inReturnStmt");
        }
    }

    public void outReturnStmt(ReturnStmt s) {
        if (this.verbose) {
            System.out.println("outReturnStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseReturnStmt(ReturnStmt s) {
        inReturnStmt(s);
        caseExprOrRefValueBox(s.getOpBox());
        outReturnStmt(s);
    }

    public void inInvokeStmt(InvokeStmt s) {
        if (this.verbose) {
            System.out.println("inInvokeStmt");
        }
    }

    public void outInvokeStmt(InvokeStmt s) {
        if (this.verbose) {
            System.out.println("outInvokeStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInvokeStmt(InvokeStmt s) {
        inInvokeStmt(s);
        caseExprOrRefValueBox(s.getInvokeExprBox());
        outInvokeStmt(s);
    }

    public void inThrowStmt(ThrowStmt s) {
        if (this.verbose) {
            System.out.println("\n\ninThrowStmt\n\n");
        }
    }

    public void outThrowStmt(ThrowStmt s) {
        if (this.verbose) {
            System.out.println("outThrowStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseThrowStmt(ThrowStmt s) {
        inThrowStmt(s);
        caseExprOrRefValueBox(s.getOpBox());
        outThrowStmt(s);
    }

    public void inDVariableDeclarationStmt(DVariableDeclarationStmt s) {
        if (this.verbose) {
            System.out.println("\n\ninDVariableDeclarationStmt\n\n" + s);
        }
    }

    public void outDVariableDeclarationStmt(DVariableDeclarationStmt s) {
        if (this.verbose) {
            System.out.println("outDVariableDeclarationStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseDVariableDeclarationStmt(DVariableDeclarationStmt s) {
        inDVariableDeclarationStmt(s);
        Type type = s.getType();
        caseType(type);
        List<Local> listDeclared = s.getDeclarations();
        for (Local declared : listDeclared) {
            decideCaseExprOrRef(declared);
        }
        outDVariableDeclarationStmt(s);
    }

    public void inStmt(Stmt s) {
        if (this.verbose) {
            System.out.println("inStmt: " + s);
        }
    }

    public void outStmt(Stmt s) {
        if (this.verbose) {
            System.out.println("outStmt");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseStmt(Stmt s) {
        inStmt(s);
        outStmt(s);
    }

    public void caseExprOrRefValueBox(ValueBox vb) {
        inExprOrRefValueBox(vb);
        decideCaseExprOrRef(vb.getValue());
        outExprOrRefValueBox(vb);
    }

    public void inExprOrRefValueBox(ValueBox vb) {
        if (this.verbose) {
            System.out.println("inExprOrRefValueBox" + vb);
        }
    }

    public void outExprOrRefValueBox(ValueBox vb) {
        if (this.verbose) {
            System.out.println("outExprOrRefValueBox" + vb);
        }
    }

    public void decideCaseExprOrRef(Value v) {
        if (v instanceof Expr) {
            caseExpr((Expr) v);
        } else if (v instanceof Ref) {
            caseRef((Ref) v);
        } else {
            caseValue(v);
        }
    }

    public void inValue(Value v) {
        if (this.verbose) {
            System.out.println("inValue" + v);
            if (v instanceof DThisRef) {
                System.out.println("DTHISREF.................");
            } else if (v instanceof Immediate) {
                System.out.println("\tIMMEDIATE");
                if (v instanceof JimpleLocal) {
                    System.out.println("\t\tJimpleLocal...................." + v);
                } else if (v instanceof Constant) {
                    System.out.println("\t\tconstant....................");
                    if (v instanceof IntConstant) {
                        System.out.println("\t\t INTconstant....................");
                    }
                } else if (v instanceof BafLocal) {
                    System.out.println("\t\tBafLocal....................");
                } else {
                    System.out.println("\t\telse!!!!!!!!!!!!");
                }
            } else {
                System.out.println("NEITHER................");
            }
        }
    }

    public void outValue(Value v) {
        if (this.verbose) {
            System.out.println("outValue");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseValue(Value v) {
        inValue(v);
        outValue(v);
    }

    public void inExpr(Expr e) {
        if (this.verbose) {
            System.out.println("inExpr");
        }
    }

    public void outExpr(Expr e) {
        if (this.verbose) {
            System.out.println("outExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseExpr(Expr e) {
        inExpr(e);
        decideCaseExpr(e);
        outExpr(e);
    }

    public void inRef(Ref r) {
        if (this.verbose) {
            System.out.println("inRef");
        }
    }

    public void outRef(Ref r) {
        if (this.verbose) {
            System.out.println("outRef");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseRef(Ref r) {
        inRef(r);
        decideCaseRef(r);
        outRef(r);
    }

    public void decideCaseExpr(Expr e) {
        if (e instanceof BinopExpr) {
            caseBinopExpr((BinopExpr) e);
        } else if (e instanceof UnopExpr) {
            caseUnopExpr((UnopExpr) e);
        } else if (e instanceof NewArrayExpr) {
            caseNewArrayExpr((NewArrayExpr) e);
        } else if (e instanceof NewMultiArrayExpr) {
            caseNewMultiArrayExpr((NewMultiArrayExpr) e);
        } else if (e instanceof InstanceOfExpr) {
            caseInstanceOfExpr((InstanceOfExpr) e);
        } else if (e instanceof InvokeExpr) {
            caseInvokeExpr((InvokeExpr) e);
        } else if (e instanceof CastExpr) {
            caseCastExpr((CastExpr) e);
        }
    }

    public void inBinopExpr(BinopExpr be) {
        if (this.verbose) {
            System.out.println("inBinopExpr");
        }
    }

    public void outBinopExpr(BinopExpr be) {
        if (this.verbose) {
            System.out.println("outBinopExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseBinopExpr(BinopExpr be) {
        inBinopExpr(be);
        caseExprOrRefValueBox(be.getOp1Box());
        caseExprOrRefValueBox(be.getOp2Box());
        outBinopExpr(be);
    }

    public void inUnopExpr(UnopExpr ue) {
        if (this.verbose) {
            System.out.println("inUnopExpr");
        }
    }

    public void outUnopExpr(UnopExpr ue) {
        if (this.verbose) {
            System.out.println("outUnopExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseUnopExpr(UnopExpr ue) {
        inUnopExpr(ue);
        caseExprOrRefValueBox(ue.getOpBox());
        outUnopExpr(ue);
    }

    public void inNewArrayExpr(NewArrayExpr nae) {
        if (this.verbose) {
            System.out.println("inNewArrayExpr");
        }
    }

    public void outNewArrayExpr(NewArrayExpr nae) {
        if (this.verbose) {
            System.out.println("outNewArrayExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseNewArrayExpr(NewArrayExpr nae) {
        inNewArrayExpr(nae);
        caseExprOrRefValueBox(nae.getSizeBox());
        outNewArrayExpr(nae);
    }

    public void inNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        if (this.verbose) {
            System.out.println("inNewMultiArrayExpr");
        }
    }

    public void outNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        if (this.verbose) {
            System.out.println("outNewMultiArrayExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        inNewMultiArrayExpr(nmae);
        for (int i = 0; i < nmae.getSizeCount(); i++) {
            caseExprOrRefValueBox(nmae.getSizeBox(i));
        }
        outNewMultiArrayExpr(nmae);
    }

    public void inInstanceOfExpr(InstanceOfExpr ioe) {
        if (this.verbose) {
            System.out.println("inInstanceOfExpr");
        }
    }

    public void outInstanceOfExpr(InstanceOfExpr ioe) {
        if (this.verbose) {
            System.out.println("outInstanceOfExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceOfExpr(InstanceOfExpr ioe) {
        inInstanceOfExpr(ioe);
        caseExprOrRefValueBox(ioe.getOpBox());
        outInstanceOfExpr(ioe);
    }

    public void inInvokeExpr(InvokeExpr ie) {
        if (this.verbose) {
            System.out.println("inInvokeExpr");
        }
    }

    public void outInvokeExpr(InvokeExpr ie) {
        if (this.verbose) {
            System.out.println("outInvokeExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInvokeExpr(InvokeExpr ie) {
        inInvokeExpr(ie);
        for (int i = 0; i < ie.getArgCount(); i++) {
            caseExprOrRefValueBox(ie.getArgBox(i));
        }
        if (ie instanceof InstanceInvokeExpr) {
            caseInstanceInvokeExpr((InstanceInvokeExpr) ie);
        }
        outInvokeExpr(ie);
    }

    public void inInstanceInvokeExpr(InstanceInvokeExpr iie) {
        if (this.verbose) {
            System.out.println("inInstanceInvokeExpr");
        }
    }

    public void outInstanceInvokeExpr(InstanceInvokeExpr iie) {
        if (this.verbose) {
            System.out.println("outInstanceInvokeExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceInvokeExpr(InstanceInvokeExpr iie) {
        inInstanceInvokeExpr(iie);
        caseExprOrRefValueBox(iie.getBaseBox());
        outInstanceInvokeExpr(iie);
    }

    public void inCastExpr(CastExpr ce) {
        if (this.verbose) {
            System.out.println("inCastExpr");
        }
    }

    public void outCastExpr(CastExpr ce) {
        if (this.verbose) {
            System.out.println("outCastExpr");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseCastExpr(CastExpr ce) {
        inCastExpr(ce);
        Type type = ce.getCastType();
        caseType(type);
        caseExprOrRefValueBox(ce.getOpBox());
        outCastExpr(ce);
    }

    public void decideCaseRef(Ref r) {
        if (r instanceof ArrayRef) {
            caseArrayRef((ArrayRef) r);
        } else if (r instanceof InstanceFieldRef) {
            caseInstanceFieldRef((InstanceFieldRef) r);
        } else if (r instanceof StaticFieldRef) {
            caseStaticFieldRef((StaticFieldRef) r);
        }
    }

    public void inArrayRef(ArrayRef ar) {
        if (this.verbose) {
            System.out.println("inArrayRef");
        }
    }

    public void outArrayRef(ArrayRef ar) {
        if (this.verbose) {
            System.out.println("outArrayRef");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseArrayRef(ArrayRef ar) {
        inArrayRef(ar);
        caseExprOrRefValueBox(ar.getBaseBox());
        caseExprOrRefValueBox(ar.getIndexBox());
        outArrayRef(ar);
    }

    public void inInstanceFieldRef(InstanceFieldRef ifr) {
        if (this.verbose) {
            System.out.println("inInstanceFieldRef");
            if (ifr instanceof DInstanceFieldRef) {
                System.out.println("...........DINSTANCEFIELDREF");
            }
        }
    }

    public void outInstanceFieldRef(InstanceFieldRef ifr) {
        if (this.verbose) {
            System.out.println("outInstanceFieldRef");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceFieldRef(InstanceFieldRef ifr) {
        inInstanceFieldRef(ifr);
        caseExprOrRefValueBox(ifr.getBaseBox());
        outInstanceFieldRef(ifr);
    }

    public void inStaticFieldRef(StaticFieldRef sfr) {
        if (this.verbose) {
            System.out.println("inStaticFieldRef");
        }
    }

    public void outStaticFieldRef(StaticFieldRef sfr) {
        if (this.verbose) {
            System.out.println("outStaticFieldRef");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseStaticFieldRef(StaticFieldRef sfr) {
        inStaticFieldRef(sfr);
        outStaticFieldRef(sfr);
    }

    public void debug(String className, String methodName, String debug) {
        if (this.DEBUG) {
            System.out.println("Analysis" + className + "..Method:" + methodName + "    DEBUG: " + debug);
        }
    }
}
