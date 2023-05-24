package fj.test.reflect;

import fj.Bottom;
import fj.Class;
import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.data.Array;
import fj.data.List;
import fj.data.Option;
import fj.test.CheckResult;
import fj.test.Property;
import fj.test.Rand;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/reflect/Check.class */
public final class Check {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/reflect/Check$PropertyMember.class */
    public interface PropertyMember {
        Class<?> type();

        AnnotatedElement element();

        String name();

        int modifiers();

        <X> Property invoke(X x) throws Exception;

        boolean isProperty();
    }

    private Check() {
        throw new UnsupportedOperationException();
    }

    public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, String... categories) {
        return check(c, Rand.standard, categories);
    }

    public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, List<String> categories) {
        return check(c, Rand.standard, categories.toArray().array(String[].class));
    }

    public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, final Rand r, final String... categories) {
        return List.join(c.map((F<Class<T>, List<P2<String, CheckResult>>>) new F<Class<T>, List<P2<String, CheckResult>>>() { // from class: fj.test.reflect.Check.1
            @Override // fj.F
            public List<P2<String, CheckResult>> f(Class<T> c2) {
                return Check.check(c2, Rand.this, categories);
            }
        }));
    }

    public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, Rand r, List<String> categories) {
        return check(c, r, categories.toArray().array(String[].class));
    }

    public static <T> List<P2<String, CheckResult>> check(Class<T> c, String... categories) {
        return check(c, Rand.standard, categories);
    }

    public static <T> List<P2<String, CheckResult>> check(Class<T> c, List<String> categories) {
        return check(c, Rand.standard, categories.toArray().array(String[].class));
    }

    public static <T> List<P2<String, CheckResult>> check(Class<T> c, final Rand r, final String... categories) {
        return List.join(Class.clas(c).inheritance().map((F<Class<? super T>, List<P3<Property, String, Option<CheckParams>>>>) new F<Class<? super T>, List<P3<Property, String, Option<CheckParams>>>>() { // from class: fj.test.reflect.Check.3
            @Override // fj.F
            public List<P3<Property, String, Option<CheckParams>>> f(Class<? super T> c2) {
                return Check.properties(c2.clas(), categories);
            }
        })).map(new F<P3<Property, String, Option<CheckParams>>, P2<String, CheckResult>>() { // from class: fj.test.reflect.Check.2
            @Override // fj.F
            public P2<String, CheckResult> f(P3<Property, String, Option<CheckParams>> p) {
                if (p._3().isSome()) {
                    CheckParams ps = p._3().some();
                    return P.p(p._2(), p._1().check(Rand.this, ps.minSuccessful(), ps.maxDiscarded(), ps.minSize(), ps.maxSize()));
                }
                return P.p(p._2(), p._1().check(Rand.this));
            }
        });
    }

    public static <T> List<P2<String, CheckResult>> check(Class<T> c, Rand r, List<String> categories) {
        return check(c, r, categories.toArray().array(String[].class));
    }

    public static <U, T extends U> List<P3<Property, String, Option<CheckParams>>> properties(Class<T> c, String... categories) {
        Array<P3<Property, String, Option<CheckParams>>> propFields = properties(Array.array(c.getDeclaredFields()).map(new F<Field, PropertyMember>() { // from class: fj.test.reflect.Check.4
            @Override // fj.F
            public PropertyMember f(final Field f) {
                return new PropertyMember() { // from class: fj.test.reflect.Check.4.1
                    @Override // fj.test.reflect.Check.PropertyMember
                    public Class<?> type() {
                        return f.getType();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public AnnotatedElement element() {
                        return f;
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public String name() {
                        return f.getName();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public int modifiers() {
                        return f.getModifiers();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public <X> Property invoke(X x) throws IllegalAccessException {
                        f.setAccessible(true);
                        return (Property) f.get(x);
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public boolean isProperty() {
                        return true;
                    }
                };
            }
        }), c, categories);
        Array<P3<Property, String, Option<CheckParams>>> propMethods = properties(Array.array(c.getDeclaredMethods()).map(new F<Method, PropertyMember>() { // from class: fj.test.reflect.Check.5
            @Override // fj.F
            public PropertyMember f(final Method m) {
                return new PropertyMember() { // from class: fj.test.reflect.Check.5.1
                    @Override // fj.test.reflect.Check.PropertyMember
                    public Class<?> type() {
                        return m.getReturnType();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public AnnotatedElement element() {
                        return m;
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public String name() {
                        return m.getName();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public int modifiers() {
                        return m.getModifiers();
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public <X> Property invoke(X x) throws Exception {
                        m.setAccessible(true);
                        return (Property) m.invoke(x, new Object[0]);
                    }

                    @Override // fj.test.reflect.Check.PropertyMember
                    public boolean isProperty() {
                        return m.getParameterTypes().length == 0;
                    }
                };
            }
        }), c, categories);
        return propFields.append(propMethods).toList();
    }

    private static <T> Array<P3<Property, String, Option<CheckParams>>> properties(Array<PropertyMember> ms, final Class<T> declaringClass, final String... categories) {
        final Option<T> t = emptyCtor(declaringClass).map(new F<Constructor<T>, T>() { // from class: fj.test.reflect.Check.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Constructor<Object>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v5, types: [T, java.lang.Object] */
            public T f(Constructor<T> ctor) {
                try {
                    ctor.setAccessible(true);
                    return ctor.newInstance(new Object[0]);
                } catch (Exception e) {
                    throw Bottom.error(e.toString());
                }
            }
        });
        final F<AnnotatedElement, F<String, Boolean>> p = new F<AnnotatedElement, F<String, Boolean>>() { // from class: fj.test.reflect.Check.7
            @Override // fj.F
            public F<String, Boolean> f(final AnnotatedElement e) {
                return new F<String, Boolean>() { // from class: fj.test.reflect.Check.7.1
                    @Override // fj.F
                    public Boolean f(final String s) {
                        F<Category, Boolean> p2 = new F<Category, Boolean>() { // from class: fj.test.reflect.Check.7.1.1
                            @Override // fj.F
                            public Boolean f(Category c) {
                                return Boolean.valueOf(Array.array(c.value()).exists(new F<String, Boolean>() { // from class: fj.test.reflect.Check.7.1.1.1
                                    @Override // fj.F
                                    public Boolean f(String cs) {
                                        return Boolean.valueOf(cs.equals(s));
                                    }
                                }));
                            }
                        };
                        List<Boolean> bss = Option.somes(List.list(Option.fromNull(e.getAnnotation(Category.class)).map(p2), Option.fromNull(declaringClass.getAnnotation(Category.class)).map(p2)));
                        return Boolean.valueOf(bss.exists(Function.identity()));
                    }
                };
            }
        };
        final F<Name, String> nameS = new F<Name, String>() { // from class: fj.test.reflect.Check.8
            @Override // fj.F
            public String f(Name name) {
                return name.value();
            }
        };
        return ms.filter(new F<PropertyMember, Boolean>() { // from class: fj.test.reflect.Check.10
            @Override // fj.F
            public Boolean f(PropertyMember m) {
                return Boolean.valueOf(m.isProperty() && m.type() == Property.class && !m.element().isAnnotationPresent(NoCheck.class) && !declaringClass.isAnnotationPresent(NoCheck.class) && (categories.length == 0 || Array.array(categories).exists((F) p.f(m.element()))) && (t.isSome() || Modifier.isStatic(m.modifiers())));
            }
        }).map(new F<PropertyMember, P3<Property, String, Option<CheckParams>>>() { // from class: fj.test.reflect.Check.9
            @Override // fj.F
            public P3<Property, String, Option<CheckParams>> f(PropertyMember m) {
                try {
                    Option<CheckParams> params = Option.fromNull(m.element().getAnnotation(CheckParams.class)).orElse(Option.fromNull(declaringClass.getAnnotation(CheckParams.class)));
                    String name = (String) Option.fromNull(m.element().getAnnotation(Name.class)).map(nameS).orSome((Option) m.name());
                    return P.p(m.invoke(t.orSome((P1<Object>) P.p(null))), name, params);
                } catch (Exception e) {
                    throw Bottom.error(e.toString());
                }
            }
        });
    }

    private static <T> Option<Constructor<T>> emptyCtor(Class<T> c) {
        Option<Constructor<T>> ctor;
        try {
            ctor = Option.some(c.getDeclaredConstructor(new Class[0]));
        } catch (NoSuchMethodException e) {
            ctor = Option.none();
        }
        return ctor;
    }
}
