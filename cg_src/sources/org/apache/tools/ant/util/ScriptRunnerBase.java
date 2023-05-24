package org.apache.tools.ant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.PropertyResource;
import org.apache.tools.ant.types.resources.StringResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ScriptRunnerBase.class */
public abstract class ScriptRunnerBase {
    private String language;
    private String encoding;
    private boolean compiled;
    private Project project;
    private ClassLoader scriptLoader;
    private boolean keepEngine = false;
    private String script = "";
    private final Map<String, Object> beans = new HashMap();

    public abstract void executeScript(String str);

    public abstract Object evaluateScript(String str);

    public abstract boolean supportsLanguage();

    public abstract String getManagerName();

    public void addBeans(Map<String, ?> dictionary) {
        dictionary.forEach(k, v -> {
            try {
                addBean(k, v);
            } catch (BuildException e) {
            }
        });
    }

    public void addBean(String key, Object bean) {
        if (!key.isEmpty() && Character.isJavaIdentifierStart(key.charAt(0)) && key.chars().skip(1L).allMatch(Character::isJavaIdentifierPart)) {
            this.beans.put(key, bean);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, Object> getBeans() {
        return this.beans;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setScriptClassLoader(ClassLoader classLoader) {
        this.scriptLoader = classLoader;
    }

    protected ClassLoader getScriptClassLoader() {
        return this.scriptLoader;
    }

    public void setKeepEngine(boolean keepEngine) {
        this.keepEngine = keepEngine;
    }

    public boolean getKeepEngine() {
        return this.keepEngine;
    }

    public final void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public final boolean getCompiled() {
        return this.compiled;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setSrc(File file) {
        String filename = file.getPath();
        if (!file.exists()) {
            throw new BuildException("file " + filename + " not found.");
        }
        try {
            InputStream in = Files.newInputStream(file.toPath(), new OpenOption[0]);
            Charset charset = null == this.encoding ? Charset.defaultCharset() : Charset.forName(this.encoding);
            readSource(in, filename, charset);
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            throw new BuildException("file " + filename + " not found.", e);
        }
    }

    private void readSource(InputStream in, String name, Charset charset) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, charset));
            this.script += FileUtils.safeReadFully(reader);
            reader.close();
        } catch (IOException ex) {
            throw new BuildException("Failed to read " + name, ex);
        }
    }

    public void loadResource(Resource sourceResource) {
        if (sourceResource instanceof StringResource) {
            this.script += ((StringResource) sourceResource).getValue();
        } else if (sourceResource instanceof PropertyResource) {
            this.script += ((PropertyResource) sourceResource).getValue();
        } else {
            String name = sourceResource.toLongString();
            try {
                InputStream in = sourceResource.getInputStream();
                try {
                    readSource(in, name, Charset.defaultCharset());
                    if (in != null) {
                        in.close();
                    }
                } catch (Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                throw new BuildException("Failed to open " + name, e);
            } catch (UnsupportedOperationException e2) {
                throw new BuildException("Failed to open " + name + " - it is not readable", e2);
            }
        }
    }

    public void loadResources(ResourceCollection collection) {
        collection.forEach(this::loadResource);
    }

    public void addText(String text) {
        this.script += text;
    }

    public String getScript() {
        return this.script;
    }

    public void clearScript() {
        this.script = "";
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    public void bindToComponent(ProjectComponent component) {
        this.project = component.getProject();
        addBeans(this.project.getProperties());
        addBeans(this.project.getUserProperties());
        addBeans(this.project.getCopyOfTargets());
        addBeans(this.project.getCopyOfReferences());
        addBean("project", this.project);
        addBean("self", component);
    }

    public void bindToComponentMinimum(ProjectComponent component) {
        this.project = component.getProject();
        addBean("project", this.project);
        addBean("self", component);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkLanguage() {
        if (this.language == null) {
            throw new BuildException("script language must be specified");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassLoader replaceContextLoader() {
        ClassLoader origContextClassLoader = Thread.currentThread().getContextClassLoader();
        if (getScriptClassLoader() == null) {
            setScriptClassLoader(getClass().getClassLoader());
        }
        Thread.currentThread().setContextClassLoader(getScriptClassLoader());
        return origContextClassLoader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void restoreContextLoader(ClassLoader origLoader) {
        Thread.currentThread().setContextClassLoader(origLoader);
    }
}
