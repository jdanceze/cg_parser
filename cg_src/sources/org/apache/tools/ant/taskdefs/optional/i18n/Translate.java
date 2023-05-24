package org.apache.tools.ant.taskdefs.optional.i18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.LineTokenizer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/i18n/Translate.class */
public class Translate extends MatchingTask {
    private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY_VARIANT = 0;
    private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY = 1;
    private static final int BUNDLE_SPECIFIED_LANGUAGE = 2;
    private static final int BUNDLE_NOMATCH = 3;
    private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY_VARIANT = 4;
    private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY = 5;
    private static final int BUNDLE_DEFAULT_LANGUAGE = 6;
    private static final int BUNDLE_MAX_ALTERNATIVES = 7;
    private String bundle;
    private String bundleLanguage;
    private String bundleCountry;
    private String bundleVariant;
    private File toDir;
    private String srcEncoding;
    private String destEncoding;
    private String bundleEncoding;
    private String startToken;
    private String endToken;
    private boolean forceOverwrite;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private long srcLastModified;
    private long destLastModified;
    private List<FileSet> filesets = new Vector();
    private Map<String, String> resourceMap = new Hashtable();
    private long[] bundleLastModified = new long[7];
    private boolean loaded = false;

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public void setBundleLanguage(String bundleLanguage) {
        this.bundleLanguage = bundleLanguage;
    }

    public void setBundleCountry(String bundleCountry) {
        this.bundleCountry = bundleCountry;
    }

    public void setBundleVariant(String bundleVariant) {
        this.bundleVariant = bundleVariant;
    }

    public void setToDir(File toDir) {
        this.toDir = toDir;
    }

    public void setStartToken(String startToken) {
        this.startToken = startToken;
    }

    public void setEndToken(String endToken) {
        this.endToken = endToken;
    }

    public void setSrcEncoding(String srcEncoding) {
        this.srcEncoding = srcEncoding;
    }

    public void setDestEncoding(String destEncoding) {
        this.destEncoding = destEncoding;
    }

    public void setBundleEncoding(String bundleEncoding) {
        this.bundleEncoding = bundleEncoding;
    }

    public void setForceOverwrite(boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }

