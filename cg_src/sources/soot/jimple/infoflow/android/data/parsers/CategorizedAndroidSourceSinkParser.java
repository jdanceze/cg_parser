package soot.jimple.infoflow.android.data.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/parsers/CategorizedAndroidSourceSinkParser.class */
public class CategorizedAndroidSourceSinkParser {
    private Set<CategoryDefinition> categories;
    private final String fileName;
    private SourceSinkType sourceSinkType;
    private final String regex = "^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>.+?\\((.+)\\)$";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CategorizedAndroidSourceSinkParser.class.desiredAssertionStatus();
    }

    public CategorizedAndroidSourceSinkParser(Set<CategoryDefinition> categories, String filename, SourceSinkType sourceSinkType) {
        this.categories = categories;
        this.fileName = filename;
        this.sourceSinkType = sourceSinkType;
    }

    public Set<ISourceSinkDefinition> parse() throws IOException {
        Set<ISourceSinkDefinition> definitions = new HashSet<>();
        CategoryDefinition allCats = new CategoryDefinition(CategoryDefinition.CATEGORY.ALL);
        boolean loadAllCategories = this.categories.contains(allCats);
        BufferedReader rdr = readFile();
        if (rdr == null) {
            throw new RuntimeException("Could not read source/sink file");
        }
        Pattern p = Pattern.compile("^<(.+):\\s*(.+)\\s+(.+)\\s*\\((.*)\\)>.+?\\((.+)\\)$");
        while (true) {
            String line = rdr.readLine();
            if (line == null) {
                break;
            }
            Matcher m = p.matcher(line);
            if (m.find()) {
                CategoryDefinition.CATEGORY strCat = CategoryDefinition.CATEGORY.valueOf(m.group(5));
                ISourceSinkCategory cat = new CategoryDefinition(strCat);
                if (loadAllCategories || this.categories.contains(cat)) {
                    AndroidMethod method = parseMethod(m);
                    method.setSourceSinkType(this.sourceSinkType);
                    MethodSourceSinkDefinition def = new MethodSourceSinkDefinition(method);
                    def.setCategory(cat);
                    definitions.add(def);
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
        return definitions;
    }

    private BufferedReader readFile() {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(this.fileName);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return br;
    }

    private AndroidMethod parseMethod(Matcher m) {
        String[] split;
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
            return new AndroidMethod(methodName, methodParameters, returnType, className);
        }
        throw new AssertionError();
    }
}
