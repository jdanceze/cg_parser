package soot;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.baf.DoubleWordType;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
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
import soot.tagkit.Base64;
import soot.tagkit.DeprecatedTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toolkits.graph.Block;
import soot.util.StringTools;
/* loaded from: gencallgraphv3.jar:soot/AbstractJasminClass.class */
public abstract class AbstractJasminClass {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJasminClass.class);
    protected Map<Unit, String> unitToLabel;
    protected Map<Local, Integer> localToSlot;
    protected Map<Unit, Integer> subroutineToReturnAddressSlot;
    protected List<String> code;
    protected boolean isEmittingMethodCode;
    protected int labelCount;
    protected boolean isNextGotoAJsr;
    protected int returnAddressSlot;
    protected Map<Local, Object> localToGroup;
    protected Map<Object, Integer> groupToColorCount;
    protected Map<Local, Integer> localToColor;
    protected int currentStackHeight = 0;
    protected int maxStackHeight = 0;
    protected Map<Block, Integer> blockToStackHeight = new HashMap();
    protected Map<Block, Integer> blockToLogicalStackHeight = new HashMap();

    protected abstract void emitMethodBody(SootMethod sootMethod);

    public static String slashify(String s) {
        return s.replace('.', '/');
    }

    public static int sizeOfType(Type t) {
        if ((t instanceof DoubleWordType) || (t instanceof LongType) || (t instanceof DoubleType)) {
            return 2;
        }
        if (t instanceof VoidType) {
            return 0;
        }
        return 1;
    }

    public static int argCountOf(SootMethodRef m) {
        int argCount = 0;
        for (Type t : m.parameterTypes()) {
            argCount += sizeOfType(t);
        }
        return argCount;
    }

    public static String jasminDescriptorOf(Type type) {
        TypeSwitch<String> sw = new TypeSwitch<String>() { // from class: soot.AbstractJasminClass.1
            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseBooleanType(BooleanType t) {
                setResult("Z");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseByteType(ByteType t) {
                setResult("B");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseCharType(CharType t) {
                setResult("C");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseDoubleType(DoubleType t) {
                setResult("D");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseFloatType(FloatType t) {
                setResult("F");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseIntType(IntType t) {
                setResult("I");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseLongType(LongType t) {
                setResult("J");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseShortType(ShortType t) {
                setResult("S");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void defaultCase(Type t) {
                throw new RuntimeException("Invalid type: " + t);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseArrayType(ArrayType t) {
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < t.numDimensions; i++) {
                    buffer.append('[');
                }
                buffer.append(AbstractJasminClass.jasminDescriptorOf(t.baseType));
                setResult(buffer.toString());
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseRefType(RefType t) {
                setResult("L" + t.getClassName().replace('.', '/') + ";");
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseVoidType(VoidType t) {
                setResult("V");
            }
        };
        type.apply(sw);
        return sw.getResult();
    }

    public static String jasminDescriptorOf(SootMethodRef m) {
        StringBuilder buffer = new StringBuilder();
        buffer.append('(');
        for (Type t : m.parameterTypes()) {
            buffer.append(jasminDescriptorOf(t));
        }
        buffer.append(')');
        buffer.append(jasminDescriptorOf(m.returnType()));
        return buffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void emit(String s) {
        okayEmit(s);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void okayEmit(String s) {
        if (this.isEmittingMethodCode && !s.endsWith(":")) {
            this.code.add(ASTNode.TAB + s);
        } else {
            this.code.add(s);
        }
    }

    private String getVisibilityAnnotationAttr(VisibilityAnnotationTag tag) {
        if (tag == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        switch (tag.getVisibility()) {
            case 0:
                sb.append(".runtime_visible_annotation\n");
                break;
            case 1:
                sb.append(".runtime_invisible_annotation\n");
                break;
            default:
                return "";
        }
        if (tag.hasAnnotations()) {
            Iterator<AnnotationTag> it = tag.getAnnotations().iterator();
            while (it.hasNext()) {
                AnnotationTag annot = it.next();
                sb.append(".annotation ");
                sb.append(StringTools.getQuotedStringOf(annot.getType())).append('\n');
                for (AnnotationElem ae : annot.getElems()) {
                    sb.append(getElemAttr(ae));
                }
                sb.append(".end .annotation\n");
            }
        }
        sb.append(".end .annotation_attr\n");
        return sb.toString();
    }

    private String getVisibilityParameterAnnotationAttr(VisibilityParameterAnnotationTag tag) {
        StringBuilder sb = new StringBuilder();
        sb.append(".param ");
        if (tag.getKind() == 0) {
            sb.append(".runtime_visible_annotation\n");
        } else {
            sb.append(".runtime_invisible_annotation\n");
        }
        ArrayList<VisibilityAnnotationTag> vis_list = tag.getVisibilityAnnotations();
        if (vis_list != null) {
            Iterator<VisibilityAnnotationTag> it = vis_list.iterator();
            while (it.hasNext()) {
                VisibilityAnnotationTag vat = it.next();
                VisibilityAnnotationTag safeVat = vat == null ? SafeVisibilityAnnotationTags.get(tag.getKind()) : vat;
                sb.append(getVisibilityAnnotationAttr(safeVat));
            }
        }
        sb.append(".end .param\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/AbstractJasminClass$SafeVisibilityAnnotationTags.class */
    public static class SafeVisibilityAnnotationTags {
        private static final Map<Integer, VisibilityAnnotationTag> safeVats = new HashMap();

        static VisibilityAnnotationTag get(int kind) {
            VisibilityAnnotationTag safeVat = safeVats.get(Integer.valueOf(kind));
            if (safeVat == null) {
                Map<Integer, VisibilityAnnotationTag> map = safeVats;
                Integer valueOf = Integer.valueOf(kind);
                VisibilityAnnotationTag visibilityAnnotationTag = new VisibilityAnnotationTag(kind);
                safeVat = visibilityAnnotationTag;
                map.put(valueOf, visibilityAnnotationTag);
            }
            return safeVat;
        }

        private SafeVisibilityAnnotationTags() {
        }
    }

    private String getElemAttr(AnnotationElem elem) {
        StringBuilder result = new StringBuilder(".elem ");
        switch (elem.getKind()) {
            case '@':
                result.append(".ann_kind ");
                result.append('\"').append(elem.getName()).append("\"\n");
                AnnotationTag annot = ((AnnotationAnnotationElem) elem).getValue();
                result.append(".annotation ");
                result.append(StringTools.getQuotedStringOf(annot.getType())).append('\n');
                for (AnnotationElem ae : annot.getElems()) {
                    result.append(getElemAttr(ae));
                }
                result.append(".end .annotation\n");
                result.append(".end .annot_elem\n");
                break;
            case 'B':
                result.append(".byte_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationIntElem) elem).getValue());
                result.append('\n');
                break;
            case 'C':
                result.append(".char_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationIntElem) elem).getValue());
                result.append('\n');
                break;
            case 'D':
                result.append(".doub_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationDoubleElem) elem).getValue());
                result.append('\n');
                break;
            case 'F':
                result.append(".float_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationFloatElem) elem).getValue());
                result.append('\n');
                break;
            case 'I':
                result.append(".int_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationIntElem) elem).getValue());
                result.append('\n');
                break;
            case 'J':
                result.append(".long_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationLongElem) elem).getValue());
                result.append('\n');
                break;
            case 'S':
                result.append(".short_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(((AnnotationIntElem) elem).getValue());
                result.append('\n');
                break;
            case 'Z':
                result.append(".bool_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                if (elem instanceof AnnotationIntElem) {
                    result.append(((AnnotationIntElem) elem).getValue());
                } else {
                    result.append(((AnnotationBooleanElem) elem).getValue() ? 1 : 0);
                }
                result.append('\n');
                break;
            case '[':
                result.append(".arr_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append('\n');
                AnnotationArrayElem arrayElem = (AnnotationArrayElem) elem;
                for (int i = 0; i < arrayElem.getNumValues(); i++) {
                    result.append(getElemAttr(arrayElem.getValueAt(i)));
                }
                result.append(".end .arr_elem\n");
                break;
            case 'c':
                result.append(".cls_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(StringTools.getQuotedStringOf(((AnnotationClassElem) elem).getDesc()));
                result.append('\n');
                break;
            case 'e':
                result.append(".enum_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(StringTools.getQuotedStringOf(((AnnotationEnumElem) elem).getTypeName()));
                result.append(' ');
                result.append(StringTools.getQuotedStringOf(((AnnotationEnumElem) elem).getConstantName()));
                result.append('\n');
                break;
            case 's':
                result.append(".str_kind ");
                result.append('\"').append(elem.getName()).append("\" ");
                result.append(StringTools.getQuotedStringOf(((AnnotationStringElem) elem).getValue()));
                result.append('\n');
                break;
            default:
                throw new RuntimeException("Unknown Elem Attr Kind: " + elem.getKind());
        }
        return result.toString();
    }

    public AbstractJasminClass(SootClass sootClass) {
        InnerClassAttribute ica;
        SourceFileTag tag;
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.start();
        }
        if (Options.v().verbose()) {
            logger.debug("[" + sootClass.getName() + "] Constructing baf.JasminClass...");
        }
        this.code = new LinkedList();
        int modifiers = sootClass.getModifiers();
        if (!Options.v().no_output_source_file_attribute() && (tag = (SourceFileTag) sootClass.getTag(SourceFileTag.NAME)) != null) {
            String srcName = tag.getSourceFile();
            String srcName2 = StringTools.getEscapedStringOf(File.separatorChar == '\\' ? srcName.replace('\\', '/') : srcName);
            if (!Options.v().android_jars().isEmpty() && !srcName2.isEmpty() && Character.isDigit(srcName2.charAt(0))) {
                srcName2 = "n_" + srcName2;
            }
            String srcName3 = srcName2.replace(Instruction.argsep, HelpFormatter.DEFAULT_OPT_PREFIX).replace("\"", "");
            if (!srcName3.isEmpty()) {
                emit(".source " + srcName3);
            }
        }
        if (Modifier.isInterface(modifiers)) {
            emit(".interface " + Modifier.toString(modifiers - 512) + Instruction.argsep + slashify(sootClass.getName()));
        } else {
            emit(".class " + Modifier.toString(modifiers) + Instruction.argsep + slashify(sootClass.getName()));
        }
        if (sootClass.hasSuperclass()) {
            emit(".super " + slashify(sootClass.getSuperclass().getName()));
        } else {
            emit(".no_super");
        }
        emit("");
        for (SootClass inter : sootClass.getInterfaces()) {
            emit(".implements " + slashify(inter.getName()));
        }
        for (Tag tag2 : sootClass.getTags()) {
            if (tag2 instanceof Attribute) {
                emit(".class_attribute " + tag2.getName() + " \"" + String.valueOf(Base64.encode(((Attribute) tag2).getValue())) + "\"");
            }
        }
        if (sootClass.hasTag(SyntheticTag.NAME) || Modifier.isSynthetic(sootClass.getModifiers())) {
            emit(".synthetic\n");
        }
        if (!Options.v().no_output_inner_classes_attribute() && (ica = (InnerClassAttribute) sootClass.getTag(InnerClassAttribute.NAME)) != null) {
            List<InnerClassTag> specs = ica.getSpecs();
            if (!specs.isEmpty()) {
                emit(".inner_class_attr ");
                for (InnerClassTag ict : specs) {
                    StringBuilder str = new StringBuilder(".inner_class_spec_attr ");
                    str.append('\"').append(ict.getInnerClass()).append("\" ");
                    str.append('\"').append(ict.getOuterClass()).append("\" ");
                    str.append('\"').append(ict.getShortName()).append("\" ");
                    str.append(Modifier.toString(ict.getAccessFlags()));
                    str.append(" .end .inner_class_spec_attr");
                    emit(str.toString());
                }
                emit(".end .inner_class_attr\n");
            }
        }
        EnclosingMethodTag eMethTag = (EnclosingMethodTag) sootClass.getTag(EnclosingMethodTag.NAME);
        if (eMethTag != null) {
            StringBuilder encMeth = new StringBuilder(".enclosing_method_attr ");
            encMeth.append('\"').append(eMethTag.getEnclosingClass()).append("\" ");
            encMeth.append('\"').append(eMethTag.getEnclosingMethod()).append("\" ");
            encMeth.append('\"').append(eMethTag.getEnclosingMethodSig()).append("\"\n");
            emit(encMeth.toString());
        }
        if (sootClass.hasTag(DeprecatedTag.NAME)) {
            emit(".deprecated\n");
        }
        SignatureTag sigTag = (SignatureTag) sootClass.getTag(SignatureTag.NAME);
        if (sigTag != null) {
            emit(".signature_attr \"" + sigTag.getSignature() + "\"\n");
        }
        for (Tag t : sootClass.getTags()) {
            if (VisibilityAnnotationTag.NAME.equals(t.getName())) {
                emit(getVisibilityAnnotationAttr((VisibilityAnnotationTag) t));
            }
        }
        for (SootField field : sootClass.getFields()) {
            StringBuilder fieldString = new StringBuilder();
            fieldString.append(".field ").append(Modifier.toString(field.getModifiers()));
            fieldString.append(" \"").append(field.getName()).append("\" ");
            fieldString.append(jasminDescriptorOf(field.getType()));
            Iterator<Tag> it = field.getTags().iterator();
            while (true) {
                if (it.hasNext()) {
                    Tag t2 = it.next();
                    String name = t2.getName();
                    switch (name.hashCode()) {
                        case -1381833090:
                            if (name.equals(StringConstantValueTag.NAME)) {
                                fieldString.append(" = ");
                                fieldString.append(StringTools.getQuotedStringOf(((StringConstantValueTag) t2).getStringValue()));
                                break;
                            }
                        case -890200343:
                            if (name.equals(LongConstantValueTag.NAME)) {
                                fieldString.append(" = ");
                                fieldString.append(((LongConstantValueTag) t2).getLongValue());
                                break;
                            }
                        case 897661291:
                            if (name.equals(IntegerConstantValueTag.NAME)) {
                                fieldString.append(" = ");
                                fieldString.append(((IntegerConstantValueTag) t2).getIntValue());
                                break;
                            }
                        case 1312183945:
                            if (name.equals(FloatConstantValueTag.NAME)) {
                                fieldString.append(" = ");
                                fieldString.append(floatToString(((FloatConstantValueTag) t2).getFloatValue()));
                                break;
                            }
                        case 2039841342:
                            if (name.equals(DoubleConstantValueTag.NAME)) {
                                fieldString.append(" = ");
                                fieldString.append(doubleToString(((DoubleConstantValueTag) t2).getDoubleValue()));
                                break;
                            }
                    }
                }
            }
            if (field.hasTag(SyntheticTag.NAME) || Modifier.isSynthetic(field.getModifiers())) {
                fieldString.append(" .synthetic");
            }
            fieldString.append('\n');
            if (field.hasTag(DeprecatedTag.NAME)) {
                fieldString.append(".deprecated\n");
            }
            SignatureTag sigTag2 = (SignatureTag) field.getTag(SignatureTag.NAME);
            if (sigTag2 != null) {
                fieldString.append(".signature_attr ");
                fieldString.append('\"').append(sigTag2.getSignature()).append("\"\n");
            }
            for (Tag t3 : field.getTags()) {
                if (VisibilityAnnotationTag.NAME.equals(t3.getName())) {
                    fieldString.append(getVisibilityAnnotationAttr((VisibilityAnnotationTag) t3));
                }
            }
            emit(fieldString.toString());
            for (Tag tag3 : field.getTags()) {
                if (tag3 instanceof Attribute) {
                    emit(".field_attribute " + tag3.getName() + " \"" + String.valueOf(Base64.encode(((Attribute) tag3).getValue())) + "\"");
                }
            }
        }
        if (sootClass.getFieldCount() != 0) {
            emit("");
        }
        Iterator<SootMethod> methodIt = sootClass.methodIterator();
        while (methodIt.hasNext()) {
            SootMethod next = methodIt.next();
            emitMethod(next);
            emit("");
        }
        if (Options.v().time()) {
            Timers.v().buildJasminTimer.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assignColorsToLocals(Body body) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Assigning colors to locals...");
        }
        if (Options.v().time()) {
            Timers.v().packTimer.start();
        }
        this.localToGroup = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
        this.groupToColorCount = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
        this.localToColor = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
        for (Local l : body.getLocals()) {
            Object g = sizeOfType(l.getType()) == 1 ? IntType.v() : LongType.v();
            this.localToGroup.put(l, g);
            this.groupToColorCount.putIfAbsent(g, 0);
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            if (s instanceof IdentityStmt) {
                Value leftOp = ((IdentityStmt) s).getLeftOp();
                if (leftOp instanceof Local) {
                    Local l2 = (Local) leftOp;
                    Object group = this.localToGroup.get(l2);
                    int count = this.groupToColorCount.get(group).intValue();
                    this.localToColor.put(l2, Integer.valueOf(count));
                    this.groupToColorCount.put(group, Integer.valueOf(count + 1));
                }
            }
        }
    }

    protected void emitMethod(SootMethod method) {
        if (method.isPhantom()) {
            return;
        }
        emit(".method " + Modifier.toString(method.getModifiers()) + Instruction.argsep + method.getName() + jasminDescriptorOf(method.makeRef()));
        for (SootClass exceptClass : method.getExceptions()) {
            emit(".throws " + exceptClass.getName());
        }
        if (method.hasTag(SyntheticTag.NAME) || Modifier.isSynthetic(method.getModifiers())) {
            emit(".synthetic");
        }
        if (method.hasTag(DeprecatedTag.NAME)) {
            emit(".deprecated");
        }
        SignatureTag sigTag = (SignatureTag) method.getTag(SignatureTag.NAME);
        if (sigTag != null) {
            emit(".signature_attr \"" + sigTag.getSignature() + "\"");
        }
        AnnotationDefaultTag annotDefTag = (AnnotationDefaultTag) method.getTag(AnnotationDefaultTag.NAME);
        if (annotDefTag != null) {
            emit(".annotation_default " + getElemAttr(annotDefTag.getDefaultVal()) + ".end .annotation_default");
        }
        for (Tag t : method.getTags()) {
            String name = t.getName();
            if (VisibilityAnnotationTag.NAME.equals(name)) {
                emit(getVisibilityAnnotationAttr((VisibilityAnnotationTag) t));
            } else if (VisibilityParameterAnnotationTag.NAME.equals(name)) {
                emit(getVisibilityParameterAnnotationAttr((VisibilityParameterAnnotationTag) t));
            }
        }
        if (method.isConcrete()) {
            if (!method.hasActiveBody()) {
                throw new RuntimeException("method: " + method.getName() + " has no active body!");
            }
            emitMethodBody(method);
        }
        emit(".end method");
        for (Tag tag : method.getTags()) {
            if (tag instanceof Attribute) {
                emit(".method_attribute " + tag.getName() + " \"" + String.valueOf(Base64.encode(tag.getValue())) + "\"");
            }
        }
    }

    public void print(PrintWriter out) {
        for (String s : this.code) {
            out.println(s);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String doubleToString(DoubleConstant v) {
        String s = v.toString();
        switch (s.hashCode()) {
            case -2076260174:
                if (s.equals("#-Infinity")) {
                    return "-DoubleInfinity";
                }
                break;
            case 1120728:
                if (s.equals("#NaN")) {
                    return "+DoubleNaN";
                }
                break;
            case 1401420651:
                if (s.equals("#Infinity")) {
                    return "+DoubleInfinity";
                }
                break;
        }
        return s;
    }

    protected String doubleToString(double d) {
        String s = Double.toString(d);
        switch (s.hashCode()) {
            case 78043:
                if (s.equals("NaN")) {
                    return "+DoubleNaN";
                }
                break;
            case 237817416:
                if (s.equals("Infinity")) {
                    return "+DoubleInfinity";
                }
                break;
            case 506745205:
                if (s.equals("-Infinity")) {
                    return "-DoubleInfinity";
                }
                break;
        }
        return s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String floatToString(FloatConstant v) {
        String s = v.toString();
        switch (s.hashCode()) {
            case 34742638:
                if (s.equals("#NaNF")) {
                    return "+FloatNaN";
                }
                break;
            case 60444116:
                if (s.equals("#-InfinityF")) {
                    return "-FloatInfinity";
                }
                break;
            case 494367291:
                if (s.equals("#InfinityF")) {
                    return "+FloatInfinity";
                }
                break;
        }
        return s;
    }

    protected String floatToString(float d) {
        String s = Float.toString(d);
        switch (s.hashCode()) {
            case 78043:
                if (s.equals("NaN")) {
                    return "+FloatNaN";
                }
                break;
            case 237817416:
                if (s.equals("Infinity")) {
                    return "+FloatInfinity";
                }
                break;
            case 506745205:
                if (s.equals("-Infinity")) {
                    return "-FloatInfinity";
                }
                break;
        }
        return s;
    }
}
