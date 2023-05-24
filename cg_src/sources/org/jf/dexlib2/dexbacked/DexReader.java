package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.util.ExceptionWithContext;
import org.jf.util.Utf8Utils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexReader.class */
public class DexReader<T extends DexBuffer> {
    @Nonnull
    public final T dexBuf;
    private int offset;

    public DexReader(@Nonnull T dexBuf, int offset) {
        this.dexBuf = dexBuf;
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int readSleb128() {
        int result;
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result2 = buf[end] & 255;
        if (result2 <= 127) {
            result = (result2 << 25) >> 25;
        } else {
            end2++;
            int currentByteValue = buf[end2] & 255;
            int result3 = (result2 & 127) | ((currentByteValue & 127) << 7);
            if (currentByteValue <= 127) {
                result = (result3 << 18) >> 18;
            } else {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                int result4 = result3 | ((currentByteValue2 & 127) << 14);
                if (currentByteValue2 <= 127) {
                    result = (result4 << 11) >> 11;
                } else {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    int result5 = result4 | ((currentByteValue3 & 127) << 21);
                    if (currentByteValue3 <= 127) {
                        result = (result5 << 4) >> 4;
                    } else {
                        end2++;
                        int currentByteValue4 = buf[end2] & 255;
                        if (currentByteValue4 > 127) {
                            throw new ExceptionWithContext("Invalid sleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                        result = result5 | (currentByteValue4 << 28);
                    }
                }
            }
        }
        this.offset = end2 - this.dexBuf.baseOffset;
        return result;
    }

    public int peekSleb128Size() {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result = buf[end] & 255;
        if (result > 127) {
            end2++;
            int currentByteValue = buf[end2] & 255;
            if (currentByteValue > 127) {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                if (currentByteValue2 > 127) {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    if (currentByteValue3 > 127) {
                        end2++;
                        int currentByteValue4 = buf[end2] & 255;
                        if (currentByteValue4 > 127) {
                            throw new ExceptionWithContext("Invalid sleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                    }
                }
            }
        }
        return end2 - (this.dexBuf.baseOffset + this.offset);
    }

    public int readSmallUleb128() {
        return readUleb128(false);
    }

    public int peekSmallUleb128Size() {
        return peekUleb128Size(false);
    }

    private int readUleb128(boolean allowLarge) {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result = buf[end] & 255;
        if (result > 127) {
            end2++;
            int currentByteValue = buf[end2] & 255;
            result = (result & 127) | ((currentByteValue & 127) << 7);
            if (currentByteValue > 127) {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                result |= (currentByteValue2 & 127) << 14;
                if (currentByteValue2 > 127) {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    result |= (currentByteValue3 & 127) << 21;
                    if (currentByteValue3 > 127) {
                        end2++;
                        byte b = buf[end2];
                        if (b < 0) {
                            throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                        if ((b & 15) > 7 && !allowLarge) {
                            throw new ExceptionWithContext("Encountered valid uleb128 that is out of range at offset 0x%x", Integer.valueOf(this.offset));
                        }
                        result |= b << 28;
                    }
                }
            }
        }
        this.offset = end2 - this.dexBuf.baseOffset;
        return result;
    }

    private int peekUleb128Size(boolean allowLarge) {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result = buf[end] & 255;
        if (result > 127) {
            end2++;
            int currentByteValue = buf[end2] & 255;
            if (currentByteValue > 127) {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                if (currentByteValue2 > 127) {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    if (currentByteValue3 > 127) {
                        end2++;
                        byte b = buf[end2];
                        if (b < 0) {
                            throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                        if ((b & 15) > 7 && !allowLarge) {
                            throw new ExceptionWithContext("Encountered valid uleb128 that is out of range at offset 0x%x", Integer.valueOf(this.offset));
                        }
                    }
                }
            }
        }
        return end2 - (this.dexBuf.baseOffset + this.offset);
    }

    public int readLargeUleb128() {
        return readUleb128(true);
    }

    public int readBigUleb128() {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result = buf[end] & 255;
        if (result > 127) {
            end2++;
            int currentByteValue = buf[end2] & 255;
            result = (result & 127) | ((currentByteValue & 127) << 7);
            if (currentByteValue > 127) {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                result |= (currentByteValue2 & 127) << 14;
                if (currentByteValue2 > 127) {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    result |= (currentByteValue3 & 127) << 21;
                    if (currentByteValue3 > 127) {
                        end2++;
                        byte b = buf[end2];
                        if (b < 0) {
                            throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                        result |= b << 28;
                    }
                }
            }
        }
        this.offset = end2 - this.dexBuf.baseOffset;
        return result;
    }

    public int peekBigUleb128Size() {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        int result = buf[end] & 255;
        if (result > 127) {
            end2++;
            int currentByteValue = buf[end2] & 255;
            if (currentByteValue > 127) {
                end2++;
                int currentByteValue2 = buf[end2] & 255;
                if (currentByteValue2 > 127) {
                    end2++;
                    int currentByteValue3 = buf[end2] & 255;
                    if (currentByteValue3 > 127) {
                        end2++;
                        if (buf[end2] < 0) {
                            throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                    }
                }
            }
        }
        return end2 - (this.dexBuf.baseOffset + this.offset);
    }

    public void skipUleb128() {
        int end = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int end2 = end + 1;
        byte currentByteValue = buf[end];
        if (currentByteValue < 0) {
            end2++;
            byte currentByteValue2 = buf[end2];
            if (currentByteValue2 < 0) {
                end2++;
                byte currentByteValue3 = buf[end2];
                if (currentByteValue3 < 0) {
                    end2++;
                    byte currentByteValue4 = buf[end2];
                    if (currentByteValue4 < 0) {
                        end2++;
                        byte currentByteValue5 = buf[end2];
                        if (currentByteValue5 < 0) {
                            throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", Integer.valueOf(this.offset));
                        }
                    }
                }
            }
        }
        this.offset = end2 - this.dexBuf.baseOffset;
    }

    public int readSmallUint() {
        int o = this.offset;
        int result = this.dexBuf.readSmallUint(o);
        this.offset = o + 4;
        return result;
    }

    public int readOptionalUint() {
        int o = this.offset;
        int result = this.dexBuf.readOptionalUint(o);
        this.offset = o + 4;
        return result;
    }

    public int peekUshort() {
        return this.dexBuf.readUshort(this.offset);
    }

    public int readUshort() {
        int o = this.offset;
        int result = this.dexBuf.readUshort(this.offset);
        this.offset = o + 2;
        return result;
    }

    public int peekUbyte() {
        return this.dexBuf.readUbyte(this.offset);
    }

    public int readUbyte() {
        int o = this.offset;
        int result = this.dexBuf.readUbyte(this.offset);
        this.offset = o + 1;
        return result;
    }

    public long readLong() {
        int o = this.offset;
        long result = this.dexBuf.readLong(this.offset);
        this.offset = o + 8;
        return result;
    }

    public int readInt() {
        int o = this.offset;
        int result = this.dexBuf.readInt(this.offset);
        this.offset = o + 4;
        return result;
    }

    public int readShort() {
        int o = this.offset;
        int result = this.dexBuf.readShort(this.offset);
        this.offset = o + 2;
        return result;
    }

    public int readByte() {
        int o = this.offset;
        int result = this.dexBuf.readByte(this.offset);
        this.offset = o + 1;
        return result;
    }

    public void skipByte() {
        this.offset++;
    }

    public void moveRelative(int i) {
        this.offset += i;
    }

    public int readSmallUint(int offset) {
        return this.dexBuf.readSmallUint(offset);
    }

    public int readUshort(int offset) {
        return this.dexBuf.readUshort(offset);
    }

    public int readUbyte(int offset) {
        return this.dexBuf.readUbyte(offset);
    }

    public long readLong(int offset) {
        return this.dexBuf.readLong(offset);
    }

    public int readInt(int offset) {
        return this.dexBuf.readInt(offset);
    }

    public int readShort(int offset) {
        return this.dexBuf.readShort(offset);
    }

    public int readByte(int offset) {
        return this.dexBuf.readByte(offset);
    }

    public int readSizedInt(int bytes) {
        int result;
        int o = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        switch (bytes) {
            case 1:
                result = buf[o];
                break;
            case 2:
                result = (buf[o] & 255) | (buf[o + 1] << 8);
                break;
            case 3:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | (buf[o + 2] << 16);
                break;
            case 4:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | (buf[o + 3] << 24);
                break;
            default:
                throw new ExceptionWithContext("Invalid size %d for sized int at offset 0x%x", Integer.valueOf(bytes), Integer.valueOf(this.offset));
        }
        this.offset = (o + bytes) - this.dexBuf.baseOffset;
        return result;
    }

    public int readSizedSmallUint(int bytes) {
        int o = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        int result = 0;
        switch (bytes) {
            case 1:
                break;
            case 4:
                byte b = buf[o + 3];
                if (b < 0) {
                    throw new ExceptionWithContext("Encountered valid sized uint that is out of range at offset 0x%x", Integer.valueOf(this.offset));
                }
                result = b << 24;
            case 3:
                result |= (buf[o + 2] & 255) << 16;
            case 2:
                result |= (buf[o + 1] & 255) << 8;
                break;
            default:
                throw new ExceptionWithContext("Invalid size %d for sized uint at offset 0x%x", Integer.valueOf(bytes), Integer.valueOf(this.offset));
        }
        int result2 = result | (buf[o] & 255);
        this.offset = (o + bytes) - this.dexBuf.baseOffset;
        return result2;
    }

    public int readSizedRightExtendedInt(int bytes) {
        int result;
        int o = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        switch (bytes) {
            case 1:
                result = buf[o] << 24;
                break;
            case 2:
                result = ((buf[o] & 255) << 16) | (buf[o + 1] << 24);
                break;
            case 3:
                result = ((buf[o] & 255) << 8) | ((buf[o + 1] & 255) << 16) | (buf[o + 2] << 24);
                break;
            case 4:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | (buf[o + 3] << 24);
                break;
            default:
                throw new ExceptionWithContext("Invalid size %d for sized, right extended int at offset 0x%x", Integer.valueOf(bytes), Integer.valueOf(this.offset));
        }
        this.offset = (o + bytes) - this.dexBuf.baseOffset;
        return result;
    }

    public long readSizedRightExtendedLong(int bytes) {
        long result;
        int o = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        switch (bytes) {
            case 1:
                result = buf[o] << 56;
                break;
            case 2:
                result = ((buf[o] & 255) << 48) | (buf[o + 1] << 56);
                break;
            case 3:
                result = ((buf[o] & 255) << 40) | ((buf[o + 1] & 255) << 48) | (buf[o + 2] << 56);
                break;
            case 4:
                result = ((buf[o] & 255) << 32) | ((buf[o + 1] & 255) << 40) | ((buf[o + 2] & 255) << 48) | (buf[o + 3] << 56);
                break;
            case 5:
                result = ((buf[o] & 255) << 24) | ((buf[o + 1] & 255) << 32) | ((buf[o + 2] & 255) << 40) | ((buf[o + 3] & 255) << 48) | (buf[o + 4] << 56);
                break;
            case 6:
                result = ((buf[o] & 255) << 16) | ((buf[o + 1] & 255) << 24) | ((buf[o + 2] & 255) << 32) | ((buf[o + 3] & 255) << 40) | ((buf[o + 4] & 255) << 48) | (buf[o + 5] << 56);
                break;
            case 7:
                result = ((buf[o] & 255) << 8) | ((buf[o + 1] & 255) << 16) | ((buf[o + 2] & 255) << 24) | ((buf[o + 3] & 255) << 32) | ((buf[o + 4] & 255) << 40) | ((buf[o + 5] & 255) << 48) | (buf[o + 6] << 56);
                break;
            case 8:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | ((buf[o + 3] & 255) << 24) | ((buf[o + 4] & 255) << 32) | ((buf[o + 5] & 255) << 40) | ((buf[o + 6] & 255) << 48) | (buf[o + 7] << 56);
                break;
            default:
                throw new ExceptionWithContext("Invalid size %d for sized, right extended long at offset 0x%x", Integer.valueOf(bytes), Integer.valueOf(this.offset));
        }
        this.offset = (o + bytes) - this.dexBuf.baseOffset;
        return result;
    }

    public long readSizedLong(int bytes) {
        long result;
        int o = this.dexBuf.baseOffset + this.offset;
        byte[] buf = this.dexBuf.buf;
        switch (bytes) {
            case 1:
                result = buf[o];
                break;
            case 2:
                result = (buf[o] & 255) | (buf[o + 1] << 8);
                break;
            case 3:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | (buf[o + 2] << 16);
                break;
            case 4:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | (buf[o + 3] << 24);
                break;
            case 5:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | ((buf[o + 3] & 255) << 24) | (buf[o + 4] << 32);
                break;
            case 6:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | ((buf[o + 3] & 255) << 24) | ((buf[o + 4] & 255) << 32) | (buf[o + 5] << 40);
                break;
            case 7:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | ((buf[o + 3] & 255) << 24) | ((buf[o + 4] & 255) << 32) | ((buf[o + 5] & 255) << 40) | (buf[o + 6] << 48);
                break;
            case 8:
                result = (buf[o] & 255) | ((buf[o + 1] & 255) << 8) | ((buf[o + 2] & 255) << 16) | ((buf[o + 3] & 255) << 24) | ((buf[o + 4] & 255) << 32) | ((buf[o + 5] & 255) << 40) | ((buf[o + 6] & 255) << 48) | (buf[o + 7] << 56);
                break;
            default:
                throw new ExceptionWithContext("Invalid size %d for sized long at offset 0x%x", Integer.valueOf(bytes), Integer.valueOf(this.offset));
        }
        this.offset = (o + bytes) - this.dexBuf.baseOffset;
        return result;
    }

    public String readString(int utf16Length) {
        int[] ret = new int[1];
        String value = Utf8Utils.utf8BytesWithUtf16LengthToString(this.dexBuf.buf, this.dexBuf.baseOffset + this.offset, utf16Length, ret);
        this.offset += ret[0];
        return value;
    }

    public int peekStringLength(int utf16Length) {
        int[] ret = new int[1];
        Utf8Utils.utf8BytesWithUtf16LengthToString(this.dexBuf.buf, this.dexBuf.baseOffset + this.offset, utf16Length, ret);
        return ret[0];
    }
}
