package org.apache.commons.io.filefilter;

import java.io.File;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/AbstractFileFilter.class */
public abstract class AbstractFileFilter implements IOFileFilter {
    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return accept(file.getParentFile(), file.getName());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
