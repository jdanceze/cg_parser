package org.apache.tools.ant.util.optional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.ScriptRunnerBase;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/optional/JavaxScriptRunner.class */
public class JavaxScriptRunner extends ScriptRunnerBase {
    private ScriptEngine keptEngine;
    private CompiledScript compiledScript;
    private static final String DROP_GRAAL_SECURITY_RESTRICTIONS = "polyglot.js.allowAllAccess";
    private static final String ENABLE_NASHORN_COMPAT_IN_GRAAL = "polyglot.js.nashorn-compat";
    private static final List<String> JS_LANGUAGES = Arrays.asList("js", "javascript");

    @Override // org.apache.tools.ant.util.ScriptRunnerBase
    public String getManagerName() {
        return "javax";
    }

    @Override // org.apache.tools.ant.util.ScriptRunnerBase
    public boolean supportsLanguage() {
        if (this.keptEngine != null) {
            return true;
        }
        checkLanguage();
        ClassLoader origLoader = replaceContextLoader();
        try {
            boolean z = createEngine() != null;
            restoreContextLoader(origLoader);
            return z;
        } catch (Exception e) {
            restoreContextLoader(origLoader);
            return false;
        } catch (Throwable th) {
            restoreContextLoader(origLoader);
            throw th;
        }
    }

    @Override // org.apache.tools.ant.util.ScriptRunnerBase
    public void executeScript(String execName) throws BuildException {
        evaluateScript(execName);
    }

    @Override // org.apache.tools.ant.util.ScriptRunnerBase
    public Object evaluateScript(String execName) throws BuildException {
        checkLanguage();
        ClassLoader origLoader = replaceContextLoader();
        try {
            try {
                try {
                    if (getCompiled()) {
                        String compiledScriptRefName = String.format("%s.%s.%d.%d", MagicNames.SCRIPT_CACHE, getLanguage(), Integer.valueOf(Objects.hashCode(getScript())), Integer.valueOf(Objects.hashCode(getClass().getClassLoader())));
                        if (null == this.compiledScript) {
                            this.compiledScript = (CompiledScript) getProject().getReference(compiledScriptRefName);
                        }
                        if (null == this.compiledScript) {
                            Compilable createEngine = createEngine();
                            if (createEngine == null) {
                                throw new BuildException("Unable to create javax script engine for %s", getLanguage());
                            }
                            if (createEngine instanceof Compilable) {
                                getProject().log("compile script " + execName, 3);
                                this.compiledScript = createEngine.compile(getScript());
                            } else {
                                getProject().log("script compilation not available for " + execName, 3);
                                this.compiledScript = null;
                            }
                            getProject().addReference(compiledScriptRefName, this.compiledScript);
                        }
                        if (null != this.compiledScript) {
                            SimpleBindings simpleBindings = new SimpleBindings();
                            Objects.requireNonNull(simpleBindings);
                            applyBindings(this::put);
                            getProject().log("run compiled script " + compiledScriptRefName, 4);
                            Object eval = this.compiledScript.eval(simpleBindings);
                            restoreContextLoader(origLoader);
                            return eval;
                        }
                    }
                    ScriptEngine engine = createEngine();
                    if (engine == null) {
                        throw new BuildException("Unable to create javax script engine for " + getLanguage());
                    }
                    Objects.requireNonNull(engine);
                    applyBindings(this::put);
                    Object eval2 = engine.eval(getScript());
                    restoreContextLoader(origLoader);
                    return eval2;
                } catch (BuildException be) {
                    throw unwrap(be);
                }
            } catch (Exception be2) {
                Throwable t = be2;
                Throwable te = be2.getCause();
                if (te != null) {
                    if (te instanceof BuildException) {
                        throw ((BuildException) te);
                    }
                    t = te;
                }
                throw new BuildException(t);
            }
        } catch (Throwable th) {
            restoreContextLoader(origLoader);
            throw th;
        }
    }

    private void applyBindings(BiConsumer<String, Object> target) {
        Map<String, Object> source = getBeans();
        if ("FX".equalsIgnoreCase(getLanguage())) {
            source = (Map) source.entrySet().stream().collect(Collectors.toMap(e -> {
                return String.format("%s:%s", e.getKey(), e.getValue().getClass().getName());
            }, (v0) -> {
                return v0.getValue();
            }));
        }
        source.forEach(target);
    }

    private ScriptEngine createEngine() {
        if (this.keptEngine != null) {
            return this.keptEngine;
        }
        if (languageIsJavaScript()) {
            maybeEnableNashornCompatibility();
        }
        ScriptEngine result = new ScriptEngineManager().getEngineByName(getLanguage());
        if (result == null && JavaEnvUtils.isAtLeastJavaVersion("15") && languageIsJavaScript()) {
            getProject().log("Java 15 has removed Nashorn, you must provide an engine for running JavaScript yourself. GraalVM JavaScript currently is the preferred option.", 1);
        }
        maybeApplyGraalJsProperties(result);
        if (result != null && getKeepEngine()) {
            this.keptEngine = result;
        }
        return result;
    }

    private void maybeApplyGraalJsProperties(ScriptEngine engine) {
        if (engine != null && engine.getClass().getName().contains("Graal")) {
            engine.getBindings(100).put(DROP_GRAAL_SECURITY_RESTRICTIONS, true);
        }
    }

    private void maybeEnableNashornCompatibility() {
        if (getProject() != null) {
            System.setProperty(ENABLE_NASHORN_COMPAT_IN_GRAAL, Project.toBoolean(getProject().getProperty(MagicNames.DISABLE_NASHORN_COMPAT)) ? "false" : "true");
        }
    }

    private boolean languageIsJavaScript() {
        return JS_LANGUAGES.contains(getLanguage());
    }

    private static BuildException unwrap(Throwable t) {
        BuildException deepest = t instanceof BuildException ? (BuildException) t : null;
        Throwable current = t;
        while (current.getCause() != null) {
            current = current.getCause();
            if (current instanceof BuildException) {
                deepest = (BuildException) current;
            }
        }
        return deepest;
    }
}
