package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
@Deprecated
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/filefilter/WildcardFilter.class */
public class WildcardFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -5037645902506953517L;
    private final String[] wildcards;

    public WildcardFilter(String wildcard) {
        if (wildcard == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        }
        this.wildcards = new String[]{wildcard};
    }

    public WildcardFilter(String... wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard array must not be null");
        }
        this.wildcards = new String[wildcards.length];
        System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
    }

    public WildcardFilter(List<String> wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard list must not be null");
        }
        this.wildcards = (String[]) wildcards.toArray(EMPTY_STRING_ARRAY);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        String[] strArr;
        if (dir != null && new File(dir, name).isDirectory()) {
            return false;
        }
        for (String wildcard : this.wildcards) {
            if (FilenameUtils.wildcardMatch(name, wildcard)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        String[] strArr;
        if (file.isDirectory()) {
            return false;
        }
        for (String wildcard : this.wildcards) {
            if (FilenameUtils.wildcardMatch(file.getName(), wildcard)) {
                return true;
            }
        }
        return false;
    }
}
