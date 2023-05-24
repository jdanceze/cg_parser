package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DexRewriter.class */
public class DexRewriter implements Rewriters {
    private final Rewriter<DexFile> dexFileRewriter;
    private final Rewriter<ClassDef> classDefRewriter;
    private final Rewriter<Field> fieldRewriter;
    private final Rewriter<Method> methodRewriter;
    private final Rewriter<MethodParameter> methodParameterRewriter;
    private final Rewriter<MethodImplementation> methodImplementationRewriter;
    private final Rewriter<Instruction> instructionRewriter;
    private final Rewriter<TryBlock<? extends ExceptionHandler>> tryBlockRewriter;
    private final Rewriter<ExceptionHandler> exceptionHandlerRewriter;
    private final Rewriter<DebugItem> debugItemRewriter;
    private final Rewriter<String> typeRewriter;
    private final Rewriter<FieldReference> fieldReferenceRewriter;
    private final Rewriter<MethodReference> methodReferenceRewriter;
    private final Rewriter<Annotation> annotationRewriter;
    private final Rewriter<AnnotationElement> annotationElementRewriter;
    private final Rewriter<EncodedValue> encodedValueRewriter;

    public DexRewriter(RewriterModule module) {
        this.dexFileRewriter = module.getDexFileRewriter(this);
        this.classDefRewriter = module.getClassDefRewriter(this);
        this.fieldRewriter = module.getFieldRewriter(this);
        this.methodRewriter = module.getMethodRewriter(this);
        this.methodParameterRewriter = module.getMethodParameterRewriter(this);
        this.methodImplementationRewriter = module.getMethodImplementationRewriter(this);
        this.instructionRewriter = module.getInstructionRewriter(this);
        this.tryBlockRewriter = module.getTryBlockRewriter(this);
        this.exceptionHandlerRewriter = module.getExceptionHandlerRewriter(this);
        this.debugItemRewriter = module.getDebugItemRewriter(this);
        this.typeRewriter = module.getTypeRewriter(this);
        this.fieldReferenceRewriter = module.getFieldReferenceRewriter(this);
        this.methodReferenceRewriter = module.getMethodReferenceRewriter(this);
        this.annotationRewriter = module.getAnnotationRewriter(this);
        this.annotationElementRewriter = module.getAnnotationElementRewriter(this);
        this.encodedValueRewriter = module.getEncodedValueRewriter(this);
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<DexFile> getDexFileRewriter() {
        return this.dexFileRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<ClassDef> getClassDefRewriter() {
        return this.classDefRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Field> getFieldRewriter() {
        return this.fieldRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Method> getMethodRewriter() {
        return this.methodRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodParameter> getMethodParameterRewriter() {
        return this.methodParameterRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodImplementation> getMethodImplementationRewriter() {
        return this.methodImplementationRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Instruction> getInstructionRewriter() {
        return this.instructionRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<TryBlock<? extends ExceptionHandler>> getTryBlockRewriter() {
        return this.tryBlockRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<ExceptionHandler> getExceptionHandlerRewriter() {
        return this.exceptionHandlerRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<DebugItem> getDebugItemRewriter() {
        return this.debugItemRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<String> getTypeRewriter() {
        return this.typeRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<FieldReference> getFieldReferenceRewriter() {
        return this.fieldReferenceRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodReference> getMethodReferenceRewriter() {
        return this.methodReferenceRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Annotation> getAnnotationRewriter() {
        return this.annotationRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<AnnotationElement> getAnnotationElementRewriter() {
        return this.annotationElementRewriter;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<EncodedValue> getEncodedValueRewriter() {
        return this.encodedValueRewriter;
    }
}
