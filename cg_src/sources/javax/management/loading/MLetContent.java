package javax.management.loading;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLetContent.class */
class MLetContent {
    private Map attributes;
    private URL documentURL;
    private URL baseURL;

    public MLetContent(URL url, Map map) {
        String file;
        int lastIndexOf;
        this.documentURL = url;
        this.attributes = map;
        String str = (String) getParameter("codebase");
        if (str != null) {
            try {
                this.baseURL = new URL(this.documentURL, str.endsWith("/") ? str : new StringBuffer().append(str).append("/").toString());
            } catch (MalformedURLException e) {
            }
        }
        if (this.baseURL == null && (lastIndexOf = (file = this.documentURL.getFile()).lastIndexOf(47)) > 0 && lastIndexOf < file.length() - 1) {
            try {
                this.baseURL = new URL(this.documentURL, file.substring(0, lastIndexOf + 1));
            } catch (MalformedURLException e2) {
            }
        }
        if (this.baseURL == null) {
            this.baseURL = this.documentURL;
        }
    }

    public Map getAttributes() {
        return this.attributes;
    }

    public URL getDocumentBase() {
        return this.documentURL;
    }

    public URL getCodeBase() {
        return this.baseURL;
    }

    public String getJarFiles() {
        return (String) getParameter("archive");
    }

    public String getCode() {
        return (String) getParameter("code");
    }

    public String getSerializedObject() {
        return (String) getParameter("object");
    }

    public String getName() {
        return (String) getParameter("name");
    }

    public String getVersion() {
        return (String) getParameter("version");
    }

    public Object getParameter(String str) {
        return this.attributes.get(str.toLowerCase());
    }
}
