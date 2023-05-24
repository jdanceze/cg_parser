package soot.JastAddJ;

import java.util.Iterator;
import soot.JastAddJ.Attributes;
import soot.JastAddJ.Signatures;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MethodInfo.class */
public class MethodInfo {
    private BytecodeParser p;
    String name;
    int flags;
    private MethodDescriptor methodDescriptor;
    private Attributes.MethodAttributes attributes;

    public MethodInfo(BytecodeParser parser) {
        this.p = parser;
        this.flags = this.p.u2();
        int name_index = this.p.u2();
        CONSTANT_Info info = this.p.constantPool[name_index];
        if (info == null || !(info instanceof CONSTANT_Utf8_Info)) {
            System.err.println("Expected CONSTANT_Utf8_Info but found: " + info.getClass().getName());
        }
        this.name = ((CONSTANT_Utf8_Info) info).string();
        this.methodDescriptor = new MethodDescriptor(this.p, this.name);
        this.attributes = new Attributes.MethodAttributes(this.p);
    }

    public BodyDecl bodyDecl() {
        List parameterList;
        BodyDecl b;
        Signatures.MethodSignature s = this.attributes.methodSignature;
        Access returnType = (s == null || !s.hasReturnType()) ? this.methodDescriptor.type() : s.returnType();
        if (isConstructor() && this.p.isInnerClass) {
            parameterList = this.methodDescriptor.parameterListSkipFirst();
            if (s != null) {
                Iterator iter = s.parameterTypes().iterator();
                if (iter.hasNext()) {
                    iter.next();
                }
                int i = 0;
                while (iter.hasNext()) {
                    Access a = (Access) iter.next();
                    ((ParameterDeclaration) parameterList.getChildNoTransform(i)).setTypeAccess(a);
                    i++;
                }
            }
        } else {
            parameterList = this.methodDescriptor.parameterList();
            if (s != null) {
                int i2 = 0;
                for (Access a2 : s.parameterTypes()) {
                    ((ParameterDeclaration) parameterList.getChildNoTransform(i2)).setTypeAccess(a2);
                    i2++;
                }
            }
        }
        if ((this.flags & 128) != 0) {
            int lastIndex = parameterList.getNumChildNoTransform() - 1;
            ParameterDeclaration p = (ParameterDeclaration) parameterList.getChildNoTransform(lastIndex);
            parameterList.setChild(new VariableArityParameterDeclaration(p.getModifiersNoTransform(), ((ArrayTypeAccess) p.getTypeAccessNoTransform()).getAccessNoTransform(), p.getID()), lastIndex);
        }
        List exceptionList = (s == null || !s.hasExceptionList()) ? this.attributes.exceptionList() : s.exceptionList();
        if (this.attributes.parameterAnnotations != null) {
            for (int i3 = 0; i3 < this.attributes.parameterAnnotations.length; i3++) {
                ParameterDeclaration p2 = (ParameterDeclaration) parameterList.getChildNoTransform(i3);
                Iterator iter2 = this.attributes.parameterAnnotations[i3].iterator();
                while (iter2.hasNext()) {
                    Modifier m = (Modifier) iter2.next();
                    p2.getModifiersNoTransform().addModifier(m);
                }
            }
        }
        if (isConstructor()) {
            b = new ConstructorDecl(BytecodeParser.modifiers(this.flags), this.name, parameterList, exceptionList, new Opt(), new Block());
        } else if (this.attributes.elementValue() != null) {
            b = new AnnotationMethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()), new Opt(this.attributes.elementValue()));
        } else if (s != null && s.hasFormalTypeParameters()) {
            b = new GenericMethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()), s.typeParameters());
        } else {
            b = new MethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()));
        }
        if (this.attributes.annotations != null) {
            Iterator iter3 = this.attributes.annotations.iterator();
            while (iter3.hasNext()) {
                if (b instanceof MethodDecl) {
                    ((MethodDecl) b).getModifiers().addModifier((Modifier) iter3.next());
                } else if (b instanceof ConstructorDecl) {
                    ((ConstructorDecl) b).getModifiers().addModifier((Modifier) iter3.next());
                }
            }
        }
        return b;
    }

    private boolean isConstructor() {
        return this.name.equals("<init>");
    }

    public boolean isSynthetic() {
        return this.attributes.isSynthetic() || (this.flags & 4096) != 0;
    }
}
