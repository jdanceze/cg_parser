package soot.jimple.infoflow.android.resources.controls;

import java.util.Map;
import soot.Scene;
import soot.SootClass;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.AXmlHandler;
import soot.jimple.infoflow.android.axml.AXmlNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/controls/LayoutControlFactory.class */
public class LayoutControlFactory {
    private boolean loadAdditionalAttributes = false;
    private SootClass scEditText = null;

    public AndroidLayoutControl createLayoutControl(String layoutFile, SootClass layoutClass, AXmlNode node) {
        if (this.scEditText == null) {
            this.scEditText = Scene.v().getSootClassUnsafe("android.widget.EditText");
        }
        AndroidLayoutControl lc = createLayoutControl(layoutClass);
        applyAttributes(node, lc);
        return lc;
    }

    protected void applyAttributes(AXmlNode node, AndroidLayoutControl lc) {
        Map<String, AXmlAttribute<?>> attributes = node.getAttributes();
        for (Map.Entry<String, AXmlAttribute<?>> entry : attributes.entrySet()) {
            if (entry.getKey() != null) {
                String attrName = entry.getKey().trim();
                AXmlAttribute<?> attr = entry.getValue();
                if (!attrName.isEmpty() && isAndroidNamespace(attr.getNamespace())) {
                    lc.handleAttribute(attr, this.loadAdditionalAttributes);
                }
            }
        }
    }

    protected AndroidLayoutControl createLayoutControl(SootClass layoutClass) {
        if (this.scEditText != null && Scene.v().getFastHierarchy().canStoreType(layoutClass.getType(), this.scEditText.getType())) {
            return new EditTextControl(layoutClass);
        }
        return new GenericLayoutControl(layoutClass);
    }

    public void setLoadAdditionalAttributes(boolean loadAdditionalAttributes) {
        this.loadAdditionalAttributes = loadAdditionalAttributes;
    }

    protected boolean isAndroidNamespace(String ns) {
        if (ns == null) {
            return false;
        }
        String ns2 = ns.trim();
        if (ns2.startsWith("*")) {
            ns2 = ns2.substring(1);
        }
        if (!ns2.equals(AXmlHandler.ANDROID_NAMESPACE)) {
            return false;
        }
        return true;
    }
}
