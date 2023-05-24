package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Typedef;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ComponentHelper.class */
public class ComponentHelper {
    private final Map<String, List<AntTypeDefinition>> restrictedDefinitions = new HashMap();
    private final Hashtable<String, AntTypeDefinition> antTypeTable = new Hashtable<>();
    private final Hashtable<String, Class<?>> taskClassDefinitions = new Hashtable<>();
    private boolean rebuildTaskClassDefinitions = true;
    private final Hashtable<String, Class<?>> typeClassDefinitions = new Hashtable<>();
    private boolean rebuildTypeClassDefinitions = true;
    private final HashSet<String> checkedNamespaces = new HashSet<>();
    private Stack<String> antLibStack = new Stack<>();
    private String antLibCurrentUri = null;
    private ComponentHelper next;
    private Project project;
    private static final String ERROR_NO_TASK_LIST_LOAD = "Can't load default task list";
    private static final String ERROR_NO_TYPE_LIST_LOAD = "Can't load default type list";
    public static final String COMPONENT_HELPER_REFERENCE = "ant.ComponentHelper";
    private static final String BUILD_SYSCLASSPATH_ONLY = "only";
    private static final String ANT_PROPERTY_TASK = "property";
    private static Properties[] defaultDefinitions = new Properties[2];

    public Project getProject() {
        return this.project;
    }

    public static ComponentHelper getComponentHelper(Project project) {
        if (project == null) {
            return null;
        }
        ComponentHelper ph = (ComponentHelper) project.getReference(COMPONENT_HELPER_REFERENCE);
        if (ph != null) {
            return ph;
        }
        ComponentHelper ph2 = new ComponentHelper();
        ph2.setProject(project);
        project.addReference(COMPONENT_HELPER_REFERENCE, ph2);
        return ph2;
    }

    protected ComponentHelper() {
    }

    public void setNext(ComponentHelper next) {
        this.next = next;
    }

