package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.coffi.Instruction;
import soot.dava.DavaBody;
import soot.dava.DavaUnitPrinter;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.DeterministicHashMap;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTMethodNode.class */
public class ASTMethodNode extends ASTNode {
    private List<Object> body;
    private DavaBody davaBody;
    private ASTStatementSequenceNode declarations;
    private List<Local> dontPrintLocals = new ArrayList();

    public ASTStatementSequenceNode getDeclarations() {
        return this.declarations;
    }

    public void setDeclarations(ASTStatementSequenceNode decl) {
        this.declarations = decl;
    }

    public void setDavaBody(DavaBody bod) {
        this.davaBody = bod;
    }

    public DavaBody getDavaBody() {
        return this.davaBody;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void storeLocals(Body OrigBody) {
        List<Local> localList;
        if (!(OrigBody instanceof DavaBody)) {
            throw new RuntimeException("Only DavaBodies should invoke this method");
        }
        this.davaBody = (DavaBody) OrigBody;
        Map<Type, List<Local>> typeToLocals = new DeterministicHashMap<>((OrigBody.getLocalCount() * 2) + 1, 0.7f);
        HashSet params = new HashSet();
        params.addAll(this.davaBody.get_ParamMap().values());
        params.addAll(this.davaBody.get_CaughtRefs());
        HashSet<Object> thisLocals = this.davaBody.get_ThisLocals();
        for (Local local : OrigBody.getLocals()) {
            if (!params.contains(local) && !thisLocals.contains(local)) {
                Type t = local.getType();
                t.toString();
                if (typeToLocals.containsKey(t)) {
                    localList = typeToLocals.get(t);
                } else {
                    localList = new ArrayList<>();
                    typeToLocals.put(t, localList);
                }
                localList.add(local);
            }
        }
        List<AugmentedStmt> statementSequence = new ArrayList<>();
        for (Type typeObject : typeToLocals.keySet()) {
            typeObject.toString();
            DVariableDeclarationStmt varStmt = new DVariableDeclarationStmt(typeObject, this.davaBody);
            for (Local element : typeToLocals.get(typeObject)) {
                varStmt.addLocal(element);
            }
            AugmentedStmt as = new AugmentedStmt(varStmt);
            statementSequence.add(as);
        }
        this.declarations = new ASTStatementSequenceNode(statementSequence);
        this.body.add(0, this.declarations);
        this.subBodies = new ArrayList();
        this.subBodies.add(this.body);
    }

    public ASTMethodNode(List<Object> body) {
        this.body = body;
        this.subBodies.add(body);
    }

    public List getDeclaredLocals() {
        List toReturn = new ArrayList();
        for (AugmentedStmt as : this.declarations.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DVariableDeclarationStmt) {
                DVariableDeclarationStmt varStmt = (DVariableDeclarationStmt) s;
                List<Object> declarations = varStmt.getDeclarations();
                for (Object obj : declarations) {
                    toReturn.add(obj);
                }
            }
        }
        return toReturn;
    }

