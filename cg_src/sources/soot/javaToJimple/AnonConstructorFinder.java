package soot.javaToJimple;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.ConstructorInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/AnonConstructorFinder.class */
public class AnonConstructorFinder extends ContextVisitor {
    private static final Logger logger = LoggerFactory.getLogger(AnonConstructorFinder.class);

    public AnonConstructorFinder(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
    }

    @Override // polyglot.visit.ContextVisitor, polyglot.visit.ErrorHandlingVisitor, polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if ((n instanceof New) && ((New) n).anonType() != null) {
            try {
                List<Type> argTypes = new ArrayList<>();
                for (Expr expr : ((New) n).arguments()) {
                    argTypes.add(expr.type());
                }
                ConstructorInstance ci = typeSystem().findConstructor(((New) n).anonType().superType().toClass(), argTypes, ((New) n).anonType().superType().toClass());
                InitialResolver.v().addToAnonConstructorMap((New) n, ci);
            } catch (SemanticException e) {
                System.out.println(e.getMessage());
                logger.error(e.getMessage(), (Throwable) e);
            }
        }
        return this;
    }
}