    public void addFileset(FileSet set) {
        this.filesets.add(set);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.bundle == null) {
            throw new BuildException("The bundle attribute must be set.", getLocation());
        }
        if (this.startToken == null) {
            throw new BuildException("The starttoken attribute must be set.", getLocation());
        }
        if (this.endToken == null) {
            throw new BuildException("The endtoken attribute must be set.", getLocation());
        }
        if (this.bundleLanguage == null) {
            Locale l = Locale.getDefault();
            this.bundleLanguage = l.getLanguage();
        }
        if (this.bundleCountry == null) {
            this.bundleCountry = Locale.getDefault().getCountry();
        }
        if (this.bundleVariant == null) {
            Locale l2 = new Locale(this.bundleLanguage, this.bundleCountry);
            this.bundleVariant = l2.getVariant();
        }
        if (this.toDir == null) {
            throw new BuildException("The todir attribute must be set.", getLocation());
        }
        if (!this.toDir.exists()) {
            this.toDir.mkdirs();
        } else if (this.toDir.isFile()) {
            throw new BuildException("%s is not a directory", this.toDir);
        }
        if (this.srcEncoding == null) {
            this.srcEncoding = System.getProperty("file.encoding");
        }
        if (this.destEncoding == null) {
            this.destEncoding = this.srcEncoding;
        }
        if (this.bundleEncoding == null) {
            this.bundleEncoding = this.srcEncoding;
        }
        loadResourceMaps();
        translate();
    }

    private void loadResourceMaps() throws BuildException {
        Locale locale = new Locale(this.bundleLanguage, this.bundleCountry, this.bundleVariant);
        String language = locale.getLanguage().isEmpty() ? "" : "_" + locale.getLanguage();
        String country = locale.getCountry().isEmpty() ? "" : "_" + locale.getCountry();
        String variant = locale.getVariant().isEmpty() ? "" : "_" + locale.getVariant();
        processBundle(this.bundle + language + country + variant, 0, false);
        processBundle(this.bundle + language + country, 1, false);
        processBundle(this.bundle + language, 2, false);
        processBundle(this.bundle, 3, false);
        Locale locale2 = Locale.getDefault();
        String language2 = locale2.getLanguage().isEmpty() ? "" : "_" + locale2.getLanguage();
        String country2 = locale2.getCountry().isEmpty() ? "" : "_" + locale2.getCountry();
        String variant2 = locale2.getVariant().isEmpty() ? "" : "_" + locale2.getVariant();
        this.bundleEncoding = System.getProperty("file.encoding");
        processBundle(this.bundle + language2 + country2 + variant2, 4, false);
        processBundle(this.bundle + language2 + country2, 5, false);
        processBundle(this.bundle + language2, 6, true);
    }

    private void processBundle(String bundleFile, int i, boolean checkLoaded) throws BuildException {
        File propsFile = getProject().resolveFile(bundleFile + ".properties");
        try {
            InputStream ins = Files.newInputStream(propsFile.toPath(), new OpenOption[0]);
            this.loaded = true;
            this.bundleLastModified[i] = propsFile.lastModified();
            log("Using " + propsFile, 4);
            loadResourceMap(ins);
        } catch (IOException ioe) {
            log(propsFile + " not found.", 4);
            if (!this.loaded && checkLoaded) {
                throw new BuildException(ioe.getMessage(), getLocation());
            }
        }
    }

    private void loadResourceMap(InputStream ins) throws BuildException {
        String line;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(ins, this.bundleEncoding));
            while (true) {
                String line2 = in.readLine();
                if (line2 != null) {
                    if (line2.trim().length() > 1 && '#' != line2.charAt(0) && '!' != line2.charAt(0)) {
                        int sepIndex = line2.indexOf(61);
                        if (-1 == sepIndex) {
                            sepIndex = line2.indexOf(58);
                        }
                        if (-1 == sepIndex) {
                            int k = 0;
                            while (true) {
                                if (k >= line2.length()) {
                                    break;
                                } else if (!Character.isSpaceChar(line2.charAt(k))) {
                                    k++;
                                } else {
                                    sepIndex = k;
                                    break;
                                }
                            }
                        }
                        if (-1 != sepIndex) {
                            String key = line2.substring(0, sepIndex).trim();
                            String value = line2.substring(sepIndex + 1).trim();
                            while (value.endsWith("\\")) {
                                value = value.substring(0, value.length() - 1);
                                if (in.readLine() == null) {
                                    break;
                                }
                                value = value + line.trim();
                            }
                            if (!key.isEmpty()) {
                                this.resourceMap.putIfAbsent(key, value);
                            }
                        }
                    }
                } else {
                    in.close();
                    return;
                }
            }
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage(), getLocation());
        }
    }

    private void translate() throws BuildException {
        String[] includedFiles;
        int filesProcessed = 0;
        for (FileSet fs : this.filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            for (String srcFile : ds.getIncludedFiles()) {
                try {
                    File dest = FILE_UTILS.resolveFile(this.toDir, srcFile);
                    try {
                        File destDir = new File(dest.getParent());
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                    } catch (Exception e) {
                        log("Exception occurred while trying to check/create  parent directory.  " + e.getMessage(), 4);
                    }
                    this.destLastModified = dest.lastModified();
                    File src = FILE_UTILS.resolveFile(ds.getBasedir(), srcFile);
                    this.srcLastModified = src.lastModified();
                    boolean needsWork = this.forceOverwrite || this.destLastModified < this.srcLastModified;
                    if (!needsWork) {
                        for (int icounter = 0; icounter < 7; icounter++) {
                            needsWork = this.destLastModified < this.bundleLastModified[icounter];
                            if (needsWork) {
                                break;
                            }
                        }
                    }
                    if (needsWork) {
                        log("Processing " + srcFile, 4);
                        translateOneFile(src, dest);
                        filesProcessed++;
                    } else {
                        log("Skipping " + srcFile + " as destination file is up to date", 3);
                    }
                } catch (IOException ioe) {
                    throw new BuildException(ioe.getMessage(), getLocation());
                }
            }
        }
        log("Translation performed on " + filesProcessed + " file(s).", 4);
    }

    private void translateOneFile(File src, File dest) throws IOException {
        String replace;
        int startIndex;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(dest.toPath(), new OpenOption[0]), this.destEncoding));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(src.toPath(), new OpenOption[0]), this.srcEncoding));
            LineTokenizer lineTokenizer = new LineTokenizer();
            lineTokenizer.setIncludeDelims(true);
            String line = lineTokenizer.getToken(in);
            while (line != null) {
                int startIndex2 = line.indexOf(this.startToken);
                while (startIndex2 >= 0 && startIndex2 + this.startToken.length() <= line.length()) {
                    int endIndex = line.indexOf(this.endToken, startIndex2 + this.startToken.length());
                    if (endIndex < 0) {
                        startIndex = startIndex2 + 1;
                    } else {
                        String token = line.substring(startIndex2 + this.startToken.length(), endIndex);
                        boolean validToken = true;
                        for (int k = 0; k < token.length() && validToken; k++) {
                            char c = token.charAt(k);
                            if (c == ':' || c == '=' || Character.isSpaceChar(c)) {
                                validToken = false;
                            }
                        }
                        if (!validToken) {
                            startIndex = startIndex2 + 1;
                        } else {
                            if (this.resourceMap.containsKey(token)) {
                                replace = this.resourceMap.get(token);
                            } else {
                                log("Replacement string missing for: " + token, 3);
                                replace = this.startToken + token + this.endToken;
                            }
                            line = line.substring(0, startIndex2) + replace + line.substring(endIndex + this.endToken.length());
                            startIndex = startIndex2 + replace.length();
                        }
                    }
                    startIndex2 = line.indexOf(this.startToken, startIndex);
                }
                out.write(line);
                line = lineTokenizer.getToken(in);
            }
            in.close();
            out.close();
        } catch (Throwable th) {
            try {
                out.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
