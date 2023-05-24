package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.selectors.Type;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Checksum.class */
public class Checksum extends MatchingTask implements Condition {
    private static final int NIBBLE = 4;
    private static final int WORD = 16;
    private static final int BUFFER_SIZE = 8192;
    private static final int BYTE_MASK = 255;
    private File todir;
    private String fileext;
    private String property;
    private String totalproperty;
    private boolean forceOverwrite;
    private String verifyProperty;
    private MessageDigest messageDigest;
    private boolean isCondition;
    private File file = null;
    private String algorithm = "MD5";
    private String provider = null;
    private Map<File, byte[]> allDigests = new HashMap();
    private Map<File, String> relativeFilePaths = new HashMap();
    private FileUnion resources = null;
    private Hashtable<File, Object> includeFileMap = new Hashtable<>();
    private int readBufferSize = 8192;
    private MessageFormat format = FormatElement.getDefault().getFormat();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Checksum$FileUnion.class */
    public static class FileUnion extends Restrict {
        private Union u = new Union();

        FileUnion() {
            super.add(this.u);
            super.add(Type.FILE);
        }

        @Override // org.apache.tools.ant.types.resources.Restrict
        public void add(ResourceCollection rc) {
            this.u.add(rc);
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setTodir(File todir) {
        this.todir = todir;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setFileext(String fileext) {
        this.fileext = fileext;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setTotalproperty(String totalproperty) {
        this.totalproperty = totalproperty;
    }

    public void setVerifyproperty(String verifyProperty) {
        this.verifyProperty = verifyProperty;
    }

    public void setForceOverwrite(boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }

    public void setReadBufferSize(int size) {
        this.readBufferSize = size;
    }

    public void setFormat(FormatElement e) {
        this.format = e.getFormat();
    }

    public void setPattern(String pattern) {
        this.format = new MessageFormat(pattern);
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection rc) {
        if (rc == null) {
            return;
        }
        this.resources = this.resources == null ? new FileUnion() : this.resources;
        this.resources.add(rc);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        this.isCondition = false;
        boolean value = validateAndExecute();
        if (this.verifyProperty != null) {
            getProject().setNewProperty(this.verifyProperty, Boolean.toString(value));
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        this.isCondition = true;
        return validateAndExecute();
    }

    private boolean validateAndExecute() throws BuildException {
        String savedFileExt = this.fileext;
        if (this.file == null && (this.resources == null || this.resources.size() == 0)) {
            throw new BuildException("Specify at least one source - a file or a resource collection.");
        }
        if (this.resources != null && !this.resources.isFilesystemOnly()) {
            throw new BuildException("Can only calculate checksums for file-based resources.");
        }
        if (this.file != null && this.file.exists() && this.file.isDirectory()) {
            throw new BuildException("Checksum cannot be generated for directories");
        }
        if (this.file != null && this.totalproperty != null) {
            throw new BuildException("File and Totalproperty cannot co-exist.");
        }
        if (this.property != null && this.fileext != null) {
            throw new BuildException("Property and FileExt cannot co-exist.");
        }
        if (this.property != null) {
            if (this.forceOverwrite) {
                throw new BuildException("ForceOverwrite cannot be used when Property is specified");
            }
            int ct = 0;
            if (this.resources != null) {
                ct = 0 + this.resources.size();
            }
            if (this.file != null) {
                ct++;
            }
            if (ct > 1) {
                throw new BuildException("Multiple files cannot be used when Property is specified");
            }
        }
        if (this.verifyProperty != null) {
            this.isCondition = true;
        }
        if (this.verifyProperty != null && this.forceOverwrite) {
            throw new BuildException("VerifyProperty and ForceOverwrite cannot co-exist.");
        }
        if (this.isCondition && this.forceOverwrite) {
            throw new BuildException("ForceOverwrite cannot be used when conditions are being used.");
        }
        this.messageDigest = null;
        if (this.provider != null) {
            try {
                this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
            } catch (NoSuchAlgorithmException | NoSuchProviderException noalgo) {
                throw new BuildException(noalgo, getLocation());
            }
        } else {
            try {
                this.messageDigest = MessageDigest.getInstance(this.algorithm);
            } catch (NoSuchAlgorithmException noalgo2) {
                throw new BuildException(noalgo2, getLocation());
            }
        }
        if (this.messageDigest == null) {
            throw new BuildException("Unable to create Message Digest", getLocation());
        }
        if (this.fileext == null) {
            this.fileext = "." + this.algorithm;
        } else if (this.fileext.trim().isEmpty()) {
            throw new BuildException("File extension when specified must not be an empty string");
        }
        try {
            if (this.resources != null) {
                Iterator<Resource> it = this.resources.iterator();
                while (it.hasNext()) {
                    Resource r = it.next();
                    File src = ((FileProvider) r.as(FileProvider.class)).getFile();
                    if (this.totalproperty != null || this.todir != null) {
                        this.relativeFilePaths.put(src, r.getName().replace(File.separatorChar, '/'));
                    }
                    addToIncludeFileMap(src);
                }
            }
            if (this.file != null) {
                if (this.totalproperty != null || this.todir != null) {
                    this.relativeFilePaths.put(this.file, this.file.getName().replace(File.separatorChar, '/'));
                }
                addToIncludeFileMap(this.file);
            }
            boolean generateChecksums = generateChecksums();
            this.fileext = savedFileExt;
            this.includeFileMap.clear();
            return generateChecksums;
        } catch (Throwable th) {
            this.fileext = savedFileExt;
            this.includeFileMap.clear();
            throw th;
        }
    }

    private void addToIncludeFileMap(File file) throws BuildException {
        if (file.exists()) {
            if (this.property == null) {
                File checksumFile = getChecksumFile(file);
                if (this.forceOverwrite || this.isCondition || file.lastModified() > checksumFile.lastModified()) {
                    this.includeFileMap.put(file, checksumFile);
                    return;
                }
                log(file + " omitted as " + checksumFile + " is up to date.", 3);
                if (this.totalproperty != null) {
                    String checksum = readChecksum(checksumFile);
                    byte[] digest = decodeHex(checksum.toCharArray());
                    this.allDigests.put(file, digest);
                    return;
                }
                return;
            }
            this.includeFileMap.put(file, this.property);
            return;
        }
        String message = "Could not find file " + file.getAbsolutePath() + " to generate checksum for.";
        log(message);
        throw new BuildException(message, getLocation());
    }

    private File getChecksumFile(File file) {
        File directory;
        if (this.todir != null) {
            String path = getRelativeFilePath(file);
            directory = new File(this.todir, path).getParentFile();
            directory.mkdirs();
        } else {
            directory = file.getParentFile();
        }
        return new File(directory, file.getName() + this.fileext);
    }

    private boolean generateChecksums() throws BuildException {
        boolean checksumMatches = true;
        InputStream fis = null;
        OutputStream fos = null;
        byte[] buf = new byte[this.readBufferSize];
        try {
            try {
                for (Map.Entry<File, Object> e : this.includeFileMap.entrySet()) {
                    this.messageDigest.reset();
                    File src = e.getKey();
                    if (!this.isCondition) {
                        log("Calculating " + this.algorithm + " checksum for " + src, 3);
                    }
                    InputStream fis2 = Files.newInputStream(src.toPath(), new OpenOption[0]);
                    DigestInputStream dis = new DigestInputStream(fis2, this.messageDigest);
                    while (dis.read(buf, 0, this.readBufferSize) != -1) {
                    }
                    dis.close();
                    fis2.close();
                    fis = null;
                    byte[] fileDigest = this.messageDigest.digest();
                    if (this.totalproperty != null) {
                        this.allDigests.put(src, fileDigest);
                    }
                    String checksum = createDigestString(fileDigest);
                    Object destination = e.getValue();
                    if (destination instanceof String) {
                        String prop = (String) destination;
                        if (this.isCondition) {
                            checksumMatches = checksumMatches && checksum.equals(this.property);
                        } else {
                            getProject().setNewProperty(prop, checksum);
                        }
                    } else if (destination instanceof File) {
                        if (this.isCondition) {
                            File existingFile = (File) destination;
                            if (existingFile.exists()) {
                                try {
                                    String suppliedChecksum = readChecksum(existingFile);
                                    checksumMatches = checksumMatches && checksum.equals(suppliedChecksum);
                                } catch (BuildException e2) {
                                    checksumMatches = false;
                                }
                            } else {
                                checksumMatches = false;
                            }
                        } else {
                            File dest = (File) destination;
                            OutputStream fos2 = Files.newOutputStream(dest.toPath(), new OpenOption[0]);
                            fos2.write(this.format.format(new Object[]{checksum, src.getName(), FileUtils.getRelativePath(dest.getParentFile(), src), FileUtils.getRelativePath(getProject().getBaseDir(), src), src.getAbsolutePath()}).getBytes());
                            fos2.write(System.lineSeparator().getBytes());
                            fos2.close();
                            fos = null;
                        }
                    }
                }
                if (this.totalproperty != null) {
                    File[] keyArray = (File[]) this.allDigests.keySet().toArray(new File[this.allDigests.size()]);
                    Arrays.sort(keyArray, Comparator.nullsFirst(Comparator.comparing(this::getRelativeFilePath)));
                    this.messageDigest.reset();
                    for (File src2 : keyArray) {
                        byte[] digest = this.allDigests.get(src2);
                        this.messageDigest.update(digest);
                        String fileName = getRelativeFilePath(src2);
                        this.messageDigest.update(fileName.getBytes());
                    }
                    String totalChecksum = createDigestString(this.messageDigest.digest());
                    getProject().setNewProperty(this.totalproperty, totalChecksum);
                }
                return checksumMatches;
            } catch (Exception e3) {
                throw new BuildException(e3, getLocation());
            }
        } finally {
            FileUtils.close(fis);
            FileUtils.close(fos);
        }
    }

    private String createDigestString(byte[] fileDigest) {
        StringBuilder checksumSb = new StringBuilder();
        for (byte digestByte : fileDigest) {
            checksumSb.append(String.format("%02x", Integer.valueOf(255 & digestByte)));
        }
        return checksumSb.toString();
    }

    public static byte[] decodeHex(char[] data) throws BuildException {
        int l = data.length;
        if ((l & 1) != 0) {
            throw new BuildException("odd number of characters.");
        }
        byte[] out = new byte[l >> 1];
        int i = 0;
        int j = 0;
        while (j < l) {
            int i2 = j;
            int j2 = j + 1;
            int f = Character.digit(data[i2], 16) << 4;
            j = j2 + 1;
            out[i] = (byte) ((f | Character.digit(data[j2], 16)) & 255);
            i++;
        }
        return out;
    }

    private String readChecksum(File f) {
        try {
            BufferedReader diskChecksumReader = new BufferedReader(new FileReader(f));
            Object[] result = this.format.parse(diskChecksumReader.readLine());
            if (result == null || result.length == 0 || result[0] == null) {
                throw new BuildException("failed to find a checksum");
            }
            String str = (String) result[0];
            diskChecksumReader.close();
            return str;
        } catch (IOException | ParseException e) {
            throw new BuildException("Couldn't read checksum file " + f, e);
        }
    }

    private String getRelativeFilePath(File f) {
        String path = this.relativeFilePaths.get(f);
        if (path == null) {
            throw new BuildException("Internal error: relativeFilePaths could not match file %s\nplease file a bug report on this", f);
        }
        return path;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Checksum$FormatElement.class */
    public static class FormatElement extends EnumeratedAttribute {
        private static HashMap<String, MessageFormat> formatMap = new HashMap<>();
        private static final String CHECKSUM = "CHECKSUM";
        private static final String MD5SUM = "MD5SUM";
        private static final String SVF = "SVF";

        static {
            formatMap.put(CHECKSUM, new MessageFormat("{0}"));
            formatMap.put(MD5SUM, new MessageFormat("{0} *{1}"));
            formatMap.put(SVF, new MessageFormat("MD5 ({1}) = {0}"));
        }

        public static FormatElement getDefault() {
            FormatElement e = new FormatElement();
            e.setValue(CHECKSUM);
            return e;
        }

        public MessageFormat getFormat() {
            return formatMap.get(getValue());
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{CHECKSUM, MD5SUM, SVF};
        }
    }
}
