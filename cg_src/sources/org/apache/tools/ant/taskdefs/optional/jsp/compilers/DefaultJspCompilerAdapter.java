package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import java.io.File;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.types.CommandlineJava;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/compilers/DefaultJspCompilerAdapter.class */
public abstract class DefaultJspCompilerAdapter implements JspCompilerAdapter {
    protected JspC owner;

    /* JADX INFO: Access modifiers changed from: protected */
    public void logAndAddFilesToCompile(JspC jspc, Vector<String> compileList, CommandlineJava cmd) {
        jspc.log("Compilation " + cmd.describeJavaCommand(), 3);
        String niceSourceList = (String) compileList.stream().peek(arg -> {
            cmd.createArgument().setValue(arg);
        }).map(arg2 -> {
            return String.format("    %s%n", arg2);
        }).collect(Collectors.joining(""));
        Object[] objArr = new Object[2];
        objArr[0] = compileList.size() == 1 ? "" : "s";
        objArr[1] = niceSourceList;
        jspc.log(String.format("File%s to be compiled:%n%s", objArr), 3);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter
    public void setJspc(JspC owner) {
        this.owner = owner;
    }

    public JspC getJspc() {
        return this.owner;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addArg(CommandlineJava cmd, String argument) {
        if (argument != null && !argument.isEmpty()) {
            cmd.createArgument().setValue(argument);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addArg(CommandlineJava cmd, String argument, String value) {
        if (value != null) {
            cmd.createArgument().setValue(argument);
            cmd.createArgument().setValue(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addArg(CommandlineJava cmd, String argument, File file) {
        if (file != null) {
            cmd.createArgument().setValue(argument);
            cmd.createArgument().setFile(file);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter
    public boolean implementsOwnDependencyChecking() {
        return false;
    }

    public Project getProject() {
        return getJspc().getProject();
    }
}
