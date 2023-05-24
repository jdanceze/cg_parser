package org.apache.tools.ant;

import android.hardware.Camera;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import org.apache.tools.ant.helper.DefaultExecutor;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.types.Description;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.VectorSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Project.class */
public class Project implements ResourceFactory {
    public static final int MSG_ERR = 0;
    public static final int MSG_WARN = 1;
    public static final int MSG_INFO = 2;
    public static final int MSG_VERBOSE = 3;
    public static final int MSG_DEBUG = 4;
    private static final String VISITING = "VISITING";
    private static final String VISITED = "VISITED";
    @Deprecated
    public static final String JAVA_1_0 = "1.0";
    @Deprecated
    public static final String JAVA_1_1 = "1.1";
    @Deprecated
    public static final String JAVA_1_2 = "1.2";
    @Deprecated
    public static final String JAVA_1_3 = "1.3";
    @Deprecated
    public static final String JAVA_1_4 = "1.4";
    public static final String TOKEN_START = "@";
    public static final String TOKEN_END = "@";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String name;
    private String description;
    private String defaultTarget;
    private final FilterSetCollection globalFilters;
    private File baseDir;
    private final Object listenersLock;
    private volatile BuildListener[] listeners;
    private final ThreadLocal<Boolean> isLoggingMessage;
    private ClassLoader coreLoader;
    private final Map<Thread, Task> threadTasks;
    private final Map<ThreadGroup, Task> threadGroupTasks;
    private InputHandler inputHandler;
    private InputStream defaultInputStream;
    private boolean keepGoingMode;
    private final Object referencesLock = new Object();
    private final Hashtable<String, Object> references = new AntRefTable();
    private final HashMap<String, Object> idReferences = new HashMap<>();
    private final Hashtable<String, Target> targets = new Hashtable<>();
    private final FilterSet globalFilterSet = new FilterSet();

    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    public void setDefaultInputStream(InputStream defaultInputStream) {
        this.defaultInputStream = defaultInputStream;
    }

    public InputStream getDefaultInputStream() {
        return this.defaultInputStream;
    }

    public InputHandler getInputHandler() {
        return this.inputHandler;
    }

    public Project() {
        this.globalFilterSet.setProject(this);
        this.globalFilters = new FilterSetCollection(this.globalFilterSet);
        this.listenersLock = new Object();
        this.listeners = new BuildListener[0];
        this.isLoggingMessage = ThreadLocal.withInitial(() -> {
            return Boolean.FALSE;
        });
        this.coreLoader = null;
        this.threadTasks = Collections.synchronizedMap(new WeakHashMap());
        this.threadGroupTasks = Collections.synchronizedMap(new WeakHashMap());
        this.inputHandler = null;
        this.defaultInputStream = null;
        this.keepGoingMode = false;
        this.inputHandler = new DefaultInputHandler();
    }

    public Project createSubProject() {
        Project subProject;
        try {
            subProject = (Project) getClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            subProject = new Project();
        }
        initSubProject(subProject);
        return subProject;
    }

    public void initSubProject(Project subProject) {
        ComponentHelper.getComponentHelper(subProject).initSubProject(ComponentHelper.getComponentHelper(this));
        subProject.setDefaultInputStream(getDefaultInputStream());
        subProject.setKeepGoingMode(isKeepGoingMode());
        subProject.setExecutor(getExecutor().getSubProjectExecutor());
    }

    public void init() throws BuildException {
        initProperties();
        ComponentHelper.getComponentHelper(this).initDefaultDefinitions();
    }

    public void initProperties() throws BuildException {
        setJavaVersionProperty();
        setSystemProperties();
        setPropertyInternal(MagicNames.ANT_VERSION, Main.getAntVersion());
        setAntLib();
    }

    private void setAntLib() {
        File antlib = Locator.getClassSource(Project.class);
        if (antlib != null) {
            setPropertyInternal(MagicNames.ANT_LIB, antlib.getAbsolutePath());
        }
    }

