package soot.dava.toolkits.base.AST.structuredAnalysis;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import soot.PrimType;
import soot.ShortType;
import soot.SootField;
import soot.Type;
import soot.Value;
import soot.dava.DavaFlowAnalysisException;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.interProcedural.ConstantFieldValueFinder;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/CP.class */
public class CP extends StructuredAnalysis {
    ArrayList<CPTuple> constantFieldTuples = null;
    ArrayList<CPTuple> formals = null;
    ArrayList<CPTuple> locals = null;
    ArrayList<CPTuple> initialInput = null;
    ASTMethodNode methodNode;
    String localClassName;

    public CP(ASTMethodNode analyze, HashMap<String, Object> constantFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
        this.methodNode = null;
        this.localClassName = null;
        this.methodNode = analyze;
        this.localClassName = analyze.getDavaBody().getMethod().getDeclaringClass().getName();
        createConstantFieldsList(constantFields, classNameFieldNameToSootFieldMapping);
        createInitialInput();
        CPFlowSet initialSet = new CPFlowSet();
        Iterator<CPTuple> it = this.initialInput.iterator();
        while (it.hasNext()) {
            initialSet.add(it.next());
        }
        CPFlowSet cPFlowSet = (CPFlowSet) process(analyze, initialSet);
    }

    public void createInitialInput() {
        Object num;
        this.initialInput = new ArrayList<>();
        this.initialInput.addAll(this.constantFieldTuples);
        this.formals = new ArrayList<>();
        Collection col = this.methodNode.getDavaBody().get_ParamMap().values();
        for (Object temp : col) {
            if (temp instanceof Local) {
                Local tempLocal = (Local) temp;
                if (tempLocal.getType() instanceof PrimType) {
                    CPVariable newVar = new CPVariable(tempLocal);
                    CPTuple newTuple = new CPTuple(this.localClassName, newVar, true);
                    this.initialInput.add(newTuple);
                    this.formals.add(newTuple);
                }
            }
        }
        List decLocals = this.methodNode.getDeclaredLocals();
        this.locals = new ArrayList<>();
        for (Object temp2 : decLocals) {
            if (temp2 instanceof Local) {
                Local tempLocal2 = (Local) temp2;
                Type localType = tempLocal2.getType();
                if (localType instanceof PrimType) {
                    CPVariable newVar2 = new CPVariable(tempLocal2);
                    if (localType instanceof BooleanType) {
                        num = new Boolean(false);
                    } else if (localType instanceof ByteType) {
                        num = new Integer(0);
                    } else if (localType instanceof CharType) {
                        num = new Integer(0);
                    } else if (localType instanceof DoubleType) {
                        num = new Double((double) Const.default_value_double);
                    } else if (localType instanceof FloatType) {
                        num = new Float(0.0f);
                    } else if (localType instanceof IntType) {
                        num = new Integer(0);
                    } else if (localType instanceof LongType) {
                        num = new Long(0L);
                    } else if (localType instanceof ShortType) {
                        num = new Integer(0);
                    } else {
                        throw new DavaFlowAnalysisException("Unknown PrimType");
                    }
                    Object value = num;
                    this.locals.add(new CPTuple(this.localClassName, newVar2, value));
                } else {
                    continue;
                }
            }
        }
    }

