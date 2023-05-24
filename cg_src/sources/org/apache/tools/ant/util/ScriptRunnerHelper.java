package org.apache.tools.ant.util;

import java.io.File;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.ClasspathUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ScriptRunnerHelper.class */
public class ScriptRunnerHelper {
    private File srcFile;
    private String encoding;
    private String language;
    private String text;
    private ProjectComponent projectComponent;
    private ClasspathUtils.Delegate cpDelegate = null;
    private String manager = "auto";
    private boolean compiled = false;
    private boolean setBeans = true;
    private ClassLoader scriptLoader = null;
    private Union resources = new Union();

    public void setProjectComponent(ProjectComponent component) {
        this.projectComponent = component;
    }

    public ScriptRunnerBase getScriptRunner() {
        ScriptRunnerBase runner = getRunner();
        runner.setCompiled(this.compiled);
        if (this.encoding != null) {
            runner.setEncoding(this.encoding);
        }
        if (this.srcFile != null) {
            runner.setSrc(this.srcFile);
        }
        if (this.text != null) {
            runner.addText(this.text);
        }
        if (this.resources != null) {
            runner.loadResources(this.resources);
        }
        if (this.setBeans) {
            runner.bindToComponent(this.projectComponent);
        } else {
            runner.bindToComponentMinimum(this.projectComponent);
        }
        return runner;
    }

    public Path createClasspath() {
        return getClassPathDelegate().createClasspath();
    }

    public void setClasspath(Path classpath) {
        getClassPathDelegate().setClasspath(classpath);
    }

    public void setClasspathRef(Reference r) {
        getClassPathDelegate().setClasspathref(r);
    }

    public void setSrc(File file) {
        this.srcFile = file;
    }

    public File getSrc() {
        return this.srcFile;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void addText(String text) {
        this.text = text;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public boolean getCompiled() {
        return this.compiled;
    }

    public void setSetBeans(boolean setBeans) {
        this.setBeans = setBeans;
    }

    public void setClassLoader(ClassLoader loader) {
        this.scriptLoader = loader;
    }

    private synchronized ClassLoader generateClassLoader() {
        if (this.scriptLoader != null) {
            return this.scriptLoader;
        }
        if (this.cpDelegate == null) {
            this.scriptLoader = getClass().getClassLoader();
            return this.scriptLoader;
        }
        this.scriptLoader = this.cpDelegate.getClassLoader();
        return this.scriptLoader;
    }

    private ClasspathUtils.Delegate getClassPathDelegate() {
        if (this.cpDelegate == null) {
            if (this.projectComponent == null) {
                throw new IllegalStateException("Can't access classpath without a project component");
            }
            this.cpDelegate = ClasspathUtils.getDelegate(this.projectComponent);
        }
        return this.cpDelegate;
    }

    private ScriptRunnerBase getRunner() {
        return new ScriptRunnerCreator(this.projectComponent.getProject()).createRunner(this.manager, this.language, generateClassLoader());
    }

    public void add(ResourceCollection resource) {
        this.resources.add(resource);
    }
}
