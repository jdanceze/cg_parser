package org.apache.tools.ant.taskdefs.optional.ejb;

import javax.xml.parsers.SAXParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EJBDeploymentTool.class */
public interface EJBDeploymentTool {
    void processDescriptor(String str, SAXParser sAXParser) throws BuildException;

    void validateConfigured() throws BuildException;

    void setTask(Task task);

    void configure(EjbJar.Config config);
}
