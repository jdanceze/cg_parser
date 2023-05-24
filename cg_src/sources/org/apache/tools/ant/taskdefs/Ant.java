package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.ParserSupports;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.VectorSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Ant.class */
public class Ant extends Task {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private Project newProject;
    private File dir = null;
    private String antFile = null;
    private String output = null;
    private boolean inheritAll = true;
    private boolean inheritRefs = false;
    private List<Property> properties = new Vector();
    private List<Reference> references = new Vector();
    private PrintStream out = null;
    private List<PropertySet> propertySets = new Vector();
    private List<String> targets = new Vector();
    private boolean targetAttributeSet = false;
    private boolean useNativeBasedir = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Ant$PropertyType.class */
    public enum PropertyType {
        PLAIN,
        INHERITED,
        USER
    }

    public Ant() {
    }

    public Ant(Task owner) {
        bindToOwner(owner);
    }

    public void setUseNativeBasedir(boolean b) {
        this.useNativeBasedir = b;
    }

    public void setInheritAll(boolean value) {
        this.inheritAll = value;
    }

    public void setInheritRefs(boolean value) {
        this.inheritRefs = value;
    }

    @Override // org.apache.tools.ant.Task
    public void init() {
        this.newProject = getProject().createSubProject();
        this.newProject.setJavaVersionProperty();
    }

    private void reinit() {
        init();
    }

