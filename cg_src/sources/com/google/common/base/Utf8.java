package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.sun.xml.fastinfoset.EncodingConstants;
@Beta
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Utf8.class */
public final class Utf8 {
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
                    if (Character.codePointAt(sequence, i) == c) {
                        throw new IllegalArgumentException(unpairedSurrogateMsg(i));
                    }
                    i++;
                }
            }
            i++;
        }
        return utf8Length;
    }

    public static boolean isWellFormed(byte[] bytes) {
        return isWellFormed(bytes, 0, bytes.length);
    }

    public static boolean isWellFormed(byte[] bytes, int off, int len) {
        int end = off + len;
        Preconditions.checkPositionIndexes(off, end, bytes.length);
        for (int i = off; i < end; i++) {
            if (bytes[i] < 0) {
                return isWellFormedSlowPath(bytes, i, end);
            }
        }
        return true;
    }

    private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
        int index = off;
        while (index < end) {
            int i = index;
            index++;
            byte b = bytes[i];
            if (b < 0) {
                if (b < -32) {
                    if (index == end || b < -62) {
                        return false;
                    }
                    index++;
                    if (bytes[index] > -65) {
                        return false;
                    }
                } else if (b < -16) {
                    if (index + 1 >= end) {
                        return false;
                    }
                    int index2 = index + 1;
                    byte b2 = bytes[index];
                    if (b2 > -65) {
                        return false;
                    }
                    if (b == -32 && b2 < -96) {
                        return false;
                    }
                    if (b == -19 && -96 <= b2) {
                        return false;
                    }
                    index = index2 + 1;
                    if (bytes[index2] > -65) {
                        return false;
                    }
                } else if (index + 2 >= end) {
                    return false;
                } else {
                    int index3 = index + 1;
                    byte b3 = bytes[index];
                    if (b3 > -65 || (((b << 28) + (b3 - (-112))) >> 30) != 0) {
                        return false;
                    }
                    int index4 = index3 + 1;
                    if (bytes[index3] > -65) {
                        return false;
                    }
                    index = index4 + 1;
                    if (bytes[index4] > -65) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static String unpairedSurrogateMsg(int i) {
        return "Unpaired surrogate at index " + i;
    }

    private Utf8() {
    }
}
