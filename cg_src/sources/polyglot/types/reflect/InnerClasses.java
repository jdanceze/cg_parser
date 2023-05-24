package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/InnerClasses.class */
public class InnerClasses extends Attribute {
    Info[] classes;

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/InnerClasses$Info.class */
    static class Info {
        int classIndex;
        int outerClassIndex;
        int nameIndex;
        int modifiers;

        Info() {
        }
    }

    public InnerClasses(DataInputStream in, int nameIndex, int length) throws IOException {
        super(nameIndex, length);
        int count = in.readUnsignedShort();
        this.classes = new Info[count];
        for (int i = 0; i < count; i++) {
            this.classes[i] = new Info();
            this.classes[i].classIndex = in.readUnsignedShort();
            this.classes[i].outerClassIndex = in.readUnsignedShort();
            this.classes[i].nameIndex = in.readUnsignedShort();
            this.classes[i].modifiers = in.readUnsignedShort();
        }
    }
}
