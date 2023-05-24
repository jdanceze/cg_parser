package soot.coffi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.VoidType;
import soot.jbco.Main;
import soot.jimple.ArrayRef;
import soot.jimple.ClassConstant;
import soot.jimple.ConditionExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.ArraySet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/coffi/CFG.class */
public class CFG {
    private method_info method;
    BasicBlock cfg;
    Chain<Unit> units;
    JimpleBody listBody;
    Map<Instruction, Stmt> instructionToFirstStmt;
    Map<Instruction, Stmt> instructionToLastStmt;
    SootMethod jmethod;
    Scene cm;
    Instruction firstInstruction;
    Instruction lastInstruction;
    private Hashtable<Instruction, BasicBlock> h2bb;
    private Hashtable<Instruction, BasicBlock> t2bb;
    private BootstrapMethods_attribute bootstrap_methods_attribute;
    private static final Logger logger = LoggerFactory.getLogger(CFG.class);
    public static HashMap<SootMethod, int[]> methodsToVEM = new HashMap<>();
    Map<Instruction, Instruction> jsr2astore = new HashMap();
    Map<Instruction, Instruction> astore2ret = new HashMap();
    LinkedList<Instruction> jsrorder = new LinkedList<>();
    private final Hashtable<Instruction, Instruction_Goto> replacedInsns = new Hashtable<>();
    private Instruction sentinel = new Instruction_Nop();

    public CFG(method_info m) {
        this.method = m;
        this.sentinel.next = m.instructions;
        m.instructions.prev = this.sentinel;
        eliminateJsrRets();
        buildBBCFG();
        m.cfg = this;
        if (this.cfg != null) {
            this.cfg.beginCode = true;
            this.firstInstruction = this.cfg.head;
        } else {
            this.firstInstruction = null;
        }
        if (Main.metrics) {
            complexity();
        }
    }

    private void complexity() {
        exception_table_entry[] exception_table_entryVarArr;
        if (!this.method.jmethod.getDeclaringClass().isApplicationClass()) {
            return;
        }
        HashMap<BasicBlock, Integer> block2exc = new HashMap<>();
        int nodes = 0;
        int edges = 0;
        int highest = 0;
        for (BasicBlock b = this.cfg; b != null; b = b.next) {
            int tmp = 0;
            for (exception_table_entry element : this.method.code_attr.exception_table) {
                Instruction start = element.start_inst;
                Instruction end = element.start_inst;
                if ((start.label >= b.head.label && start.label <= b.tail.label) || (end.label > b.head.label && (b.tail.next == null || end.label <= b.tail.next.label))) {
                    tmp++;
                }
            }
            block2exc.put(b, new Integer(tmp));
        }
        BasicBlock basicBlock = this.cfg;
        while (true) {
            BasicBlock b2 = basicBlock;
            if (b2 != null) {
                nodes++;
                int tmp2 = b2.succ.size() + block2exc.get(b2).intValue();
                int deg = b2.pred.size() + tmp2 + (b2.beginException ? 1 : 0);
                if (deg > highest) {
                    highest = deg;
                }
                edges += tmp2;
                basicBlock = b2.next;
            } else {
                methodsToVEM.put(this.method.jmethod, new int[]{nodes, edges, highest});
                return;
            }
        }
    }

    private void buildBBCFG() {
        Object[] branches;
        Object[] objArr;
        Code_attribute ca = this.method.locate_code_attribute();
        this.h2bb = new Hashtable<>(100, 25.0f);
        this.t2bb = new Hashtable<>(100, 25.0f);
        Instruction insn = this.sentinel.next;
        BasicBlock blast = null;
        if (insn != null) {
            Instruction tail = buildBasicBlock(insn);
            this.cfg = new BasicBlock(insn, tail);
            this.h2bb.put(insn, this.cfg);
            this.t2bb.put(tail, this.cfg);
            insn = tail.next;
            blast = this.cfg;
        }
        while (insn != null) {
            Instruction tail2 = buildBasicBlock(insn);
            BasicBlock block = new BasicBlock(insn, tail2);
            blast.next = block;
            blast = block;
            this.h2bb.put(insn, block);
            this.t2bb.put(tail2, block);
            insn = tail2.next;
        }
        BasicBlock basicBlock = this.cfg;
        while (true) {
            BasicBlock block2 = basicBlock;
            if (block2 == null) {
                break;
            }
            Instruction insn2 = block2.tail;
            if (insn2.branches) {
                if (insn2 instanceof Instruction_Athrow) {
                    HashSet<Instruction> ethandlers = new HashSet<>();
                    for (int i = 0; i < ca.exception_table_length; i++) {
                        exception_table_entry etentry = ca.exception_table[i];
                        if (insn2.label >= etentry.start_inst.label && (etentry.end_inst == null || insn2.label < etentry.end_inst.label)) {
                            ethandlers.add(etentry.handler_inst);
                        }
                    }
                    branches = ethandlers.toArray();
                } else {
                    branches = insn2.branchpoints(insn2.next);
                }
                if (branches != null) {
                    block2.succ.ensureCapacity(block2.succ.size() + branches.length);
                    for (Object element : branches) {
                        if (element != null) {
                            BasicBlock bb = this.h2bb.get(element);
                            if (bb == null) {
                                logger.warn("target of a branch is null");
                                logger.debug(new StringBuilder().append(insn2).toString());
                            } else {
                                block2.succ.addElement(bb);
                                bb.pred.addElement(block2);
                            }
                        }
                    }
                }
            } else if (block2.next != null) {
                block2.succ.addElement(block2.next);
                block2.next.pred.addElement(block2);
            }
            basicBlock = block2.next;
        }
        for (int i2 = 0; i2 < ca.exception_table_length; i2++) {
            BasicBlock bb2 = this.h2bb.get(ca.exception_table[i2].handler_inst);
            if (bb2 == null) {
                logger.warn("No basic block found for start of exception handler code.");
            } else {
                bb2.beginException = true;
                ca.exception_table[i2].b = bb2;
            }
        }
    }

    private static Instruction buildBasicBlock(Instruction head) {
        Instruction insn = head;
        Instruction next = insn.next;
        if (next == null) {
            return insn;
        }
        while (!insn.branches && !next.labelled) {
            insn = next;
            next = insn.next;
            if (next == null) {
                break;
            }
        }
        return insn;
    }

