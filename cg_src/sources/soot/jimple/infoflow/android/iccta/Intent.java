package soot.jimple.infoflow.android.iccta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.jimple.infoflow.android.iccta.Ic3Data;
/* compiled from: App.java */
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Intent.class */
class Intent {
    private String component;
    private String componentPackage;
    private String componentClass;
    private String action;
    private String dataScheme;
    private String dataHost;
    private String dataPath;
    private String data;
    private int flags;
    private App app;
    private LoggingPoint point;
    private String authority;
    private String dataType;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind;
    private Set<String> categories = new HashSet();
    private Map<String, String> extras = new HashMap();
    private int dataPort = -1;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[Ic3Data.AttributeKind.valuesCustom().length];
        try {
            iArr2[Ic3Data.AttributeKind.ACTION.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.AUTHORITY.ordinal()] = 9;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.CATEGORY.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.CLASS.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.EXTRA.ordinal()] = 8;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.FLAG.ordinal()] = 15;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.HOST.ordinal()] = 10;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PACKAGE.ordinal()] = 3;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PATH.ordinal()] = 11;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PORT.ordinal()] = 12;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PRIORITY.ordinal()] = 16;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.QUERY.ordinal()] = 14;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.SCHEME.ordinal()] = 7;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.SSP.ordinal()] = 13;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.TYPE.ordinal()] = 5;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.URI.ordinal()] = 6;
        } catch (NoSuchFieldError unused16) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind = iArr2;
        return iArr2;
    }

    public Intent(App app, LoggingPoint point) {
        this.app = app;
        this.point = point;
    }

    public void seal() {
    }

    public LoggingPoint getLoggingPoint() {
        return this.point;
    }

    public boolean isImplicit() {
        if (this.component != null && !this.component.isEmpty() && !this.component.contains("*") && !this.component.contains("NULL-CONSTANT")) {
            return false;
        }
        return true;
    }

    /* renamed from: clone */
    public Intent m2718clone() {
        Intent intent = new Intent(this.app, this.point);
        intent.component = this.component;
        intent.componentPackage = this.componentPackage;
        intent.componentClass = this.componentClass;
        intent.action = this.action;
        Set<String> tmpCategories = new HashSet<>();
        for (String str : this.categories) {
            tmpCategories.add(str);
        }
        intent.categories = tmpCategories;
        Map<String, String> tmpExtras = new HashMap<>();
        for (Map.Entry<String, String> entry : this.extras.entrySet()) {
            tmpExtras.put(entry.getKey(), entry.getValue());
        }
        intent.extras = tmpExtras;
        intent.dataScheme = this.dataScheme;
        intent.dataHost = this.dataHost;
        intent.dataPort = this.dataPort;
        intent.dataPath = this.dataPath;
        intent.data = this.data;
        intent.flags = this.flags;
        intent.app = this.app;
        return intent;
    }

    public String toString() {
        return "Intent [component=" + this.component + ", componentPackage=" + this.componentPackage + ", componentClass=" + this.componentClass + ", action=" + this.action + ", categories=" + this.categories + ", dataScheme=" + this.dataScheme + ", dataHost=" + this.dataHost + ", dataPort=" + this.dataPort + ", dataPath=" + this.dataPath + ", data=" + this.data + "]";
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return toString().equals(o.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
        if (component.contains("/") && !component.startsWith("/")) {
            setComponentPackage(component.split("/")[0]);
            if (!component.endsWith("/")) {
                setComponentClass(component.split("/")[1]);
            }
        }
    }

    public String getComponentPackage() {
        return this.componentPackage;
    }

    public void setComponentPackage(String componentPackage) {
        this.componentPackage = componentPackage;
    }

    public String getComponentClass() {
        return this.componentClass;
    }

    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        if (action.equals("<INTENT>")) {
            return;
        }
        this.action = action;
    }

    public Set<String> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<String> categories) {
        categories.remove("<INTENT>");
        if (!categories.isEmpty()) {
            this.categories = categories;
        }
    }

    public Map<String, String> getExtras() {
        return this.extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public String getDataScheme() {
        return this.dataScheme;
    }

    public void setDataScheme(String dataScheme) {
        if (dataScheme.equals("(.*)")) {
            return;
        }
        this.dataScheme = dataScheme;
    }

    public String getDataHost() {
        return this.dataHost;
    }

    public void setDataHost(String dataHost) {
        if (dataHost.equals("(.*)")) {
            return;
        }
        this.dataHost = dataHost;
    }

    public int getDataPort() {
        return this.dataPort;
    }

    public void setDataPort(int dataPort) {
        if (dataPort == 0) {
            dataPort = -1;
        }
        this.dataPort = dataPort;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(String dataPath) {
        if (dataPath.equals("(.*)")) {
            return;
        }
        this.dataPath = dataPath;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        if (data.equals("(.*)")) {
            return;
        }
        this.data = data;
        if (data.contains("://")) {
            if (this.dataScheme == null) {
                this.dataScheme = data.substring(0, data.indexOf("://"));
                data = data.substring(data.indexOf("://") + 3);
                if (this.dataScheme.contains(".*")) {
                    this.dataScheme = null;
                }
            }
            if (!data.isEmpty()) {
                if (this.dataHost == null) {
                    if (data.contains("/")) {
                        this.dataHost = data.substring(0, data.indexOf("/"));
                    } else {
                        this.dataHost = data;
                    }
                    if (this.dataHost.contains(".*")) {
                        this.dataHost = null;
                    }
                }
                if (this.dataPath == null && !data.isEmpty()) {
                    if (data.contains("/")) {
                        this.dataPath = data.substring(data.indexOf("/") + 1);
                    } else {
                        this.dataPath = data;
                    }
                    if (this.dataPath.contains(".*")) {
                        this.dataPath = null;
                    }
                }
            }
        }
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setAuthority(String value) {
        this.authority = value;
    }

    public String getAuthority() {
        return this.authority;
    }

    public App getApp() {
        return this.app;
    }

    public boolean hasImpreciseValues() {
        String string = toString();
        return isImprecise(string);
    }

    private boolean isImprecise(String string) {
        if (string == null) {
            return false;
        }
        if (string.toLowerCase().contains("harvester") || string.contains(".*")) {
            return true;
        }
        return false;
    }

    public boolean hasImportantImpreciseValues() {
        return isImprecise(getAction()) || isImprecise(this.authority);
    }

    /* JADX WARN: Code restructure failed: missing block: B:266:0x0628, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component> resolve(java.util.List<soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component> r7) {
        /*
            Method dump skipped, instructions count: 1589
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.android.iccta.Intent.resolve(java.util.List):java.util.List");
    }

    public void setType(String value) {
        if (value.isEmpty() || value.equals("(.*)")) {
            return;
        }
        this.dataType = value;
    }

    public String getType() {
        return this.dataType;
    }
}
