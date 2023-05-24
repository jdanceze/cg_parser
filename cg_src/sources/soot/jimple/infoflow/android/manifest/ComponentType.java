package soot.jimple.infoflow.android.manifest;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/ComponentType.class */
public enum ComponentType {
    Activity,
    Service,
    ContentProvider,
    BroadcastReceiver;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ComponentType[] valuesCustom() {
        ComponentType[] valuesCustom = values();
        int length = valuesCustom.length;
        ComponentType[] componentTypeArr = new ComponentType[length];
        System.arraycopy(valuesCustom, 0, componentTypeArr, 0, length);
        return componentTypeArr;
    }
}
