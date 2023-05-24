package org.apache.tools.ant.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Assertions.class */
public class Assertions extends DataType implements Cloneable {
    private Boolean enableSystemAssertions;
    private ArrayList<BaseAssertion> assertionList = new ArrayList<>();

    public void addEnable(EnabledAssertion assertion) {
        checkChildrenAllowed();
        this.assertionList.add(assertion);
    }

    public void addDisable(DisabledAssertion assertion) {
        checkChildrenAllowed();
        this.assertionList.add(assertion);
    }

    public void setEnableSystemAssertions(Boolean enableSystemAssertions) {
        checkAttributesAllowed();
        this.enableSystemAssertions = enableSystemAssertions;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference ref) {
        if (!this.assertionList.isEmpty() || this.enableSystemAssertions != null) {
            throw tooManyAttributes();
        }
        super.setRefid(ref);
    }

    private Assertions getFinalReference() {
        if (getRefid() == null) {
            return this;
        }
        Object o = getRefid().getReferencedObject(getProject());
        if (!(o instanceof Assertions)) {
            throw new BuildException("reference is of wrong type");
        }
        return (Assertions) o;
    }

    public int size() {
        Assertions clause = getFinalReference();
        return clause.getFinalSize();
    }

    private int getFinalSize() {
        return this.assertionList.size() + (this.enableSystemAssertions != null ? 1 : 0);
    }

    public void applyAssertions(List<String> commandList) {
        getProject().log("Applying assertions", 4);
        Assertions clause = getFinalReference();
        if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
            getProject().log("Enabling system assertions", 4);
            commandList.add("-enablesystemassertions");
        } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
            getProject().log("disabling system assertions", 4);
            commandList.add("-disablesystemassertions");
        }
        Iterator<BaseAssertion> it = clause.assertionList.iterator();
        while (it.hasNext()) {
            BaseAssertion assertion = it.next();
            String arg = assertion.toCommand();
            getProject().log("adding assertion " + arg, 4);
            commandList.add(arg);
        }
    }

    public void applyAssertions(CommandlineJava command) {
        Assertions clause = getFinalReference();
        if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
            addVmArgument(command, "-enablesystemassertions");
        } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
            addVmArgument(command, "-disablesystemassertions");
        }
        Iterator<BaseAssertion> it = clause.assertionList.iterator();
        while (it.hasNext()) {
            BaseAssertion assertion = it.next();
            String arg = assertion.toCommand();
            addVmArgument(command, arg);
        }
    }

    public void applyAssertions(ListIterator<String> commandIterator) {
        getProject().log("Applying assertions", 4);
        Assertions clause = getFinalReference();
        if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
            getProject().log("Enabling system assertions", 4);
            commandIterator.add("-enablesystemassertions");
        } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
            getProject().log("disabling system assertions", 4);
            commandIterator.add("-disablesystemassertions");
        }
        Iterator<BaseAssertion> it = clause.assertionList.iterator();
        while (it.hasNext()) {
            BaseAssertion assertion = it.next();
            String arg = assertion.toCommand();
            getProject().log("adding assertion " + arg, 4);
            commandIterator.add(arg);
        }
    }

    private static void addVmArgument(CommandlineJava command, String arg) {
        Commandline.Argument argument = command.createVmArgument();
        argument.setValue(arg);
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() throws CloneNotSupportedException {
        Assertions that = (Assertions) super.clone();
        that.assertionList = new ArrayList<>(this.assertionList);
        return that;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Assertions$BaseAssertion.class */
    public static abstract class BaseAssertion {
        private String packageName;
        private String className;

        public abstract String getCommandPrefix();

        public void setClass(String className) {
            this.className = className;
        }

        public void setPackage(String packageName) {
            this.packageName = packageName;
        }

        protected String getClassName() {
            return this.className;
        }

        protected String getPackageName() {
            return this.packageName;
        }

        public String toCommand() {
            if (getPackageName() != null && getClassName() != null) {
                throw new BuildException("Both package and class have been set");
            }
            StringBuilder command = new StringBuilder(getCommandPrefix());
            if (getPackageName() != null) {
                command.append(':');
                command.append(getPackageName());
                if (!command.toString().endsWith("...")) {
                    command.append("...");
                }
            } else if (getClassName() != null) {
                command.append(':');
                command.append(getClassName());
            }
            return command.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Assertions$EnabledAssertion.class */
    public static class EnabledAssertion extends BaseAssertion {
        @Override // org.apache.tools.ant.types.Assertions.BaseAssertion
        public String getCommandPrefix() {
            return "-ea";
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Assertions$DisabledAssertion.class */
    public static class DisabledAssertion extends BaseAssertion {
        @Override // org.apache.tools.ant.types.Assertions.BaseAssertion
        public String getCommandPrefix() {
            return "-da";
        }
    }
}
