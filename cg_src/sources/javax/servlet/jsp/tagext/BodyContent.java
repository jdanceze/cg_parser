package javax.servlet.jsp.tagext;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.servlet.jsp.JspWriter;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/BodyContent.class */
public abstract class BodyContent extends JspWriter {
    private JspWriter enclosingWriter;

    public abstract Reader getReader();

    public abstract String getString();

    public abstract void writeOut(Writer writer) throws IOException;

    protected BodyContent(JspWriter e) {
        super(-2, false);
        this.enclosingWriter = e;
    }

    @Override // javax.servlet.jsp.JspWriter, java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        throw new IOException("Illegal to flush within a custom tag");
    }

    public void clearBody() {
        try {
            clear();
        } catch (IOException e) {
            throw new Error("internal error!;");
        }
    }

    public JspWriter getEnclosingWriter() {
        return this.enclosingWriter;
    }
}
