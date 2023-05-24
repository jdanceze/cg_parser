package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/CanReadFileFilter.class */
public class CanReadFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 3179904805251622989L;
    public static final IOFileFilter CAN_READ = new CanReadFileFilter();
    public static final IOFileFilter CANNOT_READ = new NotFileFilter(CAN_READ);
    public static final IOFileFilter READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);

    protected CanReadFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead();
    }
}
