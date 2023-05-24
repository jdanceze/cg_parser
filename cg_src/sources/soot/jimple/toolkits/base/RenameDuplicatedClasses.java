package soot.jimple.toolkits.base;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/RenameDuplicatedClasses.class */
public class RenameDuplicatedClasses extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(RenameDuplicatedClasses.class);
    private static final String FIXED_CLASS_NAME_SPERATOR = "-";

    public RenameDuplicatedClasses(Singletons.Global g) {
    }

    public static RenameDuplicatedClasses v() {
        return G.v().soot_jimple_toolkits_base_RenameDuplicatedClasses();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        if (isFileSystemCaseSensitive()) {
            return;
        }
        Set<String> fixedClassNames = new HashSet<>(Arrays.asList(PhaseOptions.getString(options, "fixedClassNames").split("-")));
        duplicatedCheck(fixedClassNames);
        if (Options.v().verbose()) {
            logger.debug("The fixed class names are: " + fixedClassNames);
        }
        int count = 0;
        Map<String, String> lowerCaseClassNameToReal = new HashMap<>();
        Iterator<SootClass> iter = Scene.v().getClasses().snapshotIterator();
        while (iter.hasNext()) {
            SootClass sootClass = iter.next();
            String className = sootClass.getName();
            if (lowerCaseClassNameToReal.containsKey(className.toLowerCase())) {
                if (fixedClassNames.contains(className)) {
                    sootClass = Scene.v().getSootClass(lowerCaseClassNameToReal.get(className.toLowerCase()));
                    className = lowerCaseClassNameToReal.get(className.toLowerCase());
                }
                int i = count;
                count++;
                String newClassName = String.valueOf(className) + i;
                sootClass.rename(newClassName);
                logger.debug("Rename duplicated class " + className + " to class " + newClassName);
            } else {
                lowerCaseClassNameToReal.put(className.toLowerCase(), className);
            }
        }
    }

    public void duplicatedCheck(Iterable<String> classNames) {
        Set<String> classNameSet = new HashSet<>();
        for (String className : classNames) {
            if (classNameSet.contains(className.toLowerCase())) {
                throw new RuntimeException("The fixed class names cannot contain duplicated class names.");
            }
            classNameSet.add(className.toLowerCase());
        }
    }

    public boolean isFileSystemCaseSensitive() {
        File[] allFiles = new File(".").listFiles();
        if (allFiles != null) {
            for (File f : allFiles) {
                if (f.isFile() && (!new File(f.getAbsolutePath().toLowerCase()).exists() || !new File(f.getAbsolutePath().toUpperCase()).exists())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
