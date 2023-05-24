package net.bytebuddy.jar.asm.commons;

import java.util.ArrayList;
import net.bytebuddy.jar.asm.signature.SignatureVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/jar/asm/commons/SignatureRemapper.class */
public class SignatureRemapper extends SignatureVisitor {
    private final SignatureVisitor signatureVisitor;
    private final Remapper remapper;
    private ArrayList<String> classNames;

    public SignatureRemapper(SignatureVisitor signatureVisitor, Remapper remapper) {
        this(524288, signatureVisitor, remapper);
    }

    protected SignatureRemapper(int api, SignatureVisitor signatureVisitor, Remapper remapper) {
        super(api);
        this.classNames = new ArrayList<>();
        this.signatureVisitor = signatureVisitor;
        this.remapper = remapper;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitClassType(String name) {
        this.classNames.add(name);
        this.signatureVisitor.visitClassType(this.remapper.mapType(name));
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitInnerClassType(String name) {
        int lastIndexOf;
        String outerClassName = this.classNames.remove(this.classNames.size() - 1);
        String className = outerClassName + '$' + name;
        this.classNames.add(className);
        String remappedOuter = this.remapper.mapType(outerClassName) + '$';
        String remappedName = this.remapper.mapType(className);
        if (remappedName.startsWith(remappedOuter)) {
            lastIndexOf = remappedOuter.length();
        } else {
            lastIndexOf = remappedName.lastIndexOf(36) + 1;
        }
        int index = lastIndexOf;
        this.signatureVisitor.visitInnerClassType(remappedName.substring(index));
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String name) {
        this.signatureVisitor.visitFormalTypeParameter(name);
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitTypeVariable(String name) {
        this.signatureVisitor.visitTypeVariable(name);
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        this.signatureVisitor.visitArrayType();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitBaseType(char descriptor) {
        this.signatureVisitor.visitBaseType(descriptor);
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        this.signatureVisitor.visitClassBound();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        this.signatureVisitor.visitExceptionType();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        this.signatureVisitor.visitInterface();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.signatureVisitor.visitInterfaceBound();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        this.signatureVisitor.visitParameterType();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        this.signatureVisitor.visitReturnType();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        this.signatureVisitor.visitSuperclass();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        this.signatureVisitor.visitTypeArgument();
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char wildcard) {
        this.signatureVisitor.visitTypeArgument(wildcard);
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitEnd() {
        this.signatureVisitor.visitEnd();
        this.classNames.remove(this.classNames.size() - 1);
    }
}
