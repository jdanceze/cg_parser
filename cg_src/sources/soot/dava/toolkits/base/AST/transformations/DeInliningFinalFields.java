package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DStaticFieldRef;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/DeInliningFinalFields.class */
public class DeInliningFinalFields extends DepthFirstAdapter {
    private SootClass sootClass;
    private SootMethod sootMethod;
    private HashMap<Comparable, SootField> finalFields;

    public DeInliningFinalFields() {
        this.sootClass = null;
        this.sootMethod = null;
    }

    public DeInliningFinalFields(boolean verbose) {
        super(verbose);
        this.sootClass = null;
        this.sootMethod = null;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        this.sootMethod = node.getDavaBody().getMethod();
        this.sootClass = this.sootMethod.getDeclaringClass();
        this.finalFields = new HashMap<>();
        ArrayList<SootField> fieldChain = new ArrayList<>();
        for (SootClass tempClass : Scene.v().getApplicationClasses()) {
            for (SootField next : tempClass.getFields()) {
                fieldChain.add(next);
            }
        }
        Iterator<SootField> it = fieldChain.iterator();
        while (it.hasNext()) {
            SootField f = it.next();
            if (f.isFinal()) {
                Type fieldType = f.getType();
                if ((fieldType instanceof DoubleType) && f.hasTag(DoubleConstantValueTag.NAME)) {
                    double val = ((DoubleConstantValueTag) f.getTag(DoubleConstantValueTag.NAME)).getDoubleValue();
                    this.finalFields.put(Double.valueOf(val), f);
                } else if ((fieldType instanceof FloatType) && f.hasTag(FloatConstantValueTag.NAME)) {
                    float val2 = ((FloatConstantValueTag) f.getTag(FloatConstantValueTag.NAME)).getFloatValue();
                    this.finalFields.put(Float.valueOf(val2), f);
                } else if ((fieldType instanceof LongType) && f.hasTag(LongConstantValueTag.NAME)) {
                    long val3 = ((LongConstantValueTag) f.getTag(LongConstantValueTag.NAME)).getLongValue();
                    this.finalFields.put(Long.valueOf(val3), f);
                } else if ((fieldType instanceof CharType) && f.hasTag(IntegerConstantValueTag.NAME)) {
                    int val4 = ((IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME)).getIntValue();
                    this.finalFields.put(Integer.valueOf(val4), f);
                } else if ((fieldType instanceof BooleanType) && f.hasTag(IntegerConstantValueTag.NAME)) {
                    int val5 = ((IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME)).getIntValue();
                    if (val5 == 0) {
                        this.finalFields.put(false, f);
                    } else {
                        this.finalFields.put(true, f);
                    }
                } else if (((fieldType instanceof IntType) || (fieldType instanceof ByteType) || (fieldType instanceof ShortType)) && f.hasTag(IntegerConstantValueTag.NAME)) {
                    int val6 = ((IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME)).getIntValue();
                    this.finalFields.put(Integer.valueOf(val6), f);
                } else if (f.hasTag(StringConstantValueTag.NAME)) {
                    String val7 = ((StringConstantValueTag) f.getTag(StringConstantValueTag.NAME)).getStringValue();
                    this.finalFields.put(val7, f);
                }
            }
        }
    }

    private boolean isConstant(Value val) {
        return (val instanceof StringConstant) || (val instanceof DoubleConstant) || (val instanceof FloatConstant) || (val instanceof IntConstant) || (val instanceof LongConstant);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
    }

    public void checkAndSwitch(ValueBox valBox) {
        Value val = valBox.getValue();
        Object finalField = check(val);
        if (finalField != null) {
            SootField field = (SootField) finalField;
            if (this.sootClass.declaresField(field.getName(), field.getType())) {
                if (valBox.canContainValue(new DStaticFieldRef(field.makeRef(), true))) {
                    valBox.setValue(new DStaticFieldRef(field.makeRef(), true));
                }
            } else if (valBox.canContainValue(new DStaticFieldRef(field.makeRef(), true))) {
                valBox.setValue(new DStaticFieldRef(field.makeRef(), false));
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public Object check(Value val) {
        Integer myInt;
        Object finalField = null;
        if (isConstant(val)) {
            if (val instanceof StringConstant) {
                String myString = ((StringConstant) val).toString();
                finalField = this.finalFields.get(myString.substring(1, myString.length() - 1));
            } else if (val instanceof DoubleConstant) {
                finalField = this.finalFields.get(Double.valueOf(((DoubleConstant) val).value));
            } else if (val instanceof FloatConstant) {
                finalField = this.finalFields.get(Float.valueOf(((FloatConstant) val).value));
            } else if (val instanceof LongConstant) {
                finalField = this.finalFields.get(Long.valueOf(((LongConstant) val).value));
            } else if (val instanceof IntConstant) {
                String myString2 = ((IntConstant) val).toString();
                if (myString2.length() == 0) {
                    return null;
                }
                try {
                    if (myString2.charAt(0) == '\'') {
                        if (myString2.length() < 2) {
                            return null;
                        }
                        myInt = Integer.valueOf(myString2.charAt(1));
                    } else {
                        myInt = Integer.valueOf(myString2);
                    }
                    Type valType = ((IntConstant) val).getType();
                    if (valType instanceof ByteType) {
                        finalField = this.finalFields.get(myInt);
                    } else if (valType instanceof IntType) {
                        switch (myString2.hashCode()) {
                            case 3569038:
                                if (myString2.equals("true")) {
                                    finalField = this.finalFields.get(true);
                                    break;
                                }
                                finalField = this.finalFields.get(myInt);
                                break;
                            case 97196323:
                                if (myString2.equals("false")) {
                                    finalField = this.finalFields.get(false);
                                    break;
                                }
                                finalField = this.finalFields.get(myInt);
                                break;
                            default:
                                finalField = this.finalFields.get(myInt);
                                break;
                        }
                    } else if (valType instanceof ShortType) {
                        finalField = this.finalFields.get(myInt);
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return finalField;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        Value val = node.get_Key();
        if (isConstant(val)) {
            checkAndSwitch(node.getKeyBox());
            return;
        }
        for (ValueBox tempBox : val.getUseBoxes()) {
            checkAndSwitch(tempBox);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            for (ValueBox tempBox : s.getUseBoxes()) {
                checkAndSwitch(tempBox);
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTForLoopNode(ASTForLoopNode node) {
        for (AugmentedStmt as : node.getInit()) {
            Stmt s = as.get_Stmt();
            for (ValueBox tempBox : s.getUseBoxes()) {
                checkAndSwitch(tempBox);
            }
        }
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
        for (AugmentedStmt as2 : node.getUpdate()) {
            Stmt s2 = as2.get_Stmt();
            for (ValueBox tempBox2 : s2.getUseBoxes()) {
                checkAndSwitch(tempBox2);
            }
        }
    }

    public void checkConditionalUses(Object cond, ASTNode node) {
        if (cond instanceof ASTAggregatedCondition) {
            checkConditionalUses(((ASTAggregatedCondition) cond).getLeftOp(), node);
            checkConditionalUses(((ASTAggregatedCondition) cond).getRightOp(), node);
        } else if (cond instanceof ASTBinaryCondition) {
            Value val = ((ASTBinaryCondition) cond).getConditionExpr();
            for (ValueBox tempBox : val.getUseBoxes()) {
                checkAndSwitch(tempBox);
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfNode(ASTIfNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfElseNode(ASTIfElseNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTWhileNode(ASTWhileNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTDoWhileNode(ASTDoWhileNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }
}
