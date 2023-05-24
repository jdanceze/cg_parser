package javax.management.openmbean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/TabularDataSupport.class */
public class TabularDataSupport implements TabularData, Map, Cloneable, Serializable {
    static final long serialVersionUID = 5720150593236309827L;
    private Map dataMap;
    private TabularType tabularType;
    private transient String[] indexNamesArray;

    public TabularDataSupport(TabularType tabularType) {
        this(tabularType, 101, 0.75f);
    }

    public TabularDataSupport(TabularType tabularType, int i, float f) {
        if (tabularType == null) {
            throw new IllegalArgumentException("Argument tabularType cannot be null.");
        }
        this.tabularType = tabularType;
        List indexNames = tabularType.getIndexNames();
        this.indexNamesArray = (String[]) indexNames.toArray(new String[indexNames.size()]);
        this.dataMap = new HashMap(i, f);
    }

    @Override // javax.management.openmbean.TabularData
    public TabularType getTabularType() {
        return this.tabularType;
    }

    @Override // javax.management.openmbean.TabularData
    public Object[] calculateIndex(CompositeData compositeData) {
        checkValueType(compositeData);
        return internalCalculateIndex(compositeData).toArray();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        try {
            return containsKey((Object[]) obj);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.TabularData
    public boolean containsKey(Object[] objArr) {
        if (objArr == null) {
            return false;
        }
        return this.dataMap.containsKey(Arrays.asList(objArr));
    }

    @Override // javax.management.openmbean.TabularData
    public boolean containsValue(CompositeData compositeData) {
        return this.dataMap.containsValue(compositeData);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.dataMap.containsValue(obj);
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        return get((Object[]) obj);
    }

    @Override // javax.management.openmbean.TabularData
    public CompositeData get(Object[] objArr) {
        checkKeyType(objArr);
        return (CompositeData) this.dataMap.get(Arrays.asList(objArr));
    }

    @Override // java.util.Map
    public Object put(Object obj, Object obj2) {
        put((CompositeData) obj2);
        return obj2;
    }

    @Override // javax.management.openmbean.TabularData
    public void put(CompositeData compositeData) {
        this.dataMap.put(checkValueAndIndex(compositeData), compositeData);
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return remove((Object[]) obj);
    }

    @Override // javax.management.openmbean.TabularData
    public CompositeData remove(Object[] objArr) {
        checkKeyType(objArr);
        return (CompositeData) this.dataMap.remove(Arrays.asList(objArr));
    }

    @Override // java.util.Map
    public void putAll(Map map) {
        if (map == null || map.size() == 0) {
            return;
        }
        try {
            putAll((CompositeData[]) map.values().toArray(new CompositeData[map.size()]));
        } catch (ArrayStoreException e) {
            throw new ClassCastException("Map argument t contains values which are not instances of <tt>CompositeData</tt>");
        }
    }

    @Override // javax.management.openmbean.TabularData
    public void putAll(CompositeData[] compositeDataArr) {
        if (compositeDataArr == null || compositeDataArr.length == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList(compositeDataArr.length + 1);
        for (int i = 0; i < compositeDataArr.length; i++) {
            List checkValueAndIndex = checkValueAndIndex(compositeDataArr[i]);
            if (arrayList.contains(checkValueAndIndex)) {
                throw new KeyAlreadyExistsException(new StringBuffer().append("Argument elements values[").append(i).append("] and values[").append(arrayList.indexOf(checkValueAndIndex)).append("] have the same indexes, ").append("calculated according to this TabularData instance's tabularType.").toString());
            }
            arrayList.add(checkValueAndIndex);
        }
        for (int i2 = 0; i2 < compositeDataArr.length; i2++) {
            this.dataMap.put(arrayList.get(i2), compositeDataArr[i2]);
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public void clear() {
        this.dataMap.clear();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public int size() {
        return this.dataMap.size();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public Set keySet() {
        return this.dataMap.keySet();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public Collection values() {
        return this.dataMap.values();
    }

    @Override // java.util.Map
    public Set entrySet() {
        return this.dataMap.entrySet();
    }

    public Object clone() {
        try {
            TabularDataSupport tabularDataSupport = (TabularDataSupport) super.clone();
            tabularDataSupport.dataMap = (HashMap) ((HashMap) tabularDataSupport.dataMap).clone();
            return tabularDataSupport;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            TabularData tabularData = (TabularData) obj;
            if (!getTabularType().equals(tabularData.getTabularType()) || size() != tabularData.size()) {
                return false;
            }
            for (CompositeData compositeData : values()) {
                if (!tabularData.containsValue(compositeData)) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public int hashCode() {
        int hashCode = 0 + this.tabularType.hashCode();
        for (CompositeData compositeData : values()) {
            hashCode += compositeData.hashCode();
        }
        return hashCode;
    }

    @Override // javax.management.openmbean.TabularData
    public String toString() {
        return new StringBuffer().append(getClass().getName()).append("(tabularType=").append(this.tabularType.toString()).append(",contents=").append(this.dataMap.toString()).append(")").toString();
    }

    private List internalCalculateIndex(CompositeData compositeData) {
        return Collections.unmodifiableList(Arrays.asList(compositeData.getAll(this.indexNamesArray)));
    }

    private void checkKeyType(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            throw new NullPointerException("Argument key cannot be null or empty.");
        }
        if (objArr.length != this.indexNamesArray.length) {
            throw new InvalidKeyException(new StringBuffer().append("Argument key's length=").append(objArr.length).append(" is different from the number of item values, which is ").append(this.indexNamesArray.length).append(", specified for the indexing rows in this TabularData instance.").toString());
        }
        for (int i = 0; i < objArr.length; i++) {
            OpenType type = this.tabularType.getRowType().getType(this.indexNamesArray[i]);
            if (objArr[i] != null && !type.isValue(objArr[i])) {
                throw new InvalidKeyException(new StringBuffer().append("Argument element key[").append(i).append("] is not a value for the open type expected for ").append("this element of the index, whose name is \"").append(this.indexNamesArray[i]).append("\" and whose open type is ").append(type).toString());
            }
        }
    }

    private void checkValueType(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Argument value cannot be null.");
        }
        if (!compositeData.getCompositeType().equals(this.tabularType.getRowType())) {
            throw new InvalidOpenTypeException(new StringBuffer().append("Argument value's composite type [").append(compositeData.getCompositeType()).append("] is not equal to ").append("this TabularData instance's row type [").append(this.tabularType.getRowType()).append("].").toString());
        }
    }

    private List checkValueAndIndex(CompositeData compositeData) {
        checkValueType(compositeData);
        List internalCalculateIndex = internalCalculateIndex(compositeData);
        if (this.dataMap.containsKey(internalCalculateIndex)) {
            throw new KeyAlreadyExistsException("Argument value's index, calculated according to this TabularData instance's tabularType, already refers to a value in this table.");
        }
        return internalCalculateIndex;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        List indexNames = this.tabularType.getIndexNames();
        this.indexNamesArray = (String[]) indexNames.toArray(new String[indexNames.size()]);
    }
}
