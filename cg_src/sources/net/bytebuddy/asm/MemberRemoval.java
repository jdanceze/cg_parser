package net.bytebuddy.asm;

import java.util.HashMap;
import java.util.Map;
import net.bytebuddy.asm.AsmVisitorWrapper;
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
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberRemoval.class */
public class MemberRemoval extends AsmVisitorWrapper.AbstractBase {
    private final ElementMatcher.Junction<FieldDescription.InDefinedShape> fieldMatcher;
    private final ElementMatcher.Junction<MethodDescription> methodMatcher;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.fieldMatcher.equals(((MemberRemoval) obj).fieldMatcher) && this.methodMatcher.equals(((MemberRemoval) obj).methodMatcher);
    }

    public int hashCode() {
        return (((17 * 31) + this.fieldMatcher.hashCode()) * 31) + this.methodMatcher.hashCode();
    }

    public MemberRemoval() {
        this(ElementMatchers.none(), ElementMatchers.none());
    }

    protected MemberRemoval(ElementMatcher.Junction<FieldDescription.InDefinedShape> fieldMatcher, ElementMatcher.Junction<MethodDescription> methodMatcher) {
        this.fieldMatcher = fieldMatcher;
        this.methodMatcher = methodMatcher;
    }

    public MemberRemoval stripFields(ElementMatcher<? super FieldDescription.InDefinedShape> matcher) {
        return new MemberRemoval(this.fieldMatcher.or(matcher), this.methodMatcher);
    }

    public MemberRemoval stripMethods(ElementMatcher<? super MethodDescription> matcher) {
        return stripInvokables(ElementMatchers.isMethod().and(matcher));
    }

    public MemberRemoval stripConstructors(ElementMatcher<? super MethodDescription> matcher) {
        return stripInvokables(ElementMatchers.isConstructor().and(matcher));
    }

    public MemberRemoval stripInvokables(ElementMatcher<? super MethodDescription> matcher) {
        return new MemberRemoval(this.fieldMatcher, this.methodMatcher.or(matcher));
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        Map<String, FieldDescription.InDefinedShape> mappedFields = new HashMap<>();
        for (FieldDescription.InDefinedShape fieldDescription : fields) {
            mappedFields.put(fieldDescription.getInternalName() + fieldDescription.getDescriptor(), fieldDescription);
        }
        Map<String, MethodDescription> mappedMethods = new HashMap<>();
        for (MethodDescription methodDescription : CompoundList.of(methods, new MethodDescription.Latent.TypeInitializer(instrumentedType))) {
            mappedMethods.put(methodDescription.getInternalName() + methodDescription.getDescriptor(), methodDescription);
        }
        return new MemberRemovingClassVisitor(classVisitor, this.fieldMatcher, this.methodMatcher, mappedFields, mappedMethods);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberRemoval$MemberRemovingClassVisitor.class */
    protected static class MemberRemovingClassVisitor extends ClassVisitor {
        private static final FieldVisitor REMOVE_FIELD = null;
        private static final MethodVisitor REMOVE_METHOD = null;
        private final ElementMatcher.Junction<FieldDescription.InDefinedShape> fieldMatcher;
        private final ElementMatcher.Junction<MethodDescription> methodMatcher;
        private final Map<String, FieldDescription.InDefinedShape> fields;
        private final Map<String, MethodDescription> methods;

        protected MemberRemovingClassVisitor(ClassVisitor classVisitor, ElementMatcher.Junction<FieldDescription.InDefinedShape> fieldMatcher, ElementMatcher.Junction<MethodDescription> methodMatcher, Map<String, FieldDescription.InDefinedShape> fields, Map<String, MethodDescription> methods) {
            super(OpenedClassReader.ASM_API, classVisitor);
            this.fieldMatcher = fieldMatcher;
            this.methodMatcher = methodMatcher;
            this.fields = fields;
            this.methods = methods;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public FieldVisitor visitField(int modifiers, String internalName, String descriptor, String signature, Object value) {
            FieldDescription.InDefinedShape fieldDescription = this.fields.get(internalName + descriptor);
            return (fieldDescription == null || !this.fieldMatcher.matches(fieldDescription)) ? super.visitField(modifiers, internalName, descriptor, signature, value) : REMOVE_FIELD;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
            MethodDescription methodDescription = this.methods.get(internalName + descriptor);
            return (methodDescription == null || !this.methodMatcher.matches(methodDescription)) ? super.visitMethod(modifiers, internalName, descriptor, signature, exception) : REMOVE_METHOD;
        }
    }
}
