package javassist;

import java.io.InputStream;
import java.net.URL;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/ClassPath.class */
public interface ClassPath {
    InputStream openClassfile(String str) throws NotFoundException;

    URL find(String str);
}
