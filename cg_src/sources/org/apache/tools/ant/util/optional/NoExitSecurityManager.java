package org.apache.tools.ant.util.optional;

import java.security.Permission;
import org.apache.tools.ant.ExitException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/optional/NoExitSecurityManager.class */
public class NoExitSecurityManager extends SecurityManager {
    @Override // java.lang.SecurityManager
    public void checkExit(int status) {
        throw new ExitException(status);
    }

    @Override // java.lang.SecurityManager
    public void checkPermission(Permission perm) {
    }
}
