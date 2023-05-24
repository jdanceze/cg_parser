package soot.JastAddJ;

import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TypeDescriptor.class */
public class TypeDescriptor {
    private BytecodeParser p;
    private String descriptor;

    public TypeDescriptor(BytecodeParser parser, String descriptor) {
        this.p = parser;
        this.descriptor = descriptor;
    }

    public boolean isBoolean() {
        return this.descriptor.charAt(0) == 'Z';
    }

    public Access type() {
        return type(this.descriptor);
    }

    public Access type(String s) {
        char c = s.charAt(0);
        switch (c) {
            case 'B':
                return new PrimitiveTypeAccess("byte");
            case 'C':
                return new PrimitiveTypeAccess("char");
            case 'D':
                return new PrimitiveTypeAccess("double");
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                this.p.println("Error: unknown type in TypeDescriptor");
                throw new Error("Error: unknown Type in TypeDescriptor: " + s);
            case 'F':
                return new PrimitiveTypeAccess(Jimple.FLOAT);
            case 'I':
                return new PrimitiveTypeAccess("int");
            case 'J':
                return new PrimitiveTypeAccess("long");
            case 'L':
                return this.p.fromClassName(s.substring(1, s.length() - 1));
            case 'S':
                return new PrimitiveTypeAccess("short");
            case 'V':
                return new PrimitiveTypeAccess(Jimple.VOID);
            case 'Z':
                return new PrimitiveTypeAccess("boolean");
            case '[':
                return new ArrayTypeAccess(type(s.substring(1)));
        }
    }

    public List parameterList() {
        List list = new List();
        String str = this.descriptor;
        while (true) {
            String s = str;
            if (!s.equals("")) {
                str = typeList(s, list);
            } else {
                return list;
            }
        }
    }

    public List parameterListSkipFirst() {
        List list = new List();
        String s = this.descriptor;
        if (!s.equals("")) {
            s = typeList(s, new List());
        }
        while (!s.equals("")) {
            s = typeList(s, list);
        }
        return list;
    }

    public String typeList(String s, List l) {
        char c = s.charAt(0);
        switch (c) {
            case 'B':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("byte"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'C':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("char"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'D':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("double"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'F':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess(Jimple.FLOAT), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'I':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("int"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'J':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("long"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'L':
                int pos = s.indexOf(59);
                String s1 = s.substring(1, pos);
                String s2 = s.substring(pos + 1, s.length());
                l.add(new ParameterDeclaration(new Modifiers(), this.p.fromClassName(s1), "p" + l.getNumChildNoTransform()));
                return s2;
            case 'S':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("short"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case 'Z':
                l.add(new ParameterDeclaration(new Modifiers(), new PrimitiveTypeAccess("boolean"), "p" + l.getNumChildNoTransform()));
                return s.substring(1);
            case '[':
                int i = 1;
                while (s.charAt(i) == '[') {
                    i++;
                }
                ArrayTypeAccess bottom = new ArrayTypeAccess(new ParseName(""));
                ArrayTypeAccess top = bottom;
                for (int j = 0; j < i - 1; j++) {
                    top = new ArrayTypeAccess(top);
                }
                l.add(new ParameterDeclaration(new Modifiers(), top, "p" + l.getNumChild()));
                return arrayTypeList(s.substring(i), bottom);
            default:
                this.p.println("Error: unknown Type \"" + c + "\" in TypeDescriptor");
                throw new Error("Error: unknown Type in TypeDescriptor: " + s);
        }
    }

    public String arrayTypeList(String s, ArrayTypeAccess typeAccess) {
        char c = s.charAt(0);
        switch (c) {
            case 'B':
                typeAccess.setAccess(new PrimitiveTypeAccess("byte"));
                return s.substring(1);
            case 'C':
                typeAccess.setAccess(new PrimitiveTypeAccess("char"));
                return s.substring(1);
            case 'D':
                typeAccess.setAccess(new PrimitiveTypeAccess("double"));
                return s.substring(1);
            case 'F':
                typeAccess.setAccess(new PrimitiveTypeAccess(Jimple.FLOAT));
                return s.substring(1);
            case 'I':
                typeAccess.setAccess(new PrimitiveTypeAccess("int"));
                return s.substring(1);
            case 'J':
                typeAccess.setAccess(new PrimitiveTypeAccess("long"));
                return s.substring(1);
            case 'L':
                int pos = s.indexOf(59);
                String s1 = s.substring(1, pos);
                String s2 = s.substring(pos + 1, s.length());
                typeAccess.setAccess(this.p.fromClassName(s1));
                return s2;
            case 'S':
                typeAccess.setAccess(new PrimitiveTypeAccess("short"));
                return s.substring(1);
            case 'Z':
                typeAccess.setAccess(new PrimitiveTypeAccess("boolean"));
                return s.substring(1);
            default:
                this.p.println("Error: unknown Type in TypeDescriptor");
                throw new Error("Error: unknown Type in TypeDescriptor: " + s);
        }
    }
}
