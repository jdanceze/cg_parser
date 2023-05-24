package net.bytebuddy.utility;

import java.util.Random;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/RandomString.class */
public class RandomString {
    public static final int DEFAULT_LENGTH = 8;
    private static final char[] SYMBOL;
    private static final int KEY_BITS;
    private final Random random;
    private final int length;

    static {
        StringBuilder symbol = new StringBuilder();
        char c = '0';
        while (true) {
            char character = c;
            if (character > '9') {
                break;
            }
            symbol.append(character);
            c = (char) (character + 1);
        }
        char c2 = 'a';
        while (true) {
            char character2 = c2;
            if (character2 > 'z') {
                break;
            }
            symbol.append(character2);
            c2 = (char) (character2 + 1);
        }
        char c3 = 'A';
        while (true) {
            char character3 = c3;
            if (character3 > 'Z') {
                break;
            }
            symbol.append(character3);
            c3 = (char) (character3 + 1);
        }
        SYMBOL = symbol.toString().toCharArray();
        int bits = 32 - Integer.numberOfLeadingZeros(SYMBOL.length);
        KEY_BITS = bits - (Integer.bitCount(SYMBOL.length) == bits ? 0 : 1);
    }

    public RandomString() {
        this(8);
    }

    public RandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("A random string's length cannot be zero or negative");
        }
        this.length = length;
        this.random = new Random();
    }

    public static String make() {
        return make(8);
    }

    public static String make(int length) {
        return new RandomString(length).nextString();
    }

    public static String hashOf(int value) {
        char[] buffer = new char[(32 / KEY_BITS) + (32 % KEY_BITS == 0 ? 0 : 1)];
        for (int index = 0; index < buffer.length; index++) {
            buffer[index] = SYMBOL[(value >>> (index * KEY_BITS)) & ((-1) >>> (32 - KEY_BITS))];
        }
        return new String(buffer);
    }

    public String nextString() {
        char[] buffer = new char[this.length];
        for (int index = 0; index < this.length; index++) {
            buffer[index] = SYMBOL[this.random.nextInt(SYMBOL.length)];
        }
        return new String(buffer);
    }
}
