package soot.coffi;

import soot.Value;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_MethodHandle_info.class */
public class CONSTANT_MethodHandle_info extends cp_info {
    public int kind;
    public int target_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 4;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        cp_info target = constant_pool[this.target_index];
        return target.toString(constant_pool);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "methodhandle";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_MethodHandle_info cu = (CONSTANT_MethodHandle_info) cp;
        int i = constant_pool[this.target_index].compareTo(constant_pool, cp_constant_pool[cu.target_index], cp_constant_pool);
        if (i != 0) {
            return i;
        }
        return this.kind - cu.kind;
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return constant_pool[this.target_index].createJimpleConstantValue(constant_pool);
    }
}
