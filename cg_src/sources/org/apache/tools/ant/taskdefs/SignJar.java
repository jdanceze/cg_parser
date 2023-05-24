package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.http.HttpHost;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.IsSigned;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SignJar.class */
public class SignJar extends AbstractJarSignerTask {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    public static final String ERROR_TODIR_AND_SIGNEDJAR = "'destdir' and 'signedjar' cannot both be set";
    public static final String ERROR_TOO_MANY_MAPPERS = "Too many mappers";
    public static final String ERROR_SIGNEDJAR_AND_PATHS = "You cannot specify the signed JAR when using paths or filesets";
    public static final String ERROR_BAD_MAP = "Cannot map source file to anything sensible: ";
    public static final String ERROR_MAPPER_WITHOUT_DEST = "The destDir attribute is required if a mapper is set";
    public static final String ERROR_NO_ALIAS = "alias attribute must be set";
    public static final String ERROR_NO_STOREPASS = "storepass attribute must be set";
    protected String sigfile;
    protected File signedjar;
    protected boolean internalsf;
    protected boolean sectionsonly;
    private boolean preserveLastModified;
    protected boolean lazy;
    protected File destDir;
    private FileNameMapper mapper;
    protected String tsaurl;
    protected String tsaproxyhost;
    protected String tsaproxyport;
    protected String tsacert;
    private boolean force = false;
    private String sigAlg;
    private String digestAlg;
    private String tsaDigestAlg;

    public void setSigfile(String sigfile) {
        this.sigfile = sigfile;
    }

    public void setSignedjar(File signedjar) {
        this.signedjar = signedjar;
    }

    public void setInternalsf(boolean internalsf) {
        this.internalsf = internalsf;
    }

