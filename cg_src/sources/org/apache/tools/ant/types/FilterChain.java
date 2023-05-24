package org.apache.tools.ant.types;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.filters.ClassConstants;
import org.apache.tools.ant.filters.EscapeUnicode;
import org.apache.tools.ant.filters.ExpandProperties;
import org.apache.tools.ant.filters.HeadFilter;
import org.apache.tools.ant.filters.LineContains;
import org.apache.tools.ant.filters.LineContainsRegExp;
import org.apache.tools.ant.filters.PrefixLines;
import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.tools.ant.filters.StripJavaComments;
import org.apache.tools.ant.filters.StripLineBreaks;
import org.apache.tools.ant.filters.StripLineComments;
import org.apache.tools.ant.filters.SuffixLines;
import org.apache.tools.ant.filters.TabsToSpaces;
import org.apache.tools.ant.filters.TailFilter;
import org.apache.tools.ant.filters.TokenFilter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterChain.class */
public class FilterChain extends DataType {
    private Vector<Object> filterReaders = new Vector<>();

    public void addFilterReader(AntFilterReader filterReader) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filterReader);
    }

    public Vector<Object> getFilterReaders() {
        if (isReference()) {
            return getRef().getFilterReaders();
        }
        dieOnCircularReference();
        return this.filterReaders;
    }

    public void addClassConstants(ClassConstants classConstants) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(classConstants);
    }

    public void addExpandProperties(ExpandProperties expandProperties) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(expandProperties);
    }

    public void addHeadFilter(HeadFilter headFilter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(headFilter);
    }

    public void addLineContains(LineContains lineContains) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(lineContains);
    }

    public void addLineContainsRegExp(LineContainsRegExp lineContainsRegExp) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(lineContainsRegExp);
    }

    public void addPrefixLines(PrefixLines prefixLines) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(prefixLines);
    }

    public void addSuffixLines(SuffixLines suffixLines) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(suffixLines);
    }

    public void addReplaceTokens(ReplaceTokens replaceTokens) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(replaceTokens);
    }

    public void addStripJavaComments(StripJavaComments stripJavaComments) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(stripJavaComments);
    }

    public void addStripLineBreaks(StripLineBreaks stripLineBreaks) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(stripLineBreaks);
    }

    public void addStripLineComments(StripLineComments stripLineComments) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(stripLineComments);
    }

    public void addTabsToSpaces(TabsToSpaces tabsToSpaces) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(tabsToSpaces);
    }

    public void addTailFilter(TailFilter tailFilter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(tailFilter);
    }

    public void addEscapeUnicode(EscapeUnicode escapeUnicode) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(escapeUnicode);
    }

    public void addTokenFilter(TokenFilter tokenFilter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(tokenFilter);
    }

    public void addDeleteCharacters(TokenFilter.DeleteCharacters filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    public void addContainsRegex(TokenFilter.ContainsRegex filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    public void addReplaceRegex(TokenFilter.ReplaceRegex filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    public void addTrim(TokenFilter.Trim filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    public void addReplaceString(TokenFilter.ReplaceString filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    public void addIgnoreBlank(TokenFilter.IgnoreBlank filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (!this.filterReaders.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public void add(ChainableReader filter) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        this.filterReaders.addElement(filter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        Iterator<Object> it = this.filterReaders.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) o, stk, p);
            }
        }
        setChecked(true);
    }

    private FilterChain getRef() {
        return (FilterChain) getCheckedRef(FilterChain.class);
    }
}
