package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.MergingMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/MappedResourceCollection.class */
public class MappedResourceCollection extends DataType implements ResourceCollection, Cloneable {
    private ResourceCollection nested = null;
    private Mapper mapper = null;
    private boolean enableMultipleMappings = false;
    private boolean cache = false;
    private Collection<Resource> cachedColl = null;

    public synchronized void add(ResourceCollection c) throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.nested != null) {
            throw new BuildException("Only one resource collection can be nested into mappedresources", getLocation());
        }
        setChecked(false);
        this.cachedColl = null;
        this.nested = c;
    }

    public Mapper createMapper() throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.mapper != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS, getLocation());
        }
        setChecked(false);
        this.mapper = new Mapper(getProject());
        this.cachedColl = null;
        return this.mapper;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    public void setEnableMultipleMappings(boolean enableMultipleMappings) {
        this.enableMultipleMappings = enableMultipleMappings;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        checkInitialized();
        return false;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        checkInitialized();
        return cacheCollection().size();
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        checkInitialized();
        return cacheCollection().iterator();
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.nested != null || this.mapper != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        try {
            MappedResourceCollection c = (MappedResourceCollection) super.clone();
            c.nested = this.nested;
            c.mapper = this.mapper;
            c.cachedColl = null;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        checkInitialized();
        if (this.mapper != null) {
            pushAndInvokeCircularReferenceCheck(this.mapper, stk, p);
        }
        if (this.nested instanceof DataType) {
            pushAndInvokeCircularReferenceCheck((DataType) this.nested, stk, p);
        }
        setChecked(true);
    }

    private void checkInitialized() {
        if (this.nested == null) {
            throw new BuildException("A nested resource collection element is required", getLocation());
        }
        dieOnCircularReference();
    }

    private synchronized Collection<Resource> cacheCollection() {
        if (this.cachedColl == null || !this.cache) {
            this.cachedColl = getCollection();
        }
        return this.cachedColl;
    }

    private Collection<Resource> getCollection() {
        Stream map;
        FileNameMapper m = this.mapper == null ? new IdentityMapper() : this.mapper.getImplementation();
        if (this.enableMultipleMappings) {
            map = this.nested.stream().flatMap(r -> {
                return Stream.of((Object[]) m.mapFileName(r.getName())).filter((v0) -> {
                    return Objects.nonNull(v0);
                }).map(MergingMapper::new).map(mm -> {
                    return new MappedResource(r, mm);
                });
            });
        } else {
            map = this.nested.stream().map(r2 -> {
                return new MappedResource(r2, m);
            });
        }
        return (Collection) map.collect(Collectors.toList());
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        return isEmpty() ? "" : (String) stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(File.pathSeparator));
    }

    private MappedResourceCollection getRef() {
        return (MappedResourceCollection) getCheckedRef(MappedResourceCollection.class);
    }
}
