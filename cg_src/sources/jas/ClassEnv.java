package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ClassEnv.class */
public class ClassEnv implements RuntimeConstants {
    CP this_class;
    CP super_class;
    short class_access;
    boolean hasSuperClass;
    InnerClassAttr inner_class_attr;
    boolean classSynth;
    SyntheticAttr synthAttr;
    EnclMethAttr encl_meth_attr;
    BootstrapMethodsAttribute bsm_attr;
    SourceAttr source = null;
    DeprecatedAttr deprAttr = null;
    SignatureAttr sigAttr = null;
    VisibilityAnnotationAttr visAnnotAttr = null;
    VisibilityAnnotationAttr invisAnnotAttr = null;
    int magic = RuntimeConstants.JAVA_MAGIC;
    short version_lo = 0;
    short version_hi = 46;
    Hashtable cpe = new Hashtable();
    Hashtable cpe_index = null;
    Vector interfaces = new Vector();
    Vector vars = new Vector();
    Vector methods = new Vector();
    Vector generic = new Vector();

    public ClassEnv() {
        this.bsm_attr = null;
        this.bsm_attr = null;
    }

    public void requireJava1_4() {
        this.version_lo = (short) Math.max((int) this.version_lo, 0);
        this.version_hi = (short) Math.max((int) this.version_hi, 48);
    }

    public void requireJava5() {
        this.version_lo = (short) Math.max((int) this.version_lo, 0);
        this.version_hi = (short) Math.max((int) this.version_hi, 49);
    }

    public void requireJava6() {
        this.version_lo = (short) Math.max((int) this.version_lo, 0);
        this.version_hi = (short) Math.max((int) this.version_hi, 50);
    }

    public void requireJava7() {
        this.version_lo = (short) Math.max((int) this.version_lo, 0);
        this.version_hi = (short) Math.max((int) this.version_hi, 51);
    }

    public void setClass(CP name) {
        this.this_class = name;
        addCPItem(name);
    }

    public void setNoSuperClass() {
        this.hasSuperClass = false;
    }

    public void setSuperClass(CP name) {
        this.hasSuperClass = true;
        this.super_class = name;
        addCPItem(name);
    }

    public void setClassAccess(short access) {
        this.class_access = access;
    }

    public void addInterface(CP ifc) {
        addCPItem(ifc);
        this.interfaces.addElement(ifc);
    }

    public void addInterface(CP[] ilist) {
        for (int i = 0; i < ilist.length; i++) {
            this.interfaces.addElement(ilist[i]);
            addCPItem(ilist[i]);
        }
    }

    public void addField(Var v) {
        this.vars.addElement(v);
        v.resolve(this);
    }

