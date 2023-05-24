package soot.coffi;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import soot.Value;
import soot.jimple.FloatConstant;
import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Float_info.class */
public class CONSTANT_Float_info extends cp_info {
    public long bytes;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    public float convert() {
        return Float.intBitsToFloat((int) this.bytes);
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        return Float.toString((float) this.bytes);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return Jimple.FLOAT;
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Float_info cu = (CONSTANT_Float_info) cp;
        float d = convert() - cu.convert();
        if (d > Const.default_value_double) {
            return 1;
        }
        return ((double) d) < Const.default_value_double ? -1 : 0;
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return FloatConstant.v(convert());
    }
}
