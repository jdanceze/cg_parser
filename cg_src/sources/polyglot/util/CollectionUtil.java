package polyglot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/CollectionUtil.class */
public class CollectionUtil {
    public static List add(List l, Object o) {
        l.add(o);
        return l;
    }

    public static boolean equals(Collection a, Collection b) {
        if (a == b) {
            return true;
        }
        if ((a == null) ^ (b == null)) {
            return false;
        }
        Iterator i = a.iterator();
        Iterator j = b.iterator();
        while (i.hasNext() && j.hasNext()) {
            Object o = i.next();
            Object p = j.next();
            if (o != p) {
                return false;
            }
        }
        if (i.hasNext() || j.hasNext()) {
            return false;
        }
        return true;
    }

    public static List list(Object o) {
        return Collections.singletonList(o);
    }

    public static List list(Object o1, Object o2) {
        List l = new ArrayList(2);
        l.add(o1);
        l.add(o2);
        return l;
    }

    public static List list(Object o1, Object o2, Object o3) {
        List l = new ArrayList(3);
        l.add(o1);
        l.add(o2);
        l.add(o3);
        return l;
    }

    public static List list(Object o1, Object o2, Object o3, Object o4) {
        List l = new ArrayList(3);
        l.add(o1);
        l.add(o2);
        l.add(o3);
        l.add(o4);
        return l;
    }

    public static Object firstOrElse(Collection l, Object alt) {
        Iterator i = l.iterator();
        return i.hasNext() ? i.next() : alt;
    }

    public static Iterator pairs(Collection l) {
        List x = new LinkedList();
        Object prev = null;
        for (Object curr : l) {
            if (prev != null) {
                x.add(new Object[]{prev, curr});
            }
            prev = curr;
        }
        return x.iterator();
    }

    public static List map(List l, Transformation t) {
        List m = new ArrayList(l.size());
        Iterator i = new TransformingIterator(l.iterator(), t);
        while (i.hasNext()) {
            m.add(i.next());
        }
        return m;
    }
}
