package org.apache.tools.ant.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import org.apache.tools.ant.types.resources.FileProvider;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Resource.class */
public class Resource extends DataType implements Comparable<Resource>, ResourceCollection {
    public static final long UNKNOWN_SIZE = -1;
    public static final long UNKNOWN_DATETIME = 0;
    protected static final int MAGIC = getMagicNumber("Resource".getBytes());
    private static final int NULL_NAME = getMagicNumber("null name".getBytes());
    private String name;
    private Boolean exists;
    private Long lastmodified;
    private Boolean directory;
    private Long size;

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getMagicNumber(byte[] seed) {
        return new BigInteger(seed).intValue();
    }

    public Resource() {
        this.name = null;
        this.exists = null;
        this.lastmodified = null;
        this.directory = null;
        this.size = null;
    }

    public Resource(String name) {
        this(name, false, 0L, false);
    }

    public Resource(String name, boolean exists, long lastmodified) {
        this(name, exists, lastmodified, false);
    }

    public Resource(String name, boolean exists, long lastmodified, boolean directory) {
        this(name, exists, lastmodified, directory, -1L);
    }

    public Resource(String name, boolean exists, long lastmodified, boolean directory, long size) {
        this.name = null;
        this.exists = null;
        this.lastmodified = null;
        this.directory = null;
        this.size = null;
        this.name = name;
        setName(name);
        setExists(exists);
        setLastModified(lastmodified);
        setDirectory(directory);
        setSize(size);
    }

    public String getName() {
        return isReference() ? getRef().getName() : this.name;
    }

    public void setName(String name) {
        checkAttributesAllowed();
        this.name = name;
    }

    public boolean isExists() {
        if (isReference()) {
            return getRef().isExists();
        }
        return this.exists == null || this.exists.booleanValue();
    }

    public void setExists(boolean exists) {
        checkAttributesAllowed();
        this.exists = exists ? Boolean.TRUE : Boolean.FALSE;
    }

    public long getLastModified() {
        if (isReference()) {
            return getRef().getLastModified();
        }
        if (!isExists() || this.lastmodified == null) {
            return 0L;
        }
        long result = this.lastmodified.longValue();
        if (result < 0) {
            return 0L;
        }
        return result;
    }

    public void setLastModified(long lastmodified) {
        checkAttributesAllowed();
        this.lastmodified = Long.valueOf(lastmodified);
    }

    public boolean isDirectory() {
        if (isReference()) {
            return getRef().isDirectory();
        }
        return this.directory != null && this.directory.booleanValue();
    }

    public void setDirectory(boolean directory) {
        checkAttributesAllowed();
        this.directory = directory ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setSize(long size) {
        checkAttributesAllowed();
        this.size = Long.valueOf(size > -1 ? size : -1L);
    }

    public long getSize() {
        if (isReference()) {
            return getRef().getSize();
        }
        if (isExists()) {
            if (this.size != null) {
                return this.size.longValue();
            }
            return -1L;
        }
        return 0L;
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("CloneNotSupportedException for a Resource caught. Derived classes must support cloning.");
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Resource other) {
        if (isReference()) {
            return getRef().compareTo(other);
        }
        return toString().compareTo(other.toString());
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (isReference()) {
            return getRef().equals(other);
        }
        return other != null && other.getClass().equals(getClass()) && compareTo((Resource) other) == 0;
    }

    public int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        String name = getName();
        return MAGIC * (name == null ? NULL_NAME : name.hashCode());
    }

    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return getRef().getInputStream();
        }
        throw new UnsupportedOperationException();
    }

    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        throw new UnsupportedOperationException();
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        return isReference() ? getRef().iterator() : Collections.singleton(this).iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return 1;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return (isReference() && getRef().isFilesystemOnly()) || as(FileProvider.class) != null;
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        String n = getName();
        return n == null ? "(anonymous)" : n;
    }

    public final String toLongString() {
        return isReference() ? getRef().toLongString() : getDataTypeName() + " \"" + toString() + '\"';
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.name != null || this.exists != null || this.lastmodified != null || this.directory != null || this.size != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public <T> T as(Class<T> clazz) {
        if (clazz.isAssignableFrom(getClass())) {
            return clazz.cast(this);
        }
        return null;
    }

    public <T> Optional<T> asOptional(Class<T> clazz) {
        return Optional.ofNullable(as(clazz));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Resource getRef() {
        return (Resource) getCheckedRef(Resource.class);
    }
}
