package org.apache.tools.ant.taskdefs.optional.jlink;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jlink/jlink.class */
public class jlink {
    private static final int BUFFER_SIZE = 8192;
    private static final int VECTOR_INIT_SIZE = 10;
    private String outfile = null;
    private List<String> mergefiles = new Vector(10);
    private List<String> addfiles = new Vector(10);
    private boolean compression = false;
    byte[] buffer = new byte[8192];

    public void setOutfile(String outfile) {
        if (outfile == null) {
            return;
        }
        this.outfile = outfile;
    }

    public void addMergeFile(String fileToMerge) {
        if (fileToMerge == null) {
            return;
        }
        this.mergefiles.add(fileToMerge);
    }

    public void addAddFile(String fileToAdd) {
        if (fileToAdd == null) {
            return;
        }
        this.addfiles.add(fileToAdd);
    }

    public void addMergeFiles(String... filesToMerge) {
        if (filesToMerge == null) {
            return;
        }
        for (String element : filesToMerge) {
            addMergeFile(element);
        }
    }

    public void addAddFiles(String... filesToAdd) {
        if (filesToAdd == null) {
            return;
        }
        for (String element : filesToAdd) {
            addAddFile(element);
        }
    }

    public void setCompression(boolean compress) {
        this.compression = compress;
    }

    public void link() throws Exception {
        ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(Paths.get(this.outfile, new String[0]), new OpenOption[0]));
        try {
            if (this.compression) {
                output.setMethod(8);
                output.setLevel(-1);
            } else {
                output.setMethod(0);
            }
            for (String path : this.mergefiles) {
                File f = new File(path);
                if (f.getName().endsWith(".jar") || f.getName().endsWith(".zip")) {
                    mergeZipJarContents(output, f);
                } else {
                    addAddFile(path);
                }
            }
            for (String name : this.addfiles) {
                File f2 = new File(name);
                if (f2.isDirectory()) {
                    addDirContents(output, f2, f2.getName() + '/', this.compression);
                } else {
                    addFile(output, f2, "", this.compression);
                }
            }
            output.close();
        } catch (Throwable th) {
            try {
                output.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: jlink output input1 ... inputN");
            System.exit(1);
        }
        jlink linker = new jlink();
        linker.setOutfile(args[0]);
        for (int i = 1; i < args.length; i++) {
            linker.addMergeFile(args[i]);
        }
        try {
            linker.link();
        } catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }

    private void mergeZipJarContents(ZipOutputStream output, File f) throws IOException {
        if (!f.exists()) {
            return;
        }
        ZipFile zipf = new ZipFile(f);
        try {
            Enumeration<? extends ZipEntry> entries = zipf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry inputEntry = entries.nextElement();
                String inputEntryName = inputEntry.getName();
                int index = inputEntryName.indexOf("META-INF");
                if (index < 0) {
                    try {
                        output.putNextEntry(processEntry(zipf, inputEntry));
                        InputStream in = zipf.getInputStream(inputEntry);
                        int len = this.buffer.length;
                        while (true) {
                            int count = in.read(this.buffer, 0, len);
                            if (count <= 0) {
                                break;
                            }
                            output.write(this.buffer, 0, count);
                        }
                        output.closeEntry();
                        if (in != null) {
                            in.close();
                        }
                    } catch (ZipException ex) {
                        if (!ex.getMessage().contains("duplicate")) {
                            throw ex;
                        }
                    }
                }
            }
            zipf.close();
        } catch (Throwable th) {
            try {
                zipf.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void addDirContents(ZipOutputStream output, File dir, String prefix, boolean compress) throws IOException {
        String[] list;
        for (String name : dir.list()) {
            File file = new File(dir, name);
            if (file.isDirectory()) {
                addDirContents(output, file, prefix + name + '/', compress);
            } else {
                addFile(output, file, prefix, compress);
            }
        }
    }

    private String getEntryName(File file, String prefix) {
        String name = file.getName();
        if (!name.endsWith(".class")) {
            try {
                InputStream input = Files.newInputStream(file.toPath(), new OpenOption[0]);
                String className = ClassNameReader.getClassName(input);
                if (className != null) {
                    String str = className.replace('.', '/') + ".class";
                    if (input != null) {
                        input.close();
                    }
                    return str;
                } else if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
            }
        }
        System.out.printf("From %1$s and prefix %2$s, creating entry %2$s%3$s%n", file.getPath(), prefix, name);
        return prefix + name;
    }

    private void addFile(ZipOutputStream output, File file, String prefix, boolean compress) throws IOException {
        if (!file.exists()) {
            return;
        }
        ZipEntry entry = new ZipEntry(getEntryName(file, prefix));
        entry.setTime(file.lastModified());
        entry.setSize(file.length());
        if (!compress) {
            entry.setCrc(calcChecksum(file));
        }
        addToOutputStream(output, Files.newInputStream(file.toPath(), new OpenOption[0]), entry);
    }

    private void addToOutputStream(ZipOutputStream output, InputStream input, ZipEntry ze) throws IOException {
        try {
            output.putNextEntry(ze);
            while (true) {
                int numBytes = input.read(this.buffer);
                if (numBytes > 0) {
                    output.write(this.buffer, 0, numBytes);
                } else {
                    output.closeEntry();
                    input.close();
                    return;
                }
            }
        } catch (ZipException e) {
            input.close();
        }
    }

    private ZipEntry processEntry(ZipFile zip, ZipEntry inputEntry) {
        String name = inputEntry.getName();
        if (!inputEntry.isDirectory() && !name.endsWith(".class")) {
            try {
                InputStream input = zip.getInputStream(zip.getEntry(name));
                String className = ClassNameReader.getClassName(input);
                if (className != null) {
                    name = className.replace('.', '/') + ".class";
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
            }
        }
        ZipEntry outputEntry = new ZipEntry(name);
        outputEntry.setTime(inputEntry.getTime());
        outputEntry.setExtra(inputEntry.getExtra());
        outputEntry.setComment(inputEntry.getComment());
        outputEntry.setTime(inputEntry.getTime());
        if (this.compression) {
            outputEntry.setMethod(8);
        } else {
            outputEntry.setMethod(0);
            outputEntry.setCrc(inputEntry.getCrc());
            outputEntry.setSize(inputEntry.getSize());
        }
        return outputEntry;
    }

    private long calcChecksum(File f) throws IOException {
        return calcChecksum(new BufferedInputStream(Files.newInputStream(f.toPath(), new OpenOption[0])));
    }

    private long calcChecksum(InputStream in) throws IOException {
        CRC32 crc = new CRC32();
        int len = this.buffer.length;
        while (true) {
            int count = in.read(this.buffer, 0, len);
            if (count > 0) {
                crc.update(this.buffer, 0, count);
            } else {
                in.close();
                return crc.getValue();
            }
        }
    }
}
