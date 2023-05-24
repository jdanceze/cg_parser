package polyglot.types.reflect;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.types.CachingResolver;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MethodInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.InnerClasses;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/ClassFile.class */
public class ClassFile implements LazyClassInitializer {
    protected Constant[] constants;
    int modifiers;
    int thisClass;
    int superClass;
    int[] interfaces;
    protected Field[] fields;
    protected Method[] methods;
    protected Attribute[] attrs;
    protected InnerClasses innerClasses;
    File classFileSource;
    private ExtensionInfo extensionInfo;
    static Collection verbose = ClassFileLoader.verbose;
    Map jlcInfo = new HashMap();

    public ClassFile(File classFileSource, byte[] code, ExtensionInfo ext) {
        this.classFileSource = classFileSource;
        this.extensionInfo = ext;
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(code);
            DataInputStream in = new DataInputStream(bin);
            read(in);
            in.close();
            bin.close();
        } catch (IOException e) {
            throw new InternalCompilerError("I/O exception on ByteArrayInputStream");
        }
    }

    public Method createMethod(DataInputStream in) throws IOException {
        Method m = new Method(in, this);
        m.initialize();
        return m;
    }

    public Field createField(DataInputStream in) throws IOException {
        Field f = new Field(in, this);
        f.initialize();
        return f;
    }

    public Attribute createAttribute(DataInputStream in, String name, int nameIndex, int length) throws IOException {
        if (name.equals("InnerClasses")) {
            this.innerClasses = new InnerClasses(in, nameIndex, length);
            return this.innerClasses;
        }
        return null;
    }

    public Constant[] constants() {
        return this.constants;
    }

    @Override // polyglot.types.LazyClassInitializer
    public boolean fromClassFile() {
        return true;
    }

    JLCInfo getJLCInfo(String ts) {
        JLCInfo jlc = (JLCInfo) this.jlcInfo.get(ts);
        if (jlc != null) {
            return jlc;
        }
        JLCInfo jlc2 = new JLCInfo();
        this.jlcInfo.put(ts, jlc2);
        int mask = 0;
        for (int i = 0; i < this.fields.length; i++) {
            try {
                if (this.fields[i].name().equals(new StringBuffer().append("jlc$SourceLastModified$").append(ts).toString())) {
                    jlc2.sourceLastModified = this.fields[i].getLong();
                    mask |= 1;
                } else if (this.fields[i].name().equals(new StringBuffer().append("jlc$CompilerVersion$").append(ts).toString())) {
                    jlc2.compilerVersion = this.fields[i].getString();
                    mask |= 2;
                } else if (this.fields[i].name().equals(new StringBuffer().append("jlc$ClassType$").append(ts).toString())) {
                    jlc2.encodedClassType = this.fields[i].getString();
                    mask |= 4;
                }
            } catch (SemanticException e) {
                jlc2.sourceLastModified = 0L;
                jlc2.compilerVersion = null;
                jlc2.encodedClassType = null;
            }
        }
        if (mask != 7) {
            jlc2.sourceLastModified = 0L;
            jlc2.compilerVersion = null;
            jlc2.encodedClassType = null;
        }
        return jlc2;
    }

    public long sourceLastModified(String ts) {
        JLCInfo jlc = getJLCInfo(ts);
        return jlc.sourceLastModified;
    }

    public long rawSourceLastModified() {
        return this.classFileSource.lastModified();
    }

    public String compilerVersion(String ts) {
        JLCInfo jlc = getJLCInfo(ts);
        return jlc.compilerVersion;
    }

    public String encodedClassType(String ts) {
        JLCInfo jlc = getJLCInfo(ts);
        return jlc.encodedClassType;
    }

    void read(DataInputStream in) throws IOException {
        readHeader(in);
        readConstantPool(in);
        readAccessFlags(in);
        readClassInfo(in);
        readFields(in);
        readMethods(in);
        readAttributes(in);
    }

    public ParsedClassType type(TypeSystem ts) throws SemanticException {
        ParsedClassType ct = createType(ts);
        if (ts.equals(ct, ts.Object())) {
            ct.superType(null);
        } else {
            String superName = classNameCP(this.superClass);
            if (superName != null) {
                ct.superType(typeForName(ts, superName));
            } else {
                ct.superType(ts.Object());
            }
        }
        return ct;
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initMemberClasses(ParsedClassType ct) {
        String name;
        int index;
        if (this.innerClasses == null) {
            return;
        }
        TypeSystem ts = ct.typeSystem();
        for (int i = 0; i < this.innerClasses.classes.length; i++) {
            InnerClasses.Info c = this.innerClasses.classes[i];
            if (c.outerClassIndex == this.thisClass && c.classIndex != 0 && ((index = (name = classNameCP(c.classIndex)).lastIndexOf(36)) < 0 || !Character.isDigit(name.charAt(index + 1)))) {
                ClassType t = quietTypeForName(ts, name);
                if (t.isMember()) {
                    if (Report.should_report(verbose, 3)) {
                        Report.report(3, new StringBuffer().append("adding member ").append(t).append(" to ").append(ct).toString());
                    }
                    ct.addMemberClass(t);
                    if (t instanceof ParsedClassType) {
                        ParsedClassType pt = (ParsedClassType) t;
                        pt.flags(ts.flagsForBits(c.modifiers));
                    }
                } else {
                    throw new InternalCompilerError(new StringBuffer().append(name).append(" should be a member class.").toString());
                }
            }
        }
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initInterfaces(ParsedClassType ct) {
        TypeSystem ts = ct.typeSystem();
        for (int i = 0; i < this.interfaces.length; i++) {
            String name = classNameCP(this.interfaces[i]);
            ct.addInterface(quietTypeForName(ts, name));
        }
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initFields(ParsedClassType ct) {
        TypeSystem ts = ct.typeSystem();
        LazyClassInitializer init = ts.defaultClassInitializer();
        init.initFields(ct);
        for (int i = 0; i < this.fields.length; i++) {
            if (!this.fields[i].name().startsWith("jlc$") && !this.fields[i].isSynthetic()) {
                FieldInstance fi = this.fields[i].fieldInstance(ts, ct);
                if (Report.should_report(verbose, 3)) {
                    Report.report(3, new StringBuffer().append("adding ").append(fi).append(" to ").append(ct).toString());
                }
                ct.addField(fi);
            }
        }
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initMethods(ParsedClassType ct) {
        TypeSystem ts = ct.typeSystem();
        for (int i = 0; i < this.methods.length; i++) {
            if (!this.methods[i].name().equals("<init>") && !this.methods[i].name().equals("<clinit>") && !this.methods[i].isSynthetic()) {
                MethodInstance mi = this.methods[i].methodInstance(ts, ct);
                if (Report.should_report(verbose, 3)) {
                    Report.report(3, new StringBuffer().append("adding ").append(mi).append(" to ").append(ct).toString());
                }
                ct.addMethod(mi);
            }
        }
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initConstructors(ParsedClassType ct) {
        TypeSystem ts = ct.typeSystem();
        for (int i = 0; i < this.methods.length; i++) {
            if (this.methods[i].name().equals("<init>") && !this.methods[i].isSynthetic()) {
                ConstructorInstance ci = this.methods[i].constructorInstance(ts, ct, this.fields);
                if (Report.should_report(verbose, 3)) {
                    Report.report(3, new StringBuffer().append("adding ").append(ci).append(" to ").append(ct).toString());
                }
                ct.addConstructor(ci);
            }
        }
    }

    Type arrayOf(Type t, int dims) {
        if (dims == 0) {
            return t;
        }
        return t.typeSystem().arrayOf(t, dims);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List typeListForString(TypeSystem ts, String str) {
        List types = new ArrayList();
        int i = 0;
        while (i < str.length()) {
            int dims = 0;
            while (str.charAt(i) == '[') {
                dims++;
                i++;
            }
            switch (str.charAt(i)) {
                case 'B':
                    types.add(arrayOf(ts.Byte(), dims));
                    break;
                case 'C':
                    types.add(arrayOf(ts.Char(), dims));
                    break;
                case 'D':
                    types.add(arrayOf(ts.Double(), dims));
                    break;
                case 'F':
                    types.add(arrayOf(ts.Float(), dims));
                    break;
                case 'I':
                    types.add(arrayOf(ts.Int(), dims));
                    break;
                case 'J':
                    types.add(arrayOf(ts.Long(), dims));
                    break;
                case 'L':
                    i++;
                    while (true) {
                        if (i >= str.length()) {
                            break;
                        } else if (str.charAt(i) == ';') {
                            String s = str.substring(i, i);
                            types.add(arrayOf(quietTypeForName(ts, s.replace('/', '.')), dims));
                            break;
                        } else {
                            i++;
                        }
                    }
                    break;
                case 'S':
                    types.add(arrayOf(ts.Short(), dims));
                    break;
                case 'V':
                    types.add(arrayOf(ts.Void(), dims));
                    break;
                case 'Z':
                    types.add(arrayOf(ts.Boolean(), dims));
                    break;
            }
            i++;
        }
        if (Report.should_report(verbose, 4)) {
            Report.report(4, new StringBuffer().append("parsed \"").append(str).append("\" -> ").append(types).toString());
        }
        return types;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Type typeForString(TypeSystem ts, String str) {
        List l = typeListForString(ts, str);
        if (l.size() == 1) {
            return (Type) l.get(0);
        }
        throw new InternalCompilerError(new StringBuffer().append("Bad type string: \"").append(str).append("\"").toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassType quietTypeForName(TypeSystem ts, String name) {
        if (Report.should_report(verbose, 2)) {
            Report.report(2, new StringBuffer().append("resolving ").append(name).toString());
        }
        try {
            return (ClassType) ts.systemResolver().find(name);
        } catch (SemanticException e) {
            throw new InternalCompilerError(new StringBuffer().append("could not load ").append(name).toString());
        }
    }

    public ClassType typeForName(TypeSystem ts, String name) throws SemanticException {
        if (Report.should_report(verbose, 2)) {
            Report.report(2, new StringBuffer().append("resolving ").append(name).toString());
        }
        return (ClassType) ts.systemResolver().find(name);
    }

    ParsedClassType createType(TypeSystem ts) throws SemanticException {
        String innerName;
        String name = classNameCP(this.thisClass);
        if (Report.should_report(verbose, 2)) {
            Report.report(2, new StringBuffer().append("creating ClassType for ").append(name).toString());
        }
        ParsedClassType ct = ts.createClassType(this);
        ct.flags(ts.flagsForBits(this.modifiers));
        ct.position(position());
        ((CachingResolver) ts.systemResolver()).install(name, ct);
        String packageName = StringUtil.getPackageComponent(name);
        if (!packageName.equals("")) {
            ct.package_(ts.packageForName(packageName));
        }
        String className = StringUtil.getShortNameComponent(name);
        String outerName = name;
        while (true) {
            int dollar = outerName.lastIndexOf(36);
            if (dollar >= 0) {
                outerName = name.substring(0, dollar);
                innerName = name.substring(dollar + 1);
                try {
                    if (Report.should_report(verbose, 2)) {
                        Report.report(2, new StringBuffer().append("resolving ").append(outerName).append(" for ").append(name).toString());
                    }
                    ct.outer(typeForName(ts, outerName));
                    break;
                } catch (SemanticException e) {
                    if (Report.should_report(verbose, 3)) {
                        Report.report(2, new StringBuffer().append("error resolving ").append(outerName).toString());
                    }
                }
            } else {
                innerName = null;
                break;
            }
        }
        ClassType.Kind kind = ClassType.TOP_LEVEL;
        if (innerName != null) {
            StringTokenizer st = new StringTokenizer(className, "$");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (Character.isDigit(s.charAt(0))) {
                    kind = ClassType.ANONYMOUS;
                } else if (kind == ClassType.ANONYMOUS) {
                    kind = ClassType.LOCAL;
                } else {
                    kind = ClassType.MEMBER;
                }
            }
        }
        if (Report.should_report(verbose, 3)) {
            Report.report(3, new StringBuffer().append(name).append(" is ").append(kind).toString());
        }
        ct.kind(kind);
        if (ct.isTopLevel()) {
            ct.name(className);
        } else if (ct.isMember() || ct.isLocal()) {
            ct.name(innerName);
        }
        return ct;
    }

    public Position position() {
        return new Position(new StringBuffer().append(name()).append(".class").toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String classNameCP(int index) {
        Integer nameIndex;
        Constant c = this.constants[index];
        if (c != null && c.tag() == 7 && (nameIndex = (Integer) c.value()) != null) {
            Constant c2 = this.constants[nameIndex.intValue()];
            if (c2.tag() == 1) {
                String s = (String) c2.value();
                return s.replace('/', '.');
            }
            return null;
        }
        return null;
    }

    public String name() {
        Integer nameIndex;
        Constant c = this.constants[this.thisClass];
        if (c.tag() == 7 && (nameIndex = (Integer) c.value()) != null) {
            Constant c2 = this.constants[nameIndex.intValue()];
            if (c2.tag() == 1) {
                return (String) c2.value();
            }
        }
        throw new ClassFormatError("Couldn't find class name in file");
    }

    Constant readConstant(DataInputStream in) throws IOException {
        Object value;
        int tag = in.readUnsignedByte();
        switch (tag) {
            case 1:
                value = in.readUTF();
                break;
            case 2:
            default:
                throw new ClassFormatError(new StringBuffer().append("Invalid constant tag: ").append(tag).toString());
            case 3:
                value = new Integer(in.readInt());
                break;
            case 4:
                value = new Float(in.readFloat());
                break;
            case 5:
                value = new Long(in.readLong());
                break;
            case 6:
                value = new Double(in.readDouble());
                break;
            case 7:
            case 8:
                value = new Integer(in.readUnsignedShort());
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                value = new int[2];
                ((int[]) value)[0] = in.readUnsignedShort();
                ((int[]) value)[1] = in.readUnsignedShort();
                break;
        }
        return new Constant(tag, value);
    }

    void readHeader(DataInputStream in) throws IOException {
        int magic = in.readInt();
        if (magic != -889275714) {
            throw new ClassFormatError("Bad magic number.");
        }
        in.readUnsignedShort();
        in.readUnsignedShort();
    }

    void readConstantPool(DataInputStream in) throws IOException {
        int count = in.readUnsignedShort();
        this.constants = new Constant[count];
        this.constants[0] = null;
        int i = 1;
        while (i < count) {
            this.constants[i] = readConstant(in);
            switch (this.constants[i].tag()) {
                case 5:
                case 6:
                    i++;
                    this.constants[i] = null;
                    break;
            }
            i++;
        }
    }

    void readAccessFlags(DataInputStream in) throws IOException {
        this.modifiers = in.readUnsignedShort();
    }

    void readClassInfo(DataInputStream in) throws IOException {
        this.thisClass = in.readUnsignedShort();
        this.superClass = in.readUnsignedShort();
        int numInterfaces = in.readUnsignedShort();
        this.interfaces = new int[numInterfaces];
        for (int i = 0; i < numInterfaces; i++) {
            this.interfaces[i] = in.readUnsignedShort();
        }
    }

    void readFields(DataInputStream in) throws IOException {
        int numFields = in.readUnsignedShort();
        this.fields = new Field[numFields];
        for (int i = 0; i < numFields; i++) {
            this.fields[i] = createField(in);
        }
    }

    void readMethods(DataInputStream in) throws IOException {
        int numMethods = in.readUnsignedShort();
        this.methods = new Method[numMethods];
        for (int i = 0; i < numMethods; i++) {
            this.methods[i] = createMethod(in);
        }
    }

    public void readAttributes(DataInputStream in) throws IOException {
        int numAttributes = in.readUnsignedShort();
        this.attrs = new Attribute[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            int nameIndex = in.readUnsignedShort();
            int length = in.readInt();
            String name = (String) this.constants[nameIndex].value();
            Attribute a = createAttribute(in, name, nameIndex, length);
            if (a != null) {
                this.attrs[i] = a;
            } else {
                long n = in.skip(length);
                if (n != length) {
                    throw new EOFException();
                }
            }
        }
    }
}
