package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import soot.JastAddJ.Signatures;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Attributes.class */
public class Attributes {
    protected BytecodeParser p;
    protected boolean isSynthetic = false;

    protected Attributes(BytecodeParser parser) {
        this.p = parser;
    }

    protected void processAttribute(String attribute_name, int attribute_length) {
        if (attribute_name.equals("Synthetic")) {
            this.isSynthetic = true;
        } else {
            this.p.skip(attribute_length);
        }
    }

    protected void attributes() {
        int attributes_count = this.p.u2();
        for (int j = 0; j < attributes_count; j++) {
            int attribute_name_index = this.p.u2();
            int attribute_length = this.p.u4();
            String attribute_name = this.p.getCONSTANT_Utf8_Info(attribute_name_index).string();
            processAttribute(attribute_name, attribute_length);
        }
    }

    public boolean isSynthetic() {
        return this.isSynthetic;
    }

    protected ElementValue readElementValue() {
        char c = (char) this.p.u1();
        switch (c) {
            case '@':
                return new ElementAnnotationValue(readAnnotation());
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's':
                int const_value_index = this.p.u2();
                Expr e = this.p.getCONSTANT_Info(const_value_index).expr();
                return new ElementConstantValue(e);
            case '[':
                int index = this.p.u2();
                List list = new List();
                for (int i = 0; i < index; i++) {
                    list.add(readElementValue());
                }
                return new ElementArrayValue(list);
            case 'c':
                int class_info_index = this.p.u2();
                String descriptor = this.p.getCONSTANT_Utf8_Info(class_info_index).string();
                Expr e2 = new TypeDescriptor(this.p, descriptor).type();
                return new ElementConstantValue(e2);
            case 'e':
                int type_name_index = this.p.u2();
                String type_name = this.p.getCONSTANT_Utf8_Info(type_name_index).string();
                Access typeAccess = this.p.fromClassName(type_name.substring(1, type_name.length() - 1));
                int const_name_index = this.p.u2();
                String const_name = this.p.getCONSTANT_Utf8_Info(const_name_index).string();
                return new ElementConstantValue(typeAccess.qualifiesAccess(new VarAccess(const_name)));
            default:
                throw new Error("AnnotationDefault tag " + c + " not supported");
        }
    }

