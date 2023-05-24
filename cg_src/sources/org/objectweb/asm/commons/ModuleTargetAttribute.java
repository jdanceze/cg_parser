package org.objectweb.asm.commons;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/ModuleTargetAttribute.class */
public final class ModuleTargetAttribute extends Attribute {
    public String platform;

    public ModuleTargetAttribute(String platform) {
        super("ModuleTarget");
        this.platform = platform;
    }

    public ModuleTargetAttribute() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.objectweb.asm.Attribute
    public Attribute read(ClassReader classReader, int offset, int length, char[] charBuffer, int codeOffset, Label[] labels) {
        return new ModuleTargetAttribute(classReader.readUTF8(offset, charBuffer));
    }

    @Override // org.objectweb.asm.Attribute
    protected ByteVector write(ClassWriter classWriter, byte[] code, int codeLength, int maxStack, int maxLocals) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.platform == null ? 0 : classWriter.newUTF8(this.platform));
        return byteVector;
    }
}
