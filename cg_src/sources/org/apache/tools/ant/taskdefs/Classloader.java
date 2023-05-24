package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.StringUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Classloader.class */
public class Classloader extends Task {
    public static final String SYSTEM_LOADER_REF = "ant.coreLoader";
    private Path classpath;
    private String name = null;
    private boolean reset = false;
    private boolean parentFirst = true;
    private String parentName = null;

    public void setName(String name) {
        this.name = name;
    }

    public void setReset(boolean b) {
        this.reset = b;
    }

    @Deprecated
    public void setReverse(boolean b) {
        this.parentFirst = !b;
    }

    public void setParentFirst(boolean b) {
        this.parentFirst = b;
    }

    public void setParentName(String name) {
        this.parentName = name;
    }

    public void setClasspathRef(Reference pathRef) throws BuildException {
        this.classpath = (Path) pathRef.getReferencedObject(getProject());
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
            this.classpath = new Path(null);
        }
        return this.classpath.createPath();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        String[] list;
        try {
            if ("only".equals(getProject().getProperty(MagicNames.BUILD_SYSCLASSPATH)) && (this.name == null || "ant.coreLoader".equals(this.name))) {
                log("Changing the system loader is disabled by build.sysclasspath=only", 1);
                return;
            }
            String loaderName = this.name == null ? "ant.coreLoader" : this.name;
            Object obj = getProject().getReference(loaderName);
            if (this.reset) {
                obj = null;
            }
            if (obj != null && !(obj instanceof AntClassLoader)) {
                log("Referenced object is not an AntClassLoader", 0);
                return;
            }
            AntClassLoader acl = (AntClassLoader) obj;
            boolean existingLoader = acl != null;
            if (acl == null) {
                Object parent = null;
                if (this.parentName != null) {
                    parent = getProject().getReference(this.parentName);
                    if (!(parent instanceof ClassLoader)) {
                        parent = null;
                    }
                }
                if (parent == null) {
                    parent = getClass().getClassLoader();
                }
                if (this.name == null) {
                }
                getProject().log("Setting parent loader " + this.name + Instruction.argsep + parent + Instruction.argsep + this.parentFirst, 4);
                acl = AntClassLoader.newAntClassLoader((ClassLoader) parent, getProject(), this.classpath, this.parentFirst);
                getProject().addReference(loaderName, acl);
                if (this.name == null) {
                    acl.addLoaderPackageRoot("org.apache.tools.ant.taskdefs.optional");
                    getProject().setCoreLoader(acl);
                }
            }
            if (existingLoader && this.classpath != null) {
                for (String path : this.classpath.list()) {
                    File f = new File(path);
                    if (f.exists()) {
                        log("Adding to class loader " + acl + Instruction.argsep + f.getAbsolutePath(), 4);
                        acl.addPathElement(f.getAbsolutePath());
                    }
                }
            }
        } catch (Exception ex) {
            log(StringUtils.getStackTrace(ex), 0);
        }
    }
}
