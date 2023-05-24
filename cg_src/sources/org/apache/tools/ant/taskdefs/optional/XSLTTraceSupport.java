package org.apache.tools.ant.taskdefs.optional;

import javax.xml.transform.Transformer;
import org.apache.tools.ant.taskdefs.XSLTProcess;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/XSLTTraceSupport.class */
public interface XSLTTraceSupport {
    void configureTrace(Transformer transformer, XSLTProcess.TraceConfiguration traceConfiguration);
}
