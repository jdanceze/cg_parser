package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/DelegatedResourceComparator.class */
public class DelegatedResourceComparator extends ResourceComparator {
    private List<ResourceComparator> resourceComparators = null;

    public synchronized void add(ResourceComparator c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        this.resourceComparators = this.resourceComparators == null ? new Vector<>() : this.resourceComparators;
        this.resourceComparators.add(c);
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator, java.util.Comparator
    public synchronized boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (isReference()) {
            return getRef().equals(o);
        }
        if (o instanceof DelegatedResourceComparator) {
            List<ResourceComparator> ov = ((DelegatedResourceComparator) o).resourceComparators;
            return this.resourceComparators == null ? ov == null : this.resourceComparators.equals(ov);
        }
        return false;
    }

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    public synchronized int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        if (this.resourceComparators == null) {
            return 0;
        }
        return this.resourceComparators.hashCode();
    }

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected synchronized int resourceCompare(Resource foo, Resource bar) {
        return composite(this.resourceComparators).compare(foo, bar);
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
        if (this.resourceComparators != null && !this.resourceComparators.isEmpty()) {
            for (ResourceComparator resourceComparator : this.resourceComparators) {
                if (resourceComparator instanceof DataType) {
                    pushAndInvokeCircularReferenceCheck(resourceComparator, stk, p);
                }
            }
        }
        setChecked(true);
    }

    private DelegatedResourceComparator getRef() {
        return (DelegatedResourceComparator) getCheckedRef(DelegatedResourceComparator.class);
    }

    private static Comparator<Resource> composite(List<? extends Comparator<Resource>> foo) {
        Comparator<Resource> result = null;
        if (foo != null) {
            for (Comparator<Resource> comparator : foo) {
                if (result == null) {
                    result = comparator;
                } else {
                    result = result.thenComparing(comparator);
                }
            }
        }
        return result == null ? Comparator.naturalOrder() : result;
    }
}
