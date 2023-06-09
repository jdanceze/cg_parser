package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
/* loaded from: gencallgraphv3.jar:commons-codec-1.3.jar:org/apache/commons/codec/net/RFC1522Codec.class */
abstract class RFC1522Codec {
    protected abstract String getEncoding();

    protected abstract byte[] doEncoding(byte[] bArr) throws EncoderException;

    protected abstract byte[] doDecoding(byte[] bArr) throws DecoderException;

    /* JADX INFO: Access modifiers changed from: protected */
    public String encodeText(String text, String charset) throws EncoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("=?");
        buffer.append(charset);
        buffer.append('?');
        buffer.append(getEncoding());
        buffer.append('?');
        byte[] rawdata = doEncoding(text.getBytes(charset));
        buffer.append(new String(rawdata, "US-ASCII"));
        buffer.append("?=");
        return buffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String decodeText(String text) throws DecoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        if (!text.startsWith("=?") || !text.endsWith("?=")) {
            throw new DecoderException("RFC 1522 violation: malformed encoded content");
        }
        int termnator = text.length() - 2;
        int to = text.indexOf(TypeDescription.Generic.OfWildcardType.SYMBOL, 2);
        if (to == -1 || to == termnator) {
            throw new DecoderException("RFC 1522 violation: charset token not found");
        }
        String charset = text.substring(2, to);
        if (charset.equals("")) {
            throw new DecoderException("RFC 1522 violation: charset not specified");
        }
        int from = to + 1;
        int to2 = text.indexOf(TypeDescription.Generic.OfWildcardType.SYMBOL, from);
        if (to2 == -1 || to2 == termnator) {
            throw new DecoderException("RFC 1522 violation: encoding token not found");
        }
        String encoding = text.substring(from, to2);
        if (!getEncoding().equalsIgnoreCase(encoding)) {
            throw new DecoderException(new StringBuffer().append("This codec cannot decode ").append(encoding).append(" encoded content").toString());
        }
        int from2 = to2 + 1;
        byte[] data = text.substring(from2, text.indexOf(TypeDescription.Generic.OfWildcardType.SYMBOL, from2)).getBytes("US-ASCII");
        return new String(doDecoding(data), charset);
    }
}
