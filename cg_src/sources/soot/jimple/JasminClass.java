package soot.jimple;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AbstractJasminClass;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Timers;
import soot.Trap;
import soot.Type;
import soot.TypeSwitch;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.VoidType;
import soot.coffi.Instruction;
import soot.grimp.AbstractGrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.jimple.internal.StmtBox;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.toolkits.scalar.FastColorer;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/JasminClass.class */
public class JasminClass extends AbstractJasminClass {
    private static final Logger logger = LoggerFactory.getLogger(JasminClass.class);
    Value plusPlusValue;
    Local plusPlusHolder;
    int plusPlusState;
    int plusPlusPlace;
    int plusPlusHeight;
    Stmt plusPlusIncrementer;

    void emit(String s, int stackChange) {
        modifyStackHeight(stackChange);
        okayEmit(s);
    }

    void modifyStackHeight(int stackChange) {
        if (this.currentStackHeight > this.maxStackHeight) {
            this.maxStackHeight = this.currentStackHeight;
        }
        this.currentStackHeight += stackChange;
        if (this.currentStackHeight < 0) {
            throw new RuntimeException("Stack height is negative!");
        }
        if (this.currentStackHeight > this.maxStackHeight) {
            this.maxStackHeight = this.currentStackHeight;
        }
    }

