package soot.tagkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import soot.UnitBox;
/* loaded from: gencallgraphv3.jar:soot/tagkit/GenericAttribute.class */
public class GenericAttribute implements Attribute {
    private final String mName;
    private byte[] mValue;

    public GenericAttribute(String name, byte[] value) {
        this.mName = name;
        this.mValue = value != null ? value : new byte[0];
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return this.mName;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return this.mValue;
    }

    public String toString() {
        return String.valueOf(this.mName) + ' ' + Arrays.toString(Base64.encode(this.mValue));
    }

    @Override // soot.tagkit.Attribute
    public void setValue(byte[] value) {
        this.mValue = value;
    }

    public List<UnitBox> getUnitBoxes() {
        return Collections.emptyList();
    }
}
