package org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.apache.tools.bzip2.BZip2Constants;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.CheckMethodAdapter;
import soot.SootModuleInfo;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckClassAdapter.class */
public class CheckClassAdapter extends ClassVisitor {
    private static final String USAGE = "Verifies the given class.\nUsage: CheckClassAdapter <fully qualified class name or class file name>";
    private static final String ERROR_AT = ": error at index ";
    private boolean checkDataFlow;
    private int version;
    private boolean visitCalled;
    private boolean visitModuleCalled;
    private boolean visitSourceCalled;
    private boolean visitOuterClassCalled;
    private boolean visitNestHostCalled;
    private String nestMemberPackageName;
    private boolean visitEndCalled;
    private Map<Label, Integer> labelInsnIndices;

    public CheckClassAdapter(ClassVisitor classVisitor) {
        this(classVisitor, true);
    }

    public CheckClassAdapter(ClassVisitor classVisitor, boolean checkDataFlow) {
        this(Opcodes.ASM9, classVisitor, checkDataFlow);
        if (getClass() != CheckClassAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected CheckClassAdapter(int api, ClassVisitor classVisitor, boolean checkDataFlow) {
        super(api, classVisitor);
        this.labelInsnIndices = new HashMap();
        this.checkDataFlow = checkDataFlow;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (this.visitCalled) {
            throw new IllegalStateException("visit must be called only once");
        }
        this.visitCalled = true;
        checkState();
        checkAccess(access, 259633);
        if (name == null) {
            throw new IllegalArgumentException("Illegal class name (null)");
        }
        if (!name.endsWith(PackageDescription.PACKAGE_CLASS_NAME) && !name.endsWith(SootModuleInfo.MODULE_INFO)) {
            CheckMethodAdapter.checkInternalName(version, name, "class name");
        }
        if (TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME.equals(name)) {
            if (superName != null) {
                throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        } else if (name.endsWith(SootModuleInfo.MODULE_INFO)) {
            if (superName != null) {
                throw new IllegalArgumentException("The super class name of a module-info class must be 'null'");
            }
        } else {
            CheckMethodAdapter.checkInternalName(version, superName, "super class name");
        }
        if (signature != null) {
            checkClassSignature(signature);
        }
        if ((access & 512) != 0 && !TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME.equals(superName)) {
            throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
        }
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                CheckMethodAdapter.checkInternalName(version, interfaces[i], "interface name at index " + i);
            }
        }
        this.version = version;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitSource(String file, String debug) {
        checkState();
        if (this.visitSourceCalled) {
            throw new IllegalStateException("visitSource can be called only once.");
        }
        this.visitSourceCalled = true;
        super.visitSource(file, debug);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public ModuleVisitor visitModule(String name, int access, String version) {
        checkState();
        if (this.visitModuleCalled) {
            throw new IllegalStateException("visitModule can be called only once.");
        }
        this.visitModuleCalled = true;
        checkFullyQualifiedName(this.version, name, "module name");
        checkAccess(access, 36896);
        CheckModuleAdapter checkModuleAdapter = new CheckModuleAdapter(this.api, super.visitModule(name, access, version), (access & 32) != 0);
        checkModuleAdapter.classVersion = this.version;
        return checkModuleAdapter;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestHost(String nestHost) {
        checkState();
        CheckMethodAdapter.checkInternalName(this.version, nestHost, "nestHost");
        if (this.visitNestHostCalled) {
            throw new IllegalStateException("visitNestHost can be called only once.");
        }
        if (this.nestMemberPackageName != null) {
            throw new IllegalStateException("visitNestHost and visitNestMember are mutually exclusive.");
        }
        this.visitNestHostCalled = true;
        super.visitNestHost(nestHost);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestMember(String nestMember) {
        checkState();
        CheckMethodAdapter.checkInternalName(this.version, nestMember, "nestMember");
        if (this.visitNestHostCalled) {
            throw new IllegalStateException("visitMemberOfNest and visitNestHost are mutually exclusive.");
        }
        String packageName = packageName(nestMember);
        if (this.nestMemberPackageName == null) {
            this.nestMemberPackageName = packageName;
        } else if (!this.nestMemberPackageName.equals(packageName)) {
            throw new IllegalStateException("nest member " + nestMember + " should be in the package " + this.nestMemberPackageName);
        }
        super.visitNestMember(nestMember);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitPermittedSubclass(String permittedSubclass) {
        checkState();
        CheckMethodAdapter.checkInternalName(this.version, permittedSubclass, "permittedSubclass");
        super.visitPermittedSubclass(permittedSubclass);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String owner, String name, String descriptor) {
        checkState();
        if (this.visitOuterClassCalled) {
            throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        this.visitOuterClassCalled = true;
        if (owner == null) {
            throw new IllegalArgumentException("Illegal outer class owner");
        }
        if (descriptor != null) {
            CheckMethodAdapter.checkMethodDescriptor(this.version, descriptor);
        }
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        checkState();
        CheckMethodAdapter.checkInternalName(this.version, name, "class name");
        if (outerName != null) {
            CheckMethodAdapter.checkInternalName(this.version, outerName, "outer class name");
        }
        if (innerName != null) {
            int startIndex = 0;
            while (startIndex < innerName.length() && Character.isDigit(innerName.charAt(startIndex))) {
                startIndex++;
            }
            if (startIndex == 0 || startIndex < innerName.length()) {
                CheckMethodAdapter.checkIdentifier(this.version, innerName, startIndex, -1, "inner class name");
            }
        }
        checkAccess(access, 30239);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        checkState();
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "record component name");
        CheckMethodAdapter.checkDescriptor(this.version, descriptor, false);
        if (signature != null) {
            checkFieldSignature(signature);
        }
        return new CheckRecordComponentAdapter(this.api, super.visitRecordComponent(name, descriptor, signature));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        checkState();
        checkAccess(access, 184543);
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "field name");
        CheckMethodAdapter.checkDescriptor(this.version, descriptor, false);
        if (signature != null) {
            checkFieldSignature(signature);
        }
        if (value != null) {
            CheckMethodAdapter.checkConstant(value);
        }
        return new CheckFieldAdapter(this.api, super.visitField(access, name, descriptor, signature, value));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        CheckMethodAdapter checkMethodAdapter;
        checkState();
        checkMethodAccess(this.version, access, 171519);
        if (!"<init>".equals(name) && !"<clinit>".equals(name)) {
            CheckMethodAdapter.checkMethodIdentifier(this.version, name, "method name");
        }
        CheckMethodAdapter.checkMethodDescriptor(this.version, descriptor);
        if (signature != null) {
            checkMethodSignature(signature);
        }
        if (exceptions != null) {
            for (int i = 0; i < exceptions.length; i++) {
                CheckMethodAdapter.checkInternalName(this.version, exceptions[i], "exception name at index " + i);
            }
        }
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (this.checkDataFlow) {
            if (this.cv instanceof ClassWriter) {
                methodVisitor = new CheckMethodAdapter.MethodWriterWrapper(this.api, this.version, (ClassWriter) this.cv, methodVisitor);
            }
            checkMethodAdapter = new CheckMethodAdapter(this.api, access, name, descriptor, methodVisitor, this.labelInsnIndices);
        } else {
            checkMethodAdapter = new CheckMethodAdapter(this.api, methodVisitor, this.labelInsnIndices);
        }
        checkMethodAdapter.version = this.version;
        return checkMethodAdapter;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        checkState();
        CheckMethodAdapter.checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        checkState();
        int sort = new TypeReference(typeRef).getSort();
        if (sort != 0 && sort != 17 && sort != 16) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(sort));
        }
        checkTypeRef(typeRef);
        CheckMethodAdapter.checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        checkState();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        checkState();
        this.visitEndCalled = true;
        super.visitEnd();
    }

    private void checkState() {
        if (!this.visitCalled) {
            throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        if (this.visitEndCalled) {
            throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkAccess(int access, int possibleAccess) {
        if ((access & (possibleAccess ^ (-1))) != 0) {
            throw new IllegalArgumentException("Invalid access flags: " + access);
        }
        if (Integer.bitCount(access & 7) > 1) {
            throw new IllegalArgumentException("public, protected and private are mutually exclusive: " + access);
        }
        if (Integer.bitCount(access & 1040) > 1) {
            throw new IllegalArgumentException("final and abstract are mutually exclusive: " + access);
        }
    }

    private static void checkMethodAccess(int version, int access, int possibleAccess) {
        checkAccess(access, possibleAccess);
        if ((version & 65535) < 61 && Integer.bitCount(access & 3072) > 1) {
            throw new IllegalArgumentException("strictfp and abstract are mutually exclusive: " + access);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkFullyQualifiedName(int version, String name, String source) {
        int startIndex = 0;
        while (true) {
            try {
                int dotIndex = name.indexOf(46, startIndex + 1);
                if (dotIndex != -1) {
                    CheckMethodAdapter.checkIdentifier(version, name, startIndex, dotIndex, null);
                    startIndex = dotIndex + 1;
                } else {
                    CheckMethodAdapter.checkIdentifier(version, name, startIndex, name.length(), null);
                    return;
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid " + source + " (must be a fully qualified name): " + name, e);
            }
        }
    }

    public static void checkClassSignature(String signature) {
        int pos;
        int pos2 = 0;
        if (getChar(signature, 0) == '<') {
            pos2 = checkTypeParameters(signature, 0);
        }
        int checkClassTypeSignature = checkClassTypeSignature(signature, pos2);
        while (true) {
            pos = checkClassTypeSignature;
            if (getChar(signature, pos) != 'L') {
                break;
            }
            checkClassTypeSignature = checkClassTypeSignature(signature, pos);
        }
        if (pos != signature.length()) {
            throw new IllegalArgumentException(signature + ERROR_AT + pos);
        }
    }

    public static void checkMethodSignature(String signature) {
        int pos;
        int pos2;
        int pos3 = 0;
        if (getChar(signature, 0) == '<') {
            pos3 = checkTypeParameters(signature, 0);
        }
        int checkChar = checkChar('(', signature, pos3);
        while (true) {
            pos = checkChar;
            if ("ZCBSIFJDL[T".indexOf(getChar(signature, pos)) == -1) {
                break;
            }
            checkChar = checkJavaTypeSignature(signature, pos);
        }
        int pos4 = checkChar(')', signature, pos);
        if (getChar(signature, pos4) == 'V') {
            pos2 = pos4 + 1;
        } else {
            pos2 = checkJavaTypeSignature(signature, pos4);
        }
        while (getChar(signature, pos2) == '^') {
            int pos5 = pos2 + 1;
            if (getChar(signature, pos5) == 'L') {
                pos2 = checkClassTypeSignature(signature, pos5);
            } else {
                pos2 = checkTypeVariableSignature(signature, pos5);
            }
        }
        if (pos2 != signature.length()) {
            throw new IllegalArgumentException(signature + ERROR_AT + pos2);
        }
    }

    public static void checkFieldSignature(String signature) {
        int pos = checkReferenceTypeSignature(signature, 0);
        if (pos != signature.length()) {
            throw new IllegalArgumentException(signature + ERROR_AT + pos);
        }
    }

    private static int checkTypeParameters(String signature, int startPos) {
        int checkTypeParameter = checkTypeParameter(signature, checkChar('<', signature, startPos));
        while (true) {
            int pos = checkTypeParameter;
            if (getChar(signature, pos) != '>') {
                checkTypeParameter = checkTypeParameter(signature, pos);
            } else {
                return pos + 1;
            }
        }
    }

    private static int checkTypeParameter(String signature, int startPos) {
        int pos = checkChar(':', signature, checkSignatureIdentifier(signature, startPos));
        if ("L[T".indexOf(getChar(signature, pos)) != -1) {
            pos = checkReferenceTypeSignature(signature, pos);
        }
        while (getChar(signature, pos) == ':') {
            pos = checkReferenceTypeSignature(signature, pos + 1);
        }
        return pos;
    }

    private static int checkReferenceTypeSignature(String signature, int pos) {
        switch (getChar(signature, pos)) {
            case 'L':
                return checkClassTypeSignature(signature, pos);
            case '[':
                return checkJavaTypeSignature(signature, pos + 1);
            default:
                return checkTypeVariableSignature(signature, pos);
        }
    }

    private static int checkClassTypeSignature(String signature, int startPos) {
        int pos;
        int checkSignatureIdentifier = checkSignatureIdentifier(signature, checkChar('L', signature, startPos));
        while (true) {
            pos = checkSignatureIdentifier;
            if (getChar(signature, pos) != '/') {
                break;
            }
            checkSignatureIdentifier = checkSignatureIdentifier(signature, pos + 1);
        }
        if (getChar(signature, pos) == '<') {
            pos = checkTypeArguments(signature, pos);
        }
        while (getChar(signature, pos) == '.') {
            pos = checkSignatureIdentifier(signature, pos + 1);
            if (getChar(signature, pos) == '<') {
                pos = checkTypeArguments(signature, pos);
            }
        }
        return checkChar(';', signature, pos);
    }

    private static int checkTypeArguments(String signature, int startPos) {
        int checkTypeArgument = checkTypeArgument(signature, checkChar('<', signature, startPos));
        while (true) {
            int pos = checkTypeArgument;
            if (getChar(signature, pos) != '>') {
                checkTypeArgument = checkTypeArgument(signature, pos);
            } else {
                return pos + 1;
            }
        }
    }

    private static int checkTypeArgument(String signature, int startPos) {
        int pos = startPos;
        char c = getChar(signature, pos);
        if (c == '*') {
            return pos + 1;
        }
        if (c == '+' || c == '-') {
            pos++;
        }
        return checkReferenceTypeSignature(signature, pos);
    }

    private static int checkTypeVariableSignature(String signature, int startPos) {
        int pos = checkChar('T', signature, startPos);
        return checkChar(';', signature, checkSignatureIdentifier(signature, pos));
    }

    private static int checkJavaTypeSignature(String signature, int startPos) {
        switch (getChar(signature, startPos)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return startPos + 1;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return checkReferenceTypeSignature(signature, startPos);
        }
    }

    private static int checkSignatureIdentifier(String signature, int startPos) {
        int pos;
        int i = startPos;
        while (true) {
            pos = i;
            if (pos >= signature.length() || ".;[/<>:".indexOf(signature.codePointAt(pos)) != -1) {
                break;
            }
            i = signature.offsetByCodePoints(pos, 1);
        }
        if (pos == startPos) {
            throw new IllegalArgumentException(signature + ": identifier expected at index " + startPos);
        }
        return pos;
    }

    private static int checkChar(char c, String signature, int pos) {
        if (getChar(signature, pos) == c) {
            return pos + 1;
        }
        throw new IllegalArgumentException(signature + ": '" + c + "' expected at index " + pos);
    }

    private static char getChar(String string, int pos) {
        if (pos < string.length()) {
            return string.charAt(pos);
        }
        return (char) 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkTypeRef(int typeRef) {
        int mask = 0;
        switch (typeRef >>> 24) {
            case 0:
            case 1:
            case 22:
                mask = -65536;
                break;
            case 16:
            case 17:
            case 18:
            case 23:
            case 66:
                mask = -256;
                break;
            case 19:
            case 20:
            case 21:
            case 64:
            case 65:
            case 67:
            case 68:
            case 69:
            case 70:
                mask = -16777216;
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                mask = -16776961;
                break;
        }
        if (mask == 0 || (typeRef & (mask ^ (-1))) != 0) {
            throw new IllegalArgumentException("Invalid type reference 0x" + Integer.toHexString(typeRef));
        }
    }

    private static String packageName(String name) {
        int index = name.lastIndexOf(47);
        if (index == -1) {
            return "";
        }
        return name.substring(0, index);
    }

    public static void main(String[] args) throws IOException {
        main(args, new PrintWriter((OutputStream) System.err, true));
    }

    static void main(String[] args, PrintWriter logger) throws IOException {
        ClassReader classReader;
        if (args.length != 1) {
            logger.println(USAGE);
            return;
        }
        if (args[0].endsWith(".class")) {
            InputStream inputStream = new FileInputStream(args[0]);
            try {
                classReader = new ClassReader(inputStream);
                inputStream.close();
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (Throwable th2) {
                }
                throw th;
            }
        } else {
            classReader = new ClassReader(args[0]);
        }
        verify(classReader, false, logger);
    }

    public static void verify(ClassReader classReader, boolean printResults, PrintWriter printWriter) {
        verify(classReader, null, printResults, printWriter);
    }

    public static void verify(ClassReader classReader, ClassLoader loader, boolean printResults, PrintWriter printWriter) {
        ClassNode classNode = new ClassNode();
        classReader.accept(new CheckClassAdapter(17432576, classNode, false) { // from class: org.objectweb.asm.util.CheckClassAdapter.1
        }, 2);
        Type syperType = classNode.superName == null ? null : Type.getObjectType(classNode.superName);
        List<MethodNode> methods = classNode.methods;
        List<Type> interfaces = new ArrayList<>();
        for (String interfaceName : classNode.interfaces) {
            interfaces.add(Type.getObjectType(interfaceName));
        }
        for (MethodNode method : methods) {
            SimpleVerifier verifier = new SimpleVerifier(Type.getObjectType(classNode.name), syperType, interfaces, (classNode.access & 512) != 0);
            Analyzer<BasicValue> analyzer = new Analyzer<>(verifier);
            if (loader != null) {
                verifier.setClassLoader(loader);
            }
            try {
                analyzer.analyze(classNode.name, method);
            } catch (AnalyzerException e) {
                e.printStackTrace(printWriter);
            }
            if (printResults) {
                printAnalyzerResult(method, analyzer, printWriter);
            }
        }
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void printAnalyzerResult(MethodNode method, Analyzer<BasicValue> analyzer, PrintWriter printWriter) {
        Textifier textifier = new Textifier();
        TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(textifier);
        printWriter.println(method.name + method.desc);
        for (int i = 0; i < method.instructions.size(); i++) {
            method.instructions.get(i).accept(traceMethodVisitor);
            StringBuilder stringBuilder = new StringBuilder();
            Frame<BasicValue> frame = analyzer.getFrames()[i];
            if (frame == null) {
                stringBuilder.append('?');
            } else {
                for (int j = 0; j < frame.getLocals(); j++) {
                    stringBuilder.append(getUnqualifiedName(frame.getLocal(j).toString())).append(' ');
                }
                stringBuilder.append(" : ");
                for (int j2 = 0; j2 < frame.getStackSize(); j2++) {
                    stringBuilder.append(getUnqualifiedName(frame.getStack(j2).toString())).append(' ');
                }
            }
            while (stringBuilder.length() < method.maxStack + method.maxLocals + 1) {
                stringBuilder.append(' ');
            }
            printWriter.print(Integer.toString(i + BZip2Constants.baseBlockSize).substring(1));
            printWriter.print(Instruction.argsep + ((Object) stringBuilder) + " : " + textifier.text.get(textifier.text.size() - 1));
        }
        for (TryCatchBlockNode tryCatchBlock : method.tryCatchBlocks) {
            tryCatchBlock.accept(traceMethodVisitor);
            printWriter.print(Instruction.argsep + textifier.text.get(textifier.text.size() - 1));
        }
        printWriter.println();
    }

    private static String getUnqualifiedName(String name) {
        int lastSlashIndex = name.lastIndexOf(47);
        if (lastSlashIndex == -1) {
            return name;
        }
        int endIndex = name.length();
        if (name.charAt(endIndex - 1) == ';') {
            endIndex--;
        }
        int lastBracketIndex = name.lastIndexOf(91);
        if (lastBracketIndex == -1) {
            return name.substring(lastSlashIndex + 1, endIndex);
        }
        return name.substring(0, lastBracketIndex + 1) + name.substring(lastSlashIndex + 1, endIndex);
    }
}
