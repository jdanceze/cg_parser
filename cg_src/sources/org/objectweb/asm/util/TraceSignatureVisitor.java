package org.objectweb.asm.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceSignatureVisitor.class */
public final class TraceSignatureVisitor extends SignatureVisitor {
    private static final String COMMA_SEPARATOR = ", ";
    private static final String EXTENDS_SEPARATOR = " extends ";
    private static final String IMPLEMENTS_SEPARATOR = " implements ";
    private static final Map<Character, String> BASE_TYPES;
    private final boolean isInterface;
    private final StringBuilder declaration;
    private StringBuilder returnType;
    private StringBuilder exceptions;
    private boolean formalTypeParameterVisited;
    private boolean interfaceBoundVisited;
    private boolean parameterTypeVisited;
    private boolean interfaceVisited;
    private int argumentStack;
    private int arrayStack;
    private String separator;

    static {
        HashMap<Character, String> baseTypes = new HashMap<>();
        baseTypes.put('Z', "boolean");
        baseTypes.put('B', "byte");
        baseTypes.put('C', "char");
        baseTypes.put('S', "short");
        baseTypes.put('I', "int");
        baseTypes.put('J', "long");
        baseTypes.put('F', Jimple.FLOAT);
        baseTypes.put('D', "double");
        baseTypes.put('V', Jimple.VOID);
        BASE_TYPES = Collections.unmodifiableMap(baseTypes);
    }

    public TraceSignatureVisitor(int accessFlags) {
        super(Opcodes.ASM9);
        this.separator = "";
        this.isInterface = (accessFlags & 512) != 0;
        this.declaration = new StringBuilder();
    }

    private TraceSignatureVisitor(StringBuilder stringBuilder) {
        super(Opcodes.ASM9);
        this.separator = "";
        this.isInterface = false;
        this.declaration = stringBuilder;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String name) {
        this.declaration.append(this.formalTypeParameterVisited ? COMMA_SEPARATOR : "<").append(name);
        this.formalTypeParameterVisited = true;
        this.interfaceBoundVisited = false;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        this.separator = EXTENDS_SEPARATOR;
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.separator = this.interfaceBoundVisited ? COMMA_SEPARATOR : EXTENDS_SEPARATOR;
        this.interfaceBoundVisited = true;
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        endFormals();
        this.separator = EXTENDS_SEPARATOR;
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        if (this.interfaceVisited) {
            this.separator = COMMA_SEPARATOR;
        } else {
            this.separator = this.isInterface ? EXTENDS_SEPARATOR : IMPLEMENTS_SEPARATOR;
            this.interfaceVisited = true;
        }
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        endFormals();
        if (this.parameterTypeVisited) {
            this.declaration.append(COMMA_SEPARATOR);
        } else {
            this.declaration.append('(');
            this.parameterTypeVisited = true;
        }
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        endFormals();
        if (this.parameterTypeVisited) {
            this.parameterTypeVisited = false;
        } else {
            this.declaration.append('(');
        }
        this.declaration.append(')');
        this.returnType = new StringBuilder();
        return new TraceSignatureVisitor(this.returnType);
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        if (this.exceptions == null) {
            this.exceptions = new StringBuilder();
        } else {
            this.exceptions.append(COMMA_SEPARATOR);
        }
        return new TraceSignatureVisitor(this.exceptions);
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitBaseType(char descriptor) {
        String baseType = BASE_TYPES.get(Character.valueOf(descriptor));
        if (baseType == null) {
            throw new IllegalArgumentException();
        }
        this.declaration.append(baseType);
        endType();
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeVariable(String name) {
        this.declaration.append(this.separator).append(name);
        this.separator = "";
        endType();
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        startType();
        this.arrayStack |= 1;
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitClassType(String name) {
        if (TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME.equals(name)) {
            boolean needObjectClass = this.argumentStack % 2 != 0 || this.parameterTypeVisited;
            if (needObjectClass) {
                this.declaration.append(this.separator).append(name.replace('/', '.'));
            }
        } else {
            this.declaration.append(this.separator).append(name.replace('/', '.'));
        }
        this.separator = "";
        this.argumentStack *= 2;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitInnerClassType(String name) {
        if (this.argumentStack % 2 != 0) {
            this.declaration.append('>');
        }
        this.argumentStack /= 2;
        this.declaration.append('.');
        this.declaration.append(this.separator).append(name.replace('/', '.'));
        this.separator = "";
        this.argumentStack *= 2;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack++;
            this.declaration.append('<');
        } else {
            this.declaration.append(COMMA_SEPARATOR);
        }
        this.declaration.append('?');
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char tag) {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack++;
            this.declaration.append('<');
        } else {
            this.declaration.append(COMMA_SEPARATOR);
        }
        if (tag == '+') {
            this.declaration.append("? extends ");
        } else if (tag == '-') {
            this.declaration.append("? super ");
        }
        startType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitEnd() {
        if (this.argumentStack % 2 != 0) {
            this.declaration.append('>');
        }
        this.argumentStack /= 2;
        endType();
    }

    public String getDeclaration() {
        return this.declaration.toString();
    }

    public String getReturnType() {
        if (this.returnType == null) {
            return null;
        }
        return this.returnType.toString();
    }

    public String getExceptions() {
        if (this.exceptions == null) {
            return null;
        }
        return this.exceptions.toString();
    }

    private void endFormals() {
        if (this.formalTypeParameterVisited) {
            this.declaration.append('>');
            this.formalTypeParameterVisited = false;
        }
    }

    private void startType() {
        this.arrayStack *= 2;
    }

    private void endType() {
        if (this.arrayStack % 2 == 0) {
            this.arrayStack /= 2;
            return;
        }
        while (this.arrayStack % 2 != 0) {
            this.arrayStack /= 2;
            this.declaration.append("[]");
        }
    }
}
