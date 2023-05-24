package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/bytecode/DeprecatedAttribute.class */
public class DeprecatedAttribute extends AttributeInfo {
    public static final String tag = "Deprecated";

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeprecatedAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public DeprecatedAttribute(ConstPool cp) {
        super(cp, "Deprecated", new byte[0]);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        return new DeprecatedAttribute(newCp);
    }
}
