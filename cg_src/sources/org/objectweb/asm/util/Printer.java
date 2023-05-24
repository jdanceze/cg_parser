package org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.tar.TarConstants;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.TypePath;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/Printer.class */
public abstract class Printer {
    public static final String[] OPCODES = {"NOP", "ACONST_NULL", "ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3", "ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1", "FCONST_0", "FCONST_1", "FCONST_2", "DCONST_0", "DCONST_1", "BIPUSH", "SIPUSH", "LDC", "LDC_W", "LDC2_W", "ILOAD", "LLOAD", "FLOAD", "DLOAD", "ALOAD", "ILOAD_0", "ILOAD_1", "ILOAD_2", "ILOAD_3", "LLOAD_0", "LLOAD_1", "LLOAD_2", "LLOAD_3", "FLOAD_0", "FLOAD_1", "FLOAD_2", "FLOAD_3", "DLOAD_0", "DLOAD_1", "DLOAD_2", "DLOAD_3", "ALOAD_0", "ALOAD_1", "ALOAD_2", "ALOAD_3", "IALOAD", "LALOAD", "FALOAD", "DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "ISTORE", "LSTORE", "FSTORE", "DSTORE", "ASTORE", "ISTORE_0", "ISTORE_1", "ISTORE_2", "ISTORE_3", "LSTORE_0", "LSTORE_1", "LSTORE_2", "LSTORE_3", "FSTORE_0", "FSTORE_1", "FSTORE_2", "FSTORE_3", "DSTORE_0", "DSTORE_1", "DSTORE_2", "DSTORE_3", "ASTORE_0", "ASTORE_1", "ASTORE_2", "ASTORE_3", "IASTORE", "LASTORE", "FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE", "POP", "POP2", "DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "SWAP", "IADD", "LADD", "FADD", "DADD", "ISUB", "LSUB", "FSUB", "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV", "LDIV", "FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", "INEG", "LNEG", "FNEG", "DNEG", "ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR", "IAND", "LAND", "IOR", "LOR", "IXOR", "LXOR", "IINC", "I2L", "I2F", "I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L", "D2F", "I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL", "DCMPG", "IFEQ", "IFNE", "IFLT", "IFGE", "IFGT", "IFLE", "IF_ICMPEQ", "IF_ICMPNE", "IF_ICMPLT", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE", "IF_ACMPEQ", "IF_ACMPNE", "GOTO", "JSR", "RET", "TABLESWITCH", "LOOKUPSWITCH", "IRETURN", "LRETURN", "FRETURN", "DRETURN", "ARETURN", "RETURN", "GETSTATIC", "PUTSTATIC", "GETFIELD", "PUTFIELD", "INVOKEVIRTUAL", "INVOKESPECIAL", "INVOKESTATIC", "INVOKEINTERFACE", "INVOKEDYNAMIC", "NEW", "NEWARRAY", "ANEWARRAY", "ARRAYLENGTH", "ATHROW", "CHECKCAST", "INSTANCEOF", "MONITORENTER", "MONITOREXIT", "WIDE", "MULTIANEWARRAY", "IFNULL", "IFNONNULL"};
    public static final String[] TYPES = {"", "", "", "", "T_BOOLEAN", "T_CHAR", "T_FLOAT", "T_DOUBLE", "T_BYTE", "T_SHORT", "T_INT", "T_LONG"};
    public static final String[] HANDLE_TAG = {"", "H_GETFIELD", "H_GETSTATIC", "H_PUTFIELD", "H_PUTSTATIC", "H_INVOKEVIRTUAL", "H_INVOKESTATIC", "H_INVOKESPECIAL", "H_NEWINVOKESPECIAL", "H_INVOKEINTERFACE"};
    private static final String UNSUPPORTED_OPERATION = "Must be overridden";
    protected final int api;
    protected final StringBuilder stringBuilder = new StringBuilder();
    public final List<Object> text = new ArrayList();

    public abstract void visit(int i, int i2, String str, String str2, String str3, String[] strArr);

    public abstract void visitSource(String str, String str2);

    public abstract void visitOuterClass(String str, String str2, String str3);

    public abstract Printer visitClassAnnotation(String str, boolean z);

    public abstract void visitClassAttribute(Attribute attribute);

    public abstract void visitInnerClass(String str, String str2, String str3, int i);

    public abstract Printer visitField(int i, String str, String str2, String str3, Object obj);

    public abstract Printer visitMethod(int i, String str, String str2, String str3, String[] strArr);

    public abstract void visitClassEnd();

    public abstract void visit(String str, Object obj);

    public abstract void visitEnum(String str, String str2, String str3);

    public abstract Printer visitAnnotation(String str, String str2);

    public abstract Printer visitArray(String str);

    public abstract void visitAnnotationEnd();

    public abstract Printer visitFieldAnnotation(String str, boolean z);

    public abstract void visitFieldAttribute(Attribute attribute);

    public abstract void visitFieldEnd();

    public abstract Printer visitAnnotationDefault();

    public abstract Printer visitMethodAnnotation(String str, boolean z);

    public abstract Printer visitParameterAnnotation(int i, String str, boolean z);

    public abstract void visitMethodAttribute(Attribute attribute);

    public abstract void visitCode();

    public abstract void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2);

