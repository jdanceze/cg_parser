package org.apache.tools.ant.types;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.util.VectorSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterSet.class */
public class FilterSet extends DataType implements Cloneable {
    public static final String DEFAULT_TOKEN_START = "@";
    public static final String DEFAULT_TOKEN_END = "@";
    private String startOfToken;
    private String endOfToken;
    private Vector<String> passedTokens;
    private boolean duplicateToken;
    private boolean recurse;
    private Hashtable<String, String> filterHash;
    private Vector<File> filtersFiles;
    private OnMissing onMissingFiltersFile;
    private boolean readingFiles;
    private int recurseDepth;
    private Vector<Filter> filters;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterSet$Filter.class */
    public static class Filter {
        String token;
        String value;

        public Filter(String token, String value) {
            setToken(token);
            setValue(value);
        }

        public Filter() {
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getToken() {
            return this.token;
        }

        public String getValue() {
            return this.value;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterSet$FiltersFile.class */
    public class FiltersFile {
        public FiltersFile() {
        }

        public void setFile(File file) {
            FilterSet.this.filtersFiles.add(file);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterSet$OnMissing.class */
    public static class OnMissing extends EnumeratedAttribute {
        private static final String[] VALUES = {"fail", "warn", Definer.OnError.POLICY_IGNORE};
        public static final OnMissing FAIL = new OnMissing("fail");
        public static final OnMissing WARN = new OnMissing("warn");
        public static final OnMissing IGNORE = new OnMissing(Definer.OnError.POLICY_IGNORE);
        private static final int FAIL_INDEX = 0;
        private static final int WARN_INDEX = 1;
        private static final int IGNORE_INDEX = 2;

        public OnMissing() {
        }

        public OnMissing(String value) {
            setValue(value);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALUES;
        }
    }

    public FilterSet() {
        this.startOfToken = "@";
        this.endOfToken = "@";
        this.duplicateToken = false;
        this.recurse = true;
        this.filterHash = null;
        this.filtersFiles = new Vector<>();
        this.onMissingFiltersFile = OnMissing.FAIL;
        this.readingFiles = false;
        this.recurseDepth = 0;
        this.filters = new Vector<>();
    }

    protected FilterSet(FilterSet filterset) {
        this.startOfToken = "@";
        this.endOfToken = "@";
        this.duplicateToken = false;
        this.recurse = true;
        this.filterHash = null;
        this.filtersFiles = new Vector<>();
        this.onMissingFiltersFile = OnMissing.FAIL;
        this.readingFiles = false;
        this.recurseDepth = 0;
        this.filters = new Vector<>();
        Vector<Filter> clone = (Vector) filterset.getFilters().clone();
        this.filters = clone;
    }

    protected synchronized Vector<Filter> getFilters() {
        if (isReference()) {
            return getRef().getFilters();
        }
        dieOnCircularReference();
        if (!this.readingFiles) {
            this.readingFiles = true;
            Iterator<File> it = this.filtersFiles.iterator();
            while (it.hasNext()) {
                File filtersFile = it.next();
                readFiltersFromFile(filtersFile);
            }
            this.filtersFiles.clear();
            this.readingFiles = false;
        }
        return this.filters;
    }

    protected FilterSet getRef() {
        return (FilterSet) getCheckedRef(FilterSet.class);
    }

    public synchronized Hashtable<String, String> getFilterHash() {
        if (isReference()) {
            return getRef().getFilterHash();
        }
        dieOnCircularReference();
        if (this.filterHash == null) {
            this.filterHash = new Hashtable<>(getFilters().size());
            getFilters().forEach(filter -> {
                this.filterHash.put(filter.getToken(), filter.getValue());
            });
        }
        return this.filterHash;
    }

    public void setFiltersfile(File filtersFile) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.filtersFiles.add(filtersFile);
    }

    public void setBeginToken(String startOfToken) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (startOfToken == null || startOfToken.isEmpty()) {
            throw new BuildException("beginToken must not be empty");
        }
        this.startOfToken = startOfToken;
    }

    public String getBeginToken() {
        if (isReference()) {
            return getRef().getBeginToken();
        }
        return this.startOfToken;
    }

    public void setEndToken(String endOfToken) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (endOfToken == null || endOfToken.isEmpty()) {
            throw new BuildException("endToken must not be empty");
        }
        this.endOfToken = endOfToken;
    }

    public String getEndToken() {
        if (isReference()) {
            return getRef().getEndToken();
        }
        return this.endOfToken;
    }

    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    public boolean isRecurse() {
        return this.recurse;
    }

    public synchronized void readFiltersFromFile(File filtersFile) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (!filtersFile.exists()) {
            handleMissingFile("Could not read filters from file " + filtersFile + " as it doesn't exist.");
        }
        if (filtersFile.isFile()) {
            log("Reading filters from " + filtersFile, 3);
            try {
                InputStream in = Files.newInputStream(filtersFile.toPath(), new OpenOption[0]);
                Properties props = new Properties();
                props.load(in);
                props.forEach(k, v -> {
                    addFilter(new Filter((String) k, (String) v));
                });
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                throw new BuildException("Could not read filters from file: " + filtersFile, ex);
            }
        } else {
            handleMissingFile("Must specify a file rather than a directory in the filtersfile attribute:" + filtersFile);
        }
        this.filterHash = null;
    }

