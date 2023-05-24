package org.apache.tools.ant.taskdefs.optional.depend;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/ClassFileIterator.class */
public interface ClassFileIterator extends Iterable<ClassFile> {
    ClassFile getNextClassFile();

    @Override // java.lang.Iterable
    default Iterator<ClassFile> iterator() {
        return new Iterator<ClassFile>() { // from class: org.apache.tools.ant.taskdefs.optional.depend.ClassFileIterator.1
            ClassFile next;

            {
                this.next = ClassFileIterator.this.getNextClassFile();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public ClassFile next() {
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                try {
                    return this.next;
                } finally {
                    this.next = ClassFileIterator.this.getNextClassFile();
                }
            }
        };
    }
}
