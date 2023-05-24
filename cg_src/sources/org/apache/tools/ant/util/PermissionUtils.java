package org.apache.tools.ant.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.ArchiveResource;
import org.apache.tools.ant.types.resources.FileProvider;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/PermissionUtils.class */
public class PermissionUtils {
    private PermissionUtils() {
    }

    public static int modeFromPermissions(Set<PosixFilePermission> permissions, FileType type) {
        int mode;
        switch (type) {
            case SYMLINK:
                mode = 10;
                break;
            case REGULAR_FILE:
                mode = 8;
                break;
            case DIR:
                mode = 4;
                break;
            default:
                mode = 0;
                break;
        }
        return (int) ((((int) ((((int) (((mode << 3) << 3) | modeFromPermissions(permissions, "OWNER"))) << 3) | modeFromPermissions(permissions, "GROUP"))) << 3) | modeFromPermissions(permissions, "OTHERS"));
    }

    public static Set<PosixFilePermission> permissionsFromMode(int mode) {
        Set<PosixFilePermission> permissions = EnumSet.noneOf(PosixFilePermission.class);
        addPermissions(permissions, "OTHERS", mode);
        addPermissions(permissions, "GROUP", mode >> 3);
        addPermissions(permissions, "OWNER", mode >> 6);
        return permissions;
    }

    public static void setPermissions(Resource r, Set<PosixFilePermission> permissions, Consumer<Path> posixNotSupportedCallback) throws IOException {
        FileProvider f = (FileProvider) r.as(FileProvider.class);
        if (f == null) {
            if (r instanceof ArchiveResource) {
                ((ArchiveResource) r).setMode(modeFromPermissions(permissions, FileType.of(r)));
                return;
            }
            return;
        }
        Path p = f.getFile().toPath();
        PosixFileAttributeView view = (PosixFileAttributeView) Files.getFileAttributeView(p, PosixFileAttributeView.class, new LinkOption[0]);
        if (view != null) {
            view.setPermissions(permissions);
        } else if (posixNotSupportedCallback != null) {
            posixNotSupportedCallback.accept(p);
        }
    }

    public static Set<PosixFilePermission> getPermissions(Resource r, Function<Path, Set<PosixFilePermission>> posixNotSupportedFallback) throws IOException {
        FileProvider f = (FileProvider) r.as(FileProvider.class);
        if (f != null) {
            Path p = f.getFile().toPath();
            PosixFileAttributeView view = (PosixFileAttributeView) Files.getFileAttributeView(p, PosixFileAttributeView.class, new LinkOption[0]);
            if (view != null) {
                return view.readAttributes().permissions();
            }
            if (posixNotSupportedFallback != null) {
                return posixNotSupportedFallback.apply(p);
            }
        } else if (r instanceof ArchiveResource) {
            return permissionsFromMode(((ArchiveResource) r).getMode());
        }
        return EnumSet.noneOf(PosixFilePermission.class);
    }

    private static long modeFromPermissions(Set<PosixFilePermission> permissions, String prefix) {
        long mode = 0;
        if (permissions.contains(PosixFilePermission.valueOf(prefix + "_READ"))) {
            mode = 0 | 4;
        }
        if (permissions.contains(PosixFilePermission.valueOf(prefix + "_WRITE"))) {
            mode |= 2;
        }
        if (permissions.contains(PosixFilePermission.valueOf(prefix + "_EXECUTE"))) {
            mode |= 1;
        }
        return mode;
    }

    private static void addPermissions(Set<PosixFilePermission> permissions, String prefix, long mode) {
        if ((mode & 1) == 1) {
            permissions.add(PosixFilePermission.valueOf(prefix + "_EXECUTE"));
        }
        if ((mode & 2) == 2) {
            permissions.add(PosixFilePermission.valueOf(prefix + "_WRITE"));
        }
        if ((mode & 4) == 4) {
            permissions.add(PosixFilePermission.valueOf(prefix + "_READ"));
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/PermissionUtils$FileType.class */
    public enum FileType {
        REGULAR_FILE,
        DIR,
        SYMLINK,
        OTHER;

        public static FileType of(Path p) throws IOException {
            BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class, new LinkOption[0]);
            if (attrs.isRegularFile()) {
                return REGULAR_FILE;
            }
            if (attrs.isDirectory()) {
                return DIR;
            }
            if (attrs.isSymbolicLink()) {
                return SYMLINK;
            }
            return OTHER;
        }

        public static FileType of(Resource r) {
            if (r.isDirectory()) {
                return DIR;
            }
            return REGULAR_FILE;
        }
    }
}
