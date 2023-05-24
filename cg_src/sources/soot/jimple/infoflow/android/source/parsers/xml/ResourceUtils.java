package soot.jimple.infoflow.android.source.parsers.xml;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/parsers/xml/ResourceUtils.class */
public class ResourceUtils {
    public static InputStream getResourceStream(String filename) throws IOException {
        return soot.jimple.infoflow.util.ResourceUtils.getResourceStream(ResourceUtils.class, filename);
    }
}
