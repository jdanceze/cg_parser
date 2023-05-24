package soot.jimple.infoflow.android.data.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Instruction;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/parsers/CSVPermissionMethodParser.class */
public class CSVPermissionMethodParser implements ISourceSinkDefinitionProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Set<ISourceSinkDefinition> sourceList = null;
    private Set<ISourceSinkDefinition> sinkList = null;
    private Set<ISourceSinkDefinition> neitherList = null;
    private static final int INITIAL_SET_SIZE = 10000;
    private final String fileName;

    public CSVPermissionMethodParser(String fileName) {
        this.fileName = fileName;
    }

    public void parse() {
        String methodName;
        String[] split;
        String[] split2;
        this.sourceList = new HashSet(10000);
        this.sinkList = new HashSet(10000);
        this.neitherList = new HashSet(10000);
        BufferedReader rdr = null;
        try {
            try {
                rdr = new BufferedReader(new FileReader(this.fileName));
                boolean firstLine = true;
                while (true) {
                    String line = rdr.readLine();
                    if (line == null) {
                        break;
                    } else if (firstLine) {
                        firstLine = false;
                    } else {
                        firstLine = false;
                        String[] fields = line.split("\t");
                        if (fields.length < 1) {
                            this.logger.warn("Found invalid line: %s", line);
                        } else {
                            List<String> methodParams = new ArrayList<>();
                            Set<String> permissions = new HashSet<>();
                            try {
                                if (fields[0].contains(")")) {
                                    methodName = fields[0].substring(0, fields[0].indexOf("("));
                                } else {
                                    methodName = fields[0];
                                }
                                String className = methodName.substring(0, methodName.lastIndexOf("."));
                                String methodName2 = methodName.substring(methodName.lastIndexOf(".") + 1);
                                if (fields[0].contains("(")) {
                                    String parameters = fields[0].substring(fields[0].indexOf("(") + 1);
                                    for (String p : parameters.substring(0, parameters.indexOf(")")).split(",")) {
                                        methodParams.add(p);
                                    }
                                }
                                String perm = (fields.length > 1 ? fields[1] : "").replaceAll(" and ", Instruction.argsep).replaceAll(" or ", Instruction.argsep);
                                if (perm.contains(".")) {
                                    perm = perm.substring(perm.lastIndexOf(".") + 1);
                                }
                                for (String p2 : perm.split(Instruction.argsep)) {
                                    permissions.add(p2);
                                }
                                AndroidMethod method = new AndroidMethod(methodName2, methodParams, "", className, permissions);
                                if (method.getSourceSinkType().isSource()) {
                                    this.sourceList.add(new MethodSourceSinkDefinition(method));
                                } else if (method.getSourceSinkType().isSink()) {
                                    this.sinkList.add(new MethodSourceSinkDefinition(method));
                                } else if (method.getSourceSinkType() == SourceSinkType.Neither) {
                                    this.neitherList.add(new MethodSourceSinkDefinition(method));
                                }
                            } catch (StringIndexOutOfBoundsException ex) {
                                this.logger.warn("Could not parse line: " + line, (Throwable) ex);
                            }
                        }
                    }
                }
                if (rdr != null) {
                    try {
                        rdr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (rdr != null) {
                    try {
                        rdr.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException ex2) {
            ex2.printStackTrace();
            if (rdr != null) {
                try {
                    rdr.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSources() {
        if (this.sourceList == null || this.sinkList == null) {
            parse();
        }
        return this.sourceList;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSinks() {
        if (this.sourceList == null || this.sinkList == null) {
            parse();
        }
        return this.sinkList;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getAllMethods() {
        if (this.sourceList == null || this.sinkList == null) {
            parse();
        }
        Set<ISourceSinkDefinition> sourcesSinks = new HashSet<>(this.sourceList.size() + this.sinkList.size() + this.neitherList.size());
        sourcesSinks.addAll(this.sourceList);
        sourcesSinks.addAll(this.sinkList);
        sourcesSinks.addAll(this.neitherList);
        return sourcesSinks;
    }
}
