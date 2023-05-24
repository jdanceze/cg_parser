package soot.baf;

import android.provider.UserDictionary;
import soot.G;
import soot.IntType;
import soot.Singletons;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/WordType.class */
public class WordType extends Type {
    public WordType(Singletons.Global g) {
    }

    public static WordType v() {
        return G.v().soot_baf_WordType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return IntType.HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return UserDictionary.Words.WORD;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        throw new RuntimeException("invalid switch case");
    }
}