    public ComponentHelper getNext() {
        return this.next;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    private synchronized Set<String> getCheckedNamespace() {
        Set<String> result = (Set) this.checkedNamespaces.clone();
        return result;
    }

    private Map<String, List<AntTypeDefinition>> getRestrictedDefinition() {
        List<AntTypeDefinition> entryVal;
        Map<String, List<AntTypeDefinition>> result = new HashMap<>();
        synchronized (this.restrictedDefinitions) {
            for (Map.Entry<String, List<AntTypeDefinition>> entry : this.restrictedDefinitions.entrySet()) {
                List<AntTypeDefinition> entryVal2 = entry.getValue();
                synchronized (entryVal2) {
                    entryVal = new ArrayList<>(entryVal2);
                }
                result.put(entry.getKey(), entryVal);
            }
        }
        return result;
    }

    public void initSubProject(ComponentHelper helper) {
        Hashtable<String, AntTypeDefinition> typeTable = (Hashtable) helper.antTypeTable.clone();
        synchronized (this.antTypeTable) {
            for (AntTypeDefinition def : typeTable.values()) {
                this.antTypeTable.put(def.getName(), def);
            }
        }
        Set<String> inheritedCheckedNamespace = helper.getCheckedNamespace();
        synchronized (this) {
            this.checkedNamespaces.addAll(inheritedCheckedNamespace);
        }
        Map<String, List<AntTypeDefinition>> inheritedRestrictedDef = helper.getRestrictedDefinition();
        synchronized (this.restrictedDefinitions) {
            this.restrictedDefinitions.putAll(inheritedRestrictedDef);
        }
    }

    public Object createComponent(UnknownElement ue, String ns, String componentType) throws BuildException {
        Object component = createComponent(componentType);
        if (component instanceof Task) {
            Task task = (Task) component;
            task.setLocation(ue.getLocation());
            task.setTaskType(componentType);
            task.setTaskName(ue.getTaskName());
            task.setOwningTarget(ue.getOwningTarget());
            task.init();
        }
        return component;
    }

    public Object createComponent(String componentName) {
        AntTypeDefinition def = getDefinition(componentName);
        if (def == null) {
            return null;
        }
        return def.create(this.project);
    }

    public Class<?> getComponentClass(String componentName) {
        AntTypeDefinition def = getDefinition(componentName);
        if (def == null) {
            return null;
        }
        return def.getExposedClass(this.project);
    }

    public AntTypeDefinition getDefinition(String componentName) {
        checkNamespace(componentName);
        return this.antTypeTable.get(componentName);
    }

    public void initDefaultDefinitions() {
        initTasks();
        initTypes();
        new DefaultDefinitions(this).execute();
    }

    public void addTaskDefinition(String taskName, Class<?> taskClass) {
        checkTaskClass(taskClass);
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(taskName);
        def.setClassLoader(taskClass.getClassLoader());
        def.setClass(taskClass);
        def.setAdapterClass(TaskAdapter.class);
        def.setClassName(taskClass.getName());
        def.setAdaptToClass(Task.class);
        updateDataTypeDefinition(def);
    }

    public void checkTaskClass(Class<?> taskClass) throws BuildException {
        if (!Modifier.isPublic(taskClass.getModifiers())) {
            String message = taskClass + " is not public";
            this.project.log(message, 0);
            throw new BuildException(message);
        } else if (Modifier.isAbstract(taskClass.getModifiers())) {
            String message2 = taskClass + " is abstract";
            this.project.log(message2, 0);
            throw new BuildException(message2);
        } else {
            try {
                taskClass.getConstructor(null);
                if (!Task.class.isAssignableFrom(taskClass)) {
                    TaskAdapter.checkTaskClass(taskClass, this.project);
                }
            } catch (NoSuchMethodException e) {
                String message3 = "No public no-arg constructor in " + taskClass;
                this.project.log(message3, 0);
                throw new BuildException(message3);
            }
        }
    }

    public Hashtable<String, Class<?>> getTaskDefinitions() {
        synchronized (this.taskClassDefinitions) {
            synchronized (this.antTypeTable) {
                if (this.rebuildTaskClassDefinitions) {
                    this.taskClassDefinitions.clear();
                    this.antTypeTable.entrySet().stream().filter(e -> {
                        return ((AntTypeDefinition) e.getValue()).getExposedClass(this.project) != null && Task.class.isAssignableFrom(((AntTypeDefinition) e.getValue()).getExposedClass(this.project));
                    }).forEach(e2 -> {
                        this.taskClassDefinitions.put((String) e2.getKey(), ((AntTypeDefinition) e2.getValue()).getTypeClass(this.project));
                    });
                    this.rebuildTaskClassDefinitions = false;
                }
            }
        }
        return this.taskClassDefinitions;
    }

    public Hashtable<String, Class<?>> getDataTypeDefinitions() {
        synchronized (this.typeClassDefinitions) {
            synchronized (this.antTypeTable) {
                if (this.rebuildTypeClassDefinitions) {
                    this.typeClassDefinitions.clear();
                    this.antTypeTable.entrySet().stream().filter(e -> {
                        return (((AntTypeDefinition) e.getValue()).getExposedClass(this.project) == null || Task.class.isAssignableFrom(((AntTypeDefinition) e.getValue()).getExposedClass(this.project))) ? false : true;
                    }).forEach(e2 -> {
                        this.typeClassDefinitions.put((String) e2.getKey(), ((AntTypeDefinition) e2.getValue()).getTypeClass(this.project));
                    });
                    this.rebuildTypeClassDefinitions = false;
                }
            }
        }
        return this.typeClassDefinitions;
    }

    public List<AntTypeDefinition> getRestrictedDefinitions(String componentName) {
        List<AntTypeDefinition> list;
        synchronized (this.restrictedDefinitions) {
            list = this.restrictedDefinitions.get(componentName);
        }
        return list;
    }

    public void addDataTypeDefinition(String typeName, Class<?> typeClass) {
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(typeName);
        def.setClass(typeClass);
        updateDataTypeDefinition(def);
        this.project.log(" +User datatype: " + typeName + "     " + typeClass.getName(), 4);
    }

    public void addDataTypeDefinition(AntTypeDefinition def) {
        if (!def.isRestrict()) {
            updateDataTypeDefinition(def);
        } else {
            updateRestrictedDefinition(def);
        }
    }

    public Hashtable<String, AntTypeDefinition> getAntTypeTable() {
        return this.antTypeTable;
    }

    public Task createTask(String taskType) throws BuildException {
        Task task = createNewTask(taskType);
        if (task == null && taskType.equals("property")) {
            addTaskDefinition("property", Property.class);
            task = createNewTask(taskType);
        }
        return task;
    }

    private Task createNewTask(String taskType) throws BuildException {
        Object obj;
        Class<?> c = getComponentClass(taskType);
        if (c == null || !Task.class.isAssignableFrom(c) || (obj = createComponent(taskType)) == null) {
            return null;
        }
        if (!(obj instanceof Task)) {
            throw new BuildException("Expected a Task from '" + taskType + "' but got an instance of " + obj.getClass().getName() + " instead");
        }
        Task task = (Task) obj;
        task.setTaskType(taskType);
        task.setTaskName(taskType);
        this.project.log("   +Task: " + taskType, 4);
        return task;
    }

    public Object createDataType(String typeName) throws BuildException {
        return createComponent(typeName);
    }

    public String getElementName(Object element) {
        return getElementName(element, false);
    }

    public String getElementName(Object o, boolean brief) {
        Class<?> elementClass = o.getClass();
        String elementClassname = elementClass.getName();
        synchronized (this.antTypeTable) {
            for (AntTypeDefinition def : this.antTypeTable.values()) {
                if (elementClassname.equals(def.getClassName()) && elementClass == def.getExposedClass(this.project)) {
                    String name = def.getName();
                    return brief ? name : "The <" + name + "> type";
                }
            }
            return getUnmappedElementName(o.getClass(), brief);
        }
    }

    public static String getElementName(Project p, Object o, boolean brief) {
        if (p == null) {
            p = Project.getProject(o);
        }
        return p == null ? getUnmappedElementName(o.getClass(), brief) : getComponentHelper(p).getElementName(o, brief);
    }

    private static String getUnmappedElementName(Class<?> c, boolean brief) {
        if (brief) {
            String name = c.getName();
            return name.substring(name.lastIndexOf(46) + 1);
        }
        return c.toString();
    }

    private boolean validDefinition(AntTypeDefinition def) {
        return (def.getTypeClass(this.project) == null || def.getExposedClass(this.project) == null) ? false : true;
    }

    private boolean sameDefinition(AntTypeDefinition def, AntTypeDefinition old) {
        boolean defValid = validDefinition(def);
        boolean sameValidity = defValid == validDefinition(old);
        return sameValidity && (!defValid || def.sameDefinition(old, this.project));
    }

    private void updateRestrictedDefinition(AntTypeDefinition def) {
        List<AntTypeDefinition> list;
        String name = def.getName();
        synchronized (this.restrictedDefinitions) {
            list = this.restrictedDefinitions.computeIfAbsent(name, k -> {
                return new ArrayList();
            });
        }
        synchronized (list) {
            Iterator<AntTypeDefinition> i = list.iterator();
            while (true) {
                if (!i.hasNext()) {
                    break;
                }
                AntTypeDefinition current = i.next();
                if (current.getClassName().equals(def.getClassName())) {
                    i.remove();
                    break;
                }
            }
            list.add(def);
        }
    }

    private void updateDataTypeDefinition(AntTypeDefinition def) {
        String name = def.getName();
        synchronized (this.antTypeTable) {
            this.rebuildTaskClassDefinitions = true;
            this.rebuildTypeClassDefinitions = true;
            AntTypeDefinition old = this.antTypeTable.get(name);
            if (old != null) {
                if (sameDefinition(def, old)) {
                    return;
                }
                Class<?> oldClass = old.getExposedClass(this.project);
                boolean isTask = oldClass != null && Task.class.isAssignableFrom(oldClass);
                this.project.log("Trying to override old definition of " + (isTask ? "task " : "datatype ") + name, def.similarDefinition(old, this.project) ? 3 : 1);
            }
            this.project.log(" +Datatype " + name + Instruction.argsep + def.getClassName(), 4);
            this.antTypeTable.put(name, def);
        }
    }

    public void enterAntLib(String uri) {
        this.antLibCurrentUri = uri;
        this.antLibStack.push(uri);
    }

    public String getCurrentAntlibUri() {
        return this.antLibCurrentUri;
    }

    public void exitAntLib() {
        this.antLibStack.pop();
        this.antLibCurrentUri = this.antLibStack.isEmpty() ? null : this.antLibStack.peek();
    }

    private void initTasks() {
        ClassLoader classLoader = getClassLoader(null);
        Properties props = getDefaultDefinitions(false);
        for (String name : props.stringPropertyNames()) {
            AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(props.getProperty(name));
            def.setClassLoader(classLoader);
            def.setAdaptToClass(Task.class);
            def.setAdapterClass(TaskAdapter.class);
            this.antTypeTable.put(name, def);
        }
    }

    private ClassLoader getClassLoader(ClassLoader classLoader) {
        String buildSysclasspath = this.project.getProperty(MagicNames.BUILD_SYSCLASSPATH);
        if (this.project.getCoreLoader() != null && !BUILD_SYSCLASSPATH_ONLY.equals(buildSysclasspath)) {
            classLoader = this.project.getCoreLoader();
        }
        return classLoader;
    }

    private static synchronized Properties getDefaultDefinitions(boolean type) throws BuildException {
        int idx = type ? (char) 1 : (char) 0;
        if (defaultDefinitions[idx] == null) {
            String resource = type ? MagicNames.TYPEDEFS_PROPERTIES_RESOURCE : MagicNames.TASKDEF_PROPERTIES_RESOURCE;
            String errorString = type ? ERROR_NO_TYPE_LIST_LOAD : ERROR_NO_TASK_LIST_LOAD;
            try {
                InputStream in = ComponentHelper.class.getResourceAsStream(resource);
                if (in == null) {
                    throw new BuildException(errorString);
                }
                Properties p = new Properties();
                p.load(in);
                defaultDefinitions[idx] = p;
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new BuildException(errorString, e);
            }
        }
        return defaultDefinitions[idx];
    }

    private void initTypes() {
        ClassLoader classLoader = getClassLoader(null);
        Properties props = getDefaultDefinitions(true);
        for (String name : props.stringPropertyNames()) {
            AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(props.getProperty(name));
            def.setClassLoader(classLoader);
            this.antTypeTable.put(name, def);
        }
    }

    private synchronized void checkNamespace(String componentName) {
        String uri = ProjectHelper.extractUriFromComponentName(componentName);
        if (uri.isEmpty()) {
            uri = ProjectHelper.ANT_CORE_URI;
        }
        if (!uri.startsWith("antlib:") || this.checkedNamespaces.contains(uri)) {
            return;
        }
        this.checkedNamespaces.add(uri);
        if (this.antTypeTable.isEmpty()) {
            initDefaultDefinitions();
        }
        Typedef definer = new Typedef();
        definer.setProject(this.project);
        definer.init();
        definer.setURI(uri);
        definer.setTaskName(uri);
        definer.setResource(Definer.makeResourceFromURI(uri));
        definer.setOnError(new Definer.OnError(Definer.OnError.POLICY_IGNORE));
        definer.execute();
    }

    public String diagnoseCreationFailure(String componentName, String type) {
        String antHomeLib;
        StringWriter errorText = new StringWriter();
        PrintWriter out = new PrintWriter(errorText);
        out.println("Problem: failed to create " + type + Instruction.argsep + componentName);
        boolean lowlevel = false;
        boolean jars = false;
        boolean definitions = false;
        String home = System.getProperty(Launcher.USER_HOMEDIR);
        File libDir = new File(home, Launcher.USER_LIBDIR);
        boolean probablyIDE = false;
        String anthome = System.getProperty("ant.home");
        if (anthome != null) {
            File antHomeLibDir = new File(anthome, Launcher.ANT_PRIVATELIB);
            antHomeLib = antHomeLibDir.getAbsolutePath();
        } else {
            probablyIDE = true;
            antHomeLib = "ANT_HOME" + File.separatorChar + Launcher.ANT_PRIVATELIB;
        }
        StringBuilder dirListingText = new StringBuilder();
        dirListingText.append("        -");
        dirListingText.append(antHomeLib);
        dirListingText.append('\n');
        if (probablyIDE) {
            dirListingText.append("        -");
            dirListingText.append("the IDE Ant configuration dialogs");
        } else {
            dirListingText.append("        -");
            dirListingText.append(libDir);
            dirListingText.append('\n');
            dirListingText.append("        -");
            dirListingText.append("a directory added on the command line with the -lib argument");
        }
        String dirListing = dirListingText.toString();
        AntTypeDefinition def = getDefinition(componentName);
        if (def == null) {
            printUnknownDefinition(out, componentName, dirListing);
        } else {
            String classname = def.getClassName();
            boolean antTask = classname.startsWith("org.apache.tools.ant.");
            boolean optional = classname.startsWith("org.apache.tools.ant.types.optional") || classname.startsWith("org.apache.tools.ant.taskdefs.optional");
            Class<?> clazz = null;
            try {
                clazz = def.innerGetTypeClass();
            } catch (ClassNotFoundException e) {
                jars = true;
                if (!optional) {
                    definitions = true;
                }
                printClassNotFound(out, classname, optional, dirListing);
            } catch (NoClassDefFoundError ncdfe) {
                jars = true;
                printNotLoadDependentClass(out, optional, ncdfe, dirListing);
            }
            if (clazz != null) {
                try {
                    def.innerCreateAndSet(clazz, this.project);
                    out.println("The component could be instantiated.");
                } catch (IllegalAccessException e2) {
                    lowlevel = true;
                    out.println("Cause: The constructor for " + classname + " is private and cannot be invoked.");
                } catch (InstantiationException e3) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname + " is abstract and cannot be instantiated.");
                } catch (NoClassDefFoundError ncdfe2) {
                    jars = true;
                    out.println("Cause:  A class needed by class " + classname + " cannot be found: ");
                    out.println("       " + ncdfe2.getMessage());
                    out.println("Action: Determine what extra JAR files are needed, and place them in:");
                    out.println(dirListing);
                } catch (NoSuchMethodException e4) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname + " has no compatible constructor.");
                } catch (InvocationTargetException ex) {
                    lowlevel = true;
                    Throwable t = ex.getTargetException();
                    out.println("Cause: The constructor threw the exception");
                    out.println(t.toString());
                    t.printStackTrace(out);
                }
            }
            out.println();
            out.println("Do not panic, this is a common problem.");
            if (definitions) {
                out.println("It may just be a typographical error in the build file or the task/type declaration.");
            }
            if (jars) {
                out.println("The commonest cause is a missing JAR.");
            }
            if (lowlevel) {
                out.println("This is quite a low level problem, which may need consultation with the author of the task.");
                if (antTask) {
                    out.println("This may be the Ant team. Please file a defect or contact the developer team.");
                } else {
                    out.println("This does not appear to be a task bundled with Ant.");
                    out.println("Please take it up with the supplier of the third-party " + type + ".");
                    out.println("If you have written it yourself, you probably have a bug to fix.");
                }
            } else {
                out.println();
                out.println("This is not a bug; it is a configuration problem");
            }
        }
        out.flush();
        out.close();
        return errorText.toString();
    }

    private void printUnknownDefinition(PrintWriter out, String componentName, String dirListing) {
        boolean isAntlib = componentName.startsWith("antlib:");
        String uri = ProjectHelper.extractUriFromComponentName(componentName);
        out.println("Cause: The name is undefined.");
        out.println("Action: Check the spelling.");
        out.println("Action: Check that any custom tasks/types have been declared.");
        out.println("Action: Check that any <presetdef>/<macrodef> declarations have taken place.");
        if (!uri.isEmpty()) {
            List<AntTypeDefinition> matches = findTypeMatches(uri);
            if (matches.isEmpty()) {
                out.println("No types or tasks have been defined in this namespace yet");
                if (isAntlib) {
                    out.println();
                    out.println("This appears to be an antlib declaration. ");
                    out.println("Action: Check that the implementing library exists in one of:");
                    out.println(dirListing);
                    return;
                }
                return;
            }
            out.println();
            out.println("The definitions in the namespace " + uri + " are:");
            for (AntTypeDefinition def : matches) {
                String local = ProjectHelper.extractNameFromComponentName(def.getName());
                out.println(ASTNode.TAB + local);
            }
        }
    }

    private void printClassNotFound(PrintWriter out, String classname, boolean optional, String dirListing) {
        out.println("Cause: the class " + classname + " was not found.");
        if (optional) {
            out.println("        This looks like one of Ant's optional components.");
            out.println("Action: Check that the appropriate optional JAR exists in");
            out.println(dirListing);
            return;
        }
        out.println("Action: Check that the component has been correctly declared");
        out.println("        and that the implementing JAR is in one of:");
        out.println(dirListing);
    }

    private void printNotLoadDependentClass(PrintWriter out, boolean optional, NoClassDefFoundError ncdfe, String dirListing) {
        out.println("Cause: Could not load a dependent class " + ncdfe.getMessage());
        if (optional) {
            out.println("       It is not enough to have Ant's optional JARs");
            out.println("       you need the JAR files that the optional tasks depend upon.");
            out.println("       Ant's optional task dependencies are listed in the manual.");
        } else {
            out.println("       This class may be in a separate JAR that is not installed.");
        }
        out.println("Action: Determine what extra JAR files are needed, and place them in one of:");
        out.println(dirListing);
    }

    private List<AntTypeDefinition> findTypeMatches(String prefix) {
        List<AntTypeDefinition> list;
        synchronized (this.antTypeTable) {
            list = (List) this.antTypeTable.values().stream().filter(def -> {
                return def.getName().startsWith(prefix);
            }).collect(Collectors.toList());
        }
        return list;
    }
}
