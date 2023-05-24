package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/BindTargets.class */
public class BindTargets extends Task {
    private String extensionPoint;
    private final List<String> targets = new ArrayList();
    private ProjectHelper.OnMissingExtensionPoint onMissingExtensionPoint;

    public void setExtensionPoint(String extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    public void setOnMissingExtensionPoint(String onMissingExtensionPoint) {
        try {
            this.onMissingExtensionPoint = ProjectHelper.OnMissingExtensionPoint.valueOf(onMissingExtensionPoint);
        } catch (IllegalArgumentException e) {
            throw new BuildException("Invalid onMissingExtensionPoint: " + onMissingExtensionPoint);
        }
    }

    public void setOnMissingExtensionPoint(ProjectHelper.OnMissingExtensionPoint onMissingExtensionPoint) {
        this.onMissingExtensionPoint = onMissingExtensionPoint;
    }

    public void setTargets(String target) {
        Stream filter = Stream.of((Object[]) target.split(",")).map((v0) -> {
            return v0.trim();
        }).filter(s -> {
            return !s.isEmpty();
        });
        List<String> list = this.targets;
        Objects.requireNonNull(list);
        filter.forEach((v1) -> {
            r1.add(v1);
        });
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.extensionPoint == null) {
            throw new BuildException("extensionPoint required", getLocation());
        }
        if (getOwningTarget() == null || !getOwningTarget().getName().isEmpty()) {
            throw new BuildException("bindtargets only allowed as a top-level task");
        }
        if (this.onMissingExtensionPoint == null) {
            this.onMissingExtensionPoint = ProjectHelper.OnMissingExtensionPoint.FAIL;
        }
        ProjectHelper helper = (ProjectHelper) getProject().getReference("ant.projectHelper");
        for (String target : this.targets) {
            helper.getExtensionStack().add(new String[]{this.extensionPoint, target, this.onMissingExtensionPoint.name()});
        }
    }
}
