package soot.jimple.toolkits.typing.fast;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ListIterator;
import soot.ArrayType;
import soot.FloatType;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/BytecodeHierarchy.class */
public class BytecodeHierarchy implements IHierarchy {
    private static Collection<AncestryTreeNode> buildAncestryTree(RefType root) {
        if (root.getSootClass().isPhantom()) {
            return Collections.emptyList();
        }
        LinkedList<AncestryTreeNode> leafs = new LinkedList<>();
        leafs.add(new AncestryTreeNode(null, root));
        LinkedList<AncestryTreeNode> r = new LinkedList<>();
        RefType objectType = Scene.v().getObjectType();
        while (!leafs.isEmpty()) {
            AncestryTreeNode node = leafs.remove();
            if (TypeResolver.typesEqual(node.type, objectType)) {
                r.add(node);
            } else {
                SootClass sc = node.type.getSootClass();
                for (SootClass i : sc.getInterfaces()) {
                    leafs.add(new AncestryTreeNode(node, i.getType()));
                }
                if (!sc.isInterface() || sc.getInterfaceCount() == 0) {
                    if (!sc.isPhantom() && sc.hasSuperclass()) {
                        leafs.add(new AncestryTreeNode(node, sc.getSuperclass().getType()));
                    }
                }
            }
        }
        return r;
    }

    private static RefType leastCommonNode(AncestryTreeNode a, AncestryTreeNode b) {
        RefType r = null;
        while (a != null && b != null && TypeResolver.typesEqual(a.type, b.type)) {
            r = a.type;
            a = a.next;
            b = b.next;
        }
        return r;
    }

    public static Collection<Type> lcas_(Type a, Type b) {
        return lcas_(a, b, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Collection<Type> lcas_(Type a, Type b, boolean useWeakObjectType) {
        Collection<Type> ts;
        if (TypeResolver.typesEqual(a, b)) {
            return Collections.singletonList(a);
        }
        if (a instanceof BottomType) {
            return Collections.singletonList(b);
        }
        if (b instanceof BottomType) {
            return Collections.singletonList(a);
        }
        if ((a instanceof WeakObjectType) && (b instanceof RefType)) {
            return Collections.singletonList(b);
        }
        if ((b instanceof WeakObjectType) && (a instanceof RefType)) {
            return Collections.singletonList(a);
        }
        if ((a instanceof IntegerType) && (b instanceof IntegerType)) {
            int m = Math.max(IntUtils.getMaxValue((IntegerType) a), IntUtils.getMaxValue((IntegerType) b));
            return Collections.singletonList((Type) IntUtils.getTypeByWidth(m));
        } else if ((a instanceof IntegerType) && (b instanceof FloatType)) {
            return Collections.singletonList(FloatType.v());
        } else {
            if ((b instanceof IntegerType) && (a instanceof FloatType)) {
                return Collections.singletonList(FloatType.v());
            }
            if ((a instanceof PrimType) || (b instanceof PrimType)) {
                return Collections.emptyList();
            }
            if (a instanceof NullType) {
                return Collections.singletonList(b);
            }
            if (b instanceof NullType) {
                return Collections.singletonList(a);
            }
            if ((a instanceof ArrayType) && (b instanceof ArrayType)) {
                Type eta = ((ArrayType) a).getElementType();
                Type etb = ((ArrayType) b).getElementType();
                if ((eta instanceof PrimType) || (etb instanceof PrimType)) {
                    ts = Collections.emptyList();
                } else {
                    ts = lcas_(eta, etb);
                }
                LinkedList<Type> r = new LinkedList<>();
                if (ts.isEmpty()) {
                    if (useWeakObjectType) {
                        r.add(new WeakObjectType(Scene.v().getObjectType().toString()));
                    } else {
                        r.add(RefType.v(Scene.v().getObjectType().toString()));
                        r.add(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE));
                        r.add(RefType.v("java.lang.Cloneable"));
                    }
                } else {
                    for (Type t : ts) {
                        r.add(t.makeArrayType());
                    }
                }
                return r;
            } else if ((a instanceof ArrayType) || (b instanceof ArrayType)) {
                Type rt = a instanceof ArrayType ? b : a;
                LinkedList<Type> r2 = new LinkedList<>();
                if (!TypeResolver.typesEqual(Scene.v().getObjectType(), rt)) {
                    RefType refSerializable = RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE);
                    if (ancestor_(refSerializable, rt)) {
                        r2.add(refSerializable);
                    }
                    RefType refCloneable = RefType.v("java.lang.Cloneable");
                    if (ancestor_(refCloneable, rt)) {
                        r2.add(refCloneable);
                    }
                }
                if (r2.isEmpty()) {
                    r2.add(Scene.v().getObjectType());
                }
                return r2;
            } else {
                Collection<AncestryTreeNode> treea = buildAncestryTree((RefType) a);
                Collection<AncestryTreeNode> treeb = buildAncestryTree((RefType) b);
                LinkedList<Type> r3 = new LinkedList<>();
                for (AncestryTreeNode nodea : treea) {
                    for (AncestryTreeNode nodeb : treeb) {
                        RefType t2 = leastCommonNode(nodea, nodeb);
                        boolean least = true;
                        ListIterator<Type> i = r3.listIterator();
                        while (true) {
                            if (!i.hasNext()) {
                                break;
                            }
                            Type t_ = i.next();
                            if (ancestor_(t2, t_)) {
                                least = false;
                                break;
                            } else if (ancestor_(t_, t2)) {
                                i.remove();
                            }
                        }
                        if (least) {
                            r3.add(t2);
                        }
                    }
                }
                if (r3.isEmpty()) {
                    r3.add(Scene.v().getObjectType());
                }
                return r3;
            }
        }
    }

