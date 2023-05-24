package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/ConstantValue.class */
class ConstantValue extends Attribute {
    int index;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantValue(DataInputStream in, int nameIndex, int length) throws IOException {
        super(nameIndex, length);
        this.index = in.readUnsignedShort();
    }
}
