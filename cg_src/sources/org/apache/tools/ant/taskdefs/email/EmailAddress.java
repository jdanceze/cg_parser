package org.apache.tools.ant.taskdefs.email;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/EmailAddress.class */
public class EmailAddress {
    private String name;
    private String address;

    public EmailAddress() {
    }

    public EmailAddress(String email) {
        int len = email.length();
        if (len > 9 && ((email.charAt(0) == '<' || email.charAt(1) == '<') && (email.charAt(len - 1) == '>' || email.charAt(len - 2) == '>'))) {
            this.address = trim(email, true);
            return;
        }
        int paramDepth = 0;
        int start = 0;
        int end = 0;
        int nStart = 0;
        int nEnd = 0;
        for (int i = 0; i < len; i++) {
            char c = email.charAt(i);
            if (c == '(') {
                paramDepth++;
                if (start == 0) {
                    end = i;
                    nStart = i + 1;
                }
            } else if (c == ')') {
                paramDepth--;
                if (end == 0) {
                    start = i + 1;
                    nEnd = i;
                }
            } else if (paramDepth == 0 && c == '<') {
                nEnd = start == 0 ? i : nEnd;
                start = i + 1;
            } else if (paramDepth == 0 && c == '>') {
                end = i;
                if (end != len - 1) {
                    nStart = i + 1;
                }
            }
        }
        end = end == 0 ? len : end;
        nEnd = nEnd == 0 ? len : nEnd;
        this.address = trim(email.substring(start, end), true);
        this.name = trim(email.substring(nStart, nEnd), false);
        if (this.name.length() + this.address.length() > len) {
            this.name = null;
        }
    }

    private String trim(String t, boolean trimAngleBrackets) {
        boolean trim;
        int start = 0;
        int end = t.length();
        do {
            trim = false;
            if (t.charAt(end - 1) == ')' || ((t.charAt(end - 1) == '>' && trimAngleBrackets) || ((t.charAt(end - 1) == '\"' && t.charAt(end - 2) != '\\') || t.charAt(end - 1) <= ' '))) {
                trim = true;
                end--;
            }
            if (t.charAt(start) == '(' || ((t.charAt(start) == '<' && trimAngleBrackets) || t.charAt(start) == '\"' || t.charAt(start) <= ' ')) {
                trim = true;
                start++;
            }
        } while (trim);
        return t.substring(start, end);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        if (this.name == null) {
            return this.address;
        }
        return this.name + " <" + this.address + ">";
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }
}
