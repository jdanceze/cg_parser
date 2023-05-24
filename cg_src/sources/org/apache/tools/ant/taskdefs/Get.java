package org.apache.tools.ant.taskdefs;

import com.google.common.net.HttpHeaders;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.email.Header;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.URLProvider;
import org.apache.tools.ant.types.resources.URLResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get.class */
public class Get extends Task {
    private static final int NUMBER_RETRIES = 3;
    private static final int DOTS_PER_LINE = 50;
    private static final int BIG_BUFFER_SIZE = 102400;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final int REDIRECT_LIMIT = 25;
    private static final int HTTP_MOVED_TEMP = 307;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String DEFAULT_AGENT_PREFIX = "Apache Ant";
    private static final String GZIP_CONTENT_ENCODING = "gzip";
    private File destination;
    private final Resources sources = new Resources();
    private boolean verbose = false;
    private boolean quiet = false;
    private boolean useTimestamp = false;
    private boolean ignoreErrors = false;
    private String uname = null;
    private String pword = null;
    private long maxTime = 0;
    private int numberRetries = 3;
    private boolean skipExisting = false;
    private boolean httpUseCaches = true;
    private boolean tryGzipEncoding = false;
    private Mapper mapperElement = null;
    private String userAgent = System.getProperty(MagicNames.HTTP_AGENT_PROPERTY, "Apache Ant/" + Main.getShortAntVersion());
    private Map<String, String> headers = new LinkedHashMap();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get$DownloadProgress.class */
    public interface DownloadProgress {
        void beginDownload();

        void onTick();

