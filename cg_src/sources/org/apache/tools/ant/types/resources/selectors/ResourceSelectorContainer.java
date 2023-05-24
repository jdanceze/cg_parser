package org.apache.tools.ant.types.resources.selectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/ResourceSelectorContainer.class */
public class ResourceSelectorContainer extends DataType {
    private final List<ResourceSelector> resourceSelectors = new ArrayList();

    public ResourceSelectorContainer() {
    }

    public ResourceSelectorContainer(ResourceSelector... resourceSelectors) {
        for (ResourceSelector rsel : resourceSelectors) {
            add(rsel);
        }
    }

    public void add(ResourceSelector s) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (s == null) {
            return;
        }
        this.resourceSelectors.add(s);
        setChecked(false);
    }

    public boolean hasSelectors() {
        if (isReference()) {
            return getRef().hasSelectors();
        }
        dieOnCircularReference();
        return !this.resourceSelectors.isEmpty();
    }

    public int selectorCount() {
        if (isReference()) {
            return getRef().selectorCount();
        }
        dieOnCircularReference();
        return this.resourceSelectors.size();
    }

    public Iterator<ResourceSelector> getSelectors() {
        if (isReference()) {
            return getRef().getSelectors();
        }
        return getResourceSelectors().iterator();
    }

    public List<ResourceSelector> getResourceSelectors() {
        if (isReference()) {
            return getRef().getResourceSelectors();
        }
        dieOnCircularReference();
        return Collections.unmodifiableList(this.resourceSelectors);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        for (ResourceSelector resourceSelector : this.resourceSelectors) {
            if (resourceSelector instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) resourceSelector, stk, p);
            }
        }
        setChecked(true);
    }

    private ResourceSelectorContainer getRef() {
        return (ResourceSelectorContainer) getCheckedRef(ResourceSelectorContainer.class);
    }
}
