package soot;

import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.dava.toolkits.base.misc.PackageNamer;
/* loaded from: gencallgraphv3.jar:soot/SootModuleInfo.class */
public class SootModuleInfo extends SootClass {
    public static final String MODULE_INFO_FILE = "module-info.class";
    public static final String MODULE_INFO = "module-info";
    private static final String ALL_MODULES = "EVERYONE_MODULE";
    private final HashSet<String> modulePackages;
    private final Map<SootModuleInfo, Integer> requiredModules;
    private final Map<String, List<String>> exportedPackages;
    private final Map<String, List<String>> openedPackages;
    private boolean isAutomaticModule;

    public SootModuleInfo(String name, int modifiers, String moduleName) {
        super(name, modifiers, moduleName);
        this.modulePackages = new HashSet<>();
        this.requiredModules = new HashMap();
        this.exportedPackages = new HashMap();
        this.openedPackages = new HashMap();
    }

    public SootModuleInfo(String name, String moduleName) {
        this(name, moduleName, false);
    }

    public SootModuleInfo(String name, String moduleName, boolean isAutomatic) {
        super(name, moduleName);
        this.modulePackages = new HashSet<>();
        this.requiredModules = new HashMap();
        this.exportedPackages = new HashMap();
        this.openedPackages = new HashMap();
        this.isAutomaticModule = isAutomatic;
    }

    public boolean isAutomaticModule() {
        return this.isAutomaticModule;
    }

    public void setAutomaticModule(boolean automaticModule) {
        this.isAutomaticModule = automaticModule;
    }

    private Map<String, List<String>> getExportedPackages() {
        return this.exportedPackages;
    }

    private Map<String, List<String>> getOpenedPackages() {
        return this.openedPackages;
    }

    public Set<String> getPublicExportedPackages() {
        Set<String> publicExportedPackages = new HashSet<>();
        Iterator<String> it = this.modulePackages.iterator();
        while (it.hasNext()) {
            String packaze = it.next();
            if (exportsPackage(packaze, ALL_MODULES)) {
                publicExportedPackages.add(packaze);
            }
        }
        return publicExportedPackages;
    }

    public Set<String> getPublicOpenedPackages() {
        Set<String> publicOpenedPackages = new HashSet<>();
        Iterator<String> it = this.modulePackages.iterator();
        while (it.hasNext()) {
            String packaze = it.next();
            if (opensPackage(packaze, ALL_MODULES)) {
                publicOpenedPackages.add(packaze);
            }
        }
        return publicOpenedPackages;
    }

    public Map<SootModuleInfo, Integer> getRequiredModules() {
        return this.requiredModules;
    }

    public Map<SootModuleInfo, Integer> retrieveRequiredModules() {
        Map<SootModuleInfo, Integer> moduleInfos = this.requiredModules;
        if (this.isAutomaticModule) {
            for (SootClass sootClass : Scene.v().getClasses()) {
                if ((sootClass instanceof SootModuleInfo) && sootClass.moduleName != this.moduleName) {
                    moduleInfos.put((SootModuleInfo) sootClass, 64);
                }
            }
        }
        for (SootModuleInfo moduleInfo : moduleInfos.keySet()) {
            SootModuleResolver.v().resolveClass(MODULE_INFO, 3, Optional.fromNullable(moduleInfo.moduleName));
        }
        return moduleInfos;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addExportedPackage(String packaze, String... exportedToModules) {
        List<String> qualifiedExports;
        if (exportedToModules != null && exportedToModules.length > 0) {
            qualifiedExports = Arrays.asList(exportedToModules);
        } else {
            qualifiedExports = Collections.singletonList(ALL_MODULES);
        }
        this.exportedPackages.put(PackageNamer.v().get_FixedPackageName(packaze).replace('/', '.'), qualifiedExports);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addOpenedPackage(String packaze, String... openedToModules) {
        List<String> qualifiedOpens;
        if (openedToModules != null && openedToModules.length > 0) {
            qualifiedOpens = Arrays.asList(openedToModules);
        } else {
            qualifiedOpens = Collections.singletonList(ALL_MODULES);
        }
        this.openedPackages.put(PackageNamer.v().get_FixedPackageName(packaze).replace('/', '.'), qualifiedOpens);
    }

    public String getModuleName() {
        return this.moduleName;
    }

    @Override // soot.SootClass
    public boolean isConcrete() {
        return false;
    }

    @Override // soot.SootClass
    public boolean isExportedByModule() {
        return true;
    }

    @Override // soot.SootClass
    public boolean isExportedByModule(String toModule) {
        return true;
    }

    @Override // soot.SootClass
    public boolean isOpenedByModule() {
        return true;
    }

    public boolean exportsPackagePublic(String packaze) {
        return exportsPackage(packaze, ALL_MODULES);
    }

    public boolean openPackagePublic(String packaze) {
        return opensPackage(packaze, ALL_MODULES);
    }

    public boolean opensPackage(String packaze, String toModule) {
        if (MODULE_INFO.equalsIgnoreCase(packaze)) {
            return true;
        }
        if (getModuleName().equals(toModule) || isAutomaticModule()) {
            return this.modulePackages.contains(packaze);
        }
        List<String> qualifiedOpens = this.openedPackages.get(packaze);
        if (qualifiedOpens == null) {
            return false;
        }
        if (qualifiedOpens.contains(ALL_MODULES)) {
            return true;
        }
        return toModule != ALL_MODULES && qualifiedOpens.contains(toModule);
    }

    public boolean exportsPackage(String packaze, String toModule) {
        if (MODULE_INFO.equalsIgnoreCase(packaze)) {
            return true;
        }
        if (getModuleName().equals(toModule) || isAutomaticModule()) {
            return this.modulePackages.contains(packaze);
        }
        List<String> qualifiedExport = this.exportedPackages.get(packaze);
        if (qualifiedExport == null) {
            return false;
        }
        if (qualifiedExport.contains(ALL_MODULES)) {
            return true;
        }
        return toModule != ALL_MODULES && qualifiedExport.contains(toModule);
    }

    public Set<SootModuleInfo> getRequiredPublicModules() {
        Set<SootModuleInfo> requiredPublic = new HashSet<>();
        for (Map.Entry<SootModuleInfo, Integer> entry : this.requiredModules.entrySet()) {
            if ((entry.getValue().intValue() & 32) != 0) {
                requiredPublic.add(entry.getKey());
            }
        }
        return requiredPublic;
    }

    public void addModulePackage(String packageName) {
        this.modulePackages.add(packageName);
    }

    public boolean moduleContainsPackage(String packageName) {
        return this.modulePackages.contains(packageName);
    }
}
