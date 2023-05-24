package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/JarLibAvailableTask.class */
public class JarLibAvailableTask extends Task {
    private File libraryFile;
    private final List<ExtensionSet> extensionFileSets = new Vector();
    private String propertyName;
    private ExtensionAdapter requiredExtension;

    public void setProperty(String property) {
        this.propertyName = property;
    }

    public void setFile(File file) {
        this.libraryFile = file;
    }

    public void addConfiguredExtension(ExtensionAdapter extension) {
        if (null != this.requiredExtension) {
            throw new BuildException("Can not specify extension to search for multiple times.");
        }
        this.requiredExtension = extension;
    }

    public void addConfiguredExtensionSet(ExtensionSet extensionSet) {
        this.extensionFileSets.add(extensionSet);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Stream of;
        validate();
        Project prj = getProject();
        if (!this.extensionFileSets.isEmpty()) {
            of = this.extensionFileSets.stream().map(xset -> {
                return xset.toExtensions(prj);
            }).flatMap((v0) -> {
                return Stream.of(v0);
            });
        } else {
            of = Stream.of((Object[]) Extension.getAvailable(ExtensionUtil.getManifest(this.libraryFile)));
        }
        Extension test = this.requiredExtension.toExtension();
        if (of.anyMatch(x -> {
            return x.isCompatibleWith(test);
        })) {
            prj.setNewProperty(this.propertyName, "true");
        }
    }

    private void validate() throws BuildException {
        if (null == this.requiredExtension) {
            throw new BuildException("Extension element must be specified.");
        }
        if (null == this.libraryFile) {
            if (this.extensionFileSets.isEmpty()) {
                throw new BuildException("File attribute not specified.");
            }
        } else if (!this.libraryFile.exists()) {
            throw new BuildException("File '%s' does not exist.", this.libraryFile);
        } else {
            if (!this.libraryFile.isFile()) {
                throw new BuildException("'%s' is not a file.", this.libraryFile);
            }
        }
    }
}
