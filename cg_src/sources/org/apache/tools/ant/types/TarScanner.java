package org.apache.tools.ant.types;

import java.io.IOException;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.TarResource;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/TarScanner.class */
public class TarScanner extends ArchiveScanner {
    @Override // org.apache.tools.ant.types.ArchiveScanner
    protected void fillMapsFromArchive(Resource src, String encoding, Map<String, Resource> fileEntries, Map<String, Resource> matchFileEntries, Map<String, Resource> dirEntries, Map<String, Resource> matchDirEntries) {
        try {
            TarInputStream ti = new TarInputStream(src.getInputStream(), encoding);
            while (true) {
                try {
                    TarEntry entry = ti.getNextEntry();
                    if (entry != null) {
                        Resource r = new TarResource(src, entry);
                        String name = entry.getName();
                        if (entry.isDirectory()) {
                            String name2 = trimSeparator(name);
                            dirEntries.put(name2, r);
                            if (match(name2)) {
                                matchDirEntries.put(name2, r);
                            }
                        } else {
                            fileEntries.put(name, r);
                            if (match(name)) {
                                matchFileEntries.put(name, r);
                            }
                        }
                    } else {
                        ti.close();
                        return;
                    }
                } catch (IOException ex) {
                    throw new BuildException("problem reading " + this.srcFile, ex);
                }
            }
        } catch (IOException ex2) {
            throw new BuildException("problem opening " + this.srcFile, ex2);
        }
    }
}
