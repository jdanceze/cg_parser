package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DefBase.class */
public abstract class DefBase extends AntlibDefinition {
    private ClassLoader createdLoader;
    private ClasspathUtils.Delegate cpDelegate;

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasCpDelegate() {
        return this.cpDelegate != null;
    }

    @Deprecated
    public void setReverseLoader(boolean reverseLoader) {
        getDelegate().setReverseLoader(reverseLoader);
        log("The reverseloader attribute is DEPRECATED. It will be removed", 1);
    }

    public Path getClasspath() {
        return getDelegate().getClasspath();
    }

    public boolean isReverseLoader() {
        return getDelegate().isReverseLoader();
    }

    public String getLoaderId() {
        return getDelegate().getClassLoadId();
    }

    public String getClasspathId() {
        return getDelegate().getClassLoadId();
    }

    public void setClasspath(Path classpath) {
        getDelegate().setClasspath(classpath);
    }

    public Path createClasspath() {
        return getDelegate().createClasspath();
    }

    public void setClasspathRef(Reference r) {
        getDelegate().setClasspathref(r);
    }

    public void setLoaderRef(Reference r) {
        getDelegate().setLoaderRef(r);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassLoader createLoader() {
        if (getAntlibClassLoader() != null && this.cpDelegate == null) {
            return getAntlibClassLoader();
        }
        if (this.createdLoader == null) {
            this.createdLoader = getDelegate().getClassLoader();
            ((AntClassLoader) this.createdLoader).addSystemPackageRoot(MagicNames.ANT_CORE_PACKAGE);
        }
        return this.createdLoader;
    }

    @Override // org.apache.tools.ant.Task
    public void init() throws BuildException {
        super.init();
    }

    private ClasspathUtils.Delegate getDelegate() {
        if (this.cpDelegate == null) {
            this.cpDelegate = ClasspathUtils.getDelegate(this);
        }
        return this.cpDelegate;
    }
}
