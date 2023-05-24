package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/Var.class */
public class Var {
    short var_acc;
    CP name;
    CP sig;
    ConstAttr const_attr;
    SyntheticAttr synth_attr;
    DeprecatedAttr dep_attr;
    SignatureAttr sig_attr;
    VisibilityAnnotationAttr vis_annot_attr;
    VisibilityAnnotationAttr invis_annot_attr;
    Vector genericAttrs;

    public Var(short vacc, CP name, CP sig, ConstAttr cattr) {
        this.synth_attr = null;
        this.dep_attr = null;
        this.sig_attr = null;
        this.vis_annot_attr = null;
        this.invis_annot_attr = null;
        this.genericAttrs = new Vector();
        this.var_acc = vacc;
        this.name = name;
        this.sig = sig;
        this.const_attr = cattr;
    }

    public Var(short vacc, CP name, CP sig, ConstAttr cattr, SyntheticAttr sattr) {
        this.synth_attr = null;
        this.dep_attr = null;
        this.sig_attr = null;
        this.vis_annot_attr = null;
        this.invis_annot_attr = null;
        this.genericAttrs = new Vector();
        this.var_acc = vacc;
        this.name = name;
        this.sig = sig;
        this.const_attr = cattr;
        this.synth_attr = sattr;
    }

    public void addGenericAttr(GenericAttr g) {
        this.genericAttrs.addElement(g);
    }

    public void addDeprecatedAttr(DeprecatedAttr d) {
        this.dep_attr = d;
    }

    public void addSignatureAttr(SignatureAttr s) {
        this.sig_attr = s;
    }

    public void addVisibilityAnnotationAttrVis(VisibilityAnnotationAttr v) {
        this.vis_annot_attr = v;
    }

    public void addVisibilityAnnotationAttrInvis(VisibilityAnnotationAttr v) {
        this.invis_annot_attr = v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.name);
        e.addCPItem(this.sig);
        if (this.const_attr != null) {
            this.const_attr.resolve(e);
        }
        if (this.synth_attr != null) {
            this.synth_attr.resolve(e);
        }
        if (this.dep_attr != null) {
            this.dep_attr.resolve(e);
        }
        if (this.sig_attr != null) {
            this.sig_attr.resolve(e);
        }
        if (this.vis_annot_attr != null) {
            this.vis_annot_attr.resolve(e);
        }
        if (this.invis_annot_attr != null) {
            this.invis_annot_attr.resolve(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(this.var_acc);
        out.writeShort(e.getCPIndex(this.name));
        out.writeShort(e.getCPIndex(this.sig));
        int attrCnt = this.genericAttrs.size();
        if (this.const_attr != null) {
            attrCnt++;
        }
        if (this.synth_attr != null) {
            attrCnt++;
        }
        if (this.dep_attr != null) {
            attrCnt++;
        }
        if (this.sig_attr != null) {
            attrCnt++;
        }
        if (this.vis_annot_attr != null) {
            attrCnt++;
        }
        if (this.invis_annot_attr != null) {
            attrCnt++;
        }
        out.writeShort(attrCnt);
        if (this.const_attr != null) {
            this.const_attr.write(e, out);
        }
        if (this.synth_attr != null) {
            this.synth_attr.write(e, out);
        }
        if (this.dep_attr != null) {
            this.dep_attr.write(e, out);
        }
        if (this.sig_attr != null) {
            this.sig_attr.write(e, out);
        }
        if (this.vis_annot_attr != null) {
            this.vis_annot_attr.write(e, out);
        }
        if (this.invis_annot_attr != null) {
            this.invis_annot_attr.write(e, out);
        }
        Enumeration enu = this.genericAttrs.elements();
        while (enu.hasMoreElements()) {
            ((GenericAttr) enu.nextElement()).write(e, out);
        }
    }
}
