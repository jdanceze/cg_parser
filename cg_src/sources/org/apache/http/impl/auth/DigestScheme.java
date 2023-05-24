package org.apache.http.impl.auth;

import android.app.DownloadManager;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.params.AuthParams;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/auth/DigestScheme.class */
public class DigestScheme extends RFC2617Scheme {
    private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String NC = "00000001";
    private static final int QOP_MISSING = 0;
    private static final int QOP_AUTH_INT = 1;
    private static final int QOP_AUTH = 2;
    private String cnonce;
    private int qopVariant = 0;
    private boolean complete = false;

    @Override // org.apache.http.impl.auth.AuthSchemeBase, org.apache.http.auth.AuthScheme
    public void processChallenge(Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        if (getParameter("realm") == null) {
            throw new MalformedChallengeException("missing realm in challange");
        }
        if (getParameter("nonce") == null) {
            throw new MalformedChallengeException("missing nonce in challange");
        }
        boolean unsupportedQop = false;
        String qop = getParameter("qop");
        if (qop != null) {
            StringTokenizer tok = new StringTokenizer(qop, ",");
            while (true) {
                if (!tok.hasMoreTokens()) {
                    break;
                }
                String variant = tok.nextToken().trim();
                if (variant.equals("auth")) {
                    this.qopVariant = 2;
                    break;
                } else if (variant.equals("auth-int")) {
                    this.qopVariant = 1;
                } else {
                    unsupportedQop = true;
                }
            }
        }
        if (unsupportedQop && this.qopVariant == 0) {
            throw new MalformedChallengeException("None of the qop methods is supported");
        }
        this.cnonce = null;
        this.complete = true;
    }

    @Override // org.apache.http.auth.AuthScheme
    public boolean isComplete() {
        String s = getParameter("stale");
        if ("true".equalsIgnoreCase(s)) {
            return false;
        }
        return this.complete;
    }

    @Override // org.apache.http.auth.AuthScheme
    public String getSchemeName() {
        return "digest";
    }

    @Override // org.apache.http.auth.AuthScheme
    public boolean isConnectionBased() {
        return false;
    }

    public void overrideParamter(String name, String value) {
        getParameters().put(name, value);
    }

    private String getCnonce() {
        if (this.cnonce == null) {
            this.cnonce = createCnonce();
        }
        return this.cnonce;
    }

