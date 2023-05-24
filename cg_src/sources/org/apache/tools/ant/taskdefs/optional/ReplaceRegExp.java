package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.Substitution;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ReplaceRegExp.class */
public class ReplaceRegExp extends Task {
    private Union resources;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private boolean preserveLastModified = false;
    private String encoding = null;
    private File file = null;
    private String flags = "";
    private boolean byline = false;
    private RegularExpression regex = null;
    private Substitution subs = null;

    public void setFile(File file) {
        this.file = file;
    }

    public void setMatch(String match) {
        if (this.regex != null) {
            throw new BuildException("Only one regular expression is allowed");
        }
        this.regex = new RegularExpression();
        this.regex.setPattern(match);
    }

    public void setReplace(String replace) {
        if (this.subs != null) {
            throw new BuildException("Only one substitution expression is allowed");
        }
        this.subs = new Substitution();
        this.subs.setExpression(replace);
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    @Deprecated
    public void setByLine(String byline) {
        this.byline = Boolean.parseBoolean(byline);
    }

    public void setByLine(boolean byline) {
        this.byline = byline;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void addFileset(FileSet set) {
        addConfigured(set);
    }

    public void addConfigured(ResourceCollection rc) {
        if (!rc.isFilesystemOnly()) {
            throw new BuildException("only filesystem resources are supported");
        }
        if (this.resources == null) {
            this.resources = new Union();
        }
        this.resources.add(rc);
    }

    public RegularExpression createRegexp() {
        if (this.regex != null) {
            throw new BuildException("Only one regular expression is allowed.");
        }
        this.regex = new RegularExpression();
        return this.regex;
    }

    public Substitution createSubstitution() {
        if (this.subs != null) {
            throw new BuildException("Only one substitution expression is allowed");
        }
        this.subs = new Substitution();
        return this.subs;
    }

    public void setPreserveLastModified(boolean b) {
        this.preserveLastModified = b;
    }

    protected String doReplace(RegularExpression r, Substitution s, String input, int options) {
        String res = input;
        Regexp regexp = r.getRegexp(getProject());
        if (regexp.matches(input, options)) {
            log("Found match; substituting", 4);
            res = regexp.substitute(input, s.getExpression(getProject()), options);
        }
        return res;
    }

    protected void doReplace(File f, int options) throws IOException {
        int c;
        File temp = FILE_UTILS.createTempFile(getProject(), MSVSSConstants.WRITABLE_REPLACE, ".txt", null, true, true);
        try {
            boolean changes = false;
            Charset charset = this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding);
            InputStream is = Files.newInputStream(f.toPath(), new OpenOption[0]);
            OutputStream os = Files.newOutputStream(temp.toPath(), new OpenOption[0]);
            try {
                try {
                    Reader r = new InputStreamReader(is, charset);
                    Writer w = new OutputStreamWriter(os, charset);
                    log("Replacing pattern '" + this.regex.getPattern(getProject()) + "' with '" + this.subs.getExpression(getProject()) + "' in '" + f.getPath() + "'" + (this.byline ? " by line" : "") + (this.flags.isEmpty() ? "" : " with flags: '" + this.flags + "'") + ".", 3);
                    if (this.byline) {
                        r = new BufferedReader(r);
                        w = new BufferedWriter(w);
                        StringBuilder linebuf = new StringBuilder();
                        boolean hasCR = false;
                        do {
                            c = r.read();
                            if (c == 13) {
                                if (hasCR) {
                                    changes |= replaceAndWrite(linebuf.toString(), w, options);
                                    w.write(13);
                                    linebuf = new StringBuilder();
                                } else {
                                    hasCR = true;
                                }
                            } else if (c == 10) {
                                changes |= replaceAndWrite(linebuf.toString(), w, options);
                                if (hasCR) {
                                    w.write(13);
                                    hasCR = false;
                                }
                                w.write(10);
                                linebuf = new StringBuilder();
                            } else {
                                if (hasCR || c < 0) {
                                    changes |= replaceAndWrite(linebuf.toString(), w, options);
                                    if (hasCR) {
                                        w.write(13);
                                        hasCR = false;
                                    }
                                    linebuf = new StringBuilder();
                                }
                                if (c >= 0) {
                                    linebuf.append((char) c);
                                }
                            }
                        } while (c >= 0);
                    } else {
                        changes = multilineReplace(r, w, options);
                    }
                    FileUtils.close(r);
                    FileUtils.close(w);
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (changes) {
                        log("File has changed; saving the updated file", 3);
                        try {
                            long origLastModified = f.lastModified();
                            FILE_UTILS.rename(temp, f);
                            if (this.preserveLastModified) {
                                FILE_UTILS.setFileLastModified(f, origLastModified);
                            }
                            temp = null;
                        } catch (IOException e) {
                            throw new BuildException("Couldn't rename temporary file " + temp, e, getLocation());
                        }
                    } else {
                        log("No change made", 4);
                    }
                    if (temp != null) {
                        temp.delete();
                    }
                } catch (Throwable th) {
                    FileUtils.close((Reader) null);
                    FileUtils.close((Writer) null);
                    throw th;
                }
            } catch (Throwable th2) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        } catch (Throwable th4) {
            if (temp != null) {
                temp.delete();
            }
            throw th4;
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.regex == null) {
            throw new BuildException("No expression to match.");
        }
        if (this.subs == null) {
            throw new BuildException("Nothing to replace expression with.");
        }
        if (this.file != null && this.resources != null) {
            throw new BuildException("You cannot supply the 'file' attribute and resource collections at the same time.");
        }
        int options = RegexpUtil.asOptions(this.flags);
        if (this.file != null && this.file.exists()) {
            try {
                doReplace(this.file, options);
            } catch (IOException e) {
                log("An error occurred processing file: '" + this.file.getAbsolutePath() + "': " + e.toString(), 0);
            }
        } else if (this.file != null) {
            log("The following file is missing: '" + this.file.getAbsolutePath() + "'", 0);
        }
        if (this.resources != null) {
            Iterator<Resource> it = this.resources.iterator();
            while (it.hasNext()) {
                Resource r = it.next();
                File f = ((FileProvider) r.as(FileProvider.class)).getFile();
                if (f.exists()) {
                    try {
                        doReplace(f, options);
                    } catch (Exception e2) {
                        log("An error occurred processing file: '" + f.getAbsolutePath() + "': " + e2.toString(), 0);
                    }
                } else {
                    log("The following file is missing: '" + f.getAbsolutePath() + "'", 0);
                }
            }
        }
    }

    private boolean multilineReplace(Reader r, Writer w, int options) throws IOException {
        return replaceAndWrite(FileUtils.safeReadFully(r), w, options);
    }

    private boolean replaceAndWrite(String s, Writer w, int options) throws IOException {
        String res = doReplace(this.regex, this.subs, s, options);
        w.write(res);
        return !res.equals(s);
    }
}
