package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResourceIterator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FileList.class */
public class FileList extends DataType implements ResourceCollection {
    private List<String> filenames;
    private File dir;

    public FileList() {
        this.filenames = new ArrayList();
    }

    protected FileList(FileList filelist) {
        this.filenames = new ArrayList();
        this.dir = filelist.dir;
        this.filenames = filelist.filenames;
        setProject(filelist.getProject());
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.dir != null || !this.filenames.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public void setDir(File dir) throws BuildException {
        checkAttributesAllowed();
        this.dir = dir;
    }

    public File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        return this.dir;
    }

    public void setFiles(String filenames) {
        checkAttributesAllowed();
        if (filenames != null && !filenames.isEmpty()) {
            StringTokenizer tok = new StringTokenizer(filenames, ", \t\n\r\f", false);
            while (tok.hasMoreTokens()) {
                this.filenames.add(tok.nextToken());
            }
        }
    }

    public String[] getFiles(Project p) {
        if (isReference()) {
            return getRef(p).getFiles(p);
        }
        if (this.dir == null) {
            throw new BuildException("No directory specified for filelist.");
        }
        if (this.filenames.isEmpty()) {
            throw new BuildException("No files specified for filelist.");
        }
        return (String[]) this.filenames.toArray(new String[this.filenames.size()]);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FileList$FileName.class */
    public static class FileName {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public void addConfiguredFile(FileName name) {
        if (name.getName() == null) {
            throw new BuildException("No name specified in nested file element");
        }
        this.filenames.add(name.getName());
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        return new FileResourceIterator(getProject(), this.dir, (String[]) this.filenames.toArray(new String[this.filenames.size()]));
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return this.filenames.size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return true;
    }

    private FileList getRef() {
        return (FileList) getCheckedRef(FileList.class);
    }

    private FileList getRef(Project p) {
        return (FileList) getCheckedRef(FileList.class, getDataTypeName(), p);
    }
}
