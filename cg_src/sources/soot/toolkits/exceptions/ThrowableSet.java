package soot.toolkits.exceptions;

import com.google.common.cache.CacheBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import soot.AnySubType;
import soot.FastHierarchy;
import soot.G;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.Jimple;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowableSet.class */
public class ThrowableSet {
    private static final boolean INSTRUMENTING = false;
    private final SootClass JAVA_LANG_OBJECT_CLASS = Scene.v().getObjectType().getSootClass();
    protected final Set<RefLikeType> exceptionsIncluded;
    protected final Set<AnySubType> exceptionsExcluded;
    protected Map<Object, ThrowableSet> memoizedAdds;

    protected ThrowableSet(Set<RefLikeType> include, Set<AnySubType> exclude) {
        this.exceptionsIncluded = getImmutable(include);
        this.exceptionsExcluded = getImmutable(exclude);
    }

    private static <T> Set<T> getImmutable(Set<T> in) {
        if (in == null || in.isEmpty()) {
            return Collections.emptySet();
        }
        if (1 == in.size()) {
            return Collections.singleton(in.iterator().next());
        }
        return Collections.unmodifiableSet(in);
    }

    private static <T extends RefLikeType> Iterator<T> sortedThrowableIterator(Collection<T> coll) {
        if (coll.size() <= 1) {
            return coll.iterator();
        }
        RefLikeType[] array = (RefLikeType[]) coll.toArray(new RefLikeType[coll.size()]);
        Arrays.sort(array, new ThrowableComparator(null));
        return Arrays.asList(array).iterator();
    }

    private ThrowableSet getMemoizedAdds(Object key) {
        if (this.memoizedAdds == null) {
            return null;
        }
        return this.memoizedAdds.get(key);
    }

    private void addToMemoizedAdds(Object key, ThrowableSet value) {
        if (this.memoizedAdds == null) {
            this.memoizedAdds = new ConcurrentHashMap();
        }
        this.memoizedAdds.put(key, value);
    }

    public ThrowableSet add(RefType e) throws AlreadyHasExclusionsException {
        if (this.exceptionsIncluded.contains(e)) {
            return this;
        }
        ThrowableSet result = getMemoizedAdds(e);
        if (result != null) {
            return result;
        }
        FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
        boolean eHasNoHierarchy = hasNoHierarchy(e);
        for (AnySubType excludedType : this.exceptionsExcluded) {
            RefType exclusionBase = excludedType.getBase();
            if ((eHasNoHierarchy && exclusionBase.equals(e)) || (!eHasNoHierarchy && hierarchy.canStoreType(e, exclusionBase))) {
                throw new AlreadyHasExclusionsException("ThrowableSet.add(RefType): adding" + e.toString() + " to the set [ " + toString() + "] where " + exclusionBase.toString() + " is excluded.");
            }
        }
        if (!eHasNoHierarchy) {
            for (RefLikeType incumbent : this.exceptionsIncluded) {
                if (incumbent instanceof AnySubType) {
                    RefType incumbentBase = ((AnySubType) incumbent).getBase();
                    if (hierarchy.canStoreType(e, incumbentBase)) {
                        addToMemoizedAdds(e, this);
                        return this;
                    }
                } else if (!(incumbent instanceof RefType)) {
                    throw new IllegalStateException("ThrowableSet.add(RefType): Set element " + incumbent.toString() + " is neither a RefType nor an AnySubType.");
                }
            }
        }
        Set<RefLikeType> resultSet = new HashSet<>(this.exceptionsIncluded);
        resultSet.add(e);
        ThrowableSet result2 = Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
        addToMemoizedAdds(e, result2);
        return result2;
    }

    private boolean hasNoHierarchy(RefType type) {
        SootClass sootClass = type.getSootClass();
        return (sootClass.hasSuperclass() || this.JAVA_LANG_OBJECT_CLASS == sootClass) ? false : true;
    }

