package soot.asm;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.LambdaMetaFactory;
import soot.Local;
import soot.LongType;
import soot.MethodSource;
import soot.ModuleScene;
import soot.ModuleUtil;
import soot.PackManager;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.coffi.Util;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.MonitorStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmMethodSource.class */
public class AsmMethodSource implements MethodSource {
    private static final Logger logger;
    private static final Operand DWORD_DUMMY;
    private static final String METAFACTORY_SIGNATURE = "<java.lang.invoke.LambdaMetafactory: java.lang.invoke.CallSite metafactory(java.lang.invoke.MethodHandles$Lookup,java.lang.String,java.lang.invoke.MethodType,java.lang.invoke.MethodType,java.lang.invoke.MethodHandle,java.lang.invoke.MethodType)>";
    private static final String ALT_METAFACTORY_SIGNATURE = "<java.lang.invoke.LambdaMetafactory: java.lang.invoke.CallSite altMetafactory(java.lang.invoke.MethodHandles$Lookup,java.lang.String,java.lang.invoke.MethodType,java.lang.Object[])>";
    private final String module;
    private final int maxLocals;
    private final InsnList instructions;
    private final List<LocalVariableNode> localVars;
    private final List<TryCatchBlockNode> tryCatchBlocks;
    protected int nextLocal;
    protected Map<Integer, Local> locals;
    private Multimap<LabelNode, UnitBox> labels;
    private Map<AbstractInsnNode, Unit> units;
    private ArrayList<Operand> stack;
    private Map<AbstractInsnNode, StackFrame> frames;
    private Multimap<LabelNode, UnitBox> trapHandlers;
    private JimpleBody body;
    private Table<AbstractInsnNode, AbstractInsnNode, Edge> edges;
    private ArrayDeque<Edge> conversionWorklist;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Set<LabelNode> inlineExceptionLabels = new LinkedHashSet();
    private final Map<LabelNode, Unit> inlineExceptionHandlers = new LinkedHashMap();
    private final CastAndReturnInliner castAndReturnInliner = new CastAndReturnInliner();
    private int lastLineNumber = -1;

    static {
        $assertionsDisabled = !AsmMethodSource.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(AsmMethodSource.class);
        DWORD_DUMMY = new Operand(null, null);
    }

    public AsmMethodSource(int maxLocals, InsnList insns, List<LocalVariableNode> localVars, List<TryCatchBlockNode> tryCatchBlocks, String module) {
        this.maxLocals = maxLocals;
        this.instructions = insns;
        this.localVars = localVars;
        this.tryCatchBlocks = tryCatchBlocks;
        this.module = module;
    }

    private StackFrame getFrame(AbstractInsnNode insn) {
        StackFrame frame = this.frames.get(insn);
        if (frame == null) {
            frame = new StackFrame(this);
            this.frames.put(insn, frame);
        }
        return frame;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v20, types: [soot.SootClass] */
    private SootClass getClassFromScene(String className) {
        SootClass result;
        if (ModuleUtil.module_mode()) {
            result = ModuleScene.v().getSootClassUnsafe(className, Optional.fromNullable(this.module));
        } else {
            result = Scene.v().getSootClassUnsafe(className);
        }
        if (result == null) {
            String msg = String.format("%s was not found on classpath.", className);
            if (Options.v().allow_phantom_refs()) {
                RefType ref = RefType.v(className);
                ?? r0 = ref;
                synchronized (r0) {
                    logger.warn(msg);
                    SootClass result2 = Scene.v().makeSootClass(className, 1);
                    Scene.v().addClass(result2);
                    result2.setPhantomClass();
                    r0 = ref.getSootClass();
                }
                return r0;
            }
            throw new RuntimeException(msg);
        }
        return result;
    }

    private Local getLocal(int idx) {
        if (idx >= this.maxLocals) {
            throw new IllegalArgumentException("Invalid local index: " + idx);
        }
        Integer i = Integer.valueOf(idx);
        Local l = this.locals.get(i);
        if (l == null) {
            String name = getLocalName(idx);
            l = Jimple.v().newLocal(name, UnknownType.v());
            this.locals.put(i, l);
        }
        return l;
    }

    protected String getLocalName(int idx) {
        String name;
        if (this.localVars != null) {
            name = null;
            Iterator<LocalVariableNode> it = this.localVars.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LocalVariableNode lvn = it.next();
                if (lvn.index == idx && lvn.start != lvn.end) {
                    name = lvn.name;
                    break;
                }
            }
            if (name == null) {
                name = "l" + idx;
            }
        } else {
            name = "l" + idx;
        }
        return name;
    }

    private void push(Operand opr) {
        this.stack.add(opr);
    }

    private void pushDual(Operand opr) {
        this.stack.add(DWORD_DUMMY);
        this.stack.add(opr);
    }

    private Operand peek() {
        return this.stack.get(this.stack.size() - 1);
    }

