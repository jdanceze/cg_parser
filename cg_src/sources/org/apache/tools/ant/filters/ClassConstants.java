package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/ClassConstants.class */
public final class ClassConstants extends BaseFilterReader implements ChainableReader {
    private String queuedData;
    private static final String JAVA_CLASS_HELPER = "org.apache.tools.ant.filters.util.JavaClassHelper";

    public ClassConstants() {
        this.queuedData = null;
    }

    public ClassConstants(Reader in) {
        super(in);
        this.queuedData = null;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch = -1;
        if (this.queuedData != null && this.queuedData.isEmpty()) {
            this.queuedData = null;
        }
        if (this.queuedData == null) {
            String clazz = readFully();
            if (clazz == null || clazz.isEmpty()) {
                ch = -1;
            } else {
                byte[] bytes = clazz.getBytes(StandardCharsets.ISO_8859_1);
                try {
                    Class<?> javaClassHelper = Class.forName(JAVA_CLASS_HELPER);
                    if (javaClassHelper != null) {
                        Method getConstants = javaClassHelper.getMethod("getConstants", byte[].class);
                        StringBuffer sb = (StringBuffer) getConstants.invoke(null, bytes);
                        if (sb.length() > 0) {
                            this.queuedData = sb.toString();
                            return read();
                        }
                    }
                } catch (Exception ex) {
                    throw new BuildException(ex);
                } catch (NoClassDefFoundError | RuntimeException ex2) {
                    throw ex2;
                } catch (InvocationTargetException ex3) {
                    Throwable t = ex3.getTargetException();
                    if (t instanceof NoClassDefFoundError) {
                        throw ((NoClassDefFoundError) t);
                    }
                    if (t instanceof RuntimeException) {
                        throw ((RuntimeException) t);
                    }
                    throw new BuildException(t);
                }
            }
        } else {
            ch = this.queuedData.charAt(0);
            this.queuedData = this.queuedData.substring(1);
            if (this.queuedData.isEmpty()) {
                this.queuedData = null;
            }
        }
        return ch;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        return new ClassConstants(rdr);
    }
}
