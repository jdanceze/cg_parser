package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.LoaderUtils;
import org.apache.tools.ant.util.StreamUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ProjectHelperRepository.class */
public class ProjectHelperRepository {
    private static final String DEBUG_PROJECT_HELPER_REPOSITORY = "ant.project-helper-repo.debug";
    private static final boolean DEBUG = "true".equals(System.getProperty(DEBUG_PROJECT_HELPER_REPOSITORY));
    private static ProjectHelperRepository instance = new ProjectHelperRepository();
    private List<Constructor<? extends ProjectHelper>> helpers = new ArrayList();
    private static Constructor<ProjectHelper2> PROJECTHELPER2_CONSTRUCTOR;

    static {
        try {
            PROJECTHELPER2_CONSTRUCTOR = ProjectHelper2.class.getConstructor(new Class[0]);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public static ProjectHelperRepository getInstance() {
        return instance;
    }

    private ProjectHelperRepository() {
        collectProjectHelpers();
    }

    private void collectProjectHelpers() {
        registerProjectHelper(getProjectHelperBySystemProperty());
        try {
            ClassLoader classLoader = LoaderUtils.getContextClassLoader();
            if (classLoader != null) {
                Iterator it = Collections.list(classLoader.getResources("META-INF/services/org.apache.tools.ant.ProjectHelper")).iterator();
                while (it.hasNext()) {
                    URL resource = (URL) it.next();
                    URLConnection conn = resource.openConnection();
                    conn.setUseCaches(false);
                    registerProjectHelper(getProjectHelperByService(conn.getInputStream()));
                }
            }
            InputStream systemResource = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ProjectHelper");
            if (systemResource != null) {
                registerProjectHelper(getProjectHelperByService(systemResource));
            }
        } catch (Exception e) {
            System.err.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            if (DEBUG) {
                e.printStackTrace(System.err);
            }
        }
    }

    public void registerProjectHelper(String helperClassName) throws BuildException {
        registerProjectHelper(getHelperConstructor(helperClassName));
    }

    public void registerProjectHelper(Class<? extends ProjectHelper> helperClass) throws BuildException {
        try {
            registerProjectHelper(helperClass.getConstructor(new Class[0]));
        } catch (NoSuchMethodException e) {
            throw new BuildException("Couldn't find no-arg constructor in " + helperClass.getName());
        }
    }

    private void registerProjectHelper(Constructor<? extends ProjectHelper> helperConstructor) {
        if (helperConstructor == null) {
            return;
        }
        if (DEBUG) {
            System.out.println("ProjectHelper " + helperConstructor.getClass().getName() + " registered.");
        }
        this.helpers.add(helperConstructor);
    }

    private Constructor<? extends ProjectHelper> getProjectHelperBySystemProperty() {
        String helperClass = System.getProperty("org.apache.tools.ant.ProjectHelper");
        if (helperClass != null) {
            try {
                return getHelperConstructor(helperClass);
            } catch (SecurityException e) {
                System.err.println("Unable to load ProjectHelper class \"" + helperClass + " specified in system property org.apache.tools.ant.ProjectHelper (" + e.getMessage() + ")");
                if (DEBUG) {
                    e.printStackTrace(System.err);
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    private Constructor<? extends ProjectHelper> getProjectHelperByService(InputStream is) {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String helperClassName = rd.readLine();
            rd.close();
            if (helperClassName != null && !helperClassName.isEmpty()) {
                return getHelperConstructor(helperClassName);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e.getMessage() + ")");
            if (DEBUG) {
                e.printStackTrace(System.err);
                return null;
            }
            return null;
        }
    }

    private Constructor<? extends ProjectHelper> getHelperConstructor(String helperClass) throws BuildException {
        ClassLoader classLoader = LoaderUtils.getContextClassLoader();
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                try {
                    clazz = classLoader.loadClass(helperClass);
                } catch (Exception e) {
                    throw new BuildException(e);
                }
            } catch (ClassNotFoundException e2) {
            }
        }
        if (clazz == null) {
            clazz = Class.forName(helperClass);
        }
        return clazz.asSubclass(ProjectHelper.class).getConstructor(new Class[0]);
    }

    public ProjectHelper getProjectHelperForBuildFile(Resource buildFile) throws BuildException {
        ProjectHelper ph = (ProjectHelper) StreamUtils.iteratorAsStream(getHelpers()).filter(helper -> {
            return helper.canParseBuildFile(buildFile);
        }).findFirst().orElse(null);
        if (ph == null) {
            throw new BuildException("BUG: at least the ProjectHelper2 should have supported the file " + buildFile);
        }
        if (DEBUG) {
            System.out.println("ProjectHelper " + ph.getClass().getName() + " selected for the build file " + buildFile);
        }
        return ph;
    }

    public ProjectHelper getProjectHelperForAntlib(Resource antlib) throws BuildException {
        ProjectHelper ph = (ProjectHelper) StreamUtils.iteratorAsStream(getHelpers()).filter(helper -> {
            return helper.canParseAntlibDescriptor(antlib);
        }).findFirst().orElse(null);
        if (ph == null) {
            throw new BuildException("BUG: at least the ProjectHelper2 should have supported the file " + antlib);
        }
        if (DEBUG) {
            System.out.println("ProjectHelper " + ph.getClass().getName() + " selected for the antlib " + antlib);
        }
        return ph;
    }

    public Iterator<ProjectHelper> getHelpers() {
        Stream.Builder<Constructor<? extends ProjectHelper>> b = Stream.builder();
        List<Constructor<? extends ProjectHelper>> list = this.helpers;
        Objects.requireNonNull(b);
        list.forEach((v1) -> {
            r1.add(v1);
        });
        Stream map = b.add(PROJECTHELPER2_CONSTRUCTOR).build().map(c -> {
            try {
                return (ProjectHelper) c.newInstance(new Object[0]);
            } catch (Exception e) {
                throw new BuildException("Failed to invoke no-arg constructor on " + c.getName());
            }
        });
        Objects.requireNonNull(ProjectHelper.class);
        return map.map((v1) -> {
            return r1.cast(v1);
        }).iterator();
    }
}
