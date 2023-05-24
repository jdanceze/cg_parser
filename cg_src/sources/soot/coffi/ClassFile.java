package soot.coffi;

import android.widget.ExpandableListView;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.jimple.Jimple;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/coffi/ClassFile.class */
public class ClassFile {
    private static final Logger logger;
    static final long MAGIC = 3405691582L;
    static final short ACC_PUBLIC = 1;
    static final short ACC_PRIVATE = 2;
    static final short ACC_PROTECTED = 4;
    static final short ACC_STATIC = 8;
    static final short ACC_FINAL = 16;
    static final short ACC_SUPER = 32;
    static final short ACC_VOLATILE = 64;
    static final short ACC_TRANSIENT = 128;
    static final short ACC_INTERFACE = 512;
    static final short ACC_ABSTRACT = 1024;
    static final short ACC_STRICT = 2048;
    static final short ACC_ANNOTATION = 8192;
    static final short ACC_ENUM = 16384;
    static final short ACC_UNKNOWN = 28672;
    static final String DESC_BYTE = "B";
    static final String DESC_CHAR = "C";
    static final String DESC_DOUBLE = "D";
    static final String DESC_FLOAT = "F";
    static final String DESC_INT = "I";
    static final String DESC_LONG = "J";
    static final String DESC_OBJECT = "L";
    static final String DESC_SHORT = "S";
    static final String DESC_BOOLEAN = "Z";
    static final String DESC_VOID = "V";
    static final String DESC_ARRAY = "[";
    boolean debug;
    String fn;
    long magic;
    int minor_version;
    int major_version;
    public int constant_pool_count;
    public cp_info[] constant_pool;
    public int access_flags;
    public int this_class;
    public int super_class;
    public int interfaces_count;
    public int[] interfaces;
    public int fields_count;
    public field_info[] fields;
    public int methods_count;
    public method_info[] methods;
    public int attributes_count;
    public attribute_info[] attributes;
    public BootstrapMethods_attribute bootstrap_methods_attribute;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ClassFile.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(ClassFile.class);
    }

    public ClassFile(String nfn) {
        this.fn = nfn;
    }

    public String toString() {
        return this.constant_pool[this.this_class].toString(this.constant_pool);
    }

    public boolean loadClassFile(InputStream is) {
        InputStream f = null;
        if (Options.v().time()) {
            Timers.v().readTimer.start();
        }
        try {
            DataInputStream classDataStream = new DataInputStream(is);
            byte[] data = new byte[classDataStream.available()];
            classDataStream.readFully(data);
            f = new ByteArrayInputStream(data);
        } catch (IOException e) {
            logger.debug(e.getMessage(), (Throwable) e);
        }
        if (Options.v().time()) {
            Timers.v().readTimer.end();
        }
        DataInputStream d = new DataInputStream(f);
        boolean b = readClass(d);
        try {
            is.close();
            d.close();
            if (f != null) {
                f.close();
            }
            if (!b) {
                return false;
            }
            return true;
        } catch (IOException e2) {
            logger.debug("IOException with " + this.fn + ": " + e2.getMessage());
            return false;
        }
    }

    boolean saveClassFile() {
        FileOutputStream f;
        try {
            f = new FileOutputStream(this.fn);
        } catch (FileNotFoundException e) {
            if (this.fn.indexOf(".class") >= 0) {
                logger.debug("Can't find " + this.fn);
                return false;
            }
            this.fn = String.valueOf(this.fn) + ".class";
            try {
                f = new FileOutputStream(this.fn);
            } catch (FileNotFoundException e2) {
                logger.debug("Can't find " + this.fn);
                return false;
            }
        }
        DataOutputStream d = new DataOutputStream(f);
        boolean b = writeClass(d);
        try {
            d.close();
            f.close();
            return b;
        } catch (IOException e3) {
            logger.debug("IOException with " + this.fn + ": " + e3.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String access_string(int af, String separator) {
        boolean hasone = false;
        String s = "";
        if ((af & 1) != 0) {
            s = Jimple.PUBLIC;
            hasone = true;
        }
        if ((af & 2) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.PRIVATE;
        }
        if ((af & 4) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.PROTECTED;
        }
        if ((af & 8) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.STATIC;
        }
        if ((af & 16) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.FINAL;
        }
        if ((af & 32) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + "super";
        }
        if ((af & 64) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.VOLATILE;
        }
        if ((af & 128) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.TRANSIENT;
        }
        if ((af & 512) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + "interface";
        }
        if ((af & 1024) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.ABSTRACT;
        }
        if ((af & 2048) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + "strict";
        }
        if ((af & 8192) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + Jimple.ANNOTATION;
        }
        if ((af & 16384) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            } else {
                hasone = true;
            }
            s = String.valueOf(s) + "enum";
        }
        if ((af & 28672) != 0) {
            if (hasone) {
                s = String.valueOf(s) + separator;
            }
            s = String.valueOf(s) + "unknown";
        }
        return s;
    }

    public boolean readClass(DataInputStream d) {
        try {
            this.magic = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
            if (this.magic != MAGIC) {
                logger.debug("Wrong magic number in " + this.fn + ": " + this.magic);
                return false;
            }
            this.minor_version = d.readUnsignedShort();
            this.major_version = d.readUnsignedShort();
            this.constant_pool_count = d.readUnsignedShort();
            if (!readConstantPool(d)) {
                return false;
            }
            this.access_flags = d.readUnsignedShort();
            this.this_class = d.readUnsignedShort();
            this.super_class = d.readUnsignedShort();
            this.interfaces_count = d.readUnsignedShort();
            if (this.interfaces_count > 0) {
                this.interfaces = new int[this.interfaces_count];
                for (int j = 0; j < this.interfaces_count; j++) {
                    this.interfaces[j] = d.readUnsignedShort();
                }
            }
            if (Options.v().time()) {
                Timers.v().fieldTimer.start();
            }
            this.fields_count = d.readUnsignedShort();
            readFields(d);
            if (Options.v().time()) {
                Timers.v().fieldTimer.end();
            }
            if (Options.v().time()) {
                Timers.v().methodTimer.start();
            }
            this.methods_count = d.readUnsignedShort();
            readMethods(d);
            if (Options.v().time()) {
                Timers.v().methodTimer.end();
            }
            if (Options.v().time()) {
                Timers.v().attributeTimer.start();
            }
            this.attributes_count = d.readUnsignedShort();
            if (this.attributes_count > 0) {
                this.attributes = new attribute_info[this.attributes_count];
                readAttributes(d, this.attributes_count, this.attributes);
            }
            if (Options.v().time()) {
                Timers.v().attributeTimer.end();
                return true;
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException with " + this.fn + ": " + e.getMessage(), e);
        }
    }

    protected boolean readConstantPool(DataInputStream d) throws IOException {
        cp_info cp;
        this.constant_pool = new cp_info[this.constant_pool_count];
        boolean skipone = false;
        for (int i = 1; i < this.constant_pool_count; i++) {
            if (skipone) {
                skipone = false;
            } else {
                byte tag = (byte) d.readUnsignedByte();
                switch (tag) {
                    case 1:
                        CONSTANT_Utf8_info cputf8 = new CONSTANT_Utf8_info(d);
                        cp = CONSTANT_Utf8_collector.v().add(cputf8);
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Utf8 = \"" + cputf8.convert() + "\"");
                            break;
                        }
                        break;
                    case 2:
                    case 13:
                    case 14:
                    case 16:
                    case 17:
                    default:
                        logger.debug("Unknown tag in constant pool: " + ((int) tag) + " at entry " + i);
                        return false;
                    case 3:
                        cp = new CONSTANT_Integer_info();
                        ((CONSTANT_Integer_info) cp).bytes = d.readInt();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Integer = " + ((CONSTANT_Integer_info) cp).bytes);
                            break;
                        }
                        break;
                    case 4:
                        cp = new CONSTANT_Float_info();
                        ((CONSTANT_Float_info) cp).bytes = d.readInt();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Float = " + ((CONSTANT_Float_info) cp).convert());
                            break;
                        }
                        break;
                    case 5:
                        cp = new CONSTANT_Long_info();
                        ((CONSTANT_Long_info) cp).high = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
                        ((CONSTANT_Long_info) cp).low = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
                        if (this.debug) {
                            String temp = cp.toString(this.constant_pool);
                            logger.debug("Constant pool[" + i + "]: Long = " + temp);
                        }
                        skipone = true;
                        break;
                    case 6:
                        cp = new CONSTANT_Double_info();
                        ((CONSTANT_Double_info) cp).high = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
                        ((CONSTANT_Double_info) cp).low = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Double = " + ((CONSTANT_Double_info) cp).convert());
                        }
                        skipone = true;
                        break;
                    case 7:
                        cp = new CONSTANT_Class_info();
                        ((CONSTANT_Class_info) cp).name_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Class");
                            break;
                        }
                        break;
                    case 8:
                        cp = new CONSTANT_String_info();
                        ((CONSTANT_String_info) cp).string_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: String");
                            break;
                        }
                        break;
                    case 9:
                        cp = new CONSTANT_Fieldref_info();
                        ((CONSTANT_Fieldref_info) cp).class_index = d.readUnsignedShort();
                        ((CONSTANT_Fieldref_info) cp).name_and_type_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Fieldref");
                            break;
                        }
                        break;
                    case 10:
                        cp = new CONSTANT_Methodref_info();
                        ((CONSTANT_Methodref_info) cp).class_index = d.readUnsignedShort();
                        ((CONSTANT_Methodref_info) cp).name_and_type_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Methodref");
                            break;
                        }
                        break;
                    case 11:
                        cp = new CONSTANT_InterfaceMethodref_info();
                        ((CONSTANT_InterfaceMethodref_info) cp).class_index = d.readUnsignedShort();
                        ((CONSTANT_InterfaceMethodref_info) cp).name_and_type_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: MethodHandle");
                            break;
                        }
                        break;
                    case 12:
                        cp = new CONSTANT_NameAndType_info();
                        ((CONSTANT_NameAndType_info) cp).name_index = d.readUnsignedShort();
                        ((CONSTANT_NameAndType_info) cp).descriptor_index = d.readUnsignedShort();
                        if (this.debug) {
                            logger.debug("Constant pool[" + i + "]: Name and Type");
                            break;
                        }
                        break;
                    case 15:
                        cp = new CONSTANT_MethodHandle_info();
                        ((CONSTANT_MethodHandle_info) cp).kind = d.readByte();
                        ((CONSTANT_MethodHandle_info) cp).target_index = d.readUnsignedShort();
                        break;
                    case 18:
                        cp = new CONSTANT_InvokeDynamic_info();
                        ((CONSTANT_InvokeDynamic_info) cp).bootstrap_method_index = d.readUnsignedShort();
                        ((CONSTANT_InvokeDynamic_info) cp).name_and_type_index = d.readUnsignedShort();
                        break;
                }
                cp.tag = tag;
                this.constant_pool[i] = cp;
            }
        }
        return true;
    }

    private void readAllBytes(byte[] dest, DataInputStream d) throws IOException {
        int total_len = dest.length;
        int i = 0;
        while (true) {
            int read_len = i;
            if (read_len < total_len) {
                int to_read = total_len - read_len;
                int curr_read = d.read(dest, read_len, to_read);
                i = read_len + curr_read;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v32, types: [short[], short[][]] */
    protected boolean readAttributes(DataInputStream d, int attributes_count, attribute_info[] ai) throws IOException {
        attribute_info attribute_infoVar;
        for (int i = 0; i < attributes_count; i++) {
            int j = d.readUnsignedShort();
            long len = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
            String s = ((CONSTANT_Utf8_info) this.constant_pool[j]).convert();
            if (s.compareTo("SourceFile") == 0) {
                SourceFile_attribute sa = new SourceFile_attribute();
                sa.sourcefile_index = d.readUnsignedShort();
                attribute_infoVar = sa;
            } else if (s.compareTo("ConstantValue") == 0) {
                ConstantValue_attribute ca = new ConstantValue_attribute();
                ca.constantvalue_index = d.readUnsignedShort();
                attribute_infoVar = ca;
            } else if (s.compareTo("Code") == 0) {
                Code_attribute ca2 = new Code_attribute();
                ca2.max_stack = d.readUnsignedShort();
                ca2.max_locals = d.readUnsignedShort();
                ca2.code_length = d.readInt() & ExpandableListView.PACKED_POSITION_VALUE_NULL;
                ca2.code = new byte[(int) ca2.code_length];
                readAllBytes(ca2.code, d);
                ca2.exception_table_length = d.readUnsignedShort();
                ca2.exception_table = new exception_table_entry[ca2.exception_table_length];
                for (int k = 0; k < ca2.exception_table_length; k++) {
                    exception_table_entry e = new exception_table_entry();
                    e.start_pc = d.readUnsignedShort();
                    e.end_pc = d.readUnsignedShort();
                    e.handler_pc = d.readUnsignedShort();
                    e.catch_type = d.readUnsignedShort();
                    ca2.exception_table[k] = e;
                }
                ca2.attributes_count = d.readUnsignedShort();
                ca2.attributes = new attribute_info[ca2.attributes_count];
                readAttributes(d, ca2.attributes_count, ca2.attributes);
                attribute_infoVar = ca2;
            } else if (s.compareTo("Exceptions") == 0) {
                Exception_attribute ea = new Exception_attribute();
                ea.number_of_exceptions = d.readUnsignedShort();
                if (ea.number_of_exceptions > 0) {
                    ea.exception_index_table = new int[ea.number_of_exceptions];
                    for (int k2 = 0; k2 < ea.number_of_exceptions; k2++) {
                        ea.exception_index_table[k2] = d.readUnsignedShort();
                    }
                }
                attribute_infoVar = ea;
            } else if (s.compareTo("LineNumberTable") == 0) {
                LineNumberTable_attribute la = new LineNumberTable_attribute();
                la.line_number_table_length = d.readUnsignedShort();
                la.line_number_table = new line_number_table_entry[la.line_number_table_length];
                for (int k3 = 0; k3 < la.line_number_table_length; k3++) {
                    line_number_table_entry e2 = new line_number_table_entry();
                    e2.start_pc = d.readUnsignedShort();
                    e2.line_number = d.readUnsignedShort();
                    la.line_number_table[k3] = e2;
                }
                attribute_infoVar = la;
            } else if (s.compareTo("LocalVariableTable") == 0) {
                LocalVariableTable_attribute la2 = new LocalVariableTable_attribute();
                la2.local_variable_table_length = d.readUnsignedShort();
                la2.local_variable_table = new local_variable_table_entry[la2.local_variable_table_length];
                for (int k4 = 0; k4 < la2.local_variable_table_length; k4++) {
                    local_variable_table_entry e3 = new local_variable_table_entry();
                    e3.start_pc = d.readUnsignedShort();
                    e3.length = d.readUnsignedShort();
                    e3.name_index = d.readUnsignedShort();
                    e3.descriptor_index = d.readUnsignedShort();
                    e3.index = d.readUnsignedShort();
                    la2.local_variable_table[k4] = e3;
                }
                attribute_infoVar = la2;
            } else if (s.compareTo("LocalVariableTypeTable") == 0) {
                LocalVariableTypeTable_attribute la3 = new LocalVariableTypeTable_attribute();
                la3.local_variable_type_table_length = d.readUnsignedShort();
                la3.local_variable_type_table = new local_variable_type_table_entry[la3.local_variable_type_table_length];
                for (int k5 = 0; k5 < la3.local_variable_type_table_length; k5++) {
                    local_variable_type_table_entry e4 = new local_variable_type_table_entry();
                    e4.start_pc = d.readUnsignedShort();
                    e4.length = d.readUnsignedShort();
                    e4.name_index = d.readUnsignedShort();
                    e4.signature_index = d.readUnsignedShort();
                    e4.index = d.readUnsignedShort();
                    la3.local_variable_type_table[k5] = e4;
                }
                attribute_infoVar = la3;
            } else if (s.compareTo("Synthetic") == 0) {
                attribute_infoVar = new Synthetic_attribute();
            } else if (s.compareTo("Signature") == 0) {
                Signature_attribute ia = new Signature_attribute();
                ia.signature_index = d.readUnsignedShort();
                attribute_infoVar = ia;
            } else if (s.compareTo("Deprecated") == 0) {
                attribute_infoVar = new Deprecated_attribute();
            } else if (s.compareTo("EnclosingMethod") == 0) {
                EnclosingMethod_attribute ea2 = new EnclosingMethod_attribute();
                ea2.class_index = d.readUnsignedShort();
                ea2.method_index = d.readUnsignedShort();
                attribute_infoVar = ea2;
            } else if (s.compareTo("InnerClasses") == 0) {
                InnerClasses_attribute ia2 = new InnerClasses_attribute();
                ia2.inner_classes_length = d.readUnsignedShort();
                ia2.inner_classes = new inner_class_entry[ia2.inner_classes_length];
                for (int k6 = 0; k6 < ia2.inner_classes_length; k6++) {
                    inner_class_entry e5 = new inner_class_entry();
                    e5.inner_class_index = d.readUnsignedShort();
                    e5.outer_class_index = d.readUnsignedShort();
                    e5.name_index = d.readUnsignedShort();
                    e5.access_flags = d.readUnsignedShort();
                    ia2.inner_classes[k6] = e5;
                }
                attribute_infoVar = ia2;
            } else if (s.compareTo("RuntimeVisibleAnnotations") == 0) {
                RuntimeVisibleAnnotations_attribute ra = new RuntimeVisibleAnnotations_attribute();
                ra.number_of_annotations = d.readUnsignedShort();
                ra.annotations = new annotation[ra.number_of_annotations];
                for (int k7 = 0; k7 < ra.number_of_annotations; k7++) {
                    annotation annot = new annotation();
                    annot.type_index = d.readUnsignedShort();
                    annot.num_element_value_pairs = d.readUnsignedShort();
                    annot.element_value_pairs = readElementValues(annot.num_element_value_pairs, d, true, 0);
                    ra.annotations[k7] = annot;
                }
                attribute_infoVar = ra;
            } else if (s.compareTo("RuntimeInvisibleAnnotations") == 0) {
                RuntimeInvisibleAnnotations_attribute ra2 = new RuntimeInvisibleAnnotations_attribute();
                ra2.number_of_annotations = d.readUnsignedShort();
                ra2.annotations = new annotation[ra2.number_of_annotations];
                for (int k8 = 0; k8 < ra2.number_of_annotations; k8++) {
                    annotation annot2 = new annotation();
                    annot2.type_index = d.readUnsignedShort();
                    annot2.num_element_value_pairs = d.readUnsignedShort();
                    annot2.element_value_pairs = readElementValues(annot2.num_element_value_pairs, d, true, 0);
                    ra2.annotations[k8] = annot2;
                }
                attribute_infoVar = ra2;
            } else if (s.compareTo("RuntimeVisibleParameterAnnotations") == 0) {
                RuntimeVisibleParameterAnnotations_attribute ra3 = new RuntimeVisibleParameterAnnotations_attribute();
                ra3.num_parameters = d.readUnsignedByte();
                ra3.parameter_annotations = new parameter_annotation[ra3.num_parameters];
                for (int x = 0; x < ra3.num_parameters; x++) {
                    parameter_annotation pAnnot = new parameter_annotation();
                    pAnnot.num_annotations = d.readUnsignedShort();
                    pAnnot.annotations = new annotation[pAnnot.num_annotations];
                    for (int k9 = 0; k9 < pAnnot.num_annotations; k9++) {
                        annotation annot3 = new annotation();
                        annot3.type_index = d.readUnsignedShort();
                        annot3.num_element_value_pairs = d.readUnsignedShort();
                        annot3.element_value_pairs = readElementValues(annot3.num_element_value_pairs, d, true, 0);
                        pAnnot.annotations[k9] = annot3;
                    }
                    ra3.parameter_annotations[x] = pAnnot;
                }
                attribute_infoVar = ra3;
            } else if (s.compareTo("RuntimeInvisibleParameterAnnotations") == 0) {
                RuntimeInvisibleParameterAnnotations_attribute ra4 = new RuntimeInvisibleParameterAnnotations_attribute();
                ra4.num_parameters = d.readUnsignedByte();
                ra4.parameter_annotations = new parameter_annotation[ra4.num_parameters];
                for (int x2 = 0; x2 < ra4.num_parameters; x2++) {
                    parameter_annotation pAnnot2 = new parameter_annotation();
                    pAnnot2.num_annotations = d.readUnsignedShort();
                    pAnnot2.annotations = new annotation[pAnnot2.num_annotations];
                    for (int k10 = 0; k10 < pAnnot2.num_annotations; k10++) {
                        annotation annot4 = new annotation();
                        annot4.type_index = d.readUnsignedShort();
                        annot4.num_element_value_pairs = d.readUnsignedShort();
                        annot4.element_value_pairs = readElementValues(annot4.num_element_value_pairs, d, true, 0);
                        pAnnot2.annotations[k10] = annot4;
                    }
                    ra4.parameter_annotations[x2] = pAnnot2;
                }
                attribute_infoVar = ra4;
            } else if (s.compareTo("AnnotationDefault") == 0) {
                AnnotationDefault_attribute da = new AnnotationDefault_attribute();
                element_value[] result = readElementValues(1, d, false, 0);
                da.default_value = result[0];
                attribute_infoVar = da;
            } else if (s.equals("BootstrapMethods")) {
                BootstrapMethods_attribute bsma = new BootstrapMethods_attribute();
                int count = d.readUnsignedShort();
                bsma.method_handles = new short[count];
                bsma.arg_indices = new short[count];
                for (int num = 0; num < count; num++) {
                    short index = (short) d.readUnsignedShort();
                    bsma.method_handles[num] = index;
                    int argCount = d.readUnsignedShort();
                    bsma.arg_indices[num] = new short[argCount];
                    for (int numArg = 0; numArg < argCount; numArg++) {
                        short indexArg = (short) d.readUnsignedShort();
                        bsma.arg_indices[num][numArg] = indexArg;
                    }
                }
                if (!$assertionsDisabled && this.bootstrap_methods_attribute != null) {
                    throw new AssertionError("More than one bootstrap methods attribute!");
                }
                attribute_infoVar = bsma;
                this.bootstrap_methods_attribute = bsma;
            } else {
                Generic_attribute ga = new Generic_attribute();
                if (len > 0) {
                    ga.info = new byte[(int) len];
                    readAllBytes(ga.info, d);
                }
                attribute_infoVar = ga;
            }
            attribute_info a = attribute_infoVar;
            a.attribute_name = j;
            a.attribute_length = len;
            ai[i] = a;
        }
        return true;
    }

    private element_value[] readElementValues(int count, DataInputStream d, boolean needName, int name_index) throws IOException {
        element_value[] list = new element_value[count];
        for (int x = 0; x < count; x++) {
            if (needName) {
                name_index = d.readUnsignedShort();
            }
            int tag = d.readUnsignedByte();
            char kind = (char) tag;
            if (kind == 'B' || kind == 'C' || kind == 'D' || kind == 'F' || kind == 'I' || kind == 'J' || kind == 'S' || kind == 'Z' || kind == 's') {
                constant_element_value elem = new constant_element_value();
                elem.name_index = name_index;
                elem.tag = kind;
                elem.constant_value_index = d.readUnsignedShort();
                list[x] = elem;
            } else if (kind == 'e') {
                enum_constant_element_value elem2 = new enum_constant_element_value();
                elem2.name_index = name_index;
                elem2.tag = kind;
                elem2.type_name_index = d.readUnsignedShort();
                elem2.constant_name_index = d.readUnsignedShort();
                list[x] = elem2;
            } else if (kind == 'c') {
                class_element_value elem3 = new class_element_value();
                elem3.name_index = name_index;
                elem3.tag = kind;
                elem3.class_info_index = d.readUnsignedShort();
                list[x] = elem3;
            } else if (kind == '[') {
                array_element_value elem4 = new array_element_value();
                elem4.name_index = name_index;
                elem4.tag = kind;
                elem4.num_values = d.readUnsignedShort();
                elem4.values = readElementValues(elem4.num_values, d, false, name_index);
                list[x] = elem4;
            } else if (kind == '@') {
                annotation_element_value elem5 = new annotation_element_value();
                elem5.name_index = name_index;
                elem5.tag = kind;
                annotation annot = new annotation();
                annot.type_index = d.readUnsignedShort();
                annot.num_element_value_pairs = d.readUnsignedShort();
                annot.element_value_pairs = readElementValues(annot.num_element_value_pairs, d, true, 0);
                elem5.annotation_value = annot;
                list[x] = elem5;
            } else {
                throw new RuntimeException("Unknown element value pair kind: " + kind);
            }
        }
        return list;
    }

    protected boolean readFields(DataInputStream d) throws IOException {
        this.fields = new field_info[this.fields_count];
        for (int i = 0; i < this.fields_count; i++) {
            field_info fi = new field_info();
            fi.access_flags = d.readUnsignedShort();
            fi.name_index = d.readUnsignedShort();
            fi.descriptor_index = d.readUnsignedShort();
            fi.attributes_count = d.readUnsignedShort();
            if (fi.attributes_count > 0) {
                fi.attributes = new attribute_info[fi.attributes_count];
                readAttributes(d, fi.attributes_count, fi.attributes);
            }
            this.fields[i] = fi;
        }
        return true;
    }

    protected boolean readMethods(DataInputStream d) throws IOException {
        this.methods = new method_info[this.methods_count];
        for (int i = 0; i < this.methods_count; i++) {
            method_info mi = new method_info();
            mi.access_flags = d.readUnsignedShort();
            mi.name_index = d.readUnsignedShort();
            mi.descriptor_index = d.readUnsignedShort();
            mi.attributes_count = d.readUnsignedShort();
            if (mi.attributes_count > 0) {
                mi.attributes = new attribute_info[mi.attributes_count];
                readAttributes(d, mi.attributes_count, mi.attributes);
                int j = 0;
                while (true) {
                    if (j < mi.attributes_count) {
                        if (!(mi.attributes[j] instanceof Code_attribute)) {
                            j++;
                        } else {
                            mi.code_attr = (Code_attribute) mi.attributes[j];
                            break;
                        }
                    }
                }
            }
            this.methods[i] = mi;
        }
        return true;
    }

    protected boolean writeConstantPool(DataOutputStream dd) throws IOException {
        boolean skipone = false;
        for (int i = 1; i < this.constant_pool_count; i++) {
            if (skipone) {
                skipone = false;
            } else {
                cp_info cp = this.constant_pool[i];
                dd.writeByte(cp.tag);
                switch (cp.tag) {
                    case 1:
                        ((CONSTANT_Utf8_info) cp).writeBytes(dd);
                        continue;
                    case 2:
                    default:
                        logger.debug("Unknown tag in constant pool: " + ((int) cp.tag));
                        return false;
                    case 3:
                        dd.writeInt((int) ((CONSTANT_Integer_info) cp).bytes);
                        continue;
                    case 4:
                        dd.writeInt((int) ((CONSTANT_Float_info) cp).bytes);
                        continue;
                    case 5:
                        dd.writeInt((int) ((CONSTANT_Long_info) cp).high);
                        dd.writeInt((int) ((CONSTANT_Long_info) cp).low);
                        skipone = true;
                        continue;
                    case 6:
                        dd.writeInt((int) ((CONSTANT_Double_info) cp).high);
                        dd.writeInt((int) ((CONSTANT_Double_info) cp).low);
                        skipone = true;
                        continue;
                    case 7:
                        dd.writeShort(((CONSTANT_Class_info) cp).name_index);
                        continue;
                    case 8:
                        dd.writeShort(((CONSTANT_String_info) cp).string_index);
                        continue;
                    case 9:
                        dd.writeShort(((CONSTANT_Fieldref_info) cp).class_index);
                        dd.writeShort(((CONSTANT_Fieldref_info) cp).name_and_type_index);
                        continue;
                    case 10:
                        dd.writeShort(((CONSTANT_Methodref_info) cp).class_index);
                        dd.writeShort(((CONSTANT_Methodref_info) cp).name_and_type_index);
                        continue;
                    case 11:
                        dd.writeShort(((CONSTANT_InterfaceMethodref_info) cp).class_index);
                        dd.writeShort(((CONSTANT_InterfaceMethodref_info) cp).name_and_type_index);
                        continue;
                    case 12:
                        dd.writeShort(((CONSTANT_NameAndType_info) cp).name_index);
                        dd.writeShort(((CONSTANT_NameAndType_info) cp).descriptor_index);
                        continue;
                }
            }
        }
        return true;
    }

    protected boolean writeAttributes(DataOutputStream dd, int attributes_count, attribute_info[] ai) throws IOException {
        for (int i = 0; i < attributes_count; i++) {
            attribute_info a = ai[i];
            dd.writeShort(a.attribute_name);
            dd.writeInt((int) a.attribute_length);
            if (a instanceof SourceFile_attribute) {
                SourceFile_attribute sa = (SourceFile_attribute) a;
                dd.writeShort(sa.sourcefile_index);
            } else if (a instanceof ConstantValue_attribute) {
                dd.writeShort(((ConstantValue_attribute) a).constantvalue_index);
            } else if (a instanceof Code_attribute) {
                Code_attribute ca = (Code_attribute) a;
                dd.writeShort(ca.max_stack);
                dd.writeShort(ca.max_locals);
                dd.writeInt((int) ca.code_length);
                dd.write(ca.code, 0, (int) ca.code_length);
                dd.writeShort(ca.exception_table_length);
                for (int k = 0; k < ca.exception_table_length; k++) {
                    exception_table_entry e = ca.exception_table[k];
                    dd.writeShort(e.start_pc);
                    dd.writeShort(e.end_pc);
                    dd.writeShort(e.handler_pc);
                    dd.writeShort(e.catch_type);
                }
                dd.writeShort(ca.attributes_count);
                if (ca.attributes_count > 0) {
                    writeAttributes(dd, ca.attributes_count, ca.attributes);
                }
            } else if (a instanceof Exception_attribute) {
                Exception_attribute ea = (Exception_attribute) a;
                dd.writeShort(ea.number_of_exceptions);
                if (ea.number_of_exceptions > 0) {
                    for (int k2 = 0; k2 < ea.number_of_exceptions; k2++) {
                        dd.writeShort(ea.exception_index_table[k2]);
                    }
                }
            } else if (a instanceof LineNumberTable_attribute) {
                LineNumberTable_attribute la = (LineNumberTable_attribute) a;
                dd.writeShort(la.line_number_table_length);
                for (int k3 = 0; k3 < la.line_number_table_length; k3++) {
                    line_number_table_entry e2 = la.line_number_table[k3];
                    dd.writeShort(e2.start_pc);
                    dd.writeShort(e2.line_number);
                }
            } else if (a instanceof LocalVariableTable_attribute) {
                LocalVariableTable_attribute la2 = (LocalVariableTable_attribute) a;
                dd.writeShort(la2.local_variable_table_length);
                for (int k4 = 0; k4 < la2.local_variable_table_length; k4++) {
                    local_variable_table_entry e3 = la2.local_variable_table[k4];
                    dd.writeShort(e3.start_pc);
                    dd.writeShort(e3.length);
                    dd.writeShort(e3.name_index);
                    dd.writeShort(e3.descriptor_index);
                    dd.writeShort(e3.index);
                }
            } else {
                logger.debug("Generic/Unknown Attribute in output");
                Generic_attribute ga = (Generic_attribute) a;
                if (ga.attribute_length > 0) {
                    dd.write(ga.info, 0, (int) ga.attribute_length);
                }
            }
        }
        return true;
    }

    protected boolean writeFields(DataOutputStream dd) throws IOException {
        for (int i = 0; i < this.fields_count; i++) {
            field_info fi = this.fields[i];
            dd.writeShort(fi.access_flags);
            dd.writeShort(fi.name_index);
            dd.writeShort(fi.descriptor_index);
            dd.writeShort(fi.attributes_count);
            if (fi.attributes_count > 0) {
                writeAttributes(dd, fi.attributes_count, fi.attributes);
            }
        }
        return true;
    }

    protected boolean writeMethods(DataOutputStream dd) throws IOException {
        for (int i = 0; i < this.methods_count; i++) {
            method_info mi = this.methods[i];
            dd.writeShort(mi.access_flags);
            dd.writeShort(mi.name_index);
            dd.writeShort(mi.descriptor_index);
            dd.writeShort(mi.attributes_count);
            if (mi.attributes_count > 0) {
                writeAttributes(dd, mi.attributes_count, mi.attributes);
            }
        }
        return true;
    }

    boolean writeClass(DataOutputStream dd) {
        try {
            dd.writeInt((int) this.magic);
            dd.writeShort(this.minor_version);
            dd.writeShort(this.major_version);
            dd.writeShort(this.constant_pool_count);
            if (!writeConstantPool(dd)) {
                return false;
            }
            dd.writeShort(this.access_flags);
            dd.writeShort(this.this_class);
            dd.writeShort(this.super_class);
            dd.writeShort(this.interfaces_count);
            if (this.interfaces_count > 0) {
                for (int j = 0; j < this.interfaces_count; j++) {
                    dd.writeShort(this.interfaces[j]);
                }
            }
            dd.writeShort(this.fields_count);
            writeFields(dd);
            dd.writeShort(this.methods_count);
            writeMethods(dd);
            dd.writeShort(this.attributes_count);
            if (this.attributes_count > 0) {
                writeAttributes(dd, this.attributes_count, this.attributes);
                return true;
            }
            return true;
        } catch (IOException e) {
            logger.debug("IOException with " + this.fn + ": " + e.getMessage());
            return false;
        }
    }

    public Instruction parseMethod(method_info m) {
        attribute_info[] attribute_infoVarArr;
        line_number_table_entry[] line_number_table_entryVarArr;
        Instruction head = null;
        Instruction tail = null;
        ByteCode bc = new ByteCode();
        Code_attribute ca = m.locate_code_attribute();
        if (ca == null) {
            return null;
        }
        int j = 0;
        while (j < ca.code_length) {
            Instruction inst = bc.disassemble_bytecode(ca.code, j);
            inst.originalIndex = j;
            if (inst instanceof Instruction_Unknown) {
                logger.debug("Unknown instruction in \"" + m.toName(this.constant_pool) + "\" at offset " + j);
                logger.debug(" bytecode = " + (inst.code & 255));
            }
            j = inst.nextOffset(j);
            if (head == null) {
                head = inst;
            } else {
                tail.next = inst;
                inst.prev = tail;
            }
            tail = inst;
        }
        bc.build(head);
        for (int j2 = 0; j2 < ca.exception_table_length; j2++) {
            exception_table_entry e = ca.exception_table[j2];
            e.start_inst = bc.locateInst(e.start_pc);
            if (e.end_pc == ca.code_length) {
                e.end_inst = null;
            } else {
                e.end_inst = bc.locateInst(e.end_pc);
            }
            e.handler_inst = bc.locateInst(e.handler_pc);
            if (e.handler_inst != null) {
                e.handler_inst.labelled = true;
            }
        }
        m.instructions = head;
        for (attribute_info element : ca.attributes) {
            if (element instanceof LineNumberTable_attribute) {
                LineNumberTable_attribute lntattr = (LineNumberTable_attribute) element;
                for (line_number_table_entry element0 : lntattr.line_number_table) {
                    element0.start_inst = bc.locateInst(element0.start_pc);
                }
            }
        }
        return head;
    }

    public void parse() {
        for (int i = 0; i < this.methods_count; i++) {
            method_info mi = this.methods[i];
            mi.instructions = parseMethod(mi);
        }
    }

    int relabel(Instruction i) {
        int index = 0;
        while (i != null) {
            i.label = index;
            index = i.nextOffset(index);
            i = i.next;
        }
        return index;
    }

    byte[] unparseMethod(method_info m) {
        m.cfg.reconstructInstructions();
        int codesize = relabel(m.instructions);
        byte[] bc = new byte[codesize];
        int codesize2 = 0;
        for (Instruction i = m.instructions; i != null; i = i.next) {
            codesize2 = i.compile(bc, codesize2);
        }
        if (codesize2 != bc.length) {
            logger.warn("code size doesn't match array length!");
        }
        return bc;
    }

    void unparse() {
        for (int i = 0; i < this.methods_count; i++) {
            method_info mi = this.methods[i];
            Code_attribute ca = mi.locate_code_attribute();
            if (ca != null) {
                byte[] bc = unparseMethod(mi);
                if (bc == null) {
                    logger.debug("Recompile of " + mi.toName(this.constant_pool) + " failed!");
                } else {
                    ca.code_length = bc.length;
                    ca.code = bc;
                    for (int j = 0; j < ca.exception_table_length; j++) {
                        exception_table_entry e = ca.exception_table[j];
                        e.start_pc = e.start_inst.label;
                        if (e.end_inst != null) {
                            e.end_pc = e.end_inst.label;
                        } else {
                            e.end_pc = (int) ca.code_length;
                        }
                        e.handler_pc = e.handler_inst.label;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String parseMethodDesc_return(String s) {
        int j = s.lastIndexOf(41);
        if (j >= 0) {
            return parseDesc(s.substring(j + 1), ",");
        }
        return parseDesc(s, ",");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String parseMethodDesc_params(String s) {
        int j;
        int i = s.indexOf(40);
        if (i >= 0 && (j = s.indexOf(41, i + 1)) >= 0) {
            return parseDesc(s.substring(i + 1, j), ",");
        }
        return "<parse error>";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String parseDesc(String desc, String sep) {
        String param;
        String params = "";
        int arraylevel = 0;
        boolean didone = false;
        int len = desc.length();
        int i = 0;
        while (i < len) {
            char c = desc.charAt(i);
            if (c == DESC_BYTE.charAt(0)) {
                param = "byte";
            } else if (c == DESC_CHAR.charAt(0)) {
                param = "char";
            } else if (c == DESC_DOUBLE.charAt(0)) {
                param = "double";
            } else if (c == DESC_FLOAT.charAt(0)) {
                param = Jimple.FLOAT;
            } else if (c == DESC_INT.charAt(0)) {
                param = "int";
            } else if (c == DESC_LONG.charAt(0)) {
                param = "long";
            } else if (c == DESC_SHORT.charAt(0)) {
                param = "short";
            } else if (c == DESC_BOOLEAN.charAt(0)) {
                param = "boolean";
            } else if (c == DESC_VOID.charAt(0)) {
                param = Jimple.VOID;
            } else if (c == DESC_ARRAY.charAt(0)) {
                arraylevel++;
                i++;
            } else if (c == DESC_OBJECT.charAt(0)) {
                int j = desc.indexOf(59, i + 1);
                if (j < 0) {
                    logger.warn("Parse error -- can't find a ; in " + desc.substring(i + 1));
                    param = "<error>";
                } else {
                    if (j - i > 10 && desc.substring(i + 1, i + 11).compareTo("java/lang/") == 0) {
                        i += 10;
                    }
                    String param2 = desc.substring(i + 1, j);
                    param = param2.replace('/', '.');
                    i = j;
                }
            } else {
                param = "???";
            }
            if (didone) {
                params = String.valueOf(params) + sep;
            }
            params = String.valueOf(params) + param;
            while (arraylevel > 0) {
                params = String.valueOf(params) + "[]";
                arraylevel--;
            }
            didone = true;
            i++;
        }
        return params;
    }

    method_info findMethod(String s) {
        for (int i = 0; i < this.methods_count; i++) {
            method_info m = this.methods[i];
            if (s.equals(m.toName(this.constant_pool))) {
                return m;
            }
        }
        return null;
    }

    void listMethods() {
        for (int i = 0; i < this.methods_count; i++) {
            logger.debug(this.methods[i].prototype(this.constant_pool));
        }
    }

    void listConstantPool() {
        int i = 1;
        while (i < this.constant_pool_count) {
            cp_info c = this.constant_pool[i];
            logger.debug(DESC_ARRAY + i + "] " + c.typeName() + "=" + c.toString(this.constant_pool));
            if (this.constant_pool[i].tag == 5 || this.constant_pool[i].tag == 6) {
                i++;
            }
            i++;
        }
    }

    void listFields() {
        for (int i = 0; i < this.fields_count; i++) {
            field_info fi = this.fields[i];
            logger.debug(fi.prototype(this.constant_pool));
            int j = 0;
            while (true) {
                if (j >= fi.attributes_count) {
                    break;
                }
                CONSTANT_Utf8_info cm = (CONSTANT_Utf8_info) this.constant_pool[fi.attributes[j].attribute_name];
                if (cm.convert().compareTo("ConstantValue") != 0) {
                    j++;
                } else {
                    ConstantValue_attribute cva = (ConstantValue_attribute) fi.attributes[j];
                    logger.debug(" = " + this.constant_pool[cva.constantvalue_index].toString(this.constant_pool));
                    break;
                }
            }
            logger.debug(";");
        }
    }

    void moveMethod(String m, int pos) {
        logger.debug("Moving " + m + " to position " + pos + " of " + this.methods_count);
        for (int i = 0; i < this.methods_count; i++) {
            if (m.compareTo(this.methods[i].toName(this.constant_pool)) == 0) {
                method_info mthd = this.methods[i];
                if (i > pos) {
                    for (int j = i; j > pos && j > 0; j--) {
                        this.methods[j] = this.methods[j - 1];
                    }
                    this.methods[pos] = mthd;
                    return;
                } else if (i < pos) {
                    for (int j2 = i; j2 < pos && j2 < this.methods_count - 1; j2++) {
                        this.methods[j2] = this.methods[j2 + 1];
                    }
                    this.methods[pos] = mthd;
                    return;
                } else {
                    return;
                }
            }
        }
    }

    boolean descendsFrom(ClassFile cf) {
        return descendsFrom(cf.toString());
    }

    boolean descendsFrom(String cname) {
        cp_info cf = this.constant_pool[this.super_class];
        if (cf.toString(this.constant_pool).compareTo(cname) == 0) {
            return true;
        }
        for (int i = 0; i < this.interfaces_count; i++) {
            cp_info cf2 = this.constant_pool[this.interfaces[i]];
            if (cf2.toString(this.constant_pool).compareTo(cname) == 0) {
                return true;
            }
        }
        return false;
    }

    boolean isSterile() {
        if ((this.access_flags & 1) != 0 && (this.access_flags & 16) == 0) {
            return false;
        }
        return true;
    }

    boolean sameClass(String cfn) {
        String s = cfn;
        int i = s.lastIndexOf(".class");
        if (i > 0) {
            s = s.substring(0, i);
        }
        if (s.compareTo(toString()) == 0) {
            return true;
        }
        return false;
    }

    String fieldName(int i) {
        return this.fields[i].toName(this.constant_pool);
    }
}
