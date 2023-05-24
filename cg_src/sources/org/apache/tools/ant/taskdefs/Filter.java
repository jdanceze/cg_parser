package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Filter.class */
public class Filter extends Task {
    private String token;
    private String value;
    private File filtersFile;

    public void setToken(String token) {
        this.token = token;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setFiltersfile(File filtersFile) {
        this.filtersFile = filtersFile;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        boolean isFiltersFromFile = this.filtersFile != null && this.token == null && this.value == null;
        boolean isSingleFilter = (this.filtersFile != null || this.token == null || this.value == null) ? false : true;
        if (!isFiltersFromFile && !isSingleFilter) {
            throw new BuildException("both token and value parameters, or only a filtersFile parameter is required", getLocation());
        }
        if (isSingleFilter) {
            getProject().getGlobalFilterSet().addFilter(this.token, this.value);
        }
        if (isFiltersFromFile) {
            readFilters();
        }
    }

    protected void readFilters() throws BuildException {
        log("Reading filters from " + this.filtersFile, 3);
        getProject().getGlobalFilterSet().readFiltersFromFile(this.filtersFile);
    }
}
