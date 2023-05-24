package soot.jimple.infoflow.android.data.parsers;

import java.io.IOException;
import java.util.Set;
import soot.jimple.infoflow.android.data.AndroidMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/parsers/IPermissionMethodParser.class */
public interface IPermissionMethodParser {
    Set<AndroidMethod> parse() throws IOException;
}
