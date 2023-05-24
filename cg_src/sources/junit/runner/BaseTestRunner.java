package junit.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Properties;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestSuite;
import org.apache.tools.ant.launch.Launcher;
import org.junit.internal.Throwables;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/runner/BaseTestRunner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/runner/BaseTestRunner.class */
public abstract class BaseTestRunner implements TestListener {
    public static final String SUITE_METHODNAME = "suite";
    private static Properties fPreferences;
    static int fgMaxMessageLength;
    static boolean fgFilterStack = true;
    boolean fLoading = true;

    public abstract void testStarted(String str);

    public abstract void testEnded(String str);

    public abstract void testFailed(int i, Test test, Throwable th);

    protected abstract void runFailed(String str);

    static {
        fgMaxMessageLength = 500;
        fgMaxMessageLength = getPreference("maxmessage", fgMaxMessageLength);
    }

    @Override // junit.framework.TestListener
    public synchronized void startTest(Test test) {
        testStarted(test.toString());
    }

    protected static void setPreferences(Properties preferences) {
        fPreferences = preferences;
    }

    protected static Properties getPreferences() {
        if (fPreferences == null) {
            fPreferences = new Properties();
            fPreferences.put("loading", "true");
            fPreferences.put("filterstack", "true");
            readPreferences();
        }
        return fPreferences;
    }

    public static void savePreferences() throws IOException {
        FileOutputStream fos = new FileOutputStream(getPreferencesFile());
        try {
            getPreferences().store(fos, "");
            fos.close();
        } catch (Throwable th) {
            fos.close();
            throw th;
        }
    }

    public static void setPreference(String key, String value) {
        getPreferences().put(key, value);
    }

    @Override // junit.framework.TestListener
    public synchronized void endTest(Test test) {
        testEnded(test.toString());
    }

    @Override // junit.framework.TestListener
    public synchronized void addError(Test test, Throwable e) {
        testFailed(1, test, e);
    }

    @Override // junit.framework.TestListener
    public synchronized void addFailure(Test test, AssertionFailedError e) {
        testFailed(2, test, e);
    }

    public Test getTest(String suiteClassName) {
        if (suiteClassName.length() <= 0) {
            clearStatus();
            return null;
        }
        try {
            Class<?> testClass = loadSuiteClass(suiteClassName);
            try {
                Method suiteMethod = testClass.getMethod("suite", new Class[0]);
                if (!Modifier.isStatic(suiteMethod.getModifiers())) {
                    runFailed("Suite() method must be static");
                    return null;
                }
                try {
                    Test test = (Test) suiteMethod.invoke(null, new Object[0]);
                    if (test == null) {
                        return test;
                    }
                    clearStatus();
                    return test;
                } catch (IllegalAccessException e) {
                    runFailed("Failed to invoke suite():" + e.toString());
                    return null;
                } catch (InvocationTargetException e2) {
                    runFailed("Failed to invoke suite():" + e2.getTargetException().toString());
                    return null;
                }
            } catch (Exception e3) {
                clearStatus();
                return new TestSuite(testClass);
            }
        } catch (ClassNotFoundException e4) {
            String clazz = e4.getMessage();
            if (clazz == null) {
                clazz = suiteClassName;
            }
            runFailed("Class not found \"" + clazz + "\"");
            return null;
        } catch (Exception e5) {
            runFailed("Error: " + e5.toString());
            return null;
        }
    }

    public String elapsedTimeAsString(long runTime) {
        return NumberFormat.getInstance().format(runTime / 1000.0d);
    }

    protected String processArguments(String[] args) {
        String suiteName = null;
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("-noloading")) {
                setLoading(false);
            } else if (args[i].equals("-nofilterstack")) {
                fgFilterStack = false;
            } else if (args[i].equals("-c")) {
                if (args.length > i + 1) {
                    suiteName = extractClassName(args[i + 1]);
                } else {
                    System.out.println("Missing Test class name");
                }
                i++;
            } else {
                suiteName = args[i];
            }
            i++;
        }
        return suiteName;
    }

    public void setLoading(boolean enable) {
        this.fLoading = enable;
    }

    public String extractClassName(String className) {
        if (className.startsWith("Default package for")) {
            return className.substring(className.lastIndexOf(".") + 1);
        }
        return className;
    }

    public static String truncate(String s) {
        if (fgMaxMessageLength != -1 && s.length() > fgMaxMessageLength) {
            s = s.substring(0, fgMaxMessageLength) + "...";
        }
        return s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<?> loadSuiteClass(String suiteClassName) throws ClassNotFoundException {
        return Class.forName(suiteClassName);
    }

    protected void clearStatus() {
    }

    protected boolean useReloadingTestSuiteLoader() {
        return getPreference("loading").equals("true") && this.fLoading;
    }

    private static File getPreferencesFile() {
        String home = System.getProperty(Launcher.USER_HOMEDIR);
        return new File(home, "junit.properties");
    }

    private static void readPreferences() {
        InputStream is = null;
        try {
            is = new FileInputStream(getPreferencesFile());
            setPreferences(new Properties(getPreferences()));
            getPreferences().load(is);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        } catch (SecurityException e4) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e5) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e6) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public static String getPreference(String key) {
        return getPreferences().getProperty(key);
    }

    public static int getPreference(String key, int dflt) {
        String value = getPreference(key);
        int intValue = dflt;
        if (value == null) {
            return intValue;
        }
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        return intValue;
    }

    public static String getFilteredTrace(Throwable e) {
        return getFilteredTrace(Throwables.getStacktrace(e));
    }

    public static String getFilteredTrace(String stack) {
        if (showStackRaw()) {
            return stack;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        StringReader sr = new StringReader(stack);
        BufferedReader br = new BufferedReader(sr);
        while (true) {
            try {
                String line = br.readLine();
                if (line != null) {
                    if (!filterLine(line)) {
                        pw.println(line);
                    }
                } else {
                    return sw.toString();
                }
            } catch (Exception e) {
                return stack;
            }
        }
    }

    protected static boolean showStackRaw() {
        return (getPreference("filterstack").equals("true") && fgFilterStack) ? false : true;
    }

    static boolean filterLine(String line) {
        String[] patterns = {"junit.framework.TestCase", "junit.framework.TestResult", "junit.framework.TestSuite", "junit.framework.Assert.", "junit.swingui.TestRunner", "junit.awtui.TestRunner", "junit.textui.TestRunner", "java.lang.reflect.Method.invoke("};
        for (String str : patterns) {
            if (line.indexOf(str) > 0) {
                return true;
            }
        }
        return false;
    }
}
