package org.apache.tools.ant.filters.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.BaseFilterReader;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.types.AntFilterReader;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Parameterizable;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/util/ChainReaderHelper.class */
public final class ChainReaderHelper {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    public Reader primaryReader;
    public int bufferSize = 8192;
    public Vector<FilterChain> filterChains = new Vector<>();
    private Project project = null;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/util/ChainReaderHelper$ChainReader.class */
    public class ChainReader extends FilterReader {
        private List<AntClassLoader> cleanupLoaders;

        private ChainReader(Reader in, List<AntClassLoader> cleanupLoaders) {
            super(in);
            this.cleanupLoaders = cleanupLoaders;
        }

        public String readFully() throws IOException {
            return ChainReaderHelper.this.readFully(this);
        }

        @Override // java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            ChainReaderHelper.cleanUpClassLoaders(this.cleanupLoaders);
            super.close();
        }

        protected void finalize() throws Throwable {
            try {
                close();
            } finally {
                super.finalize();
            }
        }
    }

    public ChainReaderHelper() {
    }

    public ChainReaderHelper(Project project, Reader primaryReader, Iterable<FilterChain> filterChains) {
        withProject(project).withPrimaryReader(primaryReader).withFilterChains(filterChains);
    }

    public void setPrimaryReader(Reader rdr) {
        this.primaryReader = rdr;
    }

    public ChainReaderHelper withPrimaryReader(Reader rdr) {
        setPrimaryReader(rdr);
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ChainReaderHelper withProject(Project project) {
        setProject(project);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setBufferSize(int size) {
        this.bufferSize = size;
    }

    public ChainReaderHelper withBufferSize(int size) {
        setBufferSize(size);
        return this;
    }

    public void setFilterChains(Vector<FilterChain> fchain) {
        this.filterChains = fchain;
    }

    public ChainReaderHelper withFilterChains(Iterable<FilterChain> filterChains) {
        Vector<FilterChain> fcs;
        if (filterChains instanceof Vector) {
            fcs = (Vector) filterChains;
        } else {
            fcs = new Vector<>();
            Objects.requireNonNull(fcs);
            filterChains.forEach((v1) -> {
                r1.add(v1);
            });
        }
        setFilterChains(fcs);
        return this;
    }

    public ChainReaderHelper with(Consumer<ChainReaderHelper> consumer) {
        consumer.accept(this);
        return this;
    }

    public ChainReader getAssembledReader() throws BuildException {
        if (this.primaryReader == null) {
            throw new BuildException("primaryReader must not be null.");
        }
        Reader instream = this.primaryReader;
        List<AntClassLoader> classLoadersToCleanUp = new ArrayList<>();
        List<Object> finalFilters = (List) this.filterChains.stream().map((v0) -> {
            return v0.getFilterReaders();
        }).flatMap((v0) -> {
            return v0.stream();
        }).collect(Collectors.toList());
        if (!finalFilters.isEmpty()) {
            boolean success = false;
            try {
                for (Object o : finalFilters) {
                    if (o instanceof AntFilterReader) {
                        instream = expandReader((AntFilterReader) o, instream, classLoadersToCleanUp);
                    } else if (o instanceof ChainableReader) {
                        setProjectOnObject(o);
                        instream = ((ChainableReader) o).chain(instream);
                        setProjectOnObject(instream);
                    }
                }
                success = true;
            } finally {
                if (!success && !classLoadersToCleanUp.isEmpty()) {
                    cleanUpClassLoaders(classLoadersToCleanUp);
                }
            }
        }
        return new ChainReader(instream, classLoadersToCleanUp);
    }

    private void setProjectOnObject(Object obj) {
        if (this.project == null) {
            return;
        }
        if (obj instanceof BaseFilterReader) {
            ((BaseFilterReader) obj).setProject(this.project);
        } else {
            this.project.setProjectReference(obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cleanUpClassLoaders(List<AntClassLoader> loaders) {
        loaders.forEach((v0) -> {
            v0.cleanup();
        });
    }

    public String readFully(Reader rdr) throws IOException {
        return FileUtils.readFully(rdr, this.bufferSize);
    }

    private Reader expandReader(AntFilterReader filter, Reader ancestor, List<AntClassLoader> classLoadersToCleanUp) {
        Class asSubclass;
        String className = filter.getClassName();
        Path classpath = filter.getClasspath();
        try {
            if (className != null) {
                try {
                    if (classpath == null) {
                        asSubclass = Class.forName(className).asSubclass(FilterReader.class);
                    } else {
                        AntClassLoader al = filter.getProject().createClassLoader(classpath);
                        classLoadersToCleanUp.add(al);
                        asSubclass = Class.forName(className, true, al).asSubclass(FilterReader.class);
                    }
                    Optional<Constructor<?>> ctor = Stream.of((Object[]) asSubclass.getConstructors()).filter(c -> {
                        return c.getParameterCount() == 1 && c.getParameterTypes()[0].isAssignableFrom(Reader.class);
                    }).findFirst();
                    Object instream = ctor.orElseThrow(() -> {
                        return new BuildException("%s does not define a public constructor that takes in a %s as its single argument.", className, Reader.class.getSimpleName());
                    }).newInstance(ancestor);
                    setProjectOnObject(instream);
                    if (Parameterizable.class.isAssignableFrom(asSubclass)) {
                        ((Parameterizable) instream).setParameters(filter.getParams());
                    }
                    return (Reader) instream;
                } catch (ClassCastException e) {
                    throw new BuildException("%s does not extend %s", className, FilterReader.class.getName());
                }
            }
            return ancestor;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new BuildException(ex);
        }
    }
}
