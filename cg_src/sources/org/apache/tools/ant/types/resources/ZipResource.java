package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ZipResource.class */
public class ZipResource extends ArchiveResource {
    private String encoding;
    private ZipExtraField[] extras;
    private int method;

    public ZipResource() {
    }

    public ZipResource(File z, String enc, ZipEntry e) {
        super(z, true);
        setEncoding(enc);
        setEntry(e);
    }

    public void setZipfile(File z) {
        setArchive(z);
    }

    public File getZipfile() {
        FileProvider fp = (FileProvider) getArchive().as(FileProvider.class);
        return fp.getFile();
    }

    @Override // org.apache.tools.ant.types.resources.ArchiveResource
    public void addConfigured(ResourceCollection a) {
        super.addConfigured(a);
        if (!a.isFilesystemOnly()) {
            throw new BuildException("only filesystem resources are supported");
        }
    }

    public void setEncoding(String enc) {
        checkAttributesAllowed();
        this.encoding = enc;
    }

    public String getEncoding() {
        return isReference() ? getRef().getEncoding() : this.encoding;
    }

    @Override // org.apache.tools.ant.types.resources.ArchiveResource, org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.encoding != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return getRef().getInputStream();
        }
        return getZipEntryStream(new ZipFile(getZipfile(), getEncoding()), getName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        throw new UnsupportedOperationException("Use the zip task for zip output.");
    }

    public ZipExtraField[] getExtraFields() {
        if (isReference()) {
            return getRef().getExtraFields();
        }
        checkEntry();
        if (this.extras == null) {
            return new ZipExtraField[0];
        }
        return this.extras;
    }

    public int getMethod() {
        return this.method;
    }

    public static InputStream getZipEntryStream(final ZipFile zipFile, String zipEntry) throws IOException {
        ZipEntry ze = zipFile.getEntry(zipEntry);
        if (ze == null) {
            zipFile.close();
            throw new BuildException("no entry " + zipEntry + " in " + zipFile.getName());
        }
        return new FilterInputStream(zipFile.getInputStream(ze)) { // from class: org.apache.tools.ant.types.resources.ZipResource.1
            @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                FileUtils.close(this.in);
                zipFile.close();
            }

            protected void finalize() throws Throwable {
                try {
                    close();
                } finally {
                    super.finalize();
                }
            }
        };
    }

    @Override // org.apache.tools.ant.types.resources.ArchiveResource
    protected void fetchEntry() {
        ZipFile z = null;
        try {
            try {
                z = new ZipFile(getZipfile(), getEncoding());
                setEntry(z.getEntry(getName()));
                ZipFile.closeQuietly(z);
            } catch (IOException e) {
                log(e.getMessage(), 4);
                throw new BuildException(e);
            }
        } catch (Throwable th) {
            ZipFile.closeQuietly(z);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.resources.ArchiveResource, org.apache.tools.ant.types.Resource
    public ZipResource getRef() {
        return (ZipResource) getCheckedRef(ZipResource.class);
    }

    private void setEntry(ZipEntry e) {
        if (e == null) {
            setExists(false);
            return;
        }
        setName(e.getName());
        setExists(true);
        setLastModified(e.getTime());
        setDirectory(e.isDirectory());
        setSize(e.getSize());
        setMode(e.getUnixMode());
        this.extras = e.getExtraFields(true);
        this.method = e.getMethod();
    }
}
