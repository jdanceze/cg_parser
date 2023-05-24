package org.apache.tools.ant.property;

import java.util.Set;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/LocalProperties.class */
public class LocalProperties extends InheritableThreadLocal<LocalPropertyStack> implements PropertyHelper.PropertyEvaluator, PropertyHelper.PropertySetter, PropertyHelper.PropertyEnumerator {
    public static synchronized LocalProperties get(Project project) {
        LocalProperties l = (LocalProperties) project.getReference(MagicNames.REFID_LOCAL_PROPERTIES);
        if (l == null) {
            l = new LocalProperties();
            project.addReference(MagicNames.REFID_LOCAL_PROPERTIES, l);
            PropertyHelper.getPropertyHelper(project).add(l);
        }
        return l;
    }

    private LocalProperties() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.ThreadLocal
    public synchronized LocalPropertyStack initialValue() {
        return new LocalPropertyStack();
    }

    public void addLocal(String property) {
        ((LocalPropertyStack) get()).addLocal(property);
    }

    public void enterScope() {
        ((LocalPropertyStack) get()).enterScope();
    }

    public void exitScope() {
        ((LocalPropertyStack) get()).exitScope();
    }

    public void copy() {
        set(((LocalPropertyStack) get()).copy());
    }

    @Override // org.apache.tools.ant.PropertyHelper.PropertyEvaluator
    public Object evaluate(String property, PropertyHelper helper) {
        return ((LocalPropertyStack) get()).evaluate(property, helper);
    }

    @Override // org.apache.tools.ant.PropertyHelper.PropertySetter
    public boolean setNew(String property, Object value, PropertyHelper propertyHelper) {
        return ((LocalPropertyStack) get()).setNew(property, value, propertyHelper);
    }

    @Override // org.apache.tools.ant.PropertyHelper.PropertySetter
    public boolean set(String property, Object value, PropertyHelper propertyHelper) {
        return ((LocalPropertyStack) get()).set(property, value, propertyHelper);
    }

    @Override // org.apache.tools.ant.PropertyHelper.PropertyEnumerator
    public Set<String> getPropertyNames() {
        return ((LocalPropertyStack) get()).getPropertyNames();
    }
}
