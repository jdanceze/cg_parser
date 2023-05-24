package soot.jimple.infoflow.data;

import java.util.Arrays;
import soot.Local;
import soot.NullType;
import soot.SootField;
import soot.Type;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AccessPath.class */
public class AccessPath implements Cloneable {
    private static AccessPath zeroAccessPath;
    private final Local value;
    private final Type baseType;
    private final AccessPathFragment[] fragments;
    private final boolean taintSubFields;
    private final boolean cutOffApproximation;
    private final ArrayTaintType arrayTaintType;
    private final boolean canHaveImmutableAliases;
    private int hashCode;
    private static final AccessPath emptyAccessPath;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AccessPath$ArrayTaintType.class */
    public enum ArrayTaintType {
        Contents,
        Length,
        ContentsAndLength;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static ArrayTaintType[] valuesCustom() {
            ArrayTaintType[] valuesCustom = values();
            int length = valuesCustom.length;
            ArrayTaintType[] arrayTaintTypeArr = new ArrayTaintType[length];
            System.arraycopy(valuesCustom, 0, arrayTaintTypeArr, 0, length);
            return arrayTaintTypeArr;
        }
    }

    static {
        $assertionsDisabled = !AccessPath.class.desiredAssertionStatus();
        zeroAccessPath = null;
        emptyAccessPath = new AccessPath();
    }

    private AccessPath() {
        this.hashCode = 0;
        this.value = null;
        this.baseType = null;
        this.fragments = null;
        this.taintSubFields = true;
        this.cutOffApproximation = false;
        this.arrayTaintType = ArrayTaintType.ContentsAndLength;
        this.canHaveImmutableAliases = false;
    }

    AccessPath(Local val, SootField[] appendingFields, Type valType, Type[] appendingFieldTypes, boolean taintSubFields, boolean isCutOffApproximation, ArrayTaintType arrayTaintType, boolean canHaveImmutableAliases) {
        this.hashCode = 0;
        this.value = val;
        this.baseType = valType;
        this.fragments = AccessPathFragment.createFragmentArray(appendingFields, appendingFieldTypes);
        this.taintSubFields = taintSubFields;
        this.cutOffApproximation = isCutOffApproximation;
        this.arrayTaintType = arrayTaintType;
        this.canHaveImmutableAliases = canHaveImmutableAliases;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessPath(Local val, Type valType, AccessPathFragment[] fragments, boolean taintSubFields, boolean isCutOffApproximation, ArrayTaintType arrayTaintType, boolean canHaveImmutableAliases) {
        this.hashCode = 0;
        this.value = val;
        this.baseType = valType;
        this.fragments = fragments;
        this.taintSubFields = taintSubFields;
        this.cutOffApproximation = isCutOffApproximation;
        this.arrayTaintType = arrayTaintType;
        this.canHaveImmutableAliases = canHaveImmutableAliases;
    }

    public static boolean canContainValue(Value val) {
        if (val == null) {
            return false;
        }
        return (val instanceof Local) || (val instanceof InstanceFieldRef) || (val instanceof StaticFieldRef) || (val instanceof ArrayRef);
    }

    public Local getPlainValue() {
        return this.value;
    }

    public Value getCompleteValue() {
        SootField f = getFirstField();
        if (this.value == null) {
            if (f == null) {
                return null;
            }
            return Jimple.v().newStaticFieldRef(f.makeRef());
        } else if (f == null) {
            return this.value;
        } else {
            return Jimple.v().newInstanceFieldRef(this.value, f.makeRef());
        }
    }

    public AccessPathFragment getLastFragment() {
        if (this.fragments == null || this.fragments.length == 0) {
            return null;
        }
        return this.fragments[this.fragments.length - 1];
    }

    public AccessPathFragment getFirstFragment() {
        if (this.fragments == null || this.fragments.length == 0) {
            return null;
        }
        return this.fragments[0];
    }

    public SootField getFirstField() {
        if (this.fragments == null || this.fragments.length == 0) {
            return null;
        }
        return this.fragments[0].getField();
    }

    public SootField getLastField() {
        if (this.fragments == null || this.fragments.length == 0) {
            return null;
        }
        return this.fragments[this.fragments.length - 1].getField();
    }

    public Type getFirstFieldType() {
        if (this.fragments == null || this.fragments.length == 0) {
            return null;
        }
        return this.fragments[0].getFieldType();
    }

    public Type getLastFieldType() {
        if (this.fragments == null || this.fragments.length == 0) {
            return getBaseType();
        }
        return this.fragments[this.fragments.length - 1].getFieldType();
    }

    public boolean firstFieldMatches(SootField field) {
        return (this.fragments == null || this.fragments.length == 0 || field != this.fragments[0].getField()) ? false : true;
    }

    public AccessPathFragment[] getFragments() {
        return this.fragments;
    }

    public int getFragmentCount() {
        if (this.fragments == null) {
            return 0;
        }
        return this.fragments.length;
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        int result = (31 * 1) + (this.value == null ? 0 : this.value.hashCode());
        this.hashCode = (31 * ((31 * ((31 * ((31 * ((31 * result) + (this.baseType == null ? 0 : this.baseType.hashCode()))) + (this.fragments == null ? 0 : Arrays.hashCode(this.fragments)))) + (this.taintSubFields ? 1 : 0))) + this.arrayTaintType.hashCode())) + (this.canHaveImmutableAliases ? 1 : 0);
        return this.hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == this || super.equals(obj)) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessPath other = (AccessPath) obj;
        if (this.hashCode != 0 && other.hashCode != 0 && this.hashCode != other.hashCode) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        if (this.baseType == null) {
            if (other.baseType != null) {
                return false;
            }
        } else if (!this.baseType.equals(other.baseType)) {
            return false;
        }
        if (!Arrays.equals(this.fragments, other.fragments) || this.taintSubFields != other.taintSubFields || this.arrayTaintType != other.arrayTaintType || this.canHaveImmutableAliases != other.canHaveImmutableAliases) {
            return false;
        }
        if ($assertionsDisabled || hashCode() == obj.hashCode()) {
            return true;
        }
        throw new AssertionError();
    }

