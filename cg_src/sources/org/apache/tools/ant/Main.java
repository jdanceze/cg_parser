package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.launch.AntMain;
import org.apache.tools.ant.listener.SilentLogger;
import org.apache.tools.ant.property.GetProperty;
import org.apache.tools.ant.property.ResolvePropertyMap;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ProxySetup;
import org.apache.tools.ant.util.StreamUtils;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Main.class */
public class Main implements AntMain {
    public static final String DEFAULT_BUILD_FILENAME = "build.xml";
    private File buildFile;
    private static final Set<String> LAUNCH_COMMANDS = Collections.unmodifiableSet(new HashSet(Arrays.asList("-lib", "-cp", "-noclasspath", "--noclasspath", "-nouserlib", "-main")));
    private static final GetProperty NOPROPERTIES = aName -> {
        return null;
    };
    private static String antVersion = null;
    private static String shortAntVersion = null;
    private int msgOutputLevel = 2;
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private final Vector<String> targets = new Vector<>();
    private final Properties definedProps = new Properties();
    private final Vector<String> listeners = new Vector<>(1);
    private final Vector<String> propertyFiles = new Vector<>(1);
    private boolean allowInput = true;
    private boolean keepGoingMode = false;
    private String loggerClassname = null;
    private String inputHandlerClassname = null;
    private boolean emacsMode = false;
    private boolean silent = false;
    private boolean readyToRun = false;
    private boolean projectHelp = false;
    private boolean isLogFileUsed = false;
    private Integer threadPriority = null;
    private boolean proxy = false;
    private final Map<Class<?>, List<String>> extraArguments = new HashMap();

    private static void printMessage(Throwable t) {
        String message = t.getMessage();
        if (message != null) {
            System.err.println(message);
        }
    }

    public static void start(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
        Main m = new Main();
        m.startAnt(args, additionalUserProperties, coreLoader);
    }

    @Override // org.apache.tools.ant.launch.AntMain
    public void startAnt(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
        try {
            processArgs(args);
            if (additionalUserProperties != null) {
                additionalUserProperties.stringPropertyNames().forEach(key -> {
                    this.definedProps.put(additionalUserProperties, additionalUserProperties.getProperty(additionalUserProperties));
                });
            }
            int exitCode = 1;
            try {
                try {
                    try {
                        runBuild(coreLoader);
                        exitCode = 0;
                    } catch (ExitStatusException ese) {
                        exitCode = ese.getStatus();
                        if (exitCode != 0) {
                            throw ese;
                        }
                    }
                    handleLogfile();
                } catch (BuildException be) {
                    if (this.err != System.err) {
                        printMessage(be);
                    }
                    handleLogfile();
                } catch (Throwable exc) {
                    exc.printStackTrace();
                    printMessage(exc);
                    handleLogfile();
                }
                exit(exitCode);
            } catch (Throwable th) {
                handleLogfile();
                throw th;
            }
        } catch (Throwable exc2) {
            handleLogfile();
            printMessage(exc2);
            exit(1);
        }
    }

    protected void exit(int exitCode) {
        System.exit(exitCode);
    }

    private void handleLogfile() {
        if (this.isLogFileUsed) {
            FileUtils.close((OutputStream) this.out);
            FileUtils.close((OutputStream) this.err);
        }
    }

    public static void main(String[] args) {
        start(args, null, null);
    }

    public Main() {
    }

    @Deprecated
    protected Main(String[] args) throws BuildException {
        processArgs(args);
    }

