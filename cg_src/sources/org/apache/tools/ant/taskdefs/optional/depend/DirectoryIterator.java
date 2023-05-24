package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/DirectoryIterator.class */
public class DirectoryIterator implements ClassFileIterator {
    private Deque<Iterator<File>> enumStack = new ArrayDeque();
    private Iterator<File> currentIterator;

    public DirectoryIterator(File rootDirectory, boolean changeInto) throws IOException {
        this.currentIterator = getDirectoryEntries(rootDirectory).iterator();
    }

    private List<File> getDirectoryEntries(File directory) {
        File[] filesInDir = directory.listFiles();
        if (filesInDir == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(filesInDir);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.ClassFileIterator
    public ClassFile getNextClassFile() {
        ClassFile nextElement = null;
        while (nextElement == null) {
            try {
                if (this.currentIterator.hasNext()) {
                    File element = this.currentIterator.next();
                    if (element.isDirectory()) {
                        this.enumStack.push(this.currentIterator);
                        List<File> files = getDirectoryEntries(element);
                        this.currentIterator = files.iterator();
                    } else {
                        InputStream inFileStream = Files.newInputStream(element.toPath(), new OpenOption[0]);
                        if (element.getName().endsWith(".class")) {
                            ClassFile javaClass = new ClassFile();
                            javaClass.read(inFileStream);
                            nextElement = javaClass;
                        }
                        if (inFileStream != null) {
                            inFileStream.close();
                        }
                    }
                } else if (this.enumStack.isEmpty()) {
                    break;
                } else {
                    this.currentIterator = this.enumStack.pop();
                }
            } catch (IOException e) {
                nextElement = null;
            }
        }
        return nextElement;
    }
}
