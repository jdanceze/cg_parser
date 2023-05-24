package org.apache.tools.ant.types.selectors;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/DependSelector.class */
public class DependSelector extends MappingSelector {
    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder("{dependselector targetdir: ");
        if (this.targetdir == null) {
            buf.append("NOT YET SET");
        } else {
            buf.append(this.targetdir.getName());
        }
        buf.append(" granularity: ").append(this.granularity);
        if (this.map != null) {
            buf.append(" mapper: ");
            buf.append(this.map.toString());
        } else if (this.mapperElement != null) {
            buf.append(" mapper: ");
            buf.append(this.mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
    }

    @Override // org.apache.tools.ant.types.selectors.MappingSelector
    public boolean selectionTest(File srcfile, File destfile) {
        return SelectorUtils.isOutOfDate(srcfile, destfile, this.granularity);
    }
}
