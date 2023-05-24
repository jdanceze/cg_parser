package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.property.ResolvePropertyMap;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Property.class */
public class Property extends Task {
    protected String name;
    protected String value;
    protected File file;
    protected URL url;
    protected String resource;
    protected Path classpath;
    protected String env;
    protected Reference ref;
    protected String prefix;
    private String runtime;
    private Project fallback;
    private Object untypedValue;
    private boolean valueAttributeUsed;
    private boolean relative;
    private File basedir;
    private boolean prefixValues;
    protected boolean userProperty;

    public Property() {
        this(false);
    }

    protected Property(boolean userProperty) {
        this(userProperty, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Property(boolean userProperty, Project fallback) {
        this.valueAttributeUsed = false;
        this.relative = false;
        this.prefixValues = false;
        this.userProperty = userProperty;
        this.fallback = fallback;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLocation(File location) {
        if (this.relative) {
            internalSetValue(location);
        } else {
            setValue(location.getAbsolutePath());
        }
    }

    public void setValue(Object value) {
        this.valueAttributeUsed = true;
        internalSetValue(value);
    }

    private void internalSetValue(Object value) {
        this.untypedValue = value;
        this.value = value == null ? null : value.toString();
    }

    public void setValue(String value) {
        setValue((Object) value);
    }

    public void addText(String msg) {
        if (this.valueAttributeUsed) {
            if (!msg.trim().isEmpty()) {
                throw new BuildException("can't combine nested text with value attribute");
            }
            return;
        }
        String msg2 = getProject().replaceProperties(msg);
        String currentValue = getValue();
        if (currentValue != null) {
            msg2 = currentValue + msg2;
        }
        internalSetValue(msg2);
    }

    public String getValue() {
        return this.value;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if (prefix != null && !prefix.endsWith(".")) {
            this.prefix += ".";
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefixValues(boolean b) {
        this.prefixValues = b;
    }

    public boolean getPrefixValues() {
        return this.prefixValues;
    }

    public void setRefid(Reference ref) {
        this.ref = ref;
    }

    public Reference getRefid() {
        return this.ref;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }

    public void setEnvironment(String env) {
        this.env = env;
    }

    public String getEnvironment() {
        return this.env;
    }

    public void setRuntime(String prefix) {
        this.runtime = prefix;
    }

    public String getRuntime() {
        return this.runtime;
    }

    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public Path getClasspath() {
        return this.classpath;
    }

    @Deprecated
    public void setUserProperty(boolean userProperty) {
        log("DEPRECATED: Ignoring request to set user property in Property task.", 1);
    }

    public String toString() {
        return this.value == null ? "" : this.value;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (getProject() == null) {
            throw new IllegalStateException("project has not been set");
        }
        if (this.name != null) {
            if (this.untypedValue == null && this.ref == null) {
                throw new BuildException("You must specify value, location or refid with the name attribute", getLocation());
            }
        } else if (this.url == null && this.file == null && this.resource == null && this.env == null && this.runtime == null) {
            throw new BuildException("You must specify url, file, resource, environment or runtime when not using the name attribute", getLocation());
        }
        if (this.url == null && this.file == null && this.resource == null && this.prefix != null) {
            throw new BuildException("Prefix is only valid when loading from a url, file or resource", getLocation());
        }
        if (this.name != null && this.untypedValue != null) {
            if (this.relative) {
                try {
                    File from = this.untypedValue instanceof File ? (File) this.untypedValue : new File(this.untypedValue.toString());
                    File to = this.basedir != null ? this.basedir : getProject().getBaseDir();
                    String relPath = FileUtils.getRelativePath(to, from);
                    addProperty(this.name, relPath.replace('/', File.separatorChar));
                } catch (Exception e) {
                    throw new BuildException(e, getLocation());
                }
            } else {
                addProperty(this.name, this.untypedValue);
            }
        }
        if (this.file != null) {
            loadFile(this.file);
        }
        if (this.url != null) {
            loadUrl(this.url);
        }
        if (this.resource != null) {
            loadResource(this.resource);
        }
        if (this.env != null) {
            loadEnvironment(this.env);
        }
        if (this.runtime != null) {
            loadRuntime(this.runtime);
        }
        if (this.name != null && this.ref != null) {
            try {
                addProperty(this.name, this.ref.getReferencedObject(getProject()).toString());
            } catch (BuildException be) {
                if (this.fallback != null) {
                    addProperty(this.name, this.ref.getReferencedObject(this.fallback).toString());
                    return;
                }
                throw be;
            }
        }
    }

    protected void loadUrl(URL url) throws BuildException {
        Properties props = new Properties();
        log("Loading " + url, 3);
        try {
            InputStream is = url.openStream();
            loadProperties(props, is, url.getFile().endsWith(".xml"));
            if (is != null) {
                is.close();
            }
            addProperties(props);
        } catch (IOException ex) {
            throw new BuildException(ex, getLocation());
        }
    }

    private void loadProperties(Properties props, InputStream is, boolean isXml) throws IOException {
        if (isXml) {
            props.loadFromXML(is);
        } else {
            props.load(is);
        }
    }

    protected void loadFile(File file) throws BuildException {
        Properties props = new Properties();
        log("Loading " + file.getAbsolutePath(), 3);
        try {
            if (file.exists()) {
                InputStream fis = Files.newInputStream(file.toPath(), new OpenOption[0]);
                loadProperties(props, fis, file.getName().endsWith(".xml"));
                if (fis != null) {
                    fis.close();
                }
                addProperties(props);
            } else {
                log("Unable to find property file: " + file.getAbsolutePath(), 3);
            }
        } catch (IOException ex) {
            throw new BuildException(ex, getLocation());
        }
    }

    protected void loadResource(String name) {
        Properties props = new Properties();
        log("Resource Loading " + name, 3);
        ClassLoader cL = this.classpath == null ? getClass().getClassLoader() : getProject().createClassLoader(this.classpath);
        try {
            try {
                InputStream is = cL == null ? ClassLoader.getSystemResourceAsStream(name) : cL.getResourceAsStream(name);
                try {
                    if (is == null) {
                        log("Unable to find resource " + name, 1);
                    } else {
                        loadProperties(props, is, name.endsWith(".xml"));
                        addProperties(props);
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException ex) {
                throw new BuildException(ex, getLocation());
            }
        } finally {
            if (this.classpath != null && cL != null) {
                ((AntClassLoader) cL).cleanup();
            }
        }
    }

    protected void loadEnvironment(String prefix) {
        Properties props = new Properties();
        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }
        log("Loading Environment " + prefix, 3);
        Map<String, String> osEnv = Execute.getEnvironmentVariables();
        for (Map.Entry<String, String> entry : osEnv.entrySet()) {
            props.put(prefix + entry.getKey(), entry.getValue());
        }
        addProperties(props);
    }

    protected void loadRuntime(String prefix) {
        Properties props = new Properties();
        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }
        log("Loading Runtime properties " + prefix, 3);
        Runtime r = Runtime.getRuntime();
        props.put(prefix + "availableProcessors", String.valueOf(r.availableProcessors()));
        props.put(prefix + "freeMemory", String.valueOf(r.freeMemory()));
        props.put(prefix + "maxMemory", String.valueOf(r.maxMemory()));
        props.put(prefix + "totalMemory", String.valueOf(r.totalMemory()));
        addProperties(props);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addProperties(Properties props) {
        Map<String, Object> m = new HashMap<>();
        props.forEach(k, v -> {
            if (k instanceof String) {
                m.put((String) k, v);
            }
        });
        resolveAllProperties(m);
        m.forEach(k2, v2 -> {
            String propertyName = this.prefix == null ? k2 : this.prefix + k2;
            addProperty(propertyName, v2);
        });
    }

    protected void addProperty(String n, String v) {
        addProperty(n, (Object) v);
    }

    protected void addProperty(String n, Object v) {
        PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
        if (this.userProperty) {
            if (ph.getUserProperty(n) == null) {
                ph.setInheritedProperty(n, v);
                return;
            } else {
                log("Override ignored for " + n, 3);
                return;
            }
        }
        ph.setNewProperty(n, v);
    }

    private void resolveAllProperties(Map<String, Object> props) throws BuildException {
        PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
        new ResolvePropertyMap(getProject(), propertyHelper, propertyHelper.getExpanders()).resolveAllProperties(props, getPrefix(), getPrefixValues());
    }
}
