package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/BZip2.class */
public class BZip2 extends Pack {
    @Override // org.apache.tools.ant.taskdefs.Pack
    protected void pack() {
        CBZip2OutputStream zOut = null;
        try {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(this.zipFile.toPath(), new OpenOption[0]));
                bos.write(66);
                bos.write(90);
                zOut = new CBZip2OutputStream(bos);
                zipResource(getSrcResource(), zOut);
                FileUtils.close((OutputStream) zOut);
            } catch (IOException ioe) {
                String msg = "Problem creating bzip2 " + ioe.getMessage();
                throw new BuildException(msg, ioe, getLocation());
            }
        } catch (Throwable th) {
            FileUtils.close((OutputStream) zOut);
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Pack
    protected boolean supportsNonFileResources() {
        return getClass().equals(BZip2.class);
    }
}
