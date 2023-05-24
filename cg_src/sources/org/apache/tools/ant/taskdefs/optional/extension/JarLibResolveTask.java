package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.extension.resolvers.AntResolver;
import org.apache.tools.ant.taskdefs.optional.extension.resolvers.LocationResolver;
import org.apache.tools.ant.taskdefs.optional.extension.resolvers.URLResolver;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/JarLibResolveTask.class */
public class JarLibResolveTask extends Task {
    private String propertyName;
    private Extension requiredExtension;
    private final List<ExtensionResolver> resolvers = new ArrayList();
    private boolean checkExtension = true;
    private boolean failOnError = true;

    public void setProperty(String property) {
        this.propertyName = property;
    }

    public void setCheckExtension(boolean checkExtension) {
        this.checkExtension = checkExtension;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public void addConfiguredLocation(LocationResolver loc) {
        this.resolvers.add(loc);
    }

    public void addConfiguredUrl(URLResolver url) {
        this.resolvers.add(url);
    }

    public void addConfiguredAnt(AntResolver ant) {
        this.resolvers.add(ant);
    }

    public void addConfiguredExtension(ExtensionAdapter extension) {
        if (null != this.requiredExtension) {
            throw new BuildException("Can not specify extension to resolve multiple times.");
        }
        this.requiredExtension = extension.toExtension();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validate();
        getProject().log("Resolving extension: " + this.requiredExtension, 3);
        String candidate = getProject().getProperty(this.propertyName);
        if (null != candidate) {
            String message = "Property Already set to: " + candidate;
            if (this.failOnError) {
                throw new BuildException(message);
            }
            getProject().log(message, 0);
            return;
        }
        for (ExtensionResolver resolver : this.resolvers) {
            getProject().log("Searching for extension using Resolver:" + resolver, 3);
            try {
                File file = resolver.resolve(this.requiredExtension, getProject());
                try {
                    checkExtension(file);
                    return;
                } catch (BuildException be) {
                    getProject().log("File " + file + " returned by resolver failed to satisfy extension due to: " + be.getMessage(), 1);
                }
            } catch (BuildException be2) {
                getProject().log("Failed to resolve extension to file using resolver " + resolver + " due to: " + be2, 1);
            }
        }
        missingExtension();
    }

    private void missingExtension() {
        if (this.failOnError) {
            throw new BuildException("Unable to resolve extension to a file");
        }
        getProject().log("Unable to resolve extension to a file", 0);
    }

    private void checkExtension(File file) {
        Extension[] available;
        if (!file.exists()) {
            throw new BuildException("File %s does not exist", file);
        }
        if (!file.isFile()) {
            throw new BuildException("File %s is not a file", file);
        }
        if (!this.checkExtension) {
            getProject().log("Setting property to " + file + " without verifying library satisfies extension", 3);
            setLibraryProperty(file);
            return;
        }
        getProject().log("Checking file " + file + " to see if it satisfies extension", 3);
        Manifest manifest = ExtensionUtil.getManifest(file);
        for (Extension extension : Extension.getAvailable(manifest)) {
            if (extension.isCompatibleWith(this.requiredExtension)) {
                setLibraryProperty(file);
                return;
            }
        }
        String message = "File " + file + " skipped as it does not satisfy extension";
        getProject().log(message, 3);
        throw new BuildException(message);
    }

    private void setLibraryProperty(File file) {
        getProject().setNewProperty(this.propertyName, file.getAbsolutePath());
    }

    private void validate() throws BuildException {
        if (null == this.propertyName) {
            throw new BuildException("Property attribute must be specified.");
        }
        if (null == this.requiredExtension) {
            throw new BuildException("Extension element must be specified.");
        }
    }
}
