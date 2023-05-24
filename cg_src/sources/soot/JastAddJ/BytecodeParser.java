package soot.JastAddJ;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import soot.JastAddJ.Attributes;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BytecodeParser.class */
public class BytecodeParser implements Flags, BytecodeReader {
    public static final boolean VERBOSE = false;
    private DataInputStream is;
    public CONSTANT_Class_Info classInfo;
    public String outerClassName;
    public String name;
    public boolean isInnerClass;
    public CONSTANT_Info[] constantPool;
    private static final int CONSTANT_Class = 7;
    private static final int CONSTANT_FieldRef = 9;
    private static final int CONSTANT_MethodRef = 10;
    private static final int CONSTANT_InterfaceMethodRef = 11;
    private static final int CONSTANT_String = 8;
    private static final int CONSTANT_Integer = 3;
    private static final int CONSTANT_Float = 4;
    private static final int CONSTANT_Long = 5;
    private static final int CONSTANT_Double = 6;
    private static final int CONSTANT_NameAndType = 12;
    private static final int CONSTANT_Utf8 = 1;
    private static final int CONSTANT_MethodHandle = 15;
    private static final int CONSTANT_MethodType = 16;
    private static final int CONSTANT_InvokeDynamic = 18;

    @Override // soot.JastAddJ.BytecodeReader
    public CompilationUnit read(InputStream is, String fullName, Program p) throws FileNotFoundException, IOException {
        return new BytecodeParser(is, fullName).parse(null, null, p);
    }

    public BytecodeParser(byte[] buffer, int size, String name) {
        this.isInnerClass = false;
        this.constantPool = null;
        this.is = new DataInputStream(new ByteArrayInputStream(buffer, 0, size));
        this.name = name;
    }

    public BytecodeParser(InputStream in, String name) {
        this.isInnerClass = false;
        this.constantPool = null;
        this.is = new DataInputStream(new DummyInputStream(in));
        this.name = name;
    }

    public BytecodeParser() {
        this("");
    }

