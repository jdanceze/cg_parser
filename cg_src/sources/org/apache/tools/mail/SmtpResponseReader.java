package org.apache.tools.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/mail/SmtpResponseReader.class */
public class SmtpResponseReader {
    protected BufferedReader reader;

    public SmtpResponseReader(InputStream in) {
        this.reader = null;
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public String getResponse() throws IOException {
        StringBuilder result = new StringBuilder();
        String line = this.reader.readLine();
        if (line != null && line.length() >= 3) {
            result.append((CharSequence) line, 0, 3);
            result.append(Instruction.argsep);
        }
        while (line != null) {
            appendTo(result, line);
            if (!hasMoreLines(line)) {
                break;
            }
            line = this.reader.readLine();
        }
        return result.toString().trim();
    }

    public void close() throws IOException {
        this.reader.close();
    }

    protected boolean hasMoreLines(String line) {
        return line.length() > 3 && line.charAt(3) == '-';
    }

    private static void appendTo(StringBuilder target, String line) {
        if (line.length() > 4) {
            target.append(line.substring(4)).append(' ');
        }
    }
}
