package org.apache.tools.ant.launch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
/* loaded from: gencallgraphv3.jar:ant-launcher-1.10.11.jar:org/apache/tools/ant/launch/Locator.class */
public final class Locator {
    private static final int NIBBLE = 4;
    private static final int NIBBLE_MASK = 15;
    private static final int ASCII_SIZE = 128;
    private static final int BYTE_SIZE = 256;
    private static final int WORD = 16;
    private static final int SPACE = 32;
    private static final int DEL = 127;
    private static boolean[] gNeedEscaping = new boolean[128];
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final String ERROR_NOT_FILE_URI = "Can only handle valid file: URIs, not ";

    static {
        for (int i = 0; i < 32; i++) {
            gNeedEscaping[i] = true;
            gAfterEscaping1[i] = gHexChs[i >> 4];
            gAfterEscaping2[i] = gHexChs[i & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        char[] escChs = {' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
        for (char ch : escChs) {
            gNeedEscaping[ch] = true;
            gAfterEscaping1[ch] = gHexChs[ch >> 4];
            gAfterEscaping2[ch] = gHexChs[ch & 15];
        }
    }

    public static File getClassSource(Class<?> c) {
        String classResource = c.getName().replace('.', '/') + ".class";
        return getResourceSource(c.getClassLoader(), classResource);
    }

    public static File getResourceSource(ClassLoader c, String resource) {
        URL url;
        if (c == null) {
            c = Locator.class.getClassLoader();
        }
        if (c == null) {
            url = ClassLoader.getSystemResource(resource);
        } else {
            url = c.getResource(resource);
        }
        if (url != null) {
            String u = url.toString();
            try {
                if (u.startsWith("jar:file:")) {
                    return new File(fromJarURI(u));
                }
                if (u.startsWith("file:")) {
                    int tail = u.indexOf(resource);
                    String dirName = u.substring(0, tail);
                    return new File(fromURI(dirName));
                }
                return null;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public static String fromURI(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
        }
        if (url == null || !"file".equals(url.getProtocol())) {
            throw new IllegalArgumentException(ERROR_NOT_FILE_URI + uri);
        }
        StringBuilder buf = new StringBuilder(url.getHost());
        if (buf.length() > 0) {
            buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
        }
        String file = url.getFile();
        int queryPos = file.indexOf(63);
        buf.append(queryPos < 0 ? file : file.substring(0, queryPos));
        String uri2 = buf.toString().replace('/', File.separatorChar);
        if (File.pathSeparatorChar == ';' && uri2.startsWith("\\") && uri2.length() > 2 && Character.isLetter(uri2.charAt(1)) && uri2.lastIndexOf(58) > -1) {
            uri2 = uri2.substring(1);
        }
        try {
            String path = decodeUri(uri2);
            String cwd = System.getProperty("user.dir");
            int posi = cwd.indexOf(58);
            boolean pathStartsWithFileSeparator = path.startsWith(File.separator);
            boolean pathStartsWithUNC = path.startsWith("" + File.separator + File.separator);
            if (posi > 0 && pathStartsWithFileSeparator && !pathStartsWithUNC) {
                path = cwd.substring(0, posi + 1) + path;
            }
            return path;
        } catch (UnsupportedEncodingException exc) {
            throw new IllegalStateException("Could not convert URI " + uri2 + " to path: " + exc.getMessage());
        }
    }

    public static String fromJarURI(String uri) {
        int pling = uri.indexOf("!/");
        String jarName = uri.substring("jar:".length(), pling);
        return fromURI(jarName);
    }

    public static String decodeUri(String uri) throws UnsupportedEncodingException {
        if (!uri.contains("%")) {
            return uri;
        }
        ByteArrayOutputStream sb = new ByteArrayOutputStream(uri.length());
        CharacterIterator iter = new StringCharacterIterator(uri);
        char first = iter.first();
        while (true) {
            char c = first;
            if (c != 65535) {
                if (c == '%') {
                    char c1 = iter.next();
                    if (c1 != 65535) {
                        int i1 = Character.digit(c1, 16);
                        char c2 = iter.next();
                        if (c2 != 65535) {
                            int i2 = Character.digit(c2, 16);
                            sb.write((char) ((i1 << 4) + i2));
                        }
                    }
                } else if (c >= 0 && c < 128) {
                    sb.write(c);
                } else {
                    byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                    sb.write(bytes, 0, bytes.length);
                }
                first = iter.next();
            } else {
                return sb.toString(StandardCharsets.UTF_8.name());
            }
        }
    }

    public static String encodeURI(String path) {
        int ch;
        int i = 0;
        int len = path.length();
        StringBuilder sb = null;
        while (i < len && (ch = path.charAt(i)) < 128) {
            if (gNeedEscaping[ch]) {
                if (sb == null) {
                    sb = new StringBuilder(path.substring(0, i));
                }
                sb.append('%');
                sb.append(gAfterEscaping1[ch]);
                sb.append(gAfterEscaping2[ch]);
            } else if (sb != null) {
                sb.append((char) ch);
            }
            i++;
        }
        if (i < len) {
            if (sb == null) {
                sb = new StringBuilder(path.substring(0, i));
            }
            byte[] bytes = path.substring(i).getBytes(StandardCharsets.UTF_8);
            for (byte b : bytes) {
                if (b < 0) {
                    int ch2 = b + 256;
                    sb.append('%');
                    sb.append(gHexChs[ch2 >> 4]);
                    sb.append(gHexChs[ch2 & 15]);
                } else if (gNeedEscaping[b]) {
                    sb.append('%');
                    sb.append(gAfterEscaping1[b]);
                    sb.append(gAfterEscaping2[b]);
                } else {
                    sb.append((char) b);
                }
            }
        }
        return sb == null ? path : sb.toString();
    }

    @Deprecated
    public static URL fileToURL(File file) throws MalformedURLException {
        return new URL(file.toURI().toASCIIString());
    }

    public static File getToolsJar() {
        boolean toolsJarAvailable = false;
        try {
            Class.forName("com.sun.tools.javac.Main");
            toolsJarAvailable = true;
        } catch (Exception e) {
            try {
                Class.forName("sun.tools.javac.Main");
                toolsJarAvailable = true;
            } catch (Exception e2) {
            }
        }
        if (toolsJarAvailable) {
            return null;
        }
        String libToolsJar = File.separator + Launcher.ANT_PRIVATELIB + File.separator + "tools.jar";
        String javaHome = System.getProperty("java.home");
        File toolsJar = new File(javaHome + libToolsJar);
        if (toolsJar.exists()) {
            return toolsJar;
        }
        if (javaHome.toLowerCase(Locale.ENGLISH).endsWith(File.separator + "jre")) {
            toolsJar = new File(javaHome.substring(0, javaHome.length() - "/jre".length()) + libToolsJar);
        }
        if (!toolsJar.exists()) {
            return null;
        }
        return toolsJar;
    }

    public static URL[] getLocationURLs(File location) throws MalformedURLException {
        return getLocationURLs(location, ".jar");
    }

    public static URL[] getLocationURLs(File location, String... extensions) throws MalformedURLException {
        URL[] urls = new URL[0];
        if (!location.exists()) {
            return urls;
        }
        if (!location.isDirectory()) {
            URL[] urls2 = new URL[1];
            String path = location.getPath();
            String littlePath = path.toLowerCase(Locale.ENGLISH);
            int length = extensions.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String extension = extensions[i];
                if (!littlePath.endsWith(extension)) {
                    i++;
                } else {
                    urls2[0] = fileToURL(location);
                    break;
                }
            }
            return urls2;
        }
        File[] matches = location.listFiles(dir, name -> {
            String littleName = name.toLowerCase(Locale.ENGLISH);
            Stream of = Stream.of((Object[]) extensions);
            Objects.requireNonNull(littleName);
            return of.anyMatch(this::endsWith);
        });
        URL[] urls3 = new URL[matches.length];
        for (int i2 = 0; i2 < matches.length; i2++) {
            urls3[i2] = fileToURL(matches[i2]);
        }
        return urls3;
    }

    private Locator() {
    }
}
