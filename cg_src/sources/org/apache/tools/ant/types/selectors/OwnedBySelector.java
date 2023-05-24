package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.UserPrincipal;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/OwnedBySelector.class */
public class OwnedBySelector implements FileSelector {
    private String owner;
    private boolean followSymlinks = true;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        if (this.owner == null) {
            throw new BuildException("the owner attribute is required");
        }
        if (file != null) {
            try {
                UserPrincipal user = this.followSymlinks ? Files.getOwner(file.toPath(), new LinkOption[0]) : Files.getOwner(file.toPath(), LinkOption.NOFOLLOW_LINKS);
                if (user != null) {
                    if (this.owner.equals(user.getName())) {
                        return true;
                    }
                }
                return false;
            } catch (IOException | UnsupportedOperationException e) {
                return false;
            }
        }
        return false;
    }
}
