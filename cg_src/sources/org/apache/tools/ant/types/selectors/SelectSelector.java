package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SelectSelector.class */
public class SelectSelector extends BaseSelectorContainer {
    private Object ifCondition;
    private Object unlessCondition;

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (hasSelectors()) {
            buf.append("{select");
            if (this.ifCondition != null) {
                buf.append(" if: ");
                buf.append(this.ifCondition);
            }
            if (this.unlessCondition != null) {
                buf.append(" unless: ");
                buf.append(this.unlessCondition);
            }
            buf.append(Instruction.argsep);
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    private SelectSelector getRef() {
        return (SelectSelector) getCheckedRef(SelectSelector.class);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public boolean hasSelectors() {
        if (isReference()) {
            return getRef().hasSelectors();
        }
        return super.hasSelectors();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public int selectorCount() {
        if (isReference()) {
            return getRef().selectorCount();
        }
        return super.selectorCount();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public FileSelector[] getSelectors(Project p) {
        if (isReference()) {
            return getRef().getSelectors(p);
        }
        return super.getSelectors(p);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public Enumeration<FileSelector> selectorElements() {
        if (isReference()) {
            return getRef().selectorElements();
        }
        return super.selectorElements();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public void appendSelector(FileSelector selector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        super.appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        int cnt = selectorCount();
        if (cnt < 0 || cnt > 1) {
            setError("Only one selector is allowed within the <selector> tag");
        }
    }

    public boolean passesConditions() {
        PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
        return ph.testIfCondition(this.ifCondition) && ph.testUnlessCondition(this.unlessCondition);
    }

    public void setIf(Object ifProperty) {
        this.ifCondition = ifProperty;
    }

    public void setIf(String ifProperty) {
        setIf((Object) ifProperty);
    }

    public void setUnless(Object unlessProperty) {
        this.unlessCondition = unlessProperty;
    }

    public void setUnless(String unlessProperty) {
        setUnless((Object) unlessProperty);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        if (!passesConditions()) {
            return false;
        }
        Enumeration<FileSelector> e = selectorElements();
        return !e.hasMoreElements() || e.nextElement().isSelected(basedir, filename, file);
    }
}
