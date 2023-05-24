package soot.jimple.infoflow.android.data.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Instruction;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.data.SootFieldAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.FieldSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/parsers/PermissionMethodParser.class */
public class PermissionMethodParser implements ISourceSinkDefinitionProvider {
    private static final int INITIAL_SET_SIZE = 10000;
    private List<String> data;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, AndroidMethod> methods = null;
    private Map<String, SootFieldAndClass> fields = null;
    private Set<ISourceSinkDefinition> sourceList = null;
    private Set<ISourceSinkDefinition> sinkList = null;
    private Set<ISourceSinkDefinition> neitherList = null;
    private final String regex = "^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>\\s*(.*?)(\\s+->\\s+(.*))?$";
    private final String regexNoRet = "^<(.+):\\s*(.+)\\s*\\((.*)\\)>\\s*(.*?)?(\\s+->\\s+(.*))?$";
    private final String fieldRegex = "^<(.+):\\s*(.+)\\s+([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*>\\s*(.*?)(\\s+->\\s+(.*))?$";

    static {
        $assertionsDisabled = !PermissionMethodParser.class.desiredAssertionStatus();
    }

    public static PermissionMethodParser fromFile(String fileName) throws IOException {
        PermissionMethodParser pmp = new PermissionMethodParser();
        pmp.readFile(fileName);
        return pmp;
    }

    public static PermissionMethodParser fromStream(InputStream input) throws IOException {
        PermissionMethodParser pmp = new PermissionMethodParser();
        pmp.readReader(new InputStreamReader(input));
        return pmp;
    }

    public static PermissionMethodParser fromStringList(List<String> data) throws IOException {
        PermissionMethodParser pmp = new PermissionMethodParser(data);
        return pmp;
    }

    private PermissionMethodParser() {
    }

    private PermissionMethodParser(List<String> data) {
        this.data = data;
    }

