package org.apache.commons.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.file.Counters;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/file/PathUtils.class */
public final class PathUtils {
    public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY = new FileVisitOption[0];
    public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];

    /* JADX INFO: Access modifiers changed from: private */
    public static AccumulatorPathVisitor accumulate(Path directory, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
        return (AccumulatorPathVisitor) visitFileTree(AccumulatorPathVisitor.withLongCounters(), directory, toFileVisitOptionSet(fileVisitOptions), maxDepth);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/file/PathUtils$RelativeSortedPaths.class */
    public static class RelativeSortedPaths {
        final boolean equals;
        final List<Path> relativeDirList1;
        final List<Path> relativeDirList2;
        final List<Path> relativeFileList1;
        final List<Path> relativeFileList2;

        private RelativeSortedPaths(Path dir1, Path dir2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
            List<Path> tmpRelativeDirList1 = null;
            List<Path> tmpRelativeDirList2 = null;
            List<Path> tmpRelativeFileList1 = null;
            List<Path> tmpRelativeFileList2 = null;
            if (dir1 == null && dir2 == null) {
                this.equals = true;
            } else {
                if ((dir1 == null) ^ (dir2 == null)) {
                    this.equals = false;
                } else {
                    boolean parentDirExists1 = Files.exists(dir1, linkOptions);
                    boolean parentDirExists2 = Files.exists(dir2, linkOptions);
                    if (parentDirExists1 && parentDirExists2) {
                        AccumulatorPathVisitor visitor1 = PathUtils.accumulate(dir1, maxDepth, linkOptions, fileVisitOptions);
                        AccumulatorPathVisitor visitor2 = PathUtils.accumulate(dir2, maxDepth, linkOptions, fileVisitOptions);
                        if (visitor1.getDirList().size() != visitor2.getDirList().size() || visitor1.getFileList().size() != visitor2.getFileList().size()) {
                            this.equals = false;
                        } else {
                            tmpRelativeDirList1 = visitor1.relativizeDirectories(dir1, true, null);
                            tmpRelativeDirList2 = visitor2.relativizeDirectories(dir2, true, null);
                            if (!tmpRelativeDirList1.equals(tmpRelativeDirList2)) {
                                this.equals = false;
                            } else {
                                tmpRelativeFileList1 = visitor1.relativizeFiles(dir1, true, null);
                                tmpRelativeFileList2 = visitor2.relativizeFiles(dir2, true, null);
                                this.equals = tmpRelativeFileList1.equals(tmpRelativeFileList2);
                            }
                        }
                    } else {
                        this.equals = (parentDirExists1 || parentDirExists2) ? false : true;
                    }
                }
            }
            this.relativeDirList1 = tmpRelativeDirList1;
            this.relativeDirList2 = tmpRelativeDirList2;
            this.relativeFileList1 = tmpRelativeFileList1;
            this.relativeFileList2 = tmpRelativeFileList2;
        }
    }

    public static Counters.PathCounters cleanDirectory(Path directory) throws IOException {
        return ((CountingPathVisitor) visitFileTree(CleaningPathVisitor.withLongCounters(), directory)).getPathCounters();
    }

    public static Counters.PathCounters copyDirectory(Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        return ((CopyDirectoryVisitor) visitFileTree(new CopyDirectoryVisitor(Counters.longPathCounters(), sourceDirectory, targetDirectory, copyOptions), sourceDirectory)).getPathCounters();
    }

    public static Path copyFile(URL sourceFile, Path targetFile, CopyOption... copyOptions) throws IOException {
        InputStream inputStream = sourceFile.openStream();
        Throwable th = null;
        try {
            Files.copy(inputStream, targetFile, copyOptions);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
            return targetFile;
        } finally {
        }
    }

    public static Path copyFileToDirectory(Path sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        return Files.copy(sourceFile, targetDirectory.resolve(sourceFile.getFileName()), copyOptions);
    }

    public static Path copyFileToDirectory(URL sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        InputStream inputStream = sourceFile.openStream();
        Throwable th = null;
        try {
            Files.copy(inputStream, targetDirectory.resolve(sourceFile.getFile()), copyOptions);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
            return targetDirectory;
        } finally {
        }
    }

    public static Counters.PathCounters countDirectory(Path directory) throws IOException {
        return ((CountingPathVisitor) visitFileTree(new CountingPathVisitor(Counters.longPathCounters()), directory)).getPathCounters();
    }

    public static Counters.PathCounters delete(Path path) throws IOException {
        return Files.isDirectory(path, new LinkOption[0]) ? deleteDirectory(path) : deleteFile(path);
    }

    public static Counters.PathCounters deleteDirectory(Path directory) throws IOException {
        return ((DeletingPathVisitor) visitFileTree(DeletingPathVisitor.withLongCounters(), directory)).getPathCounters();
    }

    public static Counters.PathCounters deleteFile(Path file) throws IOException {
        if (Files.isDirectory(file, new LinkOption[0])) {
            throw new NotDirectoryException(file.toString());
        }
        Counters.PathCounters pathCounts = Counters.longPathCounters();
        long size = Files.exists(file, new LinkOption[0]) ? Files.size(file) : 0L;
        if (Files.deleteIfExists(file)) {
            pathCounts.getFileCounter().increment();
            pathCounts.getByteCounter().add(size);
        }
        return pathCounts;
    }

    public static boolean directoryAndFileContentEquals(Path path1, Path path2) throws IOException {
        return directoryAndFileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryAndFileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions, FileVisitOption[] fileVisitOption) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if ((path1 == null) ^ (path2 == null)) {
            return false;
        }
        if (!Files.exists(path1, new LinkOption[0]) && !Files.exists(path2, new LinkOption[0])) {
            return true;
        }
        RelativeSortedPaths relativeSortedPaths = new RelativeSortedPaths(path1, path2, Integer.MAX_VALUE, linkOptions, fileVisitOption);
        if (!relativeSortedPaths.equals) {
            return false;
        }
        List<Path> fileList1 = relativeSortedPaths.relativeFileList1;
        List<Path> fileList2 = relativeSortedPaths.relativeFileList2;
        for (Path path : fileList1) {
            int binarySearch = Collections.binarySearch(fileList2, path);
            if (binarySearch > -1) {
                if (!fileContentEquals(path1.resolve(path), path2.resolve(path), linkOptions, openOptions)) {
                    return false;
                }
            } else {
                throw new IllegalStateException(String.format("Unexpected mismatch.", new Object[0]));
            }
        }
        return true;
    }

    public static boolean directoryContentEquals(Path path1, Path path2) throws IOException {
        return directoryContentEquals(path1, path2, Integer.MAX_VALUE, EMPTY_LINK_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryContentEquals(Path path1, Path path2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
        return new RelativeSortedPaths(path1, path2, maxDepth, linkOptions, fileVisitOptions).equals;
    }

    public static boolean fileContentEquals(Path path1, Path path2) throws IOException {
        return fileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY);
    }

    public static boolean fileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if ((path1 == null) ^ (path2 == null)) {
            return false;
        }
        Path nPath1 = path1.normalize();
        Path nPath2 = path2.normalize();
        boolean path1Exists = Files.exists(nPath1, linkOptions);
        if (path1Exists != Files.exists(nPath2, linkOptions)) {
            return false;
        }
        if (!path1Exists) {
            return true;
        }
        if (Files.isDirectory(nPath1, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath1);
        }
        if (Files.isDirectory(nPath2, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath2);
        }
        if (Files.size(nPath1) != Files.size(nPath2)) {
            return false;
        }
        if (path1.equals(path2)) {
            return true;
        }
        InputStream inputStream1 = Files.newInputStream(nPath1, openOptions);
        Throwable th = null;
        try {
            InputStream inputStream2 = Files.newInputStream(nPath2, openOptions);
            try {
                boolean contentEquals = IOUtils.contentEquals(inputStream1, inputStream2);
                if (inputStream2 != null) {
                    if (0 != 0) {
                        inputStream2.close();
                    } else {
                        inputStream2.close();
                    }
                }
                return contentEquals;
            } finally {
            }
        } finally {
            if (inputStream1 != null) {
                if (0 != 0) {
                    try {
                        inputStream1.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream1.close();
                }
            }
        }
    }

    public static boolean isEmpty(Path path) throws IOException {
        return Files.isDirectory(path, new LinkOption[0]) ? isEmptyDirectory(path) : isEmptyFile(path);
    }

    public static boolean isEmptyDirectory(Path directory) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);
        Throwable th = null;
        try {
            if (directoryStream.iterator().hasNext()) {
                if (directoryStream != null) {
                    if (0 != 0) {
                        try {
                            directoryStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        directoryStream.close();
                    }
                }
                return false;
            } else if (directoryStream != null) {
                if (0 != 0) {
                    try {
                        directoryStream.close();
                        return true;
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                        return true;
                    }
                }
                directoryStream.close();
                return true;
            } else {
                return true;
            }
        } catch (Throwable th4) {
            try {
                throw th4;
            } catch (Throwable th5) {
                if (directoryStream != null) {
                    if (th4 != null) {
                        try {
                            directoryStream.close();
                        } catch (Throwable th6) {
                            th4.addSuppressed(th6);
                        }
                    } else {
                        directoryStream.close();
                    }
                }
                throw th5;
            }
        }
    }

    public static boolean isEmptyFile(Path file) throws IOException {
        return Files.size(file) <= 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Path> relativize(Collection<Path> collection, Path parent, boolean sort, Comparator<? super Path> comparator) {
        Stream map = collection.stream().map(e -> {
            return parent.relativize(e);
        });
        if (sort) {
            map = comparator == null ? map.sorted() : map.sorted(comparator);
        }
        return (List) map.collect(Collectors.toList());
    }

    static Set<FileVisitOption> toFileVisitOptionSet(FileVisitOption... fileVisitOptions) {
        return fileVisitOptions == null ? EnumSet.noneOf(FileVisitOption.class) : (Set) Arrays.stream(fileVisitOptions).collect(Collectors.toSet());
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path directory) throws IOException {
        Files.walkFileTree(directory, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path start, Set<FileVisitOption> options, int maxDepth) throws IOException {
        Files.walkFileTree(start, options, maxDepth, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, String first, String... more) throws IOException {
        return (T) visitFileTree(visitor, Paths.get(first, more));
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, URI uri) throws IOException {
        return (T) visitFileTree(visitor, Paths.get(uri));
    }

    private PathUtils() {
    }
}
