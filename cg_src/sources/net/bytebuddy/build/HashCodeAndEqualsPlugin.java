package net.bytebuddy.build;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;
import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.EqualsMethod;
import net.bytebuddy.implementation.HashCodeMethod;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
@Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin.class */
public class HashCodeAndEqualsPlugin implements Plugin, Plugin.Factory {

    @Target({ElementType.TYPE})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$Enhance.class */
    public @interface Enhance {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$Enhance$InvokeSuper.class */
        public enum InvokeSuper {
            IF_DECLARED { // from class: net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper.1
                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected HashCodeMethod hashCodeMethod(TypeDescription instrumentedType) {
                    TypeDefinition superClass = instrumentedType.getSuperClass();
                    while (true) {
                        TypeDefinition typeDefinition = superClass;
                        if (typeDefinition == null || typeDefinition.represents(Object.class)) {
                            break;
                        } else if (typeDefinition.asErasure().getDeclaredAnnotations().isAnnotationPresent(Enhance.class)) {
                            return HashCodeMethod.usingSuperClassOffset();
                        } else {
                            MethodList hashCode = typeDefinition.getDeclaredMethods().filter(ElementMatchers.isHashCode());
                            if (!hashCode.isEmpty()) {
                                if (((MethodDescription) hashCode.getOnly()).isAbstract()) {
                                    return HashCodeMethod.usingDefaultOffset();
                                }
                                return HashCodeMethod.usingSuperClassOffset();
                            }
                            superClass = typeDefinition.getSuperClass();
                        }
                    }
                    return HashCodeMethod.usingDefaultOffset();
                }

                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected EqualsMethod equalsMethod(TypeDescription instrumentedType) {
                    TypeDefinition superClass = instrumentedType.getSuperClass();
                    while (true) {
                        TypeDefinition typeDefinition = superClass;
                        if (typeDefinition == null || typeDefinition.represents(Object.class)) {
                            break;
                        } else if (typeDefinition.asErasure().getDeclaredAnnotations().isAnnotationPresent(Enhance.class)) {
                            return EqualsMethod.requiringSuperClassEquality();
                        } else {
                            MethodList hashCode = typeDefinition.getDeclaredMethods().filter(ElementMatchers.isHashCode());
                            if (!hashCode.isEmpty()) {
                                if (((MethodDescription) hashCode.getOnly()).isAbstract()) {
                                    return EqualsMethod.isolated();
                                }
                                return EqualsMethod.requiringSuperClassEquality();
                            }
                            superClass = typeDefinition.getSuperClass().asErasure();
                        }
                    }
                    return EqualsMethod.isolated();
                }
            },
            IF_ANNOTATED { // from class: net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper.2
                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected HashCodeMethod hashCodeMethod(TypeDescription instrumentedType) {
                    TypeDefinition superClass = instrumentedType.getSuperClass();
                    if (superClass != null && superClass.asErasure().getDeclaredAnnotations().isAnnotationPresent(Enhance.class)) {
                        return HashCodeMethod.usingSuperClassOffset();
                    }
                    return HashCodeMethod.usingDefaultOffset();
                }

                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected EqualsMethod equalsMethod(TypeDescription instrumentedType) {
                    TypeDefinition superClass = instrumentedType.getSuperClass();
                    if (superClass != null && superClass.asErasure().getDeclaredAnnotations().isAnnotationPresent(Enhance.class)) {
                        return EqualsMethod.requiringSuperClassEquality();
                    }
                    return EqualsMethod.isolated();
                }
            },
            ALWAYS { // from class: net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper.3
                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected HashCodeMethod hashCodeMethod(TypeDescription instrumentedType) {
                    return HashCodeMethod.usingSuperClassOffset();
                }

                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected EqualsMethod equalsMethod(TypeDescription instrumentedType) {
                    return EqualsMethod.requiringSuperClassEquality();
                }
            },
            NEVER { // from class: net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper.4
                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected HashCodeMethod hashCodeMethod(TypeDescription instrumentedType) {
                    return HashCodeMethod.usingDefaultOffset();
                }

                @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin.Enhance.InvokeSuper
                protected EqualsMethod equalsMethod(TypeDescription instrumentedType) {
                    return EqualsMethod.isolated();
                }
            };

            protected abstract HashCodeMethod hashCodeMethod(TypeDescription typeDescription);

            protected abstract EqualsMethod equalsMethod(TypeDescription typeDescription);
        }

        InvokeSuper invokeSuper() default InvokeSuper.IF_DECLARED;

        boolean simpleComparisonsFirst() default true;

        boolean includeSyntheticFields() default false;

        boolean permitSubclassEquality() default false;
    }

