package org.powermock.core;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/WildcardMatcher.class */
public class WildcardMatcher {
    private static final char WILDCARD = '*';

    public static boolean matches(String text, String pattern) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        String text2 = text + (char) 0;
        String pattern2 = pattern + (char) 0;
        int N = pattern2.length();
        boolean[] states = new boolean[N + 1];
        boolean[] old = new boolean[N + 1];
        old[0] = true;
        for (int i = 0; i < text2.length(); i++) {
            char c = text2.charAt(i);
            states = new boolean[N + 1];
            for (int j = 0; j < N; j++) {
                char p = pattern2.charAt(j);
                if (old[j] && p == '*') {
                    old[j + 1] = true;
                }
                if (old[j] && p == c) {
                    states[j + 1] = true;
                }
                if (old[j] && p == '*') {
                    states[j] = true;
                }
                if (old[j] && p == '*') {
                    states[j + 1] = true;
                }
            }
            old = states;
        }
        return states[N];
    }

    public static boolean matchesAny(Collection<String> patterns, String text) {
        for (String pattern : patterns) {
            if (matches(text, pattern)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchesAny(Iterable<String> patterns, String text) {
        for (String string : patterns) {
            if (matches(text, string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchesAny(String[] patterns, String text) {
        for (String string : patterns) {
            if (matches(text, string)) {
                return true;
            }
        }
        return false;
    }
}
