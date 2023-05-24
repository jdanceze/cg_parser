package soot.jimple.toolkits.thread.mhp;

import java.util.List;
import java.util.Map;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/CheckMSet.class */
public class CheckMSet {
    CheckMSet(Map m1, Map m2) {
        checkKeySet(m1, m2);
        check(m1, m2);
    }

    private void checkKeySet(Map m1, Map m2) {
        FlowSet temp = new ArraySparseSet();
        for (Object key2 : m2.keySet()) {
            if (key2 instanceof List) {
                for (Object obj : (List) key2) {
                    if (obj instanceof List) {
                        for (Object o : (List) obj) {
                            temp.add(o);
                            if (!m1.containsKey(o)) {
                                throw new RuntimeException("1--before compacting map does not contains key " + o);
                            }
                        }
                        continue;
                    } else {
                        temp.add(obj);
                        if (!m1.containsKey(obj)) {
                            throw new RuntimeException("2--before compacting map does not contains key " + obj);
                        }
                    }
                }
                continue;
            } else if (!(key2 instanceof JPegStmt)) {
                throw new RuntimeException("key error: " + key2);
            } else {
                temp.add(key2);
                if (!m1.containsKey(key2)) {
                    throw new RuntimeException("3--before compacting map does not contains key " + key2);
                }
            }
        }
        for (Object key1 : m1.keySet()) {
            if (!temp.contains(key1)) {
                throw new RuntimeException("after compacting map does not contains key " + key1);
            }
        }
    }

    private void check(Map m1, Map m2) {
        for (Object key : m2.keySet()) {
            if (key instanceof JPegStmt) {
                ((JPegStmt) key).getTags().get(0);
            }
            FlowSet mSet2 = (FlowSet) m2.get(key);
            if (key instanceof List) {
                for (Object obj : (List) key) {
                    if (obj instanceof List) {
                        for (Object oo : (List) obj) {
                            FlowSet mSet11 = (FlowSet) m1.get(oo);
                            if (mSet11 == null) {
                                throw new RuntimeException("1--mSet of " + obj + " is null!");
                            }
                            if (!compare(mSet11, mSet2)) {
                                throw new RuntimeException("1--mSet before and after are NOT the same!");
                            }
                        }
                        continue;
                    } else {
                        FlowSet mSet1 = (FlowSet) m1.get(obj);
                        if (mSet1 == null) {
                            throw new RuntimeException("2--mSet of " + obj + " is null!");
                        }
                        if (!compare(mSet1, mSet2)) {
                            throw new RuntimeException("2--mSet before and after are NOT the same!");
                        }
                    }
                }
                continue;
            } else if (!compare((FlowSet) m1.get(key), mSet2)) {
                throw new RuntimeException("3--mSet before and after are NOT the same!");
            }
        }
    }

    private boolean compare(FlowSet mSet1, FlowSet mSet2) {
        FlowSet temp = new ArraySparseSet();
        for (Object obj : mSet2) {
            if (obj instanceof List) {
                for (Object o : (List) obj) {
                    if (o instanceof List) {
                        for (Object obj2 : (List) o) {
                            temp.add(obj2);
                        }
                    } else {
                        temp.add(o);
                    }
                }
            } else {
                temp.add(obj);
            }
        }
        for (Object o2 : mSet1) {
            if (!temp.contains(o2)) {
                System.out.println("mSet2: \n" + mSet2);
                System.err.println("mSet2 does not contains " + o2);
                return false;
            }
        }
        for (Object obj3 : mSet2) {
            if (obj3 instanceof List) {
                for (Object o3 : (List) obj3) {
                    if (o3 instanceof List) {
                        for (Object oo : (List) o3) {
                            if (!mSet1.contains(oo)) {
                                System.err.println("1--mSet1 does not contains " + oo);
                                return false;
                            }
                        }
                        continue;
                    } else if (!mSet1.contains(o3)) {
                        System.err.println("2--mSet1 does not contains " + o3);
                        return false;
                    }
                }
                continue;
            } else if (!mSet1.contains(obj3)) {
                System.err.println("3--mSet1 does not contains " + obj3);
                return false;
            }
        }
        return true;
    }
}
