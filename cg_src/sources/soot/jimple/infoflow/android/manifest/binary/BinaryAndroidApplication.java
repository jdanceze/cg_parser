package soot.jimple.infoflow.android.manifest.binary;

import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.AXmlNode;
import soot.jimple.infoflow.android.manifest.BaseProcessManifest;
import soot.jimple.infoflow.android.manifest.IAndroidApplication;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/binary/BinaryAndroidApplication.class */
public class BinaryAndroidApplication implements IAndroidApplication {
    protected final AXmlNode node;
    protected final BaseProcessManifest<?, ?, ?, ?> manifest;
    protected boolean enabled;
    protected boolean debuggable;
    protected boolean allowBackup;
    protected String name;
    protected Boolean usesCleartextTraffic;

    public BinaryAndroidApplication(AXmlNode node, BaseProcessManifest<?, ?, ?, ?> manifest) {
        this.node = node;
        this.manifest = manifest;
        AXmlAttribute<?> attrEnabled = node.getAttribute("enabled");
        this.enabled = attrEnabled == null || !attrEnabled.getValue().equals(Boolean.FALSE);
        AXmlAttribute<?> attrDebuggable = node.getAttribute("debuggable");
        this.debuggable = attrDebuggable != null && attrDebuggable.getValue().equals(Boolean.TRUE);
        AXmlAttribute<?> attrAllowBackup = node.getAttribute("allowBackup");
        this.allowBackup = attrAllowBackup != null && attrAllowBackup.getValue().equals(Boolean.TRUE);
        AXmlAttribute<?> attrCleartextTraffic = node.getAttribute("usesCleartextTraffic");
        if (attrCleartextTraffic != null) {
            this.usesCleartextTraffic = Boolean.valueOf(attrCleartextTraffic.getValue().equals(Boolean.TRUE));
        }
        this.name = loadApplicationName();
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidApplication
    public boolean isEnabled() {
        return this.enabled;
    }

    private String loadApplicationName() {
        Object value;
        AXmlAttribute<?> attr = this.node.getAttribute("name");
        if (attr != null && (value = attr.getValue()) != null) {
            if (value instanceof String) {
                return this.manifest.expandClassName((String) attr.getValue());
            }
            if (value instanceof Integer) {
                ARSCFileParser.AbstractResource res = this.manifest.getArscParser().findResource(((Integer) attr.getValue()).intValue());
                if (res instanceof ARSCFileParser.StringResource) {
                    ARSCFileParser.StringResource strRes = (ARSCFileParser.StringResource) res;
                    return strRes.getValue();
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidApplication
    public String getName() {
        return this.name;
    }

    public AXmlNode getAXmlNode() {
        return this.node;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidApplication
    public boolean isDebuggable() {
        return this.debuggable;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidApplication
    public boolean isAllowBackup() {
        return this.allowBackup;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidApplication
    public Boolean isUsesCleartextTraffic() {
        return this.usesCleartextTraffic;
    }
}
