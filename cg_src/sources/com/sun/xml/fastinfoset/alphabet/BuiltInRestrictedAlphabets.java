package com.sun.xml.fastinfoset.alphabet;

import org.jvnet.fastinfoset.RestrictedAlphabet;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/alphabet/BuiltInRestrictedAlphabets.class */
public final class BuiltInRestrictedAlphabets {
    public static final char[][] table = new char[2];

    /* JADX WARN: Type inference failed for: r0v1, types: [char[], char[][]] */
    static {
        table[0] = RestrictedAlphabet.NUMERIC_CHARACTERS.toCharArray();
        table[1] = RestrictedAlphabet.DATE_TIME_CHARACTERS.toCharArray();
    }
}
