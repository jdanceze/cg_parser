package soot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;
import soot.asm.AsmUtil;
import soot.baf.BafBody;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Attribute;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.Host;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.OuterClassTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.util.backend.ASMBackendUtils;
import soot.util.backend.SootASMClassWriter;
/* loaded from: gencallgraphv3.jar:soot/AbstractASMBackend.class */
public abstract class AbstractASMBackend {
    private final Map<SootMethod, BafBody> bafBodyCache = new HashMap();
    protected final SootClass sc;
    protected final int javaVersion;
    protected ClassVisitor cv;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void generateMethodBody(MethodVisitor methodVisitor, SootMethod sootMethod);

    static {
        $assertionsDisabled = !AbstractASMBackend.class.desiredAssertionStatus();
    }

    public AbstractASMBackend(SootClass sc, int javaVersion) {
        this.sc = sc;
        javaVersion = javaVersion == 0 ? 1 : javaVersion;
        int minVersion = getMinJavaVersion(sc);
        if (javaVersion != 1 && javaVersion < minVersion) {
            throw new IllegalArgumentException("Enforced Java version " + ASMBackendUtils.translateJavaVersion(javaVersion) + " too low to support required features (" + ASMBackendUtils.translateJavaVersion(minVersion) + " required)");
        }
        this.javaVersion = AsmUtil.javaToBytecodeVersion(Math.max(javaVersion, minVersion));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BafBody getBafBody(SootMethod method) {
        Body activeBody = method.getActiveBody();
        if (activeBody instanceof BafBody) {
            return (BafBody) activeBody;
        }
        BafBody body = this.bafBodyCache.get(method);
        if (body != null) {
            return body;
        }
        if (activeBody instanceof JimpleBody) {
            BafBody body2 = PackManager.v().convertJimpleBodyToBaf(method);
            this.bafBodyCache.put(method, body2);
            return body2;
        }
        throw new RuntimeException("ASM-backend can only translate Baf and Jimple bodies! Found " + (activeBody == null ? Jimple.NULL : activeBody.getClass().getName()) + '.');
    }

    private int getMinJavaVersion(SootClass sc) {
        int minVersion = 2;
        minVersion = (Modifier.isAnnotation(sc.getModifiers()) || sc.hasTag(VisibilityAnnotationTag.NAME)) ? 6 : 6;
        if (containsGenericSignatureTag(sc)) {
            minVersion = 6;
        }
        for (SootField sf : sc.getFields()) {
            if (minVersion >= 6) {
                break;
            }
            if (sf.hasTag(VisibilityAnnotationTag.NAME)) {
                minVersion = 6;
            }
            if (containsGenericSignatureTag(sf)) {
                minVersion = 6;
            }
        }
        Iterator it = new ArrayList(sc.getMethods()).iterator();
        while (it.hasNext()) {
            SootMethod sm = (SootMethod) it.next();
            if (minVersion >= 9) {
                break;
            }
            if (sm.hasTag(VisibilityAnnotationTag.NAME) || sm.hasTag(VisibilityParameterAnnotationTag.NAME)) {
                minVersion = Math.max(minVersion, 6);
            }
            if (containsGenericSignatureTag(sm)) {
                minVersion = Math.max(minVersion, 6);
            }
            if (sm.hasActiveBody()) {
                minVersion = Math.max(minVersion, getMinJavaVersion(sm));
            }
        }
        return minVersion;
    }

    private static boolean containsGenericSignatureTag(Host h) {
        SignatureTag t = (SignatureTag) h.getTag(SignatureTag.NAME);
        return t != null && t.getSignature().indexOf(60) >= 0;
    }

    protected int getMinJavaVersion(SootMethod sm) {
        return 8;
    }

    public void generateClassFile(OutputStream os) {
        ClassWriter cw = new SootASMClassWriter(2);
        this.cv = cw;
        generateByteCode();
        try {
            os.write(cw.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Could not write class file in the ASM-backend!", e);
        }
    }

    public void generateTextualRepresentation(PrintWriter pw) {
        this.cv = new TraceClassVisitor(pw);
        generateByteCode();
    }

    protected void generateByteCode() {
        SourceFileTag t;
        generateClassHeader();
        if (!Options.v().no_output_source_file_attribute() && (t = (SourceFileTag) this.sc.getTag(SourceFileTag.NAME)) != null) {
            this.cv.visitSource(t.getSourceFile(), null);
        }
        if (this.sc.hasOuterClass() || this.sc.hasTag(EnclosingMethodTag.NAME) || this.sc.hasTag(OuterClassTag.NAME)) {
            generateOuterClassReference();
        }
        generateAnnotations(this.cv, this.sc);
        generateAttributes();
        generateInnerClassReferences();
        generateFields();
        generateMethods();
        this.cv.visitEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/AbstractASMBackend$SootMethodComparator.class */
    public static class SootMethodComparator implements Comparator<SootMethod> {
        private SootMethodComparator() {
        }

        /* synthetic */ SootMethodComparator(SootMethodComparator sootMethodComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(SootMethod o1, SootMethod o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    protected void generateMethods() {
        String[] exceptions;
        List<SootMethod> sortedMethods = new ArrayList<>(this.sc.getMethods());
        Collections.sort(sortedMethods, new SootMethodComparator(null));
        for (SootMethod sm : sortedMethods) {
            if (!sm.isPhantom()) {
                int access = getModifiers(sm.getModifiers(), sm);
                String name = sm.getName();
                StringBuilder descBuilder = new StringBuilder(5);
                descBuilder.append('(');
                for (Type t : sm.getParameterTypes()) {
                    descBuilder.append(ASMBackendUtils.toTypeDesc(t));
                }
                descBuilder.append(')');
                descBuilder.append(ASMBackendUtils.toTypeDesc(sm.getReturnType()));
                SignatureTag sigTag = (SignatureTag) sm.getTag(SignatureTag.NAME);
                String sig = sigTag == null ? null : sigTag.getSignature();
                List<SootClass> exceptionList = sm.getExceptionsUnsafe();
                if (exceptionList != null) {
                    exceptions = new String[exceptionList.size()];
                    int i = 0;
                    for (SootClass exc : exceptionList) {
                        exceptions[i] = ASMBackendUtils.slashify(exc.getName());
                        i++;
                    }
                } else {
                    exceptions = new String[0];
                }
                MethodVisitor mv = this.cv.visitMethod(access, name, descBuilder.toString(), sig, exceptions);
                if (mv != null) {
                    for (Tag t2 : sm.getTags()) {
                        if (t2 instanceof VisibilityParameterAnnotationTag) {
                            VisibilityParameterAnnotationTag vpt = (VisibilityParameterAnnotationTag) t2;
                            ArrayList<VisibilityAnnotationTag> tags = vpt.getVisibilityAnnotations();
                            if (tags != null) {
                                for (int j = 0; j < tags.size(); j++) {
                                    VisibilityAnnotationTag va = tags.get(j);
                                    if (va != null) {
                                        Iterator<AnnotationTag> it = va.getAnnotations().iterator();
                                        while (it.hasNext()) {
                                            AnnotationTag at = it.next();
                                            AnnotationVisitor av = mv.visitParameterAnnotation(j, at.getType(), va.getVisibility() == 0);
                                            generateAnnotationElems(av, at.getElems(), true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    generateAnnotations(mv, sm);
                    generateAttributes(mv, sm);
                    if (sm.hasActiveBody()) {
                        mv.visitCode();
                        generateMethodBody(mv, sm);
                        mv.visitMaxs(0, 0);
                    }
                    mv.visitEnd();
                }
            }
        }
    }

    protected void generateFields() {
        for (SootField f : this.sc.getFields()) {
            if (!f.isPhantom()) {
                String name = f.getName();
                String desc = ASMBackendUtils.toTypeDesc(f.getType());
                SignatureTag sigTag = (SignatureTag) f.getTag(SignatureTag.NAME);
                String sig = sigTag == null ? null : sigTag.getSignature();
                Object value = ASMBackendUtils.getDefaultValue(f);
                int access = getModifiers(f.getModifiers(), f);
                FieldVisitor fv = this.cv.visitField(access, name, desc, sig, value);
                if (fv != null) {
                    generateAnnotations(fv, f);
                    generateAttributes(fv, f);
                    fv.visitEnd();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/AbstractASMBackend$SootInnerClassComparator.class */
    public class SootInnerClassComparator implements Comparator<InnerClassTag> {
        private SootInnerClassComparator() {
        }

        /* synthetic */ SootInnerClassComparator(AbstractASMBackend abstractASMBackend, SootInnerClassComparator sootInnerClassComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(InnerClassTag o1, InnerClassTag o2) {
            if (o1.getInnerClass() == null) {
                return 0;
            }
            return o1.getInnerClass().compareTo(o2.getInnerClass());
        }
    }

    protected void generateInnerClassReferences() {
        if (!Options.v().no_output_inner_classes_attribute()) {
            InnerClassAttribute ica = (InnerClassAttribute) this.sc.getTag(InnerClassAttribute.NAME);
            if (ica != null) {
                List<InnerClassTag> sortedTags = new ArrayList<>(ica.getSpecs());
                Collections.sort(sortedTags, new SootInnerClassComparator(this, null));
                writeInnerClassTags(sortedTags);
                return;
            }
            writeInnerClassTags((List) this.sc.getTags().stream().filter(t -> {
                return t instanceof InnerClassTag;
            }).map(t2 -> {
                return (InnerClassTag) t2;
            }).sorted(new SootInnerClassComparator(this, null)).collect(Collectors.toList()));
        }
    }

    protected void writeInnerClassTags(List<InnerClassTag> sortedTags) {
        for (InnerClassTag ict : sortedTags) {
            String name = ASMBackendUtils.slashify(ict.getInnerClass());
            String outerClassName = ASMBackendUtils.slashify(ict.getOuterClass());
            String innerName = ASMBackendUtils.slashify(ict.getShortName());
            int access = ict.getAccessFlags();
            this.cv.visitInnerClass(name, outerClassName, innerName, access);
        }
    }

    protected void generateAttributes() {
        for (Tag t : this.sc.getTags()) {
            if (t instanceof Attribute) {
                this.cv.visitAttribute(ASMBackendUtils.createASMAttribute((Attribute) t));
            }
        }
    }

    protected void generateAttributes(FieldVisitor fv, SootField f) {
        for (Tag t : f.getTags()) {
            if (t instanceof Attribute) {
                fv.visitAttribute(ASMBackendUtils.createASMAttribute((Attribute) t));
            }
        }
    }

    protected void generateAttributes(MethodVisitor mv, SootMethod m) {
        for (Tag t : m.getTags()) {
            if (t instanceof Attribute) {
                mv.visitAttribute(ASMBackendUtils.createASMAttribute((Attribute) t));
            }
        }
    }

    protected void generateAnnotations(Object visitor, Host host) {
        for (Tag t : host.getTags()) {
            if (t instanceof VisibilityAnnotationTag) {
                VisibilityAnnotationTag vat = (VisibilityAnnotationTag) t;
                boolean runTimeVisible = vat.getVisibility() == 0;
                Iterator<AnnotationTag> it = vat.getAnnotations().iterator();
                while (it.hasNext()) {
                    AnnotationTag at = it.next();
                    AnnotationVisitor av = null;
                    if (visitor instanceof ClassVisitor) {
                        av = ((ClassVisitor) visitor).visitAnnotation(at.getType(), runTimeVisible);
                    } else if (visitor instanceof FieldVisitor) {
                        av = ((FieldVisitor) visitor).visitAnnotation(at.getType(), runTimeVisible);
                    } else if (visitor instanceof MethodVisitor) {
                        av = ((MethodVisitor) visitor).visitAnnotation(at.getType(), runTimeVisible);
                    }
                    generateAnnotationElems(av, at.getElems(), true);
                }
            } else if ((t instanceof AnnotationDefaultTag) && (host instanceof SootMethod)) {
                AnnotationDefaultTag adt = (AnnotationDefaultTag) t;
                AnnotationVisitor av2 = ((MethodVisitor) visitor).visitAnnotationDefault();
                generateAnnotationElems(av2, Collections.singleton(adt.getDefaultVal()), true);
            }
        }
    }

    protected void generateAnnotationElems(AnnotationVisitor av, Collection<AnnotationElem> elements, boolean addName) {
        if (av != null) {
            for (AnnotationElem elem : elements) {
                if (!$assertionsDisabled && elem == null) {
                    throw new AssertionError();
                }
                if (elem instanceof AnnotationEnumElem) {
                    AnnotationEnumElem enumElem = (AnnotationEnumElem) elem;
                    av.visitEnum(enumElem.getName(), enumElem.getTypeName(), enumElem.getConstantName());
                } else if (elem instanceof AnnotationArrayElem) {
                    AnnotationArrayElem arrayElem = (AnnotationArrayElem) elem;
                    AnnotationVisitor arrayVisitor = av.visitArray(arrayElem.getName());
                    generateAnnotationElems(arrayVisitor, arrayElem.getValues(), false);
                } else if (elem instanceof AnnotationAnnotationElem) {
                    AnnotationAnnotationElem aElem = (AnnotationAnnotationElem) elem;
                    AnnotationVisitor aVisitor = av.visitAnnotation(aElem.getName(), aElem.getValue().getType());
                    generateAnnotationElems(aVisitor, aElem.getValue().getElems(), true);
                } else {
                    Object val = null;
                    if (elem instanceof AnnotationIntElem) {
                        AnnotationIntElem intElem = (AnnotationIntElem) elem;
                        int value = intElem.getValue();
                        switch (intElem.getKind()) {
                            case 'B':
                                val = Byte.valueOf((byte) value);
                                break;
                            case 'C':
                                val = Character.valueOf((char) value);
                                break;
                            case 'I':
                                val = Integer.valueOf(value);
                                break;
                            case 'S':
                                val = Short.valueOf((short) value);
                                break;
                            case 'Z':
                                val = Boolean.valueOf(value == 1);
                                break;
                            default:
                                if (!$assertionsDisabled) {
                                    throw new AssertionError("Unexpected kind: " + intElem.getKind() + " (in " + intElem + ")");
                                }
                                break;
                        }
                    } else if (elem instanceof AnnotationBooleanElem) {
                        AnnotationBooleanElem booleanElem = (AnnotationBooleanElem) elem;
                        val = Boolean.valueOf(booleanElem.getValue());
                    } else if (elem instanceof AnnotationFloatElem) {
                        AnnotationFloatElem floatElem = (AnnotationFloatElem) elem;
                        val = Float.valueOf(floatElem.getValue());
                    } else if (elem instanceof AnnotationLongElem) {
                        AnnotationLongElem longElem = (AnnotationLongElem) elem;
                        val = Long.valueOf(longElem.getValue());
                    } else if (elem instanceof AnnotationDoubleElem) {
                        AnnotationDoubleElem doubleElem = (AnnotationDoubleElem) elem;
                        val = Double.valueOf(doubleElem.getValue());
                    } else if (elem instanceof AnnotationStringElem) {
                        AnnotationStringElem stringElem = (AnnotationStringElem) elem;
                        val = stringElem.getValue();
                    } else if (elem instanceof AnnotationClassElem) {
                        AnnotationClassElem classElem = (AnnotationClassElem) elem;
                        val = org.objectweb.asm.Type.getType(classElem.getDesc());
                    }
                    if (addName) {
                        av.visit(elem.getName(), val);
                    } else {
                        av.visit(null, val);
                    }
                }
            }
            av.visitEnd();
        }
    }

    protected void generateOuterClassReference() {
        OuterClassTag oct;
        String outerClassName = ASMBackendUtils.slashify(this.sc.getOuterClass().getName());
        String enclosingMethod = null;
        String enclosingMethodSig = null;
        EnclosingMethodTag emTag = (EnclosingMethodTag) this.sc.getTag(EnclosingMethodTag.NAME);
        if (emTag != null) {
            if (!this.sc.hasOuterClass()) {
                outerClassName = ASMBackendUtils.slashify(emTag.getEnclosingClass());
            }
            enclosingMethod = emTag.getEnclosingMethod();
            enclosingMethodSig = emTag.getEnclosingMethodSig();
        }
        if (!this.sc.hasOuterClass() && (oct = (OuterClassTag) this.sc.getTag(OuterClassTag.NAME)) != null) {
            outerClassName = ASMBackendUtils.slashify(oct.getName());
        }
        this.cv.visitOuterClass(outerClassName, enclosingMethod, enclosingMethodSig);
    }

    protected void generateClassHeader() {
        int modifier = getModifiers(this.sc.getModifiers(), this.sc);
        String className = ASMBackendUtils.slashify(this.sc.getName());
        SignatureTag sigTag = (SignatureTag) this.sc.getTag(SignatureTag.NAME);
        String sig = sigTag == null ? null : sigTag.getSignature();
        String superClass = TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME.equals(className) ? null : TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME;
        SootClass csuperClass = this.sc.getSuperclassUnsafe();
        if (csuperClass != null) {
            superClass = ASMBackendUtils.slashify(csuperClass.getName());
        }
        String[] interfaces = new String[this.sc.getInterfaceCount()];
        int i = 0;
        for (SootClass interf : this.sc.getInterfaces()) {
            interfaces[i] = ASMBackendUtils.slashify(interf.getName());
            i++;
        }
        this.cv.visit(this.javaVersion, modifier, className, sig, superClass, interfaces);
    }

    protected static int getModifiers(int modVal, Host host) {
        int modifier = 0;
        if (Modifier.isPublic(modVal)) {
            modifier = 0 | 1;
        } else if (Modifier.isPrivate(modVal)) {
            modifier = 0 | 2;
        } else if (Modifier.isProtected(modVal)) {
            modifier = 0 | 4;
        }
        if (Modifier.isStatic(modVal) && ((host instanceof SootField) || (host instanceof SootMethod))) {
            modifier |= 8;
        }
        if (Modifier.isFinal(modVal)) {
            modifier |= 16;
        }
        if (Modifier.isSynchronized(modVal) && (host instanceof SootMethod)) {
            modifier |= 32;
        }
        if (Modifier.isVolatile(modVal) && !(host instanceof SootClass)) {
            modifier |= 64;
        }
        if (Modifier.isTransient(modVal) && !(host instanceof SootClass)) {
            modifier |= 128;
        }
        if (Modifier.isNative(modVal) && (host instanceof SootMethod)) {
            modifier |= 256;
        }
        if (Modifier.isInterface(modVal) && (host instanceof SootClass)) {
            modifier |= 512;
        } else if (host instanceof SootClass) {
            modifier |= 32;
        }
        if (Modifier.isAbstract(modVal) && !(host instanceof SootField)) {
            modifier |= 1024;
        }
        if (Modifier.isStrictFP(modVal) && (host instanceof SootMethod)) {
            modifier |= 2048;
        }
        if (Modifier.isSynthetic(modVal) || host.hasTag(SyntheticTag.NAME)) {
            modifier |= 4096;
        }
        if (Modifier.isAnnotation(modVal) && (host instanceof SootClass)) {
            modifier |= 8192;
        }
        if (Modifier.isEnum(modVal) && !(host instanceof SootMethod)) {
            modifier |= 16384;
        }
        return modifier;
    }
}
