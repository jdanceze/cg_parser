package com.google.protobuf;

import com.sun.xml.fastinfoset.EncodingConstants;
import dalvik.bytecode.Opcodes;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8.class */
public final class Utf8 {
    private static final Processor processor;
    private static final long ASCII_MASK_LONG = -9187201950435737472L;
    static final int MAX_BYTES_PER_CHAR = 3;
    static final int COMPLETE = 0;
    static final int MALFORMED = -1;
    private static final int UNSAFE_COUNT_ASCII_THRESHOLD = 16;

    static {
        processor = (!UnsafeProcessor.isAvailable() || Android.isOnAndroidDevice()) ? new SafeProcessor() : new UnsafeProcessor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(byte[] bytes) {
        return processor.isValidUtf8(bytes, 0, bytes.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return processor.isValidUtf8(bytes, index, limit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
        return processor.partialIsValidUtf8(state, bytes, index, limit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int byte1) {
        if (byte1 > -12) {
            return -1;
        }
        return byte1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int byte1, int byte2) {
        if (byte1 > -12 || byte2 > -65) {
            return -1;
        }
        return byte1 ^ (byte2 << 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int byte1, int byte2, int byte3) {
        if (byte1 > -12 || byte2 > -65 || byte3 > -65) {
            return -1;
        }
        return (byte1 ^ (byte2 << 8)) ^ (byte3 << 16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(byte[] bytes, int index, int limit) {
        byte b = bytes[index - 1];
        switch (limit - index) {
            case 0:
                return incompleteStateFor(b);
            case 1:
                return incompleteStateFor(b, bytes[index]);
            case 2:
                return incompleteStateFor(b, bytes[index], bytes[index + 1]);
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(ByteBuffer buffer, int byte1, int index, int remaining) {
        switch (remaining) {
            case 0:
                return incompleteStateFor(byte1);
            case 1:
                return incompleteStateFor(byte1, buffer.get(index));
            case 2:
                return incompleteStateFor(byte1, buffer.get(index), buffer.get(index + 1));
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8$UnpairedSurrogateException.class */
    public static class UnpairedSurrogateException extends IllegalArgumentException {
        /* JADX INFO: Access modifiers changed from: package-private */
        public UnpairedSurrogateException(int index, int length) {
            super("Unpaired surrogate at index " + index + " of " + length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encodedLength(CharSequence sequence) {
        int utf16Length = sequence.length();
        int utf8Length = utf16Length;
        int i = 0;
        while (i < utf16Length && sequence.charAt(i) < 128) {
            i++;
        }
        while (true) {
            if (i < utf16Length) {
                char c = sequence.charAt(i);
                if (c < 2048) {
                    utf8Length += (127 - c) >>> 31;
                    i++;
                } else {
                    utf8Length += encodedLengthGeneral(sequence, i);
                    break;
                }
            } else {
                break;
            }
        }
        if (utf8Length < utf16Length) {
            throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH));
        }
        return utf8Length;
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int utf16Length = sequence.length();
        int utf8Length = 0;
        int i = start;
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c < 2048) {
                utf8Length += (127 - c) >>> 31;
            } else {
                utf8Length += 2;
                if (55296 <= c && c <= 57343) {
                    int cp = Character.codePointAt(sequence, i);
                    if (cp < 65536) {
                        throw new UnpairedSurrogateException(i, utf16Length);
                    }
                    i++;
                }
            }
            i++;
        }
        return utf8Length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encode(CharSequence in, byte[] out, int offset, int length) {
        return processor.encodeUtf8(in, out, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(ByteBuffer buffer) {
        return processor.isValidUtf8(buffer, buffer.position(), buffer.remaining());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
        return processor.partialIsValidUtf8(state, buffer, index, limit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeUtf8(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
        return processor.decodeUtf8(buffer, index, size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
        return processor.decodeUtf8(bytes, index, size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void encodeUtf8(CharSequence in, ByteBuffer out) {
        processor.encodeUtf8(in, out);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int estimateConsecutiveAscii(ByteBuffer buffer, int index, int limit) {
        int i = index;
        int lim = limit - 7;
        while (i < lim && (buffer.getLong(i) & ASCII_MASK_LONG) == 0) {
            i += 8;
        }
        return i - index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8$Processor.class */
    public static abstract class Processor {
        abstract int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3);

        abstract int partialIsValidUtf8Direct(int i, ByteBuffer byteBuffer, int i2, int i3);

        abstract String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        abstract String decodeUtf8Direct(ByteBuffer byteBuffer, int i, int i2) throws InvalidProtocolBufferException;

        abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2);

        abstract void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer);

        Processor() {
        }

        final boolean isValidUtf8(byte[] bytes, int index, int limit) {
            return partialIsValidUtf8(0, bytes, index, limit) == 0;
        }

        final boolean isValidUtf8(ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8(0, buffer, index, limit) == 0;
        }

        final int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
            if (buffer.hasArray()) {
                int offset = buffer.arrayOffset();
                return partialIsValidUtf8(state, buffer.array(), offset + index, offset + limit);
            } else if (buffer.isDirect()) {
                return partialIsValidUtf8Direct(state, buffer, index, limit);
            } else {
                return partialIsValidUtf8Default(state, buffer, index, limit);
            }
        }

        final int partialIsValidUtf8Default(int state, ByteBuffer buffer, int index, int limit) {
            if (state != 0) {
                if (index >= limit) {
                    return state;
                }
                byte byte1 = (byte) state;
                if (byte1 < -32) {
                    if (byte1 >= -62) {
                        index++;
                        if (buffer.get(index) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    byte byte2 = (byte) ((state >> 8) ^ (-1));
                    if (byte2 == 0) {
                        index++;
                        byte2 = buffer.get(index);
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                    }
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 != -19 || byte2 < -96) {
                        int i = index;
                        index++;
                        if (buffer.get(i) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    byte byte22 = (byte) ((state >> 8) ^ (-1));
                    byte byte3 = 0;
                    if (byte22 == 0) {
                        index++;
                        byte22 = buffer.get(index);
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte22);
                        }
                    } else {
                        byte3 = (byte) (state >> 16);
                    }
                    if (byte3 == 0) {
                        int i2 = index;
                        index++;
                        byte3 = buffer.get(i2);
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte22, byte3);
                        }
                    }
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0 && byte3 <= -65) {
                        int i3 = index;
                        index++;
                        if (buffer.get(i3) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            }
            return partialIsValidUtf8(buffer, index, limit);
        }

        private static int partialIsValidUtf8(ByteBuffer buffer, int index, int limit) {
            int index2 = index + Utf8.estimateConsecutiveAscii(buffer, index, limit);
            while (index2 < limit) {
                int i = index2;
                index2++;
                int byte1 = buffer.get(i);
                if (byte1 < 0) {
                    if (byte1 < -32) {
                        if (index2 >= limit) {
                            return byte1;
                        }
                        if (byte1 < -62 || buffer.get(index2) > -65) {
                            return -1;
                        }
                        index2++;
                    } else if (byte1 < -16) {
                        if (index2 >= limit - 1) {
                            return Utf8.incompleteStateFor(buffer, byte1, index2, limit - index2);
                        }
                        int index3 = index2 + 1;
                        byte byte2 = buffer.get(index2);
                        if (byte2 > -65) {
                            return -1;
                        }
                        if (byte1 != -32 || byte2 >= -96) {
                            if ((byte1 == -19 && byte2 >= -96) || buffer.get(index3) > -65) {
                                return -1;
                            }
                            index2 = index3 + 1;
                        } else {
                            return -1;
                        }
                    } else if (index2 >= limit - 2) {
                        return Utf8.incompleteStateFor(buffer, byte1, index2, limit - index2);
                    } else {
                        int index4 = index2 + 1;
                        int byte22 = buffer.get(index2);
                        if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0) {
                            int index5 = index4 + 1;
                            if (buffer.get(index4) <= -65) {
                                index2 = index5 + 1;
                                if (buffer.get(index5) > -65) {
                                    return -1;
                                }
                            } else {
                                return -1;
                            }
                        } else {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }

        final String decodeUtf8(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            if (buffer.hasArray()) {
                int offset = buffer.arrayOffset();
                return decodeUtf8(buffer.array(), offset + index, size);
            } else if (buffer.isDirect()) {
                return decodeUtf8Direct(buffer, index, size);
            } else {
                return decodeUtf8Default(buffer, index, size);
            }
        }

        final String decodeUtf8Default(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            if ((index | size | ((buffer.limit() - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(size)));
            }
            int offset = index;
            int limit = offset + size;
            char[] resultArr = new char[size];
            int resultPos = 0;
            while (offset < limit) {
                byte b = buffer.get(offset);
                if (!DecodeUtil.isOneByte(b)) {
                    break;
                }
                offset++;
                int i = resultPos;
                resultPos++;
                DecodeUtil.handleOneByte(b, resultArr, i);
            }
            while (offset < limit) {
                int i2 = offset;
                offset++;
                byte byte1 = buffer.get(i2);
                if (DecodeUtil.isOneByte(byte1)) {
                    int i3 = resultPos;
                    resultPos++;
                    DecodeUtil.handleOneByte(byte1, resultArr, i3);
                    while (offset < limit) {
                        byte b2 = buffer.get(offset);
                        if (!DecodeUtil.isOneByte(b2)) {
                            break;
                        }
                        offset++;
                        int i4 = resultPos;
                        resultPos++;
                        DecodeUtil.handleOneByte(b2, resultArr, i4);
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (offset >= limit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    offset++;
                    int i5 = resultPos;
                    resultPos++;
                    DecodeUtil.handleTwoBytes(byte1, buffer.get(offset), resultArr, i5);
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (offset >= limit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int offset2 = offset + 1;
                    byte b3 = buffer.get(offset);
                    offset = offset2 + 1;
                    int i6 = resultPos;
                    resultPos++;
                    DecodeUtil.handleThreeBytes(byte1, b3, buffer.get(offset2), resultArr, i6);
                } else if (offset >= limit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int offset3 = offset + 1;
                    byte b4 = buffer.get(offset);
                    int offset4 = offset3 + 1;
                    byte b5 = buffer.get(offset3);
                    offset = offset4 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, b5, buffer.get(offset4), resultArr, resultPos);
                    resultPos = resultPos + 1 + 1;
                }
            }
            return new String(resultArr, 0, resultPos);
        }

        final void encodeUtf8(CharSequence in, ByteBuffer out) {
            if (out.hasArray()) {
                int offset = out.arrayOffset();
                int endIndex = Utf8.encode(in, out.array(), offset + out.position(), out.remaining());
                out.position(endIndex - offset);
            } else if (out.isDirect()) {
                encodeUtf8Direct(in, out);
            } else {
                encodeUtf8Default(in, out);
            }
        }

        final void encodeUtf8Default(CharSequence in, ByteBuffer out) {
            int inLength = in.length();
            int outIx = out.position();
            int inIx = 0;
            while (inIx < inLength) {
                try {
                    char c = in.charAt(inIx);
                    if (c >= 128) {
                        break;
                    }
                    out.put(outIx + inIx, (byte) c);
                    inIx++;
                } catch (IndexOutOfBoundsException e) {
                    int badWriteIndex = out.position() + Math.max(inIx, (outIx - out.position()) + 1);
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(inIx) + " at index " + badWriteIndex);
                }
            }
            if (inIx == inLength) {
                out.position(outIx + inIx);
                return;
            }
            int outIx2 = outIx + inIx;
            while (inIx < inLength) {
                char c2 = in.charAt(inIx);
                if (c2 < 128) {
                    out.put(outIx2, (byte) c2);
                } else if (c2 < 2048) {
                    int i = outIx2;
                    outIx2++;
                    out.put(i, (byte) (192 | (c2 >>> 6)));
                    out.put(outIx2, (byte) (128 | ('?' & c2)));
                } else if (c2 < 55296 || 57343 < c2) {
                    int i2 = outIx2;
                    int outIx3 = outIx2 + 1;
                    out.put(i2, (byte) (224 | (c2 >>> '\f')));
                    outIx2 = outIx3 + 1;
                    out.put(outIx3, (byte) (128 | (63 & (c2 >>> 6))));
                    out.put(outIx2, (byte) (128 | ('?' & c2)));
                } else {
                    if (inIx + 1 != inLength) {
                        inIx++;
                        char low = in.charAt(inIx);
                        if (Character.isSurrogatePair(c2, low)) {
                            int codePoint = Character.toCodePoint(c2, low);
                            int i3 = outIx2;
                            int outIx4 = outIx2 + 1;
                            out.put(i3, (byte) (240 | (codePoint >>> 18)));
                            int outIx5 = outIx4 + 1;
                            out.put(outIx4, (byte) (128 | (63 & (codePoint >>> 12))));
                            outIx2 = outIx5 + 1;
                            out.put(outIx5, (byte) (128 | (63 & (codePoint >>> 6))));
                            out.put(outIx2, (byte) (128 | (63 & codePoint)));
                        }
                    }
                    throw new UnpairedSurrogateException(inIx, inLength);
                }
                inIx++;
                outIx2++;
            }
            out.position(outIx2);
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8$SafeProcessor.class */
    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        @Override // com.google.protobuf.Utf8.Processor
        int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
            if (state != 0) {
                if (index >= limit) {
                    return state;
                }
                int byte1 = (byte) state;
                if (byte1 < -32) {
                    if (byte1 < -62) {
                        return -1;
                    }
                    index++;
                    if (bytes[index] > -65) {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    int byte2 = (byte) ((state >> 8) ^ (-1));
                    if (byte2 == 0) {
                        index++;
                        byte2 = bytes[index];
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                    }
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 == -19 && byte2 >= -96) {
                        return -1;
                    }
                    int i = index;
                    index++;
                    if (bytes[i] > -65) {
                        return -1;
                    }
                } else {
                    int byte22 = (byte) ((state >> 8) ^ (-1));
                    int byte3 = 0;
                    if (byte22 == 0) {
                        index++;
                        byte22 = bytes[index];
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte22);
                        }
                    } else {
                        byte3 = (byte) (state >> 16);
                    }
                    if (byte3 == 0) {
                        int i2 = index;
                        index++;
                        byte3 = bytes[i2];
                        if (index >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte22, byte3);
                        }
                    }
                    if (byte22 > -65 || (((byte1 << 28) + (byte22 - (-112))) >> 30) != 0 || byte3 > -65) {
                        return -1;
                    }
                    int i3 = index;
                    index++;
                    if (bytes[i3] > -65) {
                        return -1;
                    }
                }
            }
            return partialIsValidUtf8(bytes, index, limit);
        }

        @Override // com.google.protobuf.Utf8.Processor
        int partialIsValidUtf8Direct(int state, ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8Default(state, buffer, index, limit);
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
            if ((index | size | ((bytes.length - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bytes.length), Integer.valueOf(index), Integer.valueOf(size)));
            }
            int offset = index;
            int limit = offset + size;
            char[] resultArr = new char[size];
            int resultPos = 0;
            while (offset < limit) {
                byte b = bytes[offset];
                if (!DecodeUtil.isOneByte(b)) {
                    break;
                }
                offset++;
                int i = resultPos;
                resultPos++;
                DecodeUtil.handleOneByte(b, resultArr, i);
            }
            while (offset < limit) {
                int i2 = offset;
                offset++;
                byte byte1 = bytes[i2];
                if (DecodeUtil.isOneByte(byte1)) {
                    int i3 = resultPos;
                    resultPos++;
                    DecodeUtil.handleOneByte(byte1, resultArr, i3);
                    while (offset < limit) {
                        byte b2 = bytes[offset];
                        if (!DecodeUtil.isOneByte(b2)) {
                            break;
                        }
                        offset++;
                        int i4 = resultPos;
                        resultPos++;
                        DecodeUtil.handleOneByte(b2, resultArr, i4);
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (offset >= limit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    offset++;
                    int i5 = resultPos;
                    resultPos++;
                    DecodeUtil.handleTwoBytes(byte1, bytes[offset], resultArr, i5);
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (offset >= limit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int offset2 = offset + 1;
                    byte b3 = bytes[offset];
                    offset = offset2 + 1;
                    int i6 = resultPos;
                    resultPos++;
                    DecodeUtil.handleThreeBytes(byte1, b3, bytes[offset2], resultArr, i6);
                } else if (offset >= limit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int offset3 = offset + 1;
                    byte b4 = bytes[offset];
                    int offset4 = offset3 + 1;
                    byte b5 = bytes[offset3];
                    offset = offset4 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, b5, bytes[offset4], resultArr, resultPos);
                    resultPos = resultPos + 1 + 1;
                }
            }
            return new String(resultArr, 0, resultPos);
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8Direct(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            return decodeUtf8Default(buffer, index, size);
        }

        @Override // com.google.protobuf.Utf8.Processor
        int encodeUtf8(CharSequence in, byte[] out, int offset, int length) {
            char c;
            int utf16Length = in.length();
            int i = 0;
            int limit = offset + length;
            while (i < utf16Length && i + offset < limit && (c = in.charAt(i)) < 128) {
                out[offset + i] = (byte) c;
                i++;
            }
            if (i == utf16Length) {
                return offset + utf16Length;
            }
            int j = offset + i;
            while (i < utf16Length) {
                char c2 = in.charAt(i);
                if (c2 < 128 && j < limit) {
                    int i2 = j;
                    j++;
                    out[i2] = (byte) c2;
                } else if (c2 < 2048 && j <= limit - 2) {
                    int i3 = j;
                    int j2 = j + 1;
                    out[i3] = (byte) (960 | (c2 >>> 6));
                    j = j2 + 1;
                    out[j2] = (byte) (128 | ('?' & c2));
                } else if ((c2 < 55296 || 57343 < c2) && j <= limit - 3) {
                    int i4 = j;
                    int j3 = j + 1;
                    out[i4] = (byte) (480 | (c2 >>> '\f'));
                    int j4 = j3 + 1;
                    out[j3] = (byte) (128 | (63 & (c2 >>> 6)));
                    j = j4 + 1;
                    out[j4] = (byte) (128 | ('?' & c2));
                } else if (j <= limit - 4) {
                    if (i + 1 != in.length()) {
                        i++;
                        char low = in.charAt(i);
                        if (Character.isSurrogatePair(c2, low)) {
                            int codePoint = Character.toCodePoint(c2, low);
                            int i5 = j;
                            int j5 = j + 1;
                            out[i5] = (byte) (240 | (codePoint >>> 18));
                            int j6 = j5 + 1;
                            out[j5] = (byte) (128 | (63 & (codePoint >>> 12)));
                            int j7 = j6 + 1;
                            out[j6] = (byte) (128 | (63 & (codePoint >>> 6)));
                            j = j7 + 1;
                            out[j7] = (byte) (128 | (63 & codePoint));
                        }
                    }
                    throw new UnpairedSurrogateException(i - 1, utf16Length);
                } else if (55296 <= c2 && c2 <= 57343 && (i + 1 == in.length() || !Character.isSurrogatePair(c2, in.charAt(i + 1)))) {
                    throw new UnpairedSurrogateException(i, utf16Length);
                } else {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + c2 + " at index " + j);
                }
                i++;
            }
            return j;
        }

        @Override // com.google.protobuf.Utf8.Processor
        void encodeUtf8Direct(CharSequence in, ByteBuffer out) {
            encodeUtf8Default(in, out);
        }

        private static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
            while (index < limit && bytes[index] >= 0) {
                index++;
            }
            if (index >= limit) {
                return 0;
            }
            return partialIsValidUtf8NonAscii(bytes, index, limit);
        }

        private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
            while (index < limit) {
                int i = index;
                index++;
                byte b = bytes[i];
                if (b < 0) {
                    if (b < -32) {
                        if (index >= limit) {
                            return b;
                        }
                        if (b < -62) {
                            return -1;
                        }
                        index++;
                        if (bytes[index] > -65) {
                            return -1;
                        }
                    } else if (b < -16) {
                        if (index >= limit - 1) {
                            return Utf8.incompleteStateFor(bytes, index, limit);
                        }
                        int index2 = index + 1;
                        byte b2 = bytes[index];
                        if (b2 > -65) {
                            return -1;
                        }
                        if (b == -32 && b2 < -96) {
                            return -1;
                        }
                        if (b == -19 && b2 >= -96) {
                            return -1;
                        }
                        index = index2 + 1;
                        if (bytes[index2] > -65) {
                            return -1;
                        }
                    } else if (index >= limit - 2) {
                        return Utf8.incompleteStateFor(bytes, index, limit);
                    } else {
                        int index3 = index + 1;
                        byte b3 = bytes[index];
                        if (b3 > -65 || (((b << 28) + (b3 - (-112))) >> 30) != 0) {
                            return -1;
                        }
                        int index4 = index3 + 1;
                        if (bytes[index3] > -65) {
                            return -1;
                        }
                        index = index4 + 1;
                        if (bytes[index4] > -65) {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8$UnsafeProcessor.class */
    static final class UnsafeProcessor extends Processor {
        UnsafeProcessor() {
        }

        static boolean isAvailable() {
            return UnsafeUtil.hasUnsafeArrayOperations() && UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.Utf8.Processor
        int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
            if ((index | limit | (bytes.length - limit)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bytes.length), Integer.valueOf(index), Integer.valueOf(limit)));
            }
            long offset = index;
            long offsetLimit = limit;
            if (state != 0) {
                if (offset >= offsetLimit) {
                    return state;
                }
                int byte1 = (byte) state;
                if (byte1 < -32) {
                    if (byte1 >= -62) {
                        offset++;
                        if (UnsafeUtil.getByte(bytes, (long) bytes) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    int byte2 = (byte) ((state >> 8) ^ (-1));
                    if (byte2 == 0) {
                        offset++;
                        byte2 = UnsafeUtil.getByte(bytes, (long) bytes);
                        if (offset >= offsetLimit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                    }
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 != -19 || byte2 < -96) {
                        offset++;
                        if (UnsafeUtil.getByte(bytes, (long) bytes) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    int byte22 = (byte) ((state >> 8) ^ (-1));
                    int byte3 = 0;
                    if (byte22 == 0) {
                        offset++;
                        byte22 = UnsafeUtil.getByte(bytes, (long) bytes);
                        if (offset >= offsetLimit) {
                            return Utf8.incompleteStateFor(byte1, byte22);
                        }
                    } else {
                        byte3 = (byte) (state >> 16);
                    }
                    if (byte3 == 0) {
                        offset++;
                        byte3 = UnsafeUtil.getByte(bytes, (long) bytes);
                        if (offset >= offsetLimit) {
                            return Utf8.incompleteStateFor(byte1, byte22, byte3);
                        }
                    }
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0 && byte3 <= -65) {
                        offset++;
                        if (UnsafeUtil.getByte(bytes, (long) bytes) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            }
            return partialIsValidUtf8(bytes, offset, (int) (offsetLimit - offset));
        }

        @Override // com.google.protobuf.Utf8.Processor
        int partialIsValidUtf8Direct(int state, ByteBuffer buffer, int index, int limit) {
            if ((index | limit | (buffer.limit() - limit)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(limit)));
            }
            long address = UnsafeUtil.addressOffset(buffer) + index;
            long addressLimit = address + (limit - index);
            if (state != 0) {
                if (address >= addressLimit) {
                    return state;
                }
                int byte1 = (byte) state;
                if (byte1 < -32) {
                    if (byte1 >= -62) {
                        address++;
                        if (UnsafeUtil.getByte(address) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    int byte2 = (byte) ((state >> 8) ^ (-1));
                    if (byte2 == 0) {
                        address++;
                        byte2 = UnsafeUtil.getByte(address);
                        if (address >= addressLimit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                    }
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 != -19 || byte2 < -96) {
                        long j = address;
                        address = j + 1;
                        if (UnsafeUtil.getByte(j) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    int byte22 = (byte) ((state >> 8) ^ (-1));
                    int byte3 = 0;
                    if (byte22 == 0) {
                        address++;
                        byte22 = UnsafeUtil.getByte(address);
                        if (address >= addressLimit) {
                            return Utf8.incompleteStateFor(byte1, byte22);
                        }
                    } else {
                        byte3 = (byte) (state >> 16);
                    }
                    if (byte3 == 0) {
                        long j2 = address;
                        address = j2 + 1;
                        byte3 = UnsafeUtil.getByte(j2);
                        if (address >= addressLimit) {
                            return Utf8.incompleteStateFor(byte1, byte22, byte3);
                        }
                    }
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0 && byte3 <= -65) {
                        long j3 = address;
                        address = j3 + 1;
                        if (UnsafeUtil.getByte(j3) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            }
            return partialIsValidUtf8(address, (int) (addressLimit - address));
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
            String s = new String(bytes, index, size, Internal.UTF_8);
            if (!s.contains("�")) {
                return s;
            }
            if (Arrays.equals(s.getBytes(Internal.UTF_8), Arrays.copyOfRange(bytes, index, index + size))) {
                return s;
            }
            throw InvalidProtocolBufferException.invalidUtf8();
        }

        /* JADX WARN: Type inference failed for: r3v15, types: [byte] */
        /* JADX WARN: Type inference failed for: r3v2 */
        /* JADX WARN: Type inference failed for: r3v4, types: [int] */
        /* JADX WARN: Type inference failed for: r3v9, types: [char[]] */
        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8Direct(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            ?? r3;
            if ((index | size | ((buffer.limit() - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(size)));
            }
            long address = UnsafeUtil.addressOffset(buffer) + index;
            long addressLimit = address + size;
            char[] resultArr = new char[size];
            int resultPos = 0;
            while (address < addressLimit) {
                byte b = UnsafeUtil.getByte(address);
                if (!DecodeUtil.isOneByte(b)) {
                    break;
                }
                address++;
                int i = resultPos;
                resultPos++;
                DecodeUtil.handleOneByte(b, resultArr, i);
            }
            while (address < addressLimit) {
                long j = address;
                address = j + 1;
                byte byte1 = UnsafeUtil.getByte(j);
                if (DecodeUtil.isOneByte(byte1)) {
                    int i2 = resultPos;
                    resultPos++;
                    DecodeUtil.handleOneByte(byte1, resultArr, i2);
                    while (address < addressLimit) {
                        byte b2 = UnsafeUtil.getByte(address);
                        if (!DecodeUtil.isOneByte(b2)) {
                            break;
                        }
                        address++;
                        int i3 = resultPos;
                        resultPos++;
                        DecodeUtil.handleOneByte(b2, resultArr, i3);
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (address >= addressLimit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    address++;
                    r3 = resultPos;
                    resultPos++;
                    DecodeUtil.handleTwoBytes(byte1, UnsafeUtil.getByte(byte1), resultArr, r3);
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (address >= addressLimit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    long address2 = address + 1;
                    address = address2 + 1;
                    r3 = resultArr;
                    int i4 = resultPos;
                    resultPos++;
                    DecodeUtil.handleThreeBytes(byte1, UnsafeUtil.getByte(r3), UnsafeUtil.getByte(address2), r3, i4);
                } else if (address >= addressLimit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    long address3 = address + 1;
                    byte b3 = UnsafeUtil.getByte(r3);
                    long address4 = address3 + 1;
                    byte b4 = UnsafeUtil.getByte(address3);
                    address = address4 + 1;
                    r3 = UnsafeUtil.getByte(address4);
                    DecodeUtil.handleFourBytes(byte1, b3, b4, r3, resultArr, resultPos);
                    resultPos = resultPos + 1 + 1;
                }
            }
            return new String(resultArr, 0, resultPos);
        }

        /* JADX WARN: Type inference failed for: r2v13, types: [long, byte[]] */
        /* JADX WARN: Type inference failed for: r2v37, types: [long, byte[]] */
        /* JADX WARN: Type inference failed for: r2v42, types: [long, byte[]] */
        /* JADX WARN: Type inference failed for: r2v47, types: [long, byte[]] */
        /* JADX WARN: Type inference failed for: r2v58, types: [long, byte[]] */
        /* JADX WARN: Type inference failed for: r2v8, types: [long, byte[]] */
        @Override // com.google.protobuf.Utf8.Processor
        int encodeUtf8(CharSequence in, byte[] out, int offset, int length) {
            char c;
            long outIx = offset;
            long outLimit = outIx + length;
            int inLimit = in.length();
            if (inLimit > length || out.length - length < offset) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(inLimit - 1) + " at index " + (offset + length));
            }
            int inIx = 0;
            while (inIx < inLimit && (c = in.charAt(inIx)) < 128) {
                long j = outIx;
                outIx = j + 1;
                UnsafeUtil.putByte(out, j, (byte) c);
                inIx++;
            }
            if (inIx == inLimit) {
                return (int) outIx;
            }
            while (inIx < inLimit) {
                char c2 = in.charAt(inIx);
                if (c2 < 128 && outIx < outLimit) {
                    long j2 = outIx;
                    outIx = j2 + 1;
                    UnsafeUtil.putByte(out, j2, (byte) c2);
                } else if (c2 < 2048 && outIx <= outLimit - 2) {
                    long j3 = outIx;
                    ?? r2 = j3 + 1;
                    UnsafeUtil.putByte(out, j3, (byte) (960 | (c2 >>> 6)));
                    outIx = r2 + 1;
                    UnsafeUtil.putByte((byte[]) r2, (long) r2, (byte) (128 | ('?' & c2)));
                } else if ((c2 < 55296 || 57343 < c2) && outIx <= outLimit - 3) {
                    long j4 = outIx;
                    ?? r22 = j4 + 1;
                    UnsafeUtil.putByte(out, j4, (byte) (480 | (c2 >>> '\f')));
                    ?? r23 = r22 + 1;
                    UnsafeUtil.putByte((byte[]) r22, (long) r22, (byte) (128 | (63 & (c2 >>> 6))));
                    outIx = r23 + 1;
                    UnsafeUtil.putByte((byte[]) r23, (long) r23, (byte) (128 | ('?' & c2)));
                } else if (outIx <= outLimit - 4) {
                    if (inIx + 1 != inLimit) {
                        inIx++;
                        char low = in.charAt(inIx);
                        if (Character.isSurrogatePair(c2, low)) {
                            int codePoint = Character.toCodePoint(c2, low);
                            long j5 = outIx;
                            ?? r24 = j5 + 1;
                            UnsafeUtil.putByte(out, j5, (byte) (240 | (codePoint >>> 18)));
                            ?? r25 = r24 + 1;
                            UnsafeUtil.putByte((byte[]) r24, (long) r24, (byte) (128 | (63 & (codePoint >>> 12))));
                            ?? r26 = r25 + 1;
                            UnsafeUtil.putByte((byte[]) r25, (long) r25, (byte) (128 | (63 & (codePoint >>> 6))));
                            outIx = r26 + 1;
                            UnsafeUtil.putByte((byte[]) r26, (long) r26, (byte) (128 | (63 & codePoint)));
                        }
                    }
                    throw new UnpairedSurrogateException(inIx - 1, inLimit);
                } else if (55296 <= c2 && c2 <= 57343 && (inIx + 1 == inLimit || !Character.isSurrogatePair(c2, in.charAt(inIx + 1)))) {
                    throw new UnpairedSurrogateException(inIx, inLimit);
                } else {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + c2 + " at index " + outIx);
                }
                inIx++;
            }
            return (int) outIx;
        }

        @Override // com.google.protobuf.Utf8.Processor
        void encodeUtf8Direct(CharSequence in, ByteBuffer out) {
            char c;
            long address = UnsafeUtil.addressOffset(out);
            long outIx = address + out.position();
            long outLimit = address + out.limit();
            int inLimit = in.length();
            if (inLimit > outLimit - outIx) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(inLimit - 1) + " at index " + out.limit());
            }
            int inIx = 0;
            while (inIx < inLimit && (c = in.charAt(inIx)) < 128) {
                long j = outIx;
                outIx = j + 1;
                UnsafeUtil.putByte(j, (byte) c);
                inIx++;
            }
            if (inIx == inLimit) {
                out.position((int) (outIx - address));
                return;
            }
            while (inIx < inLimit) {
                char c2 = in.charAt(inIx);
                if (c2 < 128 && outIx < outLimit) {
                    long j2 = outIx;
                    outIx = j2 + 1;
                    UnsafeUtil.putByte(j2, (byte) c2);
                } else if (c2 < 2048 && outIx <= outLimit - 2) {
                    long j3 = outIx;
                    long outIx2 = j3 + 1;
                    UnsafeUtil.putByte(j3, (byte) (960 | (c2 >>> 6)));
                    outIx = outIx2 + 1;
                    UnsafeUtil.putByte(outIx2, (byte) (128 | ('?' & c2)));
                } else if ((c2 < 55296 || 57343 < c2) && outIx <= outLimit - 3) {
                    long j4 = outIx;
                    long outIx3 = j4 + 1;
                    UnsafeUtil.putByte(j4, (byte) (480 | (c2 >>> '\f')));
                    long outIx4 = outIx3 + 1;
                    UnsafeUtil.putByte(outIx3, (byte) (128 | (63 & (c2 >>> 6))));
                    outIx = outIx4 + 1;
                    UnsafeUtil.putByte(outIx4, (byte) (128 | ('?' & c2)));
                } else if (outIx <= outLimit - 4) {
                    if (inIx + 1 != inLimit) {
                        inIx++;
                        char low = in.charAt(inIx);
                        if (Character.isSurrogatePair(c2, low)) {
                            int codePoint = Character.toCodePoint(c2, low);
                            long j5 = outIx;
                            long outIx5 = j5 + 1;
                            UnsafeUtil.putByte(j5, (byte) (240 | (codePoint >>> 18)));
                            long outIx6 = outIx5 + 1;
                            UnsafeUtil.putByte(outIx5, (byte) (128 | (63 & (codePoint >>> 12))));
                            long outIx7 = outIx6 + 1;
                            UnsafeUtil.putByte(outIx6, (byte) (128 | (63 & (codePoint >>> 6))));
                            outIx = outIx7 + 1;
                            UnsafeUtil.putByte(outIx7, (byte) (128 | (63 & codePoint)));
                        }
                    }
                    throw new UnpairedSurrogateException(inIx - 1, inLimit);
                } else if (55296 <= c2 && c2 <= 57343 && (inIx + 1 == inLimit || !Character.isSurrogatePair(c2, in.charAt(inIx + 1)))) {
                    throw new UnpairedSurrogateException(inIx, inLimit);
                } else {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + c2 + " at index " + outIx);
                }
                inIx++;
            }
            out.position((int) (outIx - address));
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static int unsafeEstimateConsecutiveAscii(byte[] bytes, long offset, int maxChars) {
            if (maxChars < 16) {
                return 0;
            }
            int unaligned = 8 - (((int) offset) & 7);
            int i = 0;
            while (i < unaligned) {
                offset++;
                if (UnsafeUtil.getByte(bytes, (long) bytes) >= 0) {
                    i++;
                } else {
                    return i;
                }
            }
            while (i + 8 <= maxChars && (UnsafeUtil.getLong((Object) bytes, UnsafeUtil.BYTE_ARRAY_BASE_OFFSET + offset) & Utf8.ASCII_MASK_LONG) == 0) {
                offset += 8;
                i += 8;
            }
            while (i < maxChars) {
                offset++;
                if (UnsafeUtil.getByte(bytes, (long) bytes) >= 0) {
                    i++;
                } else {
                    return i;
                }
            }
            return maxChars;
        }

        private static int unsafeEstimateConsecutiveAscii(long address, int maxChars) {
            if (maxChars < 16) {
                return 0;
            }
            int unaligned = (int) ((-address) & 7);
            for (int j = unaligned; j > 0; j--) {
                long j2 = address;
                address = j2 + 1;
                if (UnsafeUtil.getByte(j2) < 0) {
                    return unaligned - j;
                }
            }
            int remaining = maxChars - unaligned;
            while (remaining >= 8 && (UnsafeUtil.getLong(address) & Utf8.ASCII_MASK_LONG) == 0) {
                address += 8;
                remaining -= 8;
            }
            return maxChars - remaining;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v6, types: [long, byte[]] */
        private static int partialIsValidUtf8(byte[] bytes, long offset, int remaining) {
            int skipped = unsafeEstimateConsecutiveAscii(bytes, offset, remaining);
            int remaining2 = remaining - skipped;
            long offset2 = offset + skipped;
            while (true) {
                int byte1 = 0;
                while (remaining2 > 0) {
                    long j = offset2;
                    offset2 = j + 1;
                    int i = UnsafeUtil.getByte(bytes, j);
                    byte1 = i;
                    if (i < 0) {
                        break;
                    }
                    remaining2--;
                }
                if (remaining2 == 0) {
                    return 0;
                }
                int remaining3 = remaining2 - 1;
                if (byte1 < -32) {
                    if (remaining3 == 0) {
                        return byte1;
                    }
                    remaining2 = remaining3 - 1;
                    if (byte1 < -62) {
                        return -1;
                    }
                    ?? r1 = offset2;
                    offset2 = r1 + 1;
                    if (UnsafeUtil.getByte((byte[]) r1, (long) r1) > -65) {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    if (remaining3 < 2) {
                        return unsafeIncompleteStateFor(bytes, byte1, offset2, remaining3);
                    }
                    remaining2 = remaining3 - 2;
                    long offset3 = bytes + 1;
                    int byte2 = UnsafeUtil.getByte(bytes, offset2);
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 != -19 || byte2 < -96) {
                        offset2 = offset3 + 1;
                        if (UnsafeUtil.getByte(bytes, offset3) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else if (remaining3 < 3) {
                    return unsafeIncompleteStateFor(bytes, byte1, offset2, remaining3);
                } else {
                    remaining2 = remaining3 - 3;
                    long offset4 = bytes + 1;
                    int byte22 = UnsafeUtil.getByte(bytes, offset2);
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0) {
                        long offset5 = offset4 + 1;
                        if (UnsafeUtil.getByte(bytes, (long) bytes) <= -65) {
                            offset2 = offset5 + 1;
                            if (UnsafeUtil.getByte(bytes, offset5) > -65) {
                                return -1;
                            }
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            }
        }

        private static int partialIsValidUtf8(long address, int remaining) {
            long j;
            int skipped = unsafeEstimateConsecutiveAscii(address, remaining);
            long address2 = address + skipped;
            int remaining2 = remaining - skipped;
            while (true) {
                int byte1 = 0;
                while (remaining2 > 0) {
                    long j2 = address2;
                    j = 1;
                    address2 = j2 + 1;
                    int i = UnsafeUtil.getByte(j2);
                    byte1 = i;
                    if (i < 0) {
                        break;
                    }
                    remaining2--;
                }
                if (remaining2 == 0) {
                    return 0;
                }
                int remaining3 = remaining2 - 1;
                if (byte1 < -32) {
                    if (remaining3 == 0) {
                        return byte1;
                    }
                    remaining2 = remaining3 - 1;
                    if (byte1 < -62) {
                        return -1;
                    }
                    long j3 = address2;
                    j = 1;
                    address2 = j3 + 1;
                    if (UnsafeUtil.getByte(j3) > -65) {
                        return -1;
                    }
                } else if (byte1 < -16) {
                    if (remaining3 < 2) {
                        return unsafeIncompleteStateFor(address2, byte1, remaining3);
                    }
                    remaining2 = remaining3 - 2;
                    long address3 = j + 1;
                    byte byte2 = UnsafeUtil.getByte(address2);
                    if (byte2 > -65) {
                        return -1;
                    }
                    if (byte1 == -32 && byte2 < -96) {
                        return -1;
                    }
                    if (byte1 != -19 || byte2 < -96) {
                        j = 1;
                        address2 = address3 + 1;
                        if (UnsafeUtil.getByte(address3) > -65) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else if (remaining3 < 3) {
                    return unsafeIncompleteStateFor(address2, byte1, remaining3);
                } else {
                    remaining2 = remaining3 - 3;
                    long address4 = j + 1;
                    byte byte22 = UnsafeUtil.getByte(address2);
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 - (-112))) >> 30) == 0) {
                        long address5 = (-112) + 1;
                        if (UnsafeUtil.getByte(address4) <= -65) {
                            j = 1;
                            address2 = address5 + 1;
                            if (UnsafeUtil.getByte(address5) > -65) {
                                return -1;
                            }
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            }
        }

        private static int unsafeIncompleteStateFor(byte[] bytes, int byte1, long offset, int remaining) {
            switch (remaining) {
                case 0:
                    return Utf8.incompleteStateFor(byte1);
                case 1:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(bytes, offset));
                case 2:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(bytes, offset), UnsafeUtil.getByte(bytes, offset + 1));
                default:
                    throw new AssertionError();
            }
        }

        private static int unsafeIncompleteStateFor(long address, int byte1, int remaining) {
            switch (remaining) {
                case 0:
                    return Utf8.incompleteStateFor(byte1);
                case 1:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(address));
                case 2:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(address), UnsafeUtil.getByte(address + 1));
                default:
                    throw new AssertionError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Utf8$DecodeUtil.class */
    public static class DecodeUtil {
        private DecodeUtil() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isOneByte(byte b) {
            return b >= 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isTwoBytes(byte b) {
            return b < -32;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isThreeBytes(byte b) {
            return b < -16;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleOneByte(byte byte1, char[] resultArr, int resultPos) {
            resultArr[resultPos] = (char) byte1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleTwoBytes(byte byte1, byte byte2, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (byte1 < -62 || isNotTrailingByte(byte2)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            resultArr[resultPos] = (char) (((byte1 & 31) << 6) | trailingByteValue(byte2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleThreeBytes(byte byte1, byte byte2, byte byte3, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(byte2) || ((byte1 == -32 && byte2 < -96) || ((byte1 == -19 && byte2 >= -96) || isNotTrailingByte(byte3)))) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            resultArr[resultPos] = (char) (((byte1 & 15) << 12) | (trailingByteValue(byte2) << 6) | trailingByteValue(byte3));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleFourBytes(byte byte1, byte byte2, byte byte3, byte byte4, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(byte2) || (((byte1 << 28) + (byte2 - (-112))) >> 30) != 0 || isNotTrailingByte(byte3) || isNotTrailingByte(byte4)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            int codepoint = ((byte1 & 7) << 18) | (trailingByteValue(byte2) << 12) | (trailingByteValue(byte3) << 6) | trailingByteValue(byte4);
            resultArr[resultPos] = highSurrogate(codepoint);
            resultArr[resultPos + 1] = lowSurrogate(codepoint);
        }

        private static boolean isNotTrailingByte(byte b) {
            return b > -65;
        }

        private static int trailingByteValue(byte b) {
            return b & 63;
        }

        private static char highSurrogate(int codePoint) {
            return (char) (55232 + (codePoint >>> 10));
        }

        private static char lowSurrogate(int codePoint) {
            return (char) (56320 + (codePoint & Opcodes.OP_NEW_INSTANCE_JUMBO));
        }
    }

    private Utf8() {
    }
}
