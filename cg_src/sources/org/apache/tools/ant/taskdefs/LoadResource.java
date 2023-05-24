package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/LoadResource.class */
public class LoadResource extends Task {
    private Resource src;
    private boolean failOnError = true;
    private boolean quiet = false;
    private String encoding = null;
    private String property = null;
    private final List<FilterChain> filterChains = new Vector();

    public final void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public final void setProperty(String property) {
        this.property = property;
    }

    public final void setFailonerror(boolean fail) {
        this.failOnError = fail;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
        if (quiet) {
            this.failOnError = false;
        }
    }

    @Override // org.apache.tools.ant.Task
    public final void execute() throws BuildException {
        String text;
        if (this.src == null) {
            throw new BuildException("source resource not defined");
        }
        if (this.property == null) {
            throw new BuildException("output property not defined");
        }
        if (this.quiet && this.failOnError) {
            throw new BuildException("quiet and failonerror cannot both be set to true");
        }
        if (!this.src.isExists()) {
            String message = this.src + " doesn't exist";
            if (this.failOnError) {
                throw new BuildException(message);
            }
            log(message, this.quiet ? 1 : 0);
            return;
        }
        log("loading " + this.src + " into property " + this.property, 3);
        Charset charset = this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding);
        try {
            long len = this.src.getSize();
            log("resource size = " + (len != -1 ? String.valueOf(len) : "unknown"), 4);
            int size = (int) len;
            if (size != 0) {
                ChainReaderHelper.ChainReader chainReader = new ChainReaderHelper(getProject(), new InputStreamReader(new BufferedInputStream(this.src.getInputStream()), charset), this.filterChains).with(crh -> {
                    if (this.src.getSize() != -1) {
                        size.setBufferSize(size);
                    }
                }).getAssembledReader();
                text = chainReader.readFully();
                if (chainReader != null) {
                    chainReader.close();
                }
            } else {
                log("Do not set property " + this.property + " as its length is 0.", this.quiet ? 3 : 2);
                text = null;
            }
            if (text != null && !text.isEmpty()) {
                getProject().setNewProperty(this.property, text);
                log("loaded " + text.length() + " characters", 3);
                log(this.property + " := " + text, 4);
            }
        } catch (IOException ioe) {
            String message2 = "Unable to load resource: " + ioe;
            if (this.failOnError) {
                throw new BuildException(message2, ioe, getLocation());
            }
            log(message2, this.quiet ? 3 : 0);
        } catch (BuildException be) {
            if (this.failOnError) {
                throw be;
            }
            log(be.getMessage(), this.quiet ? 3 : 0);
        }
    }

    public final void addFilterChain(FilterChain filter) {
        this.filterChains.add(filter);
    }

    public void addConfigured(ResourceCollection a) {
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported");
        }
        this.src = a.iterator().next();
    }
}
