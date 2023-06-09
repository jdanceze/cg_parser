package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;
/* loaded from: gencallgraphv3.jar:commons-codec-1.3.jar:org/apache/commons/codec/net/BCodec.class */
public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
    private String charset;

    public BCodec() {
        this.charset = "UTF-8";
    }

    public BCodec(String charset) {
        this.charset = "UTF-8";
        this.charset = charset;
    }

    @Override // org.apache.commons.codec.net.RFC1522Codec
    protected String getEncoding() {
        return "B";
    }

    @Override // org.apache.commons.codec.net.RFC1522Codec
    protected byte[] doEncoding(byte[] bytes) throws EncoderException {
        if (bytes == null) {
            return null;
        }
        return Base64.encodeBase64(bytes);
    }

    @Override // org.apache.commons.codec.net.RFC1522Codec
    protected byte[] doDecoding(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        return Base64.decodeBase64(bytes);
    }

    public String encode(String value, String charset) throws EncoderException {
        if (value == null) {
            return null;
        }
        try {
            return encodeText(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage());
        }
    }

    @Override // org.apache.commons.codec.StringEncoder
    public String encode(String value) throws EncoderException {
        if (value == null) {
            return null;
        }
        return encode(value, getDefaultCharset());
    }

    @Override // org.apache.commons.codec.StringDecoder
    public String decode(String value) throws DecoderException {
        if (value == null) {
            return null;
        }
        try {
            return decodeText(value);
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage());
        }
    }

    @Override // org.apache.commons.codec.Encoder
    public Object encode(Object value) throws EncoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return encode((String) value);
        }
        throw new EncoderException(new StringBuffer().append("Objects of type ").append(value.getClass().getName()).append(" cannot be encoded using BCodec").toString());
    }

    @Override // org.apache.commons.codec.Decoder
    public Object decode(Object value) throws DecoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return decode((String) value);
        }
        throw new DecoderException(new StringBuffer().append("Objects of type ").append(value.getClass().getName()).append(" cannot be decoded using BCodec").toString());
    }

    public String getDefaultCharset() {
        return this.charset;
    }
}
