package polyglot.ext.jl.ast;

import polyglot.ast.Ext;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Ext_c.class */
public abstract class Ext_c implements Ext {
    protected Node node;
    protected Ext ext;

    public Ext_c(Ext ext) {
        this.node = null;
        this.ext = ext;
    }

    public Ext_c() {
        this(null);
        this.node = null;
    }

    @Override // polyglot.ast.Ext
    public void init(Node node) {
        if (this.node != null) {
            throw new InternalCompilerError("Already initialized.");
        }
        this.node = node;
        if (this.ext != null) {
            this.ext.init(node);
        }
    }

    @Override // polyglot.ast.Ext
    public Node node() {
        return this.node;
    }

    @Override // polyglot.ast.Ext
    public Ext ext() {
        return this.ext;
    }

    @Override // polyglot.ast.Ext
    public Ext ext(Ext ext) {
        Ext old = this.ext;
        this.ext = null;
        Ext_c copy = (Ext_c) copy();
        copy.ext = ext;
        this.ext = old;
        return copy;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            Ext_c copy = (Ext_c) super.clone();
            if (this.ext != null) {
                copy.ext = (Ext) this.ext.copy();
            }
            copy.node = null;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    public String toString() {
        return StringUtil.getShortNameComponent(getClass().getName());
    }

    @Override // polyglot.ast.Ext
    public void dump(CodeWriter w) {
        w.write(toString());
    }
}