    protected Annotation readAnnotation() {
        Access typeAccess = new FieldDescriptor(this.p, "").type();
        int num_element_value_pairs = this.p.u2();
        List list = new List();
        for (int i = 0; i < num_element_value_pairs; i++) {
            int element_name_index = this.p.u2();
            String element_name = this.p.getCONSTANT_Utf8_Info(element_name_index).string();
            ElementValue element_value = readElementValue();
            list.add(new ElementValuePair(element_name, element_value));
        }
        return new Annotation("Annotation", typeAccess, list);
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Attributes$FieldAttributes.class */
    public static class FieldAttributes extends Attributes {
        protected CONSTANT_Info constantValue;
        public ArrayList annotations;
        public Signatures.FieldSignature fieldSignature;

        public FieldAttributes(BytecodeParser p) {
            super(p);
            attributes();
        }

        @Override // soot.JastAddJ.Attributes
        protected void processAttribute(String attribute_name, int attribute_length) {
            if (attribute_name.equals("ConstantValue") && attribute_length == 2) {
                int constantvalue_index = this.p.u2();
                this.constantValue = this.p.getCONSTANT_Info(constantvalue_index);
            } else if (attribute_name.equals("RuntimeVisibleAnnotations")) {
                int num_annotations = this.p.u2();
                if (this.annotations == null) {
                    this.annotations = new ArrayList();
                }
                for (int j = 0; j < num_annotations; j++) {
                    this.annotations.add(readAnnotation());
                }
            } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
                int num_annotations2 = this.p.u2();
                if (this.annotations == null) {
                    this.annotations = new ArrayList();
                }
                for (int j2 = 0; j2 < num_annotations2; j2++) {
                    this.annotations.add(readAnnotation());
                }
            } else if (attribute_name.equals("Signature")) {
                int signature_index = this.p.u2();
                String s = this.p.getCONSTANT_Utf8_Info(signature_index).string();
                this.fieldSignature = new Signatures.FieldSignature(s);
            } else {
                super.processAttribute(attribute_name, attribute_length);
            }
        }

        public CONSTANT_Info constantValue() {
            return this.constantValue;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Attributes$MethodAttributes.class */
    public static class MethodAttributes extends Attributes {
        protected List exceptionList;
        protected ElementValue elementValue;
        public Signatures.MethodSignature methodSignature;
        public ArrayList annotations;
        public ArrayList[] parameterAnnotations;

        public MethodAttributes(BytecodeParser p) {
            super(p);
            attributes();
        }

        @Override // soot.JastAddJ.Attributes
        protected void processAttribute(String attribute_name, int attribute_length) {
            if (attribute_name.equals("Exceptions")) {
                parseExceptions();
            } else if (attribute_name.equals("AnnotationDefault")) {
                annotationDefault();
            } else if (attribute_name.equals("Signature")) {
                int signature_index = this.p.u2();
                String s = this.p.getCONSTANT_Utf8_Info(signature_index).string();
                this.methodSignature = new Signatures.MethodSignature(s);
            } else if (attribute_name.equals("RuntimeVisibleAnnotations")) {
                int num_annotations = this.p.u2();
                if (this.annotations == null) {
                    this.annotations = new ArrayList();
                }
                for (int j = 0; j < num_annotations; j++) {
                    this.annotations.add(readAnnotation());
                }
            } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
                int num_annotations2 = this.p.u2();
                if (this.annotations == null) {
                    this.annotations = new ArrayList();
                }
                for (int j2 = 0; j2 < num_annotations2; j2++) {
                    this.annotations.add(readAnnotation());
                }
            } else if (attribute_name.equals("RuntimeVisibleParameterAnnotations")) {
                int num_parameters = this.p.u1();
                if (this.parameterAnnotations == null) {
                    this.parameterAnnotations = new ArrayList[num_parameters];
                }
                for (int i = 0; i < num_parameters; i++) {
                    if (this.parameterAnnotations[i] == null) {
                        this.parameterAnnotations[i] = new ArrayList();
                    }
                    int num_annotations3 = this.p.u2();
                    for (int j3 = 0; j3 < num_annotations3; j3++) {
                        this.parameterAnnotations[i].add(readAnnotation());
                    }
                }
            } else if (attribute_name.equals("RuntimeInvisibleParameterAnnotations")) {
                int num_parameters2 = this.p.u1();
                if (this.parameterAnnotations == null) {
                    this.parameterAnnotations = new ArrayList[num_parameters2];
                }
                for (int i2 = 0; i2 < num_parameters2; i2++) {
                    if (this.parameterAnnotations[i2] == null) {
                        this.parameterAnnotations[i2] = new ArrayList();
                    }
                    int num_annotations4 = this.p.u2();
                    for (int j4 = 0; j4 < num_annotations4; j4++) {
                        this.parameterAnnotations[i2].add(readAnnotation());
                    }
                }
            } else {
                super.processAttribute(attribute_name, attribute_length);
            }
        }

        private void parseExceptions() {
            int number_of_exceptions = this.p.u2();
            this.exceptionList = new List();
            for (int i = 0; i < number_of_exceptions; i++) {
                CONSTANT_Class_Info exception = this.p.getCONSTANT_Class_Info(this.p.u2());
                this.exceptionList.add(exception.access());
            }
        }

        public List exceptionList() {
            return this.exceptionList != null ? this.exceptionList : new List();
        }

        public ElementValue elementValue() {
            return this.elementValue;
        }

        private void annotationDefault() {
            this.elementValue = readElementValue();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Attributes$TypeAttributes.class */
    public static class TypeAttributes extends Attributes {
        TypeDecl typeDecl;
        TypeDecl outerTypeDecl;
        Program classPath;
        private boolean isInnerClass;

        public TypeAttributes(BytecodeParser p, TypeDecl typeDecl, TypeDecl outerTypeDecl, Program classPath) {
            super(p);
            this.typeDecl = typeDecl;
            this.outerTypeDecl = outerTypeDecl;
            this.classPath = classPath;
            attributes();
        }

        public boolean isInnerClass() {
            return this.isInnerClass;
        }

        @Override // soot.JastAddJ.Attributes
        protected void processAttribute(String attribute_name, int attribute_length) {
            if (attribute_name.equals("InnerClasses")) {
                innerClasses();
            } else if (attribute_name.equals("Signature")) {
                int signature_index = this.p.u2();
                String s = this.p.getCONSTANT_Utf8_Info(signature_index).string();
                Signatures.ClassSignature classSignature = new Signatures.ClassSignature(s);
                this.typeDecl = this.typeDecl.makeGeneric(classSignature);
            } else if (attribute_name.equals("RuntimeVisibleAnnotations")) {
                int num_annotations = this.p.u2();
                for (int j = 0; j < num_annotations; j++) {
                    Annotation a = readAnnotation();
                    this.typeDecl.getModifiers().addModifier(a);
                }
            } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
                int num_annotations2 = this.p.u2();
                for (int j2 = 0; j2 < num_annotations2; j2++) {
                    Annotation a2 = readAnnotation();
                    this.typeDecl.getModifiers().addModifier(a2);
                }
            } else {
                super.processAttribute(attribute_name, attribute_length);
            }
        }

        protected void innerClasses() {
            String outer_class_name;
            int number_of_classes = this.p.u2();
            for (int i = 0; i < number_of_classes; i++) {
                int inner_class_info_index = this.p.u2();
                int outer_class_info_index = this.p.u2();
                this.p.u2();
                int inner_class_access_flags = this.p.u2();
                if (inner_class_info_index > 0) {
                    CONSTANT_Class_Info inner_class_info = this.p.getCONSTANT_Class_Info(inner_class_info_index);
                    String inner_class_name = inner_class_info.name();
                    String inner_name = inner_class_name.substring(inner_class_name.lastIndexOf("$") + 1);
                    if (outer_class_info_index > 0) {
                        CONSTANT_Class_Info outer_class_info = this.p.getCONSTANT_Class_Info(outer_class_info_index);
                        if (inner_class_info == null || outer_class_info == null) {
                            System.out.println("Null");
                        }
                        outer_class_name = outer_class_info.name();
                    } else {
                        outer_class_name = inner_class_name.substring(0, inner_class_name.lastIndexOf("$"));
                    }
                    if (inner_class_info.name().equals(this.p.classInfo.name())) {
                        this.typeDecl.setID(inner_name);
                        this.typeDecl.setModifiers(BytecodeParser.modifiers(inner_class_access_flags & 1055));
                        if (this.p.outerClassName != null && this.p.outerClassName.equals(outer_class_name)) {
                            if (this.typeDecl instanceof ClassDecl) {
                                MemberTypeDecl m = new MemberClassDecl((ClassDecl) this.typeDecl);
                                this.outerTypeDecl.addBodyDecl(m);
                            } else if (this.typeDecl instanceof InterfaceDecl) {
                                MemberTypeDecl m2 = new MemberInterfaceDecl((InterfaceDecl) this.typeDecl);
                                this.outerTypeDecl.addBodyDecl(m2);
                            }
                        } else {
                            this.isInnerClass = true;
                        }
                    }
                    if (outer_class_name.equals(this.p.classInfo.name())) {
                        InputStream is = null;
                        try {
                            try {
                                try {
                                    is = this.classPath.getInputStream(inner_class_name);
                                    if (is != null) {
                                        BytecodeParser p = new BytecodeParser(is, this.p.name);
                                        p.parse(this.typeDecl, outer_class_name, this.classPath, (inner_class_access_flags & 8) == 0);
                                    } else {
                                        this.p.println("Error: ClassFile " + inner_class_name + " not found");
                                    }
                                    if (is != null) {
                                        is.close();
                                    }
                                } catch (Throwable th) {
                                    if (is != null) {
                                        is.close();
                                    }
                                    throw th;
                                    break;
                                }
                            } catch (Error e) {
                                if (!e.getMessage().startsWith("Could not find nested type")) {
                                    throw e;
                                    break;
                                } else if (is != null) {
                                    is.close();
                                }
                            }
                        } catch (FileNotFoundException e2) {
                            System.out.println("Error: " + inner_class_name + " not found");
                        } catch (Exception e3) {
                            throw new RuntimeException(e3);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
