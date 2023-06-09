package com.sun.xml.bind.v2.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.WeakHashMap;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/EditDistance.class */
public class EditDistance {
    private static final WeakHashMap<AbstractMap.SimpleEntry<String, String>, Integer> CACHE = new WeakHashMap<>();
    private int[] cost;
    private int[] back;
    private final String a;
    private final String b;

    public static int editDistance(String a, String b) {
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>(a, b);
        Integer result = null;
        if (CACHE.containsKey(entry)) {
            result = CACHE.get(entry);
        }
        if (result == null) {
            result = Integer.valueOf(new EditDistance(a, b).calc());
            CACHE.put(entry, result);
        }
        return result.intValue();
    }

    public static String findNearest(String key, String[] group) {
        return findNearest(key, Arrays.asList(group));
    }

    public static String findNearest(String key, Collection<String> group) {
        int c = Integer.MAX_VALUE;
        String r = null;
        for (String s : group) {
            int ed = editDistance(key, s);
            if (c > ed) {
                c = ed;
                r = s;
            }
        }
        return r;
    }

    private EditDistance(String a, String b) {
        this.a = a;
        this.b = b;
        this.cost = new int[a.length() + 1];
        this.back = new int[a.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            this.cost[i] = i;
        }
    }

    private void flip() {
        int[] t = this.cost;
        this.cost = this.back;
        this.back = t;
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    private int calc() {
        for (int j = 0; j < this.b.length(); j++) {
            flip();
            this.cost[0] = j + 1;
            for (int i = 0; i < this.a.length(); i++) {
                int match = this.a.charAt(i) == this.b.charAt(j) ? 0 : 1;
                this.cost[i + 1] = min(this.back[i] + match, this.cost[i] + 1, this.back[i + 1] + 1);
            }
        }
        return this.cost[this.a.length()];
    }
}
