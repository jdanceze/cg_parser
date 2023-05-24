package soot.tagkit;

import soot.coffi.CONSTANT_Utf8_info;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/StringConstantValueTag.class */
public class StringConstantValueTag extends ConstantValueTag {
    public static final String NAME = "StringConstantValueTag";
    private final String value;

    public StringConstantValueTag(String value) {
        super(CONSTANT_Utf8_info.toUtf8(value));
        this.value = value;
    }

    public String getStringValue() {
        return this.value;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.ConstantValueTag
    public String toString() {
        return "ConstantValue: " + this.value;
    }

    @Override // soot.tagkit.ConstantValueTag
    public StringConstant getConstant() {
        return StringConstant.v(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.value == null ? 0 : this.value.hashCode());
    }

    @Override // soot.tagkit.ConstantValueTag
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        StringConstantValueTag other = (StringConstantValueTag) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        } else if (!this.value.equals(other.value)) {
            return false;
        } else {
            return true;
        }
    }
}
