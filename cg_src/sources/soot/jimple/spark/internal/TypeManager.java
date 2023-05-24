package soot.jimple.spark.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.JavaBasicTypes;
import soot.NullType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.TypeSwitch;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.typing.fast.WeakObjectType;
import soot.util.ArrayNumberer;
import soot.util.BitVector;
import soot.util.LargeNumberedMap;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/internal/TypeManager.class */
public final class TypeManager {
    protected PAG pag;
    private Map<SootClass, List<AllocNode>> class2allocs = new HashMap(1024);
    private List<AllocNode> anySubtypeAllocs = new LinkedList();
    private LargeNumberedMap<Type, BitVector> typeMask = null;
    protected Supplier<FastHierarchy> fh = null;
    protected QueueReader<AllocNode> allocNodeListener = null;
    protected final RefType rtObject = Scene.v().getObjectType();
    protected final RefType rtSerializable = RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE);
    protected final RefType rtCloneable = RefType.v("java.lang.Cloneable");

    public TypeManager(PAG pag) {
        this.pag = pag;
    }

    public static boolean isUnresolved(Type type) {
        SootClass c;
        if (type instanceof ArrayType) {
            ArrayType at = (ArrayType) type;
            type = at.getArrayElementType();
        }
        if (!(type instanceof RefType)) {
            return false;
        }
        RefType rt = (RefType) type;
        if (!rt.hasSootClass()) {
            if (!(rt instanceof WeakObjectType) || (c = Scene.v().forceResolve(rt.getClassName(), 1)) == null) {
                return true;
            }
            rt.setSootClass(c);
        }
        SootClass cl = rt.getSootClass();
        return cl.resolvingLevel() < 1;
    }

    public final BitVector get(Type type) {
        Iterable<Type> types;
        if (type == null) {
            return null;
        }
        while (this.allocNodeListener.hasNext()) {
            AllocNode n = this.allocNodeListener.next();
            Type nt = n.getType();
            if ((nt instanceof NullType) || (nt instanceof AnySubType)) {
                types = Scene.v().getTypeNumberer();
            } else {
                types = Scene.v().getOrMakeFastHierarchy().canStoreTypeList(nt);
            }
            for (Type t : types) {
                if ((t instanceof RefLikeType) && !(t instanceof AnySubType) && !isUnresolved(t)) {
                    BitVector mask = this.typeMask.get(t);
                    if (mask == null) {
                        LargeNumberedMap<Type, BitVector> largeNumberedMap = this.typeMask;
                        BitVector mask2 = new BitVector();
                        largeNumberedMap.put(t, mask2);
                        Iterator<AllocNode> it = this.pag.getAllocNodeNumberer().iterator();
                        while (it.hasNext()) {
                            AllocNode an = it.next();
                            if (castNeverFails(an.getType(), t)) {
                                mask2.set(an.getNumber());
                            }
                        }
                    } else {
                        mask.set(n.getNumber());
                    }
                }
            }
        }
        BitVector ret = this.typeMask.get(type);
        if (ret == null && this.fh != null) {
            SootClass curClass = ((RefType) type).getSootClass();
            if (curClass.isPhantom()) {
                return new BitVector();
            }
            while (curClass.hasSuperclass()) {
                curClass = curClass.getSuperclass();
                if ((type instanceof RefType) && curClass.isPhantom()) {
                    return new BitVector();
                }
            }
            throw new RuntimeException("Type mask not found for type " + type);
        }
        return ret;
    }

    public final void clearTypeMask() {
        this.typeMask = null;
    }

    public final void makeTypeMask() {
        RefType.v("java.lang.Class");
        this.typeMask = new LargeNumberedMap<>(Scene.v().getTypeNumberer());
        if (this.fh == null) {
            return;
        }
        initClass2allocs();
        makeClassTypeMask(Scene.v().getSootClass(Scene.v().getObjectType().getClassName()));
        BitVector visitedTypes = new BitVector();
        Iterator<Type> it = this.typeMask.keyIterator();
        while (it.hasNext()) {
            visitedTypes.set(it.next().getNumber());
        }
        ArrayNumberer<AllocNode> allocNodes = this.pag.getAllocNodeNumberer();
        for (Type t : Scene.v().getTypeNumberer()) {
            if ((t instanceof RefLikeType) && !(t instanceof AnySubType) && !isUnresolved(t)) {
                if ((t instanceof RefType) && t != this.rtObject && t != this.rtSerializable && t != this.rtCloneable) {
                    RefType rt = (RefType) t;
                    SootClass sc = rt.getSootClass();
                    if (sc.isInterface()) {
                        makeMaskOfInterface(sc);
                    }
                    if (!visitedTypes.get(t.getNumber()) && !rt.getSootClass().isPhantom()) {
                        makeClassTypeMask(rt.getSootClass());
                    }
                } else {
                    BitVector mask = new BitVector(allocNodes.size());
                    Iterator<AllocNode> it2 = allocNodes.iterator();
                    while (it2.hasNext()) {
                        Node n = it2.next();
                        if (castNeverFails(n.getType(), t)) {
                            mask.set(n.getNumber());
                        }
                    }
                    this.typeMask.put(t, mask);
                }
            }
        }
        this.allocNodeListener = this.pag.allocNodeListener();
    }

    public final boolean castNeverFails(Type src, Type dst) {
        if (dst == null || dst == src) {
            return true;
        }
        if (src == null) {
            return false;
        }
        if ((src instanceof NullType) || (src instanceof AnySubType)) {
            return true;
        }
        if (dst instanceof NullType) {
            return false;
        }
        if (dst instanceof AnySubType) {
            throw new RuntimeException("oops src=" + src + " dst=" + dst);
        }
        FastHierarchy fh = getFastHierarchy();
        if (fh == null) {
            return true;
        }
        return fh.canStoreType(src, dst);
    }

    public void setFastHierarchy(Supplier<FastHierarchy> fh) {
        this.fh = fh;
    }

    public FastHierarchy getFastHierarchy() {
        if (this.fh == null) {
            return null;
        }
        return this.fh.get();
    }

    private void initClass2allocs() {
        Iterator<AllocNode> it = this.pag.getAllocNodeNumberer().iterator();
        while (it.hasNext()) {
            AllocNode an = it.next();
            addAllocNode(an);
        }
    }

    private final void addAllocNode(final AllocNode alloc) {
        alloc.getType().apply(new TypeSwitch() { // from class: soot.jimple.spark.internal.TypeManager.1
            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public final void caseRefType(RefType t) {
                SootClass cl = t.getSootClass();
                List list = (List) TypeManager.this.class2allocs.get(cl);
                LinkedList linkedList = list;
                if (list == null) {
                    linkedList = new LinkedList();
                    TypeManager.this.class2allocs.put(cl, linkedList);
                }
                linkedList.add(alloc);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public final void caseAnySubType(AnySubType t) {
                TypeManager.this.anySubtypeAllocs.add(alloc);
            }
        });
    }

    private final BitVector makeClassTypeMask(SootClass clazz) {
        BitVector cachedMask = this.typeMask.get(clazz.getType());
        if (cachedMask != null) {
            return cachedMask;
        }
        int nBits = this.pag.getAllocNodeNumberer().size();
        BitVector mask = new BitVector(nBits);
        List<AllocNode> allocs = null;
        if (clazz.isConcrete()) {
            allocs = this.class2allocs.get(clazz);
        }
        if (allocs != null) {
            for (AllocNode an : allocs) {
                mask.set(an.getNumber());
            }
        }
        Collection<SootClass> subclasses = this.fh.get().getSubclassesOf(clazz);
        if (subclasses == Collections.EMPTY_LIST) {
            for (AllocNode an2 : this.anySubtypeAllocs) {
                mask.set(an2.getNumber());
            }
            this.typeMask.put(clazz.getType(), mask);
            return mask;
        }
        for (SootClass subcl : subclasses) {
            mask.or(makeClassTypeMask(subcl));
        }
        this.typeMask.put(clazz.getType(), mask);
        return mask;
    }

    private final BitVector makeMaskOfInterface(SootClass interf) {
        if (!interf.isInterface()) {
            throw new RuntimeException();
        }
        BitVector ret = new BitVector(this.pag.getAllocNodeNumberer().size());
        this.typeMask.put(interf.getType(), ret);
        Collection<SootClass> implementers = getFastHierarchy().getAllImplementersOfInterface(interf);
        for (SootClass impl : implementers) {
            BitVector other = this.typeMask.get(impl.getType());
            if (other == null) {
                other = makeClassTypeMask(impl);
            }
            ret.or(other);
        }
        if (implementers.size() == 0) {
            for (AllocNode an : this.anySubtypeAllocs) {
                ret.set(an.getNumber());
            }
        }
        return ret;
    }
}
