package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/VerifyJar.class */
public class VerifyJar extends AbstractJarSignerTask {
    public static final String ERROR_NO_FILE = "Not found :";
    public static final String ERROR_NO_VERIFY = "Failed to verify ";
    private static final String VERIFIED_TEXT = "jar verified.";
    private boolean certificates = false;
    private BufferingOutputFilter outputCache = new BufferingOutputFilter();
    private String savedStorePass = null;

    public void setCertificates(boolean certificates) {
        this.certificates = certificates;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        boolean hasJar = this.jar != null;
        if (!hasJar && !hasResources()) {
            throw new BuildException(AbstractJarSignerTask.ERROR_NO_SOURCE);
        }
        beginExecution();
        RedirectorElement redirector = getRedirector();
        redirector.setAlwaysLog(true);
        FilterChain outputFilterChain = redirector.createOutputFilterChain();
        outputFilterChain.add(this.outputCache);
        try {
            Path sources = createUnifiedSourcePath();
            Iterator<Resource> it = sources.iterator();
            while (it.hasNext()) {
                Resource r = it.next();
                FileProvider fr = (FileProvider) r.as(FileProvider.class);
                verifyOneJar(fr.getFile());
            }
        } finally {
            endExecution();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.AbstractJarSignerTask
    public void beginExecution() {
        if (this.storepass != null) {
            this.savedStorePass = this.storepass;
            setStorepass(null);
        }
        super.beginExecution();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.AbstractJarSignerTask
    public void endExecution() {
        if (this.savedStorePass != null) {
            setStorepass(this.savedStorePass);
            this.savedStorePass = null;
        }
        super.endExecution();
    }

    private void verifyOneJar(File jar) {
        if (!jar.exists()) {
            throw new BuildException(ERROR_NO_FILE + jar);
        }
        ExecTask cmd = createJarSigner();
        setCommonOptions(cmd);
        bindToKeystore(cmd);
        if (this.savedStorePass != null) {
            addValue(cmd, "-storepass");
            addValue(cmd, this.savedStorePass);
        }
        addValue(cmd, "-verify");
        if (this.certificates) {
            addValue(cmd, "-certs");
        }
        addValue(cmd, jar.getPath());
        if (this.alias != null) {
            addValue(cmd, this.alias);
        }
        log("Verifying JAR: " + jar.getAbsolutePath());
        this.outputCache.clear();
        BuildException ex = null;
        try {
            cmd.execute();
        } catch (BuildException e) {
            ex = e;
        }
        String results = this.outputCache.toString();
        if (ex != null) {
            if (results.contains("zip file closed")) {
                log("You are running jarsigner against a JVM with a known bug that manifests as an IllegalStateException.", 1);
            } else {
                throw ex;
            }
        }
        if (!results.contains(VERIFIED_TEXT)) {
            throw new BuildException(ERROR_NO_VERIFY + jar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/VerifyJar$BufferingOutputFilter.class */
    public static class BufferingOutputFilter implements ChainableReader {
        private BufferingOutputFilterReader buffer;

        private BufferingOutputFilter() {
        }

        @Override // org.apache.tools.ant.filters.ChainableReader
        public Reader chain(Reader rdr) {
            this.buffer = new BufferingOutputFilterReader(rdr);
            return this.buffer;
        }

        public String toString() {
            return this.buffer.toString();
        }

        public void clear() {
            if (this.buffer != null) {
                this.buffer.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/VerifyJar$BufferingOutputFilterReader.class */
    public static class BufferingOutputFilterReader extends Reader {
        private Reader next;
        private StringBuffer buffer = new StringBuffer();

        public BufferingOutputFilterReader(Reader next) {
            this.next = next;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf, int off, int len) throws IOException {
            int result = this.next.read(cbuf, off, len);
            this.buffer.append(cbuf, off, len);
            return result;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.next.close();
        }

        public String toString() {
            return this.buffer.toString();
        }

        public void clear() {
            this.buffer = new StringBuffer();
        }
    }
}
