package org.apache.tools.ant.taskdefs;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Definer.class */
public abstract class Definer extends DefBase {
    private static final String ANTLIB_XML = "/antlib.xml";
    private static final ThreadLocal<Map<URL, Location>> RESOURCE_STACK = ThreadLocal.withInitial(HashMap::new);
    private String name;
    private String classname;
    private File file;
    private String resource;
    private boolean restrict = false;
    private int format = 0;
    private boolean definerSet = false;
    private int onError = 0;
    private String adapter;
    private String adaptTo;
    private Class<?> adapterClass;
    private Class<?> adaptToClass;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Definer$OnError.class */
    public static class OnError extends EnumeratedAttribute {
        public static final int FAIL = 0;
        public static final int REPORT = 1;
        public static final int IGNORE = 2;
        public static final int FAIL_ALL = 3;
        public static final String POLICY_FAIL = "fail";
        public static final String POLICY_REPORT = "report";
        public static final String POLICY_IGNORE = "ignore";
        public static final String POLICY_FAILALL = "failall";

        public OnError() {
        }

        public OnError(String value) {
            setValue(value);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"fail", POLICY_REPORT, POLICY_IGNORE, POLICY_FAILALL};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Definer$Format.class */
    public static class Format extends EnumeratedAttribute {
        public static final int PROPERTIES = 0;
        public static final int XML = 1;

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"properties", EncodingConstants.XML_NAMESPACE_PREFIX};
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    public void setOnError(OnError onError) {
        this.onError = onError.getIndex();
    }

    public void setFormat(Format format) {
        this.format = format.getIndex();
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public String getResource() {
        return this.resource;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Enumeration<URL> urls;
        ClassLoader al = createLoader();
        if (!this.definerSet) {
            if (getURI() == null) {
                throw new BuildException("name, file or resource attribute of " + getTaskName() + " is undefined", getLocation());
            }
            if (getURI().startsWith("antlib:")) {
                String uri1 = getURI();
                setResource(makeResourceFromURI(uri1));
            } else {
                throw new BuildException("Only antlib URIs can be located from the URI alone, not the URI '" + getURI() + "'");
            }
        }
        if (this.name != null) {
            if (this.classname == null) {
                throw new BuildException("classname attribute of " + getTaskName() + " element is undefined", getLocation());
            }
            addDefinition(al, this.name, this.classname);
        } else if (this.classname != null) {
            throw new BuildException("You must not specify classname together with file or resource.", getLocation());
        } else {
            if (this.file == null) {
                urls = resourceToURLs(al);
            } else {
                URL url = fileToURL();
                if (url == null) {
                    return;
                }
                urls = Collections.enumeration(Collections.singleton(url));
            }
            while (urls.hasMoreElements()) {
                URL url2 = urls.nextElement();
                int fmt = this.format;
                if (url2.getPath().toLowerCase(Locale.ENGLISH).endsWith(".xml")) {
                    fmt = 1;
                }
                if (fmt == 0) {
                    loadProperties(al, url2);
                    return;
                } else if (RESOURCE_STACK.get().get(url2) != null) {
                    log("Warning: Recursive loading of " + url2 + " ignored at " + getLocation() + " originally loaded at " + RESOURCE_STACK.get().get(url2), 1);
                } else {
                    try {
                        RESOURCE_STACK.get().put(url2, getLocation());
                        loadAntlib(al, url2);
                        RESOURCE_STACK.get().remove(url2);
                    } catch (Throwable th) {
                        RESOURCE_STACK.get().remove(url2);
                        throw th;
                    }
                }
            }
        }
    }

    public static String makeResourceFromURI(String uri) {
        String resource;
        String path = uri.substring("antlib:".length());
        if (path.startsWith("//")) {
            resource = path.substring("//".length());
            if (!resource.endsWith(".xml")) {
                resource = resource + ANTLIB_XML;
            }
        } else {
            resource = path.replace('.', '/') + ANTLIB_XML;
        }
        return resource;
    }

    private URL fileToURL() {
        String message = null;
        if (!this.file.exists()) {
            message = "File " + this.file + " does not exist";
        }
        if (message == null && !this.file.isFile()) {
            message = "File " + this.file + " is not a file";
        }
        if (message == null) {
            try {
                return FileUtils.getFileUtils().getFileURL(this.file);
            } catch (Exception ex) {
                message = "File " + this.file + " cannot use as URL: " + ex.toString();
            }
        }
        switch (this.onError) {
            case 0:
            case 1:
                log(message, 1);
                return null;
            case 2:
                log(message, 3);
                return null;
            case 3:
                throw new BuildException(message);
            default:
                return null;
        }
    }

    private Enumeration<URL> resourceToURLs(ClassLoader classLoader) {
        try {
            Enumeration<URL> ret = classLoader.getResources(this.resource);
            if (!ret.hasMoreElements()) {
                String message = "Could not load definitions from resource " + this.resource + Rmic.ERROR_NOT_FOUND;
                switch (this.onError) {
                    case 0:
                    case 1:
                        log(message, 1);
                        break;
                    case 2:
                        log(message, 3);
                        break;
                    case 3:
                        throw new BuildException(message);
                }
            }
            return ret;
        } catch (IOException e) {
            throw new BuildException("Could not fetch resources named " + this.resource, e, getLocation());
        }
    }

    protected void loadProperties(ClassLoader al, URL url) {
        try {
            InputStream is = url.openStream();
            if (is == null) {
                log("Could not load definitions from " + url, 1);
                if (is != null) {
                    is.close();
                    return;
                }
                return;
            }
            Properties props = new Properties();
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                this.name = key;
                this.classname = props.getProperty(this.name);
                addDefinition(al, this.name, this.classname);
            }
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            throw new BuildException(ex, getLocation());
        }
    }

