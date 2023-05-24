package pxb.android.axml;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/Util.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/Util.class */
public class Util {
    public static byte[] readFile(File in) throws IOException {
        InputStream is = new FileInputStream(in);
        byte[] xml = new byte[is.available()];
        is.read(xml);
        is.close();
        return xml;
    }

    public static byte[] readIs(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os);
        return os.toByteArray();
    }

    public static void writeFile(byte[] data, File out) throws IOException {
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(data);
        fos.close();
    }

    public static Map<String, String> readProguardConfig(File config) throws IOException {
        int i;
        Map<String, String> clzMap = new HashMap<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(config), "utf8"));
        try {
            for (String ln = r.readLine(); ln != null; ln = r.readLine()) {
                if (!ln.startsWith("#") && !ln.startsWith(Instruction.argsep) && (i = ln.indexOf("->")) > 0) {
                    clzMap.put(ln.substring(0, i).trim(), ln.substring(i + 2, ln.length() - 1).trim());
                }
            }
            return clzMap;
        } finally {
            r.close();
        }
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] xml = new byte[10240];
        int read = is.read(xml);
        while (true) {
            int c = read;
            if (c > 0) {
                os.write(xml, 0, c);
                read = is.read(xml);
            } else {
                return;
            }
        }
    }
}
