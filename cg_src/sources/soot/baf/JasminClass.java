package soot.baf;

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
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.PackManager;
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
import soot.coffi.Instruction;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.JasminAttribute;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.util.ArraySet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/baf/JasminClass.class */
public class JasminClass extends AbstractJasminClass {
    private static final Logger logger = LoggerFactory.getLogger(JasminClass.class);

    public JasminClass(SootClass sootClass) {
        super(sootClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.AbstractJasminClass
    public void assignColorsToLocals(Body body) {
        super.assignColorsToLocals(body);
        if (Options.v().time()) {
            Timers.v().packTimer.end();
        }
    }

    @Override // soot.AbstractJasminClass
    protected void emitMethodBody(SootMethod method) {
        Integer initialHeight;
        int i;
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.end();
        }
        Body activeBody = method.getActiveBody();
        boolean z = activeBody instanceof BafBody;
        Body activeBody2 = activeBody;
        if (!z) {
            if (activeBody instanceof JimpleBody) {
                if (Options.v().verbose()) {
                    logger.debug("Was expecting Baf body for " + method + " but found a Jimple body. Will convert body to Baf on the fly.");
                }
                activeBody2 = PackManager.v().convertJimpleBodyToBaf(method);
            } else {
                throw new RuntimeException("method: " + method.getName() + " has an invalid active body!");
            }
        }
        BafBody body = (BafBody) activeBody2;
        if (body == null) {
            throw new RuntimeException("method: " + method.getName() + " has no active body!");
        }
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.start();
        }
        Chain<Unit> instList = body.getUnits();
        int stackLimitIndex = -1;
        this.subroutineToReturnAddressSlot = new HashMap(10, 0.7f);
        this.unitToLabel = new HashMap((instList.size() * 2) + 1, 0.7f);
        this.labelCount = 0;
        for (UnitBox uBox : body.getUnitBoxes(true)) {
            InstBox box = (InstBox) uBox;
            if (!this.unitToLabel.containsKey(box.getUnit())) {
                Map<Unit, String> map = this.unitToLabel;
                Unit unit = box.getUnit();
                StringBuilder sb = new StringBuilder("label");
                int i2 = this.labelCount;
                this.labelCount = i2 + 1;
                map.put(unit, sb.append(i2).toString());
            }
        }
        Set<Unit> handlerUnits = new ArraySet<>(body.getTraps().size());
        for (Trap trap : body.getTraps()) {
            handlerUnits.add(trap.getHandlerUnit());
            if (trap.getBeginUnit() != trap.getEndUnit()) {
                emit(".catch " + slashify(trap.getException().getName()) + " from " + this.unitToLabel.get(trap.getBeginUnit()) + " to " + this.unitToLabel.get(trap.getEndUnit()) + " using " + this.unitToLabel.get(trap.getHandlerUnit()));
            }
        }
        int localCount = 0;
        int[] paramSlots = new int[method.getParameterCount()];
        int thisSlot = 0;
        Set<Local> assignedLocals = new HashSet<>();
        this.localToSlot = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
        if (!method.isStatic()) {
            thisSlot = 0;
            localCount = 0 + 1;
        }
        for (int i3 = 0; i3 < method.getParameterCount(); i3++) {
            paramSlots[i3] = localCount;
            localCount += sizeOfType(method.getParameterType(i3));
        }
        for (Unit u : instList) {
            Inst s = (Inst) u;
            if (s instanceof IdentityInst) {
                IdentityInst is = (IdentityInst) s;
                Value lhs = is.getLeftOp();
                if (lhs instanceof Local) {
                    IdentityRef identity = (IdentityRef) is.getRightOp();
                    if (identity instanceof ThisRef) {
                        if (method.isStatic()) {
                            throw new RuntimeException("Attempting to use 'this' in static method");
                        }
                        i = thisSlot;
                    } else if (identity instanceof ParameterRef) {
                        i = paramSlots[((ParameterRef) identity).getIndex()];
                    }
                    int slot = i;
                    Local l = (Local) lhs;
                    this.localToSlot.put(l, Integer.valueOf(slot));
                    assignedLocals.add(l);
                } else {
                    continue;
                }
            }
        }
        for (Local local : body.getLocals()) {
            if (assignedLocals.add(local)) {
                this.localToSlot.put(local, Integer.valueOf(localCount));
                localCount += sizeOfType(local.getType());
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
        for (Unit u2 : instList) {
            Inst s2 = (Inst) u2;
            if (this.unitToLabel.containsKey(s2)) {
                emit(String.valueOf(this.unitToLabel.get(s2)) + ":");
            }
            emitInst(s2);
        }
        this.isEmittingMethodCode = false;
        this.maxStackHeight = 0;
        if (!activeBody2.getUnits().isEmpty()) {
            BlockGraph blockGraph = new BriefBlockGraph(activeBody2);
            if (!blockGraph.getBlocks().isEmpty()) {
                List<Block> entryPoints = blockGraph.getHeads();
                for (Block entryBlock : entryPoints) {
                    if (handlerUnits.contains(entryBlock.getHead())) {
                        initialHeight = 1;
                    } else {
                        initialHeight = 0;
                    }
                    if (this.blockToStackHeight == null) {
                        this.blockToStackHeight = new HashMap();
                    }
                    this.blockToStackHeight.put(entryBlock, initialHeight);
                    if (this.blockToLogicalStackHeight == null) {
                        this.blockToLogicalStackHeight = new HashMap();
                    }
                    this.blockToLogicalStackHeight.put(entryBlock, initialHeight);
                }
                for (Block nextBlock : entryPoints) {
                    calculateStackHeight(nextBlock);
                    calculateLogicalStackHeightCheck(nextBlock);
                }
            }
        }
        int modifiers2 = method.getModifiers();
        if (!Modifier.isNative(modifiers2) && !Modifier.isAbstract(modifiers2)) {
            this.code.set(stackLimitIndex, "    .limit stack " + this.maxStackHeight);
        }
        for (Tag t : body.getTags()) {
            if (t instanceof JasminAttribute) {
                emit(".code_attribute " + t.getName() + " \"" + ((JasminAttribute) t).getJasminValue(this.unitToLabel) + "\"");
            }
        }
    }

    void emitInst(Inst inst) {
        LineNumberTag lnTag = (LineNumberTag) inst.getTag(LineNumberTag.NAME);
        if (lnTag != null) {
            emit(".line " + lnTag.getLineNumber());
        }
        inst.apply(new InstSwitch() { // from class: soot.baf.JasminClass.1
            @Override // soot.baf.InstSwitch
            public void caseReturnVoidInst(ReturnVoidInst i) {
                JasminClass.this.emit("return");
            }

            @Override // soot.baf.InstSwitch
            public void caseReturnInst(ReturnInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.1
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid return type " + t.toString());
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dreturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("freturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("ireturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("ireturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("ireturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("ireturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("ireturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lreturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("areturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("areturn");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("areturn");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseNopInst(NopInst i) {
                JasminClass.this.emit(Jimple.NOP);
            }

            @Override // soot.baf.InstSwitch
            public void caseEnterMonitorInst(EnterMonitorInst i) {
                JasminClass.this.emit("monitorenter");
            }

            @Override // soot.baf.InstSwitch
            public void casePopInst(PopInst i) {
                if (i.getWordCount() == 2) {
                    JasminClass.this.emit("pop2");
                } else {
                    JasminClass.this.emit("pop");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseExitMonitorInst(ExitMonitorInst i) {
                JasminClass.this.emit("monitorexit");
            }

            @Override // soot.baf.InstSwitch
            public void caseGotoInst(GotoInst i) {
                JasminClass.this.emit("goto " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseJSRInst(JSRInst i) {
                JasminClass.this.emit("jsr " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void casePushInst(PushInst i) {
                Constant constant = i.getConstant();
                if (constant instanceof IntConstant) {
                    IntConstant v = (IntConstant) constant;
                    int val = v.value;
                    if (val == -1) {
                        JasminClass.this.emit("iconst_m1");
                    } else if (val >= 0 && val <= 5) {
                        JasminClass.this.emit("iconst_" + val);
                    } else if (val >= -128 && val <= 127) {
                        JasminClass.this.emit("bipush " + val);
                    } else if (val < -32768 || val > 32767) {
                        JasminClass.this.emit("ldc " + v.toString());
                    } else {
                        JasminClass.this.emit("sipush " + val);
                    }
                } else if (constant instanceof StringConstant) {
                    JasminClass.this.emit("ldc " + constant.toString());
                } else if (constant instanceof ClassConstant) {
                    JasminClass.this.emit("ldc " + ((ClassConstant) constant).toInternalString());
                } else if (constant instanceof DoubleConstant) {
                    DoubleConstant v2 = (DoubleConstant) constant;
                    double val2 = v2.value;
                    if (val2 == Const.default_value_double && 1.0d / val2 > Const.default_value_double) {
                        JasminClass.this.emit("dconst_0");
                    } else if (val2 == 1.0d) {
                        JasminClass.this.emit("dconst_1");
                    } else {
                        JasminClass.this.emit("ldc2_w " + JasminClass.this.doubleToString(v2));
                    }
                } else if (constant instanceof FloatConstant) {
                    FloatConstant v3 = (FloatConstant) constant;
                    float val3 = v3.value;
                    if (val3 == 0.0f && 1.0f / val3 > 1.0f) {
                        JasminClass.this.emit("fconst_0");
                    } else if (val3 == 1.0f) {
                        JasminClass.this.emit("fconst_1");
                    } else if (val3 == 2.0f) {
                        JasminClass.this.emit("fconst_2");
                    } else {
                        JasminClass.this.emit("ldc " + JasminClass.this.floatToString(v3));
                    }
                } else if (!(constant instanceof LongConstant)) {
                    if (constant instanceof NullConstant) {
                        JasminClass.this.emit("aconst_null");
                    } else if (constant instanceof MethodHandle) {
                        throw new RuntimeException("MethodHandle constants not supported by Jasmin. Please use -asm-backend.");
                    } else {
                        throw new RuntimeException("unsupported opcode");
                    }
                } else {
                    LongConstant v4 = (LongConstant) constant;
                    long val4 = v4.value;
                    if (val4 == 0) {
                        JasminClass.this.emit("lconst_0");
                    } else if (val4 == 1) {
                        JasminClass.this.emit("lconst_1");
                    } else {
                        JasminClass.this.emit("ldc2_w " + v4.toString());
                    }
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseIdentityInst(IdentityInst i) {
                if (i.getRightOp() instanceof CaughtExceptionRef) {
                    Value leftOp = i.getLeftOp();
                    if (leftOp instanceof Local) {
                        int slot = ((Integer) JasminClass.this.localToSlot.get((Local) leftOp)).intValue();
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot);
                        } else {
                            JasminClass.this.emit("astore_" + slot);
                        }
                    }
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseStoreInst(StoreInst i) {
                final int slot = ((Integer) JasminClass.this.localToSlot.get(i.getLocal())).intValue();
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.2
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot);
                        } else {
                            JasminClass.this.emit("astore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("dstore " + slot);
                        } else {
                            JasminClass.this.emit("dstore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("fstore " + slot);
                        } else {
                            JasminClass.this.emit("fstore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot);
                        } else {
                            JasminClass.this.emit("istore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot);
                        } else {
                            JasminClass.this.emit("istore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot);
                        } else {
                            JasminClass.this.emit("istore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot);
                        } else {
                            JasminClass.this.emit("istore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("istore " + slot);
                        } else {
                            JasminClass.this.emit("istore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("lstore " + slot);
                        } else {
                            JasminClass.this.emit("lstore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot);
                        } else {
                            JasminClass.this.emit("astore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseStmtAddressType(StmtAddressType t) {
                        JasminClass.this.isNextGotoAJsr = true;
                        JasminClass.this.returnAddressSlot = slot;
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("astore " + slot);
                        } else {
                            JasminClass.this.emit("astore_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid local type:" + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseLoadInst(LoadInst i) {
                final int slot = ((Integer) JasminClass.this.localToSlot.get(i.getLocal())).intValue();
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.3
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("aload " + slot);
                        } else {
                            JasminClass.this.emit("aload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid local type to load" + t);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("dload " + slot);
                        } else {
                            JasminClass.this.emit("dload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("fload " + slot);
                        } else {
                            JasminClass.this.emit("fload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("iload " + slot);
                        } else {
                            JasminClass.this.emit("iload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("iload " + slot);
                        } else {
                            JasminClass.this.emit("iload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("iload " + slot);
                        } else {
                            JasminClass.this.emit("iload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("iload " + slot);
                        } else {
                            JasminClass.this.emit("iload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("iload " + slot);
                        } else {
                            JasminClass.this.emit("iload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("lload " + slot);
                        } else {
                            JasminClass.this.emit("lload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("aload " + slot);
                        } else {
                            JasminClass.this.emit("aload_" + slot);
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        if (slot < 0 || slot > 3) {
                            JasminClass.this.emit("aload " + slot);
                        } else {
                            JasminClass.this.emit("aload_" + slot);
                        }
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayWriteInst(ArrayWriteInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.4
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("aastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("iastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("aastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("bastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("bastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("castore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("sastore");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid type: " + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayReadInst(ArrayReadInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.5
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType ty) {
                        JasminClass.this.emit("aaload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType ty) {
                        JasminClass.this.emit("baload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType ty) {
                        JasminClass.this.emit("baload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType ty) {
                        JasminClass.this.emit("caload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type ty) {
                        throw new RuntimeException("invalid base type");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType ty) {
                        JasminClass.this.emit("daload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType ty) {
                        JasminClass.this.emit("faload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType ty) {
                        JasminClass.this.emit("iaload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType ty) {
                        JasminClass.this.emit("laload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType ty) {
                        JasminClass.this.emit("aaload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType ty) {
                        JasminClass.this.emit("aaload");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType ty) {
                        JasminClass.this.emit("saload");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNullInst(IfNullInst i) {
                JasminClass.this.emit("ifnull " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNonNullInst(IfNonNullInst i) {
                JasminClass.this.emit("ifnonnull " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfEqInst(IfEqInst i) {
                JasminClass.this.emit("ifeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNeInst(IfNeInst i) {
                JasminClass.this.emit("ifne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfGtInst(IfGtInst i) {
                JasminClass.this.emit("ifgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfGeInst(IfGeInst i) {
                JasminClass.this.emit("ifge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfLtInst(IfLtInst i) {
                JasminClass.this.emit("iflt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfLeInst(IfLeInst i) {
                JasminClass.this.emit("ifle " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpEqInst(final IfCmpEqInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.6
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("ifeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("ifeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("ifeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpeq " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpNeInst(final IfCmpNeInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.7
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("ifne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("ifne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("ifne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpne " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpGtInst(final IfCmpGtInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.8
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("ifgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("ifgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("ifgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpgt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpGeInst(final IfCmpGeInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.9
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("ifge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("ifge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("ifge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpge " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpLtInst(final IfCmpLtInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.10
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("iflt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("iflt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("iflt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmplt " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpLeInst(final IfCmpLeInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.11
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg");
                        JasminClass.this.emit("ifle " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp");
                        JasminClass.this.emit("ifle " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg");
                        JasminClass.this.emit("ifle " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmple " + ((String) JasminClass.this.unitToLabel.get(i.getTarget())));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseStaticGetInst(StaticGetInst i) {
                SootFieldRef field = i.getFieldRef();
                JasminClass.this.emit("getstatic " + JasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + Instruction.argsep + JasminClass.jasminDescriptorOf(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseStaticPutInst(StaticPutInst i) {
                SootFieldRef field = i.getFieldRef();
                JasminClass.this.emit("putstatic " + JasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + Instruction.argsep + JasminClass.jasminDescriptorOf(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseFieldGetInst(FieldGetInst i) {
                SootFieldRef field = i.getFieldRef();
                JasminClass.this.emit("getfield " + JasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + Instruction.argsep + JasminClass.jasminDescriptorOf(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseFieldPutInst(FieldPutInst i) {
                SootFieldRef field = i.getFieldRef();
                JasminClass.this.emit("putfield " + JasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + Instruction.argsep + JasminClass.jasminDescriptorOf(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseInstanceCastInst(InstanceCastInst i) {
                Type castType = i.getCastType();
                if (castType instanceof RefType) {
                    JasminClass.this.emit("checkcast " + JasminClass.slashify(((RefType) castType).getClassName()));
                } else if (castType instanceof ArrayType) {
                    JasminClass.this.emit("checkcast " + JasminClass.jasminDescriptorOf(castType));
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseInstanceOfInst(InstanceOfInst i) {
                Type checkType = i.getCheckType();
                if (checkType instanceof RefType) {
                    JasminClass.this.emit("instanceof " + JasminClass.slashify(checkType.toString()));
                } else if (checkType instanceof ArrayType) {
                    JasminClass.this.emit("instanceof " + JasminClass.jasminDescriptorOf(checkType));
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseNewInst(NewInst i) {
                JasminClass.this.emit("new " + JasminClass.slashify(i.getBaseType().getClassName()));
            }

            @Override // soot.baf.InstSwitch
            public void casePrimitiveCastInst(PrimitiveCastInst i) {
                JasminClass.this.emit(i.toString());
            }

            @Override // soot.baf.InstSwitch
            public void caseDynamicInvokeInst(DynamicInvokeInst i) {
                StringBuilder str = new StringBuilder();
                SootMethodRef m = i.getMethodRef();
                str.append("invokedynamic \"").append(m.name()).append("\" ").append(JasminClass.jasminDescriptorOf(m)).append(' ');
                SootMethodRef bsm = i.getBootstrapMethodRef();
                str.append(JasminClass.slashify(bsm.declaringClass().getName())).append('/').append(bsm.name()).append(JasminClass.jasminDescriptorOf(bsm));
                str.append('(');
                Iterator<Value> iterator = i.getBootstrapArgs().iterator();
                while (iterator.hasNext()) {
                    Value val = iterator.next();
                    str.append('(').append(JasminClass.jasminDescriptorOf(val.getType())).append(')');
                    str.append(escape(val.toString()));
                    if (iterator.hasNext()) {
                        str.append(',');
                    }
                }
                str.append(')');
                JasminClass.this.emit(str.toString());
            }

            private String escape(String bsmArgString) {
                return bsmArgString.replace(",", "\\comma").replace(Instruction.argsep, "\\blank").replace("\t", "\\tab").replace("\n", "\\newline");
            }

            @Override // soot.baf.InstSwitch
            public void caseStaticInvokeInst(StaticInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                JasminClass.this.emit("invokestatic " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m));
            }

            @Override // soot.baf.InstSwitch
            public void caseVirtualInvokeInst(VirtualInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                JasminClass.this.emit("invokevirtual " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m));
            }

            @Override // soot.baf.InstSwitch
            public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                JasminClass.this.emit("invokeinterface " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m) + Instruction.argsep + (JasminClass.argCountOf(m) + 1));
            }

            @Override // soot.baf.InstSwitch
            public void caseSpecialInvokeInst(SpecialInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                JasminClass.this.emit("invokespecial " + JasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + JasminClass.jasminDescriptorOf(m));
            }

            @Override // soot.baf.InstSwitch
            public void caseThrowInst(ThrowInst i) {
                JasminClass.this.emit("athrow");
            }

            @Override // soot.baf.InstSwitch
            public void caseCmpInst(CmpInst i) {
                JasminClass.this.emit("lcmp");
            }

            @Override // soot.baf.InstSwitch
            public void caseCmplInst(CmplInst i) {
                if (i.getOpType().equals(FloatType.v())) {
                    JasminClass.this.emit("fcmpl");
                } else {
                    JasminClass.this.emit("dcmpl");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseCmpgInst(CmpgInst i) {
                if (i.getOpType().equals(FloatType.v())) {
                    JasminClass.this.emit("fcmpg");
                } else {
                    JasminClass.this.emit("dcmpg");
                }
            }

            private void emitOpTypeInst(final String s, OpTypeArgInst i) {
                i.getOpType().apply(new TypeSwitch<Object>() { // from class: soot.baf.JasminClass.1.12
                    private void handleIntCase() {
                        JasminClass.this.emit("i" + s);
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
                        JasminClass.this.emit("l" + s);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("d" + s);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("f" + s);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid argument type for div");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseAddInst(AddInst i) {
                emitOpTypeInst("add", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseDivInst(DivInst i) {
                emitOpTypeInst("div", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseSubInst(SubInst i) {
                emitOpTypeInst("sub", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseMulInst(MulInst i) {
                emitOpTypeInst("mul", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseRemInst(RemInst i) {
                emitOpTypeInst("rem", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseShlInst(ShlInst i) {
                emitOpTypeInst("shl", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseAndInst(AndInst i) {
                emitOpTypeInst("and", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseOrInst(OrInst i) {
                emitOpTypeInst("or", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseXorInst(XorInst i) {
                emitOpTypeInst("xor", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseShrInst(ShrInst i) {
                emitOpTypeInst("shr", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseUshrInst(UshrInst i) {
                emitOpTypeInst("ushr", i);
            }

            @Override // soot.baf.InstSwitch
            public void caseIncInst(IncInst i) {
                if (i.getUseBoxes().get(0).getValue() == i.getDefBoxes().get(0).getValue()) {
                    JasminClass.this.emit("iinc " + JasminClass.this.localToSlot.get(i.getLocal()) + Instruction.argsep + i.getConstant());
                    return;
                }
                throw new RuntimeException("iinc def and use boxes don't match");
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayLengthInst(ArrayLengthInst i) {
                JasminClass.this.emit("arraylength");
            }

            @Override // soot.baf.InstSwitch
            public void caseNegInst(NegInst i) {
                emitOpTypeInst(Jimple.NEG, i);
            }

            @Override // soot.baf.InstSwitch
            public void caseNewArrayInst(NewArrayInst i) {
                if (i.getBaseType() instanceof RefType) {
                    JasminClass.this.emit("anewarray " + JasminClass.slashify(((RefType) i.getBaseType()).getClassName()));
                } else if (i.getBaseType() instanceof ArrayType) {
                    JasminClass.this.emit("anewarray " + JasminClass.jasminDescriptorOf(i.getBaseType()));
                } else {
                    JasminClass.this.emit("newarray " + i.getBaseType().toString());
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseNewMultiArrayInst(NewMultiArrayInst i) {
                JasminClass.this.emit("multianewarray " + JasminClass.jasminDescriptorOf(i.getBaseType()) + Instruction.argsep + i.getDimensionCount());
            }

            @Override // soot.baf.InstSwitch
            public void caseLookupSwitchInst(LookupSwitchInst i) {
                JasminClass.this.emit(Jimple.LOOKUPSWITCH);
                List<Unit> targets = i.getTargets();
                List<IntConstant> lookupValues = i.getLookupValues();
                for (int j = 0; j < lookupValues.size(); j++) {
                    JasminClass.this.emit("  " + lookupValues.get(j) + " : " + ((String) JasminClass.this.unitToLabel.get(targets.get(j))));
                }
                JasminClass.this.emit("  default : " + ((String) JasminClass.this.unitToLabel.get(i.getDefaultTarget())));
            }

            @Override // soot.baf.InstSwitch
            public void caseTableSwitchInst(TableSwitchInst i) {
                JasminClass.this.emit("tableswitch " + i.getLowIndex() + " ; high = " + i.getHighIndex());
                for (Unit t : i.getTargets()) {
                    JasminClass.this.emit("  " + ((String) JasminClass.this.unitToLabel.get(t)));
                }
                JasminClass.this.emit("default : " + ((String) JasminClass.this.unitToLabel.get(i.getDefaultTarget())));
            }

            private boolean isDwordType(Type t) {
                return (t instanceof LongType) || (t instanceof DoubleType) || (t instanceof DoubleWordType);
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1Inst(Dup1Inst i) {
                Type firstOpType = i.getOp1Type();
                if (isDwordType(firstOpType)) {
                    JasminClass.this.emit("dup2");
                } else {
                    JasminClass.this.emit("dup");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2Inst(Dup2Inst i) {
                Type firstOpType = i.getOp1Type();
                Type secondOpType = i.getOp2Type();
                if (isDwordType(firstOpType)) {
                    JasminClass.this.emit("dup2");
                    if (isDwordType(secondOpType)) {
                        JasminClass.this.emit("dup2");
                    } else {
                        JasminClass.this.emit("dup");
                    }
                } else if (!isDwordType(secondOpType)) {
                    JasminClass.this.emit("dup2");
                } else {
                    if (isDwordType(firstOpType)) {
                        JasminClass.this.emit("dup2");
                    } else {
                        JasminClass.this.emit("dup");
                    }
                    JasminClass.this.emit("dup2");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1_x1Inst(Dup1_x1Inst i) {
                Type opType = i.getOp1Type();
                Type underType = i.getUnder1Type();
                if (isDwordType(opType)) {
                    if (isDwordType(underType)) {
                        JasminClass.this.emit("dup2_x2");
                    } else {
                        JasminClass.this.emit("dup2_x1");
                    }
                } else if (isDwordType(underType)) {
                    JasminClass.this.emit("dup_x2");
                } else {
                    JasminClass.this.emit("dup_x1");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1_x2Inst(Dup1_x2Inst i) {
                Type opType = i.getOp1Type();
                Type under1Type = i.getUnder1Type();
                Type under2Type = i.getUnder2Type();
                if (isDwordType(opType)) {
                    if (!isDwordType(under1Type) && !isDwordType(under2Type)) {
                        JasminClass.this.emit("dup2_x2");
                    } else {
                        throw new RuntimeException("magic not implemented yet");
                    }
                } else if (isDwordType(under1Type) || isDwordType(under2Type)) {
                    throw new RuntimeException("magic not implemented yet");
                }
                JasminClass.this.emit("dup_x2");
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2_x1Inst(Dup2_x1Inst i) {
                Type op1Type = i.getOp1Type();
                Type op2Type = i.getOp2Type();
                Type under1Type = i.getUnder1Type();
                if (isDwordType(under1Type)) {
                    if (isDwordType(op1Type) || isDwordType(op2Type)) {
                        JasminClass.this.emit("dup2_x2");
                    } else {
                        throw new RuntimeException("magic not implemented yet");
                    }
                } else if ((isDwordType(op1Type) && op2Type != null) || isDwordType(op2Type)) {
                    throw new RuntimeException("magic not implemented yet");
                }
                JasminClass.this.emit("dup2_x1");
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2_x2Inst(Dup2_x2Inst i) {
                Type op1Type = i.getOp1Type();
                Type op2Type = i.getOp2Type();
                Type under1Type = i.getUnder1Type();
                Type under2Type = i.getUnder2Type();
                boolean malformed = true;
                if (isDwordType(op1Type)) {
                    if (op2Type == null && under1Type != null && ((under2Type == null && isDwordType(under1Type)) || (!isDwordType(under1Type) && under2Type != null && !isDwordType(under2Type)))) {
                        malformed = false;
                    }
                } else if (op1Type != null && op2Type != null && !isDwordType(op2Type) && ((under2Type == null && isDwordType(under1Type)) || (under1Type != null && !isDwordType(under1Type) && under2Type != null && !isDwordType(under2Type)))) {
                    malformed = false;
                }
                if (!malformed) {
                    JasminClass.this.emit("dup2_x2");
                    return;
                }
                throw new RuntimeException("magic not implemented yet");
            }

            @Override // soot.baf.InstSwitch
            public void caseSwapInst(SwapInst i) {
                JasminClass.this.emit("swap");
            }
        });
    }

    private void calculateStackHeight(Block aBlock) {
        int blockHeight = this.blockToStackHeight.get(aBlock).intValue();
        if (blockHeight > this.maxStackHeight) {
            this.maxStackHeight = blockHeight;
        }
        Iterator<Unit> it = aBlock.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Inst nInst = (Inst) u;
            int blockHeight2 = blockHeight - nInst.getInMachineCount();
            if (blockHeight2 < 0) {
                throw new RuntimeException("Negative Stack height has been attained in :" + aBlock.getBody().getMethod().getSignature() + " \nStackHeight: " + blockHeight2 + "\nAt instruction:" + nInst + "\nBlock:\n" + aBlock + "\n\nMethod: " + aBlock.getBody().getMethod().getName() + "\n" + aBlock.getBody().getMethod());
            }
            blockHeight = blockHeight2 + nInst.getOutMachineCount();
            if (blockHeight > this.maxStackHeight) {
                this.maxStackHeight = blockHeight;
            }
        }
        for (Block b : aBlock.getSuccs()) {
            Integer i = this.blockToStackHeight.get(b);
            if (i != null) {
                if (i.intValue() != blockHeight) {
                    throw new RuntimeException(String.valueOf(aBlock.getBody().getMethod().getSignature()) + ": incoherent stack height at block merge point " + b + aBlock + "\ncomputed blockHeight == " + blockHeight + " recorded blockHeight = " + i);
                }
            } else {
                this.blockToStackHeight.put(b, Integer.valueOf(blockHeight));
                calculateStackHeight(b);
            }
        }
    }

    private void calculateLogicalStackHeightCheck(Block aBlock) {
        int blockHeight = this.blockToLogicalStackHeight.get(aBlock).intValue();
        Iterator<Unit> it = aBlock.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Inst nInst = (Inst) u;
            int blockHeight2 = blockHeight - nInst.getInCount();
            if (blockHeight2 < 0) {
                throw new RuntimeException("Negative Stack Logical height has been attained: \nStackHeight: " + blockHeight2 + "\nAt instruction:" + nInst + "\nBlock:\n" + aBlock + "\n\nMethod: " + aBlock.getBody().getMethod().getName() + "\n" + aBlock.getBody().getMethod());
            }
            blockHeight = blockHeight2 + nInst.getOutCount();
        }
        for (Block b : aBlock.getSuccs()) {
            Integer i = this.blockToLogicalStackHeight.get(b);
            if (i != null) {
                if (i.intValue() != blockHeight) {
                    throw new RuntimeException("incoherent logical stack height at block merge point " + b + aBlock);
                }
            } else {
                this.blockToLogicalStackHeight.put(b, Integer.valueOf(blockHeight));
                calculateLogicalStackHeightCheck(b);
            }
        }
    }
}
