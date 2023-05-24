package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/CanExecuteFileFilter.class */
public class CanExecuteFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 3179904805251622989L;
    public static final IOFileFilter CAN_EXECUTE = new CanExecuteFileFilter();
    public static final IOFileFilter CANNOT_EXECUTE = new NotFileFilter(CAN_EXECUTE);

    protected CanExecuteFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.canExecute();
    }
}
