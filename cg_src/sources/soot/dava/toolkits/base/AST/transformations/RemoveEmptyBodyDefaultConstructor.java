package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/RemoveEmptyBodyDefaultConstructor.class */
public class RemoveEmptyBodyDefaultConstructor {
    public static boolean DEBUG = false;

    public static void checkAndRemoveDefault(SootClass s) {
        debug("\n\nRemoveEmptyBodyDefaultConstructor----" + s.getName());
        List methods = s.getMethods();
        List<SootMethod> constructors = new ArrayList<>();
        for (SootMethod method : methods) {
            debug("method name is" + method.getName());
            if (method.getName().indexOf("<init>") > -1) {
                constructors.add(method);
            }
        }
        if (constructors.size() != 1) {
            debug("class has more than one constructors cant do anything");
            return;
        }
        SootMethod constructor = constructors.get(0);
        if (constructor.getParameterCount() != 0) {
            debug("constructor is not the default constructor");
            return;
        }
        debug("Check that the body is empty....and call to super contains no arguments and delete");
        if (!constructor.hasActiveBody()) {
            debug("No active body found for the default constructor");
        } else if (!constructor.isPublic()) {
            debug("Default constructor is not public.");
        } else {
            Body body = constructor.getActiveBody();
            Chain units = ((DavaBody) body).getUnits();
            if (units.size() != 1) {
                debug(" DavaBody AST does not have single root");
                return;
            }
            ASTNode AST = (ASTNode) units.getFirst();
            if (!(AST instanceof ASTMethodNode)) {
                throw new RuntimeException("Starting node of DavaBody AST is not an ASTMethodNode");
            }
            ASTMethodNode methodNode = (ASTMethodNode) AST;
            debug("got methodnode check body is empty and super has nothing in it");
            List<Object> subBodies = methodNode.get_SubBodies();
            if (subBodies.size() != 1) {
                debug("Method node does not have one subBody!!!");
                return;
            }
            List methodBody = (List) subBodies.get(0);
            if (methodBody.size() != 0) {
                debug("Method body size is greater than 1 so cant do nothing");
                return;
            }
            debug("Method body is empty...check super call is empty");
            if (((DavaBody) body).get_ConstructorExpr().getArgCount() != 0) {
                debug("call to super not empty");
                return;
            }
            debug("REMOVE METHOD");
            s.removeMethod(constructor);
        }
    }

    public static void debug(String debug) {
        if (DEBUG) {
            System.out.println("DEBUG: " + debug);
        }
    }
}
