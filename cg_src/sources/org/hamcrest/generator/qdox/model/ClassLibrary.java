package org.hamcrest.generator.qdox.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/ClassLibrary.class */
public class ClassLibrary implements Serializable {
    private final Set classNames = new TreeSet();
    private final Map classNameToClassMap = new HashMap();
    private boolean defaultClassLoadersAdded = false;
    private transient List classLoaders = new ArrayList();
    private List sourceFolders = new ArrayList();

    public ClassLibrary() {
    }

    public ClassLibrary(ClassLoader loader) {
        this.classLoaders.add(loader);
    }

    public void add(String className) {
        this.classNames.add(className);
    }

    public boolean contains(String className) {
        return (!this.classNames.contains(className) && getSourceFile(className) == null && getClass(className) == null) ? false : true;
    }

    public File getSourceFile(String className) {
        for (File sourceFolder : this.sourceFolders) {
            String mainClassName = className.split("\\$")[0];
            File classFile = new File(sourceFolder, new StringBuffer().append(mainClassName.replace('.', File.separatorChar)).append(".java").toString());
            if (classFile.exists() && classFile.isFile()) {
                return classFile;
            }
        }
        return null;
    }

    public Class getClass(String className) {
        Class cachedClass = (Class) this.classNameToClassMap.get(className);
        if (cachedClass != null) {
            return cachedClass;
        }
        for (ClassLoader classLoader : this.classLoaders) {
            if (classLoader != null) {
                try {
                    Class clazz = classLoader.loadClass(className);
                    if (clazz != null) {
                        this.classNameToClassMap.put(className, clazz);
                        return clazz;
                    }
                    continue;
                } catch (ClassNotFoundException e) {
                } catch (NoClassDefFoundError e2) {
                }
            }
        }
        return null;
    }

    public Collection all() {
        return Collections.unmodifiableCollection(this.classNames);
    }

    public void addClassLoader(ClassLoader classLoader) {
        this.classLoaders.add(classLoader);
    }

    public void addDefaultLoader() {
        if (!this.defaultClassLoadersAdded) {
            this.classLoaders.add(getClass().getClassLoader());
            this.classLoaders.add(Thread.currentThread().getContextClassLoader());
        }
        this.defaultClassLoadersAdded = true;
    }

    public void addSourceFolder(File sourceFolder) {
        this.sourceFolders.add(sourceFolder);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.classLoaders = new ArrayList();
        if (this.defaultClassLoadersAdded) {
            this.defaultClassLoadersAdded = false;
            addDefaultLoader();
        }
    }
}
