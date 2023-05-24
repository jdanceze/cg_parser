package soot.jimple.infoflow.android.manifest.binary;

import java.util.Map;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.AXmlNode;
import soot.jimple.infoflow.android.manifest.BaseProcessManifest;
import soot.jimple.infoflow.android.manifest.IAndroidComponent;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/binary/AbstractBinaryAndroidComponent.class */
public abstract class AbstractBinaryAndroidComponent implements IAndroidComponent {
    protected final AXmlNode node;
    protected final BaseProcessManifest<?, ?, ?, ?> manifest;
    protected boolean enabled;
    protected boolean exported;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractBinaryAndroidComponent(AXmlNode node, BaseProcessManifest<?, ?, ?, ?> manifest) {
        this.node = node;
        this.manifest = manifest;
        AXmlAttribute<?> attrEnabled = node.getAttribute("enabled");
        this.enabled = attrEnabled == null || !attrEnabled.getValue().equals(Boolean.FALSE);
        loadIntentFilters();
        AXmlAttribute<?> attrExported = node.getAttribute("exported");
        if (attrExported == null) {
            this.exported = getExportedDefault();
        } else {
            this.exported = !attrExported.getValue().equals(Boolean.FALSE);
        }
    }

    protected void loadIntentFilters() {
    }

    protected boolean getExportedDefault() {
        return false;
    }

    public AXmlNode getAXmlNode() {
        return this.node;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidComponent
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidComponent
    public boolean isExported() {
        return this.exported;
    }

    @Override // soot.jimple.infoflow.android.manifest.IAndroidComponent
    public String getNameString() {
        String attrValueName;
        AXmlAttribute<?> attr = this.node.getAttribute("name");
        if (attr != null) {
            return this.manifest.expandClassName((String) attr.getValue());
        }
        for (Map.Entry<String, AXmlAttribute<?>> a : this.node.getAttributes().entrySet()) {
            AXmlAttribute<?> attrValue = a.getValue();
            if (attrValue != null && ((attrValueName = attrValue.getName()) == null || attrValueName.isEmpty())) {
                if (attrValue.getType() == 3) {
                    String name = (String) attrValue.getValue();
                    if (isValidComponentName(name)) {
                        String expandedName = this.manifest.expandClassName(name);
                        if (!SystemClassHandler.v().isClassInSystemPackage(expandedName)) {
                            return expandedName;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private boolean isValidComponentName(String name) {
        if (name != null && !name.isEmpty() && !name.equals("true") && !name.equals("false") && !Character.isDigit(name.charAt(0)) && name.startsWith(".")) {
            return true;
        }
        return false;
    }
}
