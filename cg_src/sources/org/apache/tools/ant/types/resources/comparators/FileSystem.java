package org.apache.tools.ant.types.resources.comparators;

import java.io.File;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/FileSystem.class */
public class FileSystem extends ResourceComparator {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected int resourceCompare(Resource foo, Resource bar) {
        return compare(file(foo), file(bar));
    }

    private File file(Resource r) {
        return ((FileProvider) r.asOptional(FileProvider.class).orElseThrow(() -> {
            return new ClassCastException(r.getClass() + " doesn't provide files");
        })).getFile();
    }

    private int compare(File f1, File f2) {
        if (Objects.equals(f1, f2)) {
            return 0;
        }
        if (FILE_UTILS.isLeadingPath(f1, f2)) {
            return -1;
        }
        if (FILE_UTILS.isLeadingPath(f2, f1)) {
            return 1;
        }
        Function function = (v0) -> {
            return v0.getAbsolutePath();
        };
        FileUtils fileUtils = FILE_UTILS;
        Objects.requireNonNull(fileUtils);
        return Comparator.comparing(function.andThen(this::normalize)).compare(f1, f2);
    }
}