    @Target({ElementType.FIELD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$Sorted.class */
    public @interface Sorted {
        public static final int DEFAULT = 0;

        int value();
    }

    @Target({ElementType.FIELD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$ValueHandling.class */
    public @interface ValueHandling {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$ValueHandling$Sort.class */
        public enum Sort {
            IGNORE,
            REVERSE_NULLABILITY
        }

        Sort value();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass();
    }

    public int hashCode() {
        return 17;
    }

    @Override // net.bytebuddy.build.Plugin.Factory
    public Plugin make() {
        return this;
    }

    @Override // net.bytebuddy.matcher.ElementMatcher
    public boolean matches(TypeDescription target) {
        return target.getDeclaredAnnotations().isAnnotationPresent(Enhance.class);
    }

    @Override // net.bytebuddy.build.Plugin
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        ElementMatcher.Junction isSynthetic;
        ElementMatcher.Junction isSynthetic2;
        Enhance enhance = (Enhance) typeDescription.getDeclaredAnnotations().ofType(Enhance.class).load();
        if (typeDescription.getDeclaredMethods().filter(ElementMatchers.isHashCode()).isEmpty()) {
            DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> method = builder.method(ElementMatchers.isHashCode());
            HashCodeMethod hashCodeMethod = enhance.invokeSuper().hashCodeMethod(typeDescription);
            if (enhance.includeSyntheticFields()) {
                isSynthetic2 = ElementMatchers.none();
            } else {
                isSynthetic2 = ElementMatchers.isSynthetic();
            }
            builder = method.intercept(hashCodeMethod.withIgnoredFields(isSynthetic2).withIgnoredFields(new ValueMatcher(ValueHandling.Sort.IGNORE)).withNonNullableFields(nonNullable(new ValueMatcher(ValueHandling.Sort.REVERSE_NULLABILITY))));
        }
        if (typeDescription.getDeclaredMethods().filter(ElementMatchers.isEquals()).isEmpty()) {
            EqualsMethod equalsMethod = enhance.invokeSuper().equalsMethod(typeDescription);
            if (enhance.includeSyntheticFields()) {
                isSynthetic = ElementMatchers.none();
            } else {
                isSynthetic = ElementMatchers.isSynthetic();
            }
            EqualsMethod equalsMethod2 = equalsMethod.withIgnoredFields(isSynthetic).withIgnoredFields(new ValueMatcher(ValueHandling.Sort.IGNORE)).withNonNullableFields(nonNullable(new ValueMatcher(ValueHandling.Sort.REVERSE_NULLABILITY))).withFieldOrder(AnnotationOrderComparator.INSTANCE);
            if (enhance.simpleComparisonsFirst()) {
                equalsMethod2 = equalsMethod2.withPrimitiveTypedFieldsFirst().withEnumerationTypedFieldsFirst().withPrimitiveWrapperTypedFieldsFirst().withStringTypedFieldsFirst();
            }
            builder = builder.method(ElementMatchers.isEquals()).intercept(enhance.permitSubclassEquality() ? equalsMethod2.withSubclassEquality() : equalsMethod2);
        }
        return builder;
    }

    protected ElementMatcher<FieldDescription> nonNullable(ElementMatcher<FieldDescription> matcher) {
        return matcher;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$WithNonNullableFields.class */
    public static class WithNonNullableFields extends HashCodeAndEqualsPlugin {
        @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass();
            }
            return false;
        }

        @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin
        public int hashCode() {
            return super.hashCode();
        }

        @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin, net.bytebuddy.matcher.ElementMatcher
        public /* bridge */ /* synthetic */ boolean matches(TypeDescription typeDescription) {
            return super.matches(typeDescription);
        }

        @Override // net.bytebuddy.build.HashCodeAndEqualsPlugin
        protected ElementMatcher<FieldDescription> nonNullable(ElementMatcher<FieldDescription> matcher) {
            return ElementMatchers.not(matcher);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$AnnotationOrderComparator.class */
    protected enum AnnotationOrderComparator implements Comparator<FieldDescription.InDefinedShape> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(FieldDescription.InDefinedShape left, FieldDescription.InDefinedShape right) {
            AnnotationDescription.Loadable<Sorted> leftAnnotation = left.getDeclaredAnnotations().ofType(Sorted.class);
            AnnotationDescription.Loadable<Sorted> rightAnnotation = right.getDeclaredAnnotations().ofType(Sorted.class);
            int leftValue = leftAnnotation == null ? 0 : leftAnnotation.load().value();
            int rightValue = rightAnnotation == null ? 0 : rightAnnotation.load().value();
            if (leftValue > rightValue) {
                return -1;
            }
            if (leftValue < rightValue) {
                return 1;
            }
            return 0;
        }
    }

    @Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/HashCodeAndEqualsPlugin$ValueMatcher.class */
    protected static class ValueMatcher implements ElementMatcher<FieldDescription> {
        private final ValueHandling.Sort sort;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.sort.equals(((ValueMatcher) obj).sort);
        }

        public int hashCode() {
            return (17 * 31) + this.sort.hashCode();
        }

        protected ValueMatcher(ValueHandling.Sort sort) {
            this.sort = sort;
        }

        @Override // net.bytebuddy.matcher.ElementMatcher
        public boolean matches(FieldDescription target) {
            AnnotationDescription.Loadable<ValueHandling> annotation = target.getDeclaredAnnotations().ofType(ValueHandling.class);
            return annotation != null && annotation.load().value() == this.sort;
        }
    }
}
