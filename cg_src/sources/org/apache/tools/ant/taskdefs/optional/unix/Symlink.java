package org.apache.tools.ant.taskdefs.optional.unix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.dispatch.DispatchTask;
import org.apache.tools.ant.dispatch.DispatchUtils;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.FileSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/unix/Symlink.class */
public class Symlink extends DispatchTask {
    private String resource;
    private String link;
    private String linkFileName;
    private boolean overwrite;
    private boolean failonerror;
    private List<FileSet> fileSets = new ArrayList();
    private boolean executing = false;

    @Override // org.apache.tools.ant.Task
    public void init() throws BuildException {
        super.init();
        setDefaults();
    }

    @Override // org.apache.tools.ant.Task
    public synchronized void execute() throws BuildException {
        if (this.executing) {
            throw new BuildException("Infinite recursion detected in Symlink.execute()");
        }
        try {
            this.executing = true;
            DispatchUtils.execute(this);
        } finally {
            this.executing = false;
        }
    }

    public void single() throws BuildException {
        try {
            if (this.resource == null) {
                handleError("Must define the resource to symlink to!");
            } else if (this.link == null) {
                handleError("Must define the link name for symlink!");
            } else {
                doLink(this.resource, this.link);
            }
        } finally {
            setDefaults();
        }
    }

    public void delete() throws BuildException {
        try {
            if (this.link == null) {
                handleError("Must define the link name for symlink!");
                return;
            }
            Path linkPath = Paths.get(this.link, new String[0]);
            if (!Files.isSymbolicLink(linkPath)) {
                log("Skipping deletion of " + linkPath + " since it's not a symlink", 3);
                return;
            }
            log("Removing symlink: " + this.link);
            deleteSymLink(linkPath);
        } catch (IOException ioe) {
            handleError(ioe.getMessage());
        } finally {
            setDefaults();
        }
    }

    public void recreate() throws BuildException {
        try {
            if (this.fileSets.isEmpty()) {
                handleError("File set identifying link file(s) required for action recreate");
                setDefaults();
                return;
            }
            Properties links = loadLinks(this.fileSets);
            for (String link : links.stringPropertyNames()) {
                String resource = links.getProperty(link);
                try {
                    if (Files.isSymbolicLink(Paths.get(link, new String[0])) && new File(link).getCanonicalPath().equals(new File(resource).getCanonicalPath())) {
                        log("not recreating " + link + " as it points to the correct target already", 4);
                    } else {
                        doLink(resource, link);
                    }
                } catch (IOException e) {
                    String errMessage = "Failed to check if path " + link + " is a symbolic link, linking to " + resource;
                    if (this.failonerror) {
                        throw new BuildException(errMessage, e);
                    }
                    log(errMessage, 2);
                }
            }
        } finally {
            setDefaults();
        }
    }

    public void record() throws BuildException {
        try {
            if (this.fileSets.isEmpty()) {
                handleError("Fileset identifying links to record required");
            } else if (this.linkFileName == null) {
                handleError("Name of file to record links in required");
            } else {
                Map<File, List<File>> byDir = new HashMap<>();
                findLinks(this.fileSets).forEach(link -> {
                    ((List) byDir.computeIfAbsent(link.getParentFile(), k -> {
                        return new ArrayList();
                    })).add(link);
                });
                byDir.forEach(dir, linksInDir -> {
                    Properties linksToStore = new Properties();
                    Iterator it = linksInDir.iterator();
                    while (it.hasNext()) {
                        File link2 = (File) it.next();
                        try {
                            linksToStore.put(link2.getName(), link2.getCanonicalPath());
                        } catch (IOException e) {
                            handleError("Couldn't get canonical name of parent link");
                        }
                    }
                    writePropertyFile(linksToStore, dir);
                });
            }
        } finally {
            setDefaults();
        }
    }

    private void setDefaults() {
        this.resource = null;
        this.link = null;
        this.linkFileName = null;
        this.failonerror = true;
        this.overwrite = false;
        setAction("single");
        this.fileSets.clear();
    }

    public void setOverwrite(boolean owrite) {
        this.overwrite = owrite;
    }

    public void setFailOnError(boolean foe) {
        this.failonerror = foe;
    }

