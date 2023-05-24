package org.mockito.internal.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.collections.ListUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/Fields.class */
public abstract class Fields {
    public static InstanceFields allDeclaredFieldsOf(Object instance) {
        List<InstanceField> instanceFields = new ArrayList<>();
        Class<?> cls = instance.getClass();
        while (true) {
            Class<?> clazz = cls;
            if (clazz != Object.class) {
                instanceFields.addAll(instanceFieldsIn(instance, clazz.getDeclaredFields()));
                cls = clazz.getSuperclass();
            } else {
                return new InstanceFields(instance, instanceFields);
            }
        }
    }

    public static InstanceFields declaredFieldsOf(Object instance) {
        List<InstanceField> instanceFields = new ArrayList<>();
        instanceFields.addAll(instanceFieldsIn(instance, instance.getClass().getDeclaredFields()));
        return new InstanceFields(instance, instanceFields);
    }

    private static List<InstanceField> instanceFieldsIn(Object instance, Field[] fields) {
        List<InstanceField> instanceDeclaredFields = new ArrayList<>();
        for (Field field : fields) {
            InstanceField instanceField = new InstanceField(field, instance);
            instanceDeclaredFields.add(instanceField);
        }
        return instanceDeclaredFields;
    }

    public static ListUtil.Filter<InstanceField> annotatedBy(final Class<? extends Annotation>... annotations) {
        return new ListUtil.Filter<InstanceField>() { // from class: org.mockito.internal.util.reflection.Fields.1
            @Override // org.mockito.internal.util.collections.ListUtil.Filter
            public boolean isOut(InstanceField instanceField) {
                Class<? extends Annotation>[] clsArr;
                Checks.checkNotNull(annotations, "Provide at least one annotation class");
                for (Class<? extends Annotation> annotation : annotations) {
                    if (instanceField.isAnnotatedBy(annotation)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ListUtil.Filter<InstanceField> nullField() {
        return new ListUtil.Filter<InstanceField>() { // from class: org.mockito.internal.util.reflection.Fields.2
            @Override // org.mockito.internal.util.collections.ListUtil.Filter
            public boolean isOut(InstanceField instanceField) {
                return instanceField.isNull();
            }
        };
    }

    public static ListUtil.Filter<InstanceField> syntheticField() {
        return new ListUtil.Filter<InstanceField>() { // from class: org.mockito.internal.util.reflection.Fields.3
            @Override // org.mockito.internal.util.collections.ListUtil.Filter
            public boolean isOut(InstanceField instanceField) {
                return instanceField.isSynthetic();
            }
        };
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/Fields$InstanceFields.class */
    public static class InstanceFields {
        private final Object instance;
        private final List<InstanceField> instanceFields;

        public InstanceFields(Object instance, List<InstanceField> instanceFields) {
            this.instance = instance;
            this.instanceFields = instanceFields;
        }

        public InstanceFields filter(ListUtil.Filter<InstanceField> withFilter) {
            return new InstanceFields(this.instance, ListUtil.filter(this.instanceFields, withFilter));
        }

        public InstanceFields notNull() {
            return filter(Fields.nullField());
        }

        public List<InstanceField> instanceFields() {
            return new ArrayList(this.instanceFields);
        }

        public List<Object> assignedValues() {
            List<Object> values = new ArrayList<>(this.instanceFields.size());
            for (InstanceField instanceField : this.instanceFields) {
                values.add(instanceField.read());
            }
            return values;
        }

        public List<String> names() {
            List<String> fieldNames = new ArrayList<>(this.instanceFields.size());
            for (InstanceField instanceField : this.instanceFields) {
                fieldNames.add(instanceField.name());
            }
            return fieldNames;
        }
    }
}
