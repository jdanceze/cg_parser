package soot.dava.toolkits.base.AST.transformations;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DInstanceFieldRef;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DStaticFieldRef;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.toolkits.base.AST.structuredAnalysis.MustMayInitialize;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.AllVariableUses;
import soot.grimp.internal.GAssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/FinalFieldDefinition.class */
public class FinalFieldDefinition {
    SootClass sootClass;
    SootMethod sootMethod;
    DavaBody davaBody;
    List<SootField> cancelFinalModifier;

    public FinalFieldDefinition(ASTMethodNode node) {
        this.davaBody = node.getDavaBody();
        this.sootMethod = this.davaBody.getMethod();
        this.sootClass = this.sootMethod.getDeclaringClass();
        String subSignature = this.sootMethod.getName();
        if (subSignature.compareTo("<clinit>") != 0 && subSignature.compareTo("<init>") != 0) {
            return;
        }
        ArrayList<SootField> interesting = findFinalFields();
        if (interesting.isEmpty()) {
            return;
        }
        this.cancelFinalModifier = new ArrayList();
        analyzeMethod(node, interesting);
        for (SootField field : this.cancelFinalModifier) {
            field.setModifiers(65519 & field.getModifiers());
        }
    }

    public ArrayList<SootField> findFinalFields() {
        ArrayList<SootField> interestingFinalFields = new ArrayList<>();
        for (SootField tempField : this.sootClass.getFields()) {
            if (tempField.isFinal()) {
                if (tempField.isStatic() && this.sootMethod.getName().compareTo("<clinit>") == 0) {
                    interestingFinalFields.add(tempField);
                }
                if (!tempField.isStatic() && this.sootMethod.getName().compareTo("<init>") == 0) {
                    interestingFinalFields.add(tempField);
                }
            }
        }
        return interestingFinalFields;
    }

