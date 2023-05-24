package javax.security.jacc;

import java.util.HashMap;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/HttpMethodSpec.class */
final class HttpMethodSpec {
    private static Object[] methodKeys = {HttpDelete.METHOD_NAME, HttpGet.METHOD_NAME, HttpHead.METHOD_NAME, HttpOptions.METHOD_NAME, HttpPost.METHOD_NAME, HttpPut.METHOD_NAME, HttpTrace.METHOD_NAME};
    private static int mapSize = methodKeys.length;
    private static HashMap methodHash = new HashMap();
    private static String allActions;
    private static int allSet;
    private static String[] methodSetArray;

    static {
        int b = 1;
        for (int i = 0; i < mapSize; i++) {
            methodHash.put(methodKeys[i], new Integer(b));
            b <<= 1;
        }
        StringBuffer allBuffer = new StringBuffer();
        for (int i2 = 0; i2 < mapSize; i2++) {
            if (i2 == 0) {
                allBuffer.append(methodKeys[i2]);
            } else {
                allBuffer.append(new StringBuffer().append(",").append(methodKeys[i2]).toString());
            }
        }
        allActions = allBuffer.toString();
        allSet = 0;
        for (int i3 = 0; i3 < mapSize; i3++) {
            allSet <<= 1;
            allSet++;
        }
        methodSetArray = new String[allSet + 1];
    }

    private HttpMethodSpec() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMethodSet(String actions) {
        if (actions == null || actions.equals("")) {
            return allSet;
        }
        return makeMethodSet(actions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMethodSet(String[] methods) {
        return (methods == null || methods.length == 0) ? allSet : makeMethodSet(methods);
    }

    private static int makeMethodSet(String actions) {
        Integer bit;
        int i = 0;
        int mSet = 0;
        int comma = 0;
        while (comma >= 0 && i < actions.length()) {
            comma = actions.indexOf(",", i);
            if (comma != 0) {
                if (comma < 0) {
                    bit = (Integer) methodHash.get(actions.substring(i));
                } else {
                    bit = (Integer) methodHash.get(actions.substring(i, comma));
                }
                if (bit == null) {
                    throw new IllegalArgumentException("illegal HTTP method");
                }
                mSet |= bit.intValue();
                i = comma + 1;
            } else {
                throw new IllegalArgumentException("illegal HTTP method Spec");
            }
        }
        return mSet;
    }

    private static int makeMethodSet(String[] methods) {
        int mSet = 0;
        for (String str : methods) {
            Integer bit = (Integer) methodHash.get(str);
            if (bit == null) {
                throw new IllegalArgumentException("illegal HTTP method");
            }
            mSet |= bit.intValue();
        }
        return mSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getActions(int methodSet) {
        if (methodSet == allSet) {
            return null;
        }
        if (methodSetArray[methodSet] == null) {
            int bitValue = 1;
            StringBuffer actions = null;
            for (int i = 0; i < mapSize; i++) {
                if ((methodSet & bitValue) == bitValue) {
                    if (actions == null) {
                        actions = new StringBuffer("");
                    } else {
                        actions.append(",");
                    }
                    actions.append((String) methodKeys[i]);
                }
                bitValue *= 2;
            }
            if (actions != null) {
                methodSetArray[methodSet] = actions.toString();
            } else {
                throw new IllegalArgumentException("invalid methodSet");
            }
        }
        return methodSetArray[methodSet];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean implies(int thisMethodSet, int thatMethodSet) {
        return (thisMethodSet & thatMethodSet) == thatMethodSet;
    }
}
