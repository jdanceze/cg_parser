package com.sun.xml.fastinfoset.sax;

import java.io.File;
import org.apache.tools.ant.taskdefs.XSLTLiaison;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/sax/SystemIdResolver.class */
public class SystemIdResolver {
    public static String getAbsoluteURIFromRelative(String localPath) {
        String urlString;
        String str;
        if (localPath == null || localPath.length() == 0) {
            return "";
        }
        String absolutePath = localPath;
        if (!isAbsolutePath(localPath)) {
            try {
                absolutePath = getAbsolutePathFromRelativePath(localPath);
            } catch (SecurityException e) {
                return "file:" + localPath;
            }
        }
        if (null != absolutePath) {
            if (absolutePath.startsWith(File.separator)) {
                str = XSLTLiaison.FILE_PROTOCOL_PREFIX + absolutePath;
            } else {
                str = "file:///" + absolutePath;
            }
            urlString = str;
        } else {
            urlString = "file:" + localPath;
        }
        return replaceChars(urlString);
    }

    private static String getAbsolutePathFromRelativePath(String relativePath) {
        return new File(relativePath).getAbsolutePath();
    }

    public static boolean isAbsoluteURI(String systemId) {
        if (systemId == null || isWindowsAbsolutePath(systemId)) {
            return false;
        }
        int fragmentIndex = systemId.indexOf(35);
        int queryIndex = systemId.indexOf(63);
        int slashIndex = systemId.indexOf(47);
        int colonIndex = systemId.indexOf(58);
        int index = systemId.length() - 1;
        if (fragmentIndex > 0) {
            index = fragmentIndex;
        }
        if (queryIndex > 0 && queryIndex < index) {
            index = queryIndex;
        }
        if (slashIndex > 0 && slashIndex < index) {
            index = slashIndex;
        }
        return colonIndex > 0 && colonIndex < index;
    }

    public static boolean isAbsolutePath(String systemId) {
        if (systemId == null) {
            return false;
        }
        File file = new File(systemId);
        return file.isAbsolute();
    }

    private static boolean isWindowsAbsolutePath(String systemId) {
        if (isAbsolutePath(systemId) && systemId.length() > 2 && systemId.charAt(1) == ':' && Character.isLetter(systemId.charAt(0))) {
            if (systemId.charAt(2) == '\\' || systemId.charAt(2) == '/') {
                return true;
            }
            return false;
        }
        return false;
    }

    private static String replaceChars(String str) {
        StringBuffer buf = new StringBuffer(str);
        int length = buf.length();
        int i = 0;
        while (i < length) {
            char currentChar = buf.charAt(i);
            if (currentChar == ' ') {
                buf.setCharAt(i, '%');
                buf.insert(i + 1, "20");
                length += 2;
                i += 2;
            } else if (currentChar == '\\') {
                buf.setCharAt(i, '/');
            }
            i++;
        }
        return buf.toString();
    }

    public static String getAbsoluteURI(String systemId) {
        int secondColonIndex;
        String absoluteURI = systemId;
        if (isAbsoluteURI(systemId)) {
            if (systemId.startsWith("file:")) {
                String str = systemId.substring(5);
                if (str != null && str.startsWith("/")) {
                    if ((str.startsWith("///") || !str.startsWith("//")) && (secondColonIndex = systemId.indexOf(58, 5)) > 0) {
                        String localPath = systemId.substring(secondColonIndex - 1);
                        try {
                            if (!isAbsolutePath(localPath)) {
                                absoluteURI = systemId.substring(0, secondColonIndex - 1) + getAbsolutePathFromRelativePath(localPath);
                            }
                        } catch (SecurityException e) {
                            return systemId;
                        }
                    }
                    return replaceChars(absoluteURI);
                }
                return getAbsoluteURIFromRelative(systemId.substring(5));
            }
            return systemId;
        }
        return getAbsoluteURIFromRelative(systemId);
    }
}
