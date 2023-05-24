package soot.jimple;

import soot.RefType;
import soot.Type;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.StringTools;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/StringConstant.class */
public class StringConstant extends Constant {
    public final String value;
    public static final StringConstant EMPTY_STRING = new StringConstant("");

    protected StringConstant(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String constant cannot be null");
        }
        this.value = s;
    }

    public static StringConstant v(String value) {
        if (value.isEmpty()) {
            return EMPTY_STRING;
        }
        return new StringConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof StringConstant) && ((StringConstant) c).value.equals(this.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return StringTools.getQuotedStringOf(this.value);
    }

    @Override // soot.Value
    public Type getType() {
        if (Options.v().src_prec() == 7) {
            return RefType.v(DotnetBasicTypes.SYSTEM_STRING);
        }
        return RefType.v("java.lang.String");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseStringConstant(this);
    }
}
