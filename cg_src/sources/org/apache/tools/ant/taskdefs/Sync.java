package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.util.FileUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Sync.class */
public class Sync extends Task {
    private MyCopy myCopy;
    private SyncTarget syncTarget;
    private Resources resources = null;

    @Override // org.apache.tools.ant.Task
    public void init() throws BuildException {
        this.myCopy = new MyCopy();
        configureTask(this.myCopy);
        this.myCopy.setFiltering(false);
        this.myCopy.setIncludeEmptyDirs(false);
        this.myCopy.setPreserveLastModified(true);
    }

    private void configureTask(Task helper) {
        helper.setProject(getProject());
        helper.setTaskName(getTaskName());
        helper.setOwningTarget(getOwningTarget());
        helper.init();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        int removedDirCount;
        File toDir = this.myCopy.getToDir();
        Set<String> allFiles = this.myCopy.nonOrphans;
        boolean noRemovalNecessary = !toDir.exists() || toDir.list().length < 1;
        log("PASS#1: Copying files to " + toDir, 4);
        this.myCopy.execute();
        if (noRemovalNecessary) {
            log("NO removing necessary in " + toDir, 4);
            return;
        }
        Set<File> preservedDirectories = new LinkedHashSet<>();
        log("PASS#2: Removing orphan files from " + toDir, 4);
        int[] removedFileCount = removeOrphanFiles(allFiles, toDir, preservedDirectories);
        logRemovedCount(removedFileCount[0], "dangling director", "y", "ies");
        logRemovedCount(removedFileCount[1], "dangling file", "", "s");
        if (!this.myCopy.getIncludeEmptyDirs() || getExplicitPreserveEmptyDirs() == Boolean.FALSE) {
            log("PASS#3: Removing empty directories from " + toDir, 4);
            if (!this.myCopy.getIncludeEmptyDirs()) {
                removedDirCount = removeEmptyDirectories(toDir, false, preservedDirectories);
            } else {
                removedDirCount = removeEmptyDirectories(preservedDirectories);
            }
            logRemovedCount(removedDirCount, "empty director", "y", "ies");
        }
    }

    private void logRemovedCount(int count, String prefix, String singularSuffix, String pluralSuffix) {
        File toDir = this.myCopy.getToDir();
        String what = (prefix == null ? "" : prefix) + (count < 2 ? singularSuffix : pluralSuffix);
        if (count > 0) {
            log("Removed " + count + Instruction.argsep + what + " from " + toDir, 2);
        } else {
            log("NO " + what + " to remove from " + toDir, 3);
        }
    }

    private int[] removeOrphanFiles(Set<String> nonOrphans, File toDir, Set<File> preservedDirectories) {
        DirectoryScanner ds;
        String[] includedFiles;
        int[] removedCount = {0, 0};
        String[] excls = (String[]) nonOrphans.toArray(new String[nonOrphans.size() + 1]);
        excls[nonOrphans.size()] = "";
        if (this.syncTarget == null) {
            ds = new DirectoryScanner();
            ds.setBasedir(toDir);
            Optional<Boolean> isCaseSensitiveFileSystem = FileUtils.isCaseSensitiveFileSystem(toDir.toPath());
            Objects.requireNonNull(ds);
            isCaseSensitiveFileSystem.ifPresent((v1) -> {
                r1.setCaseSensitive(v1);
            });
        } else {
            FileSet fs = this.syncTarget.toFileSet(false);
            fs.setDir(toDir);
            PatternSet ps = this.syncTarget.mergePatterns(getProject());
            fs.appendExcludes(ps.getIncludePatterns(getProject()));
            fs.appendIncludes(ps.getExcludePatterns(getProject()));
            fs.setDefaultexcludes(!this.syncTarget.getDefaultexcludes());
            FileSelector[] s = this.syncTarget.getSelectors(getProject());
            if (s.length > 0) {
                NoneSelector ns = new NoneSelector();
                for (FileSelector element : s) {
                    ns.appendSelector(element);
                }
                fs.appendSelector(ns);
            }
            ds = fs.getDirectoryScanner(getProject());
        }
        ds.addExcludes(excls);
        ds.scan();
        for (String file : ds.getIncludedFiles()) {
            File f = new File(toDir, file);
            log("Removing orphan file: " + f, 4);
            f.delete();
            removedCount[1] = removedCount[1] + 1;
        }
        String[] dirs = ds.getIncludedDirectories();
        for (int i = dirs.length - 1; i >= 0; i--) {
            File f2 = new File(toDir, dirs[i]);
            String[] children = f2.list();
            if (children == null || children.length < 1) {
                log("Removing orphan directory: " + f2, 4);
                f2.delete();
                removedCount[0] = removedCount[0] + 1;
            }
        }
        Boolean ped = getExplicitPreserveEmptyDirs();
        if (ped != null && ped.booleanValue() != this.myCopy.getIncludeEmptyDirs()) {
            FileSet fs2 = this.syncTarget.toFileSet(true);
            fs2.setDir(toDir);
            String[] preservedDirs = fs2.getDirectoryScanner(getProject()).getIncludedDirectories();
            for (int i2 = preservedDirs.length - 1; i2 >= 0; i2--) {
                preservedDirectories.add(new File(toDir, preservedDirs[i2]));
            }
        }
        return removedCount;
    }

