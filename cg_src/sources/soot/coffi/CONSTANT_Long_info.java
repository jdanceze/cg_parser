package soot.coffi;

import soot.Value;
import soot.jimple.LongConstant;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Long_info.class */
public class CONSTANT_Long_info extends cp_info {
    public long high;
    public long low;

    @Override // soot.coffi.cp_info
    public int size() {
        return 9;
    }

    public long convert() {
        return ints2long(this.high, this.low);
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        return "(" + this.high + "," + this.low + ") = " + Long.toString(convert());
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "long";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Long_info cu = (CONSTANT_Long_info) cp;
        long d = convert() - cu.convert();
        if (d > 0) {
            return 1;
        }
        return d < 0 ? -1 : 0;
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return LongConstant.v(convert());
    }
}
