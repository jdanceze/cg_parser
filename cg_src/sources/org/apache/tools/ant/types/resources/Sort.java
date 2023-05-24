package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Sort.class */
public class Sort extends BaseResourceCollectionWrapper {
    private DelegatedResourceComparator comp = new DelegatedResourceComparator();

    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionWrapper
    protected synchronized Collection<Resource> getCollection() {
        Stream<? extends Resource> stream = getResourceCollection().stream();
        Objects.requireNonNull(Resource.class);
        return (Collection) stream.map((v1) -> {
            return r1.cast(v1);
        }).sorted(this.comp).collect(Collectors.toList());
    }

    public synchronized void add(ResourceComparator c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.comp.add(c);
        FailFast.invalidate(this);
        setChecked(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper, org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        super.dieOnCircularReference(stk, p);
        if (!isReference()) {
            DataType.pushAndInvokeCircularReferenceCheck(this.comp, stk, p);
            setChecked(true);
        }
    }
}