    private int removeEmptyDirectories(File dir, boolean removeIfEmpty, Set<File> preservedEmptyDirectories) {
        int removedCount = 0;
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File file : children) {
                if (file.isDirectory()) {
                    removedCount += removeEmptyDirectories(file, true, preservedEmptyDirectories);
                }
            }
            if (children.length > 0) {
                children = dir.listFiles();
            }
            if (children.length < 1 && removeIfEmpty && !preservedEmptyDirectories.contains(dir)) {
                log("Removing empty directory: " + dir, 4);
                dir.delete();
                removedCount++;
            }
        }
        return removedCount;
    }

    private int removeEmptyDirectories(Set<File> preservedEmptyDirectories) {
        int removedCount = 0;
        for (File f : preservedEmptyDirectories) {
            String[] s = f.list();
            if (s == null || s.length == 0) {
                log("Removing empty directory: " + f, 4);
                f.delete();
                removedCount++;
            }
        }
        return removedCount;
    }

    public void setTodir(File destDir) {
        this.myCopy.setTodir(destDir);
    }

    public void setVerbose(boolean verbose) {
        this.myCopy.setVerbose(verbose);
    }

    public void setOverwrite(boolean overwrite) {
        this.myCopy.setOverwrite(overwrite);
    }

    public void setIncludeEmptyDirs(boolean includeEmpty) {
        this.myCopy.setIncludeEmptyDirs(includeEmpty);
    }

    public void setFailOnError(boolean failonerror) {
        this.myCopy.setFailOnError(failonerror);
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection rc) {
        if ((rc instanceof FileSet) && rc.isFilesystemOnly()) {
            this.myCopy.add(rc);
            return;
        }
        if (this.resources == null) {
            Restrict r = new Restrict();
            r.add(new Exists());
            this.resources = new Resources();
            r.add(this.resources);
            this.myCopy.add(r);
        }
        this.resources.add(rc);
    }

    public void setGranularity(long granularity) {
        this.myCopy.setGranularity(granularity);
    }

    public void addPreserveInTarget(SyncTarget s) {
        if (this.syncTarget != null) {
            throw new BuildException("you must not specify multiple preserveintarget elements.");
        }
        this.syncTarget = s;
    }

    private Boolean getExplicitPreserveEmptyDirs() {
        if (this.syncTarget == null) {
            return null;
        }
        return this.syncTarget.getPreserveEmptyDirs();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Sync$MyCopy.class */
    public static class MyCopy extends Copy {
        private Set<String> nonOrphans = new HashSet();

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.apache.tools.ant.taskdefs.Copy
        public void scan(File fromDir, File toDir, String[] files, String[] dirs) {
            Sync.assertTrue("No mapper", this.mapperElement == null);
            super.scan(fromDir, toDir, files, dirs);
            Collections.addAll(this.nonOrphans, files);
            Collections.addAll(this.nonOrphans, dirs);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.apache.tools.ant.taskdefs.Copy
        public Map<Resource, String[]> scan(Resource[] resources, File toDir) {
            Sync.assertTrue("No mapper", this.mapperElement == null);
            Stream map = Stream.of((Object[]) resources).map((v0) -> {
                return v0.getName();
            });
            Set<String> set = this.nonOrphans;
            Objects.requireNonNull(set);
            map.forEach((v1) -> {
                r1.add(v1);
            });
            return super.scan(resources, toDir);
        }

        public File getToDir() {
            return this.destDir;
        }

        public boolean getIncludeEmptyDirs() {
            return this.includeEmpty;
        }

        @Override // org.apache.tools.ant.taskdefs.Copy
        protected boolean supportsNonFileResources() {
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Sync$SyncTarget.class */
    public static class SyncTarget extends AbstractFileSet {
        private Boolean preserveEmptyDirs;

        @Override // org.apache.tools.ant.types.AbstractFileSet
        public void setDir(File dir) throws BuildException {
            throw new BuildException("preserveintarget doesn't support the dir attribute");
        }

        public void setPreserveEmptyDirs(boolean b) {
            this.preserveEmptyDirs = Boolean.valueOf(b);
        }

        public Boolean getPreserveEmptyDirs() {
            return this.preserveEmptyDirs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FileSet toFileSet(boolean withPatterns) {
            FileSelector[] selectors;
            FileSet fs = new FileSet();
            fs.setCaseSensitive(isCaseSensitive());
            fs.setFollowSymlinks(isFollowSymlinks());
            fs.setMaxLevelsOfSymlinks(getMaxLevelsOfSymlinks());
            fs.setProject(getProject());
            if (withPatterns) {
                PatternSet ps = mergePatterns(getProject());
                fs.appendIncludes(ps.getIncludePatterns(getProject()));
                fs.appendExcludes(ps.getExcludePatterns(getProject()));
                for (FileSelector sel : getSelectors(getProject())) {
                    fs.appendSelector(sel);
                }
                fs.setDefaultexcludes(getDefaultexcludes());
            }
            return fs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new BuildException("Assertion Error: " + message);
        }
    }
}