    private void loadAntlib(ClassLoader classLoader, URL url) {
        try {
            Antlib antlib = Antlib.createAntlib(getProject(), url, getURI());
            antlib.setClassLoader(classLoader);
            antlib.setURI(getURI());
            antlib.execute();
        } catch (BuildException ex) {
            throw ProjectHelper.addLocationToBuildException(ex, getLocation());
        }
    }

    public void setFile(File file) {
        if (this.definerSet) {
            tooManyDefinitions();
        }
        this.definerSet = true;
        this.file = file;
    }

    public void setResource(String res) {
        if (this.definerSet) {
            tooManyDefinitions();
        }
        this.definerSet = true;
        this.resource = res;
    }

    public void setAntlib(String antlib) {
        if (this.definerSet) {
            tooManyDefinitions();
        }
        if (!antlib.startsWith("antlib:")) {
            throw new BuildException("Invalid antlib attribute - it must start with antlib:");
        }
        setURI(antlib);
        this.resource = antlib.substring("antlib:".length()).replace('.', '/') + ANTLIB_XML;
        this.definerSet = true;
    }

    public void setName(String name) {
        if (this.definerSet) {
            tooManyDefinitions();
        }
        this.definerSet = true;
        this.name = name;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAdapterClass(Class<?> adapterClass) {
        this.adapterClass = adapterClass;
    }

    public void setAdaptTo(String adaptTo) {
        this.adaptTo = adaptTo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAdaptToClass(Class<?> adaptToClass) {
        this.adaptToClass = adaptToClass;
    }

    protected void addDefinition(ClassLoader al, String name, String classname) throws BuildException {
        Class<?> cl = null;
        try {
            try {
                String name2 = ProjectHelper.genComponentName(getURI(), name);
                if (this.onError != 2) {
                    cl = Class.forName(classname, true, al);
                }
                if (this.adapter != null) {
                    this.adapterClass = Class.forName(this.adapter, true, al);
                }
                if (this.adaptTo != null) {
                    this.adaptToClass = Class.forName(this.adaptTo, true, al);
                }
                AntTypeDefinition def = new AntTypeDefinition();
                def.setName(name2);
                def.setClassName(classname);
                def.setClass(cl);
                def.setAdapterClass(this.adapterClass);
                def.setAdaptToClass(this.adaptToClass);
                def.setRestrict(this.restrict);
                def.setClassLoader(al);
                if (cl != null) {
                    def.checkClass(getProject());
                }
                ComponentHelper.getComponentHelper(getProject()).addDataTypeDefinition(def);
            } catch (ClassNotFoundException cnfe) {
                throw new BuildException(getTaskName() + " class " + classname + " cannot be found\n using the classloader " + al, cnfe, getLocation());
            } catch (NoClassDefFoundError ncdfe) {
                throw new BuildException(getTaskName() + " A class needed by class " + classname + " cannot be found: " + ncdfe.getMessage() + "\n using the classloader " + al, ncdfe, getLocation());
            }
        } catch (BuildException ex) {
            switch (this.onError) {
                case 0:
                case 3:
                    throw ex;
                case 1:
                    log(ex.getLocation() + "Warning: " + ex.getMessage(), 1);
                    return;
                case 2:
                default:
                    log(ex.getLocation() + ex.getMessage(), 4);
                    return;
            }
        }
    }

    private void tooManyDefinitions() {
        throw new BuildException("Only one of the attributes name, file and resource can be set", getLocation());
    }
}
