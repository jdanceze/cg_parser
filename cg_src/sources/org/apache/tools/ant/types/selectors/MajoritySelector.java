package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/MajoritySelector.class */
public class MajoritySelector extends BaseSelectorContainer {
    private boolean allowtie = true;

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (hasSelectors()) {
            buf.append("{majorityselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    public void setAllowtie(boolean tiebreaker) {
        this.allowtie = tiebreaker;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        int yesvotes = 0;
        int novotes = 0;
        Iterator it = Collections.list(selectorElements()).iterator();
        while (it.hasNext()) {
            FileSelector fs = (FileSelector) it.next();
            if (fs.isSelected(basedir, filename, file)) {
                yesvotes++;
            } else {
                novotes++;
            }
        }
        if (yesvotes > novotes) {
            return true;
        }
        if (novotes > yesvotes) {
            return false;
        }
        return this.allowtie;
    }
}
