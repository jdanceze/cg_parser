package org.apache.tools.ant.taskdefs.cvslib;

import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/CvsVersion.class */
public class CvsVersion extends AbstractCvsTask {
    static final long VERSION_1_11_2 = 11102;
    static final long MULTIPLY = 100;
    private String clientVersion;
    private String serverVersion;
    private String clientVersionProperty;
    private String serverVersionProperty;

    public String getClientVersion() {
        return this.clientVersion;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }

    public void setClientVersionProperty(String clientVersionProperty) {
        this.clientVersionProperty = clientVersionProperty;
    }

    public void setServerVersionProperty(String serverVersionProperty) {
        this.serverVersionProperty = serverVersionProperty;
    }

    public boolean supportsCvsLogWithSOption() {
        if (this.serverVersion == null) {
            return false;
        }
        StringTokenizer tokenizer = new StringTokenizer(this.serverVersion, ".");
        long counter = 10000;
        long version = 0;
        while (tokenizer.hasMoreTokens()) {
            String s = tokenizer.nextToken();
            int i = 0;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                i++;
            }
            String s2 = s.substring(0, i);
            version += counter * Long.parseLong(s2);
            if (counter == 1) {
                break;
            }
            counter /= MULTIPLY;
        }
        return version >= VERSION_1_11_2;
    }

    @Override // org.apache.tools.ant.taskdefs.AbstractCvsTask, org.apache.tools.ant.Task
    public void execute() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        setOutputStream(bos);
        ByteArrayOutputStream berr = new ByteArrayOutputStream();
        setErrorStream(berr);
        setCommand("version");
        super.execute();
        String output = bos.toString();
        log("Received version response \"" + output + "\"", 4);
        StringTokenizer st = new StringTokenizer(output);
        boolean client = false;
        boolean server = false;
        String cvs = null;
        String cachedVersion = null;
        boolean haveReadAhead = false;
        while (true) {
            if (!haveReadAhead && !st.hasMoreTokens()) {
                break;
            }
            String currentToken = haveReadAhead ? cachedVersion : st.nextToken();
            haveReadAhead = false;
            if ("Client:".equals(currentToken)) {
                client = true;
            } else if ("Server:".equals(currentToken)) {
                server = true;
            } else if (currentToken.startsWith("(CVS") && currentToken.endsWith(")")) {
                cvs = currentToken.length() == 5 ? "" : Instruction.argsep + currentToken;
            }
            if (!client && !server && cvs != null && cachedVersion == null && st.hasMoreTokens()) {
                cachedVersion = st.nextToken();
                haveReadAhead = true;
            } else if (client && cvs != null) {
                if (st.hasMoreTokens()) {
                    this.clientVersion = st.nextToken() + cvs;
                }
                client = false;
                cvs = null;
            } else if (server && cvs != null) {
                if (st.hasMoreTokens()) {
                    this.serverVersion = st.nextToken() + cvs;
                }
                server = false;
                cvs = null;
            } else if ("(client/server)".equals(currentToken) && cvs != null && cachedVersion != null && !client && !server) {
                server = true;
                client = true;
                String str = cachedVersion + cvs;
                this.serverVersion = str;
                this.clientVersion = str;
                cvs = null;
                cachedVersion = null;
            }
        }
        if (this.clientVersionProperty != null) {
            getProject().setNewProperty(this.clientVersionProperty, this.clientVersion);
        }
        if (this.serverVersionProperty != null) {
            getProject().setNewProperty(this.serverVersionProperty, this.serverVersion);
        }
    }
}
