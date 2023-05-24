package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.ProjectHelperRepository;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.URLProvider;
import org.apache.tools.ant.types.resources.URLResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ImportTask.class */
public class ImportTask extends Task {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String file;
    private boolean optional;
    private String targetPrefix = ProjectHelper.USE_PROJECT_NAME_AS_TARGET_PREFIX;
    private String prefixSeparator = ".";
    private final Union resources = new Union();

    public ImportTask() {
        this.resources.setCache(true);
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setAs(String prefix) {
        this.targetPrefix = prefix;
    }

    public void setPrefixSeparator(String s) {
        this.prefixSeparator = s;
    }

    public void add(ResourceCollection r) {
        this.resources.add(r);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.file == null && this.resources.isEmpty()) {
            throw new BuildException("import requires file attribute or at least one nested resource");
        }
        if (getOwningTarget() == null || !getOwningTarget().getName().isEmpty()) {
            throw new BuildException("import only allowed as a top-level task");
        }
        ProjectHelper helper = (ProjectHelper) getProject().getReference("ant.projectHelper");
        if (helper == null) {
            throw new BuildException("import requires support in ProjectHelper");
        }
        if (helper.getImportStack().isEmpty()) {
            throw new BuildException("import requires support in ProjectHelper");
        }
        if (getLocation() == null || getLocation().getFileName() == null) {
            throw new BuildException("Unable to get location of import task");
        }
        Union resourcesToImport = new Union(getProject(), this.resources);
        Resource fromFileAttribute = getFileAttributeResource();
        if (fromFileAttribute != null) {
            this.resources.add(fromFileAttribute);
        }
        Iterator<Resource> it = resourcesToImport.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            importResource(helper, r);
        }
    }

    private void importResource(ProjectHelper helper, Resource importedResource) {
        String prefix;
        getProject().log("Importing file " + importedResource + " from " + getLocation().getFileName(), 3);
        if (!importedResource.isExists()) {
            String message = "Cannot find " + importedResource + " imported from " + getLocation().getFileName();
            if (this.optional) {
                getProject().log(message, 3);
                return;
            }
            throw new BuildException(message);
        } else if (!isInIncludeMode() && hasAlreadyBeenImported(importedResource, helper.getImportStack())) {
            getProject().log("Skipped already imported file:\n   " + importedResource + "\n", 3);
        } else {
            String oldPrefix = ProjectHelper.getCurrentTargetPrefix();
            boolean oldIncludeMode = ProjectHelper.isInIncludeMode();
            String oldSep = ProjectHelper.getCurrentPrefixSeparator();
            try {
                try {
                    if (isInIncludeMode() && oldPrefix != null && this.targetPrefix != null) {
                        prefix = oldPrefix + oldSep + this.targetPrefix;
                    } else if (isInIncludeMode()) {
                        prefix = this.targetPrefix;
                    } else if (ProjectHelper.USE_PROJECT_NAME_AS_TARGET_PREFIX.equals(this.targetPrefix)) {
                        prefix = oldPrefix;
                    } else {
                        prefix = this.targetPrefix;
                    }
                    setProjectHelperProps(prefix, this.prefixSeparator, isInIncludeMode());
                    ProjectHelper subHelper = ProjectHelperRepository.getInstance().getProjectHelperForBuildFile(importedResource);
                    subHelper.getImportStack().addAll(helper.getImportStack());
                    subHelper.getExtensionStack().addAll(helper.getExtensionStack());
                    getProject().addReference("ant.projectHelper", subHelper);
                    subHelper.parse(getProject(), importedResource);
                    getProject().addReference("ant.projectHelper", helper);
                    helper.getImportStack().clear();
                    helper.getImportStack().addAll(subHelper.getImportStack());
                    helper.getExtensionStack().clear();
                    helper.getExtensionStack().addAll(subHelper.getExtensionStack());
                    setProjectHelperProps(oldPrefix, oldSep, oldIncludeMode);
                } catch (BuildException ex) {
                    throw ProjectHelper.addLocationToBuildException(ex, getLocation());
                }
            } catch (Throwable th) {
                setProjectHelperProps(oldPrefix, oldSep, oldIncludeMode);
                throw th;
            }
        }
    }

    private Resource getFileAttributeResource() {
        if (this.file != null) {
            if (isExistingAbsoluteFile(this.file)) {
                return new FileResource(FILE_UTILS.normalize(this.file));
            }
            File buildFile = new File(getLocation().getFileName()).getAbsoluteFile();
            if (buildFile.exists()) {
                File buildFileParent = new File(buildFile.getParent());
                File importedFile = FILE_UTILS.resolveFile(buildFileParent, this.file);
                return new FileResource(importedFile);
            }
            try {
                URL buildFileURL = new URL(getLocation().getFileName());
                URL importedFile2 = new URL(buildFileURL, this.file);
                return new URLResource(importedFile2);
            } catch (MalformedURLException ex) {
                log(ex.toString(), 3);
                throw new BuildException("failed to resolve %s relative to %s", this.file, getLocation().getFileName());
            }
        }
        return null;
    }

    private boolean isExistingAbsoluteFile(String name) {
        File f = new File(name);
        return f.isAbsolute() && f.exists();
    }

    private boolean hasAlreadyBeenImported(Resource importedResource, Vector<Object> importStack) {
        File importedFile = (File) importedResource.asOptional(FileProvider.class).map((v0) -> {
            return v0.getFile();
        }).orElse(null);
        URL importedURL = (URL) importedResource.asOptional(URLProvider.class).map((v0) -> {
            return v0.getURL();
        }).orElse(null);
        return importStack.stream().anyMatch(o -> {
            return isOneOf(importedURL, importedResource, importedResource, importedFile);
        });
    }

    private boolean isOneOf(Object o, Resource importedResource, File importedFile, URL importedURL) {
        URLProvider up;
        FileProvider fp;
        if (o.equals(importedResource) || o.equals(importedFile) || o.equals(importedURL)) {
            return true;
        }
        if (o instanceof Resource) {
            if (importedFile == null || (fp = (FileProvider) ((Resource) o).as(FileProvider.class)) == null || !fp.getFile().equals(importedFile)) {
                return (importedURL == null || (up = (URLProvider) ((Resource) o).as(URLProvider.class)) == null || !up.getURL().equals(importedURL)) ? false : true;
            }
            return true;
        }
        return false;
    }

    protected final boolean isInIncludeMode() {
        return "include".equals(getTaskType());
    }

    private static void setProjectHelperProps(String prefix, String prefixSep, boolean inIncludeMode) {
        ProjectHelper.setCurrentTargetPrefix(prefix);
        ProjectHelper.setCurrentPrefixSeparator(prefixSep);
        ProjectHelper.setInIncludeMode(inIncludeMode);
    }
}
