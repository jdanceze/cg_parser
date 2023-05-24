package org.apache.tools.ant.types.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/StringResource.class */
public class StringResource extends Resource {
    private static final int STRING_MAGIC = Resource.getMagicNumber("StringResource".getBytes());
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;

    public StringResource() {
        this.encoding = "UTF-8";
    }

    public StringResource(String value) {
        this(null, value);
    }

    public StringResource(Project project, String value) {
        this.encoding = "UTF-8";
        setProject(project);
        setValue(project == null ? value : project.replaceProperties(value));
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized void setName(String s) {
        if (getName() != null) {
            throw new BuildException(new ImmutableResourceException());
        }
        super.setName(s);
    }

    public synchronized void setValue(String s) {
        setName(s);
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized String getName() {
        return super.getName();
    }

    public synchronized String getValue() {
        return getName();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        return getValue() != null;
    }

    public void addText(String text) {
        checkChildrenAllowed();
        setValue(getProject().replaceProperties(text));
    }

    public synchronized void setEncoding(String s) {
        checkAttributesAllowed();
        this.encoding = s;
    }

    public synchronized String getEncoding() {
        return this.encoding;
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized long getSize() {
        return isReference() ? getRef().getSize() : getContent().length();
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        return super.hashCode() * STRING_MAGIC;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        return String.valueOf(getContent());
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized InputStream getInputStream() throws IOException {
        if (isReference()) {
            return getRef().getInputStream();
        }
        String content = getContent();
        if (content == null) {
            throw new IllegalStateException("unset string value");
        }
        return new ByteArrayInputStream(this.encoding == null ? content.getBytes() : content.getBytes(this.encoding));
    }

    @Override // org.apache.tools.ant.types.Resource
    public synchronized OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        if (getValue() != null) {
            throw new ImmutableResourceException();
        }
        return new StringResourceFilterOutputStream();
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.encoding != "UTF-8") {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    protected synchronized String getContent() {
        return getValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public StringResource getRef() {
        return (StringResource) getCheckedRef(StringResource.class);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/StringResource$StringResourceFilterOutputStream.class */
    private class StringResourceFilterOutputStream extends FilterOutputStream {
        private final ByteArrayOutputStream baos;

        public StringResourceFilterOutputStream() {
            super(new ByteArrayOutputStream());
            this.baos = (ByteArrayOutputStream) this.out;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            String byteArrayOutputStream;
            super.close();
            if (StringResource.this.encoding != null) {
                byteArrayOutputStream = this.baos.toString(StringResource.this.encoding);
            } else {
                byteArrayOutputStream = this.baos.toString();
            }
            String result = byteArrayOutputStream;
            setValueFromOutputStream(result);
        }

        private void setValueFromOutputStream(String output) {
            String value;
            if (StringResource.this.getProject() != null) {
                value = StringResource.this.getProject().replaceProperties(output);
            } else {
                value = output;
            }
            StringResource.this.setValue(value);
        }
    }
}
