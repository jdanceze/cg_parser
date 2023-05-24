package fj;

import fj.data.List;
import fj.data.Option;
import fj.data.Tree;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Class.class */
public final class Class<T> {
    private final java.lang.Class<T> c;

    private Class(java.lang.Class<T> c) {
        this.c = c;
    }

    public List<Class<? super T>> inheritance() {
        return List.unfold(new F<java.lang.Class<? super T>, Option<P2<java.lang.Class<? super T>, java.lang.Class<? super T>>>>() { // from class: fj.Class.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((java.lang.Class) ((java.lang.Class) obj));
            }

            public Option<P2<java.lang.Class<? super T>, java.lang.Class<? super T>>> f(final java.lang.Class<? super T> c) {
                if (c == null) {
                    return Option.none();
                }
                P2<java.lang.Class<? super T>, java.lang.Class<? super T>> p = new P2<java.lang.Class<? super T>, java.lang.Class<? super T>>() { // from class: fj.Class.2.1
                    @Override // fj.P2
                    public java.lang.Class<? super T> _1() {
                        return c;
                    }

                    @Override // fj.P2
                    public java.lang.Class<? super T> _2() {
                        return c.getSuperclass();
                    }
                };
                return Option.some(p);
            }
        }, this.c).map(new F<java.lang.Class<? super T>, Class<? super T>>() { // from class: fj.Class.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((java.lang.Class) ((java.lang.Class) obj));
            }

            public Class<? super T> f(java.lang.Class<? super T> c) {
                return Class.clas(c);
            }
        });
    }

    public Tree<Type> classParameters() {
        return typeParameterTree(this.c);
    }

    public Tree<Type> superclassParameters() {
        return typeParameterTree(this.c.getGenericSuperclass());
    }

    public List<Tree<Type>> interfaceParameters() {
        Type[] interfaces;
        List<Tree<Type>> ts = List.nil();
        for (Type t : this.c.getInterfaces()) {
            ts = ts.snoc(typeParameterTree(t));
        }
        return ts;
    }

    public static Tree<Type> typeParameterTree(Type t) {
        Tree<Type> types;
        Type[] actualTypeArguments;
        List<Tree<Type>> typeArgs = List.nil();
        if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            for (Type arg : pt.getActualTypeArguments()) {
                typeArgs = typeArgs.snoc(typeParameterTree(arg));
            }
            types = Tree.node(pt.getRawType(), typeArgs);
        } else {
            types = Tree.node(t, List.nil());
        }
        return types;
    }

    public java.lang.Class<T> clas() {
        return this.c;
    }

    public static <T> Class<T> clas(java.lang.Class<T> c) {
        return new Class<>(c);
    }
}
