package soot;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Singletons;
import soot.asm.AsmUtil;
import soot.jimple.ClassConstant;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.infoflow.results.xml.XmlConstants;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.tagkit.ArtificialEntityTag;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/LambdaMetaFactory.class */
public class LambdaMetaFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaMetaFactory.class);
    private int uniq = 0;
    private final Wrapper wrapper = new Wrapper();

    public LambdaMetaFactory(Singletons.Global g) {
    }

    public static LambdaMetaFactory v() {
        return G.v().soot_LambdaMetaFactory();
    }

    public SootMethodRef makeLambdaHelper(List<? extends Value> bootstrapArgs, int tag, String name, Type[] invokedType, SootClass enclosingClass) {
        int argsSize = bootstrapArgs.size();
        if (argsSize < 3 || !(bootstrapArgs.get(0) instanceof MethodType) || !(bootstrapArgs.get(1) instanceof MethodHandle) || !(bootstrapArgs.get(2) instanceof MethodType) || (argsSize > 3 && !(bootstrapArgs.get(3) instanceof IntConstant))) {
            LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.metaFactory: {}", bootstrapArgs);
            return null;
        }
        MethodType samMethodType = (MethodType) bootstrapArgs.get(0);
        MethodHandle implMethod = (MethodHandle) bootstrapArgs.get(1);
        resolveHandle(implMethod);
        MethodType instantiatedMethodType = (MethodType) bootstrapArgs.get(2);
        int flags = 0;
        if (argsSize > 3) {
            flags = ((IntConstant) bootstrapArgs.get(3)).value;
        }
        boolean serializable = (flags & 1) != 0;
        List<ClassConstant> markerInterfaces = new ArrayList<>();
        List<MethodType> bridges = new ArrayList<>();
        int va = 4;
        if ((flags & 2) != 0) {
            if (4 != argsSize && (bootstrapArgs.get(4) instanceof IntConstant)) {
                va = 4 + 1;
                int count = ((IntConstant) bootstrapArgs.get(4)).value;
                for (int i = 0; i < count; i++) {
                    if (va >= argsSize) {
                        LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                        return null;
                    }
                    int i2 = va;
                    va++;
                    Value v = bootstrapArgs.get(i2);
                    if (!(v instanceof ClassConstant)) {
                        LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                        return null;
                    }
                    markerInterfaces.add((ClassConstant) v);
                }
            } else {
                LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                return null;
            }
        }
        if ((flags & 4) != 0) {
            if (va == argsSize || !(bootstrapArgs.get(va) instanceof IntConstant)) {
                LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                return null;
            }
            int i3 = va;
            int va2 = va + 1;
            int count2 = ((IntConstant) bootstrapArgs.get(i3)).value;
            for (int i4 = 0; i4 < count2; i4++) {
                if (va2 >= argsSize) {
                    LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                    return null;
                }
                int i5 = va2;
                va2++;
                Value v2 = bootstrapArgs.get(i5);
                if (!(v2 instanceof MethodType)) {
                    LOGGER.warn("LambdaMetaFactory: unexpected arguments for LambdaMetaFactory.altMetaFactory");
                    return null;
                }
                bridges.add((MethodType) v2);
            }
        }
        List<Type> capTypes = Arrays.asList(invokedType).subList(0, invokedType.length - 1);
        if (!(invokedType[invokedType.length - 1] instanceof RefType)) {
            LOGGER.warn("unexpected interface type: " + invokedType[invokedType.length - 1]);
            return null;
        }
        SootClass functionalInterfaceToImplement = ((RefType) invokedType[invokedType.length - 1]).getSootClass();
        String enclosingClassname = enclosingClass.getName();
        String implMethodName = implMethod.getMethodRef().getName();
        String dummyName = "<init>".equals(implMethodName) ? "init" : implMethodName;
        String dummyName2 = dummyName.replace('$', '_');
        String prefix = (enclosingClassname == null || enclosingClassname.isEmpty()) ? "soot.dummy." : String.valueOf(enclosingClassname) + "$";
        String className = String.valueOf(prefix) + dummyName2 + "__" + uniqSupply();
        SootClass tclass = Scene.v().makeSootClass(className);
        tclass.setModifiers(17);
        tclass.setSuperclass(Scene.v().getObjectType().getSootClass());
        tclass.addInterface(functionalInterfaceToImplement);
        tclass.addTag(new ArtificialEntityTag());
        if (serializable) {
            tclass.addInterface(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE).getSootClass());
        }
        for (int i6 = 0; i6 < markerInterfaces.size(); i6++) {
            String internal = markerInterfaces.get(i6).getValue();
            RefType refType = (RefType) AsmUtil.toBaseType(internal, Optional.fromNullable(tclass.moduleName));
            SootClass interfaceClass = refType.getSootClass();
            if (!interfaceClass.equals(functionalInterfaceToImplement)) {
                tclass.addInterface(interfaceClass);
            }
        }
        List<SootField> capFields = new ArrayList<>(capTypes.size());
        int e = capTypes.size();
        for (int i7 = 0; i7 < e; i7++) {
            SootField f = Scene.v().makeSootField("cap" + i7, capTypes.get(i7), 0);
            capFields.add(f);
            tclass.addField(f);
        }
        if (MethodHandle.Kind.REF_INVOKE_STATIC.getValue() == implMethod.getKind()) {
            SootClass declClass = implMethod.getMethodRef().getDeclaringClass();
            if (declClass.getName().equals(enclosingClassname)) {
                SootMethod method = implMethod.getMethodRef().resolve();
                method.setModifiers((method.getModifiers() & (-3)) | 1);
            }
        }
        MethodSource ms = new ThunkMethodSource(capFields, samMethodType, implMethod, instantiatedMethodType);
        SootMethod tboot = Scene.v().makeSootMethod("bootstrap$", capTypes, functionalInterfaceToImplement.getType(), 9);
        tclass.addMethod(tboot);
        tboot.setSource(ms);
        SootMethod tctor = Scene.v().makeSootMethod("<init>", capTypes, VoidType.v(), 1);
        tclass.addMethod(tctor);
        tctor.setSource(ms);
        addDispatch(name, tclass, samMethodType, instantiatedMethodType, capFields, implMethod);
        for (MethodType bridgeType : bridges) {
            addDispatch(name, tclass, bridgeType, instantiatedMethodType, capFields, implMethod);
        }
        for (SootMethod m : tclass.getMethods()) {
            m.retrieveActiveBody();
        }
        addClassAndInvalidateHierarchy(tclass);
        if (enclosingClass.isApplicationClass()) {
            tclass.setApplicationClass();
        }
        return tboot.makeRef();
    }

    protected void addClassAndInvalidateHierarchy(SootClass tclass) {
        Scene.v().addClass(tclass);
        Scene.v().releaseFastHierarchy();
    }

    private synchronized void resolveHandle(MethodHandle implMethod) {
        Scene scene = Scene.v();
        SootMethodRef methodRef = implMethod.getMethodRef();
        scene.forceResolve(methodRef.getDeclaringClass().getName(), 1);
        Stream.concat(Stream.of(methodRef.getReturnType()), methodRef.getParameterTypes().stream()).filter(t -> {
            return t instanceof RefType;
        }).forEach(t2 -> {
            scene.forceResolve(((RefType) t2).getSootClass().getName(), 1);
        });
    }

    private void addDispatch(String name, SootClass tclass, MethodType implMethodType, MethodType instantiatedMethodType, List<SootField> capFields, MethodHandle implMethod) {
        ThunkMethodSource ms = new ThunkMethodSource(capFields, implMethodType, implMethod, instantiatedMethodType);
        SootMethod m = Scene.v().makeSootMethod(name, implMethodType.getParameterTypes(), implMethodType.getReturnType(), 1);
        tclass.addMethod(m);
        m.setSource(ms);
    }

    private synchronized long uniqSupply() {
        int i = this.uniq + 1;
        this.uniq = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/LambdaMetaFactory$Wrapper.class */
    public static class Wrapper {
        final Map<RefType, PrimType> wrapperTypes;
        final Map<PrimType, RefType> primitiveTypes;
        final Map<PrimType, SootMethod> valueOf;
        final Map<RefType, SootMethod> primitiveValue;

        public Wrapper() {
            Scene sc = Scene.v();
            Map<RefType, PrimType> wrapperTypesTmp = new HashMap<>();
            Map<PrimType, RefType> primitiveTypesTmp = new HashMap<>();
            Map<PrimType, SootMethod> valueOfTmp = new HashMap<>();
            Map<RefType, SootMethod> primitiveValueTmp = new HashMap<>();
            PrimType[] primTypes = {BooleanType.v(), ByteType.v(), CharType.v(), DoubleType.v(), FloatType.v(), IntType.v(), LongType.v(), ShortType.v()};
            for (PrimType primTy : primTypes) {
                RefType wrapTy = primTy.boxedType();
                wrapperTypesTmp.put(wrapTy, primTy);
                primitiveTypesTmp.put(primTy, wrapTy);
                SootClass wrapCls = wrapTy.getSootClass();
                valueOfTmp.put(primTy, sc.makeMethodRef(wrapCls, "valueOf", Collections.singletonList(primTy), wrapTy, true).resolve());
                primitiveValueTmp.put(wrapTy, sc.makeMethodRef(wrapCls, String.valueOf(primTy.toString()) + XmlConstants.Attributes.value, Collections.emptyList(), primTy, false).resolve());
            }
            this.wrapperTypes = Collections.unmodifiableMap(wrapperTypesTmp);
            this.primitiveTypes = Collections.unmodifiableMap(primitiveTypesTmp);
            this.valueOf = Collections.unmodifiableMap(valueOfTmp);
            this.primitiveValue = Collections.unmodifiableMap(primitiveValueTmp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/LambdaMetaFactory$ThunkMethodSource.class */
    public class ThunkMethodSource implements MethodSource {
        private final List<SootField> capFields;
        private final MethodType implMethodType;
        private final MethodHandle implMethod;
        private final MethodType instantiatedMethodType;
        private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$MethodHandle$Kind;

        static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$MethodHandle$Kind() {
            int[] iArr = $SWITCH_TABLE$soot$jimple$MethodHandle$Kind;
            if (iArr != null) {
                return iArr;
            }
            int[] iArr2 = new int[MethodHandle.Kind.valuesCustom().length];
            try {
                iArr2[MethodHandle.Kind.REF_GET_FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_GET_FIELD_STATIC.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_INVOKE_CONSTRUCTOR.ordinal()] = 8;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_INVOKE_INTERFACE.ordinal()] = 9;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_INVOKE_SPECIAL.ordinal()] = 7;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_INVOKE_STATIC.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_INVOKE_VIRTUAL.ordinal()] = 5;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_PUT_FIELD.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr2[MethodHandle.Kind.REF_PUT_FIELD_STATIC.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            $SWITCH_TABLE$soot$jimple$MethodHandle$Kind = iArr2;
            return iArr2;
        }

        public ThunkMethodSource(List<SootField> capFields, MethodType implMethodType, MethodHandle implMethod, MethodType instantiatedMethodType) {
            this.capFields = capFields;
            this.implMethodType = implMethodType;
            this.implMethod = implMethod;
            this.instantiatedMethodType = instantiatedMethodType;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // soot.MethodSource
        public Body getBody(SootMethod m, String phaseName) {
            if (!"jb".equals(phaseName)) {
                throw new Error("unsupported body type: " + phaseName);
            }
            SootClass tclass = m.getDeclaringClass();
            JimpleBody jb = Jimple.v().newBody(m);
            if (m.getName() != null) {
                String name = m.getName();
                switch (name.hashCode()) {
                    case -1684799626:
                        if (name.equals("bootstrap$")) {
                            getBootstrapBody(tclass, jb);
                            break;
                        }
                        getInvokeBody(tclass, jb);
                        break;
                    case 1818100338:
                        if (name.equals("<init>")) {
                            getInitBody(tclass, jb);
                            break;
                        }
                        getInvokeBody(tclass, jb);
                        break;
                    default:
                        getInvokeBody(tclass, jb);
                        break;
                }
            }
            LocalNameStandardizer.v().transform(jb, "jb.lns", PhaseOptions.v().getPhaseOptions("jb.lns"));
            return jb;
        }

        private void getInitBody(SootClass tclass, JimpleBody jb) {
            Jimple jimp = Jimple.v();
            PatchingChain<Unit> us = jb.getUnits();
            LocalGenerator lc = Scene.v().createLocalGenerator(jb);
            Local l = lc.generateLocal(tclass.getType());
            us.add((PatchingChain<Unit>) jimp.newIdentityStmt(l, jimp.newThisRef(tclass.getType())));
            Chain<Local> capLocals = new HashChain<>();
            int i = 0;
            for (SootField f : this.capFields) {
                Local l2 = lc.generateLocal(f.getType());
                us.add((PatchingChain<Unit>) jimp.newIdentityStmt(l2, jimp.newParameterRef(f.getType(), i)));
                capLocals.add(l2);
                i++;
            }
            us.add((PatchingChain<Unit>) jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(l, Scene.v().makeConstructorRef(Scene.v().getObjectType().getSootClass(), Collections.emptyList()), Collections.emptyList())));
            Iterator<Local> localItr = capLocals.iterator();
            for (SootField f2 : this.capFields) {
                us.add((PatchingChain<Unit>) jimp.newAssignStmt(jimp.newInstanceFieldRef(l, f2.makeRef()), localItr.next()));
            }
            us.add((PatchingChain<Unit>) jimp.newReturnVoidStmt());
        }

        private void getBootstrapBody(SootClass tclass, JimpleBody jb) {
            Jimple jimp = Jimple.v();
            PatchingChain<Unit> us = jb.getUnits();
            LocalGenerator lc = Scene.v().createLocalGenerator(jb);
            List<Value> capValues = new ArrayList<>();
            List<Type> capTypes = new ArrayList<>();
            int i = 0;
            for (SootField capField : this.capFields) {
                Type type = capField.getType();
                capTypes.add(type);
                Local p = lc.generateLocal(type);
                us.add((PatchingChain<Unit>) jimp.newIdentityStmt(p, jimp.newParameterRef(type, i)));
                capValues.add(p);
                i++;
            }
            Local l = lc.generateLocal(tclass.getType());
            us.add((PatchingChain<Unit>) jimp.newAssignStmt(l, jimp.newNewExpr(tclass.getType())));
            us.add((PatchingChain<Unit>) jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(l, Scene.v().makeConstructorRef(tclass, capTypes), capValues)));
            us.add((PatchingChain<Unit>) jimp.newReturnStmt(l));
        }

        private void getInvokeBody(SootClass tclass, JimpleBody jb) {
            Jimple jimp = Jimple.v();
            PatchingChain<Unit> us = jb.getUnits();
            LocalGenerator lc = Scene.v().createLocalGenerator(jb);
            Local this_ = lc.generateLocal(tclass.getType());
            us.add((PatchingChain<Unit>) jimp.newIdentityStmt(this_, jimp.newThisRef(tclass.getType())));
            Chain<Local> samParamLocals = new HashChain<>();
            int i = 0;
            for (Type ty : this.implMethodType.getParameterTypes()) {
                Local l = lc.generateLocal(ty);
                us.add((PatchingChain<Unit>) jimp.newIdentityStmt(l, jimp.newParameterRef(ty, i)));
                samParamLocals.add(l);
                i++;
            }
            Iterator<Type> iptItr = this.instantiatedMethodType.getParameterTypes().iterator();
            Chain<Local> instParamLocals = new HashChain<>();
            for (Local l2 : samParamLocals) {
                Type ipt = iptItr.next();
                instParamLocals.add(narrowingReferenceConversion(l2, ipt, jb, us, lc));
            }
            List<Local> args = new ArrayList<>();
            for (SootField f : this.capFields) {
                Local l3 = lc.generateLocal(f.getType());
                us.add((PatchingChain<Unit>) jimp.newAssignStmt(l3, jimp.newInstanceFieldRef(this_, f.makeRef())));
                args.add(l3);
            }
            int kind = this.implMethod.getKind();
            boolean needsReceiver = false;
            needsReceiver = (MethodHandle.Kind.REF_INVOKE_INTERFACE.getValue() == kind || MethodHandle.Kind.REF_INVOKE_VIRTUAL.getValue() == kind || MethodHandle.Kind.REF_INVOKE_SPECIAL.getValue() == kind) ? true : true;
            Iterator<Local> iplItr = instParamLocals.iterator();
            if (this.capFields.isEmpty() && iplItr.hasNext() && needsReceiver) {
                args.add(adapt(iplItr.next(), this.implMethod.getMethodRef().getDeclaringClass().getType(), jb, us, lc));
            }
            int j = args.size();
            if (needsReceiver) {
                j = args.size() - 1;
            }
            while (iplItr.hasNext()) {
                Local pl = iplItr.next();
                args.add(adapt(pl, this.implMethod.getMethodRef().getParameterType(j), jb, us, lc));
                j++;
            }
            invokeImplMethod(jb, us, lc, args);
        }

        private Local adapt(Local fromLocal, Type to, JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc) {
            Type from = fromLocal.getType();
            if (from.equals(to)) {
                return fromLocal;
            }
            if ((from instanceof ArrayType) || ((from instanceof RefType) && (to instanceof RefType))) {
                return wideningReferenceConversion(fromLocal);
            }
            if (from instanceof PrimType) {
                if (to instanceof PrimType) {
                    return wideningPrimitiveConversion(fromLocal, to, jb, us, lc);
                }
                return wideningReferenceConversion(box(fromLocal, jb, us, lc));
            } else if (to instanceof PrimType) {
                if (LambdaMetaFactory.this.wrapper.wrapperTypes.get(from) == null) {
                    RefType boxedType = LambdaMetaFactory.this.wrapper.primitiveTypes.get((PrimType) to);
                    Local castLocal = lc.generateLocal(boxedType);
                    us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(castLocal, Jimple.v().newCastExpr(fromLocal, boxedType)));
                    fromLocal = castLocal;
                }
                return wideningPrimitiveConversion(unbox(fromLocal, jb, us, lc), to, jb, us, lc);
            } else {
                throw new IllegalArgumentException("Expected 'to' to be a PrimType");
            }
        }

        private Local box(Local fromLocal, JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc) {
            PrimType primitiveType = (PrimType) fromLocal.getType();
            SootMethod valueOfMethod = LambdaMetaFactory.this.wrapper.valueOf.get(primitiveType);
            Local lBox = lc.generateLocal(primitiveType.boxedType());
            if (lBox == null || valueOfMethod == null || us == null) {
                throw new NullPointerException(String.format("%s,%s,%s,%s", valueOfMethod, primitiveType, LambdaMetaFactory.this.wrapper.valueOf.entrySet(), LambdaMetaFactory.this.wrapper.valueOf.get(primitiveType)));
            }
            us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(lBox, Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), fromLocal)));
            return lBox;
        }

        private Local unbox(Local fromLocal, JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc) {
            RefType wrapperType = (RefType) fromLocal.getType();
            Local lUnbox = lc.generateLocal(LambdaMetaFactory.this.wrapper.wrapperTypes.get(wrapperType));
            us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(lUnbox, Jimple.v().newVirtualInvokeExpr(fromLocal, LambdaMetaFactory.this.wrapper.primitiveValue.get(wrapperType).makeRef())));
            return lUnbox;
        }

        private Local wideningReferenceConversion(Local fromLocal) {
            return fromLocal;
        }

        private Local narrowingReferenceConversion(Local fromLocal, Type to, JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc) {
            Type fromTy = fromLocal.getType();
            if (fromTy.equals(to) || ((!(fromTy instanceof RefType) && !(fromTy instanceof ArrayType)) || (!(to instanceof RefType) && !(to instanceof ArrayType)))) {
                return fromLocal;
            }
            Local l2 = lc.generateLocal(to);
            us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(l2, Jimple.v().newCastExpr(fromLocal, to)));
            return l2;
        }

        private Local wideningPrimitiveConversion(Local fromLocal, Type to, JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc) {
            if (!(fromLocal.getType() instanceof PrimType)) {
                throw new IllegalArgumentException("Expected source to have primitive type");
            }
            if (!(to instanceof PrimType)) {
                throw new IllegalArgumentException("Expected target to have primitive type");
            }
            Local l2 = lc.generateLocal(to);
            us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(l2, Jimple.v().newCastExpr(fromLocal, to)));
            return l2;
        }

        private void invokeImplMethod(JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc, List<Local> args) {
            Value value = _invokeImplMethod(jb, us, lc, args);
            if (VoidType.v().equals(this.implMethodType.getReturnType())) {
                if (value instanceof InvokeExpr) {
                    us.add((PatchingChain<Unit>) Jimple.v().newInvokeStmt(value));
                }
                us.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
            } else if (VoidType.v().equals(this.implMethod.getMethodRef().getReturnType())) {
                us.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(value));
            } else {
                Local ret = lc.generateLocal(value.getType());
                us.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(ret, value));
                us.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(adapt(ret, this.implMethodType.getReturnType(), jb, us, lc)));
            }
        }

        private Value _invokeImplMethod(JimpleBody jb, PatchingChain<Unit> us, LocalGenerator lc, List<Local> args) {
            SootMethodRef methodRef = this.implMethod.getMethodRef();
            switch ($SWITCH_TABLE$soot$jimple$MethodHandle$Kind()[MethodHandle.Kind.getKind(this.implMethod.getKind()).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalArgumentException("Unexpected MethodHandle.Kind " + this.implMethod.getKind());
                case 5:
                    return Jimple.v().newVirtualInvokeExpr(args.get(0), methodRef, rest(args));
                case 6:
                    return Jimple.v().newStaticInvokeExpr(methodRef, args);
                case 7:
                    SootClass currentClass = jb.getMethod().getDeclaringClass();
                    SootClass calledClass = methodRef.getDeclaringClass();
                    if (currentClass == calledClass || Scene.v().getOrMakeFastHierarchy().canStoreClass(currentClass.getSuperclass(), calledClass)) {
                        return Jimple.v().newSpecialInvokeExpr(args.get(0), methodRef, rest(args));
                    }
                    SootMethod m = this.implMethod.getMethodRef().resolve();
                    if (!m.isPublic()) {
                        int mod = 1 | m.getModifiers();
                        m.setModifiers(mod & (-3) & (-5));
                    }
                    if (methodRef.getDeclaringClass().isInterface()) {
                        return Jimple.v().newInterfaceInvokeExpr(args.get(0), methodRef, rest(args));
                    }
                    return Jimple.v().newVirtualInvokeExpr(args.get(0), methodRef, rest(args));
                case 8:
                    Jimple jimp = Jimple.v();
                    RefType type = methodRef.getDeclaringClass().getType();
                    Local newLocal = lc.generateLocal(type);
                    us.add((PatchingChain<Unit>) jimp.newAssignStmt(newLocal, jimp.newNewExpr(type)));
                    us.add((PatchingChain<Unit>) jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(newLocal, methodRef, args)));
                    return newLocal;
                case 9:
                    return Jimple.v().newInterfaceInvokeExpr(args.get(0), methodRef, rest(args));
            }
        }

        private List<Local> rest(List<Local> args) {
            int last = args.size();
            if (last < 1) {
                return Collections.emptyList();
            }
            return args.subList(1, last);
        }
    }
}
