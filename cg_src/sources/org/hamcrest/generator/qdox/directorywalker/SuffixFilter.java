package org.hamcrest.generator.qdox.directorywalker;

import java.io.File;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/directorywalker/SuffixFilter.class */
public class SuffixFilter implements Filter {
    private String suffixFilter;

    public SuffixFilter(String suffixFilter) {
        this.suffixFilter = suffixFilter;
    }

    @Override // org.hamcrest.generator.qdox.directorywalker.Filter
    public boolean filter(File file) {
        return file.getName().endsWith(this.suffixFilter);
    }
}