    public void write(DataOutputStream out) throws IOException, jasError {
        boolean needAnno = false;
        if (this.visAnnotAttr != null || this.invisAnnotAttr != null) {
            needAnno = true;
        }
        Iterator i = this.methods.iterator();
        while (i.hasNext()) {
            Method m = (Method) i.next();
            if (m.invis_annot_attr != null || m.vis_annot_attr != null || m.param_invis_annot_attr != null || m.param_vis_annot_attr != null) {
                needAnno = true;
            }
        }
        Iterator i2 = this.vars.iterator();
        while (i2.hasNext()) {
            Var f = (Var) i2.next();
            if (f.invis_annot_attr != null || f.vis_annot_attr != null) {
                needAnno = true;
            }
        }
        if (needAnno) {
            requireJava1_4();
        }
        out.writeInt(this.magic);
        out.writeShort(this.version_lo);
        out.writeShort(this.version_hi);
        int curidx = 1;
        this.cpe_index = new Hashtable();
        Enumeration e = this.cpe.elements();
        while (e.hasMoreElements()) {
            CP tmp = (CP) e.nextElement();
            this.cpe_index.put(tmp.getUniq(), new Integer(curidx));
            curidx++;
            if ((tmp instanceof LongCP) || (tmp instanceof DoubleCP)) {
                curidx++;
            }
        }
        out.writeShort((short) curidx);
        Enumeration e2 = this.cpe.elements();
        while (e2.hasMoreElements()) {
            CP now = (CP) e2.nextElement();
            now.write(this, out);
        }
        out.writeShort(this.class_access);
        out.writeShort(getCPIndex(this.this_class));
        if (this.hasSuperClass) {
            out.writeShort(getCPIndex(this.super_class));
        } else {
            out.writeShort(0);
        }
        out.writeShort(this.interfaces.size());
        Enumeration e3 = this.interfaces.elements();
        while (e3.hasMoreElements()) {
            CP c = (CP) e3.nextElement();
            out.writeShort(getCPIndex(c));
        }
        out.writeShort(this.vars.size());
        Enumeration e4 = this.vars.elements();
        while (e4.hasMoreElements()) {
            Var v = (Var) e4.nextElement();
            v.write(this, out);
        }
        out.writeShort(this.methods.size());
        Enumeration e5 = this.methods.elements();
        while (e5.hasMoreElements()) {
            ((Method) e5.nextElement()).write(this, out);
        }
        short numExtra = 0;
        if (this.source != null) {
            numExtra = 1;
        }
        short numExtra2 = (short) (numExtra + this.generic.size());
        if (this.inner_class_attr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (isClassSynth()) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.deprAttr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.sigAttr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.visAnnotAttr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.invisAnnotAttr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.encl_meth_attr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        if (this.bsm_attr != null) {
            numExtra2 = (short) (numExtra2 + 1);
        }
        out.writeShort(numExtra2);
        if (this.source != null) {
            this.source.write(this, out);
        }
        Enumeration gen = this.generic.elements();
        while (gen.hasMoreElements()) {
            GenericAttr gattr = (GenericAttr) gen.nextElement();
            gattr.write(this, out);
        }
        if (isClassSynth()) {
            this.synthAttr.write(this, out);
        }
        if (this.deprAttr != null) {
            this.deprAttr.write(this, out);
        }
        if (this.sigAttr != null) {
            this.sigAttr.write(this, out);
        }
        if (this.encl_meth_attr != null) {
            this.encl_meth_attr.write(this, out);
        }
        if (this.visAnnotAttr != null) {
            this.visAnnotAttr.write(this, out);
        }
        if (this.invisAnnotAttr != null) {
            this.invisAnnotAttr.write(this, out);
        }
        if (this.inner_class_attr != null) {
            this.inner_class_attr.write(this, out);
        }
        if (this.bsm_attr != null) {
            this.bsm_attr.write(this, out);
        }
        out.flush();
    }

    public void addCPItem(CP cp) {
        String uniq = cp.getUniq();
        if (((CP) this.cpe.get(uniq)) == null) {
            this.cpe.put(uniq, cp);
            cp.resolve(this);
        }
    }

    public void addMethod(short acc, String name, String sig, CodeAttr code, ExceptAttr ex) {
        Method x = new Method(acc, new AsciiCP(name), new AsciiCP(sig), code, ex);
        x.resolve(this);
        this.methods.addElement(x);
    }

    public void setClassSynth(boolean b) {
        this.classSynth = b;
        this.synthAttr = new SyntheticAttr();
        this.synthAttr.resolve(this);
    }

    public boolean isClassSynth() {
        return this.classSynth;
    }

    public void setClassDepr(DeprecatedAttr d) {
        this.deprAttr = d;
        this.deprAttr.resolve(this);
    }

    public void setClassSigAttr(SignatureAttr s) {
        this.sigAttr = s;
        this.sigAttr.resolve(this);
    }

    public void setClassAnnotAttrVis(VisibilityAnnotationAttr s) {
        this.visAnnotAttr = s;
        this.visAnnotAttr.resolve(this);
    }

    public void setClassAnnotAttrInvis(VisibilityAnnotationAttr s) {
        this.invisAnnotAttr = s;
        this.invisAnnotAttr.resolve(this);
    }

    public void finishInnerClassAttr(InnerClassAttr attr) {
        this.inner_class_attr = attr;
        this.inner_class_attr.resolve(this);
    }

    public void addEnclMethAttr(EnclMethAttr attr) {
        this.encl_meth_attr = attr;
        this.encl_meth_attr.resolve(this);
    }

    public void setSource(SourceAttr source) {
        this.source = source;
        source.resolve(this);
    }

    public void setSource(String source) {
        if (source == null) {
            this.source = null;
            return;
        }
        this.source = new SourceAttr(source);
        this.source.resolve(this);
    }

    public void addGenericAttr(GenericAttr g) {
        this.generic.addElement(g);
        g.resolve(this);
    }

    public void addMethod(Method m) {
        m.resolve(this);
        this.methods.addElement(m);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCPIndex(CP cp) throws jasError {
        if (this.cpe_index == null) {
            throw new jasError("Internal error: CPE index has not been generated");
        }
        Integer idx = (Integer) this.cpe_index.get(cp.getUniq());
        if (idx == null) {
            throw new jasError("Item " + cp + " not in the class");
        }
        return idx.intValue();
    }

    public int addBootstrapMethod(MethodHandleCP bsm, CP[] argCPs) {
        addCPItem(bsm);
        for (CP cp : argCPs) {
            addCPItem(cp);
        }
        if (this.bsm_attr == null) {
            this.bsm_attr = new BootstrapMethodsAttribute();
            this.bsm_attr.resolve(this);
        }
        requireJava7();
        return this.bsm_attr.addEntry(bsm, argCPs);
    }
}
