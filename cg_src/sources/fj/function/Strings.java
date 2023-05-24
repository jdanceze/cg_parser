package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.data.Stream;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Strings.class */
public final class Strings {
    public static final F<String, Boolean> isNotNullOrBlank = new F<String, Boolean>() { // from class: fj.function.Strings.1
        @Override // fj.F
        public Boolean f(String a) {
            return Boolean.valueOf(Strings.isNotNullOrEmpty.f(a).booleanValue() && Stream.fromString(a).find(Booleans.not(Characters.isWhitespace)).isSome());
        }
    };
    public static final F<String, Boolean> isNullOrBlank = new F<String, Boolean>() { // from class: fj.function.Strings.2
        @Override // fj.F
        public Boolean f(String a) {
            return Boolean.valueOf(Strings.isNullOrEmpty.f(a).booleanValue() || Stream.fromString(a).find(Booleans.not(Characters.isWhitespace)).isNone());
        }
    };
    public static final F<String, Boolean> isNotNullOrEmpty = new F<String, Boolean>() { // from class: fj.function.Strings.3
        @Override // fj.F
        public Boolean f(String a) {
            return Boolean.valueOf(a != null && a.length() > 0);
        }
    };
    public static final F<String, Boolean> isNullOrEmpty = new F<String, Boolean>() { // from class: fj.function.Strings.4
        @Override // fj.F
        public Boolean f(String a) {
            return Boolean.valueOf(a == null || a.length() == 0);
        }
    };
    public static final F<String, Boolean> isEmpty = new F<String, Boolean>() { // from class: fj.function.Strings.5
        @Override // fj.F
        public Boolean f(String s) {
            return Boolean.valueOf(s.length() == 0);
        }
    };
    public static final F<String, Integer> length = new F<String, Integer>() { // from class: fj.function.Strings.6
        @Override // fj.F
        public Integer f(String s) {
            return Integer.valueOf(s.length());
        }
    };
    public static final F<String, F<String, Boolean>> contains = Function.curry(new F2<String, String, Boolean>() { // from class: fj.function.Strings.7
        @Override // fj.F2
        public Boolean f(String s1, String s2) {
            return Boolean.valueOf(s2.contains(s1));
        }
    });
    public static final F<String, F<String, Boolean>> matches = Function.curry(new F2<String, String, Boolean>() { // from class: fj.function.Strings.8
        @Override // fj.F2
        public Boolean f(String s1, String s2) {
            return Boolean.valueOf(s2.matches(s1));
        }
    });

    private Strings() {
        throw new UnsupportedOperationException();
    }
}
