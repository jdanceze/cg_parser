package soot.jimple.infoflow.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/SootMethodRepresentationParser.class */
public class SootMethodRepresentationParser {
    private static final SootMethodRepresentationParser instance = new SootMethodRepresentationParser();
    private Pattern patternSubsigToName = null;
    private Pattern patternMethodSig = null;

    private SootMethodRepresentationParser() {
    }

    public static SootMethodRepresentationParser v() {
        return instance;
    }

    public SootMethodAndClass parseSootMethodString(String parseString) {
        if (parseString == null || parseString.isEmpty()) {
            return null;
        }
        if (!parseString.startsWith("<") || !parseString.endsWith(">")) {
            throw new IllegalArgumentException("Illegal format of " + parseString + " (should use soot method representation)");
        }
        if (this.patternMethodSig == null) {
            this.patternMethodSig = Pattern.compile("<(?<className>.*?): (?<returnType>.*?) (?<methodName>.*?)\\((?<parameters>.*?)\\)>");
        }
        Matcher matcher = this.patternMethodSig.matcher(parseString);
        if (matcher.find()) {
            String className = matcher.group("className");
            String returnType = matcher.group("returnType");
            String methodName = matcher.group("methodName");
            String paramList = matcher.group("parameters");
            return new SootMethodAndClass(methodName, className, returnType, paramList);
        }
        return null;
    }

    public HashMap<String, Set<String>> parseClassNames(Collection<String> methods, boolean subSignature) {
        String params;
        HashMap<String, Set<String>> result = new HashMap<>();
        Pattern pattern = Pattern.compile("^\\s*<(.*?):\\s*(.*?)>\\s*$");
        for (String parseString : methods) {
            Matcher matcher = pattern.matcher(parseString);
            if (matcher.find()) {
                String className = matcher.group(1);
                if (subSignature) {
                    params = matcher.group(2);
                } else {
                    params = parseString;
                }
                if (result.containsKey(className)) {
                    result.get(className).add(params);
                } else {
                    Set<String> methodList = new HashSet<>();
                    methodList.add(params);
                    result.put(className, methodList);
                }
            }
        }
        return result;
    }

    public MultiMap<String, String> parseClassNames2(Collection<String> methods, boolean subSignature) {
        String str;
        MultiMap<String, String> result = new HashMultiMap<>();
        Pattern pattern = Pattern.compile("^\\s*<(.*?):\\s*(.*?)>\\s*$");
        for (String parseString : methods) {
            Matcher matcher = pattern.matcher(parseString);
            if (matcher.find()) {
                String className = matcher.group(1);
                if (subSignature) {
                    str = matcher.group(2);
                } else {
                    str = parseString;
                }
                String params = str;
                result.put(className, params);
            }
        }
        return result;
    }

    public String getMethodNameFromSubSignature(String subSignature) {
        if (this.patternSubsigToName == null) {
            Pattern pattern = Pattern.compile("^\\s*(.+)\\s+(.+)\\((.*?)\\)\\s*$");
            this.patternSubsigToName = pattern;
        }
        Matcher matcher = this.patternSubsigToName.matcher(subSignature);
        if (!matcher.find()) {
            Pattern pattern2 = Pattern.compile("^\\s*(.+)\\((.*?)\\)\\s*$");
            this.patternSubsigToName = pattern2;
            return getMethodNameFromSubSignature(subSignature);
        }
        String method = matcher.group(matcher.groupCount() - 1);
        return method;
    }

    public String[] getParameterTypesFromSubSignature(String subSignature) {
        if (this.patternSubsigToName == null) {
            Pattern pattern = Pattern.compile("^\\s*(.+)\\s+(.+)\\((.*?)\\)\\s*$");
            this.patternSubsigToName = pattern;
        }
        Matcher matcher = this.patternSubsigToName.matcher(subSignature);
        if (!matcher.find()) {
            Pattern pattern2 = Pattern.compile("^\\s*(.+)\\((.*?)\\)\\s*$");
            this.patternSubsigToName = pattern2;
            return getParameterTypesFromSubSignature(subSignature);
        }
        String params = matcher.group(matcher.groupCount());
        if (params.equals("")) {
            return null;
        }
        return params.split("\\s*,\\s*");
    }
}
