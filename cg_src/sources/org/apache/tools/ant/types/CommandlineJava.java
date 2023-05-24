package org.apache.tools.ant.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/CommandlineJava.class */
public class CommandlineJava implements Cloneable {
    private String vmVersion;
    private ExecutableType executableType;
    private Commandline vmCommand = new Commandline();
    private Commandline javaCommand = new Commandline();
    private SysProperties sysProperties = new SysProperties();
    private Path classpath = null;
    private Path bootclasspath = null;
    private Path modulepath = null;
    private Path upgrademodulepath = null;
    private String maxMemory = null;
    private Assertions assertions = null;
    private boolean cloneVm = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/CommandlineJava$ExecutableType.class */
    public enum ExecutableType {
        CLASS,
        JAR,
        MODULE,
        SOURCE_FILE
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/CommandlineJava$SysProperties.class */
    public static class SysProperties extends Environment implements Cloneable {
        Properties sys = null;
        private Vector<PropertySet> propertySets = new Vector<>();

        @Override // org.apache.tools.ant.types.Environment
        public String[] getVariables() throws BuildException {
            List<String> definitions = new LinkedList<>();
            addDefinitionsToList(definitions.listIterator());
            if (definitions.isEmpty()) {
                return null;
            }
            return (String[]) definitions.toArray(new String[definitions.size()]);
        }

        public void addDefinitionsToList(ListIterator<String> listIt) {
            String[] props = super.getVariables();
            if (props != null) {
                for (String prop : props) {
                    listIt.add(MSVSSConstants.FLAG_CODEDIFF + prop);
                }
            }
            Properties propertySetProperties = mergePropertySets();
            for (String key : propertySetProperties.stringPropertyNames()) {
                listIt.add(MSVSSConstants.FLAG_CODEDIFF + key + "=" + propertySetProperties.getProperty(key));
            }
        }

        public int size() {
            Properties p = mergePropertySets();
            return this.variables.size() + p.size();
        }

