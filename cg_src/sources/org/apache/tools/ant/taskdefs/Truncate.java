package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Truncate.class */
public class Truncate extends Task {
    private static final int BUFFER_SIZE = 1024;
    private static final String NO_CHILD = "No files specified.";
    private static final String INVALID_LENGTH = "Cannot truncate to length ";
    private static final String READ_WRITE = "rw";
    private Path path;
    private boolean create = true;
    private boolean mkdirs = false;
    private Long length;
    private Long adjust;
    private static final Long ZERO = 0L;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final byte[] FILL_BUFFER = new byte[1024];

    public void setFile(File f) {
        add(new FileResource(f));
    }

    public void add(ResourceCollection rc) {
        getPath().add(rc);
    }

    public void setAdjust(Long adjust) {
        this.adjust = adjust;
    }

    public void setLength(Long length) {
        this.length = length;
        if (length != null && length.longValue() < 0) {
            throw new BuildException(INVALID_LENGTH + length);
        }
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public void setMkdirs(boolean mkdirs) {
        this.mkdirs = mkdirs;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.length != null && this.adjust != null) {
            throw new BuildException("length and adjust are mutually exclusive options");
        }
        if (this.length == null && this.adjust == null) {
            this.length = ZERO;
        }
        if (this.path == null) {
            throw new BuildException(NO_CHILD);
        }
        Iterator<Resource> it = this.path.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            File f = ((FileProvider) r.as(FileProvider.class)).getFile();
            if (shouldProcess(f)) {
                process(f);
            }
        }
    }

    private boolean shouldProcess(File f) {
        if (f.isFile()) {
            return true;
        }
        if (!this.create) {
            return false;
        }
        Exception exception = null;
        try {
            if (FILE_UTILS.createNewFile(f, this.mkdirs)) {
                return true;
            }
        } catch (IOException e) {
            exception = e;
        }
        String msg = "Unable to create " + f;
        if (exception == null) {
            log(msg, 1);
            return false;
        }
        throw new BuildException(msg, exception);
    }

    private void process(File f) {
        long len = f.length();
        long newLength = this.length == null ? len + this.adjust.longValue() : this.length.longValue();
        if (len == newLength) {
            return;
        }
        RandomAccessFile raf = null;
        try {
            try {
                raf = new RandomAccessFile(f, READ_WRITE);
                try {
                    if (newLength > len) {
                        long pos = len;
                        raf.seek(pos);
                        while (pos < newLength) {
                            long writeCount = Math.min(FILL_BUFFER.length, newLength - pos);
                            raf.write(FILL_BUFFER, 0, (int) writeCount);
                            pos += writeCount;
                        }
                    } else {
                        raf.setLength(newLength);
                    }
                    try {
                        raf.close();
                    } catch (IOException e) {
                        log("Caught " + e + " closing " + raf, 1);
                    }
                } catch (IOException e2) {
                    throw new BuildException("Exception working with " + raf, e2);
                }
            } catch (Exception e3) {
                throw new BuildException("Could not open " + f + " for writing", e3);
            }
        } catch (Throwable th) {
            try {
                raf.close();
            } catch (IOException e4) {
                log("Caught " + e4 + " closing " + raf, 1);
            }
            throw th;
        }
    }

    private synchronized Path getPath() {
        if (this.path == null) {
            this.path = new Path(getProject());
        }
        return this.path;
    }
}
