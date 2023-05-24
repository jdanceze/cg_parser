package soot.coffi;

import soot.Value;
import soot.jimple.IntConstant;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Integer_info.class */
public class CONSTANT_Integer_info extends cp_info {
    public long bytes;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        return Integer.toString((int) this.bytes);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "int";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Integer_info cu = (CONSTANT_Integer_info) cp;
        return ((int) this.bytes) - ((int) cu.bytes);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return IntConstant.v((int) this.bytes);
    }
}
