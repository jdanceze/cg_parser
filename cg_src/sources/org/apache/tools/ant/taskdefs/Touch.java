package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Touchable;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Touch.class */
public class Touch extends Task {
    public static final DateFormatFactory DEFAULT_DF_FACTORY = new DateFormatFactory() { // from class: org.apache.tools.ant.taskdefs.Touch.1
        @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
        public DateFormat getPrimaryFormat() {
            return DateUtils.EN_US_DATE_FORMAT_MIN.get();
        }

        @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
        public DateFormat getFallbackFormat() {
            return DateUtils.EN_US_DATE_FORMAT_SEC.get();
        }
    };
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File file;
    private String dateTime;
    private Union resources;
    private boolean dateTimeConfigured;
    private boolean mkdirs;
    private long millis = -1;
    private List<FileSet> filesets = new Vector();
    private boolean verbose = true;
    private FileNameMapper fileNameMapper = null;
    private DateFormatFactory dfFactory = DEFAULT_DF_FACTORY;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Touch$DateFormatFactory.class */
    public interface DateFormatFactory {
        DateFormat getPrimaryFormat();

        DateFormat getFallbackFormat();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public void setDatetime(String dateTime) {
        if (this.dateTime != null) {
            log("Resetting datetime attribute to " + dateTime, 3);
        }
        this.dateTime = dateTime;
        this.dateTimeConfigured = false;
    }

    public void setMkdirs(boolean mkdirs) {
        this.mkdirs = mkdirs;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setPattern(final String pattern) {
        this.dfFactory = new DateFormatFactory() { // from class: org.apache.tools.ant.taskdefs.Touch.2
            @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
            public DateFormat getPrimaryFormat() {
                return new SimpleDateFormat(pattern);
            }

            @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
            public DateFormat getFallbackFormat() {
                return null;
            }
        };
    }

    public void addConfiguredMapper(Mapper mapper) {
        add(mapper.getImplementation());
    }

    public void add(FileNameMapper fileNameMapper) throws BuildException {
        if (this.fileNameMapper != null) {
            throw new BuildException("Only one mapper may be added to the %s task.", getTaskName());
        }
        this.fileNameMapper = fileNameMapper;
    }

    public void addFileset(FileSet set) {
        this.filesets.add(set);
        add(set);
    }

    public void addFilelist(FileList list) {
        add(list);
    }

    public synchronized void add(ResourceCollection rc) {
        this.resources = this.resources == null ? new Union() : this.resources;
        this.resources.add(rc);
    }

    protected synchronized void checkConfiguration() throws BuildException {
        if (this.file == null && this.resources == null) {
            throw new BuildException("Specify at least one source--a file or resource collection.");
        }
        if (this.file != null && this.file.exists() && this.file.isDirectory()) {
            throw new BuildException("Use a resource collection to touch directories.");
        }
        if (this.dateTime != null && !this.dateTimeConfigured) {
            long workmillis = this.millis;
            if ("now".equalsIgnoreCase(this.dateTime)) {
                workmillis = System.currentTimeMillis();
            } else {
                ParseException pe = null;
                try {
                    workmillis = this.dfFactory.getPrimaryFormat().parse(this.dateTime).getTime();
                } catch (ParseException peOne) {
                    DateFormat df = this.dfFactory.getFallbackFormat();
                    if (df == null) {
                        pe = peOne;
                    } else {
                        try {
                            workmillis = df.parse(this.dateTime).getTime();
                        } catch (ParseException peTwo) {
                            pe = peTwo;
                        }
                    }
                }
                if (pe != null) {
                    throw new BuildException(pe.getMessage(), pe, getLocation());
                }
                if (workmillis < 0) {
                    throw new BuildException("Date of %s results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).", this.dateTime);
                }
            }
            log("Setting millis to " + workmillis + " from datetime attribute", this.millis < 0 ? 4 : 3);
            setMillis(workmillis);
            this.dateTimeConfigured = true;
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        checkConfiguration();
        touch();
    }

    protected void touch() throws BuildException {
        String[] includedDirectories;
        long defaultTimestamp = getTimestamp();
        if (this.file != null) {
            touch(new FileResource(this.file.getParentFile(), this.file.getName()), defaultTimestamp);
        }
        if (this.resources == null) {
            return;
        }
        Iterator<Resource> it = this.resources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            Touchable t = (Touchable) r.as(Touchable.class);
            if (t == null) {
                throw new BuildException("Can't touch " + r);
            }
            touch(r, defaultTimestamp);
        }
        for (FileSet fs : this.filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File fromDir = fs.getDir(getProject());
            for (String srcDir : ds.getIncludedDirectories()) {
                touch(new FileResource(fromDir, srcDir), defaultTimestamp);
            }
        }
    }

    @Deprecated
    protected void touch(File file) {
        touch(file, getTimestamp());
    }

    private long getTimestamp() {
        return this.millis < 0 ? System.currentTimeMillis() : this.millis;
    }

    private void touch(Resource r, long defaultTimestamp) {
        if (this.fileNameMapper == null) {
            FileProvider fp = (FileProvider) r.as(FileProvider.class);
            if (fp != null) {
                touch(fp.getFile(), defaultTimestamp);
                return;
            } else {
                ((Touchable) r.as(Touchable.class)).touch(defaultTimestamp);
                return;
            }
        }
        String[] mapped = this.fileNameMapper.mapFileName(r.getName());
        if (mapped != null && mapped.length > 0) {
            long modTime = defaultTimestamp;
            if (this.millis < 0 && r.isExists()) {
                modTime = r.getLastModified();
            }
            for (String fileName : mapped) {
                touch(getProject().resolveFile(fileName), modTime);
            }
        }
    }

    private void touch(File file, long modTime) {
        if (!file.exists()) {
            log("Creating " + file, this.verbose ? 2 : 3);
            try {
                FILE_UTILS.createNewFile(file, this.mkdirs);
            } catch (IOException ioe) {
                throw new BuildException("Could not create " + file, ioe, getLocation());
            }
        }
        if (!file.canWrite()) {
            throw new BuildException("Can not change modification date of read-only file %s", file);
        }
        FILE_UTILS.setFileLastModified(file, modTime);
    }
}
