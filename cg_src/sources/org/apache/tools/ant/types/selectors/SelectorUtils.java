package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SelectorUtils.class */
public final class SelectorUtils {
    public static final String DEEP_TREE_MATCH = "**";
    private static final SelectorUtils instance = new SelectorUtils();
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private SelectorUtils() {
    }

    public static SelectorUtils getInstance() {
        return instance;
    }

    public static boolean matchPatternStart(String pattern, String str) {
        return matchPatternStart(pattern, str, true);
    }

    public static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        String[] patDirs = tokenizePathAsArray(pattern);
        String[] strDirs = tokenizePathAsArray(str);
        return matchPatternStart(patDirs, strDirs, isCaseSensitive);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean matchPatternStart(String[] patDirs, String[] strDirs, boolean isCaseSensitive) {
        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals(DEEP_TREE_MATCH)) {
                break;
            } else if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            } else {
                patIdxStart++;
                strIdxStart++;
            }
        }
        return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
    }

    public static boolean matchPath(String pattern, String str) {
        String[] patDirs = tokenizePathAsArray(pattern);
        return matchPath(patDirs, tokenizePathAsArray(str), true);
    }

    public static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
        String[] patDirs = tokenizePathAsArray(pattern);
        return matchPath(patDirs, tokenizePathAsArray(str), isCaseSensitive);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean matchPath(String[] tokenizedPattern, String[] strDirs, boolean isCaseSensitive) {
        int patIdxStart = 0;
        int patIdxEnd = tokenizedPattern.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = tokenizedPattern[patIdxStart];
            if (patDir.equals(DEEP_TREE_MATCH)) {
                break;
            } else if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            } else {
                patIdxStart++;
                strIdxStart++;
            }
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!tokenizedPattern[i].equals(DEEP_TREE_MATCH)) {
                    return false;
                }
            }
            return true;
        } else if (patIdxStart > patIdxEnd) {
            return false;
        } else {
            while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
                String patDir2 = tokenizedPattern[patIdxEnd];
                if (patDir2.equals(DEEP_TREE_MATCH)) {
                    break;
                } else if (!match(patDir2, strDirs[strIdxEnd], isCaseSensitive)) {
                    return false;
                } else {
                    patIdxEnd--;
                    strIdxEnd--;
                }
            }
            if (strIdxStart > strIdxEnd) {
                for (int i2 = patIdxStart; i2 <= patIdxEnd; i2++) {
                    if (!tokenizedPattern[i2].equals(DEEP_TREE_MATCH)) {
                        return false;
                    }
                }
                return true;
            }
            while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                int patIdxTmp = -1;
                int i3 = patIdxStart + 1;
                while (true) {
                    if (i3 <= patIdxEnd) {
                        if (!tokenizedPattern[i3].equals(DEEP_TREE_MATCH)) {
                            i3++;
                        } else {
                            patIdxTmp = i3;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (patIdxTmp == patIdxStart + 1) {
                    patIdxStart++;
                } else {
                    int patLength = (patIdxTmp - patIdxStart) - 1;
                    int strLength = (strIdxEnd - strIdxStart) + 1;
                    int foundIdx = -1;
                    int i4 = 0;
                    while (true) {
                        if (i4 > strLength - patLength) {
                            break;
                        }
                        for (int j = 0; j < patLength; j++) {
                            String subPat = tokenizedPattern[patIdxStart + j + 1];
                            String subStr = strDirs[strIdxStart + i4 + j];
                            if (!match(subPat, subStr, isCaseSensitive)) {
                                break;
                            }
                        }
                        foundIdx = strIdxStart + i4;
                        break;
                        i4++;
                    }
                    if (foundIdx == -1) {
                        return false;
                    }
                    patIdxStart = patIdxTmp;
                    strIdxStart = foundIdx + patLength;
                }
            }
            for (int i5 = patIdxStart; i5 <= patIdxEnd; i5++) {
                if (!DEEP_TREE_MATCH.equals(tokenizedPattern[i5])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean match(String pattern, String str) {
        return match(pattern, str, true);
    }

    public static boolean match(String pattern, String str, boolean caseSensitive) {
        int j;
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        boolean containsStar = false;
        int length = patArr.length;
        int i = 0;
        while (true) {
            if (i < length) {
                if (patArr[i] != '*') {
                    i++;
                } else {
                    containsStar = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!containsStar) {
            if (patIdxEnd != strIdxEnd) {
                return false;
            }
            for (int i2 = 0; i2 <= patIdxEnd; i2++) {
                char ch = patArr[i2];
                if (ch != '?' && different(caseSensitive, ch, strArr[i2])) {
                    return false;
                }
            }
            return true;
        } else if (patIdxEnd == 0) {
            return true;
        } else {
            while (true) {
                char ch2 = patArr[patIdxStart];
                if (ch2 == '*' || strIdxStart > strIdxEnd) {
                    break;
                } else if (ch2 != '?' && different(caseSensitive, ch2, strArr[strIdxStart])) {
                    return false;
                } else {
                    patIdxStart++;
                    strIdxStart++;
                }
            }
            if (strIdxStart > strIdxEnd) {
                return allStars(patArr, patIdxStart, patIdxEnd);
            }
            while (true) {
                char ch3 = patArr[patIdxEnd];
                if (ch3 == '*' || strIdxStart > strIdxEnd) {
                    break;
                } else if (ch3 != '?' && different(caseSensitive, ch3, strArr[strIdxEnd])) {
                    return false;
                } else {
                    patIdxEnd--;
                    strIdxEnd--;
                }
            }
            if (strIdxStart > strIdxEnd) {
                return allStars(patArr, patIdxStart, patIdxEnd);
            }
            while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                int patIdxTmp = -1;
                int i3 = patIdxStart + 1;
                while (true) {
                    if (i3 <= patIdxEnd) {
                        if (patArr[i3] != '*') {
                            i3++;
                        } else {
                            patIdxTmp = i3;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (patIdxTmp == patIdxStart + 1) {
                    patIdxStart++;
                } else {
                    int patLength = (patIdxTmp - patIdxStart) - 1;
                    int strLength = (strIdxEnd - strIdxStart) + 1;
                    int foundIdx = -1;
                    int i4 = 0;
                    while (true) {
                        if (i4 > strLength - patLength) {
                            break;
                        }
                        for (j = 0; j < patLength; j = j + 1) {
                            char ch4 = patArr[patIdxStart + j + 1];
                            j = (ch4 == '?' || !different(caseSensitive, ch4, strArr[(strIdxStart + i4) + j])) ? j + 1 : 0;
                        }
                        foundIdx = strIdxStart + i4;
                        break;
                        i4++;
                    }
                    if (foundIdx == -1) {
                        return false;
                    }
                    patIdxStart = patIdxTmp;
                    strIdxStart = foundIdx + patLength;
                }
            }
            return allStars(patArr, patIdxStart, patIdxEnd);
        }
    }

    private static boolean allStars(char[] chars, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (chars[i] != '*') {
                return false;
            }
        }
        return true;
    }

    private static boolean different(boolean caseSensitive, char ch, char other) {
        return caseSensitive ? ch != other : Character.toUpperCase(ch) != Character.toUpperCase(other);
    }

    public static Vector<String> tokenizePath(String path) {
        return tokenizePath(path, File.separator);
    }

    public static Vector<String> tokenizePath(String path, String separator) {
        Vector<String> ret = new Vector<>();
        if (FileUtils.isAbsolutePath(path)) {
            String[] s = FILE_UTILS.dissect(path);
            ret.add(s[0]);
            path = s[1];
        }
        StringTokenizer st = new StringTokenizer(path, separator);
        while (st.hasMoreTokens()) {
            ret.addElement(st.nextToken());
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] tokenizePathAsArray(String path) {
        int count;
        String root = null;
        if (FileUtils.isAbsolutePath(path)) {
            String[] s = FILE_UTILS.dissect(path);
            root = s[0];
            path = s[1];
        }
        char sep = File.separatorChar;
        int start = 0;
        int len = path.length();
        int count2 = 0;
        for (int pos = 0; pos < len; pos++) {
            if (path.charAt(pos) == sep) {
                if (pos != start) {
                    count2++;
                }
                start = pos + 1;
            }
        }
        if (len != start) {
            count2++;
        }
        String[] l = new String[count2 + (root == null ? 0 : 1)];
        if (root != null) {
            l[0] = root;
            count = 1;
        } else {
            count = 0;
        }
        int start2 = 0;
        for (int pos2 = 0; pos2 < len; pos2++) {
            if (path.charAt(pos2) == sep) {
                if (pos2 != start2) {
                    String tok = path.substring(start2, pos2);
                    int i = count;
                    count++;
                    l[i] = tok;
                }
                start2 = pos2 + 1;
            }
        }
        if (len != start2) {
            String tok2 = path.substring(start2);
            l[count] = tok2;
        }
        return l;
    }

    public static boolean isOutOfDate(File src, File target, int granularity) {
        return src.exists() && (!target.exists() || src.lastModified() - ((long) granularity) > target.lastModified());
    }

    public static boolean isOutOfDate(Resource src, Resource target, int granularity) {
        return isOutOfDate(src, target, granularity);
    }

    public static boolean isOutOfDate(Resource src, Resource target, long granularity) {
        long sourceLastModified = src.getLastModified();
        long targetLastModified = target.getLastModified();
        return src.isExists() && (sourceLastModified == 0 || targetLastModified == 0 || sourceLastModified - granularity > targetLastModified);
    }

    public static String removeWhitespace(String input) {
        StringBuilder result = new StringBuilder();
        if (input != null) {
            StringTokenizer st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                result.append(st.nextToken());
            }
        }
        return result.toString();
    }

    public static boolean hasWildcards(String input) {
        return input.contains("*") || input.contains(TypeDescription.Generic.OfWildcardType.SYMBOL);
    }

    public static String rtrimWildcardTokens(String input) {
        return new TokenizedPattern(input).rtrimWildcardTokens().toString();
    }
}
