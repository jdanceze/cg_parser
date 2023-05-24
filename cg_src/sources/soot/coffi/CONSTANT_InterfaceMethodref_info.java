package soot.coffi;

import soot.Value;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_InterfaceMethodref_info.class */
public class CONSTANT_InterfaceMethodref_info extends cp_info implements ICONSTANT_Methodref_info {
    public int class_index;
    public int name_and_type_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        CONSTANT_Class_info cc = (CONSTANT_Class_info) constant_pool[this.class_index];
        CONSTANT_NameAndType_info cn = (CONSTANT_NameAndType_info) constant_pool[this.name_and_type_index];
        return String.valueOf(cc.toString(constant_pool)) + "." + cn.toString(constant_pool);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "interfacemethodref";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_InterfaceMethodref_info cu = (CONSTANT_InterfaceMethodref_info) cp;
        int i = constant_pool[this.class_index].compareTo(constant_pool, cp_constant_pool[cu.class_index], cp_constant_pool);
        if (i != 0) {
            return i;
        }
        return constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        throw new UnsupportedOperationException("cannot convert to Jimple: " + typeName());
    }

    @Override // soot.coffi.ICONSTANT_Methodref_info
    public int getClassIndex() {
        return this.class_index;
    }

    @Override // soot.coffi.ICONSTANT_Methodref_info
    public int getNameAndTypeIndex() {
        return this.name_and_type_index;
    }
}
