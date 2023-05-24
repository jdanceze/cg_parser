package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.ProjectHelperRepository;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ProjectHelperTask.class */
public class ProjectHelperTask extends Task {
    private List<ProjectHelper> projectHelpers = new ArrayList();

    public synchronized void addConfigured(ProjectHelper projectHelper) {
        this.projectHelpers.add(projectHelper);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Stream<R> map = this.projectHelpers.stream().map((v0) -> {
            return v0.getClass();
        });
        ProjectHelperRepository projectHelperRepository = ProjectHelperRepository.getInstance();
        Objects.requireNonNull(projectHelperRepository);
        map.forEach(this::registerProjectHelper);
    }
}
