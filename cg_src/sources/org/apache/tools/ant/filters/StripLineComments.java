package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/StripLineComments.class */
public final class StripLineComments extends BaseParamFilterReader implements ChainableReader {
    private static final String COMMENTS_KEY = "comment";
    private Vector<String> comments;
    private String line;

    public StripLineComments() {
        this.comments = new Vector<>();
        this.line = null;
    }

    public StripLineComments(Reader in) {
        super(in);
        this.comments = new Vector<>();
        this.line = null;
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
            this.line = readLine();
            int commentsSize = this.comments.size();
            while (this.line != null) {
                int i = 0;
                while (true) {
                    if (i >= commentsSize) {
                        break;
                    }
                    String comment = this.comments.elementAt(i);
                    if (!this.line.startsWith(comment)) {
                        i++;
                    } else {
                        this.line = null;
                        break;
                    }
                }
                if (this.line != null) {
                    break;
                }
                this.line = readLine();
            }
            if (this.line != null) {
                return read();
            }
        }
        return ch;
    }

    public void addConfiguredComment(Comment comment) {
        this.comments.addElement(comment.getValue());
    }

    private void setComments(Vector<String> comments) {
        this.comments = comments;
    }

    private Vector<String> getComments() {
        return this.comments;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        StripLineComments newFilter = new StripLineComments(rdr);
        newFilter.setComments(getComments());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if ("comment".equals(param.getType())) {
                    this.comments.addElement(param.getValue());
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/StripLineComments$Comment.class */
    public static class Comment {
        private String value;

        public final void setValue(String comment) {
            if (this.value != null) {
                throw new IllegalStateException("Comment value already set.");
            }
            this.value = comment;
        }

        public final String getValue() {
            return this.value;
        }

        public void addText(String comment) {
            setValue(comment);
        }
    }
}
