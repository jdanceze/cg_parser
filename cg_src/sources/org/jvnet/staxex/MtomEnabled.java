package org.jvnet.staxex;

import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/MtomEnabled.class */
public interface MtomEnabled {
    BinaryText addBinaryText(byte[] bArr);

    BinaryText addBinaryText(String str, byte[] bArr);

    BinaryText addBinaryText(String str, DataHandler dataHandler);
}