    public JasminClass(SootClass sootClass) {
        super(sootClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.AbstractJasminClass
    public void assignColorsToLocals(Body body) {
        super.assignColorsToLocals(body);
        FastColorer.assignColorsToLocals(body, this.localToGroup, this.localToColor, this.groupToColorCount);
        if (Options.v().time()) {
            Timers.v().packTimer.end();
        }
    }

    @Override // soot.AbstractJasminClass
    protected void emitMethodBody(SootMethod method) {
        int slot;
        int i;
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.end();
        }
        Body activeBody = method.getActiveBody();
        if (!(activeBody instanceof StmtBody)) {
            throw new RuntimeException("method: " + method.getName() + " has an invalid active body!");
        }
        StmtBody body = (StmtBody) activeBody;
        body.validate();
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.start();
        }
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Performing peephole optimizations...");
        }
        this.subroutineToReturnAddressSlot = new HashMap(10, 0.7f);
        Chain<Unit> units = body.getUnits();
        this.unitToLabel = new HashMap((units.size() * 2) + 1, 0.7f);
        this.labelCount = 0;
        for (UnitBox ubox : body.getUnitBoxes(true)) {
            StmtBox box = (StmtBox) ubox;
            if (!this.unitToLabel.containsKey(box.getUnit())) {
                Map<Unit, String> map = this.unitToLabel;
                Unit unit = box.getUnit();
                StringBuilder sb = new StringBuilder("label");
                int i2 = this.labelCount;
                this.labelCount = i2 + 1;
                map.put(unit, sb.append(i2).toString());
            }
        }
        for (Trap trap : body.getTraps()) {
            if (trap.getBeginUnit() != trap.getEndUnit()) {
                emit(".catch " + slashify(trap.getException().getName()) + " from " + this.unitToLabel.get(trap.getBeginUnit()) + " to " + this.unitToLabel.get(trap.getEndUnit()) + " using " + this.unitToLabel.get(trap.getHandlerUnit()));
            }
        }
        int stackLimitIndex = -1;
        int localCount = 0;
        int[] paramSlots = new int[method.getParameterCount()];
        int thisSlot = 0;
        Set<Local> assignedLocals = new HashSet<>();
        Map<GroupIntPair, Integer> groupColorPairToSlot = new HashMap<>((body.getLocalCount() * 2) + 1, 0.7f);
        this.localToSlot = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
        assignColorsToLocals(body);
        if (!method.isStatic()) {
            thisSlot = 0;
            localCount = 0 + 1;
        }
        List<Type> paramTypes = method.getParameterTypes();
        for (int i3 = 0; i3 < paramTypes.size(); i3++) {
            paramSlots[i3] = localCount;
            localCount += sizeOfType(paramTypes.get(i3));
        }
        for (Unit u : units) {
            if (u instanceof IdentityStmt) {
                IdentityStmt is = (IdentityStmt) u;
                Value leftOp = is.getLeftOp();
                if (leftOp instanceof Local) {
                    IdentityRef identity = (IdentityRef) is.getRightOp();
                    if (identity instanceof ThisRef) {
                        if (method.isStatic()) {
                            throw new RuntimeException("Attempting to use 'this' in static method");
                        }
                        i = thisSlot;
                    } else if (identity instanceof ParameterRef) {
                        i = paramSlots[((ParameterRef) identity).getIndex()];
                    }
                    int slot2 = i;
                    Local l = (Local) leftOp;
                    groupColorPairToSlot.put(new GroupIntPair(this.localToGroup.get(l), this.localToColor.get(l).intValue()), Integer.valueOf(slot2));
                    this.localToSlot.put(l, Integer.valueOf(slot2));
                    assignedLocals.add(l);
                } else {
                    continue;
                }
            }
        }
        for (Local local : body.getLocals()) {
            if (!assignedLocals.contains(local)) {
                GroupIntPair pair = new GroupIntPair(this.localToGroup.get(local), this.localToColor.get(local).intValue());
                if (groupColorPairToSlot.containsKey(pair)) {
                    slot = groupColorPairToSlot.get(pair).intValue();
                } else {
                    slot = localCount;
                    localCount += sizeOfType(local.getType());
                    groupColorPairToSlot.put(pair, Integer.valueOf(slot));
                }
                this.localToSlot.put(local, Integer.valueOf(slot));
                assignedLocals.add(local);
            }
        }
        int modifiers = method.getModifiers();
        if (!Modifier.isNative(modifiers) && !Modifier.isAbstract(modifiers)) {
            emit("    .limit stack ?");
            stackLimitIndex = this.code.size() - 1;
            emit("    .limit locals " + localCount);
        }
        this.isEmittingMethodCode = true;
        this.maxStackHeight = 0;
        this.isNextGotoAJsr = false;
        Iterator<Unit> codeIt = units.iterator();
        while (codeIt.hasNext()) {
            Stmt s = (Stmt) codeIt.next();
            if (this.unitToLabel.containsKey(s)) {
                emit(String.valueOf(this.unitToLabel.get(s)) + ":");
            }
            if (this.subroutineToReturnAddressSlot.containsKey(s)) {
                AssignStmt assignStmt = (AssignStmt) s;
                modifyStackHeight(1);
                int slot3 = this.localToSlot.get(assignStmt.getLeftOp()).intValue();
                if (slot3 >= 0 && slot3 <= 3) {
                    emit("astore_" + slot3, -1);
                } else {
                    emit("astore " + slot3, -1);
                }
            }
            this.currentStackHeight = 0;
            emitStmt(s);
            if (this.currentStackHeight != 0) {
                throw new RuntimeException("Stack has height " + this.currentStackHeight + " after execution of stmt: " + s);
            }
        }
        this.isEmittingMethodCode = false;
        int modifiers2 = method.getModifiers();
        if (!Modifier.isNative(modifiers2) && !Modifier.isAbstract(modifiers2)) {
            this.code.set(stackLimitIndex, "    .limit stack " + this.maxStackHeight);
        }
    }

    void emitAssignStmt(AssignStmt stmt) {
        Value lvalue = stmt.getLeftOp();
        final Value rvalue = stmt.getRightOp();
        if ((lvalue instanceof Local) && ((rvalue instanceof AddExpr) || (rvalue instanceof SubExpr))) {
            Local l = (Local) lvalue;
            BinopExpr expr = (BinopExpr) rvalue;
            Value op1 = expr.getOp1();
            Value op2 = expr.getOp2();
            if (lvalue == this.plusPlusHolder) {
                emitValue(lvalue);
                this.plusPlusHolder = null;
                this.plusPlusState = 0;
            }
            if (IntType.v().equals(l.getType())) {
                boolean isValidCase = false;
                int x = 0;
                if (op1 == l && (op2 instanceof IntConstant)) {
                    x = ((IntConstant) op2).value;
                    isValidCase = true;
                } else if ((expr instanceof AddExpr) && op2 == l && (op1 instanceof IntConstant)) {
                    x = ((IntConstant) op1).value;
                    isValidCase = true;
                }
                if (isValidCase && x >= -32768 && x <= 32767) {
                    emit("iinc " + this.localToSlot.get(l) + Instruction.argsep + (expr instanceof AddExpr ? x : -x), 0);
                    return;
                }
            }
        }
        lvalue.apply(new AbstractJimpleValueSwitch<Object>() { // from class: soot.jimple.JasminClass.1
            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseArrayRef(ArrayRef v) {
                JasminClass.this.emitValue(v.getBase());
                JasminClass.this.emitValue(v.getIndex());
                JasminClass.this.emitValue(rvalue);
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.1.1
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("aastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dastore", -4);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("iastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lastore", -4);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("aastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("bastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("bastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("castore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("sastore", -3);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid type: " + t);
                    }
                });
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseInstanceFieldRef(InstanceFieldRef v) {
                JasminClass.this.emitValue(v.getBase());
                JasminClass.this.emitValue(rvalue);
                JasminClass.this.emit("putfield " + JasminClass.slashify(v.getFieldRef().declaringClass().getName()) + '/' + v.getFieldRef().name() + ' ' + JasminClass.jasminDescriptorOf(v.getFieldRef().type()), (-1) + (-JasminClass.sizeOfType(v.getFieldRef().type())));
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ImmediateSwitch
            public void caseLocal(Local v) {
                final int slot = ((Integer) JasminClass.this.localToSlot.get(v)).intValue();
                Type type = v.getType();
                final Value value = rvalue;
                type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.1.2
                    private void handleIntegerType(IntegerType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot, -1);
                        } else {
                            JasminClass.this.emit("istore_" + slot, -1);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntegerType(t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntegerType(t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntegerType(t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntegerType(t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntegerType(t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot, -1);
                        } else {
                            JasminClass.this.emit("astore_" + slot, -1);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("dstore " + slot, -2);
                        } else {
                            JasminClass.this.emit("dstore_" + slot, -2);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("fstore " + slot, -1);
                        } else {
                            JasminClass.this.emit("fstore_" + slot, -1);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("lstore " + slot, -2);
                        } else {
                            JasminClass.this.emit("lstore_" + slot, -2);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot, -1);
                        } else {
                            JasminClass.this.emit("astore_" + slot, -1);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseStmtAddressType(StmtAddressType t) {
                        JasminClass.this.isNextGotoAJsr = true;
                        JasminClass.this.returnAddressSlot = slot;
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emitValue(value);
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot, -1);
                        } else {
                            JasminClass.this.emit("astore_" + slot, -1);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid local type: " + t);
                    }
                });
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseStaticFieldRef(StaticFieldRef v) {
                SootFieldRef field = v.getFieldRef();
                JasminClass.this.emitValue(rvalue);
                JasminClass.this.emit("putstatic " + JasminClass.slashify(field.declaringClass().getName()) + '/' + field.name() + ' ' + JasminClass.jasminDescriptorOf(field.type()), -JasminClass.sizeOfType(v.getFieldRef().type()));
            }
        });
    }

    void emitIfStmt(IfStmt stmt) {
        Value cond = stmt.getCondition();
        final Value op1 = ((BinopExpr) cond).getOp1();
        Value op2 = ((BinopExpr) cond).getOp2();
        final String label = this.unitToLabel.get(stmt.getTarget());
        if ((op2 instanceof NullConstant) || (op1 instanceof NullConstant)) {
            if (op2 instanceof NullConstant) {
                emitValue(op1);
            } else {
                emitValue(op2);
            }
            if (cond instanceof EqExpr) {
                emit("ifnull " + label, -1);
            } else if (cond instanceof NeExpr) {
                emit("ifnonnull " + label, -1);
            } else {
                throw new RuntimeException("invalid condition");
            }
        } else if ((op2 instanceof IntConstant) && ((IntConstant) op2).value == 0) {
            emitValue(op1);
            cond.apply(new AbstractJimpleValueSwitch<Object>() { // from class: soot.jimple.JasminClass.2
                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    JasminClass.this.emit("ifeq " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    JasminClass.this.emit("ifne " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    JasminClass.this.emit("iflt " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    JasminClass.this.emit("ifle " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    JasminClass.this.emit("ifgt " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    JasminClass.this.emit("ifge " + label, -1);
                }
            });
        } else if ((op1 instanceof IntConstant) && ((IntConstant) op1).value == 0) {
            emitValue(op2);
            cond.apply(new AbstractJimpleValueSwitch<Object>() { // from class: soot.jimple.JasminClass.3
                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    JasminClass.this.emit("ifeq " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    JasminClass.this.emit("ifne " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    JasminClass.this.emit("ifgt " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    JasminClass.this.emit("ifge " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    JasminClass.this.emit("iflt " + label, -1);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    JasminClass.this.emit("ifle " + label, -1);
                }
            });
        } else {
            emitValue(op1);
            emitValue(op2);
            cond.apply(new AbstractJimpleValueSwitch<Object>() { // from class: soot.jimple.JasminClass.4
                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.1
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("ifeq " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("ifeq " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("ifeq " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseArrayType(ArrayType t) {
                            JasminClass.this.emit("if_acmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseRefType(RefType t) {
                            JasminClass.this.emit("if_acmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseNullType(NullType t) {
                            JasminClass.this.emit("if_acmpeq " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type");
                        }
                    });
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.2
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("ifne " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("ifne " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("ifne " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseArrayType(ArrayType t) {
                            JasminClass.this.emit("if_acmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseRefType(RefType t) {
                            JasminClass.this.emit("if_acmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseNullType(NullType t) {
                            JasminClass.this.emit("if_acmpne " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type for NeExpr: " + t);
                        }
                    });
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.3
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmplt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmplt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmplt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmplt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmplt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("iflt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("iflt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("iflt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type");
                        }
                    });
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.4
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmple " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmple " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmple " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmple " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmple " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("ifle " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("ifle " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("ifle " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type");
                        }
                    });
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.5
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmpgt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmpgt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmpgt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmpgt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmpgt " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("ifgt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("ifgt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("ifgt " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type");
                        }
                    });
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    Type type = op1.getType();
                    final String str = label;
                    type.apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.4.6
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType t) {
                            JasminClass.this.emit("if_icmpge " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType t) {
                            JasminClass.this.emit("if_icmpge " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType t) {
                            JasminClass.this.emit("if_icmpge " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType t) {
                            JasminClass.this.emit("if_icmpge " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType t) {
                            JasminClass.this.emit("if_icmpge " + str, -2);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType t) {
                            JasminClass.this.emit("dcmpg", -3);
                            JasminClass.this.emit("ifge " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType t) {
                            JasminClass.this.emit("lcmp", -3);
                            JasminClass.this.emit("ifge " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType t) {
                            JasminClass.this.emit("fcmpg", -1);
                            JasminClass.this.emit("ifge " + str, -1);
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type t) {
                            throw new RuntimeException("invalid type");
                        }
                    });
                }
            });
        }
    }

    void emitStmt(Stmt stmt) {
        LineNumberTag lnTag = (LineNumberTag) stmt.getTag(LineNumberTag.NAME);
        if (lnTag != null) {
            emit(".line " + lnTag.getLineNumber());
        }
        stmt.apply(new AbstractStmtSwitch<Object>() { // from class: soot.jimple.JasminClass.5
            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt s) {
                JasminClass.this.emitAssignStmt(s);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseIdentityStmt(IdentityStmt s) {
                if (s.getRightOp() instanceof CaughtExceptionRef) {
                    Value leftOp = s.getLeftOp();
                    if (leftOp instanceof Local) {
                        JasminClass.this.modifyStackHeight(1);
                        int slot = ((Integer) JasminClass.this.localToSlot.get((Local) leftOp)).intValue();
                        if (slot >= 0 && slot <= 3) {
                            JasminClass.this.emit("astore_" + slot, -1);
                        } else {
                            JasminClass.this.emit("astore " + slot, -1);
                        }
                    }
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseBreakpointStmt(BreakpointStmt s) {
                JasminClass.this.emit(Jimple.BREAKPOINT, 0);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseInvokeStmt(InvokeStmt s) {
                JasminClass.this.emitValue(s.getInvokeExpr());
                Type returnType = s.getInvokeExpr().getMethodRef().returnType();
                if (!VoidType.v().equals(returnType)) {
                    if (JasminClass.sizeOfType(returnType) == 1) {
                        JasminClass.this.emit("pop", -1);
                    } else {
                        JasminClass.this.emit("pop2", -2);
                    }
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseEnterMonitorStmt(EnterMonitorStmt s) {
                JasminClass.this.emitValue(s.getOp());
                JasminClass.this.emit("monitorenter", -1);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseExitMonitorStmt(ExitMonitorStmt s) {
                JasminClass.this.emitValue(s.getOp());
                JasminClass.this.emit("monitorexit", -1);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseGotoStmt(GotoStmt s) {
                if (JasminClass.this.isNextGotoAJsr) {
                    JasminClass.this.emit("jsr " + ((String) JasminClass.this.unitToLabel.get(s.getTarget())));
                    JasminClass.this.isNextGotoAJsr = false;
                    JasminClass.this.subroutineToReturnAddressSlot.put(s.getTarget(), Integer.valueOf(JasminClass.this.returnAddressSlot));
                    return;
                }
                JasminClass.this.emit("goto " + ((String) JasminClass.this.unitToLabel.get(s.getTarget())));
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseIfStmt(IfStmt s) {
                JasminClass.this.emitIfStmt(s);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseLookupSwitchStmt(LookupSwitchStmt s) {
                JasminClass.this.emitValue(s.getKey());
                JasminClass.this.emit(Jimple.LOOKUPSWITCH, -1);
                List<Unit> targets = s.getTargets();
                List<IntConstant> lookupValues = s.getLookupValues();
                for (int i = 0; i < lookupValues.size(); i++) {
                    JasminClass.this.emit("  " + lookupValues.get(i) + " : " + ((String) JasminClass.this.unitToLabel.get(targets.get(i))));
                }
                JasminClass.this.emit("  default : " + ((String) JasminClass.this.unitToLabel.get(s.getDefaultTarget())));
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseNopStmt(NopStmt s) {
                JasminClass.this.emit(Jimple.NOP, 0);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseRetStmt(RetStmt s) {
                JasminClass.this.emit("ret " + JasminClass.this.localToSlot.get(s.getStmtAddress()), 0);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseReturnStmt(ReturnStmt s) {
                Value returnValue = s.getOp();
                JasminClass.this.emitValue(returnValue);
                returnValue.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.5.1
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid return type " + t.toString());
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dreturn", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("freturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("ireturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("ireturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("ireturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("ireturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("ireturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lreturn", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("areturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("areturn", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("areturn", -1);
                    }
                });
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseReturnVoidStmt(ReturnVoidStmt s) {
                JasminClass.this.emit("return", 0);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseTableSwitchStmt(TableSwitchStmt s) {
                JasminClass.this.emitValue(s.getKey());
                JasminClass.this.emit("tableswitch " + s.getLowIndex() + " ; high = " + s.getHighIndex(), -1);
                for (Unit t : s.getTargets()) {
                    JasminClass.this.emit("  " + ((String) JasminClass.this.unitToLabel.get(t)));
                }
                JasminClass.this.emit("default : " + ((String) JasminClass.this.unitToLabel.get(s.getDefaultTarget())));
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseThrowStmt(ThrowStmt s) {
                JasminClass.this.emitValue(s.getOp());
                JasminClass.this.emit("athrow", -1);
            }
        });
    }

    void emitLocal(final Local v) {
        final int slot = this.localToSlot.get(v).intValue();
        v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.6
            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseArrayType(ArrayType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("aload_" + slot, 1);
                } else {
                    JasminClass.this.emit("aload " + slot, 1);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void defaultCase(Type t) {
                throw new RuntimeException("invalid local type to load" + t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseDoubleType(DoubleType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("dload_" + slot, 2);
                } else {
                    JasminClass.this.emit("dload " + slot, 2);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseFloatType(FloatType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("fload_" + slot, 1);
                } else {
                    JasminClass.this.emit("fload " + slot, 1);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseBooleanType(BooleanType t) {
                handleIntegerType(t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseByteType(ByteType t) {
                handleIntegerType(t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseShortType(ShortType t) {
                handleIntegerType(t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseCharType(CharType t) {
                handleIntegerType(t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseIntType(IntType t) {
                handleIntegerType(t);
            }

            public void handleIntegerType(IntegerType t) {
                if (v.equals(JasminClass.this.plusPlusHolder)) {
                    switch (JasminClass.this.plusPlusState) {
                        case 0:
                            JasminClass.this.plusPlusState = 1;
                            JasminClass.this.emitStmt(JasminClass.this.plusPlusIncrementer);
                            int diff = (JasminClass.this.plusPlusHeight - JasminClass.this.currentStackHeight) + 1;
                            if (diff > 0) {
                                JasminClass.this.code.set(JasminClass.this.plusPlusPlace, "    dup_x" + diff);
                            }
                            JasminClass.this.plusPlusHolder = null;
                            return;
                        case 1:
                            JasminClass.this.plusPlusHeight = JasminClass.this.currentStackHeight;
                            JasminClass.this.plusPlusHolder = null;
                            JasminClass.this.emitValue(JasminClass.this.plusPlusValue);
                            JasminClass.this.plusPlusPlace = JasminClass.this.code.size();
                            JasminClass.this.emit("dup", 1);
                            return;
                        case 10:
                            JasminClass.this.plusPlusState = 11;
                            JasminClass.this.plusPlusHolder = (Local) JasminClass.this.plusPlusValue;
                            JasminClass.this.emitStmt(JasminClass.this.plusPlusIncrementer);
                            int diff2 = (JasminClass.this.plusPlusHeight - JasminClass.this.currentStackHeight) + 1;
                            if (diff2 > 0 && JasminClass.this.plusPlusState == 11) {
                                JasminClass.this.code.set(JasminClass.this.plusPlusPlace, "    dup_x" + diff2);
                            }
                            JasminClass.this.plusPlusHolder = null;
                            return;
                        case 11:
                            JasminClass.this.plusPlusHeight = JasminClass.this.currentStackHeight;
                            JasminClass.this.plusPlusHolder = null;
                            JasminClass.this.emitValue(JasminClass.this.plusPlusValue);
                            if (JasminClass.this.plusPlusState != 11) {
                                JasminClass.this.emit("dup", 1);
                            }
                            JasminClass.this.plusPlusPlace = JasminClass.this.code.size();
                            return;
                    }
                }
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("iload_" + slot, 1);
                } else {
                    JasminClass.this.emit("iload " + slot, 1);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseLongType(LongType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("lload_" + slot, 2);
                } else {
                    JasminClass.this.emit("lload " + slot, 2);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseRefType(RefType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("aload_" + slot, 1);
                } else {
                    JasminClass.this.emit("aload " + slot, 1);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseNullType(NullType t) {
                if (slot >= 0 && slot <= 3) {
                    JasminClass.this.emit("aload_" + slot, 1);
                } else {
                    JasminClass.this.emit("aload " + slot, 1);
                }
            }
        });
    }

    void emitValue(Value value) {
        value.apply(new AbstractGrimpValueSwitch<Object>() { // from class: soot.jimple.JasminClass.7
            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseAddExpr(AddExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.1
                    private void handleIntCase() {
                        JasminClass.this.emit("iadd", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("ladd", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dadd", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fadd", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for add");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseAndExpr(AndExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.2
                    private void handleIntCase() {
                        JasminClass.this.emit("iand", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("land", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for and");
                    }
                });
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseArrayRef(ArrayRef v) {
                JasminClass.this.emitValue(v.getBase());
                JasminClass.this.emitValue(v.getIndex());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.3
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType ty) {
                        JasminClass.this.emit("aaload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType ty) {
                        JasminClass.this.emit("baload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType ty) {
                        JasminClass.this.emit("baload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType ty) {
                        JasminClass.this.emit("caload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type ty) {
                        throw new RuntimeException("invalid base type");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType ty) {
                        JasminClass.this.emit("daload", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType ty) {
                        JasminClass.this.emit("faload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType ty) {
                        JasminClass.this.emit("iaload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType ty) {
                        JasminClass.this.emit("laload", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType ty) {
                        JasminClass.this.emit("aaload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType ty) {
                        JasminClass.this.emit("aaload", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType ty) {
                        JasminClass.this.emit("saload", -1);
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCastExpr(final CastExpr v) {
                final Value op = v.getOp();
                JasminClass.this.emitValue(op);
                final Type toType = v.getCastType();
                if (toType instanceof RefType) {
                    JasminClass.this.emit("checkcast " + JasminClass.slashify(toType.toString()), 0);
                } else if (toType instanceof ArrayType) {
                    JasminClass.this.emit("checkcast " + JasminClass.jasminDescriptorOf(toType), 0);
                } else {
                    op.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.4
                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void defaultCase(Type ty) {
                            throw new RuntimeException("invalid fromType " + op.getType());
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseDoubleType(DoubleType ty) {
                            if (IntType.v().equals(toType)) {
                                JasminClass.this.emit("d2i", -1);
                            } else if (LongType.v().equals(toType)) {
                                JasminClass.this.emit("d2l", 0);
                            } else if (FloatType.v().equals(toType)) {
                                JasminClass.this.emit("d2f", -1);
                            } else {
                                throw new RuntimeException("invalid toType from double: " + toType);
                            }
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseFloatType(FloatType ty) {
                            if (IntType.v().equals(toType)) {
                                JasminClass.this.emit("f2i", 0);
                            } else if (LongType.v().equals(toType)) {
                                JasminClass.this.emit("f2l", 1);
                            } else if (DoubleType.v().equals(toType)) {
                                JasminClass.this.emit("f2d", 1);
                            } else {
                                throw new RuntimeException("invalid toType from float: " + toType);
                            }
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseIntType(IntType ty) {
                            emitIntToTypeCast();
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseBooleanType(BooleanType ty) {
                            emitIntToTypeCast();
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseByteType(ByteType ty) {
                            emitIntToTypeCast();
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseCharType(CharType ty) {
                            emitIntToTypeCast();
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseShortType(ShortType ty) {
                            emitIntToTypeCast();
                        }

                        private void emitIntToTypeCast() {
                            if (ByteType.v().equals(toType)) {
                                JasminClass.this.emit("i2b", 0);
                            } else if (CharType.v().equals(toType)) {
                                JasminClass.this.emit("i2c", 0);
                            } else if (ShortType.v().equals(toType)) {
                                JasminClass.this.emit("i2s", 0);
                            } else if (FloatType.v().equals(toType)) {
                                JasminClass.this.emit("i2f", 0);
                            } else if (LongType.v().equals(toType)) {
                                JasminClass.this.emit("i2l", 1);
                            } else if (DoubleType.v().equals(toType)) {
                                JasminClass.this.emit("i2d", 1);
                            } else if (!IntType.v().equals(toType) && !BooleanType.v().equals(toType)) {
                                throw new RuntimeException("invalid toType from int: " + toType + Instruction.argsep + v.toString());
                            }
                        }

                        @Override // soot.TypeSwitch, soot.ITypeSwitch
                        public void caseLongType(LongType ty) {
                            if (IntType.v().equals(toType)) {
                                JasminClass.this.emit("l2i", -1);
                            } else if (FloatType.v().equals(toType)) {
                                JasminClass.this.emit("l2f", -1);
                            } else if (DoubleType.v().equals(toType)) {
                                JasminClass.this.emit("l2d", 0);
                            } else if (ByteType.v().equals(toType)) {
                                JasminClass.this.emit("l2i", -1);
                                emitIntToTypeCast();
                            } else if (ShortType.v().equals(toType)) {
                                JasminClass.this.emit("l2i", -1);
                                emitIntToTypeCast();
                            } else if (CharType.v().equals(toType)) {
                                JasminClass.this.emit("l2i", -1);
                                emitIntToTypeCast();
                            } else if (BooleanType.v().equals(toType)) {
                                JasminClass.this.emit("l2i", -1);
                                emitIntToTypeCast();
                            } else {
                                throw new RuntimeException("invalid toType from long: " + toType);
                            }
                        }
                    });
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmpExpr(CmpExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                JasminClass.this.emit("lcmp", -3);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmpgExpr(CmpgExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                if (FloatType.v().equals(v.getOp1().getType())) {
                    JasminClass.this.emit("fcmpg", -1);
                } else {
                    JasminClass.this.emit("dcmpg", -3);
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmplExpr(CmplExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                if (FloatType.v().equals(v.getOp1().getType())) {
                    JasminClass.this.emit("fcmpl", -1);
                } else {
                    JasminClass.this.emit("dcmpl", -3);
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseDivExpr(DivExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.5
                    private void handleIntCase() {
                        JasminClass.this.emit("idiv", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("ldiv", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("ddiv", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fdiv", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for div");
                    }
                });
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseDoubleConstant(DoubleConstant v) {
                double val = v.value;
                if (val == Const.default_value_double && 1.0d / val > Const.default_value_double) {
                    JasminClass.this.emit("dconst_0", 2);
                } else if (val != 1.0d) {
                    JasminClass.this.emit("ldc2_w " + JasminClass.this.doubleToString(v), 2);
                } else {
                    JasminClass.this.emit("dconst_1", 2);
                }
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseFloatConstant(FloatConstant v) {
                float val = v.value;
                if (val == 0.0f && 1.0f / val > 0.0f) {
                    JasminClass.this.emit("fconst_0", 1);
                } else if (val == 1.0f) {
                    JasminClass.this.emit("fconst_1", 1);
                } else if (val != 2.0f) {
                    JasminClass.this.emit("ldc " + JasminClass.this.floatToString(v), 1);
                } else {
                    JasminClass.this.emit("fconst_2", 1);
                }
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseInstanceFieldRef(InstanceFieldRef v) {
                JasminClass.this.emitValue(v.getBase());
                SootFieldRef field = v.getFieldRef();
                JasminClass.this.emit("getfield " + JasminClass.slashify(field.declaringClass().getName()) + '/' + field.name() + ' ' + JasminClass.jasminDescriptorOf(field.type()), (-1) + JasminClass.sizeOfType(field.type()));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseInstanceOfExpr(InstanceOfExpr v) {
                JasminClass.this.emitValue(v.getOp());
                Type checkType = v.getCheckType();
                if (checkType instanceof RefType) {
                    JasminClass.this.emit("instanceof " + JasminClass.slashify(checkType.toString()), 0);
                } else if (checkType instanceof ArrayType) {
                    JasminClass.this.emit("instanceof " + JasminClass.jasminDescriptorOf(checkType), 0);
                }
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseIntConstant(IntConstant v) {
                int val = v.value;
                if (val == -1) {
                    JasminClass.this.emit("iconst_m1", 1);
                } else if (val >= 0 && val <= 5) {
                    JasminClass.this.emit("iconst_" + val, 1);
                } else if (val >= -128 && val <= 127) {
                    JasminClass.this.emit("bipush " + val, 1);
                } else if (val >= -32768 && val <= 32767) {
                    JasminClass.this.emit("sipush " + val, 1);
                } else {
                    JasminClass.this.emit("ldc " + v.toString(), 1);
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
                JasminClass.this.emitValue(v.getBase());
                SootMethodRef m = v.getMethodRef();
                for (int i = 0; i < m.parameterTypes().size(); i++) {
                    JasminClass.this.emitValue(v.getArg(i));
                }
                JasminClass.this.emit("invokeinterface " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m) + Instruction.argsep + (JasminClass.argCountOf(m) + 1), (-(JasminClass.argCountOf(m) + 1)) + JasminClass.sizeOfType(m.returnType()));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLengthExpr(LengthExpr v) {
                JasminClass.this.emitValue(v.getOp());
                JasminClass.this.emit("arraylength", 0);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ImmediateSwitch
            public void caseLocal(Local v) {
                JasminClass.this.emitLocal(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseLongConstant(LongConstant v) {
                long val = v.value;
                if (val == 0) {
                    JasminClass.this.emit("lconst_0", 2);
                } else if (val == 1) {
                    JasminClass.this.emit("lconst_1", 2);
                } else {
                    JasminClass.this.emit("ldc2_w " + v.toString(), 2);
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseMulExpr(MulExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.6
                    private void handleIntCase() {
                        JasminClass.this.emit("imul", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lmul", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dmul", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fmul", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for mul");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLtExpr(LtExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.7
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emitBooleanBranch("iflt");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emitBooleanBranch("iflt");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmplt", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emitBooleanBranch("iflt");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLeExpr(LeExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.8
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emitBooleanBranch("ifle");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emitBooleanBranch("ifle");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmple", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emitBooleanBranch("ifle");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseGtExpr(GtExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.9
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emitBooleanBranch("ifgt");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emitBooleanBranch("ifgt");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmpgt", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emitBooleanBranch("ifgt");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseGeExpr(GeExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.10
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emitBooleanBranch("ifge");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emitBooleanBranch("ifge");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmpge", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emitBooleanBranch("ifge");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNeExpr(NeExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.11
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpne");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpne");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmpne", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpne");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emitBooleanBranch("if_acmpne");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emitBooleanBranch("if_acmpne");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseEqExpr(EqExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getOp1().getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.12
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpeq");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -3);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpeq");
                    }

                    private void handleIntCase() {
                        JasminClass.this.emit("if_icmpeq", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("iconst_0", 1);
                        JasminClass.this.emitBooleanBranch("if_icmpeq");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emitBooleanBranch("if_acmpeq");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNegExpr(final NegExpr v) {
                JasminClass.this.emitValue(v.getOp());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.13
                    private void handleIntCase() {
                        JasminClass.this.emit("ineg", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lneg", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dneg", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fneg", 0);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for neg: " + t + ": " + v);
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewArrayExpr(NewArrayExpr v) {
                JasminClass.this.emitValue(v.getSize());
                Type baseType = v.getBaseType();
                if (baseType instanceof RefType) {
                    JasminClass.this.emit("anewarray " + JasminClass.slashify(baseType.toString()), 0);
                } else if (baseType instanceof ArrayType) {
                    JasminClass.this.emit("anewarray " + JasminClass.jasminDescriptorOf(baseType), 0);
                } else {
                    JasminClass.this.emit("newarray " + baseType.toString(), 0);
                }
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
                for (Value val : v.getSizes()) {
                    JasminClass.this.emitValue(val);
                }
                int size = v.getSizeCount();
                JasminClass.this.emit("multianewarray " + JasminClass.jasminDescriptorOf(v.getBaseType()) + Instruction.argsep + size, (-size) + 1);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewExpr(NewExpr v) {
                JasminClass.this.emit("new " + JasminClass.slashify(v.getBaseType().toString()), 1);
            }

            @Override // soot.grimp.AbstractGrimpValueSwitch, soot.grimp.GrimpValueSwitch
            public void caseNewInvokeExpr(NewInvokeExpr v) {
                JasminClass.this.emit("new " + JasminClass.slashify(v.getBaseType().toString()), 1);
                JasminClass.this.emit("dup", 1);
                SootMethodRef m = v.getMethodRef();
                for (int i = 0; i < m.parameterTypes().size(); i++) {
                    JasminClass.this.emitValue(v.getArg(i));
                }
                JasminClass.this.emit("invokespecial " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m), (-(JasminClass.argCountOf(m) + 1)) + JasminClass.sizeOfType(m.returnType()));
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseNullConstant(NullConstant v) {
                JasminClass.this.emit("aconst_null", 1);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseOrExpr(OrExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.14
                    private void handleIntCase() {
                        JasminClass.this.emit("ior", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lor", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for or");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseRemExpr(RemExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.15
                    private void handleIntCase() {
                        JasminClass.this.emit("irem", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lrem", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("drem", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("frem", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for rem");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseShlExpr(ShlExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.16
                    private void handleIntCase() {
                        JasminClass.this.emit("ishl", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lshl", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for shl");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseShrExpr(ShrExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.17
                    private void handleIntCase() {
                        JasminClass.this.emit("ishr", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lshr", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for shr");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
                JasminClass.this.emitValue(v.getBase());
                SootMethodRef m = v.getMethodRef();
                for (int i = 0; i < m.parameterTypes().size(); i++) {
                    JasminClass.this.emitValue(v.getArg(i));
                }
                JasminClass.this.emit("invokespecial " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m), (-(JasminClass.argCountOf(m) + 1)) + JasminClass.sizeOfType(m.returnType()));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseStaticInvokeExpr(StaticInvokeExpr v) {
                SootMethodRef m = v.getMethodRef();
                for (int i = 0; i < m.parameterTypes().size(); i++) {
                    JasminClass.this.emitValue(v.getArg(i));
                }
                JasminClass.this.emit("invokestatic " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m), (-JasminClass.argCountOf(m)) + JasminClass.sizeOfType(m.returnType()));
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseStaticFieldRef(StaticFieldRef v) {
                SootFieldRef field = v.getFieldRef();
                JasminClass.this.emit("getstatic " + JasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + Instruction.argsep + JasminClass.jasminDescriptorOf(field.type()), JasminClass.sizeOfType(field.type()));
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseStringConstant(StringConstant v) {
                JasminClass.this.emit("ldc " + v.toString(), 1);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseClassConstant(ClassConstant v) {
                JasminClass.this.emit("ldc " + v.toInternalString(), 1);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseSubExpr(SubExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.18
                    private void handleIntCase() {
                        JasminClass.this.emit("isub", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lsub", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dsub", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fsub", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for sub");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseUshrExpr(UshrExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.19
                    private void handleIntCase() {
                        JasminClass.this.emit("iushr", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lushr", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for ushr");
                    }
                });
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
                JasminClass.this.emitValue(v.getBase());
                SootMethodRef m = v.getMethodRef();
                for (int i = 0; i < m.parameterTypes().size(); i++) {
                    JasminClass.this.emitValue(v.getArg(i));
                }
                JasminClass.this.emit("invokevirtual " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m), (-(JasminClass.argCountOf(m) + 1)) + JasminClass.sizeOfType(m.returnType()));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseXorExpr(XorExpr v) {
                JasminClass.this.emitValue(v.getOp1());
                JasminClass.this.emitValue(v.getOp2());
                v.getType().apply(new TypeSwitch<Object>() { // from class: soot.jimple.JasminClass.7.20
                    private void handleIntCase() {
                        JasminClass.this.emit("ixor", -1);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        handleIntCase();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lxor", -2);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for xor");
                    }
                });
            }
        });
    }

    public void emitBooleanBranch(String s) {
        int count;
        if (s.contains("icmp") || s.contains("acmp")) {
            count = -2;
        } else {
            count = -1;
        }
        emit(String.valueOf(s) + " label" + this.labelCount, count);
        emit("iconst_0", 1);
        emit("goto label" + this.labelCount + 1, 0);
        StringBuilder sb = new StringBuilder("label");
        int i = this.labelCount;
        this.labelCount = i + 1;
        emit(sb.append(i).append(":").toString());
        emit("iconst_1", 1);
        StringBuilder sb2 = new StringBuilder("label");
        int i2 = this.labelCount;
        this.labelCount = i2 + 1;
        emit(sb2.append(i2).append(":").toString());
    }
}
