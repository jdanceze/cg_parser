package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/Socket.class */
public class Socket extends ProjectComponent implements Condition {
    private String server = null;
    private int port = 0;

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:15:0x0061
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws org.apache.tools.ant.BuildException {
        /*
            r5 = this;
            r0 = r5
            java.lang.String r0 = r0.server
            if (r0 != 0) goto L11
            org.apache.tools.ant.BuildException r0 = new org.apache.tools.ant.BuildException
            r1 = r0
            java.lang.String r2 = "No server specified in socket condition"
            r1.<init>(r2)
            throw r0
        L11:
            r0 = r5
            int r0 = r0.port
            if (r0 != 0) goto L22
            org.apache.tools.ant.BuildException r0 = new org.apache.tools.ant.BuildException
            r1 = r0
            java.lang.String r2 = "No port specified in socket condition"
            r1.<init>(r2)
            throw r0
        L22:
            r0 = r5
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "Checking for listener at "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r5
            java.lang.String r2 = r2.server
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = ":"
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r5
            int r2 = r2.port
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 3
            r0.log(r1, r2)
            java.net.Socket r0 = new java.net.Socket     // Catch: java.io.IOException -> L71
            r1 = r0
            r2 = r5
            java.lang.String r2 = r2.server     // Catch: java.io.IOException -> L71
            r3 = r5
            int r3 = r3.port     // Catch: java.io.IOException -> L71
            r1.<init>(r2, r3)     // Catch: java.io.IOException -> L71
            r6 = r0
            r0 = 1
            r7 = r0
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L71
            r0 = r7
            return r0
        L61:
            r7 = move-exception
            r0 = r6
            r0.close()     // Catch: java.lang.Throwable -> L69 java.io.IOException -> L71
            goto L6f
        L69:
            r8 = move-exception
            r0 = r7
            r1 = r8
            r0.addSuppressed(r1)     // Catch: java.io.IOException -> L71
        L6f:
            r0 = r7
            throw r0     // Catch: java.io.IOException -> L71
        L71:
            r6 = move-exception
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.taskdefs.condition.Socket.eval():boolean");
    }
}