    public AntClassLoader createClassLoader(Path path) {
        return AntClassLoader.newAntClassLoader(getClass().getClassLoader(), this, path, true);
    }

    public AntClassLoader createClassLoader(ClassLoader parent, Path path) {
        return AntClassLoader.newAntClassLoader(parent, this, path, true);
    }

    public void setCoreLoader(ClassLoader coreLoader) {
        this.coreLoader = coreLoader;
    }

    public ClassLoader getCoreLoader() {
        return this.coreLoader;
    }

    public void addBuildListener(BuildListener listener) {
        BuildListener[] buildListenerArr;
        synchronized (this.listenersLock) {
            for (BuildListener buildListener : this.listeners) {
                if (buildListener == listener) {
                    return;
                }
            }
            BuildListener[] newListeners = new BuildListener[this.listeners.length + 1];
            System.arraycopy(this.listeners, 0, newListeners, 0, this.listeners.length);
            newListeners[this.listeners.length] = listener;
            this.listeners = newListeners;
        }
    }

    public void removeBuildListener(BuildListener listener) {
        synchronized (this.listenersLock) {
            int i = 0;
            while (true) {
                if (i >= this.listeners.length) {
                    break;
                } else if (this.listeners[i] != listener) {
                    i++;
                } else {
                    BuildListener[] newListeners = new BuildListener[this.listeners.length - 1];
                    System.arraycopy(this.listeners, 0, newListeners, 0, i);
                    System.arraycopy(this.listeners, i + 1, newListeners, i, (this.listeners.length - i) - 1);
                    this.listeners = newListeners;
                    break;
                }
            }
        }
    }

    public Vector<BuildListener> getBuildListeners() {
        Vector<BuildListener> r;
        synchronized (this.listenersLock) {
            r = new Vector<>(this.listeners.length);
            Collections.addAll(r, this.listeners);
        }
        return r;
    }

    public void log(String message) {
        log(message, 2);
    }

    public void log(String message, int msgLevel) {
        log(message, (Throwable) null, msgLevel);
    }

    public void log(String message, Throwable throwable, int msgLevel) {
        fireMessageLogged(this, message, throwable, msgLevel);
    }

    public void log(Task task, String message, int msgLevel) {
        fireMessageLogged(task, message, (Throwable) null, msgLevel);
    }

    public void log(Task task, String message, Throwable throwable, int msgLevel) {
        fireMessageLogged(task, message, throwable, msgLevel);
    }

    public void log(Target target, String message, int msgLevel) {
        log(target, message, (Throwable) null, msgLevel);
    }

    public void log(Target target, String message, Throwable throwable, int msgLevel) {
        fireMessageLogged(target, message, throwable, msgLevel);
    }

    public FilterSet getGlobalFilterSet() {
        return this.globalFilterSet;
    }

    public void setProperty(String name, String value) {
        PropertyHelper.getPropertyHelper(this).setProperty(name, (Object) value, true);
    }

    public void setNewProperty(String name, String value) {
        PropertyHelper.getPropertyHelper(this).setNewProperty(name, value);
    }

    public void setUserProperty(String name, String value) {
        PropertyHelper.getPropertyHelper(this).setUserProperty(name, value);
    }

    public void setInheritedProperty(String name, String value) {
        PropertyHelper.getPropertyHelper(this).setInheritedProperty(name, value);
    }

    private void setPropertyInternal(String name, String value) {
        PropertyHelper.getPropertyHelper(this).setProperty(name, (Object) value, false);
    }

