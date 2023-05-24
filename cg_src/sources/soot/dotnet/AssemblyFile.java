package soot.dotnet;

import com.google.common.base.Strings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dotnet.members.DotnetEvent;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoDotnetNativeHost;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.options.Options;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/dotnet/AssemblyFile.class */
public class AssemblyFile extends File {
    private static final Logger logger = LoggerFactory.getLogger(AssemblyFile.class);
    private final String fullyQualifiedAssemblyPathFilename;
    private ProtoAssemblyAllTypes.AssemblyAllTypes protoAllTypes;
    private final String pathNativeHost;
    private boolean gotAllReferencesModuleTypes;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective;

    private native byte[] nativeGetAllTypesMsg(String str, byte[] bArr);

    private native byte[] nativeGetMethodBodyMsg(String str, byte[] bArr);

    private native byte[] nativeGetMethodBodyOfPropertyMsg(String str, byte[] bArr);

    private native byte[] nativeGetMethodBodyOfEventMsg(String str, byte[] bArr);

    private native byte[] nativeGetAssemblyContentMsg(String str, byte[] bArr);

    private native boolean nativeIsAssembly(String str, String str2);

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[DotnetEvent.EventDirective.valuesCustom().length];
        try {
            iArr2[DotnetEvent.EventDirective.ADD.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[DotnetEvent.EventDirective.INVOKE.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[DotnetEvent.EventDirective.REMOVE.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective = iArr2;
        return iArr2;
    }

    public AssemblyFile(String fullyQualifiedAssemblyPathFilename) {
        super(fullyQualifiedAssemblyPathFilename);
        this.gotAllReferencesModuleTypes = false;
        this.fullyQualifiedAssemblyPathFilename = fullyQualifiedAssemblyPathFilename;
        this.pathNativeHost = Options.v().dotnet_nativehost_path();
        System.load(this.pathNativeHost);
    }

    public ProtoAssemblyAllTypes.AssemblyAllTypes getAllTypes() {
        if (this.protoAllTypes != null) {
            return this.protoAllTypes;
        }
        try {
            ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder analyzerParamsBuilder = createAnalyzerParamsBuilder("", ProtoDotnetNativeHost.AnalyzerMethodCall.GET_ALL_TYPES);
            ProtoDotnetNativeHost.AnalyzerParamsMsg analyzerParamsMsg = analyzerParamsBuilder.build();
            byte[] protobufMessageBytes = nativeGetAllTypesMsg(this.pathNativeHost, analyzerParamsMsg.toByteArray());
            ProtoAssemblyAllTypes.AssemblyAllTypes a = ProtoAssemblyAllTypes.AssemblyAllTypes.parseFrom(protobufMessageBytes);
            this.protoAllTypes = a;
            return a;
        } catch (Exception e) {
            if (Options.v().verbose()) {
                logger.warn(String.valueOf(getAssemblyFileName()) + " has no types. Error of protobuf message: " + e.getMessage());
                return null;
            }
            return null;
        }
    }

    public ProtoIlInstructions.IlFunctionMsg getMethodBody(String className, String method, int peToken) {
        ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder analyzerParamsBuilder = createAnalyzerParamsBuilder(className, ProtoDotnetNativeHost.AnalyzerMethodCall.GET_METHOD_BODY);
        Pair<String, String> methodNameSuffixPair = helperExtractMethodNameSuffix(method);
        analyzerParamsBuilder.setMethodName(methodNameSuffixPair.getO1());
        analyzerParamsBuilder.setMethodNameSuffix(methodNameSuffixPair.getO2());
        analyzerParamsBuilder.setMethodPeToken(peToken);
        ProtoDotnetNativeHost.AnalyzerParamsMsg analyzerParamsMsg = analyzerParamsBuilder.build();
        try {
            byte[] protoMsgBytes = nativeGetMethodBodyMsg(this.pathNativeHost, analyzerParamsMsg.toByteArray());
            return ProtoIlInstructions.IlFunctionMsg.parseFrom(protoMsgBytes);
        } catch (Exception e) {
            if (Options.v().verbose()) {
                logger.warn("Exception while getting method body of method " + className + "." + method + ": " + e.getMessage());
                return null;
            }
            return null;
        }
    }

    private Pair<String, String> helperExtractMethodNameSuffix(String sootMethodName) {
        if (!sootMethodName.contains("[[") || !sootMethodName.contains("]]")) {
            return new Pair<>(sootMethodName, "");
        }
        int startSuffix = sootMethodName.indexOf("[[");
        String suffix = sootMethodName.substring(startSuffix);
        String cilMethodName = sootMethodName.substring(0, startSuffix);
        return new Pair<>(cilMethodName, suffix);
    }

    public ProtoIlInstructions.IlFunctionMsg getMethodBodyOfProperty(String className, String propertyName, boolean isSetter) {
        ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder analyzerParamsBuilder = createAnalyzerParamsBuilder(className, ProtoDotnetNativeHost.AnalyzerMethodCall.GET_METHOD_BODY_OF_PROPERTY);
        analyzerParamsBuilder.setPropertyName(propertyName);
        analyzerParamsBuilder.setPropertyIsSetter(isSetter);
        ProtoDotnetNativeHost.AnalyzerParamsMsg analyzerParamsMsg = analyzerParamsBuilder.build();
        try {
            byte[] protoMsgBytes = nativeGetMethodBodyOfPropertyMsg(this.pathNativeHost, analyzerParamsMsg.toByteArray());
            return ProtoIlInstructions.IlFunctionMsg.parseFrom(protoMsgBytes);
        } catch (Exception e) {
            if (Options.v().verbose()) {
                logger.warn("Exception while getting method body of property " + className + "." + propertyName + ": " + e.getMessage());
                logger.warn("Return null");
                return null;
            }
            return null;
        }
    }

    public ProtoIlInstructions.IlFunctionMsg getMethodBodyOfEvent(String className, String eventName, DotnetEvent.EventDirective eventDirective) {
        ProtoDotnetNativeHost.EventAccessorType accessorType;
        ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder analyzerParamsBuilder = createAnalyzerParamsBuilder(className, ProtoDotnetNativeHost.AnalyzerMethodCall.GET_METHOD_BODY_OF_EVENT);
        analyzerParamsBuilder.setEventName(eventName);
        switch ($SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective()[eventDirective.ordinal()]) {
            case 1:
                accessorType = ProtoDotnetNativeHost.EventAccessorType.ADD_ACCESSOR;
                break;
            case 2:
                accessorType = ProtoDotnetNativeHost.EventAccessorType.REMOVE_ACCESSOR;
                break;
            case 3:
                accessorType = ProtoDotnetNativeHost.EventAccessorType.INVOKE_ACCESSOR;
                break;
            default:
                throw new RuntimeException("Wrong Event Accessor Type!");
        }
        analyzerParamsBuilder.setEventAccessorType(accessorType);
        ProtoDotnetNativeHost.AnalyzerParamsMsg analyzerParamsMsg = analyzerParamsBuilder.build();
        try {
            byte[] protoMsgBytes = nativeGetMethodBodyOfEventMsg(this.pathNativeHost, analyzerParamsMsg.toByteArray());
            return ProtoIlInstructions.IlFunctionMsg.parseFrom(protoMsgBytes);
        } catch (Exception e) {
            if (Options.v().verbose()) {
                logger.warn("Exception while getting method body of event " + className + "." + eventName + ": " + e.getMessage());
                return null;
            }
            return null;
        }
    }

    public boolean isAssembly() {
        return nativeIsAssembly(this.pathNativeHost, this.fullyQualifiedAssemblyPathFilename);
    }

    public ProtoAssemblyAllTypes.TypeDefinition getTypeDefinition(String className) {
        ProtoAssemblyAllTypes.AssemblyAllTypes allTypes;
        if (Strings.isNullOrEmpty(className) || (allTypes = getAllTypes()) == null) {
            return null;
        }
        List<ProtoAssemblyAllTypes.TypeDefinition> allTypesList = allTypes.getListOfTypesList();
        Optional<ProtoAssemblyAllTypes.TypeDefinition> c = allTypesList.stream().filter(x -> {
            return x.getFullname().equals(className);
        }).findFirst();
        return c.orElse(null);
    }

    public List<String> getAllTypeNames() {
        ProtoAssemblyAllTypes.AssemblyAllTypes allTypes = getAllTypes();
        if (allTypes == null) {
            return null;
        }
        List<ProtoAssemblyAllTypes.TypeDefinition> listOfTypesList = allTypes.getListOfTypesList();
        return (List) listOfTypesList.stream().map((v0) -> {
            return v0.getFullname();
        }).collect(Collectors.toList());
    }

    public List<String> getAllReferencedModuleTypes() {
        ProtoAssemblyAllTypes.AssemblyAllTypes allTypes = getAllTypes();
        if (allTypes == null || this.gotAllReferencesModuleTypes) {
            return new ArrayList();
        }
        this.gotAllReferencesModuleTypes = true;
        return allTypes.getAllReferencedModuleTypesList();
    }

    private ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder createAnalyzerParamsBuilder(String className, ProtoDotnetNativeHost.AnalyzerMethodCall methodCall) {
        ProtoDotnetNativeHost.AnalyzerParamsMsg.Builder analyzerParamsBuilder = ProtoDotnetNativeHost.AnalyzerParamsMsg.newBuilder();
        analyzerParamsBuilder.setAnalyzerMethodCall(methodCall);
        analyzerParamsBuilder.setAssemblyFileAbsolutePath(this.fullyQualifiedAssemblyPathFilename);
        analyzerParamsBuilder.setTypeReflectionName(className);
        if (Options.v().verbose() || Options.v().debug()) {
            analyzerParamsBuilder.setDebugMode(true);
        }
        return analyzerParamsBuilder;
    }

    public String getFullPath() {
        return FilenameUtils.getFullPath(this.fullyQualifiedAssemblyPathFilename);
    }

    public String getAssemblyFileName() {
        return FilenameUtils.getName(this.fullyQualifiedAssemblyPathFilename);
    }
}
