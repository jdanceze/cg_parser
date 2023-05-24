package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/Jasper41Mangler.class */
public class Jasper41Mangler implements JspMangler {
    @Override // org.apache.tools.ant.taskdefs.optional.jsp.JspMangler
    public String mapJspToJavaName(File jspFile) {
        char[] charArray;
        String jspUri = jspFile.getAbsolutePath();
        int start = jspUri.lastIndexOf(File.separatorChar) + 1;
        StringBuilder modifiedClassName = new StringBuilder(jspUri.length() - start);
        if (!Character.isJavaIdentifierStart(jspUri.charAt(start)) || jspUri.charAt(start) == '_') {
            modifiedClassName.append('_');
        }
        for (char ch : jspUri.substring(start).toCharArray()) {
            if (Character.isJavaIdentifierPart(ch)) {
                modifiedClassName.append(ch);
            } else if (ch == '.') {
                modifiedClassName.append('_');
            } else {
                modifiedClassName.append(mangleChar(ch));
            }
        }
        return modifiedClassName.toString();
    }

    private static String mangleChar(char ch) {
        String s = Integer.toHexString(ch);
        int nzeros = 5 - s.length();
        char[] result = new char[6];
        result[0] = '_';
        for (int i = 1; i <= nzeros; i++) {
            result[i] = '0';
        }
        int i2 = nzeros + 1;
        int j = 0;
        while (i2 < 6) {
            result[i2] = s.charAt(j);
            i2++;
            j++;
        }
        return new String(result);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.JspMangler
    public String mapPath(String path) {
        return null;
    }
}
