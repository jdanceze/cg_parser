package net.bytebuddy.asm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.modifier.ModifierContributor;
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
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/ModifierAdjustment.class */
public class ModifierAdjustment extends AsmVisitorWrapper.AbstractBase {
    private final List<Adjustment<TypeDescription>> typeAdjustments;
    private final List<Adjustment<FieldDescription.InDefinedShape>> fieldAdjustments;
    private final List<Adjustment<MethodDescription>> methodAdjustments;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.typeAdjustments.equals(((ModifierAdjustment) obj).typeAdjustments) && this.fieldAdjustments.equals(((ModifierAdjustment) obj).fieldAdjustments) && this.methodAdjustments.equals(((ModifierAdjustment) obj).methodAdjustments);
    }

    public int hashCode() {
        return (((((17 * 31) + this.typeAdjustments.hashCode()) * 31) + this.fieldAdjustments.hashCode()) * 31) + this.methodAdjustments.hashCode();
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public /* bridge */ /* synthetic */ ClassVisitor wrap(TypeDescription typeDescription, ClassVisitor classVisitor, Implementation.Context context, TypePool typePool, FieldList fieldList, MethodList methodList, int i, int i2) {
        return wrap(typeDescription, classVisitor, context, typePool, (FieldList<FieldDescription.InDefinedShape>) fieldList, (MethodList<?>) methodList, i, i2);
    }

    public ModifierAdjustment() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    protected ModifierAdjustment(List<Adjustment<TypeDescription>> typeAdjustments, List<Adjustment<FieldDescription.InDefinedShape>> fieldAdjustments, List<Adjustment<MethodDescription>> methodAdjustments) {
        this.typeAdjustments = typeAdjustments;
        this.fieldAdjustments = fieldAdjustments;
        this.methodAdjustments = methodAdjustments;
    }

    public ModifierAdjustment withTypeModifiers(ModifierContributor.ForType... modifierContributor) {
        return withTypeModifiers(Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withTypeModifiers(List<? extends ModifierContributor.ForType> modifierContributors) {
        return withTypeModifiers(ElementMatchers.any(), modifierContributors);
    }

    public ModifierAdjustment withTypeModifiers(ElementMatcher<? super TypeDescription> matcher, ModifierContributor.ForType... modifierContributor) {
        return withTypeModifiers(matcher, Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withTypeModifiers(ElementMatcher<? super TypeDescription> matcher, List<? extends ModifierContributor.ForType> modifierContributors) {
        return new ModifierAdjustment(CompoundList.of(new Adjustment(matcher, ModifierContributor.Resolver.of(modifierContributors)), this.typeAdjustments), this.fieldAdjustments, this.methodAdjustments);
    }

    public ModifierAdjustment withFieldModifiers(ModifierContributor.ForField... modifierContributor) {
        return withFieldModifiers(Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withFieldModifiers(List<? extends ModifierContributor.ForField> modifierContributors) {
        return withFieldModifiers(ElementMatchers.any(), modifierContributors);
    }

    public ModifierAdjustment withFieldModifiers(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, ModifierContributor.ForField... modifierContributor) {
        return withFieldModifiers(matcher, Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withFieldModifiers(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, List<? extends ModifierContributor.ForField> modifierContributors) {
        return new ModifierAdjustment(this.typeAdjustments, CompoundList.of(new Adjustment(matcher, ModifierContributor.Resolver.of(modifierContributors)), this.fieldAdjustments), this.methodAdjustments);
    }

    public ModifierAdjustment withMethodModifiers(ModifierContributor.ForMethod... modifierContributor) {
        return withMethodModifiers(Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withMethodModifiers(List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return withMethodModifiers(ElementMatchers.any(), modifierContributors);
    }

    public ModifierAdjustment withMethodModifiers(ElementMatcher<? super MethodDescription> matcher, ModifierContributor.ForMethod... modifierContributor) {
        return withMethodModifiers(matcher, Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withMethodModifiers(ElementMatcher<? super MethodDescription> matcher, List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return withInvokableModifiers(ElementMatchers.isMethod().and(matcher), modifierContributors);
    }

    public ModifierAdjustment withConstructorModifiers(ModifierContributor.ForMethod... modifierContributor) {
        return withConstructorModifiers(Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withConstructorModifiers(List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return withConstructorModifiers(ElementMatchers.any(), modifierContributors);
    }

    public ModifierAdjustment withConstructorModifiers(ElementMatcher<? super MethodDescription> matcher, ModifierContributor.ForMethod... modifierContributor) {
        return withConstructorModifiers(matcher, Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withConstructorModifiers(ElementMatcher<? super MethodDescription> matcher, List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return withInvokableModifiers(ElementMatchers.isConstructor().and(matcher), modifierContributors);
    }

    public ModifierAdjustment withInvokableModifiers(ModifierContributor.ForMethod... modifierContributor) {
        return withInvokableModifiers(Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withInvokableModifiers(List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return withInvokableModifiers(ElementMatchers.any(), modifierContributors);
    }

    public ModifierAdjustment withInvokableModifiers(ElementMatcher<? super MethodDescription> matcher, ModifierContributor.ForMethod... modifierContributor) {
        return withInvokableModifiers(matcher, Arrays.asList(modifierContributor));
    }

    public ModifierAdjustment withInvokableModifiers(ElementMatcher<? super MethodDescription> matcher, List<? extends ModifierContributor.ForMethod> modifierContributors) {
        return new ModifierAdjustment(this.typeAdjustments, this.fieldAdjustments, CompoundList.of(new Adjustment(matcher, ModifierContributor.Resolver.of(modifierContributors)), this.methodAdjustments));
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public ModifierAdjustingClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        Map<String, FieldDescription.InDefinedShape> mappedFields = new HashMap<>();
        for (FieldDescription.InDefinedShape fieldDescription : fields) {
            mappedFields.put(fieldDescription.getInternalName() + fieldDescription.getDescriptor(), fieldDescription);
        }
        Map<String, MethodDescription> mappedMethods = new HashMap<>();
        for (MethodDescription methodDescription : CompoundList.of(methods, new MethodDescription.Latent.TypeInitializer(instrumentedType))) {
            mappedMethods.put(methodDescription.getInternalName() + methodDescription.getDescriptor(), methodDescription);
        }
        return new ModifierAdjustingClassVisitor(classVisitor, this.typeAdjustments, this.fieldAdjustments, this.methodAdjustments, instrumentedType, mappedFields, mappedMethods);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/ModifierAdjustment$Adjustment.class */
    public static class Adjustment<T> implements ElementMatcher<T> {
        private final ElementMatcher<? super T> matcher;
        private final ModifierContributor.Resolver<?> resolver;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Adjustment) obj).matcher) && this.resolver.equals(((Adjustment) obj).resolver);
        }

        public int hashCode() {
            return (((17 * 31) + this.matcher.hashCode()) * 31) + this.resolver.hashCode();
        }

        protected Adjustment(ElementMatcher<? super T> matcher, ModifierContributor.Resolver<?> resolver) {
            this.matcher = matcher;
            this.resolver = resolver;
        }

        @Override // net.bytebuddy.matcher.ElementMatcher
        public boolean matches(T target) {
            return this.matcher.matches(target);
        }

        protected int resolve(int modifiers) {
            return this.resolver.resolve(modifiers);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/ModifierAdjustment$ModifierAdjustingClassVisitor.class */
    public static class ModifierAdjustingClassVisitor extends ClassVisitor {
        private final List<Adjustment<TypeDescription>> typeAdjustments;
        private final List<Adjustment<FieldDescription.InDefinedShape>> fieldAdjustments;
        private final List<Adjustment<MethodDescription>> methodAdjustments;
        private final TypeDescription instrumentedType;
        private final Map<String, FieldDescription.InDefinedShape> fields;
        private final Map<String, MethodDescription> methods;

        protected ModifierAdjustingClassVisitor(ClassVisitor classVisitor, List<Adjustment<TypeDescription>> typeAdjustments, List<Adjustment<FieldDescription.InDefinedShape>> fieldAdjustments, List<Adjustment<MethodDescription>> methodAdjustments, TypeDescription instrumentedType, Map<String, FieldDescription.InDefinedShape> fields, Map<String, MethodDescription> methods) {
            super(OpenedClassReader.ASM_API, classVisitor);
            this.typeAdjustments = typeAdjustments;
            this.fieldAdjustments = fieldAdjustments;
            this.methodAdjustments = methodAdjustments;
            this.instrumentedType = instrumentedType;
            this.fields = fields;
            this.methods = methods;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visit(int version, int modifiers, String internalName, String signature, String superClassName, String[] interfaceName) {
            Iterator<Adjustment<TypeDescription>> it = this.typeAdjustments.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Adjustment<TypeDescription> adjustment = it.next();
                if (adjustment.matches(this.instrumentedType)) {
                    modifiers = adjustment.resolve(modifiers);
                    break;
                }
            }
            super.visit(version, modifiers, internalName, signature, superClassName, interfaceName);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitInnerClass(String internalName, String outerName, String innerName, int modifiers) {
            if (this.instrumentedType.getInternalName().equals(internalName)) {
                Iterator<Adjustment<TypeDescription>> it = this.typeAdjustments.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Adjustment<TypeDescription> adjustment = it.next();
                    if (adjustment.matches(this.instrumentedType)) {
                        modifiers = adjustment.resolve(modifiers);
                        break;
                    }
                }
            }
            super.visitInnerClass(internalName, outerName, innerName, modifiers);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public FieldVisitor visitField(int modifiers, String internalName, String descriptor, String signature, Object value) {
            FieldDescription.InDefinedShape fieldDescription = this.fields.get(internalName + descriptor);
            if (fieldDescription != null) {
                Iterator<Adjustment<FieldDescription.InDefinedShape>> it = this.fieldAdjustments.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Adjustment<FieldDescription.InDefinedShape> adjustment = it.next();
                    if (adjustment.matches(fieldDescription)) {
                        modifiers = adjustment.resolve(modifiers);
                        break;
                    }
                }
            }
            return super.visitField(modifiers, internalName, descriptor, signature, value);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
            MethodDescription methodDescription = this.methods.get(internalName + descriptor);
            if (methodDescription != null) {
                Iterator<Adjustment<MethodDescription>> it = this.methodAdjustments.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Adjustment<MethodDescription> adjustment = it.next();
                    if (adjustment.matches(methodDescription)) {
                        modifiers = adjustment.resolve(modifiers);
                        break;
                    }
                }
            }
            return super.visitMethod(modifiers, internalName, descriptor, signature, exception);
        }
    }
}