    private void createConstantFieldsList(HashMap<String, Object> constantFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
        this.constantFieldTuples = new ArrayList<>();
        for (String combined : constantFields.keySet()) {
            int temp = combined.indexOf(ConstantFieldValueFinder.combiner, 0);
            if (temp > 0) {
                String className = combined.substring(0, temp);
                SootField field = classNameFieldNameToSootFieldMapping.get(combined);
                if (field.getType() instanceof PrimType) {
                    Object value = constantFields.get(combined);
                    CPVariable var = new CPVariable(field);
                    CPTuple newTuples = new CPTuple(className, var, value);
                    this.constantFieldTuples.add(newTuples);
                }
            } else {
                throw new DavaFlowAnalysisException("Second argument of VariableValuePair not a variable");
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet emptyFlowSet() {
        return new CPFlowSet();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public void setMergeType() {
        this.MERGETYPE = 2;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet newInitialFlow() {
        CPFlowSet flowSet = new CPFlowSet();
        ArrayList<CPTuple> localsAndFormals = new ArrayList<>();
        localsAndFormals.addAll(this.formals);
        localsAndFormals.addAll(this.locals);
        Iterator<CPTuple> it = localsAndFormals.iterator();
        while (it.hasNext()) {
            CPTuple tempTuple = it.next().m2535clone();
            if (!tempTuple.isTop()) {
                tempTuple.setTop();
            }
            flowSet.add(tempTuple);
        }
        Iterator<CPTuple> it2 = this.constantFieldTuples.iterator();
        while (it2.hasNext()) {
            flowSet.add(it2.next());
        }
        return flowSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet cloneFlowSet(DavaFlowSet flowSet) {
        if (flowSet instanceof CPFlowSet) {
            return ((CPFlowSet) flowSet).clone();
        }
        throw new RuntimeException("cloneFlowSet not implemented for other flowSet types" + flowSet.toString());
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
        if (!(input instanceof CPFlowSet)) {
            throw new RuntimeException("processCondition is not implemented for other flowSet types" + input.toString());
        }
        CPFlowSet inSet = (CPFlowSet) input;
        return inSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
        if (!(input instanceof CPFlowSet)) {
            throw new RuntimeException("processSynchronized  is not implemented for other flowSet types" + input.toString());
        }
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
        if (!(input instanceof CPFlowSet)) {
            throw new RuntimeException("processCondition is not implemented for other flowSet types" + input.toString());
        }
        CPFlowSet inSet = (CPFlowSet) input;
        return inSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
        if (!(input instanceof CPFlowSet)) {
            throw new RuntimeException("processStatement is not implemented for other flowSet types");
        }
        CPFlowSet inSet = (CPFlowSet) input;
        if (inSet == this.NOPATH || !(s instanceof DefinitionStmt)) {
            return inSet;
        }
        DefinitionStmt defStmt = (DefinitionStmt) s;
        Value left = defStmt.getLeftOp();
        if (!(left instanceof Local) || !(((Local) left).getType() instanceof PrimType)) {
            return inSet;
        }
        CPFlowSet toReturn = (CPFlowSet) cloneFlowSet(inSet);
        Object killedValue = killButGetValueForUse((Local) left, toReturn);
        Value right = defStmt.getRightOp();
        Object value = CPHelper.isAConstantValue(right);
        if (value != null) {
            if (left.getType() instanceof BooleanType) {
                Integer tempValue = (Integer) value;
                if (tempValue.intValue() == 0) {
                    value = new Boolean(false);
                } else {
                    value = new Boolean(true);
                }
            }
            addOrUpdate(toReturn, (Local) left, value);
        } else {
            handleMathematical(toReturn, (Local) left, right, killedValue);
        }
        return toReturn;
    }

    public Object killButGetValueForUse(Local left, CPFlowSet toReturn) {
        Iterator<CPTuple> it = toReturn.iterator();
        while (it.hasNext()) {
            CPTuple tempTuple = it.next();
            if (tempTuple.getSootClassName().equals(this.localClassName) && tempTuple.containsLocal()) {
                Local tempLocal = tempTuple.getVariable().getLocal();
                if (left.getName().equals(tempLocal.getName())) {
                    Object killedValue = tempTuple.getValue();
                    tempTuple.setTop();
                    return killedValue;
                }
            }
        }
        CPVariable newVar = new CPVariable(left);
        CPTuple newTuple = new CPTuple(this.localClassName, newVar, false);
        toReturn.add(newTuple);
        return null;
    }

    private void addOrUpdate(CPFlowSet toReturn, Local left, Object val) {
        CPVariable newVar = new CPVariable(left);
        CPTuple newTuple = new CPTuple(this.localClassName, newVar, val);
        toReturn.addIfNotPresent(newTuple);
    }

    private void handleMathematical(CPFlowSet toReturn, Local left, Value right, Object killedValue) {
        Object value = isANotTopConstantInInputSet(toReturn, right);
        if (value != null) {
            Object toSend = CPHelper.wrapperClassCloner(value);
            if (toSend != null) {
                addOrUpdate(toReturn, left, toSend);
            }
        } else if (right instanceof BinopExpr) {
            Value op1 = ((BinopExpr) right).getOp1();
            Value op2 = ((BinopExpr) right).getOp2();
            Object op1Val = CPHelper.isAConstantValue(op1);
            Object op2Val = CPHelper.isAConstantValue(op2);
            if (op1Val == null) {
                op1Val = isANotTopConstantInInputSet(toReturn, op1);
            }
            if (op2Val == null) {
                op2Val = isANotTopConstantInInputSet(toReturn, op2);
            }
            if (op1 == left) {
                op1Val = killedValue;
            }
            if (op2 == left) {
                op2Val = killedValue;
            }
            if (op1Val != null && op2Val != null && (left.getType() instanceof IntType) && (op1Val instanceof Integer) && (op2Val instanceof Integer)) {
                int op1IntValue = ((Integer) op1Val).intValue();
                int op2IntValue = ((Integer) op2Val).intValue();
                String tempStr = ((BinopExpr) right).getSymbol();
                if (tempStr.length() > 1) {
                    char symbol = tempStr.charAt(1);
                    int newValue = 0;
                    boolean set = false;
                    switch (symbol) {
                        case '*':
                            newValue = op1IntValue * op2IntValue;
                            set = true;
                            break;
                        case '+':
                            newValue = op1IntValue + op2IntValue;
                            set = true;
                            break;
                        case '-':
                            newValue = op1IntValue - op2IntValue;
                            set = true;
                            break;
                    }
                    if (set) {
                        Integer newValueObject = new Integer(newValue);
                        addOrUpdate(toReturn, left, newValueObject);
                    }
                }
            }
        }
    }

    private Object isANotTopConstantInInputSet(CPFlowSet set, Value toCheck) {
        String toCheckClassName;
        if ((toCheck instanceof Local) || (toCheck instanceof FieldRef)) {
            if (toCheck instanceof Local) {
                toCheckClassName = this.localClassName;
            } else {
                toCheckClassName = ((FieldRef) toCheck).getField().getDeclaringClass().getName();
            }
            Iterator<CPTuple> it = set.iterator();
            while (it.hasNext()) {
                CPTuple tempTuple = it.next();
                if (tempTuple.getSootClassName().equals(toCheckClassName)) {
                    boolean tupleFound = false;
                    if (tempTuple.containsLocal() && (toCheck instanceof Local)) {
                        Local tempLocal = tempTuple.getVariable().getLocal();
                        if (tempLocal.getName().equals(((Local) toCheck).getName())) {
                            tupleFound = true;
                        }
                    } else if (tempTuple.containsField() && (toCheck instanceof FieldRef)) {
                        SootField toCheckField = ((FieldRef) toCheck).getField();
                        SootField tempField = tempTuple.getVariable().getSootField();
                        if (tempField.getName().equals(toCheckField.getName())) {
                            tupleFound = true;
                        }
                    }
                    if (tupleFound) {
                        if (tempTuple.isTop()) {
                            return null;
                        }
                        return tempTuple.getValue();
                    }
                }
            }
            return null;
        }
        return null;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTIfNode(ASTIfNode node, DavaFlowSet input) {
        if (DEBUG_IF) {
            System.out.println("Processing if node using over-ridden process if method" + input.toString());
        }
        DavaFlowSet input2 = processCondition(node.get_Condition(), input);
        if (!(input2 instanceof CPFlowSet)) {
            throw new DavaFlowAnalysisException("not a flow set");
        }
        CPFlowSet inputToBody = ((CPFlowSet) input2).clone();
        CPTuple tuple = checkForValueHints(node.get_Condition(), inputToBody, false);
        if (tuple != null) {
            inputToBody.addIfNotPresentButDontUpdate(tuple);
        }
        DavaFlowSet output1 = processSingleSubBodyNode(node, inputToBody);
        if (DEBUG_IF) {
            System.out.println("\n\nINPUTS TO MERGE ARE input (original):" + input2.toString() + "processingBody output:" + output1.toString() + "\n\n\n");
        }
        DavaFlowSet output2 = merge(input2, output1);
        String label = getLabel(node);
        DavaFlowSet temp = handleBreak(label, output2, node);
        if (DEBUG_IF) {
            System.out.println("Exiting if node" + temp.toString());
        }
        return temp;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTIfElseNode(ASTIfElseNode node, DavaFlowSet input) {
        if (DEBUG_IF) {
            System.out.println("Processing IF-ELSE node using over-ridden process if method" + input.toString());
        }
        if (!(input instanceof CPFlowSet)) {
            throw new DavaFlowAnalysisException("not a flow set");
        }
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 2) {
            throw new RuntimeException("processASTIfElseNode called with a node without two subBodies");
        }
        List subBodyOne = (List) subBodies.get(0);
        List subBodyTwo = (List) subBodies.get(1);
        DavaFlowSet input2 = processCondition(node.get_Condition(), input);
        DavaFlowSet clonedInput = cloneFlowSet(input2);
        CPTuple tuple = checkForValueHints(node.get_Condition(), (CPFlowSet) clonedInput, false);
        if (tuple != null) {
            ((CPFlowSet) clonedInput).addIfNotPresentButDontUpdate(tuple);
        }
        DavaFlowSet output1 = process(subBodyOne, clonedInput);
        DavaFlowSet clonedInput2 = cloneFlowSet(input2);
        CPTuple tuple1 = checkForValueHints(node.get_Condition(), (CPFlowSet) clonedInput2, true);
        if (tuple1 != null) {
            ((CPFlowSet) clonedInput2).addIfNotPresentButDontUpdate(tuple1);
        }
        DavaFlowSet output2 = process(subBodyTwo, clonedInput2);
        if (DEBUG_IF) {
            System.out.println("\n\n  IF-ELSE   INPUTS TO MERGE ARE input (if):" + output1.toString() + " else:" + output2.toString() + "\n\n\n");
        }
        DavaFlowSet temp = merge(output1, output2);
        String label = getLabel(node);
        DavaFlowSet output12 = handleBreak(label, temp, node);
        if (DEBUG_IF) {
            System.out.println("Exiting ifelse node" + output12.toString());
        }
        return output12;
    }

    public CPTuple checkForValueHints(ASTCondition cond, CPFlowSet input, boolean isElseBranch) {
        Boolean equal;
        if (cond instanceof ASTUnaryCondition) {
            ASTUnaryCondition unary = (ASTUnaryCondition) cond;
            Value unaryValue = unary.getValue();
            boolean NOTTED = false;
            if (unaryValue instanceof DNotExpr) {
                unaryValue = ((DNotExpr) unaryValue).getOp();
                NOTTED = true;
            }
            if (!(unaryValue instanceof Local)) {
                return null;
            }
            CPVariable variable = new CPVariable((Local) unaryValue);
            if (!isElseBranch) {
                Boolean boolVal = new Boolean(!NOTTED);
                return new CPTuple(this.localClassName, variable, boolVal);
            }
            Boolean boolVal2 = new Boolean(NOTTED);
            return new CPTuple(this.localClassName, variable, boolVal2);
        } else if (cond instanceof ASTBinaryCondition) {
            ASTBinaryCondition binary = (ASTBinaryCondition) cond;
            ConditionExpr expr = binary.getConditionExpr();
            String symbol = expr.getSymbol();
            if (symbol.indexOf("==") > -1) {
                equal = new Boolean(true);
            } else if (symbol.indexOf("!=") > -1) {
                equal = new Boolean(false);
            } else {
                return null;
            }
            Value a = expr.getOp1();
            Value b = expr.getOp2();
            CPTuple tuple = createCPTupleIfPossible(a, b, input);
            if (tuple == null) {
                return null;
            }
            if (equal.booleanValue()) {
                if (!isElseBranch) {
                    return tuple;
                }
                return null;
            } else if (isElseBranch) {
                return tuple;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public CPTuple createCPTupleIfPossible(Value a, Value b, CPFlowSet input) {
        Object aVal = CPHelper.isAConstantValue(a);
        Object bVal = CPHelper.isAConstantValue(b);
        if (aVal != null && bVal != null) {
            return null;
        }
        CPVariable cpVar = null;
        Object constantToUse = null;
        if (aVal == null && bVal == null) {
            Object av1 = isANotTopConstantInInputSet(input, a);
            Object av2 = isANotTopConstantInInputSet(input, b);
            if (av1 == null && av2 == null) {
                return null;
            }
            if (av1 != null || av2 == null) {
                if (av1 != null && av2 == null) {
                    if (!(b instanceof Local) || !(((Local) b).getType() instanceof PrimType)) {
                        return null;
                    }
                    cpVar = new CPVariable((Local) b);
                    constantToUse = av1;
                }
            } else if (!(a instanceof Local) || !(((Local) a).getType() instanceof PrimType)) {
                return null;
            } else {
                cpVar = new CPVariable((Local) a);
                constantToUse = av2;
            }
        } else if (aVal == null || bVal != null) {
            if (aVal == null && bVal != null) {
                if (!(a instanceof Local) || !(((Local) a).getType() instanceof PrimType)) {
                    return null;
                }
                cpVar = new CPVariable((Local) a);
                constantToUse = bVal;
            }
        } else if (!(b instanceof Local) || !(((Local) b).getType() instanceof PrimType)) {
            return null;
        } else {
            cpVar = new CPVariable((Local) b);
            constantToUse = aVal;
        }
        if (cpVar != null && constantToUse != null) {
            if (cpVar.getLocal().getType() instanceof BooleanType) {
                if (!(constantToUse instanceof Integer)) {
                    return null;
                }
                Integer tempValue = (Integer) constantToUse;
                if (tempValue.intValue() == 0) {
                    constantToUse = new Boolean(false);
                } else {
                    constantToUse = new Boolean(true);
                }
            }
            return new CPTuple(this.localClassName, cpVar, constantToUse);
        }
        return null;
    }
}
