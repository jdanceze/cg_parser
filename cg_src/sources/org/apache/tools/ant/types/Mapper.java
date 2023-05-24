package org.apache.tools.ant.types;

import java.util.Properties;
import org.apache.http.protocol.HTTP;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.CompositeMapper;
import org.apache.tools.ant.util.ContainerMapper;
import org.apache.tools.ant.util.FileNameMapper;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Mapper.class */
public class Mapper extends DataType {
    protected MapperType type = null;
    protected String classname = null;
    protected Path classpath = null;
    protected String from = null;
    protected String to = null;
    private ContainerMapper container = null;

    public Mapper(Project p) {
        setProject(p);
    }

    public void setType(MapperType type) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.type = type;
    }

    public void addConfigured(FileNameMapper fileNameMapper) {
        add(fileNameMapper);
    }

    public void add(FileNameMapper fileNameMapper) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.container == null) {
            if (this.type == null && this.classname == null) {
                this.container = new CompositeMapper();
            } else {
                FileNameMapper m = getImplementation();
                if (m instanceof ContainerMapper) {
                    this.container = (ContainerMapper) m;
                } else {
                    throw new BuildException(String.valueOf(m) + " mapper implementation does not support nested mappers!");
                }
            }
        }
        this.container.add(fileNameMapper);
        setChecked(false);
    }

    public void addConfiguredMapper(Mapper mapper) {
        add(mapper.getImplementation());
    }

    public void setClassname(String classname) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.classname = classname;
    }

    public void setClasspath(Path classpath) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        setChecked(false);
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference ref) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createClasspath().setRefid(ref);
    }

    public void setFrom(String from) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.from = from;
    }

    public void setTo(String to) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.to = to;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.type != null || this.from != null || this.to != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public FileNameMapper getImplementation() throws BuildException {
        if (isReference()) {
            dieOnCircularReference();
            Reference r = getRefid();
            Object o = r.getReferencedObject(getProject());
            if (o instanceof FileNameMapper) {
                return (FileNameMapper) o;
            }
            if (o instanceof Mapper) {
                return ((Mapper) o).getImplementation();
            }
            String od = o == null ? Jimple.NULL : o.getClass().getName();
            throw new BuildException(od + " at reference '" + r.getRefId() + "' is not a valid mapper reference.");
        } else if (this.type == null && this.classname == null && this.container == null) {
            throw new BuildException("nested mapper or one of the attributes type or classname is required");
        } else {
            if (this.container != null) {
                return this.container;
            }
            if (this.type != null && this.classname != null) {
                throw new BuildException("must not specify both type and classname attribute");
            }
            try {
                FileNameMapper m = getImplementationClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                Project p = getProject();
                if (p != null) {
                    p.setProjectReference(m);
                }
                m.setFrom(this.from);
                m.setTo(this.to);
                return m;
            } catch (BuildException be) {
                throw be;
            } catch (Throwable t) {
                throw new BuildException(t);
            }
        }
    }

    protected Class<? extends FileNameMapper> getImplementationClass() throws ClassNotFoundException {
        ClassLoader createClassLoader;
        String cName = this.classname;
        if (this.type != null) {
            cName = this.type.getImplementation();
        }
        if (this.classpath == null) {
            createClassLoader = getClass().getClassLoader();
        } else {
            createClassLoader = getProject().createClassLoader(this.classpath);
        }
        ClassLoader loader = createClassLoader;
        return Class.forName(cName, true, loader).asSubclass(FileNameMapper.class);
    }

    @Deprecated
    protected Mapper getRef() {
        return (Mapper) getCheckedRef(Mapper.class);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Mapper$MapperType.class */
    public static class MapperType extends EnumeratedAttribute {
        private Properties implementations = new Properties();

        public MapperType() {
            this.implementations.put(HTTP.IDENTITY_CODING, "org.apache.tools.ant.util.IdentityMapper");
            this.implementations.put("flatten", "org.apache.tools.ant.util.FlatFileNameMapper");
            this.implementations.put("glob", "org.apache.tools.ant.util.GlobPatternMapper");
            this.implementations.put("merge", "org.apache.tools.ant.util.MergingMapper");
            this.implementations.put(RegularExpression.DATA_TYPE_NAME, "org.apache.tools.ant.util.RegexpPatternMapper");
            this.implementations.put("package", "org.apache.tools.ant.util.PackageNameMapper");
            this.implementations.put("unpackage", "org.apache.tools.ant.util.UnPackageNameMapper");
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{HTTP.IDENTITY_CODING, "flatten", "glob", "merge", RegularExpression.DATA_TYPE_NAME, "package", "unpackage"};
        }

        public String getImplementation() {
            return this.implementations.getProperty(getValue());
        }
    }
}
