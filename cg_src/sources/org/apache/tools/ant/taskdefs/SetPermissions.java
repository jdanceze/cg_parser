package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.util.PermissionUtils;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SetPermissions.class */
public class SetPermissions extends Task {
    private final Set<PosixFilePermission> permissions = EnumSet.noneOf(PosixFilePermission.class);
    private Resources resources = null;
    private boolean failonerror = true;
    private NonPosixMode nonPosixMode = NonPosixMode.fail;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SetPermissions$NonPosixMode.class */
    public enum NonPosixMode {
        fail,
        pass,
        tryDosOrFail,
        tryDosOrPass
    }

    public void setPermissions(String perms) {
        if (perms != null) {
            Stream map = Arrays.stream(perms.split(",")).map((v0) -> {
                return v0.trim();
            }).filter(s -> {
                return !s.isEmpty();
            }).map(s2 -> {
                return (PosixFilePermission) Enum.valueOf(PosixFilePermission.class, s2);
            });
            Set<PosixFilePermission> set = this.permissions;
            Objects.requireNonNull(set);
            map.forEach((v1) -> {
                r1.add(v1);
            });
        }
    }

    public void setMode(String octalString) {
        int mode = Integer.parseInt(octalString, 8);
        this.permissions.addAll(PermissionUtils.permissionsFromMode(mode));
    }

    public void setFailOnError(boolean failonerror) {
        this.failonerror = failonerror;
    }

    public void setNonPosixMode(NonPosixMode m) {
        this.nonPosixMode = m;
    }

    public void add(ResourceCollection rc) {
        if (this.resources == null) {
            this.resources = new Resources();
        }
        this.resources.add(rc);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.resources == null) {
            throw new BuildException("At least one resource-collection is required");
        }
        Resource currentResource = null;
        try {
            Iterator<Resource> it = this.resources.iterator();
            while (it.hasNext()) {
                Resource r = it.next();
                currentResource = r;
                try {
                    PermissionUtils.setPermissions(r, this.permissions, this::posixPermissionsNotSupported);
                } catch (IOException ioe) {
                    maybeThrowException(ioe, "Failed to set permissions on '%s' due to %s", r, ioe.getMessage());
                }
            }
        } catch (ClassCastException e) {
            maybeThrowException(null, "some specified permissions are not of type PosixFilePermission: %s", StringUtils.join(this.permissions, ", "));
        } catch (SecurityException e2) {
            maybeThrowException(null, "the SecurityManager denies role accessUserInformation or write access for SecurityManager.checkWrite for resource '%s'", currentResource);
        } catch (BuildException be) {
            maybeThrowException(be, be.getMessage(), new Object[0]);
        }
    }

    private void maybeThrowException(Exception exc, String msgFormat, Object... msgArgs) {
        String msg = String.format(msgFormat, msgArgs);
        if (this.failonerror) {
            if (exc instanceof BuildException) {
                throw ((BuildException) exc);
            }
            throw new BuildException(msg, exc);
        }
        log("Warning: " + msg, 0);
    }

    private void posixPermissionsNotSupported(Path p) {
        String msg = String.format("the associated path '%s' does not support the PosixFileAttributeView", p);
        switch (this.nonPosixMode) {
            case fail:
                throw new BuildException(msg);
            case pass:
                log("Warning: " + msg, 0);
                return;
            case tryDosOrFail:
                tryDos(p, true);
                return;
            case tryDosOrPass:
                tryDos(p, false);
                return;
            default:
                return;
        }
    }

    private void tryDos(Path p, boolean failIfDosIsNotSupported) {
        log("Falling back to DosFileAttributeView");
        boolean readOnly = !isWritable();
        DosFileAttributeView view = (DosFileAttributeView) Files.getFileAttributeView(p, DosFileAttributeView.class, new LinkOption[0]);
        if (view != null) {
            try {
                view.setReadOnly(readOnly);
                return;
            } catch (IOException ioe) {
                maybeThrowException(ioe, "Failed to set permissions on '%s' due to %s", p, ioe.getMessage());
                return;
            } catch (SecurityException e) {
                maybeThrowException(null, "the SecurityManager denies role accessUserInformation or write access for SecurityManager.checkWrite for resource '%s'", p);
                return;
            }
        }
        String msg = String.format("the associated path '%s' does not support the DosFileAttributeView", p);
        if (failIfDosIsNotSupported) {
            throw new BuildException(msg);
        }
        log("Warning: " + msg, 0);
    }

    private boolean isWritable() {
        return this.permissions.contains(PosixFilePermission.OWNER_WRITE) || this.permissions.contains(PosixFilePermission.GROUP_WRITE) || this.permissions.contains(PosixFilePermission.OTHERS_WRITE);
    }
}
