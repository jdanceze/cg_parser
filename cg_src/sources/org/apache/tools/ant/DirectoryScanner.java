package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.SelectorScanner;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.types.selectors.TokenizedPath;
import org.apache.tools.ant.types.selectors.TokenizedPattern;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.VectorSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/DirectoryScanner.class */
public class DirectoryScanner implements FileScanner, SelectorScanner, ResourceFactory {
    public static final int MAX_LEVELS_OF_SYMLINKS = 5;
    public static final String DOES_NOT_EXIST_POSTFIX = " does not exist.";
    protected File basedir;
    protected String[] includes;
    protected String[] excludes;
    protected Vector<String> filesIncluded;
    protected Vector<String> filesNotIncluded;
    protected Vector<String> filesExcluded;
    protected Vector<String> dirsIncluded;
    protected Vector<String> dirsNotIncluded;
    protected Vector<String> dirsExcluded;
    protected Vector<String> filesDeselected;
    protected Vector<String> dirsDeselected;
    private TokenizedPattern[] includePatterns;
    private TokenizedPattern[] excludePatterns;
    private static final boolean ON_VMS = Os.isFamily(Os.FAMILY_VMS);
    @Deprecated
    protected static final String[] DEFAULTEXCLUDES = {"**/*~", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.git", "**/.git/**", "**/.gitattributes", "**/.gitignore", "**/.gitmodules", "**/.hg", "**/.hg/**", "**/.hgignore", "**/.hgsub", "**/.hgsubstate", "**/.hgtags", "**/.bzr", "**/.bzr/**", "**/.bzrignore", "**/.DS_Store"};
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final Set<String> defaultExcludes = new HashSet();
    protected FileSelector[] selectors = null;
    protected boolean haveSlowResults = false;
    protected boolean isCaseSensitive = true;
    protected boolean errorOnMissingDir = true;
    private boolean followSymlinks = true;
    protected boolean everythingIncluded = true;
    private final Set<String> scannedDirs = new HashSet();
    private final Map<String, TokenizedPath> includeNonPatterns = new HashMap();
    private final Map<String, TokenizedPath> excludeNonPatterns = new HashMap();
    private boolean areNonPatternSetsReady = false;
    private boolean scanning = false;
    private final Object scanLock = new Object();
    private boolean slowScanning = false;
    private final Object slowScanLock = new Object();
    private IllegalStateException illegal = null;
    private int maxLevelsOfSymlinks = 5;
    private final Set<String> notFollowedSymlinks = new HashSet();

    static {
        resetDefaultExcludes();
    }

    protected static boolean matchPatternStart(String pattern, String str) {
        return SelectorUtils.matchPatternStart(pattern, str);
    }

    protected static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
        return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive);
    }

    protected static boolean matchPath(String pattern, String str) {
        return SelectorUtils.matchPath(pattern, str);
    }

    protected static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
        return SelectorUtils.matchPath(pattern, str, isCaseSensitive);
    }

    public static boolean match(String pattern, String str) {
        return SelectorUtils.match(pattern, str);
    }

    protected static boolean match(String pattern, String str, boolean isCaseSensitive) {
        return SelectorUtils.match(pattern, str, isCaseSensitive);
    }

    public static String[] getDefaultExcludes() {
        String[] strArr;
        synchronized (defaultExcludes) {
            strArr = (String[]) defaultExcludes.toArray(new String[defaultExcludes.size()]);
        }
        return strArr;
    }

    public static boolean addDefaultExclude(String s) {
        boolean add;
        synchronized (defaultExcludes) {
            add = defaultExcludes.add(s);
        }
        return add;
    }

    public static boolean removeDefaultExclude(String s) {
        boolean remove;
        synchronized (defaultExcludes) {
            remove = defaultExcludes.remove(s);
        }
        return remove;
    }

    public static void resetDefaultExcludes() {
        synchronized (defaultExcludes) {
            defaultExcludes.clear();
            Collections.addAll(defaultExcludes, DEFAULTEXCLUDES);
        }
    }

    @Override // org.apache.tools.ant.FileScanner
    public void setBasedir(String basedir) {
        setBasedir(basedir == null ? null : new File(basedir.replace('/', File.separatorChar).replace('\\', File.separatorChar)));
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized File getBasedir() {
        return this.basedir;
    }

    public synchronized boolean isCaseSensitive() {
        return this.isCaseSensitive;
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    public void setErrorOnMissingDir(boolean errorOnMissingDir) {
        this.errorOnMissingDir = errorOnMissingDir;
    }

    public synchronized boolean isFollowSymlinks() {
        return this.followSymlinks;
    }

    public synchronized void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    public void setMaxLevelsOfSymlinks(int max) {
        this.maxLevelsOfSymlinks = max;
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized void setIncludes(String[] includes) {
        if (includes == null) {
            this.includes = null;
        } else {
            this.includes = (String[]) Stream.of((Object[]) includes).map(DirectoryScanner::normalizePattern).toArray(x$0 -> {
                return new String[x$0];
            });
        }
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized void setExcludes(String[] excludes) {
        if (excludes == null) {
            this.excludes = null;
        } else {
            this.excludes = (String[]) Stream.of((Object[]) excludes).map(DirectoryScanner::normalizePattern).toArray(x$0 -> {
                return new String[x$0];
            });
        }
    }

    public synchronized void addExcludes(String[] excludes) {
        if (excludes != null && excludes.length > 0) {
            if (this.excludes == null || this.excludes.length == 0) {
                setExcludes(excludes);
            } else {
                this.excludes = (String[]) Stream.concat(Stream.of((Object[]) this.excludes), Stream.of((Object[]) excludes).map(DirectoryScanner::normalizePattern)).toArray(x$0 -> {
                    return new String[x$0];
                });
            }
        }
    }

    private static String normalizePattern(String p) {
        String pattern = p.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        if (pattern.endsWith(File.separator)) {
            pattern = pattern + SelectorUtils.DEEP_TREE_MATCH;
        }
        return pattern;
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorScanner
    public synchronized void setSelectors(FileSelector[] selectors) {
        this.selectors = selectors;
    }

    public synchronized boolean isEverythingIncluded() {
        return this.everythingIncluded;
    }

    /*  JADX ERROR: NullPointerException in pass: AttachTryCatchVisitor
        java.lang.NullPointerException: Cannot invoke "String.charAt(int)" because "obj" is null
        	at jadx.core.utils.Utils.cleanObjectName(Utils.java:35)
        	at jadx.core.dex.instructions.args.ArgType.object(ArgType.java:84)
        	at jadx.core.dex.info.ClassInfo.fromName(ClassInfo.java:41)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.convertToHandlers(AttachTryCatchVisitor.java:118)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.initTryCatches(AttachTryCatchVisitor.java:59)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.visit(AttachTryCatchVisitor.java:47)
        */
    @Override // org.apache.tools.ant.FileScanner
    public void scan() throws java.lang.IllegalStateException {
        /*
            Method dump skipped, instructions count: 636
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.DirectoryScanner.scan():void");
    }

    private void checkIncludePatterns() {
        TokenizedPattern[] tokenizedPatternArr;
        String removeLeadingPath;
        File f;
        String removeLeadingPath2;
        boolean equalsIgnoreCase;
        ensureNonPatternSetsReady();
        Map<TokenizedPath, String> newroots = new HashMap<>();
        for (TokenizedPattern includePattern : this.includePatterns) {
            String pattern = includePattern.toString();
            if (!shouldSkipPattern(pattern)) {
                newroots.put(includePattern.rtrimWildcardTokens(), pattern);
            }
        }
        for (Map.Entry<String, TokenizedPath> entry : this.includeNonPatterns.entrySet()) {
            String pattern2 = entry.getKey();
            if (!shouldSkipPattern(pattern2)) {
                newroots.put(entry.getValue(), pattern2);
            }
        }
        if (newroots.containsKey(TokenizedPath.EMPTY_PATH) && this.basedir != null) {
            scandir(this.basedir, "", true);
            return;
        }
        File canonBase = null;
        if (this.basedir != null) {
            try {
                canonBase = this.basedir.getCanonicalFile();
            } catch (IOException ex) {
                throw new BuildException(ex);
            }
        }
        for (Map.Entry<TokenizedPath, String> entry2 : newroots.entrySet()) {
            TokenizedPath currentPath = entry2.getKey();
            String currentelement = currentPath.toString();
            if (this.basedir != null || FileUtils.isAbsolutePath(currentelement)) {
                File myfile = new File(this.basedir, currentelement);
                if (myfile.exists()) {
                    try {
                        if (this.basedir == null) {
                            removeLeadingPath = myfile.getCanonicalPath();
                        } else {
                            removeLeadingPath = FILE_UTILS.removeLeadingPath(canonBase, myfile.getCanonicalFile());
                        }
                        String path = removeLeadingPath;
                        if (!path.equals(currentelement) || ON_VMS) {
                            myfile = currentPath.findFile(this.basedir, true);
                            if (myfile != null && this.basedir != null) {
                                currentelement = FILE_UTILS.removeLeadingPath(this.basedir, myfile);
                                if (!currentPath.toString().equals(currentelement)) {
                                    currentPath = new TokenizedPath(currentelement);
                                }
                            }
                        }
                    } catch (IOException ex2) {
                        throw new BuildException(ex2);
                    }
                }
                if ((myfile == null || !myfile.exists()) && !isCaseSensitive() && (f = currentPath.findFile(this.basedir, false)) != null && f.exists()) {
                    if (this.basedir == null) {
                        removeLeadingPath2 = f.getAbsolutePath();
                    } else {
                        removeLeadingPath2 = FILE_UTILS.removeLeadingPath(this.basedir, f);
                    }
                    currentelement = removeLeadingPath2;
                    myfile = f;
                    currentPath = new TokenizedPath(currentelement);
                }
                if (myfile != null && myfile.exists()) {
                    if (!this.followSymlinks && currentPath.isSymlink(this.basedir)) {
                        accountForNotFollowedSymlink(currentPath, myfile);
                    } else if (myfile.isDirectory()) {
                        if (isIncluded(currentPath) && !currentelement.isEmpty()) {
                            accountForIncludedDir(currentPath, myfile, true);
                        } else {
                            scandir(myfile, currentPath, true);
                        }
                    } else if (myfile.isFile()) {
                        String originalpattern = entry2.getValue();
                        if (isCaseSensitive()) {
                            equalsIgnoreCase = originalpattern.equals(currentelement);
                        } else {
                            equalsIgnoreCase = originalpattern.equalsIgnoreCase(currentelement);
                        }
                        boolean included = equalsIgnoreCase;
                        if (included) {
                            accountForIncludedFile(currentPath, myfile);
                        }
                    }
                }
            }
        }
    }

    private boolean shouldSkipPattern(String pattern) {
        return FileUtils.isAbsolutePath(pattern) ? (this.basedir == null || SelectorUtils.matchPatternStart(pattern, this.basedir.getAbsolutePath(), isCaseSensitive())) ? false : true : this.basedir == null;
    }

    protected synchronized void clearResults() {
        this.filesIncluded = new VectorSet();
        this.filesNotIncluded = new VectorSet();
        this.filesExcluded = new VectorSet();
        this.filesDeselected = new VectorSet();
        this.dirsIncluded = new VectorSet();
        this.dirsNotIncluded = new VectorSet();
        this.dirsExcluded = new VectorSet();
        this.dirsDeselected = new VectorSet();
        this.everythingIncluded = this.basedir != null;
        this.scannedDirs.clear();
        this.notFollowedSymlinks.clear();
    }

    protected void slowScan() {
        synchronized (this.slowScanLock) {
            if (this.haveSlowResults) {
                return;
            }
            if (this.slowScanning) {
                while (this.slowScanning) {
                    try {
                        this.slowScanLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                return;
            }
            this.slowScanning = true;
            try {
                synchronized (this) {
                    boolean nullIncludes = this.includes == null;
                    this.includes = nullIncludes ? new String[]{SelectorUtils.DEEP_TREE_MATCH} : this.includes;
                    boolean nullExcludes = this.excludes == null;
                    this.excludes = nullExcludes ? new String[0] : this.excludes;
                    String[] excl = new String[this.dirsExcluded.size()];
                    this.dirsExcluded.copyInto(excl);
                    String[] notIncl = new String[this.dirsNotIncluded.size()];
                    this.dirsNotIncluded.copyInto(notIncl);
                    ensureNonPatternSetsReady();
                    processSlowScan(excl);
                    processSlowScan(notIncl);
                    clearCaches();
                    this.includes = nullIncludes ? null : this.includes;
                    this.excludes = nullExcludes ? null : this.excludes;
                }
                synchronized (this.slowScanLock) {
                    this.haveSlowResults = true;
                    this.slowScanning = false;
                    this.slowScanLock.notifyAll();
                }
            } catch (Throwable th) {
                synchronized (this.slowScanLock) {
                    this.haveSlowResults = true;
                    this.slowScanning = false;
                    this.slowScanLock.notifyAll();
                    throw th;
                }
            }
        }
    }

    private void processSlowScan(String[] arr) {
        for (String element : arr) {
            TokenizedPath path = new TokenizedPath(element);
            if (!couldHoldIncluded(path) || contentsExcluded(path)) {
                scandir(new File(this.basedir, element), path, false);
            }
        }
    }

    protected void scandir(File dir, String vpath, boolean fast) {
        scandir(dir, new TokenizedPath(vpath), fast);
    }

    private void scandir(File dir, TokenizedPath path, boolean fast) {
        if (dir == null) {
            throw new BuildException("dir must not be null.");
        }
        String[] newfiles = dir.list();
        if (newfiles == null) {
            if (!dir.exists()) {
                throw new BuildException(dir + DOES_NOT_EXIST_POSTFIX);
            }
            if (!dir.isDirectory()) {
                throw new BuildException("%s is not a directory.", dir);
            }
            throw new BuildException("IO error scanning directory '%s'", dir.getAbsolutePath());
        }
        scandir(dir, path, fast, newfiles, new LinkedList());
    }

    private void scandir(File dir, TokenizedPath path, boolean fast, String[] newFiles, Deque<String> directoryNamesFollowed) {
        String[] strArr;
        Path filePath;
        String vpath = path.toString();
        if (!vpath.isEmpty() && !vpath.endsWith(File.separator)) {
            vpath = vpath + File.separator;
        }
        if (fast && hasBeenScanned(vpath)) {
            return;
        }
        if (!this.followSymlinks) {
            ArrayList<String> noLinks = new ArrayList<>();
            for (String newFile : newFiles) {
                if (dir == null) {
                    filePath = Paths.get(newFile, new String[0]);
                } else {
                    filePath = Paths.get(dir.toPath().toString(), newFile);
                }
                if (Files.isSymbolicLink(filePath)) {
                    String name = vpath + newFile;
                    File file = new File(dir, newFile);
                    if (file.isDirectory()) {
                        this.dirsExcluded.addElement(name);
                    } else if (file.isFile()) {
                        this.filesExcluded.addElement(name);
                    }
                    accountForNotFollowedSymlink(name, file);
                } else {
                    noLinks.add(newFile);
                }
            }
            newFiles = (String[]) noLinks.toArray(new String[noLinks.size()]);
        } else {
            directoryNamesFollowed.addFirst(dir.getName());
        }
        for (String newFile2 : newFiles) {
            String name2 = vpath + newFile2;
            TokenizedPath newPath = new TokenizedPath(path, newFile2);
            File file2 = new File(dir, newFile2);
            String[] children = file2.list();
            if (children == null || (children.length == 0 && file2.isFile())) {
                if (isIncluded(newPath)) {
                    accountForIncludedFile(newPath, file2);
                } else {
                    this.everythingIncluded = false;
                    this.filesNotIncluded.addElement(name2);
                }
            } else if (file2.isDirectory()) {
                if (this.followSymlinks && causesIllegalSymlinkLoop(newFile2, dir, directoryNamesFollowed)) {
                    System.err.println("skipping symbolic link " + file2.getAbsolutePath() + " -- too many levels of symbolic links.");
                    this.notFollowedSymlinks.add(file2.getAbsolutePath());
                } else {
                    if (isIncluded(newPath)) {
                        accountForIncludedDir(newPath, file2, fast, children, directoryNamesFollowed);
                    } else {
                        this.everythingIncluded = false;
                        this.dirsNotIncluded.addElement(name2);
                        if (fast && couldHoldIncluded(newPath) && !contentsExcluded(newPath)) {
                            scandir(file2, newPath, fast, children, directoryNamesFollowed);
                        }
                    }
                    if (!fast) {
                        scandir(file2, newPath, fast, children, directoryNamesFollowed);
                    }
                }
            }
        }
        if (this.followSymlinks) {
            directoryNamesFollowed.removeFirst();
        }
    }

    private void accountForIncludedFile(TokenizedPath name, File file) {
        processIncluded(name, file, this.filesIncluded, this.filesExcluded, this.filesDeselected);
    }

    private void accountForIncludedDir(TokenizedPath name, File file, boolean fast) {
        processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
        if (fast && couldHoldIncluded(name) && !contentsExcluded(name)) {
            scandir(file, name, fast);
        }
    }

    private void accountForIncludedDir(TokenizedPath name, File file, boolean fast, String[] children, Deque<String> directoryNamesFollowed) {
        processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
        if (fast && couldHoldIncluded(name) && !contentsExcluded(name)) {
            scandir(file, name, fast, children, directoryNamesFollowed);
        }
    }

    private void accountForNotFollowedSymlink(String name, File file) {
        accountForNotFollowedSymlink(new TokenizedPath(name), file);
    }

    private void accountForNotFollowedSymlink(TokenizedPath name, File file) {
        if (!isExcluded(name)) {
            if (isIncluded(name) || (file.isDirectory() && couldHoldIncluded(name) && !contentsExcluded(name))) {
                this.notFollowedSymlinks.add(file.getAbsolutePath());
            }
        }
    }

    private void processIncluded(TokenizedPath path, File file, List<String> inc, List<String> exc, List<String> des) {
        String name = path.toString();
        if (inc.contains(name) || exc.contains(name) || des.contains(name)) {
            return;
        }
        boolean included = false;
        if (isExcluded(path)) {
            exc.add(name);
        } else if (isSelected(name, file)) {
            included = true;
            inc.add(name);
        } else {
            des.add(name);
        }
        this.everythingIncluded &= included;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isIncluded(String name) {
        return isIncluded(new TokenizedPath(name));
    }

    private boolean isIncluded(TokenizedPath path) {
        ensureNonPatternSetsReady();
        String toMatch = path.toString();
        if (!isCaseSensitive()) {
            toMatch = toMatch.toUpperCase();
        }
        return this.includeNonPatterns.containsKey(toMatch) || Stream.of((Object[]) this.includePatterns).anyMatch(p -> {
            return path.matchPath(path, isCaseSensitive());
        });
    }

    protected boolean couldHoldIncluded(String name) {
        return couldHoldIncluded(new TokenizedPath(name));
    }

    private boolean couldHoldIncluded(TokenizedPath tokenizedName) {
        return Stream.concat(Stream.of((Object[]) this.includePatterns), this.includeNonPatterns.values().stream().map((v0) -> {
            return v0.toPattern();
        })).anyMatch(pat -> {
            return couldHoldIncluded(tokenizedName, tokenizedName);
        });
    }

    private boolean couldHoldIncluded(TokenizedPath tokenizedName, TokenizedPattern tokenizedInclude) {
        return tokenizedInclude.matchStartOf(tokenizedName, isCaseSensitive()) && isMorePowerfulThanExcludes(tokenizedName.toString()) && isDeeper(tokenizedInclude, tokenizedName);
    }

    private boolean isDeeper(TokenizedPattern pattern, TokenizedPath name) {
        return pattern.containsPattern(SelectorUtils.DEEP_TREE_MATCH) || pattern.depth() > name.depth();
    }

    private boolean isMorePowerfulThanExcludes(String name) {
        String soughtexclude = name + File.separatorChar + SelectorUtils.DEEP_TREE_MATCH;
        return Stream.of((Object[]) this.excludePatterns).map((v0) -> {
            return v0.toString();
        }).noneMatch(Predicate.isEqual(soughtexclude));
    }

    boolean contentsExcluded(TokenizedPath path) {
        return Stream.of((Object[]) this.excludePatterns).filter(p -> {
            return p.endsWith(SelectorUtils.DEEP_TREE_MATCH);
        }).map((v0) -> {
            return v0.withoutLastToken();
        }).anyMatch(wlt -> {
            return path.matchPath(path, isCaseSensitive());
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isExcluded(String name) {
        return isExcluded(new TokenizedPath(name));
    }

    private boolean isExcluded(TokenizedPath name) {
        ensureNonPatternSetsReady();
        String toMatch = name.toString();
        if (!isCaseSensitive()) {
            toMatch = toMatch.toUpperCase();
        }
        return this.excludeNonPatterns.containsKey(toMatch) || Stream.of((Object[]) this.excludePatterns).anyMatch(p -> {
            return name.matchPath(name, isCaseSensitive());
        });
    }

    protected boolean isSelected(String name, File file) {
        return this.selectors == null || Stream.of((Object[]) this.selectors).allMatch(sel -> {
            return file.isSelected(this.basedir, name, name);
        });
    }

    @Override // org.apache.tools.ant.FileScanner
    public String[] getIncludedFiles() {
        String[] files;
        synchronized (this) {
            if (this.filesIncluded == null) {
                throw new IllegalStateException("Must call scan() first");
            }
            files = (String[]) this.filesIncluded.toArray(new String[this.filesIncluded.size()]);
        }
        Arrays.sort(files);
        return files;
    }

    public synchronized int getIncludedFilesCount() {
        if (this.filesIncluded == null) {
            throw new IllegalStateException("Must call scan() first");
        }
        return this.filesIncluded.size();
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized String[] getNotIncludedFiles() {
        slowScan();
        return (String[]) this.filesNotIncluded.toArray(new String[this.filesNotIncluded.size()]);
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized String[] getExcludedFiles() {
        slowScan();
        return (String[]) this.filesExcluded.toArray(new String[this.filesExcluded.size()]);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorScanner
    public synchronized String[] getDeselectedFiles() {
        slowScan();
        return (String[]) this.filesDeselected.toArray(new String[this.filesDeselected.size()]);
    }

    @Override // org.apache.tools.ant.FileScanner
    public String[] getIncludedDirectories() {
        String[] directories;
        synchronized (this) {
            if (this.dirsIncluded == null) {
                throw new IllegalStateException("Must call scan() first");
            }
            directories = (String[]) this.dirsIncluded.toArray(new String[this.dirsIncluded.size()]);
        }
        Arrays.sort(directories);
        return directories;
    }

    public synchronized int getIncludedDirsCount() {
        if (this.dirsIncluded == null) {
            throw new IllegalStateException("Must call scan() first");
        }
        return this.dirsIncluded.size();
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized String[] getNotIncludedDirectories() {
        slowScan();
        return (String[]) this.dirsNotIncluded.toArray(new String[this.dirsNotIncluded.size()]);
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized String[] getExcludedDirectories() {
        slowScan();
        return (String[]) this.dirsExcluded.toArray(new String[this.dirsExcluded.size()]);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorScanner
    public synchronized String[] getDeselectedDirectories() {
        slowScan();
        return (String[]) this.dirsDeselected.toArray(new String[this.dirsDeselected.size()]);
    }

    public synchronized String[] getNotFollowedSymlinks() {
        String[] links;
        links = (String[]) this.notFollowedSymlinks.toArray(new String[this.notFollowedSymlinks.size()]);
        Arrays.sort(links);
        return links;
    }

    @Override // org.apache.tools.ant.FileScanner
    public synchronized void addDefaultExcludes() {
        Stream<String> s = Stream.of((Object[]) getDefaultExcludes()).map(p -> {
            return p.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        });
        if (this.excludes != null) {
            s = Stream.concat(Stream.of((Object[]) this.excludes), s);
        }
        this.excludes = (String[]) s.toArray(x$0 -> {
            return new String[x$0];
        });
    }

    @Override // org.apache.tools.ant.types.ResourceFactory
    public synchronized Resource getResource(String name) {
        return new FileResource(this.basedir, name);
    }

    private boolean hasBeenScanned(String vpath) {
        return !this.scannedDirs.add(vpath);
    }

    Set<String> getScannedDirs() {
        return this.scannedDirs;
    }

    private synchronized void clearCaches() {
        this.includeNonPatterns.clear();
        this.excludeNonPatterns.clear();
        this.includePatterns = null;
        this.excludePatterns = null;
        this.areNonPatternSetsReady = false;
    }

    synchronized void ensureNonPatternSetsReady() {
        if (!this.areNonPatternSetsReady) {
            this.includePatterns = fillNonPatternSet(this.includeNonPatterns, this.includes);
            this.excludePatterns = fillNonPatternSet(this.excludeNonPatterns, this.excludes);
            this.areNonPatternSetsReady = true;
        }
    }

    private TokenizedPattern[] fillNonPatternSet(Map<String, TokenizedPath> map, String[] patterns) {
        List<TokenizedPattern> al = new ArrayList<>(patterns.length);
        for (String pattern : patterns) {
            if (SelectorUtils.hasWildcards(pattern)) {
                al.add(new TokenizedPattern(pattern));
            } else {
                String s = isCaseSensitive() ? pattern : pattern.toUpperCase();
                map.put(s, new TokenizedPath(s));
            }
        }
        return (TokenizedPattern[]) al.toArray(new TokenizedPattern[al.size()]);
    }

    private boolean causesIllegalSymlinkLoop(String dirName, File parent, Deque<String> directoryNamesFollowed) {
        Path dirPath;
        try {
            if (parent == null) {
                dirPath = Paths.get(dirName, new String[0]);
            } else {
                dirPath = Paths.get(parent.toPath().toString(), dirName);
            }
            if (directoryNamesFollowed.size() >= this.maxLevelsOfSymlinks && Collections.frequency(directoryNamesFollowed, dirName) >= this.maxLevelsOfSymlinks && Files.isSymbolicLink(dirPath)) {
                List<String> files = new ArrayList<>();
                File f = FILE_UTILS.resolveFile(parent, dirName);
                String target = f.getCanonicalPath();
                files.add(target);
                StringBuilder relPath = new StringBuilder();
                for (String dir : directoryNamesFollowed) {
                    relPath.append("../");
                    if (dirName.equals(dir)) {
                        File f2 = FILE_UTILS.resolveFile(parent, ((Object) relPath) + dir);
                        files.add(f2.getCanonicalPath());
                        if (files.size() > this.maxLevelsOfSymlinks && Collections.frequency(files, target) > this.maxLevelsOfSymlinks) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;
        } catch (IOException ex) {
            throw new BuildException("Caught error while checking for symbolic links", ex);
        }
    }
}
