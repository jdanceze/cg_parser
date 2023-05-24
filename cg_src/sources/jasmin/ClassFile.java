package jasmin;

import jas.AnnotElemValPair;
import jas.AnnotationAttr;
import jas.AnnotationDefaultAttr;
import jas.ArrayElemValPair;
import jas.AsciiCP;
import jas.CP;
import jas.Catchtable;
import jas.ClassCP;
import jas.ClassElemValPair;
import jas.ClassEnv;
import jas.CodeAttr;
import jas.ConstAttr;
import jas.DeprecatedAttr;
import jas.DoubleCP;
import jas.DoubleElemValPair;
import jas.ElemValPair;
import jas.EnclMethAttr;
import jas.EnumElemValPair;
import jas.ExceptAttr;
import jas.FieldCP;
import jas.FloatCP;
import jas.FloatElemValPair;
import jas.GenericAttr;
import jas.IincInsn;
import jas.InnerClassAttr;
import jas.InnerClassSpecAttr;
import jas.Insn;
import jas.IntElemValPair;
import jas.IntegerCP;
import jas.InterfaceCP;
import jas.InvokeDynamicCP;
import jas.InvokeinterfaceInsn;
import jas.Label;
import jas.LineTableAttr;
import jas.LocalVarEntry;
import jas.LocalVarTableAttr;
import jas.LongCP;
import jas.LongElemValPair;
import jas.LookupswitchInsn;
import jas.Method;
import jas.MethodCP;
import jas.MethodHandleCP;
import jas.MultiarrayInsn;
import jas.ParameterVisibilityAnnotationAttr;
import jas.SignatureAttr;
import jas.StringCP;
import jas.StringElemValPair;
import jas.SyntheticAttr;
import jas.TableswitchInsn;
import jas.Var;
import jas.VisibilityAnnotationAttr;
import jas.jasError;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jasmin/ClassFile.class */
public class ClassFile {
    String filename;
    ClassEnv class_env;
    String class_name;
    String source_name;
    Scanner scanner;
    String method_name;
    String method_signature;
    short method_access;
    ExceptAttr except_attr;
    Catchtable catch_table;
    LocalVarTableAttr var_table;
    LineTableAttr line_table;
    CodeAttr code;
    InnerClassAttr inner_class_attr;
    Hashtable labels;
    boolean methSynth;
    boolean methDepr;
    String methSigAttr;
    VisibilityAnnotationAttr methAnnotAttrVis;
    VisibilityAnnotationAttr methAnnotAttrInvis;
    ParameterVisibilityAnnotationAttr methParamAnnotAttrVis;
    ParameterVisibilityAnnotationAttr methParamAnnotAttrInvis;
    ElemValPair methAnnotDef;
    int line_label_count;
    int line_num;
    boolean auto_number;
    Vector switch_vec;
    int low_value;
    int high_value;
    int lastInstSize;
    Method currentMethod;
    Var currentField;
    VisibilityAnnotationAttr vis_annot_attr;
    static final String BGN_METHOD = "bgnmethod:";
    static final String END_METHOD = "endmethod:";
    int errors;
    AnnotationAttr currAnn = null;

    public void addSootCodeAttr(String name, String value) {
        this.class_env.addCPItem(new AsciiCP(name));
        this.code.addSootCodeAttr(name, value);
    }

    public void addGenericAttrToMethod(String name, byte[] value) {
        if (this.currentMethod == null) {
            System.err.println("Error: no field in scope to add attribute onto.");
            return;
        }
        this.class_env.requireJava6();
        this.class_env.addCPItem(new AsciiCP(name));
        this.currentMethod.addGenericAttr(new GenericAttr(name, value));
    }

    public void addGenericAttrToField(String name, byte[] value) {
        if (this.currentField == null) {
            System.err.println("Error: no field in scope to add attribute onto.");
            return;
        }
        this.class_env.requireJava6();
        this.class_env.addCPItem(new AsciiCP(name));
        this.currentField.addGenericAttr(new GenericAttr(name, value));
    }

    public void addDeprecatedToField() {
        if (this.currentField == null) {
            System.err.println("Error: no field in scope to add deprecated attribute onto");
            return;
        }
        this.class_env.requireJava6();
        this.currentField.addDeprecatedAttr(new DeprecatedAttr());
    }

