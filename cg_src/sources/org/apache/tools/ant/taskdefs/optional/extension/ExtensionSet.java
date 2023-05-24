package org.apache.tools.ant.taskdefs.optional.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/ExtensionSet.class */
public class ExtensionSet extends DataType {
    private final List<ExtensionAdapter> extensions = new ArrayList();
    private final List<FileSet> extensionsFilesets = new ArrayList();

    public void addExtension(ExtensionAdapter extensionAdapter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.extensions.add(extensionAdapter);
    }

    public void addLibfileset(LibFileSet fileSet) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.extensionsFilesets.add(fileSet);
    }

    public void addFileset(FileSet fileSet) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.extensionsFilesets.add(fileSet);
    }

    public Extension[] toExtensions(Project proj) throws BuildException {
        if (isReference()) {
            return getRef().toExtensions(proj);
        }
        dieOnCircularReference();
        List<Extension> extensionsList = ExtensionUtil.toExtensions(this.extensions);
        ExtensionUtil.extractExtensions(proj, extensionsList, this.extensionsFilesets);
        return (Extension[]) extensionsList.toArray(new Extension[extensionsList.size()]);
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference reference) throws BuildException {
        if (!this.extensions.isEmpty() || !this.extensionsFilesets.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(reference);
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
        for (ExtensionAdapter extensionAdapter : this.extensions) {
            pushAndInvokeCircularReferenceCheck(extensionAdapter, stk, p);
        }
        for (FileSet fileSet : this.extensionsFilesets) {
            pushAndInvokeCircularReferenceCheck(fileSet, stk, p);
        }
        setChecked(true);
    }

    private ExtensionSet getRef() {
        return (ExtensionSet) getCheckedRef(ExtensionSet.class);
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return "ExtensionSet" + Arrays.asList(toExtensions(getProject()));
    }
}
