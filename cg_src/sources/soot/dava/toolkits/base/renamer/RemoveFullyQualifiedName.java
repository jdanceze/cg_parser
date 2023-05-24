package soot.dava.toolkits.base.renamer;

import java.util.Iterator;
import soot.ArrayType;
import soot.Type;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/renamer/RemoveFullyQualifiedName.class */
public class RemoveFullyQualifiedName {
    public static boolean containsMultiple(Iterator it, String qualifiedName, Type t) {
        if (t != null && (t instanceof ArrayType) && qualifiedName.indexOf(91) >= 0) {
            qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf(91));
        }
        String className = getClassName(qualifiedName);
        int count = 0;
        while (it.hasNext()) {
            String tempName = getClassName((String) it.next());
            if (tempName.equals(className)) {
                count++;
            }
        }
        if (count > 1) {
            return true;
        }
        return false;
    }

    public static String getClassName(String qualifiedName) {
        if (qualifiedName.lastIndexOf(46) > -1) {
            return qualifiedName.substring(qualifiedName.lastIndexOf(46) + 1);
        }
        return qualifiedName;
    }

    public static String getReducedName(IterableSet importList, String qualifiedName, Type t) {
        if (!containsMultiple(importList.iterator(), qualifiedName, t)) {
            return getClassName(qualifiedName);
        }
        return qualifiedName;
    }
}
