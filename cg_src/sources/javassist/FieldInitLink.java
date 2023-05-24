package javassist;

import javassist.CtField;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CtClassType.java */
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/FieldInitLink.class */
public class FieldInitLink {
    FieldInitLink next = null;
    CtField field;
    CtField.Initializer init;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldInitLink(CtField f, CtField.Initializer i) {
        this.field = f;
        this.init = i;
    }
}
