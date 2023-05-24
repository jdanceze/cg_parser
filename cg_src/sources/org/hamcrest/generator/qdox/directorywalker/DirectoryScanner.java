package org.hamcrest.generator.qdox.directorywalker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/directorywalker/DirectoryScanner.class */
public class DirectoryScanner {
    private File file;
    private Collection filters = new HashSet();

    public DirectoryScanner(File file) {
        this.file = file;
    }

    public File[] scan() {
        List results = new ArrayList();
        walk(new FileVisitor(this, results) { // from class: org.hamcrest.generator.qdox.directorywalker.DirectoryScanner.1
            private final List val$results;
            private final DirectoryScanner this$0;

            {
                this.this$0 = this;
                this.val$results = results;
            }

            @Override // org.hamcrest.generator.qdox.directorywalker.FileVisitor
            public void visitFile(File file) {
                this.val$results.add(file);
            }
        }, this.file);
        File[] resultsArray = new File[results.size()];
        results.toArray(resultsArray);
        return resultsArray;
    }

    private void walk(FileVisitor visitor, File current) {
        if (current.isDirectory()) {
            File[] currentFiles = current.listFiles();
            for (File file : currentFiles) {
                walk(visitor, file);
            }
            return;
        }
        for (Filter filter : this.filters) {
            if (!filter.filter(current)) {
                return;
            }
        }
        visitor.visitFile(current);
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    public void scan(FileVisitor fileVisitor) {
        walk(fileVisitor, this.file);
    }
}
