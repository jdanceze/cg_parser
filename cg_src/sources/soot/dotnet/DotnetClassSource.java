package soot.dotnet;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ClassSource;
import soot.PrimType;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.Type;
import soot.VoidType;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.types.DotnetBasicTypes;
import soot.dotnet.types.DotnetFakeLdFtnType;
import soot.dotnet.types.DotnetType;
import soot.dotnet.types.DotnetTypeFactory;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/dotnet/DotnetClassSource.class */
public class DotnetClassSource extends ClassSource {
    private static final Logger logger = LoggerFactory.getLogger(DotnetClassSource.class);
    protected AssemblyFile assemblyFile;

    public DotnetClassSource(String className, File path) {
        super(className);
        if (className.equals(DotnetBasicTypes.FAKE_LDFTN)) {
            return;
        }
        if (!(path instanceof AssemblyFile)) {
            throw new RuntimeException("Given File object is no assembly file!");
        }
        this.assemblyFile = (AssemblyFile) path;
    }

    @Override // soot.ClassSource
    public IInitialResolver.Dependencies resolve(SootClass sc) {
        if (sc.getName().equals(DotnetBasicTypes.FAKE_LDFTN)) {
            return DotnetFakeLdFtnType.resolve(sc);
        }
        if (Options.v().verbose()) {
            logger.info("resolving " + this.className + " type definition from file " + this.assemblyFile.getPath());
        }
        resolveSignatureDependencies();
        ProtoAssemblyAllTypes.TypeDefinition typeDefinition = this.assemblyFile.getTypeDefinition(sc.getName());
        DotnetType dotnetType = new DotnetType(typeDefinition, this.assemblyFile);
        return dotnetType.resolveSootClass(sc);
    }

    private void resolveSignatureDependencies() {
        List<String> allModuleTypesList = this.assemblyFile.getAllReferencedModuleTypes();
        for (String i : allModuleTypesList) {
            Type st = DotnetTypeFactory.toSootType(i);
            String sootTypeName = st.toString();
            if (!Scene.v().containsClass(sootTypeName)) {
                if (!(st instanceof PrimType) && !(st instanceof VoidType)) {
                    SootResolver.v().makeClassRef(sootTypeName);
                }
            }
            SootResolver.v().resolveClass(sootTypeName, 2);
        }
    }
}
