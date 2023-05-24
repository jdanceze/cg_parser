package org.objectweb.asm.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.signature.SignatureReader;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/Textifier.class */
public class Textifier extends Printer {
    private static final String USAGE = "Prints a disassembled view of the given class.\nUsage: Textifier [-nodebug] <fully qualified class name or class file name>";
    public static final int INTERNAL_NAME = 0;
    public static final int FIELD_DESCRIPTOR = 1;
    public static final int FIELD_SIGNATURE = 2;
    public static final int METHOD_DESCRIPTOR = 3;
    public static final int METHOD_SIGNATURE = 4;
    public static final int CLASS_SIGNATURE = 5;
    public static final int HANDLE_DESCRIPTOR = 9;
    private static final String CLASS_SUFFIX = ".class";
    private static final String DEPRECATED = "// DEPRECATED\n";
    private static final String RECORD = "// RECORD\n";
    private static final String INVISIBLE = " // invisible\n";
    private static final List<String> FRAME_TYPES = Collections.unmodifiableList(Arrays.asList("T", "I", "F", "D", "J", "N", "U"));
    protected String tab;
    protected String tab2;
    protected String tab3;
    protected String ltab;
    protected Map<Label, String> labelNames;
    private int access;
    private int numAnnotationValues;

    public Textifier() {
        this(Opcodes.ASM9);
        if (getClass() != Textifier.class) {
            throw new IllegalStateException();
        }
    }

    protected Textifier(int api) {
        super(api);
        this.tab = "  ";
        this.tab2 = ASTNode.TAB;
        this.tab3 = "      ";
        this.ltab = "   ";
    }

    public static void main(String[] args) throws IOException {
        main(args, new PrintWriter((OutputStream) System.out, true), new PrintWriter((OutputStream) System.err, true));
    }

