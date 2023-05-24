package org.apache.tools.ant.util;

import java.io.ByteArrayOutputStream;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/PropertyOutputStream.class */
public class PropertyOutputStream extends ByteArrayOutputStream {
    private Project project;
    private String property;
    private boolean trim;

    public PropertyOutputStream(Project p, String s) {
        this(p, s, true);
    }

    public PropertyOutputStream(Project p, String s, boolean b) {
        this.project = p;
        this.property = s;
        this.trim = b;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.project != null && this.property != null) {
            String s = new String(toByteArray());
            this.project.setNewProperty(this.property, this.trim ? s.trim() : s);
        }
    }
}
