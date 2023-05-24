package soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JastAddJ.BodyDecl;
import soot.JastAddJ.CompilationUnit;
import soot.JastAddJ.ConstructorDecl;
import soot.JastAddJ.MethodDecl;
import soot.JastAddJ.Problem;
import soot.JastAddJ.Program;
import soot.JastAddJ.TypeDecl;
import soot.Singletons;
import soot.javaToJimple.IInitialResolver;
/* loaded from: gencallgraphv3.jar:soot/JastAddInitialResolver.class */
public class JastAddInitialResolver implements IInitialResolver {
    private static final Logger logger = LoggerFactory.getLogger(JastAddInitialResolver.class);
    protected Map<String, CompilationUnit> classNameToCU = new HashMap();

    public JastAddInitialResolver(Singletons.Global g) {
    }

    public static JastAddInitialResolver v() {
        return G.v().soot_JastAddInitialResolver();
    }

    @Override // soot.javaToJimple.IInitialResolver
    public void formAst(String fullPath, List<String> locations, String className) {
        Program program = SootResolver.v().getProgram();
        CompilationUnit u = program.getCachedOrLoadCompilationUnit(fullPath);
        if (u != null && !u.isResolved) {
            u.isResolved = true;
            ArrayList<Problem> errors = new ArrayList<>();
            u.errorCheck(errors);
            if (!errors.isEmpty()) {
                Iterator<Problem> it = errors.iterator();
                while (it.hasNext()) {
                    Problem p = it.next();
                    logger.debug(new StringBuilder().append(p).toString());
                }
                throw new CompilationDeathException(0, "there were errors during parsing and/or type checking (JastAdd frontend)");
            }
            u.transformation();
            u.jimplify1phase1();
            u.jimplify1phase2();
            HashSet<SootClass> types = new HashSet<>();
            Iterator<TypeDecl> it2 = u.getTypeDecls().iterator();
            while (it2.hasNext()) {
                TypeDecl typeDecl = it2.next();
                collectTypeDecl(typeDecl, types);
            }
            if (types.isEmpty()) {
                this.classNameToCU.put(className, u);
                return;
            }
            Iterator<SootClass> it3 = types.iterator();
            while (it3.hasNext()) {
                SootClass sc = it3.next();
                this.classNameToCU.put(sc.getName(), u);
            }
        }
    }

    private void collectTypeDecl(TypeDecl typeDecl, HashSet<SootClass> types) {
        types.add(typeDecl.getSootClassDecl());
        for (TypeDecl nestedType : typeDecl.nestedTypes()) {
            collectTypeDecl(nestedType, types);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TypeDecl findNestedTypeDecl(TypeDecl typeDecl, SootClass sc) {
        if (typeDecl.sootClass() == sc) {
            return typeDecl;
        }
        for (TypeDecl nestedType : typeDecl.nestedTypes()) {
            TypeDecl t = findNestedTypeDecl(nestedType, sc);
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    @Override // soot.javaToJimple.IInitialResolver
    public IInitialResolver.Dependencies resolveFromJavaFile(SootClass sootclass) {
        CompilationUnit u = this.classNameToCU.get(sootclass.getName());
        if (u == null) {
            throw new RuntimeException("Error: couldn't find class: " + sootclass.getName() + " are the packages set properly?");
        }
        HashSet<SootClass> types = new HashSet<>();
        Iterator<TypeDecl> it = u.getTypeDecls().iterator();
        while (it.hasNext()) {
            TypeDecl typeDecl = it.next();
            collectTypeDecl(typeDecl, types);
        }
        IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
        u.collectTypesToHierarchy(deps.typesToHierarchy);
        u.collectTypesToSignatures(deps.typesToSignature);
        Iterator<SootClass> it2 = types.iterator();
        while (it2.hasNext()) {
            SootClass sc = it2.next();
            for (SootMethod m : sc.getMethods()) {
                m.setSource(new MethodSource() { // from class: soot.JastAddInitialResolver.1
                    @Override // soot.MethodSource
                    public Body getBody(SootMethod m2, String phaseName) {
                        SootClass sc2 = m2.getDeclaringClass();
                        CompilationUnit u2 = JastAddInitialResolver.this.classNameToCU.get(sc2.getName());
                        Iterator<TypeDecl> it3 = u2.getTypeDecls().iterator();
                        while (it3.hasNext()) {
                            TypeDecl typeDecl2 = JastAddInitialResolver.this.findNestedTypeDecl(it3.next(), sc2);
                            if (typeDecl2 != null) {
                                if (typeDecl2.clinit == m2) {
                                    typeDecl2.jimplify2clinit();
                                    return m2.getActiveBody();
                                }
                                Iterator<BodyDecl> it4 = typeDecl2.getBodyDecls().iterator();
                                while (it4.hasNext()) {
                                    BodyDecl bodyDecl = it4.next();
                                    if (bodyDecl instanceof MethodDecl) {
                                        MethodDecl methodDecl = (MethodDecl) bodyDecl;
                                        if (m2.equals(methodDecl.sootMethod)) {
                                            methodDecl.jimplify2();
                                            return m2.getActiveBody();
                                        }
                                    } else if (bodyDecl instanceof ConstructorDecl) {
                                        ConstructorDecl constrDecl = (ConstructorDecl) bodyDecl;
                                        if (m2.equals(constrDecl.sootMethod)) {
                                            constrDecl.jimplify2();
                                            return m2.getActiveBody();
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                                continue;
                            }
                        }
                        throw new RuntimeException("Could not find body for " + m2.getSignature() + " in " + m2.getDeclaringClass().getName());
                    }
                });
            }
        }
        return deps;
    }
}
