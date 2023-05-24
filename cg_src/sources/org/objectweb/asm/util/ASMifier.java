package org.objectweb.asm.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import soot.SootModuleInfo;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/ASMifier.class */
public class ASMifier extends Printer {
    private static final String USAGE = "Prints the ASM code to generate the given class.\nUsage: ASMifier [-nodebug] <fully qualified class name or class file name>";
    private static final int ACCESS_CLASS = 262144;
    private static final int ACCESS_FIELD = 524288;
    private static final int ACCESS_INNER = 1048576;
    private static final int ACCESS_MODULE = 2097152;
    private static final String ANNOTATION_VISITOR = "annotationVisitor";
    private static final String ANNOTATION_VISITOR0 = "annotationVisitor0 = ";
    private static final String COMMA = "\", \"";
    private static final String END_ARRAY = " });\n";
    private static final String END_PARAMETERS = ");\n\n";
    private static final String NEW_OBJECT_ARRAY = ", new Object[] {";
    private static final String VISIT_END = ".visitEnd();\n";
    private static final List<String> FRAME_TYPES = Collections.unmodifiableList(Arrays.asList("Opcodes.TOP", "Opcodes.INTEGER", "Opcodes.FLOAT", "Opcodes.DOUBLE", "Opcodes.LONG", "Opcodes.NULL", "Opcodes.UNINITIALIZED_THIS"));
    private static final Map<Integer, String> CLASS_VERSIONS;
    protected final String name;
    protected final int id;
    protected Map<Label, String> labelNames;

    static {
        HashMap<Integer, String> classVersions = new HashMap<>();
        classVersions.put(196653, "V1_1");
        classVersions.put(46, "V1_2");
        classVersions.put(47, "V1_3");
        classVersions.put(48, "V1_4");
        classVersions.put(49, "V1_5");
        classVersions.put(50, "V1_6");
        classVersions.put(51, "V1_7");
        classVersions.put(52, "V1_8");
        classVersions.put(53, "V9");
        classVersions.put(54, "V10");
        classVersions.put(55, "V11");
        classVersions.put(56, "V12");
        classVersions.put(57, "V13");
        classVersions.put(58, "V14");
        classVersions.put(59, "V15");
        classVersions.put(60, "V16");
        classVersions.put(61, "V17");
        classVersions.put(62, "V18");
        classVersions.put(63, "V19");
        classVersions.put(64, "V20");
        CLASS_VERSIONS = Collections.unmodifiableMap(classVersions);
    }

    public ASMifier() {
        this(Opcodes.ASM9, "classWriter", 0);
        if (getClass() != ASMifier.class) {
            throw new IllegalStateException();
        }
    }

    protected ASMifier(int api, String visitorVariableName, int annotationVisitorId) {
        super(api);
        this.name = visitorVariableName;
        this.id = annotationVisitorId;
    }

    public static void main(String[] args) throws IOException {
        main(args, new PrintWriter((OutputStream) System.out, true), new PrintWriter((OutputStream) System.err, true));
    }

