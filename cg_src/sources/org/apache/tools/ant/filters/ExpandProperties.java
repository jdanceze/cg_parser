package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.property.GetProperty;
import org.apache.tools.ant.property.ParseProperties;
import org.apache.tools.ant.types.PropertySet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/ExpandProperties.class */
public final class ExpandProperties extends BaseFilterReader implements ChainableReader {
    private static final int EOF = -1;
    private char[] buffer;
    private int index;
    private PropertySet propertySet;

    public ExpandProperties() {
    }

    public ExpandProperties(Reader in) {
        super(in);
    }

    public void add(PropertySet propertySet) {
        if (this.propertySet != null) {
            throw new BuildException("expandproperties filter accepts only one propertyset");
        }
        this.propertySet = propertySet;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        GetProperty getProperty;
        if (this.index > -1) {
            if (this.buffer == null) {
                String data = readFully();
                Project project = getProject();
                if (this.propertySet == null) {
                    getProperty = PropertyHelper.getPropertyHelper(project);
                } else {
                    Properties properties = this.propertySet.getProperties();
                    Objects.requireNonNull(properties);
                    getProperty = this::getProperty;
                }
                Object expanded = new ParseProperties(project, PropertyHelper.getPropertyHelper(project).getExpanders(), getProperty).parseProperties(data);
                this.buffer = expanded == null ? new char[0] : expanded.toString().toCharArray();
            }
            if (this.index < this.buffer.length) {
                char[] cArr = this.buffer;
                int i = this.index;
                this.index = i + 1;
                return cArr[i];
            }
            this.index = -1;
            return -1;
        }
        return -1;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        ExpandProperties newFilter = new ExpandProperties(rdr);
        newFilter.setProject(getProject());
        newFilter.add(this.propertySet);
        return newFilter;
    }
}
