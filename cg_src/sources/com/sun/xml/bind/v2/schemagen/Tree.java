package com.sun.xml.bind.v2.schemagen;

import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
import com.sun.xml.bind.v2.schemagen.xmlschema.Occurs;
import com.sun.xml.bind.v2.schemagen.xmlschema.Particle;
import com.sun.xml.bind.v2.schemagen.xmlschema.TypeDefParticle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Tree.class */
public abstract class Tree {
    abstract boolean isNullable();

    protected abstract void write(ContentModelContainer contentModelContainer, boolean z, boolean z2);

    Tree() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Tree makeOptional(boolean really) {
        return really ? new Optional() : this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Tree makeRepeated(boolean really) {
        return really ? new Repeated() : this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Tree makeGroup(GroupKind kind, List<Tree> children) {
        if (children.size() == 1) {
            return children.get(0);
        }
        List<Tree> normalizedChildren = new ArrayList<>(children.size());
        for (Tree t : children) {
            if (t instanceof Group) {
                Group g = (Group) t;
                if (g.kind == kind) {
                    normalizedChildren.addAll(Arrays.asList(g.children));
                }
            }
            normalizedChildren.add(t);
        }
        return new Group(kind, (Tree[]) normalizedChildren.toArray(new Tree[normalizedChildren.size()]));
    }

    boolean canBeTopLevel() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void write(TypeDefParticle ct) {
        if (canBeTopLevel()) {
            write((ContentModelContainer) ct._cast(ContentModelContainer.class), false, false);
        } else {
            new Group(GroupKind.SEQUENCE, new Tree[]{this}).write(ct);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void writeOccurs(Occurs o, boolean isOptional, boolean repeated) {
        if (isOptional) {
            o.minOccurs(0);
        }
        if (repeated) {
            o.maxOccurs("unbounded");
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Tree$Term.class */
    static abstract class Term extends Tree {
        @Override // com.sun.xml.bind.v2.schemagen.Tree
        boolean isNullable() {
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Tree$Optional.class */
    private static final class Optional extends Tree {
        private final Tree body;

        private Optional(Tree body) {
            this.body = body;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        boolean isNullable() {
            return true;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        Tree makeOptional(boolean really) {
            return this;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            this.body.write(parent, true, repeated);
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Tree$Repeated.class */
    private static final class Repeated extends Tree {
        private final Tree body;

        private Repeated(Tree body) {
            this.body = body;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        boolean isNullable() {
            return this.body.isNullable();
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        Tree makeRepeated(boolean really) {
            return this;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            this.body.write(parent, isOptional, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Tree$Group.class */
    public static final class Group extends Tree {
        private final GroupKind kind;
        private final Tree[] children;

        private Group(GroupKind kind, Tree... children) {
            this.kind = kind;
            this.children = children;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        boolean canBeTopLevel() {
            return true;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        boolean isNullable() {
            Tree[] treeArr;
            Tree[] treeArr2;
            if (this.kind == GroupKind.CHOICE) {
                for (Tree t : this.children) {
                    if (t.isNullable()) {
                        return true;
                    }
                }
                return false;
            }
            for (Tree t2 : this.children) {
                if (!t2.isNullable()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.sun.xml.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            Tree[] treeArr;
            Particle c = this.kind.write(parent);
            writeOccurs(c, isOptional, repeated);
            for (Tree child : this.children) {
                child.write(c, false, false);
            }
        }
    }
}
