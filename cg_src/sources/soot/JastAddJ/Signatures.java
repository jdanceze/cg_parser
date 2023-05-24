package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.cli.HelpFormatter;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Signatures.class */
public class Signatures {
    String data;
    int pos = 0;
    protected List typeParameters;

    public Signatures(String s) {
        this.data = s;
    }

    public boolean next(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (this.data.charAt(this.pos + i) != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void eat(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (this.data.charAt(this.pos + i) != s.charAt(i)) {
                error(s);
            }
        }
        this.pos += s.length();
    }

    public void error(String s) {
        throw new Error("Expected " + s + " but found " + this.data.substring(this.pos));
    }

    public String identifier() {
        int i = this.pos;
        while (Character.isJavaIdentifierPart(this.data.charAt(i))) {
            i++;
        }
        String result = this.data.substring(this.pos, i);
        this.pos = i;
        return result;
    }

    public boolean eof() {
        return this.pos == this.data.length();
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Signatures$ClassSignature.class */
    public static class ClassSignature extends Signatures {
        protected Access superclassSignature;
        protected List superinterfaceSignature;

        public ClassSignature(String s) {
            super(s);
            this.superinterfaceSignature = new List();
            classSignature();
        }

        void classSignature() {
            if (next("<")) {
                formalTypeParameters();
            }
            this.superclassSignature = parseSuperclassSignature();
            while (!eof()) {
                this.superinterfaceSignature.add(parseSuperinterfaceSignature());
            }
        }

        public boolean hasFormalTypeParameters() {
            return this.typeParameters != null;
        }

        public List typeParameters() {
            return this.typeParameters;
        }

        public boolean hasSuperclassSignature() {
            return this.superclassSignature != null;
        }

        public Access superclassSignature() {
            return this.superclassSignature;
        }

        public boolean hasSuperinterfaceSignature() {
            return this.superinterfaceSignature.getNumChildNoTransform() != 0;
        }

        public List superinterfaceSignature() {
            return this.superinterfaceSignature;
        }

        Access parseSuperclassSignature() {
            return classTypeSignature();
        }

        Access parseSuperinterfaceSignature() {
            return classTypeSignature();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Signatures$FieldSignature.class */
    public static class FieldSignature extends Signatures {
        private Access fieldTypeAccess;

        public FieldSignature(String s) {
            super(s);
            this.fieldTypeAccess = fieldTypeSignature();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Access fieldTypeAccess() {
            return this.fieldTypeAccess;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Signatures$MethodSignature.class */
    public static class MethodSignature extends Signatures {
        protected Collection parameterTypes;
        protected List exceptionList;
        protected Access returnType;

        public MethodSignature(String s) {
            super(s);
            this.parameterTypes = new ArrayList();
            this.exceptionList = new List();
            this.returnType = null;
            methodTypeSignature();
        }

        void methodTypeSignature() {
            if (next("<")) {
                formalTypeParameters();
            }
            eat("(");
            while (!next(")")) {
                this.parameterTypes.add(typeSignature());
            }
            eat(")");
            this.returnType = parseReturnType();
            while (!eof()) {
                this.exceptionList.add(throwsSignature());
            }
        }

        Access parseReturnType() {
            if (next("V")) {
                eat("V");
                return new PrimitiveTypeAccess(Jimple.VOID);
            }
            return typeSignature();
        }

        Access throwsSignature() {
            eat("^");
            if (next("L")) {
                return classTypeSignature();
            }
            return typeVariableSignature();
        }

        public boolean hasFormalTypeParameters() {
            return this.typeParameters != null;
        }

        public List typeParameters() {
            return this.typeParameters;
        }

        public Collection parameterTypes() {
            return this.parameterTypes;
        }

        public List exceptionList() {
            return this.exceptionList;
        }

        public boolean hasExceptionList() {
            return this.exceptionList.getNumChildNoTransform() != 0;
        }

        public boolean hasReturnType() {
            return this.returnType != null;
        }

        public Access returnType() {
            return this.returnType;
        }
    }

    void formalTypeParameters() {
        eat("<");
        this.typeParameters = new List();
        do {
            this.typeParameters.add(formalTypeParameter());
        } while (!next(">"));
        eat(">");
    }

    TypeVariable formalTypeParameter() {
        String id = identifier();
        List bounds = new List();
        Access classBound = classBound();
        if (classBound != null) {
            bounds.add(classBound);
        }
        while (next(":")) {
            bounds.add(interfaceBound());
        }
        if (bounds.getNumChildNoTransform() == 0) {
            bounds.add(new TypeAccess("java.lang", "Object"));
        }
        return new TypeVariable(new Modifiers(new List()), id, new List(), bounds);
    }

    Access classBound() {
        eat(":");
        if (nextIsFieldTypeSignature()) {
            return fieldTypeSignature();
        }
        return null;
    }

    Access interfaceBound() {
        eat(":");
        return fieldTypeSignature();
    }

    Access fieldTypeSignature() {
        if (next("L")) {
            return classTypeSignature();
        }
        if (next("[")) {
            return arrayTypeSignature();
        }
        if (next("T")) {
            return typeVariableSignature();
        }
        error("L or [ or T");
        return null;
    }

    boolean nextIsFieldTypeSignature() {
        return next("L") || next("[") || next("T");
    }

    Access classTypeSignature() {
        String typeName;
        Access bytecodeTypeAccess;
        eat("L");
        StringBuffer packageName = new StringBuffer();
        String identifier = identifier();
        while (true) {
            typeName = identifier;
            if (!next("/")) {
                break;
            }
            eat("/");
            if (packageName.length() != 0) {
                packageName.append(".");
            }
            packageName.append(typeName);
            identifier = identifier();
        }
        if (typeName.indexOf(36) == -1) {
            bytecodeTypeAccess = new TypeAccess(packageName.toString(), typeName);
        } else {
            bytecodeTypeAccess = new BytecodeTypeAccess(packageName.toString(), typeName);
        }
        Access a = bytecodeTypeAccess;
        if (next("<")) {
            a = new ParTypeAccess(a, typeArguments());
        }
        while (next(".")) {
            a = a.qualifiesAccess(classTypeSignatureSuffix());
        }
        eat(";");
        return a;
    }

    Access classTypeSignatureSuffix() {
        eat(".");
        String id = identifier();
        Access a = id.indexOf(36) == -1 ? new TypeAccess(id) : new BytecodeTypeAccess("", id);
        if (next("<")) {
            a = new ParTypeAccess(a, typeArguments());
        }
        return a;
    }

    Access typeVariableSignature() {
        eat("T");
        String id = identifier();
        eat(";");
        return new TypeAccess(id);
    }

    List typeArguments() {
        eat("<");
        List list = new List();
        do {
            list.add(typeArgument());
        } while (!next(">"));
        eat(">");
        return list;
    }

    Access typeArgument() {
        if (next("*")) {
            eat("*");
            return new Wildcard();
        } else if (next("+")) {
            eat("+");
            return new WildcardExtends(fieldTypeSignature());
        } else if (next(HelpFormatter.DEFAULT_OPT_PREFIX)) {
            eat(HelpFormatter.DEFAULT_OPT_PREFIX);
            return new WildcardSuper(fieldTypeSignature());
        } else {
            return fieldTypeSignature();
        }
    }

    Access arrayTypeSignature() {
        eat("[");
        return new ArrayTypeAccess(typeSignature());
    }

    Access typeSignature() {
        if (nextIsFieldTypeSignature()) {
            return fieldTypeSignature();
        }
        return baseType();
    }

    Access baseType() {
        if (next("B")) {
            eat("B");
            return new PrimitiveTypeAccess("byte");
        } else if (next("C")) {
            eat("C");
            return new PrimitiveTypeAccess("char");
        } else if (next("D")) {
            eat("D");
            return new PrimitiveTypeAccess("double");
        } else if (next("F")) {
            eat("F");
            return new PrimitiveTypeAccess(Jimple.FLOAT);
        } else if (next("I")) {
            eat("I");
            return new PrimitiveTypeAccess("int");
        } else if (next("J")) {
            eat("J");
            return new PrimitiveTypeAccess("long");
        } else if (next("S")) {
            eat("S");
            return new PrimitiveTypeAccess("short");
        } else if (next("Z")) {
            eat("Z");
            return new PrimitiveTypeAccess("boolean");
        } else {
            error("baseType");
            return null;
        }
    }
}