    public void analyzeMethod(ASTMethodNode node, List<SootField> varsOfInterest) {
        MustMayInitialize must = new MustMayInitialize(node, 0);
        for (SootField interest : varsOfInterest) {
            Type fieldType = interest.getType();
            if (!(fieldType instanceof DoubleType) || !interest.hasTag(DoubleConstantValueTag.NAME)) {
                if (!(fieldType instanceof FloatType) || !interest.hasTag(FloatConstantValueTag.NAME)) {
                    if (!(fieldType instanceof LongType) || !interest.hasTag(LongConstantValueTag.NAME)) {
                        if (!(fieldType instanceof CharType) || !interest.hasTag(IntegerConstantValueTag.NAME)) {
                            if (!(fieldType instanceof BooleanType) || !interest.hasTag(IntegerConstantValueTag.NAME)) {
                                if ((!(fieldType instanceof IntType) && !(fieldType instanceof ByteType) && !(fieldType instanceof ShortType)) || !interest.hasTag(IntegerConstantValueTag.NAME)) {
                                    if (!interest.hasTag(StringConstantValueTag.NAME) && !must.isMustInitialized(interest)) {
                                        MustMayInitialize may = new MustMayInitialize(node, 1);
                                        if (may.isMayInitialized(interest)) {
                                            List defs = must.getDefs(interest);
                                            if (defs == null) {
                                                throw new RuntimeException("Sootfield: " + interest + " is mayInitialized but the defs is null");
                                            }
                                            handleAssignOnSomePaths(node, interest, defs);
                                        } else {
                                            assignDefault(node, interest);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void assignDefault(ASTMethodNode node, SootField f) {
        AugmentedStmt defaultStmt = createDefaultStmt(f);
        if (defaultStmt == null) {
            return;
        }
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new RuntimeException("SubBodies size of method node not equal to 1");
        }
        List<Object> body = (List) subBodies.get(0);
        boolean done = false;
        if (!body.isEmpty()) {
            ASTNode lastNode = (ASTNode) body.get(body.size() - 1);
            if (lastNode instanceof ASTStatementSequenceNode) {
                List<AugmentedStmt> stmts = ((ASTStatementSequenceNode) lastNode).getStatements();
                if (!stmts.isEmpty()) {
                    Stmt s = stmts.get(0).get_Stmt();
                    if (!(s instanceof DVariableDeclarationStmt)) {
                        stmts.add(defaultStmt);
                        ASTStatementSequenceNode newNode = new ASTStatementSequenceNode(stmts);
                        body.remove(body.size() - 1);
                        body.add(newNode);
                        node.replaceBody(body);
                        done = true;
                    }
                }
            }
        }
        if (!done) {
            List<AugmentedStmt> newBody = new ArrayList<>();
            newBody.add(defaultStmt);
            ASTStatementSequenceNode newNode2 = new ASTStatementSequenceNode(newBody);
            body.add(newNode2);
            node.replaceBody(body);
        }
    }

    public AugmentedStmt createDefaultStmt(Object field) {
        Value ref = null;
        Type fieldType = null;
        if (field instanceof SootField) {
            SootFieldRef tempFieldRef = ((SootField) field).makeRef();
            fieldType = ((SootField) field).getType();
            if (((SootField) field).isStatic()) {
                ref = new DStaticFieldRef(tempFieldRef, true);
            } else {
                ref = new DInstanceFieldRef(new JimpleLocal("this", fieldType), tempFieldRef, new HashSet());
            }
        } else if (field instanceof Local) {
            ref = (Local) field;
            fieldType = ((Local) field).getType();
        }
        GAssignStmt assignStmt = null;
        if (fieldType instanceof RefType) {
            assignStmt = new GAssignStmt(ref, NullConstant.v());
        } else if (fieldType instanceof DoubleType) {
            assignStmt = new GAssignStmt(ref, DoubleConstant.v(Const.default_value_double));
        } else if (fieldType instanceof FloatType) {
            assignStmt = new GAssignStmt(ref, FloatConstant.v(0.0f));
        } else if (fieldType instanceof LongType) {
            assignStmt = new GAssignStmt(ref, LongConstant.v(0L));
        } else if ((fieldType instanceof IntType) || (fieldType instanceof ByteType) || (fieldType instanceof ShortType) || (fieldType instanceof CharType) || (fieldType instanceof BooleanType)) {
            assignStmt = new GAssignStmt(ref, DIntConstant.v(0, fieldType));
        }
        if (assignStmt != null) {
            AugmentedStmt as = new AugmentedStmt(assignStmt);
            return as;
        }
        return null;
    }

    public void handleAssignOnSomePaths(ASTMethodNode node, SootField field, List defs) {
        ASTNode nodeSecond;
        Value ref;
        List<ASTStatementSequenceNode> ancestorSubBody;
        if (defs.size() != 1) {
            this.cancelFinalModifier.add(field);
            return;
        }
        AllVariableUses varUses = new AllVariableUses(node);
        node.apply(varUses);
        List allUses = varUses.getUsesForField(field);
        if (allUses != null && !allUses.isEmpty()) {
            this.cancelFinalModifier.add(field);
            return;
        }
        Type localType = field.getType();
        Local newLocal = new JimpleLocal("DavaTemp_" + field.getName(), localType);
        DVariableDeclarationStmt varStmt = new DVariableDeclarationStmt(localType, this.davaBody);
        varStmt.addLocal(newLocal);
        AugmentedStmt as = new AugmentedStmt(varStmt);
        List<AugmentedStmt> stmts = node.getDeclarations().getStatements();
        stmts.add(as);
        ASTStatementSequenceNode declNode = new ASTStatementSequenceNode(stmts);
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new DecompilationException("ASTMethodNode does not have one subBody");
        }
        List<Object> body = (List) subBodies.get(0);
        body.remove(0);
        body.add(0, declNode);
        node.replaceBody(body);
        node.setDeclarations(declNode);
        AugmentedStmt initialization = createDefaultStmt(newLocal);
        if (body.size() < 2) {
            throw new RuntimeException("Size of body is less than 1");
        }
        ASTNode nodeSecond2 = (ASTNode) body.get(1);
        if (nodeSecond2 instanceof ASTStatementSequenceNode) {
            List<AugmentedStmt> stmts1 = ((ASTStatementSequenceNode) nodeSecond2).getStatements();
            stmts1.add(initialization);
            nodeSecond = new ASTStatementSequenceNode(stmts1);
            body.remove(1);
        } else {
            List<AugmentedStmt> tempList = new ArrayList<>();
            tempList.add(initialization);
            nodeSecond = new ASTStatementSequenceNode(tempList);
        }
        body.add(1, nodeSecond);
        node.replaceBody(body);
        ((GAssignStmt) defs.get(0)).setLeftOp(newLocal);
        SootFieldRef tempFieldRef = field.makeRef();
        if (field.isStatic()) {
            ref = new DStaticFieldRef(tempFieldRef, true);
        } else {
            ref = new DInstanceFieldRef(new JimpleLocal("this", field.getType()), tempFieldRef, new HashSet());
        }
        GAssignStmt assignStmt = new GAssignStmt(ref, newLocal);
        AugmentedStmt assignStmt1 = new AugmentedStmt(assignStmt);
        ASTParentNodeFinder parentFinder = new ASTParentNodeFinder();
        node.apply(parentFinder);
        Object parent = parentFinder.getParentOf(defs.get(0));
        if (!(parent instanceof ASTStatementSequenceNode)) {
            throw new DecompilationException("Parent of stmt was not a stmt seq node");
        }
        Object grandParent = parentFinder.getParentOf(parent);
        if (grandParent == null) {
            throw new DecompilationException("Parent of stmt seq node was null");
        }
        MustMayInitialize must = new MustMayInitialize(node, 0);
        while (!must.isMustInitialized(field)) {
            Object parentOfGrandParent = parentFinder.getParentOf(grandParent);
            if (!(grandParent instanceof ASTMethodNode) && parentOfGrandParent == null) {
                throw new DecompilationException("Parent of non method node was null");
            }
            boolean notResolved = false;
            ASTNode ancestor = (ASTNode) parentOfGrandParent;
            Iterator<Object> it = ancestor.get_SubBodies().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (ancestor instanceof ASTTryNode) {
                    ancestorSubBody = (List) ((ASTTryNode.container) next).o;
                } else {
                    ancestorSubBody = (List) next;
                }
                if (ancestorSubBody.indexOf(grandParent) > -1) {
                    int index = ancestorSubBody.indexOf(grandParent);
                    if (index + 1 < ancestorSubBody.size() && (ancestorSubBody.get(index + 1) instanceof ASTStatementSequenceNode)) {
                        ASTStatementSequenceNode someNode = ancestorSubBody.get(index + 1);
                        List<AugmentedStmt> stmtsLast = someNode.getStatements();
                        List<AugmentedStmt> newStmts = new ArrayList<>();
                        newStmts.add(assignStmt1);
                        newStmts.addAll(stmtsLast);
                        someNode.setStatements(newStmts);
                        must = new MustMayInitialize(node, 0);
                        if (!must.isMustInitialized(field)) {
                            someNode.setStatements(stmtsLast);
                            notResolved = true;
                        }
                    } else {
                        List<AugmentedStmt> tempList2 = new ArrayList<>();
                        tempList2.add(assignStmt1);
                        ASTStatementSequenceNode lastNode = new ASTStatementSequenceNode(tempList2);
                        ancestorSubBody.add(index + 1, lastNode);
                        must = new MustMayInitialize(node, 0);
                        if (!must.isMustInitialized(field)) {
                            ancestorSubBody.remove(index + 1);
                            notResolved = true;
                        }
                    }
                }
            }
            if (notResolved) {
                grandParent = parentFinder.getParentOf(grandParent);
            }
        }
    }
}
