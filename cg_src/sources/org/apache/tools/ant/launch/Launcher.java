package org.apache.tools.ant.launch;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/* loaded from: gencallgraphv3.jar:ant-launcher-1.10.11.jar:org/apache/tools/ant/launch/Launcher.class */
public class Launcher {
    public static final String ANTHOME_PROPERTY = "ant.home";
    public static final String ANTLIBDIR_PROPERTY = "ant.library.dir";
    public static final String ANT_PRIVATEDIR = ".ant";
    public static final String ANT_PRIVATELIB = "lib";
    public static final String USER_LIBDIR = ANT_PRIVATEDIR + File.separatorChar + ANT_PRIVATELIB;
    public static final String MAIN_CLASS = "org.apache.tools.ant.Main";
    public static final String USER_HOMEDIR = "user.home";
    private static final String JAVA_CLASS_PATH = "java.class.path";
    protected static final int EXIT_CODE_ERROR = 2;
    public boolean launchDiag = false;

    public static void main(String[] args) {
        int exitCode;
        boolean launchDiag = false;
        try {
            Launcher launcher = new Launcher();
            exitCode = launcher.run(args);
            launchDiag = launcher.launchDiag;
        } catch (LaunchException e) {
            exitCode = 2;
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            exitCode = 2;
            t.printStackTrace(System.err);
        }
        if (exitCode != 0) {
            if (launchDiag) {
                System.out.println("Exit code: " + exitCode);
            }
            System.exit(exitCode);
        }
    }

    private Launcher() {
    }

