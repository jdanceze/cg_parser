package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Ear.class */
public class Ear extends Jar {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File deploymentDescriptor;
    private boolean descriptorAdded;
    private static final String XML_DESCRIPTOR_PATH = "META-INF/application.xml";

    public Ear() {
        this.archiveType = "ear";
        this.emptyBehavior = "create";
    }

    @Deprecated
    public void setEarfile(File earFile) {
        setDestFile(earFile);
    }

    public void setAppxml(File descr) {
        this.deploymentDescriptor = descr;
        if (!this.deploymentDescriptor.exists()) {
            throw new BuildException("Deployment descriptor: %s does not exist.", this.deploymentDescriptor);
        }
        ZipFileSet fs = new ZipFileSet();
        fs.setFile(this.deploymentDescriptor);
        fs.setFullpath(XML_DESCRIPTOR_PATH);
        super.addFileset(fs);
    }

    public void addArchives(ZipFileSet fs) {
        fs.setPrefix("/");
        super.addFileset(fs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Jar, org.apache.tools.ant.taskdefs.Zip
    public void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
        if (this.deploymentDescriptor == null && !isInUpdateMode()) {
            throw new BuildException("appxml attribute is required", getLocation());
        }
        super.initZipOutputStream(zOut);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Zip
    public void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
        if (XML_DESCRIPTOR_PATH.equalsIgnoreCase(vPath)) {
            if (this.deploymentDescriptor == null || !FILE_UTILS.fileNameEquals(this.deploymentDescriptor, file) || this.descriptorAdded) {
                logWhenWriting("Warning: selected " + this.archiveType + " files include a " + XML_DESCRIPTOR_PATH + " which will be ignored (please use appxml attribute to " + this.archiveType + " task)", 1);
                return;
            }
            super.zipFile(file, zOut, vPath, mode);
            this.descriptorAdded = true;
            return;
        }
        super.zipFile(file, zOut, vPath, mode);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Jar, org.apache.tools.ant.taskdefs.Zip
    public void cleanUp() {
        this.descriptorAdded = false;
        super.cleanUp();
    }
}
