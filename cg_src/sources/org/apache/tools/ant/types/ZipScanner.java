package org.apache.tools.ant.types;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.ZipResource;
import org.apache.tools.ant.util.StreamUtils;
import org.apache.tools.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ZipScanner.class */
public class ZipScanner extends ArchiveScanner {
    @Override // org.apache.tools.ant.types.ArchiveScanner
    protected void fillMapsFromArchive(Resource src, String encoding, Map<String, Resource> fileEntries, Map<String, Resource> matchFileEntries, Map<String, Resource> dirEntries, Map<String, Resource> matchDirEntries) {
        File srcFile = (File) src.asOptional(FileProvider.class).map((v0) -> {
            return v0.getFile();
        }).orElseThrow(() -> {
            return new BuildException("Only file provider resources are supported");
        });
        try {
            ZipFile zf = new ZipFile(srcFile, encoding);
            try {
                StreamUtils.enumerationAsStream(zf.getEntries()).forEach(entry -> {
                    ZipResource zipResource = new ZipResource(srcFile, srcFile, matchFileEntries);
                    String name = matchFileEntries.getName();
                    if (matchFileEntries.isDirectory()) {
                        String name2 = trimSeparator(name);
                        encoding.put(name2, zipResource);
                        if (match(name2)) {
                            dirEntries.put(name2, zipResource);
                            return;
                        }
                        return;
                    }
                    matchDirEntries.put(name, zipResource);
                    if (match(name)) {
                        fileEntries.put(name, zipResource);
                    }
                });
                zf.close();
            } catch (Throwable th) {
                try {
                    zf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (ZipException ex) {
            throw new BuildException("Problem reading " + srcFile, ex);
        } catch (IOException ex2) {
            throw new BuildException("Problem opening " + srcFile, ex2);
        }
    }
}
