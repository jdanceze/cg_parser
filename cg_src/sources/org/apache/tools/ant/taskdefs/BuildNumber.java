package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Properties;
import javax.resource.spi.work.WorkException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/BuildNumber.class */
public class BuildNumber extends Task {
    private static final String DEFAULT_PROPERTY_NAME = "build.number";
    private static final String DEFAULT_FILENAME = "build.number";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File myFile;

    public void setFile(File file) {
        this.myFile = file;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        File savedFile = this.myFile;
        validate();
        Properties properties = loadProperties();
        int buildNumber = getBuildNumber(properties);
        properties.put("build.number", String.valueOf(buildNumber + 1));
        try {
            try {
                OutputStream output = Files.newOutputStream(this.myFile.toPath(), new OpenOption[0]);
                try {
                    properties.store(output, "Build Number for ANT. Do not edit!");
                    if (output != null) {
                        output.close();
                    }
                    getProject().setNewProperty("build.number", String.valueOf(buildNumber));
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
            } catch (IOException ioe) {
                throw new BuildException("Error while writing " + this.myFile, ioe);
            }
        } finally {
            this.myFile = savedFile;
        }
    }

    private int getBuildNumber(Properties properties) throws BuildException {
        String buildNumber = properties.getProperty("build.number", WorkException.UNDEFINED).trim();
        try {
            return Integer.parseInt(buildNumber);
        } catch (NumberFormatException nfe) {
            throw new BuildException(this.myFile + " contains a non integer build number: " + buildNumber, nfe);
        }
    }

    private Properties loadProperties() throws BuildException {
        try {
            InputStream input = Files.newInputStream(this.myFile.toPath(), new OpenOption[0]);
            Properties properties = new Properties();
            properties.load(input);
            if (input != null) {
                input.close();
            }
            return properties;
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
    }

    private void validate() throws BuildException {
        if (null == this.myFile) {
            this.myFile = FILE_UTILS.resolveFile(getProject().getBaseDir(), "build.number");
        }
        if (!this.myFile.exists()) {
            try {
                FILE_UTILS.createNewFile(this.myFile);
            } catch (IOException ioe) {
                throw new BuildException(this.myFile + " doesn't exist and new file can't be created.", ioe);
            }
        }
        if (!this.myFile.canRead()) {
            throw new BuildException("Unable to read from " + this.myFile + ".");
        }
        if (!this.myFile.canWrite()) {
            throw new BuildException("Unable to write to " + this.myFile + ".");
        }
    }
}
