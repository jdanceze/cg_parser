package org.jvnet.fastinfoset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/EncodingAlgorithm.class */
public interface EncodingAlgorithm {
    Object decodeFromBytes(byte[] bArr, int i, int i2) throws EncodingAlgorithmException;

    Object decodeFromInputStream(InputStream inputStream) throws EncodingAlgorithmException, IOException;

    void encodeToOutputStream(Object obj, OutputStream outputStream) throws EncodingAlgorithmException, IOException;

    Object convertFromCharacters(char[] cArr, int i, int i2) throws EncodingAlgorithmException;

    void convertToCharacters(Object obj, StringBuffer stringBuffer) throws EncodingAlgorithmException;
}