    private void processArgs(String[] args) {
        String searchForThis = null;
        boolean searchForFile = false;
        PrintStream logTo = null;
        boolean justPrintUsage = false;
        boolean justPrintVersion = false;
        boolean justPrintDiagnostics = false;
        ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.equals("-help") || arg.equals("-h")) {
                justPrintUsage = true;
            } else if (arg.equals("-version")) {
                justPrintVersion = true;
            } else if (arg.equals("-diagnostics")) {
                justPrintDiagnostics = true;
            } else if (arg.equals("-quiet") || arg.equals("-q")) {
                this.msgOutputLevel = 1;
            } else if (arg.equals(SOSCmd.FLAG_VERBOSE) || arg.equals("-v")) {
                this.msgOutputLevel = 3;
            } else if (arg.equals("-debug") || arg.equals("-d")) {
                this.msgOutputLevel = 4;
            } else if (arg.equals("-silent") || arg.equals("-S")) {
                this.silent = true;
            } else if (arg.equals("-noinput")) {
                this.allowInput = false;
            } else if (arg.equals("-logfile") || arg.equals("-l")) {
                try {
                    File logFile = new File(args[i + 1]);
                    i++;
                    logTo = new PrintStream(Files.newOutputStream(logFile.toPath(), new OpenOption[0]));
                    this.isLogFileUsed = true;
                } catch (IOException e) {
                    throw new BuildException("Cannot write on the specified log file. Make sure the path exists and you have write permissions.");
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new BuildException("You must specify a log file when using the -log argument");
                }
            } else if (arg.equals("-buildfile") || arg.equals(SOSCmd.FLAG_FILE) || arg.equals("-f")) {
                i = handleArgBuildFile(args, i);
            } else if (arg.equals("-listener")) {
                i = handleArgListener(args, i);
            } else if (arg.startsWith(MSVSSConstants.FLAG_CODEDIFF)) {
                i = handleArgDefine(args, i);
            } else if (arg.equals("-logger")) {
                i = handleArgLogger(args, i);
            } else if (arg.equals("-inputhandler")) {
                i = handleArgInputHandler(args, i);
            } else if (arg.equals("-emacs") || arg.equals("-e")) {
                this.emacsMode = true;
            } else if (arg.equals("-projecthelp") || arg.equals("-p")) {
                this.projectHelp = true;
            } else if (arg.equals("-find") || arg.equals("-s")) {
                searchForFile = true;
                if (i < args.length - 1) {
                    i++;
                    searchForThis = args[i];
                }
            } else if (arg.startsWith("-propertyfile")) {
                i = handleArgPropertyFile(args, i);
            } else if (arg.equals("-k") || arg.equals("-keep-going")) {
                this.keepGoingMode = true;
            } else if (arg.equals("-nice")) {
                i = handleArgNice(args, i);
            } else if (LAUNCH_COMMANDS.contains(arg)) {
                String msg = "Ant's Main method is being handed an option " + arg + " that is only for the launcher class.\nThis can be caused by a version mismatch between the ant script/.bat file and Ant itself.";
                throw new BuildException(msg);
            } else if (arg.equals("-autoproxy")) {
                this.proxy = true;
            } else if (arg.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                boolean processed = false;
                Iterator<ArgumentProcessor> it = processorRegistry.getProcessors().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ArgumentProcessor processor = it.next();
                    int newI = processor.readArguments(args, i);
                    if (newI != -1) {
                        List<String> extraArgs = this.extraArguments.computeIfAbsent(processor.getClass(), k -> {
                            return new ArrayList();
                        });
                        extraArgs.addAll(Arrays.asList(args).subList(newI, args.length));
                        processed = true;
                        break;
                    }
                }
                if (!processed) {
                    String msg2 = "Unknown argument: " + arg;
                    System.err.println(msg2);
                    printUsage();
                    throw new BuildException("");
                }
            } else {
                this.targets.addElement(arg);
            }
            i++;
        }
        if (this.msgOutputLevel >= 3 || justPrintVersion) {
            printVersion(this.msgOutputLevel);
        }
        if (justPrintUsage || justPrintVersion || justPrintDiagnostics) {
            if (justPrintUsage) {
                printUsage();
            }
            if (justPrintDiagnostics) {
                Diagnostics.doReport(System.out, this.msgOutputLevel);
                return;
            }
            return;
        }
        if (this.buildFile == null) {
            if (searchForFile) {
                if (searchForThis != null) {
                    this.buildFile = findBuildFile(System.getProperty("user.dir"), searchForThis);
                } else {
                    Iterator<ProjectHelper> it2 = ProjectHelperRepository.getInstance().getHelpers();
                    do {
                        ProjectHelper helper = it2.next();
                        String searchForThis2 = helper.getDefaultBuildFile();
                        if (this.msgOutputLevel >= 3) {
                            System.out.println("Searching the default build file: " + searchForThis2);
                        }
                        this.buildFile = findBuildFile(System.getProperty("user.dir"), searchForThis2);
                        if (this.buildFile != null) {
                            break;
                        }
                    } while (it2.hasNext());
                }
                if (this.buildFile == null) {
                    throw new BuildException("Could not locate a build file!");
                }
            } else {
                Iterator<ProjectHelper> it3 = ProjectHelperRepository.getInstance().getHelpers();
                do {
                    ProjectHelper helper2 = it3.next();
                    this.buildFile = new File(helper2.getDefaultBuildFile());
                    if (this.msgOutputLevel >= 3) {
                        System.out.println("Trying the default build file: " + this.buildFile);
                    }
                    if (this.buildFile.exists()) {
                        break;
                    }
                } while (it3.hasNext());
            }
        }
        if (!this.buildFile.exists()) {
            System.out.println("Buildfile: " + this.buildFile + " does not exist!");
            throw new BuildException("Build failed");
        }
        if (this.buildFile.isDirectory()) {
            File whatYouMeant = new File(this.buildFile, DEFAULT_BUILD_FILENAME);
            if (whatYouMeant.isFile()) {
                this.buildFile = whatYouMeant;
            } else {
                System.out.println("What? Buildfile: " + this.buildFile + " is a dir!");
                throw new BuildException("Build failed");
            }
        }
        this.buildFile = FileUtils.getFileUtils().normalize(this.buildFile.getAbsolutePath());
        loadPropertyFiles();
        if (this.msgOutputLevel >= 2) {
            System.out.println("Buildfile: " + this.buildFile);
        }
        if (logTo != null) {
            this.out = logTo;
            this.err = logTo;
            System.setOut(this.out);
            System.setErr(this.err);
        }
        this.readyToRun = true;
    }

    private int handleArgBuildFile(String[] args, int pos) {
        try {
            int pos2 = pos + 1;
            this.buildFile = new File(args[pos2].replace('/', File.separatorChar));
            return pos2;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must specify a buildfile when using the -buildfile argument");
        }
    }

    private int handleArgListener(String[] args, int pos) {
        try {
            this.listeners.addElement(args[pos + 1]);
            return pos + 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must specify a classname when using the -listener argument");
        }
    }

    private int handleArgDefine(String[] args, int argPos) {
        String value;
        String arg = args[argPos];
        String name = arg.substring(2);
        int posEq = name.indexOf(61);
        if (posEq > 0) {
            value = name.substring(posEq + 1);
            name = name.substring(0, posEq);
        } else if (argPos < args.length - 1) {
            argPos++;
            value = args[argPos];
        } else {
            throw new BuildException("Missing value for property " + name);
        }
        this.definedProps.put(name, value);
        return argPos;
    }

    private int handleArgLogger(String[] args, int pos) {
        if (this.loggerClassname != null) {
            throw new BuildException("Only one logger class may be specified.");
        }
        try {
            int pos2 = pos + 1;
            this.loggerClassname = args[pos2];
            return pos2;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must specify a classname when using the -logger argument");
        }
    }

    private int handleArgInputHandler(String[] args, int pos) {
        if (this.inputHandlerClassname != null) {
            throw new BuildException("Only one input handler class may be specified.");
        }
        try {
            int pos2 = pos + 1;
            this.inputHandlerClassname = args[pos2];
            return pos2;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must specify a classname when using the -inputhandler argument");
        }
    }

    private int handleArgPropertyFile(String[] args, int pos) {
        try {
            int pos2 = pos + 1;
            this.propertyFiles.addElement(args[pos2]);
            return pos2;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must specify a property filename when using the -propertyfile argument");
        }
    }

    private int handleArgNice(String[] args, int pos) {
        try {
            pos++;
            this.threadPriority = Integer.decode(args[pos]);
            if (this.threadPriority.intValue() < 1 || this.threadPriority.intValue() > 10) {
                throw new BuildException("Niceness value is out of the range 1-10");
            }
            return pos;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BuildException("You must supply a niceness value (1-10) after the -nice option");
        } catch (NumberFormatException e2) {
            throw new BuildException("Unrecognized niceness value: " + args[pos]);
        }
    }

    private void loadPropertyFiles() {
        Iterator<String> it = this.propertyFiles.iterator();
        while (it.hasNext()) {
            String filename = it.next();
            Properties props = new Properties();
            try {
                InputStream fis = Files.newInputStream(Paths.get(filename, new String[0]), new OpenOption[0]);
                props.load(fis);
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("Could not load property file " + filename + ": " + e.getMessage());
            }
            props.stringPropertyNames().stream().filter(name -> {
                return this.definedProps.getProperty(name) == null;
            }).forEach(name2 -> {
                this.definedProps.put(props, props.getProperty(props));
            });
        }
    }

    @Deprecated
    private File getParentFile(File file) {
        File parent = file.getParentFile();
        if (parent != null && this.msgOutputLevel >= 3) {
            System.out.println("Searching in " + parent.getAbsolutePath());
        }
        return parent;
    }

    private File findBuildFile(String start, String suffix) {
        if (this.msgOutputLevel >= 2) {
            System.out.println("Searching for " + suffix + " ...");
        }
        File parent = new File(new File(start).getAbsolutePath());
        File file = new File(parent, suffix);
        while (true) {
            File file2 = file;
            if (!file2.exists()) {
                parent = getParentFile(parent);
                if (parent == null) {
                    return null;
                }
                file = new File(parent, suffix);
            } else {
                return file2;
            }
        }
    }

    private void runBuild(ClassLoader coreLoader) throws BuildException {
        BuildException buildException;
        BuildException buildException2;
        if (this.readyToRun) {
            ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
            for (ArgumentProcessor processor : processorRegistry.getProcessors()) {
                List<String> extraArgs = this.extraArguments.get(processor.getClass());
                if (extraArgs != null && processor.handleArg(extraArgs)) {
                    return;
                }
            }
            Project project = new Project();
            project.setCoreLoader(coreLoader);
            Throwable error = null;
            try {
                try {
                    addBuildListeners(project);
                    addInputHandler(project);
                    PrintStream savedErr = System.err;
                    PrintStream savedOut = System.out;
                    InputStream savedIn = System.in;
                    try {
                        if (this.allowInput) {
                            project.setDefaultInputStream(System.in);
                        }
                        System.setIn(new DemuxInputStream(project));
                        System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
                        System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
                        if (!this.projectHelp) {
                            project.fireBuildStarted();
                        }
                        if (this.threadPriority != null) {
                            try {
                                project.log("Setting Ant's thread priority to " + this.threadPriority, 3);
                                Thread.currentThread().setPriority(this.threadPriority.intValue());
                            } catch (SecurityException e) {
                                project.log("A security manager refused to set the -nice value");
                            }
                        }
                        setProperties(project);
                        project.setKeepGoingMode(this.keepGoingMode);
                        if (this.proxy) {
                            ProxySetup proxySetup = new ProxySetup(project);
                            proxySetup.enableProxies();
                        }
                        for (ArgumentProcessor processor2 : processorRegistry.getProcessors()) {
                            List<String> extraArgs2 = this.extraArguments.get(processor2.getClass());
                            if (extraArgs2 != null) {
                                processor2.prepareConfigure(project, extraArgs2);
                            }
                        }
                        ProjectHelper.configureProject(project, this.buildFile);
                        for (ArgumentProcessor processor3 : processorRegistry.getProcessors()) {
                            List<String> extraArgs3 = this.extraArguments.get(processor3.getClass());
                            if (extraArgs3 != null && processor3.handleArg(project, extraArgs3)) {
                                if (!this.projectHelp) {
                                    try {
                                        project.fireBuildFinished(null);
                                        return;
                                    } finally {
                                    }
                                } else if (0 != 0) {
                                    project.log(error.toString(), 0);
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                        if (this.projectHelp) {
                            printDescription(project);
                            printTargets(project, this.msgOutputLevel > 2, this.msgOutputLevel > 3);
                            System.setOut(savedOut);
                            System.setErr(savedErr);
                            System.setIn(savedIn);
                            if (!this.projectHelp) {
                                try {
                                    project.fireBuildFinished(null);
                                    return;
                                } finally {
                                }
                            } else if (0 != 0) {
                                project.log(error.toString(), 0);
                                return;
                            } else {
                                return;
                            }
                        }
                        if (this.targets.isEmpty() && project.getDefaultTarget() != null) {
                            this.targets.addElement(project.getDefaultTarget());
                        }
                        project.executeTargets(this.targets);
                        System.setOut(savedOut);
                        System.setErr(savedErr);
                        System.setIn(savedIn);
                        if (this.projectHelp) {
                            if (0 != 0) {
                                project.log(error.toString(), 0);
                                return;
                            }
                            return;
                        }
                        try {
                            project.fireBuildFinished(null);
                        } finally {
                            System.err.println("Caught an exception while logging the end of the build.  Exception was:");
                            t.printStackTrace();
                            if (0 != 0) {
                                System.err.println("There has been an error prior to that:");
                                error.printStackTrace();
                            }
                            buildException2 = new BuildException(t);
                        }
                    } finally {
                        System.setOut(savedOut);
                        System.setErr(savedErr);
                        System.setIn(savedIn);
                    }
                } catch (Error | RuntimeException exc) {
                    throw exc;
                }
            } catch (Throwable th) {
                if (!this.projectHelp) {
                    try {
                        project.fireBuildFinished(null);
                    } finally {
                        System.err.println("Caught an exception while logging the end of the build.  Exception was:");
                        t.printStackTrace();
                        if (0 != 0) {
                            System.err.println("There has been an error prior to that:");
                            error.printStackTrace();
                        }
                        buildException = new BuildException(t);
                    }
                } else if (0 != 0) {
                    project.log(error.toString(), 0);
                }
                throw th;
            }
        }
    }

    private void setProperties(Project project) {
        project.init();
        PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(project);
        Map raw = new HashMap(this.definedProps);
        ResolvePropertyMap resolver = new ResolvePropertyMap(project, NOPROPERTIES, propertyHelper.getExpanders());
        resolver.resolveAllProperties(raw, null, false);
        raw.forEach(arg, value -> {
            project.setUserProperty(arg, String.valueOf(value));
        });
        project.setUserProperty(MagicNames.ANT_FILE, this.buildFile.getAbsolutePath());
        project.setUserProperty(MagicNames.ANT_FILE_TYPE, "file");
        project.setUserProperty(MagicNames.PROJECT_INVOKED_TARGETS, String.join(",", this.targets));
    }

    protected void addBuildListeners(Project project) {
        project.addBuildListener(createLogger());
        int count = this.listeners.size();
        for (int i = 0; i < count; i++) {
            String className = this.listeners.elementAt(i);
            BuildListener listener = (BuildListener) ClasspathUtils.newInstance(className, Main.class.getClassLoader(), BuildListener.class);
            project.setProjectReference(listener);
            project.addBuildListener(listener);
        }
    }

    private void addInputHandler(Project project) throws BuildException {
        InputHandler handler;
        if (this.inputHandlerClassname == null) {
            handler = new DefaultInputHandler();
        } else {
            handler = (InputHandler) ClasspathUtils.newInstance(this.inputHandlerClassname, Main.class.getClassLoader(), InputHandler.class);
            project.setProjectReference(handler);
        }
        project.setInputHandler(handler);
    }

    private BuildLogger createLogger() {
        BuildLogger logger;
        if (this.silent) {
            logger = new SilentLogger();
            this.msgOutputLevel = 1;
            this.emacsMode = true;
        } else if (this.loggerClassname != null) {
            try {
                logger = (BuildLogger) ClasspathUtils.newInstance(this.loggerClassname, Main.class.getClassLoader(), BuildLogger.class);
            } catch (BuildException e) {
                System.err.println("The specified logger class " + this.loggerClassname + " could not be used because " + e.getMessage());
                throw e;
            }
        } else {
            logger = new DefaultLogger();
        }
        logger.setMessageOutputLevel(this.msgOutputLevel);
        logger.setOutputPrintStream(this.out);
        logger.setErrorPrintStream(this.err);
        logger.setEmacsMode(this.emacsMode);
        return logger;
    }

    private static void printUsage() {
        System.out.println("ant [options] [target [target2 [target3] ...]]");
        System.out.println("Options: ");
        System.out.println("  -help, -h              print this message and exit");
        System.out.println("  -projecthelp, -p       print project help information and exit");
        System.out.println("  -version               print the version information and exit");
        System.out.println("  -diagnostics           print information that might be helpful to");
        System.out.println("                         diagnose or report problems and exit");
        System.out.println("  -quiet, -q             be extra quiet");
        System.out.println("  -silent, -S            print nothing but task outputs and build failures");
        System.out.println("  -verbose, -v           be extra verbose");
        System.out.println("  -debug, -d             print debugging information");
        System.out.println("  -emacs, -e             produce logging information without adornments");
        System.out.println("  -lib <path>            specifies a path to search for jars and classes");
        System.out.println("  -logfile <file>        use given file for log");
        System.out.println("    -l     <file>                ''");
        System.out.println("  -logger <classname>    the class which is to perform logging");
        System.out.println("  -listener <classname>  add an instance of class as a project listener");
        System.out.println("  -noinput               do not allow interactive input");
        System.out.println("  -buildfile <file>      use given buildfile");
        System.out.println("    -file    <file>              ''");
        System.out.println("    -f       <file>              ''");
        System.out.println("  -D<property>=<value>   use value for given property");
        System.out.println("  -keep-going, -k        execute all targets that do not depend");
        System.out.println("                         on failed target(s)");
        System.out.println("  -propertyfile <name>   load all properties from file with -D");
        System.out.println("                         properties taking precedence");
        System.out.println("  -inputhandler <class>  the class which will handle input requests");
        System.out.println("  -find <file>           (s)earch for buildfile towards the root of");
        System.out.println("    -s  <file>           the filesystem and use it");
        System.out.println("  -nice  number          A niceness value for the main thread:");
        System.out.println("                         1 (lowest) to 10 (highest); 5 is the default");
        System.out.println("  -nouserlib             Run ant without using the jar files from");
        System.out.println("                         ${user.home}/.ant/lib");
        System.out.println("  -noclasspath           Run ant without using CLASSPATH");
        System.out.println("  -autoproxy             Java1.5+: use the OS proxy settings");
        System.out.println("  -main <class>          override Ant's normal entry point");
        for (ArgumentProcessor processor : ArgumentProcessorRegistry.getInstance().getProcessors()) {
            processor.printUsage(System.out);
        }
    }

    private static void printVersion(int logLevel) throws BuildException {
        System.out.println(getAntVersion());
    }

    public static synchronized String getAntVersion() throws BuildException {
        if (antVersion == null) {
            try {
                Properties props = new Properties();
                InputStream in = Main.class.getResourceAsStream("/org/apache/tools/ant/version.txt");
                props.load(in);
                in.close();
                shortAntVersion = props.getProperty("VERSION");
                antVersion = "Apache Ant(TM) version " + shortAntVersion + " compiled on " + props.getProperty("DATE");
            } catch (IOException ioe) {
                throw new BuildException("Could not load the version information:" + ioe.getMessage());
            } catch (NullPointerException e) {
                throw new BuildException("Could not load the version information.");
            }
        }
        return antVersion;
    }

    public static String getShortAntVersion() throws BuildException {
        if (shortAntVersion == null) {
            getAntVersion();
        }
        return shortAntVersion;
    }

    private static void printDescription(Project project) {
        if (project.getDescription() != null) {
            project.log(project.getDescription());
        }
    }

    private static Map<String, Target> removeDuplicateTargets(Map<String, Target> targets) {
        Map<Location, Target> locationMap = new HashMap<>();
        targets.forEach(name, target -> {
            Target otherTarget = (Target) locationMap.get(target.getLocation());
            if (otherTarget == null || otherTarget.getName().length() > name.length()) {
                locationMap.put(target.getLocation(), target);
            }
        });
        return (Map) locationMap.values().stream().collect(Collectors.toMap((v0) -> {
            return v0.getName();
        }, target2 -> {
            return target2;
        }, a, b -> {
            return b;
        }));
    }

    private static void printTargets(Project project, boolean printSubTargets, boolean printDependencies) {
        int maxLength = 0;
        Map<String, Target> ptargets = removeDuplicateTargets(project.getTargets());
        Vector<String> topNames = new Vector<>();
        Vector<String> topDescriptions = new Vector<>();
        Vector<Enumeration<String>> topDependencies = new Vector<>();
        Vector<String> subNames = new Vector<>();
        Vector<Enumeration<String>> subDependencies = new Vector<>();
        for (Target currentTarget : ptargets.values()) {
            String targetName = currentTarget.getName();
            if (!targetName.isEmpty()) {
                String targetDescription = currentTarget.getDescription();
                if (targetDescription == null) {
                    int pos = findTargetPosition(subNames, targetName);
                    subNames.insertElementAt(targetName, pos);
                    if (printDependencies) {
                        subDependencies.insertElementAt(currentTarget.getDependencies(), pos);
                    }
                } else {
                    int pos2 = findTargetPosition(topNames, targetName);
                    topNames.insertElementAt(targetName, pos2);
                    topDescriptions.insertElementAt(targetDescription, pos2);
                    if (targetName.length() > maxLength) {
                        maxLength = targetName.length();
                    }
                    if (printDependencies) {
                        topDependencies.insertElementAt(currentTarget.getDependencies(), pos2);
                    }
                }
            }
        }
        printTargets(project, topNames, topDescriptions, topDependencies, "Main targets:", maxLength);
        if (topNames.isEmpty()) {
            printSubTargets = true;
        }
        if (printSubTargets) {
            printTargets(project, subNames, null, subDependencies, "Other targets:", 0);
        }
        String defaultTarget = project.getDefaultTarget();
        if (defaultTarget != null && !defaultTarget.isEmpty()) {
            project.log("Default target: " + defaultTarget);
        }
    }

    private static int findTargetPosition(Vector<String> names, String name) {
        int size = names.size();
        int res = size;
        for (int i = 0; i < size && res == size; i++) {
            if (name.compareTo(names.elementAt(i)) < 0) {
                res = i;
            }
        }
        return res;
    }

    private static void printTargets(Project project, Vector<String> names, Vector<String> descriptions, Vector<Enumeration<String>> dependencies, String heading, int maxlen) {
        String eol = System.lineSeparator();
        StringBuilder spaces = new StringBuilder(ASTNode.TAB);
        while (spaces.length() <= maxlen) {
            spaces.append((CharSequence) spaces);
        }
        StringBuilder msg = new StringBuilder();
        msg.append(heading).append(eol).append(eol);
        int size = names.size();
        for (int i = 0; i < size; i++) {
            msg.append(Instruction.argsep);
            msg.append(names.elementAt(i));
            if (descriptions != null) {
                msg.append(spaces.substring(0, (maxlen - names.elementAt(i).length()) + 2));
                msg.append(descriptions.elementAt(i));
            }
            msg.append(eol);
            if (!dependencies.isEmpty() && dependencies.elementAt(i).hasMoreElements()) {
                msg.append((String) StreamUtils.enumerationAsStream(dependencies.elementAt(i)).collect(Collectors.joining(", ", "   depends on: ", eol)));
            }
        }
        project.log(msg.toString(), 1);
    }
}
