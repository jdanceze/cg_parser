package soot.jimple.infoflow.android.resources.controls;

import java.util.HashMap;
import java.util.Map;
import soot.SootClass;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.resources.controls.LayoutControl;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/controls/AndroidLayoutControl.class */
public abstract class AndroidLayoutControl extends LayoutControl {
    protected int id;
    protected SootClass viewClass;
    protected String clickListener;
    private Map<String, Object> additionalAttributes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AndroidLayoutControl(SootClass viewClass) {
        this(-1, viewClass);
    }

    public AndroidLayoutControl(int id, SootClass viewClass) {
        this.clickListener = null;
        this.additionalAttributes = null;
        this.id = id;
        this.viewClass = viewClass;
    }

    public AndroidLayoutControl(int id, SootClass viewClass, Map<String, Object> additionalAttributes) {
        this(id, viewClass);
        this.additionalAttributes = additionalAttributes;
    }

    public int getID() {
        return this.id;
    }

    public SootClass getViewClass() {
        return this.viewClass;
    }

    public void addAdditionalAttribute(String key, String value) {
        if (this.additionalAttributes != null) {
            this.additionalAttributes = new HashMap();
        }
        this.additionalAttributes.put(key, value);
    }

    public String getClickListener() {
        return this.clickListener;
    }

    public Map<String, Object> getAdditionalAttributes() {
        return this.additionalAttributes;
    }

    void setId(int id) {
        this.id = id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAttribute(AXmlAttribute<?> attribute, boolean loadOptionalData) {
        String attrName = attribute.getName().trim();
        int type = attribute.getType();
        if (attrName.equals("id") && (type == 1 || attribute.getType() == 17)) {
            this.id = ((Integer) attribute.getValue()).intValue();
        } else if (isActionListener(attrName) && type == 3 && (attribute.getValue() instanceof String)) {
            this.clickListener = ((String) attribute.getValue()).trim();
        } else if (loadOptionalData) {
            if (this.additionalAttributes == null) {
                this.additionalAttributes = new HashMap();
            }
            this.additionalAttributes.put(attrName, attribute.getValue());
        }
    }

    protected boolean isActionListener(String name) {
        return name.equals("onClick");
    }

    public String toString() {
        return String.valueOf(this.id) + " - " + this.viewClass;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.additionalAttributes == null ? 0 : this.additionalAttributes.hashCode());
        return (31 * ((31 * ((31 * result) + (this.clickListener == null ? 0 : this.clickListener.hashCode()))) + this.id)) + (this.viewClass == null ? 0 : this.viewClass.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AndroidLayoutControl other = (AndroidLayoutControl) obj;
        if (this.additionalAttributes == null) {
            if (other.additionalAttributes != null) {
                return false;
            }
        } else if (!this.additionalAttributes.equals(other.additionalAttributes)) {
            return false;
        }
        if (this.clickListener == null) {
            if (other.clickListener != null) {
                return false;
            }
        } else if (!this.clickListener.equals(other.clickListener)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (this.viewClass == null) {
            if (other.viewClass != null) {
                return false;
            }
            return true;
        } else if (!this.viewClass.equals(other.viewClass)) {
            return false;
        } else {
            return true;
        }
    }
}
