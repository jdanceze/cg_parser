package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.StreamUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ManifestTask.class */
public class ManifestTask extends Task {
    public static final String VALID_ATTRIBUTE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private File manifestFile;
    private String encoding;
    private Manifest nestedManifest = new Manifest();
    private boolean mergeClassPaths = false;
    private boolean flattenClassPaths = false;
    private Mode mode = new Mode();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ManifestTask$Mode.class */
    public static class Mode extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"update", MSVSSConstants.WRITABLE_REPLACE};
        }
    }

    public ManifestTask() {
        this.mode.setValue(MSVSSConstants.WRITABLE_REPLACE);
    }

    public void addConfiguredSection(Manifest.Section section) throws ManifestException {
        Stream enumerationAsStream = StreamUtils.enumerationAsStream(section.getAttributeKeys());
        Objects.requireNonNull(section);
        enumerationAsStream.map(this::getAttribute).forEach(this::checkAttribute);
        this.nestedManifest.addConfiguredSection(section);
    }

    public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
        checkAttribute(attribute);
        this.nestedManifest.addConfiguredAttribute(attribute);
    }

    private void checkAttribute(Manifest.Attribute attribute) throws BuildException {
        String name = attribute.getName();
        char ch = name.charAt(0);
        if (ch == '-' || ch == '_') {
            throw new BuildException("Manifest attribute names must not start with '%c'.", Character.valueOf(ch));
        }
        for (int i = 0; i < name.length(); i++) {
            char ch2 = name.charAt(i);
            if (VALID_ATTRIBUTE_CHARS.indexOf(ch2) < 0) {
                throw new BuildException("Manifest attribute names must not contain '%c'", Character.valueOf(ch2));
            }
        }
    }

    public void setFile(File f) {
        this.manifestFile = f;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setMode(Mode m) {
        this.mode = m;
    }

    public void setMergeClassPathAttributes(boolean b) {
        this.mergeClassPaths = b;
    }

    public void setFlattenAttributes(boolean b) {
        this.flattenClassPaths = b;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.manifestFile == null) {
            throw new BuildException("the file attribute is required");
        }
        Manifest toWrite = Manifest.getDefaultManifest();
        Manifest current = null;
        BuildException error = null;
        if (this.manifestFile.exists()) {
            Charset charset = Charset.forName(this.encoding == null ? "UTF-8" : this.encoding);
            try {
                InputStreamReader isr = new InputStreamReader(Files.newInputStream(this.manifestFile.toPath(), new OpenOption[0]), charset);
                try {
                    current = new Manifest(isr);
                    isr.close();
                } catch (Throwable th) {
                    try {
                        isr.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e) {
                error = new BuildException("Failed to read " + this.manifestFile, e, getLocation());
            } catch (ManifestException m) {
                error = new BuildException("Existing manifest " + this.manifestFile + " is invalid", m, getLocation());
            }
        }
        StreamUtils.enumerationAsStream(this.nestedManifest.getWarnings()).forEach(e2 -> {
            log("Manifest warning: " + e2, 1);
        });
        try {
            if ("update".equals(this.mode.getValue()) && this.manifestFile.exists()) {
                if (current != null) {
                    toWrite.merge(current, false, this.mergeClassPaths);
                } else if (error != null) {
                    throw error;
                }
            }
            toWrite.merge(this.nestedManifest, false, this.mergeClassPaths);
            if (toWrite.equals(current)) {
                log("Manifest has not changed, do not recreate", 3);
                return;
            }
            try {
                PrintWriter w = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.manifestFile.toPath(), new OpenOption[0]), Manifest.JAR_CHARSET));
                toWrite.write(w, this.flattenClassPaths);
                if (w.checkError()) {
                    throw new IOException("Encountered an error writing manifest");
                }
                w.close();
            } catch (IOException e3) {
                throw new BuildException("Failed to write " + this.manifestFile, e3, getLocation());
            }
        } catch (ManifestException m2) {
            throw new BuildException("Manifest is invalid", m2, getLocation());
        }
    }
}
