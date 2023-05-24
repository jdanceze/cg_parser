package soot.jimple.infoflow.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ArgParser.class */
public class ArgParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static String METHODKEYWORD = "-entrypoints";
    public static String SOURCEKEYWORD = "-sources";
    public static String SINKKEYWORD = "-sinks";
    public static String PATHKEYWORD = "-path";

    public List<List<String>> parseClassArguments(String[] args) {
        List<String> argList = Arrays.asList(args);
        List<String> sourceList = new ArrayList<>();
        List<String> sinkList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        if (argList.contains(METHODKEYWORD)) {
            List<String> ePointList = getListToAttribute(argList, METHODKEYWORD);
            if (argList.contains(SOURCEKEYWORD)) {
                sourceList = getListToAttribute(argList, SOURCEKEYWORD);
            }
            if (argList.contains(SINKKEYWORD)) {
                sinkList = getListToAttribute(argList, SINKKEYWORD);
            }
            if (argList.contains(PATHKEYWORD)) {
                pathList = getListToAttribute(argList, PATHKEYWORD);
            }
            List<List<String>> resultlist = new ArrayList<>();
            resultlist.add(ePointList);
            resultlist.add(sourceList);
            resultlist.add(sinkList);
            resultlist.add(pathList);
            return resultlist;
        }
        this.logger.error("parameter '" + METHODKEYWORD + "' is missing or has not enough arguments!");
        return null;
    }

    private List<String> getListToAttribute(List<String> argList, String attr) {
        List<String> result = new ArrayList<>();
        if (argList.indexOf(attr) + 1 < argList.size() && !argList.get(argList.indexOf(attr) + 1).startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
            for (int position = argList.indexOf(attr); position + 1 < argList.size() && !argList.get(position + 1).startsWith(HelpFormatter.DEFAULT_OPT_PREFIX); position++) {
                result.add(argList.get(position + 1));
            }
        }
        return result;
    }
}
