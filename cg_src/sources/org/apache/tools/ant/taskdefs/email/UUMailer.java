package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.UUEncoder;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/UUMailer.class */
class UUMailer extends PlainMailer {
    UUMailer() {
    }

    @Override // org.apache.tools.ant.taskdefs.email.PlainMailer
    protected void attach(File file, PrintStream out) throws IOException {
        if (!file.exists() || !file.canRead()) {
            throw new BuildException("File \"%s\" does not exist or is not readable.", file.getAbsolutePath());
        }
        InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath(), new OpenOption[0]));
        try {
            new UUEncoder(file.getName()).encode(in, out);
            in.close();
        } catch (Throwable th) {
            try {
                in.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
