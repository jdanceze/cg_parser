package soot.dotnet.members;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.dotnet.AssemblyFile;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetEvent.class */
public class DotnetEvent extends AbstractDotnetMember {
    private static final Logger logger = LoggerFactory.getLogger(DotnetEvent.class);
    private final SootClass declaringClass;
    private final ProtoAssemblyAllTypes.EventDefinition protoEvent;
    private DotnetMethod addAccessorMethod;
    private DotnetMethod invokeAccessorMethod;
    private DotnetMethod removeAccessorMethod;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective;

    /* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetEvent$EventDirective.class */
    public enum EventDirective {
        ADD,
        REMOVE,
        INVOKE;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static EventDirective[] valuesCustom() {
            EventDirective[] valuesCustom = values();
            int length = valuesCustom.length;
            EventDirective[] eventDirectiveArr = new EventDirective[length];
            System.arraycopy(valuesCustom, 0, eventDirectiveArr, 0, length);
            return eventDirectiveArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[EventDirective.valuesCustom().length];
        try {
            iArr2[EventDirective.ADD.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[EventDirective.INVOKE.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[EventDirective.REMOVE.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective = iArr2;
        return iArr2;
    }

    public DotnetEvent(ProtoAssemblyAllTypes.EventDefinition protoEvent, SootClass declaringClass) {
        this.protoEvent = protoEvent;
        this.declaringClass = declaringClass;
        initDotnetMethods();
    }

    private void initDotnetMethods() {
        if (getCanAdd() && this.protoEvent.hasAddAccessorMethod()) {
            this.addAccessorMethod = new DotnetMethod(this.protoEvent.getAddAccessorMethod(), this.declaringClass, DotnetMethod.DotnetMethodType.EVENT);
        }
        if (getCanInvoke() && this.protoEvent.hasInvokeAccessorMethod()) {
            this.invokeAccessorMethod = new DotnetMethod(this.protoEvent.getInvokeAccessorMethod(), this.declaringClass, DotnetMethod.DotnetMethodType.EVENT);
        }
        if (getCanRemove() && this.protoEvent.hasRemoveAccessorMethod()) {
            this.removeAccessorMethod = new DotnetMethod(this.protoEvent.getRemoveAccessorMethod(), this.declaringClass, DotnetMethod.DotnetMethodType.EVENT);
        }
    }

    public boolean getCanAdd() {
        return this.protoEvent.getCanAdd();
    }

    public boolean getCanInvoke() {
        return this.protoEvent.getCanInvoke();
    }

    public boolean getCanRemove() {
        return this.protoEvent.getCanRemove();
    }

    public SootMethod makeSootMethodAdd() {
        if (!getCanAdd() || !this.protoEvent.hasAddAccessorMethod()) {
            return null;
        }
        return this.addAccessorMethod.toSootMethod(createMethodSource(EventDirective.ADD));
    }

    public SootMethod makeSootMethodInvoke() {
        if (!getCanInvoke() || !this.protoEvent.hasInvokeAccessorMethod()) {
            return null;
        }
        return this.invokeAccessorMethod.toSootMethod(createMethodSource(EventDirective.INVOKE));
    }

    public SootMethod makeSootMethodRemove() {
        if (!getCanRemove() || !this.protoEvent.hasRemoveAccessorMethod()) {
            return null;
        }
        return this.removeAccessorMethod.toSootMethod(createMethodSource(EventDirective.REMOVE));
    }

    private MethodSource createMethodSource(EventDirective eventMethodType) {
        return m, phaseName -> {
            DotnetMethod dotnetMethod;
            AssemblyFile assemblyFile = (AssemblyFile) SourceLocator.v().dexClassIndex().get(this.declaringClass.getName());
            ProtoIlInstructions.IlFunctionMsg ilFunctionMsg = assemblyFile.getMethodBodyOfEvent(this.declaringClass.getName(), this.protoEvent.getName(), r6);
            switch ($SWITCH_TABLE$soot$dotnet$members$DotnetEvent$EventDirective()[r6.ordinal()]) {
                case 1:
                    dotnetMethod = this.addAccessorMethod;
                    break;
                case 2:
                    dotnetMethod = this.removeAccessorMethod;
                    break;
                case 3:
                    dotnetMethod = this.invokeAccessorMethod;
                    break;
                default:
                    throw new RuntimeException("Unexpected selection of event method type!");
            }
            Body b = dotnetMethod.jimplifyMethodBody(ilFunctionMsg);
            eventMethodType.setActiveBody(b);
            return eventMethodType.getActiveBody();
        };
    }
}
