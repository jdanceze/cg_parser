package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.input.ObservableInputStream;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/MessageDigestCalculatingInputStream.class */
public class MessageDigestCalculatingInputStream extends ObservableInputStream {
    private final MessageDigest messageDigest;

    /* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/MessageDigestCalculatingInputStream$MessageDigestMaintainingObserver.class */
    public static class MessageDigestMaintainingObserver extends ObservableInputStream.Observer {
        private final MessageDigest messageDigest;

        public MessageDigestMaintainingObserver(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override // org.apache.commons.io.input.ObservableInputStream.Observer
        public void data(int input) throws IOException {
            this.messageDigest.update((byte) input);
        }

        @Override // org.apache.commons.io.input.ObservableInputStream.Observer
        public void data(byte[] input, int offset, int length) throws IOException {
            this.messageDigest.update(input, offset, length);
        }
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream, MessageDigest MessageDigest) {
        super(inputStream);
        this.messageDigest = MessageDigest;
        add(new MessageDigestMaintainingObserver(MessageDigest));
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream, String algorithm) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance(algorithm));
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance("MD5"));
    }

    public MessageDigest getMessageDigest() {
        return this.messageDigest;
    }
}
