package soot.dexpler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.AnnotationVisibility;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.coffi.Instruction;
import soot.javaToJimple.IInitialResolver;
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
import soot.tagkit.DeprecatedTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.Host;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toDex.SootToDexUtils;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexAnnotation.class */
public class DexAnnotation {
    private static final Logger logger;
    public static final String JAVA_DEPRECATED = "java.lang.Deprecated";
    public static final String DALVIK_ANNOTATION_THROWS = "dalvik.annotation.Throws";
    public static final String DALVIK_ANNOTATION_SIGNATURE = "dalvik.annotation.Signature";
    public static final String DALVIK_ANNOTATION_MEMBERCLASSES = "dalvik.annotation.MemberClasses";
    public static final String DALVIK_ANNOTATION_INNERCLASS = "dalvik.annotation.InnerClass";
    public static final String DALVIK_ANNOTATION_ENCLOSINGMETHOD = "dalvik.annotation.EnclosingMethod";
    public static final String DALVIK_ANNOTATION_ENCLOSINGCLASS = "dalvik.annotation.EnclosingClass";
    public static final String DALVIK_ANNOTATION_DEFAULT = "dalvik.annotation.AnnotationDefault";
    private final Type ARRAY_TYPE = RefType.v("Array");
    private final SootClass clazz;
    private final IInitialResolver.Dependencies deps;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexAnnotation.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(DexAnnotation.class);
    }

    public DexAnnotation(SootClass clazz, IInitialResolver.Dependencies deps) {
        this.clazz = clazz;
        this.deps = deps;
    }

    public void handleClassAnnotation(ClassDef classDef) {
        List<Tag> tags;
        Set<? extends Annotation> aSet = classDef.getAnnotations();
        if (aSet == null || aSet.isEmpty() || (tags = handleAnnotation(aSet, classDef.getType())) == null) {
            return;
        }
        InnerClassAttribute ica = null;
        for (Tag t : tags) {
            if (t != null) {
                if (t instanceof InnerClassTag) {
                    if (ica == null) {
                        ica = (InnerClassAttribute) this.clazz.getTag(InnerClassAttribute.NAME);
                        if (ica == null) {
                            ica = new InnerClassAttribute();
                            this.clazz.addTag(ica);
                        }
                    }
                    ica.add((InnerClassTag) t);
                } else if (t instanceof VisibilityAnnotationTag) {
                    VisibilityAnnotationTag vt = (VisibilityAnnotationTag) t;
                    Iterator<AnnotationTag> it = vt.getAnnotations().iterator();
                    while (it.hasNext()) {
                        AnnotationTag a = it.next();
                        if (a.getType().equals("Ldalvik/annotation/AnnotationDefault;")) {
                            for (AnnotationElem ae : a.getElems()) {
                                if (ae instanceof AnnotationAnnotationElem) {
                                    AnnotationAnnotationElem aae = (AnnotationAnnotationElem) ae;
                                    AnnotationTag at = aae.getValue();
                                    Map<String, AnnotationElem> defaults = new HashMap<>();
                                    for (AnnotationElem aelem : at.getElems()) {
                                        defaults.put(aelem.getName(), aelem);
                                    }
                                    for (SootMethod sm : this.clazz.getMethods()) {
                                        String methodName = sm.getName();
                                        if (defaults.containsKey(methodName)) {
                                            AnnotationElem e = defaults.get(methodName);
                                            Type annotationType = getSootType(e);
                                            boolean isCorrectType = false;
                                            if (annotationType == null) {
                                                isCorrectType = true;
                                            } else if (annotationType.equals(sm.getReturnType())) {
                                                isCorrectType = true;
                                            } else if (annotationType.equals(this.ARRAY_TYPE) && (sm.getReturnType() instanceof ArrayType)) {
                                                isCorrectType = true;
                                            }
                                            if (isCorrectType && sm.getParameterCount() == 0) {
                                                e.setName("default");
                                                AnnotationDefaultTag d = new AnnotationDefaultTag(e);
                                                sm.addTag(d);
                                                defaults.remove(sm.getName());
                                            }
                                        }
                                    }
                                    for (Map.Entry<String, AnnotationElem> leftOverEntry : defaults.entrySet()) {
                                        SootMethod found = this.clazz.getMethodByNameUnsafe(leftOverEntry.getKey());
                                        AnnotationElem element = leftOverEntry.getValue();
                                        if (found != null) {
                                            element.setName("default");
                                            AnnotationDefaultTag d2 = new AnnotationDefaultTag(element);
                                            found.addTag(d2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (vt.getVisibility() != 1) {
                        this.clazz.addTag(vt);
                    }
                } else {
                    this.clazz.addTag(t);
                }
            }
        }
    }

    private Type getSootType(AnnotationElem e) {
        Type annotationType;
        switch (e.getKind()) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'L':
            case 'S':
            case 'V':
            case 'Z':
                annotationType = Util.getType(String.valueOf(e.getKind()));
                break;
            case '[':
                annotationType = this.ARRAY_TYPE;
                AnnotationArrayElem array = (AnnotationArrayElem) e;
                if (array.getNumValues() > 0) {
                    AnnotationElem firstElement = array.getValueAt(0);
                    Type type = getSootType(firstElement);
                    if (type == null) {
                        return null;
                    }
                    if (type.equals(this.ARRAY_TYPE)) {
                        return this.ARRAY_TYPE;
                    }
                    return ArrayType.v(type, 1);
                }
                break;
            case 'c':
                annotationType = RefType.v("java.lang.Class");
                break;
            case 'e':
                AnnotationEnumElem enumElem = (AnnotationEnumElem) e;
                annotationType = Util.getType(enumElem.getTypeName());
                break;
            case 's':
                annotationType = RefType.v("java.lang.String");
                break;
            default:
                annotationType = null;
                break;
        }
        return annotationType;
    }

    public void handleFieldAnnotation(Host h, Field f) {
        List<Tag> tags;
        Set<? extends Annotation> aSet = f.getAnnotations();
        if (aSet != null && !aSet.isEmpty() && (tags = handleAnnotation(aSet, null)) != null) {
            for (Tag t : tags) {
                if (t != null) {
                    h.addTag(t);
                }
            }
        }
    }

    public void handleMethodAnnotation(Host h, Method method) {
        AnnotationTag annotationTag;
        List<Tag> tags;
        Set<? extends Annotation> aSet = method.getAnnotations();
        if (aSet != null && !aSet.isEmpty() && (tags = handleAnnotation(aSet, null)) != null) {
            for (Tag t : tags) {
                if (t != null) {
                    h.addTag(t);
                }
            }
        }
        String[] parameterNames = null;
        int i = 0;
        for (MethodParameter p : method.getParameters()) {
            String name = p.getName();
            if (name != null) {
                if (parameterNames == null) {
                    parameterNames = new String[method.getParameters().size()];
                }
                parameterNames[i] = name;
            }
            i++;
        }
        if (parameterNames != null) {
            h.addTag(new ParamNamesTag(parameterNames));
        }
        boolean doParam = false;
        List<? extends MethodParameter> parameters = method.getParameters();
        Iterator<? extends MethodParameter> it = parameters.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MethodParameter p2 = it.next();
            if (p2.getAnnotations().size() > 0) {
                doParam = true;
                break;
            }
        }
        if (doParam) {
            VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(parameters.size(), 0);
            for (MethodParameter p3 : parameters) {
                List<Tag> tags2 = handleAnnotation(p3.getAnnotations(), null);
                if (tags2 == null) {
                    tag.addVisibilityAnnotation(null);
                } else {
                    VisibilityAnnotationTag paramVat = new VisibilityAnnotationTag(0);
                    tag.addVisibilityAnnotation(paramVat);
                    for (Tag t2 : tags2) {
                        if (t2 != null) {
                            if (!(t2 instanceof VisibilityAnnotationTag)) {
                                if (t2 instanceof DeprecatedTag) {
                                    annotationTag = new AnnotationTag("Ljava/lang/Deprecated;");
                                } else if (t2 instanceof SignatureTag) {
                                    SignatureTag sig = (SignatureTag) t2;
                                    ArrayList<AnnotationElem> sigElements = new ArrayList<>();
                                    for (String s : SootToDexUtils.splitSignature(sig.getSignature())) {
                                        sigElements.add(new AnnotationStringElem(s, 's', "value"));
                                    }
                                    AnnotationElem elem = new AnnotationArrayElem(sigElements, '[', "value");
                                    annotationTag = new AnnotationTag("Ldalvik/annotation/Signature;", Collections.singleton(elem));
                                } else {
                                    throw new RuntimeException("error: unhandled tag for parameter annotation in method " + h + " (" + t2 + ").");
                                }
                            } else {
                                annotationTag = ((VisibilityAnnotationTag) t2).getAnnotations().get(0);
                            }
                            AnnotationTag vat = annotationTag;
                            paramVat.addAnnotation(vat);
                        }
                    }
                    continue;
                }
            }
            if (tag.getVisibilityAnnotations().size() > 0) {
                h.addTag(tag);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dexpler/DexAnnotation$MyAnnotations.class */
    class MyAnnotations {
        List<AnnotationTag> annotationList = new ArrayList();
        List<Integer> visibilityList = new ArrayList();

        MyAnnotations() {
        }

        public void add(AnnotationTag a, int visibility) {
            this.annotationList.add(a);
            this.visibilityList.add(new Integer(visibility));
        }

        public List<AnnotationTag> getAnnotations() {
            return this.annotationList;
        }

        public List<Integer> getVisibilityList() {
            return this.visibilityList;
        }
    }

    private List<Tag> handleAnnotation(Set<? extends Annotation> annotations, String classType) {
        if (annotations == null || annotations.size() == 0) {
            return null;
        }
        List<Tag> tags = new ArrayList<>();
        VisibilityAnnotationTag[] vatg = new VisibilityAnnotationTag[3];
        for (Annotation a : annotations) {
            addAnnotation(classType, tags, vatg, a);
        }
        for (VisibilityAnnotationTag vat : vatg) {
            if (vat != null) {
                tags.add(vat);
            }
        }
        return tags;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected void addAnnotation(String classType, List<Tag> tags, VisibilityAnnotationTag[] vatg, Annotation a) {
        String outerClass;
        String outerClass2;
        String name;
        int v = getVisibility(a.getVisibility());
        Tag t = null;
        Type atype = DexType.toSoot(a.getType());
        String atypes = atype.toString();
        int eSize = a.getElements().size();
        switch (atypes.hashCode()) {
            case -2023695885:
                if (atypes.equals(DALVIK_ANNOTATION_ENCLOSINGMETHOD)) {
                    if (eSize == 0) {
                        return;
                    }
                    if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation EnclosingMethod. Got " + eSize + " instead.");
                    }
                    AnnotationStringElem e = (AnnotationStringElem) getElements(a.getElements()).get(0);
                    String[] split1 = e.getValue().split("\\ \\|");
                    if (split1.length < 4) {
                        logger.debug("Invalid or unsupported dalvik EnclosingMethod annotation value: \"{}\"", e.getValue());
                        break;
                    } else {
                        String classString = split1[0];
                        String methodString = split1[1];
                        String parameters = split1[2];
                        String returnType = split1[3];
                        String methodSigString = "(" + parameters + ")" + returnType;
                        t = new EnclosingMethodTag(classString, methodString, methodSigString);
                        String outerClass3 = classString.replace("/", ".");
                        this.deps.typesToSignature.add(RefType.v(outerClass3));
                        this.clazz.setOuterClass(SootResolver.v().makeClassRef(outerClass3));
                        if (!$assertionsDisabled && this.clazz.getOuterClass() == this.clazz) {
                            throw new AssertionError();
                        }
                    }
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case -1757144142:
                if (atypes.equals(DALVIK_ANNOTATION_MEMBERCLASSES)) {
                    AnnotationArrayElem arre = (AnnotationArrayElem) getElements(a.getElements()).get(0);
                    Iterator<AnnotationElem> it = arre.getValues().iterator();
                    while (it.hasNext()) {
                        AnnotationElem ae = it.next();
                        AnnotationClassElem c = (AnnotationClassElem) ae;
                        String innerClass = c.getDesc();
                        if (innerClass.contains("$-")) {
                            int i = innerClass.indexOf("$-");
                            outerClass2 = innerClass.substring(0, i);
                            name = innerClass.substring(i + 2).replaceAll(";$", "");
                        } else if (innerClass.contains("$")) {
                            int i2 = innerClass.lastIndexOf("$");
                            outerClass2 = innerClass.substring(0, i2);
                            name = innerClass.substring(i2 + 1).replaceAll(";$", "");
                        } else {
                            outerClass2 = null;
                            name = null;
                        }
                        if (name != null && name.matches("^\\d*$")) {
                            name = null;
                        }
                        Tag innerTag = new InnerClassTag(DexType.toSootICAT(innerClass), outerClass2 == null ? null : DexType.toSootICAT(outerClass2), name, 0);
                        tags.add(innerTag);
                    }
                    return;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case -551984322:
                if (atypes.equals(DALVIK_ANNOTATION_SIGNATURE)) {
                    if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation Signature. Got " + eSize + " instead. Class " + classType);
                    }
                    AnnotationArrayElem arre2 = (AnnotationArrayElem) getElements(a.getElements()).get(0);
                    String sig = "";
                    Iterator<AnnotationElem> it2 = arre2.getValues().iterator();
                    while (it2.hasNext()) {
                        AnnotationElem ae2 = it2.next();
                        AnnotationStringElem s = (AnnotationStringElem) ae2;
                        sig = String.valueOf(sig) + s.getValue();
                    }
                    t = new SignatureTag(sig);
                    break;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case 421456263:
                if (atypes.equals(DALVIK_ANNOTATION_THROWS)) {
                    return;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case 618411558:
                if (atypes.equals(DALVIK_ANNOTATION_ENCLOSINGCLASS)) {
                    if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation EnclosingClass. Got " + eSize + " instead.");
                    }
                    for (AnnotationElement elem : a.getElements()) {
                        String outerClass4 = ((TypeEncodedValue) elem.getValue()).getValue();
                        String outerClass5 = Util.dottedClassName(outerClass4);
                        if (outerClass5.equals(this.clazz.getName())) {
                            if (outerClass5.contains("$-")) {
                                outerClass5 = outerClass5.substring(0, outerClass5.indexOf("$-"));
                            } else if (outerClass5.contains("$")) {
                                outerClass5 = outerClass5.substring(0, outerClass5.lastIndexOf("$"));
                            }
                        }
                        this.deps.typesToSignature.add(RefType.v(outerClass5));
                        this.clazz.setOuterClass(SootResolver.v().makeClassRef(outerClass5));
                        if (!$assertionsDisabled && this.clazz.getOuterClass() == this.clazz) {
                            throw new AssertionError();
                        }
                    }
                    return;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case 1260995736:
                if (atypes.equals(DALVIK_ANNOTATION_DEFAULT)) {
                    if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation Default. Got " + eSize + " instead.");
                    }
                    AnnotationElem anne = getElements(a.getElements()).get(0);
                    AnnotationTag adt = new AnnotationTag(a.getType());
                    adt.addElem(anne);
                    if (vatg[v] == null) {
                        vatg[v] = new VisibilityAnnotationTag(v);
                    }
                    vatg[v].addAnnotation(adt);
                    break;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case 1935798791:
                if (atypes.equals(JAVA_DEPRECATED)) {
                    if (eSize > 2) {
                        throw new RuntimeException("error: expected up to 2 attributes for annotation Deprecated. Got " + eSize + " instead. Class " + classType);
                    }
                    Boolean forRemoval = null;
                    String since = null;
                    Iterator<AnnotationElem> it3 = getElements(a.getElements()).iterator();
                    while (it3.hasNext()) {
                        AnnotationElem elem2 = it3.next();
                        if ((elem2 instanceof AnnotationBooleanElem) && "forRemoval".equals(elem2.getName())) {
                            forRemoval = Boolean.valueOf(((AnnotationBooleanElem) elem2).getValue());
                        } else if ((elem2 instanceof AnnotationStringElem) && "since".equals(elem2.getName())) {
                            since = ((AnnotationStringElem) elem2).getValue();
                        } else {
                            throw new RuntimeException("Unsupported attribute in Deprecated annotation found in class " + classType + Instruction.argsep + elem2);
                        }
                    }
                    t = new DeprecatedTag(forRemoval, since);
                    AnnotationTag deprecated = new AnnotationTag("Ljava/lang/Deprecated;");
                    if (vatg[v] == null) {
                        vatg[v] = new VisibilityAnnotationTag(v);
                    }
                    vatg[v].addAnnotation(deprecated);
                    break;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            case 1944483580:
                if (atypes.equals(DALVIK_ANNOTATION_INNERCLASS)) {
                    int accessFlags = -1;
                    String name2 = null;
                    Iterator<AnnotationElem> it4 = getElements(a.getElements()).iterator();
                    while (it4.hasNext()) {
                        AnnotationElem ele = it4.next();
                        if ((ele instanceof AnnotationIntElem) && ele.getName().equals("accessFlags")) {
                            accessFlags = ((AnnotationIntElem) ele).getValue();
                        } else if ((ele instanceof AnnotationStringElem) && ele.getName().equals("name")) {
                            name2 = ((AnnotationStringElem) ele).getValue();
                        } else {
                            throw new RuntimeException("Unexpected inner class annotation element");
                        }
                    }
                    if (this.clazz.hasOuterClass()) {
                        outerClass = this.clazz.getOuterClass().getName();
                    } else if (classType.contains("$-")) {
                        outerClass = classType.substring(0, classType.indexOf("$-"));
                        if (Util.isByteCodeClassName(classType)) {
                            outerClass = String.valueOf(outerClass) + ";";
                        }
                    } else if (classType.contains("$")) {
                        outerClass = String.valueOf(classType.substring(0, classType.lastIndexOf("$"))) + ";";
                        if (Util.isByteCodeClassName(classType)) {
                            outerClass = String.valueOf(outerClass) + ";";
                        }
                    } else {
                        outerClass = null;
                    }
                    Tag innerTag2 = new InnerClassTag(DexType.toSootICAT(classType), outerClass == null ? null : DexType.toSootICAT(outerClass), name2, accessFlags);
                    tags.add(innerTag2);
                    if (outerClass != null && !this.clazz.hasOuterClass()) {
                        String sootOuterClass = Util.dottedClassName(outerClass);
                        this.deps.typesToSignature.add(RefType.v(sootOuterClass));
                        this.clazz.setOuterClass(SootResolver.v().makeClassRef(sootOuterClass));
                        if (!$assertionsDisabled && this.clazz.getOuterClass() == this.clazz) {
                            throw new AssertionError();
                        }
                        return;
                    }
                    return;
                }
                addNormalAnnotation(vatg, a, v);
                break;
            default:
                addNormalAnnotation(vatg, a, v);
                break;
        }
        tags.add(t);
    }

    protected void addNormalAnnotation(VisibilityAnnotationTag[] vatg, Annotation a, int v) {
        if (vatg[v] == null) {
            vatg[v] = new VisibilityAnnotationTag(v);
        }
        AnnotationTag tag = new AnnotationTag(a.getType());
        Iterator<AnnotationElem> it = getElements(a.getElements()).iterator();
        while (it.hasNext()) {
            AnnotationElem e = it.next();
            tag.addElem(e);
        }
        vatg[v].addAnnotation(tag);
    }

    private ArrayList<AnnotationElem> getElements(Set<? extends AnnotationElement> set) {
        ArrayList<AnnotationElem> aelemList = new ArrayList<>();
        for (AnnotationElement ae : set) {
            logger.trace("element: {}={} type: {}", ae.getName(), ae.getValue(), ae.getClass());
            List<AnnotationElem> eList = handleAnnotationElement(ae, Collections.singletonList(ae.getValue()));
            if (eList != null) {
                aelemList.addAll(eList);
            }
        }
        return aelemList;
    }

    private ArrayList<AnnotationElem> handleAnnotationElement(AnnotationElement ae, List<? extends EncodedValue> evList) {
        ArrayList<AnnotationElem> aelemList = new ArrayList<>();
        for (EncodedValue ev : evList) {
            int type = ev.getValueType();
            AnnotationElem elem = null;
            switch (type) {
                case 0:
                    elem = new AnnotationIntElem(((ByteEncodedValue) ev).getValue(), 'B', ae.getName());
                    break;
                case 1:
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                default:
                    throw new RuntimeException("Unknown annotation element 0x" + Integer.toHexString(type));
                case 2:
                    elem = new AnnotationIntElem(((ShortEncodedValue) ev).getValue(), 'S', ae.getName());
                    break;
                case 3:
                    elem = new AnnotationIntElem(((CharEncodedValue) ev).getValue(), 'C', ae.getName());
                    break;
                case 4:
                    elem = new AnnotationIntElem(((IntEncodedValue) ev).getValue(), 'I', ae.getName());
                    break;
                case 6:
                    elem = new AnnotationLongElem(((LongEncodedValue) ev).getValue(), 'J', ae.getName());
                    break;
                case 16:
                    elem = new AnnotationFloatElem(((FloatEncodedValue) ev).getValue(), 'F', ae.getName());
                    break;
                case 17:
                    elem = new AnnotationDoubleElem(((DoubleEncodedValue) ev).getValue(), 'D', ae.getName());
                    break;
                case 23:
                    elem = new AnnotationStringElem(((StringEncodedValue) ev).getValue(), 's', ae.getName());
                    break;
                case 24:
                    elem = new AnnotationClassElem(((TypeEncodedValue) ev).getValue(), 'c', ae.getName());
                    break;
                case 25:
                    FieldReference fr = ((FieldEncodedValue) ev).getValue();
                    String fieldSig = String.valueOf("") + DexType.toSootAT(fr.getDefiningClass()) + ": ";
                    elem = new AnnotationStringElem(String.valueOf(String.valueOf(fieldSig) + DexType.toSootAT(fr.getType()) + Instruction.argsep) + fr.getName(), 'f', ae.getName());
                    break;
                case 26:
                    MethodReference mr = ((MethodEncodedValue) ev).getValue();
                    String className = DexType.toSootICAT(mr.getDefiningClass());
                    String returnType = DexType.toSootAT(mr.getReturnType());
                    String methodName = mr.getName();
                    String parameters = "";
                    for (CharSequence p : mr.getParameterTypes()) {
                        parameters = String.valueOf(parameters) + DexType.toSootAT(p.toString());
                    }
                    String mSig = String.valueOf(className) + " |" + methodName + " |" + parameters + " |" + returnType;
                    elem = new AnnotationStringElem(mSig, 'M', ae.getName());
                    break;
                case 27:
                    FieldReference fr2 = ((EnumEncodedValue) ev).getValue();
                    elem = new AnnotationEnumElem(DexType.toSootAT(fr2.getType()).toString(), fr2.getName(), 'e', ae.getName());
                    break;
                case 28:
                    ArrayList<AnnotationElem> l = handleAnnotationElement(ae, ((ArrayEncodedValue) ev).getValue());
                    if (l != null) {
                        elem = new AnnotationArrayElem(l, '[', ae.getName());
                        break;
                    }
                    break;
                case 29:
                    AnnotationEncodedValue v = (AnnotationEncodedValue) ev;
                    AnnotationTag t = new AnnotationTag(DexType.toSootAT(v.getType()).toString());
                    for (AnnotationElement newElem : v.getElements()) {
                        List<EncodedValue> l2 = new ArrayList<>();
                        l2.add(newElem.getValue());
                        List<AnnotationElem> aList = handleAnnotationElement(newElem, l2);
                        if (aList != null) {
                            for (AnnotationElem e : aList) {
                                t.addElem(e);
                            }
                        }
                    }
                    elem = new AnnotationAnnotationElem(t, '@', ae.getName());
                    break;
                case 30:
                    elem = new AnnotationStringElem(null, 'N', ae.getName());
                    break;
                case 31:
                    elem = new AnnotationBooleanElem(((BooleanEncodedValue) ev).getValue(), 'Z', ae.getName());
                    break;
            }
            if (elem != null) {
                aelemList.add(elem);
            }
        }
        return aelemList;
    }

    protected static int getVisibility(int visibility) {
        String visibility2 = AnnotationVisibility.getVisibility(visibility);
        switch (visibility2.hashCode()) {
            case -887328209:
                if (visibility2.equals("system")) {
                    return 1;
                }
                break;
            case 94094958:
                if (visibility2.equals("build")) {
                    return 2;
                }
                break;
            case 1550962648:
                if (visibility2.equals("runtime")) {
                    return 0;
                }
                break;
        }
        throw new RuntimeException(String.format("error: unknown annotation visibility: '%d'", Integer.valueOf(visibility)));
    }
}
