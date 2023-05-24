package org.apache.tools.ant.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.types.resources.Appendable;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.tools.ant.types.resources.Touchable;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.selectors.Date;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.SelectorUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ResourceUtils.class */
public class ResourceUtils {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    @Deprecated
    public static final String ISO_8859_1 = "ISO-8859-1";
    private static final long MAX_IO_CHUNK_SIZE = 16777216;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ResourceUtils$ResourceSelectorProvider.class */
    public interface ResourceSelectorProvider {
        ResourceSelector getTargetSelectorForSource(Resource resource);
    }

    public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets) {
        return selectOutOfDateSources(logTo, source, mapper, targets, FILE_UTILS.getFileTimestampGranularity());
    }

    public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
        Union u = new Union();
        u.addAll(Arrays.asList(source));
        ResourceCollection rc = selectOutOfDateSources(logTo, u, mapper, targets, granularity);
        return rc.size() == 0 ? new Resource[0] : ((Union) rc).listResources();
    }

    public static ResourceCollection selectOutOfDateSources(ProjectComponent logTo, ResourceCollection source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
        logFuture(logTo, source, granularity);
        return selectSources(logTo, source, mapper, targets, sr -> {
            return target -> {
                return SelectorUtils.isOutOfDate(sr, target, granularity);
            };
        });
    }

    public static ResourceCollection selectSources(ProjectComponent logTo, ResourceCollection source, FileNameMapper mapper, ResourceFactory targets, ResourceSelectorProvider selector) {
        String[] strArr;
        if (source.isEmpty()) {
            logTo.log("No sources found.", 3);
            return Resources.NONE;
        }
        ResourceCollection source2 = Union.getInstance(source);
        Union result = new Union();
        for (Resource sr : source2) {
            String srName = sr.getName();
            if (srName != null) {
                srName = srName.replace('/', File.separatorChar);
            }
            String[] targetnames = null;
            try {
                targetnames = mapper.mapFileName(srName);
            } catch (Exception e) {
                logTo.log("Caught " + e + " mapping resource " + sr, 3);
            }
            if (targetnames == null || targetnames.length == 0) {
                logTo.log(sr + " skipped - don't know how to handle it", 3);
            } else {
                Union targetColl = new Union();
                for (String targetname : targetnames) {
                    if (targetname == null) {
                        targetname = "(no name)";
                    }
                    targetColl.add(targets.getResource(targetname.replace(File.separatorChar, '/')));
                }
                Restrict r = new Restrict();
                r.add(selector.getTargetSelectorForSource(sr));
                r.add(targetColl);
                if (r.size() > 0) {
                    result.add(sr);
                    Resource t = r.iterator().next();
                    logTo.log(sr.getName() + " added as " + t.getName() + (t.isExists() ? " is outdated." : " doesn't exist."), 3);
                } else {
                    logTo.log(sr.getName() + " omitted as " + targetColl.toString() + (targetColl.size() == 1 ? " is" : " are ") + " up to date.", 3);
                }
            }
        }
        return result;
    }

    public static void copyResource(Resource source, Resource dest) throws IOException {
        copyResource(source, dest, null);
    }

    public static void copyResource(Resource source, Resource dest, Project project) throws IOException {
        copyResource(source, dest, null, null, false, false, null, null, project);
    }

    public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
        copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
    }

    public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
        copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
    }

    public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project, boolean force) throws IOException {
        String effectiveInputEncoding;
        Touchable t;
        if (!overwrite && !SelectorUtils.isOutOfDate(source, dest, FileUtils.getFileUtils().getFileTimestampGranularity())) {
            return;
        }
        boolean filterSetsAvailable = filters != null && filters.hasFilters();
        boolean filterChainsAvailable = (filterChains == null || filterChains.isEmpty()) ? false : true;
        if (source instanceof StringResource) {
            effectiveInputEncoding = ((StringResource) source).getEncoding();
        } else {
            effectiveInputEncoding = inputEncoding;
        }
        File destFile = null;
        if (dest.as(FileProvider.class) != null) {
            destFile = ((FileProvider) dest.as(FileProvider.class)).getFile();
        }
        if (destFile != null && destFile.isFile() && !destFile.canWrite()) {
            if (!force) {
                throw new ReadOnlyTargetFileException(destFile);
            }
            if (!FILE_UTILS.tryHardToDelete(destFile)) {
                throw new IOException("failed to delete read-only destination file " + destFile);
            }
        }
        if (filterSetsAvailable) {
            copyWithFilterSets(source, dest, filters, filterChains, append, effectiveInputEncoding, outputEncoding, project);
        } else if (filterChainsAvailable || ((effectiveInputEncoding != null && !effectiveInputEncoding.equals(outputEncoding)) || (effectiveInputEncoding == null && outputEncoding != null))) {
            copyWithFilterChainsOrTranscoding(source, dest, filterChains, append, effectiveInputEncoding, outputEncoding, project);
        } else {
            boolean copied = false;
            if (source.as(FileProvider.class) != null && destFile != null && !append) {
                File sourceFile = ((FileProvider) source.as(FileProvider.class)).getFile();
                try {
                    copyUsingFileChannels(sourceFile, destFile, project);
                    copied = true;
                } catch (IOException ex) {
                    String msg = "Attempt to copy " + sourceFile + " to " + destFile + " using NIO Channels failed due to '" + ex.getMessage() + "'.  Falling back to streams.";
                    if (project != null) {
                        project.log(msg, 1);
                    } else {
                        System.err.println(msg);
                    }
                }
            }
            if (!copied) {
                copyUsingStreams(source, dest, append, project);
            }
        }
        if (preserveLastModified && (t = (Touchable) dest.as(Touchable.class)) != null) {
            setLastModified(t, source.getLastModified());
        }
    }

    public static void setLastModified(Touchable t, long time) {
        t.touch(time < 0 ? System.currentTimeMillis() : time);
    }

    public static boolean contentEquals(Resource r1, Resource r2, boolean text) throws IOException {
        if (r1.isExists() != r2.isExists()) {
            return false;
        }
        if (!r1.isExists()) {
            return true;
        }
        if (r1.isDirectory() || r2.isDirectory()) {
            return false;
        }
        if (r1.equals(r2)) {
            return true;
        }
        if (!text) {
            long s1 = r1.getSize();
            long s2 = r2.getSize();
            if (s1 != -1 && s2 != -1 && s1 != s2) {
                return false;
            }
        }
        return compareContent(r1, r2, text) == 0;
    }

    public static int compareContent(Resource r1, Resource r2, boolean text) throws IOException {
        if (r1.equals(r2)) {
            return 0;
        }
        boolean e1 = r1.isExists();
        boolean e2 = r2.isExists();
        if (!e1 && !e2) {
            return 0;
        }
        if (e1 != e2) {
            return e1 ? 1 : -1;
        }
        boolean d1 = r1.isDirectory();
        boolean d2 = r2.isDirectory();
        if (d1 && d2) {
            return 0;
        }
        return (d1 || d2) ? d1 ? -1 : 1 : text ? textCompare(r1, r2) : binaryCompare(r1, r2);
    }

    public static FileResource asFileResource(FileProvider fileProvider) {
        if ((fileProvider instanceof FileResource) || fileProvider == null) {
            return (FileResource) fileProvider;
        }
        return new FileResource(Project.getProject(fileProvider), fileProvider.getFile());
    }

    private static int binaryCompare(Resource r1, Resource r2) throws IOException {
        InputStream in1 = new BufferedInputStream(r1.getInputStream());
        try {
            InputStream in2 = new BufferedInputStream(r2.getInputStream());
            int b1 = in1.read();
            while (b1 != -1) {
                int b2 = in2.read();
                if (b1 == b2) {
                    b1 = in1.read();
                } else {
                    int i = b1 > b2 ? 1 : -1;
                    in2.close();
                    in1.close();
                    return i;
                }
            }
            int i2 = in2.read() == -1 ? 0 : -1;
            in2.close();
            in1.close();
            return i2;
        } catch (Throwable th) {
            try {
                in1.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static int textCompare(Resource r1, Resource r2) throws IOException {
        BufferedReader in1 = new BufferedReader(new InputStreamReader(r1.getInputStream()));
        try {
            BufferedReader in2 = new BufferedReader(new InputStreamReader(r2.getInputStream()));
            for (String expected = in1.readLine(); expected != null; expected = in1.readLine()) {
                String actual = in2.readLine();
                if (!expected.equals(actual)) {
                    if (actual != null) {
                        int compareTo = expected.compareTo(actual);
                        in2.close();
                        in1.close();
                        return compareTo;
                    }
                    in2.close();
                    in1.close();
                    return 1;
                }
            }
            int i = in2.readLine() == null ? 0 : -1;
            in2.close();
            in1.close();
            return i;
        } catch (Throwable th) {
            try {
                in1.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static void logFuture(ProjectComponent logTo, ResourceCollection rc, long granularity) {
        long now = System.currentTimeMillis() + granularity;
        Date sel = new Date();
        sel.setMillis(now);
        sel.setWhen(TimeComparison.AFTER);
        Restrict future = new Restrict();
        future.add(sel);
        future.add(rc);
        Iterator<Resource> it = future.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            logTo.log("Warning: " + r.getName() + " modified in the future.", 1);
        }
    }

    private static void copyWithFilterSets(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
        if (areSame(source, dest)) {
            log(project, "Skipping (self) copy of " + source + " to " + dest);
            return;
        }
        Reader in = filterWith(project, inputEncoding, filterChains, source.getInputStream());
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(getOutputStream(dest, append, project), charsetFor(outputEncoding)));
            LineTokenizer lineTokenizer = new LineTokenizer();
            lineTokenizer.setIncludeDelims(true);
            for (String line = lineTokenizer.getToken(in); line != null; line = lineTokenizer.getToken(in)) {
                if (line.isEmpty()) {
                    out.newLine();
                } else {
                    out.write(filters.replaceTokens(line));
                }
            }
            out.close();
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static Reader filterWith(Project project, String encoding, Vector<FilterChain> filterChains, InputStream input) {
        Reader r = new InputStreamReader(input, charsetFor(encoding));
        if (filterChains != null && !filterChains.isEmpty()) {
            ChainReaderHelper crh = new ChainReaderHelper();
            crh.setBufferSize(8192);
            crh.setPrimaryReader(r);
            crh.setFilterChains(filterChains);
            crh.setProject(project);
            r = crh.getAssembledReader();
        }
        return new BufferedReader(r);
    }

    private static Charset charsetFor(String encoding) {
        return encoding == null ? Charset.defaultCharset() : Charset.forName(encoding);
    }

    private static void copyWithFilterChainsOrTranscoding(Resource source, Resource dest, Vector<FilterChain> filterChains, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
        if (areSame(source, dest)) {
            log(project, "Skipping (self) copy of " + source + " to " + dest);
            return;
        }
        Reader in = filterWith(project, inputEncoding, filterChains, source.getInputStream());
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(getOutputStream(dest, append, project), charsetFor(outputEncoding)));
            char[] buffer = new char[8192];
            while (true) {
                int nRead = in.read(buffer, 0, buffer.length);
                if (nRead == -1) {
                    break;
                }
                out.write(buffer, 0, nRead);
            }
            out.close();
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void copyUsingFileChannels(File sourceFile, File destFile, Project project) throws IOException {
        if (FileUtils.getFileUtils().areSame(sourceFile, destFile)) {
            log(project, "Skipping (self) copy of " + sourceFile + " to " + destFile);
            return;
        }
        File parent = destFile.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("failed to create the parent directory for " + destFile);
        }
        FileChannel srcChannel = FileChannel.open(sourceFile.toPath(), StandardOpenOption.READ);
        try {
            FileChannel destChannel = FileChannel.open(destFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            long position = 0;
            long count = srcChannel.size();
            while (position < count) {
                long chunk = Math.min((long) MAX_IO_CHUNK_SIZE, count - position);
                position += destChannel.transferFrom(srcChannel, position, chunk);
            }
            if (destChannel != null) {
                destChannel.close();
            }
            if (srcChannel != null) {
                srcChannel.close();
            }
        } catch (Throwable th) {
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void copyUsingStreams(Resource source, Resource dest, boolean append, Project project) throws IOException {
        if (areSame(source, dest)) {
            log(project, "Skipping (self) copy of " + source + " to " + dest);
            return;
        }
        InputStream in = source.getInputStream();
        try {
            OutputStream out = getOutputStream(dest, append, project);
            byte[] buffer = new byte[8192];
            int count = 0;
            do {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static OutputStream getOutputStream(Resource resource, boolean append, Project project) throws IOException {
        if (append) {
            Appendable a = (Appendable) resource.as(Appendable.class);
            if (a != null) {
                return a.getAppendOutputStream();
            }
            String msg = "Appendable OutputStream not available for non-appendable resource " + resource + "; using plain OutputStream";
            if (project != null) {
                project.log(msg, 3);
            } else {
                System.out.println(msg);
            }
        }
        return resource.getOutputStream();
    }

    private static boolean areSame(Resource resource1, Resource resource2) throws IOException {
        if (resource1 == null || resource2 == null) {
            return false;
        }
        FileProvider fileResource1 = (FileProvider) resource1.as(FileProvider.class);
        FileProvider fileResource2 = (FileProvider) resource2.as(FileProvider.class);
        return (fileResource1 == null || fileResource2 == null || !FileUtils.getFileUtils().areSame(fileResource1.getFile(), fileResource2.getFile())) ? false : true;
    }

    private static void log(Project project, String message) {
        log(project, message, 3);
    }

    private static void log(Project project, String message, int level) {
        if (project == null) {
            System.out.println(message);
        } else {
            project.log(message, level);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ResourceUtils$ReadOnlyTargetFileException.class */
    public static class ReadOnlyTargetFileException extends IOException {
        private static final long serialVersionUID = 1;

        public ReadOnlyTargetFileException(File destFile) {
            super("can't write to read-only destination file " + destFile);
        }
    }
}
