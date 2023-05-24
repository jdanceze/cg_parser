package org.apache.http.auth.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/auth/params/AuthParamBean.class */
public class AuthParamBean extends HttpAbstractParamBean {
    public AuthParamBean(HttpParams params) {
        super(params);
    }

    public void setCredentialCharset(String charset) {
        AuthParams.setCredentialCharset(this.params, charset);
    }
}
