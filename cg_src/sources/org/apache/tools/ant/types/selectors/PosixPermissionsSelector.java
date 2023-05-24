package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.PermissionUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/PosixPermissionsSelector.class */
public class PosixPermissionsSelector implements FileSelector {
    private String permissions;
    private boolean followSymlinks = true;

    public void setPermissions(String permissions) {
        if (permissions.length() == 3 && permissions.matches("^[0-7]+$")) {
            this.permissions = PosixFilePermissions.toString(PermissionUtils.permissionsFromMode(Integer.parseInt(permissions, 8)));
            return;
        }
        try {
            this.permissions = PosixFilePermissions.toString(PosixFilePermissions.fromString(permissions));
        } catch (IllegalArgumentException ex) {
            throw new BuildException("the permissions attribute " + permissions + " is invalid", ex);
        }
    }

    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        Set<PosixFilePermission> posixFilePermissions;
        if (this.permissions == null) {
            throw new BuildException("the permissions attribute is required");
        }
        try {
            if (this.followSymlinks) {
                posixFilePermissions = Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
            } else {
                posixFilePermissions = Files.getPosixFilePermissions(file.toPath(), LinkOption.NOFOLLOW_LINKS);
            }
            return PosixFilePermissions.toString(posixFilePermissions).equals(this.permissions);
        } catch (IOException e) {
            return false;
        }
    }
}
