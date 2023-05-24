package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;
import org.apache.tools.ant.util.StringUtils;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/JspNameMangler.class */
public class JspNameMangler implements JspMangler {
    public static final String[] keywords = {"assert", Jimple.ABSTRACT, "boolean", Jimple.BREAK, "byte", Jimple.CASE, Jimple.CATCH, "char", "class", "const", "continue", "default", "do", "double", "else", Jimple.EXTENDS, Jimple.FINAL, "finally", Jimple.FLOAT, "for", Jimple.GOTO, Jimple.IF, Jimple.IMPLEMENTS, "import", Jimple.INSTANCEOF, "int", "interface", "long", Jimple.NATIVE, "new", "package", Jimple.PRIVATE, Jimple.PROTECTED, Jimple.PUBLIC, "return", "short", Jimple.STATIC, "super", "switch", Jimple.SYNCHRONIZED, "this", Jimple.THROW, Jimple.THROWS, Jimple.TRANSIENT, "try", Jimple.VOID, Jimple.VOLATILE, "while"};

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.JspMangler
    public String mapJspToJavaName(File jspFile) {
        return mapJspToBaseName(jspFile) + ".java";
    }

    private String mapJspToBaseName(File jspFile) {
        char[] charArray;
        String className = stripExtension(jspFile);
        String[] strArr = keywords;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String keyword = strArr[i];
            if (!className.equals(keyword)) {
                i++;
            } else {
                className = className + "%";
                break;
            }
        }
        StringBuilder modifiedClassName = new StringBuilder(className.length());
        char firstChar = className.charAt(0);
        if (Character.isJavaIdentifierStart(firstChar)) {
            modifiedClassName.append(firstChar);
        } else {
            modifiedClassName.append(mangleChar(firstChar));
        }
        for (char subChar : className.substring(1).toCharArray()) {
            if (Character.isJavaIdentifierPart(subChar)) {
                modifiedClassName.append(subChar);
            } else {
                modifiedClassName.append(mangleChar(subChar));
            }
        }
        return modifiedClassName.toString();
    }

    private String stripExtension(File jspFile) {
        return StringUtils.removeSuffix(jspFile.getName(), ".jsp");
    }

    private static String mangleChar(char ch) {
        if (ch == File.separatorChar) {
            ch = '/';
        }
        String s = Integer.toHexString(ch);
        int nzeros = 5 - s.length();
        char[] result = new char[6];
        result[0] = '_';
        for (int i = 1; i <= nzeros; i++) {
            result[i] = '0';
        }
        int resultIndex = 0;
        for (int i2 = nzeros + 1; i2 < 6; i2++) {
            int i3 = resultIndex;
            resultIndex++;
            result[i2] = s.charAt(i3);
        }
        return new String(result);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.JspMangler
    public String mapPath(String path) {
        return null;
    }
}