    public ThrowableSet add(AnySubType e) throws AlreadyHasExclusionsException {
        ThrowableSet result;
        ThrowableSet result2 = getMemoizedAdds(e);
        if (result2 != null) {
            return result2;
        }
        SootClass objectClass = Scene.v().getObjectType().getSootClass();
        FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
        RefType newBase = e.getBase();
        boolean newBaseHasNoHierarchy = hasNoHierarchy(newBase);
        for (AnySubType excludedType : this.exceptionsExcluded) {
            RefType exclusionBase = excludedType.getBase();
            boolean exclusionBaseHasNoHierarchy = (exclusionBase.getSootClass().hasSuperclass() || exclusionBase.getSootClass() == objectClass) ? false : true;
            boolean isExcluded = exclusionBaseHasNoHierarchy && exclusionBase.equals(newBase);
            if (isExcluded | (!exclusionBaseHasNoHierarchy && (hierarchy.canStoreType(newBase, exclusionBase) || hierarchy.canStoreType(exclusionBase, newBase)))) {
                throw new AlreadyHasExclusionsException("ThrowableSet.add(" + e.toString() + ") to the set [ " + toString() + "] where " + exclusionBase.toString() + " is excluded.");
            }
        }
        if (this.exceptionsIncluded.contains(e)) {
            return this;
        }
        int changes = 0;
        boolean addNewException = true;
        Set<RefLikeType> resultSet = new HashSet<>();
        for (RefLikeType incumbent : this.exceptionsIncluded) {
            if (incumbent instanceof RefType) {
                if (hierarchy.canStoreType(incumbent, newBase)) {
                    changes++;
                } else {
                    resultSet.add(incumbent);
                }
            } else if (incumbent instanceof AnySubType) {
                RefType incumbentBase = ((AnySubType) incumbent).getBase();
                if (newBaseHasNoHierarchy) {
                    if (!incumbentBase.equals(newBase)) {
                        resultSet.add(incumbent);
                    }
                } else if (hierarchy.canStoreType(newBase, incumbentBase)) {
                    addNewException = false;
                    resultSet.add(incumbent);
                } else if (hierarchy.canStoreType(incumbentBase, newBase)) {
                    changes++;
                } else {
                    resultSet.add(incumbent);
                }
            } else {
                throw new IllegalStateException("ThrowableSet.add(AnySubType): Set element " + incumbent.toString() + " is neither a RefType nor an AnySubType.");
            }
        }
        if (addNewException) {
            resultSet.add(e);
            changes++;
        }
        if (changes > 0) {
            result = Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
        } else {
            result = this;
        }
        addToMemoizedAdds(e, result);
        return result;
    }

    public ThrowableSet add(ThrowableSet s) throws AlreadyHasExclusionsException {
        if (this.exceptionsExcluded.size() > 0 || s.exceptionsExcluded.size() > 0) {
            throw new AlreadyHasExclusionsException("ThrowableSet.Add(ThrowableSet): attempt to add to [" + toString() + "] after removals recorded.");
        }
        ThrowableSet result = getMemoizedAdds(s);
        if (result == null) {
            result = add(s.exceptionsIncluded);
            addToMemoizedAdds(s, result);
        }
        return result;
    }

    public boolean isEmpty() {
        return this.exceptionsIncluded.isEmpty();
    }

