package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/Message.class */
public class Message extends ProjectComponent {
    private File messageSource;
    private StringBuffer buffer;
    private String mimeType;
    private boolean specified;
    private String charset;
    private String inputEncoding;

    public Message() {
        this.messageSource = null;
        this.buffer = new StringBuffer();
        this.mimeType = "text/plain";
        this.specified = false;
        this.charset = null;
    }

    public Message(String text) {
        this.messageSource = null;
        this.buffer = new StringBuffer();
        this.mimeType = "text/plain";
        this.specified = false;
        this.charset = null;
        addText(text);
    }

    public Message(File file) {
        this.messageSource = null;
        this.buffer = new StringBuffer();
        this.mimeType = "text/plain";
        this.specified = false;
        this.charset = null;
        this.messageSource = file;
    }

    public void addText(String text) {
        this.buffer.append(text);
    }

    public void setSrc(File src) {
        this.messageSource = src;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
        this.specified = true;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void print(PrintStream ps) throws IOException {
        BufferedWriter out = this.charset == null ? new BufferedWriter(new OutputStreamWriter(ps)) : new BufferedWriter(new OutputStreamWriter(ps, this.charset));
        if (this.messageSource != null) {
            BufferedReader in = new BufferedReader(getReader(this.messageSource));
            while (true) {
                try {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    out.write(getProject().replaceProperties(line));
                    out.newLine();
                } catch (Throwable th) {
                    try {
                        in.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            in.close();
        } else {
            out.write(getProject().replaceProperties(this.buffer.substring(0)));
            out.newLine();
        }
        out.flush();
    }

    public boolean isMimeTypeSpecified() {
        return this.specified;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setInputEncoding(String encoding) {
        this.inputEncoding = encoding;
    }

    private Reader getReader(File f) throws IOException {
        if (this.inputEncoding != null) {
            InputStream fis = Files.newInputStream(f.toPath(), new OpenOption[0]);
            try {
                return new InputStreamReader(fis, this.inputEncoding);
            } catch (IOException ex) {
                fis.close();
                throw ex;
            }
        }
        return new FileReader(f);
    }
}
