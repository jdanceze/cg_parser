package soot.jimple.infoflow.android.data.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/parsers/PScoutPermissionMethodParser.class */
public class PScoutPermissionMethodParser implements ISourceSinkDefinitionProvider {
    private static final int INITIAL_SET_SIZE = 10000;
    private String fileName;
    private Reader reader;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Set<ISourceSinkDefinition> sourceList = null;
    private Set<ISourceSinkDefinition> sinkList = null;
    private Set<ISourceSinkDefinition> neitherList = null;
    private Map<String, CategoryDefinition> categories = new HashMap();
    private final String regex = "^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>.+?(->.+)?$";
    private final boolean SET_IMPLICIT_SOURCE_TO_SOURCE = false;
    private final boolean SET_INDIRECT_SINK_TO_SINK = false;

    static {
        $assertionsDisabled = !PScoutPermissionMethodParser.class.desiredAssertionStatus();
    }

    public PScoutPermissionMethodParser(String filename) {
        this.fileName = filename;
        initializeCategoryMap();
    }

    public PScoutPermissionMethodParser(Reader reader) {
        this.reader = reader;
        initializeCategoryMap();
    }

    private void initializeCategoryMap() {
        this.categories.put("_NO_CATEGORY_", new CategoryDefinition(CategoryDefinition.CATEGORY.NO_CATEGORY));
        this.categories.put("_HARDWARE_INFO_", new CategoryDefinition(CategoryDefinition.CATEGORY.HARDWARE_INFO));
        this.categories.put("_NFC_", new CategoryDefinition(CategoryDefinition.CATEGORY.NFC));
        this.categories.put("_PHONE_CONNECTION_", new CategoryDefinition(CategoryDefinition.CATEGORY.PHONE_CONNECTION));
        this.categories.put("_INTER_APP_COMMUNICATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.INTER_APP_COMMUNICATION));
        this.categories.put("_VOIP_", new CategoryDefinition(CategoryDefinition.CATEGORY.VOIP));
        this.categories.put("_CONTACT_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.CONTACT_INFORMATION));
        this.categories.put("_UNIQUE_IDENTIFIER_", new CategoryDefinition(CategoryDefinition.CATEGORY.UNIQUE_IDENTIFIER));
        this.categories.put("_PHONE_STATE_", new CategoryDefinition(CategoryDefinition.CATEGORY.PHONE_STATE));
        this.categories.put("_SYSTEM_SETTINGS_", new CategoryDefinition(CategoryDefinition.CATEGORY.SYSTEM_SETTINGS));
        this.categories.put("_LOCATION_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.LOCATION_INFORMATION));
        this.categories.put("_NETWORK_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.NETWORK_INFORMATION));
        this.categories.put("_EMAIL_", new CategoryDefinition(CategoryDefinition.CATEGORY.EMAIL));
        this.categories.put("_SMS_MMS_", new CategoryDefinition(CategoryDefinition.CATEGORY.SMS_MMS));
        this.categories.put("_CALENDAR_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.CALENDAR_INFORMATION));
        this.categories.put("_ACCOUNT_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.ACCOUNT_INFORMATION));
        this.categories.put("_BLUETOOTH_", new CategoryDefinition(CategoryDefinition.CATEGORY.BLUETOOTH));
        this.categories.put("_ACCOUNT_SETTINGS_", new CategoryDefinition(CategoryDefinition.CATEGORY.ACCOUNT_SETTINGS));
        this.categories.put("_VIDEO_", new CategoryDefinition(CategoryDefinition.CATEGORY.VIDEO));
        this.categories.put("_AUDIO_", new CategoryDefinition(CategoryDefinition.CATEGORY.AUDIO));
        this.categories.put("_SYNCHRONIZATION_DATA_", new CategoryDefinition(CategoryDefinition.CATEGORY.SYNCHRONIZATION_DATA));
        this.categories.put("_NETWORK_", new CategoryDefinition(CategoryDefinition.CATEGORY.NETWORK));
        this.categories.put("_EMAIL_SETTINGS_", new CategoryDefinition(CategoryDefinition.CATEGORY.EMAIL_SETTINGS));
        this.categories.put("_EMAIL_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.EMAIL_INFORMATION));
        this.categories.put("_IMAGE_", new CategoryDefinition(CategoryDefinition.CATEGORY.IMAGE));
        this.categories.put("_FILE_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.FILE_INFORMATION));
        this.categories.put("_BLUETOOTH_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.BLUETOOTH_INFORMATION));
        this.categories.put("_BROWSER_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.BROWSER_INFORMATION));
        this.categories.put("_FILE_", new CategoryDefinition(CategoryDefinition.CATEGORY.FILE));
        this.categories.put("_VOIP_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.VOIP_INFORMATION));
        this.categories.put("_DATABASE_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.DATABASE_INFORMATION));
        this.categories.put("_PHONE_INFORMATION_", new CategoryDefinition(CategoryDefinition.CATEGORY.PHONE_INFORMATION));
        this.categories.put("_LOG_", new CategoryDefinition(CategoryDefinition.CATEGORY.LOG));
    }

    private void parse() {
        this.sourceList = new HashSet(10000);
        this.sinkList = new HashSet(10000);
        this.neitherList = new HashSet(10000);
        BufferedReader rdr = readFile();
        Pattern p = Pattern.compile("^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>.+?(->.+)?$");
        String currentPermission = null;
        while (true) {
            try {
                String line = rdr.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith("Permission:")) {
                    currentPermission = line.substring(11);
                } else {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        parseMethod(m, currentPermission);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (rdr != null) {
            rdr.close();
        }
    }

    private void addToList(Set<ISourceSinkDefinition> sourceList, MethodSourceSinkDefinition def, String currentPermission) {
        if (!sourceList.add(def)) {
            for (ISourceSinkDefinition ssdef : sourceList) {
                if (ssdef instanceof MethodSourceSinkDefinition) {
                    MethodSourceSinkDefinition mssdef = (MethodSourceSinkDefinition) ssdef;
                    SootMethodAndClass singleMethod = def.getMethod();
                    if ((singleMethod instanceof AndroidMethod) && mssdef.getMethod().equals(singleMethod)) {
                        ((AndroidMethod) singleMethod).addPermission(currentPermission);
                        return;
                    }
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

    /* JADX WARN: Multi-variable type inference failed */
    private BufferedReader readFile() {
        Reader r;
        BufferedReader br = null;
        try {
            if (this.reader != null) {
                r = this.reader;
            } else {
                r = new FileReader(this.fileName);
            }
            br = new BufferedReader(r);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return br;
    }

    private MethodSourceSinkDefinition parseMethod(Matcher m, String currentPermission) {
        String[] split;
        String[] split2;
        if ($assertionsDisabled || !(m.group(1) == null || m.group(2) == null || m.group(3) == null || m.group(4) == null)) {
            int groupIdx = 1 + 1;
            String className = m.group(1).trim();
            int groupIdx2 = groupIdx + 1;
            String returnType = m.group(groupIdx).trim();
            int groupIdx3 = groupIdx2 + 1;
            String methodName = m.group(groupIdx2).trim();
            List<String> methodParameters = new ArrayList<>();
            int i = groupIdx3 + 1;
            String params = m.group(groupIdx3).trim();
            if (!params.isEmpty()) {
                for (String parameter : params.split(",")) {
                    methodParameters.add(parameter.trim());
                }
            }
            Set<String> permissions = null;
            if (currentPermission != null) {
                permissions = new HashSet<>();
                permissions.add(currentPermission);
            }
            AndroidMethod singleMethod = new AndroidMethod(methodName, methodParameters, returnType, className, permissions);
            MethodSourceSinkDefinition sourceSinkDef = new MethodSourceSinkDefinition(singleMethod);
            if (m.group(5) != null) {
                String targets = m.group(5).substring(3);
                for (String target : targets.split(Instruction.argsep)) {
                    if (target.startsWith("_SOURCE_")) {
                        singleMethod.setSourceSinkType(SourceSinkType.Source);
                        if (target.contains("|")) {
                            String cat = target.substring(target.indexOf(124) + 1);
                            sourceSinkDef.setCategory(returnCorrectCategory(cat));
                        }
                    } else if (target.startsWith("_SINK_")) {
                        singleMethod.setSourceSinkType(SourceSinkType.Sink);
                        if (target.contains("|")) {
                            String cat2 = target.substring(target.indexOf(124) + 1);
                            sourceSinkDef.setCategory(returnCorrectCategory(cat2));
                        }
                    } else if (target.equals("_NONE_")) {
                        singleMethod.setSourceSinkType(SourceSinkType.Neither);
                    } else if (target.startsWith("_IMPSOURCE_")) {
                        singleMethod.setSourceSinkType(SourceSinkType.Neither);
                    } else if (target.startsWith("_INDSINK_")) {
                        singleMethod.setSourceSinkType(SourceSinkType.Neither);
                    } else if (target.equals("_IGNORE_")) {
                        return null;
                    } else {
                        if (target.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                            String cat3 = target.substring(target.indexOf(124) + 1);
                            sourceSinkDef.setCategory(returnCorrectCategory(cat3));
                        } else {
                            throw new RuntimeException("error in target definition");
                        }
                    }
                }
            }
            if (singleMethod != null) {
                if (singleMethod.getSourceSinkType().isSource()) {
                    addToList(this.sourceList, sourceSinkDef, currentPermission);
                } else if (singleMethod.getSourceSinkType().isSink()) {
                    addToList(this.sinkList, sourceSinkDef, currentPermission);
                } else if (singleMethod.getSourceSinkType() == SourceSinkType.Neither) {
                    addToList(this.neitherList, sourceSinkDef, currentPermission);
                }
            }
            return sourceSinkDef;
        }
        throw new AssertionError();
    }

    private CategoryDefinition returnCorrectCategory(String category) {
        CategoryDefinition def = this.categories.get(category);
        if (def == null) {
            throw new RuntimeException("The category -" + category + "- is not supported!");
        }
        return def;
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
