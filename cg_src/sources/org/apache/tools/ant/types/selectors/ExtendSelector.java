package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/ExtendSelector.class */
public class ExtendSelector extends BaseSelector {
    private String classname = null;
    private FileSelector dynselector = null;
    private List<Parameter> parameters = Collections.synchronizedList(new ArrayList());
    private Path classpath = null;

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void selectorCreate() {
        Class<?> c;
        if (this.classname == null || this.classname.isEmpty()) {
            setError("There is no classname specified");
            return;
        }
        try {
            if (this.classpath == null) {
                c = Class.forName(this.classname);
            } else {
                AntClassLoader al = getProject().createClassLoader(this.classpath);
                c = Class.forName(this.classname, true, al);
            }
            this.dynselector = (FileSelector) c.asSubclass(FileSelector.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Project p = getProject();
            if (p != null) {
                p.setProjectReference(this.dynselector);
            }
        } catch (ClassNotFoundException e) {
            setError("Selector " + this.classname + " not initialized, no such class");
        } catch (IllegalAccessException e2) {
            setError("Selector " + this.classname + " not initialized, class not accessible");
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e3) {
            setError("Selector " + this.classname + " not initialized, could not create class");
        }
    }

    public void addParam(Parameter p) {
        this.parameters.add(p);
    }

    public final void setClasspath(Path classpath) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    public final Path createClasspath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public final Path getClasspath() {
        return this.classpath;
    }

    public void setClasspathref(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createClasspath().setRefid(r);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.dynselector == null) {
            selectorCreate();
        }
        if (this.classname == null || this.classname.length() < 1) {
            setError("The classname attribute is required");
        } else if (this.dynselector == null) {
            setError("Internal Error: The custom selector was not created");
        } else if (!(this.dynselector instanceof ExtendFileSelector) && !this.parameters.isEmpty()) {
            setError("Cannot set parameters on custom selector that does not implement ExtendFileSelector");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) throws BuildException {
        validate();
        if (!this.parameters.isEmpty() && (this.dynselector instanceof ExtendFileSelector)) {
            ((ExtendFileSelector) this.dynselector).setParameters((Parameter[]) this.parameters.toArray(new Parameter[this.parameters.size()]));
        }
        return this.dynselector.isSelected(basedir, filename, file);
    }
}
