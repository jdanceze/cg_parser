package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.launch.Launcher;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/JarLibManifestTask.class */
public final class JarLibManifestTask extends Task {
    private static final String MANIFEST_VERSION = "1.0";
    private static final String CREATED_BY = "Created-By";
    private File destFile;
    private Extension extension;
    private final List<ExtensionSet> dependencies = new ArrayList();
    private final List<ExtensionSet> optionals = new ArrayList();
    private final List<ExtraAttribute> extraAttributes = new ArrayList();

    public void setDestfile(File destFile) {
        this.destFile = destFile;
    }

    public void addConfiguredExtension(ExtensionAdapter extensionAdapter) throws BuildException {
        if (null != this.extension) {
            throw new BuildException("Can not have multiple extensions defined in one library.");
        }
        this.extension = extensionAdapter.toExtension();
    }

    public void addConfiguredDepends(ExtensionSet extensionSet) {
        this.dependencies.add(extensionSet);
    }

    public void addConfiguredOptions(ExtensionSet extensionSet) {
        this.optionals.add(extensionSet);
    }

    public void addConfiguredAttribute(ExtraAttribute attribute) {
        this.extraAttributes.add(attribute);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validate();
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.putValue(CREATED_BY, "Apache Ant " + getProject().getProperty(MagicNames.ANT_VERSION));
        appendExtraAttributes(attributes);
        if (null != this.extension) {
            Extension.addExtension(this.extension, attributes);
        }
        List<Extension> depends = toExtensions(this.dependencies);
        appendExtensionList(attributes, Extension.EXTENSION_LIST, Launcher.ANT_PRIVATELIB, depends.size());
        appendLibraryList(attributes, Launcher.ANT_PRIVATELIB, depends);
        List<Extension> option = toExtensions(this.optionals);
        appendExtensionList(attributes, Extension.OPTIONAL_EXTENSION_LIST, "opt", option.size());
        appendLibraryList(attributes, "opt", option);
        try {
            log("Generating manifest " + this.destFile.getAbsoluteFile(), 2);
            writeManifest(manifest);
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage(), ioe);
        }
    }

    private void validate() throws BuildException {
        if (null == this.destFile) {
            throw new BuildException("Destfile attribute not specified.");
        }
        if (this.destFile.exists() && !this.destFile.isFile()) {
            throw new BuildException("%s is not a file.", this.destFile);
        }
    }

    private void appendExtraAttributes(Attributes attributes) {
        for (ExtraAttribute attribute : this.extraAttributes) {
            attributes.putValue(attribute.getName(), attribute.getValue());
        }
    }

    private void writeManifest(Manifest manifest) throws IOException {
        OutputStream output = Files.newOutputStream(this.destFile.toPath(), new OpenOption[0]);
        try {
            manifest.write(output);
            output.flush();
            if (output != null) {
                output.close();
            }
        } catch (Throwable th) {
            if (output != null) {
                try {
                    output.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void appendLibraryList(Attributes attributes, String listPrefix, List<Extension> extensions) throws BuildException {
        int size = extensions.size();
        for (int i = 0; i < size; i++) {
            Extension.addExtension(extensions.get(i), listPrefix + i + HelpFormatter.DEFAULT_OPT_PREFIX, attributes);
        }
    }

    private void appendExtensionList(Attributes attributes, Attributes.Name extensionKey, String listPrefix, int size) {
        attributes.put(extensionKey, IntStream.range(0, size).mapToObj(i -> {
            return listPrefix + i;
        }).collect(Collectors.joining(Instruction.argsep)));
    }

    private List<Extension> toExtensions(List<ExtensionSet> extensionSets) throws BuildException {
        Project prj = getProject();
        return (List) extensionSets.stream().map(xset -> {
            return xset.toExtensions(prj);
        }).flatMap((v0) -> {
            return Stream.of(v0);
        }).collect(Collectors.toList());
    }
}