    private void initializeProject() {
        File outfile;
        this.newProject.setInputHandler(getProject().getInputHandler());
        getProject().getBuildListeners().forEach(bl -> {
            this.newProject.addBuildListener(bl);
        });
        if (this.output != null) {
            if (this.dir != null) {
                outfile = FILE_UTILS.resolveFile(this.dir, this.output);
            } else {
                outfile = getProject().resolveFile(this.output);
            }
            try {
                this.out = new PrintStream(Files.newOutputStream(outfile.toPath(), new OpenOption[0]));
                DefaultLogger logger = new DefaultLogger();
                logger.setMessageOutputLevel(2);
                logger.setOutputPrintStream(this.out);
                logger.setErrorPrintStream(this.out);
                this.newProject.addBuildListener(logger);
            } catch (IOException e) {
                log("Ant: Can't set output to " + this.output);
            }
        }
        if (this.useNativeBasedir) {
            addAlmostAll(getProject().getUserProperties(), PropertyType.USER);
        } else {
            getProject().copyUserProperties(this.newProject);
        }
        if (!this.inheritAll) {
            this.newProject.initProperties();
        } else {
            addAlmostAll(getProject().getProperties(), PropertyType.PLAIN);
        }
        for (PropertySet ps : this.propertySets) {
            addAlmostAll(ps.getProperties(), PropertyType.PLAIN);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void handleOutput(String outputToHandle) {
        if (this.newProject != null) {
            this.newProject.demuxOutput(outputToHandle, false);
        } else {
            super.handleOutput(outputToHandle);
        }
    }

    @Override // org.apache.tools.ant.Task
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        if (this.newProject != null) {
            return this.newProject.demuxInput(buffer, offset, length);
        }
        return super.handleInput(buffer, offset, length);
    }

    @Override // org.apache.tools.ant.Task
    public void handleFlush(String toFlush) {
        if (this.newProject != null) {
            this.newProject.demuxFlush(toFlush, false);
        } else {
            super.handleFlush(toFlush);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void handleErrorOutput(String errorOutputToHandle) {
        if (this.newProject != null) {
            this.newProject.demuxOutput(errorOutputToHandle, true);
        } else {
            super.handleErrorOutput(errorOutputToHandle);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void handleErrorFlush(String errorOutputToFlush) {
        if (this.newProject != null) {
            this.newProject.demuxFlush(errorOutputToFlush, true);
        } else {
            super.handleErrorFlush(errorOutputToFlush);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String defaultTarget;
        File savedDir = this.dir;
        String savedAntFile = this.antFile;
        Vector<String> locals = new VectorSet<>(this.targets);
        try {
            getNewProject();
            if (this.dir == null && this.inheritAll) {
                this.dir = getProject().getBaseDir();
            }
            initializeProject();
            if (this.dir != null) {
                if (!this.useNativeBasedir) {
                    this.newProject.setBaseDir(this.dir);
                    if (savedDir != null) {
                        this.newProject.setInheritedProperty(MagicNames.PROJECT_BASEDIR, this.dir.getAbsolutePath());
                    }
                }
            } else {
                this.dir = getProject().getBaseDir();
            }
            overrideProperties();
            if (this.antFile == null) {
                this.antFile = getDefaultBuildFile();
            }
            File file = FILE_UTILS.resolveFile(this.dir, this.antFile);
            this.antFile = file.getAbsolutePath();
            log("calling target(s) " + (locals.isEmpty() ? "[default]" : locals.toString()) + " in build file " + this.antFile, 3);
            this.newProject.setUserProperty(MagicNames.ANT_FILE, this.antFile);
            String thisAntFile = getProject().getProperty(MagicNames.ANT_FILE);
            if (thisAntFile != null && file.equals(getProject().resolveFile(thisAntFile)) && getOwningTarget() != null && getOwningTarget().getName().isEmpty()) {
                if ("antcall".equals(getTaskName())) {
                    throw new BuildException("antcall must not be used at the top level.");
                }
                throw new BuildException("%s task at the top level must not invoke its own build file.", getTaskName());
            }
            try {
                ProjectHelper.configureProject(this.newProject, file);
                if (locals.isEmpty() && (defaultTarget = this.newProject.getDefaultTarget()) != null) {
                    locals.add(defaultTarget);
                }
                if (this.newProject.getProperty(MagicNames.ANT_FILE).equals(getProject().getProperty(MagicNames.ANT_FILE)) && getOwningTarget() != null) {
                    String owningTargetName = getOwningTarget().getName();
                    if (locals.contains(owningTargetName)) {
                        throw new BuildException("%s task calling its own parent target.", getTaskName());
                    }
                    Map<String, Target> targetsMap = getProject().getTargets();
                    Stream stream = locals.stream();
                    Objects.requireNonNull(targetsMap);
                    if (stream.map((v1) -> {
                        return r1.get(v1);
                    }).filter((v0) -> {
                        return Objects.nonNull(v0);
                    }).anyMatch(other -> {
                        return other.dependsOn(owningTargetName);
                    })) {
                        throw new BuildException("%s task calling a target that depends on its parent target '%s'.", getTaskName(), owningTargetName);
                    }
                }
                addReferences();
                if (!locals.isEmpty() && (locals.size() != 1 || locals.get(0) == null || !locals.get(0).isEmpty())) {
                    try {
                        log("Entering " + this.antFile + "...", 3);
                        this.newProject.fireSubBuildStarted();
                        this.newProject.executeTargets(locals);
                        log("Exiting " + this.antFile + ".", 3);
                        this.newProject.fireSubBuildFinished(null);
                    } catch (BuildException ex) {
                        BuildException be = ProjectHelper.addLocationToBuildException(ex, getLocation());
                        throw be;
                    }
                }
            } catch (BuildException ex2) {
                throw ProjectHelper.addLocationToBuildException(ex2, getLocation());
            }
        } finally {
            this.newProject = null;
            for (Property p : this.properties) {
                p.setProject(null);
            }
            if (this.output != null && this.out != null) {
                FileUtils.close((OutputStream) this.out);
            }
            this.dir = savedDir;
            this.antFile = savedAntFile;
        }
    }

    protected String getDefaultBuildFile() {
        return Main.DEFAULT_BUILD_FILENAME;
    }

    private void overrideProperties() throws BuildException {
        Set<String> set = new HashSet<>();
        for (int i = this.properties.size() - 1; i >= 0; i--) {
            Property p = this.properties.get(i);
            if (p.getName() != null && !p.getName().isEmpty()) {
                if (set.contains(p.getName())) {
                    this.properties.remove(i);
                } else {
                    set.add(p.getName());
                }
            }
        }
        this.properties.stream().peek(p2 -> {
            p2.setProject(this.newProject);
        }).forEach((v0) -> {
            v0.execute();
        });
        if (this.useNativeBasedir) {
            addAlmostAll(getProject().getInheritedProperties(), PropertyType.INHERITED);
        } else {
            getProject().copyInheritedProperties(this.newProject);
        }
    }

    private void addReferences() throws BuildException {
        Map<String, Object> thisReferences = new HashMap<>(getProject().getReferences());
        for (Reference ref : this.references) {
            String refid = ref.getRefId();
            if (refid == null) {
                throw new BuildException("the refid attribute is required for reference elements");
            }
            if (!thisReferences.containsKey(refid)) {
                log("Parent project doesn't contain any reference '" + refid + "'", 1);
            } else {
                thisReferences.remove(refid);
                String toRefid = ref.getToRefid();
                if (toRefid == null) {
                    toRefid = refid;
                }
                copyReference(refid, toRefid);
            }
        }
        if (this.inheritRefs) {
            Map<String, Object> newReferences = this.newProject.getReferences();
            for (String key : thisReferences.keySet()) {
                if (!newReferences.containsKey(key)) {
                    copyReference(key, key);
                    this.newProject.inheritIDReferences(getProject());
                }
            }
        }
    }

    private void copyReference(String oldKey, String newKey) {
        Object orig = getProject().getReference(oldKey);
        if (orig == null) {
            log("No object referenced by " + oldKey + ". Can't copy to " + newKey, 1);
            return;
        }
        Class<?> c = orig.getClass();
        Object copy = orig;
        try {
            Method cloneM = c.getMethod("clone", new Class[0]);
            if (cloneM != null) {
                copy = cloneM.invoke(orig, new Object[0]);
                log("Adding clone of reference " + oldKey, 4);
            }
        } catch (Exception e) {
        }
        if (copy instanceof ProjectComponent) {
            ((ProjectComponent) copy).setProject(this.newProject);
        } else {
            try {
                Method setProjectM = c.getMethod("setProject", Project.class);
                if (setProjectM != null) {
                    setProjectM.invoke(copy, this.newProject);
                }
            } catch (NoSuchMethodException e2) {
            } catch (Exception e22) {
                throw new BuildException("Error setting new project instance for reference with id " + oldKey, e22, getLocation());
            }
        }
        this.newProject.addReference(newKey, copy);
    }

    private void addAlmostAll(Map<?, ?> props, PropertyType type) {
        props.forEach(k, v -> {
            String key = type.toString();
            if (MagicNames.PROJECT_BASEDIR.equals(key) || MagicNames.ANT_FILE.equals(key)) {
                return;
            }
            String value = v.toString();
            switch (type) {
                case PLAIN:
                    if (this.newProject.getProperty(key) == null) {
                        this.newProject.setNewProperty(key, value);
                        return;
                    }
                    return;
                case USER:
                    this.newProject.setUserProperty(key, value);
                    return;
                case INHERITED:
                    this.newProject.setInheritedProperty(key, value);
                    return;
                default:
                    return;
            }
        });
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setAntfile(String antFile) {
        this.antFile = antFile;
    }

    public void setTarget(String targetToAdd) {
        if (targetToAdd.isEmpty()) {
            throw new BuildException("target attribute must not be empty");
        }
        this.targets.add(targetToAdd);
        this.targetAttributeSet = true;
    }

    public void setOutput(String outputFile) {
        this.output = outputFile;
    }

    public Property createProperty() {
        Property p = new Property(true, getProject());
        p.setProject(getNewProject());
        p.setTaskName(ParserSupports.PROPERTY);
        this.properties.add(p);
        return p;
    }

    public void addReference(Reference ref) {
        this.references.add(ref);
    }

    public void addConfiguredTarget(TargetElement t) {
        if (this.targetAttributeSet) {
            throw new BuildException("nested target is incompatible with the target attribute");
        }
        String name = t.getName();
        if (name.isEmpty()) {
            throw new BuildException("target name must not be empty");
        }
        this.targets.add(name);
    }

    public void addPropertyset(PropertySet ps) {
        this.propertySets.add(ps);
    }

    protected Project getNewProject() {
        if (this.newProject == null) {
            reinit();
        }
        return this.newProject;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Ant$Reference.class */
    public static class Reference extends org.apache.tools.ant.types.Reference {
        private String targetid = null;

        public void setToRefid(String targetid) {
            this.targetid = targetid;
        }

        public String getToRefid() {
            return this.targetid;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Ant$TargetElement.class */
    public static class TargetElement {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
