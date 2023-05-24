package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ResourceDecorator.class */
public abstract class ResourceDecorator extends Resource {
    private Resource resource;

    /* JADX INFO: Access modifiers changed from: protected */
    public ResourceDecorator() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ResourceDecorator(ResourceCollection other) {
        addConfigured(other);
    }

    public final void addConfigured(ResourceCollection a) {
        checkChildrenAllowed();
        if (this.resource != null) {
            throw new BuildException("you must not specify more than one resource");
        }
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported");
        }
        setChecked(false);
        this.resource = a.iterator().next();
    }

    @Override // org.apache.tools.ant.types.Resource
    public String getName() {
        return getResource().getName();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        return getResource().isExists();
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getLastModified() {
        return getResource().getLastModified();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isDirectory() {
        return getResource().isDirectory();
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getSize() {
        return getResource().getSize();
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        return getResource().getInputStream();
    }

    @Override // org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        return getResource().getOutputStream();
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return as(FileProvider.class) != null;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.resource != null) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.Resource
    public <T> T as(Class<T> clazz) {
        return (T) getResource().as(clazz);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.tools.ant.types.Resource, java.lang.Comparable
    public int compareTo(Resource other) {
        if (other == this) {
            return 0;
        }
        if (other instanceof ResourceDecorator) {
            return getResource().compareTo(((ResourceDecorator) other).getResource());
        }
        return getResource().compareTo(other);
    }

    @Override // org.apache.tools.ant.types.Resource
    public int hashCode() {
        return (getClass().hashCode() << 4) | getResource().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Resource getResource() {
        if (isReference()) {
            return (Resource) getCheckedRef(Resource.class);
        }
        if (this.resource == null) {
            throw new BuildException("no resource specified");
        }
        dieOnCircularReference();
        return this.resource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public void dieOnCircularReference(Stack<Object> stack, Project project) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stack, project);
            return;
        }
        pushAndInvokeCircularReferenceCheck(this.resource, stack, project);
        setChecked(true);
    }

    @Override // org.apache.tools.ant.types.Resource
    public void setName(String name) throws BuildException {
        throw new BuildException("you can't change the name of a " + getDataTypeName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public void setExists(boolean exists) {
        throw new BuildException("you can't change the exists state of a " + getDataTypeName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public void setLastModified(long lastmodified) throws BuildException {
        throw new BuildException("you can't change the timestamp of a " + getDataTypeName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public void setDirectory(boolean directory) throws BuildException {
        throw new BuildException("you can't change the directory state of a " + getDataTypeName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public void setSize(long size) throws BuildException {
        throw new BuildException("you can't change the size of a " + getDataTypeName());
    }
}
