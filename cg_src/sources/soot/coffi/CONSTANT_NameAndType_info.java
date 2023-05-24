package soot.coffi;

import soot.Value;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_NameAndType_info.class */
public class CONSTANT_NameAndType_info extends cp_info {
    public int name_index;
    public int descriptor_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info) constant_pool[this.name_index];
        return ci.convert();
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "nameandtype";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_NameAndType_info cu = (CONSTANT_NameAndType_info) cp;
        int i = ((CONSTANT_Utf8_info) constant_pool[this.name_index]).compareTo(cp_constant_pool[cu.name_index]);
        if (i != 0) {
            return i;
        }
        return ((CONSTANT_Utf8_info) constant_pool[this.descriptor_index]).compareTo(cp_constant_pool[cu.descriptor_index]);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        throw new UnsupportedOperationException("cannot convert to Jimple: " + typeName());
    }
}
