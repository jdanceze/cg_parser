package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/ConstantPool.class */
public class ConstantPool {
    private final List<ConstantPoolEntry> entries = new ArrayList();
    private final Map<String, Integer> utf8Indexes = new HashMap();

    public ConstantPool() {
        this.entries.add(null);
    }

    public void read(DataInputStream classStream) throws IOException {
        int numEntries = classStream.readUnsignedShort();
        int i = 1;
        while (i < numEntries) {
            ConstantPoolEntry nextEntry = ConstantPoolEntry.readEntry(classStream);
            i += nextEntry.getNumEntries();
            addEntry(nextEntry);
        }
    }

    public int size() {
        return this.entries.size();
    }

    public int addEntry(ConstantPoolEntry entry) {
        int index = this.entries.size();
        this.entries.add(entry);
        int numSlots = entry.getNumEntries();
        for (int j = 0; j < numSlots - 1; j++) {
            this.entries.add(null);
        }
        if (entry instanceof Utf8CPInfo) {
            Utf8CPInfo utf8Info = (Utf8CPInfo) entry;
            this.utf8Indexes.put(utf8Info.getValue(), Integer.valueOf(index));
        }
        return index;
    }

    public void resolve() {
        for (ConstantPoolEntry poolInfo : this.entries) {
            if (poolInfo != null && !poolInfo.isResolved()) {
                poolInfo.resolve(this);
            }
        }
    }

    public ConstantPoolEntry getEntry(int index) {
        return this.entries.get(index);
    }

    public int getUTF8Entry(String value) {
        int index = -1;
        Integer indexInteger = this.utf8Indexes.get(value);
        if (indexInteger != null) {
            index = indexInteger.intValue();
        }
        return index;
    }

    public int getClassEntry(String className) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof ClassCPInfo) {
                ClassCPInfo classinfo = (ClassCPInfo) element;
                if (classinfo.getClassName().equals(className)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public int getConstantEntry(Object constantValue) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof ConstantCPInfo) {
                ConstantCPInfo constantEntry = (ConstantCPInfo) element;
                if (constantEntry.getValue().equals(constantValue)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public int getMethodRefEntry(String methodClassName, String methodName, String methodType) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof MethodRefCPInfo) {
                MethodRefCPInfo methodRefEntry = (MethodRefCPInfo) element;
                if (methodRefEntry.getMethodClassName().equals(methodClassName) && methodRefEntry.getMethodName().equals(methodName) && methodRefEntry.getMethodType().equals(methodType)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public int getInterfaceMethodRefEntry(String interfaceMethodClassName, String interfaceMethodName, String interfaceMethodType) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof InterfaceMethodRefCPInfo) {
                InterfaceMethodRefCPInfo interfaceMethodRefEntry = (InterfaceMethodRefCPInfo) element;
                if (interfaceMethodRefEntry.getInterfaceMethodClassName().equals(interfaceMethodClassName) && interfaceMethodRefEntry.getInterfaceMethodName().equals(interfaceMethodName) && interfaceMethodRefEntry.getInterfaceMethodType().equals(interfaceMethodType)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public int getFieldRefEntry(String fieldClassName, String fieldName, String fieldType) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof FieldRefCPInfo) {
                FieldRefCPInfo fieldRefEntry = (FieldRefCPInfo) element;
                if (fieldRefEntry.getFieldClassName().equals(fieldClassName) && fieldRefEntry.getFieldName().equals(fieldName) && fieldRefEntry.getFieldType().equals(fieldType)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public int getNameAndTypeEntry(String name, String type) {
        int index = -1;
        int size = this.entries.size();
        for (int i = 0; i < size && index == -1; i++) {
            Object element = this.entries.get(i);
            if (element instanceof NameAndTypeCPInfo) {
                NameAndTypeCPInfo nameAndTypeEntry = (NameAndTypeCPInfo) element;
                if (nameAndTypeEntry.getName().equals(name) && nameAndTypeEntry.getType().equals(type)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public String toString() {
        return (String) IntStream.range(0, this.entries.size()).mapToObj(i -> {
            return String.format("[%d] = %s", Integer.valueOf(i), getEntry(i));
        }).collect(Collectors.joining("\n", "\n", "\n"));
    }
}
