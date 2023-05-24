package soot.dotnet;

import com.google.common.base.Strings;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ClassProvider;
import soot.ClassSource;
import soot.SourceLocator;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/dotnet/DotnetClassProvider.class */
public class DotnetClassProvider implements ClassProvider {
    private static final Logger logger = LoggerFactory.getLogger(DotnetClassProvider.class);

    @Override // soot.ClassProvider
    public ClassSource find(String className) {
        ensureAssemblyIndex();
        if (className.equals(DotnetBasicTypes.FAKE_LDFTN)) {
            return new DotnetClassSource(className, null);
        }
        File assemblyFile = SourceLocator.v().dexClassIndex().get(className);
        if (assemblyFile == null) {
            return null;
        }
        return new DotnetClassSource(className, assemblyFile);
    }

    private void ensureAssemblyIndex() {
        Map<String, File> index = SourceLocator.v().dexClassIndex();
        if (index == null) {
            if (Options.v().verbose()) {
                logger.info("Creating assembly index");
            }
            index = new HashMap<>();
            buildAssemblyIndex(index, SourceLocator.v().classPath());
            SourceLocator.v().setDexClassIndex(index);
            if (Options.v().verbose()) {
                logger.info("Created assembly index");
            }
        }
        if (SourceLocator.v().getDexClassPathExtensions() != null) {
            if (Options.v().verbose()) {
                logger.info("Process classpath extensions");
            }
            buildAssemblyIndex(index, new ArrayList(SourceLocator.v().getDexClassPathExtensions()));
            SourceLocator.v().clearDexClassPathExtensions();
        }
    }

    private void buildAssemblyIndex(Map<String, File> index, List<String> classPath) {
        File[] fileArr;
        ProtoAssemblyAllTypes.AssemblyAllTypes assemblyDefinition;
        if (Strings.isNullOrEmpty(Options.v().dotnet_nativehost_path())) {
            throw new RuntimeException("Dotnet NativeHost Path is not set! Use -dotnet-nativehost-path Soot parameter!");
        }
        for (String path : classPath) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    File[] listFiles = file.isDirectory() ? file.listFiles((v0) -> {
                        return v0.isFile();
                    }) : new File[]{file};
                    for (File f : (File[]) Objects.requireNonNull(listFiles)) {
                        if (Options.v().verbose()) {
                            logger.info("Process " + f.getCanonicalPath() + " file");
                        }
                        if (f.getCanonicalPath().endsWith(".exe") || f.getCanonicalPath().endsWith(".dll")) {
                            AssemblyFile assemblyFile = new AssemblyFile(f.getCanonicalPath());
                            if (assemblyFile.isAssembly() && (assemblyDefinition = assemblyFile.getAllTypes()) != null) {
                                if (!index.containsKey(f.getCanonicalPath())) {
                                    index.put(f.getCanonicalPath(), assemblyFile);
                                }
                                List<ProtoAssemblyAllTypes.TypeDefinition> allTypesOfMainModule = assemblyDefinition.getListOfTypesList();
                                for (ProtoAssemblyAllTypes.TypeDefinition type : allTypesOfMainModule) {
                                    String typeName = type.getFullname();
                                    if (Options.v().verbose()) {
                                        logger.info("Add class " + typeName + " to index");
                                    }
                                    if (!index.containsKey(typeName)) {
                                        index.put(typeName, assemblyFile);
                                    } else if (Options.v().verbose()) {
                                        logger.debug(String.format("Warning: Duplicate of class '%s' found in assembly file '%s' from source '%s'. Omitting class.", type, assemblyFile.getAssemblyFileName(), assemblyFile.getFullPath()));
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("exception while processing assembly file '" + path + "'");
                logger.warn("Exception: " + e);
            }
        }
    }
}
