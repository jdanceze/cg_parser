package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ClassCPInfo;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/ClassFile.class */
public class ClassFile {
    private static final int CLASS_MAGIC = -889275714;
    private ConstantPool constantPool;
    private String className;

    public void read(InputStream stream) throws IOException, ClassFormatError {
        DataInputStream classStream = new DataInputStream(stream);
        if (classStream.readInt() != -889275714) {
            throw new ClassFormatError("No Magic Code Found - probably not a Java class file.");
        }
        classStream.readUnsignedShort();
        classStream.readUnsignedShort();
        this.constantPool = new ConstantPool();
        this.constantPool.read(classStream);
        this.constantPool.resolve();
        classStream.readUnsignedShort();
        int thisClassIndex = classStream.readUnsignedShort();
        classStream.readUnsignedShort();
        ClassCPInfo classInfo = (ClassCPInfo) this.constantPool.getEntry(thisClassIndex);
        this.className = classInfo.getClassName();
    }

    public Vector<String> getClassRefs() {
        Vector<String> classRefs = new Vector<>();
        int size = this.constantPool.size();
        for (int i = 0; i < size; i++) {
            ConstantPoolEntry entry = this.constantPool.getEntry(i);
            if (entry != null && entry.getTag() == 7) {
                ClassCPInfo classEntry = (ClassCPInfo) entry;
                if (!classEntry.getClassName().equals(this.className)) {
                    classRefs.add(ClassFileUtils.convertSlashName(classEntry.getClassName()));
                }
            }
        }
        return classRefs;
    }

    public String getFullClassName() {
        return ClassFileUtils.convertSlashName(this.className);
    }
}
