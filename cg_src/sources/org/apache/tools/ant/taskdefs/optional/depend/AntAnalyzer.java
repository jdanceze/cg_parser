package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.types.resources.ZipResource;
import org.apache.tools.ant.util.depend.AbstractAnalyzer;
import org.apache.tools.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/AntAnalyzer.class */
public class AntAnalyzer extends AbstractAnalyzer {
    @Override // org.apache.tools.ant.util.depend.AbstractAnalyzer
    protected void determineDependencies(Vector<File> files, Vector<String> classes) {
        InputStream zipEntryStream;
        Set<String> dependencies = new HashSet<>();
        Set<File> containers = new HashSet<>();
        Set<String> toAnalyze = new HashSet<>(Collections.list(getRootClasses()));
        Set<String> analyzedDeps = new HashSet<>();
        int count = 0;
        int maxCount = isClosureRequired() ? 1000 : 1;
        while (!toAnalyze.isEmpty()) {
            int i = count;
            count++;
            if (i >= maxCount) {
                break;
            }
            analyzedDeps.clear();
            for (String classname : toAnalyze) {
                dependencies.add(classname);
                File container = null;
                try {
                    container = getClassContainer(classname);
                } catch (IOException e) {
                }
                if (container != null) {
                    containers.add(container);
                    try {
                        if (container.getName().endsWith(".class")) {
                            zipEntryStream = Files.newInputStream(Paths.get(container.getPath(), new String[0]), new OpenOption[0]);
                        } else {
                            zipEntryStream = ZipResource.getZipEntryStream(new ZipFile(container.getPath(), "UTF-8"), classname.replace('.', '/') + ".class");
                        }
                        InputStream inStream = zipEntryStream;
                        ClassFile classFile = new ClassFile();
                        classFile.read(inStream);
                        analyzedDeps.addAll(classFile.getClassRefs());
                        if (inStream != null) {
                            inStream.close();
                        }
                    } catch (IOException e2) {
                    }
                }
            }
            toAnalyze.clear();
            Stream<String> filter = analyzedDeps.stream().filter(className -> {
                return !dependencies.contains(className);
            });
            Objects.requireNonNull(toAnalyze);
            filter.forEach((v1) -> {
                r1.add(v1);
            });
        }
        dependencies.addAll(analyzedDeps);
        files.removeAllElements();
        files.addAll(containers);
        classes.removeAllElements();
        classes.addAll(dependencies);
    }

    @Override // org.apache.tools.ant.util.depend.AbstractAnalyzer
    protected boolean supportsFileDependencies() {
        return true;
    }
}
