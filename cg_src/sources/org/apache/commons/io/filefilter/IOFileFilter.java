package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/IOFileFilter.class */
public interface IOFileFilter extends FileFilter, FilenameFilter {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    @Override // java.io.FileFilter
    boolean accept(File file);

    boolean accept(File file, String str);
}
