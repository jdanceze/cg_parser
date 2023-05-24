package org.apache.tools.ant.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PatternSet.class */
public class PatternSet extends DataType implements Cloneable {
    private List<NameEntry> includeList = new ArrayList();
    private List<NameEntry> excludeList = new ArrayList();
    private List<PatternFileNameEntry> includesFileList = new ArrayList();
    private List<PatternFileNameEntry> excludesFileList = new ArrayList();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PatternSet$NameEntry.class */
    public class NameEntry {
        private String name;
        private Object ifCond;
        private Object unlessCond;

        public NameEntry() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIf(Object cond) {
            this.ifCond = cond;
        }

        public void setIf(String cond) {
            setIf((Object) cond);
        }

        public void setUnless(Object cond) {
            this.unlessCond = cond;
        }

        public void setUnless(String cond) {
            setUnless((Object) cond);
        }

        public String getName() {
            return this.name;
        }

        public String evalName(Project p) {
            if (valid(p)) {
                return this.name;
            }
            return null;
        }

        private boolean valid(Project p) {
            PropertyHelper ph = PropertyHelper.getPropertyHelper(p);
            return ph.testIfCondition(this.ifCond) && ph.testUnlessCondition(this.unlessCond);
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            if (this.name == null) {
                buf.append("noname");
            } else {
                buf.append(this.name);
            }
            if (this.ifCond != null || this.unlessCond != null) {
                buf.append(":");
                String connector = "";
                if (this.ifCond != null) {
                    buf.append("if->");
                    buf.append(this.ifCond);
                    connector = ";";
                }
                if (this.unlessCond != null) {
                    buf.append(connector);
                    buf.append("unless->");
                    buf.append(this.unlessCond);
                }
            }
            return buf.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PatternSet$PatternFileNameEntry.class */
    public class PatternFileNameEntry extends NameEntry {
        private String encoding;

        public PatternFileNameEntry() {
            super();
        }

        public final void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public final String getEncoding() {
            return this.encoding;
        }

        @Override // org.apache.tools.ant.types.PatternSet.NameEntry
        public String toString() {
            String baseString = super.toString();
            return this.encoding == null ? baseString : baseString + ";encoding->" + this.encoding;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PatternSet$InvertedPatternSet.class */
    private static final class InvertedPatternSet extends PatternSet {
        private InvertedPatternSet(PatternSet p) {
            setProject(p.getProject());
            addConfiguredPatternset(p);
        }

        @Override // org.apache.tools.ant.types.PatternSet
        public String[] getIncludePatterns(Project p) {
            return super.getExcludePatterns(p);
        }

        @Override // org.apache.tools.ant.types.PatternSet
        public String[] getExcludePatterns(Project p) {
            return super.getIncludePatterns(p);
        }
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (!this.includeList.isEmpty() || !this.excludeList.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public void addConfiguredPatternset(PatternSet p) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        String[] nestedIncludes = p.getIncludePatterns(getProject());
        String[] nestedExcludes = p.getExcludePatterns(getProject());
        if (nestedIncludes != null) {
            for (String nestedInclude : nestedIncludes) {
                createInclude().setName(nestedInclude);
            }
        }
        if (nestedExcludes != null) {
            for (String nestedExclude : nestedExcludes) {
                createExclude().setName(nestedExclude);
            }
        }
    }

    public NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(this.includeList);
    }

    public NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternFileToList(this.includesFileList);
    }

    public NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(this.excludeList);
    }

    public NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternFileToList(this.excludesFileList);
    }

    public void setIncludes(String includes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (includes != null && !includes.isEmpty()) {
            StringTokenizer tok = new StringTokenizer(includes, ", ", false);
            while (tok.hasMoreTokens()) {
                createInclude().setName(tok.nextToken());
            }
        }
    }

    public void setExcludes(String excludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (excludes != null && !excludes.isEmpty()) {
            StringTokenizer tok = new StringTokenizer(excludes, ", ", false);
            while (tok.hasMoreTokens()) {
                createExclude().setName(tok.nextToken());
            }
        }
    }

    private NameEntry addPatternToList(List<NameEntry> list) {
        NameEntry result = new NameEntry();
        list.add(result);
        return result;
    }

    private PatternFileNameEntry addPatternFileToList(List<PatternFileNameEntry> list) {
        PatternFileNameEntry result = new PatternFileNameEntry();
        list.add(result);
        return result;
    }

