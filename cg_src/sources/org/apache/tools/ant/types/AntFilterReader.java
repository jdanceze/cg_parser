package org.apache.tools.ant.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/AntFilterReader.class */
public final class AntFilterReader extends DataType {
    private String className;
    private final List<Parameter> parameters = new ArrayList();
    private Path classpath;

    public void setClassName(String className) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.className = className;
    }

    public String getClassName() {
        if (isReference()) {
            return getRef().getClassName();
        }
        dieOnCircularReference();
        return this.className;
    }

    public void addParam(Parameter param) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.parameters.add(param);
    }

    public void setClasspath(Path classpath) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
        setChecked(false);
    }

    public Path createClasspath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        setChecked(false);
        return this.classpath.createPath();
    }

    public Path getClasspath() {
        if (isReference()) {
            getRef().getClasspath();
        }
        dieOnCircularReference();
        return this.classpath;
    }

    public void setClasspathRef(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createClasspath().setRefid(r);
    }

    public Parameter[] getParams() {
        if (isReference()) {
            getRef().getParams();
        }
        dieOnCircularReference();
        return (Parameter[]) this.parameters.toArray(new Parameter[this.parameters.size()]);
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (!this.parameters.isEmpty() || this.className != null || this.classpath != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
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
        if (this.classpath != null) {
            pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
        }
        setChecked(true);
    }

    private AntFilterReader getRef() {
        return (AntFilterReader) getCheckedRef(AntFilterReader.class);
    }
}
