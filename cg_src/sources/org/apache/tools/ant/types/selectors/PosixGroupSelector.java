package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/PosixGroupSelector.class */
public class PosixGroupSelector implements FileSelector {
    private String group;
    private boolean followSymlinks = true;

    public void setGroup(String group) {
        this.group = group;
    }

    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        GroupPrincipal group;
        if (this.group == null) {
            throw new BuildException("the group attribute is required");
        }
        try {
            if (this.followSymlinks) {
                group = ((PosixFileAttributes) Files.readAttributes(file.toPath(), PosixFileAttributes.class, new LinkOption[0])).group();
            } else {
                group = ((PosixFileAttributes) Files.readAttributes(file.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS)).group();
            }
            GroupPrincipal actualGroup = group;
            if (actualGroup != null) {
                if (actualGroup.getName().equals(this.group)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
