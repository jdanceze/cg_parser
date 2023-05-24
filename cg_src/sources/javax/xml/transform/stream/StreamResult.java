package javax.xml.transform.stream;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.Result;
import org.apache.tools.ant.taskdefs.XSLTLiaison;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/stream/StreamResult.class */
public class StreamResult implements Result {
    public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
    private String systemId;
    private OutputStream outputStream;
    private Writer writer;

    public StreamResult() {
    }

    public StreamResult(OutputStream outputStream) {
        setOutputStream(outputStream);
    }

    public StreamResult(Writer writer) {
        setWriter(writer);
    }

    public StreamResult(String str) {
        this.systemId = str;
    }

    public StreamResult(File file) {
        setSystemId(file);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return this.writer;
    }

    @Override // javax.xml.transform.Result
    public void setSystemId(String str) {
        this.systemId = str;
    }

    public void setSystemId(File file) {
        String absolutePath = file.getAbsolutePath();
        if (File.separatorChar != '/') {
            absolutePath = absolutePath.replace(File.separatorChar, '/');
        }
        if (absolutePath.startsWith("/")) {
            this.systemId = new StringBuffer().append(XSLTLiaison.FILE_PROTOCOL_PREFIX).append(absolutePath).toString();
        } else {
            this.systemId = new StringBuffer().append("file:///").append(absolutePath).toString();
        }
    }

    @Override // javax.xml.transform.Result
    public String getSystemId() {
        return this.systemId;
    }
}
