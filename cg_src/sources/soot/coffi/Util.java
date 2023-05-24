package soot.coffi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
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
import soot.tagkit.ConstantValueTag;
import soot.tagkit.DeprecatedTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.GenericAttribute;
import soot.tagkit.Host;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/coffi/Util.class */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private LocalVariableTable_attribute activeVariableTable;
    private Map<String, Map<Integer, Local>> nameToIndexToLocal;
    int nextEasyNameIndex;
    private cp_info[] activeConstantPool = null;
    private boolean useFaithfulNaming = false;
    private final ArrayList<Type> conversionTypes = new ArrayList<>();
    private final Map<String, Type[]> cache = new HashMap();

    public Util(Singletons.Global g) {
    }

    public static Util v() {
        return G.v().soot_coffi_Util();
    }

    public void bodySetup(LocalVariableTable_attribute la, LocalVariableTypeTable_attribute lt, cp_info[] ca) {
        this.activeVariableTable = la;
        this.activeConstantPool = ca;
        this.nameToIndexToLocal = null;
    }

    public void setFaithfulNaming(boolean v) {
        this.useFaithfulNaming = v;
    }

    public boolean isUsingFaithfulNaming() {
        return this.useFaithfulNaming;
    }

    public void resolveFromClassFile(SootClass aClass, InputStream is, String filePath, Collection<Type> references) {
        Type[] types;
        ConstantValueTag tag;
        String className = aClass.getName();
        ClassFile coffiClass = new ClassFile(className);
        boolean success = coffiClass.loadClassFile(is);
        if (!success) {
            if (!Scene.v().allowsPhantomRefs()) {
                throw new RuntimeException("Could not load classfile: " + aClass.getName());
            }
            logger.warn(className + " is a phantom class!");
            aClass.setPhantomClass();
            return;
        }
        CONSTANT_Class_info c = (CONSTANT_Class_info) coffiClass.constant_pool[coffiClass.this_class];
        String name = ((CONSTANT_Utf8_info) coffiClass.constant_pool[c.name_index]).convert().replace('/', '.');
        if (!name.equals(aClass.getName())) {
            throw new RuntimeException("Error: class " + name + " read in from a classfile in which " + aClass.getName() + " was expected.");
        }
        aClass.setModifiers(coffiClass.access_flags & (-33));
        if (coffiClass.super_class != 0) {
            CONSTANT_Class_info c2 = (CONSTANT_Class_info) coffiClass.constant_pool[coffiClass.super_class];
            String superName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[c2.name_index]).convert().replace('/', '.');
            references.add(RefType.v(superName));
            aClass.setSuperclass(SootResolver.v().makeClassRef(superName));
        }
        for (int i = 0; i < coffiClass.interfaces_count; i++) {
            CONSTANT_Class_info c3 = (CONSTANT_Class_info) coffiClass.constant_pool[coffiClass.interfaces[i]];
            String interfaceName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[c3.name_index]).convert().replace('/', '.');
            references.add(RefType.v(interfaceName));
            SootClass interfaceClass = SootResolver.v().makeClassRef(interfaceName);
            interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
            aClass.addInterface(interfaceClass);
        }
        for (int i2 = 0; i2 < coffiClass.fields_count; i2++) {
            field_info fieldInfo = coffiClass.fields[i2];
            String fieldName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[fieldInfo.name_index]).convert();
            String fieldDescriptor = ((CONSTANT_Utf8_info) coffiClass.constant_pool[fieldInfo.descriptor_index]).convert();
            int modifiers = fieldInfo.access_flags;
            Type fieldType = jimpleTypeOfFieldDescriptor(fieldDescriptor);
            SootField field = Scene.v().makeSootField(fieldName, fieldType, modifiers);
            aClass.addField(field);
            references.add(fieldType);
            for (int j = 0; j < fieldInfo.attributes_count; j++) {
                if (fieldInfo.attributes[j] instanceof ConstantValue_attribute) {
                    cp_info cval = coffiClass.constant_pool[((ConstantValue_attribute) fieldInfo.attributes[j]).constantvalue_index];
                    switch (cval.tag) {
                        case 3:
                            tag = new IntegerConstantValueTag((int) ((CONSTANT_Integer_info) cval).bytes);
                            break;
                        case 4:
                            tag = new FloatConstantValueTag(((CONSTANT_Float_info) cval).convert());
                            break;
                        case 5:
                            CONSTANT_Long_info lcval = (CONSTANT_Long_info) cval;
                            tag = new LongConstantValueTag((lcval.high << 32) + lcval.low);
                            break;
                        case 6:
                            CONSTANT_Double_info dcval = (CONSTANT_Double_info) cval;
                            tag = new DoubleConstantValueTag(dcval.convert());
                            break;
                        case 7:
                        default:
                            throw new RuntimeException("unexpected ConstantValue: " + cval);
                        case 8:
                            CONSTANT_String_info scval = (CONSTANT_String_info) cval;
                            CONSTANT_Utf8_info ucval = (CONSTANT_Utf8_info) coffiClass.constant_pool[scval.string_index];
                            tag = new StringConstantValueTag(ucval.convert());
                            break;
                    }
                    field.addTag(tag);
                } else if (fieldInfo.attributes[j] instanceof Synthetic_attribute) {
                    field.addTag(new SyntheticTag());
                } else if (fieldInfo.attributes[j] instanceof Deprecated_attribute) {
                    field.addTag(new DeprecatedTag());
                } else if (fieldInfo.attributes[j] instanceof Signature_attribute) {
                    int signature_index = ((Signature_attribute) fieldInfo.attributes[j]).signature_index;
                    String generic_sig = ((CONSTANT_Utf8_info) coffiClass.constant_pool[signature_index]).convert();
                    field.addTag(new SignatureTag(generic_sig));
                } else if ((fieldInfo.attributes[j] instanceof RuntimeVisibleAnnotations_attribute) || (fieldInfo.attributes[j] instanceof RuntimeInvisibleAnnotations_attribute)) {
                    addAnnotationVisibilityAttribute(field, fieldInfo.attributes[j], coffiClass, references);
                } else if (fieldInfo.attributes[j] instanceof Generic_attribute) {
                    Generic_attribute attr = (Generic_attribute) fieldInfo.attributes[j];
                    field.addTag(new GenericAttribute(((CONSTANT_Utf8_info) coffiClass.constant_pool[attr.attribute_name]).convert(), attr.info));
                }
            }
        }
        for (int i3 = 0; i3 < coffiClass.methods_count; i3++) {
            method_info methodInfo = coffiClass.methods[i3];
            if (coffiClass.constant_pool[methodInfo.name_index] == null) {
                logger.debug("method index: " + methodInfo.toName(coffiClass.constant_pool));
                throw new RuntimeException("method has no name");
            }
            String methodName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[methodInfo.name_index]).convert();
            String methodDescriptor = ((CONSTANT_Utf8_info) coffiClass.constant_pool[methodInfo.descriptor_index]).convert();
            Type[] types2 = jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
            List<Type> parameterTypes = new ArrayList<>();
            for (int j2 = 0; j2 < types2.length - 1; j2++) {
                references.add(types2[j2]);
                parameterTypes.add(types2[j2]);
            }
            Type returnType = types2[types2.length - 1];
            references.add(returnType);
            int modifiers2 = methodInfo.access_flags;
            SootMethod method = Scene.v().makeSootMethod(methodName, parameterTypes, returnType, modifiers2);
            aClass.addMethod(method);
            methodInfo.jmethod = method;
            for (int j3 = 0; j3 < methodInfo.attributes_count; j3++) {
                if (methodInfo.attributes[j3] instanceof Exception_attribute) {
                    Exception_attribute exceptions = (Exception_attribute) methodInfo.attributes[j3];
                    for (int k = 0; k < exceptions.number_of_exceptions; k++) {
                        CONSTANT_Class_info c4 = (CONSTANT_Class_info) coffiClass.constant_pool[exceptions.exception_index_table[k]];
                        String exceptionName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[c4.name_index]).convert().replace('/', '.');
                        references.add(RefType.v(exceptionName));
                        method.addExceptionIfAbsent(SootResolver.v().makeClassRef(exceptionName));
                    }
                } else if (methodInfo.attributes[j3] instanceof Synthetic_attribute) {
                    method.addTag(new SyntheticTag());
                } else if (methodInfo.attributes[j3] instanceof Deprecated_attribute) {
                    method.addTag(new DeprecatedTag());
                } else if (methodInfo.attributes[j3] instanceof Signature_attribute) {
                    int signature_index2 = ((Signature_attribute) methodInfo.attributes[j3]).signature_index;
                    String generic_sig2 = ((CONSTANT_Utf8_info) coffiClass.constant_pool[signature_index2]).convert();
                    method.addTag(new SignatureTag(generic_sig2));
                } else if ((methodInfo.attributes[j3] instanceof RuntimeVisibleAnnotations_attribute) || (methodInfo.attributes[j3] instanceof RuntimeInvisibleAnnotations_attribute)) {
                    addAnnotationVisibilityAttribute(method, methodInfo.attributes[j3], coffiClass, references);
                } else if ((methodInfo.attributes[j3] instanceof RuntimeVisibleParameterAnnotations_attribute) || (methodInfo.attributes[j3] instanceof RuntimeInvisibleParameterAnnotations_attribute)) {
                    addAnnotationVisibilityParameterAttribute(method, methodInfo.attributes[j3], coffiClass, references);
                } else if (methodInfo.attributes[j3] instanceof AnnotationDefault_attribute) {
                    element_value[] input = {((AnnotationDefault_attribute) methodInfo.attributes[j3]).default_value};
                    ArrayList<AnnotationElem> list = createElementTags(1, coffiClass, input);
                    method.addTag(new AnnotationDefaultTag(list.get(0)));
                } else if (methodInfo.attributes[j3] instanceof Generic_attribute) {
                    Generic_attribute attr2 = (Generic_attribute) methodInfo.attributes[j3];
                    method.addTag(new GenericAttribute(((CONSTANT_Utf8_info) coffiClass.constant_pool[attr2.attribute_name]).convert(), attr2.info));
                }
            }
            for (int k2 = 0; k2 < coffiClass.constant_pool_count; k2++) {
                if (coffiClass.constant_pool[k2] instanceof CONSTANT_Class_info) {
                    CONSTANT_Class_info c5 = (CONSTANT_Class_info) coffiClass.constant_pool[k2];
                    String desc = ((CONSTANT_Utf8_info) coffiClass.constant_pool[c5.name_index]).convert();
                    String name2 = desc.replace('/', '.');
                    if (name2.startsWith("[")) {
                        references.add(jimpleTypeOfFieldDescriptor(desc));
                    } else {
                        references.add(RefType.v(name2));
                    }
                }
                if ((coffiClass.constant_pool[k2] instanceof CONSTANT_Fieldref_info) || (coffiClass.constant_pool[k2] instanceof CONSTANT_Methodref_info) || (coffiClass.constant_pool[k2] instanceof CONSTANT_InterfaceMethodref_info)) {
                    for (Type element : jimpleTypesOfFieldOrMethodDescriptor(cp_info.getTypeDescr(coffiClass.constant_pool, k2))) {
                        references.add(element);
                    }
                }
            }
        }
        for (int i4 = 0; i4 < coffiClass.methods_count; i4++) {
            method_info methodInfo2 = coffiClass.methods[i4];
            methodInfo2.jmethod.setSource(new CoffiMethodSource(coffiClass, methodInfo2));
        }
        for (int i5 = 0; i5 < coffiClass.attributes_count; i5++) {
            if (coffiClass.attributes[i5] instanceof SourceFile_attribute) {
                String sourceFile = ((CONSTANT_Utf8_info) coffiClass.constant_pool[((SourceFile_attribute) coffiClass.attributes[i5]).sourcefile_index]).convert();
                if (sourceFile.indexOf(32) >= 0) {
                    logger.debug("Warning: Class " + className + " has invalid SourceFile attribute (will be ignored).");
                } else {
                    aClass.addTag(new SourceFileTag(sourceFile, filePath));
                }
            } else if (coffiClass.attributes[i5] instanceof InnerClasses_attribute) {
                InnerClasses_attribute attr3 = (InnerClasses_attribute) coffiClass.attributes[i5];
                for (int j4 = 0; j4 < attr3.inner_classes_length; j4++) {
                    inner_class_entry e = attr3.inner_classes[j4];
                    String inner = null;
                    String outer = null;
                    String name3 = null;
                    if (e.inner_class_index != 0) {
                        int name_index = ((CONSTANT_Class_info) coffiClass.constant_pool[e.inner_class_index]).name_index;
                        inner = ((CONSTANT_Utf8_info) coffiClass.constant_pool[name_index]).convert();
                    }
                    if (e.outer_class_index != 0) {
                        int name_index2 = ((CONSTANT_Class_info) coffiClass.constant_pool[e.outer_class_index]).name_index;
                        outer = ((CONSTANT_Utf8_info) coffiClass.constant_pool[name_index2]).convert();
                    }
                    if (e.name_index != 0) {
                        name3 = ((CONSTANT_Utf8_info) coffiClass.constant_pool[e.name_index]).convert();
                    }
                    aClass.addTag(new InnerClassTag(inner, outer, name3, e.access_flags));
                }
            } else if (coffiClass.attributes[i5] instanceof Synthetic_attribute) {
                aClass.addTag(new SyntheticTag());
            } else if (coffiClass.attributes[i5] instanceof Deprecated_attribute) {
                aClass.addTag(new DeprecatedTag());
            } else if (coffiClass.attributes[i5] instanceof Signature_attribute) {
                int signature_index3 = ((Signature_attribute) coffiClass.attributes[i5]).signature_index;
                String generic_sig3 = ((CONSTANT_Utf8_info) coffiClass.constant_pool[signature_index3]).convert();
                aClass.addTag(new SignatureTag(generic_sig3));
            } else if (coffiClass.attributes[i5] instanceof EnclosingMethod_attribute) {
                EnclosingMethod_attribute attr4 = (EnclosingMethod_attribute) coffiClass.attributes[i5];
                int name_index3 = ((CONSTANT_Class_info) coffiClass.constant_pool[attr4.class_index]).name_index;
                String class_name = ((CONSTANT_Utf8_info) coffiClass.constant_pool[name_index3]).convert();
                CONSTANT_NameAndType_info info = (CONSTANT_NameAndType_info) coffiClass.constant_pool[attr4.method_index];
                String method_name = "";
                String method_sig = "";
                if (info != null) {
                    method_name = ((CONSTANT_Utf8_info) coffiClass.constant_pool[info.name_index]).convert();
                    method_sig = ((CONSTANT_Utf8_info) coffiClass.constant_pool[info.descriptor_index]).convert();
                }
                aClass.addTag(new EnclosingMethodTag(class_name, method_name, method_sig));
            } else if ((coffiClass.attributes[i5] instanceof RuntimeVisibleAnnotations_attribute) || (coffiClass.attributes[i5] instanceof RuntimeInvisibleAnnotations_attribute)) {
                addAnnotationVisibilityAttribute(aClass, coffiClass.attributes[i5], coffiClass, references);
            } else if (coffiClass.attributes[i5] instanceof Generic_attribute) {
                Generic_attribute attr5 = (Generic_attribute) coffiClass.attributes[i5];
                aClass.addTag(new GenericAttribute(((CONSTANT_Utf8_info) coffiClass.constant_pool[attr5.attribute_name]).convert(), attr5.info));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Type jimpleReturnTypeOfMethodDescriptor(String descriptor) {
        Type[] types = jimpleTypesOfFieldOrMethodDescriptor(descriptor);
        return types[types.length - 1];
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x01a7, code lost:
        if (r15 != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01af, code lost:
        if (r13 == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01b2, code lost:
        r0 = soot.ArrayType.v(r15, r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01be, code lost:
        r0 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01c2, code lost:
        r16 = r0;
        r0.add(r16);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Map<java.lang.String, soot.Type[]>] */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.Map<java.lang.String, soot.Type[]>] */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public soot.Type[] jimpleTypesOfFieldOrMethodDescriptor(java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 514
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.coffi.Util.jimpleTypesOfFieldOrMethodDescriptor(java.lang.String):soot.Type[]");
    }

    public Type jimpleTypeOfFieldDescriptor(String descriptor) {
        Type baseType;
        int numDimensions = 0;
        while (descriptor.startsWith("[")) {
            numDimensions++;
            descriptor = descriptor.substring(1);
        }
        if (descriptor.equals("B")) {
            baseType = ByteType.v();
        } else if (descriptor.equals("C")) {
            baseType = CharType.v();
        } else if (descriptor.equals("D")) {
            baseType = DoubleType.v();
        } else if (descriptor.equals("F")) {
            baseType = FloatType.v();
        } else if (descriptor.equals("I")) {
            baseType = IntType.v();
        } else if (descriptor.equals("J")) {
            baseType = LongType.v();
        } else if (descriptor.equals("V")) {
            baseType = VoidType.v();
        } else if (descriptor.startsWith("L")) {
            if (!descriptor.endsWith(";")) {
                throw new RuntimeException("Class reference does not end with ;");
            }
            String className = descriptor.substring(1, descriptor.length() - 1);
            baseType = RefType.v(className.replace('/', '.'));
        } else if (descriptor.equals("S")) {
            baseType = ShortType.v();
        } else if (descriptor.equals("Z")) {
            baseType = BooleanType.v();
        } else {
            throw new RuntimeException("Unknown field type: " + descriptor);
        }
        return numDimensions > 0 ? ArrayType.v(baseType, numDimensions) : baseType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetEasyNames() {
        this.nextEasyNameIndex = 0;
    }

    String getNextEasyName() {
        String[] easyNames = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        int justifiedIndex = this.nextEasyNameIndex;
        this.nextEasyNameIndex = justifiedIndex + 1;
        if (justifiedIndex >= easyNames.length) {
            return "local" + (justifiedIndex - easyNames.length);
        }
        return easyNames[justifiedIndex];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Local getLocalForStackOp(JimpleBody listBody, TypeStack typeStack, int index) {
        if (typeStack.get(index).equals(Double2ndHalfType.v()) || typeStack.get(index).equals(Long2ndHalfType.v())) {
            index--;
        }
        return getLocalCreatingIfNecessary(listBody, "$stack" + index, UnknownType.v());
    }

    String getAbbreviationOfClassName(String className) {
        StringBuffer buffer = new StringBuffer(new Character(className.charAt(0)).toString());
        int periodIndex = 0;
        while (true) {
            periodIndex = className.indexOf(46, periodIndex + 1);
            if (periodIndex != -1) {
                buffer.append(Character.toLowerCase(className.charAt(periodIndex + 1)));
            } else {
                return buffer.toString();
            }
        }
    }

    String getNormalizedClassName(String className) {
        String className2 = className.replace('/', '.');
        if (className2.endsWith(";")) {
            className2 = className2.substring(0, className2.length() - 1);
        }
        int numDimensions = 0;
        while (className2.startsWith("[")) {
            numDimensions++;
            className2 = String.valueOf(className2.substring(1, className2.length())) + "[]";
        }
        if (numDimensions != 0) {
            if (!className2.startsWith("L")) {
                throw new RuntimeException("For some reason an array reference does not start with L");
            }
            className2 = className2.substring(1, className2.length());
        }
        return className2;
    }

    private Local getLocalUnsafe(Body b, String name) {
        for (Local local : b.getLocals()) {
            if (local.getName().equals(name)) {
                return local;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Local getLocalCreatingIfNecessary(JimpleBody listBody, String name, Type type) {
        Local l = getLocalUnsafe(listBody, name);
        if (l != null) {
            if (!l.getType().equals(type)) {
                throw new RuntimeException("The body already declares this local name with a different type.");
            }
        } else {
            l = Jimple.v().newLocal(name, type);
            listBody.getLocals().add(l);
        }
        return l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Local getLocalForParameter(JimpleBody listBody, int index) {
        return getLocalForIndex(listBody, index, 0, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Local getLocalForIndex(JimpleBody listBody, int index, Instruction context) {
        return getLocalForIndex(listBody, index, context.originalIndex, context.nextOffset(context.originalIndex), ByteCode.isLocalStore(context.code));
    }

    private Local getLocalForIndex(JimpleBody listBody, int index, int bcIndex, int nextBcIndex, boolean isLocalStore) {
        Map<Integer, Local> indexToLocal;
        Local local;
        String name = null;
        if (this.useFaithfulNaming && this.activeVariableTable != null && bcIndex != -1) {
            int lookupBcIndex = bcIndex;
            if (isLocalStore) {
                lookupBcIndex = nextBcIndex;
            }
            name = this.activeVariableTable.getLocalVariableName(this.activeConstantPool, index, lookupBcIndex);
        }
        if (name == null) {
            name = "l" + index;
        }
        if (this.nameToIndexToLocal == null) {
            this.nameToIndexToLocal = new HashMap();
        }
        if (!this.nameToIndexToLocal.containsKey(name)) {
            indexToLocal = new HashMap<>();
            this.nameToIndexToLocal.put(name, indexToLocal);
        } else {
            indexToLocal = this.nameToIndexToLocal.get(name);
        }
        if (indexToLocal.containsKey(Integer.valueOf(index))) {
            local = indexToLocal.get(Integer.valueOf(index));
        } else {
            local = Jimple.v().newLocal(name, UnknownType.v());
            listBody.getLocals().add(local);
            indexToLocal.put(Integer.valueOf(index), local);
        }
        return local;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidJimpleName(String prospectiveName) {
        if (prospectiveName == null) {
            return false;
        }
        for (int i = 0; i < prospectiveName.length(); i++) {
            char c = prospectiveName.charAt(i);
            if (i != 0 || c < '0' || c > '9') {
                if ((c < '0' || c > '9') && ((c < 'a' || c > 'z') && ((c < 'A' || c > 'Z') && c != '_' && c != '$'))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private void addAnnotationVisibilityAttribute(Host host, attribute_info attribute, ClassFile coffiClass, Collection<Type> references) {
        VisibilityAnnotationTag tag;
        if (attribute instanceof RuntimeVisibleAnnotations_attribute) {
            tag = new VisibilityAnnotationTag(0);
            RuntimeVisibleAnnotations_attribute attr = (RuntimeVisibleAnnotations_attribute) attribute;
            addAnnotations(attr.number_of_annotations, attr.annotations, coffiClass, tag, references);
        } else {
            tag = new VisibilityAnnotationTag(1);
            RuntimeInvisibleAnnotations_attribute attr2 = (RuntimeInvisibleAnnotations_attribute) attribute;
            addAnnotations(attr2.number_of_annotations, attr2.annotations, coffiClass, tag, references);
        }
        host.addTag(tag);
    }

    private void addAnnotationVisibilityParameterAttribute(Host host, attribute_info attribute, ClassFile coffiClass, Collection<Type> references) {
        VisibilityParameterAnnotationTag tag;
        if (attribute instanceof RuntimeVisibleParameterAnnotations_attribute) {
            RuntimeVisibleParameterAnnotations_attribute attr = (RuntimeVisibleParameterAnnotations_attribute) attribute;
            tag = new VisibilityParameterAnnotationTag(attr.num_parameters, 0);
            for (int i = 0; i < attr.num_parameters; i++) {
                parameter_annotation pAnnot = attr.parameter_annotations[i];
                VisibilityAnnotationTag vTag = new VisibilityAnnotationTag(0);
                addAnnotations(pAnnot.num_annotations, pAnnot.annotations, coffiClass, vTag, references);
                tag.addVisibilityAnnotation(vTag);
            }
        } else {
            RuntimeInvisibleParameterAnnotations_attribute attr2 = (RuntimeInvisibleParameterAnnotations_attribute) attribute;
            tag = new VisibilityParameterAnnotationTag(attr2.num_parameters, 1);
            for (int i2 = 0; i2 < attr2.num_parameters; i2++) {
                parameter_annotation pAnnot2 = attr2.parameter_annotations[i2];
                VisibilityAnnotationTag vTag2 = new VisibilityAnnotationTag(1);
                addAnnotations(pAnnot2.num_annotations, pAnnot2.annotations, coffiClass, vTag2, references);
                tag.addVisibilityAnnotation(vTag2);
            }
        }
        host.addTag(tag);
    }

    private void addAnnotations(int numAnnots, annotation[] annotations, ClassFile coffiClass, VisibilityAnnotationTag tag, Collection<Type> references) {
        for (int i = 0; i < numAnnots; i++) {
            annotation annot = annotations[i];
            String annotType = ((CONSTANT_Utf8_info) coffiClass.constant_pool[annot.type_index]).convert();
            String ref = annotType.substring(1, annotType.length() - 1);
            references.add(RefType.v(ref.replace('/', '.')));
            AnnotationTag annotTag = new AnnotationTag(annotType, createElementTags(annot.num_element_value_pairs, coffiClass, annot.element_value_pairs));
            tag.addAnnotation(annotTag);
        }
    }

    private ArrayList<AnnotationElem> createElementTags(int count, ClassFile coffiClass, element_value[] elems) {
        ArrayList<AnnotationElem> list = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            element_value ev = elems[j];
            char kind = ev.tag;
            String elemName = "default";
            if (ev.name_index != 0) {
                elemName = ((CONSTANT_Utf8_info) coffiClass.constant_pool[ev.name_index]).convert();
            }
            if (kind == 'B' || kind == 'C' || kind == 'I' || kind == 'S' || kind == 'Z' || kind == 'D' || kind == 'F' || kind == 'J' || kind == 's') {
                constant_element_value cev = (constant_element_value) ev;
                if (kind == 'B' || kind == 'C' || kind == 'I' || kind == 'S' || kind == 'Z') {
                    cp_info cval = coffiClass.constant_pool[cev.constant_value_index];
                    int constant_val = (int) ((CONSTANT_Integer_info) cval).bytes;
                    AnnotationIntElem elem = new AnnotationIntElem(constant_val, kind, elemName);
                    list.add(elem);
                } else if (kind == 'D') {
                    cp_info cval2 = coffiClass.constant_pool[cev.constant_value_index];
                    double constant_val2 = ((CONSTANT_Double_info) cval2).convert();
                    AnnotationDoubleElem elem2 = new AnnotationDoubleElem(constant_val2, kind, elemName);
                    list.add(elem2);
                } else if (kind == 'F') {
                    cp_info cval3 = coffiClass.constant_pool[cev.constant_value_index];
                    float constant_val3 = ((CONSTANT_Float_info) cval3).convert();
                    AnnotationFloatElem elem3 = new AnnotationFloatElem(constant_val3, kind, elemName);
                    list.add(elem3);
                } else if (kind == 'J') {
                    cp_info cval4 = coffiClass.constant_pool[cev.constant_value_index];
                    CONSTANT_Long_info lcval = (CONSTANT_Long_info) cval4;
                    long constant_val4 = (lcval.high << 32) + lcval.low;
                    AnnotationLongElem elem4 = new AnnotationLongElem(constant_val4, kind, elemName);
                    list.add(elem4);
                } else if (kind == 's') {
                    cp_info cval5 = coffiClass.constant_pool[cev.constant_value_index];
                    String constant_val5 = ((CONSTANT_Utf8_info) cval5).convert();
                    AnnotationStringElem elem5 = new AnnotationStringElem(constant_val5, kind, elemName);
                    list.add(elem5);
                }
            } else if (kind == 'e') {
                enum_constant_element_value ecev = (enum_constant_element_value) ev;
                cp_info type_val = coffiClass.constant_pool[ecev.type_name_index];
                String type_name = ((CONSTANT_Utf8_info) type_val).convert();
                cp_info name_val = coffiClass.constant_pool[ecev.constant_name_index];
                String constant_name = ((CONSTANT_Utf8_info) name_val).convert();
                AnnotationEnumElem elem6 = new AnnotationEnumElem(type_name, constant_name, kind, elemName);
                list.add(elem6);
            } else if (kind == 'c') {
                cp_info cval6 = coffiClass.constant_pool[((class_element_value) ev).class_info_index];
                CONSTANT_Utf8_info sval = (CONSTANT_Utf8_info) cval6;
                String desc = sval.convert();
                AnnotationClassElem elem7 = new AnnotationClassElem(desc, kind, elemName);
                list.add(elem7);
            } else if (kind == '[') {
                array_element_value aev = (array_element_value) ev;
                int num_vals = aev.num_values;
                ArrayList<AnnotationElem> elemVals = createElementTags(num_vals, coffiClass, aev.values);
                AnnotationArrayElem elem8 = new AnnotationArrayElem(elemVals, kind, elemName);
                list.add(elem8);
            } else if (kind == '@') {
                annotation annot = ((annotation_element_value) ev).annotation_value;
                String annotType = ((CONSTANT_Utf8_info) coffiClass.constant_pool[annot.type_index]).convert();
                AnnotationTag annotTag = new AnnotationTag(annotType, createElementTags(annot.num_element_value_pairs, coffiClass, annot.element_value_pairs));
                AnnotationAnnotationElem elem9 = new AnnotationAnnotationElem(annotTag, kind, elemName);
                list.add(elem9);
            }
        }
        return list;
    }
}
