package soot.jimple.infoflow.android.entryPointCreators;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/AndroidEntryPointUtils.class */
public class AndroidEntryPointUtils {
    private static final Logger logger = LoggerFactory.getLogger(AndroidEntryPointUtils.class);
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType;
    private Map<SootClass, ComponentType> componentTypeCache = new HashMap();
    private SootClass osClassApplication = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.APPLICATIONCLASS);
    private SootClass osClassActivity = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ACTIVITYCLASS);
    private SootClass osClassService = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.SERVICECLASS);
    private SootClass osClassFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.FRAGMENTCLASS);
    private SootClass osClassSupportFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.SUPPORTFRAGMENTCLASS);
    private SootClass osClassAndroidXFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ANDROIDXFRAGMENTCLASS);
    private SootClass osClassBroadcastReceiver = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.BROADCASTRECEIVERCLASS);
    private SootClass osClassContentProvider = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.CONTENTPROVIDERCLASS);
    private SootClass osClassGCMBaseIntentService = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.GCMBASEINTENTSERVICECLASS);
    private SootClass osClassGCMListenerService = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.GCMLISTENERSERVICECLASS);
    private SootClass osClassHostApduService = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.HOSTAPDUSERVICECLASS);
    private SootClass osInterfaceServiceConnection = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.SERVICECONNECTIONINTERFACE);
    private SootClass osClassMapActivity = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.MAPACTIVITYCLASS);

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/AndroidEntryPointUtils$ComponentType.class */
    public enum ComponentType {
        Application,
        Activity,
        Service,
        Fragment,
        BroadcastReceiver,
        ContentProvider,
        GCMBaseIntentService,
        GCMListenerService,
        HostApduService,
        ServiceConnection,
        Plain;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static ComponentType[] valuesCustom() {
            ComponentType[] valuesCustom = values();
            int length = valuesCustom.length;
            ComponentType[] componentTypeArr = new ComponentType[length];
            System.arraycopy(valuesCustom, 0, componentTypeArr, 0, length);
            return componentTypeArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ComponentType.valuesCustom().length];
        try {
            iArr2[ComponentType.Activity.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ComponentType.Application.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ComponentType.BroadcastReceiver.ordinal()] = 5;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ComponentType.ContentProvider.ordinal()] = 6;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ComponentType.Fragment.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ComponentType.GCMBaseIntentService.ordinal()] = 7;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ComponentType.GCMListenerService.ordinal()] = 8;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ComponentType.HostApduService.ordinal()] = 9;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[ComponentType.Plain.ordinal()] = 11;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[ComponentType.Service.ordinal()] = 3;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[ComponentType.ServiceConnection.ordinal()] = 10;
        } catch (NoSuchFieldError unused11) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType = iArr2;
        return iArr2;
    }

    public ComponentType getComponentType(SootClass currentClass) {
        if (this.componentTypeCache.containsKey(currentClass)) {
            return this.componentTypeCache.get(currentClass);
        }
        ComponentType ctype = ComponentType.Plain;
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        if (fh != null) {
            if (this.osClassFragment != null && Scene.v().getOrMakeFastHierarchy().canStoreType(currentClass.getType(), this.osClassFragment.getType())) {
                ctype = ComponentType.Fragment;
            } else if (this.osClassSupportFragment != null && fh.canStoreType(currentClass.getType(), this.osClassSupportFragment.getType())) {
                ctype = ComponentType.Fragment;
            } else if (this.osClassAndroidXFragment != null && fh.canStoreType(currentClass.getType(), this.osClassAndroidXFragment.getType())) {
                ctype = ComponentType.Fragment;
            } else if (this.osClassGCMBaseIntentService != null && fh.canStoreType(currentClass.getType(), this.osClassGCMBaseIntentService.getType())) {
                ctype = ComponentType.GCMBaseIntentService;
            } else if (this.osClassGCMListenerService != null && fh.canStoreType(currentClass.getType(), this.osClassGCMListenerService.getType())) {
                ctype = ComponentType.GCMListenerService;
            } else if (this.osClassHostApduService != null && fh.canStoreType(currentClass.getType(), this.osClassHostApduService.getType())) {
                ctype = ComponentType.HostApduService;
            } else if (this.osInterfaceServiceConnection != null && fh.canStoreType(currentClass.getType(), this.osInterfaceServiceConnection.getType())) {
                ctype = ComponentType.ServiceConnection;
            } else if (this.osClassMapActivity != null && fh.canStoreType(currentClass.getType(), this.osClassMapActivity.getType())) {
                ctype = ComponentType.Activity;
            } else if (this.osClassApplication != null && fh.canStoreType(currentClass.getType(), this.osClassApplication.getType())) {
                ctype = ComponentType.Application;
            } else if (this.osClassService != null && fh.canStoreType(currentClass.getType(), this.osClassService.getType())) {
                ctype = ComponentType.Service;
            } else if (this.osClassActivity != null && fh.canStoreType(currentClass.getType(), this.osClassActivity.getType())) {
                ctype = ComponentType.Activity;
            } else if (this.osClassBroadcastReceiver != null && fh.canStoreType(currentClass.getType(), this.osClassBroadcastReceiver.getType())) {
                ctype = ComponentType.BroadcastReceiver;
            } else if (this.osClassContentProvider != null && fh.canStoreType(currentClass.getType(), this.osClassContentProvider.getType())) {
                ctype = ComponentType.ContentProvider;
            }
        } else {
            logger.warn(String.format("No FastHierarchy, assuming %s is a plain class", currentClass.getName()));
        }
        this.componentTypeCache.put(currentClass, ctype);
        return ctype;
    }

    public boolean isApplicationClass(SootClass clazz) {
        return this.osClassApplication != null && Scene.v().getOrMakeFastHierarchy().canStoreType(clazz.getType(), this.osClassApplication.getType());
    }

    public boolean isEntryPointMethod(SootMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Given method is null");
        }
        ComponentType componentType = getComponentType(method.getDeclaringClass());
        String subsignature = method.getSubSignature();
        if (componentType == ComponentType.Activity && AndroidEntryPointConstants.getActivityLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.Service && AndroidEntryPointConstants.getServiceLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.Application && AndroidEntryPointConstants.getApplicationLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.Fragment && AndroidEntryPointConstants.getFragmentLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.BroadcastReceiver && AndroidEntryPointConstants.getBroadcastLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.ContentProvider && AndroidEntryPointConstants.getContentproviderLifecycleMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.GCMBaseIntentService && AndroidEntryPointConstants.getGCMIntentServiceMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.GCMListenerService && AndroidEntryPointConstants.getGCMListenerServiceMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.ServiceConnection && AndroidEntryPointConstants.getServiceConnectionMethods().contains(subsignature)) {
            return true;
        }
        if (componentType == ComponentType.HostApduService && AndroidEntryPointConstants.getHostApduServiceMethods().contains(subsignature)) {
            return true;
        }
        return false;
    }

    public Collection<? extends MethodOrMethodContext> getLifecycleMethods(SootClass sc) {
        return getLifecycleMethods(getComponentType(sc), sc);
    }

    public static Collection<? extends MethodOrMethodContext> getLifecycleMethods(ComponentType componentType, SootClass sc) {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$android$entryPointCreators$AndroidEntryPointUtils$ComponentType()[componentType.ordinal()]) {
            case 1:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getApplicationLifecycleMethods());
            case 2:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getActivityLifecycleMethods());
            case 3:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getServiceLifecycleMethods());
            case 4:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getFragmentLifecycleMethods());
            case 5:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getBroadcastLifecycleMethods());
            case 6:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getContentproviderLifecycleMethods());
            case 7:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getGCMIntentServiceMethods());
            case 8:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getGCMListenerServiceMethods());
            case 9:
            default:
                return Collections.emptySet();
            case 10:
                return getLifecycleMethods(sc, AndroidEntryPointConstants.getServiceConnectionMethods());
            case 11:
                return Collections.emptySet();
        }
    }

    private static Collection<? extends MethodOrMethodContext> getLifecycleMethods(SootClass sc, List<String> methods) {
        Set<MethodOrMethodContext> lifecycleMethods = new HashSet<>();
        SootClass sootClass = sc;
        while (true) {
            SootClass currentClass = sootClass;
            if (currentClass != null) {
                for (String sig : methods) {
                    SootMethod sm = currentClass.getMethodUnsafe(sig);
                    if (sm != null && !SystemClassHandler.v().isClassInSystemPackage(sm.getDeclaringClass().getName())) {
                        lifecycleMethods.add(sm);
                    }
                }
                sootClass = currentClass.hasSuperclass() ? currentClass.getSuperclass() : null;
            } else {
                return lifecycleMethods;
            }
        }
    }
}