    @Override // org.apache.http.auth.AuthScheme
    public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        getParameters().put("methodname", request.getRequestLine().getMethod());
        getParameters().put(DownloadManager.COLUMN_URI, request.getRequestLine().getUri());
        String charset = getParameter("charset");
        if (charset == null) {
            String charset2 = AuthParams.getCredentialCharset(request.getParams());
            getParameters().put("charset", charset2);
        }
        String digest = createDigest(credentials);
        return createDigestHeader(credentials, digest);
    }

    private static MessageDigest createMessageDigest(String digAlg) throws UnsupportedDigestAlgorithmException {
        try {
            return MessageDigest.getInstance(digAlg);
        } catch (Exception e) {
            throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
        }
    }

    private String createDigest(Credentials credentials) throws AuthenticationException {
        String serverDigestValue;
        String uri = getParameter(DownloadManager.COLUMN_URI);
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");
        if (uri == null) {
            throw new IllegalStateException("URI may not be null");
        }
        if (realm == null) {
            throw new IllegalStateException("Realm may not be null");
        }
        if (nonce == null) {
            throw new IllegalStateException("Nonce may not be null");
        }
        if (algorithm == null) {
            algorithm = "MD5";
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        if (this.qopVariant == 1) {
            throw new AuthenticationException("Unsupported qop in HTTP Digest authentication");
        }
        String digAlg = algorithm;
        if (digAlg.equalsIgnoreCase("MD5-sess")) {
            digAlg = "MD5";
        }
        MessageDigest digester = createMessageDigest(digAlg);
        String uname = credentials.getUserPrincipal().getName();
        String pwd = credentials.getPassword();
        StringBuilder tmp = new StringBuilder(uname.length() + realm.length() + pwd.length() + 2);
        tmp.append(uname);
        tmp.append(':');
        tmp.append(realm);
        tmp.append(':');
        tmp.append(pwd);
        String a1 = tmp.toString();
        if (algorithm.equalsIgnoreCase("MD5-sess")) {
            String cnonce = getCnonce();
            String tmp2 = encode(digester.digest(EncodingUtils.getBytes(a1, charset)));
            StringBuilder tmp3 = new StringBuilder(tmp2.length() + nonce.length() + cnonce.length() + 2);
            tmp3.append(tmp2);
            tmp3.append(':');
            tmp3.append(nonce);
            tmp3.append(':');
            tmp3.append(cnonce);
            a1 = tmp3.toString();
        }
        String hasha1 = encode(digester.digest(EncodingUtils.getBytes(a1, charset)));
        String a2 = null;
        if (this.qopVariant != 1) {
            a2 = method + ':' + uri;
        }
        String hasha2 = encode(digester.digest(EncodingUtils.getAsciiBytes(a2)));
        if (this.qopVariant == 0) {
            StringBuilder tmp22 = new StringBuilder(hasha1.length() + nonce.length() + hasha1.length());
            tmp22.append(hasha1);
            tmp22.append(':');
            tmp22.append(nonce);
            tmp22.append(':');
            tmp22.append(hasha2);
            serverDigestValue = tmp22.toString();
        } else {
            String qopOption = getQopVariantString();
            String cnonce2 = getCnonce();
            StringBuilder tmp23 = new StringBuilder(hasha1.length() + nonce.length() + NC.length() + cnonce2.length() + qopOption.length() + hasha2.length() + 5);
            tmp23.append(hasha1);
            tmp23.append(':');
            tmp23.append(nonce);
            tmp23.append(':');
            tmp23.append(NC);
            tmp23.append(':');
            tmp23.append(cnonce2);
            tmp23.append(':');
            tmp23.append(qopOption);
            tmp23.append(':');
            tmp23.append(hasha2);
            serverDigestValue = tmp23.toString();
        }
        String serverDigest = encode(digester.digest(EncodingUtils.getAsciiBytes(serverDigestValue)));
        return serverDigest;
    }

    private Header createDigestHeader(Credentials credentials, String digest) {
        CharArrayBuffer buffer = new CharArrayBuffer(128);
        if (isProxy()) {
            buffer.append("Proxy-Authorization");
        } else {
            buffer.append("Authorization");
        }
        buffer.append(": Digest ");
        String uri = getParameter(DownloadManager.COLUMN_URI);
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String opaque = getParameter("opaque");
        String algorithm = getParameter("algorithm");
        String uname = credentials.getUserPrincipal().getName();
        List<BasicNameValuePair> params = new ArrayList<>(20);
        params.add(new BasicNameValuePair("username", uname));
        params.add(new BasicNameValuePair("realm", realm));
        params.add(new BasicNameValuePair("nonce", nonce));
        params.add(new BasicNameValuePair(DownloadManager.COLUMN_URI, uri));
        params.add(new BasicNameValuePair("response", digest));
        if (this.qopVariant != 0) {
            params.add(new BasicNameValuePair("qop", getQopVariantString()));
            params.add(new BasicNameValuePair("nc", NC));
            params.add(new BasicNameValuePair("cnonce", getCnonce()));
        }
        if (algorithm != null) {
            params.add(new BasicNameValuePair("algorithm", algorithm));
        }
        if (opaque != null) {
            params.add(new BasicNameValuePair("opaque", opaque));
        }
        for (int i = 0; i < params.size(); i++) {
            BasicNameValuePair param = params.get(i);
            if (i > 0) {
                buffer.append(", ");
            }
            boolean noQuotes = "nc".equals(param.getName()) || "qop".equals(param.getName());
            BasicHeaderValueFormatter.DEFAULT.formatNameValuePair(buffer, param, !noQuotes);
        }
        return new BufferedHeader(buffer);
    }

    private String getQopVariantString() {
        String qopOption;
        if (this.qopVariant == 1) {
            qopOption = "auth-int";
        } else {
            qopOption = "auth";
        }
        return qopOption;
    }

    private static String encode(byte[] binaryData) {
        int n = binaryData.length;
        char[] buffer = new char[n * 2];
        for (int i = 0; i < n; i++) {
            int low = binaryData[i] & 15;
            int high = (binaryData[i] & 240) >> 4;
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[(i * 2) + 1] = HEXADECIMAL[low];
        }
        return new String(buffer);
    }

    public static String createCnonce() {
        MessageDigest md5Helper = createMessageDigest("MD5");
        String cnonce = Long.toString(System.currentTimeMillis());
        return encode(md5Helper.digest(EncodingUtils.getAsciiBytes(cnonce)));
    }
}
