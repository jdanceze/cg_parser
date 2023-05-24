package org.apache.commons.io.input;

import java.io.Reader;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/CharacterSetFilterReader.class */
public class CharacterSetFilterReader extends AbstractCharacterFilterReader {
    private static final Set<Integer> EMPTY_SET = Collections.emptySet();
    private final Set<Integer> skipSet;

    public CharacterSetFilterReader(Reader reader, Set<Integer> skip) {
        super(reader);
        this.skipSet = skip == null ? EMPTY_SET : Collections.unmodifiableSet(skip);
    }

    @Override // org.apache.commons.io.input.AbstractCharacterFilterReader
    protected boolean filter(int ch) {
        return this.skipSet.contains(Integer.valueOf(ch));
    }
}
