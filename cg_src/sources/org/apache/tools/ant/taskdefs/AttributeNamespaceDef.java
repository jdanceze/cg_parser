package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.attribute.AttributeNamespace;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AttributeNamespaceDef.class */
public final class AttributeNamespaceDef extends AntlibDefinition {
    @Override // org.apache.tools.ant.Task
    public void execute() {
        String componentName = ProjectHelper.nsToComponentName(getURI());
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(componentName);
        def.setClassName(AttributeNamespace.class.getName());
        def.setClass(AttributeNamespace.class);
        def.setRestrict(true);
        def.setClassLoader(AttributeNamespace.class.getClassLoader());
        ComponentHelper.getComponentHelper(getProject()).addDataTypeDefinition(def);
    }
}
