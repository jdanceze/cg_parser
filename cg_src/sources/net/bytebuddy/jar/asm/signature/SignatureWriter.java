package net.bytebuddy.jar.asm.signature;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/jar/asm/signature/SignatureWriter.class */
public class SignatureWriter extends SignatureVisitor {
    private final StringBuilder stringBuilder;
    private boolean hasFormals;
    private boolean hasParameters;
    private int argumentStack;

    public SignatureWriter() {
        super(524288);
        this.stringBuilder = new StringBuilder();
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String name) {
        if (!this.hasFormals) {
            this.hasFormals = true;
            this.stringBuilder.append('<');
        }
        this.stringBuilder.append(name);
        this.stringBuilder.append(':');
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.stringBuilder.append(':');
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        endFormals();
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        endFormals();
        if (!this.hasParameters) {
            this.hasParameters = true;
            this.stringBuilder.append('(');
        }
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        endFormals();
        if (!this.hasParameters) {
            this.stringBuilder.append('(');
        }
        this.stringBuilder.append(')');
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        this.stringBuilder.append('^');
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitBaseType(char descriptor) {
        this.stringBuilder.append(descriptor);
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitTypeVariable(String name) {
        this.stringBuilder.append('T');
        this.stringBuilder.append(name);
        this.stringBuilder.append(';');
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        this.stringBuilder.append('[');
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitClassType(String name) {
        this.stringBuilder.append('L');
        this.stringBuilder.append(name);
        this.argumentStack *= 2;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitInnerClassType(String name) {
        endArguments();
        this.stringBuilder.append('.');
        this.stringBuilder.append(name);
        this.argumentStack *= 2;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack |= 1;
            this.stringBuilder.append('<');
        }
        this.stringBuilder.append('*');
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char wildcard) {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack |= 1;
            this.stringBuilder.append('<');
        }
        if (wildcard != '=') {
            this.stringBuilder.append(wildcard);
        }
        return this;
    }

    @Override // net.bytebuddy.jar.asm.signature.SignatureVisitor
    public void visitEnd() {
        endArguments();
        this.stringBuilder.append(';');
    }

    public String toString() {
        return this.stringBuilder.toString();
    }

    private void endFormals() {
        if (this.hasFormals) {
            this.hasFormals = false;
            this.stringBuilder.append('>');
        }
    }

    private void endArguments() {
        if (this.argumentStack % 2 == 1) {
            this.stringBuilder.append('>');
        }
        this.argumentStack /= 2;
    }
}
