package org.apache.tools.ant.types.optional;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ScriptRunnerBase;
import org.apache.tools.ant.util.ScriptRunnerHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/optional/AbstractScriptComponent.class */
public abstract class AbstractScriptComponent extends ProjectComponent {
    private ScriptRunnerHelper helper = new ScriptRunnerHelper();
    private ScriptRunnerBase runner = null;

    @Override // org.apache.tools.ant.ProjectComponent
    public void setProject(Project project) {
        super.setProject(project);
        this.helper.setProjectComponent(this);
    }

    public ScriptRunnerBase getRunner() {
        initScriptRunner();
        return this.runner;
    }

    public void setSrc(File file) {
        this.helper.setSrc(file);
    }

    public void addText(String text) {
        this.helper.addText(text);
    }

    public void setManager(String manager) {
        this.helper.setManager(manager);
    }

    public void setLanguage(String language) {
        this.helper.setLanguage(language);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initScriptRunner() {
        if (this.runner != null) {
            return;
        }
        this.helper.setProjectComponent(this);
        this.runner = this.helper.getScriptRunner();
    }

    public void setClasspath(Path classpath) {
        this.helper.setClasspath(classpath);
    }

    public Path createClasspath() {
        return this.helper.createClasspath();
    }

    public void setClasspathRef(Reference r) {
        this.helper.setClasspathRef(r);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void executeScript(String execName) {
        getRunner().executeScript(execName);
    }

    public void setSetBeans(boolean setBeans) {
        this.helper.setSetBeans(setBeans);
    }

    public void setEncoding(String encoding) {
        this.helper.setEncoding(encoding);
    }
}