    public void removeDeclaredLocal(Local local) {
        Iterator<AugmentedStmt> it = this.declarations.getStatements().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Stmt s = it.next().get_Stmt();
            if (s instanceof DVariableDeclarationStmt) {
                DVariableDeclarationStmt varStmt = (DVariableDeclarationStmt) s;
                List declarations = varStmt.getDeclarations();
                Iterator decIt = declarations.iterator();
                boolean foundIt = false;
                while (true) {
                    if (!decIt.hasNext()) {
                        break;
                    }
                    Local temp = (Local) decIt.next();
                    if (temp.getName().compareTo(local.getName()) == 0) {
                        foundIt = true;
                        break;
                    }
                }
                if (foundIt) {
                    varStmt.removeLocal(local);
                    break;
                }
            }
        }
        List<AugmentedStmt> newSequence = new ArrayList<>();
        for (AugmentedStmt as : this.declarations.getStatements()) {
            Stmt s2 = as.get_Stmt();
            if ((s2 instanceof DVariableDeclarationStmt) && ((DVariableDeclarationStmt) s2).getDeclarations().size() != 0) {
                newSequence.add(as);
            }
        }
        this.declarations.setStatements(newSequence);
    }

    public void replaceBody(List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        ASTMethodNode toReturn = new ASTMethodNode(this.body);
        toReturn.setDeclarations((ASTStatementSequenceNode) this.declarations.clone());
        toReturn.setDontPrintLocals(this.dontPrintLocals);
        return toReturn;
    }

    public void setDontPrintLocals(List<Local> list) {
        this.dontPrintLocals = list;
    }

    public void addToDontPrintLocalsList(Local toAdd) {
        this.dontPrintLocals.add(toAdd);
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void perform_Analysis(ASTAnalysis a) {
        perform_AnalysisOnSubBodies(a);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        if (!(up instanceof DavaUnitPrinter)) {
            throw new RuntimeException("Only DavaUnitPrinter should be used to print DavaBody");
        }
        DavaUnitPrinter dup = (DavaUnitPrinter) up;
        if (this.davaBody != null) {
            InstanceInvokeExpr constructorExpr = this.davaBody.get_ConstructorExpr();
            if (constructorExpr != null) {
                boolean printCloseBrace = true;
                if (this.davaBody.getMethod().getDeclaringClass().getName().equals(constructorExpr.getMethodRef().declaringClass().toString())) {
                    dup.printString("        this(");
                } else if (constructorExpr.getArgCount() > 0) {
                    dup.printString("        super(");
                } else {
                    printCloseBrace = false;
                }
                Iterator ait = constructorExpr.getArgs().iterator();
                while (ait.hasNext()) {
                    Object arg = ait.next();
                    if (arg instanceof Value) {
                        dup.noIndent();
                        ((Value) arg).toString(dup);
                    } else {
                        dup.printString(arg.toString());
                    }
                    if (ait.hasNext()) {
                        dup.printString(", ");
                    }
                }
                if (printCloseBrace) {
                    dup.printString(");\n");
                }
            }
            up.newline();
        }
        printDeclarationsFollowedByBody(up, this.body);
    }

    public void printDeclarationsFollowedByBody(UnitPrinter up, List<Object> body) {
        for (AugmentedStmt as : this.declarations.getStatements()) {
            Unit u = as.get_Stmt();
            if (u instanceof DVariableDeclarationStmt) {
                DVariableDeclarationStmt declStmt = (DVariableDeclarationStmt) u;
                List<Local> localDeclarations = declStmt.getDeclarations();
                boolean shouldContinue = false;
                Iterator declsIt = localDeclarations.iterator();
                while (true) {
                    if (declsIt.hasNext()) {
                        if (!this.dontPrintLocals.contains(declsIt.next())) {
                            shouldContinue = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (shouldContinue && localDeclarations.size() != 0) {
                    if (!(up instanceof DavaUnitPrinter)) {
                        throw new RuntimeException("DavaBody should always be printed using the DavaUnitPrinter");
                    }
                    DavaUnitPrinter dup = (DavaUnitPrinter) up;
                    dup.startUnit(u);
                    String type = declStmt.getType().toString();
                    if (type.equals(Jimple.NULL_TYPE)) {
                        dup.printString("Object");
                    } else {
                        IterableSet importSet = this.davaBody.getImportList();
                        if (!importSet.contains(type)) {
                            this.davaBody.addToImportList(type);
                        }
                        dup.printString(RemoveFullyQualifiedName.getReducedName(this.davaBody.getImportList(), type, declStmt.getType()));
                    }
                    dup.printString(Instruction.argsep);
                    int number = 0;
                    for (Local tempDec : localDeclarations) {
                        if (!this.dontPrintLocals.contains(tempDec)) {
                            if (number != 0) {
                                dup.printString(", ");
                            }
                            number++;
                            dup.printString(tempDec.getName());
                        }
                    }
                    up.literal(";");
                    up.endUnit(u);
                    up.newline();
                }
            } else {
                up.startUnit(u);
                u.toString(up);
                up.literal(";");
                up.endUnit(u);
                up.newline();
            }
        }
        boolean printed = false;
        if (body.size() > 0) {
            ASTNode firstNode = (ASTNode) body.get(0);
            if (firstNode instanceof ASTStatementSequenceNode) {
                List<AugmentedStmt> tempstmts = ((ASTStatementSequenceNode) firstNode).getStatements();
                if (tempstmts.size() != 0) {
                    AugmentedStmt tempas = tempstmts.get(0);
                    Stmt temps = tempas.get_Stmt();
                    if (temps instanceof DVariableDeclarationStmt) {
                        printed = true;
                        body_toString(up, body.subList(1, body.size()));
                    }
                }
            }
        }
        if (!printed) {
            body_toString(up, body);
        }
    }

    public String toString() {
        InstanceInvokeExpr constructorExpr;
        StringBuffer b = new StringBuffer();
        if (this.davaBody != null && (constructorExpr = this.davaBody.get_ConstructorExpr()) != null) {
            if (this.davaBody.getMethod().getDeclaringClass().getName().equals(constructorExpr.getMethodRef().declaringClass().toString())) {
                b.append("        this(");
            } else {
                b.append("        super(");
            }
            boolean isFirst = true;
            for (Value val : constructorExpr.getArgs()) {
                if (!isFirst) {
                    b.append(", ");
                }
                b.append(val.toString());
                isFirst = false;
            }
            b.append(");\n\n");
        }
        b.append(body_toString(this.body));
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTMethodNode(this);
    }
}