    public void setIncludesfile(File includesFile) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createIncludesFile().setName(includesFile.getAbsolutePath());
    }

    public void setExcludesfile(File excludesFile) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createExcludesFile().setName(excludesFile.getAbsolutePath());
    }

    private void readPatterns(File patternfile, String encoding, List<NameEntry> patternlist, Project p) throws BuildException {
        try {
            Reader r = encoding == null ? new FileReader(patternfile) : new InputStreamReader(new FileInputStream(patternfile), encoding);
            BufferedReader patternReader = new BufferedReader(r);
            try {
                Stream<String> filter = patternReader.lines().filter((v0) -> {
                    return v0.isEmpty();
                }.negate());
                Objects.requireNonNull(p);
                filter.map(this::replaceProperties).forEach(line -> {
                    addPatternToList(patternlist).setName(patternlist);
                });
                patternReader.close();
                if (r != null) {
                    r.close();
                }
            } catch (Throwable th) {
                try {
                    patternReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException ioe) {
            throw new BuildException("An error occurred while reading from pattern file: " + patternfile, ioe);
        }
    }

    public void append(PatternSet other, Project p) {
        if (isReference()) {
            throw new BuildException("Cannot append to a reference");
        }
        dieOnCircularReference(p);
        String[] incl = other.getIncludePatterns(p);
        if (incl != null) {
            for (String include : incl) {
                createInclude().setName(include);
            }
        }
        String[] excl = other.getExcludePatterns(p);
        if (excl != null) {
            for (String exclude : excl) {
                createExclude().setName(exclude);
            }
        }
    }

    public String[] getIncludePatterns(Project p) {
        if (isReference()) {
            return getRef(p).getIncludePatterns(p);
        }
        dieOnCircularReference(p);
        readFiles(p);
        return makeArray(this.includeList, p);
    }

    public String[] getExcludePatterns(Project p) {
        if (isReference()) {
            return getRef(p).getExcludePatterns(p);
        }
        dieOnCircularReference(p);
        readFiles(p);
        return makeArray(this.excludeList, p);
    }

    public boolean hasPatterns(Project p) {
        if (isReference()) {
            return getRef(p).hasPatterns(p);
        }
        dieOnCircularReference(p);
        return (this.includesFileList.isEmpty() && this.excludesFileList.isEmpty() && this.includeList.isEmpty() && this.excludeList.isEmpty()) ? false : true;
    }

    private PatternSet getRef(Project p) {
        return (PatternSet) getCheckedRef(PatternSet.class, getDataTypeName(), p);
    }

    private String[] makeArray(List<NameEntry> list, Project p) {
        if (list.isEmpty()) {
            return null;
        }
        return (String[]) list.stream().map(ne -> {
            return ne.evalName(p);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).filter(pattern -> {
            return !pattern.isEmpty();
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    private void readFiles(Project p) {
        if (!this.includesFileList.isEmpty()) {
            for (PatternFileNameEntry ne : this.includesFileList) {
                String fileName = ne.evalName(p);
                if (fileName != null) {
                    File inclFile = p.resolveFile(fileName);
                    if (!inclFile.exists()) {
                        throw new BuildException("Includesfile " + inclFile.getAbsolutePath() + " not found.");
                    }
                    readPatterns(inclFile, ne.getEncoding(), this.includeList, p);
                }
            }
            this.includesFileList.clear();
        }
        if (!this.excludesFileList.isEmpty()) {
            for (PatternFileNameEntry ne2 : this.excludesFileList) {
                String fileName2 = ne2.evalName(p);
                if (fileName2 != null) {
                    File exclFile = p.resolveFile(fileName2);
                    if (!exclFile.exists()) {
                        throw new BuildException("Excludesfile " + exclFile.getAbsolutePath() + " not found.");
                    }
                    readPatterns(exclFile, ne2.getEncoding(), this.excludeList, p);
                }
            }
            this.excludesFileList.clear();
        }
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return String.format("patternSet{ includes: %s excludes: %s }", this.includeList, this.excludeList);
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        try {
            PatternSet ps = (PatternSet) super.clone();
            ps.includeList = new ArrayList(this.includeList);
            ps.excludeList = new ArrayList(this.excludeList);
            ps.includesFileList = new ArrayList(this.includesFileList);
            ps.excludesFileList = new ArrayList(this.excludesFileList);
            return ps;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    public void addConfiguredInvert(PatternSet p) {
        addConfiguredPatternset(new InvertedPatternSet());
    }
}
