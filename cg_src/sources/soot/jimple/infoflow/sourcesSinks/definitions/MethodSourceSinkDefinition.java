package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import soot.jimple.infoflow.data.SootMethodAndClass;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/MethodSourceSinkDefinition.class */
public class MethodSourceSinkDefinition extends AbstractSourceSinkDefinition implements IAccessPathBasedSourceSinkDefinition {
    private static MethodSourceSinkDefinition BASE_OBJ_SOURCE;
    private static MethodSourceSinkDefinition BASE_OBJ_SINK;
    private static MethodSourceSinkDefinition[] PARAM_OBJ_SOURCE = new MethodSourceSinkDefinition[5];
    protected final SootMethodAndClass method;
    protected final CallType callType;
    protected Set<AccessPathTuple> baseObjects;
    protected Set<AccessPathTuple>[] parameters;
    protected Set<AccessPathTuple> returnValues;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/MethodSourceSinkDefinition$CallType.class */
    public enum CallType {
        MethodCall,
        Callback,
        Return;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CallType[] valuesCustom() {
            CallType[] valuesCustom = values();
            int length = valuesCustom.length;
            CallType[] callTypeArr = new CallType[length];
            System.arraycopy(valuesCustom, 0, callTypeArr, 0, length);
            return callTypeArr;
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public /* bridge */ /* synthetic */ IAccessPathBasedSourceSinkDefinition filter(Collection collection) {
        return filter((Collection<AccessPathTuple>) collection);
    }

    public MethodSourceSinkDefinition(Set<AccessPathTuple> baseObjects, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> returnValues, CallType callType) {
        this(null, baseObjects, setArr, returnValues, callType);
    }

    public MethodSourceSinkDefinition(SootMethodAndClass am) {
        this(am, null, null, null, CallType.MethodCall);
    }

    public MethodSourceSinkDefinition(SootMethodAndClass am, CallType callType) {
        this(am, null, null, null, callType);
    }

    public MethodSourceSinkDefinition(SootMethodAndClass am, Set<AccessPathTuple> baseObjects, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> returnValues, CallType callType) {
        this(am, baseObjects, setArr, returnValues, callType, null);
    }

    public MethodSourceSinkDefinition(SootMethodAndClass am, Set<AccessPathTuple> baseObjects, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> returnValues, CallType callType, ISourceSinkCategory category) {
        super(category);
        this.method = am;
        this.baseObjects = (baseObjects == null || baseObjects.isEmpty()) ? null : baseObjects;
        this.parameters = setArr;
        this.returnValues = (returnValues == null || returnValues.isEmpty()) ? null : returnValues;
        this.callType = callType;
    }

    public SootMethodAndClass getMethod() {
        return this.method;
    }

    public CallType getCallType() {
        return this.callType;
    }

    public Set<AccessPathTuple> getBaseObjects() {
        return this.baseObjects;
    }

    public int getBaseObjectCount() {
        if (this.baseObjects == null) {
            return 0;
        }
        return this.baseObjects.size();
    }

    public Set<AccessPathTuple>[] getParameters() {
        return this.parameters;
    }

    public int getParameterCount() {
        Set<AccessPathTuple>[] setArr;
        if (this.parameters == null || this.parameters.length == 0) {
            return 0;
        }
        int cnt = 0;
        for (Set<AccessPathTuple> apt : this.parameters) {
            cnt += apt.size();
        }
        return cnt;
    }

    public Set<AccessPathTuple> getReturnValues() {
        return this.returnValues;
    }

    public int getReturnValueCount() {
        if (this.returnValues == null) {
            return 0;
        }
        return this.returnValues.size();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public boolean isEmpty() {
        boolean parametersEmpty = true;
        if (this.parameters != null) {
            Set<AccessPathTuple>[] setArr = this.parameters;
            int length = setArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Set<AccessPathTuple> paramSet = setArr[i];
                if (paramSet == null || paramSet.isEmpty()) {
                    i++;
                } else {
                    parametersEmpty = false;
                    break;
                }
            }
        }
        if ((this.baseObjects == null || this.baseObjects.isEmpty()) && parametersEmpty) {
            return this.returnValues == null || this.returnValues.isEmpty();
        }
        return false;
    }

    public String toString() {
        return this.method == null ? "<no method>" : this.method.getSignature();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public MethodSourceSinkDefinition getSourceOnlyDefinition() {
        Set<AccessPathTuple> baseSources = null;
        if (this.baseObjects != null) {
            baseSources = new HashSet<>(this.baseObjects.size());
            for (AccessPathTuple apt : this.baseObjects) {
                if (apt.getSourceSinkType().isSource()) {
                    baseSources.add(apt);
                }
            }
        }
        Set[] paramSources = null;
        if (this.parameters != null && this.parameters.length > 0) {
            paramSources = new Set[this.parameters.length];
            for (int i = 0; i < this.parameters.length; i++) {
                Set<AccessPathTuple> aptSet = this.parameters[i];
                if (aptSet != null) {
                    Set<AccessPathTuple> thisParam = new HashSet<>(aptSet.size());
                    paramSources[i] = thisParam;
                    for (AccessPathTuple apt2 : aptSet) {
                        if (apt2.getSourceSinkType().isSource()) {
                            thisParam.add(apt2);
                        }
                    }
                }
            }
        }
        Set<AccessPathTuple> returnSources = null;
        if (this.returnValues != null) {
            returnSources = new HashSet<>(this.returnValues.size());
            for (AccessPathTuple apt3 : this.returnValues) {
                if (apt3.getSourceSinkType().isSource()) {
                    returnSources.add(apt3);
                }
            }
        }
        MethodSourceSinkDefinition mssd = buildNewDefinition(baseSources, paramSources, returnSources);
        return mssd;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public MethodSourceSinkDefinition getSinkOnlyDefinition() {
        Set<AccessPathTuple> baseSinks = null;
        if (this.baseObjects != null) {
            baseSinks = new HashSet<>(this.baseObjects.size());
            for (AccessPathTuple apt : this.baseObjects) {
                if (apt.getSourceSinkType().isSink()) {
                    baseSinks.add(apt);
                }
            }
        }
        Set[] paramSinks = null;
        if (this.parameters != null) {
            paramSinks = new Set[this.parameters.length];
            for (int i = 0; i < this.parameters.length; i++) {
                Set<AccessPathTuple> aptSet = this.parameters[i];
                if (aptSet != null) {
                    Set<AccessPathTuple> thisParam = new HashSet<>(aptSet.size());
                    paramSinks[i] = thisParam;
                    for (AccessPathTuple apt2 : aptSet) {
                        if (apt2.getSourceSinkType().isSink()) {
                            thisParam.add(apt2);
                        }
                    }
                }
            }
        }
        Set<AccessPathTuple> returnSinks = null;
        if (this.returnValues != null) {
            returnSinks = new HashSet<>(this.returnValues.size());
            for (AccessPathTuple apt3 : this.returnValues) {
                if (apt3.getSourceSinkType().isSink()) {
                    returnSinks.add(apt3);
                }
            }
        }
        MethodSourceSinkDefinition mssd = buildNewDefinition(baseSinks, paramSinks, returnSinks);
        return mssd;
    }

    protected MethodSourceSinkDefinition buildNewDefinition(Set<AccessPathTuple> baseAPTs, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> returnAPTs) {
        MethodSourceSinkDefinition def = buildNewDefinition(this.method, baseAPTs, setArr, returnAPTs, this.callType);
        def.category = this.category;
        return def;
    }

    protected MethodSourceSinkDefinition buildNewDefinition(SootMethodAndClass methodAndclass, Set<AccessPathTuple> filteredBaseObjects, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> filteredReturnValues, CallType callType) {
        return new MethodSourceSinkDefinition(methodAndclass, filteredBaseObjects, setArr, filteredReturnValues, callType);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition
    public void merge(ISourceSinkDefinition other) {
        if (other instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition otherMethod = (MethodSourceSinkDefinition) other;
            if (otherMethod.baseObjects != null && !otherMethod.baseObjects.isEmpty()) {
                if (this.baseObjects == null) {
                    this.baseObjects = new HashSet();
                }
                for (AccessPathTuple apt : otherMethod.baseObjects) {
                    this.baseObjects.add(apt);
                }
            }
            if (otherMethod.parameters != null && otherMethod.parameters.length > 0) {
                if (this.parameters == null) {
                    this.parameters = new Set[this.method.getParameters().size()];
                }
                for (int i = 0; i < otherMethod.parameters.length; i++) {
                    addParameterDefinition(i, otherMethod.parameters[i]);
                }
            }
            if (otherMethod.returnValues != null && !otherMethod.returnValues.isEmpty()) {
                if (this.returnValues == null) {
                    this.returnValues = new HashSet();
                }
                for (AccessPathTuple apt2 : otherMethod.returnValues) {
                    this.returnValues.add(apt2);
                }
            }
        }
    }

    public void addParameterDefinition(int paramIdx, Set<AccessPathTuple> paramDefs) {
        if (paramDefs != null && !paramDefs.isEmpty()) {
            Set[] oldSet = this.parameters;
            if (oldSet.length <= paramIdx) {
                Set[] newSet = new Set[paramIdx + 1];
                System.arraycopy(oldSet, 0, newSet, 0, paramIdx);
                this.parameters = newSet;
            }
            Set<AccessPathTuple> aps = this.parameters[paramIdx];
            if (aps == null) {
                aps = new HashSet<>(paramDefs.size());
                this.parameters[paramIdx] = aps;
            }
            aps.addAll(paramDefs);
        }
    }

    public MethodSourceSinkDefinition getBaseObjectSource() {
        if (BASE_OBJ_SOURCE == null) {
            BASE_OBJ_SOURCE = new MethodSourceSinkDefinition(Collections.singleton(AccessPathTuple.getBlankSourceTuple()), null, null, CallType.MethodCall);
        }
        return BASE_OBJ_SOURCE;
    }

    public MethodSourceSinkDefinition getBaseObjectSink() {
        if (BASE_OBJ_SINK == null) {
            BASE_OBJ_SINK = new MethodSourceSinkDefinition(Collections.singleton(AccessPathTuple.getBlankSinkTuple()), null, null, CallType.MethodCall);
        }
        return BASE_OBJ_SINK;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + (this.baseObjects == null ? 0 : this.baseObjects.hashCode()))) + (this.callType == null ? 0 : this.callType.hashCode()))) + (this.method == null ? 0 : this.method.hashCode()))) + Arrays.hashCode(this.parameters))) + (this.returnValues == null ? 0 : this.returnValues.hashCode());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        MethodSourceSinkDefinition other = (MethodSourceSinkDefinition) obj;
        if (this.baseObjects == null) {
            if (other.baseObjects != null) {
                return false;
            }
        } else if (!this.baseObjects.equals(other.baseObjects)) {
            return false;
        }
        if (this.callType != other.callType) {
            return false;
        }
        if (this.method == null) {
            if (other.method != null) {
                return false;
            }
        } else if (!this.method.equals(other.method)) {
            return false;
        }
        if (!Arrays.equals(this.parameters, other.parameters)) {
            return false;
        }
        if (this.returnValues == null) {
            if (other.returnValues != null) {
                return false;
            }
            return true;
        } else if (!this.returnValues.equals(other.returnValues)) {
            return false;
        } else {
            return true;
        }
    }

    public static MethodSourceSinkDefinition createParameterSource(int index, CallType callType) {
        if (index < 5 && callType == CallType.MethodCall) {
            MethodSourceSinkDefinition def = PARAM_OBJ_SOURCE[index];
            if (def == null) {
                Set[] params = new Set[index + 1];
                params[index] = Collections.singleton(AccessPathTuple.getBlankSourceTuple());
                def = new MethodSourceSinkDefinition(null, params, null, callType);
                PARAM_OBJ_SOURCE[index] = def;
            }
            return def;
        }
        return new MethodSourceSinkDefinition(null, new Set[]{Collections.singleton(AccessPathTuple.getBlankSourceTuple())}, null, callType);
    }

    public static MethodSourceSinkDefinition createReturnSource(CallType callType) {
        return new MethodSourceSinkDefinition(null, null, Collections.singleton(AccessPathTuple.getBlankSourceTuple()), callType);
    }

    public MethodSourceSinkDefinition simplify() {
        MethodSourceSinkDefinition baseObjSource = getBaseObjectSource();
        MethodSourceSinkDefinition baseObjSink = getBaseObjectSink();
        if (equals(baseObjSource)) {
            return baseObjSource;
        }
        if (equals(baseObjSink)) {
            return baseObjSink;
        }
        for (int i = 0; i < PARAM_OBJ_SOURCE.length; i++) {
            MethodSourceSinkDefinition def = createParameterSource(i, getCallType());
            if (equals(def)) {
                return def;
            }
        }
        return this;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public Set<AccessPathTuple> getAllAccessPaths() {
        Set<AccessPathTuple>[] setArr;
        Set<AccessPathTuple> aps = new HashSet<>();
        if (this.baseObjects != null && !this.baseObjects.isEmpty()) {
            aps.addAll(this.baseObjects);
        }
        if (this.returnValues != null && !this.returnValues.isEmpty()) {
            aps.addAll(this.returnValues);
        }
        if (this.parameters != null && this.parameters.length > 0) {
            for (Set<AccessPathTuple> paramAPs : this.parameters) {
                if (paramAPs != null && !paramAPs.isEmpty()) {
                    aps.addAll(paramAPs);
                }
            }
        }
        return aps;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public MethodSourceSinkDefinition filter(Collection<AccessPathTuple> accessPaths) {
        Set<AccessPathTuple> filteredBaseObjects = null;
        if (this.baseObjects != null && !this.baseObjects.isEmpty()) {
            filteredBaseObjects = new HashSet<>(this.baseObjects.size());
            for (AccessPathTuple ap : this.baseObjects) {
                if (accessPaths.contains(ap)) {
                    filteredBaseObjects.add(ap);
                }
            }
        }
        Set<AccessPathTuple> filteredReturnValues = null;
        if (this.returnValues != null && !this.returnValues.isEmpty()) {
            filteredReturnValues = new HashSet<>(this.returnValues.size());
            for (AccessPathTuple ap2 : this.returnValues) {
                if (accessPaths.contains(ap2)) {
                    filteredReturnValues.add(ap2);
                }
            }
        }
        Set[] filteredParameters = null;
        if (this.parameters != null && this.parameters.length > 0) {
            filteredParameters = new Set[this.parameters.length];
            for (int i = 0; i < this.parameters.length; i++) {
                if (this.parameters[i] != null && !this.parameters[i].isEmpty()) {
                    filteredParameters[i] = new HashSet();
                    for (AccessPathTuple ap3 : this.parameters[i]) {
                        if (accessPaths.contains(ap3)) {
                            filteredParameters[i].add(ap3);
                        }
                    }
                }
            }
        }
        MethodSourceSinkDefinition def = buildNewDefinition(this.method, filteredBaseObjects, filteredParameters, filteredReturnValues, this.callType);
        def.setCategory(this.category);
        return def;
    }
}
