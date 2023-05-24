package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PropertyHelperTask.class */
public class PropertyHelperTask extends Task {
    private PropertyHelper propertyHelper;
    private List<Object> delegates;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PropertyHelperTask$DelegateElement.class */
    public final class DelegateElement {
        private String refid;

        private DelegateElement() {
        }

        public String getRefid() {
            return this.refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public PropertyHelper.Delegate resolve() {
            if (this.refid == null) {
                throw new BuildException("refid required for generic delegate");
            }
            return (PropertyHelper.Delegate) PropertyHelperTask.this.getProject().getReference(this.refid);
        }
    }

    public synchronized void addConfigured(PropertyHelper propertyHelper) {
        if (this.propertyHelper != null) {
            throw new BuildException("Only one PropertyHelper can be installed");
        }
        this.propertyHelper = propertyHelper;
    }

    public synchronized void addConfigured(PropertyHelper.Delegate delegate) {
        getAddDelegateList().add(delegate);
    }

    public DelegateElement createDelegate() {
        DelegateElement result = new DelegateElement();
        getAddDelegateList().add(result);
        return result;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        PropertyHelper ph;
        if (getProject() == null) {
            throw new BuildException("Project instance not set");
        }
        if (this.propertyHelper == null && this.delegates == null) {
            throw new BuildException("Either a new PropertyHelper or one or more PropertyHelper delegates are required");
        }
        PropertyHelper ph2 = this.propertyHelper;
        if (ph2 == null) {
            ph = PropertyHelper.getPropertyHelper(getProject());
        } else {
            ph = this.propertyHelper;
        }
        synchronized (ph) {
            if (this.delegates != null) {
                for (Object o : this.delegates) {
                    PropertyHelper.Delegate delegate = o instanceof DelegateElement ? ((DelegateElement) o).resolve() : (PropertyHelper.Delegate) o;
                    log("Adding PropertyHelper delegate " + delegate, 4);
                    ph.add(delegate);
                }
            }
        }
        if (this.propertyHelper != null) {
            log("Installing PropertyHelper " + this.propertyHelper, 4);
            getProject().addReference(MagicNames.REFID_PROPERTY_HELPER, this.propertyHelper);
        }
    }

    private synchronized List<Object> getAddDelegateList() {
        if (this.delegates == null) {
            this.delegates = new ArrayList();
        }
        return this.delegates;
    }
}
