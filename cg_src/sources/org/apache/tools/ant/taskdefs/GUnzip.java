package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.zip.GZIPInputStream;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/GUnzip.class */
public class GUnzip extends Unpack {
    private static final int BUFFER_SIZE = 8192;
    private static final String DEFAULT_EXTENSION = ".gz";

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected void extract() {
        if (this.srcResource.getLastModified() > this.dest.lastModified()) {
            log("Expanding " + this.srcResource.getName() + " to " + this.dest.getAbsolutePath());
            try {
                OutputStream out = Files.newOutputStream(this.dest.toPath(), new OpenOption[0]);
                GZIPInputStream zIn = new GZIPInputStream(this.srcResource.getInputStream());
                try {
                    byte[] buffer = new byte[8192];
                    int count = 0;
                    do {
                        out.write(buffer, 0, count);
                        count = zIn.read(buffer, 0, buffer.length);
                    } while (count != -1);
                    zIn.close();
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    try {
                        zIn.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException ioe) {
                String msg = "Problem expanding gzip " + ioe.getMessage();
                throw new BuildException(msg, ioe, getLocation());
            }
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected boolean supportsNonFileResources() {
        return getClass().equals(GUnzip.class);
    }
}
