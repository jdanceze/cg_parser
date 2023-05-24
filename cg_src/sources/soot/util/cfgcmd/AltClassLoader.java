package soot.util.cfgcmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/AltClassLoader.class */
public class AltClassLoader extends ClassLoader {
    private static final Logger logger = LoggerFactory.getLogger(AltClassLoader.class);
    private static final boolean DEBUG = false;
    private String[] locations;
    private final Map<String, Class<?>> alreadyFound = new HashMap();
    private final Map<String, String> nameToMangledName = new HashMap();
    private final Map<String, String> mangledNameToName = new HashMap();

    public AltClassLoader(Singletons.Global g) {
    }

    public static AltClassLoader v() {
        return G.v().soot_util_cfgcmd_AltClassLoader();
    }

    public void setAltClassPath(String altClassPath) {
        List<String> locationList = new LinkedList<>();
        StringTokenizer tokens = new StringTokenizer(altClassPath, File.pathSeparator, false);
        while (tokens.hasMoreTokens()) {
            String location = tokens.nextToken();
            locationList.add(location);
        }
        this.locations = (String[]) locationList.toArray(new String[locationList.size()]);
    }

    public void setAltClasses(String[] classNames) {
        this.nameToMangledName.clear();
        for (String origName : classNames) {
            String mangledName = mangleName(origName);
            this.nameToMangledName.put(origName, mangledName);
            this.mangledNameToName.put(mangledName, origName);
        }
    }

    private static String mangleName(String origName) throws IllegalArgumentException {
        int lastDot = origName.lastIndexOf(46);
        StringBuilder mangledName = new StringBuilder(origName);
        int replacements = 0;
        int nextDot = lastDot;
        while (true) {
            int lastIndexOf = origName.lastIndexOf(46, nextDot - 1);
            nextDot = lastIndexOf;
            if (lastIndexOf < 0) {
                break;
            }
            mangledName.setCharAt(nextDot, '_');
            replacements++;
        }
        if (replacements <= 0) {
            throw new IllegalArgumentException("AltClassLoader.mangleName()'s crude classname mangling cannot deal with " + origName);
        }
        return mangledName.toString();
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String maybeMangledName) throws ClassNotFoundException {
        String[] strArr;
        Class<?> result = this.alreadyFound.get(maybeMangledName);
        if (result != null) {
            return result;
        }
        String name = this.mangledNameToName.get(maybeMangledName);
        if (name == null) {
            name = maybeMangledName;
        }
        String pathTail = "/" + name.replace('.', File.separatorChar) + ".class";
        for (String element : this.locations) {
            String path = String.valueOf(element) + pathTail;
            Throwable th = null;
            try {
                try {
                    FileInputStream stream = new FileInputStream(path);
                    try {
                        byte[] classBytes = new byte[stream.available()];
                        stream.read(classBytes);
                        replaceAltClassNames(classBytes);
                        Class<?> result2 = defineClass(maybeMangledName, classBytes, 0, classBytes.length);
                        this.alreadyFound.put(maybeMangledName, result2);
                        if (stream != null) {
                            stream.close();
                        }
                        return result2;
                    } catch (Throwable th2) {
                        if (stream != null) {
                            stream.close();
                        }
                        throw th2;
                    }
                } catch (Throwable th3) {
                    if (0 == 0) {
                        th = th3;
                    } else if (null != th3) {
                        th.addSuppressed(th3);
                    }
                    throw th;
                }
            } catch (IOException | ClassFormatError e) {
            }
        }
        throw new ClassNotFoundException("Unable to find class" + name + " in alternate classpath");
    }

    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        String nameForParent = this.nameToMangledName.get(name);
        if (nameForParent == null) {
            nameForParent = name;
        }
        return super.loadClass(nameForParent, false);
    }

    private void replaceAltClassNames(byte[] classBytes) {
        for (Map.Entry<String, String> entry : this.nameToMangledName.entrySet()) {
            String origName = entry.getKey().replace('.', '/');
            String mangledName = entry.getValue().replace('.', '/');
            findAndReplace(classBytes, stringToUtf8Pattern(origName), stringToUtf8Pattern(mangledName));
            findAndReplace(classBytes, stringToTypeStringPattern(origName), stringToTypeStringPattern(mangledName));
        }
    }

    private static byte[] stringToUtf8Pattern(String s) {
        byte[] origBytes = s.getBytes();
        int length = origBytes.length;
        byte[] result = new byte[length + 3];
        result[0] = 1;
        result[1] = (byte) (length & 65280);
        result[2] = (byte) (length & 255);
        System.arraycopy(origBytes, 0, result, 3, length);
        return result;
    }

    private static byte[] stringToTypeStringPattern(String s) {
        byte[] origBytes = s.getBytes();
        int length = origBytes.length;
        byte[] result = new byte[length + 2];
        result[0] = 76;
        System.arraycopy(origBytes, 0, result, 1, length);
        result[length + 1] = 59;
        return result;
    }

    private static void findAndReplace(byte[] text, byte[] pattern, byte[] replacement) throws IllegalArgumentException {
        int patternLength = pattern.length;
        if (patternLength != replacement.length) {
            throw new IllegalArgumentException("findAndReplace(): The lengths of the pattern and replacement must match.");
        }
        int match = 0;
        while (true) {
            int match2 = findMatch(text, pattern, match);
            if (match2 >= 0) {
                replace(text, replacement, match2);
                match = match2 + patternLength;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0035, code lost:
        r9 = r9 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int findMatch(byte[] r4, byte[] r5, int r6) {
        /*
            r0 = r4
            int r0 = r0.length
            r7 = r0
            r0 = r5
            int r0 = r0.length
            r8 = r0
            r0 = r6
            r9 = r0
            goto L38
        Ld:
            r0 = r9
            r10 = r0
            r0 = 0
            r11 = r0
            goto L2b
        L17:
            r0 = r4
            r1 = r10
            r0 = r0[r1]
            r1 = r5
            r2 = r11
            r1 = r1[r2]
            if (r0 == r1) goto L25
            goto L35
        L25:
            int r10 = r10 + 1
            int r11 = r11 + 1
        L2b:
            r0 = r11
            r1 = r8
            if (r0 < r1) goto L17
            r0 = r9
            return r0
        L35:
            int r9 = r9 + 1
        L38:
            r0 = r9
            r1 = r7
            if (r0 < r1) goto Ld
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.util.cfgcmd.AltClassLoader.findMatch(byte[], byte[], int):int");
    }

    private static void replace(byte[] text, byte[] replacement, int start) {
        int t = start;
        for (byte b : replacement) {
            text[t] = b;
            t++;
        }
    }

    public static void main(String[] argv) throws ClassNotFoundException {
        v().setAltClassPath(argv[0]);
        for (int i = 1; i < argv.length; i++) {
            v().setAltClasses(new String[]{argv[i]});
            logger.debug("main() loadClass(" + argv[i] + ")");
            v().loadClass(argv[i]);
        }
    }
}
