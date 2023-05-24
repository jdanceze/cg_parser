package polyglot.types.reflect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/ClassFileLoader.class */
public class ClassFileLoader {
    private ExtensionInfo extensionInfo;
    static final Object not_found = new Object();
    static Collection verbose = new HashSet();
    Map zipCache = new HashMap();
    Map dirContentsCache = new HashMap();
    Set packageCache = new HashSet();

    static {
        verbose.add(Report.loader);
    }

    public ClassFileLoader(ExtensionInfo ext) {
        this.extensionInfo = ext;
    }

    public boolean packageExists(File dir, String name) {
        if (Report.should_report(verbose, 3)) {
            Report.report(3, new StringBuffer().append("looking in ").append(dir).append(" for ").append(name.replace('.', File.separatorChar)).toString());
        }
        try {
            if (dir.getName().endsWith(".jar") || dir.getName().endsWith(".zip")) {
                String entryName = name.replace('.', '/');
                if (this.packageCache.contains(entryName)) {
                    return true;
                }
                loadZip(dir);
                return this.packageCache.contains(entryName);
            }
            File f = new File(dir, name.replace('.', File.separatorChar));
            return f.exists() && f.isDirectory();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e2) {
            throw new InternalCompilerError(e2);
        }
    }

    public ClassFile loadClass(File dir, String name) {
        if (Report.should_report(verbose, 3)) {
            Report.report(3, new StringBuffer().append("looking in ").append(dir).append(" for ").append(name.replace('.', File.separatorChar)).append(".class").toString());
        }
        try {
            if (dir.getName().endsWith(".jar") || dir.getName().endsWith(".zip")) {
                ZipFile zip = loadZip(dir);
                String entryName = new StringBuffer().append(name.replace('.', '/')).append(".class").toString();
                return loadFromZip(dir, zip, entryName);
            }
            return loadFromFile(name, dir);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e2) {
            throw new InternalCompilerError(e2);
        }
    }

    ZipFile loadZip(File dir) throws IOException {
        ZipFile zip;
        Object o = this.zipCache.get(dir);
        if (o != not_found) {
            ZipFile zip2 = (ZipFile) o;
            if (zip2 != null) {
                return zip2;
            }
            if (!dir.exists()) {
                this.zipCache.put(dir, not_found);
            } else {
                if (Report.should_report(verbose, 2)) {
                    Report.report(2, new StringBuffer().append("Opening zip ").append(dir).toString());
                }
                if (dir.getName().endsWith(".jar")) {
                    zip = new JarFile(dir);
                } else {
                    zip = new ZipFile(dir);
                }
                this.zipCache.put(dir, zip);
                Enumeration i = zip.entries();
                while (i.hasMoreElements()) {
                    ZipEntry ei = i.nextElement();
                    String n = ei.getName();
                    int indexOf = n.indexOf(47);
                    while (true) {
                        int index = indexOf;
                        if (index >= 0) {
                            this.packageCache.add(n.substring(0, index));
                            indexOf = n.indexOf(47, index + 1);
                        }
                    }
                }
                return zip;
            }
        }
        throw new FileNotFoundException(dir.getAbsolutePath());
    }

    ClassFile loadFromZip(File source, ZipFile zip, String entryName) throws IOException {
        ZipEntry entry;
        if (Report.should_report(verbose, 2)) {
            Report.report(2, new StringBuffer().append("Looking for ").append(entryName).append(" in ").append(zip.getName()).toString());
        }
        if (zip != null && (entry = zip.getEntry(entryName)) != null) {
            if (Report.should_report(verbose, 3)) {
                Report.report(3, new StringBuffer().append("found zip entry ").append(entry).toString());
            }
            InputStream in = zip.getInputStream(entry);
            ClassFile c = loadFromStream(source, in, entryName);
            in.close();
            return c;
        }
        return null;
    }

    ClassFile loadFromFile(String name, File dir) throws IOException {
        HashSet dirContents = (Set) this.dirContentsCache.get(dir);
        if (dirContents == null) {
            dirContents = new HashSet();
            this.dirContentsCache.put(dir, dirContents);
            if (dir.exists() && dir.isDirectory()) {
                String[] contents = dir.list();
                for (String str : contents) {
                    dirContents.add(str);
                }
            }
        }
        StringBuffer filenameSB = new StringBuffer(name.length() + 8);
        int firstSeparator = -1;
        filenameSB.append(name);
        for (int i = 0; i < filenameSB.length(); i++) {
            if (filenameSB.charAt(i) == '.') {
                filenameSB.setCharAt(i, File.separatorChar);
                if (firstSeparator == -1) {
                    firstSeparator = i;
                }
            }
        }
        filenameSB.append(".class");
        String filename = filenameSB.toString();
        String firstPart = firstSeparator == -1 ? filename : filename.substring(0, firstSeparator);
        if (!dirContents.contains(firstPart)) {
            return null;
        }
        File file = new File(dir, filename);
        FileInputStream in = new FileInputStream(file);
        if (Report.should_report(verbose, 3)) {
            Report.report(3, new StringBuffer().append("found ").append(file).toString());
        }
        ClassFile c = loadFromStream(file, in, name);
        in.close();
        return c;
    }

    ClassFile loadFromStream(File source, InputStream in, String name) throws IOException {
        int n;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        do {
            n = in.read(buf);
            if (n >= 0) {
                out.write(buf, 0, n);
            }
        } while (n >= 0);
        byte[] bytecode = out.toByteArray();
        try {
            if (Report.should_report(verbose, 3)) {
                Report.report(3, new StringBuffer().append("defining class ").append(name).toString());
            }
            return this.extensionInfo.createClassFile(source, bytecode);
        } catch (ClassFormatError e) {
            throw new IOException(e.getMessage());
        }
    }
}
