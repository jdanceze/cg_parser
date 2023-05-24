package soot;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Singletons;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/ModuleUtil.class */
public class ModuleUtil {
    private static final Logger logger = LoggerFactory.getLogger(ModuleUtil.class);
    private static final List<String> packagesJavaBaseModule = parseJavaBasePackage();
    private static final String JAVABASEFILE = "javabase.txt";
    private final Cache<String, String> modulePackageCache = CacheBuilder.newBuilder().initialCapacity(60).maximumSize(800).concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
    private final LoadingCache<String, ModuleClassNameWrapper> wrapperCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(1000).concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(new CacheLoader<String, ModuleClassNameWrapper>() { // from class: soot.ModuleUtil.1
        @Override // com.google.common.cache.CacheLoader
        public ModuleClassNameWrapper load(String key) throws Exception {
            return new ModuleClassNameWrapper(key, null);
        }
    });

    public ModuleUtil(Singletons.Global g) {
    }

    public static ModuleUtil v() {
        return G.v().soot_ModuleUtil();
    }

    public final ModuleClassNameWrapper makeWrapper(String className) {
        try {
            return this.wrapperCache.get(className);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean module_mode() {
        return !Options.v().soot_modulepath().isEmpty();
    }

    public final String declaringModule(String className, String toModuleName) {
        SootModuleInfo modInfo;
        if (SootModuleInfo.MODULE_INFO.equalsIgnoreCase(className)) {
            return toModuleName;
        }
        ModuleScene modSc = ModuleScene.v();
        if (modSc.containsClass(SootModuleInfo.MODULE_INFO, Optional.fromNullable(toModuleName))) {
            SootClass temp = modSc.getSootClass(SootModuleInfo.MODULE_INFO, Optional.fromNullable(toModuleName));
            if (temp.resolvingLevel() < 3) {
                modInfo = (SootModuleInfo) SootModuleResolver.v().resolveClass(SootModuleInfo.MODULE_INFO, 3, Optional.fromNullable(toModuleName));
            } else {
                modInfo = (SootModuleInfo) temp;
            }
        } else {
            modInfo = (SootModuleInfo) SootModuleResolver.v().resolveClass(SootModuleInfo.MODULE_INFO, 3, Optional.fromNullable(toModuleName));
        }
        if (modInfo == null) {
            return null;
        }
        String packageName = getPackageName(className);
        String chacheKey = String.valueOf(modInfo.getModuleName()) + '/' + packageName;
        String moduleName = this.modulePackageCache.getIfPresent(chacheKey);
        if (moduleName != null) {
            return moduleName;
        }
        if (modInfo.exportsPackage(packageName, toModuleName)) {
            return modInfo.getModuleName();
        }
        if (modInfo.isAutomaticModule() && modSc.containsClass(className)) {
            String foundModuleName = modSc.getSootClass(className).getModuleInformation().getModuleName();
            this.modulePackageCache.put(chacheKey, foundModuleName);
            return foundModuleName;
        }
        for (SootModuleInfo modInf : modInfo.retrieveRequiredModules().keySet()) {
            if (modInf.exportsPackage(packageName, toModuleName)) {
                this.modulePackageCache.put(chacheKey, modInf.getModuleName());
                return modInf.getModuleName();
            }
            String tModuleName = checkTransitiveChain(modInf, packageName, toModuleName, new HashSet());
            if (tModuleName != null) {
                this.modulePackageCache.put(chacheKey, tModuleName);
                return tModuleName;
            }
        }
        return toModuleName;
    }

    private String checkTransitiveChain(SootModuleInfo modInfo, String packageName, String toModuleName, Set<String> hasCheckedModule) {
        for (Map.Entry<SootModuleInfo, Integer> entry : modInfo.retrieveRequiredModules().entrySet()) {
            if ((entry.getValue().intValue() & 32) != 0) {
                SootModuleInfo key = entry.getKey();
                String moduleName = key.getModuleName();
                if (!hasCheckedModule.contains(moduleName)) {
                    hasCheckedModule.add(moduleName);
                    if (key.exportsPackage(packageName, toModuleName)) {
                        return moduleName;
                    }
                    return checkTransitiveChain(key, packageName, toModuleName, hasCheckedModule);
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPackageName(String className) {
        int index = className.lastIndexOf(46);
        return index > 0 ? className.substring(0, index) : "";
    }

    /* JADX WARN: Finally extract failed */
    private static List<String> parseJavaBasePackage() {
        List<String> packages = new ArrayList<>();
        Path excludeFile = Paths.get(JAVABASEFILE, new String[0]);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.exists(excludeFile, new LinkOption[0]) ? Files.newInputStream(excludeFile, new OpenOption[0]) : ModuleUtil.class.getResourceAsStream("/javabase.txt")));
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    packages.add(line);
                } catch (Throwable th) {
                    if (reader != null) {
                        reader.close();
                    }
                    throw th;
                }
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException x) {
            logger.warn("Cannot open file specifying the packages of module 'java.base'", (Throwable) x);
        }
        return packages;
    }

    /* loaded from: gencallgraphv3.jar:soot/ModuleUtil$ModuleClassNameWrapper.class */
    public static final class ModuleClassNameWrapper {
        private static final String fullQualifiedName = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)+[a-zA-Z_$][a-zA-Z\\d_$]*";
        private static final Pattern fqnClassNamePattern = Pattern.compile(fullQualifiedName);
        private static final String qualifiedModuleName = "([a-zA-Z_$])([a-zA-Z\\d_$\\.]*)+";
        private static final Pattern qualifiedModuleNamePattern = Pattern.compile(qualifiedModuleName);
        private final String className;
        private final String moduleName;

        private ModuleClassNameWrapper(String className) {
            if (SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME.equals(className)) {
                this.className = className;
                this.moduleName = null;
                return;
            }
            String refinedClassName = className;
            String refinedModuleName = null;
            if (!className.contains(":")) {
                if (fqnClassNamePattern.matcher(className).matches() && ModuleUtil.packagesJavaBaseModule.contains(ModuleUtil.getPackageName(className))) {
                    refinedModuleName = "java.base";
                }
            } else {
                String[] split = className.split(":");
                if (split.length == 2 && qualifiedModuleNamePattern.matcher(split[0]).matches() && fqnClassNamePattern.matcher(split[1]).matches()) {
                    refinedModuleName = split[0];
                    refinedClassName = split[1];
                }
            }
            this.className = refinedClassName;
            this.moduleName = refinedModuleName;
            ModuleUtil.v().wrapperCache.put(className, this);
        }

        /* synthetic */ ModuleClassNameWrapper(String str, ModuleClassNameWrapper moduleClassNameWrapper) {
            this(str);
        }

        public String getClassName() {
            return this.className;
        }

        public String getModuleName() {
            return this.moduleName;
        }

        public Optional<String> getModuleNameOptional() {
            return Optional.fromNullable(this.moduleName);
        }
    }
}
