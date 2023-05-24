package soot.baf;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import soot.AbstractASMBackend;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PolymorphicMethodRef;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.TypeSwitch;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.baf.internal.BafLocal;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.ConstantSwitch;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityRef;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.util.Chain;
import soot.util.backend.ASMBackendUtils;
/* loaded from: gencallgraphv3.jar:soot/baf/BafASMBackend.class */
public class BafASMBackend extends AbstractASMBackend {
    protected final Map<Unit, Label> branchTargetLabels;
    protected final Map<Local, Integer> localToSlot;

    protected Label getBranchTargetLabel(Unit target) {
        return this.branchTargetLabels.get(target);
    }

    public BafASMBackend(SootClass sc, int javaVersion) {
        super(sc, javaVersion);
        this.branchTargetLabels = new HashMap();
        this.localToSlot = new HashMap();
    }

    @Override // soot.AbstractASMBackend
    protected int getMinJavaVersion(SootMethod method) {
        BafBody body = getBafBody(method);
        int minVersion = 2;
        if (method.getDeclaringClass().isInterface() && !method.isAbstract()) {
            minVersion = Math.max(2, 9);
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (minVersion == 10) {
                return minVersion;
            }
            if (u instanceof DynamicInvokeInst) {
                minVersion = Math.max(minVersion, 8);
            }
            if (u instanceof PushInst) {
                Constant constant = ((PushInst) u).getConstant();
                if (constant instanceof ClassConstant) {
                    minVersion = Math.max(minVersion, 6);
                }
                String typeString = constant.getType().toQuotedString();
                if (PolymorphicMethodRef.METHODHANDLE_SIGNATURE.equals(typeString)) {
                    minVersion = Math.max(minVersion, 8);
                }
                if (PolymorphicMethodRef.VARHANDLE_SIGNATURE.equals(typeString)) {
                    minVersion = Math.max(minVersion, 10);
                }
            }
        }
        return minVersion;
    }

