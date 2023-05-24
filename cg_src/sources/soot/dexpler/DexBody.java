package soot.dexpler;

import com.google.common.collect.ArrayListMultimap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.ClassPathResolver;
import org.jf.dexlib2.analysis.ClassProvider;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.immutable.debug.ImmutableEndLocal;
import org.jf.dexlib2.immutable.debug.ImmutableLineNumber;
import org.jf.dexlib2.immutable.debug.ImmutableRestartLocal;
import org.jf.dexlib2.immutable.debug.ImmutableStartLocal;
import org.jf.dexlib2.util.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.PackManager;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.UnknownType;
import soot.dexpler.instructions.DanglingInstruction;
import soot.dexpler.instructions.DeferableInstruction;
import soot.dexpler.instructions.DexlibAbstractInstruction;
import soot.dexpler.instructions.InstructionFactory;
import soot.dexpler.instructions.MoveExceptionInstruction;
import soot.dexpler.instructions.OdexInstruction;
import soot.dexpler.instructions.PseudoInstruction;
import soot.dexpler.instructions.RetypeableInstruction;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantCastEliminator;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.FieldStaticnessCorrector;
import soot.jimple.toolkits.scalar.IdentityCastEliminator;
import soot.jimple.toolkits.scalar.IdentityOperationEliminator;
import soot.jimple.toolkits.scalar.MethodStaticnessCorrector;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.JBOptions;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLineNumberTag;
import soot.toolkits.exceptions.TrapTightener;
import soot.toolkits.scalar.LocalPacker;
import soot.toolkits.scalar.LocalSplitter;
import soot.toolkits.scalar.UnusedLocalEliminator;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexBody.class */
public class DexBody {
    private static final Logger logger = LoggerFactory.getLogger(DexBody.class);
    protected List<DexlibAbstractInstruction> instructions;
    protected Local[] registerLocals;
    protected Local storeResultLocal;
    protected Map<Integer, DexlibAbstractInstruction> instructionAtAddress;
    protected List<DeferableInstruction> deferredInstructions;
    protected Set<RetypeableInstruction> instructionsToRetype;
    protected DanglingInstruction dangling;
    protected int numRegisters;
    protected int numParameterRegisters;
    protected final List<Type> parameterTypes;
    protected final List<String> parameterNames;
    protected boolean isStatic;
    protected JimpleBody jBody;
    protected List<? extends TryBlock<? extends ExceptionHandler>> tries;
    protected RefType declaringClassType;
    protected final MultiDexContainer.DexEntry<? extends DexFile> dexEntry;
    protected final Method method;
    private final ArrayListMultimap<Integer, RegDbgEntry> localDebugs;
    protected Set<String> takenLocalNames;
    protected List<PseudoInstruction> pseudoInstructionData = new ArrayList();
    private LocalSplitter localSplitter = null;
    private UnreachableCodeEliminator unreachableCodeEliminator = null;
    private CopyPropagator copyPropagator = null;

    /* loaded from: gencallgraphv3.jar:soot/dexpler/DexBody$RegDbgEntry.class */
    protected class RegDbgEntry {
        public int startAddress;
        public int endAddress;
        public int register;
        public String name;
        public Type type;
        public String signature;

        public RegDbgEntry(int sa, int ea, int reg, String nam, String ty, String sig) {
            this.startAddress = sa;
            this.endAddress = ea;
            this.register = reg;
            this.name = nam;
            this.type = DexType.toSoot(ty);
            this.signature = sig;
        }
    }

    PseudoInstruction isAddressInData(int a) {
        for (PseudoInstruction pi : this.pseudoInstructionData) {
            int fb = pi.getDataFirstByte();
            int lb = pi.getDataLastByte();
            if (fb <= a && a <= lb) {
                return pi;
            }
        }
        return null;
    }

