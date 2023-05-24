package org.apache.tools.ant.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.PathTokenizer;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/FileUtils.class */
public class FileUtils {
    private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
    private static final int EXPAND_SPACE = 50;
    static final int BUF_SIZE = 8192;
    public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000;
    public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000;
    public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1;
    private Object cacheFromUriLock = new Object();
    private String cacheFromUriRequest = null;
    private String cacheFromUriResponse = null;
    private static final String NULL_PLACEHOLDER = "null";
    private static final FileUtils PRIMARY_INSTANCE = new FileUtils();
    private static Random rand = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
    private static final boolean ON_NETWARE = Os.isFamily(Os.FAMILY_NETWARE);
    private static final boolean ON_DOS = Os.isFamily(Os.FAMILY_DOS);
    private static final boolean ON_WIN9X = Os.isFamily(Os.FAMILY_9X);
    private static final boolean ON_WINDOWS = Os.isFamily(Os.FAMILY_WINDOWS);
    private static final FileAttribute[] TMPFILE_ATTRIBUTES = {PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE))};
    private static final FileAttribute[] TMPDIR_ATTRIBUTES = {PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE))};
    private static final FileAttribute[] NO_TMPFILE_ATTRIBUTES = new FileAttribute[0];

    @Deprecated
    public static FileUtils newFileUtils() {
        return new FileUtils();
    }

    public static FileUtils getFileUtils() {
        return PRIMARY_INSTANCE;
    }

    protected FileUtils() {
    }

    public URL getFileURL(File file) throws MalformedURLException {
        return new URL(file.toURI().toASCIIString());
    }

    public void copyFile(String sourceFile, String destFile) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), (FilterSetCollection) null, false, false);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, false, false);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, overwrite, false);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified, encoding);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, encoding, project);
    }

    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, inputEncoding, outputEncoding, project);
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        copyFile(sourceFile, destFile, (FilterSetCollection) null, false, false);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters) throws IOException {
        copyFile(sourceFile, destFile, filters, false, false);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
        copyFile(sourceFile, destFile, filters, overwrite, false);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
        copyFile(sourceFile, destFile, filters, overwrite, preserveLastModified, (String) null);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
        copyFile(sourceFile, destFile, filters, (Vector<FilterChain>) null, overwrite, preserveLastModified, encoding, (Project) null);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
        copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, encoding, encoding, project);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
        copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
        copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
    }

    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project, boolean force) throws IOException {
        ResourceUtils.copyResource(new FileResource(sourceFile), new FileResource(destFile), filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, force);
    }

    public void setFileLastModified(File file, long time) {
        ResourceUtils.setLastModified(new FileResource(file), time);
    }

    public File resolveFile(File file, String filename) {
        if (!isAbsolutePath(filename)) {
            char sep = File.separatorChar;
            String filename2 = filename.replace('/', sep).replace('\\', sep);
            if (isContextRelativePath(filename2)) {
                file = null;
                String udir = System.getProperty("user.dir");
                if (filename2.charAt(0) == sep && udir.charAt(0) == sep) {
                    filename2 = dissect(udir)[0] + filename2.substring(1);
                }
            }
            filename = new File(file, filename2).getAbsolutePath();
        }
        return normalize(filename);
    }

    public static boolean isContextRelativePath(String filename) {
        if ((!ON_DOS && !ON_NETWARE) || filename.isEmpty()) {
            return false;
        }
        char sep = File.separatorChar;
        String filename2 = filename.replace('/', sep).replace('\\', sep);
        char c = filename2.charAt(0);
        int len = filename2.length();
        return (c == sep && (len == 1 || filename2.charAt(1) != sep)) || (Character.isLetter(c) && len > 1 && filename2.charAt(1) == ':' && (len == 2 || filename2.charAt(2) != sep));
    }

    public static boolean isAbsolutePath(String filename) {
        int nextsep;
        if (filename.isEmpty()) {
            return false;
        }
        int len = filename.length();
        char sep = File.separatorChar;
        String filename2 = filename.replace('/', sep).replace('\\', sep);
        char c = filename2.charAt(0);
        if (!ON_DOS && !ON_NETWARE) {
            return c == sep;
        } else if (c == sep) {
            return ON_DOS && len > 4 && filename2.charAt(1) == sep && (nextsep = filename2.indexOf(sep, 2)) > 2 && nextsep + 1 < len;
        } else {
            int colon = filename2.indexOf(58);
            return (Character.isLetter(c) && colon == 1 && filename2.length() > 2 && filename2.charAt(2) == sep) || (ON_NETWARE && colon > 0);
        }
    }

    public static String translatePath(String toProcess) {
        if (toProcess == null || toProcess.isEmpty()) {
            return "";
        }
        StringBuilder path = new StringBuilder(toProcess.length() + 50);
        PathTokenizer tokenizer = new PathTokenizer(toProcess);
        while (tokenizer.hasMoreTokens()) {
            String pathComponent = tokenizer.nextToken();
            String pathComponent2 = pathComponent.replace('/', File.separatorChar).replace('\\', File.separatorChar);
            if (path.length() > 0) {
                path.append(File.pathSeparatorChar);
            }
            path.append(pathComponent2);
        }
        return path.toString();
    }

    public File normalize(String path) {
        Stack<String> s = new Stack<>();
        String[] dissect = dissect(path);
        s.push(dissect[0]);
        java.util.StringTokenizer tok = new java.util.StringTokenizer(dissect[1], File.separator);
        while (tok.hasMoreTokens()) {
            String thisToken = tok.nextToken();
            if (!".".equals(thisToken)) {
                if ("..".equals(thisToken)) {
                    if (s.size() < 2) {
                        return new File(path);
                    }
                    s.pop();
                } else {
                    s.push(thisToken);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        int size = s.size();
        for (int i = 0; i < size; i++) {
            if (i > 1) {
                sb.append(File.separatorChar);
            }
            sb.append(s.elementAt(i));
        }
        return new File(sb.toString());
    }

    public String[] dissect(String path) {
        String root;
        String path2;
        char sep = File.separatorChar;
        String path3 = path.replace('/', sep).replace('\\', sep);
        if (!isAbsolutePath(path3)) {
            throw new BuildException(path3 + " is not an absolute path");
        }
        int colon = path3.indexOf(58);
        if (colon > 0 && (ON_DOS || ON_NETWARE)) {
            int next = colon + 1;
            String root2 = path3.substring(0, next);
            char[] ca = path3.toCharArray();
            root = root2 + sep;
            int next2 = ca[next] == sep ? next + 1 : next;
            StringBuffer sbPath = new StringBuffer();
            for (int i = next2; i < ca.length; i++) {
                if (ca[i] != sep || ca[i - 1] != sep) {
                    sbPath.append(ca[i]);
                }
            }
            path2 = sbPath.toString();
        } else if (path3.length() > 1 && path3.charAt(1) == sep) {
            int nextsep = path3.indexOf(sep, path3.indexOf(sep, 2) + 1);
            root = nextsep > 2 ? path3.substring(0, nextsep + 1) : path3;
            path2 = path3.substring(root.length());
        } else {
            root = File.separator;
            path2 = path3.substring(1);
        }
        return new String[]{root, path2};
    }

    public String toVMSPath(File f) {
        String path = normalize(f.getAbsolutePath()).getPath();
        String name = f.getName();
        boolean isAbsolute = path.charAt(0) == File.separatorChar;
        boolean isDirectory = f.isDirectory() && !name.regionMatches(true, name.length() - 4, ".DIR", 0, 4);
        String device = null;
        StringBuilder directory = null;
        String file = null;
        int index = 0;
        if (isAbsolute) {
            int index2 = path.indexOf(File.separatorChar, 1);
            if (index2 == -1) {
                return path.substring(1) + ":[000000]";
            }
            index = index2 + 1;
            device = path.substring(1, index2);
        }
        if (isDirectory) {
            directory = new StringBuilder(path.substring(index).replace(File.separatorChar, '.'));
        } else {
            int dirEnd = path.lastIndexOf(File.separatorChar);
            if (dirEnd == -1 || dirEnd < index) {
                file = path.substring(index);
            } else {
                directory = new StringBuilder(path.substring(index, dirEnd).replace(File.separatorChar, '.'));
                int index3 = dirEnd + 1;
                if (path.length() > index3) {
                    file = path.substring(index3);
                }
            }
        }
        if (!isAbsolute && directory != null) {
            directory.insert(0, '.');
        }
        String osPath = (device != null ? device + ":" : "") + (directory != null ? "[" + ((Object) directory) + "]" : "") + (file != null ? file : "");
        return osPath;
    }

    @Deprecated
    public File createTempFile(String prefix, String suffix, File parentDir) {
        return createTempFile(prefix, suffix, parentDir, false, false);
    }

    @Deprecated
    public File createTempFile(String prefix, String suffix, File parentDir, boolean deleteOnExit, boolean createFile) {
        return createTempFile(null, prefix, suffix, parentDir, deleteOnExit, createFile);
    }

    public File createTempFile(Project project, String prefix, String suffix, File parentDir, boolean deleteOnExit, boolean createFile) {
        File result;
        String p = null;
        if (parentDir != null) {
            p = parentDir.getPath();
        } else if (project != null && project.getProperty(MagicNames.TMPDIR) != null) {
            p = project.getProperty(MagicNames.TMPDIR);
        } else if (project != null && deleteOnExit) {
            if (project.getProperty(MagicNames.AUTO_TMPDIR) != null) {
                p = project.getProperty(MagicNames.AUTO_TMPDIR);
            } else {
                Path systemTempDirPath = new File(System.getProperty("java.io.tmpdir")).toPath();
                PosixFileAttributeView systemTempDirPosixAttributes = (PosixFileAttributeView) Files.getFileAttributeView(systemTempDirPath, PosixFileAttributeView.class, new LinkOption[0]);
                if (systemTempDirPosixAttributes != null) {
                    try {
                        File projectTempDir = Files.createTempDirectory(systemTempDirPath, "ant", TMPDIR_ATTRIBUTES).toFile();
                        projectTempDir.deleteOnExit();
                        p = projectTempDir.getAbsolutePath();
                        project.setProperty(MagicNames.AUTO_TMPDIR, p);
                    } catch (IOException e) {
                    }
                }
            }
        }
        String parent = p != null ? p : System.getProperty("java.io.tmpdir");
        if (prefix == null) {
            prefix = "null";
        }
        if (suffix == null) {
            suffix = "null";
        }
        if (createFile) {
            try {
                Path parentPath = new File(parent).toPath();
                PosixFileAttributeView parentPosixAttributes = (PosixFileAttributeView) Files.getFileAttributeView(parentPath, PosixFileAttributeView.class, new LinkOption[0]);
                result = Files.createTempFile(parentPath, prefix, suffix, parentPosixAttributes != null ? TMPFILE_ATTRIBUTES : NO_TMPFILE_ATTRIBUTES).toFile();
            } catch (IOException e2) {
                throw new BuildException("Could not create tempfile in " + parent, e2);
            }
        } else {
            DecimalFormat fmt = new DecimalFormat("#####");
            synchronized (rand) {
                do {
                    result = new File(parent, prefix + fmt.format(rand.nextInt(Integer.MAX_VALUE)) + suffix);
                } while (result.exists());
            }
        }
        if (deleteOnExit) {
            result.deleteOnExit();
        }
        return result;
    }

    @Deprecated
    public File createTempFile(String prefix, String suffix, File parentDir, boolean deleteOnExit) {
        return createTempFile(prefix, suffix, parentDir, deleteOnExit, false);
    }

    public boolean contentEquals(File f1, File f2) throws IOException {
        return contentEquals(f1, f2, false);
    }

    public boolean contentEquals(File f1, File f2, boolean textfile) throws IOException {
        return ResourceUtils.contentEquals(new FileResource(f1), new FileResource(f2), textfile);
    }

    @Deprecated
    public File getParentFile(File f) {
        if (f == null) {
            return null;
        }
        return f.getParentFile();
    }

    public static String readFully(Reader rdr) throws IOException {
        return readFully(rdr, 8192);
    }

    public static String readFully(Reader rdr, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        }
        char[] buffer = new char[bufferSize];
        int bufferLength = 0;
        StringBuilder textBuffer = new StringBuilder();
        while (bufferLength != -1) {
            bufferLength = rdr.read(buffer);
            if (bufferLength > 0) {
                textBuffer.append(buffer, 0, bufferLength);
            }
        }
        if (textBuffer.length() == 0) {
            return null;
        }
        return textBuffer.toString();
    }

    public static String safeReadFully(Reader reader) throws IOException {
        String ret = readFully(reader);
        return ret == null ? "" : ret;
    }

    public boolean createNewFile(File f) throws IOException {
        return f.createNewFile();
    }

    public boolean createNewFile(File f, boolean mkdirs) throws IOException {
        File parent = f.getParentFile();
        if (mkdirs && !parent.exists()) {
            parent.mkdirs();
        }
        return f.createNewFile();
    }

    @Deprecated
    public boolean isSymbolicLink(File parent, String name) throws IOException {
        return parent == null ? Files.isSymbolicLink(Paths.get(name, new String[0])) : Files.isSymbolicLink(Paths.get(parent.toPath().toString(), name));
    }

    public String removeLeadingPath(File leading, File path) {
        String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
        String p = normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return "";
        }
        if (!l.endsWith(File.separator)) {
            l = l + File.separator;
        }
        return p.startsWith(l) ? p.substring(l.length()) : p;
    }

    public boolean isLeadingPath(File leading, File path) {
        String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
        String p = normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return true;
        }
        if (!l.endsWith(File.separator)) {
            l = l + File.separator;
        }
        String up = File.separator + ".." + File.separator;
        if (l.contains(up) || p.contains(up) || (p + File.separator).contains(up)) {
            return false;
        }
        return p.startsWith(l);
    }

    public boolean isLeadingPath(File leading, File path, boolean resolveSymlinks) throws IOException {
        if (!resolveSymlinks) {
            return isLeadingPath(leading, path);
        }
        File l = leading.getCanonicalFile();
        File p = path.getCanonicalFile();
        while (!l.equals(p)) {
            p = p.getParentFile();
            if (p == null) {
                return false;
            }
        }
        return true;
    }

    public String toURI(String path) {
        return new File(path).toURI().toASCIIString();
    }

    public String fromURI(String uri) {
        synchronized (this.cacheFromUriLock) {
            if (uri.equals(this.cacheFromUriRequest)) {
                return this.cacheFromUriResponse;
            }
            String path = Locator.fromURI(uri);
            String ret = isAbsolutePath(path) ? normalize(path).getAbsolutePath() : path;
            this.cacheFromUriRequest = uri;
            this.cacheFromUriResponse = ret;
            return ret;
        }
    }

    public boolean fileNameEquals(File f1, File f2) {
        return normalize(f1.getAbsolutePath()).getAbsolutePath().equals(normalize(f2.getAbsolutePath()).getAbsolutePath());
    }

    public boolean areSame(File f1, File f2) throws IOException {
        if (f1 == null && f2 == null) {
            return true;
        }
        if (f1 == null || f2 == null) {
            return false;
        }
        File f1Normalized = normalize(f1.getAbsolutePath());
        File f2Normalized = normalize(f2.getAbsolutePath());
        return f1Normalized.equals(f2Normalized) || f1Normalized.getCanonicalFile().equals(f2Normalized.getCanonicalFile());
    }

    public void rename(File from, File to) throws IOException {
        File from2 = normalize(from.getAbsolutePath()).getCanonicalFile();
        File to2 = normalize(to.getAbsolutePath());
        if (!from2.exists()) {
            System.err.println("Cannot rename nonexistent file " + from2);
        } else if (from2.getAbsolutePath().equals(to2.getAbsolutePath())) {
            System.err.println("Rename of " + from2 + " to " + to2 + " is a no-op.");
        } else if (to2.exists() && !areSame(from2, to2) && !tryHardToDelete(to2)) {
            throw new IOException("Failed to delete " + to2 + " while trying to rename " + from2);
        } else {
            File parent = to2.getParentFile();
            if (parent != null && !parent.isDirectory() && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Failed to create directory " + parent + " while trying to rename " + from2);
            }
            if (!from2.renameTo(to2)) {
                copyFile(from2, to2);
                if (!tryHardToDelete(from2)) {
                    throw new IOException("Failed to delete " + from2 + " while trying to rename it.");
                }
            }
        }
    }

    public long getFileTimestampGranularity() {
        if (ON_WIN9X) {
            return FAT_FILE_TIMESTAMP_GRANULARITY;
        }
        if (ON_WINDOWS) {
            return 1L;
        }
        if (ON_DOS) {
            return FAT_FILE_TIMESTAMP_GRANULARITY;
        }
        return 1000L;
    }

    public boolean hasErrorInCase(File localFile) {
        File localFile2 = normalize(localFile.getAbsolutePath());
        if (!localFile2.exists()) {
            return false;
        }
        String localFileName = localFile2.getName();
        FilenameFilter ff = dir, name -> {
            return name.equalsIgnoreCase(localFileName) && !name.equals(localFileName);
        };
        String[] names = localFile2.getParentFile().list(ff);
        return names != null && names.length == 1;
    }

    public boolean isUpToDate(File source, File dest, long granularity) {
        if (!dest.exists()) {
            return false;
        }
        long sourceTime = source.lastModified();
        long destTime = dest.lastModified();
        return isUpToDate(sourceTime, destTime, granularity);
    }

    public boolean isUpToDate(File source, File dest) {
        return isUpToDate(source, dest, getFileTimestampGranularity());
    }

    public boolean isUpToDate(long sourceTime, long destTime, long granularity) {
        return destTime != -1 && destTime >= sourceTime + granularity;
    }

    public boolean isUpToDate(long sourceTime, long destTime) {
        return isUpToDate(sourceTime, destTime, getFileTimestampGranularity());
    }

    public static void close(Writer device) {
        close((AutoCloseable) device);
    }

    public static void close(Reader device) {
        close((AutoCloseable) device);
    }

    public static void close(OutputStream device) {
        close((AutoCloseable) device);
    }

    public static void close(InputStream device) {
        close((AutoCloseable) device);
    }

    public static void close(Channel device) {
        close((AutoCloseable) device);
    }

    public static void close(URLConnection conn) {
        if (conn != null) {
            try {
                if (conn instanceof JarURLConnection) {
                    JarURLConnection juc = (JarURLConnection) conn;
                    JarFile jf = juc.getJarFile();
                    jf.close();
                } else if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void close(AutoCloseable ac) {
        if (null != ac) {
            try {
                ac.close();
            } catch (Exception e) {
            }
        }
    }

    public static void delete(File file) {
        if (file != null) {
            file.delete();
        }
    }

    public boolean tryHardToDelete(File f) {
        return tryHardToDelete(f, ON_WINDOWS);
    }

    public boolean tryHardToDelete(File f, boolean runGC) {
        if (!f.delete()) {
            if (runGC) {
                System.gc();
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
            return f.delete();
        }
        return true;
    }

    public static String getRelativePath(File fromFile, File toFile) throws Exception {
        String fromPath = fromFile.getCanonicalPath();
        String toPath = toFile.getCanonicalPath();
        String[] fromPathStack = getPathStack(fromPath);
        String[] toPathStack = getPathStack(toPath);
        if (0 < toPathStack.length && 0 < fromPathStack.length) {
            if (!fromPathStack[0].equals(toPathStack[0])) {
                return getPath(Arrays.asList(toPathStack));
            }
            int minLength = Math.min(fromPathStack.length, toPathStack.length);
            int same = 1;
            while (same < minLength && fromPathStack[same].equals(toPathStack[same])) {
                same++;
            }
            List<String> relativePathStack = new ArrayList<>();
            for (int i = same; i < fromPathStack.length; i++) {
                relativePathStack.add("..");
            }
            relativePathStack.addAll(Arrays.asList(toPathStack).subList(same, toPathStack.length));
            return getPath(relativePathStack);
        }
        return getPath(Arrays.asList(toPathStack));
    }

    public static String[] getPathStack(String path) {
        String normalizedPath = path.replace(File.separatorChar, '/');
        return normalizedPath.split("/");
    }

    public static String getPath(List<String> pathStack) {
        return getPath(pathStack, '/');
    }

    public static String getPath(List<String> pathStack, char separatorChar) {
        return (String) pathStack.stream().collect(Collectors.joining(Character.toString(separatorChar)));
    }

    public String getDefaultEncoding() {
        InputStreamReader is = new InputStreamReader(new InputStream() { // from class: org.apache.tools.ant.util.FileUtils.1
            @Override // java.io.InputStream
            public int read() {
                return -1;
            }
        });
        try {
            return is.getEncoding();
        } finally {
            close((Reader) is);
        }
    }

    public static OutputStream newOutputStream(Path path, boolean append) throws IOException {
        if (append) {
            return Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
        }
        return Files.newOutputStream(path, new OpenOption[0]);
    }

    public static Optional<Boolean> isCaseSensitiveFileSystem(Path path) {
        Path mixedCaseTmpFile;
        boolean caseSensitive;
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        Path mixedCaseTmpFile2 = null;
        try {
            try {
                if (Files.isRegularFile(path, new LinkOption[0])) {
                    mixedCaseTmpFile = Files.createTempFile(path.getParent(), "aNt", null, new FileAttribute[0]);
                } else if (!Files.isDirectory(path, new LinkOption[0])) {
                    Optional<Boolean> empty = Optional.empty();
                    if (0 != 0) {
                        delete(mixedCaseTmpFile2.toFile());
                    }
                    return empty;
                } else {
                    mixedCaseTmpFile = Files.createTempFile(path, "aNt", null, new FileAttribute[0]);
                }
                Path lowerCasePath = Paths.get(mixedCaseTmpFile.toString().toLowerCase(Locale.US), new String[0]);
                try {
                    caseSensitive = !Files.isSameFile(mixedCaseTmpFile, lowerCasePath);
                } catch (NoSuchFileException e) {
                    caseSensitive = true;
                }
                if (mixedCaseTmpFile != null) {
                    delete(mixedCaseTmpFile.toFile());
                }
                return Optional.of(Boolean.valueOf(caseSensitive));
            } catch (IOException ioe) {
                System.err.println("Could not determine the case sensitivity of the filesystem for path " + path + " due to " + ioe);
                Optional<Boolean> empty2 = Optional.empty();
                if (0 != 0) {
                    delete(mixedCaseTmpFile2.toFile());
                }
                return empty2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                delete(mixedCaseTmpFile2.toFile());
            }
            throw th;
        }
    }
}