    public synchronized String replaceTokens(String line) {
        return iReplaceTokens(line);
    }

    public synchronized void addFilter(Filter filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.filters.addElement(filter);
        this.filterHash = null;
    }

    public FiltersFile createFiltersfile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return new FiltersFile();
    }

    public synchronized void addFilter(String token, String value) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        addFilter(new Filter(token, value));
    }

    public synchronized void addConfiguredFilterSet(FilterSet filterSet) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        Iterator<Filter> it = filterSet.getFilters().iterator();
        while (it.hasNext()) {
            Filter filter = it.next();
            addFilter(filter);
        }
    }

    public synchronized void addConfiguredPropertySet(PropertySet propertySet) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        Properties p = propertySet.getProperties();
        Set<Map.Entry<Object, Object>> entries = p.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            addFilter(new Filter(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
        }
    }

    public synchronized boolean hasFilters() {
        return !getFilters().isEmpty();
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public synchronized Object clone() throws BuildException {
        if (isReference()) {
            return getRef().clone();
        }
        try {
            FilterSet fs = (FilterSet) super.clone();
            Vector<Filter> clonedFilters = (Vector) getFilters().clone();
            fs.filters = clonedFilters;
            fs.setProject(getProject());
            return fs;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    public void setOnMissingFiltersFile(OnMissing onMissingFiltersFile) {
        this.onMissingFiltersFile = onMissingFiltersFile;
    }

    public OnMissing getOnMissingFiltersFile() {
        return this.onMissingFiltersFile;
    }

    private synchronized String iReplaceTokens(String line) {
        int endIndex;
        String beginToken = getBeginToken();
        String endToken = getEndToken();
        int index = line.indexOf(beginToken);
        if (index > -1) {
            Hashtable<String, String> tokens = getFilterHash();
            try {
                StringBuilder b = new StringBuilder();
                int i = 0;
                while (index > -1 && (endIndex = line.indexOf(endToken, index + beginToken.length() + 1)) != -1) {
                    String token = line.substring(index + beginToken.length(), endIndex);
                    b.append((CharSequence) line, i, index);
                    if (tokens.containsKey(token)) {
                        String value = tokens.get(token);
                        if (this.recurse && !value.equals(token)) {
                            value = replaceTokens(value, token);
                        }
                        log("Replacing: " + beginToken + token + endToken + " -> " + value, 3);
                        b.append(value);
                        i = index + beginToken.length() + token.length() + endToken.length();
                    } else {
                        b.append(beginToken.charAt(0));
                        i = index + 1;
                    }
                    index = line.indexOf(beginToken, i);
                }
                b.append(line.substring(i));
                return b.toString();
            } catch (StringIndexOutOfBoundsException e) {
                return line;
            }
        }
        return line;
    }

    private synchronized String replaceTokens(String line, String parent) throws BuildException {
        String beginToken = getBeginToken();
        String endToken = getEndToken();
        if (this.recurseDepth == 0) {
            this.passedTokens = new VectorSet();
        }
        this.recurseDepth++;
        if (this.passedTokens.contains(parent) && !this.duplicateToken) {
            this.duplicateToken = true;
            System.out.println("Infinite loop in tokens. Currently known tokens : " + this.passedTokens.toString() + "\nProblem token : " + beginToken + parent + endToken + " called from " + beginToken + this.passedTokens.lastElement() + endToken);
            this.recurseDepth--;
            return parent;
        }
        this.passedTokens.addElement(parent);
        String value = iReplaceTokens(line);
        if (!value.contains(beginToken) && !this.duplicateToken && this.recurseDepth == 1) {
            this.passedTokens = null;
        } else if (this.duplicateToken) {
            if (!this.passedTokens.isEmpty()) {
                value = this.passedTokens.remove(this.passedTokens.size() - 1);
                if (this.passedTokens.isEmpty()) {
                    value = beginToken + value + endToken;
                    this.duplicateToken = false;
                }
            }
        } else if (!this.passedTokens.isEmpty()) {
            this.passedTokens.remove(this.passedTokens.size() - 1);
        }
        this.recurseDepth--;
        return value;
    }

    private void handleMissingFile(String message) {
        switch (this.onMissingFiltersFile.getIndex()) {
            case 0:
                throw new BuildException(message);
            case 1:
                log(message, 1);
                return;
            case 2:
                return;
            default:
                throw new BuildException("Invalid value for onMissingFiltersFile");
        }
    }
}
