package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/URLResource.class */
public class URLResource extends Resource implements URLProvider {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final int NULL_URL = Resource.getMagicNumber("null URL".getBytes());
    private URL url;
    private URLConnection conn;
    private URL baseURL;
    private String relPath;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/URLResource$ConnectionUser.class */
    public interface ConnectionUser {
        long useConnection(URLConnection uRLConnection);
    }

    public URLResource() {
    }

    public URLResource(URL u) {
        setURL(u);
    }

    public URLResource(URLProvider u) {
        setURL(u.getURL());
    }

    public URLResource(File f) {
        setFile(f);
    }

    public URLResource(String u) {
        this(newURL(u));
    }

    public synchronized void setURL(URL u) {
        checkAttributesAllowed();
        this.url = u;
    }

    public synchronized void setFile(File f) {
        try {
            setURL(FILE_UTILS.getFileURL(f));
        } catch (MalformedURLException e) {
            throw new BuildException(e);
        }
    }

    public synchronized void setBaseURL(URL base) {
        checkAttributesAllowed();
        if (this.url != null) {
            throw new BuildException("can't define URL and baseURL attribute");
        }
        this.baseURL = base;
    }

    public synchronized void setRelativePath(String r) {
        checkAttributesAllowed();
        if (this.url != null) {
            throw new BuildException("can't define URL and relativePath attribute");
        }
        this.relPath = r;
    }

    @Override // org.apache.tools.ant.types.resources.URLProvider
    public synchronized URL getURL() {
        if (isReference()) {
            return getRef().getURL();
        }
        if (this.url == null && this.baseURL != null) {
            if (this.relPath == null) {
                throw new BuildException("must provide relativePath attribute when using baseURL.");
            }
            try {
                this.url = new URL(this.baseURL, this.relPath);
            } catch (MalformedURLException e) {
                throw new BuildException(e);
            }
        }
        return this.url;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public synchronized void setRefid(Reference r) {
        if (this.url != null || this.baseURL != null || this.relPath != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized String getName() {
        if (isReference()) {
            return getRef().getName();
        }
        String name = getURL().getFile();
        return name.isEmpty() ? name : name.substring(1);
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public synchronized String toString() {
        return isReference() ? getRef().toString() : String.valueOf(getURL());
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized boolean isExists() {
        if (isReference()) {
            return getRef().isExists();
        }
        return isExists(false);
    }

    private synchronized boolean isExists(boolean closeConnection) {
        if (getURL() == null) {
            return false;
        }
        try {
            connect(3);
            if (this.conn instanceof HttpURLConnection) {
                int sc = ((HttpURLConnection) this.conn).getResponseCode();
                boolean z = sc < 400;
                if (closeConnection) {
                    close();
                }
                return z;
            }
            if (this.url.getProtocol().startsWith("ftp")) {
                closeConnection = true;
                InputStream in = this.conn.getInputStream();
                FileUtils.close(in);
            }
            if (closeConnection) {
                close();
            }
            return true;
        } catch (IOException e) {
            if (closeConnection) {
                close();
            }
            return false;
        } catch (Throwable th) {
            if (closeConnection) {
                close();
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized long getLastModified() {
        if (isReference()) {
            return getRef().getLastModified();
        }
        if (!isExists(false)) {
            return 0L;
        }
        return withConnection(c -> {
            return this.conn.getLastModified();
        }, 0L);
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized boolean isDirectory() {
        if (isReference()) {
            return getRef().isDirectory();
        }
        return getName().endsWith("/");
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized long getSize() {
        if (isReference()) {
            return getRef().getSize();
        }
        if (!isExists(false)) {
            return 0L;
        }
        return withConnection(c -> {
            return this.conn.getContentLength();
        }, -1L);
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (isReference()) {
            return getRef().equals(another);
        }
        if (another == null || another.getClass() != getClass()) {
            return false;
        }
        URLResource other = (URLResource) another;
        if (getURL() == null) {
            return other.getURL() == null;
        }
        return getURL().equals(other.getURL());
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        return MAGIC * (getURL() == null ? NULL_URL : getURL().hashCode());
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized InputStream getInputStream() throws IOException {
        if (isReference()) {
            return getRef().getInputStream();
        }
        connect();
        try {
            return this.conn.getInputStream();
        } finally {
            this.conn = null;
        }
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        connect();
        try {
            return this.conn.getOutputStream();
        } finally {
            this.conn = null;
        }
    }

    protected void connect() throws IOException {
        connect(0);
    }

    protected synchronized void connect(int logLevel) throws IOException {
        URL u = getURL();
        if (u == null) {
            throw new BuildException("URL not set");
        }
        if (this.conn == null) {
            try {
                this.conn = u.openConnection();
                this.conn.connect();
            } catch (IOException e) {
                log(e.toString(), logLevel);
                this.conn = null;
                throw e;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public URLResource getRef() {
        return (URLResource) getCheckedRef(URLResource.class);
    }

    private synchronized void close() {
        try {
            FileUtils.close(this.conn);
        } finally {
            this.conn = null;
        }
    }

    private static URL newURL(String u) {
        try {
            return new URL(u);
        } catch (MalformedURLException e) {
            throw new BuildException(e);
        }
    }

    private long withConnection(ConnectionUser u, long defaultValue) {
        try {
            if (this.conn != null) {
                return u.useConnection(this.conn);
            }
            connect();
            long useConnection = u.useConnection(this.conn);
            close();
            return useConnection;
        } catch (IOException e) {
            return defaultValue;
        }
    }
}
