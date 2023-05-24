package org.apache.tools.ant.types.optional.depend;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/optional/depend/ClassfileSet.class */
public class ClassfileSet extends FileSet {
    private List<String> rootClasses;
    private List<FileSet> rootFileSets;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/optional/depend/ClassfileSet$ClassRoot.class */
    public static class ClassRoot {
        private String rootClass;

        public void setClassname(String name) {
            this.rootClass = name;
        }

        public String getClassname() {
            return this.rootClass;
        }
    }

    public ClassfileSet() {
        this.rootClasses = new ArrayList();
        this.rootFileSets = new ArrayList();
    }

    protected ClassfileSet(ClassfileSet s) {
        super(s);
        this.rootClasses = new ArrayList();
        this.rootFileSets = new ArrayList();
        this.rootClasses.addAll(s.rootClasses);
    }

    public void addRootFileset(FileSet rootFileSet) {
        this.rootFileSets.add(rootFileSet);
        setChecked(false);
    }

    public void setRootClass(String rootClass) {
        this.rootClasses.add(rootClass);
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet
    public DirectoryScanner getDirectoryScanner(Project p) {
        String[] includedFiles;
        if (isReference()) {
            return getRef(p).getDirectoryScanner(p);
        }
        dieOnCircularReference(p);
        DirectoryScanner parentScanner = super.getDirectoryScanner(p);
        DependScanner scanner = new DependScanner(parentScanner);
        Vector<String> allRootClasses = new Vector<>(this.rootClasses);
        for (FileSet additionalRootSet : this.rootFileSets) {
            DirectoryScanner additionalScanner = additionalRootSet.getDirectoryScanner(p);
            for (String file : additionalScanner.getIncludedFiles()) {
                if (file.endsWith(".class")) {
                    String classFilePath = StringUtils.removeSuffix(file, ".class");
                    String className = classFilePath.replace('/', '.').replace('\\', '.');
                    allRootClasses.addElement(className);
                }
            }
            scanner.addBasedir(additionalRootSet.getDir(p));
        }
        scanner.setBasedir(getDir(p));
        scanner.setRootClasses(allRootClasses);
        scanner.scan();
        return scanner;
    }

    public void addConfiguredRoot(ClassRoot root) {
        this.rootClasses.add(root.getClassname());
    }

    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        return new ClassfileSet(isReference() ? getRef() : this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
        if (isChecked()) {
            return;
        }
        super.dieOnCircularReference(stk, p);
        if (!isReference()) {
            for (FileSet additionalRootSet : this.rootFileSets) {
                pushAndInvokeCircularReferenceCheck(additionalRootSet, stk, p);
            }
            setChecked(true);
        }
    }

    private ClassfileSet getRef() {
        return (ClassfileSet) getCheckedRef(ClassfileSet.class);
    }
}