    @Override // org.apache.tools.ant.dispatch.DispatchTask
    public void setAction(String action) {
        super.setAction(action);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setResource(String src) {
        this.resource = src;
    }

    public void setLinkfilename(String lf) {
        this.linkFileName = lf;
    }

    public void addFileset(FileSet set) {
        this.fileSets.add(set);
    }

    @Deprecated
    public static void deleteSymlink(String path) throws IOException {
        deleteSymlink(Paths.get(path, new String[0]).toFile());
    }

    @Deprecated
    public static void deleteSymlink(File linkfil) throws IOException {
        if (!Files.isSymbolicLink(linkfil.toPath())) {
            return;
        }
        deleteSymLink(linkfil.toPath());
    }

    private void writePropertyFile(Properties properties, File dir) throws BuildException {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(new File(dir, this.linkFileName).toPath(), new OpenOption[0]));
            properties.store(bos, "Symlinks from " + dir);
            bos.close();
        } catch (IOException ioe) {
            throw new BuildException(ioe, getLocation());
        }
    }

    private void handleError(String msg) {
        if (this.failonerror) {
            throw new BuildException(msg);
        }
        log(msg);
    }

    private void doLink(String resource, String link) throws BuildException {
        Path linkPath = Paths.get(link, new String[0]);
        Path target = Paths.get(resource, new String[0]);
        boolean alreadyExists = Files.exists(linkPath, LinkOption.NOFOLLOW_LINKS);
        if (!alreadyExists) {
            try {
                log("creating symlink " + linkPath + " -> " + target, 4);
                Files.createSymbolicLink(linkPath, target, new FileAttribute[0]);
            } catch (IOException e) {
                if (this.failonerror) {
                    throw new BuildException("Failed to create symlink " + link + " to target " + resource, e);
                }
                log("Unable to create symlink " + link + " to target " + resource, e, 2);
            }
        } else if (!this.overwrite) {
            log("Skipping symlink creation, since file at " + link + " already exists and overwrite is set to false", 2);
        } else {
            boolean existingFileDeleted = linkPath.toFile().delete();
            if (!existingFileDeleted) {
                handleError("Deletion of file at " + link + " failed, while trying to overwrite it with a symlink");
                return;
            }
            try {
                log("creating symlink " + linkPath + " -> " + target + " after removing original", 4);
                Files.createSymbolicLink(linkPath, target, new FileAttribute[0]);
            } catch (IOException e2) {
                if (this.failonerror) {
                    throw new BuildException("Failed to create symlink " + link + " to target " + resource, e2);
                }
                log("Unable to create symlink " + link + " to target " + resource, e2, 2);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Set<File> findLinks(List<FileSet> fileSets) {
        Set<File> result = new HashSet<>();
        for (FileSet fs : fileSets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File dir = fs.getDir(getProject());
            Stream.of((Object[]) new String[]{ds.getIncludedFiles(), ds.getIncludedDirectories()}).flatMap((v0) -> {
                return Stream.of(v0);
            }).forEach(path -> {
                try {
                    File f = new File(dir, result);
                    File pf = f.getParentFile();
                    String name = f.getName();
                    File parentDirCanonicalizedFile = new File(pf.getCanonicalPath(), name);
                    if (Files.isSymbolicLink(parentDirCanonicalizedFile.toPath())) {
                        dir.add(parentDirCanonicalizedFile);
                    }
                } catch (IOException e) {
                    handleError("IOException: " + result + " omitted");
                }
            });
        }
        return result;
    }

    private Properties loadLinks(List<FileSet> fileSets) {
        String[] includedFiles;
        Properties finalList = new Properties();
        loop0: for (FileSet fs : fileSets) {
            DirectoryScanner ds = new DirectoryScanner();
            fs.setupDirectoryScanner(ds, getProject());
            ds.setFollowSymlinks(false);
            ds.scan();
            File dir = fs.getDir(getProject());
            for (String name : ds.getIncludedFiles()) {
                File inc = new File(dir, name);
                File pf = inc.getParentFile();
                Properties links = new Properties();
                try {
                    InputStream is = new BufferedInputStream(Files.newInputStream(inc.toPath(), new OpenOption[0]));
                    try {
                        links.load(is);
                        File pf2 = pf.getCanonicalFile();
                        is.close();
                        try {
                            links.store(new PrintStream(new LogOutputStream((Task) this, 2)), "listing properties");
                        } catch (IOException e) {
                            log("failed to log unshortened properties");
                            links.list(new PrintStream(new LogOutputStream((Task) this, 2)));
                        }
                        for (String key : links.stringPropertyNames()) {
                            finalList.put(new File(pf2, key).getAbsolutePath(), links.getProperty(key));
                        }
                    } catch (Throwable th) {
                        try {
                            is.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                        break loop0;
                    }
                } catch (FileNotFoundException e2) {
                    handleError("Unable to find " + name + "; skipping it.");
                } catch (IOException e3) {
                    handleError("Unable to open " + name + " or its parent dir; skipping it.");
                }
            }
        }
        return finalList;
    }

    private static void deleteSymLink(Path path) throws IOException {
        boolean deleted = path.toFile().delete();
        if (!deleted) {
            throw new IOException("Could not delete symlink at " + path);
        }
    }
}