    public BytecodeParser(String name) {
        this.isInnerClass = false;
        this.constantPool = null;
        this.name = name.endsWith(".class") ? name : String.valueOf(name.replace('.', '/')) + ".class";
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/BytecodeParser$DummyInputStream.class */
    private static class DummyInputStream extends InputStream {
        byte[] bytes;
        int pos;
        int size;

        public DummyInputStream(byte[] buffer, int size) {
            this.bytes = buffer;
            this.size = size;
        }

        public DummyInputStream(InputStream is) {
            int status;
            this.bytes = new byte[1024];
            int index = 0;
            this.size = 1024;
            do {
                try {
                    status = is.read(this.bytes, index, this.size - index);
                    if (status != -1) {
                        index += status;
                        if (index == this.size) {
                            byte[] newBytes = new byte[this.size * 2];
                            System.arraycopy(this.bytes, 0, newBytes, 0, this.size);
                            this.bytes = newBytes;
                            this.size *= 2;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Something went wrong trying to read " + is);
                }
            } while (status != -1);
            this.size = index;
            this.pos = 0;
        }

        @Override // java.io.InputStream
        public int available() {
            return this.size - this.pos;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.InputStream
        public void mark(int readlimit) {
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return false;
        }

        @Override // java.io.InputStream
        public int read(byte[] b) {
            int actualLength = Math.min(b.length, this.size - this.pos);
            System.arraycopy(this.bytes, this.pos, b, 0, actualLength);
            this.pos += actualLength;
            return actualLength;
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int offset, int length) {
            int actualLength = Math.min(length, this.size - this.pos);
            System.arraycopy(this.bytes, this.pos, b, offset, actualLength);
            this.pos += actualLength;
            return actualLength;
        }

        @Override // java.io.InputStream
        public void reset() {
        }

        @Override // java.io.InputStream
        public long skip(long n) {
            if (this.size == this.pos) {
                return -1L;
            }
            long skipSize = Math.min(n, this.size - this.pos);
            this.pos = (int) (this.pos + skipSize);
            return skipSize;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.pos < this.size) {
                byte[] bArr = this.bytes;
                int i = this.pos;
                this.pos = i + 1;
                int i2 = bArr[i];
                if (i2 < 0) {
                    i2 += 256;
                }
                return i2;
            }
            return -1;
        }
    }

    public int next() {
        try {
            return this.is.read();
        } catch (IOException e) {
            System.exit(1);
            return -1;
        }
    }

    public int u1() {
        try {
            return this.is.readUnsignedByte();
        } catch (IOException e) {
            System.exit(1);
            return -1;
        }
    }

    public int u2() {
        try {
            return this.is.readUnsignedShort();
        } catch (IOException e) {
            System.exit(1);
            return -1;
        }
    }

    public int u4() {
        try {
            return this.is.readInt();
        } catch (IOException e) {
            System.exit(1);
            return -1;
        }
    }

    public int readInt() {
        try {
            return this.is.readInt();
        } catch (IOException e) {
            System.exit(1);
            return -1;
        }
    }

    public float readFloat() {
        try {
            return this.is.readFloat();
        } catch (IOException e) {
            System.exit(1);
            return -1.0f;
        }
    }

    public long readLong() {
        try {
            return this.is.readLong();
        } catch (IOException e) {
            System.exit(1);
            return -1L;
        }
    }

    public double readDouble() {
        try {
            return this.is.readDouble();
        } catch (IOException e) {
            System.exit(1);
            return -1.0d;
        }
    }

    public String readUTF() {
        try {
            return this.is.readUTF();
        } catch (IOException e) {
            System.exit(1);
            return "";
        }
    }

    public void skip(int length) {
        try {
            this.is.skip(length);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public void error(String s) {
        throw new RuntimeException(s);
    }

    public void print(String s) {
    }

    public void println(String s) {
        print(String.valueOf(s) + "\n");
    }

    public void println() {
        print("\n");
    }

    public CompilationUnit parse(TypeDecl outerTypeDecl, String outerClassName, Program classPath, boolean isInner) throws FileNotFoundException, IOException {
        this.isInnerClass = isInner;
        return parse(outerTypeDecl, outerClassName, classPath);
    }

    public CompilationUnit parse(TypeDecl outerTypeDecl, String outerClassName, Program program) throws FileNotFoundException, IOException {
        if (this.is == null) {
            FileInputStream file = new FileInputStream(this.name);
            if (file == null) {
                throw new FileNotFoundException(this.name);
            }
            this.is = new DataInputStream(new BufferedInputStream(file));
        }
        this.outerClassName = outerClassName;
        parseMagic();
        parseMinor();
        parseMajor();
        parseConstantPool();
        CompilationUnit cu = new CompilationUnit();
        TypeDecl typeDecl = parseTypeDecl();
        cu.setPackageDecl(this.classInfo.packageDecl());
        cu.addTypeDecl(typeDecl);
        parseFields(typeDecl);
        parseMethods(typeDecl);
        if (new Attributes.TypeAttributes(this, typeDecl, outerTypeDecl, program).isInnerClass()) {
            program.addCompilationUnit(cu);
            for (int i = 0; i < cu.getTypeDecls().getNumChild(); i++) {
                cu.getTypeDecls().removeChild(i);
            }
            program.getCompilationUnits().removeChild(program.getCompilationUnits().getIndexOfChild(cu));
        }
        this.is.close();
        this.is = null;
        return cu;
    }

    public void parseMagic() {
        if (next() != 202 || next() != 254 || next() != 186 || next() != 190) {
            error("magic error");
        }
    }

    public void parseMinor() {
        u1();
        u1();
    }

    public void parseMajor() {
        u1();
        u1();
    }

    public TypeDecl parseTypeDecl() {
        int flags = u2();
        Modifiers modifiers = modifiers(flags & 64991);
        if ((flags & 16896) == 16384) {
            EnumDecl decl = new EnumDecl();
            decl.setModifiers(modifiers);
            decl.setID(parseThisClass());
            parseSuperClass();
            decl.setImplementsList(parseInterfaces(new List()));
            return decl;
        } else if ((flags & 512) == 0) {
            ClassDecl decl2 = new ClassDecl();
            decl2.setModifiers(modifiers);
            decl2.setID(parseThisClass());
            Access superClass = parseSuperClass();
            decl2.setSuperClassAccessOpt(superClass == null ? new Opt<>() : new Opt<>(superClass));
            decl2.setImplementsList(parseInterfaces(new List()));
            return decl2;
        } else if ((flags & 8192) == 0) {
            InterfaceDecl decl3 = new InterfaceDecl();
            decl3.setModifiers(modifiers);
            decl3.setID(parseThisClass());
            Access superClass2 = parseSuperClass();
            decl3.setSuperInterfaceIdList(parseInterfaces(superClass2 == null ? new List() : new List().add(superClass2)));
            return decl3;
        } else {
            AnnotationDecl decl4 = new AnnotationDecl();
            decl4.setModifiers(modifiers);
            decl4.setID(parseThisClass());
            Access superClass3 = parseSuperClass();
            parseInterfaces(superClass3 == null ? new List() : new List().add(superClass3));
            return decl4;
        }
    }

    public String parseThisClass() {
        int index = u2();
        CONSTANT_Class_Info info = (CONSTANT_Class_Info) this.constantPool[index];
        this.classInfo = info;
        return info.simpleName();
    }

    public Access parseSuperClass() {
        int index = u2();
        if (index == 0) {
            return null;
        }
        CONSTANT_Class_Info info = (CONSTANT_Class_Info) this.constantPool[index];
        return info.access();
    }

    public List parseInterfaces(List list) {
        int count = u2();
        for (int i = 0; i < count; i++) {
            CONSTANT_Class_Info info = (CONSTANT_Class_Info) this.constantPool[u2()];
            list.add(info.access());
        }
        return list;
    }

    public Access fromClassName(String s) {
        String packageName = "";
        int index = s.lastIndexOf(47);
        if (index != -1) {
            packageName = s.substring(0, index).replace('/', '.');
        }
        String typeName = s.substring(index + 1, s.length());
        if (typeName.indexOf(36) != -1) {
            return new BytecodeTypeAccess(packageName, typeName);
        }
        return new TypeAccess(packageName, typeName);
    }

    public static Modifiers modifiers(int flags) {
        Modifiers m = new Modifiers();
        if ((flags & 1) != 0) {
            m.addModifier(new Modifier(Jimple.PUBLIC));
        }
        if ((flags & 2) != 0) {
            m.addModifier(new Modifier(Jimple.PRIVATE));
        }
        if ((flags & 4) != 0) {
            m.addModifier(new Modifier(Jimple.PROTECTED));
        }
        if ((flags & 8) != 0) {
            m.addModifier(new Modifier(Jimple.STATIC));
        }
        if ((flags & 16) != 0) {
            m.addModifier(new Modifier(Jimple.FINAL));
        }
        if ((flags & 32) != 0) {
            m.addModifier(new Modifier(Jimple.SYNCHRONIZED));
        }
        if ((flags & 64) != 0) {
            m.addModifier(new Modifier(Jimple.VOLATILE));
        }
        if ((flags & 128) != 0) {
            m.addModifier(new Modifier(Jimple.TRANSIENT));
        }
        if ((flags & 256) != 0) {
            m.addModifier(new Modifier(Jimple.NATIVE));
        }
        if ((flags & 1024) != 0) {
            m.addModifier(new Modifier(Jimple.ABSTRACT));
        }
        if ((flags & 2048) != 0) {
            m.addModifier(new Modifier(Jimple.STRICTFP));
        }
        return m;
    }

    public void parseFields(TypeDecl typeDecl) {
        int count = u2();
        for (int i = 0; i < count; i++) {
            FieldInfo fieldInfo = new FieldInfo(this);
            if (!fieldInfo.isSynthetic()) {
                typeDecl.addBodyDecl(fieldInfo.bodyDecl());
            }
        }
    }

    public void parseMethods(TypeDecl typeDecl) {
        int count = u2();
        for (int i = 0; i < count; i++) {
            MethodInfo info = new MethodInfo(this);
            if (!info.isSynthetic() && !info.name.equals("<clinit>")) {
                typeDecl.addBodyDecl(info.bodyDecl());
            }
        }
    }

    private void checkLengthAndNull(int index) {
        if (index >= this.constantPool.length) {
            throw new Error("Trying to access element " + index + " in constant pool of length " + this.constantPool.length);
        }
        if (this.constantPool[index] == null) {
            throw new Error("Unexpected null element in constant pool at index " + index);
        }
    }

    public boolean validConstantPoolIndex(int index) {
        return index < this.constantPool.length && this.constantPool[index] != null;
    }

    public CONSTANT_Info getCONSTANT_Info(int index) {
        checkLengthAndNull(index);
        return this.constantPool[index];
    }

    public CONSTANT_Utf8_Info getCONSTANT_Utf8_Info(int index) {
        checkLengthAndNull(index);
        CONSTANT_Info info = this.constantPool[index];
        if (!(info instanceof CONSTANT_Utf8_Info)) {
            throw new Error("Expected CONSTANT_Utf8_info at " + index + " in constant pool but found " + info.getClass().getName());
        }
        return (CONSTANT_Utf8_Info) info;
    }

    public CONSTANT_Class_Info getCONSTANT_Class_Info(int index) {
        checkLengthAndNull(index);
        CONSTANT_Info info = this.constantPool[index];
        if (!(info instanceof CONSTANT_Class_Info)) {
            throw new Error("Expected CONSTANT_Class_info at " + index + " in constant pool but found " + info.getClass().getName());
        }
        return (CONSTANT_Class_Info) info;
    }

    public void parseConstantPool() {
        int count = u2();
        this.constantPool = new CONSTANT_Info[count + 1];
        int i = 1;
        while (i < count) {
            parseEntry(i);
            if ((this.constantPool[i] instanceof CONSTANT_Long_Info) || (this.constantPool[i] instanceof CONSTANT_Double_Info)) {
                i++;
            }
            i++;
        }
    }

    public void parseEntry(int i) {
        int tag = u1();
        switch (tag) {
            case 1:
                this.constantPool[i] = new CONSTANT_Utf8_Info(this);
                return;
            case 2:
            case 13:
            case 14:
            case 17:
            default:
                println("Unknown entry: " + tag);
                return;
            case 3:
                this.constantPool[i] = new CONSTANT_Integer_Info(this);
                return;
            case 4:
                this.constantPool[i] = new CONSTANT_Float_Info(this);
                return;
            case 5:
                this.constantPool[i] = new CONSTANT_Long_Info(this);
                return;
            case 6:
                this.constantPool[i] = new CONSTANT_Double_Info(this);
                return;
            case 7:
                this.constantPool[i] = new CONSTANT_Class_Info(this);
                return;
            case 8:
                this.constantPool[i] = new CONSTANT_String_Info(this);
                return;
            case 9:
                this.constantPool[i] = new CONSTANT_Fieldref_Info(this);
                return;
            case 10:
                this.constantPool[i] = new CONSTANT_Methodref_Info(this);
                return;
            case 11:
                this.constantPool[i] = new CONSTANT_InterfaceMethodref_Info(this);
                return;
            case 12:
                this.constantPool[i] = new CONSTANT_NameAndType_Info(this);
                return;
            case 15:
                this.constantPool[i] = new CONSTANT_MethodHandle_Info(this);
                return;
            case 16:
                this.constantPool[i] = new CONSTANT_MethodType_Info(this);
                return;
            case 18:
                this.constantPool[i] = new CONSTANT_InvokeDynamic_Info(this);
                return;
        }
    }
}
