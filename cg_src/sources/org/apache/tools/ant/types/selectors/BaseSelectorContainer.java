package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/BaseSelectorContainer.class */
public abstract class BaseSelectorContainer extends BaseSelector implements SelectorContainer {
    private List<FileSelector> selectorsList = Collections.synchronizedList(new ArrayList());

    @Override // org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public abstract boolean isSelected(File file, String str, File file2);

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public boolean hasSelectors() {
        dieOnCircularReference();
        return !this.selectorsList.isEmpty();
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public int selectorCount() {
        dieOnCircularReference();
        return this.selectorsList.size();
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public FileSelector[] getSelectors(Project p) {
        dieOnCircularReference();
        return (FileSelector[]) this.selectorsList.toArray(new FileSelector[this.selectorsList.size()]);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public Enumeration<FileSelector> selectorElements() {
        dieOnCircularReference();
        return Collections.enumeration(this.selectorsList);
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        dieOnCircularReference();
        return (String) this.selectorsList.stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(", "));
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void appendSelector(FileSelector selector) {
        this.selectorsList.add(selector);
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void validate() {
        verifySettings();
        dieOnCircularReference();
        String errmsg = getError();
        if (errmsg != null) {
            throw new BuildException(errmsg);
        }
        Stream<FileSelector> stream = this.selectorsList.stream();
        Objects.requireNonNull(BaseSelector.class);
        Stream<FileSelector> filter = stream.filter((v1) -> {
            return r1.isInstance(v1);
        });
        Objects.requireNonNull(BaseSelector.class);
        filter.map((v1) -> {
            return r1.cast(v1);
        }).forEach((v0) -> {
            v0.validate();
        });
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addSelector(SelectSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addAnd(AndSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addOr(OrSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addNot(NotSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addNone(NoneSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addMajority(MajoritySelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDate(DateSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addSize(SizeSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addFilename(FilenameSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addCustom(ExtendSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addContains(ContainsSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addPresent(PresentSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDepth(DepthSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDepend(DependSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDifferent(DifferentSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addType(TypeSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addContainsRegexp(ContainsRegexpSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addModified(ModifiedSelector selector) {
        appendSelector(selector);
    }

    public void addReadable(ReadableSelector r) {
        appendSelector(r);
    }

    public void addWritable(WritableSelector w) {
        appendSelector(w);
    }

    public void addExecutable(ExecutableSelector e) {
        appendSelector(e);
    }

    public void addSymlink(SymlinkSelector e) {
        appendSelector(e);
    }

    public void addOwnedBy(OwnedBySelector o) {
        appendSelector(o);
    }

    public void addPosixGroup(PosixGroupSelector o) {
        appendSelector(o);
    }

    public void addPosixPermissions(PosixPermissionsSelector o) {
        appendSelector(o);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void add(FileSelector selector) {
        appendSelector(selector);
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
        for (FileSelector fileSelector : this.selectorsList) {
            if (fileSelector instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) fileSelector, stk, p);
            }
        }
        setChecked(true);
    }
}
