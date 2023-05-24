package org.apache.tools.ant.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ScriptRunnerCreator.class */
public class ScriptRunnerCreator {
    private static final String AUTO = "auto";
    private static final String UTIL_OPT = "org.apache.tools.ant.util.optional";
    private static final String BSF = "bsf";
    private static final String BSF_PACK = "org.apache.bsf";
    private static final String BSF_MANAGER = "org.apache.bsf.BSFManager";
    private static final String BSF_RUNNER = "org.apache.tools.ant.util.optional.ScriptRunner";
    private static final String JAVAX = "javax";
    private static final String JAVAX_MANAGER = "javax.script.ScriptEngineManager";
    private static final String JAVAX_RUNNER = "org.apache.tools.ant.util.optional.JavaxScriptRunner";
    private Project project;
    private String manager;
    private String language;
    private ClassLoader scriptLoader = null;

    public ScriptRunnerCreator(Project project) {
        this.project = project;
    }

    public synchronized ScriptRunnerBase createRunner(String manager, String language, ClassLoader classLoader) {
        this.manager = manager;
        this.language = language;
        this.scriptLoader = classLoader;
        if (language == null) {
            throw new BuildException("script language must be specified");
        }
        if (!manager.equals("auto") && !manager.equals(JAVAX) && !manager.equals(BSF)) {
            throw new BuildException("Unsupported language prefix " + manager);
        }
        ScriptRunnerBase ret = createRunner(BSF, BSF_MANAGER, BSF_RUNNER);
        if (ret == null) {
            ret = createRunner(JAVAX, JAVAX_MANAGER, JAVAX_RUNNER);
        }
        if (ret != null) {
            return ret;
        }
        if (JAVAX.equals(manager)) {
            throw new BuildException("Unable to load the script engine manager (javax.script.ScriptEngineManager)");
        }
        if (BSF.equals(manager)) {
            throw new BuildException("Unable to load the BSF script engine manager (org.apache.bsf.BSFManager)");
        }
        throw new BuildException("Unable to load a script engine manager (org.apache.bsf.BSFManager or javax.script.ScriptEngineManager)");
    }

    private ScriptRunnerBase createRunner(String checkManager, String managerClass, String runnerClass) {
        if (!this.manager.equals("auto") && !this.manager.equals(checkManager)) {
            return null;
        }
        if (managerClass.equals(BSF_MANAGER)) {
            if (this.scriptLoader.getResource(LoaderUtils.classNameToResource(managerClass)) == null) {
                return null;
            }
            new ScriptFixBSFPath().fixClassLoader(this.scriptLoader, this.language);
        } else {
            try {
                Class.forName(managerClass, true, this.scriptLoader);
            } catch (Exception e) {
                return null;
            }
        }
        try {
            ScriptRunnerBase runner = (ScriptRunnerBase) Class.forName(runnerClass, true, this.scriptLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            runner.setProject(this.project);
            runner.setLanguage(this.language);
            runner.setScriptClassLoader(this.scriptLoader);
            return runner;
        } catch (Exception ex) {
            throw ReflectUtil.toBuildException(ex);
        }
    }
}
