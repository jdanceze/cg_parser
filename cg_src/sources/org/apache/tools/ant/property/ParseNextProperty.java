package org.apache.tools.ant.property;

import java.text.ParsePosition;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/ParseNextProperty.class */
public interface ParseNextProperty {
    Project getProject();

    Object parseNextProperty(String str, ParsePosition parsePosition);
}
