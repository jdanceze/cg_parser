package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Move.class */
public class Move extends Copy {
    private boolean performGc = Os.isFamily(Os.FAMILY_WINDOWS);

    public Move() {
        setOverwrite(true);
    }

    public void setPerformGcOnFailedDelete(boolean b) {
        this.performGc = b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Copy
    public void validateAttributes() throws BuildException {
        if (this.file != null && this.file.isDirectory()) {
            if ((this.destFile != null && this.destDir != null) || (this.destFile == null && this.destDir == null)) {
                throw new BuildException("One and only one of tofile and todir must be set.");
            }
            this.destFile = this.destFile == null ? new File(this.destDir, this.file.getName()) : this.destFile;
            this.destDir = this.destDir == null ? this.destFile.getParentFile() : this.destDir;
            this.completeDirMap.put(this.file, this.destFile);
            this.file = null;
            return;
        }
        super.validateAttributes();
    }

    @Override // org.apache.tools.ant.taskdefs.Copy
    protected void doFileOperations() {
        String[] value;
        if (this.completeDirMap.size() > 0) {
            for (Map.Entry<File, File> entry : this.completeDirMap.entrySet()) {
                File fromDir = entry.getKey();
                File toDir = entry.getValue();
                try {
                    log("Attempting to rename dir: " + fromDir + " to " + toDir, this.verbosity);
                    boolean renamed = renameFile(fromDir, toDir, this.filtering, this.forceOverwrite);
                    if (!renamed) {
                        FileSet fs = new FileSet();
                        fs.setProject(getProject());
                        fs.setDir(fromDir);
                        addFileset(fs);
                        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                        scan(fromDir, toDir, ds.getIncludedFiles(), ds.getIncludedDirectories());
                    }
                } catch (IOException ioe) {
                    String msg = "Failed to rename dir " + fromDir + " to " + toDir + " due to " + ioe.getMessage();
                    throw new BuildException(msg, ioe, getLocation());
                }
            }
        }
        int moveCount = this.fileCopyMap.size();
        if (moveCount > 0) {
            log("Moving " + moveCount + " file" + (moveCount == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
            for (Map.Entry<String, String[]> entry2 : this.fileCopyMap.entrySet()) {
                String fromFile = entry2.getKey();
                File f = new File(fromFile);
                boolean selfMove = false;
                if (f.exists()) {
                    String[] toFiles = entry2.getValue();
                    for (int i = 0; i < toFiles.length; i++) {
                        String toFile = toFiles[i];
                        if (fromFile.equals(toFile)) {
                            log("Skipping self-move of " + fromFile, this.verbosity);
                            selfMove = true;
                        } else {
                            File d = new File(toFile);
                            if (i + 1 == toFiles.length && !selfMove) {
                                moveFile(f, d, this.filtering, this.forceOverwrite);
                            } else {
                                copyFile(f, d, this.filtering, this.forceOverwrite);
                            }
                        }
                    }
                }
            }
        }
        if (this.includeEmpty) {
            int createCount = 0;
            for (Map.Entry<String, String[]> entry3 : this.dirCopyMap.entrySet()) {
                String fromDirName = entry3.getKey();
                boolean selfMove2 = false;
                for (String toDirName : entry3.getValue()) {
                    if (fromDirName.equals(toDirName)) {
                        log("Skipping self-move of " + fromDirName, this.verbosity);
                        selfMove2 = true;
                    } else {
                        File d2 = new File(toDirName);
                        if (!d2.exists()) {
                            if (!d2.mkdirs() && !d2.exists()) {
                                log("Unable to create directory " + d2.getAbsolutePath(), 0);
                            } else {
                                createCount++;
                            }
                        }
                    }
                }
                File fromDir2 = new File(fromDirName);
                if (!selfMove2 && okToDelete(fromDir2)) {
                    deleteDir(fromDir2);
                }
            }
            if (createCount > 0) {
                log("Moved " + this.dirCopyMap.size() + " empty director" + (this.dirCopyMap.size() == 1 ? "y" : "ies") + " to " + createCount + " empty director" + (createCount == 1 ? "y" : "ies") + " under " + this.destDir.getAbsolutePath());
            }
        }
    }

    private void moveFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
        try {
            log("Attempting to rename: " + fromFile + " to " + toFile, this.verbosity);
            boolean moved = renameFile(fromFile, toFile, filtering, this.forceOverwrite);
            if (!moved) {
                copyFile(fromFile, toFile, filtering, overwrite);
                if (!getFileUtils().tryHardToDelete(fromFile, this.performGc)) {
                    throw new BuildException("Unable to delete file %s", fromFile.getAbsolutePath());
                }
            }
        } catch (IOException ioe) {
            throw new BuildException("Failed to rename " + fromFile + " to " + toFile + " due to " + ioe.getMessage(), ioe, getLocation());
        }
    }

    private void copyFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
        try {
            log("Copying " + fromFile + " to " + toFile, this.verbosity);
            FilterSetCollection executionFilters = new FilterSetCollection();
            if (filtering) {
                executionFilters.addFilterSet(getProject().getGlobalFilterSet());
            }
            Vector<FilterSet> filterSets = getFilterSets();
            Objects.requireNonNull(executionFilters);
            filterSets.forEach(this::addFilterSet);
            getFileUtils().copyFile(fromFile, toFile, executionFilters, getFilterChains(), this.forceOverwrite, getPreserveLastModified(), false, getEncoding(), getOutputEncoding(), getProject(), getForce());
        } catch (IOException ioe) {
            throw new BuildException("Failed to copy " + fromFile + " to " + toFile + " due to " + ioe.getMessage(), ioe, getLocation());
        }
    }

    protected boolean okToDelete(File d) {
        String[] list = d.list();
        if (list == null) {
            return false;
        }
        for (String s : list) {
            File f = new File(d, s);
            if (!f.isDirectory() || !okToDelete(f)) {
                return false;
            }
        }
        return true;
    }

    protected void deleteDir(File d) {
        deleteDir(d, false);
    }

    protected void deleteDir(File d, boolean deleteFiles) {
        String[] list = d.list();
        if (list == null) {
            return;
        }
        for (String s : list) {
            File f = new File(d, s);
            if (f.isDirectory()) {
                deleteDir(f);
            } else if (deleteFiles && !getFileUtils().tryHardToDelete(f, this.performGc)) {
                throw new BuildException("Unable to delete file %s", f.getAbsolutePath());
            } else {
                throw new BuildException("UNEXPECTED ERROR - The file %s should not exist!", f.getAbsolutePath());
            }
        }
        log("Deleting directory " + d.getAbsolutePath(), this.verbosity);
        if (!getFileUtils().tryHardToDelete(d, this.performGc)) {
            throw new BuildException("Unable to delete directory %s", d.getAbsolutePath());
        }
    }

    protected boolean renameFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException, BuildException {
        if (destFile.isDirectory() || filtering || !getFilterSets().isEmpty() || !getFilterChains().isEmpty()) {
            return false;
        }
        if (destFile.isFile() && !destFile.canWrite()) {
            if (!getForce()) {
                throw new IOException(String.format("can't replace read-only destination file %s", destFile));
            }
            if (!getFileUtils().tryHardToDelete(destFile)) {
                throw new IOException(String.format("failed to delete read-only destination file %s", destFile));
            }
        }
        File parent = destFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        } else if (destFile.isFile()) {
            sourceFile = getFileUtils().normalize(sourceFile.getAbsolutePath()).getCanonicalFile();
            destFile = getFileUtils().normalize(destFile.getAbsolutePath());
            if (destFile.getAbsolutePath().equals(sourceFile.getAbsolutePath())) {
                log("Rename of " + sourceFile + " to " + destFile + " is a no-op.", 3);
                return true;
            } else if (!getFileUtils().areSame(sourceFile, destFile) && !getFileUtils().tryHardToDelete(destFile, this.performGc)) {
                throw new BuildException("Unable to remove existing file %s", destFile);
            }
        }
        return sourceFile.renameTo(destFile);
    }
}
