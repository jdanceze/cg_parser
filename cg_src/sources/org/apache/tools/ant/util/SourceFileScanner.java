package org.apache.tools.ant.util;

import java.io.File;
import java.util.stream.Stream;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/SourceFileScanner.class */
public class SourceFileScanner implements ResourceFactory {
    protected Task task;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File destDir;

    public SourceFileScanner(Task task) {
        this.task = task;
    }

    public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
        return restrict(files, srcDir, destDir, mapper, FILE_UTILS.getFileTimestampGranularity());
    }

    public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
        this.destDir = destDir;
        Resource[] sourceResources = (Resource[]) Stream.of((Object[]) files).map(f -> {
            return new FileResource(srcDir, srcDir) { // from class: org.apache.tools.ant.util.SourceFileScanner.1
                @Override // org.apache.tools.ant.types.resources.FileResource, org.apache.tools.ant.types.Resource
                public String getName() {
                    return srcDir;
                }
            };
        }).toArray(x$0 -> {
            return new Resource[x$0];
        });
        return (String[]) Stream.of((Object[]) ResourceUtils.selectOutOfDateSources(this.task, sourceResources, mapper, this, granularity)).map((v0) -> {
            return v0.getName();
        }).toArray(x$02 -> {
            return new String[x$02];
        });
    }

    public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
        return restrictAsFiles(files, srcDir, destDir, mapper, FILE_UTILS.getFileTimestampGranularity());
    }

    public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
        return (File[]) Stream.of((Object[]) restrict(files, srcDir, destDir, mapper, granularity)).map(name -> {
            return new File(srcDir, name);
        }).toArray(x$0 -> {
            return new File[x$0];
        });
    }

    @Override // org.apache.tools.ant.types.ResourceFactory
    public Resource getResource(String name) {
        return new FileResource(this.destDir, name);
    }
}