    private void push(Type t, Operand opr) {
        if (AsmUtil.isDWord(t)) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private Operand pop() {
        if (this.stack.isEmpty()) {
            throw new RuntimeException("Stack underrun");
        }
        return this.stack.remove(this.stack.size() - 1);
    }

    private Operand popDual() {
        Operand o = pop();
        Operand o2 = pop();
        if (o2 != DWORD_DUMMY && o2 != o) {
            throw new AssertionError("Not dummy operand, " + o2.value + " -- " + o.value);
        }
        return o;
    }

    private Operand pop(Type t) {
        return AsmUtil.isDWord(t) ? popDual() : pop();
    }

    private Operand popLocal(Operand o) {
        Value v = o.value;
        if (o.stack == null && !(v instanceof Local)) {
            Local l = newStackLocal();
            o.stack = l;
            setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
            o.updateBoxes();
        }
        return o;
    }

    private Operand popImmediate(Operand o) {
        Value v = o.value;
        if (o.stack == null && !(v instanceof Local) && !(v instanceof Constant)) {
            Local l = newStackLocal();
            o.stack = l;
            setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
            o.updateBoxes();
        }
        return o;
    }

    private Operand popStackConst(Operand o) {
        Value v = o.value;
        if (o.stack == null && !(v instanceof Constant)) {
            Local l = newStackLocal();
            o.stack = l;
            setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
            o.updateBoxes();
        }
        return o;
    }

    private Operand popLocal() {
        return popLocal(pop());
    }

    private Operand popLocalDual() {
        return popLocal(popDual());
    }

    private Operand popLocal(Type t) {
        return AsmUtil.isDWord(t) ? popLocalDual() : popLocal();
    }

    private Operand popImmediate() {
        return popImmediate(pop());
    }

    private Operand popImmediateDual() {
        return popImmediate(popDual());
    }

    private Operand popImmediate(Type t) {
        return AsmUtil.isDWord(t) ? popImmediateDual() : popImmediate();
    }

    private Operand popStackConst() {
        return popStackConst(pop());
    }

    private Operand popStackConstDual() {
        return popStackConst(popDual());
    }

    private Operand popStackConst(Type t) {
        return AsmUtil.isDWord(t) ? popStackConstDual() : popStackConst();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUnit(AbstractInsnNode insn, Unit u) {
        if (Options.v().keep_line_number() && this.lastLineNumber >= 0) {
            Tag lineTag = u.getTag(LineNumberTag.NAME);
            if (lineTag == null) {
                u.addTag(new LineNumberTag(this.lastLineNumber));
            } else if (((LineNumberTag) lineTag).getLineNumber() != this.lastLineNumber) {
                throw new RuntimeException("Line tag mismatch");
            }
        }
        Unit o = this.units.put(insn, u);
        if (o != null) {
            throw new AssertionError(String.valueOf(insn.getOpcode()) + " already has a unit, " + o);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mergeUnits(AbstractInsnNode insn, Unit u) {
        Unit prev = this.units.put(insn, u);
        if (prev != null) {
            Unit merged = new UnitContainer(prev, u);
            this.units.put(insn, merged);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local newStackLocal() {
        int i = this.nextLocal;
        this.nextLocal = i + 1;
        Integer idx = Integer.valueOf(i);
        Local l = Jimple.v().newLocal("$stack" + idx, UnknownType.v());
        this.locals.put(idx, l);
        return l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <A extends Unit> A getUnit(AbstractInsnNode insn) {
        return (A) this.units.get(insn);
    }

    private void assignReadOps(Local l) {
        if (this.stack.isEmpty()) {
            return;
        }
        Iterator<Operand> it = this.stack.iterator();
        while (it.hasNext()) {
            Operand opr = it.next();
            if (opr != DWORD_DUMMY && opr.stack == null && (l != null || !(opr.value instanceof Local))) {
                if (l != null && !opr.value.equivTo(l)) {
                    List<ValueBox> uses = opr.value.getUseBoxes();
                    boolean noref = true;
                    Iterator<ValueBox> it2 = uses.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        ValueBox use = it2.next();
                        Value val = use.getValue();
                        if (val.equivTo(l)) {
                            noref = false;
                            break;
                        }
                    }
                    if (noref) {
                    }
                }
                int op = opr.insn.getOpcode();
                if (l != null || op == 180 || op == 178 || op >= 46 || op <= 53) {
                    Local stack = newStackLocal();
                    opr.stack = stack;
                    AssignStmt as = Jimple.v().newAssignStmt(stack, opr.value);
                    opr.updateBoxes();
                    setUnit(opr.insn, as);
                }
            }
        }
    }

    private void convertGetFieldInsn(FieldInsnNode insn) {
        Operand opr;
        Type type;
        Value val;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            SootClass declClass = getClassFromScene(AsmUtil.toQualifiedName(insn.owner));
            type = AsmUtil.toJimpleType(insn.desc, Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
            if (insn.getOpcode() == 178) {
                SootFieldRef ref = Scene.v().makeFieldRef(declClass, insn.name, type, true);
                val = Jimple.v().newStaticFieldRef(ref);
            } else {
                Operand base = popLocal();
                SootFieldRef ref2 = Scene.v().makeFieldRef(declClass, insn.name, type, false);
                InstanceFieldRef ifr = Jimple.v().newInstanceFieldRef(base.stackOrValue(), ref2);
                val = ifr;
                base.addBox(ifr.getBaseBox());
                frame.in(base);
                frame.boxes(ifr.getBaseBox());
            }
            opr = new Operand(insn, val);
            frame.out(opr);
        } else {
            opr = out[0];
            type = ((FieldRef) opr.value()).getFieldRef().type();
            if (insn.getOpcode() == 180) {
                frame.mergeIn(pop());
            }
        }
        push(type, opr);
    }

    private void convertPutFieldInsn(FieldInsnNode insn) {
        Value val;
        boolean instance = insn.getOpcode() == 181;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            SootClass declClass = getClassFromScene(AsmUtil.toQualifiedName(insn.owner));
            Type type = AsmUtil.toJimpleType(insn.desc, Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
            Operand rvalue = popImmediate(type);
            if (!instance) {
                SootFieldRef ref = Scene.v().makeFieldRef(declClass, insn.name, type, true);
                val = Jimple.v().newStaticFieldRef(ref);
                frame.in(rvalue);
            } else {
                Operand base = popLocal();
                SootFieldRef ref2 = Scene.v().makeFieldRef(declClass, insn.name, type, false);
                InstanceFieldRef ifr = Jimple.v().newInstanceFieldRef(base.stackOrValue(), ref2);
                val = ifr;
                base.addBox(ifr.getBaseBox());
                frame.in(rvalue, base);
            }
            Operand opr = new Operand(insn, val);
            frame.out(opr);
            AssignStmt as = Jimple.v().newAssignStmt(val, rvalue.stackOrValue());
            rvalue.addBox(as.getRightOpBox());
            if (!instance) {
                frame.boxes(as.getRightOpBox());
            } else {
                frame.boxes(as.getRightOpBox(), ((InstanceFieldRef) val).getBaseBox());
            }
            setUnit(insn, as);
        } else {
            Operand opr2 = out[0];
            Operand rvalue2 = pop(((FieldRef) opr2.value()).getFieldRef().type());
            if (!instance) {
                frame.mergeIn(rvalue2);
            } else {
                frame.mergeIn(rvalue2, pop());
            }
        }
        assignReadOps(null);
    }

    private void convertFieldInsn(FieldInsnNode insn) {
        int op = insn.getOpcode();
        if (op == 178 || op == 180) {
            convertGetFieldInsn(insn);
        } else {
            convertPutFieldInsn(insn);
        }
    }

    private void convertIincInsn(IincInsnNode insn) {
        Local local = getLocal(insn.var);
        assignReadOps(local);
        if (!this.units.containsKey(insn)) {
            AddExpr add = Jimple.v().newAddExpr(local, IntConstant.v(insn.incr));
            setUnit(insn, Jimple.v().newAssignStmt(local, add));
        }
    }

    private void convertConstInsn(InsnNode insn) {
        Operand opr;
        Value v;
        int op = insn.getOpcode();
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            if (op == 1) {
                v = NullConstant.v();
            } else if (op >= 2 && op <= 8) {
                v = IntConstant.v(op - 3);
            } else if (op == 9 || op == 10) {
                v = LongConstant.v(op - 9);
            } else if (op >= 11 && op <= 13) {
                v = FloatConstant.v(op - 11);
            } else if (op == 14 || op == 15) {
                v = DoubleConstant.v(op - 14);
            } else {
                throw new AssertionError("Unknown constant opcode: " + op);
            }
            opr = new Operand(insn, v);
            frame.out(opr);
        } else {
            opr = out[0];
        }
        if (op == 9 || op == 10 || op == 14 || op == 15) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertArrayLoadInsn(InsnNode insn) {
        Operand opr;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            Operand indx = popImmediate();
            Operand base = popImmediate();
            if (base.value == NullConstant.v()) {
                opr = new Operand(insn, NullConstant.v());
                frame.in(indx, base);
                frame.out(opr);
            } else {
                ArrayRef ar = Jimple.v().newArrayRef(base.stackOrValue(), indx.stackOrValue());
                indx.addBox(ar.getIndexBox());
                base.addBox(ar.getBaseBox());
                opr = new Operand(insn, ar);
                frame.in(indx, base);
                frame.boxes(ar.getIndexBox(), ar.getBaseBox());
                frame.out(opr);
            }
        } else {
            opr = out[0];
            frame.mergeIn(pop(), pop());
        }
        int op = insn.getOpcode();
        if (op == 49 || op == 47) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertArrayStoreInsn(InsnNode insn) {
        int op = insn.getOpcode();
        boolean dword = op == 80 || op == 82;
        StackFrame frame = getFrame(insn);
        if (!this.units.containsKey(insn)) {
            Operand valu = dword ? popImmediateDual() : popImmediate();
            Operand indx = popImmediate();
            Operand base = popLocal();
            ArrayRef ar = Jimple.v().newArrayRef(base.stackOrValue(), indx.stackOrValue());
            indx.addBox(ar.getIndexBox());
            base.addBox(ar.getBaseBox());
            AssignStmt as = Jimple.v().newAssignStmt(ar, valu.stackOrValue());
            valu.addBox(as.getRightOpBox());
            frame.in(valu, indx, base);
            frame.boxes(as.getRightOpBox(), ar.getIndexBox(), ar.getBaseBox());
            setUnit(insn, as);
            return;
        }
        Operand[] operandArr = new Operand[3];
        operandArr[0] = dword ? popDual() : pop();
        operandArr[1] = pop();
        operandArr[2] = pop();
        frame.mergeIn(operandArr);
    }

    private void convertDupInsn(InsnNode insn) {
        int op = insn.getOpcode();
        Operand dupd = popImmediate();
        Operand dupd2 = null;
        boolean dword = op == 92 || op == 93 || op == 94;
        if (dword) {
            if (peek() == DWORD_DUMMY) {
                pop();
                dupd2 = dupd;
            } else {
                dupd2 = popImmediate();
            }
        }
        switch (op) {
            case 89:
                push(dupd);
                push(dupd);
                return;
            case 90:
                Operand o2 = popImmediate();
                push(dupd);
                push(o2);
                push(dupd);
                return;
            case 91:
                Operand o22 = popImmediate();
                Operand o3 = peek() == DWORD_DUMMY ? pop() : popImmediate();
                push(dupd);
                push(o3);
                push(o22);
                push(dupd);
                return;
            case 92:
                push(dupd2);
                push(dupd);
                push(dupd2);
                push(dupd);
                return;
            case 93:
                Operand o23 = popImmediate();
                push(dupd2);
                push(dupd);
                push(o23);
                push(dupd2);
                push(dupd);
                return;
            case 94:
                Operand o24 = popImmediate();
                Operand o2h = peek() == DWORD_DUMMY ? pop() : popImmediate();
                push(dupd2);
                push(dupd);
                push(o2h);
                push(o24);
                push(dupd2);
                push(dupd);
                return;
            default:
                return;
        }
    }

    private void convertBinopInsn(InsnNode insn) {
        Operand opr;
        BinopExpr binop;
        int op = insn.getOpcode();
        boolean dword = op == 99 || op == 97 || op == 103 || op == 101 || op == 107 || op == 105 || op == 111 || op == 109 || op == 115 || op == 113 || op == 121 || op == 123 || op == 125 || op == 127 || op == 129 || op == 131 || op == 148 || op == 151 || op == 152;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            Operand op2 = (!dword || op == 121 || op == 123 || op == 125) ? popImmediate() : popImmediateDual();
            Operand op1 = dword ? popImmediateDual() : popImmediate();
            Value v1 = op1.stackOrValue();
            Value v2 = op2.stackOrValue();
            if (op >= 96 && op <= 99) {
                binop = Jimple.v().newAddExpr(v1, v2);
            } else if (op >= 100 && op <= 103) {
                binop = Jimple.v().newSubExpr(v1, v2);
            } else if (op >= 104 && op <= 107) {
                binop = Jimple.v().newMulExpr(v1, v2);
            } else if (op >= 108 && op <= 111) {
                binop = Jimple.v().newDivExpr(v1, v2);
            } else if (op >= 112 && op <= 115) {
                binop = Jimple.v().newRemExpr(v1, v2);
            } else if (op >= 120 && op <= 121) {
                binop = Jimple.v().newShlExpr(v1, v2);
            } else if (op >= 122 && op <= 123) {
                binop = Jimple.v().newShrExpr(v1, v2);
            } else if (op >= 124 && op <= 125) {
                binop = Jimple.v().newUshrExpr(v1, v2);
            } else if (op >= 126 && op <= 127) {
                binop = Jimple.v().newAndExpr(v1, v2);
            } else if (op >= 128 && op <= 129) {
                binop = Jimple.v().newOrExpr(v1, v2);
            } else if (op >= 130 && op <= 131) {
                binop = Jimple.v().newXorExpr(v1, v2);
            } else if (op == 148) {
                binop = Jimple.v().newCmpExpr(v1, v2);
            } else if (op == 149 || op == 151) {
                binop = Jimple.v().newCmplExpr(v1, v2);
            } else if (op == 150 || op == 152) {
                binop = Jimple.v().newCmpgExpr(v1, v2);
            } else {
                throw new AssertionError("Unknown binop: " + op);
            }
            op1.addBox(binop.getOp1Box());
            op2.addBox(binop.getOp2Box());
            opr = new Operand(insn, binop);
            frame.in(op2, op1);
            frame.boxes(binop.getOp2Box(), binop.getOp1Box());
            frame.out(opr);
        } else {
            opr = out[0];
            if (dword) {
                if (op != 121 && op != 123 && op != 125) {
                    frame.mergeIn(popDual(), popDual());
                } else {
                    frame.mergeIn(pop(), popDual());
                }
            } else {
                frame.mergeIn(pop(), pop());
            }
        }
        if (dword && (op < 148 || op > 152)) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertUnopInsn(InsnNode insn) {
        Operand opr;
        UnopExpr unop;
        int op = insn.getOpcode();
        boolean dword = op == 117 || op == 119;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            Operand op1 = dword ? popImmediateDual() : popImmediate();
            Value v1 = op1.stackOrValue();
            if (op >= 116 && op <= 119) {
                unop = Jimple.v().newNegExpr(v1);
            } else if (op == 190) {
                unop = Jimple.v().newLengthExpr(v1);
            } else {
                throw new AssertionError("Unknown unop: " + op);
            }
            op1.addBox(unop.getOpBox());
            opr = new Operand(insn, unop);
            frame.in(op1);
            frame.boxes(unop.getOpBox());
            frame.out(opr);
        } else {
            opr = out[0];
            Operand[] operandArr = new Operand[1];
            operandArr[0] = dword ? popDual() : pop();
            frame.mergeIn(operandArr);
        }
        if (dword) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertPrimCastInsn(InsnNode insn) {
        Operand opr;
        Type totype;
        int op = insn.getOpcode();
        boolean tod = op == 133 || op == 135 || op == 140 || op == 141 || op == 143 || op == 138;
        boolean fromd = op == 143 || op == 138 || op == 142 || op == 136 || op == 144 || op == 137;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            switch (op) {
                case 133:
                case 140:
                case 143:
                    totype = LongType.v();
                    break;
                case 134:
                case 137:
                case 144:
                    totype = FloatType.v();
                    break;
                case 135:
                case 138:
                case 141:
                    totype = DoubleType.v();
                    break;
                case 136:
                case 139:
                case 142:
                    totype = IntType.v();
                    break;
                case 145:
                    totype = ByteType.v();
                    break;
                case 146:
                    totype = CharType.v();
                    break;
                case 147:
                    totype = ShortType.v();
                    break;
                default:
                    throw new AssertionError("Unknonw prim cast op: " + op);
            }
            Operand val = fromd ? popImmediateDual() : popImmediate();
            CastExpr cast = Jimple.v().newCastExpr(val.stackOrValue(), totype);
            opr = new Operand(insn, cast);
            val.addBox(cast.getOpBox());
            frame.in(val);
            frame.boxes(cast.getOpBox());
            frame.out(opr);
        } else {
            opr = out[0];
            Operand[] operandArr = new Operand[1];
            operandArr[0] = fromd ? popDual() : pop();
            frame.mergeIn(operandArr);
        }
        if (tod) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertReturnInsn(InsnNode insn) {
        int op = insn.getOpcode();
        boolean dword = op == 173 || op == 175;
        StackFrame frame = getFrame(insn);
        if (!this.units.containsKey(insn)) {
            Operand val = dword ? popImmediateDual() : popImmediate();
            ReturnStmt ret = Jimple.v().newReturnStmt(val.stackOrValue());
            val.addBox(ret.getOpBox());
            frame.in(val);
            frame.boxes(ret.getOpBox());
            setUnit(insn, ret);
            return;
        }
        Operand[] operandArr = new Operand[1];
        operandArr[0] = dword ? popDual() : pop();
        frame.mergeIn(operandArr);
    }

    private void convertInsn(InsnNode insn) {
        Operand opr;
        int op = insn.getOpcode();
        if (op == 0) {
            if (!this.units.containsKey(insn)) {
                this.units.put(insn, Jimple.v().newNopStmt());
            }
        } else if (op >= 1 && op <= 15) {
            convertConstInsn(insn);
        } else if (op >= 46 && op <= 53) {
            convertArrayLoadInsn(insn);
        } else if (op >= 79 && op <= 86) {
            convertArrayStoreInsn(insn);
        } else if (op == 87) {
            popImmediate();
        } else if (op == 88) {
            popImmediate();
            if (peek() == DWORD_DUMMY) {
                pop();
            } else {
                popImmediate();
            }
        } else if (op >= 89 && op <= 94) {
            convertDupInsn(insn);
        } else if (op == 95) {
            Operand o1 = popImmediate();
            Operand o2 = popImmediate();
            push(o1);
            push(o2);
        } else if ((op >= 96 && op <= 115) || ((op >= 120 && op <= 131) || (op >= 148 && op <= 152))) {
            convertBinopInsn(insn);
        } else if ((op >= 116 && op <= 119) || op == 190) {
            convertUnopInsn(insn);
        } else if (op >= 133 && op <= 147) {
            convertPrimCastInsn(insn);
        } else if (op >= 172 && op <= 176) {
            convertReturnInsn(insn);
        } else if (op == 177) {
            if (!this.units.containsKey(insn)) {
                setUnit(insn, Jimple.v().newReturnVoidStmt());
            }
        } else if (op == 191) {
            StackFrame frame = getFrame(insn);
            if (!this.units.containsKey(insn)) {
                opr = popImmediate();
                ThrowStmt ts = Jimple.v().newThrowStmt(opr.stackOrValue());
                opr.addBox(ts.getOpBox());
                frame.in(opr);
                frame.out(opr);
                frame.boxes(ts.getOpBox());
                setUnit(insn, ts);
            } else {
                opr = pop();
                frame.mergeIn(opr);
            }
            push(opr);
        } else if (op == 194 || op == 195) {
            StackFrame frame2 = getFrame(insn);
            if (!this.units.containsKey(insn)) {
                Operand opr2 = popStackConst();
                MonitorStmt ts2 = op == 194 ? Jimple.v().newEnterMonitorStmt(opr2.stackOrValue()) : Jimple.v().newExitMonitorStmt(opr2.stackOrValue());
                opr2.addBox(ts2.getOpBox());
                frame2.in(opr2);
                frame2.boxes(ts2.getOpBox());
                setUnit(insn, ts2);
                return;
            }
            frame2.mergeIn(pop());
        } else {
            throw new AssertionError("Unknown insn op: " + op);
        }
    }

    private void convertIntInsn(IntInsnNode insn) {
        Operand opr;
        Value v;
        Type type;
        int op = insn.getOpcode();
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            if (op == 16 || op == 17) {
                v = IntConstant.v(insn.operand);
            } else {
                switch (insn.operand) {
                    case 4:
                        type = BooleanType.v();
                        break;
                    case 5:
                        type = CharType.v();
                        break;
                    case 6:
                        type = FloatType.v();
                        break;
                    case 7:
                        type = DoubleType.v();
                        break;
                    case 8:
                        type = ByteType.v();
                        break;
                    case 9:
                        type = ShortType.v();
                        break;
                    case 10:
                        type = IntType.v();
                        break;
                    case 11:
                        type = LongType.v();
                        break;
                    default:
                        throw new AssertionError("Unknown NEWARRAY type!");
                }
                Operand size = popImmediate();
                NewArrayExpr anew = Jimple.v().newNewArrayExpr(type, size.stackOrValue());
                size.addBox(anew.getSizeBox());
                frame.in(size);
                frame.boxes(anew.getSizeBox());
                v = anew;
            }
            opr = new Operand(insn, v);
            frame.out(opr);
        } else {
            opr = out[0];
            if (op == 188) {
                frame.mergeIn(pop());
            }
        }
        push(opr);
    }

    private void convertJumpInsn(JumpInsnNode insn) {
        ConditionExpr cond;
        int op = insn.getOpcode();
        if (op == 167) {
            if (!this.units.containsKey(insn)) {
                UnitBox box = Jimple.v().newStmtBox(null);
                this.labels.put(insn.label, box);
                setUnit(insn, Jimple.v().newGotoStmt(box));
                return;
            }
            return;
        }
        StackFrame frame = getFrame(insn);
        if (!this.units.containsKey(insn)) {
            Operand val = popImmediate();
            Value v = val.stackOrValue();
            if (op >= 159 && op <= 166) {
                Operand val1 = popImmediate();
                Value v1 = val1.stackOrValue();
                switch (op) {
                    case 159:
                        cond = Jimple.v().newEqExpr(v1, v);
                        break;
                    case 160:
                        cond = Jimple.v().newNeExpr(v1, v);
                        break;
                    case 161:
                        cond = Jimple.v().newLtExpr(v1, v);
                        break;
                    case 162:
                        cond = Jimple.v().newGeExpr(v1, v);
                        break;
                    case 163:
                        cond = Jimple.v().newGtExpr(v1, v);
                        break;
                    case 164:
                        cond = Jimple.v().newLeExpr(v1, v);
                        break;
                    case 165:
                        cond = Jimple.v().newEqExpr(v1, v);
                        break;
                    case 166:
                        cond = Jimple.v().newNeExpr(v1, v);
                        break;
                    default:
                        throw new AssertionError("Unknown if op: " + op);
                }
                val1.addBox(cond.getOp1Box());
                val.addBox(cond.getOp2Box());
                frame.boxes(cond.getOp2Box(), cond.getOp1Box());
                frame.in(val, val1);
            } else {
                switch (op) {
                    case 153:
                        cond = Jimple.v().newEqExpr(v, IntConstant.v(0));
                        break;
                    case 154:
                        cond = Jimple.v().newNeExpr(v, IntConstant.v(0));
                        break;
                    case 155:
                        cond = Jimple.v().newLtExpr(v, IntConstant.v(0));
                        break;
                    case 156:
                        cond = Jimple.v().newGeExpr(v, IntConstant.v(0));
                        break;
                    case 157:
                        cond = Jimple.v().newGtExpr(v, IntConstant.v(0));
                        break;
                    case 158:
                        cond = Jimple.v().newLeExpr(v, IntConstant.v(0));
                        break;
                    case 198:
                        cond = Jimple.v().newEqExpr(v, NullConstant.v());
                        break;
                    case 199:
                        cond = Jimple.v().newNeExpr(v, NullConstant.v());
                        break;
                    default:
                        throw new AssertionError("Unknown if op: " + op);
                }
                val.addBox(cond.getOp1Box());
                frame.boxes(cond.getOp1Box());
                frame.in(val);
            }
            UnitBox box2 = Jimple.v().newStmtBox(null);
            this.labels.put(insn.label, box2);
            setUnit(insn, Jimple.v().newIfStmt(cond, box2));
        } else if (op >= 159 && op <= 166) {
            frame.mergeIn(pop(), pop());
        } else {
            frame.mergeIn(pop());
        }
    }

    private void convertLdcInsn(LdcInsnNode insn) {
        Operand opr;
        Object val = insn.cst;
        boolean dword = (val instanceof Long) || (val instanceof Double);
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            Value v = toSootValue(val);
            opr = new Operand(insn, v);
            frame.out(opr);
        } else {
            opr = out[0];
        }
        if (dword) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private Value toSootValue(Object val) throws AssertionError {
        Value v;
        if (val instanceof Integer) {
            v = IntConstant.v(((Integer) val).intValue());
        } else if (val instanceof Float) {
            v = FloatConstant.v(((Float) val).floatValue());
        } else if (val instanceof Long) {
            v = LongConstant.v(((Long) val).longValue());
        } else if (val instanceof Double) {
            v = DoubleConstant.v(((Double) val).doubleValue());
        } else if (val instanceof String) {
            v = StringConstant.v(val.toString());
        } else if (val instanceof org.objectweb.asm.Type) {
            org.objectweb.asm.Type t = (org.objectweb.asm.Type) val;
            if (t.getSort() == 11) {
                List<Type> paramTypes = AsmUtil.toJimpleDesc(((org.objectweb.asm.Type) val).getDescriptor(), Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
                Type returnType = paramTypes.remove(paramTypes.size() - 1);
                v = MethodType.v(paramTypes, returnType);
            } else {
                v = ClassConstant.v(((org.objectweb.asm.Type) val).getDescriptor());
            }
        } else if (val instanceof Handle) {
            Handle h = (Handle) val;
            if (MethodHandle.isMethodRef(h.getTag())) {
                v = MethodHandle.v(toSootMethodRef(h), h.getTag());
            } else {
                v = MethodHandle.v(toSootFieldRef(h), h.getTag());
            }
        } else {
            throw new AssertionError("Unknown constant type: " + val.getClass());
        }
        return v;
    }

    private void convertLookupSwitchInsn(LookupSwitchInsnNode insn) {
        StackFrame frame = getFrame(insn);
        if (this.units.containsKey(insn)) {
            frame.mergeIn(pop());
            return;
        }
        Operand key = popImmediate();
        UnitBox dflt = Jimple.v().newStmtBox(null);
        List<UnitBox> targets = new ArrayList<>(insn.labels.size());
        this.labels.put(insn.dflt, dflt);
        for (LabelNode ln : insn.labels) {
            UnitBox box = Jimple.v().newStmtBox(null);
            targets.add(box);
            this.labels.put(ln, box);
        }
        List<IntConstant> keys = new ArrayList<>(insn.keys.size());
        for (Integer i : insn.keys) {
            keys.add(IntConstant.v(i.intValue()));
        }
        LookupSwitchStmt lss = Jimple.v().newLookupSwitchStmt(key.stackOrValue(), keys, targets, dflt);
        key.addBox(lss.getKeyBox());
        frame.in(key);
        frame.boxes(lss.getKeyBox());
        setUnit(insn, lss);
    }

    private void convertMethodInsn(MethodInsnNode insn) {
        Operand opr;
        Operand[] oprs;
        Type returnType;
        Operand[] args;
        List<Value> argList;
        InstanceInvokeExpr iinvoke;
        InvokeExpr invoke;
        int op = insn.getOpcode();
        boolean instance = op != 184;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            String clsName = AsmUtil.toQualifiedName(insn.owner);
            if (clsName.charAt(0) == '[') {
                clsName = JavaBasicTypes.JAVA_LANG_OBJECT;
            }
            List<Type> sigTypes = AsmUtil.toJimpleDesc(insn.desc, Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
            returnType = sigTypes.remove(sigTypes.size() - 1);
            SootMethodRef ref = Scene.v().makeMethodRef(getClassFromScene(clsName), insn.name, sigTypes, returnType, !instance);
            int nrArgs = sigTypes.size();
            List<Value> emptyList = Collections.emptyList();
            if (!instance) {
                args = nrArgs == 0 ? null : new Operand[nrArgs];
                argList = emptyList;
                if (args != null) {
                    argList = new ArrayList<>(nrArgs);
                }
            } else {
                args = new Operand[nrArgs + 1];
                argList = emptyList;
                if (nrArgs != 0) {
                    argList = new ArrayList<>(nrArgs);
                }
            }
            while (true) {
                int i = nrArgs;
                nrArgs--;
                if (i == 0) {
                    break;
                }
                args[nrArgs] = popImmediate(sigTypes.get(nrArgs));
                argList.add(args[nrArgs].stackOrValue());
            }
            if (argList.size() > 1) {
                Collections.reverse(argList);
            }
            if (instance) {
                args[args.length - 1] = popLocal();
            }
            ValueBox[] boxes = args == null ? null : new ValueBox[args.length];
            if (!instance) {
                invoke = Jimple.v().newStaticInvokeExpr(ref, argList);
            } else {
                Local base = (Local) args[args.length - 1].stackOrValue();
                switch (op) {
                    case 182:
                        iinvoke = Jimple.v().newVirtualInvokeExpr(base, ref, argList);
                        break;
                    case 183:
                        iinvoke = Jimple.v().newSpecialInvokeExpr(base, ref, argList);
                        break;
                    case 184:
                    default:
                        throw new AssertionError("Unknown invoke op:" + op);
                    case 185:
                        iinvoke = Jimple.v().newInterfaceInvokeExpr(base, ref, argList);
                        break;
                }
                boxes[boxes.length - 1] = iinvoke.getBaseBox();
                args[args.length - 1].addBox(boxes[boxes.length - 1]);
                invoke = iinvoke;
            }
            if (boxes != null) {
                for (int i2 = 0; i2 != sigTypes.size(); i2++) {
                    boxes[i2] = invoke.getArgBox(i2);
                    args[i2].addBox(boxes[i2]);
                }
                frame.boxes(boxes);
                frame.in(args);
            }
            opr = new Operand(insn, invoke);
            frame.out(opr);
        } else {
            opr = out[0];
            InvokeExpr expr = (InvokeExpr) opr.value;
            List<Type> types = expr.getMethodRef().getParameterTypes();
            int nrArgs2 = types.size();
            if (expr.getMethodRef().isStatic()) {
                oprs = nrArgs2 == 0 ? null : new Operand[nrArgs2];
            } else {
                oprs = new Operand[nrArgs2 + 1];
            }
            if (oprs != null) {
                while (true) {
                    int i3 = nrArgs2;
                    nrArgs2--;
                    if (i3 == 0) {
                        break;
                    }
                    oprs[nrArgs2] = pop(types.get(nrArgs2));
                }
                if (!expr.getMethodRef().isStatic()) {
                    oprs[oprs.length - 1] = pop();
                }
                frame.mergeIn(oprs);
                types.size();
            }
            returnType = expr.getMethodRef().getReturnType();
        }
        if (AsmUtil.isDWord(returnType)) {
            pushDual(opr);
        } else if (!(returnType instanceof VoidType)) {
            push(opr);
        } else if (!this.units.containsKey(insn)) {
            setUnit(insn, Jimple.v().newInvokeStmt(opr.value));
        }
        assignReadOps(null);
    }

    private void convertInvokeDynamicInsn(InvokeDynamicInsnNode insn) {
        Operand opr;
        Operand[] oprs;
        Type returnType;
        Object[] objArr;
        InvokeExpr indy;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            SootMethodRef bsmMethodRef = toSootMethodRef(insn.bsm);
            List<Value> bsmMethodArgs = new ArrayList<>(insn.bsmArgs.length);
            for (Object bsmArg : insn.bsmArgs) {
                bsmMethodArgs.add(toSootValue(bsmArg));
            }
            Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(insn.desc);
            int nrArgs = types.length - 1;
            List<Type> parameterTypes = new ArrayList<>(nrArgs);
            List<Value> methodArgs = new ArrayList<>(nrArgs);
            Operand[] args = new Operand[nrArgs];
            ValueBox[] boxes = new ValueBox[nrArgs];
            while (true) {
                int i = nrArgs;
                nrArgs--;
                if (i == 0) {
                    break;
                }
                parameterTypes.add(types[nrArgs]);
                args[nrArgs] = popImmediate(types[nrArgs]);
                methodArgs.add(args[nrArgs].stackOrValue());
            }
            if (methodArgs.size() > 1) {
                Collections.reverse(methodArgs);
                Collections.reverse(parameterTypes);
            }
            returnType = types[types.length - 1];
            SootMethodRef bootstrap_model = null;
            if (PhaseOptions.getBoolean(PhaseOptions.v().getPhaseOptions("jb"), "model-lambdametafactory")) {
                String bsmMethodRefStr = bsmMethodRef.toString();
                if (bsmMethodRefStr.equals(METAFACTORY_SIGNATURE) || bsmMethodRefStr.equals(ALT_METAFACTORY_SIGNATURE)) {
                    SootClass enclosingClass = this.body.getMethod().getDeclaringClass();
                    bootstrap_model = LambdaMetaFactory.v().makeLambdaHelper(bsmMethodArgs, insn.bsm.getTag(), insn.name, types, enclosingClass);
                }
            }
            if (bootstrap_model != null) {
                indy = Jimple.v().newStaticInvokeExpr(bootstrap_model, methodArgs);
            } else {
                SootClass bclass = Scene.v().getSootClass(SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME);
                SootMethodRef methodRef = Scene.v().makeMethodRef(bclass, insn.name, parameterTypes, returnType, true);
                indy = Jimple.v().newDynamicInvokeExpr(bsmMethodRef, bsmMethodArgs, methodRef, insn.bsm.getTag(), methodArgs);
            }
            if (boxes != null) {
                for (int i2 = 0; i2 < types.length - 1; i2++) {
                    boxes[i2] = indy.getArgBox(i2);
                    args[i2].addBox(boxes[i2]);
                }
                frame.boxes(boxes);
                frame.in(args);
            }
            opr = new Operand(insn, indy);
            frame.out(opr);
        } else {
            opr = out[0];
            InvokeExpr expr = (InvokeExpr) opr.value;
            List<Type> types2 = expr.getMethodRef().getParameterTypes();
            int nrArgs2 = types2.size();
            if (expr.getMethodRef().isStatic()) {
                oprs = nrArgs2 == 0 ? null : new Operand[nrArgs2];
            } else {
                oprs = new Operand[nrArgs2 + 1];
            }
            if (oprs != null) {
                while (true) {
                    int i3 = nrArgs2;
                    nrArgs2--;
                    if (i3 == 0) {
                        break;
                    }
                    oprs[nrArgs2] = pop(types2.get(nrArgs2));
                }
                if (!expr.getMethodRef().isStatic()) {
                    oprs[oprs.length - 1] = pop();
                }
                frame.mergeIn(oprs);
                types2.size();
            }
            returnType = expr.getMethodRef().getReturnType();
        }
        if (AsmUtil.isDWord(returnType)) {
            pushDual(opr);
        } else if (!(returnType instanceof VoidType)) {
            push(opr);
        } else if (!this.units.containsKey(insn)) {
            setUnit(insn, Jimple.v().newInvokeStmt(opr.value));
        }
        assignReadOps(null);
    }

    private SootMethodRef toSootMethodRef(Handle methodHandle) {
        String bsmClsName = AsmUtil.toQualifiedName(methodHandle.getOwner());
        SootClass bsmCls = getClassFromScene(bsmClsName);
        List<Type> bsmSigTypes = AsmUtil.toJimpleDesc(methodHandle.getDesc(), Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
        Type returnType = bsmSigTypes.remove(bsmSigTypes.size() - 1);
        return Scene.v().makeMethodRef(bsmCls, methodHandle.getName(), bsmSigTypes, returnType, methodHandle.getTag() == MethodHandle.Kind.REF_INVOKE_STATIC.getValue());
    }

    private SootFieldRef toSootFieldRef(Handle methodHandle) {
        String bsmClsName = AsmUtil.toQualifiedName(methodHandle.getOwner());
        SootClass bsmCls = Scene.v().getSootClass(bsmClsName);
        Type t = AsmUtil.toJimpleDesc(methodHandle.getDesc(), Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName)).get(0);
        int kind = methodHandle.getTag();
        return Scene.v().makeFieldRef(bsmCls, methodHandle.getName(), t, kind == MethodHandle.Kind.REF_GET_FIELD_STATIC.getValue() || kind == MethodHandle.Kind.REF_PUT_FIELD_STATIC.getValue());
    }

    private void convertMultiANewArrayInsn(MultiANewArrayInsnNode insn) {
        Operand opr;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            ArrayType t = (ArrayType) AsmUtil.toJimpleType(insn.desc, Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName));
            int dims = insn.dims;
            Operand[] sizes = new Operand[dims];
            Value[] sizeVals = new Value[dims];
            ValueBox[] boxes = new ValueBox[dims];
            while (true) {
                int i = dims;
                dims--;
                if (i == 0) {
                    break;
                }
                sizes[dims] = popImmediate();
                sizeVals[dims] = sizes[dims].stackOrValue();
            }
            NewMultiArrayExpr nm = Jimple.v().newNewMultiArrayExpr(t, Arrays.asList(sizeVals));
            for (int i2 = 0; i2 != boxes.length; i2++) {
                ValueBox vb = nm.getSizeBox(i2);
                sizes[i2].addBox(vb);
                boxes[i2] = vb;
            }
            frame.boxes(boxes);
            frame.in(sizes);
            opr = new Operand(insn, nm);
            frame.out(opr);
        } else {
            opr = out[0];
            int dims2 = insn.dims;
            Operand[] sizes2 = new Operand[dims2];
            while (true) {
                int i3 = dims2;
                dims2--;
                if (i3 == 0) {
                    break;
                }
                sizes2[dims2] = pop();
            }
            frame.mergeIn(sizes2);
        }
        push(opr);
    }

    private void convertTableSwitchInsn(TableSwitchInsnNode insn) {
        StackFrame frame = getFrame(insn);
        if (this.units.containsKey(insn)) {
            frame.mergeIn(pop());
            return;
        }
        Operand key = popImmediate();
        UnitBox dflt = Jimple.v().newStmtBox(null);
        List<UnitBox> targets = new ArrayList<>(insn.labels.size());
        this.labels.put(insn.dflt, dflt);
        for (LabelNode ln : insn.labels) {
            UnitBox box = Jimple.v().newStmtBox(null);
            targets.add(box);
            this.labels.put(ln, box);
        }
        TableSwitchStmt tss = Jimple.v().newTableSwitchStmt(key.stackOrValue(), insn.min, insn.max, targets, dflt);
        key.addBox(tss.getKeyBox());
        frame.in(key);
        frame.boxes(tss.getKeyBox());
        setUnit(insn, tss);
    }

    private void convertTypeInsn(TypeInsnNode insn) {
        Operand opr;
        ValueBox vb;
        Value val;
        int op = insn.getOpcode();
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            Optional<String> module = Optional.fromNullable(this.body.getMethod().getDeclaringClass().moduleName);
            Type t = AsmUtil.toJimpleRefType(insn.desc, module);
            if (op == 187) {
                val = Jimple.v().newNewExpr((RefType) t);
            } else {
                Operand op1 = popImmediate();
                Value v1 = op1.stackOrValue();
                switch (op) {
                    case 189:
                        NewArrayExpr expr = Jimple.v().newNewArrayExpr(t, v1);
                        vb = expr.getSizeBox();
                        val = expr;
                        break;
                    case 190:
                    case 191:
                    default:
                        throw new AssertionError("Unknown type op: " + op);
                    case 192:
                        CastExpr expr2 = Jimple.v().newCastExpr(v1, t);
                        vb = expr2.getOpBox();
                        val = expr2;
                        break;
                    case 193:
                        InstanceOfExpr expr3 = Jimple.v().newInstanceOfExpr(v1, t);
                        vb = expr3.getOpBox();
                        val = expr3;
                        break;
                }
                op1.addBox(vb);
                frame.in(op1);
                frame.boxes(vb);
            }
            opr = new Operand(insn, val);
            frame.out(opr);
        } else {
            opr = out[0];
            if (op != 187) {
                frame.mergeIn(pop());
            }
        }
        push(opr);
    }

    private void convertVarLoadInsn(VarInsnNode insn) {
        Operand opr;
        int op = insn.getOpcode();
        boolean dword = op == 22 || op == 24;
        StackFrame frame = getFrame(insn);
        Operand[] out = frame.out();
        if (out == null) {
            opr = new Operand(insn, getLocal(insn.var));
            frame.out(opr);
        } else {
            opr = out[0];
        }
        if (dword) {
            pushDual(opr);
        } else {
            push(opr);
        }
    }

    private void convertVarStoreInsn(VarInsnNode insn) {
        int op = insn.getOpcode();
        boolean dword = op == 55 || op == 57;
        StackFrame frame = getFrame(insn);
        Operand opr = dword ? popDual() : pop();
        Local local = getLocal(insn.var);
        if (!this.units.containsKey(insn)) {
            DefinitionStmt as = Jimple.v().newAssignStmt(local, opr.stackOrValue());
            opr.addBox(as.getRightOpBox());
            frame.boxes(as.getRightOpBox());
            frame.in(opr);
            setUnit(insn, as);
        } else {
            frame.mergeIn(opr);
        }
        assignReadOps(local);
    }

    private void convertVarInsn(VarInsnNode insn) {
        int op = insn.getOpcode();
        if (op >= 21 && op <= 25) {
            convertVarLoadInsn(insn);
        } else if (op >= 54 && op <= 58) {
            convertVarStoreInsn(insn);
        } else if (op == 169) {
            if (!this.units.containsKey(insn)) {
                setUnit(insn, Jimple.v().newRetStmt(getLocal(insn.var)));
            }
        } else {
            throw new AssertionError("Unknown var op: " + op);
        }
    }

    private void convertLabel(LabelNode ln) {
        Operand opr;
        if (!this.trapHandlers.containsKey(ln)) {
            return;
        }
        if (this.inlineExceptionLabels.contains(ln)) {
            if (!this.units.containsKey(ln)) {
                NopStmt nop = Jimple.v().newNopStmt();
                setUnit(ln, nop);
                return;
            }
            return;
        }
        StackFrame frame = getFrame(ln);
        Operand[] out = frame.out();
        if (out == null) {
            CaughtExceptionRef ref = Jimple.v().newCaughtExceptionRef();
            Local stack = newStackLocal();
            DefinitionStmt as = Jimple.v().newIdentityStmt(stack, ref);
            opr = new Operand(ln, ref);
            opr.stack = stack;
            frame.out(opr);
            setUnit(ln, as);
        } else {
            opr = out[0];
        }
        push(opr);
    }

    private void convertLine(LineNumberNode ln) {
        this.lastLineNumber = ln.line;
    }

    private void addEdges(AbstractInsnNode cur, AbstractInsnNode tgt1, List<LabelNode> tgts) {
        AbstractInsnNode abstractInsnNode;
        int lastIdx = tgts == null ? -1 : tgts.size() - 1;
        Operand[] stackss = (Operand[]) this.stack.toArray(new Operand[this.stack.size()]);
        List<Operand> stackssL = Arrays.asList(stackss);
        AbstractInsnNode tgt = tgt1;
        int i = 0;
        do {
            Edge edge = this.edges.get(cur, tgt);
            if (edge == null) {
                Edge edge2 = new Edge(this, tgt, this.lastLineNumber);
                edge2.prevStacks.add(stackssL);
                this.edges.put(cur, tgt, edge2);
                this.conversionWorklist.add(edge2);
            } else if (edge.stack != null) {
                ArrayList<Operand> stackTemp = edge.stack;
                if (stackTemp.size() != stackss.length) {
                    throw new AssertionError("Multiple un-equal stacks!");
                }
                for (int j = 0; j != stackss.length; j++) {
                    Operand tempOp = stackTemp.get(j);
                    Operand stackOp = stackss[j];
                    if (!tempOp.equivTo(stackOp)) {
                        merge(tempOp, stackOp);
                    }
                }
            } else if (edge.prevStacks.add(stackssL)) {
                edge.stack = new ArrayList<>(this.stack);
                this.conversionWorklist.add(edge);
            }
            if (i > lastIdx) {
                return;
            }
            int i2 = i;
            i++;
            abstractInsnNode = tgts.get(i2);
            tgt = abstractInsnNode;
        } while (abstractInsnNode != null);
    }

    private void merge(Operand firstOp, Operand secondOp) {
        if (secondOp.stack != null) {
            if (firstOp.stack == null) {
                Local stack = secondOp.stack;
                firstOp.stack = stack;
                AssignStmt as = Jimple.v().newAssignStmt(stack, firstOp.stackOrValue());
                setUnit(firstOp.insn, as);
                return;
            }
            Local stack2 = firstOp.stack;
            AssignStmt as2 = Jimple.v().newAssignStmt(stack2, secondOp.stackOrValue());
            mergeUnits(secondOp.insn, as2);
            secondOp.addBox(as2.getRightOpBox());
            secondOp.stack = stack2;
        } else if (firstOp.stack != null) {
            Local stack3 = firstOp.stack;
            secondOp.stack = stack3;
            setUnit(secondOp.insn, Jimple.v().newAssignStmt(stack3, secondOp.stackOrValue()));
        } else {
            throw new RuntimeException("Cannot merge operands, since neither has a stack local. Bummer.");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0291 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void convert() {
        /*
            Method dump skipped, instructions count: 675
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.asm.AsmMethodSource.convert():void");
    }

    private void handleInlineExceptionHandler(LabelNode ln, ArrayDeque<Edge> worklist) {
        CaughtExceptionRef ref = Jimple.v().newCaughtExceptionRef();
        Local local = newStackLocal();
        DefinitionStmt as = Jimple.v().newIdentityStmt(local, ref);
        Operand opr = new Operand(ln, ref);
        opr.stack = local;
        ArrayList<Operand> stack = new ArrayList<>();
        stack.add(opr);
        worklist.add(new Edge(ln, stack));
        this.inlineExceptionHandlers.put(ln, as);
    }

    private boolean checkInlineExceptionHandler(LabelNode ln) {
        Iterator<AbstractInsnNode> iterator2 = this.instructions.iterator2();
        while (iterator2.hasNext()) {
            AbstractInsnNode node = iterator2.next();
            if (node instanceof JumpInsnNode) {
                if (((JumpInsnNode) node).label == ln) {
                    this.inlineExceptionLabels.add(ln);
                    return true;
                }
            } else if (node instanceof LookupSwitchInsnNode) {
                if (((LookupSwitchInsnNode) node).labels.contains(ln)) {
                    this.inlineExceptionLabels.add(ln);
                    return true;
                }
            } else if ((node instanceof TableSwitchInsnNode) && ((TableSwitchInsnNode) node).labels.contains(ln)) {
                this.inlineExceptionLabels.add(ln);
                return true;
            }
        }
        return false;
    }

    private void emitLocals() {
        JimpleBody jb = this.body;
        SootMethod m = jb.getMethod();
        Collection<Local> jbl = jb.getLocals();
        Collection<Unit> jbu = jb.getUnits();
        int iloc = 0;
        if (!m.isStatic()) {
            iloc = 0 + 1;
            Local l = getLocal(0);
            jbu.add(Jimple.v().newIdentityStmt(l, Jimple.v().newThisRef(m.getDeclaringClass().getType())));
        }
        int nrp = 0;
        for (Object ot : m.getParameterTypes()) {
            Type t = (Type) ot;
            Local l2 = getLocal(iloc);
            int i = nrp;
            nrp++;
            jbu.add(Jimple.v().newIdentityStmt(l2, Jimple.v().newParameterRef(t, i)));
            if (AsmUtil.isDWord(t)) {
                iloc += 2;
            } else {
                iloc++;
            }
        }
        for (Local l3 : this.locals.values()) {
            jbl.add(l3);
        }
    }

    private void emitTraps() {
        Chain<Trap> traps = this.body.getTraps();
        SootClass throwable = Scene.v().getSootClass("java.lang.Throwable");
        Map<LabelNode, Iterator<UnitBox>> handlers = new LinkedHashMap<>(this.tryCatchBlocks.size());
        for (TryCatchBlockNode tc : this.tryCatchBlocks) {
            UnitBox start = Jimple.v().newStmtBox(null);
            UnitBox end = Jimple.v().newStmtBox(null);
            Iterator<UnitBox> hitr = handlers.get(tc.handler);
            if (hitr == null) {
                hitr = this.trapHandlers.get(tc.handler).iterator();
                handlers.put(tc.handler, hitr);
            }
            UnitBox handler = hitr.next();
            SootClass cls = tc.type == null ? throwable : getClassFromScene(AsmUtil.toQualifiedName(tc.type));
            Trap trap = Jimple.v().newTrap(cls, start, end, handler);
            traps.add(trap);
            this.labels.put(tc.start, start);
            this.labels.put(tc.end, end);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/asm/AsmMethodSource$UnitContainerWorklistElement.class */
    public static class UnitContainerWorklistElement {
        UnitContainer u;
        int position;

        public UnitContainerWorklistElement(UnitContainer u) {
            this.u = u;
        }
    }

    static void emitUnits(Unit u, UnitPatchingChain chain) {
        if (u instanceof UnitContainer) {
            Stack<UnitContainerWorklistElement> stack = new Stack<>();
            stack.push(new UnitContainerWorklistElement((UnitContainer) u));
            while (!stack.isEmpty()) {
                UnitContainerWorklistElement r = stack.peek();
                int i = r.position;
                while (true) {
                    if (i < r.u.units.length) {
                        r.position = i + 1;
                        Unit e = r.u.units[i];
                        if (e instanceof UnitContainer) {
                            stack.push(new UnitContainerWorklistElement((UnitContainer) e));
                            break;
                        } else {
                            chain.add((UnitPatchingChain) e);
                            i++;
                        }
                    } else if (stack.pop() != r) {
                        throw new AssertionError("Not expected element");
                    }
                }
            }
            return;
        }
        chain.add((UnitPatchingChain) u);
    }

    private void emitUnits() {
        AbstractInsnNode insn = this.instructions.getFirst();
        ArrayDeque<LabelNode> labls = new ArrayDeque<>();
        while (insn != null) {
            if (insn instanceof LabelNode) {
                labls.add((LabelNode) insn);
            }
            Unit u = this.units.get(insn);
            if (u == null) {
                insn = insn.getNext();
            } else {
                emitUnits(u, this.body.getUnits());
                IdentityStmt caughtEx = null;
                if (u instanceof IdentityStmt) {
                    caughtEx = (IdentityStmt) u;
                } else if (u instanceof UnitContainer) {
                    caughtEx = getIdentityRefFromContrainer((UnitContainer) u);
                }
                if ((insn instanceof LabelNode) && caughtEx != null && (caughtEx.getRightOp() instanceof CaughtExceptionRef)) {
                    Collection<UnitBox> traps = this.trapHandlers.get((LabelNode) insn);
                    for (UnitBox ub : traps) {
                        ub.setUnit(caughtEx);
                    }
                }
                while (!labls.isEmpty()) {
                    Collection<UnitBox> boxes = this.labels.get(labls.poll());
                    if (boxes != null) {
                        for (UnitBox box : boxes) {
                            box.setUnit(u instanceof UnitContainer ? ((UnitContainer) u).getFirstUnit() : u);
                        }
                    }
                }
                insn = insn.getNext();
            }
        }
        for (LabelNode ln : this.inlineExceptionHandlers.keySet()) {
            Unit handler = this.inlineExceptionHandlers.get(ln);
            emitUnits(handler, this.body.getUnits());
            Collection<UnitBox> traps2 = this.trapHandlers.get(ln);
            for (UnitBox ub2 : traps2) {
                ub2.setUnit(handler);
            }
            Unit targetUnit = this.units.get(ln);
            GotoStmt gotoImpl = Jimple.v().newGotoStmt(targetUnit);
            this.body.getUnits().add((UnitPatchingChain) gotoImpl);
        }
        if (labls.isEmpty()) {
            return;
        }
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        while (!labls.isEmpty()) {
            Collection<UnitBox> boxes2 = this.labels.get(labls.poll());
            if (boxes2 != null) {
                for (UnitBox box2 : boxes2) {
                    box2.setUnit(newNopStmt);
                }
            }
        }
    }

    private IdentityStmt getIdentityRefFromContrainer(UnitContainer u) {
        Unit[] unitArr;
        for (Unit uu : u.units) {
            if (uu instanceof IdentityStmt) {
                return (IdentityStmt) uu;
            }
            if (uu instanceof UnitContainer) {
                return getIdentityRefFromContrainer((UnitContainer) uu);
            }
        }
        return null;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod m, String phaseName) {
        if (!m.isConcrete() || this.instructions == null || this.instructions.size() == 0) {
            return null;
        }
        Jimple jimp = Jimple.v();
        JimpleBody jb = jimp.newBody(m);
        int nrInsn = this.instructions.size();
        this.nextLocal = this.maxLocals;
        this.locals = new LinkedHashMap(this.maxLocals + (this.maxLocals / 2));
        this.labels = LinkedListMultimap.create(4);
        this.units = new LinkedHashMap(nrInsn);
        this.frames = new LinkedHashMap(nrInsn);
        this.trapHandlers = LinkedListMultimap.create(this.tryCatchBlocks.size());
        this.body = jb;
        for (TryCatchBlockNode tc : this.tryCatchBlocks) {
            this.trapHandlers.put(tc.handler, jimp.newStmtBox(null));
        }
        try {
            convert();
            emitLocals();
            emitTraps();
            emitUnits();
            if (PhaseOptions.getBoolean(PhaseOptions.v().getPhaseOptions("jb"), "use-original-names")) {
                tryCorrectingLocalNames(jimp, jb);
            }
            this.locals = null;
            this.labels = null;
            this.units = null;
            this.stack = null;
            this.frames = null;
            this.body = null;
            this.castAndReturnInliner.transform(jb);
            try {
                PackManager.v().getPack("jb").apply(jb);
                return jb;
            } catch (Throwable t) {
                throw new RuntimeException("Failed to apply jb to " + m, t);
            }
        } catch (Throwable t2) {
            throw new RuntimeException("Failed to convert " + m, t2);
        }
    }

    protected void tryCorrectingLocalNames(Jimple jimp, JimpleBody jb) {
        Unit uStart;
        Unit uEnd;
        Chain<Local> jbLocals = jb.getLocals();
        int sizeLVT = this.localVars.size();
        if (sizeLVT > 0) {
            Multimap<Integer, LocalVariableNode> groups = LinkedListMultimap.create(sizeLVT);
            for (LocalVariableNode lvn : this.localVars) {
                if (lvn.start != lvn.end) {
                    groups.put(Integer.valueOf(lvn.index), lvn);
                }
            }
            Chain<Unit> jbUnits = jb.getUnits();
            Table<Integer, String, Local> newLocals = null;
            for (Map.Entry<Integer, Collection<LocalVariableNode>> e : groups.asMap().entrySet()) {
                Collection<LocalVariableNode> lvns = e.getValue();
                if (lvns.size() > 1) {
                    Integer localNum = e.getKey();
                    Local chosen = this.locals.get(localNum);
                    String chosenName = chosen.getName();
                    Type chosenType = chosen.getType();
                    IdentityHashMap<ValueBox, Local> boxToNewLoc = new IdentityHashMap<>();
                    for (Unit u : jbUnits) {
                        for (ValueBox box : u.getUseAndDefBoxes()) {
                            Value val = box.getValue();
                            if (val == chosen) {
                                Local old = boxToNewLoc.put(box, null);
                                if (!$assertionsDisabled && old != null) {
                                    throw new AssertionError();
                                }
                            }
                        }
                    }
                    boolean isConsistent = true;
                    Iterator<LocalVariableNode> it = lvns.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        LocalVariableNode lvn2 = it.next();
                        String name = lvn2.name;
                        if (!chosenName.equals(name)) {
                            AbstractInsnNode previous = lvn2.start.getPrevious();
                            while (true) {
                                AbstractInsnNode i = previous;
                                Unit unit = this.units.get(i);
                                uStart = unit;
                                if (unit != null || i == null) {
                                    break;
                                }
                                previous = i.getNext();
                            }
                            if (uStart instanceof UnitContainer) {
                                uStart = ((UnitContainer) uStart).getFirstUnit();
                            }
                            AbstractInsnNode previous2 = lvn2.end.getPrevious();
                            while (true) {
                                AbstractInsnNode i2 = previous2;
                                Unit unit2 = this.units.get(i2);
                                uEnd = unit2;
                                if (unit2 != null || i2 == null) {
                                    break;
                                }
                                previous2 = i2.getPrevious();
                            }
                            if (uEnd instanceof UnitContainer) {
                                uEnd = ((UnitContainer) uEnd).getFirstUnit();
                            }
                            if (newLocals == null) {
                                newLocals = HashBasedTable.create(this.maxLocals, 1);
                            }
                            Local newLocal = newLocals.get(localNum, name);
                            if (newLocal == null) {
                                newLocal = jimp.newLocal(name, chosenType);
                                Local old2 = newLocals.put(localNum, name, newLocal);
                                if (!$assertionsDisabled && old2 != null) {
                                    throw new AssertionError();
                                }
                            }
                            Iterator<Unit> it2 = jbUnits.iterator(uStart, uEnd);
                            while (it2.hasNext()) {
                                Unit u2 = it2.next();
                                for (ValueBox box2 : u2.getUseAndDefBoxes()) {
                                    Value val2 = box2.getValue();
                                    if (val2 == chosen) {
                                        if (!$assertionsDisabled && !boxToNewLoc.containsKey(box2)) {
                                            throw new AssertionError();
                                        }
                                        Local conflict = boxToNewLoc.put(box2, newLocal);
                                        if (conflict != null) {
                                            isConsistent = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                    }
                    HashSet<Local> newLocalSet = new HashSet<>(boxToNewLoc.values());
                    if (isConsistent && !newLocalSet.contains(null)) {
                        jbLocals.addAll(newLocalSet);
                        for (Map.Entry<ValueBox, Local> r : boxToNewLoc.entrySet()) {
                            r.getKey().setValue(r.getValue());
                        }
                    }
                }
            }
        }
        ensureUniqueNames(jbLocals);
    }

    private void ensureUniqueNames(Chain<Local> jbLocals) {
        Multimap<String, Local> nameToLocal = LinkedListMultimap.create(jbLocals.size());
        for (Local l : jbLocals) {
            nameToLocal.put(l.getName(), l);
        }
        for (Collection<Local> locs : nameToLocal.asMap().values()) {
            if (locs.size() > 1) {
                int num = 0;
                for (Local l2 : locs) {
                    num++;
                    l2.setName(String.valueOf(l2.getName()) + '_' + num);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/asm/AsmMethodSource$Edge.class */
    public final class Edge {
        final AbstractInsnNode insn;
        final Set<List<Operand>> prevStacks;
        private int lastLineNumber;
        ArrayList<Operand> stack;

        Edge(AbstractInsnNode insn, ArrayList<Operand> stack) {
            this.lastLineNumber = -1;
            this.insn = insn;
            this.prevStacks = new HashSet();
            this.stack = stack;
        }

        Edge(AsmMethodSource asmMethodSource, AbstractInsnNode insn, int lastLineNumber) {
            this(insn, new ArrayList(asmMethodSource.stack));
            this.lastLineNumber = lastLineNumber;
        }
    }
}
