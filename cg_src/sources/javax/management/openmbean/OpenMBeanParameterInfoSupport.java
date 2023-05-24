package javax.management.openmbean;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.management.MBeanParameterInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanParameterInfoSupport.class */
public class OpenMBeanParameterInfoSupport extends MBeanParameterInfo implements OpenMBeanParameterInfo, Serializable {
    static final long serialVersionUID = -7235016873758443122L;
    private OpenType openType;
    private Object defaultValue;
    private Set legalValues;
    private Comparable minValue;
    private Comparable maxValue;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType openType) {
        super(str, openType == null ? null : openType.getClassName(), str2);
        this.defaultValue = null;
        this.legalValues = null;
        this.minValue = null;
        this.maxValue = null;
        this.myHashCode = null;
        this.myToString = null;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument name cannot be null or empty.");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty.");
        }
        if (openType == null) {
            throw new IllegalArgumentException("Argument openType cannot be null.");
        }
        this.openType = openType;
    }

    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType openType, Object obj) throws OpenDataException {
        this(str, str2, openType);
        if (obj != null) {
            if (openType.isArray() || (openType instanceof TabularType)) {
                throw new OpenDataException("Default value not supported for ArrayType and TabularType.");
            }
            if (!openType.isValue(obj)) {
                throw new OpenDataException(new StringBuffer().append("Argument defaultValue's class [\"").append(obj.getClass().getName()).append("\"] does not match the one defined in openType[\"").append(openType.getClassName()).append("\"].").toString());
            }
            this.defaultValue = obj;
        }
    }

    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType openType, Object obj, Object[] objArr) throws OpenDataException {
        this(str, str2, openType, obj);
        if (objArr != null && objArr.length > 0) {
            if ((openType instanceof TabularType) || openType.isArray()) {
                throw new OpenDataException("Legal values not supported for TabularType and arrays");
            }
            for (int i = 0; i < objArr.length; i++) {
                if (!openType.isValue(objArr[i])) {
                    throw new OpenDataException(new StringBuffer().append("Element legalValues[").append(i).append("]=").append(objArr[i]).append(" is not a valid value for the specified openType [").append(openType.toString()).append("].").toString());
                }
            }
            HashSet hashSet = new HashSet(objArr.length + 1, 1.0f);
            for (Object obj2 : objArr) {
                hashSet.add(obj2);
            }
            this.legalValues = Collections.unmodifiableSet(hashSet);
        }
        if (hasDefaultValue() && hasLegalValues() && !this.legalValues.contains(obj)) {
            throw new OpenDataException("defaultValue is not contained in legalValues");
        }
    }

    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType openType, Object obj, Comparable comparable, Comparable comparable2) throws OpenDataException {
        this(str, str2, openType, obj);
        if (comparable != null) {
            if (!openType.isValue(comparable)) {
                throw new OpenDataException(new StringBuffer().append("Argument minValue's class [\"").append(comparable.getClass().getName()).append("\"] does not match openType's definition [\"").append(openType.getClassName()).append("\"].").toString());
            }
            this.minValue = comparable;
        }
        if (comparable2 != null) {
            if (!openType.isValue(comparable2)) {
                throw new OpenDataException(new StringBuffer().append("Argument maxValue's class [\"").append(comparable2.getClass().getName()).append("\"] does not match openType's definition [\"").append(openType.getClassName()).append("\"].").toString());
            }
            this.maxValue = comparable2;
        }
        if (hasMinValue() && hasMaxValue() && comparable.compareTo(comparable2) > 0) {
            throw new OpenDataException("minValue cannot be greater than maxValue.");
        }
        if (hasDefaultValue() && hasMinValue() && comparable.compareTo((Comparable) obj) > 0) {
            throw new OpenDataException("minValue cannot be greater than defaultValue.");
        }
        if (hasDefaultValue() && hasMaxValue() && ((Comparable) obj).compareTo(comparable2) > 0) {
            throw new OpenDataException("defaultValue cannot be greater than maxValue.");
        }
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public OpenType getOpenType() {
        return this.openType;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Set getLegalValues() {
        return this.legalValues;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Comparable getMinValue() {
        return this.minValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Comparable getMaxValue() {
        return this.maxValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasLegalValues() {
        return this.legalValues != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasMinValue() {
        return this.minValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasMaxValue() {
        return this.maxValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean isValue(Object obj) {
        boolean z;
        if (hasDefaultValue() && obj == null) {
            z = true;
        } else if (!this.openType.isValue(obj)) {
            z = false;
        } else if (hasLegalValues() && !this.legalValues.contains(obj)) {
            z = false;
        } else if (hasMinValue() && this.minValue.compareTo(obj) > 0) {
            z = false;
        } else if (hasMaxValue() && this.maxValue.compareTo(obj) < 0) {
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    @Override // javax.management.MBeanParameterInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanParameterInfo openMBeanParameterInfo = (OpenMBeanParameterInfo) obj;
            if (!getName().equals(openMBeanParameterInfo.getName()) || !getOpenType().equals(openMBeanParameterInfo.getOpenType())) {
                return false;
            }
            if (hasDefaultValue()) {
                if (!this.defaultValue.equals(openMBeanParameterInfo.getDefaultValue())) {
                    return false;
                }
            } else if (openMBeanParameterInfo.hasDefaultValue()) {
                return false;
            }
            if (hasMinValue()) {
                if (!this.minValue.equals(openMBeanParameterInfo.getMinValue())) {
                    return false;
                }
            } else if (openMBeanParameterInfo.hasMinValue()) {
                return false;
            }
            if (hasMaxValue()) {
                if (!this.maxValue.equals(openMBeanParameterInfo.getMaxValue())) {
                    return false;
                }
            } else if (openMBeanParameterInfo.hasMaxValue()) {
                return false;
            }
            if (hasLegalValues()) {
                if (!this.legalValues.equals(openMBeanParameterInfo.getLegalValues())) {
                    return false;
                }
                return true;
            } else if (openMBeanParameterInfo.hasLegalValues()) {
                return false;
            } else {
                return true;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.MBeanParameterInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            int hashCode = 0 + getName().hashCode() + this.openType.hashCode();
            if (hasDefaultValue()) {
                hashCode += this.defaultValue.hashCode();
            }
            if (hasMinValue()) {
                hashCode += this.minValue.hashCode();
            }
            if (hasMaxValue()) {
                hashCode += this.maxValue.hashCode();
            }
            if (hasLegalValues()) {
                hashCode += this.legalValues.hashCode();
            }
            this.myHashCode = new Integer(hashCode);
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = new StringBuffer().append(getClass().getName()).append("(name=").append(getName()).append(",openType=").append(this.openType.toString()).append(",default=").append(String.valueOf(this.defaultValue)).append(",min=").append(String.valueOf(this.minValue)).append(",max=").append(String.valueOf(this.maxValue)).append(",legals=").append(String.valueOf(this.legalValues)).append(")").toString();
        }
        return this.myToString;
    }
}
