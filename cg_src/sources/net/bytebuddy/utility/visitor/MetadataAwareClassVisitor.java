package net.bytebuddy.utility.visitor;

import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.Attribute;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.RecordComponentVisitor;
import net.bytebuddy.jar.asm.TypePath;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/visitor/MetadataAwareClassVisitor.class */
public abstract class MetadataAwareClassVisitor extends ClassVisitor {
    private boolean triggerNestHost;
    private boolean triggerPermittedSubclasses;
    private boolean triggerOuterClass;
    private boolean triggerAttributes;
    private boolean triggerRecordComponents;

    /* JADX INFO: Access modifiers changed from: protected */
    public MetadataAwareClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.triggerNestHost = true;
        this.triggerPermittedSubclasses = true;
        this.triggerOuterClass = true;
        this.triggerAttributes = true;
        this.triggerRecordComponents = true;
    }

    protected void onNestHost() {
    }

    protected void onAfterPermittedSubclasses() {
    }

    protected void onOuterType() {
    }

    protected void onAfterAttributes() {
    }

    protected void onAfterRecordComponents() {
    }

    private void considerTriggerNestHost() {
        if (this.triggerNestHost) {
            this.triggerNestHost = false;
            onNestHost();
        }
    }

    private void considerTriggerOuterClass() {
        if (this.triggerOuterClass) {
            this.triggerOuterClass = false;
            onOuterType();
        }
    }

    private void considerTriggerAfterPermittedSubclasses() {
        if (this.triggerPermittedSubclasses) {
            this.triggerPermittedSubclasses = false;
            onAfterPermittedSubclasses();
        }
    }

    private void considerTriggerAfterAttributes() {
        if (this.triggerAttributes) {
            this.triggerAttributes = false;
            onAfterAttributes();
        }
    }

    private void considerTriggerAfterRecordComponents() {
        if (this.triggerRecordComponents) {
            this.triggerRecordComponents = false;
            onAfterRecordComponents();
        }
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitNestHost(String nestHost) {
        this.triggerNestHost = false;
        onVisitNestHost(nestHost);
    }

    protected void onVisitNestHost(String nestHost) {
        super.visitNestHost(nestHost);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitOuterClass(String owner, String name, String descriptor) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        this.triggerOuterClass = false;
        onVisitOuterClass(owner, name, descriptor);
    }

    protected void onVisitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        return onVisitRecordComponent(name, descriptor, signature);
    }

    protected RecordComponentVisitor onVisitRecordComponent(String name, String descriptor, String signature) {
        return super.visitRecordComponent(name, descriptor, signature);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        return onVisitAnnotation(descriptor, visible);
    }

    protected AnnotationVisitor onVisitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final AnnotationVisitor visitTypeAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        return onVisitTypeAnnotation(typeReference, typePath, descriptor, visible);
    }

    protected AnnotationVisitor onVisitTypeAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeReference, typePath, descriptor, visible);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitAttribute(Attribute attribute) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        onVisitAttribute(attribute);
    }

    protected void onVisitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitNestMember(String nestMember) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        onVisitNestMember(nestMember);
    }

    protected void onVisitNestMember(String nestMember) {
        super.visitNestMember(nestMember);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitInnerClass(String name, String outerName, String innerName, int modifiers) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        onVisitInnerClass(name, outerName, innerName, modifiers);
    }

    protected void onVisitInnerClass(String name, String outerName, String innerName, int modifiers) {
        super.visitInnerClass(name, outerName, innerName, modifiers);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final FieldVisitor visitField(int modifiers, String internalName, String descriptor, String signature, Object defaultValue) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        considerTriggerAfterRecordComponents();
        return onVisitField(modifiers, internalName, descriptor, signature, defaultValue);
    }

    protected FieldVisitor onVisitField(int modifiers, String internalName, String descriptor, String signature, Object defaultValue) {
        return super.visitField(modifiers, internalName, descriptor, signature, defaultValue);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        considerTriggerAfterRecordComponents();
        return onVisitMethod(modifiers, internalName, descriptor, signature, exception);
    }

    protected MethodVisitor onVisitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
        return super.visitMethod(modifiers, internalName, descriptor, signature, exception);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public final void visitEnd() {
        considerTriggerNestHost();
        considerTriggerAfterPermittedSubclasses();
        considerTriggerOuterClass();
        considerTriggerAfterAttributes();
        considerTriggerAfterRecordComponents();
        onVisitEnd();
    }

    protected void onVisitEnd() {
        super.visitEnd();
    }
}
