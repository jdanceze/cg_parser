package soot.jimple.infoflow.android.axml;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlElement.class */
public abstract class AXmlElement {
    protected boolean include = true;
    protected boolean added;
    String ns;

    public AXmlElement(String ns, boolean added) {
        this.ns = ns;
        this.added = added;
    }

    public String getNamespace() {
        return this.ns;
    }

    public void setNamespace(String ns) {
        this.ns = ns;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isAdded() {
        return this.added;
    }

    public boolean isIncluded() {
        return this.include;
    }

    public void include() {
        include(true);
    }

    public void exclude() {
        include(false);
    }

    protected void include(boolean include) {
        this.include = include;
    }
}