    protected String freshLocalName(String hint) {
        String fresh;
        hint = (hint == null || hint.equals("")) ? "$local" : "$local";
        if (!this.takenLocalNames.contains(hint)) {
            fresh = hint;
        } else {
            int i = 1;
            while (true) {
                fresh = String.valueOf(hint) + Integer.toString(i);
                if (!this.takenLocalNames.contains(fresh)) {
                    break;
                }
                i++;
            }
        }
        this.takenLocalNames.add(fresh);
        return fresh;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DexBody(MultiDexContainer.DexEntry<? extends DexFile> dexFile, Method method, RefType declaringClassType) {
        int reg;
        int codeAddr;
        String name;
        String type;
        String signature;
        MethodImplementation code = method.getImplementation();
        if (code == null) {
            throw new RuntimeException("error: no code for method " + method.getName());
        }
        this.declaringClassType = declaringClassType;
        this.tries = code.getTryBlocks();
        List<? extends MethodParameter> parameters = method.getParameters();
        if (parameters != null) {
            this.parameterNames = new ArrayList();
            this.parameterTypes = new ArrayList();
            for (MethodParameter param : method.getParameters()) {
                this.parameterNames.add(param.getName());
                this.parameterTypes.add(DexType.toSoot(param.getType()));
            }
        } else {
            this.parameterNames = Collections.emptyList();
            this.parameterTypes = Collections.emptyList();
        }
        this.isStatic = Modifier.isStatic(method.getAccessFlags());
        this.numRegisters = code.getRegisterCount();
        this.numParameterRegisters = MethodUtil.getParameterRegisterCount(method);
        if (!this.isStatic) {
            this.numParameterRegisters--;
        }
        this.instructions = new ArrayList();
        this.instructionAtAddress = new HashMap();
        this.localDebugs = ArrayListMultimap.create();
        this.takenLocalNames = new HashSet();
        this.registerLocals = new Local[this.numRegisters];
        extractDexInstructions(code);
        if (this.numParameterRegisters > this.numRegisters) {
            throw new RuntimeException("Malformed dex file: insSize (" + this.numParameterRegisters + ") > registersSize (" + this.numRegisters + ")");
        }
        for (DebugItem di : code.getDebugItems()) {
            if (di instanceof ImmutableLineNumber) {
                ImmutableLineNumber ln = (ImmutableLineNumber) di;
                DexlibAbstractInstruction ins = instructionAtAddress(ln.getCodeAddress());
                if (ins != null) {
                    ins.setLineNumber(ln.getLineNumber());
                }
            } else if ((di instanceof ImmutableStartLocal) || (di instanceof ImmutableRestartLocal)) {
                if (di instanceof ImmutableStartLocal) {
                    ImmutableStartLocal sl = (ImmutableStartLocal) di;
                    reg = sl.getRegister();
                    codeAddr = sl.getCodeAddress();
                    name = sl.getName();
                    type = sl.getType();
                    signature = sl.getSignature();
                } else {
                    ImmutableRestartLocal sl2 = (ImmutableRestartLocal) di;
                    reg = sl2.getRegister();
                    codeAddr = sl2.getCodeAddress();
                    name = sl2.getName();
                    type = sl2.getType();
                    signature = sl2.getSignature();
                }
                if (name != null && type != null) {
                    this.localDebugs.put(Integer.valueOf(reg), new RegDbgEntry(codeAddr, -1, reg, name, type, signature));
                }
            } else if (di instanceof ImmutableEndLocal) {
                ImmutableEndLocal el = (ImmutableEndLocal) di;
                List<RegDbgEntry> lds = this.localDebugs.get((Object) Integer.valueOf(el.getRegister()));
                if (lds != null && !lds.isEmpty()) {
                    lds.get(lds.size() - 1).endAddress = el.getCodeAddress();
                }
            }
        }
        this.dexEntry = dexFile;
        this.method = method;
    }

    protected void extractDexInstructions(MethodImplementation code) {
        int address = 0;
        for (Instruction instruction : code.getInstructions()) {
            DexlibAbstractInstruction dexInstruction = InstructionFactory.fromInstruction(instruction, address);
            this.instructions.add(dexInstruction);
            this.instructionAtAddress.put(Integer.valueOf(address), dexInstruction);
            address += instruction.getCodeUnits();
        }
    }

    public Set<Type> usedTypes() {
        Set<Type> types = new HashSet<>();
        for (DexlibAbstractInstruction i : this.instructions) {
            types.addAll(i.introducedTypes());
        }
        if (this.tries != null) {
            for (TryBlock<? extends ExceptionHandler> tryItem : this.tries) {
                Iterator<? extends Object> it = tryItem.getExceptionHandlers().iterator();
                while (it.hasNext()) {
                    ExceptionHandler handler = (ExceptionHandler) it.next();
                    String exType = handler.getExceptionType();
                    if (exType != null) {
                        types.add(DexType.toSoot(exType));
                    }
                }
            }
        }
        return types;
    }

    public void add(Unit u) {
        getBody().getUnits().add((UnitPatchingChain) u);
    }

    public void addDeferredJimplification(DeferableInstruction i) {
        this.deferredInstructions.add(i);
    }

    public void addRetype(RetypeableInstruction i) {
        this.instructionsToRetype.add(i);
    }

    public Body getBody() {
        if (this.jBody == null) {
            throw new RuntimeException("No jimplification happened yet, no body available.");
        }
        return this.jBody;
    }

    public Local[] getRegisterLocals() {
        return this.registerLocals;
    }

    public Local getRegisterLocal(int num) throws InvalidDalvikBytecodeException {
        int totalRegisters = this.registerLocals.length;
        if (num > totalRegisters) {
            throw new InvalidDalvikBytecodeException("Trying to access register " + num + " but only " + totalRegisters + " is/are available.");
        }
        return this.registerLocals[num];
    }

    public Local getStoreResultLocal() {
        return this.storeResultLocal;
    }

    public DexlibAbstractInstruction instructionAtAddress(int address) {
        DexlibAbstractInstruction i = null;
        while (i == null && address >= 0) {
            i = this.instructionAtAddress.get(Integer.valueOf(address));
            address--;
        }
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Body jimplify(Body b, SootMethod m) {
        String str;
        String localName;
        String name;
        Jimple jimple = Jimple.v();
        Type unknownType = UnknownType.v();
        NullConstant nullConstant = NullConstant.v();
        Options options = Options.v();
        JBOptions jbOptions = new JBOptions(PhaseOptions.v().getPhaseOptions("jb"));
        this.jBody = (JimpleBody) b;
        this.deferredInstructions = new ArrayList();
        this.instructionsToRetype = new HashSet();
        if (jbOptions.use_original_names()) {
            PhaseOptions.v().setPhaseOptionIfUnset("jb.lns", "only-stack-locals");
        }
        if (jbOptions.stabilize_local_names()) {
            PhaseOptions.v().setPhaseOption("jb.lns", "sort-locals:true");
        }
        List<Local> paramLocals = new LinkedList<>();
        if (!this.isStatic) {
            int thisRegister = (this.numRegisters - this.numParameterRegisters) - 1;
            Local thisLocal = jimple.newLocal(freshLocalName("this"), unknownType);
            this.jBody.getLocals().add(thisLocal);
            this.registerLocals[thisRegister] = thisLocal;
            JIdentityStmt idStmt = (JIdentityStmt) jimple.newIdentityStmt(thisLocal, jimple.newThisRef(this.declaringClassType));
            add(idStmt);
            paramLocals.add(thisLocal);
        }
        int i = 0;
        int argIdx = 0;
        int parameterRegister = this.numRegisters - this.numParameterRegisters;
        for (Type t : this.parameterTypes) {
            String localName2 = null;
            Type localType = null;
            if (jbOptions.use_original_names()) {
                try {
                    localName2 = this.parameterNames.get(argIdx);
                    localType = this.parameterTypes.get(argIdx);
                } catch (Exception ex) {
                    logger.error("Exception while reading original parameter names.", (Throwable) ex);
                }
            }
            if (localName2 == null && this.localDebugs.containsKey(Integer.valueOf(parameterRegister))) {
                localName = ((RegDbgEntry) this.localDebugs.get((Object) Integer.valueOf(parameterRegister)).get(0)).name;
            } else {
                localName = "$u" + parameterRegister;
            }
            if (localType == null) {
                localType = unknownType;
            }
            Local gen = jimple.newLocal(freshLocalName(localName), localType);
            this.jBody.getLocals().add(gen);
            this.registerLocals[parameterRegister] = gen;
            int i2 = i;
            i++;
            JIdentityStmt idStmt2 = (JIdentityStmt) jimple.newIdentityStmt(gen, jimple.newParameterRef(t, i2));
            add(idStmt2);
            paramLocals.add(gen);
            if ((t instanceof LongType) || (t instanceof DoubleType)) {
                parameterRegister++;
                if (this.localDebugs.containsKey(Integer.valueOf(parameterRegister))) {
                    name = ((RegDbgEntry) this.localDebugs.get((Object) Integer.valueOf(parameterRegister)).get(0)).name;
                } else {
                    name = "$u" + parameterRegister;
                }
                Local g = jimple.newLocal(freshLocalName(name), unknownType);
                this.jBody.getLocals().add(g);
                this.registerLocals[parameterRegister] = g;
            }
            parameterRegister++;
            argIdx++;
        }
        int i3 = 0;
        while (true) {
            if (i3 >= (this.numRegisters - this.numParameterRegisters) - (this.isStatic ? 0 : 1)) {
                break;
            }
            if (this.localDebugs.containsKey(Integer.valueOf(i3))) {
                str = ((RegDbgEntry) this.localDebugs.get((Object) Integer.valueOf(i3)).get(0)).name;
            } else {
                str = "$u" + i3;
            }
            String name2 = str;
            this.registerLocals[i3] = jimple.newLocal(freshLocalName(name2), unknownType);
            this.jBody.getLocals().add(this.registerLocals[i3]);
            i3++;
        }
        this.storeResultLocal = jimple.newLocal(freshLocalName("$u-1"), unknownType);
        this.jBody.getLocals().add(this.storeResultLocal);
        DexFile dexFile = this.dexEntry.getDexFile();
        boolean isOdex = dexFile instanceof DexBackedDexFile ? ((DexBackedDexFile) dexFile).supportsOptimizedOpcodes() : false;
        ClassPath cp = null;
        if (isOdex) {
            String[] sootClasspath = options.soot_classpath().split(File.pathSeparator);
            List<String> classpathList = new ArrayList<>();
            for (String str2 : sootClasspath) {
                classpathList.add(str2);
            }
            try {
                ClassPathResolver resolver = new ClassPathResolver(classpathList, classpathList, classpathList, this.dexEntry);
                cp = new ClassPath((ClassProvider[]) resolver.getResolvedClassProviders().toArray(new ClassProvider[0]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int prevLineNumber = -1;
        for (DexlibAbstractInstruction instruction : this.instructions) {
            if (isOdex && (instruction instanceof OdexInstruction)) {
                ((OdexInstruction) instruction).deOdex(dexFile, this.method, cp);
            }
            if (this.dangling != null) {
                this.dangling.finalize(this, instruction);
                this.dangling = null;
            }
            if (instruction.getLineNumber() > 0) {
                prevLineNumber = instruction.getLineNumber();
            } else {
                instruction.setLineNumber(prevLineNumber);
            }
            instruction.jimplify(this);
        }
        if (this.dangling != null) {
            this.dangling.finalize(this, null);
        }
        for (DeferableInstruction instruction2 : this.deferredInstructions) {
            instruction2.deferredJimplify(this);
        }
        if (this.tries != null) {
            addTraps();
        }
        if (options.keep_line_number()) {
            fixLineNumbers();
        }
        this.instructions = null;
        this.instructionAtAddress.clear();
        this.deferredInstructions = null;
        this.dangling = null;
        this.tries = null;
        this.parameterNames.clear();
        DexTrapStackFixer.v().transform(this.jBody);
        DexJumpChainShortener.v().transform(this.jBody);
        DexReturnInliner.v().transform(this.jBody);
        DexArrayInitReducer.v().transform(this.jBody);
        getLocalSplitter().transform(this.jBody);
        getUnreachableCodeEliminator().transform(this.jBody);
        DeadAssignmentEliminator.v().transform(this.jBody);
        UnusedLocalEliminator.v().transform(this.jBody);
        for (RetypeableInstruction i4 : this.instructionsToRetype) {
            i4.retype(this.jBody);
        }
        DexNumTransformer.v().transform(this.jBody);
        DexReturnValuePropagator.v().transform(this.jBody);
        getCopyPopagator().transform(this.jBody);
        DexNullThrowTransformer.v().transform(this.jBody);
        DexNullTransformer.v().transform(this.jBody);
        DexIfTransformer.v().transform(this.jBody);
        DeadAssignmentEliminator.v().transform(this.jBody);
        UnusedLocalEliminator.v().transform(this.jBody);
        DexNullArrayRefTransformer.v().transform(this.jBody);
        DexNullInstanceofTransformer.v().transform(this.jBody);
        DexNullIfTransformer ni = DexNullIfTransformer.v();
        ni.transform(this.jBody);
        if (ni.hasModifiedBody()) {
            ConditionalBranchFolder.v().transform(this.jBody);
            UnreachableCodeEliminator.v().transform(this.jBody);
            DeadAssignmentEliminator.v().transform(this.jBody);
            UnconditionalBranchFolder.v().transform(this.jBody);
        }
        TypeAssigner.v().transform(this.jBody);
        RefType objectType = RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT);
        LocalPacker.v().transform(this.jBody);
        UnusedLocalEliminator.v().transform(this.jBody);
        PackManager.v().getTransform("jb.lns").apply(this.jBody);
        if (Options.v().wrong_staticness() == 3 || Options.v().wrong_staticness() == 4) {
            FieldStaticnessCorrector.v().transform(this.jBody);
            MethodStaticnessCorrector.v().transform(this.jBody);
        }
        TrapTightener.v().transform(this.jBody);
        TrapMinimizer.v().transform(this.jBody);
        Aggregator.v().transform(this.jBody);
        ConditionalBranchFolder.v().transform(this.jBody);
        ConstantCastEliminator.v().transform(this.jBody);
        IdentityCastEliminator.v().transform(this.jBody);
        IdentityOperationEliminator.v().transform(this.jBody);
        UnreachableCodeEliminator.v().transform(this.jBody);
        DeadAssignmentEliminator.v().transform(this.jBody);
        UnusedLocalEliminator.v().transform(this.jBody);
        NopEliminator.v().transform(this.jBody);
        DexReturnPacker.v().transform(this.jBody);
        Iterator<Unit> it = this.jBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt ass = (AssignStmt) u;
                if (ass.getRightOp() instanceof CastExpr) {
                    CastExpr c = (CastExpr) ass.getRightOp();
                    if (c.getType() instanceof NullType) {
                        ass.setRightOp(nullConstant);
                    }
                }
            }
            if (u instanceof DefinitionStmt) {
                DefinitionStmt def = (DefinitionStmt) u;
                if ((def.getLeftOp() instanceof Local) && (def.getRightOp() instanceof CaughtExceptionRef)) {
                    Type t2 = def.getLeftOp().getType();
                    if (t2 instanceof RefType) {
                        RefType rt = (RefType) t2;
                        if (rt.getSootClass().isPhantom() && !rt.getSootClass().hasSuperclass() && !rt.getSootClass().getName().equals("java.lang.Throwable")) {
                            rt.getSootClass().setSuperclass(Scene.v().getSootClass("java.lang.Throwable"));
                        }
                    }
                }
            }
        }
        for (Local l : this.jBody.getLocals()) {
            if (l.getType() instanceof NullType) {
                l.setType(objectType);
            }
        }
        PackManager.v().getTransform("jb.lns").apply(this.jBody);
        return this.jBody;
    }

    protected void fixLineNumbers() {
        int prevLn = -1;
        for (DexlibAbstractInstruction instruction : this.instructions) {
            Unit unit = instruction.getUnit();
            int lineNumber = unit.getJavaSourceStartLineNumber();
            if (lineNumber < 0) {
                if (prevLn >= 0) {
                    unit.addTag(new LineNumberTag(prevLn));
                    unit.addTag(new SourceLineNumberTag(prevLn));
                }
            } else {
                prevLn = lineNumber;
            }
        }
    }

    protected LocalSplitter getLocalSplitter() {
        if (this.localSplitter == null) {
            this.localSplitter = new LocalSplitter(DalvikThrowAnalysis.v());
        }
        return this.localSplitter;
    }

    protected UnreachableCodeEliminator getUnreachableCodeEliminator() {
        if (this.unreachableCodeEliminator == null) {
            this.unreachableCodeEliminator = new UnreachableCodeEliminator(DalvikThrowAnalysis.v());
        }
        return this.unreachableCodeEliminator;
    }

    protected CopyPropagator getCopyPopagator() {
        if (this.copyPropagator == null) {
            this.copyPropagator = new CopyPropagator(DalvikThrowAnalysis.v(), false);
        }
        return this.copyPropagator;
    }

    public void setDanglingInstruction(DanglingInstruction i) {
        this.dangling = i;
    }

    public List<DexlibAbstractInstruction> instructionsAfter(DexlibAbstractInstruction instruction) {
        int i = this.instructions.indexOf(instruction);
        if (i == -1) {
            throw new IllegalArgumentException("Instruction" + instruction + "not part of this body.");
        }
        return this.instructions.subList(i + 1, this.instructions.size());
    }

    public List<DexlibAbstractInstruction> instructionsBefore(DexlibAbstractInstruction instruction) {
        int i = this.instructions.indexOf(instruction);
        if (i == -1) {
            throw new IllegalArgumentException("Instruction " + instruction + " not part of this body.");
        }
        List<DexlibAbstractInstruction> l = new ArrayList<>();
        l.addAll(this.instructions.subList(0, i));
        Collections.reverse(l);
        return l;
    }

    private void addTraps() {
        Jimple jimple = Jimple.v();
        for (TryBlock<? extends ExceptionHandler> tryItem : this.tries) {
            int startAddress = tryItem.getStartCodeAddress();
            int length = tryItem.getCodeUnitCount();
            int endAddress = startAddress + length;
            Unit beginStmt = instructionAtAddress(startAddress).getUnit();
            Unit endStmt = instructionAtAddress(endAddress).getUnit();
            Unit endStmt2 = endStmt;
            if (this.jBody.getUnits().getLast() == endStmt) {
                endStmt2 = endStmt;
                if (instructionAtAddress(endAddress - 1).getUnit() == endStmt) {
                    NopStmt newNopStmt = jimple.newNopStmt();
                    this.jBody.getUnits().insertAfter(newNopStmt, (NopStmt) endStmt);
                    endStmt2 = newNopStmt;
                }
            }
            Iterator<? extends Object> it = tryItem.getExceptionHandlers().iterator();
            while (it.hasNext()) {
                ExceptionHandler handler = (ExceptionHandler) it.next();
                String exceptionType = handler.getExceptionType();
                if (exceptionType == null) {
                    exceptionType = "Ljava/lang/Throwable;";
                }
                Type t = DexType.toSoot(exceptionType);
                if (t instanceof RefType) {
                    SootClass exception = ((RefType) t).getSootClass();
                    DexlibAbstractInstruction instruction = instructionAtAddress(handler.getHandlerCodeAddress());
                    if (!(instruction instanceof MoveExceptionInstruction)) {
                        logger.debug(String.format("First instruction of trap handler unit not MoveException but %s", instruction.getClass().getName()));
                    } else {
                        ((MoveExceptionInstruction) instruction).setRealType(this, exception.getType());
                    }
                    Trap trap = jimple.newTrap(exception, beginStmt, endStmt2, instruction.getUnit());
                    this.jBody.getTraps().add(trap);
                }
            }
        }
    }
}
