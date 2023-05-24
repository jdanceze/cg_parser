package net.bytebuddy.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.OpenedClassReader;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper.class */
public interface AsmVisitorWrapper {
    public static final int NO_FLAGS = 0;

    int mergeWriter(int i);

    int mergeReader(int i);

    ClassVisitor wrap(TypeDescription typeDescription, ClassVisitor classVisitor, Implementation.Context context, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fieldList, MethodList<?> methodList, int i, int i2);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$NoOp.class */
    public enum NoOp implements AsmVisitorWrapper {
        INSTANCE;

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeWriter(int flags) {
            return flags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeReader(int flags) {
            return flags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            return classVisitor;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$AbstractBase.class */
    public static abstract class AbstractBase implements AsmVisitorWrapper {
        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeWriter(int flags) {
            return flags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeReader(int flags) {
            return flags;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredFields.class */
    public static class ForDeclaredFields extends AbstractBase {
        private final List<Entry> entries;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredFields$FieldVisitorWrapper.class */
        public interface FieldVisitorWrapper {
            FieldVisitor wrap(TypeDescription typeDescription, FieldDescription.InDefinedShape inDefinedShape, FieldVisitor fieldVisitor);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.entries.equals(((ForDeclaredFields) obj).entries);
        }

        public int hashCode() {
            return (17 * 31) + this.entries.hashCode();
        }

        public ForDeclaredFields() {
            this(Collections.emptyList());
        }

        protected ForDeclaredFields(List<Entry> entries) {
            this.entries = entries;
        }

        public ForDeclaredFields field(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, FieldVisitorWrapper... fieldVisitorWrapper) {
            return field(matcher, Arrays.asList(fieldVisitorWrapper));
        }

        public ForDeclaredFields field(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, List<? extends FieldVisitorWrapper> fieldVisitorWrappers) {
            return new ForDeclaredFields(CompoundList.of(this.entries, new Entry(matcher, fieldVisitorWrappers)));
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            Map<String, FieldDescription.InDefinedShape> mapped = new HashMap<>();
            for (FieldDescription.InDefinedShape fieldDescription : fields) {
                mapped.put(fieldDescription.getInternalName() + fieldDescription.getDescriptor(), fieldDescription);
            }
            return new DispatchingVisitor(classVisitor, instrumentedType, mapped);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredFields$Entry.class */
        public static class Entry implements ElementMatcher<FieldDescription.InDefinedShape>, FieldVisitorWrapper {
            private final ElementMatcher<? super FieldDescription.InDefinedShape> matcher;
            private final List<? extends FieldVisitorWrapper> fieldVisitorWrappers;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.fieldVisitorWrappers.equals(((Entry) obj).fieldVisitorWrappers);
            }

            public int hashCode() {
                return (((17 * 31) + this.matcher.hashCode()) * 31) + this.fieldVisitorWrappers.hashCode();
            }

            protected Entry(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, List<? extends FieldVisitorWrapper> fieldVisitorWrappers) {
                this.matcher = matcher;
                this.fieldVisitorWrappers = fieldVisitorWrappers;
            }

            @Override // net.bytebuddy.matcher.ElementMatcher
            public boolean matches(FieldDescription.InDefinedShape target) {
                return target != null && this.matcher.matches(target);
            }

            @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredFields.FieldVisitorWrapper
            public FieldVisitor wrap(TypeDescription instrumentedType, FieldDescription.InDefinedShape fieldDescription, FieldVisitor fieldVisitor) {
                for (FieldVisitorWrapper fieldVisitorWrapper : this.fieldVisitorWrappers) {
                    fieldVisitor = fieldVisitorWrapper.wrap(instrumentedType, fieldDescription, fieldVisitor);
                }
                return fieldVisitor;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredFields$DispatchingVisitor.class */
        protected class DispatchingVisitor extends ClassVisitor {
            private final TypeDescription instrumentedType;
            private final Map<String, FieldDescription.InDefinedShape> fields;

            protected DispatchingVisitor(ClassVisitor classVisitor, TypeDescription instrumentedType, Map<String, FieldDescription.InDefinedShape> fields) {
                super(OpenedClassReader.ASM_API, classVisitor);
                this.instrumentedType = instrumentedType;
                this.fields = fields;
            }

            @Override // net.bytebuddy.jar.asm.ClassVisitor
            public FieldVisitor visitField(int modifiers, String internalName, String descriptor, String signature, Object defaultValue) {
                FieldVisitor fieldVisitor = super.visitField(modifiers, internalName, descriptor, signature, defaultValue);
                FieldDescription.InDefinedShape fieldDescription = this.fields.get(internalName + descriptor);
                if (fieldVisitor != null && fieldDescription != null) {
                    for (Entry entry : ForDeclaredFields.this.entries) {
                        if (entry.matches(fieldDescription)) {
                            fieldVisitor = entry.wrap(this.instrumentedType, fieldDescription, fieldVisitor);
                        }
                    }
                }
                return fieldVisitor;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredMethods.class */
    public static class ForDeclaredMethods implements AsmVisitorWrapper {
        private final List<Entry> entries;
        private final int writerFlags;
        private final int readerFlags;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredMethods$MethodVisitorWrapper.class */
        public interface MethodVisitorWrapper {
            MethodVisitor wrap(TypeDescription typeDescription, MethodDescription methodDescription, MethodVisitor methodVisitor, Implementation.Context context, TypePool typePool, int i, int i2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.writerFlags == ((ForDeclaredMethods) obj).writerFlags && this.readerFlags == ((ForDeclaredMethods) obj).readerFlags && this.entries.equals(((ForDeclaredMethods) obj).entries);
        }

        public int hashCode() {
            return (((((17 * 31) + this.entries.hashCode()) * 31) + this.writerFlags) * 31) + this.readerFlags;
        }

        public ForDeclaredMethods() {
            this(Collections.emptyList(), 0, 0);
        }

        protected ForDeclaredMethods(List<Entry> entries, int writerFlags, int readerFlags) {
            this.entries = entries;
            this.writerFlags = writerFlags;
            this.readerFlags = readerFlags;
        }

        public ForDeclaredMethods method(ElementMatcher<? super MethodDescription> matcher, MethodVisitorWrapper... methodVisitorWrapper) {
            return method(matcher, Arrays.asList(methodVisitorWrapper));
        }

        public ForDeclaredMethods method(ElementMatcher<? super MethodDescription> matcher, List<? extends MethodVisitorWrapper> methodVisitorWrappers) {
            return invokable(ElementMatchers.isMethod().and(matcher), methodVisitorWrappers);
        }

        public ForDeclaredMethods constructor(ElementMatcher<? super MethodDescription> matcher, MethodVisitorWrapper... methodVisitorWrapper) {
            return constructor(matcher, Arrays.asList(methodVisitorWrapper));
        }

        public ForDeclaredMethods constructor(ElementMatcher<? super MethodDescription> matcher, List<? extends MethodVisitorWrapper> methodVisitorWrappers) {
            return invokable(ElementMatchers.isConstructor().and(matcher), methodVisitorWrappers);
        }

        public ForDeclaredMethods invokable(ElementMatcher<? super MethodDescription> matcher, MethodVisitorWrapper... methodVisitorWrapper) {
            return invokable(matcher, Arrays.asList(methodVisitorWrapper));
        }

        public ForDeclaredMethods invokable(ElementMatcher<? super MethodDescription> matcher, List<? extends MethodVisitorWrapper> methodVisitorWrappers) {
            return new ForDeclaredMethods(CompoundList.of(this.entries, new Entry(matcher, methodVisitorWrappers)), this.writerFlags, this.readerFlags);
        }

        public ForDeclaredMethods writerFlags(int flags) {
            return new ForDeclaredMethods(this.entries, this.writerFlags | flags, this.readerFlags);
        }

        public ForDeclaredMethods readerFlags(int flags) {
            return new ForDeclaredMethods(this.entries, this.writerFlags, this.readerFlags | flags);
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeWriter(int flags) {
            return flags | this.writerFlags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeReader(int flags) {
            return flags | this.readerFlags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            Map<String, MethodDescription> mapped = new HashMap<>();
            for (MethodDescription methodDescription : CompoundList.of(methods, new MethodDescription.Latent.TypeInitializer(instrumentedType))) {
                mapped.put(methodDescription.getInternalName() + methodDescription.getDescriptor(), methodDescription);
            }
            return new DispatchingVisitor(classVisitor, instrumentedType, implementationContext, typePool, mapped, writerFlags, readerFlags);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredMethods$Entry.class */
        public static class Entry implements ElementMatcher<MethodDescription>, MethodVisitorWrapper {
            private final ElementMatcher<? super MethodDescription> matcher;
            private final List<? extends MethodVisitorWrapper> methodVisitorWrappers;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.methodVisitorWrappers.equals(((Entry) obj).methodVisitorWrappers);
            }

            public int hashCode() {
                return (((17 * 31) + this.matcher.hashCode()) * 31) + this.methodVisitorWrappers.hashCode();
            }

            protected Entry(ElementMatcher<? super MethodDescription> matcher, List<? extends MethodVisitorWrapper> methodVisitorWrappers) {
                this.matcher = matcher;
                this.methodVisitorWrappers = methodVisitorWrappers;
            }

            @Override // net.bytebuddy.matcher.ElementMatcher
            public boolean matches(MethodDescription target) {
                return target != null && this.matcher.matches(target);
            }

            @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
            public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
                for (MethodVisitorWrapper methodVisitorWrapper : this.methodVisitorWrappers) {
                    methodVisitor = methodVisitorWrapper.wrap(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, typePool, writerFlags, readerFlags);
                }
                return methodVisitor;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$ForDeclaredMethods$DispatchingVisitor.class */
        protected class DispatchingVisitor extends ClassVisitor {
            private final TypeDescription instrumentedType;
            private final Implementation.Context implementationContext;
            private final TypePool typePool;
            private final int writerFlags;
            private final int readerFlags;
            private final Map<String, MethodDescription> methods;

            protected DispatchingVisitor(ClassVisitor classVisitor, TypeDescription instrumentedType, Implementation.Context implementationContext, TypePool typePool, Map<String, MethodDescription> methods, int writerFlags, int readerFlags) {
                super(OpenedClassReader.ASM_API, classVisitor);
                this.instrumentedType = instrumentedType;
                this.implementationContext = implementationContext;
                this.typePool = typePool;
                this.methods = methods;
                this.writerFlags = writerFlags;
                this.readerFlags = readerFlags;
            }

            @Override // net.bytebuddy.jar.asm.ClassVisitor
            public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(modifiers, internalName, descriptor, signature, exceptions);
                MethodDescription methodDescription = this.methods.get(internalName + descriptor);
                if (methodVisitor != null && methodDescription != null) {
                    for (Entry entry : ForDeclaredMethods.this.entries) {
                        if (entry.matches(methodDescription)) {
                            methodVisitor = entry.wrap(this.instrumentedType, methodDescription, methodVisitor, this.implementationContext, this.typePool, this.writerFlags, this.readerFlags);
                        }
                    }
                }
                return methodVisitor;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/AsmVisitorWrapper$Compound.class */
    public static class Compound implements AsmVisitorWrapper {
        private final List<AsmVisitorWrapper> asmVisitorWrappers;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.asmVisitorWrappers.equals(((Compound) obj).asmVisitorWrappers);
        }

        public int hashCode() {
            return (17 * 31) + this.asmVisitorWrappers.hashCode();
        }

        public Compound(AsmVisitorWrapper... asmVisitorWrapper) {
            this(Arrays.asList(asmVisitorWrapper));
        }

        public Compound(List<? extends AsmVisitorWrapper> asmVisitorWrappers) {
            this.asmVisitorWrappers = new ArrayList();
            for (AsmVisitorWrapper asmVisitorWrapper : asmVisitorWrappers) {
                if (asmVisitorWrapper instanceof Compound) {
                    this.asmVisitorWrappers.addAll(((Compound) asmVisitorWrapper).asmVisitorWrappers);
                } else if (!(asmVisitorWrapper instanceof NoOp)) {
                    this.asmVisitorWrappers.add(asmVisitorWrapper);
                }
            }
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeWriter(int flags) {
            for (AsmVisitorWrapper asmVisitorWrapper : this.asmVisitorWrappers) {
                flags = asmVisitorWrapper.mergeWriter(flags);
            }
            return flags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public int mergeReader(int flags) {
            for (AsmVisitorWrapper asmVisitorWrapper : this.asmVisitorWrappers) {
                flags = asmVisitorWrapper.mergeReader(flags);
            }
            return flags;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            for (AsmVisitorWrapper asmVisitorWrapper : this.asmVisitorWrappers) {
                classVisitor = asmVisitorWrapper.wrap(instrumentedType, classVisitor, implementationContext, typePool, fields, methods, writerFlags, readerFlags);
            }
            return classVisitor;
        }
    }
}
