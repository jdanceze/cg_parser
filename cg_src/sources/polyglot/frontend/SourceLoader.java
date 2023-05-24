package polyglot.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/SourceLoader.class */
public class SourceLoader {
    protected ExtensionInfo sourceExt;
    protected Collection sourcePath;
    static File current_dir = null;
    protected Map directoryContentsCache = new HashMap();
    protected int caseInsensitive = 0;
    protected Set loadedSources = new HashSet();

    public SourceLoader(ExtensionInfo sourceExt, Collection sourcePath) {
        this.sourcePath = sourcePath;
        this.sourceExt = sourceExt;
    }

    public FileSource fileSource(String fileName) throws IOException {
        setCaseInsensitive(fileName);
        File sourceFile = new File(fileName);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(fileName);
        }
        if (this.loadedSources.contains(fileKey(sourceFile))) {
            throw new FileNotFoundException(fileName);
        }
        this.loadedSources.add(fileKey(sourceFile));
        String[] exts = this.sourceExt.fileExtensions();
        boolean ok = false;
        int i = 0;
        while (true) {
            if (i >= exts.length) {
                break;
            }
            String ext = exts[i];
            if (!fileName.endsWith(new StringBuffer().append(".").append(ext).toString())) {
                i++;
            } else {
                ok = true;
                break;
            }
        }
        if (!ok) {
            String extString = "";
            for (int i2 = 0; i2 < exts.length; i2++) {
                if (exts.length == 2 && i2 == exts.length - 1) {
                    extString = new StringBuffer().append(extString).append(" or ").toString();
                } else if (exts.length != 1 && i2 == exts.length - 1) {
                    extString = new StringBuffer().append(extString).append(", or ").toString();
                } else if (i2 != 0) {
                    extString = new StringBuffer().append(extString).append(", ").toString();
                }
                extString = new StringBuffer().append(extString).append("\".").append(exts[i2]).append("\"").toString();
            }
            if (exts.length == 1) {
                throw new IOException(new StringBuffer().append("Source \"").append(fileName).append("\" does not have the extension ").append(extString).append(".").toString());
            }
            throw new IOException(new StringBuffer().append("Source \"").append(fileName).append("\" does not have any of the extensions ").append(extString).append(".").toString());
        }
        if (Report.should_report(Report.frontend, 2)) {
            Report.report(2, new StringBuffer().append("Loading class from ").append(sourceFile).toString());
        }
        return new FileSource(sourceFile);
    }

    protected static File current_dir() {
        if (current_dir == null) {
            current_dir = new File(System.getProperty("user.dir"));
        }
        return current_dir;
    }

    public boolean packageExists(String name) {
        String fileName = name.replace('.', File.separatorChar);
        for (File directory : this.sourcePath) {
            File f = new File(directory, fileName);
            if (f.exists() && f.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    public FileSource classSource(String className) {
        File sourceFile;
        String[] exts = this.sourceExt.fileExtensions();
        for (String str : exts) {
            String fileName = new StringBuffer().append(className.replace('.', File.separatorChar)).append(".").append(str).toString();
            for (File directory : this.sourcePath) {
                HashSet dirContents = (Set) this.directoryContentsCache.get(directory);
                if (dirContents == null) {
                    dirContents = new HashSet();
                    this.directoryContentsCache.put(directory, dirContents);
                    if (directory.exists()) {
                        String[] contents = directory.list();
                        for (String str2 : contents) {
                            dirContents.add(str2);
                        }
                    }
                }
                int index = fileName.indexOf(File.separatorChar);
                if (index < 0) {
                    index = fileName.length();
                }
                String firstPart = fileName.substring(0, index);
                if (dirContents.contains(firstPart)) {
                    if (directory != null && directory.equals(current_dir())) {
                        sourceFile = new File(fileName);
                    } else {
                        sourceFile = new File(directory, fileName);
                    }
                    if (this.loadedSources.contains(fileKey(sourceFile))) {
                        continue;
                    } else {
                        try {
                            if (Report.should_report(Report.frontend, 2)) {
                                Report.report(2, new StringBuffer().append("Loading ").append(className).append(" from ").append(sourceFile).toString());
                            }
                            FileSource s = new FileSource(sourceFile);
                            this.loadedSources.add(fileKey(sourceFile));
                            return s;
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return null;
    }

    public Object fileKey(File file) {
        setCaseInsensitive(file.getAbsolutePath());
        if (caseInsensitive()) {
            return file.getAbsolutePath().toLowerCase();
        }
        return file.getAbsolutePath();
    }

    public boolean caseInsensitive() {
        if (this.caseInsensitive == 0) {
            throw new InternalCompilerError("unknown case sensitivity");
        }
        return this.caseInsensitive == 1;
    }

    protected void setCaseInsensitive(String fileName) {
        File dir;
        if (this.caseInsensitive != 0) {
            return;
        }
        File f1 = new File(fileName.toUpperCase());
        File f2 = new File(fileName.toLowerCase());
        if (f1.equals(f2)) {
            this.caseInsensitive = 1;
        } else if (f1.exists() && f2.exists()) {
            boolean f1Exists = false;
            boolean f2Exists = false;
            if (f1.getParent() != null) {
                dir = new File(f1.getParent());
            } else {
                dir = current_dir();
            }
            File[] ls = dir.listFiles();
            for (int i = 0; i < ls.length; i++) {
                if (f1.equals(ls[i])) {
                    f1Exists = true;
                }
                if (f2.equals(ls[i])) {
                    f2Exists = true;
                }
            }
            if (!f1Exists || !f2Exists) {
                this.caseInsensitive = 1;
            } else {
                this.caseInsensitive = -1;
            }
        } else {
            this.caseInsensitive = -1;
        }
    }

    protected String canonicalize(String fileName) {
        return fileName;
    }
}