    static void main(String[] args, PrintWriter output, PrintWriter logger) throws IOException {
        main(args, USAGE, new ASMifier(), output, logger);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String simpleName;
        if (name == null) {
            simpleName = SootModuleInfo.MODULE_INFO;
        } else {
            int lastSlashIndex = name.lastIndexOf(47);
            if (lastSlashIndex == -1) {
                simpleName = name;
            } else {
                this.text.add("package asm." + name.substring(0, lastSlashIndex).replace('/', '.') + ";\n");
                simpleName = name.substring(lastSlashIndex + 1).replaceAll("[-\\(\\)]", "_");
            }
        }
        this.text.add("import org.objectweb.asm.AnnotationVisitor;\n");
        this.text.add("import org.objectweb.asm.Attribute;\n");
        this.text.add("import org.objectweb.asm.ClassReader;\n");
        this.text.add("import org.objectweb.asm.ClassWriter;\n");
        this.text.add("import org.objectweb.asm.ConstantDynamic;\n");
        this.text.add("import org.objectweb.asm.FieldVisitor;\n");
        this.text.add("import org.objectweb.asm.Handle;\n");
        this.text.add("import org.objectweb.asm.Label;\n");
        this.text.add("import org.objectweb.asm.MethodVisitor;\n");
        this.text.add("import org.objectweb.asm.Opcodes;\n");
        this.text.add("import org.objectweb.asm.RecordComponentVisitor;\n");
        this.text.add("import org.objectweb.asm.Type;\n");
        this.text.add("import org.objectweb.asm.TypePath;\n");
        this.text.add("public class " + simpleName + "Dump implements Opcodes {\n\n");
        this.text.add("public static byte[] dump () throws Exception {\n\n");
        this.text.add("ClassWriter classWriter = new ClassWriter(0);\n");
        this.text.add("FieldVisitor fieldVisitor;\n");
        this.text.add("RecordComponentVisitor recordComponentVisitor;\n");
        this.text.add("MethodVisitor methodVisitor;\n");
        this.text.add("AnnotationVisitor annotationVisitor0;\n\n");
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visit(");
        String versionString = CLASS_VERSIONS.get(Integer.valueOf(version));
        if (versionString != null) {
            this.stringBuilder.append(versionString);
        } else {
            this.stringBuilder.append(version);
        }
        this.stringBuilder.append(", ");
        appendAccessFlags(access | 262144);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(signature);
        this.stringBuilder.append(", ");
        appendConstant(superName);
        this.stringBuilder.append(", ");
        if (interfaces != null && interfaces.length > 0) {
            this.stringBuilder.append("new String[] {");
            int i = 0;
            while (i < interfaces.length) {
                this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
                appendConstant(interfaces[i]);
                i++;
            }
            this.stringBuilder.append(" }");
        } else {
            this.stringBuilder.append(Jimple.NULL);
        }
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitSource(String file, String debug) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitSource(");
        appendConstant(file);
        this.stringBuilder.append(", ");
        appendConstant(debug);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitModule(String name, int flags, String version) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n");
        this.stringBuilder.append("ModuleVisitor moduleVisitor = classWriter.visitModule(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendAccessFlags(flags | 2097152);
        this.stringBuilder.append(", ");
        appendConstant(version);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier("moduleVisitor", 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitNestHost(String nestHost) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitNestHost(");
        appendConstant(nestHost);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitOuterClass(");
        appendConstant(owner);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitClassAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitClassTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitClassAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitNestMember(String nestMember) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitNestMember(");
        appendConstant(nestMember);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitPermittedSubclass(String permittedSubclass) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitPermittedSubclass(");
        appendConstant(permittedSubclass);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("classWriter.visitInnerClass(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(outerName);
        this.stringBuilder.append(", ");
        appendConstant(innerName);
        this.stringBuilder.append(", ");
        appendAccessFlags(access | 1048576);
        this.stringBuilder.append(END_PARAMETERS);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitRecordComponent(String name, String descriptor, String signature) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n");
        this.stringBuilder.append("recordComponentVisitor = classWriter.visitRecordComponent(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(signature);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier("recordComponentVisitor", 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitField(int access, String name, String descriptor, String signature, Object value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n");
        this.stringBuilder.append("fieldVisitor = classWriter.visitField(");
        appendAccessFlags(access | 524288);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(signature);
        this.stringBuilder.append(", ");
        appendConstant(value);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier("fieldVisitor", 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n");
        this.stringBuilder.append("methodVisitor = classWriter.visitMethod(");
        appendAccessFlags(access);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(signature);
        this.stringBuilder.append(", ");
        if (exceptions != null && exceptions.length > 0) {
            this.stringBuilder.append("new String[] {");
            int i = 0;
            while (i < exceptions.length) {
                this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
                appendConstant(exceptions[i]);
                i++;
            }
            this.stringBuilder.append(" }");
        } else {
            this.stringBuilder.append(Jimple.NULL);
        }
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier("methodVisitor", 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitClassEnd() {
        this.text.add("classWriter.visitEnd();\n\n");
        this.text.add("return classWriter.toByteArray();\n");
        this.text.add("}\n");
        this.text.add("}\n");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMainClass(String mainClass) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("moduleVisitor.visitMainClass(");
        appendConstant(mainClass);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitPackage(String packaze) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("moduleVisitor.visitPackage(");
        appendConstant(packaze);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRequire(String module, int access, String version) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("moduleVisitor.visitRequire(");
        appendConstant(module);
        this.stringBuilder.append(", ");
        appendAccessFlags(access | 2097152);
        this.stringBuilder.append(", ");
        appendConstant(version);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitExport(String packaze, int access, String... modules) {
        visitExportOrOpen("moduleVisitor.visitExport(", packaze, access, modules);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitOpen(String packaze, int access, String... modules) {
        visitExportOrOpen("moduleVisitor.visitOpen(", packaze, access, modules);
    }

    private void visitExportOrOpen(String visitMethod, String packaze, int access, String... modules) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(visitMethod);
        appendConstant(packaze);
        this.stringBuilder.append(", ");
        appendAccessFlags(access | 2097152);
        if (modules != null && modules.length > 0) {
            this.stringBuilder.append(", new String[] {");
            int i = 0;
            while (i < modules.length) {
                this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
                appendConstant(modules[i]);
                i++;
            }
            this.stringBuilder.append(" }");
        }
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitUse(String service) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("moduleVisitor.visitUse(");
        appendConstant(service);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitProvide(String service, String... providers) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("moduleVisitor.visitProvide(");
        appendConstant(service);
        this.stringBuilder.append(",  new String[] {");
        int i = 0;
        while (i < providers.length) {
            this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
            appendConstant(providers[i]);
            i++;
        }
        this.stringBuilder.append(END_ARRAY);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitModuleEnd() {
        this.text.add("moduleVisitor.visitEnd();\n");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visit(String name, Object value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(ANNOTATION_VISITOR).append(this.id).append(".visit(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(value);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitEnum(String name, String descriptor, String value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(ANNOTATION_VISITOR).append(this.id).append(".visitEnum(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(value);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitAnnotation(String name, String descriptor) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append("AnnotationVisitor annotationVisitor").append(this.id + 1).append(" = annotationVisitor");
        this.stringBuilder.append(this.id).append(".visitAnnotation(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, this.id + 1);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitArray(String name) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n");
        this.stringBuilder.append("AnnotationVisitor annotationVisitor").append(this.id + 1).append(" = annotationVisitor");
        this.stringBuilder.append(this.id).append(".visitArray(");
        appendConstant(name);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, this.id + 1);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitAnnotationEnd() {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(ANNOTATION_VISITOR).append(this.id).append(VISIT_END);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitRecordComponentAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitRecordComponentTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRecordComponentAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRecordComponentEnd() {
        visitMemberEnd();
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitFieldAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitFieldTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldEnd() {
        visitMemberEnd();
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitParameter(String parameterName, int access) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitParameter(");
        appendString(this.stringBuilder, parameterName);
        this.stringBuilder.append(", ");
        appendAccessFlags(access);
        this.text.add(this.stringBuilder.append(");\n").toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitAnnotationDefault() {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append(ANNOTATION_VISITOR0).append(this.name).append(".visitAnnotationDefault();\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitMethodAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitMethodTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitAnnotableParameterCount(int parameterCount, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitAnnotableParameterCount(").append(parameterCount).append(", ").append(visible).append(");\n");
        this.text.add(this.stringBuilder.toString());
        return this;
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append(ANNOTATION_VISITOR0).append(this.name).append(".visitParameterAnnotation(").append(parameter).append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ").append(visible).append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitCode() {
        this.text.add(this.name + ".visitCode();\n");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.stringBuilder.setLength(0);
        switch (type) {
            case -1:
            case 0:
                declareFrameTypes(numLocal, local);
                declareFrameTypes(numStack, stack);
                if (type == -1) {
                    this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_NEW, ");
                } else {
                    this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_FULL, ");
                }
                this.stringBuilder.append(numLocal).append(NEW_OBJECT_ARRAY);
                appendFrameTypes(numLocal, local);
                this.stringBuilder.append("}, ").append(numStack).append(NEW_OBJECT_ARRAY);
                appendFrameTypes(numStack, stack);
                this.stringBuilder.append('}');
                break;
            case 1:
                declareFrameTypes(numLocal, local);
                this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_APPEND,").append(numLocal).append(NEW_OBJECT_ARRAY);
                appendFrameTypes(numLocal, local);
                this.stringBuilder.append("}, 0, null");
                break;
            case 2:
                this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_CHOP,").append(numLocal).append(", null, 0, null");
                break;
            case 3:
                this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_SAME, 0, null, 0, null");
                break;
            case 4:
                declareFrameTypes(1, stack);
                this.stringBuilder.append(this.name).append(".visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {");
                appendFrameTypes(1, stack);
                this.stringBuilder.append('}');
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInsn(int opcode) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitInsn(").append(OPCODES[opcode]).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitIntInsn(int opcode, int operand) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitIntInsn(").append(OPCODES[opcode]).append(", ").append(opcode == 188 ? TYPES[operand] : Integer.toString(operand)).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitVarInsn(int opcode, int varIndex) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitVarInsn(").append(OPCODES[opcode]).append(", ").append(varIndex).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTypeInsn(int opcode, String type) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitTypeInsn(").append(OPCODES[opcode]).append(", ");
        appendConstant(type);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitFieldInsn(").append(OPCODES[opcode]).append(", ");
        appendConstant(owner);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitMethodInsn(").append(OPCODES[opcode]).append(", ");
        appendConstant(owner);
        this.stringBuilder.append(", ");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        this.stringBuilder.append(isInterface ? "true" : "false");
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitInvokeDynamicInsn(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(bootstrapMethodHandle);
        this.stringBuilder.append(", new Object[]{");
        for (int i = 0; i < bootstrapMethodArguments.length; i++) {
            appendConstant(bootstrapMethodArguments[i]);
            if (i != bootstrapMethodArguments.length - 1) {
                this.stringBuilder.append(", ");
            }
        }
        this.stringBuilder.append("});\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitJumpInsn(int opcode, Label label) {
        this.stringBuilder.setLength(0);
        declareLabel(label);
        this.stringBuilder.append(this.name).append(".visitJumpInsn(").append(OPCODES[opcode]).append(", ");
        appendLabel(label);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLabel(Label label) {
        this.stringBuilder.setLength(0);
        declareLabel(label);
        this.stringBuilder.append(this.name).append(".visitLabel(");
        appendLabel(label);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLdcInsn(Object value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitLdcInsn(");
        appendConstant(value);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitIincInsn(int varIndex, int increment) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitIincInsn(").append(varIndex).append(", ").append(increment).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.stringBuilder.setLength(0);
        for (Label label : labels) {
            declareLabel(label);
        }
        declareLabel(dflt);
        this.stringBuilder.append(this.name).append(".visitTableSwitchInsn(").append(min).append(", ").append(max).append(", ");
        appendLabel(dflt);
        this.stringBuilder.append(", new Label[] {");
        int i = 0;
        while (i < labels.length) {
            this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
            appendLabel(labels[i]);
            i++;
        }
        this.stringBuilder.append(END_ARRAY);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.stringBuilder.setLength(0);
        for (Label label : labels) {
            declareLabel(label);
        }
        declareLabel(dflt);
        this.stringBuilder.append(this.name).append(".visitLookupSwitchInsn(");
        appendLabel(dflt);
        this.stringBuilder.append(", new int[] {");
        int i = 0;
        while (i < keys.length) {
            this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ").append(keys[i]);
            i++;
        }
        this.stringBuilder.append(" }, new Label[] {");
        int i2 = 0;
        while (i2 < labels.length) {
            this.stringBuilder.append(i2 == 0 ? Instruction.argsep : ", ");
            appendLabel(labels[i2]);
            i2++;
        }
        this.stringBuilder.append(END_ARRAY);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitMultiANewArrayInsn(");
        appendConstant(descriptor);
        this.stringBuilder.append(", ").append(numDimensions).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation("visitInsnAnnotation", typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.stringBuilder.setLength(0);
        declareLabel(start);
        declareLabel(end);
        declareLabel(handler);
        this.stringBuilder.append(this.name).append(".visitTryCatchBlock(");
        appendLabel(start);
        this.stringBuilder.append(", ");
        appendLabel(end);
        this.stringBuilder.append(", ");
        appendLabel(handler);
        this.stringBuilder.append(", ");
        appendConstant(type);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public ASMifier visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation("visitTryCatchAnnotation", typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitLocalVariable(");
        appendConstant(name);
        this.stringBuilder.append(", ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ");
        appendConstant(signature);
        this.stringBuilder.append(", ");
        appendLabel(start);
        this.stringBuilder.append(", ");
        appendLabel(end);
        this.stringBuilder.append(", ").append(index).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append(ANNOTATION_VISITOR0).append(this.name).append(".visitLocalVariableAnnotation(").append(typeRef);
        if (typePath == null) {
            this.stringBuilder.append(", null, ");
        } else {
            this.stringBuilder.append(", TypePath.fromString(\"").append(typePath).append("\"), ");
        }
        this.stringBuilder.append("new Label[] {");
        int i = 0;
        while (i < start.length) {
            this.stringBuilder.append(i == 0 ? Instruction.argsep : ", ");
            appendLabel(start[i]);
            i++;
        }
        this.stringBuilder.append(" }, new Label[] {");
        int i2 = 0;
        while (i2 < end.length) {
            this.stringBuilder.append(i2 == 0 ? Instruction.argsep : ", ");
            appendLabel(end[i2]);
            i2++;
        }
        this.stringBuilder.append(" }, new int[] {");
        int i3 = 0;
        while (i3 < index.length) {
            this.stringBuilder.append(i3 == 0 ? Instruction.argsep : ", ").append(index[i3]);
            i3++;
        }
        this.stringBuilder.append(" }, ");
        appendConstant(descriptor);
        this.stringBuilder.append(", ").append(visible).append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLineNumber(int line, Label start) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitLineNumber(").append(line).append(", ");
        appendLabel(start);
        this.stringBuilder.append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMaxs(int maxStack, int maxLocals) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(".visitMaxs(").append(maxStack).append(", ").append(maxLocals).append(");\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodEnd() {
        visitMemberEnd();
    }

    public ASMifier visitAnnotation(String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append(ANNOTATION_VISITOR0).append(this.name).append(".visitAnnotation(");
        appendConstant(descriptor);
        this.stringBuilder.append(", ").append(visible).append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    public ASMifier visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation("visitTypeAnnotation", typeRef, typePath, descriptor, visible);
    }

    public ASMifier visitTypeAnnotation(String method, int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("{\n").append(ANNOTATION_VISITOR0).append(this.name).append('.').append(method).append('(').append(typeRef);
        if (typePath == null) {
            this.stringBuilder.append(", null, ");
        } else {
            this.stringBuilder.append(", TypePath.fromString(\"").append(typePath).append("\"), ");
        }
        appendConstant(descriptor);
        this.stringBuilder.append(", ").append(visible).append(");\n");
        this.text.add(this.stringBuilder.toString());
        ASMifier asmifier = createASMifier(ANNOTATION_VISITOR, 0);
        this.text.add(asmifier.getText());
        this.text.add("}\n");
        return asmifier;
    }

    public void visitAttribute(Attribute attribute) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("// ATTRIBUTE ").append(attribute.type).append('\n');
        if (attribute instanceof ASMifierSupport) {
            if (this.labelNames == null) {
                this.labelNames = new HashMap();
            }
            this.stringBuilder.append("{\n");
            ((ASMifierSupport) attribute).asmify(this.stringBuilder, "attribute", this.labelNames);
            this.stringBuilder.append(this.name).append(".visitAttribute(attribute);\n");
            this.stringBuilder.append("}\n");
        }
        this.text.add(this.stringBuilder.toString());
    }

    private void visitMemberEnd() {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.name).append(VISIT_END);
        this.text.add(this.stringBuilder.toString());
    }

    protected ASMifier createASMifier(String visitorVariableName, int annotationVisitorId) {
        return new ASMifier(this.api, visitorVariableName, annotationVisitorId);
    }

    private void appendAccessFlags(int accessFlags) {
        boolean isEmpty = true;
        if ((accessFlags & 1) != 0) {
            this.stringBuilder.append("ACC_PUBLIC");
            isEmpty = false;
        }
        if ((accessFlags & 2) != 0) {
            this.stringBuilder.append("ACC_PRIVATE");
            isEmpty = false;
        }
        if ((accessFlags & 4) != 0) {
            this.stringBuilder.append("ACC_PROTECTED");
            isEmpty = false;
        }
        if ((accessFlags & 16) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_FINAL");
            isEmpty = false;
        }
        if ((accessFlags & 8) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_STATIC");
            isEmpty = false;
        }
        if ((accessFlags & 32) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            if ((accessFlags & 262144) == 0) {
                if ((accessFlags & 2097152) == 0) {
                    this.stringBuilder.append("ACC_SYNCHRONIZED");
                } else {
                    this.stringBuilder.append("ACC_TRANSITIVE");
                }
            } else {
                this.stringBuilder.append("ACC_SUPER");
            }
            isEmpty = false;
        }
        if ((accessFlags & 64) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            if ((accessFlags & 524288) == 0) {
                if ((accessFlags & 2097152) == 0) {
                    this.stringBuilder.append("ACC_BRIDGE");
                } else {
                    this.stringBuilder.append("ACC_STATIC_PHASE");
                }
            } else {
                this.stringBuilder.append("ACC_VOLATILE");
            }
            isEmpty = false;
        }
        if ((accessFlags & 128) != 0 && (accessFlags & 786432) == 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_VARARGS");
            isEmpty = false;
        }
        if ((accessFlags & 128) != 0 && (accessFlags & 524288) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_TRANSIENT");
            isEmpty = false;
        }
        if ((accessFlags & 256) != 0 && (accessFlags & 786432) == 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_NATIVE");
            isEmpty = false;
        }
        if ((accessFlags & 16384) != 0 && (accessFlags & 1835008) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_ENUM");
            isEmpty = false;
        }
        if ((accessFlags & 8192) != 0 && (accessFlags & 1310720) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_ANNOTATION");
            isEmpty = false;
        }
        if ((accessFlags & 1024) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_ABSTRACT");
            isEmpty = false;
        }
        if ((accessFlags & 512) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_INTERFACE");
            isEmpty = false;
        }
        if ((accessFlags & 2048) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_STRICT");
            isEmpty = false;
        }
        if ((accessFlags & 4096) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_SYNTHETIC");
            isEmpty = false;
        }
        if ((accessFlags & 131072) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_DEPRECATED");
            isEmpty = false;
        }
        if ((accessFlags & 65536) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            this.stringBuilder.append("ACC_RECORD");
            isEmpty = false;
        }
        if ((accessFlags & 32768) != 0) {
            if (!isEmpty) {
                this.stringBuilder.append(" | ");
            }
            if ((accessFlags & 262144) == 0) {
                this.stringBuilder.append("ACC_MANDATED");
            } else {
                this.stringBuilder.append("ACC_MODULE");
            }
            isEmpty = false;
        }
        if (isEmpty) {
            this.stringBuilder.append('0');
        }
    }

    protected void appendConstant(Object value) {
        if (value == null) {
            this.stringBuilder.append(Jimple.NULL);
        } else if (value instanceof String) {
            appendString(this.stringBuilder, (String) value);
        } else if (value instanceof Type) {
            this.stringBuilder.append("Type.getType(\"");
            this.stringBuilder.append(((Type) value).getDescriptor());
            this.stringBuilder.append("\")");
        } else if (value instanceof Handle) {
            this.stringBuilder.append("new Handle(");
            Handle handle = (Handle) value;
            this.stringBuilder.append("Opcodes.").append(HANDLE_TAG[handle.getTag()]).append(", \"");
            this.stringBuilder.append(handle.getOwner()).append(COMMA);
            this.stringBuilder.append(handle.getName()).append(COMMA);
            this.stringBuilder.append(handle.getDesc()).append("\", ");
            this.stringBuilder.append(handle.isInterface()).append(')');
        } else if (value instanceof ConstantDynamic) {
            this.stringBuilder.append("new ConstantDynamic(\"");
            ConstantDynamic constantDynamic = (ConstantDynamic) value;
            this.stringBuilder.append(constantDynamic.getName()).append(COMMA);
            this.stringBuilder.append(constantDynamic.getDescriptor()).append("\", ");
            appendConstant(constantDynamic.getBootstrapMethod());
            this.stringBuilder.append(NEW_OBJECT_ARRAY);
            int bootstrapMethodArgumentCount = constantDynamic.getBootstrapMethodArgumentCount();
            for (int i = 0; i < bootstrapMethodArgumentCount; i++) {
                appendConstant(constantDynamic.getBootstrapMethodArgument(i));
                if (i != bootstrapMethodArgumentCount - 1) {
                    this.stringBuilder.append(", ");
                }
            }
            this.stringBuilder.append("})");
        } else if (value instanceof Byte) {
            this.stringBuilder.append("new Byte((byte)").append(value).append(')');
        } else if (value instanceof Boolean) {
            this.stringBuilder.append(((Boolean) value).booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
        } else if (value instanceof Short) {
            this.stringBuilder.append("new Short((short)").append(value).append(')');
        } else if (value instanceof Character) {
            this.stringBuilder.append("new Character((char)").append((int) ((Character) value).charValue()).append(')');
        } else if (value instanceof Integer) {
            this.stringBuilder.append("new Integer(").append(value).append(')');
        } else if (value instanceof Float) {
            this.stringBuilder.append("new Float(\"").append(value).append("\")");
        } else if (value instanceof Long) {
            this.stringBuilder.append("new Long(").append(value).append("L)");
        } else if (value instanceof Double) {
            this.stringBuilder.append("new Double(\"").append(value).append("\")");
        } else if (value instanceof byte[]) {
            byte[] byteArray = (byte[]) value;
            this.stringBuilder.append("new byte[] {");
            int i2 = 0;
            while (i2 < byteArray.length) {
                this.stringBuilder.append(i2 == 0 ? "" : ",").append((int) byteArray[i2]);
                i2++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) value;
            this.stringBuilder.append("new boolean[] {");
            int i3 = 0;
            while (i3 < booleanArray.length) {
                this.stringBuilder.append(i3 == 0 ? "" : ",").append(booleanArray[i3]);
                i3++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof short[]) {
            short[] shortArray = (short[]) value;
            this.stringBuilder.append("new short[] {");
            int i4 = 0;
            while (i4 < shortArray.length) {
                this.stringBuilder.append(i4 == 0 ? "" : ",").append("(short)").append((int) shortArray[i4]);
                i4++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof char[]) {
            char[] charArray = (char[]) value;
            this.stringBuilder.append("new char[] {");
            int i5 = 0;
            while (i5 < charArray.length) {
                this.stringBuilder.append(i5 == 0 ? "" : ",").append("(char)").append((int) charArray[i5]);
                i5++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof int[]) {
            int[] intArray = (int[]) value;
            this.stringBuilder.append("new int[] {");
            int i6 = 0;
            while (i6 < intArray.length) {
                this.stringBuilder.append(i6 == 0 ? "" : ",").append(intArray[i6]);
                i6++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof long[]) {
            long[] longArray = (long[]) value;
            this.stringBuilder.append("new long[] {");
            int i7 = 0;
            while (i7 < longArray.length) {
                this.stringBuilder.append(i7 == 0 ? "" : ",").append(longArray[i7]).append('L');
                i7++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof float[]) {
            float[] floatArray = (float[]) value;
            this.stringBuilder.append("new float[] {");
            int i8 = 0;
            while (i8 < floatArray.length) {
                this.stringBuilder.append(i8 == 0 ? "" : ",").append(floatArray[i8]).append('f');
                i8++;
            }
            this.stringBuilder.append('}');
        } else if (value instanceof double[]) {
            double[] doubleArray = (double[]) value;
            this.stringBuilder.append("new double[] {");
            int i9 = 0;
            while (i9 < doubleArray.length) {
                this.stringBuilder.append(i9 == 0 ? "" : ",").append(doubleArray[i9]).append('d');
                i9++;
            }
            this.stringBuilder.append('}');
        }
    }

    private void declareFrameTypes(int numTypes, Object[] frameTypes) {
        for (int i = 0; i < numTypes; i++) {
            if (frameTypes[i] instanceof Label) {
                declareLabel((Label) frameTypes[i]);
            }
        }
    }

    private void appendFrameTypes(int numTypes, Object[] frameTypes) {
        for (int i = 0; i < numTypes; i++) {
            if (i > 0) {
                this.stringBuilder.append(", ");
            }
            if (frameTypes[i] instanceof String) {
                appendConstant(frameTypes[i]);
            } else if (frameTypes[i] instanceof Integer) {
                this.stringBuilder.append(FRAME_TYPES.get(((Integer) frameTypes[i]).intValue()));
            } else {
                appendLabel((Label) frameTypes[i]);
            }
        }
    }

    protected void declareLabel(Label label) {
        if (this.labelNames == null) {
            this.labelNames = new HashMap();
        }
        if (this.labelNames.get(label) == null) {
            String labelName = "label" + this.labelNames.size();
            this.labelNames.put(label, labelName);
            this.stringBuilder.append("Label ").append(labelName).append(" = new Label();\n");
        }
    }

    protected void appendLabel(Label label) {
        this.stringBuilder.append(this.labelNames.get(label));
    }
}
