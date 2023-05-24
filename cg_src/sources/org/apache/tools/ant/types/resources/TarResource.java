package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/TarResource.class */
public class TarResource extends ArchiveResource {
    private String userName;
    private String groupName;
    private long uid;
    private long gid;

    public TarResource() {
        this.userName = "";
        this.groupName = "";
    }

    public TarResource(File a, TarEntry e) {
        super(a, true);
        this.userName = "";
        this.groupName = "";
        setEntry(e);
    }

    public TarResource(Resource a, TarEntry e) {
        super(a, true);
        this.userName = "";
        this.groupName = "";
        setEntry(e);
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        TarEntry te;
        if (isReference()) {
            return getRef().getInputStream();
        }
        Resource archive = getArchive();
        TarInputStream i = new TarInputStream(archive.getInputStream());
        do {
            te = i.getNextEntry();
            if (te == null) {
                FileUtils.close((InputStream) i);
                throw new BuildException("no entry " + getName() + " in " + getArchive());
            }
        } while (!te.getName().equals(getName()));
        return i;
    }

    @Override // org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        throw new UnsupportedOperationException("Use the tar task for tar output.");
    }

    public String getUserName() {
        if (isReference()) {
            return getRef().getUserName();
        }
        checkEntry();
        return this.userName;
    }

    public String getGroup() {
        if (isReference()) {
            return getRef().getGroup();
        }
        checkEntry();
        return this.groupName;
    }

    public long getLongUid() {
        if (isReference()) {
            return getRef().getLongUid();
        }
        checkEntry();
        return this.uid;
    }

    @Deprecated
    public int getUid() {
        return (int) getLongUid();
    }

    public long getLongGid() {
        if (isReference()) {
            return getRef().getLongGid();
        }
        checkEntry();
        return this.gid;
    }

    @Deprecated
    public int getGid() {
        return (int) getLongGid();
    }

    @Override // org.apache.tools.ant.types.resources.ArchiveResource
    protected void fetchEntry() {
        TarEntry te;
        Resource archive = getArchive();
        try {
            TarInputStream i = new TarInputStream(archive.getInputStream());
            do {
                te = i.getNextEntry();
                if (te == null) {
                    i.close();
                    setEntry(null);
                    return;
                }
            } while (!te.getName().equals(getName()));
            setEntry(te);
            i.close();
        } catch (IOException e) {
            log(e.getMessage(), 4);
            throw new BuildException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.resources.ArchiveResource, org.apache.tools.ant.types.Resource
    public TarResource getRef() {
        return (TarResource) getCheckedRef(TarResource.class);
    }

    private void setEntry(TarEntry e) {
        if (e == null) {
            setExists(false);
            return;
        }
        setName(e.getName());
        setExists(true);
        setLastModified(e.getModTime().getTime());
        setDirectory(e.isDirectory());
        setSize(e.getSize());
        setMode(e.getMode());
        this.userName = e.getUserName();
        this.groupName = e.getGroupName();
        this.uid = e.getLongUserId();
        this.gid = e.getLongGroupId();
    }
}