    public abstract void visitInsn(int i);

    public abstract void visitIntInsn(int i, int i2);

    public abstract void visitVarInsn(int i, int i2);

    public abstract void visitTypeInsn(int i, String str);

    public abstract void visitFieldInsn(int i, String str, String str2, String str3);

    public abstract void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr);

    public abstract void visitJumpInsn(int i, Label label);

    public abstract void visitLabel(Label label);

    public abstract void visitLdcInsn(Object obj);

    public abstract void visitIincInsn(int i, int i2);

    public abstract void visitTableSwitchInsn(int i, int i2, Label label, Label... labelArr);

    public abstract void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr);

    public abstract void visitMultiANewArrayInsn(String str, int i);

    public abstract void visitTryCatchBlock(Label label, Label label2, Label label3, String str);

    public abstract void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i);

    public abstract void visitLineNumber(int i, Label label);

    public abstract void visitMaxs(int i, int i2);

    public abstract void visitMethodEnd();

    /* JADX INFO: Access modifiers changed from: protected */
    public Printer(int api) {
        this.api = api;
    }

    public Printer visitModule(String name, int access, String version) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitNestHost(String nestHost) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitClassTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitNestMember(String nestMember) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitPermittedSubclass(String permittedSubclass) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitRecordComponent(String name, String descriptor, String signature) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitMainClass(String mainClass) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitPackage(String packaze) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitRequire(String module, int access, String version) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitExport(String packaze, int access, String... modules) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitOpen(String packaze, int access, String... modules) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitUse(String service) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitProvide(String service, String... providers) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitModuleEnd() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitRecordComponentAnnotation(String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitRecordComponentTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitRecordComponentAttribute(Attribute attribute) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitRecordComponentEnd() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitFieldTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public void visitParameter(String name, int access) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitMethodTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitAnnotableParameterCount(int parameterCount, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Deprecated
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        visitMethodInsn(opcode, owner, name, descriptor, opcode == 185);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public Printer visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    public List<Object> getText() {
        return this.text;
    }

    public void print(PrintWriter printWriter) {
        printList(printWriter, this.text);
    }

    static void printList(PrintWriter printWriter, List<?> list) {
        for (Object o : list) {
            if (o instanceof List) {
                printList(printWriter, (List) o);
            } else {
                printWriter.print(o.toString());
            }
        }
    }

    public static void appendString(StringBuilder stringBuilder, String string) {
        stringBuilder.append('\"');
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '\n') {
                stringBuilder.append("\\n");
            } else if (c == '\r') {
                stringBuilder.append("\\r");
            } else if (c == '\\') {
                stringBuilder.append("\\\\");
            } else if (c == '\"') {
                stringBuilder.append("\\\"");
            } else if (c < ' ' || c > 127) {
                stringBuilder.append("\\u");
                if (c < 16) {
                    stringBuilder.append("000");
                } else if (c < 256) {
                    stringBuilder.append(TarConstants.VERSION_POSIX);
                } else if (c < 4096) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(Integer.toString(c, 16));
            } else {
                stringBuilder.append(c);
            }
        }
        stringBuilder.append('\"');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void main(String[] args, String usage, Printer printer, PrintWriter output, PrintWriter logger) throws IOException {
        String className;
        int parsingOptions;
        if (args.length < 1 || args.length > 2 || ((args[0].equals("-debug") || args[0].equals("-nodebug")) && args.length != 2)) {
            logger.println(usage);
            return;
        }
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, output);
        if (args[0].equals("-nodebug")) {
            className = args[1];
            parsingOptions = 2;
        } else {
            className = args[0];
            parsingOptions = 0;
        }
        if (className.endsWith(".class") || className.indexOf(92) != -1 || className.indexOf(47) != -1) {
            InputStream inputStream = new FileInputStream(className);
            try {
                new ClassReader(inputStream).accept(traceClassVisitor, parsingOptions);
                inputStream.close();
                return;
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (Throwable th2) {
                }
                throw th;
            }
        }
        new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }
}
