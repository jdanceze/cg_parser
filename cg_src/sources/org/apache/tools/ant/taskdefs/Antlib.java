package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.ProjectHelperRepository;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.types.resources.URLResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Antlib.class */
public class Antlib extends Task implements TaskContainer {
    public static final String TAG = "antlib";
    private ClassLoader classLoader;
    private String uri = "";
    private List<Task> tasks = new ArrayList();

    public static Antlib createAntlib(Project project, URL antlibUrl, String uri) {
        try {
            URLConnection conn = antlibUrl.openConnection();
            conn.setUseCaches(false);
            conn.connect();
            ComponentHelper helper = ComponentHelper.getComponentHelper(project);
            helper.enterAntLib(uri);
            URLResource antlibResource = new URLResource(antlibUrl);
            try {
                ProjectHelper parser = null;
                Object p = project.getReference("ant.projectHelper");
                if (p instanceof ProjectHelper) {
                    parser = (ProjectHelper) p;
                    if (!parser.canParseAntlibDescriptor(antlibResource)) {
                        parser = null;
                    }
                }
                if (parser == null) {
                    ProjectHelperRepository helperRepository = ProjectHelperRepository.getInstance();
                    parser = helperRepository.getProjectHelperForAntlib(antlibResource);
                }
                UnknownElement ue = parser.parseAntlibDescriptor(project, antlibResource);
                if (!TAG.equals(ue.getTag())) {
                    throw new BuildException("Unexpected tag " + ue.getTag() + " expecting " + TAG, ue.getLocation());
                }
                Antlib antlib = new Antlib();
                antlib.setProject(project);
                antlib.setLocation(ue.getLocation());
                antlib.setTaskName(TAG);
                antlib.init();
                ue.configure(antlib);
                helper.exitAntLib();
                return antlib;
            } catch (Throwable th) {
                helper.exitAntLib();
                throw th;
            }
        } catch (IOException ex) {
            throw new BuildException("Unable to find " + antlibUrl, ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setURI(String uri) {
        this.uri = uri;
    }

    private ClassLoader getClassLoader() {
        if (this.classLoader == null) {
            this.classLoader = Antlib.class.getClassLoader();
        }
        return this.classLoader;
    }

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task nestedTask) {
        this.tasks.add(nestedTask);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        for (Task task : this.tasks) {
            UnknownElement ue = (UnknownElement) task;
            setLocation(ue.getLocation());
            ue.maybeConfigure();
            Object configuredObject = ue.getRealThing();
            if (configuredObject != null) {
                if (!(configuredObject instanceof AntlibDefinition)) {
                    throw new BuildException("Invalid task in antlib %s %s does not extend %s", ue.getTag(), configuredObject.getClass(), AntlibDefinition.class.getName());
                }
                AntlibDefinition def = (AntlibDefinition) configuredObject;
                def.setURI(this.uri);
                def.setAntlibClassLoader(getClassLoader());
                def.init();
                def.execute();
            }
        }
    }
}
