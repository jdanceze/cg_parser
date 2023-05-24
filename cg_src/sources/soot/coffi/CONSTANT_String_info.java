package soot.coffi;

import soot.Value;
import soot.jimple.StringConstant;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_String_info.class */
public class CONSTANT_String_info extends cp_info {
    public int string_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 3;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info) constant_pool[this.string_index];
        return "\"" + ci.convert() + "\"";
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "string";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_String_info cu = (CONSTANT_String_info) cp;
        return ((CONSTANT_Utf8_info) constant_pool[this.string_index]).compareTo(cp_constant_pool[cu.string_index]);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info) constant_pool[this.string_index];
        return StringConstant.v(ci.convert());
    }
}