        void endDownload();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:4|(1:51)(2:6|(6:43|44|(1:46)|47|(1:49)|50)(3:8|(3:40|41|42)(3:10|11|(3:37|38|39)(3:13|14|(3:34|35|36)(2:16|17)))|25))|18|(1:20)|21|22|24|25|2) */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0156, code lost:
        r14 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0158, code lost:
        log("Error getting " + r0 + " to " + r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x017e, code lost:
        if (r6.ignoreErrors == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x018e, code lost:
        throw new org.apache.tools.ant.BuildException(r14, getLocation());
     */
    @Override // org.apache.tools.ant.Task
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void execute() throws org.apache.tools.ant.BuildException {
        /*
            Method dump skipped, instructions count: 403
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.taskdefs.Get.execute():void");
    }

    @Deprecated
    public boolean doGet(int logLevel, DownloadProgress progress) throws IOException {
        checkAttributes();
        return doGet(((URLProvider) this.sources.iterator().next().as(URLProvider.class)).getURL(), this.destination, logLevel, progress);
    }

    public boolean doGet(URL source, File dest, int logLevel, DownloadProgress progress) throws IOException {
        if (dest.exists() && this.skipExisting) {
            log("Destination already exists (skipping): " + dest.getAbsolutePath(), logLevel);
            return true;
        }
        if (progress == null) {
            progress = new NullProgress();
        }
        log("Getting: " + source, logLevel);
        log("To: " + dest.getAbsolutePath(), logLevel);
        long timestamp = 0;
        boolean hasTimestamp = false;
        if (this.useTimestamp && dest.exists()) {
            timestamp = dest.lastModified();
            if (this.verbose) {
                Date t = new Date(timestamp);
                log("local file date : " + t.toString(), logLevel);
            }
            hasTimestamp = true;
        }
        GetThread getThread = new GetThread(source, dest, hasTimestamp, timestamp, progress, logLevel, this.userAgent);
        getThread.setDaemon(true);
        getProject().registerThreadTask(getThread, this);
        getThread.start();
        try {
            getThread.join(this.maxTime * 1000);
        } catch (InterruptedException e) {
            log("interrupted waiting for GET to finish", 3);
        }
        if (getThread.isAlive()) {
            String msg = "The GET operation took longer than " + this.maxTime + " seconds, stopping it.";
            if (this.ignoreErrors) {
                log(msg);
            }
            getThread.closeStreams();
            if (!this.ignoreErrors) {
                throw new BuildException(msg);
            }
            return false;
        }
        return getThread.wasSuccessful();
    }

    @Override // org.apache.tools.ant.Task, org.apache.tools.ant.ProjectComponent
    public void log(String msg, int msgLevel) {
        if (!this.quiet || msgLevel <= 0) {
            super.log(msg, msgLevel);
        }
    }

    private void checkAttributes() {
        if (this.userAgent == null || this.userAgent.trim().isEmpty()) {
            throw new BuildException("userAgent may not be null or empty");
        }
        if (this.sources.size() == 0) {
            throw new BuildException("at least one source is required", getLocation());
        }
        Iterator<Resource> it = this.sources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            URLProvider up = (URLProvider) r.as(URLProvider.class);
            if (up == null) {
                throw new BuildException("Only URLProvider resources are supported", getLocation());
            }
        }
        if (this.destination == null) {
            throw new BuildException("dest attribute is required", getLocation());
        }
        if (this.destination.exists() && this.sources.size() > 1 && !this.destination.isDirectory()) {
            throw new BuildException("The specified destination is not a directory", getLocation());
        }
        if (this.destination.exists() && !this.destination.canWrite()) {
            throw new BuildException("Can't write to " + this.destination.getAbsolutePath(), getLocation());
        }
        if (this.sources.size() > 1 && !this.destination.exists()) {
            this.destination.mkdirs();
        }
    }

    public void setSrc(URL u) {
        add(new URLResource(u));
    }

    public void add(ResourceCollection rc) {
        this.sources.add(rc);
    }

    public void setDest(File dest) {
        this.destination = dest;
    }

    public void setVerbose(boolean v) {
        this.verbose = v;
    }

    public void setQuiet(boolean v) {
        this.quiet = v;
    }

    public void setIgnoreErrors(boolean v) {
        this.ignoreErrors = v;
    }

    public void setUseTimestamp(boolean v) {
        this.useTimestamp = v;
    }

    public void setUsername(String u) {
        this.uname = u;
    }

    public void setPassword(String p) {
        this.pword = p;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void setRetries(int r) {
        if (r <= 0) {
            log("Setting retries to " + r + " will make the task not even try to reach the URI at all", 1);
        }
        this.numberRetries = r;
    }

    public void setSkipExisting(boolean s) {
        this.skipExisting = s;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setHttpUseCaches(boolean httpUseCache) {
        this.httpUseCaches = httpUseCache;
    }

    public void setTryGzipEncoding(boolean b) {
        this.tryGzipEncoding = b;
    }

    public void addConfiguredHeader(Header header) {
        if (header != null) {
            String key = StringUtils.trimToNull(header.getName());
            String value = StringUtils.trimToNull(header.getValue());
            if (key != null && value != null) {
                this.headers.put(key, value);
            }
        }
    }

    public Mapper createMapper() throws BuildException {
        if (this.mapperElement != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS, getLocation());
        }
        this.mapperElement = new Mapper(getProject());
        return this.mapperElement;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get$Base64Converter.class */
    public static class Base64Converter extends org.apache.tools.ant.util.Base64Converter {
        protected Base64Converter() {
        }
    }

    public static boolean isMoved(int responseCode) {
        return responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get$NullProgress.class */
    public static class NullProgress implements DownloadProgress {
        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void beginDownload() {
        }

        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void onTick() {
        }

        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void endDownload() {
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get$VerboseProgress.class */
    public static class VerboseProgress implements DownloadProgress {
        private int dots = 0;
        PrintStream out;

        public VerboseProgress(PrintStream out) {
            this.out = out;
        }

        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void beginDownload() {
            this.dots = 0;
        }

        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void onTick() {
            this.out.print(".");
            int i = this.dots;
            this.dots = i + 1;
            if (i > 50) {
                this.out.flush();
                this.dots = 0;
            }
        }

        @Override // org.apache.tools.ant.taskdefs.Get.DownloadProgress
        public void endDownload() {
            this.out.println();
            this.out.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Get$GetThread.class */
    public class GetThread extends Thread {
        private final URL source;
        private final File dest;
        private final boolean hasTimestamp;
        private final long timestamp;
        private final DownloadProgress progress;
        private final int logLevel;
        private URLConnection connection;
        private String userAgent;
        private boolean success = false;
        private IOException ioexception = null;
        private BuildException exception = null;
        private InputStream is = null;
        private OutputStream os = null;
        private int redirections = 0;

        GetThread(URL source, File dest, boolean h, long t, DownloadProgress p, int l, String userAgent) {
            this.userAgent = null;
            this.source = source;
            this.dest = dest;
            this.hasTimestamp = h;
            this.timestamp = t;
            this.progress = p;
            this.logLevel = l;
            this.userAgent = userAgent;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                this.success = get();
            } catch (IOException ioex) {
                this.ioexception = ioex;
            } catch (BuildException bex) {
                this.exception = bex;
            }
        }

        private boolean get() throws IOException, BuildException {
            this.connection = openConnection(this.source);
            if (this.connection == null) {
                return false;
            }
            boolean downloadSucceeded = downloadFile();
            if (downloadSucceeded && Get.this.useTimestamp) {
                updateTimeStamp();
            }
            return downloadSucceeded;
        }

        private boolean redirectionAllowed(URL aSource, URL aDest) {
            if (!aSource.getProtocol().equals(aDest.getProtocol()) && (!"http".equals(aSource.getProtocol()) || !Get.HTTPS.equals(aDest.getProtocol()))) {
                String message = "Redirection detected from " + aSource.getProtocol() + " to " + aDest.getProtocol() + ". Protocol switch unsafe, not allowed.";
                if (Get.this.ignoreErrors) {
                    Get.this.log(message, this.logLevel);
                    return false;
                }
                throw new BuildException(message);
            }
            this.redirections++;
            if (this.redirections > 25) {
                if (Get.this.ignoreErrors) {
                    Get.this.log("More than 25 times redirected, giving up", this.logLevel);
                    return false;
                }
                throw new BuildException("More than 25 times redirected, giving up");
            }
            return true;
        }

        private URLConnection openConnection(URL aSource) throws IOException {
            URLConnection connection = aSource.openConnection();
            if (this.hasTimestamp) {
                connection.setIfModifiedSince(this.timestamp);
            }
            connection.addRequestProperty("User-Agent", this.userAgent);
            if (Get.this.uname != null || Get.this.pword != null) {
                String up = Get.this.uname + ":" + Get.this.pword;
                Base64Converter encoder = new Base64Converter();
                String encoding = encoder.encode(up.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + encoding);
            }
            if (Get.this.tryGzipEncoding) {
                connection.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, Get.GZIP_CONTENT_ENCODING);
            }
            for (Map.Entry<String, String> header : Get.this.headers.entrySet()) {
                Get.this.log(String.format("Adding header '%s' ", header.getKey()));
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
                connection.setUseCaches(Get.this.httpUseCaches);
            }
            try {
                connection.connect();
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (Get.isMoved(responseCode)) {
                        String newLocation = httpConnection.getHeaderField(HttpHeaders.LOCATION);
                        String message = aSource + (responseCode == 301 ? " permanently" : "") + " moved to " + newLocation;
                        Get.this.log(message, this.logLevel);
                        URL newURL = new URL(aSource, newLocation);
                        if (!redirectionAllowed(aSource, newURL)) {
                            return null;
                        }
                        return openConnection(newURL);
                    }
                    long lastModified = httpConnection.getLastModified();
                    if (responseCode == 304 || (lastModified != 0 && this.hasTimestamp && this.timestamp >= lastModified)) {
                        Get.this.log("Not modified - so not downloaded", this.logLevel);
                        return null;
                    } else if (responseCode == 401) {
                        if (Get.this.ignoreErrors) {
                            Get.this.log("HTTP Authorization failure", this.logLevel);
                            return null;
                        }
                        throw new BuildException("HTTP Authorization failure");
                    }
                }
                return connection;
            } catch (NullPointerException e) {
                throw new BuildException("Failed to parse " + this.source.toString(), e);
            }
        }

        private boolean downloadFile() throws IOException {
            int length;
            for (int i = 0; i < Get.this.numberRetries; i++) {
                try {
                    this.is = this.connection.getInputStream();
                    break;
                } catch (IOException ex) {
                    Get.this.log("Error opening connection " + ex, this.logLevel);
                }
            }
            if (this.is != null) {
                if (Get.this.tryGzipEncoding && Get.GZIP_CONTENT_ENCODING.equals(this.connection.getContentEncoding())) {
                    this.is = new GZIPInputStream(this.is);
                }
                this.os = Files.newOutputStream(this.dest.toPath(), new OpenOption[0]);
                this.progress.beginDownload();
                boolean finished = false;
                try {
                    byte[] buffer = new byte[Get.BIG_BUFFER_SIZE];
                    while (!isInterrupted() && (length = this.is.read(buffer)) >= 0) {
                        this.os.write(buffer, 0, length);
                        this.progress.onTick();
                    }
                    finished = !isInterrupted();
                    this.progress.endDownload();
                    return true;
                } finally {
                    FileUtils.close(this.os);
                    FileUtils.close(this.is);
                    if (!finished) {
                        this.dest.delete();
                    }
                }
            }
            Get.this.log("Can't get " + this.source + " to " + this.dest, this.logLevel);
            if (Get.this.ignoreErrors) {
                return false;
            }
            throw new BuildException("Can't get " + this.source + " to " + this.dest, Get.this.getLocation());
        }

        private void updateTimeStamp() {
            long remoteTimestamp = this.connection.getLastModified();
            if (Get.this.verbose) {
                Date t = new Date(remoteTimestamp);
                Get.this.log("last modified = " + t.toString() + (remoteTimestamp == 0 ? " - using current time instead" : ""), this.logLevel);
            }
            if (remoteTimestamp != 0) {
                Get.FILE_UTILS.setFileLastModified(this.dest, remoteTimestamp);
            }
        }

        boolean wasSuccessful() throws IOException, BuildException {
            if (this.ioexception != null) {
                throw this.ioexception;
            }
            if (this.exception != null) {
                throw this.exception;
            }
            return this.success;
        }

        void closeStreams() {
            interrupt();
            FileUtils.close(this.os);
            FileUtils.close(this.is);
            if (!this.success && this.dest.exists()) {
                this.dest.delete();
            }
        }
    }
}
