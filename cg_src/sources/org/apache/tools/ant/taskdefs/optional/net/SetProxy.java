package org.apache.tools.ant.taskdefs.optional.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ProxySetup;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/net/SetProxy.class */
public class SetProxy extends Task {
    private static final int HTTP_PORT = 80;
    private static final int SOCKS_PORT = 1080;
    protected String proxyHost = null;
    protected int proxyPort = 80;
    private String socksProxyHost = null;
    private int socksProxyPort = 1080;
    private String nonProxyHosts = null;
    private String proxyUser = null;
    private String proxyPassword = null;

    public void setProxyHost(String hostname) {
        this.proxyHost = hostname;
    }

    public void setProxyPort(int port) {
        this.proxyPort = port;
    }

    public void setSocksProxyHost(String host) {
        this.socksProxyHost = host;
    }

    public void setSocksProxyPort(int port) {
        this.socksProxyPort = port;
    }

    public void setNonProxyHosts(String nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public void applyWebProxySettings() {
        boolean settingsChanged = false;
        boolean enablingProxy = false;
        Properties sysprops = System.getProperties();
        if (this.proxyHost != null) {
            settingsChanged = true;
            if (!this.proxyHost.isEmpty()) {
                traceSettingInfo();
                enablingProxy = true;
                sysprops.put(ProxySetup.HTTP_PROXY_HOST, this.proxyHost);
                String portString = Integer.toString(this.proxyPort);
                sysprops.put(ProxySetup.HTTP_PROXY_PORT, portString);
                sysprops.put(ProxySetup.HTTPS_PROXY_HOST, this.proxyHost);
                sysprops.put(ProxySetup.HTTPS_PROXY_PORT, portString);
                sysprops.put(ProxySetup.FTP_PROXY_HOST, this.proxyHost);
                sysprops.put(ProxySetup.FTP_PROXY_PORT, portString);
                if (this.nonProxyHosts != null) {
                    sysprops.put(ProxySetup.HTTP_NON_PROXY_HOSTS, this.nonProxyHosts);
                    sysprops.put(ProxySetup.HTTPS_NON_PROXY_HOSTS, this.nonProxyHosts);
                    sysprops.put(ProxySetup.FTP_NON_PROXY_HOSTS, this.nonProxyHosts);
                }
                if (this.proxyUser != null) {
                    sysprops.put(ProxySetup.HTTP_PROXY_USERNAME, this.proxyUser);
                    sysprops.put(ProxySetup.HTTP_PROXY_PASSWORD, this.proxyPassword);
                }
            } else {
                log("resetting http proxy", 3);
                sysprops.remove(ProxySetup.HTTP_PROXY_HOST);
                sysprops.remove(ProxySetup.HTTP_PROXY_PORT);
                sysprops.remove(ProxySetup.HTTP_PROXY_USERNAME);
                sysprops.remove(ProxySetup.HTTP_PROXY_PASSWORD);
                sysprops.remove(ProxySetup.HTTPS_PROXY_HOST);
                sysprops.remove(ProxySetup.HTTPS_PROXY_PORT);
                sysprops.remove(ProxySetup.FTP_PROXY_HOST);
                sysprops.remove(ProxySetup.FTP_PROXY_PORT);
            }
        }
        if (this.socksProxyHost != null) {
            settingsChanged = true;
            if (!this.socksProxyHost.isEmpty()) {
                enablingProxy = true;
                sysprops.put(ProxySetup.SOCKS_PROXY_HOST, this.socksProxyHost);
                sysprops.put(ProxySetup.SOCKS_PROXY_PORT, Integer.toString(this.socksProxyPort));
                if (this.proxyUser != null) {
                    sysprops.put(ProxySetup.SOCKS_PROXY_USERNAME, this.proxyUser);
                    sysprops.put(ProxySetup.SOCKS_PROXY_PASSWORD, this.proxyPassword);
                }
            } else {
                log("resetting socks proxy", 3);
                sysprops.remove(ProxySetup.SOCKS_PROXY_HOST);
                sysprops.remove(ProxySetup.SOCKS_PROXY_PORT);
                sysprops.remove(ProxySetup.SOCKS_PROXY_USERNAME);
                sysprops.remove(ProxySetup.SOCKS_PROXY_PASSWORD);
            }
        }
        if (this.proxyUser != null) {
            if (enablingProxy) {
                Authenticator.setDefault(new ProxyAuth(this.proxyUser, this.proxyPassword));
            } else if (settingsChanged) {
                Authenticator.setDefault(new ProxyAuth("", ""));
            }
        }
    }

    private void traceSettingInfo() {
        log("Setting proxy to " + (this.proxyHost != null ? this.proxyHost : "''") + ":" + this.proxyPort, 3);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        applyWebProxySettings();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/net/SetProxy$ProxyAuth.class */
    public static final class ProxyAuth extends Authenticator {
        private PasswordAuthentication auth;

        private ProxyAuth(String user, String pass) {
            this.auth = new PasswordAuthentication(user, pass.toCharArray());
        }

        @Override // java.net.Authenticator
        protected PasswordAuthentication getPasswordAuthentication() {
            return this.auth;
        }
    }
}
