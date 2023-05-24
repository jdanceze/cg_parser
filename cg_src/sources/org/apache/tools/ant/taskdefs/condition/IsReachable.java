package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsReachable.class */
public class IsReachable extends ProjectComponent implements Condition {
    public static final int DEFAULT_TIMEOUT = 30;
    private static final int SECOND = 1000;
    public static final String ERROR_NO_HOSTNAME = "No hostname defined";
    public static final String ERROR_BAD_TIMEOUT = "Invalid timeout value";
    private static final String WARN_UNKNOWN_HOST = "Unknown host: ";
    public static final String ERROR_ON_NETWORK = "network error to ";
    public static final String ERROR_BOTH_TARGETS = "Both url and host have been specified";
    public static final String MSG_NO_REACHABLE_TEST = "cannot do a proper reachability test on this Java version";
    public static final String ERROR_BAD_URL = "Bad URL ";
    public static final String ERROR_NO_HOST_IN_URL = "No hostname in URL ";
    @Deprecated
    public static final String METHOD_NAME = "isReachable";
    private String host;
    private String url;
    private int timeout = 30;

    public void setHost(String host) {
        this.host = host;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        boolean reachable;
        if (isNullOrEmpty(this.host) && isNullOrEmpty(this.url)) {
            throw new BuildException(ERROR_NO_HOSTNAME);
        }
        if (this.timeout < 0) {
            throw new BuildException(ERROR_BAD_TIMEOUT);
        }
        String target = this.host;
        if (!isNullOrEmpty(this.url)) {
            if (!isNullOrEmpty(this.host)) {
                throw new BuildException(ERROR_BOTH_TARGETS);
            }
            try {
                URL realURL = new URL(this.url);
                target = realURL.getHost();
                if (isNullOrEmpty(target)) {
                    throw new BuildException(ERROR_NO_HOST_IN_URL + this.url);
                }
            } catch (MalformedURLException e) {
                throw new BuildException(ERROR_BAD_URL + this.url, e);
            }
        }
        log("Probing host " + target, 3);
        try {
            InetAddress address = InetAddress.getByName(target);
            log("Host address = " + address.getHostAddress(), 3);
            try {
                reachable = address.isReachable(this.timeout * 1000);
            } catch (IOException ioe) {
                reachable = false;
                log(ERROR_ON_NETWORK + target + ": " + ioe.toString());
            }
            log("host is" + (reachable ? "" : " not") + " reachable", 3);
            return reachable;
        } catch (UnknownHostException e2) {
            log(WARN_UNKNOWN_HOST + target);
            return false;
        }
    }
}
