package org.apache.tools.ant.property;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import org.apache.tools.ant.PropertyHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/LocalPropertyStack.class */
public class LocalPropertyStack {
    private final Deque<Map<String, Object>> stack = new LinkedList();
    private final Object LOCK = new Object();

    public void addLocal(String property) {
        synchronized (this.LOCK) {
            Map<String, Object> map = this.stack.peek();
            if (map != null) {
                map.put(property, NullReturn.NULL);
            }
        }
    }

    public void enterScope() {
        synchronized (this.LOCK) {
            this.stack.addFirst(new ConcurrentHashMap());
        }
    }

    public void exitScope() {
        synchronized (this.LOCK) {
            this.stack.removeFirst().clear();
        }
    }

    public LocalPropertyStack copy() {
        LocalPropertyStack ret;
        synchronized (this.LOCK) {
            ret = new LocalPropertyStack();
            ret.stack.addAll(this.stack);
        }
        return ret;
    }

    public Object evaluate(String property, PropertyHelper helper) {
        synchronized (this.LOCK) {
            for (Map<String, Object> map : this.stack) {
                Object ret = map.get(property);
                if (ret != null) {
                    return ret;
                }
            }
            return null;
        }
    }

    public boolean setNew(String property, Object value, PropertyHelper propertyHelper) {
        Map<String, Object> map = getMapForProperty(property);
        if (map == null) {
            return false;
        }
        Object currValue = map.get(property);
        if (currValue == NullReturn.NULL) {
            map.put(property, value);
            return true;
        }
        return true;
    }

    public boolean set(String property, Object value, PropertyHelper propertyHelper) {
        Map<String, Object> map = getMapForProperty(property);
        if (map == null) {
            return false;
        }
        map.put(property, value);
        return true;
    }

    public Set<String> getPropertyNames() {
        Set<String> names = (Set) this.stack.stream().map((v0) -> {
            return v0.keySet();
        }).collect(Collector.of(() -> {
            return new HashSet();
        }, ns, ks -> {
            ns.addAll(ks);
        }, ns1, ns2 -> {
            ns1.addAll(ns2);
            return ns1;
        }, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
        return Collections.unmodifiableSet(names);
    }

    private Map<String, Object> getMapForProperty(String property) {
        synchronized (this.LOCK) {
            for (Map<String, Object> map : this.stack) {
                if (map.get(property) != null) {
                    return map;
                }
            }
            return null;
        }
    }
}
