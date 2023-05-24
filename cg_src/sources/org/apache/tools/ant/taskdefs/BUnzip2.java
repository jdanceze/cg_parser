package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2InputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/BUnzip2.class */
public class BUnzip2 extends Unpack {
    private static final int BUFFER_SIZE = 8192;
    private static final String DEFAULT_EXTENSION = ".bz2";

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected void extract() {
        if (this.srcResource.getLastModified() > this.dest.lastModified()) {
            log("Expanding " + this.srcResource.getName() + " to " + this.dest.getAbsolutePath());
            OutputStream out = null;
            CBZip2InputStream zIn = null;
            InputStream fis = null;
            BufferedInputStream bis = null;
            try {
                try {
                    out = Files.newOutputStream(this.dest.toPath(), new OpenOption[0]);
                    fis = this.srcResource.getInputStream();
                    bis = new BufferedInputStream(fis);
                    int b = bis.read();
                    if (b != 66) {
                        throw new BuildException("Invalid bz2 file.", getLocation());
                    }
                    int b2 = bis.read();
                    if (b2 != 90) {
                        throw new BuildException("Invalid bz2 file.", getLocation());
                    }
                    zIn = new CBZip2InputStream(bis, true);
                    byte[] buffer = new byte[8192];
                    int count = 0;
                    do {
                        out.write(buffer, 0, count);
                        count = zIn.read(buffer, 0, buffer.length);
                    } while (count != -1);
                } catch (IOException ioe) {
                    String msg = "Problem expanding bzip2 " + ioe.getMessage();
                    throw new BuildException(msg, ioe, getLocation());
                }
            } finally {
                FileUtils.close((InputStream) bis);
                FileUtils.close(fis);
                FileUtils.close(out);
                FileUtils.close((InputStream) zIn);
            }
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Unpack
    protected boolean supportsNonFileResources() {
        return getClass().equals(BUnzip2.class);
    }
}
