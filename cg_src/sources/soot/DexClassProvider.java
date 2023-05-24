package soot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dexpler.DexFileProvider;
import soot.dexpler.Util;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/DexClassProvider.class */
public class DexClassProvider implements ClassProvider {
    private static final Logger logger = LoggerFactory.getLogger(DexClassProvider.class);

    public static Set<String> classesOfDex(DexFile dexFile) {
        Set<String> classes = new HashSet<>();
        for (ClassDef c : dexFile.getClasses()) {
            classes.add(Util.dottedClassName(c.getType()));
        }
        return classes;
    }

    @Override // soot.ClassProvider
    public ClassSource find(String className) {
        ensureDexIndex();
        File file = SourceLocator.v().dexClassIndex().get(className);
        if (file == null) {
            return null;
        }
        return new DexClassSource(className, file);
    }

    protected void ensureDexIndex() {
        SourceLocator loc = SourceLocator.v();
        Map<String, File> index = loc.dexClassIndex();
        if (index == null) {
            index = new HashMap<>();
            buildDexIndex(index, loc.classPath());
            loc.setDexClassIndex(index);
        }
        Set<String> extensions = loc.getDexClassPathExtensions();
        if (extensions != null) {
            buildDexIndex(index, new ArrayList(extensions));
            loc.clearDexClassPathExtensions();
        }
    }

    private void buildDexIndex(Map<String, File> index, List<String> classPath) {
        for (String path : classPath) {
            try {
                File dexFile = new File(path);
                if (dexFile.exists()) {
                    for (DexFileProvider.DexContainer<? extends DexFile> container : DexFileProvider.v().getDexFromSource(dexFile)) {
                        for (String className : classesOfDex(container.getBase().getDexFile())) {
                            if (!index.containsKey(className)) {
                                index.put(className, container.getFilePath());
                            } else if (Options.v().verbose()) {
                                logger.debug(String.format("Warning: Duplicate of class '%s' found in dex file '%s' from source '%s'. Omitting class.", className, container.getDexName(), container.getFilePath().getCanonicalPath()));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.warn("IO error while processing dex file '" + path + "'");
                logger.debug("Exception: " + e);
            } catch (Exception e2) {
                logger.warn("exception while processing dex file '" + path + "'");
                logger.debug("Exception: " + e2);
            }
        }
    }
}
