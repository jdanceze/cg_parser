package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/SortFilter.class */
public final class SortFilter extends BaseParamFilterReader implements ChainableReader {
    private static final String REVERSE_KEY = "reverse";
    private static final String COMPARATOR_KEY = "comparator";
    private Comparator<? super String> comparator;
    private boolean reverse;
    private List<String> lines;
    private String line;
    private Iterator<String> iterator;

    public SortFilter() {
        this.comparator = null;
        this.line = null;
        this.iterator = null;
    }

    public SortFilter(Reader in) {
        super(in);
        this.comparator = null;
        this.line = null;
        this.iterator = null;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        int ch = -1;
        if (this.line != null) {
            ch = this.line.charAt(0);
            if (this.line.length() == 1) {
                this.line = null;
            } else {
                this.line = this.line.substring(1);
            }
        } else {
            if (this.lines == null) {
                this.lines = new ArrayList();
                this.line = readLine();
                while (this.line != null) {
                    this.lines.add(this.line);
                    this.line = readLine();
                }
                sort();
                this.iterator = this.lines.iterator();
            }
            if (this.iterator.hasNext()) {
                this.line = this.iterator.next();
            } else {
                this.line = null;
                this.lines = null;
                this.iterator = null;
            }
            if (this.line != null) {
                return read();
            }
        }
        return ch;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        SortFilter newFilter = new SortFilter(rdr);
        newFilter.setReverse(isReverse());
        newFilter.setComparator(getComparator());
        newFilter.setInitialized(true);
        return newFilter;
    }

    public boolean isReverse() {
        return this.reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public Comparator<? super String> getComparator() {
        return this.comparator;
    }

    public void setComparator(Comparator<? super String> comparator) {
        this.comparator = comparator;
    }

    public void add(Comparator<? super String> comparator) {
        if (this.comparator != null && comparator != null) {
            throw new BuildException("can't have more than one comparator");
        }
        setComparator(comparator);
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                String paramName = param.getName();
                if (REVERSE_KEY.equals(paramName)) {
                    setReverse(Boolean.valueOf(param.getValue()).booleanValue());
                } else if (COMPARATOR_KEY.equals(paramName)) {
                    try {
                        String className = param.getValue();
                        Comparator<? super String> comparatorInstance = (Comparator) Class.forName(className).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                        setComparator(comparatorInstance);
                    } catch (ClassCastException e) {
                        throw new BuildException("Value of comparator attribute should implement java.util.Comparator interface");
                    } catch (Exception e2) {
                        throw new BuildException(e2);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void sort() {
        if (this.comparator == null) {
            if (isReverse()) {
                this.lines.sort(Comparator.reverseOrder());
                return;
            } else {
                Collections.sort(this.lines);
                return;
            }
        }
        this.lines.sort(this.comparator);
    }
}
