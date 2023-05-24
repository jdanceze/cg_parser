package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import org.apache.commons.io.file.Counters;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/file/CopyDirectoryVisitor.class */
public class CopyDirectoryVisitor extends CountingPathVisitor {
    private static final CopyOption[] EMPTY_COPY_OPTIONS = new CopyOption[0];
    private final CopyOption[] copyOptions;
    private final Path sourceDirectory;
    private final Path targetDirectory;

    public CopyDirectoryVisitor(Counters.PathCounters pathCounter, Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) {
        super(pathCounter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = copyOptions == null ? EMPTY_COPY_OPTIONS : (CopyOption[]) copyOptions.clone();
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
        Path newTargetDir = this.targetDirectory.resolve(this.sourceDirectory.relativize(directory));
        if (Files.notExists(newTargetDir, new LinkOption[0])) {
            Files.createDirectory(newTargetDir, new FileAttribute[0]);
        }
        return super.preVisitDirectory((Object) directory, attributes);
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attributes) throws IOException {
        Path targetFile = this.targetDirectory.resolve(this.sourceDirectory.relativize(sourceFile));
        Files.copy(sourceFile, targetFile, this.copyOptions);
        return super.visitFile(targetFile, attributes);
    }
}
