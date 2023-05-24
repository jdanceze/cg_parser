package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.zip.GZIPOutputStream;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/GZip.class */
public class GZip extends Pack {
    @Override // org.apache.tools.ant.taskdefs.Pack
    protected void pack() {
        try {
            GZIPOutputStream zOut = new GZIPOutputStream(Files.newOutputStream(this.zipFile.toPath(), new OpenOption[0]));
            zipResource(getSrcResource(), zOut);
            zOut.close();
        } catch (IOException ioe) {
            String msg = "Problem creating gzip " + ioe.getMessage();
            throw new BuildException(msg, ioe, getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Pack
    protected boolean supportsNonFileResources() {
        return getClass().equals(GZip.class);
    }
}
