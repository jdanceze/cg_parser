package org.objectweb.asm.commons;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/ModuleResolutionAttribute.class */
public final class ModuleResolutionAttribute extends Attribute {
    public static final int RESOLUTION_DO_NOT_RESOLVE_BY_DEFAULT = 1;
    public static final int RESOLUTION_WARN_DEPRECATED = 2;
    public static final int RESOLUTION_WARN_DEPRECATED_FOR_REMOVAL = 4;
    public static final int RESOLUTION_WARN_INCUBATING = 8;
    public int resolution;

    public ModuleResolutionAttribute(int resolution) {
        super("ModuleResolution");
        this.resolution = resolution;
    }

    public ModuleResolutionAttribute() {
        this(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.objectweb.asm.Attribute
    public Attribute read(ClassReader classReader, int offset, int length, char[] charBuffer, int codeOffset, Label[] labels) {
        return new ModuleResolutionAttribute(classReader.readUnsignedShort(offset));
    }

    @Override // org.objectweb.asm.Attribute
    protected ByteVector write(ClassWriter classWriter, byte[] code, int codeLength, int maxStack, int maxLocals) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.resolution);
        return byteVector;
    }
}
