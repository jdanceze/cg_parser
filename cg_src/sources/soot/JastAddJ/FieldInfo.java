package soot.JastAddJ;

import java.util.Iterator;
import soot.JastAddJ.Attributes;
import soot.JastAddJ.Signatures;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FieldInfo.class */
public class FieldInfo {
    private BytecodeParser p;
    String name;
    int flags;
    private FieldDescriptor fieldDescriptor;
    private Attributes.FieldAttributes attributes;

    public FieldInfo(BytecodeParser parser) {
        this.p = parser;
        this.flags = this.p.u2();
        int name_index = this.p.u2();
        this.name = ((CONSTANT_Utf8_Info) this.p.constantPool[name_index]).string();
        this.fieldDescriptor = new FieldDescriptor(this.p, this.name);
        this.attributes = new Attributes.FieldAttributes(this.p);
    }

    public BodyDecl bodyDecl() {
        FieldDeclaration f;
        if ((this.flags & 16384) != 0) {
            f = new EnumConstant(BytecodeParser.modifiers(this.flags), this.name, new List(), new List());
        } else {
            Signatures.FieldSignature s = this.attributes.fieldSignature;
            Access type = s != null ? s.fieldTypeAccess() : this.fieldDescriptor.type();
            f = new FieldDeclaration(BytecodeParser.modifiers(this.flags), type, this.name, new Opt());
        }
        if (this.attributes.constantValue() != null) {
            if (this.fieldDescriptor.isBoolean()) {
                f.setInit(this.attributes.constantValue().exprAsBoolean());
            } else {
                f.setInit(this.attributes.constantValue().expr());
            }
        }
        if (this.attributes.annotations != null) {
            Iterator iter = this.attributes.annotations.iterator();
            while (iter.hasNext()) {
                f.getModifiersNoTransform().addModifier((Modifier) iter.next());
            }
        }
        return f;
    }

    public boolean isSynthetic() {
        return this.attributes.isSynthetic();
    }
}
