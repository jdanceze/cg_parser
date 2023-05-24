package org.apache.tools.ant.types.resources;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ResourceList.class */
public class ResourceList extends DataType implements ResourceCollection {
    private File baseDir;
    private final Vector<FilterChain> filterChains = new Vector<>();
    private final ArrayList<ResourceCollection> textDocuments = new ArrayList<>();
    private AppendableResourceCollection cachedResources = null;
    private String encoding = null;
    private boolean preserveDuplicates = false;

    public void add(ResourceCollection rc) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.textDocuments.add(rc);
        setChecked(false);
    }

    public final void addFilterChain(FilterChain filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.filterChains.add(filter);
        setChecked(false);
    }

    public final void setEncoding(String encoding) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.encoding = encoding;
    }

    public final void setBasedir(File baseDir) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.baseDir = baseDir;
    }

    public final void setPreserveDuplicates(boolean preserveDuplicates) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.preserveDuplicates = preserveDuplicates;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.encoding != null) {
            throw tooManyAttributes();
        }
        if (!this.filterChains.isEmpty() || !this.textDocuments.isEmpty()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    @Override // java.lang.Iterable
    public final synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        return cache().iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        return cache().size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        return cache().isFilesystemOnly();
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
        Iterator<ResourceCollection> it = this.textDocuments.iterator();
        while (it.hasNext()) {
            ResourceCollection resourceCollection = it.next();
            if (resourceCollection instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) resourceCollection, stk, p);
            }
        }
        Iterator<FilterChain> it2 = this.filterChains.iterator();
        while (it2.hasNext()) {
            FilterChain filterChain = it2.next();
            pushAndInvokeCircularReferenceCheck(filterChain, stk, p);
        }
        setChecked(true);
    }

    private ResourceList getRef() {
        return (ResourceList) getCheckedRef(ResourceList.class);
    }

    private AppendableResourceCollection newResourceCollection() {
        if (this.preserveDuplicates) {
            Resources resources = new Resources();
            resources.setCache(true);
            return resources;
        }
        Union union = new Union();
        union.setCache(true);
        return union;
    }

    private synchronized ResourceCollection cache() {
        if (this.cachedResources == null) {
            dieOnCircularReference();
            this.cachedResources = newResourceCollection();
            Stream map = this.textDocuments.stream().flatMap((v0) -> {
                return v0.stream();
            }).map(this::read);
            AppendableResourceCollection appendableResourceCollection = this.cachedResources;
            Objects.requireNonNull(appendableResourceCollection);
            map.forEach(this::add);
        }
        return this.cachedResources;
    }

    private ResourceCollection read(Resource r) {
        try {
            BufferedReader reader = new BufferedReader(open(r));
            AppendableResourceCollection streamResources = newResourceCollection();
            Stream<R> map = reader.lines().map(this::parse);
            Objects.requireNonNull(streamResources);
            map.forEach((v1) -> {
                r1.add(v1);
            });
            reader.close();
            return streamResources;
        } catch (IOException ioe) {
            throw new BuildException("Unable to read resource " + r.getName() + ": " + ioe, ioe, getLocation());
        }
    }

    private Reader open(Resource r) throws IOException {
        ChainReaderHelper crh = new ChainReaderHelper();
        crh.setPrimaryReader(new InputStreamReader(new BufferedInputStream(r.getInputStream()), this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding)));
        crh.setFilterChains(this.filterChains);
        crh.setProject(getProject());
        return crh.getAssembledReader();
    }

    private Resource parse(String line) {
        PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
        Object expanded = propertyHelper.parseProperties(line);
        if (expanded instanceof Resource) {
            return (Resource) expanded;
        }
        String expandedLine = expanded.toString();
        if (expandedLine.contains(":")) {
            try {
                return new URLResource(expandedLine);
            } catch (BuildException e) {
            }
        }
        if (this.baseDir != null) {
            FileResource fr = new FileResource(this.baseDir, expandedLine);
            fr.setProject(getProject());
            return fr;
        }
        return new FileResource(getProject(), expandedLine);
    }
}
