package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InsnOperand.class */
public abstract class InsnOperand {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void write(ClassEnv classEnv, CodeAttr codeAttr, DataOutputStream dataOutputStream) throws IOException, jasError;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int size(ClassEnv classEnv, CodeAttr codeAttr) throws jasError;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void resolve(ClassEnv classEnv);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
    }
}
