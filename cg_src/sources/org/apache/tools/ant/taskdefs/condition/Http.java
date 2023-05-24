package org.apache.tools.ant.taskdefs.condition;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Get;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/Http.class */
public class Http extends ProjectComponent implements Condition {
    private static final int ERROR_BEGINS = 400;
    private static final String DEFAULT_REQUEST_METHOD = "GET";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private String spec = null;
    private String requestMethod = "GET";
    private boolean followRedirects = true;
    private int errorsBeginAt = 400;
    private int readTimeout = 0;

    public void setUrl(String url) {
        this.spec = url;
    }

    public void setErrorsBeginAt(int errorsBeginAt) {
        this.errorsBeginAt = errorsBeginAt;
    }

    public void setRequestMethod(String method) {
        this.requestMethod = method == null ? "GET" : method.toUpperCase(Locale.ENGLISH);
    }

    public void setFollowRedirects(boolean f) {
        this.followRedirects = f;
    }

    public void setReadTimeout(int t) {
        if (t >= 0) {
            this.readTimeout = t;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.spec == null) {
            throw new BuildException("No url specified in http condition");
        }
        log("Checking for " + this.spec, 3);
        try {
            URL url = new URL(this.spec);
            try {
                URLConnection conn = url.openConnection();
                if (conn instanceof HttpURLConnection) {
                    int code = request((HttpURLConnection) conn, url);
                    log("Result code for " + this.spec + " was " + code, 3);
                    if (code > 0) {
                        if (code < this.errorsBeginAt) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            } catch (ProtocolException pe) {
                throw new BuildException("Invalid HTTP protocol: " + this.requestMethod, pe);
            } catch (IOException e) {
                return false;
            }
        } catch (MalformedURLException e2) {
            throw new BuildException("Badly formed URL: " + this.spec, e2);
        }
    }

    private int request(HttpURLConnection http, URL url) throws IOException {
        http.setRequestMethod(this.requestMethod);
        http.setInstanceFollowRedirects(this.followRedirects);
        http.setReadTimeout(this.readTimeout);
        int firstStatusCode = http.getResponseCode();
        if (Get.isMoved(firstStatusCode)) {
            String newLocation = http.getHeaderField(HttpHeaders.LOCATION);
            URL newURL = new URL(newLocation);
            if (redirectionAllowed(url, newURL)) {
                URLConnection newConn = newURL.openConnection();
                if (newConn instanceof HttpURLConnection) {
                    log("Following redirect from " + url + " to " + newURL);
                    return request((HttpURLConnection) newConn, newURL);
                }
            }
        }
        return firstStatusCode;
    }

    private boolean redirectionAllowed(URL from, URL to) {
        if (from.equals(to)) {
            return false;
        }
        if (!from.getProtocol().equals(to.getProtocol())) {
            if (!"http".equals(from.getProtocol()) || !HTTPS.equals(to.getProtocol())) {
                log("Redirection detected from " + from.getProtocol() + " to " + to.getProtocol() + ". Protocol switch unsafe, not allowed.");
                return false;
            }
            return true;
        }
        return true;
    }
}
