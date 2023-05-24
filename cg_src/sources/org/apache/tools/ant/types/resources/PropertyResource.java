package org.apache.tools.ant.types.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.PropertyOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/PropertyResource.class */
public class PropertyResource extends Resource {
    private static final int PROPERTY_MAGIC = Resource.getMagicNumber("PropertyResource".getBytes());
    private static final InputStream UNSET = new InputStream() { // from class: org.apache.tools.ant.types.resources.PropertyResource.1
        @Override // java.io.InputStream
        public int read() {
            return -1;
        }
    };

    public PropertyResource() {
    }

    public PropertyResource(Project p, String n) {
        super(n);
        setProject(p);
    }

    public String getValue() {
        if (isReference()) {
            return getRef().getValue();
        }
        Project p = getProject();
        if (p == null) {
            return null;
        }
        return p.getProperty(getName());
    }

    public Object getObjectValue() {
        if (isReference()) {
            return getRef().getObjectValue();
        }
        Project p = getProject();
        if (p == null) {
            return null;
        }
        return PropertyHelper.getProperty(p, getName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().isExists();
        }
        return getObjectValue() != null;
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getSize() {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().getSize();
        }
        Object o = getObjectValue();
        if (o == null) {
            return 0L;
        }
        return String.valueOf(o).length();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean equals(Object o) {
        return super.equals(o) || (isReferenceOrProxy() && getReferencedOrProxied().equals(o));
    }

    @Override // org.apache.tools.ant.types.Resource
    public int hashCode() {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().hashCode();
        }
        return super.hashCode() * PROPERTY_MAGIC;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().toString();
        }
        return getValue();
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().getInputStream();
        }
        Object o = getObjectValue();
        return o == null ? UNSET : new ByteArrayInputStream(String.valueOf(o).getBytes());
    }

    @Override // org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        if (isReferenceOrProxy()) {
            return getReferencedOrProxied().getOutputStream();
        }
        if (isExists()) {
            throw new ImmutableResourceException();
        }
        return new PropertyOutputStream(getProject(), getName());
    }

    protected boolean isReferenceOrProxy() {
        return isReference() || (getObjectValue() instanceof Resource);
    }

    protected Resource getReferencedOrProxied() {
        if (isReference()) {
            return super.getRef();
        }
        Object o = getObjectValue();
        if (o instanceof Resource) {
            return (Resource) o;
        }
        throw new IllegalStateException("This PropertyResource does not reference or proxy another Resource");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public PropertyResource getRef() {
        return (PropertyResource) getCheckedRef(PropertyResource.class);
    }
}
