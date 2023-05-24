package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.AbstractAndroidEntryPointCreator;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/AbstractComponentEntryPointCreator.class */
public abstract class AbstractComponentEntryPointCreator extends AbstractAndroidEntryPointCreator {
    private final Logger logger;
    protected final SootClass component;
    protected final SootClass applicationClass;
    protected Set<SootMethod> callbacks;
    protected Local thisLocal;
    protected Local intentLocal;
    protected SootField intentField;
    private RefType INTENT_TYPE;

    protected abstract void generateComponentLifecycle();

    public AbstractComponentEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(manifest);
        this.logger = LoggerFactory.getLogger(getClass());
        this.callbacks = null;
        this.thisLocal = null;
        this.intentLocal = null;
        this.intentField = null;
        this.INTENT_TYPE = RefType.v("android.content.Intent");
        this.component = component;
        this.applicationClass = applicationClass;
        this.overwriteDummyMainMethod = true;
    }

    public void setCallbacks(Set<SootMethod> callbacks) {
        this.callbacks = callbacks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalFields() {
        super.createAdditionalFields();
        String fieldName = "ipcIntent";
        int fieldIdx = 0;
        while (this.component.declaresFieldByName(fieldName)) {
            int i = fieldIdx;
            fieldIdx++;
            fieldName = "ipcIntent_" + i;
        }
        this.intentField = Scene.v().makeSootField(fieldName, RefType.v("android.content.Intent"), 1);
        this.intentField.addTag(SimulatedCodeElementTag.TAG);
        this.component.addField(this.intentField);
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    protected void createEmptyMainMethod() {
        String componentPart = this.component.getName();
        if (componentPart.contains(".")) {
            componentPart = componentPart.replace("_", "__").replace(".", "_");
        }
        String baseMethodName = String.valueOf(this.dummyMethodName) + "_" + componentPart;
        int methodIndex = 0;
        String methodName = baseMethodName;
        SootClass mainClass = Scene.v().getSootClass(this.dummyClassName);
        if (!this.overwriteDummyMainMethod) {
            while (mainClass.declaresMethodByName(methodName)) {
                int i = methodIndex;
                methodIndex++;
                methodName = String.valueOf(baseMethodName) + "_" + i;
            }
        }
        this.mainMethod = mainClass.getMethodByNameUnsafe(methodName);
        if (this.mainMethod != null) {
            mainClass.removeMethod(this.mainMethod);
            this.mainMethod = null;
        }
        List<Type> defaultParams = getDefaultMainMethodParams();
        List<Type> additionalParams = getAdditionalMainMethodParams();
        List<Type> argList = new ArrayList<>(defaultParams);
        if (additionalParams != null && !additionalParams.isEmpty()) {
            argList.addAll(additionalParams);
        }
        this.mainMethod = Scene.v().makeSootMethod(methodName, argList, this.component.getType());
        JimpleBody body = Jimple.v().newBody();
        body.setMethod(this.mainMethod);
        this.mainMethod.setActiveBody(body);
        mainClass.addMethod(this.mainMethod);
        mainClass.setApplicationClass();
        this.mainMethod.setModifiers(9);
        body.insertIdentityStmts();
        this.intentLocal = body.getParameterLocal(0);
        for (int i2 = 0; i2 < argList.size(); i2++) {
            Local lc = body.getParameterLocal(i2);
            if (lc.getType() instanceof RefType) {
                RefType rt = (RefType) lc.getType();
                this.localVarsForClasses.put(rt.getSootClass(), lc);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final List<Type> getDefaultMainMethodParams() {
        return Collections.singletonList(RefType.v("android.content.Intent"));
    }

    protected List<Type> getAdditionalMainMethodParams() {
        return null;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<String> getRequiredClasses() {
        return null;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    protected SootMethod createDummyMainInternal() {
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        try {
            createIfStmt(newNopStmt2);
            this.thisLocal = generateClassConstructor(this.component);
            if (this.thisLocal != null) {
                this.localVarsForClasses.put(this.component, this.thisLocal);
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(this.thisLocal, this.intentField.makeRef()), this.intentLocal));
                generateComponentLifecycle();
            }
            createIfStmt(newNopStmt);
            NopEliminator.v().transform(this.body);
            instrumentDummyMainMethod();
            return this.mainMethod;
        } finally {
            this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
            if (this.thisLocal == null) {
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(NullConstant.v()));
            } else {
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(this.thisLocal));
            }
        }
    }

    protected void instrumentDummyMainMethod() {
        Body body = this.mainMethod.getActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> iter = units.snapshotIterator();
        while (iter.hasNext()) {
            Stmt stmt = (Stmt) iter.next();
            if (!(stmt instanceof IdentityStmt) && stmt.containsInvokeExpr()) {
                InvokeExpr iexpr = stmt.getInvokeExpr();
                if (!iexpr.getMethodRef().isConstructor()) {
                    List<Type> types = stmt.getInvokeExpr().getMethod().getParameterTypes();
                    for (int i = 0; i < types.size(); i++) {
                        Type type = types.get(i);
                        if (type.equals(this.INTENT_TYPE)) {
                            try {
                                assignIntent(this.component, stmt.getInvokeExpr().getMethod(), i);
                            } catch (Exception ex) {
                                this.logger.error("Assign Intent for " + stmt.getInvokeExpr().getMethod() + " fails.", (Throwable) ex);
                            }
                        }
                    }
                }
            }
        }
    }

    public void assignIntent(SootClass hostComponent, SootMethod method, int indexOfArgs) {
        if (method.isConcrete() && !method.isStatic()) {
            JimpleBody body = (JimpleBody) method.retrieveActiveBody();
            SootMethod m = hostComponent.getMethodUnsafe("android.content.Intent getIntent()");
            if (m != null) {
                UnitPatchingChain units = body.getUnits();
                Local thisLocal = body.getThisLocal();
                Local intentV = body.getParameterLocal(indexOfArgs);
                Iterator<Unit> iter = units.snapshotIterator();
                while (iter.hasNext()) {
                    Stmt stmt = (Stmt) iter.next();
                    if (stmt.getTag(SimulatedCodeElementTag.TAG_NAME) != null && stmt.containsInvokeExpr() && stmt.getInvokeExpr().getMethod().equals(m)) {
                        return;
                    }
                }
                Stmt stmt2 = body.getFirstNonIdentityStmt();
                AssignStmt newAssignStmt = Jimple.v().newAssignStmt(intentV, Jimple.v().newVirtualInvokeExpr(thisLocal, m.makeRef()));
                newAssignStmt.addTag(SimulatedCodeElementTag.TAG);
                units.insertBefore(newAssignStmt, (AssignStmt) stmt2);
            }
        }
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootMethod> getAdditionalMethods() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootField> getAdditionalFields() {
        if (this.intentField == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(this.intentField);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addCallbackMethods() {
        return addCallbackMethods(null, "");
    }

    protected boolean addCallbackMethods(Set<SootClass> referenceClasses, String callbackSignature) {
        if (this.callbacks == null) {
            return false;
        }
        RefType rtView = RefType.v("android.view.View");
        Value viewVal = getValueForType(rtView, referenceClasses, null, null, true);
        if (viewVal instanceof Local) {
            this.localVarsForClasses.put(rtView.getSootClass(), (Local) viewVal);
        }
        MultiMap<SootClass, SootMethod> callbackClasses = getCallbackMethods(callbackSignature);
        HashSet<SootClass> hashSet = new HashSet();
        if (hashSet == null || hashSet.isEmpty()) {
            hashSet.add(this.component);
        } else {
            hashSet.addAll(hashSet);
            hashSet.add(this.component);
        }
        hashSet.add(rtView.getSootClass());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        boolean callbackFound = false;
        for (SootClass callbackClass : callbackClasses.keySet()) {
            Set<SootMethod> callbackMethods = callbackClasses.get(callbackClass);
            boolean hasParentClass = false;
            for (SootClass parentClass : hashSet) {
                Local parentLocal = this.localVarsForClasses.get(parentClass);
                if (isCompatible(parentClass, callbackClass)) {
                    addSingleCallbackMethod(hashSet, callbackMethods, callbackClass, parentLocal);
                    callbackFound = true;
                    hasParentClass = true;
                }
            }
            if (!hasParentClass) {
                Local classLocal = this.localVarsForClasses.get(callbackClass);
                Set<Local> tempLocals = new HashSet<>();
                if (classLocal == null) {
                    classLocal = generateClassConstructor(callbackClass, new HashSet<>(), hashSet, tempLocals);
                    if (classLocal == null) {
                    }
                }
                addSingleCallbackMethod(hashSet, callbackMethods, callbackClass, classLocal);
                callbackFound = true;
                for (Local tempLocal : tempLocals) {
                    this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(tempLocal, NullConstant.v()));
                }
            }
        }
        if (callbackFound) {
            createIfStmt(newNopStmt);
        }
        return callbackFound;
    }

    private MultiMap<SootClass, SootMethod> getCallbackMethods(String callbackSignature) {
        MultiMap<SootClass, SootMethod> callbackClasses = new HashMultiMap<>();
        for (SootMethod theMethod : this.callbacks) {
            if (callbackSignature.isEmpty() || callbackSignature.equals(theMethod.getSubSignature())) {
                if (!this.entryPointUtils.isEntryPointMethod(theMethod)) {
                    callbackClasses.put(theMethod.getDeclaringClass(), theMethod);
                }
            }
        }
        return callbackClasses;
    }

    private void addSingleCallbackMethod(Set<SootClass> referenceClasses, Set<SootMethod> callbackMethods, SootClass callbackClass, Local classLocal) {
        for (SootMethod callbackMethod : callbackMethods) {
            NopStmt thenStmt = Jimple.v().newNopStmt();
            createIfStmt(thenStmt);
            buildMethodCall(callbackMethod, classLocal, referenceClasses);
            this.body.getUnits().add((UnitPatchingChain) thenStmt);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void reset() {
        super.reset();
        this.component.removeField(this.intentField);
        this.intentField = null;
    }

    public ComponentEntryPointInfo getComponentInfo() {
        ComponentEntryPointInfo info = new ComponentEntryPointInfo(this.mainMethod);
        info.setIntentField(this.intentField);
        return info;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createGetIntentMethod() {
        if (this.component.declaresMethod("android.content.Intent getIntent()")) {
            return;
        }
        Type intentType = RefType.v("android.content.Intent");
        SootMethod sm = Scene.v().makeSootMethod("getIntent", Collections.emptyList(), intentType, 1);
        sm.addTag(SimulatedCodeElementTag.TAG);
        this.component.addMethod(sm);
        sm.addTag(SimulatedCodeElementTag.TAG);
        JimpleBody b = Jimple.v().newBody(sm);
        sm.setActiveBody(b);
        b.insertIdentityStmts();
        LocalGenerator localGen = Scene.v().createLocalGenerator(b);
        Local lcIntent = localGen.generateLocal(intentType);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(lcIntent, Jimple.v().newInstanceFieldRef(b.getThisLocal(), this.intentField.makeRef())));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(lcIntent));
    }
}
