package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Redirector;
import org.apache.tools.ant.util.MergingMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/RedirectorElement.class */
public class RedirectorElement extends DataType {
    private Boolean logError;
    private String outputProperty;
    private String errorProperty;
    private String inputString;
    private Boolean append;
    private Boolean alwaysLog;
    private Boolean createEmptyFiles;
    private Mapper inputMapper;
    private Mapper outputMapper;
    private Mapper errorMapper;
    private String outputEncoding;
    private String errorEncoding;
    private String inputEncoding;
    private Boolean logInputString;
    private boolean usingInput = false;
    private boolean usingOutput = false;
    private boolean usingError = false;
    private Vector<FilterChain> inputFilterChains = new Vector<>();
    private Vector<FilterChain> outputFilterChains = new Vector<>();
    private Vector<FilterChain> errorFilterChains = new Vector<>();
    private boolean outputIsBinary = false;

    public void addConfiguredInputMapper(Mapper inputMapper) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.inputMapper != null) {
            if (this.usingInput) {
                throw new BuildException("attribute \"input\" cannot coexist with a nested <inputmapper>");
            }
            throw new BuildException("Cannot have > 1 <inputmapper>");
        }
        setChecked(false);
        this.inputMapper = inputMapper;
    }

    public void addConfiguredOutputMapper(Mapper outputMapper) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.outputMapper != null) {
            if (this.usingOutput) {
                throw new BuildException("attribute \"output\" cannot coexist with a nested <outputmapper>");
            }
            throw new BuildException("Cannot have > 1 <outputmapper>");
        }
        setChecked(false);
        this.outputMapper = outputMapper;
    }

    public void addConfiguredErrorMapper(Mapper errorMapper) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.errorMapper != null) {
            if (this.usingError) {
                throw new BuildException("attribute \"error\" cannot coexist with a nested <errormapper>");
            }
            throw new BuildException("Cannot have > 1 <errormapper>");
        }
        setChecked(false);
        this.errorMapper = errorMapper;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.usingInput || this.usingOutput || this.usingError || this.inputString != null || this.logError != null || this.append != null || this.createEmptyFiles != null || this.inputEncoding != null || this.outputEncoding != null || this.errorEncoding != null || this.outputProperty != null || this.errorProperty != null || this.logInputString != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    public void setInput(File input) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.inputString != null) {
            throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
        }
        this.usingInput = true;
        this.inputMapper = createMergeMapper(input);
    }

    public void setInputString(String inputString) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.usingInput) {
            throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
        }
        this.inputString = inputString;
    }

    public void setLogInputString(boolean logInputString) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.logInputString = logInputString ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setOutput(File out) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (out == null) {
            throw new IllegalArgumentException("output file specified as null");
        }
        this.usingOutput = true;
        this.outputMapper = createMergeMapper(out);
    }

    public void setOutputEncoding(String outputEncoding) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.outputEncoding = outputEncoding;
    }

    public void setErrorEncoding(String errorEncoding) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.errorEncoding = errorEncoding;
    }

    public void setInputEncoding(String inputEncoding) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.inputEncoding = inputEncoding;
    }

    public void setLogError(boolean logError) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.logError = logError ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setError(File error) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (error == null) {
            throw new IllegalArgumentException("error file specified as null");
        }
        this.usingError = true;
        this.errorMapper = createMergeMapper(error);
    }

    public void setOutputProperty(String outputProperty) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.outputProperty = outputProperty;
    }

    public void setAppend(boolean append) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.append = append ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setAlwaysLog(boolean alwaysLog) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.alwaysLog = alwaysLog ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setCreateEmptyFiles(boolean createEmptyFiles) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.createEmptyFiles = createEmptyFiles ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setErrorProperty(String errorProperty) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.errorProperty = errorProperty;
    }

    public FilterChain createInputFilterChain() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        FilterChain result = new FilterChain();
        result.setProject(getProject());
        this.inputFilterChains.add(result);
        setChecked(false);
        return result;
    }

    public FilterChain createOutputFilterChain() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        FilterChain result = new FilterChain();
        result.setProject(getProject());
        this.outputFilterChains.add(result);
        setChecked(false);
        return result;
    }

    public FilterChain createErrorFilterChain() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        FilterChain result = new FilterChain();
        result.setProject(getProject());
        this.errorFilterChains.add(result);
        setChecked(false);
        return result;
    }

    public void setBinaryOutput(boolean b) {
        this.outputIsBinary = b;
    }

    public void configure(Redirector redirector) {
        configure(redirector, null);
    }

    public void configure(Redirector redirector, String sourcefile) {
        if (isReference()) {
            getRef().configure(redirector, sourcefile);
            return;
        }
        dieOnCircularReference();
        if (this.alwaysLog != null) {
            redirector.setAlwaysLog(this.alwaysLog.booleanValue());
        }
        if (this.logError != null) {
            redirector.setLogError(this.logError.booleanValue());
        }
        if (this.append != null) {
            redirector.setAppend(this.append.booleanValue());
        }
        if (this.createEmptyFiles != null) {
            redirector.setCreateEmptyFiles(this.createEmptyFiles.booleanValue());
        }
        if (this.outputProperty != null) {
            redirector.setOutputProperty(this.outputProperty);
        }
        if (this.errorProperty != null) {
            redirector.setErrorProperty(this.errorProperty);
        }
        if (this.inputString != null) {
            redirector.setInputString(this.inputString);
        }
        if (this.logInputString != null) {
            redirector.setLogInputString(this.logInputString.booleanValue());
        }
        if (this.inputMapper != null) {
            String[] inputTargets = null;
            try {
                inputTargets = this.inputMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException enPeaEx) {
                if (sourcefile != null) {
                    throw enPeaEx;
                }
            }
            if (inputTargets != null && inputTargets.length > 0) {
                redirector.setInput(toFileArray(inputTargets));
            }
        }
        if (this.outputMapper != null) {
            String[] outputTargets = null;
            try {
                outputTargets = this.outputMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException enPeaEx2) {
                if (sourcefile != null) {
                    throw enPeaEx2;
                }
            }
            if (outputTargets != null && outputTargets.length > 0) {
                redirector.setOutput(toFileArray(outputTargets));
            }
        }
        if (this.errorMapper != null) {
            String[] errorTargets = null;
            try {
                errorTargets = this.errorMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException enPeaEx3) {
                if (sourcefile != null) {
                    throw enPeaEx3;
                }
            }
            if (errorTargets != null && errorTargets.length > 0) {
                redirector.setError(toFileArray(errorTargets));
            }
        }
        if (!this.inputFilterChains.isEmpty()) {
            redirector.setInputFilterChains(this.inputFilterChains);
        }
        if (!this.outputFilterChains.isEmpty()) {
            redirector.setOutputFilterChains(this.outputFilterChains);
        }
        if (!this.errorFilterChains.isEmpty()) {
            redirector.setErrorFilterChains(this.errorFilterChains);
        }
        if (this.inputEncoding != null) {
            redirector.setInputEncoding(this.inputEncoding);
        }
        if (this.outputEncoding != null) {
            redirector.setOutputEncoding(this.outputEncoding);
        }
        if (this.errorEncoding != null) {
            redirector.setErrorEncoding(this.errorEncoding);
        }
        redirector.setBinaryOutput(this.outputIsBinary);
    }

    protected Mapper createMergeMapper(File destfile) {
        Mapper result = new Mapper(getProject());
        result.setClassname(MergingMapper.class.getName());
        result.setTo(destfile.getAbsolutePath());
        return result;
    }

    protected File[] toFileArray(String[] name) {
        if (name == null) {
            return null;
        }
        ArrayList<File> list = new ArrayList<>(name.length);
        for (String n : name) {
            if (n != null) {
                list.add(getProject().resolveFile(n));
            }
        }
        return (File[]) list.toArray(new File[list.size()]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        for (Mapper m : Arrays.asList(this.inputMapper, this.outputMapper, this.errorMapper)) {
            if (m != null) {
                stk.push(m);
                m.dieOnCircularReference(stk, p);
                stk.pop();
            }
        }
        List<? extends List<FilterChain>> filterChainLists = Arrays.asList(this.inputFilterChains, this.outputFilterChains, this.errorFilterChains);
        for (List<FilterChain> filterChains : filterChainLists) {
            if (filterChains != null) {
                for (FilterChain fc : filterChains) {
                    pushAndInvokeCircularReferenceCheck(fc, stk, p);
                }
            }
        }
        setChecked(true);
    }

    private RedirectorElement getRef() {
        return (RedirectorElement) getCheckedRef(RedirectorElement.class);
    }
}
