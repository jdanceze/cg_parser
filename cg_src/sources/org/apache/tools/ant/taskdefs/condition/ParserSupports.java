package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/ParserSupports.class */
public class ParserSupports extends ProjectComponent implements Condition {
    public static final String ERROR_BOTH_ATTRIBUTES = "Property and feature attributes are exclusive";
    public static final String FEATURE = "feature";
    public static final String PROPERTY = "property";
    public static final String NOT_RECOGNIZED = " not recognized: ";
    public static final String NOT_SUPPORTED = " not supported: ";
    public static final String ERROR_NO_ATTRIBUTES = "Neither feature or property are set";
    public static final String ERROR_NO_VALUE = "A value is needed when testing for property support";
    private String feature;
    private String property;
    private String value;

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.feature != null && this.property != null) {
            throw new BuildException(ERROR_BOTH_ATTRIBUTES);
        }
        if (this.feature == null && this.property == null) {
            throw new BuildException(ERROR_NO_ATTRIBUTES);
        }
        if (this.feature != null) {
            return evalFeature();
        }
        if (this.value == null) {
            throw new BuildException(ERROR_NO_VALUE);
        }
        return evalProperty();
    }

    private XMLReader getReader() {
        JAXPUtils.getParser();
        return JAXPUtils.getXMLReader();
    }

    public boolean evalFeature() {
        XMLReader reader = getReader();
        if (this.value == null) {
            this.value = "true";
        }
        boolean v = Project.toBoolean(this.value);
        try {
            reader.setFeature(this.feature, v);
            return true;
        } catch (SAXNotRecognizedException e) {
            log("feature not recognized: " + this.feature, 3);
            return false;
        } catch (SAXNotSupportedException e2) {
            log("feature not supported: " + this.feature, 3);
            return false;
        }
    }

    public boolean evalProperty() {
        XMLReader reader = getReader();
        try {
            reader.setProperty(this.property, this.value);
            return true;
        } catch (SAXNotRecognizedException e) {
            log("property not recognized: " + this.property, 3);
            return false;
        } catch (SAXNotSupportedException e2) {
            log("property not supported: " + this.property, 3);
            return false;
        }
    }
}
