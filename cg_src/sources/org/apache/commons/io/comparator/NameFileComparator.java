package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOCase;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/comparator/NameFileComparator.class */
public class NameFileComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = 8397947749814525798L;
    public static final Comparator<File> NAME_COMPARATOR = new NameFileComparator();
    public static final Comparator<File> NAME_REVERSE = new ReverseComparator(NAME_COMPARATOR);
    public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(IOCase.INSENSITIVE);
    public static final Comparator<File> NAME_INSENSITIVE_REVERSE = new ReverseComparator(NAME_INSENSITIVE_COMPARATOR);
    public static final Comparator<File> NAME_SYSTEM_COMPARATOR = new NameFileComparator(IOCase.SYSTEM);
    public static final Comparator<File> NAME_SYSTEM_REVERSE = new ReverseComparator(NAME_SYSTEM_COMPARATOR);
    private final IOCase caseSensitivity;

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List list) {
        return super.sort(list);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] fileArr) {
        return super.sort(fileArr);
    }

    public NameFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public NameFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return this.caseSensitivity.checkCompareTo(file1.getName(), file2.getName());
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
