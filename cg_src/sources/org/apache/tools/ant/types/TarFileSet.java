package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/TarFileSet.class */
public class TarFileSet extends ArchiveFileSet {
    private boolean userNameSet;
    private boolean groupNameSet;
    private boolean userIdSet;
    private boolean groupIdSet;
    private String userName;
    private String groupName;
    private int uid;
    private int gid;

    public TarFileSet() {
        this.userName = "";
        this.groupName = "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TarFileSet(FileSet fileset) {
        super(fileset);
        this.userName = "";
        this.groupName = "";
    }

    protected TarFileSet(TarFileSet fileset) {
        super((ArchiveFileSet) fileset);
        this.userName = "";
        this.groupName = "";
    }

    public void setUserName(String userName) {
        checkTarFileSetAttributesAllowed();
        this.userNameSet = true;
        this.userName = userName;
    }

    public String getUserName() {
        if (isReference()) {
            return ((TarFileSet) getRef()).getUserName();
        }
        return this.userName;
    }

    public boolean hasUserNameBeenSet() {
        return this.userNameSet;
    }

    public void setUid(int uid) {
        checkTarFileSetAttributesAllowed();
        this.userIdSet = true;
        this.uid = uid;
    }

    public int getUid() {
        if (isReference()) {
            return ((TarFileSet) getRef()).getUid();
        }
        return this.uid;
    }

    public boolean hasUserIdBeenSet() {
        return this.userIdSet;
    }

    public void setGroup(String groupName) {
        checkTarFileSetAttributesAllowed();
        this.groupNameSet = true;
        this.groupName = groupName;
    }

    public String getGroup() {
        if (isReference()) {
            return ((TarFileSet) getRef()).getGroup();
        }
        return this.groupName;
    }

    public boolean hasGroupBeenSet() {
        return this.groupNameSet;
    }

    public void setGid(int gid) {
        checkTarFileSetAttributesAllowed();
        this.groupIdSet = true;
        this.gid = gid;
    }

    public int getGid() {
        if (isReference()) {
            return ((TarFileSet) getRef()).getGid();
        }
        return this.gid;
    }

    public boolean hasGroupIdBeenSet() {
        return this.groupIdSet;
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet
    protected ArchiveScanner newArchiveScanner() {
        TarScanner zs = new TarScanner();
        zs.setEncoding(getEncoding());
        return zs;
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.userNameSet || this.userIdSet || this.groupNameSet || this.groupIdSet) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet
    public AbstractFileSet getRef(Project p) {
        dieOnCircularReference(p);
        Object o = getRefid().getReferencedObject(p);
        if (o instanceof TarFileSet) {
            return (AbstractFileSet) o;
        }
        if (o instanceof FileSet) {
            TarFileSet zfs = new TarFileSet((FileSet) o);
            configureFileSet(zfs);
            return zfs;
        }
        String msg = getRefid().getRefId() + " doesn't denote a tarfileset or a fileset";
        throw new BuildException(msg);
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet
    protected AbstractFileSet getRef() {
        return getRef(getProject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.ArchiveFileSet
    public void configureFileSet(ArchiveFileSet zfs) {
        super.configureFileSet(zfs);
        if (zfs instanceof TarFileSet) {
            TarFileSet tfs = (TarFileSet) zfs;
            tfs.setUserName(this.userName);
            tfs.setGroup(this.groupName);
            tfs.setUid(this.uid);
            tfs.setGid(this.gid);
        }
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet, org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        return super.clone();
    }

    private void checkTarFileSetAttributesAllowed() {
        if (getProject() == null || (isReference() && (getRefid().getReferencedObject(getProject()) instanceof TarFileSet))) {
            checkAttributesAllowed();
        }
    }
}