    public static boolean ancestor_(Type ancestor, Type child) {
        if (TypeResolver.typesEqual(ancestor, child) || (child instanceof BottomType)) {
            return true;
        }
        if (ancestor instanceof BottomType) {
            return false;
        }
        if ((ancestor instanceof IntegerType) && (child instanceof IntegerType)) {
            return true;
        }
        if ((ancestor instanceof PrimType) || (child instanceof PrimType)) {
            return false;
        }
        if (child instanceof NullType) {
            return true;
        }
        if (ancestor instanceof NullType) {
            return false;
        }
        return Scene.v().getOrMakeFastHierarchy().canStoreType(child, ancestor);
    }

    private static Deque<RefType> superclassPath(RefType t, RefType anchor) {
        Deque<RefType> r = new ArrayDeque<>();
        r.addFirst(t);
        if (TypeResolver.typesEqual(t, anchor)) {
            return r;
        }
        SootClass sc = t.getSootClass();
        while (sc.hasSuperclass()) {
            sc = sc.getSuperclass();
            RefType cur = sc.getType();
            r.addFirst(cur);
            if (TypeResolver.typesEqual(cur, anchor)) {
                break;
            }
        }
        if (!TypeResolver.typesEqual(r.getFirst(), anchor)) {
            r.addFirst(anchor);
        }
        return r;
    }

    public static RefType lcsc(RefType a, RefType b) {
        return lcsc(a, b, Scene.v().getObjectType());
    }

    public static RefType lcsc(RefType a, RefType b, RefType anchor) {
        if (a == b) {
            return a;
        }
        Deque<RefType> pathA = superclassPath(a, anchor);
        Deque<RefType> pathB = superclassPath(b, anchor);
        RefType r = null;
        while (!pathA.isEmpty() && !pathB.isEmpty() && TypeResolver.typesEqual(pathA.getFirst(), pathB.getFirst())) {
            r = pathA.removeFirst();
            pathB.removeFirst();
        }
        return r;
    }

    @Override // soot.jimple.toolkits.typing.fast.IHierarchy
    public Collection<Type> lcas(Type a, Type b, boolean useWeakObjectType) {
        return lcas_(a, b, useWeakObjectType);
    }

    @Override // soot.jimple.toolkits.typing.fast.IHierarchy
    public boolean ancestor(Type ancestor, Type child) {
        return ancestor_(ancestor, child);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/BytecodeHierarchy$AncestryTreeNode.class */
    public static class AncestryTreeNode {
        public final AncestryTreeNode next;
        public final RefType type;

        public AncestryTreeNode(AncestryTreeNode next, RefType type) {
            this.next = next;
            this.type = type;
        }
    }
}