    private boolean eliminateJsrRets() {
        Instruction insn;
        Instruction instruction = this.sentinel;
        while (true) {
            insn = instruction;
            if (insn.next == null) {
                break;
            }
            instruction = insn.next;
        }
        this.lastInstruction = insn;
        HashMap<Instruction, Instruction> todoBlocks = new HashMap<>();
        todoBlocks.put(this.sentinel.next, this.lastInstruction);
        LinkedList<Instruction> todoList = new LinkedList<>();
        todoList.add(this.sentinel.next);
        while (!todoList.isEmpty()) {
            Instruction firstInsn = todoList.removeFirst();
            Instruction lastInsn = todoBlocks.get(firstInsn);
            this.jsrorder.clear();
            this.jsr2astore.clear();
            this.astore2ret.clear();
            if (findOutmostJsrs(firstInsn, lastInsn)) {
                HashMap<Instruction, Instruction> newblocks = inliningJsrTargets();
                todoBlocks.putAll(newblocks);
                todoList.addAll(newblocks.keySet());
            }
        }
        this.method.instructions = this.sentinel.next;
        adjustExceptionTable();
        adjustLineNumberTable();
        adjustBranchTargets();
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0089 A[EDGE_INSN: B:24:0x0089->B:17:0x0089 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean findOutmostJsrs(soot.coffi.Instruction r6, soot.coffi.Instruction r7) {
        /*
            r5 = this;
            java.util.HashSet r0 = new java.util.HashSet
            r1 = r0
            r1.<init>()
            r8 = r0
            r0 = 0
            r9 = r0
            r0 = r6
            r10 = r0
        Le:
            r0 = r10
            boolean r0 = r0 instanceof soot.coffi.Instruction_Jsr
            if (r0 != 0) goto L1e
            r0 = r10
            boolean r0 = r0 instanceof soot.coffi.Instruction_Jsr_w
            if (r0 == 0) goto L79
        L1e:
            r0 = r8
            r1 = r10
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L31
            r0 = r10
            soot.coffi.Instruction r0 = r0.next
            r10 = r0
            goto L80
        L31:
            r0 = r10
            soot.coffi.Instruction_branch r0 = (soot.coffi.Instruction_branch) r0
            soot.coffi.Instruction r0 = r0.target
            r11 = r0
            r0 = r11
            boolean r0 = r0 instanceof soot.coffi.Interface_Astore
            if (r0 != 0) goto L49
            r0 = 1
            r9 = r0
            goto L89
        L49:
            r0 = r5
            r1 = r11
            r2 = r10
            r3 = r8
            soot.coffi.Instruction r0 = r0.findMatchingRet(r1, r2, r3)
            r12 = r0
            r0 = r5
            java.util.LinkedList<soot.coffi.Instruction> r0 = r0.jsrorder
            r1 = r10
            r0.addLast(r1)
            r0 = r5
            java.util.Map<soot.coffi.Instruction, soot.coffi.Instruction> r0 = r0.jsr2astore
            r1 = r10
            r2 = r11
            java.lang.Object r0 = r0.put(r1, r2)
            r0 = r5
            java.util.Map<soot.coffi.Instruction, soot.coffi.Instruction> r0 = r0.astore2ret
            r1 = r11
            r2 = r12
            java.lang.Object r0 = r0.put(r1, r2)
        L79:
            r0 = r10
            soot.coffi.Instruction r0 = r0.next
            r10 = r0
        L80:
            r0 = r10
            r1 = r7
            soot.coffi.Instruction r1 = r1.next
            if (r0 != r1) goto Le
        L89:
            r0 = r9
            if (r0 == 0) goto L9b
            org.slf4j.Logger r0 = soot.coffi.CFG.logger
            java.lang.String r1 = "Sorry, I cannot handle this method."
            r0.debug(r1)
            r0 = 0
            return r0
        L9b:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.coffi.CFG.findOutmostJsrs(soot.coffi.Instruction, soot.coffi.Instruction):boolean");
    }

    private Instruction findMatchingRet(Instruction astore, Instruction jsr, HashSet<Instruction> innerJsrs) {
        int astorenum = ((Interface_Astore) astore).getLocalNumber();
        Instruction instruction = astore.next;
        while (true) {
            Instruction insn = instruction;
            if (insn != null) {
                if ((insn instanceof Instruction_Ret) || (insn instanceof Instruction_Ret_w)) {
                    int retnum = ((Interface_OneIntArg) insn).getIntArg();
                    if (astorenum == retnum) {
                        return insn;
                    }
                } else if ((insn instanceof Instruction_Jsr) || (insn instanceof Instruction_Jsr_w)) {
                    innerJsrs.add(insn);
                }
                instruction = insn.next;
            } else {
                return null;
            }
        }
    }

    private HashMap<Instruction, Instruction> inliningJsrTargets() {
        HashMap<Instruction, Instruction> newblocks = new HashMap<>();
        while (!this.jsrorder.isEmpty()) {
            Instruction jsr = this.jsrorder.removeFirst();
            Instruction astore = this.jsr2astore.get(jsr);
            Instruction ret = this.astore2ret.get(astore);
            Instruction newhead = makeCopyOf(astore, ret, jsr.next);
            Instruction_Goto togo = new Instruction_Goto();
            togo.target = newhead;
            newhead.labelled = true;
            togo.label = jsr.label;
            togo.labelled = jsr.labelled;
            togo.prev = jsr.prev;
            togo.next = jsr.next;
            togo.prev.next = togo;
            togo.next.prev = togo;
            this.replacedInsns.put(jsr, togo);
            if (ret != null) {
                newblocks.put(newhead, this.lastInstruction);
            }
        }
        return newblocks;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Instruction makeCopyOf(Instruction astore, Instruction ret, Instruction target) {
        if (ret == null) {
            return astore.next;
        }
        Instruction last = this.lastInstruction;
        int curlabel = this.lastInstruction.label;
        HashMap<Instruction, Instruction> insnmap = new HashMap<>();
        Instruction instruction = astore.next;
        while (true) {
            Instruction insn = instruction;
            if (insn == ret || insn == null) {
                break;
            }
            try {
                Instruction newone = (Instruction) insn.clone();
                curlabel++;
                newone.label = curlabel;
                newone.prev = last;
                last.next = newone;
                last = newone;
                insnmap.put(insn, newone);
            } catch (CloneNotSupportedException e) {
                logger.debug("Error !");
            }
            instruction = insn.next;
        }
        Instruction_Goto togo = new Instruction_Goto();
        togo.target = target;
        target.labelled = true;
        togo.label = curlabel + 1;
        last.next = togo;
        togo.prev = last;
        this.lastInstruction = togo;
        insnmap.put(astore, last.next);
        insnmap.put(ret, togo);
        Instruction instruction2 = last.next;
        while (true) {
            Instruction insn2 = instruction2;
            if (insn2 == togo) {
                break;
            }
            if (insn2 instanceof Instruction_branch) {
                Instruction oldtgt = ((Instruction_branch) insn2).target;
                Instruction newtgt = insnmap.get(oldtgt);
                if (newtgt != null) {
                    ((Instruction_branch) insn2).target = newtgt;
                    newtgt.labelled = true;
                }
            } else if (insn2 instanceof Instruction_Lookupswitch) {
                Instruction_Lookupswitch switchinsn = (Instruction_Lookupswitch) insn2;
                Instruction newdefault = insnmap.get(switchinsn.default_inst);
                if (newdefault != null) {
                    switchinsn.default_inst = newdefault;
                    newdefault.labelled = true;
                }
                for (int i = 0; i < switchinsn.match_insts.length; i++) {
                    Instruction newtgt2 = insnmap.get(switchinsn.match_insts[i]);
                    if (newtgt2 != null) {
                        switchinsn.match_insts[i] = newtgt2;
                        newtgt2.labelled = true;
                    }
                }
            } else if (insn2 instanceof Instruction_Tableswitch) {
                Instruction_Tableswitch switchinsn2 = (Instruction_Tableswitch) insn2;
                Instruction newdefault2 = insnmap.get(switchinsn2.default_inst);
                if (newdefault2 != null) {
                    switchinsn2.default_inst = newdefault2;
                    newdefault2.labelled = true;
                }
                for (int i2 = 0; i2 < switchinsn2.jump_insts.length; i2++) {
                    Instruction newtgt3 = insnmap.get(switchinsn2.jump_insts[i2]);
                    if (newtgt3 != null) {
                        switchinsn2.jump_insts[i2] = newtgt3;
                        newtgt3.labelled = true;
                    }
                }
            }
            instruction2 = insn2.next;
        }
        Code_attribute ca = this.method.locate_code_attribute();
        LinkedList<exception_table_entry> newentries = new LinkedList<>();
        int orig_start_of_subr = astore.next.originalIndex;
        int orig_end_of_subr = ret.originalIndex;
        for (int i3 = 0; i3 < ca.exception_table_length; i3++) {
            exception_table_entry etentry = ca.exception_table[i3];
            int orig_start_of_trap = etentry.start_pc;
            int orig_end_of_trap = etentry.end_pc;
            if (orig_start_of_trap < orig_end_of_subr && orig_end_of_trap > orig_start_of_subr) {
                exception_table_entry newone2 = new exception_table_entry();
                if (orig_start_of_trap <= orig_start_of_subr) {
                    newone2.start_inst = last.next;
                } else {
                    Instruction ins = insnmap.get(etentry.start_inst);
                    if (ins != null) {
                        newone2.start_inst = insnmap.get(etentry.start_inst);
                    } else {
                        newone2.start_inst = etentry.start_inst;
                    }
                }
                if (orig_end_of_trap > orig_end_of_subr) {
                    newone2.end_inst = null;
                } else {
                    newone2.end_inst = insnmap.get(etentry.end_inst);
                }
                newone2.handler_inst = insnmap.get(etentry.handler_inst);
                if (newone2.handler_inst == null) {
                    newone2.handler_inst = etentry.handler_inst;
                }
                newentries.add(newone2);
            }
            if (etentry.end_inst == null) {
                etentry.end_inst = last.next;
            }
        }
        if (newentries.size() > 0) {
            ca.exception_table_length += newentries.size();
            exception_table_entry[] newtable = new exception_table_entry[ca.exception_table_length];
            System.arraycopy(ca.exception_table, 0, newtable, 0, ca.exception_table.length);
            int i4 = 0;
            int j = ca.exception_table.length;
            while (i4 < newentries.size()) {
                newtable[j] = newentries.get(i4);
                i4++;
                j++;
            }
            ca.exception_table = newtable;
        }
        return last.next;
    }

    private void adjustBranchTargets() {
        Instruction instruction = this.sentinel.next;
        while (true) {
            Instruction insn = instruction;
            if (insn != null) {
                if (insn instanceof Instruction_branch) {
                    Instruction_branch binsn = (Instruction_branch) insn;
                    Instruction newtgt = this.replacedInsns.get(binsn.target);
                    if (newtgt != null) {
                        binsn.target = newtgt;
                        newtgt.labelled = true;
                    }
                } else if (insn instanceof Instruction_Lookupswitch) {
                    Instruction_Lookupswitch switchinsn = (Instruction_Lookupswitch) insn;
                    Instruction newdefault = this.replacedInsns.get(switchinsn.default_inst);
                    if (newdefault != null) {
                        switchinsn.default_inst = newdefault;
                        newdefault.labelled = true;
                    }
                    for (int i = 0; i < switchinsn.npairs; i++) {
                        Instruction newtgt2 = this.replacedInsns.get(switchinsn.match_insts[i]);
                        if (newtgt2 != null) {
                            switchinsn.match_insts[i] = newtgt2;
                            newtgt2.labelled = true;
                        }
                    }
                } else if (insn instanceof Instruction_Tableswitch) {
                    Instruction_Tableswitch switchinsn2 = (Instruction_Tableswitch) insn;
                    Instruction newdefault2 = this.replacedInsns.get(switchinsn2.default_inst);
                    if (newdefault2 != null) {
                        switchinsn2.default_inst = newdefault2;
                        newdefault2.labelled = true;
                    }
                    for (int i2 = 0; i2 <= switchinsn2.high - switchinsn2.low; i2++) {
                        Instruction newtgt3 = this.replacedInsns.get(switchinsn2.jump_insts[i2]);
                        if (newtgt3 != null) {
                            switchinsn2.jump_insts[i2] = newtgt3;
                            newtgt3.labelled = true;
                        }
                    }
                }
                instruction = insn.next;
            } else {
                return;
            }
        }
    }

    private void adjustExceptionTable() {
        Instruction newinsn;
        Code_attribute codeAttribute = this.method.locate_code_attribute();
        for (int i = 0; i < codeAttribute.exception_table_length; i++) {
            exception_table_entry entry = codeAttribute.exception_table[i];
            Instruction oldinsn = entry.start_inst;
            Instruction newinsn2 = this.replacedInsns.get(oldinsn);
            if (newinsn2 != null) {
                entry.start_inst = newinsn2;
            }
            Instruction oldinsn2 = entry.end_inst;
            if (entry.end_inst != null && (newinsn = this.replacedInsns.get(oldinsn2)) != null) {
                entry.end_inst = newinsn;
            }
            Instruction oldinsn3 = entry.handler_inst;
            Instruction newinsn3 = this.replacedInsns.get(oldinsn3);
            if (newinsn3 != null) {
                entry.handler_inst = newinsn3;
            }
        }
    }

    private void adjustLineNumberTable() {
        line_number_table_entry[] line_number_table_entryVarArr;
        if (!Options.v().keep_line_number() || this.method.code_attr == null) {
            return;
        }
        attribute_info[] attributes = this.method.code_attr.attributes;
        for (attribute_info element : attributes) {
            if (element instanceof LineNumberTable_attribute) {
                LineNumberTable_attribute lntattr = (LineNumberTable_attribute) element;
                for (line_number_table_entry element0 : lntattr.line_number_table) {
                    Instruction oldinst = element0.start_inst;
                    Instruction newinst = this.replacedInsns.get(oldinst);
                    if (newinst != null) {
                        element0.start_inst = newinst;
                    }
                }
            }
        }
    }

    public Instruction reconstructInstructions() {
        if (this.cfg != null) {
            return this.cfg.head;
        }
        return null;
    }

    public boolean jimplify(cp_info[] constant_pool, int this_class, BootstrapMethods_attribute bootstrap_methods_attribute, JimpleBody listBody) {
        this.bootstrap_methods_attribute = bootstrap_methods_attribute;
        Chain<Unit> units = listBody.getUnits();
        this.listBody = listBody;
        this.units = units;
        this.instructionToFirstStmt = new HashMap();
        this.instructionToLastStmt = new HashMap();
        this.jmethod = listBody.getMethod();
        this.cm = Scene.v();
        Set<Local> initialLocals = new ArraySet<>();
        List<Type> parameterTypes = this.jmethod.getParameterTypes();
        Code_attribute ca = this.method.locate_code_attribute();
        LocalVariableTable_attribute la = ca.findLocalVariableTable();
        LocalVariableTypeTable_attribute lt = ca.findLocalVariableTypeTable();
        Util.v().bodySetup(la, lt, constant_pool);
        boolean isStatic = Modifier.isStatic(this.jmethod.getModifiers());
        int currentLocalIndex = 0;
        if (!isStatic) {
            currentLocalIndex = 0 + 1;
            units.add(Jimple.v().newIdentityStmt(Util.v().getLocalForParameter(listBody, 0), Jimple.v().newThisRef(this.jmethod.getDeclaringClass().getType())));
        }
        int argCount = 0;
        for (Type type : parameterTypes) {
            Local local = Util.v().getLocalForParameter(listBody, currentLocalIndex);
            initialLocals.add(local);
            units.add(Jimple.v().newIdentityStmt(local, Jimple.v().newParameterRef(type, argCount)));
            if (type.equals(DoubleType.v()) || type.equals(LongType.v())) {
                currentLocalIndex += 2;
            } else {
                currentLocalIndex++;
            }
            argCount++;
        }
        Util.v().resetEasyNames();
        jimplify(constant_pool, this_class);
        return true;
    }

    private void buildInsnCFGfromBBCFG() {
        Instruction insn;
        BasicBlock basicBlock = this.cfg;
        while (true) {
            BasicBlock block = basicBlock;
            if (block != null) {
                Instruction instruction = block.head;
                while (true) {
                    insn = instruction;
                    if (insn == block.tail) {
                        break;
                    }
                    insn.succs = new Instruction[]{insn.next};
                    instruction = insn.next;
                }
                Vector<BasicBlock> bsucc = block.succ;
                int size = bsucc.size();
                Instruction[] succs = new Instruction[size];
                for (int i = 0; i < size; i++) {
                    succs[i] = bsucc.elementAt(i).head;
                }
                insn.succs = succs;
                basicBlock = block.next;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v195, types: [soot.jimple.Stmt] */
    void jimplify(cp_info[] constant_pool, int this_class) {
        line_number_table_entry[] line_number_table_entryVarArr;
        SootClass exception;
        IdentityStmt newIdentityStmt;
        Unit prev;
        Stmt afterEndStmt;
        TypeStack newTypeStack;
        Instruction[] succs;
        SootClass exception2;
        Code_attribute codeAttribute = this.method.locate_code_attribute();
        Set<Instruction> handlerInstructions = new ArraySet<>();
        Map<Instruction, SootClass> handlerInstructionToException = new HashMap<>();
        buildInsnCFGfromBBCFG();
        for (int i = 0; i < codeAttribute.exception_table_length; i++) {
            Instruction startIns = codeAttribute.exception_table[i].start_inst;
            Instruction endIns = codeAttribute.exception_table[i].end_inst;
            Instruction handlerIns = codeAttribute.exception_table[i].handler_inst;
            handlerInstructions.add(handlerIns);
            int catchType = codeAttribute.exception_table[i].catch_type;
            if (catchType != 0) {
                CONSTANT_Class_info classinfo = (CONSTANT_Class_info) constant_pool[catchType];
                String name = ((CONSTANT_Utf8_info) constant_pool[classinfo.name_index]).convert();
                exception2 = this.cm.getSootClass(name.replace('/', '.'));
            } else {
                exception2 = this.cm.getSootClass("java.lang.Throwable");
            }
            handlerInstructionToException.put(handlerIns, exception2);
            if (startIns == endIns) {
                throw new RuntimeException("Empty catch range for exception handler");
            }
            Instruction ins = startIns;
            do {
                Instruction[] succs2 = ins.succs;
                Instruction[] newsuccs = new Instruction[succs2.length + 1];
                System.arraycopy(succs2, 0, newsuccs, 0, succs2.length);
                newsuccs[succs2.length] = handlerIns;
                ins.succs = newsuccs;
                ins = ins.next;
                if (ins != endIns) {
                }
            } while (ins != null);
        }
        Set<Instruction> reachableInstructions = new HashSet<>();
        LinkedList<Instruction> instructionsToVisit = new LinkedList<>();
        reachableInstructions.add(this.firstInstruction);
        instructionsToVisit.addLast(this.firstInstruction);
        while (!instructionsToVisit.isEmpty()) {
            Instruction ins2 = instructionsToVisit.removeFirst();
            for (Instruction succ : ins2.succs) {
                if (!reachableInstructions.contains(succ)) {
                    reachableInstructions.add(succ);
                    instructionsToVisit.addLast(succ);
                }
            }
        }
        Map<Instruction, TypeStack> instructionToTypeStack = new HashMap<>();
        Map<Instruction, TypeStack> instructionToPostTypeStack = new HashMap<>();
        Set<Instruction> visitedInstructions = new HashSet<>();
        List<Instruction> changedInstructions = new ArrayList<>();
        TypeStack initialTypeStack = TypeStack.v();
        instructionToTypeStack.put(this.firstInstruction, initialTypeStack);
        visitedInstructions.add(this.firstInstruction);
        changedInstructions.add(this.firstInstruction);
        while (!changedInstructions.isEmpty()) {
            Instruction ins3 = changedInstructions.get(0);
            changedInstructions.remove(0);
            OutFlow ret = processFlow(ins3, instructionToTypeStack.get(ins3), constant_pool);
            instructionToPostTypeStack.put(ins3, ret.typeStack);
            Instruction[] successors = ins3.succs;
            for (Instruction s : successors) {
                if (!visitedInstructions.contains(s)) {
                    if (handlerInstructions.contains(s)) {
                        TypeStack exceptionTypeStack = TypeStack.v().push(RefType.v(handlerInstructionToException.get(s).getName()));
                        instructionToTypeStack.put(s, exceptionTypeStack);
                    } else {
                        instructionToTypeStack.put(s, ret.typeStack);
                    }
                    visitedInstructions.add(s);
                    changedInstructions.add(s);
                } else {
                    TypeStack oldTypeStack = instructionToTypeStack.get(s);
                    if (handlerInstructions.contains(s)) {
                        TypeStack exceptionTypeStack2 = TypeStack.v().push(RefType.v(handlerInstructionToException.get(s).getName()));
                        newTypeStack = exceptionTypeStack2;
                    } else {
                        try {
                            newTypeStack = ret.typeStack.merge(oldTypeStack);
                        } catch (RuntimeException re) {
                            logger.debug("Considering " + s);
                            throw re;
                        }
                    }
                    if (!newTypeStack.equals(oldTypeStack)) {
                        changedInstructions.add(s);
                    }
                    instructionToTypeStack.put(s, newTypeStack);
                }
            }
        }
        BasicBlock basicBlock = this.cfg;
        while (true) {
            BasicBlock b = basicBlock;
            if (b == null) {
                break;
            }
            Instruction ins4 = b.head;
            b.statements = new ArrayList();
            List<Stmt> blockStatements = b.statements;
            while (true) {
                List<Stmt> statementsForIns = new ArrayList<>();
                if (reachableInstructions.contains(ins4)) {
                    generateJimple(ins4, instructionToTypeStack.get(ins4), instructionToPostTypeStack.get(ins4), constant_pool, statementsForIns, b);
                } else {
                    statementsForIns.add(Jimple.v().newNopStmt());
                }
                if (!statementsForIns.isEmpty()) {
                    for (int i2 = 0; i2 < statementsForIns.size(); i2++) {
                        this.units.add(statementsForIns.get(i2));
                        blockStatements.add(statementsForIns.get(i2));
                    }
                    this.instructionToFirstStmt.put(ins4, statementsForIns.get(0));
                    this.instructionToLastStmt.put(ins4, statementsForIns.get(statementsForIns.size() - 1));
                }
                if (ins4 == b.tail) {
                    break;
                }
                ins4 = ins4.next;
            }
            basicBlock = b.next;
        }
        jimpleTargetFixup();
        Map<Stmt, Stmt> targetToHandler = new HashMap<>();
        for (int i3 = 0; i3 < codeAttribute.exception_table_length; i3++) {
            Instruction startIns2 = codeAttribute.exception_table[i3].start_inst;
            Instruction endIns2 = codeAttribute.exception_table[i3].end_inst;
            Instruction targetIns = codeAttribute.exception_table[i3].handler_inst;
            if (!this.instructionToFirstStmt.containsKey(startIns2) || (endIns2 != null && !this.instructionToLastStmt.containsKey(endIns2))) {
                throw new RuntimeException("Exception range does not coincide with jimple instructions");
            }
            if (!this.instructionToFirstStmt.containsKey(targetIns)) {
                throw new RuntimeException("Exception handler does not coincide with jimple instruction");
            }
            int catchType2 = codeAttribute.exception_table[i3].catch_type;
            if (catchType2 != 0) {
                CONSTANT_Class_info classinfo2 = (CONSTANT_Class_info) constant_pool[catchType2];
                String name2 = ((CONSTANT_Utf8_info) constant_pool[classinfo2.name_index]).convert();
                exception = this.cm.getSootClass(name2.replace('/', '.'));
            } else {
                exception = this.cm.getSootClass("java.lang.Throwable");
            }
            Stmt firstTargetStmt = this.instructionToFirstStmt.get(targetIns);
            if (targetToHandler.containsKey(firstTargetStmt)) {
                newIdentityStmt = targetToHandler.get(firstTargetStmt);
            } else {
                Local local = Util.v().getLocalCreatingIfNecessary(this.listBody, "$stack0", UnknownType.v());
                newIdentityStmt = Jimple.v().newIdentityStmt(local, Jimple.v().newCaughtExceptionRef());
                ((PatchingChain) this.units).insertBeforeNoRedirect(newIdentityStmt, (IdentityStmt) firstTargetStmt);
                targetToHandler.put(firstTargetStmt, newIdentityStmt);
                if (this.units.getFirst() != newIdentityStmt && (prev = this.units.getPredOf(newIdentityStmt)) != null && prev.fallsThrough()) {
                    this.units.insertAfter(Jimple.v().newGotoStmt(firstTargetStmt), (GotoStmt) prev);
                }
            }
            Stmt firstStmt = this.instructionToFirstStmt.get(startIns2);
            if (endIns2 == null) {
                afterEndStmt = (Stmt) this.units.getLast();
            } else {
                afterEndStmt = this.instructionToLastStmt.get(endIns2);
                Stmt catchStart = (IdentityStmt) targetToHandler.get(afterEndStmt);
                if (catchStart == null) {
                    continue;
                } else if (catchStart != this.units.getPredOf(afterEndStmt)) {
                    throw new IllegalStateException("Assertion failure: catchStart != pred of afterEndStmt");
                } else {
                    afterEndStmt = catchStart;
                }
            }
            Trap trap = Jimple.v().newTrap(exception, firstStmt, afterEndStmt, newIdentityStmt);
            this.listBody.getTraps().add(trap);
        }
        if (Options.v().keep_line_number()) {
            HashMap<Stmt, Tag> stmtstags = new HashMap<>();
            LinkedList<Stmt> startstmts = new LinkedList<>();
            attribute_info[] attrs = codeAttribute.attributes;
            for (attribute_info element : attrs) {
                if (element instanceof LineNumberTable_attribute) {
                    LineNumberTable_attribute lntattr = (LineNumberTable_attribute) element;
                    for (line_number_table_entry element0 : lntattr.line_number_table) {
                        Stmt start_stmt = this.instructionToFirstStmt.get(element0.start_inst);
                        if (start_stmt != null) {
                            LineNumberTag lntag = new LineNumberTag(element0.line_number);
                            stmtstags.put(start_stmt, lntag);
                            startstmts.add(start_stmt);
                        }
                    }
                }
            }
            Iterator<Stmt> stmtIt = new ArrayList(stmtstags.keySet()).iterator();
            while (stmtIt.hasNext()) {
                Stmt stmt = stmtIt.next();
                Stmt pred = stmt;
                Tag tag = stmtstags.get(stmt);
                while (true) {
                    pred = (Stmt) this.units.getPredOf(pred);
                    if (pred != null && (pred instanceof IdentityStmt)) {
                        stmtstags.put(pred, tag);
                        pred.addTag(tag);
                    }
                }
            }
            for (int i4 = 0; i4 < startstmts.size(); i4++) {
                Stmt stmt2 = startstmts.get(i4);
                Tag tag2 = stmtstags.get(stmt2);
                stmt2.addTag(tag2);
                Unit succOf = this.units.getSuccOf(stmt2);
                while (true) {
                    Stmt stmt3 = (Stmt) succOf;
                    if (stmt3 != null && !stmtstags.containsKey(stmt3)) {
                        stmt3.addTag(tag2);
                        succOf = this.units.getSuccOf(stmt3);
                    }
                }
            }
        }
    }

    private Type byteCodeTypeOf(Type type) {
        if (type.equals(ShortType.v()) || type.equals(CharType.v()) || type.equals(ByteType.v()) || type.equals(BooleanType.v())) {
            return IntType.v();
        }
        return type;
    }

    OutFlow processFlow(Instruction ins, TypeStack typeStack, cp_info[] constant_pool) {
        Type castType;
        TypeStack popSafe;
        TypeStack popSafe2;
        TypeStack popSafe3;
        TypeStack popSafe4;
        TypeStack popSafe5;
        TypeStack typeStack2;
        Type baseType;
        int x = ins.code & 255;
        switch (x) {
            case 0:
            case 116:
            case 117:
            case 118:
            case 119:
            case 132:
            case 145:
            case 146:
            case 147:
            case 167:
            case 169:
            case 177:
            case 191:
            case 200:
            case 202:
            case 209:
                break;
            case 1:
                typeStack = typeStack.push(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                typeStack = typeStack.push(IntType.v());
                break;
            case 9:
            case 10:
                typeStack = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 11:
            case 12:
            case 13:
                typeStack = typeStack.push(FloatType.v());
                break;
            case 14:
            case 15:
                typeStack = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 16:
                typeStack = typeStack.push(IntType.v());
                break;
            case 17:
                typeStack = typeStack.push(IntType.v());
                break;
            case 18:
                return processCPEntry(constant_pool, ((Instruction_Ldc1) ins).arg_b, typeStack, this.jmethod);
            case 19:
            case 20:
                return processCPEntry(constant_pool, ((Instruction_intindex) ins).arg_i, typeStack, this.jmethod);
            case 21:
                typeStack = typeStack.push(IntType.v());
                break;
            case 22:
                typeStack = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 23:
                typeStack = typeStack.push(FloatType.v());
                break;
            case 24:
                typeStack = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 25:
                typeStack = typeStack.push(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                typeStack = typeStack.push(IntType.v());
                break;
            case 30:
            case 31:
            case 32:
            case 33:
                typeStack = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 34:
            case 35:
            case 36:
            case 37:
                typeStack = typeStack.push(FloatType.v());
                break;
            case 38:
            case 39:
            case 40:
            case 41:
                typeStack = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 42:
            case 43:
            case 44:
            case 45:
                typeStack = typeStack.push(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
                break;
            case 46:
            case 51:
            case 52:
            case 53:
                typeStack = popSafeRefType(popSafe(typeStack, IntType.v())).push(IntType.v());
                break;
            case 47:
                typeStack = popSafeRefType(popSafe(typeStack, IntType.v())).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 48:
                typeStack = popSafeRefType(popSafe(typeStack, FloatType.v())).push(FloatType.v());
                break;
            case 49:
                typeStack = popSafeRefType(popSafe(typeStack, IntType.v())).push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 50:
                TypeStack typeStack3 = popSafe(typeStack, IntType.v());
                if (typeStack3.top() instanceof ArrayType) {
                    ArrayType arrayType = (ArrayType) typeStack3.top();
                    TypeStack typeStack4 = popSafeRefType(typeStack3);
                    if (arrayType.numDimensions == 1) {
                        typeStack = typeStack4.push(arrayType.baseType);
                        break;
                    } else {
                        typeStack = typeStack4.push(ArrayType.v(arrayType.baseType, arrayType.numDimensions - 1));
                        break;
                    }
                } else {
                    typeStack = popSafeRefType(typeStack3).push(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
                    break;
                }
            case 54:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 55:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                break;
            case 56:
                typeStack = popSafe(typeStack, FloatType.v());
                break;
            case 57:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                break;
            case 58:
                typeStack = typeStack.pop();
                break;
            case 59:
            case 60:
            case 61:
            case 62:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 63:
            case 64:
            case 65:
            case 66:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                break;
            case 67:
            case 68:
            case 69:
            case 70:
                typeStack = popSafe(typeStack, FloatType.v());
                break;
            case 71:
            case 72:
            case 73:
            case 74:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                break;
            case 75:
            case 76:
            case 77:
            case 78:
                if (!(typeStack.top() instanceof StmtAddressType) && !(typeStack.top() instanceof RefType) && !(typeStack.top() instanceof ArrayType)) {
                    throw new RuntimeException("Astore failed, invalid stack type: " + typeStack.top());
                }
                typeStack = typeStack.pop();
                break;
                break;
            case 79:
            case 84:
            case 85:
            case 86:
                typeStack = popSafeRefType(popSafe(popSafe(typeStack, IntType.v()), IntType.v()));
                break;
            case 80:
                typeStack = popSafeRefType(popSafe(popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()), IntType.v()));
                break;
            case 81:
                typeStack = popSafeRefType(popSafe(popSafe(typeStack, FloatType.v()), IntType.v()));
                break;
            case 82:
                typeStack = popSafeRefType(popSafe(popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()), IntType.v()));
                break;
            case 83:
                typeStack = popSafeRefType(popSafe(popSafeRefType(typeStack), IntType.v()));
                break;
            case 87:
                typeStack = typeStack.pop();
                break;
            case 88:
                typeStack = typeStack.pop().pop();
                break;
            case 89:
                typeStack = typeStack.push(typeStack.top());
                break;
            case 90:
                Type topType = typeStack.get(typeStack.topIndex());
                typeStack = typeStack.pop().pop().push(topType).push(typeStack.get(typeStack.topIndex() - 1)).push(topType);
                break;
            case 91:
                Type topType2 = typeStack.get(typeStack.topIndex());
                Type secondType = typeStack.get(typeStack.topIndex() - 1);
                Type thirdType = typeStack.get(typeStack.topIndex() - 2);
                typeStack = typeStack.pop().pop().pop().push(topType2).push(thirdType).push(secondType).push(topType2);
                break;
            case 92:
                Type topType3 = typeStack.get(typeStack.topIndex());
                typeStack = typeStack.push(typeStack.get(typeStack.topIndex() - 1)).push(topType3);
                break;
            case 93:
                Type topType4 = typeStack.get(typeStack.topIndex());
                Type secondType2 = typeStack.get(typeStack.topIndex() - 1);
                Type thirdType2 = typeStack.get(typeStack.topIndex() - 2);
                typeStack = typeStack.pop().pop().pop().push(secondType2).push(topType4).push(thirdType2).push(secondType2).push(topType4);
                break;
            case 94:
                Type topType5 = typeStack.get(typeStack.topIndex());
                Type secondType3 = typeStack.get(typeStack.topIndex() - 1);
                Type thirdType3 = typeStack.get(typeStack.topIndex() - 2);
                Type fourthType = typeStack.get(typeStack.topIndex() - 3);
                typeStack = typeStack.pop().pop().pop().pop().push(secondType3).push(topType5).push(fourthType).push(thirdType3).push(secondType3).push(topType5);
                break;
            case 95:
                Type topType6 = typeStack.top();
                TypeStack typeStack5 = typeStack.pop();
                typeStack = typeStack5.pop().push(topType6).push(typeStack5.top());
                break;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
                typeStack = popSafe(popSafe(typeStack, IntType.v()), IntType.v()).push(IntType.v());
                break;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                typeStack = popSafe(popSafe(popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()), Long2ndHalfType.v()), LongType.v()).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
                typeStack = popSafe(popSafe(typeStack, FloatType.v()), FloatType.v()).push(FloatType.v());
                break;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                typeStack = popSafe(popSafe(popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()), Double2ndHalfType.v()), DoubleType.v()).push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 121:
            case 123:
            case 125:
                typeStack = popSafe(popSafe(popSafe(typeStack, IntType.v()), Long2ndHalfType.v()), LongType.v()).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 133:
                typeStack = popSafe(typeStack, IntType.v()).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 134:
                typeStack = popSafe(typeStack, IntType.v()).push(FloatType.v());
                break;
            case 135:
                typeStack = popSafe(typeStack, IntType.v()).push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 136:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()).push(IntType.v());
                break;
            case 137:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()).push(FloatType.v());
                break;
            case 138:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()).push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 139:
                typeStack = popSafe(typeStack, FloatType.v()).push(IntType.v());
                break;
            case 140:
                typeStack = popSafe(typeStack, FloatType.v()).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 141:
                typeStack = popSafe(typeStack, FloatType.v()).push(DoubleType.v()).push(Double2ndHalfType.v());
                break;
            case 142:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()).push(IntType.v());
                break;
            case 143:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()).push(LongType.v()).push(Long2ndHalfType.v());
                break;
            case 144:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()).push(FloatType.v());
                break;
            case 148:
                typeStack = popSafe(popSafe(popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v()), Long2ndHalfType.v()), LongType.v()).push(IntType.v());
                break;
            case 149:
            case 150:
                typeStack = popSafe(popSafe(typeStack, FloatType.v()), FloatType.v()).push(IntType.v());
                break;
            case 151:
            case 152:
                typeStack = popSafe(popSafe(popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v()), Double2ndHalfType.v()), DoubleType.v()).push(IntType.v());
                break;
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
                typeStack = popSafe(popSafe(typeStack, IntType.v()), IntType.v());
                break;
            case 165:
            case 166:
                typeStack = popSafeRefType(popSafeRefType(typeStack));
                break;
            case 168:
            case 201:
                typeStack = typeStack.push(StmtAddressType.v());
                break;
            case 170:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 171:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 172:
                typeStack = popSafe(typeStack, IntType.v());
                break;
            case 173:
                typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                break;
            case 174:
                typeStack = popSafe(typeStack, FloatType.v());
                break;
            case 175:
                typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                break;
            case 176:
                typeStack = popSafeRefType(typeStack);
                break;
            case 178:
                Type type = byteCodeTypeOf(jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Getstatic) ins).arg_i));
                if (type.equals(DoubleType.v())) {
                    typeStack = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
                    break;
                } else if (type.equals(LongType.v())) {
                    typeStack = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
                    break;
                } else {
                    typeStack = typeStack.push(type);
                    break;
                }
            case 179:
                Type type2 = byteCodeTypeOf(jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Putstatic) ins).arg_i));
                if (type2.equals(DoubleType.v())) {
                    typeStack = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    break;
                } else if (type2.equals(LongType.v())) {
                    typeStack = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    break;
                } else if (type2 instanceof RefType) {
                    typeStack = popSafeRefType(typeStack);
                    break;
                } else {
                    typeStack = popSafe(typeStack, type2);
                    break;
                }
            case 180:
                Type type3 = byteCodeTypeOf(jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Getfield) ins).arg_i));
                TypeStack typeStack6 = popSafeRefType(typeStack);
                if (type3.equals(DoubleType.v())) {
                    typeStack = typeStack6.push(DoubleType.v()).push(Double2ndHalfType.v());
                    break;
                } else if (type3.equals(LongType.v())) {
                    typeStack = typeStack6.push(LongType.v()).push(Long2ndHalfType.v());
                    break;
                } else {
                    typeStack = typeStack6.push(type3);
                    break;
                }
            case 181:
                Type type4 = byteCodeTypeOf(jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Putfield) ins).arg_i));
                if (type4.equals(DoubleType.v())) {
                    typeStack2 = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                } else if (type4.equals(LongType.v())) {
                    typeStack2 = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                } else if (type4 instanceof RefType) {
                    typeStack2 = popSafeRefType(typeStack);
                } else {
                    typeStack2 = popSafe(typeStack, type4);
                }
                typeStack = popSafeRefType(typeStack2);
                break;
            case 182:
                Instruction_Invokevirtual iv = (Instruction_Invokevirtual) ins;
                int args = cp_info.countParams(constant_pool, iv.arg_i);
                Type returnType = byteCodeTypeOf(jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv.arg_i));
                for (int j = args - 1; j >= 0; j--) {
                    if (typeStack.top().equals(Long2ndHalfType.v())) {
                        popSafe4 = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    } else if (typeStack.top().equals(Double2ndHalfType.v())) {
                        popSafe4 = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    } else {
                        popSafe4 = popSafe(typeStack, typeStack.top());
                    }
                    typeStack = popSafe4;
                }
                typeStack = popSafeRefType(typeStack);
                if (!returnType.equals(VoidType.v())) {
                    typeStack = smartPush(typeStack, returnType);
                    break;
                }
                break;
            case 183:
                Instruction_Invokenonvirtual iv2 = (Instruction_Invokenonvirtual) ins;
                int args2 = cp_info.countParams(constant_pool, iv2.arg_i);
                Type returnType2 = byteCodeTypeOf(jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv2.arg_i));
                for (int j2 = args2 - 1; j2 >= 0; j2--) {
                    if (typeStack.top().equals(Long2ndHalfType.v())) {
                        popSafe3 = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    } else if (typeStack.top().equals(Double2ndHalfType.v())) {
                        popSafe3 = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    } else {
                        popSafe3 = popSafe(typeStack, typeStack.top());
                    }
                    typeStack = popSafe3;
                }
                typeStack = popSafeRefType(typeStack);
                if (!returnType2.equals(VoidType.v())) {
                    typeStack = smartPush(typeStack, returnType2);
                    break;
                }
                break;
            case 184:
                Instruction_Invokestatic iv3 = (Instruction_Invokestatic) ins;
                int args3 = cp_info.countParams(constant_pool, iv3.arg_i);
                Type returnType3 = byteCodeTypeOf(jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv3.arg_i));
                for (int j3 = args3 - 1; j3 >= 0; j3--) {
                    if (typeStack.top().equals(Long2ndHalfType.v())) {
                        popSafe2 = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    } else if (typeStack.top().equals(Double2ndHalfType.v())) {
                        popSafe2 = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    } else {
                        popSafe2 = popSafe(typeStack, typeStack.top());
                    }
                    typeStack = popSafe2;
                }
                if (!returnType3.equals(VoidType.v())) {
                    typeStack = smartPush(typeStack, returnType3);
                    break;
                }
                break;
            case 185:
                Instruction_Invokeinterface iv4 = (Instruction_Invokeinterface) ins;
                int args4 = cp_info.countParams(constant_pool, iv4.arg_i);
                Type returnType4 = byteCodeTypeOf(jimpleReturnTypeOfInterfaceMethodRef(this.cm, constant_pool, iv4.arg_i));
                for (int j4 = args4 - 1; j4 >= 0; j4--) {
                    if (typeStack.top().equals(Long2ndHalfType.v())) {
                        popSafe = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    } else if (typeStack.top().equals(Double2ndHalfType.v())) {
                        popSafe = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    } else {
                        popSafe = popSafe(typeStack, typeStack.top());
                    }
                    typeStack = popSafe;
                }
                typeStack = popSafeRefType(typeStack);
                if (!returnType4.equals(VoidType.v())) {
                    typeStack = smartPush(typeStack, returnType4);
                    break;
                }
                break;
            case 186:
                CONSTANT_InvokeDynamic_info iv_info = (CONSTANT_InvokeDynamic_info) constant_pool[((Instruction_Invokedynamic) ins).invoke_dynamic_index];
                int args5 = cp_info.countParams(constant_pool, iv_info.name_and_type_index);
                Type returnType5 = byteCodeTypeOf(jimpleReturnTypeOfNameAndType(this.cm, constant_pool, iv_info.name_and_type_index));
                for (int j5 = args5 - 1; j5 >= 0; j5--) {
                    if (typeStack.top().equals(Long2ndHalfType.v())) {
                        popSafe5 = popSafe(popSafe(typeStack, Long2ndHalfType.v()), LongType.v());
                    } else if (typeStack.top().equals(Double2ndHalfType.v())) {
                        popSafe5 = popSafe(popSafe(typeStack, Double2ndHalfType.v()), DoubleType.v());
                    } else {
                        popSafe5 = popSafe(typeStack, typeStack.top());
                    }
                    typeStack = popSafe5;
                }
                if (!returnType5.equals(VoidType.v())) {
                    typeStack = smartPush(typeStack, returnType5);
                    break;
                }
                break;
            case 187:
                typeStack = typeStack.push(RefType.v(getClassName(constant_pool, ((Instruction_New) ins).arg_i)));
                break;
            case 188:
                TypeStack typeStack7 = popSafe(typeStack, IntType.v());
                Type baseType2 = jimpleTypeOfAtype(((Instruction_Newarray) ins).atype);
                typeStack = typeStack7.push(ArrayType.v(baseType2, 1));
                break;
            case 189:
                CONSTANT_Class_info c = (CONSTANT_Class_info) constant_pool[((Instruction_Anewarray) ins).arg_i];
                String name = ((CONSTANT_Utf8_info) constant_pool[c.name_index]).convert().replace('/', '.');
                if (name.startsWith("[")) {
                    String baseName = getClassName(constant_pool, ((Instruction_Anewarray) ins).arg_i);
                    baseType = Util.v().jimpleTypeOfFieldDescriptor(baseName);
                } else {
                    baseType = RefType.v(name);
                }
                typeStack = popSafe(typeStack, IntType.v()).push(baseType.makeArrayType());
                break;
            case 190:
                typeStack = popSafeRefType(typeStack).push(IntType.v());
                break;
            case 192:
                String className = getClassName(constant_pool, ((Instruction_Checkcast) ins).arg_i);
                if (className.startsWith("[")) {
                    castType = Util.v().jimpleTypeOfFieldDescriptor(getClassName(constant_pool, ((Instruction_Checkcast) ins).arg_i));
                } else {
                    castType = RefType.v(className);
                }
                typeStack = popSafeRefType(typeStack).push(castType);
                break;
            case 193:
                typeStack = popSafeRefType(typeStack).push(IntType.v());
                break;
            case 194:
                typeStack = popSafeRefType(typeStack);
                break;
            case 195:
                typeStack = popSafeRefType(typeStack);
                break;
            case 196:
                throw new RuntimeException("Wide instruction should not be encountered");
            case 197:
                int bdims = ((Instruction_Multianewarray) ins).dims;
                CONSTANT_Class_info c2 = (CONSTANT_Class_info) constant_pool[((Instruction_Multianewarray) ins).arg_i];
                String arrayDescriptor = ((CONSTANT_Utf8_info) constant_pool[c2.name_index]).convert();
                ArrayType arrayType2 = (ArrayType) Util.v().jimpleTypeOfFieldDescriptor(arrayDescriptor);
                for (int j6 = 0; j6 < bdims; j6++) {
                    typeStack = popSafe(typeStack, IntType.v());
                }
                typeStack = typeStack.push(arrayType2);
                break;
            case 198:
            case 199:
                typeStack = popSafeRefType(typeStack);
                break;
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            default:
                throw new RuntimeException("processFlow failed: Unknown bytecode instruction: " + x);
        }
        return new OutFlow(typeStack);
    }

    private Type jimpleTypeOfFieldInFieldRef(Scene cm, cp_info[] constant_pool, int index) {
        CONSTANT_Fieldref_info fr = (CONSTANT_Fieldref_info) constant_pool[index];
        CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info) constant_pool[fr.name_and_type_index];
        String fieldDescriptor = ((CONSTANT_Utf8_info) constant_pool[nat.descriptor_index]).convert();
        return Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor);
    }

    private Type jimpleReturnTypeOfNameAndType(Scene cm, cp_info[] constant_pool, int index) {
        CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info) constant_pool[index];
        String methodDescriptor = ((CONSTANT_Utf8_info) constant_pool[nat.descriptor_index]).convert();
        return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
    }

    private Type jimpleReturnTypeOfMethodRef(Scene cm, cp_info[] constant_pool, int index) {
        CONSTANT_Methodref_info mr = (CONSTANT_Methodref_info) constant_pool[index];
        CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info) constant_pool[mr.name_and_type_index];
        String methodDescriptor = ((CONSTANT_Utf8_info) constant_pool[nat.descriptor_index]).convert();
        return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
    }

    private Type jimpleReturnTypeOfInterfaceMethodRef(Scene cm, cp_info[] constant_pool, int index) {
        CONSTANT_InterfaceMethodref_info mr = (CONSTANT_InterfaceMethodref_info) constant_pool[index];
        CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info) constant_pool[mr.name_and_type_index];
        String methodDescriptor = ((CONSTANT_Utf8_info) constant_pool[nat.descriptor_index]).convert();
        return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
    }

    private OutFlow processCPEntry(cp_info[] constant_pool, int i, TypeStack typeStack, SootMethod jmethod) {
        TypeStack typeStack2;
        Type baseType;
        cp_info c = constant_pool[i];
        if (c instanceof CONSTANT_Integer_info) {
            typeStack2 = typeStack.push(IntType.v());
        } else if (c instanceof CONSTANT_Float_info) {
            typeStack2 = typeStack.push(FloatType.v());
        } else if (c instanceof CONSTANT_Long_info) {
            typeStack2 = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
        } else if (c instanceof CONSTANT_Double_info) {
            typeStack2 = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
        } else if (c instanceof CONSTANT_String_info) {
            typeStack2 = typeStack.push(RefType.v("java.lang.String"));
        } else if (c instanceof CONSTANT_Utf8_info) {
            typeStack2 = typeStack.push(RefType.v("java.lang.String"));
        } else if (c instanceof CONSTANT_Class_info) {
            CONSTANT_Class_info info = (CONSTANT_Class_info) c;
            String name = ((CONSTANT_Utf8_info) constant_pool[info.name_index]).convert().replace('/', '.');
            if (name.charAt(0) == '[') {
                int dim = 0;
                while (name.charAt(dim) == '[') {
                    dim++;
                }
                char typeIndicator = name.charAt(dim);
                switch (typeIndicator) {
                    case 'B':
                        baseType = ByteType.v();
                        break;
                    case 'C':
                        baseType = CharType.v();
                        break;
                    case 'D':
                        baseType = DoubleType.v();
                        break;
                    case 'F':
                        baseType = FloatType.v();
                        break;
                    case 'I':
                        baseType = IntType.v();
                        break;
                    case 'J':
                        baseType = LongType.v();
                        break;
                    case 'L':
                        baseType = RefType.v(name.substring(dim + 1, name.length() - 1));
                        break;
                    case 'S':
                        baseType = ShortType.v();
                        break;
                    case 'Z':
                        baseType = BooleanType.v();
                        break;
                    default:
                        throw new RuntimeException("Unknown Array Base Type in Class Constant");
                }
                typeStack2 = typeStack.push(ArrayType.v(baseType, dim));
            } else {
                typeStack2 = typeStack.push(RefType.v(name));
            }
        } else {
            throw new RuntimeException("Attempting to push a non-constant cp entry" + c.getClass());
        }
        return new OutFlow(typeStack2);
    }

    TypeStack smartPush(TypeStack typeStack, Type type) {
        TypeStack typeStack2;
        if (type.equals(LongType.v())) {
            typeStack2 = typeStack.push(LongType.v()).push(Long2ndHalfType.v());
        } else if (type.equals(DoubleType.v())) {
            typeStack2 = typeStack.push(DoubleType.v()).push(Double2ndHalfType.v());
        } else {
            typeStack2 = typeStack.push(type);
        }
        return typeStack2;
    }

    TypeStack popSafeRefType(TypeStack typeStack) {
        return typeStack.pop();
    }

    TypeStack popSafeArrayType(TypeStack typeStack) {
        return typeStack.pop();
    }

    TypeStack popSafe(TypeStack typeStack, Type requiredType) {
        return typeStack.pop();
    }

    void confirmType(Type actualType, Type requiredType) {
    }

    String getClassName(cp_info[] constant_pool, int index) {
        CONSTANT_Class_info c = (CONSTANT_Class_info) constant_pool[index];
        String name = ((CONSTANT_Utf8_info) constant_pool[c.name_index]).convert();
        return name.replace('/', '.');
    }

    void confirmRefType(Type actualType) {
    }

    private void processTargetFixup(BBQ bbq) {
        while (!bbq.isEmpty()) {
            try {
                BasicBlock b = bbq.pull();
                Stmt s = b.getTailJStmt();
                if (s instanceof GotoStmt) {
                    if (b.succ.size() == 1) {
                        ((GotoStmt) s).setTarget(b.succ.firstElement().getHeadJStmt());
                    } else {
                        logger.debug("Error :");
                        for (int i = 0; i < b.statements.size(); i++) {
                            logger.debug(new StringBuilder().append(b.statements.get(i)).toString());
                        }
                        throw new RuntimeException(b + " has " + b.succ.size() + " successors.");
                    }
                } else if (s instanceof IfStmt) {
                    if (b.succ.size() != 2) {
                        logger.debug("How can an if not have 2 successors?");
                    }
                    if (b.succ.firstElement() == b.next) {
                        ((IfStmt) s).setTarget(b.succ.elementAt(1).getHeadJStmt());
                    } else {
                        ((IfStmt) s).setTarget(b.succ.firstElement().getHeadJStmt());
                    }
                } else if (s instanceof TableSwitchStmt) {
                    int count = 0;
                    TableSwitchStmt sts = (TableSwitchStmt) s;
                    Iterator<BasicBlock> it = b.succ.iterator();
                    while (it.hasNext()) {
                        BasicBlock basicBlock = it.next();
                        if (count == 0) {
                            sts.setDefaultTarget(basicBlock.getHeadJStmt());
                        } else {
                            sts.setTarget(count - 1, basicBlock.getHeadJStmt());
                        }
                        count++;
                    }
                } else if (s instanceof LookupSwitchStmt) {
                    int count2 = 0;
                    LookupSwitchStmt sls = (LookupSwitchStmt) s;
                    Iterator<BasicBlock> it2 = b.succ.iterator();
                    while (it2.hasNext()) {
                        BasicBlock basicBlock2 = it2.next();
                        if (count2 == 0) {
                            sls.setDefaultTarget(basicBlock2.getHeadJStmt());
                        } else {
                            sls.setTarget(count2 - 1, basicBlock2.getHeadJStmt());
                        }
                        count2++;
                    }
                }
                b.done = false;
                Iterator<BasicBlock> it3 = b.succ.iterator();
                while (it3.hasNext()) {
                    BasicBlock basicBlock3 = it3.next();
                    if (basicBlock3.done) {
                        bbq.push(basicBlock3);
                    }
                }
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    void jimpleTargetFixup() {
        BBQ bbq = new BBQ();
        Code_attribute c = this.method.locate_code_attribute();
        if (c == null) {
            return;
        }
        BasicBlock basicBlock = this.cfg;
        while (true) {
            BasicBlock bb = basicBlock;
            if (bb == null) {
                break;
            }
            bb.done = true;
            basicBlock = bb.next;
        }
        bbq.push(this.cfg);
        processTargetFixup(bbq);
        if (bbq.isEmpty()) {
            for (int i = 0; i < c.exception_table_length; i++) {
                BasicBlock b = c.exception_table[i].b;
                if (b != null && b.done) {
                    bbq.push(b);
                    processTargetFixup(bbq);
                    if (!bbq.isEmpty()) {
                        logger.debug("Error 2nd processing exception block.");
                        return;
                    }
                }
            }
        }
    }

    private void generateJimpleForCPEntry(cp_info[] constant_pool, int i, TypeStack typeStack, TypeStack postTypeStack, SootMethod jmethod, List<Stmt> statements) {
        Stmt stmt;
        cp_info c = constant_pool[i];
        if (c instanceof CONSTANT_Integer_info) {
            CONSTANT_Integer_info ci = (CONSTANT_Integer_info) c;
            Value rvalue = IntConstant.v((int) ci.bytes);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
        } else if (c instanceof CONSTANT_Float_info) {
            CONSTANT_Float_info cf = (CONSTANT_Float_info) c;
            Value rvalue2 = FloatConstant.v(cf.convert());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue2);
        } else if (c instanceof CONSTANT_Long_info) {
            CONSTANT_Long_info cl = (CONSTANT_Long_info) c;
            Value rvalue3 = LongConstant.v(cl.convert());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue3);
        } else if (c instanceof CONSTANT_Double_info) {
            CONSTANT_Double_info cd = (CONSTANT_Double_info) c;
            Value rvalue4 = DoubleConstant.v(cd.convert());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue4);
        } else if (c instanceof CONSTANT_String_info) {
            CONSTANT_String_info cs = (CONSTANT_String_info) c;
            String constant = cs.toString(constant_pool);
            if (constant.startsWith("\"") && constant.endsWith("\"")) {
                constant = constant.substring(1, constant.length() - 1);
            }
            Value rvalue5 = StringConstant.v(constant);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue5);
        } else if (c instanceof CONSTANT_Utf8_info) {
            CONSTANT_Utf8_info cu = (CONSTANT_Utf8_info) c;
            String constant2 = cu.convert();
            if (constant2.startsWith("\"") && constant2.endsWith("\"")) {
                constant2 = constant2.substring(1, constant2.length() - 1);
            }
            Value rvalue6 = StringConstant.v(constant2);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue6);
        } else if (c instanceof CONSTANT_Class_info) {
            String className = ((CONSTANT_Utf8_info) constant_pool[((CONSTANT_Class_info) c).name_index]).convert();
            Value rvalue7 = ClassConstant.v(className);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue7);
        } else {
            throw new RuntimeException("Attempting to push a non-constant cp entry" + c);
        }
        statements.add(stmt);
    }

    void generateJimple(Instruction ins, TypeStack typeStack, TypeStack postTypeStack, cp_info[] constant_pool, List<Stmt> statements, BasicBlock basicBlock) {
        Type checkType;
        Type castType;
        TypeStack pop;
        TypeStack pop2;
        TypeStack pop3;
        TypeStack pop4;
        TypeStack pop5;
        Type baseType;
        Stmt stmt = null;
        int x = ins.code & 255;
        switch (x) {
            case 0:
                stmt = Jimple.v().newNopStmt();
                break;
            case 1:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), NullConstant.v());
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), IntConstant.v(x - 3));
                break;
            case 9:
            case 10:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), LongConstant.v(x - 9));
                break;
            case 11:
            case 12:
            case 13:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), FloatConstant.v(x - 11));
                break;
            case 14:
            case 15:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), DoubleConstant.v(x - 14));
                break;
            case 16:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), IntConstant.v(((Instruction_Bipush) ins).arg_b));
                break;
            case 17:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), IntConstant.v(((Instruction_Sipush) ins).arg_i));
                break;
            case 18:
                generateJimpleForCPEntry(constant_pool, ((Instruction_Ldc1) ins).arg_b, typeStack, postTypeStack, this.jmethod, statements);
                break;
            case 19:
            case 20:
                generateJimpleForCPEntry(constant_pool, ((Instruction_intindex) ins).arg_i, typeStack, postTypeStack, this.jmethod, statements);
                break;
            case 21:
                Local local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
                break;
            case 22:
                Local local2 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local2);
                break;
            case 23:
                Local local3 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local3);
                break;
            case 24:
                Local local4 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local4);
                break;
            case 25:
                Local local5 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local5);
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                Local local6 = Util.v().getLocalForIndex(this.listBody, x - 26, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local6);
                break;
            case 30:
            case 31:
            case 32:
            case 33:
                Local local7 = Util.v().getLocalForIndex(this.listBody, x - 30, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local7);
                break;
            case 34:
            case 35:
            case 36:
            case 37:
                Local local8 = Util.v().getLocalForIndex(this.listBody, x - 34, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local8);
                break;
            case 38:
            case 39:
            case 40:
            case 41:
                Local local9 = Util.v().getLocalForIndex(this.listBody, x - 38, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local9);
                break;
            case 42:
            case 43:
            case 44:
            case 45:
                Local local10 = Util.v().getLocalForIndex(this.listBody, x - 42, ins);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local10);
                break;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
                ArrayRef a = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), a);
                break;
            case 54:
                Local local11 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(local11, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 55:
                Local local12 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(local12, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 56:
                Local local13 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(local13, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 57:
                Local local14 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(local14, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 58:
                Local local15 = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar) ins).arg_b, ins);
                stmt = Jimple.v().newAssignStmt(local15, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 59:
            case 60:
            case 61:
            case 62:
                Local local16 = Util.v().getLocalForIndex(this.listBody, x - 59, ins);
                stmt = Jimple.v().newAssignStmt(local16, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 63:
            case 64:
            case 65:
            case 66:
                Local local17 = Util.v().getLocalForIndex(this.listBody, x - 63, ins);
                stmt = Jimple.v().newAssignStmt(local17, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 67:
            case 68:
            case 69:
            case 70:
                Local local18 = Util.v().getLocalForIndex(this.listBody, x - 67, ins);
                stmt = Jimple.v().newAssignStmt(local18, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 71:
            case 72:
            case 73:
            case 74:
                Local local19 = Util.v().getLocalForIndex(this.listBody, x - 71, ins);
                stmt = Jimple.v().newAssignStmt(local19, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 75:
            case 76:
            case 77:
            case 78:
                Local local20 = Util.v().getLocalForIndex(this.listBody, x - 75, ins);
                stmt = Jimple.v().newAssignStmt(local20, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                ArrayRef a2 = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(a2, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 80:
            case 82:
                ArrayRef a3 = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2));
                stmt = Jimple.v().newAssignStmt(a3, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 87:
            case 88:
                stmt = Jimple.v().newNopStmt();
                break;
            case 89:
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 90:
                Local l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                Local l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1));
                statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2));
                statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex())));
                stmt = null;
                break;
            case 91:
                if (typeSize(typeStack.get(typeStack.topIndex() - 2)) == 2) {
                    Local l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
                    Local l12 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l12));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l12));
                    stmt = null;
                    break;
                } else {
                    Local l32 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
                    Local l22 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                    Local l13 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l13));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l22));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l32));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex())));
                    stmt = null;
                    break;
                }
            case 92:
                if (typeSize(typeStack.top()) == 2) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                    break;
                } else {
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1)));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex())));
                    stmt = null;
                    break;
                }
            case 93:
                if (typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
                    Local l23 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                    Local l33 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l23));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l33));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1)));
                    stmt = null;
                    break;
                } else {
                    Local l34 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
                    Local l24 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                    Local l14 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l14));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l24));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l34));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex())));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1)));
                    stmt = null;
                    break;
                }
            case 94:
                if (typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
                    Local l25 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l25));
                } else {
                    Local l15 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    Local l26 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l26));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l15));
                }
                if (typeSize(typeStack.get(typeStack.topIndex() - 3)) == 2) {
                    Local l4 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3);
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l4));
                } else {
                    Local l42 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3);
                    Local l35 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l42));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l35));
                }
                if (typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 5), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1)));
                } else {
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 5), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1)));
                    statements.add(Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex())));
                }
                stmt = null;
                break;
            case 95:
                TypeStack typeStack2 = typeStack.push(typeStack.top());
                Local first = Util.v().getLocalForStackOp(this.listBody, typeStack2, typeStack2.topIndex());
                typeStack2.pop();
                Local second = Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex());
                Local third = Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1);
                statements.add(Jimple.v().newAssignStmt(first, second));
                statements.add(Jimple.v().newAssignStmt(second, third));
                statements.add(Jimple.v().newAssignStmt(third, first));
                stmt = null;
                break;
            case 96:
            case 98:
                Expr rhs = Jimple.v().newAddExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
                break;
            case 97:
            case 99:
                Expr rhs2 = Jimple.v().newAddExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs2);
                break;
            case 100:
            case 102:
                Expr rhs3 = Jimple.v().newSubExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs3);
                break;
            case 101:
            case 103:
                Expr rhs4 = Jimple.v().newSubExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs4);
                break;
            case 104:
            case 106:
                Expr rhs5 = Jimple.v().newMulExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs5);
                break;
            case 105:
            case 107:
                Expr rhs6 = Jimple.v().newMulExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs6);
                break;
            case 108:
            case 110:
                Expr rhs7 = Jimple.v().newDivExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs7);
                break;
            case 109:
            case 111:
                Expr rhs8 = Jimple.v().newDivExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs8);
                break;
            case 112:
            case 114:
                Expr rhs9 = Jimple.v().newRemExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs9);
                break;
            case 113:
            case 115:
                Expr rhs10 = Jimple.v().newRemExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs10);
                break;
            case 116:
            case 117:
            case 118:
            case 119:
                Expr rhs11 = Jimple.v().newNegExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs11);
                break;
            case 120:
                Expr rhs12 = Jimple.v().newShlExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs12);
                break;
            case 121:
                Expr rhs13 = Jimple.v().newShlExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs13);
                break;
            case 122:
                Expr rhs14 = Jimple.v().newShrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs14);
                break;
            case 123:
                Expr rhs15 = Jimple.v().newShrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs15);
                break;
            case 124:
                Expr rhs16 = Jimple.v().newUshrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs16);
                break;
            case 125:
                Expr rhs17 = Jimple.v().newUshrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs17);
                break;
            case 126:
                Expr rhs18 = Jimple.v().newAndExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs18);
                break;
            case 127:
                Expr rhs19 = Jimple.v().newAndExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs19);
                break;
            case 128:
                Expr rhs20 = Jimple.v().newOrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs20);
                break;
            case 129:
                Expr rhs21 = Jimple.v().newOrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs21);
                break;
            case 130:
                Expr rhs22 = Jimple.v().newXorExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs22);
                break;
            case 131:
                Expr rhs23 = Jimple.v().newXorExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs23);
                break;
            case 132:
                Local local21 = Util.v().getLocalForIndex(this.listBody, ((Instruction_Iinc) ins).arg_b, ins);
                int amt = ((Instruction_Iinc) ins).arg_c;
                Expr rhs24 = Jimple.v().newAddExpr(local21, IntConstant.v(amt));
                stmt = Jimple.v().newAssignStmt(local21, rhs24);
                break;
            case 133:
            case 140:
            case 143:
                Expr rhs25 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), LongType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs25);
                break;
            case 134:
            case 137:
            case 144:
                Expr rhs26 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), FloatType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs26);
                break;
            case 135:
            case 138:
            case 141:
                Expr rhs27 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), DoubleType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs27);
                break;
            case 136:
            case 139:
            case 142:
                Expr rhs28 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs28);
                break;
            case 145:
                Expr rhs29 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), ByteType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs29);
                break;
            case 146:
                Expr rhs30 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), CharType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs30);
                break;
            case 147:
                Expr rhs31 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), ShortType.v());
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs31);
                break;
            case 148:
                Expr rhs32 = Jimple.v().newCmpExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs32);
                break;
            case 149:
                Expr rhs33 = Jimple.v().newCmplExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs33);
                break;
            case 150:
                Expr rhs34 = Jimple.v().newCmpgExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs34);
                break;
            case 151:
                Expr rhs35 = Jimple.v().newCmplExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs35);
                break;
            case 152:
                Expr rhs36 = Jimple.v().newCmpgExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs36);
                break;
            case 153:
                ConditionExpr co = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co, new FutureStmt());
                break;
            case 154:
                ConditionExpr co2 = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co2, new FutureStmt());
                break;
            case 155:
                ConditionExpr co3 = Jimple.v().newLtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co3, new FutureStmt());
                break;
            case 156:
                ConditionExpr co4 = Jimple.v().newGeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co4, new FutureStmt());
                break;
            case 157:
                ConditionExpr co5 = Jimple.v().newGtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co5, new FutureStmt());
                break;
            case 158:
                ConditionExpr co6 = Jimple.v().newLeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
                stmt = Jimple.v().newIfStmt(co6, new FutureStmt());
                break;
            case 159:
                ConditionExpr co7 = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co7, new FutureStmt());
                break;
            case 160:
                ConditionExpr co8 = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co8, new FutureStmt());
                break;
            case 161:
                ConditionExpr co9 = Jimple.v().newLtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co9, new FutureStmt());
                break;
            case 162:
                ConditionExpr co10 = Jimple.v().newGeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co10, new FutureStmt());
                break;
            case 163:
                ConditionExpr co11 = Jimple.v().newGtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co11, new FutureStmt());
                break;
            case 164:
                ConditionExpr co12 = Jimple.v().newLeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co12, new FutureStmt());
                break;
            case 165:
                ConditionExpr co13 = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co13, new FutureStmt());
                break;
            case 166:
                ConditionExpr co14 = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newIfStmt(co14, new FutureStmt());
                break;
            case 167:
                stmt = Jimple.v().newGotoStmt(new FutureStmt());
                break;
            case 168:
            case 201:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            default:
                throw new RuntimeException("Unrecognized bytecode instruction: " + x);
            case 169:
                Local local22 = Util.v().getLocalForIndex(this.listBody, ((Instruction_Ret) ins).arg_b, ins);
                stmt = Jimple.v().newRetStmt(local22);
                break;
            case 170:
                int lowIndex = ((Instruction_Tableswitch) ins).low;
                int highIndex = ((Instruction_Tableswitch) ins).high;
                stmt = Jimple.v().newTableSwitchStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), lowIndex, highIndex, Arrays.asList(new FutureStmt[(highIndex - lowIndex) + 1]), new FutureStmt());
                break;
            case 171:
                List<IntConstant> matches = new ArrayList<>();
                int npairs = ((Instruction_Lookupswitch) ins).npairs;
                for (int j = 0; j < npairs; j++) {
                    matches.add(IntConstant.v(((Instruction_Lookupswitch) ins).match_offsets[j * 2]));
                }
                stmt = Jimple.v().newLookupSwitchStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), matches, Arrays.asList(new FutureStmt[npairs]), new FutureStmt());
                break;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
                stmt = Jimple.v().newReturnStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 177:
                stmt = Jimple.v().newReturnVoidStmt();
                break;
            case 178:
                CONSTANT_Fieldref_info fieldInfo = (CONSTANT_Fieldref_info) constant_pool[((Instruction_Getstatic) ins).arg_i];
                CONSTANT_Class_info c = (CONSTANT_Class_info) constant_pool[fieldInfo.class_index];
                String className = ((CONSTANT_Utf8_info) constant_pool[c.name_index]).convert().replace('/', '.');
                CONSTANT_NameAndType_info i = (CONSTANT_NameAndType_info) constant_pool[fieldInfo.name_and_type_index];
                String fieldName = ((CONSTANT_Utf8_info) constant_pool[i.name_index]).convert();
                String fieldDescriptor = ((CONSTANT_Utf8_info) constant_pool[i.descriptor_index]).convert();
                Type fieldType = Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor);
                SootClass bclass = this.cm.getSootClass(className);
                SootFieldRef fieldRef = Scene.v().makeFieldRef(bclass, fieldName, fieldType, true);
                StaticFieldRef fr = Jimple.v().newStaticFieldRef(fieldRef);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), fr);
                break;
            case 179:
                CONSTANT_Fieldref_info fieldInfo2 = (CONSTANT_Fieldref_info) constant_pool[((Instruction_Putstatic) ins).arg_i];
                CONSTANT_Class_info c2 = (CONSTANT_Class_info) constant_pool[fieldInfo2.class_index];
                String className2 = ((CONSTANT_Utf8_info) constant_pool[c2.name_index]).convert().replace('/', '.');
                CONSTANT_NameAndType_info i2 = (CONSTANT_NameAndType_info) constant_pool[fieldInfo2.name_and_type_index];
                String fieldName2 = ((CONSTANT_Utf8_info) constant_pool[i2.name_index]).convert();
                String fieldDescriptor2 = ((CONSTANT_Utf8_info) constant_pool[i2.descriptor_index]).convert();
                Type fieldType2 = Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor2);
                SootClass bclass2 = this.cm.getSootClass(className2);
                SootFieldRef fieldRef2 = Scene.v().makeFieldRef(bclass2, fieldName2, fieldType2, true);
                StaticFieldRef fr2 = Jimple.v().newStaticFieldRef(fieldRef2);
                stmt = Jimple.v().newAssignStmt(fr2, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 180:
                CONSTANT_Fieldref_info fieldInfo3 = (CONSTANT_Fieldref_info) constant_pool[((Instruction_Getfield) ins).arg_i];
                CONSTANT_Class_info c3 = (CONSTANT_Class_info) constant_pool[fieldInfo3.class_index];
                String className3 = ((CONSTANT_Utf8_info) constant_pool[c3.name_index]).convert().replace('/', '.');
                CONSTANT_NameAndType_info i3 = (CONSTANT_NameAndType_info) constant_pool[fieldInfo3.name_and_type_index];
                String fieldName3 = ((CONSTANT_Utf8_info) constant_pool[i3.name_index]).convert();
                String fieldDescriptor3 = ((CONSTANT_Utf8_info) constant_pool[i3.descriptor_index]).convert();
                if (className3.charAt(0) == '[') {
                    className3 = JavaBasicTypes.JAVA_LANG_OBJECT;
                }
                SootClass bclass3 = this.cm.getSootClass(className3);
                Type fieldType3 = Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor3);
                SootFieldRef fieldRef3 = Scene.v().makeFieldRef(bclass3, fieldName3, fieldType3, false);
                InstanceFieldRef fr3 = Jimple.v().newInstanceFieldRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), fieldRef3);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), fr3);
                break;
            case 181:
                CONSTANT_Fieldref_info fieldInfo4 = (CONSTANT_Fieldref_info) constant_pool[((Instruction_Putfield) ins).arg_i];
                CONSTANT_Class_info c4 = (CONSTANT_Class_info) constant_pool[fieldInfo4.class_index];
                String className4 = ((CONSTANT_Utf8_info) constant_pool[c4.name_index]).convert().replace('/', '.');
                CONSTANT_NameAndType_info i4 = (CONSTANT_NameAndType_info) constant_pool[fieldInfo4.name_and_type_index];
                String fieldName4 = ((CONSTANT_Utf8_info) constant_pool[i4.name_index]).convert();
                String fieldDescriptor4 = ((CONSTANT_Utf8_info) constant_pool[i4.descriptor_index]).convert();
                Type fieldType4 = Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor4);
                SootClass bclass4 = this.cm.getSootClass(className4);
                SootFieldRef fieldRef4 = Scene.v().makeFieldRef(bclass4, fieldName4, fieldType4, false);
                InstanceFieldRef fr4 = Jimple.v().newInstanceFieldRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - typeSize(typeStack.top())), fieldRef4);
                stmt = Jimple.v().newAssignStmt(fr4, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 182:
                Instruction_Invokevirtual iv = (Instruction_Invokevirtual) ins;
                int args = cp_info.countParams(constant_pool, iv.arg_i);
                CONSTANT_Methodref_info methodInfo = (CONSTANT_Methodref_info) constant_pool[iv.arg_i];
                SootMethodRef methodRef = createMethodRef(constant_pool, methodInfo, false);
                Type returnType = methodRef.returnType();
                Value[] params = new Value[args];
                for (int j2 = args - 1; j2 >= 0; j2--) {
                    params[j2] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    if (typeSize(typeStack.top()) == 2) {
                        pop4 = typeStack.pop().pop();
                    } else {
                        pop4 = typeStack.pop();
                    }
                    typeStack = pop4;
                }
                Value rvalue = Jimple.v().newVirtualInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), methodRef, Arrays.asList(params));
                if (!returnType.equals(VoidType.v())) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
                    break;
                } else {
                    stmt = Jimple.v().newInvokeStmt(rvalue);
                    break;
                }
            case 183:
                Instruction_Invokenonvirtual iv2 = (Instruction_Invokenonvirtual) ins;
                int args2 = cp_info.countParams(constant_pool, iv2.arg_i);
                CONSTANT_Methodref_info methodInfo2 = (CONSTANT_Methodref_info) constant_pool[iv2.arg_i];
                SootMethodRef methodRef2 = createMethodRef(constant_pool, methodInfo2, false);
                Type returnType2 = methodRef2.returnType();
                Value[] params2 = new Value[args2];
                for (int j3 = args2 - 1; j3 >= 0; j3--) {
                    params2[j3] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    if (typeSize(typeStack.top()) == 2) {
                        pop3 = typeStack.pop().pop();
                    } else {
                        pop3 = typeStack.pop();
                    }
                    typeStack = pop3;
                }
                Value rvalue2 = Jimple.v().newSpecialInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), methodRef2, Arrays.asList(params2));
                if (!returnType2.equals(VoidType.v())) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue2);
                    break;
                } else {
                    stmt = Jimple.v().newInvokeStmt(rvalue2);
                    break;
                }
            case 184:
                Instruction_Invokestatic is = (Instruction_Invokestatic) ins;
                int args3 = cp_info.countParams(constant_pool, is.arg_i);
                CONSTANT_Methodref_info methodInfo3 = (CONSTANT_Methodref_info) constant_pool[is.arg_i];
                SootMethodRef methodRef3 = createMethodRef(constant_pool, methodInfo3, true);
                Type returnType3 = methodRef3.returnType();
                Value[] params3 = new Value[args3];
                for (int j4 = args3 - 1; j4 >= 0; j4--) {
                    params3[j4] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    if (typeSize(typeStack.top()) == 2) {
                        pop2 = typeStack.pop().pop();
                    } else {
                        pop2 = typeStack.pop();
                    }
                    typeStack = pop2;
                }
                Value rvalue3 = Jimple.v().newStaticInvokeExpr(methodRef3, Arrays.asList(params3));
                if (!returnType3.equals(VoidType.v())) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue3);
                    break;
                } else {
                    stmt = Jimple.v().newInvokeStmt(rvalue3);
                    break;
                }
            case 185:
                Instruction_Invokeinterface ii = (Instruction_Invokeinterface) ins;
                int args4 = cp_info.countParams(constant_pool, ii.arg_i);
                CONSTANT_InterfaceMethodref_info methodInfo4 = (CONSTANT_InterfaceMethodref_info) constant_pool[ii.arg_i];
                SootMethodRef methodRef4 = createMethodRef(constant_pool, methodInfo4, false);
                Type returnType4 = methodRef4.returnType();
                Value[] params4 = new Value[args4];
                for (int j5 = args4 - 1; j5 >= 0; j5--) {
                    params4[j5] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    if (typeSize(typeStack.top()) == 2) {
                        pop = typeStack.pop().pop();
                    } else {
                        pop = typeStack.pop();
                    }
                    typeStack = pop;
                }
                Value rvalue4 = Jimple.v().newInterfaceInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), methodRef4, Arrays.asList(params4));
                if (!returnType4.equals(VoidType.v())) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue4);
                    break;
                } else {
                    stmt = Jimple.v().newInvokeStmt(rvalue4);
                    break;
                }
            case 186:
                CONSTANT_InvokeDynamic_info iv_info = (CONSTANT_InvokeDynamic_info) constant_pool[((Instruction_Invokedynamic) ins).invoke_dynamic_index];
                int args5 = cp_info.countParams(constant_pool, iv_info.name_and_type_index);
                List<Value> bootstrapArgs = new LinkedList<>();
                short[] bootstrapMethodTable = this.bootstrap_methods_attribute.method_handles;
                short methodSigIndex = bootstrapMethodTable[iv_info.bootstrap_method_index];
                CONSTANT_MethodHandle_info mhInfo = (CONSTANT_MethodHandle_info) constant_pool[methodSigIndex];
                CONSTANT_Methodref_info bsmInfo = (CONSTANT_Methodref_info) constant_pool[mhInfo.target_index];
                SootMethodRef bootstrapMethodRef = createMethodRef(constant_pool, bsmInfo, false);
                int kind = mhInfo.kind;
                short[] bsmArgIndices = this.bootstrap_methods_attribute.arg_indices[iv_info.bootstrap_method_index];
                if (bsmArgIndices.length > 0) {
                    for (short bsmArgIndex : bsmArgIndices) {
                        cp_info cpEntry = constant_pool[bsmArgIndex];
                        Value val = cpEntry.createJimpleConstantValue(constant_pool);
                        bootstrapArgs.add(val);
                    }
                }
                CONSTANT_NameAndType_info nameAndTypeInfo = (CONSTANT_NameAndType_info) constant_pool[iv_info.name_and_type_index];
                String methodName = ((CONSTANT_Utf8_info) constant_pool[nameAndTypeInfo.name_index]).convert();
                String methodDescriptor = ((CONSTANT_Utf8_info) constant_pool[nameAndTypeInfo.descriptor_index]).convert();
                SootClass bclass5 = this.cm.getSootClass(SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME);
                Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
                List<Type> parameterTypes = new ArrayList<>();
                for (int k = 0; k < types.length - 1; k++) {
                    parameterTypes.add(types[k]);
                }
                Type returnType5 = types[types.length - 1];
                SootMethodRef methodRef5 = Scene.v().makeMethodRef(bclass5, methodName, parameterTypes, returnType5, true);
                Value[] params5 = new Value[args5];
                for (int j6 = args5 - 1; j6 >= 0; j6--) {
                    params5[j6] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
                    if (typeSize(typeStack.top()) == 2) {
                        pop5 = typeStack.pop().pop();
                    } else {
                        pop5 = typeStack.pop();
                    }
                    typeStack = pop5;
                }
                Value rvalue5 = Jimple.v().newDynamicInvokeExpr(bootstrapMethodRef, bootstrapArgs, methodRef5, kind, Arrays.asList(params5));
                if (!returnType5.equals(VoidType.v())) {
                    stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue5);
                    break;
                } else {
                    stmt = Jimple.v().newInvokeStmt(rvalue5);
                    break;
                }
            case 187:
                SootClass bclass6 = this.cm.getSootClass(getClassName(constant_pool, ((Instruction_New) ins).arg_i));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Jimple.v().newNewExpr(RefType.v(bclass6.getName())));
                break;
            case 188:
                Type baseType2 = jimpleTypeOfAtype(((Instruction_Newarray) ins).atype);
                Expr rhs37 = Jimple.v().newNewArrayExpr(baseType2, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs37);
                break;
            case 189:
                String baseName = getClassName(constant_pool, ((Instruction_Anewarray) ins).arg_i);
                if (baseName.startsWith("[")) {
                    baseType = Util.v().jimpleTypeOfFieldDescriptor(getClassName(constant_pool, ((Instruction_Anewarray) ins).arg_i));
                } else {
                    baseType = RefType.v(baseName);
                }
                Expr rhs38 = Jimple.v().newNewArrayExpr(baseType, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs38);
                break;
            case 190:
                Expr rhs39 = Jimple.v().newLengthExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs39);
                break;
            case 191:
                stmt = Jimple.v().newThrowStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 192:
                String className5 = getClassName(constant_pool, ((Instruction_Checkcast) ins).arg_i);
                if (className5.startsWith("[")) {
                    castType = Util.v().jimpleTypeOfFieldDescriptor(getClassName(constant_pool, ((Instruction_Checkcast) ins).arg_i));
                } else {
                    castType = RefType.v(className5);
                }
                Expr rhs40 = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), castType);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs40);
                break;
            case 193:
                String className6 = getClassName(constant_pool, ((Instruction_Instanceof) ins).arg_i);
                if (className6.startsWith("[")) {
                    checkType = Util.v().jimpleTypeOfFieldDescriptor(getClassName(constant_pool, ((Instruction_Instanceof) ins).arg_i));
                } else {
                    checkType = RefType.v(className6);
                }
                Expr rhs41 = Jimple.v().newInstanceOfExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), checkType);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs41);
                break;
            case 194:
                stmt = Jimple.v().newEnterMonitorStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 195:
                stmt = Jimple.v().newExitMonitorStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
                break;
            case 196:
                throw new RuntimeException("WIDE instruction should not be encountered anymore");
            case 197:
                int bdims = ((Instruction_Multianewarray) ins).dims;
                List<Value> dims = new ArrayList<>();
                for (int j7 = 0; j7 < bdims; j7++) {
                    dims.add(Util.v().getLocalForStackOp(this.listBody, typeStack, (typeStack.topIndex() - bdims) + j7 + 1));
                }
                String mstype = constant_pool[((Instruction_Multianewarray) ins).arg_i].toString(constant_pool);
                ArrayType jimpleType = (ArrayType) Util.v().jimpleTypeOfFieldDescriptor(mstype);
                Expr rhs42 = Jimple.v().newNewMultiArrayExpr(jimpleType, dims);
                stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs42);
                break;
            case 198:
                ConditionExpr co15 = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), NullConstant.v());
                stmt = Jimple.v().newIfStmt(co15, new FutureStmt());
                break;
            case 199:
                ConditionExpr co16 = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), NullConstant.v());
                stmt = Jimple.v().newIfStmt(co16, new FutureStmt());
                break;
            case 200:
                stmt = Jimple.v().newGotoStmt(new FutureStmt());
                break;
            case 202:
                stmt = Jimple.v().newBreakpointStmt();
                break;
            case 209:
                Local local23 = Util.v().getLocalForIndex(this.listBody, ((Instruction_Ret_w) ins).arg_i, ins);
                stmt = Jimple.v().newRetStmt(local23);
                break;
        }
        if (stmt != null) {
            if (Options.v().keep_offset()) {
                stmt.addTag(new BytecodeOffsetTag(ins.label));
            }
            statements.add(stmt);
        }
    }

    private SootMethodRef createMethodRef(cp_info[] constant_pool, ICONSTANT_Methodref_info methodInfo, boolean isStatic) {
        CONSTANT_Class_info c = (CONSTANT_Class_info) constant_pool[methodInfo.getClassIndex()];
        String className = ((CONSTANT_Utf8_info) constant_pool[c.name_index]).convert().replace('/', '.');
        CONSTANT_NameAndType_info i = (CONSTANT_NameAndType_info) constant_pool[methodInfo.getNameAndTypeIndex()];
        String methodName = ((CONSTANT_Utf8_info) constant_pool[i.name_index]).convert();
        String methodDescriptor = ((CONSTANT_Utf8_info) constant_pool[i.descriptor_index]).convert();
        if (className.charAt(0) == '[') {
            className = JavaBasicTypes.JAVA_LANG_OBJECT;
        }
        SootClass bclass = this.cm.getSootClass(className);
        Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
        List<Type> parameterTypes = new ArrayList<>();
        for (int k = 0; k < types.length - 1; k++) {
            parameterTypes.add(types[k]);
        }
        Type returnType = types[types.length - 1];
        SootMethodRef methodRef = Scene.v().makeMethodRef(bclass, methodName, parameterTypes, returnType, isStatic);
        return methodRef;
    }

    Type jimpleTypeOfAtype(int atype) {
        switch (atype) {
            case 4:
                return BooleanType.v();
            case 5:
                return CharType.v();
            case 6:
                return FloatType.v();
            case 7:
                return DoubleType.v();
            case 8:
                return ByteType.v();
            case 9:
                return ShortType.v();
            case 10:
                return IntType.v();
            case 11:
                return LongType.v();
            default:
                throw new RuntimeException("Undefined 'atype' in NEWARRAY byte instruction");
        }
    }

    int typeSize(Type type) {
        if (type.equals(LongType.v()) || type.equals(DoubleType.v()) || type.equals(Long2ndHalfType.v()) || type.equals(Double2ndHalfType.v())) {
            return 2;
        }
        return 1;
    }
}
