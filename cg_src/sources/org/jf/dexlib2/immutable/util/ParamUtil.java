package org.jf.dexlib2.immutable.util;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.immutable.ImmutableAnnotation;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/util/ParamUtil.class */
public class ParamUtil {
    /* JADX INFO: Access modifiers changed from: private */
    public static int findTypeEnd(@Nonnull String str, int index) {
        int i;
        int i2;
        char c = str.charAt(index);
        switch (c) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return index + 1;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException(String.format("Param string \"%s\" contains invalid type prefix: %s", str, Character.toString(c)));
            case 'L':
                do {
                    i2 = index;
                    index++;
                } while (str.charAt(i2) != ';');
                return index;
            case '[':
                do {
                    i = index;
                    index++;
                } while (str.charAt(i) != '[');
                return findTypeEnd(str, index);
        }
    }

    @Nonnull
    public static Iterable<ImmutableMethodParameter> parseParamString(@Nonnull final String params) {
        return new Iterable<ImmutableMethodParameter>() { // from class: org.jf.dexlib2.immutable.util.ParamUtil.1
            @Override // java.lang.Iterable
            public Iterator<ImmutableMethodParameter> iterator() {
                return new Iterator<ImmutableMethodParameter>() { // from class: org.jf.dexlib2.immutable.util.ParamUtil.1.1
                    private int index = 0;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.index < params.length();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImmutableMethodParameter next() {
                        int end = ParamUtil.findTypeEnd(params, this.index);
                        String ret = params.substring(this.index, end);
                        this.index = end;
                        return new ImmutableMethodParameter(ret, (ImmutableSet<? extends ImmutableAnnotation>) null, (String) null);
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
