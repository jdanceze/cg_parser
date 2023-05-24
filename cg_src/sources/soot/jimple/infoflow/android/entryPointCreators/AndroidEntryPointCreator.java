package soot.jimple.infoflow.android.entryPointCreators;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointUtils;
import soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.ActivityEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.BroadcastReceiverEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointCollection;
import soot.jimple.infoflow.android.entryPointCreators.components.ContentProviderEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.FragmentEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.ServiceConnectionEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.ServiceEntryPointCreator;
import soot.jimple.infoflow.android.manifest.IAndroidApplication;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.cfg.LibraryClassPatcher;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.entryPointCreators.IEntryPointCreator;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.options.Options;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/AndroidEntryPointCreator.class */
public class AndroidEntryPointCreator extends AbstractAndroidEntryPointCreator implements IEntryPointCreator {
    private final Logger logger;
    private static final boolean DEBUG = false;
    protected MultiMap<SootClass, SootMethod> callbackFunctions;
    private SootClass applicationClass;
    private Local applicationLocal;
    private MultiMap<SootClass, String> activityLifecycleCallbacks;
    private MultiMap<SootClass, String> applicationCallbackClasses;
    private Map<SootClass, SootField> callbackClassToField;
    private MultiMap<SootClass, SootClass> fragmentClasses;
    private final ComponentEntryPointCollection componentToInfo;
    private Collection<SootClass> components;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[AndroidEntryPointUtils.ComponentType.valuesCustom().length];
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.Activity.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.Application.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.BroadcastReceiver.ordinal()] = 5;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.ContentProvider.ordinal()] = 6;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.Fragment.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.GCMBaseIntentService.ordinal()] = 7;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.GCMListenerService.ordinal()] = 8;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.HostApduService.ordinal()] = 9;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.Plain.ordinal()] = 11;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.Service.ordinal()] = 3;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[AndroidEntryPointUtils.ComponentType.ServiceConnection.ordinal()] = 10;
        } catch (NoSuchFieldError unused11) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType = iArr2;
        return iArr2;
    }

    public AndroidEntryPointCreator(IManifestHandler manifest, Collection<SootClass> components) {
        super(manifest);
        this.logger = LoggerFactory.getLogger(getClass());
        this.callbackFunctions = new HashMultiMap();
        this.applicationClass = null;
        this.applicationLocal = null;
        this.activityLifecycleCallbacks = new HashMultiMap();
        this.applicationCallbackClasses = new HashMultiMap();
        this.callbackClassToField = new HashMap();
        this.fragmentClasses = null;
        this.componentToInfo = new ComponentEntryPointCollection();
        this.components = components;
        this.overwriteDummyMainMethod = true;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    protected SootMethod createDummyMainInternal() {
        AbstractComponentEntryPointCreator componentCreator;
        Set<SootClass> fragments;
        Local localVal;
        reset();
        this.logger.info(String.format("Creating Android entry point for %d components...", Integer.valueOf(this.components.size())));
        boolean hasContentProviders = false;
        NopStmt beforeContentProvidersStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) beforeContentProvidersStmt);
        for (SootClass currentClass : this.components) {
            if (this.entryPointUtils.getComponentType(currentClass) == AndroidEntryPointUtils.ComponentType.ContentProvider && (localVal = generateClassConstructor(currentClass)) != null) {
                this.localVarsForClasses.put(currentClass, localVal);
                NopStmt thenStmt = Jimple.v().newNopStmt();
                createIfStmt(thenStmt);
                searchAndBuildMethod(AndroidEntryPointConstants.CONTENTPROVIDER_ONCREATE, currentClass, localVal);
                this.body.getUnits().add((UnitPatchingChain) thenStmt);
                hasContentProviders = true;
            }
        }
        if (hasContentProviders) {
            createIfStmt(beforeContentProvidersStmt);
        }
        initializeApplicationClass();
        if (this.applicationClass != null) {
            this.applicationLocal = generateClassConstructor(this.applicationClass);
            this.localVarsForClasses.put(this.applicationClass, this.applicationLocal);
            if (this.applicationLocal != null) {
                this.localVarsForClasses.put(this.applicationClass, this.applicationLocal);
                boolean hasApplicationCallbacks = (this.applicationCallbackClasses == null || this.applicationCallbackClasses.isEmpty()) ? false : true;
                boolean hasActivityLifecycleCallbacks = (this.activityLifecycleCallbacks == null || this.activityLifecycleCallbacks.isEmpty()) ? false : true;
                if (hasApplicationCallbacks || hasActivityLifecycleCallbacks) {
                    NopStmt beforeCbCons = Jimple.v().newNopStmt();
                    this.body.getUnits().add((UnitPatchingChain) beforeCbCons);
                    if (hasApplicationCallbacks) {
                        createClassInstances(this.applicationCallbackClasses.keySet());
                    }
                    if (hasActivityLifecycleCallbacks) {
                        createClassInstances(this.activityLifecycleCallbacks.keySet());
                        for (SootClass sc : this.activityLifecycleCallbacks.keySet()) {
                            SootField fld = this.callbackClassToField.get(sc);
                            Local lc = this.localVarsForClasses.get(sc);
                            if (sc != null && lc != null) {
                                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(fld.makeRef()), lc));
                            }
                        }
                    }
                    createIfStmt(beforeCbCons);
                }
                searchAndBuildMethod("void onCreate()", this.applicationClass, this.applicationLocal);
                SootClass scApplicationHolder = LibraryClassPatcher.createOrGetApplicationHolder();
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(scApplicationHolder.getFieldByName("application").makeRef()), this.applicationLocal));
            }
        }
        NopStmt outerStartStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) outerStartStmt);
        Map<SootClass, SootMethod> fragmentToMainMethod = new HashMap<>();
        for (SootClass parentActivity : this.fragmentClasses.keySet()) {
            for (SootClass fragment : this.fragmentClasses.get(parentActivity)) {
                FragmentEntryPointCreator entryPointCreator = new FragmentEntryPointCreator(fragment, this.applicationClass, this.manifest);
                entryPointCreator.setDummyClassName(this.mainMethod.getDeclaringClass().getName());
                entryPointCreator.setCallbacks(this.callbackFunctions.get(fragment));
                SootMethod fragmentMethod = entryPointCreator.createDummyMain();
                fragmentToMainMethod.put(fragment, fragmentMethod);
                this.componentToInfo.put(fragment, fragmentMethod);
            }
        }
        for (SootClass currentClass2 : this.components) {
            currentClass2.setApplicationClass();
            AndroidEntryPointUtils.ComponentType componentType = this.entryPointUtils.getComponentType(currentClass2);
            NopStmt newNopStmt = Jimple.v().newNopStmt();
            NopStmt newNopStmt2 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt);
            switch ($SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType()[componentType.ordinal()]) {
                case 2:
                    Map<SootClass, SootMethod> curActivityToFragmentMethod = new HashMap<>();
                    if (this.fragmentClasses != null && (fragments = this.fragmentClasses.get(currentClass2)) != null && !fragments.isEmpty()) {
                        for (SootClass fragment2 : fragments) {
                            curActivityToFragmentMethod.put(fragment2, fragmentToMainMethod.get(fragment2));
                        }
                    }
                    componentCreator = new ActivityEntryPointCreator(currentClass2, this.applicationClass, this.activityLifecycleCallbacks, this.callbackClassToField, curActivityToFragmentMethod, this.manifest);
                    break;
                case 3:
                case 7:
                case 8:
                case 9:
                    componentCreator = new ServiceEntryPointCreator(currentClass2, this.applicationClass, this.manifest);
                    break;
                case 4:
                default:
                    componentCreator = new ServiceEntryPointCreator(currentClass2, this.applicationClass, this.manifest);
                    break;
                case 5:
                    componentCreator = new BroadcastReceiverEntryPointCreator(currentClass2, this.applicationClass, this.manifest);
                    break;
                case 6:
                    componentCreator = new ContentProviderEntryPointCreator(currentClass2, this.applicationClass, this.manifest);
                    break;
                case 10:
                    componentCreator = new ServiceConnectionEntryPointCreator(currentClass2, this.applicationClass, this.manifest);
                    break;
            }
            createIfStmt(newNopStmt2);
            if (componentCreator != null) {
                componentCreator.setDummyClassName(this.mainMethod.getDeclaringClass().getName());
                componentCreator.setCallbacks(this.callbackFunctions.get(currentClass2));
                SootMethod lifecycleMethod = componentCreator.createDummyMain();
                this.componentToInfo.put(currentClass2, componentCreator.getComponentInfo());
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(lifecycleMethod.makeRef(), Collections.singletonList(NullConstant.v()))));
            }
            createIfStmt(newNopStmt);
            this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        }
        if (this.applicationLocal != null) {
            NopStmt newNopStmt3 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt3);
            addApplicationCallbackMethods();
            createIfStmt(newNopStmt3);
        }
        createIfStmt(outerStartStmt);
        if (this.applicationLocal != null) {
            searchAndBuildMethod(AndroidEntryPointConstants.APPLICATION_ONTERMINATE, this.applicationClass, this.applicationLocal);
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        NopEliminator.v().transform(this.body);
        eliminateSelfLoops();
        eliminateFallthroughIfs(this.body);
        if (Options.v().validate()) {
            this.mainMethod.getActiveBody().validate();
        }
        return this.mainMethod;
    }

    private void initializeApplicationClass() {
        IAndroidApplication app = this.manifest.getApplication();
        if (app != null) {
            String applicationName = app.getName();
            if (applicationName == null || applicationName.isEmpty()) {
                return;
            }
            Iterator<SootClass> it = this.components.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SootClass currentClass = it.next();
                if (this.entryPointUtils.isApplicationClass(currentClass) && currentClass.getName().equals(applicationName)) {
                    if (this.applicationClass != null && currentClass != this.applicationClass) {
                        throw new RuntimeException("Multiple application classes in app");
                    }
                    this.applicationClass = currentClass;
                }
            }
        }
        if (this.applicationClass == null) {
            return;
        }
        SootClass scActCallbacks = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACKSINTERFACE);
        Collection<SootMethod> callbacks = this.callbackFunctions.get(this.applicationClass);
        if (callbacks != null) {
            for (SootMethod smCallback : callbacks) {
                if (scActCallbacks != null && Scene.v().getOrMakeFastHierarchy().canStoreType(smCallback.getDeclaringClass().getType(), scActCallbacks.getType())) {
                    this.activityLifecycleCallbacks.put(smCallback.getDeclaringClass(), smCallback.getSignature());
                } else {
                    this.applicationCallbackClasses.put(smCallback.getDeclaringClass(), smCallback.getSignature());
                }
            }
        }
        for (SootClass callbackClass : this.activityLifecycleCallbacks.keySet()) {
            String baseName = callbackClass.getName();
            if (baseName.contains(".")) {
                baseName = baseName.substring(baseName.lastIndexOf(".") + 1);
            }
            SootClass dummyMainClass = this.mainMethod.getDeclaringClass();
            int idx = 0;
            String fieldName = baseName;
            while (dummyMainClass.declaresFieldByName(fieldName)) {
                fieldName = String.valueOf(baseName) + "_" + idx;
                idx++;
            }
            SootField fld = Scene.v().makeSootField(fieldName, RefType.v(callbackClass), 10);
            this.mainMethod.getDeclaringClass().addField(fld);
            this.callbackClassToField.put(callbackClass, fld);
        }
    }

    private void eliminateFallthroughIfs(Body body) {
        boolean changed;
        do {
            changed = false;
            IfStmt ifs = null;
            Iterator<Unit> unitIt = body.getUnits().snapshotIterator();
            while (unitIt.hasNext()) {
                Unit u = unitIt.next();
                if (ifs != null && ifs.getTarget() == u) {
                    body.getUnits().remove(ifs);
                    changed = true;
                }
                ifs = null;
                if (u instanceof IfStmt) {
                    ifs = (IfStmt) u;
                }
            }
        } while (changed);
    }

    private void addApplicationCallbackMethods() {
        if (!this.callbackFunctions.containsKey(this.applicationClass) || this.applicationClass.isAbstract()) {
            return;
        }
        if (this.applicationClass.isPhantom()) {
            this.logger.warn("Skipping possible application callbacks in phantom class %s", this.applicationClass);
            return;
        }
        List<String> lifecycleMethods = AndroidEntryPointConstants.getApplicationLifecycleMethods();
        for (SootClass sc : this.applicationCallbackClasses.keySet()) {
            for (String methodSig : this.applicationCallbackClasses.get(sc)) {
                SootMethodAndClass methodAndClass = SootMethodRepresentationParser.v().parseSootMethodString(methodSig);
                String subSig = methodAndClass.getSubSignature();
                SootMethod method = findMethod(Scene.v().getSootClass(sc.getName()), subSig);
                if (sc != this.applicationClass || !lifecycleMethods.contains(subSig)) {
                    if (!this.activityLifecycleCallbacks.containsKey(sc) || !lifecycleMethods.contains(subSig)) {
                        if (method != null && !SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName())) {
                            Local local = this.localVarsForClasses.get(sc);
                            if (local == null) {
                                this.logger.warn(String.format("Could not create call to application callback %s. Local was null.", method.getSignature()));
                            } else {
                                NopStmt thenStmt = Jimple.v().newNopStmt();
                                createIfStmt(thenStmt);
                                buildMethodCall(method, local);
                                this.body.getUnits().add((UnitPatchingChain) thenStmt);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<String> getRequiredClasses() {
        Set<String> requiredClasses = new HashSet<>(this.components.size());
        for (SootClass sc : this.components) {
            requiredClasses.add(sc.getName());
        }
        return requiredClasses;
    }

    public void setFragments(MultiMap<SootClass, SootClass> fragments) {
        this.fragmentClasses = fragments;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootMethod> getAdditionalMethods() {
        return this.componentToInfo.getLifecycleMethods();
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootField> getAdditionalFields() {
        return this.componentToInfo.getAdditionalFields();
    }

    public ComponentEntryPointCollection getComponentToEntryPointInfo() {
        return this.componentToInfo;
    }

    public void setCallbackFunctions(MultiMap<SootClass, SootMethod> callbackFunctions) {
        this.callbackFunctions = callbackFunctions;
    }

    public MultiMap<SootClass, SootMethod> getCallbackFunctions() {
        return this.callbackFunctions;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void reset() {
        super.reset();
        for (SootMethod sm : getAdditionalMethods()) {
            if (sm.isDeclared()) {
                sm.getDeclaringClass().removeMethod(sm);
            }
        }
        for (SootField sf : getAdditionalFields()) {
            if (sf.isDeclared()) {
                sf.getDeclaringClass().removeField(sf);
            }
        }
        for (SootField fld : this.callbackClassToField.values()) {
            if (fld.isDeclared()) {
                fld.getDeclaringClass().removeField(fld);
            }
        }
        this.componentToInfo.clear();
        this.callbackClassToField.clear();
    }

    public void setComponents(Collection<SootClass> components) {
        this.components = components;
    }

    public void removeGeneratedMethods(boolean removeClass) {
        SootClass mainClass = this.mainMethod.getDeclaringClass();
        if (removeClass) {
            Scene.v().removeClass(mainClass);
        } else {
            mainClass.removeMethod(this.mainMethod);
        }
        for (SootMethod sm : getAdditionalMethods()) {
            if (sm.isDeclared()) {
                SootClass declaringClass = sm.getDeclaringClass();
                if (declaringClass.isInScene()) {
                    declaringClass.removeMethod(sm);
                }
            }
        }
    }
}
