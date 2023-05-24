package soot.jimple.toolkits.infoflow;

import android.os.Environment;
import java.util.ArrayList;
import java.util.List;
import soot.EquivalentValue;
import soot.RefLikeType;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/CallLocalityContext.class */
public class CallLocalityContext {
    List<EquivalentValue> nodes = new ArrayList();
    List<Boolean> isNodeLocal;

    public CallLocalityContext(List<EquivalentValue> nodes) {
        this.nodes.addAll(nodes);
        this.isNodeLocal = new ArrayList(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            this.isNodeLocal.add(i, Boolean.FALSE);
        }
    }

    public void setFieldLocal(EquivalentValue fieldRef) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (fieldRef.equals(this.nodes.get(i))) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.TRUE);
                return;
            }
        }
        this.nodes.add(fieldRef);
        this.isNodeLocal.add(Boolean.TRUE);
    }

    public void setFieldShared(EquivalentValue fieldRef) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (fieldRef.equals(this.nodes.get(i))) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.FALSE);
                return;
            }
        }
        this.nodes.add(fieldRef);
        this.isNodeLocal.add(Boolean.FALSE);
    }

    public void setAllFieldsLocal() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof InstanceFieldRef) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.TRUE);
            }
        }
    }

    public void setAllFieldsShared() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof InstanceFieldRef) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.FALSE);
            }
        }
    }

    public void setParamLocal(int index) {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ParameterRef) {
                ParameterRef pr = (ParameterRef) r;
                if (pr.getIndex() == index) {
                    this.isNodeLocal.remove(i);
                    this.isNodeLocal.add(i, Boolean.TRUE);
                }
            }
        }
    }

    public void setParamShared(int index) {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ParameterRef) {
                ParameterRef pr = (ParameterRef) r;
                if (pr.getIndex() == index) {
                    this.isNodeLocal.remove(i);
                    this.isNodeLocal.add(i, Boolean.FALSE);
                }
            }
        }
    }

    public void setAllParamsLocal() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ParameterRef) {
                ParameterRef pr = (ParameterRef) r;
                if (pr.getIndex() != -1) {
                    this.isNodeLocal.remove(i);
                    this.isNodeLocal.add(i, Boolean.TRUE);
                }
            }
        }
    }

    public void setAllParamsShared() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ParameterRef) {
                ParameterRef pr = (ParameterRef) r;
                if (pr.getIndex() != -1) {
                    this.isNodeLocal.remove(i);
                    this.isNodeLocal.add(i, Boolean.FALSE);
                }
            }
        }
    }

    public void setThisLocal() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ThisRef) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.TRUE);
            }
        }
    }

    public void setThisShared() {
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof ThisRef) {
                this.isNodeLocal.remove(i);
                this.isNodeLocal.add(i, Boolean.FALSE);
            }
        }
    }

    public void setReturnLocal() {
        setParamLocal(-1);
    }

    public void setReturnShared() {
        setParamShared(-1);
    }

    public List<Object> getLocalRefs() {
        List<Object> ret = new ArrayList<>();
        for (int i = 0; i < this.nodes.size(); i++) {
            if (this.isNodeLocal.get(i).booleanValue()) {
                ret.add(this.nodes.get(i));
            }
        }
        return ret;
    }

    public List<Object> getSharedRefs() {
        List<Object> ret = new ArrayList<>();
        for (int i = 0; i < this.nodes.size(); i++) {
            if (!this.isNodeLocal.get(i).booleanValue()) {
                ret.add(this.nodes.get(i));
            }
        }
        return ret;
    }

    public boolean isFieldLocal(EquivalentValue fieldRef) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (fieldRef.equals(this.nodes.get(i))) {
                return this.isNodeLocal.get(i).booleanValue();
            }
        }
        return false;
    }

    public boolean containsField(EquivalentValue fieldRef) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (fieldRef.equals(this.nodes.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean merge(CallLocalityContext other) {
        boolean isChanged = false;
        if (other.nodes.size() > this.nodes.size()) {
            isChanged = true;
            for (int i = this.nodes.size(); i < other.nodes.size(); i++) {
                this.nodes.add(other.nodes.get(i));
                this.isNodeLocal.add(other.isNodeLocal.get(i));
            }
        }
        for (int i2 = 0; i2 < other.nodes.size(); i2++) {
            Boolean temp = new Boolean(this.isNodeLocal.get(i2).booleanValue() && other.isNodeLocal.get(i2).booleanValue());
            if (!temp.equals(this.isNodeLocal.get(i2))) {
                isChanged = true;
            }
            this.isNodeLocal.remove(i2);
            this.isNodeLocal.add(i2, temp);
        }
        return isChanged;
    }

    public boolean equals(Object o) {
        if (o instanceof CallLocalityContext) {
            CallLocalityContext other = (CallLocalityContext) o;
            return this.isNodeLocal.equals(other.isNodeLocal);
        }
        return false;
    }

    public int hashCode() {
        return this.isNodeLocal.hashCode();
    }

    public boolean isAllShared(boolean refsOnly) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (!refsOnly && this.isNodeLocal.get(i).booleanValue()) {
                return false;
            }
            if ((this.nodes.get(i).getValue().getType() instanceof RefLikeType) && this.isNodeLocal.get(i).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String fieldrefs = "";
        String staticrefs = "";
        String paramrefs = "";
        String thisref = "";
        if (this.nodes.size() == 0) {
            return "Call Locality Context: NO NODES\n";
        }
        for (int i = 0; i < this.nodes.size(); i++) {
            Ref r = (Ref) this.nodes.get(i).getValue();
            if (r instanceof InstanceFieldRef) {
                fieldrefs = String.valueOf(fieldrefs) + r + ": " + (this.isNodeLocal.get(i).booleanValue() ? "local" : Environment.MEDIA_SHARED) + "\n";
            } else if (r instanceof StaticFieldRef) {
                staticrefs = String.valueOf(staticrefs) + r + ": " + (this.isNodeLocal.get(i).booleanValue() ? "local" : Environment.MEDIA_SHARED) + "\n";
            } else if (r instanceof ParameterRef) {
                paramrefs = String.valueOf(paramrefs) + r + ": " + (this.isNodeLocal.get(i).booleanValue() ? "local" : Environment.MEDIA_SHARED) + "\n";
            } else if (!(r instanceof ThisRef)) {
                return "Call Locality Context: HAS STRANGE NODE " + r + "\n";
            } else {
                thisref = String.valueOf(thisref) + r + ": " + (this.isNodeLocal.get(i).booleanValue() ? "local" : Environment.MEDIA_SHARED) + "\n";
            }
        }
        return "Call Locality Context: \n" + fieldrefs + paramrefs + thisref + staticrefs;
    }

    public String toShortString() {
        String ret = "[";
        for (int i = 0; i < this.nodes.size(); i++) {
            ret = String.valueOf(ret) + (this.isNodeLocal.get(i).booleanValue() ? "L" : "S");
        }
        return String.valueOf(ret) + "]";
    }
}