    private ThrowableSet add(Set<RefLikeType> addedExceptions) {
        ThrowableSet result;
        Set<RefLikeType> resultSet = new HashSet<>(this.exceptionsIncluded);
        int changes = 0;
        FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
        for (RefLikeType newType : addedExceptions) {
            if (!resultSet.contains(newType)) {
                boolean addNewType = true;
                if (newType instanceof RefType) {
                    for (RefLikeType incumbentType : resultSet) {
                        if (incumbentType instanceof RefType) {
                            if (newType == incumbentType) {
                                throw new IllegalStateException("ThrowableSet.add(Set): resultSet.contains() failed to screen duplicate RefType " + newType);
                            }
                        } else if (incumbentType instanceof AnySubType) {
                            if (hierarchy.canStoreType(newType, ((AnySubType) incumbentType).getBase())) {
                                addNewType = false;
                            }
                        } else {
                            throw new IllegalStateException("ThrowableSet.add(Set): incumbent Set element " + incumbentType + " is neither a RefType nor an AnySubType.");
                        }
                    }
                } else if (newType instanceof AnySubType) {
                    RefType newBase = ((AnySubType) newType).getBase();
                    Iterator<RefLikeType> j = resultSet.iterator();
                    while (j.hasNext()) {
                        RefLikeType incumbentType2 = j.next();
                        if (incumbentType2 instanceof RefType) {
                            if (hierarchy.canStoreType((RefType) incumbentType2, newBase)) {
                                j.remove();
                                changes++;
                            }
                        } else if (incumbentType2 instanceof AnySubType) {
                            RefType incumbentBase = ((AnySubType) incumbentType2).getBase();
                            if (newBase == incumbentBase) {
                                throw new IllegalStateException("ThrowableSet.add(Set): resultSet.contains() failed to screen duplicate AnySubType " + newBase);
                            }
                            if (hierarchy.canStoreType(incumbentBase, newBase)) {
                                j.remove();
                                changes++;
                            } else if (hierarchy.canStoreType(newBase, incumbentBase)) {
                                addNewType = false;
                            }
                        } else {
                            throw new IllegalStateException("ThrowableSet.add(Set): old Set element " + incumbentType2 + " is neither a RefType nor an AnySubType.");
                        }
                    }
                } else {
                    throw new IllegalArgumentException("ThrowableSet.add(Set): new Set element " + newType + " is neither a RefType nor an AnySubType.");
                }
                if (addNewType) {
                    changes++;
                    resultSet.add(newType);
                }
            }
        }
        if (changes > 0) {
            result = Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
        } else {
            result = this;
        }
        return result;
    }

    private ThrowableSet remove(Set<RefLikeType> removedExceptions) {
        ThrowableSet result;
        if (removedExceptions.isEmpty()) {
            return this;
        }
        int changes = 0;
        Set<RefLikeType> resultSet = new HashSet<>(this.exceptionsIncluded);
        for (RefLikeType tp : removedExceptions) {
            if ((tp instanceof RefType) && resultSet.remove(tp)) {
                changes++;
            }
        }
        if (changes > 0) {
            result = Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
        } else {
            result = this;
        }
        return result;
    }

    public ThrowableSet remove(ThrowableSet s) {
        if (this.exceptionsExcluded.size() > 0 || s.exceptionsExcluded.size() > 0) {
            throw new AlreadyHasExclusionsException("ThrowableSet.Add(ThrowableSet): attempt to add to [" + toString() + "] after removals recorded.");
        }
        return remove(s.exceptionsIncluded);
    }

