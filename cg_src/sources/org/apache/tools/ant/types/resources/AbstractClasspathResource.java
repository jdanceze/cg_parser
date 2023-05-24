package org.apache.tools.ant.types.resources;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AbstractClasspathResource.class */
public abstract class AbstractClasspathResource extends Resource {
    private Path classpath;
    private Reference loader;
    private boolean parentFirst = true;

    protected abstract InputStream openInputStream(ClassLoader classLoader) throws IOException;

    public void setClasspath(Path classpath) {
        checkAttributesAllowed();
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
        setChecked(false);
    }

    public Path createClasspath() {
        checkChildrenAllowed();
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        setChecked(false);
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        checkAttributesAllowed();
        createClasspath().setRefid(r);
    }

    public Path getClasspath() {
        if (isReference()) {
            return getRef().getClasspath();
        }
        dieOnCircularReference();
        return this.classpath;
    }

    public Reference getLoader() {
        if (isReference()) {
            return getRef().getLoader();
        }
        dieOnCircularReference();
        return this.loader;
    }

    public void setLoaderRef(Reference r) {
        checkAttributesAllowed();
        this.loader = r;
    }

    public void setParentFirst(boolean b) {
        this.parentFirst = b;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.loader != null || this.classpath != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:17:0x002c
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.isReference()
            if (r0 == 0) goto Lf
            r0 = r3
            org.apache.tools.ant.types.resources.AbstractClasspathResource r0 = r0.getRef()
            boolean r0 = r0.isExists()
            return r0
        Lf:
            r0 = r3
            r0.dieOnCircularReference()
            r0 = r3
            java.io.InputStream r0 = r0.getInputStream()     // Catch: java.io.IOException -> L40
            r4 = r0
            r0 = r4
            if (r0 == 0) goto L20
            r0 = 1
            goto L21
        L20:
            r0 = 0
        L21:
            r5 = r0
            r0 = r4
            if (r0 == 0) goto L2a
            r0 = r4
            r0.close()     // Catch: java.io.IOException -> L40
        L2a:
            r0 = r5
            return r0
        L2c:
            r5 = move-exception
            r0 = r4
            if (r0 == 0) goto L3e
            r0 = r4
            r0.close()     // Catch: java.lang.Throwable -> L38 java.io.IOException -> L40
            goto L3e
        L38:
            r6 = move-exception
            r0 = r5
            r1 = r6
            r0.addSuppressed(r1)     // Catch: java.io.IOException -> L40
        L3e:
            r0 = r5
            throw r0     // Catch: java.io.IOException -> L40
        L40:
            r4 = move-exception
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.types.resources.AbstractClasspathResource.isExists():boolean");
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return getRef().getInputStream();
        }
        dieOnCircularReference();
        final ClassLoaderWithFlag classLoader = getClassLoader();
        if (!classLoader.needsCleanup()) {
            return openInputStream(classLoader.getLoader());
        }
        return new FilterInputStream(openInputStream(classLoader.getLoader())) { // from class: org.apache.tools.ant.types.resources.AbstractClasspathResource.1
            @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                FileUtils.close(this.in);
                classLoader.cleanup();
            }

            protected void finalize() throws Throwable {
                try {
                    close();
                } finally {
                    super.finalize();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public ClassLoaderWithFlag getClassLoader() {
        ClassLoader cl = null;
        if (this.loader != null) {
            cl = (ClassLoader) this.loader.getReferencedObject();
        }
        boolean clNeedsCleanup = false;
        if (cl == null) {
            if (getClasspath() != null) {
                Path p = getClasspath().concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
                if (this.parentFirst) {
                    cl = getProject().createClassLoader(p);
                } else {
                    cl = AntClassLoader.newAntClassLoader(getProject().getCoreLoader(), getProject(), p, false);
                }
                clNeedsCleanup = this.loader == null;
            } else {
                cl = JavaResource.class.getClassLoader();
            }
            if (this.loader != null && cl != null) {
                getProject().addReference(this.loader.getRefId(), cl);
            }
        }
        return new ClassLoaderWithFlag(cl, clNeedsCleanup);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        if (this.classpath != null) {
            pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
        }
        setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public AbstractClasspathResource getRef() {
        return (AbstractClasspathResource) getCheckedRef(AbstractClasspathResource.class);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AbstractClasspathResource$ClassLoaderWithFlag.class */
    public static class ClassLoaderWithFlag {
        private final ClassLoader loader;
        private final boolean cleanup;

        ClassLoaderWithFlag(ClassLoader l, boolean needsCleanup) {
            this.loader = l;
            this.cleanup = needsCleanup && (l instanceof AntClassLoader);
        }

        public ClassLoader getLoader() {
            return this.loader;
        }

        public boolean needsCleanup() {
            return this.cleanup;
        }

        public void cleanup() {
            if (this.cleanup) {
                ((AntClassLoader) this.loader).cleanup();
            }
        }
    }
}
