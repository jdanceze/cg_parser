package soot.jimple.infoflow.android.resources.controls;

import android.accounts.AccountManager;
import android.content.ContentResolver;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import soot.SootClass;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.flags.InputType;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/controls/EditTextControl.class */
public class EditTextControl extends AndroidLayoutControl {
    protected static final ISourceSinkDefinition UI_PASSWORD_SOURCE_DEF = new MethodSourceSinkDefinition(null, null, Collections.singleton(AccessPathTuple.fromPathElements("android.widget.TextView", Collections.singletonList(ContentResolver.SCHEME_CONTENT), Collections.singletonList("android.text.Editable"), SourceSinkType.Source)), MethodSourceSinkDefinition.CallType.MethodCall);
    protected static final ISourceSinkDefinition UI_ELEMENT_SOURCE_DEF = new MethodSourceSinkDefinition(null, null, Collections.singleton(AccessPathTuple.fromPathElements("android.widget.TextView", Collections.singletonList(ContentResolver.SCHEME_CONTENT), Collections.singletonList("android.text.Editable"), SourceSinkType.Source)), MethodSourceSinkDefinition.CallType.MethodCall);
    private int inputType;
    private boolean isPassword;
    private String text;
    private Collection<Integer> inputTypeFlags;

    static {
        UI_PASSWORD_SOURCE_DEF.setCategory(new ISourceSinkCategory() { // from class: soot.jimple.infoflow.android.resources.controls.EditTextControl.1
            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getHumanReadableDescription() {
                return "Password Input";
            }

            public String toString() {
                return "Password Input";
            }

            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getID() {
                return "PASSWORD";
            }
        });
        UI_ELEMENT_SOURCE_DEF.setCategory(new ISourceSinkCategory() { // from class: soot.jimple.infoflow.android.resources.controls.EditTextControl.2
            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getHumanReadableDescription() {
                return "UI Element";
            }

            public String toString() {
                return "UI Element";
            }

            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getID() {
                return "UIELEMENT";
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EditTextControl(SootClass viewClass) {
        super(viewClass);
    }

    public EditTextControl(int id, SootClass viewClass) {
        super(id, viewClass);
    }

    public EditTextControl(int id, SootClass viewClass, Map<String, Object> additionalAttributes) {
        super(id, viewClass, additionalAttributes);
    }

    void setInputType(int inputType) {
        this.inputType = inputType;
        this.inputTypeFlags = InputType.getFlags(inputType);
    }

    public int getInputType() {
        return this.inputType;
    }

    public boolean satisfiesInputType(int... type) {
        if (this.inputTypeFlags == null) {
            return false;
        }
        for (int i : type) {
            if (!this.inputTypeFlags.contains(Integer.valueOf(i))) {
                return false;
            }
        }
        return true;
    }

    public String getText() {
        return this.text;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl
    public void handleAttribute(AXmlAttribute<?> attribute, boolean loadOptionalData) {
        String attrName = attribute.getName().trim();
        int type = attribute.getType();
        if (attrName.equals("inputType") && type == 17) {
            setInputType(((Integer) attribute.getValue()).intValue());
        } else if (attrName.equals(AccountManager.KEY_PASSWORD)) {
            if (type == 17) {
                this.isPassword = ((Integer) attribute.getValue()).intValue() != 0;
            } else if (type == 18) {
                this.isPassword = ((Boolean) attribute.getValue()).booleanValue();
            } else {
                throw new RuntimeException("Unknown representation of boolean data type");
            }
        } else if (loadOptionalData && type == 3 && attrName.equals("text")) {
            this.text = (String) attribute.getValue();
        } else {
            super.handleAttribute(attribute, loadOptionalData);
        }
    }

    @Override // soot.jimple.infoflow.resources.controls.LayoutControl
    public boolean isSensitive() {
        if (this.isPassword || satisfiesInputType(InputType.numberPassword) || satisfiesInputType(InputType.textVisiblePassword) || satisfiesInputType(InputType.textWebPassword) || satisfiesInputType(InputType.textPassword)) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.resources.controls.LayoutControl
    public ISourceSinkDefinition getSourceDefinition() {
        return isSensitive() ? UI_PASSWORD_SOURCE_DEF : UI_ELEMENT_SOURCE_DEF;
    }

    @Override // soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * result) + this.inputType)) + (this.isPassword ? 1231 : 1237))) + (this.text == null ? 0 : this.text.hashCode());
    }

    @Override // soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        EditTextControl other = (EditTextControl) obj;
        if (this.inputType != other.inputType || this.isPassword != other.isPassword) {
            return false;
        }
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
            return true;
        } else if (!this.text.equals(other.text)) {
            return false;
        } else {
            return true;
        }
    }
}
