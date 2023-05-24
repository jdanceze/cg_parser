package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.internal.MethodSorter;
import soot.jimple.Jimple;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/TestClass.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/TestClass.class */
public class TestClass implements Annotatable {
    private static final FieldComparator FIELD_COMPARATOR = new FieldComparator();
    private static final MethodComparator METHOD_COMPARATOR = new MethodComparator();
    private final Class<?> clazz;
    private final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations;
    private final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations;

    public TestClass(Class<?> clazz) {
        this.clazz = clazz;
        if (clazz != null && clazz.getConstructors().length > 1) {
            throw new IllegalArgumentException("Test class can only have one constructor");
        }
        Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations = new LinkedHashMap<>();
        Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations = new LinkedHashMap<>();
        scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        this.methodsForAnnotations = makeDeeplyUnmodifiable(methodsForAnnotations);
        this.fieldsForAnnotations = makeDeeplyUnmodifiable(fieldsForAnnotations);
    }

    protected void scanAnnotatedMembers(Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations, Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
        for (Class<?> eachClass : getSuperClasses(this.clazz)) {
            Method[] arr$ = MethodSorter.getDeclaredMethods(eachClass);
            for (Method eachMethod : arr$) {
                addToAnnotationLists(new FrameworkMethod(eachMethod), methodsForAnnotations);
            }
            Field[] arr$2 = getSortedDeclaredFields(eachClass);
            for (Field eachField : arr$2) {
                addToAnnotationLists(new FrameworkField(eachField), fieldsForAnnotations);
            }
        }
    }

    private static Field[] getSortedDeclaredFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Arrays.sort(declaredFields, FIELD_COMPARATOR);
        return declaredFields;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected static <T extends FrameworkMember<T>> void addToAnnotationLists(T member, Map<Class<? extends Annotation>, List<T>> map) {
        Annotation[] arr$ = member.getAnnotations();
        for (Annotation each : arr$) {
            Class<? extends Annotation> type = each.annotationType();
            List annotatedMembers = getAnnotatedMembers(map, type, true);
            FrameworkMember handlePossibleBridgeMethod = member.handlePossibleBridgeMethod(annotatedMembers);
            if (handlePossibleBridgeMethod == null) {
                return;
            }
            if (runsTopToBottom(type)) {
                annotatedMembers.add(0, handlePossibleBridgeMethod);
            } else {
                annotatedMembers.add(handlePossibleBridgeMethod);
            }
        }
    }

    private static <T extends FrameworkMember<T>> Map<Class<? extends Annotation>, List<T>> makeDeeplyUnmodifiable(Map<Class<? extends Annotation>, List<T>> source) {
        Map<Class<? extends Annotation>, List<T>> copy = new LinkedHashMap<>();
        for (Map.Entry<Class<? extends Annotation>, List<T>> entry : source.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    public List<FrameworkMethod> getAnnotatedMethods() {
        List<FrameworkMethod> methods = collectValues(this.methodsForAnnotations);
        Collections.sort(methods, METHOD_COMPARATOR);
        return methods;
    }

    public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList(getAnnotatedMembers(this.methodsForAnnotations, annotationClass, false));
    }

    public List<FrameworkField> getAnnotatedFields() {
        return collectValues(this.fieldsForAnnotations);
    }

    public List<FrameworkField> getAnnotatedFields(Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList(getAnnotatedMembers(this.fieldsForAnnotations, annotationClass, false));
    }

    private <T> List<T> collectValues(Map<?, List<T>> map) {
        Set<T> values = new LinkedHashSet<>();
        for (List<T> additionalValues : map.values()) {
            values.addAll(additionalValues);
        }
        return new ArrayList(values);
    }

    private static <T> List<T> getAnnotatedMembers(Map<Class<? extends Annotation>, List<T>> map, Class<? extends Annotation> type, boolean fillIfAbsent) {
        if (!map.containsKey(type) && fillIfAbsent) {
            map.put(type, new ArrayList());
        }
        List<T> members = map.get(type);
        return members == null ? Collections.emptyList() : members;
    }

    private static boolean runsTopToBottom(Class<? extends Annotation> annotation) {
        return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
    }

    private static List<Class<?>> getSuperClasses(Class<?> testClass) {
        List<Class<?>> results = new ArrayList<>();
        Class<?> cls = testClass;
        while (true) {
            Class<?> current = cls;
            if (current != null) {
                results.add(current);
                cls = current.getSuperclass();
            } else {
                return results;
            }
        }
    }

    public Class<?> getJavaClass() {
        return this.clazz;
    }

    public String getName() {
        if (this.clazz == null) {
            return Jimple.NULL;
        }
        return this.clazz.getName();
    }

    public Constructor<?> getOnlyConstructor() {
        Constructor<?>[] constructors = this.clazz.getConstructors();
        Assert.assertEquals(1L, constructors.length);
        return constructors[0];
    }

    @Override // org.junit.runners.model.Annotatable
    public Annotation[] getAnnotations() {
        if (this.clazz == null) {
            return new Annotation[0];
        }
        return this.clazz.getAnnotations();
    }

    @Override // org.junit.runners.model.Annotatable
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        if (this.clazz == null) {
            return null;
        }
        return (T) this.clazz.getAnnotation(annotationType);
    }