    private void readFile(String fileName) throws IOException {
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            readReader(fr);
            if (fr != null) {
                fr.close();
            }
        } catch (Throwable th) {
            if (fr != null) {
                fr.close();
            }
            throw th;
        }
    }

    private void readReader(Reader r) throws IOException {
        this.data = new ArrayList();
        BufferedReader br = new BufferedReader(r);
        while (true) {
            try {
                String line = br.readLine();
                if (line != null) {
                    this.data.add(line);
                } else {
                    return;
                }
            } finally {
                br.close();
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

    private void parse() {
        this.fields = new HashMap(10000);
        this.methods = new HashMap(10000);
        this.sourceList = new HashSet(10000);
        this.sinkList = new HashSet(10000);
        this.neitherList = new HashSet(10000);
        Pattern p = Pattern.compile("^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>\\s*(.*?)(\\s+->\\s+(.*))?$");
        Pattern pNoRet = Pattern.compile("^<(.+):\\s*(.+)\\s*\\((.*)\\)>\\s*(.*?)?(\\s+->\\s+(.*))?$");
        Pattern fieldPattern = Pattern.compile("^<(.+):\\s*(.+)\\s+([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*>\\s*(.*?)(\\s+->\\s+(.*))?$");
        for (String line : this.data) {
            if (!line.isEmpty() && !line.startsWith("%")) {
                Matcher fieldMatch = fieldPattern.matcher(line);
                if (fieldMatch.find()) {
                    createField(fieldMatch);
                } else {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        createMethod(m);
                    } else {
                        Matcher mNoRet = pNoRet.matcher(line);
                        if (mNoRet.find()) {
                            createMethod(mNoRet);
                        } else {
                            this.logger.warn(String.format("Line does not match: %s", line));
                        }
                    }
                }
            }
        }
        for (AndroidMethod am : this.methods.values()) {
            MethodSourceSinkDefinition singleMethod = new MethodSourceSinkDefinition(am);
            if (am.getSourceSinkType().isSource()) {
                this.sourceList.add(singleMethod);
            }
            if (am.getSourceSinkType().isSink()) {
                this.sinkList.add(singleMethod);
            }
            if (am.getSourceSinkType() == SourceSinkType.Neither) {
                this.neitherList.add(singleMethod);
            }
        }
        for (SootFieldAndClass sootField : this.fields.values()) {
            FieldSourceSinkDefinition fieldDefinition = new FieldSourceSinkDefinition(sootField.getSignature());
            if (sootField.getSourceSinkType().isSource()) {
                this.sourceList.add(fieldDefinition);
            }
            if (sootField.getSourceSinkType().isSink()) {
                this.sinkList.add(fieldDefinition);
            }
            if (sootField.getSourceSinkType() == SourceSinkType.Neither) {
                this.neitherList.add(fieldDefinition);
            }
        }
    }

    private AndroidMethod createMethod(Matcher m) {
        AndroidMethod am = parseMethod(m, true);
        AndroidMethod oldMethod = this.methods.get(am.getSignature());
        if (oldMethod != null) {
            oldMethod.setSourceSinkType(oldMethod.getSourceSinkType().addType(am.getSourceSinkType()));
            return oldMethod;
        }
        this.methods.put(am.getSignature(), am);
        return am;
    }

    private AndroidMethod parseMethod(Matcher m, boolean hasReturnType) {
        String[] split;
        String[] split2;
        if ($assertionsDisabled || !(m.group(1) == null || m.group(2) == null || m.group(3) == null || m.group(4) == null)) {
            int groupIdx = 1 + 1;
            String className = m.group(1).trim();
            String returnType = "";
            if (hasReturnType) {
                groupIdx++;
                returnType = m.group(groupIdx).trim();
            }
            int i = groupIdx;
            int groupIdx2 = groupIdx + 1;
            String methodName = m.group(i).trim();
            List<String> methodParameters = new ArrayList<>();
            int groupIdx3 = groupIdx2 + 1;
            String params = m.group(groupIdx2).trim();
            if (!params.isEmpty()) {
                for (String parameter : params.split(",")) {
                    methodParameters.add(parameter.trim());
                }
            }
            String classData = "";
            String permData = "";
            Set<String> permissions = null;
            if (groupIdx3 < m.groupCount() && m.group(groupIdx3) != null) {
                permData = m.group(groupIdx3);
                if (permData.contains("->")) {
                    classData = permData.replace("->", "").trim();
                    permData = "";
                }
                groupIdx3++;
            }
            if (!permData.isEmpty()) {
                permissions = new HashSet<>();
                for (String permission : permData.split(Instruction.argsep)) {
                    permissions.add(permission);
                }
            }
            AndroidMethod singleMethod = new AndroidMethod(methodName, methodParameters, returnType, className, permissions);
            if (classData.isEmpty() && m.group(groupIdx3) != null) {
                classData = m.group(groupIdx3).replace("->", "").trim();
                int i2 = groupIdx3 + 1;
            }
            if (!classData.isEmpty()) {
                for (String target : classData.split("\\s")) {
                    String target2 = target.trim();
                    if (target2.contains("|")) {
                        target2 = target2.substring(target2.indexOf(124));
                    }
                    if (!target2.isEmpty() && !target2.startsWith("|")) {
                        if (target2.equals("_SOURCE_")) {
                            singleMethod.setSourceSinkType(SourceSinkType.Source);
                        } else if (target2.equals("_SINK_")) {
                            singleMethod.setSourceSinkType(SourceSinkType.Sink);
                        } else if (target2.equals("_NONE_")) {
                            singleMethod.setSourceSinkType(SourceSinkType.Neither);
                        } else if (target2.equals("_BOTH_")) {
                            singleMethod.setSourceSinkType(SourceSinkType.Both);
                        } else {
                            throw new RuntimeException("error in target definition: " + target2);
                        }
                    }
                }
            }
            return singleMethod;
        }
        throw new AssertionError();
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

    private SootFieldAndClass createField(Matcher m) {
        SootFieldAndClass sootField = parseField(m);
        SootFieldAndClass oldField = this.fields.get(sootField.getSignature());
        if (oldField != null) {
            oldField.setSourceSinkType(oldField.getSourceSinkType().addType(sootField.getSourceSinkType()));
            return oldField;
        }
        this.fields.put(sootField.getSignature(), sootField);
        return sootField;
    }

    private SootFieldAndClass parseField(Matcher m) {
        if ($assertionsDisabled || !(m.group(1) == null || m.group(2) == null || m.group(3) == null || m.group(4) == null)) {
            int groupIdx = 1 + 1;
            String className = m.group(1).trim();
            int groupIdx2 = groupIdx + 1;
            String fieldType = m.group(groupIdx).trim();
            String fieldName = m.group(groupIdx2).trim();
            String sourceSinkTypeString = m.group(groupIdx2 + 1).replace("->", "").replace("_", "").trim();
            SourceSinkType sourceSinkType = SourceSinkType.fromString(sourceSinkTypeString);
            SootFieldAndClass sootFieldAndClass = new SootFieldAndClass(fieldName, className, fieldType, sourceSinkType);
            return sootFieldAndClass;
        }
        throw new AssertionError();
    }
}