        public void setSystem() throws BuildException {
            try {
                this.sys = System.getProperties();
                Properties p = new Properties();
                for (String name : this.sys.stringPropertyNames()) {
                    String value = this.sys.getProperty(name);
                    if (value != null) {
                        p.put(name, value);
                    }
                }
                p.putAll(mergePropertySets());
                Iterator<Environment.Variable> it = this.variables.iterator();
                while (it.hasNext()) {
                    Environment.Variable v = it.next();
                    v.validate();
                    p.put(v.getKey(), v.getValue());
                }
                System.setProperties(p);
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        public void restoreSystem() throws BuildException {
            if (this.sys == null) {
                throw new BuildException("Unbalanced nesting of SysProperties");
            }
            try {
                System.setProperties(this.sys);
                this.sys = null;
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        public Object clone() throws CloneNotSupportedException {
            try {
                SysProperties c = (SysProperties) super.clone();
                c.variables = (Vector) this.variables.clone();
                c.propertySets = (Vector) this.propertySets.clone();
                return c;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public void addSyspropertyset(PropertySet ps) {
            this.propertySets.addElement(ps);
        }

        public void addSysproperties(SysProperties ps) {
            this.variables.addAll(ps.variables);
            this.propertySets.addAll(ps.propertySets);
        }

        private Properties mergePropertySets() {
            Properties p = new Properties();
            Iterator<PropertySet> it = this.propertySets.iterator();
            while (it.hasNext()) {
                PropertySet ps = it.next();
                p.putAll(ps.getProperties());
            }
            return p;
        }
    }

    public CommandlineJava() {
        setVm(JavaEnvUtils.getJreExecutable("java"));
        setVmversion(JavaEnvUtils.getJavaVersion());
    }

    public Commandline.Argument createArgument() {
        return this.javaCommand.createArgument();
    }

    public Commandline.Argument createVmArgument() {
        return this.vmCommand.createArgument();
    }

    public void addSysproperty(Environment.Variable sysp) {
        this.sysProperties.addVariable(sysp);
    }

    public void addSyspropertyset(PropertySet sysp) {
        this.sysProperties.addSyspropertyset(sysp);
    }

    public void addSysproperties(SysProperties sysp) {
        this.sysProperties.addSysproperties(sysp);
    }

    public void setVm(String vm) {
        this.vmCommand.setExecutable(vm);
    }

    public void setVmversion(String value) {
        this.vmVersion = value;
    }

    public void setCloneVm(boolean cloneVm) {
        this.cloneVm = cloneVm;
    }

    public Assertions getAssertions() {
        return this.assertions;
    }

    public void setAssertions(Assertions assertions) {
        this.assertions = assertions;
    }

    public void setJar(String jarpathname) {
        this.javaCommand.setExecutable(jarpathname);
        this.executableType = ExecutableType.JAR;
    }

    public String getJar() {
        if (this.executableType == ExecutableType.JAR) {
            return this.javaCommand.getExecutable();
        }
        return null;
    }

    public void setClassname(String classname) {
        if (this.executableType == ExecutableType.MODULE) {
            this.javaCommand.setExecutable(createModuleClassPair(parseModuleFromModuleClassPair(this.javaCommand.getExecutable()), classname), false);
            return;
        }
        this.javaCommand.setExecutable(classname);
        this.executableType = ExecutableType.CLASS;
    }

    public String getClassname() {
        if (this.executableType != null) {
            switch (this.executableType) {
                case CLASS:
                    return this.javaCommand.getExecutable();
                case MODULE:
                    return parseClassFromModuleClassPair(this.javaCommand.getExecutable());
                default:
                    return null;
            }
        }
        return null;
    }

    public void setSourceFile(String sourceFile) {
        this.executableType = ExecutableType.SOURCE_FILE;
        this.javaCommand.setExecutable(sourceFile);
    }

    public String getSourceFile() {
        if (this.executableType == ExecutableType.SOURCE_FILE) {
            return this.javaCommand.getExecutable();
        }
        return null;
    }

    public void setModule(String module) {
        if (this.executableType == null) {
            this.javaCommand.setExecutable(module);
        } else {
            switch (this.executableType) {
                case CLASS:
                    this.javaCommand.setExecutable(createModuleClassPair(module, this.javaCommand.getExecutable()), false);
                    break;
                case MODULE:
                    this.javaCommand.setExecutable(createModuleClassPair(module, parseClassFromModuleClassPair(this.javaCommand.getExecutable())), false);
                    break;
                case JAR:
                    this.javaCommand.setExecutable(module, false);
                    break;
            }
        }
        this.executableType = ExecutableType.MODULE;
    }

    public String getModule() {
        if (this.executableType == ExecutableType.MODULE) {
            return parseModuleFromModuleClassPair(this.javaCommand.getExecutable());
        }
        return null;
    }

    public Path createClasspath(Project p) {
        if (this.classpath == null) {
            this.classpath = new Path(p);
        }
        return this.classpath;
    }

    public Path createBootclasspath(Project p) {
        if (this.bootclasspath == null) {
            this.bootclasspath = new Path(p);
        }
        return this.bootclasspath;
    }

    public Path createModulepath(Project p) {
        if (this.modulepath == null) {
            this.modulepath = new Path(p);
        }
        return this.modulepath;
    }

    public Path createUpgrademodulepath(Project p) {
        if (this.upgrademodulepath == null) {
            this.upgrademodulepath = new Path(p);
        }
        return this.upgrademodulepath;
    }

    public String getVmversion() {
        return this.vmVersion;
    }

    public String[] getCommandline() {
        List<String> commands = new LinkedList<>();
        addCommandsToList(commands.listIterator());
        return (String[]) commands.toArray(new String[commands.size()]);
    }

    private void addCommandsToList(ListIterator<String> listIterator) {
        getActualVMCommand().addCommandToList(listIterator);
        this.sysProperties.addDefinitionsToList(listIterator);
        if (isCloneVm()) {
            SysProperties clonedSysProperties = new SysProperties();
            PropertySet ps = new PropertySet();
            PropertySet.BuiltinPropertySetName sys = new PropertySet.BuiltinPropertySetName();
            sys.setValue("system");
            ps.appendBuiltin(sys);
            clonedSysProperties.addSyspropertyset(ps);
            clonedSysProperties.addDefinitionsToList(listIterator);
        }
        Path bcp = calculateBootclasspath(true);
        if (bcp.size() > 0) {
            listIterator.add("-Xbootclasspath:" + bcp.toString());
        }
        if (haveClasspath()) {
            listIterator.add("-classpath");
            listIterator.add(this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE).toString());
        }
        if (haveModulepath()) {
            listIterator.add("--module-path");
            listIterator.add(this.modulepath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE).toString());
        }
        if (haveUpgrademodulepath()) {
            listIterator.add("--upgrade-module-path");
            listIterator.add(this.upgrademodulepath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE).toString());
        }
        if (getAssertions() != null) {
            getAssertions().applyAssertions(listIterator);
        }
        if (this.executableType == ExecutableType.JAR) {
            listIterator.add("-jar");
        } else if (this.executableType == ExecutableType.MODULE) {
            listIterator.add("-m");
        }
        this.javaCommand.addCommandToList(listIterator);
    }

    public void setMaxmemory(String max) {
        this.maxMemory = max;
    }

    public String toString() {
        return Commandline.toString(getCommandline());
    }

    public String describeCommand() {
        return Commandline.describeCommand(getCommandline());
    }

    public String describeJavaCommand() {
        return Commandline.describeCommand(getJavaCommand());
    }

    protected Commandline getActualVMCommand() {
        Commandline actualVMCommand = (Commandline) this.vmCommand.clone();
        if (this.maxMemory != null) {
            if (this.vmVersion.startsWith("1.1")) {
                actualVMCommand.createArgument().setValue("-mx" + this.maxMemory);
            } else {
                actualVMCommand.createArgument().setValue("-Xmx" + this.maxMemory);
            }
        }
        return actualVMCommand;
    }

    @Deprecated
    public int size() {
        int size = getActualVMCommand().size() + this.javaCommand.size() + this.sysProperties.size();
        if (isCloneVm()) {
            size += System.getProperties().size();
        }
        if (haveClasspath()) {
            size += 2;
        }
        if (calculateBootclasspath(true).size() > 0) {
            size++;
        }
        if (this.executableType == ExecutableType.JAR || this.executableType == ExecutableType.MODULE) {
            size++;
        }
        if (getAssertions() != null) {
            size += getAssertions().size();
        }
        return size;
    }

    public Commandline getJavaCommand() {
        return this.javaCommand;
    }

    public Commandline getVmCommand() {
        return getActualVMCommand();
    }

    public Path getClasspath() {
        return this.classpath;
    }

    public Path getBootclasspath() {
        return this.bootclasspath;
    }

    public Path getModulepath() {
        return this.modulepath;
    }

    public Path getUpgrademodulepath() {
        return this.upgrademodulepath;
    }

    public void setSystemProperties() throws BuildException {
        this.sysProperties.setSystem();
    }

    public void restoreSystemProperties() throws BuildException {
        this.sysProperties.restoreSystem();
    }

    public SysProperties getSystemProperties() {
        return this.sysProperties;
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            CommandlineJava c = (CommandlineJava) super.clone();
            c.vmCommand = (Commandline) this.vmCommand.clone();
            c.javaCommand = (Commandline) this.javaCommand.clone();
            c.sysProperties = (SysProperties) this.sysProperties.clone();
            if (this.classpath != null) {
                c.classpath = (Path) this.classpath.clone();
            }
            if (this.bootclasspath != null) {
                c.bootclasspath = (Path) this.bootclasspath.clone();
            }
            if (this.modulepath != null) {
                c.modulepath = (Path) this.modulepath.clone();
            }
            if (this.upgrademodulepath != null) {
                c.upgrademodulepath = (Path) this.upgrademodulepath.clone();
            }
            if (this.assertions != null) {
                c.assertions = (Assertions) this.assertions.clone();
            }
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    public void clearJavaArgs() {
        this.javaCommand.clearArgs();
    }

    public boolean haveClasspath() {
        Path fullClasspath = this.classpath == null ? null : this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
        return (fullClasspath == null || fullClasspath.toString().trim().isEmpty()) ? false : true;
    }

    protected boolean haveBootclasspath(boolean log) {
        return calculateBootclasspath(log).size() > 0;
    }

    public boolean haveModulepath() {
        Path fullClasspath = this.modulepath != null ? this.modulepath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE) : null;
        return (fullClasspath == null || fullClasspath.toString().trim().isEmpty()) ? false : true;
    }

    public boolean haveUpgrademodulepath() {
        Path fullClasspath = this.upgrademodulepath != null ? this.upgrademodulepath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE) : null;
        return (fullClasspath == null || fullClasspath.toString().trim().isEmpty()) ? false : true;
    }

    private Path calculateBootclasspath(boolean log) {
        if (this.vmVersion.startsWith("1.1")) {
            if (this.bootclasspath != null && log) {
                this.bootclasspath.log("Ignoring bootclasspath as the target VM doesn't support it.");
            }
            return new Path(null);
        }
        Path b = this.bootclasspath;
        if (b == null) {
            b = new Path(null);
        }
        return b.concatSystemBootClasspath(isCloneVm() ? "last" : Definer.OnError.POLICY_IGNORE);
    }

    private boolean isCloneVm() {
        return this.cloneVm || Boolean.parseBoolean(System.getProperty("ant.build.clonevm"));
    }

    private static String createModuleClassPair(String module, String classname) {
        return classname == null ? module : String.format("%s/%s", module, classname);
    }

    private static String parseModuleFromModuleClassPair(String moduleClassPair) {
        if (moduleClassPair == null) {
            return null;
        }
        String[] moduleAndClass = moduleClassPair.split("/");
        return moduleAndClass[0];
    }

    private static String parseClassFromModuleClassPair(String moduleClassPair) {
        if (moduleClassPair == null) {
            return null;
        }
        String[] moduleAndClass = moduleClassPair.split("/");
        if (moduleAndClass.length == 2) {
            return moduleAndClass[1];
        }
        return null;
    }
}
