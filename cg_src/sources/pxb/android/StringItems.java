package pxb.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.bytebuddy.asm.Advice;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/StringItems.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/StringItems.class */
public class StringItems extends ArrayList<StringItem> {
    private static final int UTF8_FLAG = 256;
    byte[] stringData;
    private boolean useUTF8 = true;

    public static String[] read(ByteBuffer in) throws IOException {
        String str;
        int trunkOffset = in.position() - 8;
        int stringCount = in.getInt();
        in.getInt();
        int flags = in.getInt();
        int stringDataOffset = in.getInt();
        in.getInt();
        int[] offsets = new int[stringCount];
        String[] strings = new String[stringCount];
        for (int i = 0; i < stringCount; i++) {
            offsets[i] = in.getInt();
        }
        int base = trunkOffset + stringDataOffset;
        for (int i2 = 0; i2 < offsets.length; i2++) {
            in.position(base + offsets[i2]);
            if (0 != (flags & 256)) {
                u8length(in);
                int u8len = u8length(in);
                int start = in.position();
                int blength = u8len;
                while (in.get(start + blength) != 0) {
                    blength++;
                }
                str = new String(in.array(), start, blength, "UTF-8");
            } else {
                int length = u16length(in);
                str = new String(in.array(), in.position(), length * 2, "UTF-16LE");
            }
            String s = str;
            strings[i2] = s;
        }
        return strings;
    }

    static int u16length(ByteBuffer in) {
        int length = in.getShort() & 65535;
        if (length > 32767) {
            length = ((length & Advice.MethodSizeHandler.UNDEFINED_SIZE) << 8) | (in.getShort() & 65535);
        }
        return length;
    }

    static int u8length(ByteBuffer in) {
        int len = in.get() & 255;
        if ((len & 128) != 0) {
            len = ((len & 127) << 8) | (in.get() & 255);
        }
        return len;
    }

    public int getSize() {
        return 20 + (size() * 4) + this.stringData.length + 0;
    }

    public void prepare() throws IOException {
        Iterator<StringItem> it = iterator();
        while (it.hasNext()) {
            StringItem s = it.next();
            if (s.data.length() > 32767) {
                this.useUTF8 = false;
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = 0;
        int offset = 0;
        baos.reset();
        Map<String, Integer> map = new HashMap<>();
        Iterator<StringItem> it2 = iterator();
        while (it2.hasNext()) {
            StringItem item = it2.next();
            int i2 = i;
            i++;
            item.index = i2;
            String stringData = item.data;
            Integer of = map.get(stringData);
            if (of != null) {
                item.dataOffset = of.intValue();
            } else {
                item.dataOffset = offset;
                map.put(stringData, Integer.valueOf(offset));
                if (this.useUTF8) {
                    int length = stringData.length();
                    byte[] data = stringData.getBytes("UTF-8");
                    int u8lenght = data.length;
                    if (length > 127) {
                        offset++;
                        baos.write((length >> 8) | 128);
                    }
                    baos.write(length);
                    if (u8lenght > 127) {
                        offset++;
                        baos.write((u8lenght >> 8) | 128);
                    }
                    baos.write(u8lenght);
                    baos.write(data);
                    baos.write(0);
                    offset += 3 + u8lenght;
                } else {
                    int length2 = stringData.length();
                    byte[] data2 = stringData.getBytes("UTF-16LE");
                    if (length2 > 32767) {
                        int x = (length2 >> 16) | 32768;
                        baos.write(x);
                        baos.write(x >> 8);
                        offset += 2;
                    }
                    baos.write(length2);
                    baos.write(length2 >> 8);
                    baos.write(data2);
                    baos.write(0);
                    baos.write(0);
                    offset += 4 + data2.length;
                }
            }
        }
        this.stringData = baos.toByteArray();
    }

    public void write(ByteBuffer out) throws IOException {
        out.putInt(size());
        out.putInt(0);
        out.putInt(this.useUTF8 ? 256 : 0);
        out.putInt(28 + (size() * 4));
        out.putInt(0);
        Iterator<StringItem> it = iterator();
        while (it.hasNext()) {
            StringItem item = it.next();
            out.putInt(item.dataOffset);
        }
        out.put(this.stringData);
    }
}
