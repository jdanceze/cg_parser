package org.apache.tools.ant.util.facade;

import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/facade/ImplementationSpecificArgument.class */
public class ImplementationSpecificArgument extends Commandline.Argument {
    private String impl;

    public void setImplementation(String impl) {
        this.impl = impl;
    }

    public final String[] getParts(String chosenImpl) {
        if (this.impl == null || this.impl.equals(chosenImpl)) {
            return super.getParts();
        }
        return new String[0];
    }
}
