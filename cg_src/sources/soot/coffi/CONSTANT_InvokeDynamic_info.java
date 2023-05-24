package soot.coffi;

import soot.Value;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_InvokeDynamic_info.class */
public class CONSTANT_InvokeDynamic_info extends cp_info {
    public int bootstrap_method_index;
    public int name_and_type_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        cp_info bsm = constant_pool[this.bootstrap_method_index];
        cp_info nat = constant_pool[this.name_and_type_index];
        return String.valueOf(nat.toString(constant_pool)) + " - " + bsm.toString(constant_pool);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "invokedynamic";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_InvokeDynamic_info cu = (CONSTANT_InvokeDynamic_info) cp;
        int i = constant_pool[this.bootstrap_method_index].compareTo(constant_pool, cp_constant_pool[cu.bootstrap_method_index], cp_constant_pool);
        if (i != 0) {
            return i;
        }
        return constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        throw new UnsupportedOperationException("cannot convert to Jimple: " + typeName());
    }
}