    public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        final List<T> results = new ArrayList<>();
        collectAnnotatedFieldValues(test, annotationClass, valueClass, new MemberValueConsumer<T>() { // from class: org.junit.runners.model.TestClass.1
            @Override // org.junit.runners.model.MemberValueConsumer
            public void accept(FrameworkMember<?> member, T value) {
                results.add(value);
            }
        });
        return results;
    }

    public <T> void collectAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass, MemberValueConsumer<T> consumer) {
        for (FrameworkField each : getAnnotatedFields(annotationClass)) {
            try {
                Object fieldValue = each.get(test);
                if (valueClass.isInstance(fieldValue)) {
                    consumer.accept(each, valueClass.cast(fieldValue));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("How did getFields return a field we couldn't access?", e);
            }
        }
    }

    public <T> List<T> getAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        final List<T> results = new ArrayList<>();
        collectAnnotatedMethodValues(test, annotationClass, valueClass, new MemberValueConsumer<T>() { // from class: org.junit.runners.model.TestClass.2
            @Override // org.junit.runners.model.MemberValueConsumer
            public void accept(FrameworkMember<?> member, T value) {
                results.add(value);
            }
        });
        return results;
    }

    public <T> void collectAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass, MemberValueConsumer<T> consumer) {
        for (FrameworkMethod each : getAnnotatedMethods(annotationClass)) {
            try {
                if (valueClass.isAssignableFrom(each.getReturnType())) {
                    Object fieldValue = each.invokeExplosively(test, new Object[0]);
                    consumer.accept(each, valueClass.cast(fieldValue));
                }
            } catch (Throwable e) {
                throw new RuntimeException("Exception in " + each.getName(), e);
            }
        }
    }

    public boolean isPublic() {
        return Modifier.isPublic(this.clazz.getModifiers());
    }

    public boolean isANonStaticInnerClass() {
        return this.clazz.isMemberClass() && !Modifier.isStatic(this.clazz.getModifiers());
    }

    public int hashCode() {
        if (this.clazz == null) {
            return 0;
        }
        return this.clazz.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TestClass other = (TestClass) obj;
        return this.clazz == other.clazz;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/TestClass$FieldComparator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/TestClass$FieldComparator.class */
    public static class FieldComparator implements Comparator<Field> {
        private FieldComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Field left, Field right) {
            return left.getName().compareTo(right.getName());
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/TestClass$MethodComparator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/TestClass$MethodComparator.class */
    private static class MethodComparator implements Comparator<FrameworkMethod> {
        private MethodComparator() {
        }

        @Override // java.util.Comparator
        public int compare(FrameworkMethod left, FrameworkMethod right) {
            return MethodSorter.NAME_ASCENDING.compare(left.getMethod(), right.getMethod());
        }
    }
}
