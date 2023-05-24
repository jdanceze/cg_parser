package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.ConcatResourceInputStream;
import org.apache.tools.ant.util.LineTokenizer;
import org.apache.tools.ant.util.Tokenizer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Tokens.class */
public class Tokens extends BaseResourceCollectionWrapper {
    private Tokenizer tokenizer;
    private String encoding;

    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionWrapper
    protected synchronized Collection<Resource> getCollection() {
        ResourceCollection rc = getResourceCollection();
        if (rc.isEmpty()) {
            return Collections.emptySet();
        }
        if (this.tokenizer == null) {
            this.tokenizer = new LineTokenizer();
        }
        try {
            ConcatResourceInputStream cat = new ConcatResourceInputStream(rc);
            InputStreamReader rdr = new InputStreamReader(cat, this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding));
            try {
                cat.setManagingComponent(this);
                List<Resource> result = new ArrayList<>();
                String s = this.tokenizer.getToken(rdr);
                while (s != null) {
                    StringResource resource = new StringResource(s);
                    resource.setProject(getProject());
                    result.add(resource);
                    s = this.tokenizer.getToken(rdr);
                }
                rdr.close();
                cat.close();
                return result;
            } catch (Throwable th) {
                try {
                    rdr.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            throw new BuildException("Error reading tokens", e);
        }
    }

    public synchronized void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public synchronized void add(Tokenizer tokenizer) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.tokenizer != null) {
            throw new BuildException("Only one nested tokenizer allowed.");
        }
        this.tokenizer = tokenizer;
        setChecked(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper, org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        super.dieOnCircularReference(stk, p);
        if (!isReference()) {
            if (this.tokenizer instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) this.tokenizer, stk, p);
            }
            setChecked(true);
        }
    }
}
