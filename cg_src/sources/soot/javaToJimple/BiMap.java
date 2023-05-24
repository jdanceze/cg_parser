package soot.javaToJimple;

import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/BiMap.class */
public class BiMap {
    HashMap<Object, Object> keyVal;
    HashMap<Object, Object> valKey;

    public void put(Object key, Object val) {
        if (this.keyVal == null) {
            this.keyVal = new HashMap<>();
        }
        if (this.valKey == null) {
            this.valKey = new HashMap<>();
        }
        this.keyVal.put(key, val);
        this.valKey.put(val, key);
    }

    public Object getKey(Object val) {
        if (this.valKey == null) {
            return null;
        }
        return this.valKey.get(val);
    }

    public Object getVal(Object key) {
        if (this.keyVal == null) {
            return null;
        }
        return this.keyVal.get(key);
    }

    public boolean containsKey(Object key) {
        if (this.keyVal == null) {
            return false;
        }
        return this.keyVal.containsKey(key);
    }

    public boolean containsVal(Object val) {
        if (this.valKey == null) {
            return false;
        }
        return this.valKey.containsKey(val);
    }
}
