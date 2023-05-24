package javax.management.openmbean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/CompositeDataSupport.class */
public class CompositeDataSupport implements CompositeData, Serializable {
    static final long serialVersionUID = 8003518976613702244L;
    private SortedMap contents;
    private CompositeType compositeType;

    public CompositeDataSupport(CompositeType compositeType, String[] strArr, Object[] objArr) throws OpenDataException {
        this.contents = new TreeMap();
        if (compositeType == null) {
            throw new IllegalArgumentException("Argument compositeType cannot be null.");
        }
        Set keySet = compositeType.keySet();
        checkForNullElement(strArr, "itemNames");
        checkForEmptyString(strArr, "itemNames");
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException("Argument itemValues[] cannot be null or empty.");
        }
        if (strArr.length != objArr.length) {
            throw new IllegalArgumentException(new StringBuffer().append("Array arguments itemNames[] and itemValues[] should be of same length (got ").append(strArr.length).append(" and ").append(objArr.length).append(").").toString());
        }
        if (strArr.length != keySet.size()) {
            throw new OpenDataException(new StringBuffer().append("The size of array arguments itemNames[] and itemValues[] should be equal to the number of items defined in argument compositeType (found ").append(strArr.length).append(" elements in itemNames[] and itemValues[],").append(" expecting ").append(keySet.size()).append(" elements according to compositeType.").toString());
        }
        if (!Arrays.asList(strArr).containsAll(keySet)) {
            throw new OpenDataException("Argument itemNames[] does not contain all names defined in the compositeType of this instance.");
        }
        for (int i = 0; i < objArr.length; i++) {
            OpenType type = compositeType.getType(strArr[i]);
            if (objArr[i] != null && !type.isValue(objArr[i])) {
                throw new OpenDataException(new StringBuffer().append("Argument's element itemValues[").append(i).append("]=\"").append(objArr[i]).append("\" is not a valid value for").append("this item (itemName=").append(strArr[i]).append(",itemType=").append(type).append(").").toString());
            }
        }
        this.compositeType = compositeType;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            this.contents.put(strArr[i2], objArr[i2]);
        }
    }

    public CompositeDataSupport(CompositeType compositeType, Map map) throws OpenDataException {
        this(compositeType, map == null ? null : (String[]) map.keySet().toArray(new String[map.size()]), map == null ? null : map.values().toArray());
    }

    private static void checkForNullElement(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Argument ").append(str).append("[] cannot be null or empty.").toString());
        }
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] == null) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be null.").toString());
            }
        }
    }

    private static void checkForEmptyString(String[] strArr, String str) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].trim().equals("")) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be an empty string.").toString());
            }
        }
    }

    @Override // javax.management.openmbean.CompositeData
    public CompositeType getCompositeType() {
        return this.compositeType;
    }

    @Override // javax.management.openmbean.CompositeData
    public Object get(String str) {
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument key cannot be a null or empty String.");
        }
        if (!this.contents.containsKey(str.trim())) {
            throw new InvalidKeyException(new StringBuffer().append("Argument key=\"").append(str.trim()).append("\" is not an existing item name for this CompositeData instance.").toString());
        }
        return this.contents.get(str.trim());
    }

    @Override // javax.management.openmbean.CompositeData
    public Object[] getAll(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return new Object[0];
        }
        Object[] objArr = new Object[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            objArr[i] = get(strArr[i]);
        }
        return objArr;
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsKey(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        return this.contents.containsKey(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsValue(Object obj) {
        return this.contents.containsValue(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public Collection values() {
        return Collections.unmodifiableCollection(this.contents.values());
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            CompositeData compositeData = (CompositeData) obj;
            if (!getCompositeType().equals(compositeData.getCompositeType())) {
                return false;
            }
            for (Map.Entry entry : this.contents.entrySet()) {
                if (!(entry.getValue() == null ? compositeData.get((String) entry.getKey()) == null : entry.getValue().equals(compositeData.get((String) entry.getKey())))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.CompositeData
    public int hashCode() {
        int hashCode = 0 + this.compositeType.hashCode();
        for (Map.Entry entry : this.contents.entrySet()) {
            hashCode += entry.getValue() == null ? 0 : entry.getValue().hashCode();
        }
        return hashCode;
    }

    @Override // javax.management.openmbean.CompositeData
    public String toString() {
        return new StringBuffer().append(getClass().getName()).append("(compositeType=").append(this.compositeType.toString()).append(",contents=").append(this.contents.toString()).append(")").toString();
    }
}
