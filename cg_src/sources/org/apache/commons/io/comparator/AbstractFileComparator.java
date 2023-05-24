package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/comparator/AbstractFileComparator.class */
abstract class AbstractFileComparator implements Comparator<File> {
    public File[] sort(File... files) {
        if (files != null) {
            Arrays.sort(files, this);
        }
        return files;
    }

    public List<File> sort(List<File> files) {
        if (files != null) {
            Collections.sort(files, this);
        }
        return files;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
