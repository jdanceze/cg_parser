package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ArchiveResource.class */
public abstract class ArchiveResource extends Resource {
    private static final int NULL_ARCHIVE = Resource.getMagicNumber("null archive".getBytes());
    private Resource archive;
    private boolean haveEntry;
    private boolean modeSet;
    private int mode;

    protected abstract void fetchEntry();

    /* JADX INFO: Access modifiers changed from: protected */
    public ArchiveResource() {
        this.haveEntry = false;
        this.modeSet = false;
        this.mode = 0;
    }

    protected ArchiveResource(File a) {
        this(a, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArchiveResource(File a, boolean withEntry) {
        this.haveEntry = false;
        this.modeSet = false;
        this.mode = 0;
        setArchive(a);
        this.haveEntry = withEntry;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArchiveResource(Resource a, boolean withEntry) {
        this.haveEntry = false;
        this.modeSet = false;
        this.mode = 0;
        addConfigured(a);
        this.haveEntry = withEntry;
    }

    public void setArchive(File a) {
        checkAttributesAllowed();
        this.archive = new FileResource(a);
    }

    public void setMode(int mode) {
        checkAttributesAllowed();
        this.mode = mode;
        this.modeSet = true;
    }

    public void addConfigured(ResourceCollection a) {
        checkChildrenAllowed();
        if (this.archive != null) {
            throw new BuildException("you must not specify more than one archive");
        }
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported as archives");
        }
        this.archive = a.iterator().next();
    }

    public Resource getArchive() {
        return isReference() ? getRef().getArchive() : this.archive;
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getLastModified() {
        if (isReference()) {
            return getRef().getLastModified();
        }
        checkEntry();
        return super.getLastModified();
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getSize() {
        if (isReference()) {
            return getRef().getSize();
        }
        checkEntry();
        return super.getSize();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isDirectory() {
        if (isReference()) {
            return getRef().isDirectory();
        }
        checkEntry();
        return super.isDirectory();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        if (isReference()) {
            return getRef().isExists();
        }
        checkEntry();
        return super.isExists();
    }

    public int getMode() {
        if (isReference()) {
            return getRef().getMode();
        }
        checkEntry();
        return this.mode;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.archive != null || this.modeSet) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.tools.ant.types.Resource, java.lang.Comparable
    public int compareTo(Resource another) {
        if (equals(another)) {
            return 0;
        }
        return super.compareTo(another);
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (isReference()) {
            return getRef().equals(another);
        }
        if (another == null || !another.getClass().equals(getClass())) {
            return false;
        }
        ArchiveResource r = (ArchiveResource) another;
        return getArchive().equals(r.getArchive()) && getName().equals(r.getName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public int hashCode() {
        return super.hashCode() * (getArchive() == null ? NULL_ARCHIVE : getArchive().hashCode());
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        return isReference() ? getRef().toString() : getArchive().toString() + ':' + getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void checkEntry() throws BuildException {
        dieOnCircularReference();
        if (this.haveEntry) {
            return;
        }
        String name = getName();
        if (name == null) {
            throw new BuildException("entry name not set");
        }
        Resource r = getArchive();
        if (r == null) {
            throw new BuildException("archive attribute not set");
        }
        if (!r.isExists()) {
            throw new BuildException("%s does not exist.", r);
        }
        if (r.isDirectory()) {
            throw new BuildException("%s denotes a directory.", r);
        }
        fetchEntry();
        this.haveEntry = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        if (this.archive != null) {
            pushAndInvokeCircularReferenceCheck(this.archive, stk, p);
        }
        setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public ArchiveResource getRef() {
        return (ArchiveResource) getCheckedRef(ArchiveResource.class);
    }
}
