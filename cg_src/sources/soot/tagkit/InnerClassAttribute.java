package soot.tagkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/tagkit/InnerClassAttribute.class */
public class InnerClassAttribute implements Tag {
    public static final String NAME = "InnerClassAttribute";
    private ArrayList<InnerClassTag> list;

    public InnerClassAttribute() {
        this.list = null;
    }

    public InnerClassAttribute(ArrayList<InnerClassTag> list) {
        this.list = list;
    }

    public String getClassSpecs() {
        if (this.list == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<InnerClassTag> it = this.list.iterator();
        while (it.hasNext()) {
            InnerClassTag ict = it.next();
            sb.append(".inner_class_spec_attr ");
            sb.append(ict.getInnerClass());
            sb.append(' ');
            sb.append(ict.getOuterClass());
            sb.append(' ');
            sb.append(ict.getShortName());
            sb.append(' ');
            sb.append(ict.getAccessFlags());
            sb.append(' ');
            sb.append(".end .inner_class_spec_attr ");
        }
        return sb.toString();
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        return new byte[1];
    }

    public List<InnerClassTag> getSpecs() {
        return this.list == null ? Collections.emptyList() : this.list;
    }

    public void add(InnerClassTag newt) {
        ArrayList<InnerClassTag> this_list = this.list;
        if (this_list == null) {
            ArrayList<InnerClassTag> arrayList = new ArrayList<>();
            this_list = arrayList;
            this.list = arrayList;
        } else {
            String newt_inner = newt.getInnerClass();
            int newt_accessFlags = newt.getAccessFlags();
            Iterator<InnerClassTag> it = this_list.iterator();
            while (it.hasNext()) {
                InnerClassTag ict = it.next();
                if (newt_inner.equals(ict.getInnerClass())) {
                    int ict_accessFlags = ict.getAccessFlags();
                    if (ict_accessFlags != 0 && newt_accessFlags > 0 && ict_accessFlags != newt_accessFlags) {
                        throw new RuntimeException("Error: trying to add an InnerClassTag twice with different access flags! (" + ict_accessFlags + " and " + newt_accessFlags + ")");
                    }
                    if (ict_accessFlags == 0 && newt_accessFlags != 0) {
                        this_list.remove(ict);
                        this_list.add(newt);
                        return;
                    }
                    return;
                }
            }
        }
        this_list.add(newt);
    }
}
