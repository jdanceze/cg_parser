package org.apache.tools.ant.filters;

import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Parameterizable;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/BaseParamFilterReader.class */
public abstract class BaseParamFilterReader extends BaseFilterReader implements Parameterizable {
    private Parameter[] parameters;

    public BaseParamFilterReader() {
    }

    public BaseParamFilterReader(Reader in) {
        super(in);
    }

    @Override // org.apache.tools.ant.types.Parameterizable
    public final void setParameters(Parameter... parameters) {
        this.parameters = parameters;
        setInitialized(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parameter[] getParameters() {
        return this.parameters;
    }
}
