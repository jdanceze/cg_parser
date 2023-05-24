package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Iterator;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.comparators.Date;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.apache.tools.ant.types.resources.comparators.Reverse;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.StreamUtils;
import soot.coffi.Instruction;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DependSet.class */
public class DependSet extends MatchingTask {
    private static final ResourceSelector NOT_EXISTS = new Not(new Exists());
    private static final ResourceComparator DATE = new Date();
    private static final ResourceComparator REVERSE_DATE = new Reverse(DATE);
    private Union sources = null;
    private Path targets = null;
    private boolean verbose;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DependSet$NonExistent.class */
    public static final class NonExistent extends Restrict {
        private NonExistent(ResourceCollection rc) {
            super.add(rc);
            super.add(DependSet.NOT_EXISTS);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DependSet$HideMissingBasedir.class */
    private static final class HideMissingBasedir implements ResourceCollection {
        private FileSet fs;

        private HideMissingBasedir(FileSet fs) {
            this.fs = fs;
        }

        @Override // java.lang.Iterable
        public Iterator<Resource> iterator() {
            return basedirExists() ? this.fs.iterator() : Resources.EMPTY_ITERATOR;
        }

        @Override // org.apache.tools.ant.types.ResourceCollection
        public int size() {
            if (basedirExists()) {
                return this.fs.size();
            }
            return 0;
        }

        @Override // org.apache.tools.ant.types.ResourceCollection
        public boolean isFilesystemOnly() {
            return true;
        }

        private boolean basedirExists() {
            File basedir = this.fs.getDir();
            return basedir == null || basedir.exists();
        }
    }

    public synchronized Union createSources() {
        this.sources = this.sources == null ? new Union() : this.sources;
        return this.sources;
    }

    public void addSrcfileset(FileSet fs) {
        createSources().add(fs);
    }

    public void addSrcfilelist(FileList fl) {
        createSources().add(fl);
    }

    public synchronized Path createTargets() {
        this.targets = this.targets == null ? new Path(getProject()) : this.targets;
        return this.targets;
    }

    public void addTargetfileset(FileSet fs) {
        createTargets().add(new HideMissingBasedir(fs));
    }

    public void addTargetfilelist(FileList fl) {
        createTargets().add(fl);
    }

    public void setVerbose(boolean b) {
        this.verbose = b;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String[] list;
        if (this.sources == null) {
            throw new BuildException("At least one set of source resources must be specified");
        }
        if (this.targets == null) {
            throw new BuildException("At least one set of target files must be specified");
        }
        if (!this.sources.isEmpty() && !this.targets.isEmpty() && !uptodate(this.sources, this.targets)) {
            log("Deleting all target files.", 3);
            if (this.verbose) {
                for (String t : this.targets.list()) {
                    log("Deleting " + t);
                }
            }
            Delete delete = new Delete();
            delete.bindToOwner(this);
            delete.add(this.targets);
            delete.perform();
        }
    }

    private boolean uptodate(ResourceCollection src, ResourceCollection target) {
        org.apache.tools.ant.types.resources.selectors.Date datesel = new org.apache.tools.ant.types.resources.selectors.Date();
        datesel.setMillis(System.currentTimeMillis());
        datesel.setWhen(TimeComparison.AFTER);
        datesel.setGranularity(0L);
        logFuture(this.targets, datesel);
        NonExistent missingTargets = new NonExistent(this.targets);
        int neTargets = missingTargets.size();
        if (neTargets > 0) {
            log(neTargets + " nonexistent targets", 3);
            logMissing(missingTargets, TypeProxy.INSTANCE_FIELD);
            return false;
        }
        Resource oldestTarget = getOldest(this.targets);
        logWithModificationTime(oldestTarget, "oldest target file");
        logFuture(this.sources, datesel);
        NonExistent missingSources = new NonExistent(this.sources);
        int neSources = missingSources.size();
        if (neSources > 0) {
            log(neSources + " nonexistent sources", 3);
            logMissing(missingSources, RIFLConstants.SOURCE_TAG);
            return false;
        }
        Resource newestSource = getNewest(this.sources);
        logWithModificationTime(newestSource, "newest source");
        return oldestTarget.getLastModified() >= newestSource.getLastModified();
    }

    private void logFuture(ResourceCollection rc, ResourceSelector rsel) {
        Restrict r = new Restrict();
        r.add(rsel);
        r.add(rc);
        Iterator<Resource> it = r.iterator();
        while (it.hasNext()) {
            Resource res = it.next();
            log("Warning: " + res + " modified in the future.", 1);
        }
    }

    private Resource getXest(ResourceCollection rc, ResourceComparator c) {
        return (Resource) StreamUtils.iteratorAsStream(rc.iterator()).max(c).orElse(null);
    }

    private Resource getOldest(ResourceCollection rc) {
        return getXest(rc, REVERSE_DATE);
    }

    private Resource getNewest(ResourceCollection rc) {
        return getXest(rc, DATE);
    }

    private void logWithModificationTime(Resource r, String what) {
        log(r.toLongString() + " is " + what + ", modified at " + new java.util.Date(r.getLastModified()), this.verbose ? 2 : 3);
    }

    private void logMissing(ResourceCollection missing, String what) {
        if (this.verbose) {
            for (Resource r : missing) {
                log("Expected " + what + Instruction.argsep + r.toLongString() + " is missing.");
            }
        }
    }
}