    @Override // soot.AbstractASMBackend
    protected void generateMethodBody(MethodVisitor mv, SootMethod method) {
        Local jimpleLocal;
        BafBody body = getBafBody(method);
        for (UnitBox box : body.getUnitBoxes(true)) {
            Unit u = box.getUnit();
            if (!this.branchTargetLabels.containsKey(u)) {
                this.branchTargetLabels.put(u, new Label());
            }
        }
        Label startLabel = null;
        if (Options.v().write_local_annotations()) {
            startLabel = new Label();
            mv.visitLabel(startLabel);
        }
        for (Trap trap : body.getTraps()) {
            if (trap.getBeginUnit() != trap.getEndUnit()) {
                Label start = this.branchTargetLabels.get(trap.getBeginUnit());
                Label end = this.branchTargetLabels.get(trap.getEndUnit());
                Label handler = this.branchTargetLabels.get(trap.getHandlerUnit());
                String type = ASMBackendUtils.slashify(trap.getException().getName());
                mv.visitTryCatchBlock(start, end, handler, type);
            }
        }
        int localCount = method.isStatic() ? 0 : 1;
        int[] paramSlots = new int[method.getParameterCount()];
        int e = paramSlots.length;
        for (int i = 0; i < e; i++) {
            paramSlots[i] = localCount;
            localCount += ASMBackendUtils.sizeOfType(method.getParameterType(i));
        }
        Set<Local> assignedLocals = new HashSet<>();
        Chain<Unit> instructions = body.getUnits();
        for (Unit u2 : instructions) {
            if (u2 instanceof IdentityInst) {
                Value leftOp = ((IdentityInst) u2).getLeftOp();
                if (leftOp instanceof Local) {
                    int slot = 0;
                    IdentityRef identity = (IdentityRef) ((IdentityInst) u2).getRightOp();
                    if (identity instanceof ThisRef) {
                        if (method.isStatic()) {
                            throw new RuntimeException("Attempting to use 'this' in static method");
                        }
                    } else if (identity instanceof ParameterRef) {
                        slot = paramSlots[((ParameterRef) identity).getIndex()];
                    }
                    Local l = (Local) leftOp;
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
                localCount += ASMBackendUtils.sizeOfType(local.getType());
            }
        }
        for (Unit u3 : instructions) {
            if (this.branchTargetLabels.containsKey(u3)) {
                mv.visitLabel(this.branchTargetLabels.get(u3));
            }
            generateTagsForUnit(mv, u3);
            generateInstruction(mv, (Inst) u3);
        }
        if (Options.v().write_local_annotations()) {
            Label endLabel = new Label();
            mv.visitLabel(endLabel);
            for (Local local2 : body.getLocals()) {
                Integer slot2 = this.localToSlot.get(local2);
                if (slot2 != null && (jimpleLocal = ((BafLocal) local2).getOriginalLocal()) != null) {
                    mv.visitLocalVariable(jimpleLocal.getName(), ASMBackendUtils.toTypeDesc(jimpleLocal.getType()), null, startLabel, endLabel, slot2.intValue());
                }
            }
        }
    }

    protected void generateTagsForUnit(MethodVisitor mv, Unit u) {
        Label l;
        LineNumberTag lnt = (LineNumberTag) u.getTag(LineNumberTag.NAME);
        if (lnt != null) {
            if (this.branchTargetLabels.containsKey(u)) {
                l = this.branchTargetLabels.get(u);
            } else {
                l = new Label();
                mv.visitLabel(l);
            }
            mv.visitLineNumber(lnt.getLineNumber(), l);
        }
    }

    protected void generateInstruction(final MethodVisitor mv, Inst inst) {
        inst.apply(new InstSwitch() { // from class: soot.baf.BafASMBackend.1
            @Override // soot.baf.InstSwitch
            public void caseReturnVoidInst(ReturnVoidInst i) {
                mv.visitInsn(177);
            }

            @Override // soot.baf.InstSwitch
            public void caseReturnInst(ReturnInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.1
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitInsn(176);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(172);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(172);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(172);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(175);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(174);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(172);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(173);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitInsn(176);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(172);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitInsn(176);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid return type " + t.toString());
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseNopInst(NopInst i) {
                mv.visitInsn(0);
            }

            @Override // soot.baf.InstSwitch
            public void caseJSRInst(JSRInst i) {
                mv.visitJumpInsn(168, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void casePushInst(PushInst i) {
                Handle handle;
                Constant c = i.getConstant();
                if (c instanceof IntConstant) {
                    int v = ((IntConstant) c).value;
                    switch (v) {
                        case -1:
                            mv.visitInsn(2);
                            return;
                        case 0:
                            mv.visitInsn(3);
                            return;
                        case 1:
                            mv.visitInsn(4);
                            return;
                        case 2:
                            mv.visitInsn(5);
                            return;
                        case 3:
                            mv.visitInsn(6);
                            return;
                        case 4:
                            mv.visitInsn(7);
                            return;
                        case 5:
                            mv.visitInsn(8);
                            return;
                        default:
                            if (v >= -128 && v <= 127) {
                                mv.visitIntInsn(16, v);
                                return;
                            } else if (v >= -32768 && v <= 32767) {
                                mv.visitIntInsn(17, v);
                                return;
                            } else {
                                mv.visitLdcInsn(Integer.valueOf(v));
                                return;
                            }
                    }
                } else if (c instanceof StringConstant) {
                    mv.visitLdcInsn(((StringConstant) c).value);
                } else if (c instanceof ClassConstant) {
                    mv.visitLdcInsn(org.objectweb.asm.Type.getType(((ClassConstant) c).getValue()));
                } else if (c instanceof DoubleConstant) {
                    double v2 = ((DoubleConstant) c).value;
                    if (new Double(v2).equals(Double.valueOf((double) Const.default_value_double))) {
                        mv.visitInsn(14);
                    } else if (v2 == 1.0d) {
                        mv.visitInsn(15);
                    } else {
                        mv.visitLdcInsn(Double.valueOf(v2));
                    }
                } else if (c instanceof FloatConstant) {
                    float v3 = ((FloatConstant) c).value;
                    if (new Float(v3).equals(Float.valueOf(0.0f))) {
                        mv.visitInsn(11);
                    } else if (v3 == 1.0f) {
                        mv.visitInsn(12);
                    } else if (v3 == 2.0f) {
                        mv.visitInsn(13);
                    } else {
                        mv.visitLdcInsn(Float.valueOf(v3));
                    }
                } else if (c instanceof LongConstant) {
                    long v4 = ((LongConstant) c).value;
                    if (v4 == 0) {
                        mv.visitInsn(9);
                    } else if (v4 == 1) {
                        mv.visitInsn(10);
                    } else {
                        mv.visitLdcInsn(Long.valueOf(v4));
                    }
                } else if (c instanceof NullConstant) {
                    mv.visitInsn(1);
                } else if (c instanceof MethodHandle) {
                    if (((MethodHandle) c).isMethodRef()) {
                        SootMethodRef methodRef = ((MethodHandle) c).getMethodRef();
                        handle = new Handle(((MethodHandle) c).getKind(), ASMBackendUtils.slashify(methodRef.declaringClass().getName()), methodRef.name(), ASMBackendUtils.toTypeDesc(methodRef), methodRef.declaringClass().isInterface());
                    } else {
                        SootFieldRef fieldRef = ((MethodHandle) c).getFieldRef();
                        handle = new Handle(((MethodHandle) c).getKind(), ASMBackendUtils.slashify(fieldRef.declaringClass().getName()), fieldRef.name(), ASMBackendUtils.toTypeDesc(fieldRef.type()), fieldRef.declaringClass().isInterface());
                    }
                    mv.visitLdcInsn(handle);
                } else {
                    throw new RuntimeException("unsupported opcode");
                }
            }

            @Override // soot.baf.InstSwitch
            public void casePopInst(PopInst i) {
                if (i.getWordCount() == 2) {
                    mv.visitInsn(88);
                } else {
                    mv.visitInsn(87);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseIdentityInst(IdentityInst i) {
                Value l = i.getLeftOp();
                Value r = i.getRightOp();
                if ((r instanceof CaughtExceptionRef) && (l instanceof Local)) {
                    mv.visitVarInsn(58, BafASMBackend.this.localToSlot.get((Local) l).intValue());
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseStoreInst(StoreInst i) {
                final int slot = BafASMBackend.this.localToSlot.get(i.getLocal()).intValue();
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.2
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitVarInsn(58, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitVarInsn(54, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitVarInsn(54, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitVarInsn(54, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitVarInsn(57, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitVarInsn(56, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitVarInsn(54, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitVarInsn(55, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitVarInsn(58, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitVarInsn(54, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseStmtAddressType(StmtAddressType t) {
                        throw new RuntimeException("JSR not supported, use recent Java compiler!");
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitVarInsn(58, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid local type: " + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseGotoInst(GotoInst i) {
                mv.visitJumpInsn(167, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseLoadInst(LoadInst i) {
                final int slot = BafASMBackend.this.localToSlot.get(i.getLocal()).intValue();
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.3
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitVarInsn(25, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitVarInsn(21, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitVarInsn(21, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitVarInsn(21, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitVarInsn(24, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitVarInsn(23, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitVarInsn(21, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitVarInsn(22, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitVarInsn(25, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitVarInsn(21, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitVarInsn(25, slot);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid local type: " + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayWriteInst(ArrayWriteInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.4
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitInsn(83);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(84);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(84);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(85);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(82);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(81);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(79);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(80);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitInsn(83);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(86);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid type: " + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayReadInst(ArrayReadInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.5
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitInsn(50);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(51);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(51);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(52);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(49);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(48);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(46);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(47);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitInsn(50);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(53);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitInsn(50);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("Invalid type: " + t);
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNullInst(IfNullInst i) {
                mv.visitJumpInsn(198, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNonNullInst(IfNonNullInst i) {
                mv.visitJumpInsn(199, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfEqInst(IfEqInst i) {
                mv.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfNeInst(IfNeInst i) {
                mv.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfGtInst(IfGtInst i) {
                mv.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfGeInst(IfGeInst i) {
                mv.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfLtInst(IfLtInst i) {
                mv.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfLeInst(IfLeInst i) {
                mv.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpEqInst(final IfCmpEqInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.6
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpNeInst(final IfCmpNeInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.7
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseArrayType(ArrayType t) {
                        methodVisitor.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseRefType(RefType t) {
                        methodVisitor.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseNullType(NullType t) {
                        methodVisitor.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpGtInst(final IfCmpGtInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.8
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpGeInst(final IfCmpGeInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.9
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpLtInst(final IfCmpLtInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.10
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIfCmpLeInst(final IfCmpLeInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.11
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(152);
                        methodVisitor.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(150);
                        methodVisitor.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(148);
                        methodVisitor.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
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
                mv.visitFieldInsn(178, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseStaticPutInst(StaticPutInst i) {
                SootFieldRef field = i.getFieldRef();
                mv.visitFieldInsn(179, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseFieldGetInst(FieldGetInst i) {
                SootFieldRef field = i.getFieldRef();
                mv.visitFieldInsn(180, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseFieldPutInst(FieldPutInst i) {
                SootFieldRef field = i.getFieldRef();
                mv.visitFieldInsn(181, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
            }

            @Override // soot.baf.InstSwitch
            public void caseInstanceCastInst(InstanceCastInst i) {
                Type castType = i.getCastType();
                if (castType instanceof RefType) {
                    mv.visitTypeInsn(192, ASMBackendUtils.slashify(((RefType) castType).getClassName()));
                } else if (castType instanceof ArrayType) {
                    mv.visitTypeInsn(192, ASMBackendUtils.toTypeDesc(castType));
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseInstanceOfInst(InstanceOfInst i) {
                Type checkType = i.getCheckType();
                if (checkType instanceof RefType) {
                    mv.visitTypeInsn(193, ASMBackendUtils.slashify(((RefType) checkType).getClassName()));
                } else if (checkType instanceof ArrayType) {
                    mv.visitTypeInsn(193, ASMBackendUtils.toTypeDesc(checkType));
                }
            }

            @Override // soot.baf.InstSwitch
            public void casePrimitiveCastInst(PrimitiveCastInst i) {
                final Type to = i.getToType();
                Type fromType = i.getFromType();
                final MethodVisitor methodVisitor = mv;
                fromType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.12
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        emitIntToTypeCast();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        emitIntToTypeCast();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        emitIntToTypeCast();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        if (IntType.v().equals(to)) {
                            methodVisitor.visitInsn(142);
                        } else if (LongType.v().equals(to)) {
                            methodVisitor.visitInsn(143);
                        } else if (FloatType.v().equals(to)) {
                            methodVisitor.visitInsn(144);
                        } else {
                            throw new RuntimeException("invalid to-type from double");
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        if (IntType.v().equals(to)) {
                            methodVisitor.visitInsn(139);
                        } else if (LongType.v().equals(to)) {
                            methodVisitor.visitInsn(140);
                        } else if (DoubleType.v().equals(to)) {
                            methodVisitor.visitInsn(141);
                        } else {
                            throw new RuntimeException("invalid to-type from float");
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        emitIntToTypeCast();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        if (IntType.v().equals(to)) {
                            methodVisitor.visitInsn(136);
                        } else if (FloatType.v().equals(to)) {
                            methodVisitor.visitInsn(137);
                        } else if (DoubleType.v().equals(to)) {
                            methodVisitor.visitInsn(138);
                        } else {
                            throw new RuntimeException("invalid to-type from long");
                        }
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        emitIntToTypeCast();
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid from-type: " + t);
                    }

                    private void emitIntToTypeCast() {
                        if (ByteType.v().equals(to)) {
                            methodVisitor.visitInsn(145);
                        } else if (CharType.v().equals(to)) {
                            methodVisitor.visitInsn(146);
                        } else if (ShortType.v().equals(to)) {
                            methodVisitor.visitInsn(147);
                        } else if (FloatType.v().equals(to)) {
                            methodVisitor.visitInsn(134);
                        } else if (LongType.v().equals(to)) {
                            methodVisitor.visitInsn(133);
                        } else if (DoubleType.v().equals(to)) {
                            methodVisitor.visitInsn(135);
                        } else if (!IntType.v().equals(to) && !BooleanType.v().equals(to)) {
                            throw new RuntimeException("invalid to-type from int");
                        }
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseDynamicInvokeInst(DynamicInvokeInst i) {
                List<Value> args = i.getBootstrapArgs();
                final Object[] argsArray = new Object[args.size()];
                int index = 0;
                for (Value v : args) {
                    final int j = index;
                    v.apply(new ConstantSwitch() { // from class: soot.baf.BafASMBackend.1.13
                        @Override // soot.jimple.ConstantSwitch
                        public void defaultCase(Object object) {
                            throw new RuntimeException("Unexpected constant type!");
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseStringConstant(StringConstant v2) {
                            argsArray[j] = v2.value;
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseNullConstant(NullConstant v2) {
                            throw new RuntimeException("Unexpected NullType as argument-type in invokedynamic!");
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseMethodHandle(MethodHandle handle) {
                            if (handle.isMethodRef()) {
                                SootMethodRef methodRef = handle.getMethodRef();
                                argsArray[j] = new Handle(handle.getKind(), ASMBackendUtils.slashify(methodRef.declaringClass().getName()), methodRef.name(), ASMBackendUtils.toTypeDesc(methodRef), methodRef.declaringClass().isInterface());
                                return;
                            }
                            SootFieldRef fieldRef = handle.getFieldRef();
                            argsArray[j] = new Handle(handle.getKind(), ASMBackendUtils.slashify(fieldRef.declaringClass().getName()), fieldRef.name(), ASMBackendUtils.toTypeDesc(fieldRef.type()), fieldRef.declaringClass().isInterface());
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseMethodType(MethodType type) {
                            argsArray[j] = org.objectweb.asm.Type.getType(ASMBackendUtils.toTypeDesc(type.getParameterTypes(), type.getReturnType()));
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseLongConstant(LongConstant v2) {
                            argsArray[j] = Long.valueOf(v2.value);
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseIntConstant(IntConstant v2) {
                            argsArray[j] = Integer.valueOf(v2.value);
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseFloatConstant(FloatConstant v2) {
                            argsArray[j] = Float.valueOf(v2.value);
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseDoubleConstant(DoubleConstant v2) {
                            argsArray[j] = Double.valueOf(v2.value);
                        }

                        @Override // soot.jimple.ConstantSwitch
                        public void caseClassConstant(ClassConstant v2) {
                            argsArray[j] = org.objectweb.asm.Type.getType(v2.getValue());
                        }
                    });
                    index++;
                }
                SootMethodRef m = i.getMethodRef();
                SootMethodRef bsm = i.getBootstrapMethodRef();
                mv.visitInvokeDynamicInsn(m.name(), ASMBackendUtils.toTypeDesc(m), new Handle(i.getHandleTag(), ASMBackendUtils.slashify(bsm.declaringClass().getName()), bsm.name(), ASMBackendUtils.toTypeDesc(bsm), bsm.declaringClass().isInterface()), argsArray);
            }

            @Override // soot.baf.InstSwitch
            public void caseStaticInvokeInst(StaticInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                mv.visitMethodInsn(184, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface() && !m.isStatic());
            }

            @Override // soot.baf.InstSwitch
            public void caseVirtualInvokeInst(VirtualInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                mv.visitMethodInsn(182, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface());
            }

            @Override // soot.baf.InstSwitch
            public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                SootClass declaration = m.declaringClass();
                boolean isInterface = true;
                if (!declaration.isPhantom() && !declaration.isInterface()) {
                    isInterface = false;
                }
                mv.visitMethodInsn(185, ASMBackendUtils.slashify(declaration.getName()), m.name(), ASMBackendUtils.toTypeDesc(m), isInterface);
            }

            @Override // soot.baf.InstSwitch
            public void caseSpecialInvokeInst(SpecialInvokeInst i) {
                SootMethodRef m = i.getMethodRef();
                mv.visitMethodInsn(183, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface());
            }

            @Override // soot.baf.InstSwitch
            public void caseThrowInst(ThrowInst i) {
                mv.visitInsn(191);
            }

            @Override // soot.baf.InstSwitch
            public void caseAddInst(AddInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.14
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(96);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(96);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(96);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(99);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(98);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(96);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(97);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(96);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseAndInst(AndInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(127);
                } else {
                    mv.visitInsn(126);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseOrInst(OrInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(129);
                } else {
                    mv.visitInsn(128);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseXorInst(XorInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(131);
                } else {
                    mv.visitInsn(130);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseArrayLengthInst(ArrayLengthInst i) {
                mv.visitInsn(190);
            }

            @Override // soot.baf.InstSwitch
            public void caseCmpInst(CmpInst i) {
                mv.visitInsn(148);
            }

            @Override // soot.baf.InstSwitch
            public void caseCmpgInst(CmpgInst i) {
                if (i.getOpType().equals(FloatType.v())) {
                    mv.visitInsn(150);
                } else {
                    mv.visitInsn(152);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseCmplInst(CmplInst i) {
                if (i.getOpType().equals(FloatType.v())) {
                    mv.visitInsn(149);
                } else {
                    mv.visitInsn(151);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDivInst(DivInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.15
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(108);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(108);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(108);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(111);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(110);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(108);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(109);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(108);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseIncInst(IncInst i) {
                if (i.getUseBoxes().get(0).getValue() != i.getDefBoxes().get(0).getValue()) {
                    throw new RuntimeException("iinc def and use boxes don't match");
                }
                if (i.getConstant() instanceof IntConstant) {
                    mv.visitIincInsn(BafASMBackend.this.localToSlot.get(i.getLocal()).intValue(), ((IntConstant) i.getConstant()).value);
                    return;
                }
                throw new RuntimeException("Wrong constant type for increment!");
            }

            @Override // soot.baf.InstSwitch
            public void caseMulInst(MulInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.16
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(104);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(104);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(104);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(107);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(106);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(104);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(105);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(104);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseRemInst(RemInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.17
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(112);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(112);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(112);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(115);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(114);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(112);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(113);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(112);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseSubInst(SubInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.18
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(100);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(100);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(100);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(103);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(102);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(100);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(101);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(100);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseShlInst(ShlInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(121);
                } else {
                    mv.visitInsn(120);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseShrInst(ShrInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(123);
                } else {
                    mv.visitInsn(122);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseUshrInst(UshrInst i) {
                if (i.getOpType().equals(LongType.v())) {
                    mv.visitInsn(125);
                } else {
                    mv.visitInsn(124);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseNewInst(NewInst i) {
                mv.visitTypeInsn(187, ASMBackendUtils.slashify(i.getBaseType().getClassName()));
            }

            @Override // soot.baf.InstSwitch
            public void caseNegInst(NegInst i) {
                Type opType = i.getOpType();
                final MethodVisitor methodVisitor = mv;
                opType.apply(new TypeSwitch() { // from class: soot.baf.BafASMBackend.1.19
                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseBooleanType(BooleanType t) {
                        methodVisitor.visitInsn(116);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseByteType(ByteType t) {
                        methodVisitor.visitInsn(116);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseCharType(CharType t) {
                        methodVisitor.visitInsn(116);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseDoubleType(DoubleType t) {
                        methodVisitor.visitInsn(119);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseFloatType(FloatType t) {
                        methodVisitor.visitInsn(118);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseIntType(IntType t) {
                        methodVisitor.visitInsn(116);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseLongType(LongType t) {
                        methodVisitor.visitInsn(117);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void caseShortType(ShortType t) {
                        methodVisitor.visitInsn(116);
                    }

                    @Override // soot.TypeSwitch, soot.ITypeSwitch
                    public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                    }
                });
            }

            @Override // soot.baf.InstSwitch
            public void caseSwapInst(SwapInst i) {
                mv.visitInsn(95);
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1Inst(Dup1Inst i) {
                if (ASMBackendUtils.sizeOfType(i.getOp1Type()) == 2) {
                    mv.visitInsn(92);
                } else {
                    mv.visitInsn(89);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2Inst(Dup2Inst i) {
                Type firstOpType = i.getOp1Type();
                Type secondOpType = i.getOp2Type();
                if (ASMBackendUtils.sizeOfType(firstOpType) == 2) {
                    mv.visitInsn(92);
                    if (ASMBackendUtils.sizeOfType(secondOpType) == 2) {
                        mv.visitInsn(92);
                    } else {
                        mv.visitInsn(89);
                    }
                } else if (ASMBackendUtils.sizeOfType(secondOpType) == 2) {
                    mv.visitInsn(89);
                    mv.visitInsn(92);
                } else {
                    mv.visitInsn(92);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1_x1Inst(Dup1_x1Inst i) {
                Type opType = i.getOp1Type();
                Type underType = i.getUnder1Type();
                if (ASMBackendUtils.sizeOfType(opType) == 2) {
                    if (ASMBackendUtils.sizeOfType(underType) == 2) {
                        mv.visitInsn(94);
                    } else {
                        mv.visitInsn(93);
                    }
                } else if (ASMBackendUtils.sizeOfType(underType) == 2) {
                    mv.visitInsn(91);
                } else {
                    mv.visitInsn(90);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup1_x2Inst(Dup1_x2Inst i) {
                int toSkip = ASMBackendUtils.sizeOfType(i.getUnder1Type()) + ASMBackendUtils.sizeOfType(i.getUnder2Type());
                if (ASMBackendUtils.sizeOfType(i.getOp1Type()) == 2) {
                    if (toSkip == 2) {
                        mv.visitInsn(94);
                        return;
                    }
                    throw new RuntimeException("magic not implemented yet");
                } else if (toSkip == 2) {
                    mv.visitInsn(91);
                } else {
                    throw new RuntimeException("magic not implemented yet");
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2_x1Inst(Dup2_x1Inst i) {
                int toDup = ASMBackendUtils.sizeOfType(i.getOp1Type()) + ASMBackendUtils.sizeOfType(i.getOp2Type());
                if (toDup == 2) {
                    if (ASMBackendUtils.sizeOfType(i.getUnder1Type()) == 2) {
                        mv.visitInsn(94);
                        return;
                    } else {
                        mv.visitInsn(93);
                        return;
                    }
                }
                throw new RuntimeException("magic not implemented yet");
            }

            @Override // soot.baf.InstSwitch
            public void caseDup2_x2Inst(Dup2_x2Inst i) {
                int toDup = ASMBackendUtils.sizeOfType(i.getOp1Type()) + ASMBackendUtils.sizeOfType(i.getOp2Type());
                int toSkip = ASMBackendUtils.sizeOfType(i.getUnder1Type()) + ASMBackendUtils.sizeOfType(i.getUnder2Type());
                if (toDup > 2 || toSkip > 2) {
                    throw new RuntimeException("magic not implemented yet");
                }
                if (toDup == 2 && toSkip == 2) {
                    mv.visitInsn(94);
                    return;
                }
                throw new RuntimeException("VoidType not allowed in Dup2_x2 Instruction");
            }

            @Override // soot.baf.InstSwitch
            public void caseNewArrayInst(NewArrayInst i) {
                int type;
                Type t = i.getBaseType();
                if (t instanceof RefType) {
                    mv.visitTypeInsn(189, ASMBackendUtils.slashify(((RefType) t).getClassName()));
                } else if (t instanceof ArrayType) {
                    mv.visitTypeInsn(189, ASMBackendUtils.toTypeDesc(t));
                } else {
                    if (BooleanType.v().equals(t)) {
                        type = 4;
                    } else if (CharType.v().equals(t)) {
                        type = 5;
                    } else if (FloatType.v().equals(t)) {
                        type = 6;
                    } else if (DoubleType.v().equals(t)) {
                        type = 7;
                    } else if (ByteType.v().equals(t)) {
                        type = 8;
                    } else if (ShortType.v().equals(t)) {
                        type = 9;
                    } else if (IntType.v().equals(t)) {
                        type = 10;
                    } else if (LongType.v().equals(t)) {
                        type = 11;
                    } else {
                        throw new RuntimeException("invalid type");
                    }
                    mv.visitIntInsn(188, type);
                }
            }

            @Override // soot.baf.InstSwitch
            public void caseNewMultiArrayInst(NewMultiArrayInst i) {
                mv.visitMultiANewArrayInsn(ASMBackendUtils.toTypeDesc(i.getBaseType()), i.getDimensionCount());
            }

            @Override // soot.baf.InstSwitch
            public void caseLookupSwitchInst(LookupSwitchInst i) {
                List<IntConstant> values = i.getLookupValues();
                List<Unit> targets = i.getTargets();
                int size = values.size();
                int[] keys = new int[size];
                Label[] labels = new Label[size];
                for (int j = 0; j < size; j++) {
                    keys[j] = values.get(j).value;
                    labels[j] = BafASMBackend.this.branchTargetLabels.get(targets.get(j));
                }
                mv.visitLookupSwitchInsn(BafASMBackend.this.branchTargetLabels.get(i.getDefaultTarget()), keys, labels);
            }

            @Override // soot.baf.InstSwitch
            public void caseTableSwitchInst(TableSwitchInst i) {
                List<Unit> targets = i.getTargets();
                int size = targets.size();
                Label[] labels = new Label[size];
                for (int j = 0; j < size; j++) {
                    labels[j] = BafASMBackend.this.branchTargetLabels.get(targets.get(j));
                }
                mv.visitTableSwitchInsn(i.getLowIndex(), i.getHighIndex(), BafASMBackend.this.branchTargetLabels.get(i.getDefaultTarget()), labels);
            }

            @Override // soot.baf.InstSwitch
            public void caseEnterMonitorInst(EnterMonitorInst i) {
                mv.visitInsn(194);
            }

            @Override // soot.baf.InstSwitch
            public void caseExitMonitorInst(ExitMonitorInst i) {
                mv.visitInsn(195);
            }
        });
    }
}
