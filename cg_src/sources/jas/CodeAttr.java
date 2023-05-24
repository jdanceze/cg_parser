package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/CodeAttr.class */
public class CodeAttr {
    static CP attr = new AsciiCP("Code");
    int code_size;
    Hashtable insn_pc;
    LineTableAttr ltab;
    LocalVarTableAttr lvar;
    Hashtable labels;
    Vector sootAttrNames = new Vector();
    Vector sootAttrValues = new Vector();
    short stack_size = 1;
    short num_locals = 1;
    Catchtable ctb = null;
    Vector insns = new Vector();
    Vector generic = new Vector();

    public void setCatchtable(Catchtable ctb) {
        this.ctb = ctb;
    }

    public void setLineTable(LineTableAttr ltab) {
        this.ltab = ltab;
    }

    public void setLocalVarTable(LocalVarTableAttr lvar) {
        this.lvar = lvar;
    }

    public void addGenericAttr(GenericAttr g) {
        this.generic.addElement(g);
    }

    public void addSootCodeAttr(String name, String value) {
        this.sootAttrNames.addElement(name);
        this.sootAttrValues.addElement(value);
    }

    Label getLabel(String name) {
        Label lab = (Label) this.labels.get(name);
        if (lab == null) {
            lab = new Label(name);
            this.labels.put(name, lab);
        }
        return lab;
    }

    public void setLabelTable(Hashtable labelTable) {
        this.labels = labelTable;
    }

    private int processSootAttributes() {
        Hashtable labelToPc = new Hashtable();
        int totalSize = 0;
        Enumeration enumeration = this.sootAttrValues.elements();
        Enumeration nameEnum = this.sootAttrNames.elements();
        while (enumeration.hasMoreElements()) {
            String attrValue = (String) enumeration.nextElement();
            String attrName = (String) nameEnum.nextElement();
            boolean isLabel = false;
            StringTokenizer st = new StringTokenizer(attrValue, "%", true);
            while (st.hasMoreTokens()) {
                String token = (String) st.nextElement();
                if (token.equals("%")) {
                    isLabel = !isLabel;
                } else if (isLabel) {
                    Integer i = (Integer) labelToPc.get(token);
                    if (i == null) {
                        try {
                            labelToPc.put(token, new Integer(getPc(getLabel(token))));
                        } catch (jasError e) {
                            throw new RuntimeException(e.toString());
                        }
                    }
                } else {
                    continue;
                }
            }
            byte[] data = CodeAttributeDecoder.decode(attrValue, labelToPc);
            GenericAttr ga = new GenericAttr(attrName, data);
            totalSize += ga.size();
            addGenericAttr(ga);
        }
        this.sootAttrNames.removeAllElements();
        this.sootAttrValues.removeAllElements();
        return totalSize;
    }

    public void addInsn(Insn insn) {
        this.insns.addElement(insn);
    }

    public void setStackSize(short stack_size) {
        this.stack_size = stack_size;
    }

    public void setVarSize(short num_vars) {
        this.num_locals = num_vars;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
        Enumeration en = this.insns.elements();
        while (en.hasMoreElements()) {
            Insn i = (Insn) en.nextElement();
            i.resolve(e);
        }
        if (this.ctb != null) {
            this.ctb.resolve(e);
        }
        if (this.ltab != null) {
            this.ltab.resolve(e);
        }
        if (this.lvar != null) {
            this.lvar.resolve(e);
        }
        Enumeration gen = this.generic.elements();
        while (gen.hasMoreElements()) {
            GenericAttr gattr = (GenericAttr) gen.nextElement();
            gattr.resolve(e);
        }
    }

    public int getPc(Insn i) throws jasError {
        Integer tmp;
        if (this.insn_pc == null) {
            throw new jasError("Internal error, insn_pc has not been initialized");
        }
        if (i instanceof Label) {
            tmp = (Integer) this.insn_pc.get(((Label) i).id);
        } else {
            tmp = (Integer) this.insn_pc.get(i);
        }
        if (tmp == null) {
            throw new jasError(i + " has not been added to the code");
        }
        return tmp.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        int code_size = 0;
        this.insn_pc = new Hashtable();
        Enumeration en = this.insns.elements();
        while (en.hasMoreElements()) {
            Insn now = (Insn) en.nextElement();
            if (now instanceof Label) {
                this.insn_pc.put(((Label) now).id, new Integer(code_size));
            } else {
                this.insn_pc.put(now, new Integer(code_size));
            }
            code_size += now.size(e, this);
        }
        int total_size = code_size;
        if (this.ctb != null) {
            total_size += this.ctb.size();
        }
        if (this.ltab != null) {
            total_size += this.ltab.size();
        }
        if (this.lvar != null) {
            total_size += this.lvar.size();
        }
        Enumeration gen = this.generic.elements();
        while (gen.hasMoreElements()) {
            GenericAttr gattr = (GenericAttr) gen.nextElement();
            total_size += gattr.size();
        }
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(total_size + processSootAttributes() + 12);
        out.writeShort(this.stack_size);
        out.writeShort(this.num_locals);
        out.writeInt(code_size);
        Enumeration en2 = this.insns.elements();
        while (en2.hasMoreElements()) {
            ((Insn) en2.nextElement()).write(e, this, out);
        }
        if (this.ctb != null) {
            this.ctb.write(e, this, out);
        } else {
            out.writeShort(0);
        }
        short extra = 0;
        if (this.ltab != null) {
            extra = (short) (0 + 1);
        }
        if (this.lvar != null) {
            extra = (short) (extra + 1);
        }
        out.writeShort((short) (extra + this.generic.size()));
        if (this.ltab != null) {
            this.ltab.write(e, this, out);
        }
        if (this.lvar != null) {
            this.lvar.write(e, this, out);
        }
        Enumeration gen2 = this.generic.elements();
        while (gen2.hasMoreElements()) {
            GenericAttr gattr2 = (GenericAttr) gen2.nextElement();
            gattr2.write(e, out);
        }
    }

    public String toString() {
        return "<#code-attr>";
    }
}
