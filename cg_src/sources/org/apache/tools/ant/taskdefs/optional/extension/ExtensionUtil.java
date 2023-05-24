package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/ExtensionUtil.class */
public final class ExtensionUtil {
    private ExtensionUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<Extension> toExtensions(List<? extends ExtensionAdapter> adapters) throws BuildException {
        return (ArrayList) adapters.stream().map((v0) -> {
            return v0.toExtension();
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void extractExtensions(Project project, List<Extension> libraries, List<FileSet> fileset) throws BuildException {
        if (!fileset.isEmpty()) {
            Collections.addAll(libraries, getExtensions(project, fileset));
        }
    }

    private static Extension[] getExtensions(Project project, List<FileSet> libraries) throws BuildException {
        String[] includedFiles;
        List<Extension> extensions = new ArrayList<>();
        for (FileSet fileSet : libraries) {
            boolean includeImpl = true;
            boolean includeURL = true;
            if (fileSet instanceof LibFileSet) {
                LibFileSet libFileSet = (LibFileSet) fileSet;
                includeImpl = libFileSet.isIncludeImpl();
                includeURL = libFileSet.isIncludeURL();
            }
            DirectoryScanner scanner = fileSet.getDirectoryScanner(project);
            File basedir = scanner.getBasedir();
            for (String fileName : scanner.getIncludedFiles()) {
                File file = new File(basedir, fileName);
                loadExtensions(file, extensions, includeImpl, includeURL);
            }
        }
        return (Extension[]) extensions.toArray(new Extension[extensions.size()]);
    }

    private static void loadExtensions(File file, List<Extension> extensionList, boolean includeImpl, boolean includeURL) throws BuildException {
        Extension[] available;
        try {
            JarFile jarFile = new JarFile(file);
            for (Extension extension : Extension.getAvailable(jarFile.getManifest())) {
                addExtension(extensionList, extension, includeImpl, includeURL);
            }
            jarFile.close();
        } catch (Exception e) {
            throw new BuildException(e.getMessage(), e);
        }
    }

    private static void addExtension(List<Extension> extensionList, Extension originalExtension, boolean includeImpl, boolean includeURL) {
        Extension extension = originalExtension;
        if (!includeURL && null != extension.getImplementationURL()) {
            extension = new Extension(extension.getExtensionName(), extension.getSpecificationVersion().toString(), extension.getSpecificationVendor(), extension.getImplementationVersion().toString(), extension.getImplementationVendor(), extension.getImplementationVendorID(), null);
        }
        boolean hasImplAttributes = (null == extension.getImplementationURL() && null == extension.getImplementationVersion() && null == extension.getImplementationVendorID() && null == extension.getImplementationVendor()) ? false : true;
        if (!includeImpl && hasImplAttributes) {
            extension = new Extension(extension.getExtensionName(), extension.getSpecificationVersion().toString(), extension.getSpecificationVendor(), null, null, null, extension.getImplementationURL());
        }
        extensionList.add(extension);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Manifest getManifest(File file) throws BuildException {
        try {
            JarFile jarFile = new JarFile(file);
            Manifest m = jarFile.getManifest();
            if (m == null) {
                throw new BuildException("%s doesn't have a MANIFEST", file);
            }
            jarFile.close();
            return m;
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage(), ioe);
        }
    }
}
