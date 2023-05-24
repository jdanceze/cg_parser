package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Characters.class */
public final class Characters {
    public static final F<Character, String> toString = new F<Character, String>() { // from class: fj.function.Characters.1
        @Override // fj.F
        public String f(Character c) {
            return Character.toString(c.charValue());
        }
    };
    public static final F<Character, Boolean> isLowerCase = new F<Character, Boolean>() { // from class: fj.function.Characters.2
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isLowerCase(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isUpperCase = new F<Character, Boolean>() { // from class: fj.function.Characters.3
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isUpperCase(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isTitleCase = new F<Character, Boolean>() { // from class: fj.function.Characters.4
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isTitleCase(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isDigit = new F<Character, Boolean>() { // from class: fj.function.Characters.5
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isDigit(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isDefined = new F<Character, Boolean>() { // from class: fj.function.Characters.6
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isDefined(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isLetter = new F<Character, Boolean>() { // from class: fj.function.Characters.7
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isLetter(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isLetterOrDigit = new F<Character, Boolean>() { // from class: fj.function.Characters.8
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isLetterOrDigit(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isJavaIdentifierStart = new F<Character, Boolean>() { // from class: fj.function.Characters.9
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isJavaIdentifierStart(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isJavaIdentifierPart = new F<Character, Boolean>() { // from class: fj.function.Characters.10
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isJavaIdentifierPart(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isUnicodeIdentifierStart = new F<Character, Boolean>() { // from class: fj.function.Characters.11
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isUnicodeIdentifierStart(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isUnicodeIdentifierPart = new F<Character, Boolean>() { // from class: fj.function.Characters.12
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isUnicodeIdentifierPart(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isIdentifierIgnorable = new F<Character, Boolean>() { // from class: fj.function.Characters.13
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isIdentifierIgnorable(ch.charValue()));
        }
    };
    public static final F<Character, Character> toLowerCase = new F<Character, Character>() { // from class: fj.function.Characters.14
        @Override // fj.F
        public Character f(Character ch) {
            return Character.valueOf(Character.toLowerCase(ch.charValue()));
        }
    };
    public static final F<Character, Character> toUpperCase = new F<Character, Character>() { // from class: fj.function.Characters.15
        @Override // fj.F
        public Character f(Character ch) {
            return Character.valueOf(Character.toUpperCase(ch.charValue()));
        }
    };
    public static final F<Character, Character> toTitleCase = new F<Character, Character>() { // from class: fj.function.Characters.16
        @Override // fj.F
        public Character f(Character ch) {
            return Character.valueOf(Character.toTitleCase(ch.charValue()));
        }
    };
    public static final F<Character, F<Integer, Integer>> digit = Function.curry(new F2<Character, Integer, Integer>() { // from class: fj.function.Characters.17
        @Override // fj.F2
        public Integer f(Character ch, Integer radix) {
            return Integer.valueOf(Character.digit(ch.charValue(), radix.intValue()));
        }
    });
    public static final F<Character, Integer> getNumericValue = new F<Character, Integer>() { // from class: fj.function.Characters.18
        @Override // fj.F
        public Integer f(Character ch) {
            return Integer.valueOf(Character.getNumericValue(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isSpaceChar = new F<Character, Boolean>() { // from class: fj.function.Characters.19
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isSpaceChar(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isWhitespace = new F<Character, Boolean>() { // from class: fj.function.Characters.20
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isWhitespace(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isISOControl = new F<Character, Boolean>() { // from class: fj.function.Characters.21
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isISOControl(ch.charValue()));
        }
    };
    public static final F<Character, Integer> getType = new F<Character, Integer>() { // from class: fj.function.Characters.22
        @Override // fj.F
        public Integer f(Character ch) {
            return Integer.valueOf(Character.getType(ch.charValue()));
        }
    };
    public static final F<Character, Byte> getDirectionality = new F<Character, Byte>() { // from class: fj.function.Characters.23
        @Override // fj.F
        public Byte f(Character ch) {
            return Byte.valueOf(Character.getDirectionality(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isMirrored = new F<Character, Boolean>() { // from class: fj.function.Characters.24
        @Override // fj.F
        public Boolean f(Character ch) {
            return Boolean.valueOf(Character.isMirrored(ch.charValue()));
        }
    };
    public static final F<Character, Character> reverseBytes = new F<Character, Character>() { // from class: fj.function.Characters.25
        @Override // fj.F
        public Character f(Character ch) {
            return Character.valueOf(Character.reverseBytes(ch.charValue()));
        }
    };
    public static final F<Character, Boolean> isNewLine = new F<Character, Boolean>() { // from class: fj.function.Characters.26
        @Override // fj.F
        public Boolean f(Character c) {
            return Boolean.valueOf(c.charValue() == '\n');
        }
    };

    private Characters() {
        throw new UnsupportedOperationException();
    }
}
