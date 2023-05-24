package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/FileResourceIterator.class */
public class FileResourceIterator implements Iterator<Resource> {
    private Project project;
    private File basedir;
    private String[] files;
    private int pos;

    @Deprecated
    public FileResourceIterator() {
        this.pos = 0;
    }

    public FileResourceIterator(Project project) {
        this.pos = 0;
        this.project = project;
    }

    @Deprecated
    public FileResourceIterator(File basedir) {
        this((Project) null, basedir);
    }

    public FileResourceIterator(Project project, File basedir) {
        this(project);
        this.basedir = basedir;
    }

    @Deprecated
    public FileResourceIterator(File basedir, String[] filenames) {
        this(null, basedir, filenames);
    }

    public FileResourceIterator(Project project, File basedir, String[] filenames) {
        this(project, basedir);
        addFiles(filenames);
    }

    public void addFiles(String[] s) {
        int start = this.files == null ? 0 : this.files.length;
        String[] newfiles = new String[start + s.length];
        if (start > 0) {
            System.arraycopy(this.files, 0, newfiles, 0, start);
        }
        this.files = newfiles;
        System.arraycopy(s, 0, this.files, start, s.length);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.pos < this.files.length;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Resource next() {
        return nextResource();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public FileResource nextResource() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        File file = this.basedir;
        String[] strArr = this.files;
        int i = this.pos;
        this.pos = i + 1;
        FileResource result = new FileResource(file, strArr[i]);
        result.setProject(this.project);
        return result;
    }
}
