package org.apache.tools.ant.types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.helper.ProjectHelperImpl;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Description.class */
public class Description extends DataType {
    public void addText(String text) {
        ProjectHelper ph = (ProjectHelper) getProject().getReference("ant.projectHelper");
        if (!(ph instanceof ProjectHelperImpl)) {
            return;
        }
        String currentDescription = getProject().getDescription();
        if (currentDescription == null) {
            getProject().setDescription(text);
        } else {
            getProject().setDescription(currentDescription + text);
        }
    }

    public static String getDescription(Project project) {
        List<Target> targets = (List) project.getReference(ProjectHelper2.REFID_TARGETS);
        if (targets == null) {
            return null;
        }
        StringBuilder description = new StringBuilder();
        for (Target t : targets) {
            concatDescriptions(project, t, description);
        }
        return description.toString();
    }

    private static void concatDescriptions(Project project, Target t, StringBuilder description) {
        if (t == null) {
            return;
        }
        for (Task task : findElementInTarget(t, "description")) {
            if (task instanceof UnknownElement) {
                UnknownElement ue = (UnknownElement) task;
                String descComp = ue.getWrapper().getText().toString();
                if (descComp != null) {
                    description.append(project.replaceProperties(descComp));
                }
            }
        }
    }

    private static List<Task> findElementInTarget(Target t, String name) {
        return (List) Stream.of((Object[]) t.getTasks()).filter(task -> {
            return name.equals(task.getTaskName());
        }).collect(Collectors.toList());
    }
}
