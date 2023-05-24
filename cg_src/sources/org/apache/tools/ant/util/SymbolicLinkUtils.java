package org.apache.tools.ant.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/SymbolicLinkUtils.class */
public class SymbolicLinkUtils {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final SymbolicLinkUtils PRIMARY_INSTANCE = new SymbolicLinkUtils();

    public static SymbolicLinkUtils getSymbolicLinkUtils() {
        return PRIMARY_INSTANCE;
    }

    protected SymbolicLinkUtils() {
    }

    public boolean isSymbolicLink(File file) throws IOException {
        return isSymbolicLink(file.getParentFile(), file.getName());
    }

    public boolean isSymbolicLink(String name) throws IOException {
        return isSymbolicLink(new File(name));
    }

    public boolean isSymbolicLink(File parent, String name) throws IOException {
        File file;
        if (parent != null) {
            file = new File(parent.getCanonicalPath(), name);
        } else {
            file = new File(name);
        }
        File toTest = file;
        return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
    }

    public boolean isDanglingSymbolicLink(String name) throws IOException {
        return isDanglingSymbolicLink(new File(name));
    }

    public boolean isDanglingSymbolicLink(File file) throws IOException {
        return isDanglingSymbolicLink(file.getParentFile(), file.getName());
    }

    public boolean isDanglingSymbolicLink(File parent, String name) throws IOException {
        File f = new File(parent, name);
        if (!f.exists()) {
            String localName = f.getName();
            String[] c = parent.list(d, n -> {
                return localName.equals(n);
            });
            return c != null && c.length > 0;
        }
        return false;
    }

    public void deleteSymbolicLink(File link, Task task) throws IOException {
        if (isDanglingSymbolicLink(link)) {
            if (!link.delete()) {
                throw new IOException("failed to remove dangling symbolic link " + link);
            }
        } else if (!isSymbolicLink(link)) {
        } else {
            if (!link.exists()) {
                throw new FileNotFoundException("No such symbolic link: " + link);
            }
            File target = link.getCanonicalFile();
            if (task == null || target.getParentFile().canWrite()) {
                Project project = task == null ? null : task.getProject();
                File temp = FILE_UTILS.createTempFile(project, "symlink", ".tmp", target.getParentFile(), false, false);
                if (FILE_UTILS.isLeadingPath(target, link)) {
                    link = new File(temp, FILE_UTILS.removeLeadingPath(target, link));
                }
                boolean renamedTarget = false;
                try {
                    try {
                        FILE_UTILS.rename(target, temp);
                        renamedTarget = true;
                        if (!link.delete()) {
                            throw new IOException("Couldn't delete symlink: " + link + " (was it a real file? is this not a UNIX system?)");
                        }
                        if (1 != 0) {
                            try {
                                FILE_UTILS.rename(temp, target);
                                return;
                            } catch (IOException e) {
                                String msg = "Couldn't return resource " + temp + " to its original name: " + target.getAbsolutePath() + ". Reason: " + e.getMessage() + "\n THE RESOURCE'S NAME ON DISK HAS BEEN CHANGED BY THIS ERROR!\n";
                                if (1 != 0) {
                                    throw new IOException(msg);
                                }
                                System.err.println(msg);
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        if (renamedTarget) {
                            try {
                                FILE_UTILS.rename(temp, target);
                            } catch (IOException e2) {
                                String msg2 = "Couldn't return resource " + temp + " to its original name: " + target.getAbsolutePath() + ". Reason: " + e2.getMessage() + "\n THE RESOURCE'S NAME ON DISK HAS BEEN CHANGED BY THIS ERROR!\n";
                                if (0 != 0) {
                                    throw new IOException(msg2);
                                }
                                System.err.println(msg2);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e3) {
                    throw new IOException("Couldn't rename resource when attempting to delete '" + link + "'.  Reason: " + e3.getMessage());
                }
            }
            Execute.runCommand(task, "rm", link.getAbsolutePath());
        }
    }
}
