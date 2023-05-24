package javax.servlet.jsp.tagext;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagAttributeInfo.class */
public class TagAttributeInfo {
    public static final String ID = "id";
    private String name;
    private String type;
    private boolean reqTime;
    private boolean required;
    private boolean fragment;

    public TagAttributeInfo(String name, boolean required, String type, boolean reqTime) {
        this.name = name;
        this.required = required;
        this.type = type;
        this.reqTime = reqTime;
    }

    public TagAttributeInfo(String name, boolean required, String type, boolean reqTime, boolean fragment) {
        this(name, required, type, reqTime);
        this.fragment = fragment;
    }

    public String getName() {
        return this.name;
    }

    public String getTypeName() {
        return this.type;
    }

    public boolean canBeRequestTime() {
        return this.reqTime;
    }

    public boolean isRequired() {
        return this.required;
    }

    public static TagAttributeInfo getIdAttribute(TagAttributeInfo[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].getName().equals("id")) {
                return a[i];
            }
        }
        return null;
    }

    public boolean isFragment() {
        return this.fragment;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(new StringBuffer().append("name = ").append(this.name).append(Instruction.argsep).toString());
        b.append(new StringBuffer().append("type = ").append(this.type).append(Instruction.argsep).toString());
        b.append(new StringBuffer().append("reqTime = ").append(this.reqTime).append(Instruction.argsep).toString());
        b.append(new StringBuffer().append("required = ").append(this.required).append(Instruction.argsep).toString());
        b.append(new StringBuffer().append("fragment = ").append(this.fragment).append(Instruction.argsep).toString());
        return b.toString();
    }
}
