package org.apache.tools.ant.types;

import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.IdentityStack;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/DataType.class */
public abstract class DataType extends ProjectComponent implements Cloneable {
    @Deprecated
    protected Reference ref;
    @Deprecated
    protected boolean checked = true;

    public boolean isReference() {
        return this.ref != null;
    }

    public void setRefid(Reference ref) {
        this.ref = ref;
        this.checked = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDataTypeName() {
        return ComponentHelper.getElementName(getProject(), this, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dieOnCircularReference() {
        dieOnCircularReference(getProject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dieOnCircularReference(Project p) {
        if (this.checked || !isReference()) {
            return;
        }
        dieOnCircularReference(new IdentityStack(this), p);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dieOnCircularReference(Stack<Object> stack, Project project) throws BuildException {
        if (this.checked || !isReference()) {
            return;
        }
        Object o = this.ref.getReferencedObject(project);
        if (o instanceof DataType) {
            IdentityStack<Object> id = IdentityStack.getInstance(stack);
            if (id.contains(o)) {
                throw circularReference();
            }
            id.push(o);
            ((DataType) o).dieOnCircularReference(id, project);
            id.pop();
        }
        this.checked = true;
    }

    public static void invokeCircularReferenceCheck(DataType dt, Stack<Object> stk, Project p) {
        dt.dieOnCircularReference(stk, p);
    }

    public static void pushAndInvokeCircularReferenceCheck(DataType dt, Stack<Object> stk, Project p) {
        stk.push(dt);
        dt.dieOnCircularReference(stk, p);
        stk.pop();
    }

    @Deprecated
    protected <T> T getCheckedRef() {
        return (T) getCheckedRef(getProject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> T getCheckedRef(Class<T> requiredClass) {
        return (T) getCheckedRef(requiredClass, getDataTypeName(), getProject());
    }

    @Deprecated
    protected <T> T getCheckedRef(Project p) {
        return (T) getCheckedRef(getClass(), getDataTypeName(), p);
    }

    protected <T> T getCheckedRef(Class<T> requiredClass, String dataTypeName) {
        return (T) getCheckedRef(requiredClass, dataTypeName, getProject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> T getCheckedRef(Class<T> requiredClass, String dataTypeName, Project project) {
        if (project == null) {
            throw new BuildException("No Project specified");
        }
        dieOnCircularReference(project);
        T o = (T) this.ref.getReferencedObject(project);
        if (requiredClass.isAssignableFrom(o.getClass())) {
            return o;
        }
        log("Class " + displayName(o.getClass()) + " is not a subclass of " + displayName(requiredClass), 3);
        throw new BuildException(this.ref.getRefId() + " doesn't denote a " + dataTypeName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BuildException tooManyAttributes() {
        return new BuildException("You must not specify more than one attribute when using refid");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BuildException noChildrenAllowed() {
        return new BuildException("You must not specify nested elements when using refid");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BuildException circularReference() {
        return new BuildException("This data type contains a circular reference.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isChecked() {
        return this.checked;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Reference getRefid() {
        return this.ref;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkAttributesAllowed() {
        if (isReference()) {
            throw tooManyAttributes();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkChildrenAllowed() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
    }

    public String toString() {
        String d = getDescription();
        return d == null ? getDataTypeName() : getDataTypeName() + Instruction.argsep + d;
    }

    @Override // org.apache.tools.ant.ProjectComponent
    public Object clone() throws CloneNotSupportedException {
        DataType dt = (DataType) super.clone();
        dt.setDescription(getDescription());
        if (getRefid() != null) {
            dt.setRefid(getRefid());
        }
        dt.setChecked(isChecked());
        return dt;
    }

    private String displayName(Class<?> clazz) {
        return clazz.getName() + " (loaded via " + clazz.getClassLoader() + ")";
    }
}
