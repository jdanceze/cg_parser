package org.apache.tools.ant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/AntTypeDefinition.class */
public class AntTypeDefinition {
    private String name;
    private Class<?> clazz;
    private Class<?> adapterClass;
    private Class<?> adaptToClass;
    private String className;
    private ClassLoader classLoader;
    private boolean restrict = false;

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    public boolean isRestrict() {
        return this.restrict;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setClass(Class<?> clazz) {
        this.clazz = clazz;
        if (clazz == null) {
            return;
        }
        this.classLoader = this.classLoader == null ? clazz.getClassLoader() : this.classLoader;
        this.className = this.className == null ? clazz.getName() : this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setAdapterClass(Class<?> adapterClass) {
        this.adapterClass = adapterClass;
    }

    public void setAdaptToClass(Class<?> adaptToClass) {
        this.adaptToClass = adaptToClass;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public Class<?> getExposedClass(Project project) {
        Class<?> z;
        if (this.adaptToClass == null || !((z = getTypeClass(project)) == null || this.adaptToClass.isAssignableFrom(z))) {
            return this.adapterClass == null ? getTypeClass(project) : this.adapterClass;
        }
        return z;
    }

    public Class<?> getTypeClass(Project project) {
        try {
            return innerGetTypeClass();
        } catch (ClassNotFoundException e) {
            project.log("Could not load class (" + this.className + ") for type " + this.name, 4);
            return null;
        } catch (NoClassDefFoundError ncdfe) {
            project.log("Could not load a dependent class (" + ncdfe.getMessage() + ") for type " + this.name, 4);
            return null;
        }
    }

    public Class<?> innerGetTypeClass() throws ClassNotFoundException {
        if (this.clazz != null) {
            return this.clazz;
        }
        if (this.classLoader == null) {
            this.clazz = Class.forName(this.className);
        } else {
            this.clazz = this.classLoader.loadClass(this.className);
        }
        return this.clazz;
    }

    public Object create(Project project) {
        return icreate(project);
    }

    private Object icreate(Project project) {
        Class<?> c = getTypeClass(project);
        if (c == null) {
            return null;
        }
        Object o = createAndSet(project, c);
        if (this.adapterClass == null || (this.adaptToClass != null && this.adaptToClass.isAssignableFrom(o.getClass()))) {
            return o;
        }
        TypeAdapter adapterObject = (TypeAdapter) createAndSet(project, this.adapterClass);
        adapterObject.setProxy(o);
        return adapterObject;
    }

    public void checkClass(Project project) {
        if (this.clazz == null) {
            this.clazz = getTypeClass(project);
            if (this.clazz == null) {
                throw new BuildException("Unable to create class for " + getName());
            }
        }
        if (this.adapterClass != null) {
            if (this.adaptToClass == null || !this.adaptToClass.isAssignableFrom(this.clazz)) {
                TypeAdapter adapter = (TypeAdapter) createAndSet(project, this.adapterClass);
                adapter.checkProxyClass(this.clazz);
            }
        }
    }

    private Object createAndSet(Project project, Class<?> c) {
        try {
            return innerCreateAndSet(c, project);
        } catch (IllegalAccessException e) {
            throw new BuildException("Could not create type " + this.name + " as the constructor " + c + " is not accessible");
        } catch (InstantiationException e2) {
            throw new BuildException("Could not create type " + this.name + " as the class " + c + " is abstract");
        } catch (NoClassDefFoundError ncdfe) {
            String msg = "Type " + this.name + ": A class needed by class " + c + " cannot be found: " + ncdfe.getMessage();
            throw new BuildException(msg, ncdfe);
        } catch (NoSuchMethodException e3) {
            throw new BuildException("Could not create type " + this.name + " as the class " + c + " has no compatible constructor");
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            throw new BuildException("Could not create type " + this.name + " due to " + t, t);
        } catch (Throwable t2) {
            throw new BuildException("Could not create type " + this.name + " due to " + t2, t2);
        }
    }

    public <T> T innerCreateAndSet(Class<T> newclass, Project project) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> ctor;
        boolean noArg;
        try {
            ctor = newclass.getConstructor(new Class[0]);
            noArg = true;
        } catch (NoSuchMethodException e) {
            ctor = newclass.getConstructor(Project.class);
            noArg = false;
        }
        T o = ctor.newInstance(noArg ? new Object[0] : new Object[]{project});
        project.setProjectReference(o);
        return o;
    }

    public boolean sameDefinition(AntTypeDefinition other, Project project) {
        return other != null && other.getClass() == getClass() && other.getTypeClass(project).equals(getTypeClass(project)) && other.getExposedClass(project).equals(getExposedClass(project)) && other.restrict == this.restrict && other.adapterClass == this.adapterClass && other.adaptToClass == this.adaptToClass;
    }

    public boolean similarDefinition(AntTypeDefinition other, Project project) {
        if (other == null || getClass() != other.getClass() || !getClassName().equals(other.getClassName()) || !extractClassname(this.adapterClass).equals(extractClassname(other.adapterClass)) || !extractClassname(this.adaptToClass).equals(extractClassname(other.adaptToClass)) || this.restrict != other.restrict) {
            return false;
        }
        ClassLoader oldLoader = other.getClassLoader();
        ClassLoader newLoader = getClassLoader();
        return oldLoader == newLoader || ((oldLoader instanceof AntClassLoader) && (newLoader instanceof AntClassLoader) && ((AntClassLoader) oldLoader).getClasspath().equals(((AntClassLoader) newLoader).getClasspath()));
    }

    private String extractClassname(Class<?> c) {
        return c == null ? "<null>" : c.getName();
    }
}
