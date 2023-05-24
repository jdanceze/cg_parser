package org.apache.tools.ant.property;

import java.text.ParsePosition;
import org.apache.tools.ant.PropertyHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/PropertyExpander.class */
public interface PropertyExpander extends PropertyHelper.Delegate {
    String parsePropertyName(String str, ParsePosition parsePosition, ParseNextProperty parseNextProperty);
}