    public void addGenericAttrToClass(GenericAttr g) {
        this.class_env.requireJava6();
        this.class_env.addGenericAttr(g);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void report_error(String msg) {
        System.err.print(this.filename + ":");
        System.err.print(this.scanner.line_num);
        System.err.println(": " + msg + ".");
        if (this.scanner.char_num >= 0) {
            System.err.println(this.scanner.line.toString());
            for (int i = 0; i < this.scanner.char_num; i++) {
                if (this.scanner.line.charAt(i) == '\t') {
                    System.err.print("\t");
                } else {
                    System.err.print(Instruction.argsep);
                }
            }
            System.err.println("^");
        }
        this.errors++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSource(String name) {
        this.source_name = name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClass(String name, short acc) {
        this.class_name = name;
        this.class_env.setClass(new ClassCP(name));
        this.class_env.setClassAccess(acc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSuperClass(String name) {
        this.class_env.setSuperClass(new ClassCP(name));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNoSuperClass() {
        this.class_env.setNoSuperClass();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addInterface(String name) {
        this.class_env.addInterface(new ClassCP(name));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassDeprAttr(Object res) {
        if (res != null) {
            this.class_env.requireJava5();
            this.class_env.setClassDepr(new DeprecatedAttr());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassSigAttr(Object res) {
        if (res != null) {
            String sig = (String) res;
            this.class_env.setClassSigAttr(new SignatureAttr(sig));
            if (sig.contains("<")) {
                this.class_env.requireJava5();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassAnnotAttrVisible(Object res) {
        if (res != null) {
            this.class_env.requireJava5();
            this.class_env.setClassAnnotAttrVis((VisibilityAnnotationAttr) res);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassAnnotAttrInvisible(Object res) {
        if (res != null) {
            this.class_env.requireJava5();
            this.class_env.setClassAnnotAttrInvis((VisibilityAnnotationAttr) res);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addField(short access, String name, String sig, Object value, Object dep_attr, Object sig_attr, Object vis_annot_attr, Object vis_annot_attr2) {
        addField(access, name, sig, value, null, dep_attr, sig_attr, vis_annot_attr, vis_annot_attr2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addField(short access, String name, String sig, Object value, String synth, Object dep_attr, Object sig_attr, Object vis_annot_attr, Object vis_annot_attr2) {
        if (sig.contains("<") || (sig_attr != null && ((String) sig_attr).contains("<"))) {
            this.class_env.requireJava5();
        }
        if (value == null) {
            if (synth == null) {
                this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), null);
            } else {
                this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), null, new SyntheticAttr());
                this.class_env.requireJava6();
            }
            if (dep_attr != null) {
                this.class_env.requireJava5();
                this.currentField.addDeprecatedAttr(new DeprecatedAttr());
            }
            if (sig_attr != null) {
                this.currentField.addSignatureAttr(new SignatureAttr((String) sig_attr));
            }
            if (vis_annot_attr != null) {
                this.class_env.requireJava5();
                VisibilityAnnotationAttr attribute = (VisibilityAnnotationAttr) vis_annot_attr;
                if (attribute.getKind().equals("RuntimeVisible")) {
                    this.currentField.addVisibilityAnnotationAttrVis(attribute);
                } else {
                    this.currentField.addVisibilityAnnotationAttrInvis(attribute);
                }
            }
            if (vis_annot_attr2 != null) {
                this.class_env.requireJava5();
                VisibilityAnnotationAttr attribute2 = (VisibilityAnnotationAttr) vis_annot_attr2;
                if (attribute2.getKind().equals("RuntimeVisible")) {
                    this.currentField.addVisibilityAnnotationAttrVis(attribute2);
                } else {
                    this.currentField.addVisibilityAnnotationAttrInvis(attribute2);
                }
            }
            this.class_env.addField(this.currentField);
            return;
        }
        CP cp = null;
        if (sig.equals("I") || sig.equals("Z") || sig.equals("C") || sig.equals("B") || sig.equals("S")) {
            cp = new IntegerCP(((Number) value).intValue());
        } else if (sig.equals("F")) {
            cp = new FloatCP(((Number) value).floatValue());
        } else if (sig.equals("D")) {
            cp = new DoubleCP(((Number) value).doubleValue());
        } else if (sig.equals("J")) {
            cp = new LongCP(((Number) value).longValue());
        } else if (sig.equals("Ljava/lang/String;")) {
            cp = new StringCP((String) value);
        }
        if (synth == null) {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), cp == null ? null : new ConstAttr(cp));
        } else {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), cp == null ? null : new ConstAttr(cp), new SyntheticAttr());
            this.class_env.requireJava6();
        }
        if (dep_attr != null) {
            this.class_env.requireJava5();
            this.currentField.addDeprecatedAttr(new DeprecatedAttr());
        }
        if (sig_attr != null) {
            this.currentField.addSignatureAttr(new SignatureAttr((String) sig_attr));
        }
        this.class_env.addField(this.currentField);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newMethod(String name, String signature, int access) {
        this.labels = new Hashtable();
        this.method_name = name;
        this.code = null;
        this.except_attr = null;
        this.catch_table = null;
        this.var_table = null;
        this.line_table = null;
        this.line_label_count = 0;
        this.method_signature = signature;
        this.method_access = (short) access;
        this.methSynth = false;
        this.methDepr = false;
        this.methSigAttr = null;
        this.methAnnotAttrVis = null;
        this.methAnnotAttrInvis = null;
        this.methParamAnnotAttrVis = null;
        this.methParamAnnotAttrInvis = null;
        this.methAnnotDef = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endMethod() throws jasError {
        if (this.code != null) {
            plantLabel(END_METHOD);
            if (this.catch_table != null) {
                this.code.setCatchtable(this.catch_table);
            }
            if (this.var_table != null) {
                this.code.setLocalVarTable(this.var_table);
            }
            if (this.line_table != null) {
                this.code.setLineTable(this.line_table);
            }
            this.code.setLabelTable(this.labels);
        }
        if (!this.methSynth) {
            this.currentMethod = new Method(this.method_access, new AsciiCP(this.method_name), new AsciiCP(this.method_signature), this.code, this.except_attr);
        } else {
            this.currentMethod = new Method(this.method_access, new AsciiCP(this.method_name), new AsciiCP(this.method_signature), this.code, this.except_attr, new SyntheticAttr());
            this.class_env.requireJava6();
        }
        if (this.methDepr) {
            this.class_env.requireJava5();
            this.currentMethod.addDeprecatedAttr(new DeprecatedAttr());
        }
        if (this.methSigAttr != null) {
            this.currentMethod.addSignatureAttr(new SignatureAttr(this.methSigAttr));
        }
        if (this.methAnnotAttrVis != null) {
            this.class_env.requireJava5();
            this.currentMethod.addVisAnnotationAttr(this.methAnnotAttrVis);
        }
        if (this.methAnnotAttrInvis != null) {
            this.class_env.requireJava5();
            this.currentMethod.addInvisAnnotationAttr(this.methAnnotAttrInvis);
        }
        if (this.methParamAnnotAttrVis != null) {
            this.class_env.requireJava5();
            this.currentMethod.addVisParamAnnotationAttr(this.methParamAnnotAttrVis);
        }
        if (this.methParamAnnotAttrInvis != null) {
            this.class_env.requireJava5();
            this.currentMethod.addInvisParamAnnotationAttr(this.methParamAnnotAttrInvis);
        }
        if (this.methAnnotDef != null) {
            this.class_env.requireJava5();
            this.methAnnotDef.setNoName();
            this.currentMethod.addAnnotationDef(new AnnotationDefaultAttr(this.methAnnotDef));
        }
        this.class_env.addMethod(this.currentMethod);
        this.code = null;
        this.labels = null;
        this.method_name = null;
        this.code = null;
        this.except_attr = null;
        this.catch_table = null;
        this.line_table = null;
        this.var_table = null;
        this.methSynth = false;
        this.methDepr = false;
        this.methSigAttr = null;
        this.methAnnotAttrVis = null;
        this.methParamAnnotAttrVis = null;
        this.methAnnotDef = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name) throws jasError {
        InsnInfo insn = InsnInfo.get(name);
        autoNumber();
        if (insn.args.equals("")) {
            Insn inst = new Insn(insn.opcode);
            _getCode().addInsn(inst);
        } else if (!insn.name.equals("wide")) {
            throw new jasError("Missing arguments for instruction " + name);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, int v1, int v2) throws jasError {
        autoNumber();
        if (name.equals("iinc")) {
            Insn inst = new IincInsn(v1, v2);
            _getCode().addInsn(inst);
            return;
        }
        throw new jasError("Bad arguments for instruction " + name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, int val) throws jasError {
        Insn inst;
        InsnInfo insn = InsnInfo.get(name);
        CodeAttr code = _getCode();
        autoNumber();
        if (insn.args.equals("i")) {
            inst = new Insn(insn.opcode, val);
        } else if (insn.args.equals("constant")) {
            inst = new Insn(insn.opcode, new IntegerCP(val));
        } else if (insn.args.equals("bigconstant")) {
            inst = new Insn(insn.opcode, new LongCP(val));
        } else {
            throw new jasError("Bad arguments for instruction " + name);
        }
        code.addInsn(inst);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, Number val) throws jasError {
        InsnInfo insn = InsnInfo.get(name);
        _getCode();
        autoNumber();
        Insn inst = null;
        if (insn.args.equals("i") && (val instanceof Integer)) {
            inst = new Insn(insn.opcode, val.intValue());
        } else if (insn.args.equals("constant")) {
            if ((val instanceof Integer) || (val instanceof Long)) {
                inst = new Insn(insn.opcode, new IntegerCP(val.intValue()));
            } else if ((val instanceof Float) || (val instanceof Double)) {
                inst = new Insn(insn.opcode, new FloatCP(val.floatValue()));
            }
        } else if (insn.args.equals("bigconstant")) {
            if ((val instanceof Integer) || (val instanceof Long)) {
                inst = new Insn(insn.opcode, new LongCP(val.longValue()));
            } else if ((val instanceof Float) || (val instanceof Double)) {
                inst = new Insn(insn.opcode, new DoubleCP(val.doubleValue()));
            }
        } else {
            throw new jasError("Bad arguments for instruction " + name);
        }
        _getCode().addInsn(inst);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plantString(String name, String val) throws jasError {
        InsnInfo insn = InsnInfo.get(name);
        autoNumber();
        if (insn.args.equals("constant")) {
            Insn inst = new Insn(insn.opcode, new StringCP(val));
            this.code.addInsn(inst);
            return;
        }
        throw new jasError("Bad arguments for instruction " + name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, String val, int nargs) throws jasError {
        Insn inst;
        InsnInfo insn = InsnInfo.get(name);
        CodeAttr code = _getCode();
        autoNumber();
        if (insn.args.equals("interface")) {
            String[] split = ScannerUtils.splitClassMethodSignature(val);
            inst = new InvokeinterfaceInsn(new InterfaceCP(split[0], split[1], split[2]), nargs);
        } else if (insn.args.equals("marray")) {
            inst = new MultiarrayInsn(new ClassCP(val), nargs);
        } else {
            throw new jasError("Bad arguments for instruction " + name);
        }
        code.addInsn(inst);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, String val) throws jasError {
        Insn inst;
        int atype;
        InsnInfo insn = InsnInfo.get(name);
        CodeAttr code = _getCode();
        autoNumber();
        if (insn.args.equals("method")) {
            String[] split = ScannerUtils.splitClassMethodSignature(val);
            inst = new Insn(insn.opcode, new MethodCP(split[0], split[1], split[2]));
        } else if (insn.args.equals("constant")) {
            inst = new Insn(insn.opcode, new ClassCP(val));
        } else if (insn.args.equals("atype")) {
            if (val.equals("boolean")) {
                atype = 4;
            } else if (val.equals("char")) {
                atype = 5;
            } else if (val.equals(Jimple.FLOAT)) {
                atype = 6;
            } else if (val.equals("double")) {
                atype = 7;
            } else if (val.equals("byte")) {
                atype = 8;
            } else if (val.equals("short")) {
                atype = 9;
            } else if (val.equals("int")) {
                atype = 10;
            } else if (val.equals("long")) {
                atype = 11;
            } else {
                throw new jasError("Bad array type: " + name);
            }
            inst = new Insn(insn.opcode, atype);
        } else if (insn.args.equals("label")) {
            inst = new Insn(insn.opcode, getLabel(val));
        } else if (insn.args.equals("class")) {
            inst = new Insn(insn.opcode, new ClassCP(val));
        } else {
            throw new jasError("Bad arguments for instruction " + name);
        }
        code.addInsn(inst);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, String v1, String v2) throws jasError {
        InsnInfo info = InsnInfo.get(name);
        CodeAttr code = _getCode();
        autoNumber();
        if (info.args.equals("field")) {
            String[] split = ScannerUtils.splitClassField(v1);
            Insn inst = new Insn(info.opcode, new FieldCP(split[0], split[1], v2));
            code.addInsn(inst);
            return;
        }
        throw new jasError("Bad arguments for instruction " + name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plant(String name, String v1, String v2, String v3) throws jasError {
        CP[] argCPs;
        CP integerCP;
        InsnInfo info = InsnInfo.get(name);
        CodeAttr code = _getCode();
        autoNumber();
        if (name.equals("invokedynamic")) {
            this.class_env.requireJava7();
            String bsmNameAndSig = v3.substring(0, v3.indexOf("Ljava/lang/invoke/CallSite;(") + "Ljava/lang/invoke/CallSite;".length());
            String bsmName = bsmNameAndSig.substring(0, bsmNameAndSig.indexOf("("));
            String bsmClassName = bsmName.substring(0, bsmName.lastIndexOf("/"));
            String bsmMethodName = bsmName.substring(bsmName.lastIndexOf("/") + 1);
            String bsmSig = bsmNameAndSig.substring(bsmNameAndSig.indexOf("("));
            String bsmArgs = v3.substring(v3.indexOf("Ljava/lang/invoke/CallSite;(") + "Ljava/lang/invoke/CallSite;(".length(), v3.length() - 1);
            String[] bsmArgsList = bsmArgs.split(",");
            if (!bsmArgs.isEmpty()) {
                argCPs = new CP[bsmArgsList.length];
                for (int i = 0; i < bsmArgsList.length; i++) {
                    String sig = bsmArgsList[i].substring(1, bsmArgsList[i].indexOf(")"));
                    String val = unescape(bsmArgsList[i].substring(bsmArgsList[i].indexOf(")") + 1));
                    if (sig.equals("I") || sig.equals("Z") || sig.equals("C") || sig.equals("B") || sig.equals("S")) {
                        integerCP = new IntegerCP(Integer.parseInt(val));
                    } else if (sig.equals("F")) {
                        integerCP = new FloatCP(Float.parseFloat(val));
                    } else if (sig.equals("D")) {
                        integerCP = new DoubleCP(Double.parseDouble(val));
                    } else if (sig.equals("J")) {
                        integerCP = new LongCP(Long.parseLong(val));
                    } else if (sig.equals("Ljava/lang/String;")) {
                        integerCP = new StringCP(val);
                    } else if (sig.equals("Ljava/lang/Class;")) {
                        integerCP = new ClassCP(val);
                    } else {
                        throw new UnsupportedOperationException("static argument type not currently supported: " + sig);
                    }
                    CP cp = integerCP;
                    argCPs[i] = cp;
                }
            } else {
                argCPs = new CP[0];
            }
            int index = this.class_env.addBootstrapMethod(new MethodHandleCP(6, bsmClassName, bsmMethodName, bsmSig), argCPs);
            Insn inst = new Insn(info.opcode, new InvokeDynamicCP(bsmClassName, bsmMethodName, bsmSig, v1, v2, index));
            code.addInsn(inst);
            return;
        }
        throw new jasError("Bad arguments for instruction " + name);
    }

    private String unescape(String val) {
        return val.replace("\\comma", ",").replace("\\blank", Instruction.argsep).replace("\\tab", "\t").replace("\\newline", "\n");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newLookupswitch() throws jasError {
        this.switch_vec = new Vector();
        autoNumber();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addLookupswitch(int val, String label) throws jasError {
        this.switch_vec.addElement(new Integer(val));
        this.switch_vec.addElement(getLabel(label));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endLookupswitch(String deflabel) throws jasError {
        int n = this.switch_vec.size() >> 1;
        int[] offsets = new int[n];
        Label[] labels = new Label[n];
        Enumeration e = this.switch_vec.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            offsets[i] = ((Integer) e.nextElement()).intValue();
            labels[i] = (Label) e.nextElement();
            i++;
        }
        _getCode().addInsn(new LookupswitchInsn(getLabel(deflabel), offsets, labels));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newTableswitch(int lowval) throws jasError {
        newTableswitch(lowval, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newTableswitch(int lowval, int hival) throws jasError {
        this.switch_vec = new Vector();
        this.low_value = lowval;
        this.high_value = hival;
        autoNumber();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addTableswitch(String label) throws jasError {
        this.switch_vec.addElement(getLabel(label));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endTableswitch(String deflabel) throws jasError {
        int n = this.switch_vec.size();
        Label[] labels = new Label[n];
        Enumeration e = this.switch_vec.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            labels[i] = (Label) e.nextElement();
            i++;
        }
        if (this.high_value != -1 && this.high_value != (this.low_value + n) - 1) {
            report_error("tableswitch - given incorrect value for <high>");
        }
        _getCode().addInsn(new TableswitchInsn(this.low_value, (this.low_value + n) - 1, getLabel(deflabel), labels));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLine(int l) {
        this.line_num = l;
    }

    void autoNumber() throws jasError {
        if (this.auto_number) {
            addLineInfo(this.line_num);
        }
    }

    Label getLabel(String name) throws jasError {
        if (this.method_name == null) {
            throw new jasError("illegal use of label outside of method definition");
        }
        Label lab = (Label) this.labels.get(name);
        if (lab == null) {
            lab = new Label(name);
            this.labels.put(name, lab);
        }
        return lab;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void plantLabel(String name) throws jasError {
        _getCode().addInsn(getLabel(name));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addVar(String startLab, String endLab, String name, String sig, int var_num) throws jasError {
        if (startLab == null) {
            startLab = BGN_METHOD;
        }
        if (endLab == null) {
            endLab = END_METHOD;
        }
        Label slab = getLabel(startLab);
        Label elab = getLabel(endLab);
        if (this.var_table == null) {
            this.var_table = new LocalVarTableAttr();
        }
        this.var_table.addEntry(new LocalVarEntry(slab, elab, name, sig, var_num));
    }

    void addLineInfo(int line_num) throws jasError {
        StringBuilder append = new StringBuilder().append("L:");
        int i = this.line_label_count;
        this.line_label_count = i + 1;
        String l = append.append(i).toString();
        if (this.line_table == null) {
            this.line_table = new LineTableAttr();
        }
        plantLabel(l);
        this.line_table.addEntry(getLabel(l), line_num);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addLine(int line_num) throws jasError {
        if (!this.auto_number) {
            addLineInfo(line_num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addThrow(String name) throws jasError {
        if (this.method_name == null) {
            throw new jasError("illegal use of .throw outside of method definition");
        }
        if (this.except_attr == null) {
            this.except_attr = new ExceptAttr();
        }
        this.except_attr.addException(new ClassCP(name));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCatch(String name, String start_lab, String end_lab, String branch_lab) throws jasError {
        ClassCP class_cp;
        if (this.method_name == null) {
            throw new jasError("illegal use of .catch outside of method definition");
        }
        if (this.catch_table == null) {
            this.catch_table = new Catchtable();
        }
        if (name.equals("all")) {
            class_cp = null;
        } else {
            class_cp = new ClassCP(name);
        }
        this.catch_table.addEntry(getLabel(start_lab), getLabel(end_lab), getLabel(branch_lab), class_cp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStackSize(short v) throws jasError {
        _getCode().setStackSize(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVarSize(short v) throws jasError {
        _getCode().setVarSize(v);
    }

    CodeAttr _getCode() throws jasError {
        if (this.method_name == null) {
            throw new jasError("illegal use of instruction outside of method definition");
        }
        if (this.code == null) {
            this.code = new CodeAttr();
            plantLabel(BGN_METHOD);
        }
        return this.code;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addInnerClassAttr() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addInnerClassSpec(String inner_class_name, String outer_class_name, String inner_name, short access) {
        if (this.inner_class_attr == null) {
            this.inner_class_attr = new InnerClassAttr();
        }
        this.inner_class_attr.addInnerClassSpec(new InnerClassSpecAttr(inner_class_name, outer_class_name, inner_name, access));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endInnerClassAttr() {
        this.class_env.finishInnerClassAttr(this.inner_class_attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassSynthAttr() {
        this.class_env.setClassSynth(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethSynthAttr() {
        this.methSynth = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethDeprAttr() {
        this.methDepr = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethSigAttr(String s) {
        this.methSigAttr = s;
        if (s.contains("<")) {
            this.class_env.requireJava5();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addEnclMethAttr(String cls, String meth, String sig) {
        this.class_env.addEnclMethAttr(new EnclMethAttr(cls, meth, sig));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethAnnotAttrVisible(Object attr) {
        this.methAnnotAttrVis = (VisibilityAnnotationAttr) attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethAnnotAttrInvisible(Object attr) {
        this.methAnnotAttrInvis = (VisibilityAnnotationAttr) attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethParamAnnotAttrVisible(Object attr) {
        this.methParamAnnotAttrVis = (ParameterVisibilityAnnotationAttr) attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethParamAnnotAttrInvisible(Object attr) {
        this.methParamAnnotAttrInvis = (ParameterVisibilityAnnotationAttr) attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMethAnnotDefault(Object attr) {
        this.methAnnotDef = (ElemValPair) attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VisibilityAnnotationAttr makeVisibilityAnnotation(Object tval, Object list) {
        return new VisibilityAnnotationAttr((String) tval, (ArrayList) list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParameterVisibilityAnnotationAttr makeParameterVisibilityAnnotation(Object kind, Object list) {
        return new ParameterVisibilityAnnotationAttr(((String) kind) + "Parameter", (ArrayList) list);
    }

    void endVisibilityAnnotation() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList makeNewAnnotAttrList(Object annot) {
        ArrayList list = new ArrayList();
        list.add(annot);
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList mergeNewAnnotAttr(Object list, Object elem) {
        ((ArrayList) list).add(elem);
        return (ArrayList) list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList makeNewAnnotationList(Object elem) {
        ArrayList list = new ArrayList();
        list.add(elem);
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList mergeNewAnnotation(Object list, Object elem) {
        ((ArrayList) list).add(elem);
        return (ArrayList) list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationAttr makeAnnotation(String type, Object elems) {
        return new AnnotationAttr(type, (ArrayList) elems);
    }

    void endAnnotation() {
        if (this.vis_annot_attr == null) {
            this.vis_annot_attr = new VisibilityAnnotationAttr();
        }
        this.vis_annot_attr.addAnnotation(this.currAnn);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList makeNewElemValPairList(Object elem) {
        ArrayList list = new ArrayList();
        list.add(elem);
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList mergeNewElemValPair(Object list, Object elem) {
        ((ArrayList) list).add(elem);
        return (ArrayList) list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElemValPair makeConstantElem(String name, char kind, Object val) {
        ElemValPair result = null;
        switch (kind) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                result = new IntElemValPair(name, kind, ((Integer) val).intValue());
                break;
            case 'D':
                result = new DoubleElemValPair(name, kind, ((Number) val).doubleValue());
                break;
            case 'F':
                result = new FloatElemValPair(name, kind, ((Number) val).floatValue());
                break;
            case 'J':
                result = new LongElemValPair(name, kind, ((Number) val).longValue());
                break;
            case 's':
                result = new StringElemValPair(name, kind, (String) val);
                break;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElemValPair makeEnumElem(String name, char kind, String tval, String cval) {
        return new EnumElemValPair(name, kind, tval, cval);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElemValPair makeClassElem(String name, char kind, String cval) {
        return new ClassElemValPair(name, kind, cval);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElemValPair makeAnnotElem(String name, char kind, Object attr) {
        return new AnnotElemValPair(name, kind, (AnnotationAttr) attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElemValPair makeArrayElem(String name, char kind, Object list) {
        ArrayElemValPair elem = new ArrayElemValPair(name, kind, (ArrayList) list);
        elem.setNoName();
        return elem;
    }

    void endAnnotElem() {
    }

    void endArrayElem() {
    }

    public void readJasmin(InputStream input, String name, boolean numberLines) throws IOException, Exception {
        this.errors = 0;
        this.filename = name;
        this.auto_number = numberLines;
        this.class_env = new ClassEnv();
        this.scanner = new Scanner(input);
        parser parse_obj = new parser(this, this.scanner);
        parse_obj.parse();
    }

    public int errorCount() {
        return this.errors;
    }

    public String getClassName() {
        return this.class_name;
    }

    public void write(OutputStream outp) throws IOException, jasError {
        this.class_env.setSource(this.source_name);
        this.class_env.write(new DataOutputStream(outp));
    }
}
