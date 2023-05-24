package org.apache.tools.ant.filters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Properties;
import java.util.TreeMap;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/ReplaceTokens.class */
public final class ReplaceTokens extends BaseParamFilterReader implements ChainableReader {
    private static final String DEFAULT_BEGIN_TOKEN = "@";
    private static final String DEFAULT_END_TOKEN = "@";
    private Hashtable<String, String> hash;
    private final TreeMap<String, String> resolvedTokens;
    private boolean resolvedTokensBuilt;
    private String readBuffer;
    private String replaceData;
    private int replaceIndex;
    private String beginToken;
    private String endToken;

    public ReplaceTokens() {
        this.hash = new Hashtable<>();
        this.resolvedTokens = new TreeMap<>();
        this.resolvedTokensBuilt = false;
        this.readBuffer = "";
        this.replaceData = null;
        this.replaceIndex = -1;
        this.beginToken = "@";
        this.endToken = "@";
    }

    public ReplaceTokens(Reader in) {
        super(in);
        this.hash = new Hashtable<>();
        this.resolvedTokens = new TreeMap<>();
        this.resolvedTokensBuilt = false;
        this.readBuffer = "";
        this.replaceData = null;
        this.replaceIndex = -1;
        this.beginToken = "@";
        this.endToken = "@";
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0100, code lost:
        return getFirstCharacterFromReadBuffer();
     */
    @Override // java.io.FilterReader, java.io.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int read() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 357
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.filters.ReplaceTokens.read():int");
    }

    private int getFirstCharacterFromReadBuffer() {
        if (this.readBuffer.isEmpty()) {
            return -1;
        }
        int chr = this.readBuffer.charAt(0);
        this.readBuffer = this.readBuffer.substring(1);
        return chr;
    }

    public void setBeginToken(String beginToken) {
        this.beginToken = beginToken;
    }

    private String getBeginToken() {
        return this.beginToken;
    }

    public void setEndToken(String endToken) {
        this.endToken = endToken;
    }

    private String getEndToken() {
        return this.endToken;
    }

    public void setPropertiesResource(Resource r) {
        makeTokensFromProperties(r);
    }

    public void addConfiguredToken(Token token) {
        this.hash.put(token.getKey(), token.getValue());
        this.resolvedTokensBuilt = false;
    }

    private Properties getProperties(Resource resource) {
        InputStream in = null;
        Properties props = new Properties();
        try {
            try {
                in = resource.getInputStream();
                props.load(in);
                FileUtils.close(in);
            } catch (IOException ioe) {
                if (getProject() != null) {
                    getProject().log("getProperties failed, " + ioe.getMessage(), 0);
                } else {
                    ioe.printStackTrace();
                }
                FileUtils.close(in);
            }
            return props;
        } catch (Throwable th) {
            FileUtils.close(in);
            throw th;
        }
    }

    private void setTokens(Hashtable<String, String> hash) {
        this.hash = hash;
    }

    private Hashtable<String, String> getTokens() {
        return this.hash;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        ReplaceTokens newFilter = new ReplaceTokens(rdr);
        newFilter.setBeginToken(getBeginToken());
        newFilter.setEndToken(getEndToken());
        newFilter.setTokens(getTokens());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if (param != null) {
                    String type = param.getType();
                    if ("tokenchar".equals(type)) {
                        String name = param.getName();
                        if ("begintoken".equals(name)) {
                            this.beginToken = param.getValue();
                        } else if ("endtoken".equals(name)) {
                            this.endToken = param.getValue();
                        }
                    } else if ("token".equals(type)) {
                        String name2 = param.getName();
                        String value = param.getValue();
                        this.hash.put(name2, value);
                    } else if ("propertiesfile".equals(type)) {
                        makeTokensFromProperties(new FileResource(new File(param.getValue())));
                    }
                }
            }
        }
    }

    private void makeTokensFromProperties(Resource r) {
        Properties props = getProperties(r);
        props.stringPropertyNames().forEach(key -> {
            this.hash.put(props, props.getProperty(props));
        });
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/ReplaceTokens$Token.class */
    public static class Token {
        private String key;
        private String value;

        public final void setKey(String key) {
            this.key = key;
        }

        public final void setValue(String value) {
            this.value = value;
        }

        public final String getKey() {
            return this.key;
        }

        public final String getValue() {
            return this.value;
        }
    }
}
