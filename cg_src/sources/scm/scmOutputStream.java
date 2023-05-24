package scm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmOutputStream.class */
class scmOutputStream extends DataOutputStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public scmOutputStream(String path) throws IOException {
        super(new BufferedOutputStream(new FileOutputStream(path)));
    }
}