    public void setSectionsonly(boolean sectionsonly) {
        this.sectionsonly = sectionsonly;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void add(FileNameMapper newMapper) {
        if (this.mapper != null) {
            throw new BuildException(ERROR_TOO_MANY_MAPPERS);
        }
        this.mapper = newMapper;
    }

    public FileNameMapper getMapper() {
        return this.mapper;
    }

    public String getTsaurl() {
        return this.tsaurl;
    }

    public void setTsaurl(String tsaurl) {
        this.tsaurl = tsaurl;
    }

    public String getTsaproxyhost() {
        return this.tsaproxyhost;
    }

    public void setTsaproxyhost(String tsaproxyhost) {
        this.tsaproxyhost = tsaproxyhost;
    }

    public String getTsaproxyport() {
        return this.tsaproxyport;
    }

    public void setTsaproxyport(String tsaproxyport) {
        this.tsaproxyport = tsaproxyport;
    }

    public String getTsacert() {
        return this.tsacert;
    }

    public void setTsacert(String tsacert) {
        this.tsacert = tsacert;
    }

    public void setForce(boolean b) {
        this.force = b;
    }

    public boolean isForce() {
        return this.force;
    }

    public void setSigAlg(String sigAlg) {
        this.sigAlg = sigAlg;
    }

    public String getSigAlg() {
        return this.sigAlg;
    }

    public void setDigestAlg(String digestAlg) {
        this.digestAlg = digestAlg;
    }

    public String getDigestAlg() {
        return this.digestAlg;
    }

    public void setTSADigestAlg(String digestAlg) {
        this.tsaDigestAlg = digestAlg;
    }

    public String getTSADigestAlg() {
        return this.tsaDigestAlg;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        boolean hasJar = this.jar != null;
        boolean hasSignedJar = this.signedjar != null;
        boolean hasDestDir = this.destDir != null;
        boolean hasMapper = this.mapper != null;
        if (!hasJar && !hasResources()) {
            throw new BuildException(AbstractJarSignerTask.ERROR_NO_SOURCE);
        }
        if (null == this.alias) {
            throw new BuildException(ERROR_NO_ALIAS);
        }
        if (null == this.storepass) {
            throw new BuildException(ERROR_NO_STOREPASS);
        }
        if (hasDestDir && hasSignedJar) {
            throw new BuildException(ERROR_TODIR_AND_SIGNEDJAR);
        }
        if (hasResources() && hasSignedJar) {
            throw new BuildException(ERROR_SIGNEDJAR_AND_PATHS);
        }
        if (!hasDestDir && hasMapper) {
            throw new BuildException(ERROR_MAPPER_WITHOUT_DEST);
        }
        beginExecution();
        try {
            if (hasJar && hasSignedJar) {
                signOneJar(this.jar, this.signedjar);
                endExecution();
                return;
            }
            Path sources = createUnifiedSourcePath();
            FileNameMapper destMapper = hasMapper ? this.mapper : new IdentityMapper();
            Iterator<Resource> it = sources.iterator();
            while (it.hasNext()) {
                Resource r = it.next();
                FileResource fr = ResourceUtils.asFileResource((FileProvider) r.as(FileProvider.class));
                File toDir = hasDestDir ? this.destDir : fr.getBaseDir();
                String[] destFilenames = destMapper.mapFileName(fr.getName());
                if (destFilenames == null || destFilenames.length != 1) {
                    throw new BuildException(ERROR_BAD_MAP + fr.getFile());
                }
                File destFile = new File(toDir, destFilenames[0]);
                signOneJar(fr.getFile(), destFile);
            }
        } finally {
            endExecution();
        }
    }

    private void signOneJar(File jarSource, File jarTarget) throws BuildException {
        File targetFile = jarTarget;
        if (targetFile == null) {
            targetFile = jarSource;
        }
        if (isUpToDate(jarSource, targetFile)) {
            return;
        }
        long lastModified = jarSource.lastModified();
        ExecTask cmd = createJarSigner();
        setCommonOptions(cmd);
        bindToKeystore(cmd);
        if (null != this.sigfile) {
            addValue(cmd, "-sigfile");
            String value = this.sigfile;
            addValue(cmd, value);
        }
        try {
            if (!FILE_UTILS.areSame(jarSource, targetFile)) {
                addValue(cmd, "-signedjar");
                addValue(cmd, targetFile.getPath());
            }
            if (this.internalsf) {
                addValue(cmd, "-internalsf");
            }
            if (this.sectionsonly) {
                addValue(cmd, "-sectionsonly");
            }
            if (this.sigAlg != null) {
                addValue(cmd, "-sigalg");
                addValue(cmd, this.sigAlg);
            }
            if (this.digestAlg != null) {
                addValue(cmd, "-digestalg");
                addValue(cmd, this.digestAlg);
            }
            addTimestampAuthorityCommands(cmd);
            addValue(cmd, jarSource.getPath());
            addValue(cmd, this.alias);
            log("Signing JAR: " + jarSource.getAbsolutePath() + " to " + targetFile.getAbsolutePath() + " as " + this.alias);
            cmd.execute();
            if (this.preserveLastModified) {
                FILE_UTILS.setFileLastModified(targetFile, lastModified);
            }
        } catch (IOException ioex) {
            throw new BuildException(ioex);
        }
    }

    private void addTimestampAuthorityCommands(ExecTask cmd) {
        if (this.tsaurl != null) {
            addValue(cmd, "-tsa");
            addValue(cmd, this.tsaurl);
        }
        if (this.tsacert != null) {
            addValue(cmd, "-tsacert");
            addValue(cmd, this.tsacert);
        }
        if (this.tsaproxyhost != null) {
            if (this.tsaurl == null || this.tsaurl.startsWith("https")) {
                addProxyFor(cmd, "https");
            }
            if (this.tsaurl == null || !this.tsaurl.startsWith("https")) {
                addProxyFor(cmd, HttpHost.DEFAULT_SCHEME_NAME);
            }
        }
        if (this.tsaDigestAlg != null) {
            addValue(cmd, "-tsadigestalg");
            addValue(cmd, this.tsaDigestAlg);
        }
    }

    protected boolean isUpToDate(File jarFile, File signedjarFile) {
        if (isForce() || null == jarFile || !jarFile.exists()) {
            return false;
        }
        File destFile = signedjarFile;
        if (destFile == null) {
            destFile = jarFile;
        }
        if (jarFile.equals(destFile)) {
            if (this.lazy) {
                return isSigned(jarFile);
            }
            return false;
        }
        return FILE_UTILS.isUpToDate(jarFile, destFile);
    }

    protected boolean isSigned(File file) {
        try {
            return IsSigned.isSigned(file, this.sigfile == null ? this.alias : this.sigfile);
        } catch (IOException e) {
            log(e.toString(), 3);
            return false;
        }
    }

    public void setPreserveLastModified(boolean preserveLastModified) {
        this.preserveLastModified = preserveLastModified;
    }

    private void addProxyFor(ExecTask cmd, String scheme) {
        addValue(cmd, "-J-D" + scheme + ".proxyHost=" + this.tsaproxyhost);
        if (this.tsaproxyport != null) {
            addValue(cmd, "-J-D" + scheme + ".proxyPort=" + this.tsaproxyport);
        }
    }
}
