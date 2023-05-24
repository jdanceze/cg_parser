package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/CP.class */
public abstract class CP {
    String uniq;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void resolve(ClassEnv classEnv);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void write(ClassEnv classEnv, DataOutputStream dataOutputStream) throws IOException, jasError;

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getUniq() {
        return this.uniq;
    }
}