    public String getProperty(String propertyName) {
        Object value = PropertyHelper.getPropertyHelper(this).getProperty(propertyName);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public String replaceProperties(String value) throws BuildException {
        return PropertyHelper.getPropertyHelper(this).replaceProperties(null, value, null);
    }

    public String getUserProperty(String propertyName) {
        return (String) PropertyHelper.getPropertyHelper(this).getUserProperty(propertyName);
    }

    public Hashtable<String, Object> getProperties() {
        return PropertyHelper.getPropertyHelper(this).getProperties();
    }

    public Set<String> getPropertyNames() {
        return PropertyHelper.getPropertyHelper(this).getPropertyNames();
    }

    public Hashtable<String, Object> getUserProperties() {
        return PropertyHelper.getPropertyHelper(this).getUserProperties();
    }

    public Hashtable<String, Object> getInheritedProperties() {
        return PropertyHelper.getPropertyHelper(this).getInheritedProperties();
    }

    public void copyUserProperties(Project other) {
        PropertyHelper.getPropertyHelper(this).copyUserProperties(other);
    }

    public void copyInheritedProperties(Project other) {
        PropertyHelper.getPropertyHelper(this).copyInheritedProperties(other);
    }

    @Deprecated
    public void setDefaultTarget(String defaultTarget) {
        setDefault(defaultTarget);
    }

    public String getDefaultTarget() {
        return this.defaultTarget;
    }

    public void setDefault(String defaultTarget) {
        if (defaultTarget != null) {
            setUserProperty(MagicNames.PROJECT_DEFAULT_TARGET, defaultTarget);
        }
        this.defaultTarget = defaultTarget;
    }

    public void setName(String name) {
        setUserProperty(MagicNames.PROJECT_NAME, name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        if (this.description == null) {
            this.description = Description.getDescription(this);
        }
        return this.description;
    }

    @Deprecated
    public void addFilter(String token, String value) {
        if (token == null) {
            return;
        }
        this.globalFilterSet.addFilter(new FilterSet.Filter(token, value));
    }

    @Deprecated
    public Hashtable<String, String> getFilters() {
        return this.globalFilterSet.getFilterHash();
    }

    public void setBasedir(String baseD) throws BuildException {
        setBaseDir(new File(baseD));
    }

    public void setBaseDir(File baseDir) throws BuildException {
        File baseDir2 = FILE_UTILS.normalize(baseDir.getAbsolutePath());
        if (!baseDir2.exists()) {
            throw new BuildException("Basedir " + baseDir2.getAbsolutePath() + " does not exist");
        }
        if (!baseDir2.isDirectory()) {
            throw new BuildException("Basedir " + baseDir2.getAbsolutePath() + " is not a directory");
        }
        this.baseDir = baseDir2;
        setPropertyInternal(MagicNames.PROJECT_BASEDIR, this.baseDir.getPath());
        String msg = "Project base dir set to: " + this.baseDir;
        log(msg, 3);
    }

    public File getBaseDir() {
        if (this.baseDir == null) {
            try {
                setBasedir(".");
            } catch (BuildException ex) {
                ex.printStackTrace();
            }
        }
        return this.baseDir;
    }

    public void setKeepGoingMode(boolean keepGoingMode) {
        this.keepGoingMode = keepGoingMode;
    }

    public boolean isKeepGoingMode() {
        return this.keepGoingMode;
    }

    @Deprecated
    public static String getJavaVersion() {
        return JavaEnvUtils.getJavaVersion();
    }

    public void setJavaVersionProperty() throws BuildException {
        String javaVersion = JavaEnvUtils.getJavaVersion();
        setPropertyInternal(MagicNames.ANT_JAVA_VERSION, javaVersion);
        if (!JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_1_8)) {
            throw new BuildException("Ant cannot work on Java prior to 1.8");
        }
        log("Detected Java version: " + javaVersion + " in: " + System.getProperty("java.home"), 3);
        log("Detected OS: " + System.getProperty("os.name"), 3);
    }

    public void setSystemProperties() {
        Properties systemP = System.getProperties();
        for (String propertyName : systemP.stringPropertyNames()) {
            String value = systemP.getProperty(propertyName);
            if (value != null) {
                setPropertyInternal(propertyName, value);
            }
        }
    }

    public void addTaskDefinition(String taskName, Class<?> taskClass) throws BuildException {
        ComponentHelper.getComponentHelper(this).addTaskDefinition(taskName, taskClass);
    }

    public void checkTaskClass(Class<?> taskClass) throws BuildException {
        ComponentHelper.getComponentHelper(this).checkTaskClass(taskClass);
        if (!Modifier.isPublic(taskClass.getModifiers())) {
            String message = taskClass + " is not public";
            log(message, 0);
            throw new BuildException(message);
        } else if (Modifier.isAbstract(taskClass.getModifiers())) {
            String message2 = taskClass + " is abstract";
            log(message2, 0);
            throw new BuildException(message2);
        } else {
            try {
                taskClass.getConstructor(new Class[0]);
                if (!Task.class.isAssignableFrom(taskClass)) {
                    TaskAdapter.checkTaskClass(taskClass, this);
                }
            } catch (LinkageError e) {
                String message3 = "Could not load " + taskClass + ": " + e;
                log(message3, 0);
                throw new BuildException(message3, e);
            } catch (NoSuchMethodException e2) {
                String message4 = "No public no-arg constructor in " + taskClass;
                log(message4, 0);
                throw new BuildException(message4);
            }
        }
    }

    public Hashtable<String, Class<?>> getTaskDefinitions() {
        return ComponentHelper.getComponentHelper(this).getTaskDefinitions();
    }

    public Map<String, Class<?>> getCopyOfTaskDefinitions() {
        return new HashMap(getTaskDefinitions());
    }

    public void addDataTypeDefinition(String typeName, Class<?> typeClass) {
        ComponentHelper.getComponentHelper(this).addDataTypeDefinition(typeName, typeClass);
    }

    public Hashtable<String, Class<?>> getDataTypeDefinitions() {
        return ComponentHelper.getComponentHelper(this).getDataTypeDefinitions();
    }

    public Map<String, Class<?>> getCopyOfDataTypeDefinitions() {
        return new HashMap(getDataTypeDefinitions());
    }

    public void addTarget(Target target) throws BuildException {
        addTarget(target.getName(), target);
    }

    public void addTarget(String targetName, Target target) throws BuildException {
        if (this.targets.get(targetName) != null) {
            throw new BuildException("Duplicate target: `" + targetName + "'");
        }
        addOrReplaceTarget(targetName, target);
    }

    public void addOrReplaceTarget(Target target) {
        addOrReplaceTarget(target.getName(), target);
    }

    public void addOrReplaceTarget(String targetName, Target target) {
        String msg = " +Target: " + targetName;
        log(msg, 4);
        target.setProject(this);
        this.targets.put(targetName, target);
    }

    public Hashtable<String, Target> getTargets() {
        return this.targets;
    }

    public Map<String, Target> getCopyOfTargets() {
        return new HashMap(this.targets);
    }

    public Task createTask(String taskType) throws BuildException {
        return ComponentHelper.getComponentHelper(this).createTask(taskType);
    }

    public Object createDataType(String typeName) throws BuildException {
        return ComponentHelper.getComponentHelper(this).createDataType(typeName);
    }

    public void setExecutor(Executor e) {
        addReference(MagicNames.ANT_EXECUTOR_REFERENCE, e);
    }

    public Executor getExecutor() {
        Object o = getReference(MagicNames.ANT_EXECUTOR_REFERENCE);
        if (o == null) {
            String classname = getProperty(MagicNames.ANT_EXECUTOR_CLASSNAME);
            if (classname == null) {
                classname = DefaultExecutor.class.getName();
            }
            log("Attempting to create object of type " + classname, 4);
            try {
                o = Class.forName(classname, true, this.coreLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassNotFoundException e) {
                try {
                    o = Class.forName(classname).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Exception ex) {
                    log(ex.toString(), 0);
                }
            } catch (Exception ex2) {
                log(ex2.toString(), 0);
            }
            if (o == null) {
                throw new BuildException("Unable to obtain a Target Executor instance.");
            }
            setExecutor((Executor) o);
        }
        return (Executor) o;
    }

    public void executeTargets(Vector<String> names) throws BuildException {
        setUserProperty(MagicNames.PROJECT_INVOKED_TARGETS, String.join(",", names));
        getExecutor().executeTargets(this, (String[]) names.toArray(new String[names.size()]));
    }

    public void demuxOutput(String output, boolean isWarning) {
        Task task = getThreadTask(Thread.currentThread());
        if (task == null) {
            log(output, isWarning ? 1 : 2);
        } else if (isWarning) {
            task.handleErrorOutput(output);
        } else {
            task.handleOutput(output);
        }
    }

    public int defaultInput(byte[] buffer, int offset, int length) throws IOException {
        if (this.defaultInputStream == null) {
            throw new EOFException("No input provided for project");
        }
        System.out.flush();
        return this.defaultInputStream.read(buffer, offset, length);
    }

    public int demuxInput(byte[] buffer, int offset, int length) throws IOException {
        Task task = getThreadTask(Thread.currentThread());
        if (task == null) {
            return defaultInput(buffer, offset, length);
        }
        return task.handleInput(buffer, offset, length);
    }

    public void demuxFlush(String output, boolean isError) {
        Task task = getThreadTask(Thread.currentThread());
        if (task == null) {
            fireMessageLogged(this, output, isError ? 0 : 2);
        } else if (isError) {
            task.handleErrorFlush(output);
        } else {
            task.handleFlush(output);
        }
    }

    public void executeTarget(String targetName) throws BuildException {
        if (targetName == null) {
            throw new BuildException("No target specified");
        }
        executeSortedTargets(topoSort(targetName, this.targets, false));
    }

    public void executeSortedTargets(Vector<Target> sortedTargets) throws BuildException {
        Set<String> succeededTargets = new HashSet<>();
        BuildException buildException = null;
        Iterator<Target> it = sortedTargets.iterator();
        while (it.hasNext()) {
            Target curtarget = it.next();
            boolean canExecute = true;
            Iterator it2 = Collections.list(curtarget.getDependencies()).iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                String dependencyName = (String) it2.next();
                if (!succeededTargets.contains(dependencyName)) {
                    canExecute = false;
                    log(curtarget, "Cannot execute '" + curtarget.getName() + "' - '" + dependencyName + "' failed or was not executed.", 0);
                    break;
                }
            }
            if (canExecute) {
                Throwable thrownException = null;
                try {
                    curtarget.performTasks();
                    succeededTargets.add(curtarget.getName());
                } catch (RuntimeException ex) {
                    if (!this.keepGoingMode) {
                        throw ex;
                    }
                    thrownException = ex;
                } catch (Throwable ex2) {
                    if (!this.keepGoingMode) {
                        throw new BuildException(ex2);
                    }
                    thrownException = ex2;
                }
                if (thrownException != null) {
                    if (thrownException instanceof BuildException) {
                        log(curtarget, "Target '" + curtarget.getName() + "' failed with message '" + thrownException.getMessage() + "'.", 0);
                        if (buildException == null) {
                            buildException = (BuildException) thrownException;
                        }
                    } else {
                        log(curtarget, "Target '" + curtarget.getName() + "' failed with message '" + thrownException.getMessage() + "'.", 0);
                        thrownException.printStackTrace(System.err);
                        if (buildException == null) {
                            buildException = new BuildException(thrownException);
                        }
                    }
                }
            }
        }
        if (buildException != null) {
            throw buildException;
        }
    }

    @Deprecated
    public File resolveFile(String fileName, File rootDir) {
        return FILE_UTILS.resolveFile(rootDir, fileName);
    }

    public File resolveFile(String fileName) {
        return FILE_UTILS.resolveFile(this.baseDir, fileName);
    }

    @Deprecated
    public static String translatePath(String toProcess) {
        return FileUtils.translatePath(toProcess);
    }

    @Deprecated
    public void copyFile(String sourceFile, String destFile) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile);
    }

    @Deprecated
    public void copyFile(String sourceFile, String destFile, boolean filtering) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
    }

    @Deprecated
    public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
    }

    @Deprecated
    public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
    }

    @Deprecated
    public void copyFile(File sourceFile, File destFile) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile);
    }

    @Deprecated
    public void copyFile(File sourceFile, File destFile, boolean filtering) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
    }

    @Deprecated
    public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
    }

    @Deprecated
    public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
        FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
    }

    @Deprecated
    public void setFileLastModified(File file, long time) throws BuildException {
        FILE_UTILS.setFileLastModified(file, time);
        log("Setting modification time for " + file, 3);
    }

    public static boolean toBoolean(String s) {
        return Camera.Parameters.FLASH_MODE_ON.equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s);
    }

    public static Project getProject(Object o) {
        if (o instanceof ProjectComponent) {
            return ((ProjectComponent) o).getProject();
        }
        try {
            Method m = o.getClass().getMethod("getProject", new Class[0]);
            if (Project.class.equals(m.getReturnType())) {
                return (Project) m.invoke(o, new Object[0]);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public final Vector<Target> topoSort(String root, Hashtable<String, Target> targetTable) throws BuildException {
        return topoSort(new String[]{root}, targetTable, true);
    }

    public final Vector<Target> topoSort(String root, Hashtable<String, Target> targetTable, boolean returnAll) throws BuildException {
        return topoSort(new String[]{root}, targetTable, returnAll);
    }

    public final Vector<Target> topoSort(String[] roots, Hashtable<String, Target> targetTable, boolean returnAll) throws BuildException {
        Vector<Target> ret = new VectorSet<>();
        Hashtable<String, String> state = new Hashtable<>();
        Stack<String> visiting = new Stack<>();
        for (String root : roots) {
            String st = state.get(root);
            if (st == null) {
                tsort(root, targetTable, state, visiting, ret);
            } else if (st == VISITING) {
                throw new BuildException("Unexpected node in visiting state: " + root);
            }
        }
        log("Build sequence for target(s)" + ((String) Arrays.stream(roots).map(root2 -> {
            return String.format(" `%s'", root2);
        }).collect(Collectors.joining(","))) + " is " + ret, 3);
        Vector<Target> complete = returnAll ? ret : new Vector<>(ret);
        for (String curTarget : targetTable.keySet()) {
            String st2 = state.get(curTarget);
            if (st2 == null) {
                tsort(curTarget, targetTable, state, visiting, complete);
            } else if (st2 == VISITING) {
                throw new BuildException("Unexpected node in visiting state: " + curTarget);
            }
        }
        log("Complete build sequence is " + complete, 3);
        return ret;
    }

    private void tsort(String root, Hashtable<String, Target> targetTable, Hashtable<String, String> state, Stack<String> visiting, Vector<Target> ret) throws BuildException {
        state.put(root, VISITING);
        visiting.push(root);
        Target target = targetTable.get(root);
        if (target == null) {
            StringBuilder sb = new StringBuilder("Target \"");
            sb.append(root);
            sb.append("\" does not exist in the project \"");
            sb.append(this.name);
            sb.append("\". ");
            visiting.pop();
            if (!visiting.empty()) {
                String parent = visiting.peek();
                sb.append("It is used from target \"");
                sb.append(parent);
                sb.append("\".");
            }
            throw new BuildException(new String(sb));
        }
        Iterator it = Collections.list(target.getDependencies()).iterator();
        while (it.hasNext()) {
            String cur = (String) it.next();
            String m = state.get(cur);
            if (m == null) {
                tsort(cur, targetTable, state, visiting, ret);
            } else if (m == VISITING) {
                throw makeCircularException(cur, visiting);
            }
        }
        String p = visiting.pop();
        if (root != p) {
            throw new BuildException("Unexpected internal error: expected to pop " + root + " but got " + p);
        }
        state.put(root, VISITED);
        ret.addElement(target);
    }

    private static BuildException makeCircularException(String end, Stack<String> stk) {
        String c;
        StringBuilder sb = new StringBuilder("Circular dependency: ");
        sb.append(end);
        do {
            c = stk.pop();
            sb.append(" <- ");
            sb.append(c);
        } while (!c.equals(end));
        return new BuildException(sb.toString());
    }

    public void inheritIDReferences(Project parent) {
    }

    public void addIdReference(String id, Object value) {
        this.idReferences.put(id, value);
    }

    public void addReference(String referenceName, Object value) {
        synchronized (this.referencesLock) {
            Object old = ((AntRefTable) this.references).getReal(referenceName);
            if (old == value) {
                return;
            }
            if (old != null && !(old instanceof UnknownElement)) {
                log("Overriding previous definition of reference to " + referenceName, 3);
            }
            log("Adding reference: " + referenceName, 4);
            this.references.put(referenceName, value);
        }
    }

    public Hashtable<String, Object> getReferences() {
        return this.references;
    }

    public boolean hasReference(String key) {
        boolean containsKey;
        synchronized (this.referencesLock) {
            containsKey = this.references.containsKey(key);
        }
        return containsKey;
    }

    public Map<String, Object> getCopyOfReferences() {
        HashMap hashMap;
        synchronized (this.referencesLock) {
            hashMap = new HashMap(this.references);
        }
        return hashMap;
    }

    public <T> T getReference(String key) {
        synchronized (this.referencesLock) {
            T ret = (T) this.references.get(key);
            if (ret != null) {
                return ret;
            }
            if (!key.equals(MagicNames.REFID_PROPERTY_HELPER)) {
                try {
                    if (PropertyHelper.getPropertyHelper(this).containsProperties(key)) {
                        log("Unresolvable reference " + key + " might be a misuse of property expansion syntax.", 1);
                    }
                    return null;
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }

    public String getElementName(Object element) {
        return ComponentHelper.getComponentHelper(this).getElementName(element);
    }

    public void fireBuildStarted() {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(this);
        for (BuildListener currListener : this.listeners) {
            currListener.buildStarted(event);
        }
    }

    public void fireBuildFinished(Throwable exception) {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(this);
        event.setException(exception);
        for (BuildListener currListener : this.listeners) {
            currListener.buildFinished(event);
        }
        IntrospectionHelper.clearCache();
    }

    public void fireSubBuildStarted() {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(this);
        for (BuildListener currListener : this.listeners) {
            if (currListener instanceof SubBuildListener) {
                ((SubBuildListener) currListener).subBuildStarted(event);
            }
        }
    }

    public void fireSubBuildFinished(Throwable exception) {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(this);
        event.setException(exception);
        for (BuildListener currListener : this.listeners) {
            if (currListener instanceof SubBuildListener) {
                ((SubBuildListener) currListener).subBuildFinished(event);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireTargetStarted(Target target) {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(target);
        for (BuildListener currListener : this.listeners) {
            currListener.targetStarted(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireTargetFinished(Target target, Throwable exception) {
        BuildListener[] buildListenerArr;
        BuildEvent event = new BuildEvent(target);
        event.setException(exception);
        for (BuildListener currListener : this.listeners) {
            currListener.targetFinished(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireTaskStarted(Task task) {
        BuildListener[] buildListenerArr;
        registerThreadTask(Thread.currentThread(), task);
        BuildEvent event = new BuildEvent(task);
        for (BuildListener currListener : this.listeners) {
            currListener.taskStarted(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireTaskFinished(Task task, Throwable exception) {
        BuildListener[] buildListenerArr;
        registerThreadTask(Thread.currentThread(), null);
        System.out.flush();
        System.err.flush();
        BuildEvent event = new BuildEvent(task);
        event.setException(exception);
        for (BuildListener currListener : this.listeners) {
            currListener.taskFinished(event);
        }
    }

    private void fireMessageLoggedEvent(BuildEvent event, String message, int priority) {
        BuildListener[] buildListenerArr;
        if (message == null) {
            message = String.valueOf(message);
        }
        if (message.endsWith(System.lineSeparator())) {
            int endIndex = message.length() - System.lineSeparator().length();
            event.setMessage(message.substring(0, endIndex), priority);
        } else {
            event.setMessage(message, priority);
        }
        if (this.isLoggingMessage.get() != Boolean.FALSE) {
            return;
        }
        try {
            this.isLoggingMessage.set(Boolean.TRUE);
            for (BuildListener currListener : this.listeners) {
                currListener.messageLogged(event);
            }
        } finally {
            this.isLoggingMessage.set(Boolean.FALSE);
        }
    }

    protected void fireMessageLogged(Project project, String message, int priority) {
        fireMessageLogged(project, message, (Throwable) null, priority);
    }

    protected void fireMessageLogged(Project project, String message, Throwable throwable, int priority) {
        BuildEvent event = new BuildEvent(project);
        event.setException(throwable);
        fireMessageLoggedEvent(event, message, priority);
    }

    protected void fireMessageLogged(Target target, String message, int priority) {
        fireMessageLogged(target, message, (Throwable) null, priority);
    }

    protected void fireMessageLogged(Target target, String message, Throwable throwable, int priority) {
        BuildEvent event = new BuildEvent(target);
        event.setException(throwable);
        fireMessageLoggedEvent(event, message, priority);
    }

    protected void fireMessageLogged(Task task, String message, int priority) {
        fireMessageLogged(task, message, (Throwable) null, priority);
    }

    protected void fireMessageLogged(Task task, String message, Throwable throwable, int priority) {
        BuildEvent event = new BuildEvent(task);
        event.setException(throwable);
        fireMessageLoggedEvent(event, message, priority);
    }

    public void registerThreadTask(Thread thread, Task task) {
        synchronized (this.threadTasks) {
            if (task != null) {
                this.threadTasks.put(thread, task);
                this.threadGroupTasks.put(thread.getThreadGroup(), task);
            } else {
                this.threadTasks.remove(thread);
                this.threadGroupTasks.remove(thread.getThreadGroup());
            }
        }
    }

    public Task getThreadTask(Thread thread) {
        Task task;
        synchronized (this.threadTasks) {
            Task task2 = this.threadTasks.get(thread);
            if (task2 == null) {
                for (ThreadGroup group = thread.getThreadGroup(); task2 == null && group != null; group = group.getParent()) {
                    task2 = this.threadGroupTasks.get(group);
                }
            }
            task = task2;
        }
        return task;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Project$AntRefTable.class */
    public static class AntRefTable extends Hashtable<String, Object> {
        private static final long serialVersionUID = 1;

        AntRefTable() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object getReal(Object key) {
            return super.get(key);
        }

        @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
        public Object get(Object key) {
            Object o = getReal(key);
            if (o instanceof UnknownElement) {
                UnknownElement ue = (UnknownElement) o;
                ue.maybeConfigure();
                o = ue.getRealThing();
            }
            return o;
        }
    }

    public final void setProjectReference(Object obj) {
        if (obj instanceof ProjectComponent) {
            ((ProjectComponent) obj).setProject(this);
            return;
        }
        try {
            Method method = obj.getClass().getMethod("setProject", Project.class);
            if (method != null) {
                method.invoke(obj, this);
            }
        } catch (Throwable th) {
        }
    }

    @Override // org.apache.tools.ant.types.ResourceFactory
    public Resource getResource(String name) {
        return new FileResource(getBaseDir(), name);
    }
}
