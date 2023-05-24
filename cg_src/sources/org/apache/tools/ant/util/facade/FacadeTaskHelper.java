package org.apache.tools.ant.util.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/facade/FacadeTaskHelper.class */
public class FacadeTaskHelper {
    private List<ImplementationSpecificArgument> args;
    private String userChoice;
    private String magicValue;
    private String defaultValue;
    private Path implementationClasspath;

    public FacadeTaskHelper(String defaultValue) {
        this(defaultValue, null);
    }

    public FacadeTaskHelper(String defaultValue, String magicValue) {
        this.args = new ArrayList();
        this.defaultValue = defaultValue;
        this.magicValue = magicValue;
    }

    public void setMagicValue(String magicValue) {
        this.magicValue = magicValue;
    }

    public void setImplementation(String userChoice) {
        this.userChoice = userChoice;
    }

    public String getImplementation() {
        return this.userChoice != null ? this.userChoice : this.magicValue != null ? this.magicValue : this.defaultValue;
    }

    public String getExplicitChoice() {
        return this.userChoice;
    }

    public void addImplementationArgument(ImplementationSpecificArgument arg) {
        this.args.add(arg);
    }

    public String[] getArgs() {
        String implementation = getImplementation();
        return (String[]) this.args.stream().map(arg -> {
            return arg.getParts(implementation);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).flatMap((v0) -> {
            return Stream.of(v0);
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public boolean hasBeenSet() {
        return (this.userChoice == null && this.magicValue == null) ? false : true;
    }

    public Path getImplementationClasspath(Project project) {
        if (this.implementationClasspath == null) {
            this.implementationClasspath = new Path(project);
        }
        return this.implementationClasspath;
    }
}
