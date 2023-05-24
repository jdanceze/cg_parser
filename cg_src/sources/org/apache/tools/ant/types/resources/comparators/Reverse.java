package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;
import java.util.Optional;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/Reverse.class */
public class Reverse extends ResourceComparator {
    private static final String ONE_NESTED = "You must not nest more than one ResourceComparator for reversal.";
    private ResourceComparator nested;

    public Reverse() {
    }

    public Reverse(ResourceComparator c) {
        add(c);
    }

    public void add(ResourceComparator c) {
        if (this.nested != null) {
            throw new BuildException(ONE_NESTED);
        }
        this.nested = c;
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected int resourceCompare(Resource foo, Resource bar) {
        return ((Comparator) Optional.ofNullable(this.nested).orElseGet(Comparator::naturalOrder)).reversed().compare(foo, bar);
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
        if (this.nested != null) {
            pushAndInvokeCircularReferenceCheck(this.nested, stk, p);
        }
        setChecked(true);
    }
}
