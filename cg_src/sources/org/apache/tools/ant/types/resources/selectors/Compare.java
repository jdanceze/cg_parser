package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Quantifier;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Compare.class */
public class Compare extends DataType implements ResourceSelector {
    private DelegatedResourceComparator comp = new DelegatedResourceComparator();
    private Quantifier against = Quantifier.ALL;
    private Comparison when = Comparison.EQUAL;
    private Union control;

    public synchronized void add(ResourceComparator c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.comp.add(c);
        setChecked(false);
    }

    public synchronized void setAgainst(Quantifier against) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.against = against;
    }

    public synchronized void setWhen(Comparison when) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.when = when;
    }

    public synchronized ResourceCollection createControl() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.control != null) {
            throw oneControl();
        }
        this.control = new Union();
        setChecked(false);
        return this.control;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public synchronized boolean isSelected(Resource r) {
        if (isReference()) {
            return getRef().isSelected(r);
        }
        if (this.control == null) {
            throw oneControl();
        }
        dieOnCircularReference();
        int t = 0;
        int f = 0;
        Iterator<Resource> it = this.control.iterator();
        while (it.hasNext()) {
            Resource res = it.next();
            if (this.when.evaluate(this.comp.compare(r, res))) {
                t++;
            } else {
                f++;
            }
        }
        return this.against.evaluate(t, f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        if (this.control != null) {
            DataType.pushAndInvokeCircularReferenceCheck(this.control, stk, p);
        }
        DataType.pushAndInvokeCircularReferenceCheck(this.comp, stk, p);
        setChecked(true);
    }

    private ResourceSelector getRef() {
        return (ResourceSelector) getCheckedRef(ResourceSelector.class);
    }

    private BuildException oneControl() {
        return new BuildException("%s the <control> element should be specified exactly once.", super.toString());
    }
}
