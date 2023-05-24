package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.util.LoaderUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ArgumentProcessorRegistry.class */
public class ArgumentProcessorRegistry {
    private static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ArgumentProcessor";
    private List<ArgumentProcessor> processors = new ArrayList();
    private static final String DEBUG_ARGUMENT_PROCESSOR_REPOSITORY = "ant.argument-processor-repo.debug";
    private static final boolean DEBUG = "true".equals(System.getProperty(DEBUG_ARGUMENT_PROCESSOR_REPOSITORY));
    private static ArgumentProcessorRegistry instance = new ArgumentProcessorRegistry();

    public static ArgumentProcessorRegistry getInstance() {
        return instance;
    }

    private ArgumentProcessorRegistry() {
        collectArgumentProcessors();
    }

    public List<ArgumentProcessor> getProcessors() {
        return this.processors;
    }

    private void collectArgumentProcessors() {
        try {
            ClassLoader classLoader = LoaderUtils.getContextClassLoader();
            if (classLoader != null) {
                Iterator it = Collections.list(classLoader.getResources(SERVICE_ID)).iterator();
                while (it.hasNext()) {
                    URL resource = (URL) it.next();
                    URLConnection conn = resource.openConnection();
                    conn.setUseCaches(false);
                    ArgumentProcessor processor = getProcessorByService(conn.getInputStream());
                    registerArgumentProcessor(processor);
                }
            }
            InputStream systemResource = ClassLoader.getSystemResourceAsStream(SERVICE_ID);
            if (systemResource != null) {
                ArgumentProcessor processor2 = getProcessorByService(systemResource);
                registerArgumentProcessor(processor2);
            }
        } catch (Exception e) {
            System.err.println("Unable to load ArgumentProcessor from service META-INF/services/org.apache.tools.ant.ArgumentProcessor (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            if (DEBUG) {
                e.printStackTrace(System.err);
            }
        }
    }

    public void registerArgumentProcessor(String helperClassName) throws BuildException {
        registerArgumentProcessor(getProcessor(helperClassName));
    }

    public void registerArgumentProcessor(Class<? extends ArgumentProcessor> helperClass) throws BuildException {
        registerArgumentProcessor(getProcessor(helperClass));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ArgumentProcessor getProcessor(String helperClassName) {
        try {
            return getProcessor((Class<? extends ArgumentProcessor>) Class.forName(helperClassName));
        } catch (ClassNotFoundException e) {
            throw new BuildException("Argument processor class " + helperClassName + " was not found", e);
        }
    }

    private ArgumentProcessor getProcessor(Class<? extends ArgumentProcessor> processorClass) {
        try {
            ArgumentProcessor processor = processorClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            return processor;
        } catch (Exception e) {
            throw new BuildException("The argument processor class" + processorClass.getName() + " could not be instantiated with a default constructor", e);
        }
    }

    public void registerArgumentProcessor(ArgumentProcessor processor) {
        if (processor == null) {
            return;
        }
        this.processors.add(processor);
        if (DEBUG) {
            System.out.println("Argument processor " + processor.getClass().getName() + " registered.");
        }
    }

    private ArgumentProcessor getProcessorByService(InputStream is) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        try {
            String processorClassName = rd.readLine();
            if (processorClassName != null && !processorClassName.isEmpty()) {
                ArgumentProcessor processor = getProcessor(processorClassName);
                rd.close();
                return processor;
            }
            rd.close();
            return null;
        } catch (Throwable th) {
            try {
                rd.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
