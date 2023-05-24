package soot.jimple.infoflow.data;

import java.util.Objects;
import soot.SootField;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AccessPathFragment.class */
public class AccessPathFragment {
    private final SootField field;
    private final Type fieldType;
    private final ContextDefinition context;

    public AccessPathFragment(SootField field, Type fieldType) {
        this.field = field;
        this.fieldType = fieldType == null ? field.getType() : fieldType;
        this.context = null;
    }

    public AccessPathFragment(SootField field, Type fieldType, ContextDefinition context) {
        this.field = field;
        this.fieldType = fieldType;
        this.context = context;
    }

    public SootField getField() {
        return this.field;
    }

    public Type getFieldType() {
        return this.fieldType == null ? this.field.getType() : this.fieldType;
    }

    public ContextDefinition getContext() {
        return this.context;
    }

    public String toString() {
        return this.field.toString();
    }

    public boolean isValid() {
        return this.fieldType != null;
    }

    public static AccessPathFragment[] createFragmentArray(SootField[] fields, Type[] fieldTypes) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        AccessPathFragment[] fragments = new AccessPathFragment[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fragments[i] = new AccessPathFragment(fields[i], fieldTypes == null ? null : fieldTypes[i]);
        }
        return fragments;
    }

    public int hashCode() {
        return Objects.hash(this.context, this.field, this.fieldType);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessPathFragment other = (AccessPathFragment) obj;
        return Objects.equals(this.context, other.context) && Objects.equals(this.field, other.field) && Objects.equals(this.fieldType, other.fieldType);
    }

    public AccessPathFragment copyWithNewType(Type newType) {
        return new AccessPathFragment(this.field, newType, this.context);
    }
}
