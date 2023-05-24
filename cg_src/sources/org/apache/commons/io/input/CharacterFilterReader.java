package org.apache.commons.io.input;

import java.io.Reader;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/CharacterFilterReader.class */
public class CharacterFilterReader extends AbstractCharacterFilterReader {
    private final int skip;

    public CharacterFilterReader(Reader reader, int skip) {
        super(reader);
        this.skip = skip;
    }

    @Override // org.apache.commons.io.input.AbstractCharacterFilterReader
    protected boolean filter(int ch) {
        return ch == this.skip;
    }
}
