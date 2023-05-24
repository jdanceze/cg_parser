package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ScriptRunnerHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Script.class */
public class Script extends Task {
    private ScriptRunnerHelper helper = new ScriptRunnerHelper();

    @Override // org.apache.tools.ant.ProjectComponent
    public void setProject(Project project) {
        super.setProject(project);
        this.helper.setProjectComponent(this);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        this.helper.getScriptRunner().executeScript("ANT");
    }

    public void setManager(String manager) {
        this.helper.setManager(manager);
    }

    public void setLanguage(String language) {
        this.helper.setLanguage(language);
    }

    public void setSrc(String fileName) {
        this.helper.setSrc(new File(fileName));
    }

    public void addText(String text) {
        this.helper.addText(text);
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

    public void setSetBeans(boolean setBeans) {
        this.helper.setSetBeans(setBeans);
    }

    public void setEncoding(String encoding) {
        this.helper.setEncoding(encoding);
    }
}
