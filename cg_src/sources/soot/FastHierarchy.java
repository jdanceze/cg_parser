package soot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.spark.internal.TypeManager;
import soot.options.Options;
import soot.util.ConcurrentHashMultiMap;
import soot.util.MultiMap;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/FastHierarchy.class */
public class FastHierarchy {
    protected static final int USE_INTERVALS_BOUNDARY = 100;
    protected Table<SootClass, NumberedString, SootMethod> typeToVtbl = HashBasedTable.create();
    protected MultiMap<SootClass, SootClass> classToSubclasses = new ConcurrentHashMultiMap();
    protected MultiMap<SootClass, SootClass> interfaceToSubinterfaces = new ConcurrentHashMultiMap();
    protected MultiMap<SootClass, SootClass> interfaceToImplementers = new ConcurrentHashMultiMap();
    protected MultiMap<SootClass, SootClass> interfaceToAllSubinterfaces = new ConcurrentHashMultiMap();
    protected MultiMap<SootClass, SootClass> interfaceToAllImplementers = new ConcurrentHashMultiMap();
    protected Map<SootClass, Interval> classToInterval = new HashMap();
    protected final Scene sc = Scene.v();
    protected final RefType rtObject = this.sc.getObjectType();
    protected final RefType rtSerializable = RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE);
    protected final RefType rtCloneable = RefType.v("java.lang.Cloneable");
    protected final RefType cilArray = RefType.v(DotnetBasicTypes.SYSTEM_ARRAY);
    protected final RefType cilIcomparable = RefType.v(DotnetBasicTypes.SYSTEM_ICOMPARABLE);
    protected final RefType cilIcomparable1 = RefType.v(DotnetBasicTypes.SYSTEM_ICOMPARABLE_1);
    protected final RefType cilIconvertible = RefType.v(DotnetBasicTypes.SYSTEM_ICONVERTIBLE);
    protected final RefType cilIequatable1 = RefType.v(DotnetBasicTypes.SYSTEM_IEQUATABLE_1);
    protected final RefType cilIformattable = RefType.v(DotnetBasicTypes.SYSTEM_IFORMATTABLE);

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/FastHierarchy$Interval.class */
    public class Interval {
        int lower;
        int upper;

        public Interval() {
        }

        public Interval(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public boolean isSubrange(Interval potentialSubrange) {
            if (potentialSubrange != this) {
                return potentialSubrange != null && this.lower <= potentialSubrange.lower && this.upper >= potentialSubrange.upper;
            }
            return true;
        }
    }

    protected int dfsVisit(int start, SootClass c) {
        Interval r = new Interval();
        int start2 = start + 1;
        r.lower = start;
        Collection<SootClass> col = this.classToSubclasses.get(c);
        if (col != null) {
            for (SootClass sc : col) {
                if (!sc.isInterface()) {
                    start2 = dfsVisit(start2, sc);
                }
            }
        }
        int i = start2;
        int start3 = start2 + 1;
        r.upper = i;
        if (c.isInterface()) {
            throw new RuntimeException("Attempt to dfs visit interface " + c);
        }
        this.classToInterval.putIfAbsent(c, r);
        return start3;
    }

    public FastHierarchy() {
        buildInverseMaps();
        int r = dfsVisit(0, this.sc.getObjectType().getSootClass());
        Iterator<SootClass> phantomClassIt = this.sc.getPhantomClasses().snapshotIterator();
        while (phantomClassIt.hasNext()) {
            SootClass phantomClass = phantomClassIt.next();
            if (!phantomClass.isInterface()) {
                r = dfsVisit(r, phantomClass);
            }
        }
    }

    protected void buildInverseMaps() {
        SootClass superClass;
        for (SootClass cl : this.sc.getClasses().getElementsUnsorted()) {
            if (cl.resolvingLevel() >= 1) {
                if (!cl.isInterface() && (superClass = cl.getSuperclassUnsafe()) != null) {
                    this.classToSubclasses.put(superClass, cl);
                }
                for (SootClass supercl : cl.getInterfaces()) {
                    if (cl.isInterface()) {
                        this.interfaceToSubinterfaces.put(supercl, cl);
                    } else {
                        this.interfaceToImplementers.put(supercl, cl);
                    }
                }
            }
        }
    }

    public boolean isSubclass(SootClass child, SootClass parent) {
        child.checkLevel(1);
        parent.checkLevel(1);
        Interval parentInterval = this.classToInterval.get(parent);
        Interval childInterval = this.classToInterval.get(child);
        return (parentInterval == null || childInterval == null || !parentInterval.isSubrange(childInterval)) ? false : true;
    }

    public Set<SootClass> getAllImplementersOfInterface(SootClass parent) {
        parent.checkLevel(1);
        Set<SootClass> result = this.interfaceToAllImplementers.get(parent);
        if (!result.isEmpty()) {
            return result;
        }
        Set<SootClass> result2 = new HashSet<>();
        for (SootClass subinterface : getAllSubinterfaces(parent)) {
            if (subinterface != parent) {
                result2.addAll(getAllImplementersOfInterface(subinterface));
            }
        }
        result2.addAll(this.interfaceToImplementers.get(parent));
        this.interfaceToAllImplementers.putAll(parent, result2);
        return result2;
    }

    public Set<SootClass> getAllSubinterfaces(SootClass parent) {
        parent.checkLevel(1);
        if (!parent.isInterface()) {
            return Collections.emptySet();
        }
        Set<SootClass> result = this.interfaceToAllSubinterfaces.get(parent);
        if (!result.isEmpty()) {
            return result;
        }
        Set<SootClass> result2 = new HashSet<>();
        result2.add(parent);
        for (SootClass si : this.interfaceToSubinterfaces.get(parent)) {
            result2.addAll(getAllSubinterfaces(si));
        }
        this.interfaceToAllSubinterfaces.putAll(parent, result2);
        return result2;
    }

    public boolean canStoreType(Type child, Type parent) {
        if (child == parent || child.equals(parent)) {
            return true;
        }
        if (parent instanceof NullType) {
            return false;
        }
        if (child instanceof NullType) {
            return parent instanceof RefLikeType;
        }
        if (child instanceof RefType) {
            if (parent == this.rtObject) {
                return true;
            }
            if (parent instanceof RefType) {
                return canStoreClass(((RefType) child).getSootClass(), ((RefType) parent).getSootClass());
            }
            return false;
        } else if (child instanceof AnySubType) {
            if (!(parent instanceof RefLikeType)) {
                throw new RuntimeException("Unhandled type " + parent + "! Type " + child + " cannot be stored in type " + parent);
            }
            if (parent instanceof ArrayType) {
                Type base = ((AnySubType) child).getBase();
                return Options.v().src_prec() == 7 ? base == this.cilArray : base == this.rtObject || base == this.rtSerializable || base == this.rtCloneable;
            }
            Deque<SootClass> worklist = new ArrayDeque<>();
            SootClass base2 = ((AnySubType) child).getBase().getSootClass();
            if (base2.isInterface()) {
                worklist.addAll(getAllImplementersOfInterface(base2));
            } else {
                worklist.add(base2);
            }
            SootClass parentClass = ((RefType) parent).getSootClass();
            Set<SootClass> workset = new HashSet<>();
            while (true) {
                SootClass cl = worklist.poll();
                if (cl != null) {
                    if (workset.add(cl)) {
                        if (cl.isConcrete() && canStoreClass(cl, parentClass)) {
                            return true;
                        }
                        worklist.addAll(getSubclassesOf(cl));
                    }
                } else {
                    return false;
                }
            }
        } else if (child instanceof ArrayType) {
            if (parent instanceof RefType) {
                return Options.v().src_prec() == 7 ? parent == this.cilArray : parent == this.rtObject || parent == this.rtSerializable || parent == this.rtCloneable;
            } else if (parent instanceof ArrayType) {
                ArrayType aparent = (ArrayType) parent;
                ArrayType achild = (ArrayType) child;
                if (achild.numDimensions == aparent.numDimensions) {
                    Type pBaseType = aparent.baseType;
                    Type cBaseType = achild.baseType;
                    if (cBaseType.equals(pBaseType)) {
                        return true;
                    }
                    if ((cBaseType instanceof RefType) && (pBaseType instanceof RefType)) {
                        return canStoreType(cBaseType, pBaseType);
                    }
                    return false;
                } else if (achild.numDimensions > aparent.numDimensions) {
                    Type pBaseType2 = aparent.baseType;
                    return Options.v().src_prec() == 7 ? pBaseType2 == this.cilArray : pBaseType2 == this.rtObject || pBaseType2 == this.rtSerializable || pBaseType2 == this.rtCloneable;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (Options.v().src_prec() == 7 && (child instanceof PrimType) && (parent instanceof RefType)) {
            return parent == this.cilIcomparable || parent == this.cilIcomparable1 || parent == this.cilIconvertible || parent == this.cilIformattable || parent == this.cilIequatable1 || parent == this.rtObject;
        } else {
            return false;
        }
    }

    public boolean canStoreClass(SootClass child, SootClass parent) {
        parent.checkLevel(1);
        child.checkLevel(1);
        Interval parentInterval = this.classToInterval.get(parent);
        Interval childInterval = this.classToInterval.get(child);
        if (parentInterval != null && childInterval != null) {
            return parentInterval.isSubrange(childInterval);
        }
        if (childInterval == null) {
            if (parentInterval != null) {
                return parent == this.rtObject.getSootClass();
            }
            return getAllSubinterfaces(parent).contains(child);
        }
        Set<SootClass> impl = getAllImplementersOfInterface(parent);
        if (impl.size() > 100) {
            return canStoreClassClassic(child, parent);
        }
        for (SootClass c : impl) {
            Interval interval = this.classToInterval.get(c);
            if (interval != null && interval.isSubrange(childInterval)) {
                return true;
            }
        }
        return false;
    }

    protected boolean canStoreClassClassic(SootClass child, SootClass parent) {
        boolean parentIsInterface = parent.isInterface();
        ArrayDeque<SootClass> children = new ArrayDeque<>();
        children.add(child);
        while (true) {
            SootClass p = children.poll();
            if (p != null) {
                SootClass sootClass = p;
                while (true) {
                    SootClass sc = sootClass;
                    if (sc == null) {
                        break;
                    } else if (sc == parent) {
                        return true;
                    } else {
                        if (parentIsInterface) {
                            for (SootClass interf : sc.getInterfaces()) {
                                if (interf == parent) {
                                    return true;
                                }
                                children.push(interf);
                            }
                            continue;
                        }
                        sootClass = sc.getSuperclassUnsafe();
                    }
                }
            } else {
                return false;
            }
        }
    }

    public Collection<SootMethod> resolveConcreteDispatchWithoutFailing(Collection<Type> concreteTypes, SootMethod m, RefType declaredTypeOfBase) {
        SootMethod concreteM;
        SootMethod concreteM2;
        SootMethod concreteM3;
        SootClass declaringClass = declaredTypeOfBase.getSootClass();
        declaringClass.checkLevel(1);
        Set<SootMethod> ret = new HashSet<>();
        for (Type t : concreteTypes) {
            if (t instanceof AnySubType) {
                HashSet<SootClass> s = new HashSet<>();
                s.add(declaringClass);
                while (!s.isEmpty()) {
                    SootClass c = s.iterator().next();
                    s.remove(c);
                    if (!c.isInterface() && !c.isAbstract() && canStoreClass(c, declaringClass) && (concreteM = resolveConcreteDispatch(c, m)) != null) {
                        ret.add(concreteM);
                    }
                    Set<SootClass> subclasses = this.classToSubclasses.get(c);
                    if (subclasses != null) {
                        s.addAll(subclasses);
                    }
                    Set<SootClass> subinterfaces = this.interfaceToSubinterfaces.get(c);
                    if (subinterfaces != null) {
                        s.addAll(subinterfaces);
                    }
                    Set<SootClass> implementers = this.interfaceToImplementers.get(c);
                    if (implementers != null) {
                        s.addAll(implementers);
                    }
                }
                return ret;
            } else if (t instanceof RefType) {
                SootClass concreteClass = ((RefType) t).getSootClass();
                if (canStoreClass(concreteClass, declaringClass)) {
                    try {
                        concreteM2 = resolveConcreteDispatch(concreteClass, m);
                    } catch (Exception e) {
                        concreteM2 = null;
                    }
                    if (concreteM2 != null) {
                        ret.add(concreteM2);
                    }
                }
            } else if (t instanceof ArrayType) {
                try {
                    concreteM3 = resolveConcreteDispatch(this.sc.getObjectType().getSootClass(), m);
                } catch (Exception e2) {
                    concreteM3 = null;
                }
                if (concreteM3 != null) {
                    ret.add(concreteM3);
                }
            } else {
                throw new RuntimeException("Unrecognized reaching type " + t);
            }
        }
        return ret;
    }

    public Collection<SootMethod> resolveConcreteDispatch(Collection<Type> concreteTypes, SootMethod m, RefType declaredTypeOfBase) {
        SootMethod concreteM;
        SootMethod concreteM2;
        SootClass declaringClass = declaredTypeOfBase.getSootClass();
        declaringClass.checkLevel(1);
        Set<SootMethod> ret = new HashSet<>();
        for (Type t : concreteTypes) {
            if (t instanceof AnySubType) {
                HashSet<SootClass> s = new HashSet<>();
                s.add(declaringClass);
                while (!s.isEmpty()) {
                    SootClass c = s.iterator().next();
                    s.remove(c);
                    if (!c.isInterface() && !c.isAbstract() && canStoreClass(c, declaringClass) && (concreteM = resolveConcreteDispatch(c, m)) != null) {
                        ret.add(concreteM);
                    }
                    Set<SootClass> subclasses = this.classToSubclasses.get(c);
                    if (subclasses != null) {
                        s.addAll(subclasses);
                    }
                    Set<SootClass> subinterfaces = this.interfaceToSubinterfaces.get(c);
                    if (subinterfaces != null) {
                        s.addAll(subinterfaces);
                    }
                    Set<SootClass> implementers = this.interfaceToImplementers.get(c);
                    if (implementers != null) {
                        s.addAll(implementers);
                    }
                }
                return ret;
            } else if (t instanceof RefType) {
                SootClass concreteClass = ((RefType) t).getSootClass();
                if (canStoreClass(concreteClass, declaringClass) && (concreteM2 = resolveConcreteDispatch(concreteClass, m)) != null) {
                    ret.add(concreteM2);
                }
            } else if (t instanceof ArrayType) {
                SootMethod concreteM3 = resolveConcreteDispatch(this.rtObject.getSootClass(), m);
                if (concreteM3 != null) {
                    ret.add(concreteM3);
                }
            } else {
                throw new RuntimeException("Unrecognized reaching type " + t);
            }
        }
        return ret;
    }

    private boolean isVisible(SootClass from, SootClass declaringClass, int modifier) {
        from.checkLevel(1);
        if (Modifier.isPublic(modifier)) {
            return true;
        }
        SootClass curDecl = declaringClass;
        while (curDecl.hasOuterClass()) {
            curDecl = curDecl.getOuterClass();
            if (from.equals(curDecl)) {
                return true;
            }
            SootClass curFrom = from;
            while (curFrom.hasOuterClass()) {
                curFrom = curFrom.getOuterClass();
                if (curDecl.equals(curFrom)) {
                    return true;
                }
            }
        }
        if (Modifier.isPrivate(modifier)) {
            return from.equals(declaringClass);
        }
        if (Modifier.isProtected(modifier)) {
            return canStoreClass(from, declaringClass);
        }
        return from.getJavaPackageName().equals(declaringClass.getJavaPackageName());
    }

    public Set<SootMethod> resolveAbstractDispatch(SootClass baseType, SootMethod m) {
        return resolveAbstractDispatch(baseType, m.makeRef());
    }

    public Set<SootMethod> resolveAbstractDispatch(SootClass baseType, SootMethodRef m) {
        SootMethod resolvedMethod;
        final HashSet<SootClass> resolved = new HashSet<>();
        HashSet<SootMethod> ret = new HashSet<>();
        ArrayDeque<SootClass> worklist = new ArrayDeque<SootClass>() { // from class: soot.FastHierarchy.1
            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean addAll(Collection<? extends SootClass> c) {
                boolean b = false;
                for (SootClass e : c) {
                    if (add(e)) {
                        b = true;
                    }
                }
                return b;
            }

            @Override // java.util.ArrayDeque, java.util.AbstractCollection, java.util.Collection, java.util.Deque, java.util.Queue
            public boolean add(SootClass e) {
                if (resolved.contains(e) && FastHierarchy.this.classToSubclasses.get(e).isEmpty()) {
                    return false;
                }
                return super.add((AnonymousClass1) e);
            }
        };
        worklist.add(baseType);
        while (true) {
            SootClass concreteType = worklist.poll();
            if (concreteType != null) {
                if (concreteType.isInterface()) {
                    worklist.addAll(getAllImplementersOfInterface(concreteType));
                } else {
                    Collection<SootClass> c = this.classToSubclasses.get(concreteType);
                    if (c != null) {
                        worklist.addAll(c);
                    }
                    if (!resolved.contains(concreteType) && (resolvedMethod = resolveMethod(concreteType, m, false, resolved)) != null) {
                        ret.add(resolvedMethod);
                    }
                }
            } else {
                return ret;
            }
        }
    }

    public SootMethod resolveConcreteDispatch(SootClass baseType, SootMethod m) {
        return resolveConcreteDispatch(baseType, m.makeRef());
    }

    public SootMethod resolveConcreteDispatch(SootClass baseType, SootMethodRef m) {
        baseType.checkLevel(1);
        if (baseType.isInterface()) {
            throw new RuntimeException("A concrete type cannot be an interface: " + baseType);
        }
        return resolveMethod(baseType, m, false);
    }

    public SootMethod resolveMethod(SootClass baseType, SootMethod m, boolean allowAbstract) {
        return resolveMethod(baseType, m.makeRef(), allowAbstract);
    }

    public SootMethod resolveMethod(SootClass baseType, SootMethodRef m, boolean allowAbstract) {
        return resolveMethod(baseType, m, allowAbstract, new HashSet());
    }

    private SootMethod resolveMethod(SootClass baseType, SootMethodRef m, boolean allowAbstract, Set<SootClass> ignoreList) {
        return resolveMethod(baseType, m.getDeclaringClass(), m.getName(), m.getParameterTypes(), m.getReturnType(), allowAbstract, ignoreList, m.getSubSignature());
    }

    public SootMethod resolveMethod(SootClass baseType, SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean allowAbstract) {
        return resolveMethod(baseType, declaringClass, name, parameterTypes, returnType, allowAbstract, new HashSet(), null);
    }

    public SootMethod resolveMethod(SootClass baseType, SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean allowAbstract, NumberedString subsignature) {
        return resolveMethod(baseType, declaringClass, name, parameterTypes, returnType, allowAbstract, new HashSet(), subsignature);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0079, code lost:
        if (r12 != false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0081, code lost:
        if (r16.isAbstract() == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0084, code lost:
        r16 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008f, code lost:
        if (r16.isAbstract() != false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0092, code lost:
        r6.typeToVtbl.put(r7, r15, r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a3, code lost:
        return r16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private soot.SootMethod resolveMethod(soot.SootClass r7, soot.SootClass r8, java.lang.String r9, java.util.List<soot.Type> r10, soot.Type r11, boolean r12, java.util.Set<soot.SootClass> r13, soot.util.NumberedString r14) {
        /*
            Method dump skipped, instructions count: 395
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.FastHierarchy.resolveMethod(soot.SootClass, soot.SootClass, java.lang.String, java.util.List, soot.Type, boolean, java.util.Set, soot.util.NumberedString):soot.SootMethod");
    }

    private boolean isHandleDefaultMethods() {
        int version = Options.v().java_version();
        return version == 0 || version > 7;
    }

    public SootMethod resolveSpecialDispatch(SootMethod callee, SootMethod container) {
        SootClass containerClass = container.getDeclaringClass();
        SootClass calleeClass = callee.getDeclaringClass();
        if (containerClass.getType() != calleeClass.getType() && canStoreType(containerClass.getType(), calleeClass.getType()) && !"<init>".equals(callee.getName()) && !"<clinit>".equals(callee.getName()) && !calleeClass.isInterface()) {
            return resolveConcreteDispatch(containerClass, callee);
        }
        return callee;
    }

    private SootMethod getSignaturePolymorphicMethod(SootClass concreteType, String name, List<Type> parameterTypes, Type returnType) {
        if (concreteType == null) {
            throw new RuntimeException("The concreteType cannot not be null!");
        }
        SootMethod candidate = null;
        for (SootMethod method : concreteType.getMethodsByNameAndParamCount(name, parameterTypes.size())) {
            if (method.getParameterTypes().equals(parameterTypes) && canStoreType(method.getReturnType(), returnType)) {
                candidate = method;
                returnType = method.getReturnType();
            }
            if (Options.v().src_prec() == 7 && method.getParameterCount() == parameterTypes.size() && canStoreType(returnType, method.getReturnType())) {
                boolean canStore = true;
                for (int i = 0; i < method.getParameterCount(); i++) {
                    Type methodParameter = method.getParameterType(i);
                    Type calleeParameter = parameterTypes.get(i);
                    if (!methodParameter.equals(calleeParameter) && !canStoreType(calleeParameter, methodParameter)) {
                        canStore = false;
                    }
                }
                if (canStore) {
                    candidate = method;
                    returnType = method.getReturnType();
                }
            }
        }
        return candidate;
    }

    public Collection<SootClass> getSubclassesOf(SootClass c) {
        c.checkLevel(1);
        Set<SootClass> ret = this.classToSubclasses.get(c);
        return ret == null ? Collections.emptySet() : ret;
    }

    public Iterable<Type> canStoreTypeList(final Type nt) {
        return new Iterable<Type>() { // from class: soot.FastHierarchy.2
            @Override // java.lang.Iterable
            public Iterator<Type> iterator() {
                final Iterator<Type> it = Scene.v().getTypeNumberer().iterator();
                final Type type = nt;
                return new Iterator<Type>() { // from class: soot.FastHierarchy.2.1
                    Type crt = null;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        if (this.crt != null) {
                            return true;
                        }
                        while (it.hasNext()) {
                            Type c = (Type) it.next();
                            if (!TypeManager.isUnresolved(c) && FastHierarchy.this.canStoreType(type, c)) {
                                this.crt = c;
                                return true;
                            }
                        }
                        return false;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public Type next() {
                        Type old = this.crt;
                        this.crt = null;
                        hasNext();
                        return old;
                    }
                };
            }
        };
    }
}
