package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/DefaultDefinitions.class */
public final class DefaultDefinitions {
    private static final String IF_NAMESPACE = "ant:if";
    private static final String UNLESS_NAMESPACE = "ant:unless";
    private final ComponentHelper componentHelper;

    public DefaultDefinitions(ComponentHelper componentHelper) {
        this.componentHelper = componentHelper;
    }

    public void execute() {
        attributeNamespaceDef(IF_NAMESPACE);
        attributeNamespaceDef(UNLESS_NAMESPACE);
        ifUnlessDef("true", "IfTrueAttribute");
        ifUnlessDef("set", "IfSetAttribute");
        ifUnlessDef("blank", "IfBlankAttribute");
    }

    private void attributeNamespaceDef(String ns) {
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(ProjectHelper.nsToComponentName(ns));
        def.setClassName("org.apache.tools.ant.attribute.AttributeNamespace");
        def.setClassLoader(getClass().getClassLoader());
        def.setRestrict(true);
        this.componentHelper.addDataTypeDefinition(def);
    }

    private void ifUnlessDef(String name, String base) {
        String classname = "org.apache.tools.ant.attribute." + base;
        componentDef(IF_NAMESPACE, name, classname);
        componentDef(UNLESS_NAMESPACE, name, classname + "$Unless");
    }

    private void componentDef(String ns, String name, String classname) {
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(ProjectHelper.genComponentName(ns, name));
        def.setClassName(classname);
        def.setClassLoader(getClass().getClassLoader());
        def.setRestrict(true);
        this.componentHelper.addDataTypeDefinition(def);
    }
}