    public boolean catchableAs(RefType catcher) {
        FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
        boolean catcherHasNoHierarchy = hasNoHierarchy(catcher);
        if (this.exceptionsExcluded.size() > 0) {
            for (AnySubType exclusion : this.exceptionsExcluded) {
                if (catcherHasNoHierarchy) {
                    if (exclusion.getBase().equals(catcher)) {
                        return false;
                    }
                } else if (h.canStoreType(catcher, exclusion.getBase())) {
                    return false;
                }
            }
        }
        if (this.exceptionsIncluded.contains(catcher)) {
            return true;
        }
        for (RefLikeType thrownType : this.exceptionsIncluded) {
            if (thrownType instanceof RefType) {
                if (thrownType == catcher) {
                    throw new IllegalStateException("ThrowableSet.catchableAs(RefType): exceptions.contains() failed to match contained RefType " + catcher);
                }
                if (!catcherHasNoHierarchy && h.canStoreType(thrownType, catcher)) {
                    return true;
                }
            } else {
                RefType thrownBase = ((AnySubType) thrownType).getBase();
                if (catcherHasNoHierarchy) {
                    if (thrownBase.equals(catcher) || thrownBase.getClassName().equals(Scene.v().getBaseExceptionType().toString())) {
                        return true;
                    }
                } else if (h.canStoreType(thrownBase, catcher) || h.canStoreType(catcher, thrownBase)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Pair whichCatchableAs(RefType catcher) {
        FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
        Set<RefLikeType> caughtIncluded = null;
        Set<AnySubType> caughtExcluded = null;
        Set<RefLikeType> uncaughtIncluded = null;
        Set<AnySubType> uncaughtExcluded = null;
        boolean catcherHasNoHierarchy = hasNoHierarchy(catcher);
        for (AnySubType exclusion : this.exceptionsExcluded) {
            RefType exclusionBase = exclusion.getBase();
            if (catcherHasNoHierarchy && exclusionBase.equals(catcher)) {
                return new Pair(Manager.v().EMPTY, this);
            }
            if (h.canStoreType(catcher, exclusionBase)) {
                return new Pair(Manager.v().EMPTY, this);
            }
            if (h.canStoreType(exclusionBase, catcher)) {
                caughtExcluded = addExceptionToSet(exclusion, caughtExcluded);
            } else {
                uncaughtExcluded = addExceptionToSet(exclusion, uncaughtExcluded);
            }
        }
        for (RefLikeType inclusion : this.exceptionsIncluded) {
            if (inclusion instanceof RefType) {
                if (catcherHasNoHierarchy) {
                    if (inclusion.equals(catcher)) {
                        caughtIncluded = addExceptionToSet(inclusion, caughtIncluded);
                    } else {
                        uncaughtIncluded = addExceptionToSet(inclusion, uncaughtIncluded);
                    }
                } else if (h.canStoreType(inclusion, catcher)) {
                    caughtIncluded = addExceptionToSet(inclusion, caughtIncluded);
                } else {
                    uncaughtIncluded = addExceptionToSet(inclusion, uncaughtIncluded);
                }
            } else {
                RefType base = ((AnySubType) inclusion).getBase();
                if (catcherHasNoHierarchy) {
                    if (base.equals(catcher)) {
                        caughtIncluded = addExceptionToSet(inclusion, caughtIncluded);
                    } else {
                        if (base.getClassName().equals(Scene.v().getBaseExceptionType().getClassName())) {
                            caughtIncluded = addExceptionToSet(catcher, caughtIncluded);
                        }
                        uncaughtIncluded = addExceptionToSet(inclusion, uncaughtIncluded);
                    }
                } else if (h.canStoreType(base, catcher)) {
                    caughtIncluded = addExceptionToSet(inclusion, caughtIncluded);
                } else if (h.canStoreType(catcher, base)) {
                    uncaughtIncluded = addExceptionToSet(inclusion, uncaughtIncluded);
                    uncaughtExcluded = addExceptionToSet(AnySubType.v(catcher), uncaughtExcluded);
                    caughtIncluded = addExceptionToSet(AnySubType.v(catcher), caughtIncluded);
                } else {
                    uncaughtIncluded = addExceptionToSet(inclusion, uncaughtIncluded);
                }
            }
        }
        ThrowableSet caughtSet = Manager.v().registerSetIfNew(caughtIncluded, caughtExcluded);
        ThrowableSet uncaughtSet = Manager.v().registerSetIfNew(uncaughtIncluded, uncaughtExcluded);
        return new Pair(caughtSet, uncaughtSet);
    }

    private <T> Set<T> addExceptionToSet(T e, Set<T> set) {
        if (set == null) {
            set = new HashSet();
        }
        set.add(e);
        return set;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(toBriefString());
        buffer.append(":\n  ");
        Iterator<RefLikeType> it = this.exceptionsIncluded.iterator();
        while (it.hasNext()) {
            RefLikeType ei = it.next();
            buffer.append('+');
            buffer.append(ei == null ? Jimple.NULL : ei.toString());
        }
        for (RefLikeType ee : this.exceptionsExcluded) {
            buffer.append('-');
            buffer.append(ee.toString());
        }
        return buffer.toString();
    }

    public String toBriefString() {
        return super.toString();
    }

    public String toAbbreviatedString() {
        return String.valueOf(toAbbreviatedString(this.exceptionsIncluded, '+')) + toAbbreviatedString(this.exceptionsExcluded, '-');
    }

    private String toAbbreviatedString(Set<? extends RefLikeType> s, char connector) {
        RefType baseType;
        Collection<RefLikeType> vmErrorThrowables = Manager.v().VM_ERRORS.exceptionsIncluded;
        boolean containsAllVmErrors = s.containsAll(vmErrorThrowables);
        StringBuffer buf = new StringBuffer();
        if (containsAllVmErrors) {
            buf.append(connector);
            buf.append("vmErrors");
        }
        Iterator<? extends RefLikeType> it = sortedThrowableIterator(s);
        while (it.hasNext()) {
            RefLikeType reflikeType = (RefLikeType) it.next();
            if (reflikeType instanceof RefType) {
                baseType = (RefType) reflikeType;
                if (!containsAllVmErrors || !vmErrorThrowables.contains(baseType)) {
                    buf.append(connector);
                }
            } else if (reflikeType instanceof AnySubType) {
                buf.append(connector);
                buf.append('(');
                baseType = ((AnySubType) reflikeType).getBase();
            } else {
                throw new RuntimeException("Unsupported type " + reflikeType.getClass().getName());
            }
            String typeName = baseType.toString();
            int start = 0;
            int end = typeName.length();
            if (typeName.startsWith("java.lang.")) {
                start = 0 + "java.lang.".length();
            }
            if (typeName.endsWith("Exception")) {
                end -= "Exception".length();
            }
            buf.append((CharSequence) typeName, start, end);
            if (reflikeType instanceof AnySubType) {
                buf.append(')');
            }
        }
        return buf.toString();
    }

    Collection<RefLikeType> typesIncluded() {
        return this.exceptionsIncluded;
    }

    Collection<AnySubType> typesExcluded() {
        return this.exceptionsExcluded;
    }

    Map<Object, ThrowableSet> getMemoizedAdds() {
        if (this.memoizedAdds == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(this.memoizedAdds);
    }

    public int hashCode() {
        int result = (31 * 1) + this.exceptionsIncluded.hashCode();
        return (31 * result) + this.exceptionsExcluded.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ThrowableSet other = (ThrowableSet) obj;
        return this.exceptionsIncluded.equals(other.exceptionsIncluded) && this.exceptionsExcluded.equals(other.exceptionsExcluded);
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowableSet$Manager.class */
    public static class Manager {
        public final ThrowableSet EMPTY;
        public final ThrowableSet RESOLVE_CLASS_ERRORS;
        public final RefType RUNTIME_EXCEPTION;
        public final RefType ARITHMETIC_EXCEPTION;
        public final RefType ARRAY_STORE_EXCEPTION;
        public final RefType CLASS_CAST_EXCEPTION;
        public final RefType ILLEGAL_MONITOR_STATE_EXCEPTION;
        public final RefType INDEX_OUT_OF_BOUNDS_EXCEPTION;
        public final RefType ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION;
        public final RefType NEGATIVE_ARRAY_SIZE_EXCEPTION;
        public final RefType NULL_POINTER_EXCEPTION;
        public final RefType INSTANTIATION_ERROR;
        final ThrowableSet ALL_THROWABLES;
        final ThrowableSet VM_ERRORS;
        final ThrowableSet RESOLVE_FIELD_ERRORS;
        final ThrowableSet RESOLVE_METHOD_ERRORS;
        final ThrowableSet INITIALIZATION_ERRORS;
        private final Map<ThrowableSet, ThrowableSet> registry = CacheBuilder.newBuilder().weakValues().build().asMap();
        private final int removesFromMap = 0;
        private final int removesFromMemo = 0;
        private int addsOfRefType = 0;
        private int addsOfAnySubType = 0;
        private int addsOfSet = 0;
        private int addsInclusionFromMap = 0;
        private int addsInclusionFromMemo = 0;
        private int addsInclusionFromSearch = 0;
        private int addsInclusionInterrupted = 0;
        private int addsExclusionWithSearch = 0;
        private int addsExclusionWithoutSearch = 0;
        private int removesOfAnySubType = 0;
        private int removesFromSearch = 0;
        private int registrationCalls = 0;
        private int catchableAsQueries = 0;
        private int catchableAsFromMap = 0;
        private int catchableAsFromSearch = 0;

        public Manager(Singletons.Global g) {
            Scene scene = Scene.v();
            if (Options.v().src_prec() == 7) {
                this.RUNTIME_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_SYSTEMEXCEPTION);
                this.ARITHMETIC_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION);
                this.ARRAY_STORE_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_ARRAYTYPEMISMATCHEXCEPTION);
                this.CLASS_CAST_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_INVALIDCASTEXCEPTION);
                this.ILLEGAL_MONITOR_STATE_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_EXCEPTION);
                this.INDEX_OUT_OF_BOUNDS_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION);
                this.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION);
                this.NEGATIVE_ARRAY_SIZE_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION);
                this.NULL_POINTER_EXCEPTION = scene.getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION);
                this.INSTANTIATION_ERROR = scene.getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION);
                this.EMPTY = registerSetIfNew(null, null);
                Set<RefLikeType> allThrowablesSet = new HashSet<>();
                allThrowablesSet.add(AnySubType.v(scene.getRefType(DotnetBasicTypes.SYSTEM_EXCEPTION)));
                this.ALL_THROWABLES = registerSetIfNew(allThrowablesSet, null);
                Set<RefLikeType> vmErrorSet = new HashSet<>();
                vmErrorSet.add(scene.getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                vmErrorSet.add(scene.getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION));
                this.VM_ERRORS = registerSetIfNew(vmErrorSet, null);
                Set<RefLikeType> resolveClassErrorSet = new HashSet<>();
                this.RESOLVE_CLASS_ERRORS = registerSetIfNew(resolveClassErrorSet, null);
                Set<RefLikeType> resolveFieldErrorSet = new HashSet<>(resolveClassErrorSet);
                resolveFieldErrorSet.add(scene.getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
                this.RESOLVE_FIELD_ERRORS = registerSetIfNew(resolveFieldErrorSet, null);
                Set<RefLikeType> resolveMethodErrorSet = new HashSet<>(resolveClassErrorSet);
                resolveMethodErrorSet.add(scene.getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.RESOLVE_METHOD_ERRORS = registerSetIfNew(resolveMethodErrorSet, null);
                this.INITIALIZATION_ERRORS = registerSetIfNew(new HashSet<>(), null);
                return;
            }
            this.RUNTIME_EXCEPTION = scene.getRefTypeUnsafe("java.lang.RuntimeException");
            this.ARITHMETIC_EXCEPTION = scene.getRefTypeUnsafe("java.lang.ArithmeticException");
            this.ARRAY_STORE_EXCEPTION = scene.getRefTypeUnsafe("java.lang.ArrayStoreException");
            this.CLASS_CAST_EXCEPTION = scene.getRefTypeUnsafe("java.lang.ClassCastException");
            this.ILLEGAL_MONITOR_STATE_EXCEPTION = scene.getRefTypeUnsafe("java.lang.IllegalMonitorStateException");
            this.INDEX_OUT_OF_BOUNDS_EXCEPTION = scene.getRefTypeUnsafe("java.lang.IndexOutOfBoundsException");
            this.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = scene.getRefTypeUnsafe("java.lang.ArrayIndexOutOfBoundsException");
            this.NEGATIVE_ARRAY_SIZE_EXCEPTION = scene.getRefTypeUnsafe("java.lang.NegativeArraySizeException");
            this.NULL_POINTER_EXCEPTION = scene.getRefTypeUnsafe("java.lang.NullPointerException");
            this.INSTANTIATION_ERROR = scene.getRefType("java.lang.InstantiationError");
            this.EMPTY = registerSetIfNew(null, null);
            Set<RefLikeType> allThrowablesSet2 = new HashSet<>();
            allThrowablesSet2.add(AnySubType.v(scene.getRefType("java.lang.Throwable")));
            this.ALL_THROWABLES = registerSetIfNew(allThrowablesSet2, null);
            Set<RefLikeType> vmErrorSet2 = new HashSet<>();
            vmErrorSet2.add(scene.getRefTypeUnsafe("java.lang.InternalError"));
            vmErrorSet2.add(scene.getRefTypeUnsafe("java.lang.OutOfMemoryError"));
            vmErrorSet2.add(scene.getRefTypeUnsafe("java.lang.StackOverflowError"));
            vmErrorSet2.add(scene.getRefTypeUnsafe("java.lang.UnknownError"));
            vmErrorSet2.add(scene.getRefTypeUnsafe("java.lang.ThreadDeath"));
            this.VM_ERRORS = registerSetIfNew(vmErrorSet2, null);
            Set<RefLikeType> resolveClassErrorSet2 = new HashSet<>();
            resolveClassErrorSet2.add(scene.getRefType("java.lang.ClassCircularityError"));
            if (!Options.v().j2me()) {
                resolveClassErrorSet2.add(AnySubType.v(Scene.v().getRefTypeUnsafe("java.lang.ClassFormatError")));
            }
            resolveClassErrorSet2.add(scene.getRefTypeUnsafe("java.lang.IllegalAccessError"));
            resolveClassErrorSet2.add(scene.getRefTypeUnsafe("java.lang.IncompatibleClassChangeError"));
            resolveClassErrorSet2.add(scene.getRefTypeUnsafe("java.lang.LinkageError"));
            resolveClassErrorSet2.add(scene.getRefTypeUnsafe("java.lang.NoClassDefFoundError"));
            resolveClassErrorSet2.add(scene.getRefTypeUnsafe("java.lang.VerifyError"));
            this.RESOLVE_CLASS_ERRORS = registerSetIfNew(resolveClassErrorSet2, null);
            Set<RefLikeType> resolveFieldErrorSet2 = new HashSet<>(resolveClassErrorSet2);
            resolveFieldErrorSet2.add(scene.getRefTypeUnsafe("java.lang.NoSuchFieldError"));
            this.RESOLVE_FIELD_ERRORS = registerSetIfNew(resolveFieldErrorSet2, null);
            Set<RefLikeType> resolveMethodErrorSet2 = new HashSet<>(resolveClassErrorSet2);
            resolveMethodErrorSet2.add(scene.getRefTypeUnsafe("java.lang.AbstractMethodError"));
            resolveMethodErrorSet2.add(scene.getRefTypeUnsafe("java.lang.NoSuchMethodError"));
            resolveMethodErrorSet2.add(scene.getRefTypeUnsafe("java.lang.UnsatisfiedLinkError"));
            this.RESOLVE_METHOD_ERRORS = registerSetIfNew(resolveMethodErrorSet2, null);
            Set<RefLikeType> initializationErrorSet = new HashSet<>();
            initializationErrorSet.add(AnySubType.v(scene.getRefTypeUnsafe("java.lang.Error")));
            this.INITIALIZATION_ERRORS = registerSetIfNew(initializationErrorSet, null);
        }

        public static Manager v() {
            return G.v().soot_toolkits_exceptions_ThrowableSet_Manager();
        }

        protected ThrowableSet registerSetIfNew(Set<RefLikeType> include, Set<AnySubType> exclude) {
            ThrowableSet result = new ThrowableSet(include, exclude);
            ThrowableSet ref = this.registry.get(result);
            if (ref != null) {
                return ref;
            }
            this.registry.put(result, result);
            return result;
        }

        public String reportInstrumentation() {
            int setCount = this.registry.size();
            StringBuffer buf = new StringBuffer("registeredSets: ").append(setCount).append("\naddsOfRefType: ").append(this.addsOfRefType).append("\naddsOfAnySubType: ").append(this.addsOfAnySubType).append("\naddsOfSet: ").append(this.addsOfSet).append("\naddsInclusionFromMap: ").append(this.addsInclusionFromMap).append("\naddsInclusionFromMemo: ").append(this.addsInclusionFromMemo).append("\naddsInclusionFromSearch: ").append(this.addsInclusionFromSearch).append("\naddsInclusionInterrupted: ").append(this.addsInclusionInterrupted).append("\naddsExclusionWithoutSearch: ").append(this.addsExclusionWithoutSearch).append("\naddsExclusionWithSearch: ").append(this.addsExclusionWithSearch).append("\nremovesOfAnySubType: ").append(this.removesOfAnySubType).append("\nremovesFromMap: ").append(0).append("\nremovesFromMemo: ").append(0).append("\nremovesFromSearch: ").append(this.removesFromSearch).append("\nregistrationCalls: ").append(this.registrationCalls).append("\ncatchableAsQueries: ").append(this.catchableAsQueries).append("\ncatchableAsFromMap: ").append(this.catchableAsFromMap).append("\ncatchableAsFromSearch: ").append(this.catchableAsFromSearch).append('\n');
            return buf.toString();
        }

        Set<ThrowableSet> getThrowableSets() {
            return this.registry.keySet();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowableSet$AlreadyHasExclusionsException.class */
    public static class AlreadyHasExclusionsException extends IllegalStateException {
        private static final long serialVersionUID = 6785184160868722359L;

        public AlreadyHasExclusionsException(String s) {
            super(s);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowableSet$Pair.class */
    public static class Pair {
        private ThrowableSet caught;
        private ThrowableSet uncaught;

        protected Pair(ThrowableSet caught, ThrowableSet uncaught) {
            this.caught = caught;
            this.uncaught = uncaught;
        }

        public ThrowableSet getCaught() {
            return this.caught;
        }

        public ThrowableSet getUncaught() {
            return this.uncaught;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Pair)) {
                return false;
            }
            Pair tsp = (Pair) o;
            if (this.caught.equals(tsp.caught) && this.uncaught.equals(tsp.uncaught)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = (37 * 31) + this.caught.hashCode();
            return (37 * result) + this.uncaught.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowableSet$ThrowableComparator.class */
    public static class ThrowableComparator<T extends RefLikeType> implements Comparator<T> {
        private ThrowableComparator() {
        }

        /* synthetic */ ThrowableComparator(ThrowableComparator throwableComparator) {
            this();
        }

        private static RefType baseType(RefLikeType o) {
            if (o instanceof AnySubType) {
                return ((AnySubType) o).getBase();
            }
            return (RefType) o;
        }

        @Override // java.util.Comparator
        public int compare(T o1, T o2) {
            RefType t1 = baseType(o1);
            RefType t2 = baseType(o2);
            if (t1.equals(t2)) {
                if (o1 instanceof AnySubType) {
                    if (o2 instanceof AnySubType) {
                        return 0;
                    }
                    return -1;
                } else if (o2 instanceof AnySubType) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return t1.toString().compareTo(t2.toString());
        }
    }
}
