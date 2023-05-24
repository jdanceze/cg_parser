package soot.asm;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import soot.ModuleRefType;
import soot.ModuleUtil;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootModuleInfo;
import soot.SootModuleResolver;
import soot.SootResolver;
import soot.Type;
import soot.options.Options;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/asm/SootClassBuilder.class */
public class SootClassBuilder extends ClassVisitor {
    protected final SootClass klass;
    protected final Set<Type> deps;
    protected TagBuilder tb;

    /* JADX INFO: Access modifiers changed from: protected */
    public SootClassBuilder(SootClass klass) {
        super(Opcodes.ASM9);
        this.klass = klass;
        this.deps = new HashSet();
    }

    private TagBuilder getTagBuilder() {
        TagBuilder t = this.tb;
        if (t == null) {
            TagBuilder tagBuilder = new TagBuilder(this.klass, this);
            this.tb = tagBuilder;
            t = tagBuilder;
        }
        return t;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootClass getKlass() {
        return this.klass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addDep(String s) {
        addDep(makeRefType(AsmUtil.baseTypeName(s)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addDep(Type s) {
        this.deps.add(s);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        setJavaVersion(version);
        if (access != 32768 && ModuleUtil.module_mode()) {
            SootModuleInfo moduleInfo = (SootModuleInfo) SootModuleResolver.v().makeClassRef(SootModuleInfo.MODULE_INFO, Optional.fromNullable(this.klass.moduleName));
            this.klass.setModuleInformation(moduleInfo);
        }
        String name2 = AsmUtil.toQualifiedName(name);
        if (!name2.equals(this.klass.getName()) && Options.v().verbose()) {
            System.err.println("Class names not equal! " + name2 + " != " + this.klass.getName());
        }
        this.klass.setModifiers(filterASMFlags(access) & (-33));
        if (superName != null) {
            String superName2 = AsmUtil.toQualifiedName(superName);
            addDep(makeRefType(superName2));
            SootClass superClass = makeClassRef(superName2);
            this.klass.setSuperclass(superClass);
        }
        for (String intrf : interfaces) {
            String intrf2 = AsmUtil.toQualifiedName(intrf);
            addDep(makeRefType(intrf2));
            SootClass interfaceClass = makeClassRef(intrf2);
            interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
            this.klass.addInterface(interfaceClass);
        }
        if (signature != null) {
            this.klass.addTag(new SignatureTag(signature));
        }
    }

    private void setJavaVersion(int version) {
        Options opts = Options.v();
        if (opts.derive_java_version()) {
            opts.set_java_version(Math.max(opts.java_version(), AsmUtil.byteCodeToJavaVersion(version)));
        }
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Tag tag;
        Type type = AsmUtil.toJimpleType(desc, Optional.fromNullable(this.klass.moduleName));
        addDep(type);
        SootField field = Scene.v().makeSootField(name, type, filterASMFlags(access));
        if (value instanceof Integer) {
            tag = new IntegerConstantValueTag(((Integer) value).intValue());
        } else if (value instanceof Float) {
            tag = new FloatConstantValueTag(((Float) value).floatValue());
        } else if (value instanceof Long) {
            tag = new LongConstantValueTag(((Long) value).longValue());
        } else if (value instanceof Double) {
            tag = new DoubleConstantValueTag(((Double) value).doubleValue());
        } else if (value instanceof String) {
            tag = new StringConstantValueTag(value.toString());
        } else {
            tag = null;
        }
        if (tag != null) {
            field.addTag(tag);
        }
        if (signature != null) {
            field.addTag(new SignatureTag(signature));
        }
        return new FieldBuilder(this.klass.getOrAddField(field), this);
    }

    public static int filterASMFlags(int access) {
        return access & (-131073) & (-65537);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        List<SootClass> thrownExceptions;
        if (exceptions == null || exceptions.length == 0) {
            thrownExceptions = Collections.emptyList();
        } else {
            int len = exceptions.length;
            thrownExceptions = new ArrayList<>(len);
            for (int i = 0; i != len; i++) {
                String ex = AsmUtil.toQualifiedName(exceptions[i]);
                addDep(makeRefType(ex));
                thrownExceptions.add(makeClassRef(ex));
            }
        }
        List<Type> sigTypes = AsmUtil.toJimpleDesc(desc, Optional.fromNullable(this.klass.moduleName));
        for (Type type : sigTypes) {
            addDep(type);
        }
        SootMethod method = Scene.v().makeSootMethod(name, sigTypes, sigTypes.remove(sigTypes.size() - 1), filterASMFlags(access), thrownExceptions);
        if (signature != null) {
            method.addTag(new SignatureTag(signature));
        }
        return createMethodBuilder(this.klass.getOrAddMethod(method), desc, exceptions);
    }

    protected MethodVisitor createMethodBuilder(SootMethod sootMethod, String desc, String[] exceptions) {
        return new MethodBuilder(sootMethod, this, desc, exceptions);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitSource(String source, String debug) {
        if (source != null) {
            this.klass.addTag(new SourceFileTag(source));
        }
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.klass.addTag(new InnerClassTag(name, outerName, innerName, access));
        if (!(this.klass instanceof SootModuleInfo)) {
            this.deps.add(makeRefType(AsmUtil.toQualifiedName(name)));
        }
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String owner, String name, String desc) {
        if (name != null) {
            this.klass.addTag(new EnclosingMethodTag(owner, name, desc));
        }
        String owner2 = AsmUtil.toQualifiedName(owner);
        this.deps.add(makeRefType(owner2));
        this.klass.setOuterClass(makeClassRef(owner2));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return getTagBuilder().visitAnnotation(desc, visible);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attr) {
        getTagBuilder().visitAttribute(attr);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public ModuleVisitor visitModule(String name, int access, String version) {
        return new SootModuleInfoBuilder(name, (SootModuleInfo) this.klass, this);
    }

    private SootClass makeClassRef(String className) {
        if (ModuleUtil.module_mode()) {
            return SootModuleResolver.v().makeClassRef(className, Optional.fromNullable(this.klass.moduleName));
        }
        return SootResolver.v().makeClassRef(className);
    }

    private RefType makeRefType(String className) {
        if (ModuleUtil.module_mode()) {
            return ModuleRefType.v(className, Optional.fromNullable(this.klass.moduleName));
        }
        return RefType.v(className);
    }
}