    private void addPath(String path, boolean getJars, List<URL> libPathURLs) throws MalformedURLException {
        URL[] locationURLs;
        StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);
        while (tokenizer.hasMoreElements()) {
            String elementName = tokenizer.nextToken();
            File element = new File(elementName);
            if (!elementName.contains("%") || element.exists()) {
                if (getJars && element.isDirectory()) {
                    for (URL dirURL : Locator.getLocationURLs(element)) {
                        if (this.launchDiag) {
                            System.out.println("adding library JAR: " + dirURL);
                        }
                        libPathURLs.add(dirURL);
                    }
                }
                URL url = new URL(element.toURI().toASCIIString());
                if (this.launchDiag) {
                    System.out.println("adding library URL: " + url);
                }
                libPathURLs.add(url);
            }
        }
    }

    private int run(String[] args) throws LaunchException, MalformedURLException {
        String[] newArgs;
        String antHomeProperty = System.getProperty("ant.home");
        File antHome = null;
        File sourceJar = Locator.getClassSource(getClass());
        File jarDir = sourceJar.getParentFile();
        String mainClassname = MAIN_CLASS;
        if (antHomeProperty != null) {
            antHome = new File(antHomeProperty);
        }
        if (antHome == null || !antHome.exists()) {
            antHome = jarDir.getParentFile();
            setProperty("ant.home", antHome.getAbsolutePath());
        }
        if (!antHome.exists()) {
            throw new LaunchException("Ant home is set incorrectly or ant could not be located (estimated value=" + antHome.getAbsolutePath() + ")");
        }
        List<String> libPaths = new ArrayList<>();
        String cpString = null;
        List<String> argList = new ArrayList<>();
        boolean noUserLib = false;
        boolean noClassPath = false;
        int i = 0;
        while (i < args.length) {
            if ("-lib".equals(args[i])) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -lib argument must be followed by a library location");
                }
                i++;
                libPaths.add(args[i]);
            } else if ("-cp".equals(args[i])) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -cp argument must be followed by a classpath expression");
                }
                if (cpString != null) {
                    throw new LaunchException("The -cp argument must not be repeated");
                }
                i++;
                cpString = args[i];
            } else if ("--nouserlib".equals(args[i]) || "-nouserlib".equals(args[i])) {
                noUserLib = true;
            } else if ("--launchdiag".equals(args[i])) {
                this.launchDiag = true;
            } else if ("--noclasspath".equals(args[i]) || "-noclasspath".equals(args[i])) {
                noClassPath = true;
            } else if ("-main".equals(args[i])) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -main argument must be followed by a library location");
                }
                i++;
                mainClassname = args[i];
            } else {
                argList.add(args[i]);
            }
            i++;
        }
        logPath("Launcher JAR", sourceJar);
        logPath("Launcher JAR directory", sourceJar.getParentFile());
        logPath("java.home", new File(System.getProperty("java.home")));
        if (argList.size() == args.length) {
            newArgs = args;
        } else {
            newArgs = (String[]) argList.toArray(new String[argList.size()]);
        }
        URL[] libURLs = getLibPathURLs(noClassPath ? null : cpString, libPaths);
        URL[] systemURLs = getSystemURLs(jarDir);
        URL[] userURLs = noUserLib ? new URL[0] : getUserURLs();
        File toolsJAR = Locator.getToolsJar();
        logPath("tools.jar", toolsJAR);
        URL[] jars = getJarArray(libURLs, userURLs, systemURLs, toolsJAR);
        StringBuilder baseClassPath = new StringBuilder(System.getProperty(JAVA_CLASS_PATH));
        if (baseClassPath.charAt(baseClassPath.length() - 1) == File.pathSeparatorChar) {
            baseClassPath.setLength(baseClassPath.length() - 1);
        }
        for (URL jar : jars) {
            baseClassPath.append(File.pathSeparatorChar);
            baseClassPath.append(Locator.fromURI(jar.toString()));
        }
        setProperty(JAVA_CLASS_PATH, baseClassPath.toString());
        URLClassLoader loader = new URLClassLoader(jars, Launcher.class.getClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
        Class<? extends AntMain> mainClass = null;
        int exitCode = 0;
        Throwable thrown = null;
        try {
            mainClass = loader.loadClass(mainClassname).asSubclass(AntMain.class);
            AntMain main = (AntMain) mainClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            main.startAnt(newArgs, null, null);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Failed to locate" + mainClassname);
            thrown = cnfe;
        } catch (InstantiationException ex) {
            System.err.println("Incompatible version of " + mainClassname + " detected");
            File mainJar = Locator.getClassSource(mainClass);
            System.err.println("Location of this class " + mainJar);
            thrown = ex;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            thrown = t;
        }
        if (thrown != null) {
            System.err.println("ant.home: " + antHome.getAbsolutePath());
            System.err.println("Classpath: " + baseClassPath.toString());
            System.err.println("Launcher JAR: " + sourceJar.getAbsolutePath());
            System.err.println("Launcher Directory: " + jarDir.getAbsolutePath());
            exitCode = 2;
        }
        return exitCode;
    }

    private URL[] getLibPathURLs(String cpString, List<String> libPaths) throws MalformedURLException {
        List<URL> libPathURLs = new ArrayList<>();
        if (cpString != null) {
            addPath(cpString, false, libPathURLs);
        }
        for (String libPath : libPaths) {
            addPath(libPath, true, libPathURLs);
        }
        return (URL[]) libPathURLs.toArray(new URL[libPathURLs.size()]);
    }

    private URL[] getSystemURLs(File antLauncherDir) throws MalformedURLException {
        File antLibDir = null;
        String antLibDirProperty = System.getProperty(ANTLIBDIR_PROPERTY);
        if (antLibDirProperty != null) {
            antLibDir = new File(antLibDirProperty);
        }
        if (antLibDir == null || !antLibDir.exists()) {
            antLibDir = antLauncherDir;
            setProperty(ANTLIBDIR_PROPERTY, antLibDir.getAbsolutePath());
        }
        return Locator.getLocationURLs(antLibDir);
    }

    private URL[] getUserURLs() throws MalformedURLException {
        File userLibDir = new File(System.getProperty(USER_HOMEDIR), USER_LIBDIR);
        return Locator.getLocationURLs(userLibDir);
    }

    private URL[] getJarArray(URL[] libJars, URL[] userJars, URL[] systemJars, File toolsJar) throws MalformedURLException {
        int numJars = libJars.length + userJars.length + systemJars.length;
        if (toolsJar != null) {
            numJars++;
        }
        URL[] jars = new URL[numJars];
        System.arraycopy(libJars, 0, jars, 0, libJars.length);
        System.arraycopy(userJars, 0, jars, libJars.length, userJars.length);
        System.arraycopy(systemJars, 0, jars, userJars.length + libJars.length, systemJars.length);
        if (toolsJar != null) {
            jars[jars.length - 1] = new URL(toolsJar.toURI().toASCIIString());
        }
        return jars;
    }

    private void setProperty(String name, String value) {
        if (this.launchDiag) {
            System.out.println("Setting \"" + name + "\" to \"" + value + "\"");
        }
        System.setProperty(name, value);
    }

    private void logPath(String name, File path) {
        if (this.launchDiag) {
            System.out.println(name + "= \"" + path + "\"");
        }
    }
}
