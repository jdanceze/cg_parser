package soot.coffi;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import soot.Value;
import soot.jimple.DoubleConstant;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Double_info.class */
public class CONSTANT_Double_info extends cp_info {
    public long high;
    public long low;

    @Override // soot.coffi.cp_info
    public int size() {
        return 9;
    }

    public double convert() {
        return Double.longBitsToDouble(ints2long(this.high, this.low));
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        return Double.toString(convert());
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "double";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Double_info cu = (CONSTANT_Double_info) cp;
        double d = convert() - cu.convert();
        if (d > Const.default_value_double) {
            return 1;
        }
        return d < Const.default_value_double ? -1 : 0;
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return DoubleConstant.v(convert());
    }
}
