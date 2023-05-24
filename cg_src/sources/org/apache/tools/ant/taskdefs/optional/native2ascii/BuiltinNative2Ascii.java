package org.apache.tools.ant.taskdefs.optional.native2ascii;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.function.UnaryOperator;
import org.apache.http.protocol.HTTP;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
import org.apache.tools.ant.util.Native2AsciiUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/native2ascii/BuiltinNative2Ascii.class */
public class BuiltinNative2Ascii implements Native2AsciiAdapter {
    static final String IMPLEMENTATION_NAME = "builtin";

    @Override // org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapter
    public final boolean convert(Native2Ascii args, File srcFile, File destFile) throws BuildException {
        boolean reverse = args.getReverse();
        String encoding = args.getEncoding();
        try {
            BufferedReader input = getReader(srcFile, encoding, reverse);
            Writer output = getWriter(destFile, encoding, reverse);
            try {
                translate(input, output, reverse ? Native2AsciiUtils::ascii2native : Native2AsciiUtils::native2ascii);
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                return true;
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
        } catch (IOException ex) {
            throw new BuildException("Exception trying to translate data", ex);
        }
    }

    private BufferedReader getReader(File srcFile, String encoding, boolean reverse) throws IOException {
        if (reverse || encoding == null) {
            return new BufferedReader(new FileReader(srcFile));
        }
        return new BufferedReader(new InputStreamReader(Files.newInputStream(srcFile.toPath(), new OpenOption[0]), encoding));
    }

    private Writer getWriter(File destFile, String encoding, boolean reverse) throws IOException {
        if (!reverse) {
            encoding = HTTP.ASCII;
        }
        if (encoding == null) {
            return new BufferedWriter(new FileWriter(destFile));
        }
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(destFile.toPath(), new OpenOption[0]), encoding));
    }

    private void translate(BufferedReader input, Writer output, UnaryOperator<String> translation) throws IOException {
        for (String line : () -> {
            return input.lines().map(translation).iterator();
        }) {
            output.write(String.format("%s%n", line));
        }
    }
}
