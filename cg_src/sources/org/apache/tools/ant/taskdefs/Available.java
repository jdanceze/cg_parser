package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.InputStream;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Available.class */
public class Available extends Task implements Condition {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String property;
    private String classname;
    private String filename;
    private File file;
    private Path filepath;
    private String resource;
    private FileDir type;
    private Path classpath;
    private AntClassLoader loader;
    private Object value = "true";
    private boolean isTask = false;
    private boolean ignoreSystemclasses = false;
    private boolean searchParents = false;

    public void setSearchParents(boolean searchParents) {
        this.searchParents = searchParents;
    }

    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
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

    public void setFilepath(Path filepath) {
        createFilepath().append(filepath);
    }

    public Path createFilepath() {
        if (this.filepath == null) {
            this.filepath = new Path(getProject());
        }
        return this.filepath.createPath();
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValue(String value) {
        setValue((Object) value);
    }

    public void setClassname(String classname) {
        if (!classname.isEmpty()) {
            this.classname = classname;
        }
    }

    public void setFile(File file) {
        this.file = file;
        this.filename = FILE_UTILS.removeLeadingPath(getProject().getBaseDir(), file);
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Deprecated
    public void setType(String type) {
        log("DEPRECATED - The setType(String) method has been deprecated. Use setType(Available.FileDir) instead.", 1);
        this.type = new FileDir();
        this.type.setValue(type);
    }

    public void setType(FileDir type) {
        this.type = type;
    }

    public void setIgnoresystemclasses(boolean ignore) {
        this.ignoreSystemclasses = ignore;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.property == null) {
            throw new BuildException("property attribute is required", getLocation());
        }
        this.isTask = true;
        try {
            if (eval()) {
                PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
                Object oldvalue = ph.getProperty(this.property);
                if (null != oldvalue && !oldvalue.equals(this.value)) {
                    log(String.format("DEPRECATED - <available> used to override an existing property.%n  Build file should not reuse the same property name for different values.", new Object[0]), 1);
                }
                ph.setProperty(this.property, this.value, true);
            }
        } finally {
            this.isTask = false;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        try {
            if (this.classname == null && this.file == null && this.resource == null) {
                throw new BuildException("At least one of (classname|file|resource) is required", getLocation());
            }
            if (this.type == null || this.file != null) {
                if (this.classpath != null) {
                    this.classpath.setProject(getProject());
                    this.loader = getProject().createClassLoader(this.classpath);
                }
                String appendix = "";
                if (this.isTask) {
                    appendix = " to set property " + this.property;
                } else {
                    setTaskName("available");
                }
                if (this.classname != null && !checkClass(this.classname)) {
                    log("Unable to load class " + this.classname + appendix, 3);
                    if (this.loader != null) {
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (!this.isTask) {
                        setTaskName(null);
                    }
                    return false;
                } else if (this.file != null && !checkFile()) {
                    StringBuilder buf = new StringBuilder("Unable to find ");
                    if (this.type != null) {
                        buf.append(this.type).append(' ');
                    }
                    buf.append(this.filename).append(appendix);
                    log(buf.toString(), 3);
                    if (this.loader != null) {
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (!this.isTask) {
                        setTaskName(null);
                    }
                    return false;
                } else if (this.resource == null || checkResource(this.resource)) {
                    if (this.loader != null) {
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (this.isTask) {
                        return true;
                    }
                    setTaskName(null);
                    return true;
                } else {
                    log("Unable to load resource " + this.resource + appendix, 3);
                    if (this.loader != null) {
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (!this.isTask) {
                        setTaskName(null);
                    }
                    return false;
                }
            }
            throw new BuildException("The type attribute is only valid when specifying the file attribute.", getLocation());
        } catch (Throwable th) {
            if (this.loader != null) {
                this.loader.cleanup();
                this.loader = null;
            }
            if (!this.isTask) {
                setTaskName(null);
            }
            throw th;
        }
    }

    private boolean checkFile() {
        if (this.filepath == null) {
            return checkFile(this.file, this.filename);
        }
        String[] paths = this.filepath.list();
        for (String p : paths) {
            log("Searching " + p, 3);
            File path = new File(p);
            if (path.exists() && (this.filename.equals(p) || this.filename.equals(path.getName()))) {
                if (this.type == null) {
                    log("Found: " + path, 3);
                    return true;
                } else if (this.type.isDir() && path.isDirectory()) {
                    log("Found directory: " + path, 3);
                    return true;
                } else if (this.type.isFile() && path.isFile()) {
                    log("Found file: " + path, 3);
                    return true;
                } else {
                    return false;
                }
            }
            File parent = path.getParentFile();
            if (parent != null && parent.exists() && this.filename.equals(parent.getAbsolutePath())) {
                if (this.type == null) {
                    log("Found: " + parent, 3);
                    return true;
                } else if (this.type.isDir()) {
                    log("Found directory: " + parent, 3);
                    return true;
                } else {
                    return false;
                }
            } else if (path.exists() && path.isDirectory() && checkFile(new File(path, this.filename), this.filename + " in " + path)) {
                return true;
            } else {
                while (this.searchParents && parent != null && parent.exists()) {
                    if (checkFile(new File(parent, this.filename), this.filename + " in " + parent)) {
                        return true;
                    }
                    parent = parent.getParentFile();
                }
            }
        }
        return false;
    }

    private boolean checkFile(File f, String text) {
        if (this.type != null) {
            if (this.type.isDir()) {
                if (f.isDirectory()) {
                    log("Found directory: " + text, 3);
                }
                return f.isDirectory();
            } else if (this.type.isFile()) {
                if (f.isFile()) {
                    log("Found file: " + text, 3);
                }
                return f.isFile();
            }
        }
        if (f.exists()) {
            log("Found: " + text, 3);
        }
        return f.exists();
    }

    private boolean checkResource(String resource) {
        InputStream is;
        try {
            if (this.loader != null) {
                is = this.loader.getResourceAsStream(resource);
            } else {
                ClassLoader cL = getClass().getClassLoader();
                if (cL != null) {
                    is = cL.getResourceAsStream(resource);
                } else {
                    is = ClassLoader.getSystemResourceAsStream(resource);
                }
            }
            boolean z = is != null;
            FileUtils.close(is);
            return z;
        } catch (Throwable th) {
            FileUtils.close((InputStream) null);
            throw th;
        }
    }

    private boolean checkClass(String classname) {
        try {
            if (this.ignoreSystemclasses) {
                this.loader = getProject().createClassLoader(this.classpath);
                this.loader.setParentFirst(false);
                this.loader.addJavaLibraries();
                try {
                    this.loader.findClass(classname);
                    return true;
                } catch (SecurityException e) {
                    return true;
                }
            } else if (this.loader != null) {
                this.loader.loadClass(classname);
                return true;
            } else {
                ClassLoader l = getClass().getClassLoader();
                if (l != null) {
                    Class.forName(classname, true, l);
                    return true;
                }
                Class.forName(classname);
                return true;
            }
        } catch (ClassNotFoundException e2) {
            log("class \"" + classname + "\" was not found", 4);
            return false;
        } catch (NoClassDefFoundError e3) {
            log("Could not load dependent class \"" + e3.getMessage() + "\" for class \"" + classname + "\"", 4);
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Available$FileDir.class */
    public static class FileDir extends EnumeratedAttribute {
        private static final String[] VALUES = {"file", "dir"};

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALUES;
        }

        public boolean isDir() {
            return "dir".equalsIgnoreCase(getValue());
        }

        public boolean isFile() {
            return "file".equalsIgnoreCase(getValue());
        }
    }
}
