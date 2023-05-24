package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileNameMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/MappedResource.class */
public class MappedResource extends ResourceDecorator {
    private final FileNameMapper mapper;

    public MappedResource(Resource r, FileNameMapper m) {
        super(r);
        this.mapper = m;
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public String getName() {
        String name = getResource().getName();
        if (isReference()) {
            return name;
        }
        String[] mapped = this.mapper.mapFileName(name);
        if (mapped == null || mapped.length <= 0) {
            return null;
        }
        return mapped[0];
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.mapper != null) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public <T> T as(Class<T> clazz) {
        if (FileProvider.class.isAssignableFrom(clazz)) {
            return null;
        }
        return (T) getResource().as(clazz);
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public int hashCode() {
        String n = getName();
        return n == null ? super.hashCode() : n.hashCode();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        MappedResource m = (MappedResource) other;
        String myName = getName();
        String otherName = m.getName();
        if (myName != null ? myName.equals(otherName) : otherName == null) {
            if (getResource().equals(m.getResource())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        return getName();
    }
}
