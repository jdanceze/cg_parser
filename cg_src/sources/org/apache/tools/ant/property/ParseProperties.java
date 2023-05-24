package org.apache.tools.ant.property;

import java.text.ParsePosition;
import java.util.Collection;
import java.util.Objects;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/ParseProperties.class */
public class ParseProperties implements ParseNextProperty {
    private final Project project;
    private final GetProperty getProperty;
    private final Collection<PropertyExpander> expanders;

    public ParseProperties(Project project, Collection<PropertyExpander> expanders, GetProperty getProperty) {
        this.project = project;
        this.expanders = expanders;
        this.getProperty = getProperty;
    }

    @Override // org.apache.tools.ant.property.ParseNextProperty
    public Project getProject() {
        return this.project;
    }

    public Object parseProperties(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        int len = value.length();
        ParsePosition pos = new ParsePosition(0);
        Object o = parseNextProperty(value, pos);
        if (o != null && pos.getIndex() >= len) {
            return o;
        }
        StringBuilder sb = new StringBuilder(len * 2);
        if (o == null) {
            sb.append(value.charAt(pos.getIndex()));
            pos.setIndex(pos.getIndex() + 1);
        } else {
            sb.append(o);
        }
        while (pos.getIndex() < len) {
            Object o2 = parseNextProperty(value, pos);
            if (o2 == null) {
                sb.append(value.charAt(pos.getIndex()));
                pos.setIndex(pos.getIndex() + 1);
            } else {
                sb.append(o2);
            }
        }
        return sb.toString();
    }

    public boolean containsProperties(String value) {
        if (value == null) {
            return false;
        }
        int len = value.length();
        ParsePosition pos = new ParsePosition(0);
        while (pos.getIndex() < len) {
            if (parsePropertyName(value, pos) != null) {
                return true;
            }
            pos.setIndex(pos.getIndex() + 1);
        }
        return false;
    }

    @Override // org.apache.tools.ant.property.ParseNextProperty
    public Object parseNextProperty(String value, ParsePosition pos) {
        String propertyName;
        int start = pos.getIndex();
        if (start <= value.length() && (propertyName = parsePropertyName(value, pos)) != null) {
            Object result = getProperty(propertyName);
            if (result != null) {
                return result;
            }
            if (this.project != null) {
                this.project.log("Property \"" + propertyName + "\" has not been set", 3);
            }
            return value.substring(start, pos.getIndex());
        }
        return null;
    }

    private String parsePropertyName(String value, ParsePosition pos) {
        return (String) this.expanders.stream().map(xp -> {
            return pos.parsePropertyName(value, value, this);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).findFirst().orElse(null);
    }

    private Object getProperty(String propertyName) {
        return this.getProperty.getProperty(propertyName);
    }
}
