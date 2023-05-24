package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.PropertyOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length.class */
public class Length extends Task implements Condition {
    private static final String ALL = "all";
    private static final String EACH = "each";
    private static final String STRING = "string";
    private static final String LENGTH_REQUIRED = "Use of the Length condition requires that the length attribute be set.";
    private String property;
    private String string;
    private Boolean trim;
    private String mode = ALL;
    private Comparison when = Comparison.EQUAL;
    private Long length;
    private Resources resources;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$When.class */
    public static class When extends Comparison {
    }

    public synchronized void setProperty(String property) {
        this.property = property;
    }

    public synchronized void setResource(Resource resource) {
        add(resource);
    }

    public synchronized void setFile(File file) {
        add(new FileResource(file));
    }

    public synchronized void add(FileSet fs) {
        add((ResourceCollection) fs);
    }

    public synchronized void add(ResourceCollection c) {
        if (c == null) {
            return;
        }
        this.resources = this.resources == null ? new Resources() : this.resources;
        this.resources.add(c);
    }

    public synchronized void setLength(long ell) {
        this.length = Long.valueOf(ell);
    }

    public synchronized void setWhen(When w) {
        setWhen((Comparison) w);
    }

    public synchronized void setWhen(Comparison c) {
        this.when = c;
    }

    public synchronized void setMode(FileMode m) {
        this.mode = m.getValue();
    }

    public synchronized void setString(String string) {
        this.string = string;
        this.mode = STRING;
    }

    public synchronized void setTrim(boolean trim) {
        this.trim = Boolean.valueOf(trim);
    }

    public boolean getTrim() {
        return Boolean.TRUE.equals(this.trim);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        validate();
        OutputStream out = this.property == null ? new LogOutputStream((Task) this, 2) : new PropertyOutputStream(getProject(), this.property);
        PrintStream ps = new PrintStream(out);
        String str = this.mode;
        boolean z = true;
        switch (str.hashCode()) {
            case -891985903:
                if (str.equals(STRING)) {
                    z = false;
                    break;
                }
                break;
            case 96673:
                if (str.equals(ALL)) {
                    z = true;
                    break;
                }
                break;
            case 3105281:
                if (str.equals(EACH)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                ps.print(getLength(this.string, getTrim()));
                ps.close();
                return;
            case true:
                handleResources(new EachHandler(ps));
                return;
            case true:
                handleResources(new AllHandler(ps));
                return;
            default:
                return;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() {
        Long ell;
        validate();
        if (this.length == null) {
            throw new BuildException(LENGTH_REQUIRED);
        }
        if (STRING.equals(this.mode)) {
            ell = Long.valueOf(getLength(this.string, getTrim()));
        } else {
            AccumHandler h = new AccumHandler();
            handleResources(h);
            ell = Long.valueOf(h.getAccum());
        }
        return this.when.evaluate(ell.compareTo(this.length));
    }

    private void validate() {
        if (this.string != null) {
            if (this.resources != null) {
                throw new BuildException("the string length function is incompatible with the file/resource length function");
            }
            if (!STRING.equals(this.mode)) {
                throw new BuildException("the mode attribute is for use with the file/resource length function");
            }
        } else if (this.resources != null) {
            if (!EACH.equals(this.mode) && !ALL.equals(this.mode)) {
                throw new BuildException("invalid mode setting for file/resource length function: \"" + this.mode + "\"");
            }
            if (this.trim != null) {
                throw new BuildException("the trim attribute is for use with the string length function only");
            }
        } else {
            throw new BuildException("you must set either the string attribute or specify one or more files using the file attribute or nested resource collections");
        }
    }

    private void handleResources(Handler h) {
        Iterator<Resource> it = this.resources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            if (!r.isExists()) {
                log(r + " does not exist", 1);
            }
            if (r.isDirectory()) {
                log(r + " is a directory; length may not be meaningful", 1);
            }
            h.handle(r);
        }
        h.complete();
    }

    private static long getLength(String s, boolean t) {
        return (t ? s.trim() : s).length();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$FileMode.class */
    public static class FileMode extends EnumeratedAttribute {
        static final String[] MODES = {Length.EACH, Length.ALL};

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return MODES;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$Handler.class */
    public abstract class Handler {
        private PrintStream ps;

        protected abstract void handle(Resource resource);

        Handler(PrintStream ps) {
            this.ps = ps;
        }

        protected PrintStream getPs() {
            return this.ps;
        }

        void complete() {
            FileUtils.close((OutputStream) this.ps);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$EachHandler.class */
    private class EachHandler extends Handler {
        EachHandler(PrintStream ps) {
            super(ps);
        }

        @Override // org.apache.tools.ant.taskdefs.Length.Handler
        protected void handle(Resource r) {
            getPs().print(r.toString());
            getPs().print(" : ");
            long size = r.getSize();
            if (size == -1) {
                getPs().println("unknown");
            } else {
                getPs().println(size);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$AccumHandler.class */
    private class AccumHandler extends Handler {
        private long accum;

        AccumHandler() {
            super(null);
            this.accum = 0L;
        }

        protected AccumHandler(PrintStream ps) {
            super(ps);
            this.accum = 0L;
        }

        protected long getAccum() {
            return this.accum;
        }

        @Override // org.apache.tools.ant.taskdefs.Length.Handler
        protected synchronized void handle(Resource r) {
            long size = r.getSize();
            if (size == -1) {
                Length.this.log("Size unknown for " + r.toString(), 1);
            } else {
                this.accum += size;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Length$AllHandler.class */
    private class AllHandler extends AccumHandler {
        AllHandler(PrintStream ps) {
            super(ps);
        }

        @Override // org.apache.tools.ant.taskdefs.Length.Handler
        void complete() {
            getPs().print(getAccum());
            super.complete();
        }
    }
}