    static void main(String[] args, PrintWriter output, PrintWriter logger) throws IOException {
        main(args, USAGE, new Textifier(), output, logger);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((access & 32768) != 0) {
            return;
        }
        this.access = access;
        int majorVersion = version & 65535;
        int minorVersion = version >>> 16;
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("// class version ").append(majorVersion).append('.').append(minorVersion).append(" (").append(version).append(")\n");
        if ((access & 131072) != 0) {
            this.stringBuilder.append(DEPRECATED);
        }
        if ((access & 65536) != 0) {
            this.stringBuilder.append(RECORD);
        }
        appendRawAccess(access);
        appendDescriptor(5, signature);
        if (signature != null) {
            appendJavaDeclaration(name, signature);
        }
        appendAccess(access & (-32801));
        if ((access & 8192) != 0) {
            this.stringBuilder.append("@interface ");
        } else if ((access & 512) != 0) {
            this.stringBuilder.append("interface ");
        } else if ((access & 16384) == 0) {
            this.stringBuilder.append("class ");
        }
        appendDescriptor(0, name);
        if (superName != null && !TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME.equals(superName)) {
            this.stringBuilder.append(" extends ");
            appendDescriptor(0, superName);
        }
        if (interfaces != null && interfaces.length > 0) {
            this.stringBuilder.append(" implements ");
            for (int i = 0; i < interfaces.length; i++) {
                appendDescriptor(0, interfaces[i]);
                if (i != interfaces.length - 1) {
                    this.stringBuilder.append(' ');
                }
            }
        }
        this.stringBuilder.append(" {\n\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitSource(String file, String debug) {
        this.stringBuilder.setLength(0);
        if (file != null) {
            this.stringBuilder.append(this.tab).append("// compiled from: ").append(file).append('\n');
        }
        if (debug != null) {
            this.stringBuilder.append(this.tab).append("// debug info: ").append(debug).append('\n');
        }
        if (this.stringBuilder.length() > 0) {
            this.text.add(this.stringBuilder.toString());
        }
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitModule(String name, int access, String version) {
        this.stringBuilder.setLength(0);
        if ((access & 32) != 0) {
            this.stringBuilder.append("open ");
        }
        this.stringBuilder.append("module ").append(name).append(" { ").append(version == null ? "" : "// " + version).append("\n\n");
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(null);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitNestHost(String nestHost) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("NESTHOST ");
        appendDescriptor(0, nestHost);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("OUTERCLASS ");
        appendDescriptor(0, owner);
        this.stringBuilder.append(' ');
        if (name != null) {
            this.stringBuilder.append(name).append(' ');
        }
        appendDescriptor(3, descriptor);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitClassAnnotation(String descriptor, boolean visible) {
        this.text.add("\n");
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitClassTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.text.add("\n");
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitClassAttribute(Attribute attribute) {
        this.text.add("\n");
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitNestMember(String nestMember) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("NESTMEMBER ");
        appendDescriptor(0, nestMember);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitPermittedSubclass(String permittedSubclass) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("PERMITTEDSUBCLASS ");
        appendDescriptor(0, permittedSubclass);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab);
        appendRawAccess(access & (-33));
        this.stringBuilder.append(this.tab);
        appendAccess(access);
        this.stringBuilder.append("INNERCLASS ");
        appendDescriptor(0, name);
        this.stringBuilder.append(' ');
        appendDescriptor(0, outerName);
        this.stringBuilder.append(' ');
        appendDescriptor(0, innerName);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitRecordComponent(String name, String descriptor, String signature) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("RECORDCOMPONENT ");
        if (signature != null) {
            this.stringBuilder.append(this.tab);
            appendDescriptor(2, signature);
            this.stringBuilder.append(this.tab);
            appendJavaDeclaration(name, signature);
        }
        this.stringBuilder.append(this.tab);
        appendDescriptor(1, descriptor);
        this.stringBuilder.append(' ').append(name);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(null);
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitField(int access, String name, String descriptor, String signature, Object value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append('\n');
        if ((access & 131072) != 0) {
            this.stringBuilder.append(this.tab).append(DEPRECATED);
        }
        this.stringBuilder.append(this.tab);
        appendRawAccess(access);
        if (signature != null) {
            this.stringBuilder.append(this.tab);
            appendDescriptor(2, signature);
            this.stringBuilder.append(this.tab);
            appendJavaDeclaration(name, signature);
        }
        this.stringBuilder.append(this.tab);
        appendAccess(access);
        appendDescriptor(1, descriptor);
        this.stringBuilder.append(' ').append(name);
        if (value != null) {
            this.stringBuilder.append(" = ");
            if (value instanceof String) {
                this.stringBuilder.append('\"').append(value).append('\"');
            } else {
                this.stringBuilder.append(value);
            }
        }
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(null);
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append('\n');
        if ((access & 131072) != 0) {
            this.stringBuilder.append(this.tab).append(DEPRECATED);
        }
        this.stringBuilder.append(this.tab);
        appendRawAccess(access);
        if (signature != null) {
            this.stringBuilder.append(this.tab);
            appendDescriptor(4, signature);
            this.stringBuilder.append(this.tab);
            appendJavaDeclaration(name, signature);
        }
        this.stringBuilder.append(this.tab);
        appendAccess(access & (-193));
        if ((access & 256) != 0) {
            this.stringBuilder.append("native ");
        }
        if ((access & 128) != 0) {
            this.stringBuilder.append("varargs ");
        }
        if ((access & 64) != 0) {
            this.stringBuilder.append("bridge ");
        }
        if ((this.access & 512) != 0 && (access & 1032) == 0) {
            this.stringBuilder.append("default ");
        }
        this.stringBuilder.append(name);
        appendDescriptor(3, descriptor);
        if (exceptions != null && exceptions.length > 0) {
            this.stringBuilder.append(" throws ");
            for (String exception : exceptions) {
                appendDescriptor(0, exception);
                this.stringBuilder.append(' ');
            }
        }
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(null);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitClassEnd() {
        this.text.add("}\n");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMainClass(String mainClass) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("  // main class ").append(mainClass).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitPackage(String packaze) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("  // package ").append(packaze).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRequire(String require, int access, String version) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("requires ");
        if ((access & 32) != 0) {
            this.stringBuilder.append("transitive ");
        }
        if ((access & 64) != 0) {
            this.stringBuilder.append("static ");
        }
        this.stringBuilder.append(require).append(';');
        appendRawAccess(access);
        if (version != null) {
            this.stringBuilder.append("  // version ").append(version).append('\n');
        }
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitExport(String packaze, int access, String... modules) {
        visitExportOrOpen("exports ", packaze, access, modules);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitOpen(String packaze, int access, String... modules) {
        visitExportOrOpen("opens ", packaze, access, modules);
    }

    private void visitExportOrOpen(String method, String packaze, int access, String... modules) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append(method);
        this.stringBuilder.append(packaze);
        if (modules != null && modules.length > 0) {
            this.stringBuilder.append(" to");
        } else {
            this.stringBuilder.append(';');
        }
        appendRawAccess(access);
        if (modules != null && modules.length > 0) {
            int i = 0;
            while (i < modules.length) {
                this.stringBuilder.append(this.tab2).append(modules[i]);
                this.stringBuilder.append(i != modules.length - 1 ? ",\n" : ";\n");
                i++;
            }
        }
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitUse(String use) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("uses ");
        appendDescriptor(0, use);
        this.stringBuilder.append(";\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitProvide(String provide, String... providers) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("provides ");
        appendDescriptor(0, provide);
        this.stringBuilder.append(" with\n");
        int i = 0;
        while (i < providers.length) {
            this.stringBuilder.append(this.tab2);
            appendDescriptor(0, providers[i]);
            this.stringBuilder.append(i != providers.length - 1 ? ",\n" : ";\n");
            i++;
        }
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitModuleEnd() {
    }

    @Override // org.objectweb.asm.util.Printer
    public void visit(String name, Object value) {
        visitAnnotationValue(name);
        if (value instanceof String) {
            visitString((String) value);
        } else if (value instanceof Type) {
            visitType((Type) value);
        } else if (value instanceof Byte) {
            visitByte(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            visitBoolean(((Boolean) value).booleanValue());
        } else if (value instanceof Short) {
            visitShort(((Short) value).shortValue());
        } else if (value instanceof Character) {
            visitChar(((Character) value).charValue());
        } else if (value instanceof Integer) {
            visitInt(((Integer) value).intValue());
        } else if (value instanceof Float) {
            visitFloat(((Float) value).floatValue());
        } else if (value instanceof Long) {
            visitLong(((Long) value).longValue());
        } else if (value instanceof Double) {
            visitDouble(((Double) value).doubleValue());
        } else if (value.getClass().isArray()) {
            this.stringBuilder.append('{');
            if (value instanceof byte[]) {
                byte[] byteArray = (byte[]) value;
                for (int i = 0; i < byteArray.length; i++) {
                    maybeAppendComma(i);
                    visitByte(byteArray[i]);
                }
            } else if (value instanceof boolean[]) {
                boolean[] booleanArray = (boolean[]) value;
                for (int i2 = 0; i2 < booleanArray.length; i2++) {
                    maybeAppendComma(i2);
                    visitBoolean(booleanArray[i2]);
                }
            } else if (value instanceof short[]) {
                short[] shortArray = (short[]) value;
                for (int i3 = 0; i3 < shortArray.length; i3++) {
                    maybeAppendComma(i3);
                    visitShort(shortArray[i3]);
                }
            } else if (value instanceof char[]) {
                char[] charArray = (char[]) value;
                for (int i4 = 0; i4 < charArray.length; i4++) {
                    maybeAppendComma(i4);
                    visitChar(charArray[i4]);
                }
            } else if (value instanceof int[]) {
                int[] intArray = (int[]) value;
                for (int i5 = 0; i5 < intArray.length; i5++) {
                    maybeAppendComma(i5);
                    visitInt(intArray[i5]);
                }
            } else if (value instanceof long[]) {
                long[] longArray = (long[]) value;
                for (int i6 = 0; i6 < longArray.length; i6++) {
                    maybeAppendComma(i6);
                    visitLong(longArray[i6]);
                }
            } else if (value instanceof float[]) {
                float[] floatArray = (float[]) value;
                for (int i7 = 0; i7 < floatArray.length; i7++) {
                    maybeAppendComma(i7);
                    visitFloat(floatArray[i7]);
                }
            } else if (value instanceof double[]) {
                double[] doubleArray = (double[]) value;
                for (int i8 = 0; i8 < doubleArray.length; i8++) {
                    maybeAppendComma(i8);
                    visitDouble(doubleArray[i8]);
                }
            }
            this.stringBuilder.append('}');
        }
        this.text.add(this.stringBuilder.toString());
    }

    private void visitInt(int value) {
        this.stringBuilder.append(value);
    }

    private void visitLong(long value) {
        this.stringBuilder.append(value).append('L');
    }

    private void visitFloat(float value) {
        this.stringBuilder.append(value).append('F');
    }

    private void visitDouble(double value) {
        this.stringBuilder.append(value).append('D');
    }

    private void visitChar(char value) {
        this.stringBuilder.append("(char)").append((int) value);
    }

    private void visitShort(short value) {
        this.stringBuilder.append("(short)").append((int) value);
    }

    private void visitByte(byte value) {
        this.stringBuilder.append("(byte)").append((int) value);
    }

    private void visitBoolean(boolean value) {
        this.stringBuilder.append(value);
    }

    private void visitString(String value) {
        appendString(this.stringBuilder, value);
    }

    private void visitType(Type value) {
        this.stringBuilder.append(value.getClassName()).append(".class");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitEnum(String name, String descriptor, String value) {
        visitAnnotationValue(name);
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('.').append(value);
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitAnnotation(String name, String descriptor) {
        visitAnnotationValue(name);
        this.stringBuilder.append('@');
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(")");
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitArray(String name) {
        visitAnnotationValue(name);
        this.stringBuilder.append('{');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier("}");
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitAnnotationEnd() {
    }

    private void visitAnnotationValue(String name) {
        this.stringBuilder.setLength(0);
        int i = this.numAnnotationValues;
        this.numAnnotationValues = i + 1;
        maybeAppendComma(i);
        if (name != null) {
            this.stringBuilder.append(name).append('=');
        }
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitRecordComponentAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitRecordComponentTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRecordComponentAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitRecordComponentEnd() {
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitFieldAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitFieldTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldEnd() {
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitParameter(String name, int access) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("// parameter ");
        appendAccess(access);
        this.stringBuilder.append(' ').append(name == null ? "<no name>" : name).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitAnnotationDefault() {
        this.text.add(this.tab2 + "default=");
        return addNewTextifier("\n");
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitMethodAnnotation(String descriptor, boolean visible) {
        return visitAnnotation(descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitMethodTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitAnnotableParameterCount(int parameterCount, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("// annotable parameter count: ");
        this.stringBuilder.append(parameterCount);
        this.stringBuilder.append(visible ? " (visible)\n" : " (invisible)\n");
        this.text.add(this.stringBuilder.toString());
        return this;
    }

    @Override // org.objectweb.asm.util.Printer
    public Textifier visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append('@');
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(visible ? ") // parameter " : ") // invisible, parameter ").append(parameter).append('\n');
        return addNewTextifier(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitCode() {
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.ltab);
        this.stringBuilder.append("FRAME ");
        switch (type) {
            case -1:
            case 0:
                this.stringBuilder.append("FULL [");
                appendFrameTypes(numLocal, local);
                this.stringBuilder.append("] [");
                appendFrameTypes(numStack, stack);
                this.stringBuilder.append(']');
                break;
            case 1:
                this.stringBuilder.append("APPEND [");
                appendFrameTypes(numLocal, local);
                this.stringBuilder.append(']');
                break;
            case 2:
                this.stringBuilder.append("CHOP ").append(numLocal);
                break;
            case 3:
                this.stringBuilder.append("SAME");
                break;
            case 4:
                this.stringBuilder.append("SAME1 ");
                appendFrameTypes(1, stack);
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInsn(int opcode) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitIntInsn(int opcode, int operand) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ').append(opcode == 188 ? TYPES[operand] : Integer.toString(operand)).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitVarInsn(int opcode, int varIndex) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ').append(varIndex).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTypeInsn(int opcode, String type) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(0, type);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(0, owner);
        this.stringBuilder.append('.').append(name).append(" : ");
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(0, owner);
        this.stringBuilder.append('.').append(name).append(' ');
        appendDescriptor(3, descriptor);
        if (isInterface) {
            this.stringBuilder.append(" (itf)");
        }
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("INVOKEDYNAMIC").append(' ');
        this.stringBuilder.append(name);
        appendDescriptor(3, descriptor);
        this.stringBuilder.append(" [");
        this.stringBuilder.append('\n');
        this.stringBuilder.append(this.tab3);
        appendHandle(bootstrapMethodHandle);
        this.stringBuilder.append('\n');
        this.stringBuilder.append(this.tab3).append("// arguments:");
        if (bootstrapMethodArguments.length == 0) {
            this.stringBuilder.append(" none");
        } else {
            this.stringBuilder.append('\n');
            for (Object value : bootstrapMethodArguments) {
                this.stringBuilder.append(this.tab3);
                if (value instanceof String) {
                    Printer.appendString(this.stringBuilder, (String) value);
                } else if (value instanceof Type) {
                    Type type = (Type) value;
                    if (type.getSort() == 11) {
                        appendDescriptor(3, type.getDescriptor());
                    } else {
                        visitType(type);
                    }
                } else if (value instanceof Handle) {
                    appendHandle((Handle) value);
                } else {
                    this.stringBuilder.append(value);
                }
                this.stringBuilder.append(", \n");
            }
            this.stringBuilder.setLength(this.stringBuilder.length() - 3);
        }
        this.stringBuilder.append('\n');
        this.stringBuilder.append(this.tab2).append("]\n");
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitJumpInsn(int opcode, Label label) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append(OPCODES[opcode]).append(' ');
        appendLabel(label);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLabel(Label label) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.ltab);
        appendLabel(label);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLdcInsn(Object value) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("LDC ");
        if (value instanceof String) {
            Printer.appendString(this.stringBuilder, (String) value);
        } else if (value instanceof Type) {
            this.stringBuilder.append(((Type) value).getDescriptor()).append(".class");
        } else {
            this.stringBuilder.append(value);
        }
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitIincInsn(int varIndex, int increment) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("IINC ").append(varIndex).append(' ').append(increment).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("TABLESWITCH\n");
        for (int i = 0; i < labels.length; i++) {
            this.stringBuilder.append(this.tab3).append(min + i).append(": ");
            appendLabel(labels[i]);
            this.stringBuilder.append('\n');
        }
        this.stringBuilder.append(this.tab3).append("default: ");
        appendLabel(dflt);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("LOOKUPSWITCH\n");
        for (int i = 0; i < labels.length; i++) {
            this.stringBuilder.append(this.tab3).append(keys[i]).append(": ");
            appendLabel(labels[i]);
            this.stringBuilder.append('\n');
        }
        this.stringBuilder.append(this.tab3).append("default: ");
        appendLabel(dflt);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("MULTIANEWARRAY ");
        appendDescriptor(1, descriptor);
        this.stringBuilder.append(' ').append(numDimensions).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("TRYCATCHBLOCK ");
        appendLabel(start);
        this.stringBuilder.append(' ');
        appendLabel(end);
        this.stringBuilder.append(' ');
        appendLabel(handler);
        this.stringBuilder.append(' ');
        appendDescriptor(0, type);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("TRYCATCHBLOCK @");
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(") : ");
        appendTypeReference(typeRef);
        this.stringBuilder.append(", ").append(typePath);
        this.stringBuilder.append(visible ? "\n" : INVISIBLE);
        return addNewTextifier(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("LOCALVARIABLE ").append(name).append(' ');
        appendDescriptor(1, descriptor);
        this.stringBuilder.append(' ');
        appendLabel(start);
        this.stringBuilder.append(' ');
        appendLabel(end);
        this.stringBuilder.append(' ').append(index).append('\n');
        if (signature != null) {
            this.stringBuilder.append(this.tab2);
            appendDescriptor(2, signature);
            this.stringBuilder.append(this.tab2);
            appendJavaDeclaration(name, signature);
        }
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public Printer visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("LOCALVARIABLE @");
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(") : ");
        appendTypeReference(typeRef);
        this.stringBuilder.append(", ").append(typePath);
        for (int i = 0; i < start.length; i++) {
            this.stringBuilder.append(" [ ");
            appendLabel(start[i]);
            this.stringBuilder.append(" - ");
            appendLabel(end[i]);
            this.stringBuilder.append(" - ").append(index[i]).append(" ]");
        }
        this.stringBuilder.append(visible ? "\n" : INVISIBLE);
        return addNewTextifier(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitLineNumber(int line, Label start) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("LINENUMBER ").append(line).append(' ');
        appendLabel(start);
        this.stringBuilder.append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMaxs(int maxStack, int maxLocals) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("MAXSTACK = ").append(maxStack).append('\n');
        this.text.add(this.stringBuilder.toString());
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab2).append("MAXLOCALS = ").append(maxLocals).append('\n');
        this.text.add(this.stringBuilder.toString());
    }

    @Override // org.objectweb.asm.util.Printer
    public void visitMethodEnd() {
    }

    public Textifier visitAnnotation(String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append('@');
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        return addNewTextifier(visible ? ")\n" : ") // invisible\n");
    }

    public Textifier visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append('@');
        appendDescriptor(1, descriptor);
        this.stringBuilder.append('(');
        this.text.add(this.stringBuilder.toString());
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(") : ");
        appendTypeReference(typeRef);
        this.stringBuilder.append(", ").append(typePath);
        this.stringBuilder.append(visible ? "\n" : INVISIBLE);
        return addNewTextifier(this.stringBuilder.toString());
    }

    public void visitAttribute(Attribute attribute) {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(this.tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attribute.type);
        if (attribute instanceof TextifierSupport) {
            if (this.labelNames == null) {
                this.labelNames = new HashMap();
            }
            ((TextifierSupport) attribute).textify(this.stringBuilder, this.labelNames);
        } else {
            this.stringBuilder.append(" : unknown\n");
        }
        this.text.add(this.stringBuilder.toString());
    }

    private void appendAccess(int accessFlags) {
        if ((accessFlags & 1) != 0) {
            this.stringBuilder.append("public ");
        }
        if ((accessFlags & 2) != 0) {
            this.stringBuilder.append("private ");
        }
        if ((accessFlags & 4) != 0) {
            this.stringBuilder.append("protected ");
        }
        if ((accessFlags & 16) != 0) {
            this.stringBuilder.append("final ");
        }
        if ((accessFlags & 8) != 0) {
            this.stringBuilder.append("static ");
        }
        if ((accessFlags & 32) != 0) {
            this.stringBuilder.append("synchronized ");
        }
        if ((accessFlags & 64) != 0) {
            this.stringBuilder.append("volatile ");
        }
        if ((accessFlags & 128) != 0) {
            this.stringBuilder.append("transient ");
        }
        if ((accessFlags & 1024) != 0) {
            this.stringBuilder.append("abstract ");
        }
        if ((accessFlags & 2048) != 0) {
            this.stringBuilder.append("strictfp ");
        }
        if ((accessFlags & 4096) != 0) {
            this.stringBuilder.append("synthetic ");
        }
        if ((accessFlags & 32768) != 0) {
            this.stringBuilder.append("mandated ");
        }
        if ((accessFlags & 16384) != 0) {
            this.stringBuilder.append("enum ");
        }
    }

    private void appendRawAccess(int accessFlags) {
        this.stringBuilder.append("// access flags 0x").append(Integer.toHexString(accessFlags).toUpperCase()).append('\n');
    }

    protected void appendDescriptor(int type, String value) {
        if (type == 5 || type == 2 || type == 4) {
            if (value != null) {
                this.stringBuilder.append("// signature ").append(value).append('\n');
                return;
            }
            return;
        }
        this.stringBuilder.append(value);
    }

    private void appendJavaDeclaration(String name, String signature) {
        TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(this.access);
        new SignatureReader(signature).accept(traceSignatureVisitor);
        this.stringBuilder.append("// declaration: ");
        if (traceSignatureVisitor.getReturnType() != null) {
            this.stringBuilder.append(traceSignatureVisitor.getReturnType());
            this.stringBuilder.append(' ');
        }
        this.stringBuilder.append(name);
        this.stringBuilder.append(traceSignatureVisitor.getDeclaration());
        if (traceSignatureVisitor.getExceptions() != null) {
            this.stringBuilder.append(" throws ").append(traceSignatureVisitor.getExceptions());
        }
        this.stringBuilder.append('\n');
    }

    protected void appendLabel(Label label) {
        if (this.labelNames == null) {
            this.labelNames = new HashMap();
        }
        String name = this.labelNames.get(label);
        if (name == null) {
            name = "L" + this.labelNames.size();
            this.labelNames.put(label, name);
        }
        this.stringBuilder.append(name);
    }

    protected void appendHandle(Handle handle) {
        int tag = handle.getTag();
        this.stringBuilder.append("// handle kind 0x").append(Integer.toHexString(tag)).append(" : ");
        boolean isMethodHandle = false;
        switch (tag) {
            case 1:
                this.stringBuilder.append("GETFIELD");
                break;
            case 2:
                this.stringBuilder.append("GETSTATIC");
                break;
            case 3:
                this.stringBuilder.append("PUTFIELD");
                break;
            case 4:
                this.stringBuilder.append("PUTSTATIC");
                break;
            case 5:
                this.stringBuilder.append("INVOKEVIRTUAL");
                isMethodHandle = true;
                break;
            case 6:
                this.stringBuilder.append("INVOKESTATIC");
                isMethodHandle = true;
                break;
            case 7:
                this.stringBuilder.append("INVOKESPECIAL");
                isMethodHandle = true;
                break;
            case 8:
                this.stringBuilder.append("NEWINVOKESPECIAL");
                isMethodHandle = true;
                break;
            case 9:
                this.stringBuilder.append("INVOKEINTERFACE");
                isMethodHandle = true;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.stringBuilder.append('\n');
        this.stringBuilder.append(this.tab3);
        appendDescriptor(0, handle.getOwner());
        this.stringBuilder.append('.');
        this.stringBuilder.append(handle.getName());
        if (!isMethodHandle) {
            this.stringBuilder.append('(');
        }
        appendDescriptor(9, handle.getDesc());
        if (!isMethodHandle) {
            this.stringBuilder.append(')');
        }
        if (handle.isInterface()) {
            this.stringBuilder.append(" itf");
        }
    }

    private void maybeAppendComma(int numValues) {
        if (numValues > 0) {
            this.stringBuilder.append(", ");
        }
    }

    private void appendTypeReference(int typeRef) {
        TypeReference typeReference = new TypeReference(typeRef);
        switch (typeReference.getSort()) {
            case 0:
                this.stringBuilder.append("CLASS_TYPE_PARAMETER ").append(typeReference.getTypeParameterIndex());
                return;
            case 1:
                this.stringBuilder.append("METHOD_TYPE_PARAMETER ").append(typeReference.getTypeParameterIndex());
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalArgumentException();
            case 16:
                this.stringBuilder.append("CLASS_EXTENDS ").append(typeReference.getSuperTypeIndex());
                return;
            case 17:
                this.stringBuilder.append("CLASS_TYPE_PARAMETER_BOUND ").append(typeReference.getTypeParameterIndex()).append(", ").append(typeReference.getTypeParameterBoundIndex());
                return;
            case 18:
                this.stringBuilder.append("METHOD_TYPE_PARAMETER_BOUND ").append(typeReference.getTypeParameterIndex()).append(", ").append(typeReference.getTypeParameterBoundIndex());
                return;
            case 19:
                this.stringBuilder.append("FIELD");
                return;
            case 20:
                this.stringBuilder.append("METHOD_RETURN");
                return;
            case 21:
                this.stringBuilder.append("METHOD_RECEIVER");
                return;
            case 22:
                this.stringBuilder.append("METHOD_FORMAL_PARAMETER ").append(typeReference.getFormalParameterIndex());
                return;
            case 23:
                this.stringBuilder.append("THROWS ").append(typeReference.getExceptionIndex());
                return;
            case 64:
                this.stringBuilder.append("LOCAL_VARIABLE");
                return;
            case 65:
                this.stringBuilder.append("RESOURCE_VARIABLE");
                return;
            case 66:
                this.stringBuilder.append("EXCEPTION_PARAMETER ").append(typeReference.getTryCatchBlockIndex());
                return;
            case 67:
                this.stringBuilder.append("INSTANCEOF");
                return;
            case 68:
                this.stringBuilder.append("NEW");
                return;
            case 69:
                this.stringBuilder.append("CONSTRUCTOR_REFERENCE");
                return;
            case 70:
                this.stringBuilder.append("METHOD_REFERENCE");
                return;
            case 71:
                this.stringBuilder.append("CAST ").append(typeReference.getTypeArgumentIndex());
                return;
            case 72:
                this.stringBuilder.append("CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                return;
            case 73:
                this.stringBuilder.append("METHOD_INVOCATION_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                return;
            case 74:
                this.stringBuilder.append("CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                return;
            case 75:
                this.stringBuilder.append("METHOD_REFERENCE_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                return;
        }
    }

    private void appendFrameTypes(int numTypes, Object[] frameTypes) {
        for (int i = 0; i < numTypes; i++) {
            if (i > 0) {
                this.stringBuilder.append(' ');
            }
            if (frameTypes[i] instanceof String) {
                String descriptor = (String) frameTypes[i];
                if (descriptor.charAt(0) == '[') {
                    appendDescriptor(1, descriptor);
                } else {
                    appendDescriptor(0, descriptor);
                }
            } else if (frameTypes[i] instanceof Integer) {
                this.stringBuilder.append(FRAME_TYPES.get(((Integer) frameTypes[i]).intValue()));
            } else {
                appendLabel((Label) frameTypes[i]);
            }
        }
    }

    private Textifier addNewTextifier(String endText) {
        Textifier textifier = createTextifier();
        this.text.add(textifier.getText());
        if (endText != null) {
            this.text.add(endText);
        }
        return textifier;
    }

    protected Textifier createTextifier() {
        return new Textifier(this.api);
    }
}