    public boolean isStaticFieldRef() {
        return this.value == null && this.fragments != null && this.fragments.length > 0;
    }

    public boolean isInstanceFieldRef() {
        return (this.value == null || this.fragments == null || this.fragments.length <= 0) ? false : true;
    }

    public boolean isFieldRef() {
        return this.fragments != null && this.fragments.length > 0;
    }

    public boolean isLocal() {
        if (this.value == null || !(this.value instanceof Local)) {
            return false;
        }
        return this.fragments == null || this.fragments.length == 0;
    }

    public String toString() {
        String str = "";
        if (this.value != null) {
            str = String.valueOf(str) + this.value.toString() + "(" + this.value.getType() + ")";
        }
        if (this.fragments != null && this.fragments.length > 0) {
            for (int i = 0; i < this.fragments.length; i++) {
                if (this.fragments[i] != null) {
                    if (!str.isEmpty()) {
                        str = String.valueOf(str) + Instruction.argsep;
                    }
                    str = String.valueOf(str) + this.fragments[i];
                }
            }
        }
        if (this.taintSubFields) {
            str = String.valueOf(str) + " *";
        }
        if (this.arrayTaintType == ArrayTaintType.ContentsAndLength) {
            str = String.valueOf(str) + " <+length>";
        } else if (this.arrayTaintType == ArrayTaintType.Length) {
            str = String.valueOf(str) + " <length>";
        }
        return str;
    }

    /* renamed from: clone */
    public AccessPath m2735clone() {
        if (this == emptyAccessPath) {
            return this;
        }
        AccessPath a = new AccessPath(this.value, this.baseType, this.fragments, this.taintSubFields, this.cutOffApproximation, this.arrayTaintType, this.canHaveImmutableAliases);
        if ($assertionsDisabled || a.equals(this)) {
            return a;
        }
        throw new AssertionError();
    }

    public static AccessPath getEmptyAccessPath() {
        return emptyAccessPath;
    }

    public boolean isEmpty() {
        if (this.value == null) {
            return this.fragments == null || this.fragments.length == 0;
        }
        return false;
    }

    public boolean entails(AccessPath a2) {
        if (isEmpty() || a2.isEmpty()) {
            return false;
        }
        if (this.value == null || a2.value != null) {
            if (this.value == null && a2.value != null) {
                return false;
            }
            if (this.value != null && !this.value.equals(a2.value)) {
                return false;
            }
            if (this.fragments != null && a2.fragments != null) {
                if (this.fragments.length > a2.fragments.length) {
                    return false;
                }
                for (int i = 0; i < this.fragments.length; i++) {
                    if (!this.fragments[i].getField().equals(a2.fragments[i].getField())) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public AccessPath dropLastField() {
        AccessPathFragment[] newFragments;
        if (this.fragments == null || this.fragments.length == 0) {
            return this;
        }
        if (this.fragments.length > 1) {
            int newLength = this.fragments.length - 1;
            newFragments = new AccessPathFragment[newLength];
            System.arraycopy(this.fragments, 0, newFragments, 0, newLength);
        } else {
            newFragments = null;
        }
        return new AccessPath(this.value, this.baseType, newFragments, this.taintSubFields, this.cutOffApproximation, this.arrayTaintType, this.canHaveImmutableAliases);
    }

    public Type getBaseType() {
        return this.baseType;
    }

    public boolean getTaintSubFields() {
        return this.taintSubFields;
    }

    public boolean isCutOffApproximation() {
        return this.cutOffApproximation;
    }

    public ArrayTaintType getArrayTaintType() {
        return this.arrayTaintType;
    }

    public boolean startsWith(Value val) {
        if (!canContainValue(val)) {
            return false;
        }
        if ((val instanceof Local) && this.value == val) {
            return true;
        }
        if (val instanceof StaticFieldRef) {
            return this.value == null && getFirstField() == ((StaticFieldRef) val).getField();
        } else if (val instanceof InstanceFieldRef) {
            InstanceFieldRef iref = (InstanceFieldRef) val;
            return this.value == iref.getBase() && getFirstField() == iref.getField();
        } else {
            return false;
        }
    }

    public boolean getCanHaveImmutableAliases() {
        return this.canHaveImmutableAliases;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessPath getZeroAccessPath() {
        if (zeroAccessPath == null) {
            zeroAccessPath = new AccessPath(Jimple.v().newLocal("zero", NullType.v()), null, NullType.v(), null, false, false, ArrayTaintType.ContentsAndLength, false);
        }
        return zeroAccessPath;
    }
}
