package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.file.Counters;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/file/AccumulatorPathVisitor.class */
public class AccumulatorPathVisitor extends CountingPathVisitor {
    private final List<Path> dirList;
    private final List<Path> fileList;

    public static AccumulatorPathVisitor withBigIntegerCounters() {
        return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters());
    }

    public static AccumulatorPathVisitor withLongCounters() {
        return new AccumulatorPathVisitor(Counters.longPathCounters());
    }

    public AccumulatorPathVisitor(Counters.PathCounters pathCounter) {
        super(pathCounter);
        this.dirList = new ArrayList();
        this.fileList = new ArrayList();
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof AccumulatorPathVisitor)) {
            return false;
        }
        AccumulatorPathVisitor other = (AccumulatorPathVisitor) obj;
        return Objects.equals(this.dirList, other.dirList) && Objects.equals(this.fileList, other.fileList);
    }

    public List<Path> getDirList() {
        return this.dirList;
    }

    public List<Path> getFileList() {
        return this.fileList;
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Objects.hash(this.dirList, this.fileList);
    }

    public List<Path> relativizeDirectories(Path parent, boolean sort, Comparator<? super Path> comparator) {
        return PathUtils.relativize(getDirList(), parent, sort, comparator);
    }

    public List<Path> relativizeFiles(Path parent, boolean sort, Comparator<? super Path> comparator) {
        return PathUtils.relativize(getFileList(), parent, sort, comparator);
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        (Files.isDirectory(file, new LinkOption[0]) ? this.dirList : this.fileList).add(file.normalize());
        return super.visitFile(file, attributes);
    }
}
