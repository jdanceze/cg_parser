package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ManifestClassPath.class */
public class ManifestClassPath extends Task {
    private String name;
    private File dir;
    private int maxParentLevels = 2;
    private Path path;

    @Override // org.apache.tools.ant.Task
    public void execute() {
        String[] list;
        String relPath;
        if (this.name == null) {
            throw new BuildException("Missing 'property' attribute!");
        }
        if (this.dir == null) {
            throw new BuildException("Missing 'jarfile' attribute!");
        }
        if (getProject().getProperty(this.name) != null) {
            throw new BuildException("Property '%s' already set!", this.name);
        }
        if (this.path == null) {
            throw new BuildException("Missing nested <classpath>!");
        }
        StringBuilder tooLongSb = new StringBuilder();
        for (int i = 0; i < this.maxParentLevels + 1; i++) {
            tooLongSb.append("../");
        }
        String tooLongPrefix = tooLongSb.toString();
        FileUtils fileUtils = FileUtils.getFileUtils();
        this.dir = fileUtils.normalize(this.dir.getAbsolutePath());
        StringBuilder buffer = new StringBuilder();
        for (String element : this.path.list()) {
            String fullPath = new File(element).getAbsolutePath();
            File pathEntry = fileUtils.normalize(fullPath);
            try {
                if (this.dir.equals(pathEntry)) {
                    relPath = ".";
                } else {
                    relPath = FileUtils.getRelativePath(this.dir, pathEntry);
                }
                String canonicalPath = pathEntry.getCanonicalPath();
                if (File.separatorChar != '/') {
                    canonicalPath = canonicalPath.replace(File.separatorChar, '/');
                }
                if (relPath.equals(canonicalPath) || relPath.startsWith(tooLongPrefix)) {
                    throw new BuildException("No suitable relative path from %s to %s", this.dir, fullPath);
                }
                if (pathEntry.isDirectory() && !relPath.endsWith("/")) {
                    relPath = relPath + '/';
                }
                buffer.append(Locator.encodeURI(relPath));
                buffer.append(' ');
            } catch (Exception e) {
                throw new BuildException("error trying to get the relative path from " + this.dir + " to " + fullPath, e);
            }
        }
        getProject().setNewProperty(this.name, buffer.toString().trim());
    }

    public void setProperty(String name) {
        this.name = name;
    }

    public void setJarFile(File jarfile) {
        File parent = jarfile.getParentFile();
        if (!parent.isDirectory()) {
            throw new BuildException("Jar's directory not found: %s", parent);
        }
        this.dir = parent;
    }

    public void setMaxParentLevels(int levels) {
        if (levels < 0) {
            throw new BuildException("maxParentLevels must not be a negative number");
        }
        this.maxParentLevels = levels;
    }

    public void addClassPath(Path path) {
        this.path = path;
    }
}
