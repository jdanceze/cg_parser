package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/FileFileFilter.class */
public class FileFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 5345244090827540862L;
    public static final IOFileFilter FILE = new FileFileFilter();

    protected FileFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.isFile();
    }
}
