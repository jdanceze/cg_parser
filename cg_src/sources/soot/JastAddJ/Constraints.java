package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constraints.class */
public class Constraints {
    public boolean rawAccess = false;
    private Collection typeVariables = new ArrayList(4);
    private Map constraintsMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constraints$ConstraintSet.class */
    public static class ConstraintSet {
        public Collection supertypeConstraints = new HashSet(4);
        public Collection subtypeConstraints = new HashSet(4);
        public Collection equaltypeConstraints = new HashSet(4);
        public TypeDecl typeArgument;

        ConstraintSet() {
        }
    }

    public void addTypeVariable(TypeVariable T) {
        if (!this.typeVariables.contains(T)) {
            this.typeVariables.add(T);
            this.constraintsMap.put(T, new ConstraintSet());
        }
    }

    public boolean unresolvedTypeArguments() {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            if (set.typeArgument == null) {
                return true;
            }
        }
        return false;
    }

    public void printConstraints() {
        System.err.println("Current constraints:");
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            for (TypeDecl U : set.supertypeConstraints) {
                System.err.println("  " + T.fullName() + " :> " + U.fullName());
            }
            for (TypeDecl U2 : set.subtypeConstraints) {
                System.err.println("  " + T.fullName() + " <: " + U2.fullName());
            }
            for (TypeDecl U3 : set.equaltypeConstraints) {
                System.err.println("  " + T.fullName() + " = " + U3.fullName());
            }
        }
    }

    public void resolveBounds() {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            if (set.typeArgument == null) {
                set.typeArgument = T.getTypeBound(0).type();
            }
        }
    }

    public void resolveEqualityConstraints() {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            boolean done = false;
            Iterator i2 = set.equaltypeConstraints.iterator();
            while (!done && i2.hasNext()) {
                TypeDecl U = (TypeDecl) i2.next();
                if (!this.typeVariables.contains(U)) {
                    replaceEqualityConstraints(T, U);
                    set.equaltypeConstraints.clear();
                    set.equaltypeConstraints.add(U);
                    set.typeArgument = U;
                    done = true;
                } else if (T != U) {
                    replaceAllConstraints(T, U);
                    done = true;
                }
            }
            if (set.typeArgument == null && set.equaltypeConstraints.size() == 1 && set.equaltypeConstraints.contains(T)) {
                set.typeArgument = T;
            }
        }
    }

    public void replaceEqualityConstraints(TypeDecl before, TypeDecl after) {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            replaceConstraints(set.equaltypeConstraints, before, after);
        }
    }

    public void replaceAllConstraints(TypeDecl before, TypeDecl after) {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            replaceConstraints(set.supertypeConstraints, before, after);
            replaceConstraints(set.subtypeConstraints, before, after);
            replaceConstraints(set.equaltypeConstraints, before, after);
        }
    }

    private void replaceConstraints(Collection constraints, TypeDecl before, TypeDecl after) {
        ArrayList arrayList = new ArrayList();
        Iterator i2 = constraints.iterator();
        while (i2.hasNext()) {
            TypeDecl U = (TypeDecl) i2.next();
            if (U == before) {
                i2.remove();
                arrayList.add(after);
            }
        }
        constraints.addAll(arrayList);
    }

    public void resolveSubtypeConstraints() {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            if (!set.subtypeConstraints.isEmpty() || T.getNumTypeBound() > 0) {
                if (set.typeArgument == null) {
                    ArrayList bounds = new ArrayList();
                    for (Object obj : set.subtypeConstraints) {
                        bounds.add(obj);
                    }
                    for (int i = 0; i < T.getNumTypeBound(); i++) {
                        bounds.add(T.getTypeBound(i).type());
                    }
                    set.typeArgument = GLBTypeFactory.glb(bounds);
                }
            }
        }
    }

    public void resolveSupertypeConstraints() {
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            if (!set.supertypeConstraints.isEmpty() && set.typeArgument == null) {
                TypeDecl typeDecl = T.lookupLUBType(set.supertypeConstraints).lub();
                set.typeArgument = typeDecl;
            }
        }
    }

    public static HashSet directSupertypes(TypeDecl t) {
        if (t instanceof ClassDecl) {
            ClassDecl type = (ClassDecl) t;
            HashSet set = new HashSet();
            if (type.hasSuperclass()) {
                set.add(type.superclass());
            }
            for (int i = 0; i < type.getNumImplements(); i++) {
                set.add(type.getImplements(i).type());
            }
            return set;
        } else if (t instanceof InterfaceDecl) {
            InterfaceDecl type2 = (InterfaceDecl) t;
            HashSet set2 = new HashSet();
            for (int i2 = 0; i2 < type2.getNumSuperInterfaceId(); i2++) {
                set2.add(type2.getSuperInterfaceId(i2).type());
            }
            return set2;
        } else if (t instanceof TypeVariable) {
            TypeVariable type3 = (TypeVariable) t;
            HashSet set3 = new HashSet();
            for (int i3 = 0; i3 < type3.getNumTypeBound(); i3++) {
                set3.add(type3.getTypeBound(i3).type());
            }
            return set3;
        } else {
            throw new Error("Operation not supported for " + t.fullName() + ", " + t.getClass().getName());
        }
    }

    public static HashSet parameterizedSupertypes(TypeDecl t) {
        HashSet result = new HashSet();
        addParameterizedSupertypes(t, new HashSet(), result);
        return result;
    }

    public static void addParameterizedSupertypes(TypeDecl t, HashSet processed, HashSet result) {
        if (!processed.contains(t)) {
            processed.add(t);
            if (t.isParameterizedType()) {
                result.add(t);
            }
            Iterator iter = directSupertypes(t).iterator();
            while (iter.hasNext()) {
                TypeDecl typeDecl = (TypeDecl) iter.next();
                addParameterizedSupertypes(typeDecl, processed, result);
            }
        }
    }

    public Collection typeArguments() {
        ArrayList list = new ArrayList(this.typeVariables.size());
        for (TypeVariable T : this.typeVariables) {
            ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
            list.add(set.typeArgument);
        }
        return list;
    }

    public void addSupertypeConstraint(TypeDecl T, TypeDecl A) {
        ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
        set.supertypeConstraints.add(A);
    }

    public void addSubtypeConstraint(TypeDecl T, TypeDecl A) {
        ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
        set.subtypeConstraints.add(A);
    }

    public void addEqualConstraint(TypeDecl T, TypeDecl A) {
        ConstraintSet set = (ConstraintSet) this.constraintsMap.get(T);
        set.equaltypeConstraints.add(A);
    }

    public void convertibleTo(TypeDecl A, TypeDecl F) {
        if (!F.involvesTypeParameters() || A.isNull()) {
            return;
        }
        if (A.isUnboxedPrimitive()) {
            convertibleTo(A.boxed(), F);
        } else if (F instanceof TypeVariable) {
            if (this.typeVariables.contains(F)) {
                addSupertypeConstraint(F, A);
            }
        } else if (F.isArrayDecl()) {
            TypeDecl U = ((ArrayDecl) F).componentType();
            if (!U.involvesTypeParameters()) {
                return;
            }
            if (A.isArrayDecl()) {
                TypeDecl V = ((ArrayDecl) A).componentType();
                if (V.isReferenceType()) {
                    convertibleTo(V, U);
                }
            } else if (A.isTypeVariable()) {
                TypeVariable t = (TypeVariable) A;
                for (int i = 0; i < t.getNumTypeBound(); i++) {
                    TypeDecl typeBound = t.getTypeBound(i).type();
                    if (typeBound.isArrayDecl() && ((ArrayDecl) typeBound).componentType().isReferenceType()) {
                        TypeDecl V2 = ((ArrayDecl) typeBound).componentType();
                        convertibleTo(V2, U);
                    }
                }
            }
        } else if ((F instanceof ParTypeDecl) && !F.isRawType()) {
            Iterator iter = parameterizedSupertypes(A).iterator();
            while (iter.hasNext()) {
                ParTypeDecl PF = (ParTypeDecl) F;
                ParTypeDecl PA = (ParTypeDecl) iter.next();
                if (PF.genericDecl() == PA.genericDecl()) {
                    if (A.isRawType()) {
                        this.rawAccess = true;
                    } else {
                        for (int i2 = 0; i2 < PF.getNumArgument(); i2++) {
                            TypeDecl T = PF.getArgument(i2).type();
                            if (T.involvesTypeParameters()) {
                                if (!T.isWildcard()) {
                                    TypeDecl V3 = PA.getArgument(i2).type();
                                    constraintEqual(V3, T);
                                } else if (T instanceof WildcardExtendsType) {
                                    TypeDecl U2 = ((WildcardExtendsType) T).getAccess().type();
                                    TypeDecl S = PA.getArgument(i2).type();
                                    if (!S.isWildcard()) {
                                        convertibleTo(S, U2);
                                    } else if (S instanceof WildcardExtendsType) {
                                        TypeDecl V4 = ((WildcardExtendsType) S).getAccess().type();
                                        convertibleTo(V4, U2);
                                    }
                                } else if (T instanceof WildcardSuperType) {
                                    TypeDecl U3 = ((WildcardSuperType) T).getAccess().type();
                                    TypeDecl S2 = PA.getArgument(i2).type();
                                    if (!S2.isWildcard()) {
                                        convertibleFrom(S2, U3);
                                    } else if (S2 instanceof WildcardSuperType) {
                                        TypeDecl V5 = ((WildcardSuperType) S2).getAccess().type();
                                        convertibleFrom(V5, U3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void convertibleFrom(TypeDecl A, TypeDecl F) {
        if (!F.involvesTypeParameters() || A.isNull()) {
            return;
        }
        if (F instanceof TypeVariable) {
            if (this.typeVariables.contains(F)) {
                addSubtypeConstraint(F, A);
            }
        } else if (F.isArrayDecl()) {
            TypeDecl U = ((ArrayDecl) F).componentType();
            if (A.isArrayDecl()) {
                convertibleFrom(((ArrayDecl) A).componentType(), U);
            } else if (A.isTypeVariable()) {
                TypeVariable t = (TypeVariable) A;
                for (int i = 0; i < t.getNumTypeBound(); i++) {
                    TypeDecl typeBound = t.getTypeBound(i).type();
                    if (typeBound.isArrayDecl() && ((ArrayDecl) typeBound).componentType().isReferenceType()) {
                        convertibleFrom(((ArrayDecl) typeBound).componentType(), U);
                    }
                }
            }
        } else if ((F instanceof ParTypeDecl) && !F.isRawType() && (A instanceof ParTypeDecl) && !A.isRawType()) {
            ParTypeDecl PF = (ParTypeDecl) F;
            ParTypeDecl PA = (ParTypeDecl) A;
            TypeDecl G = PF.genericDecl();
            TypeDecl H = PA.genericDecl();
            for (int i2 = 0; i2 < PF.getNumArgument(); i2++) {
                TypeDecl T = PF.getArgument(i2).type();
                if (T.involvesTypeParameters()) {
                    if (!T.isWildcard()) {
                        if (G.instanceOf(H)) {
                            if (H != G) {
                                Iterator iter = parameterizedSupertypes(F).iterator();
                                while (iter.hasNext()) {
                                    TypeDecl V = (TypeDecl) iter.next();
                                    if (!V.isRawType() && ((ParTypeDecl) V).genericDecl() == H && F.instanceOf(V)) {
                                        convertibleFrom(A, V);
                                    }
                                }
                            } else if (PF.getNumArgument() == PA.getNumArgument()) {
                                TypeDecl X = PA.getArgument(i2).type();
                                if (!X.isWildcard()) {
                                    constraintEqual(X, T);
                                } else if (X instanceof WildcardExtendsType) {
                                    TypeDecl W = ((WildcardExtendsType) X).getAccess().type();
                                    convertibleFrom(W, T);
                                } else if (X instanceof WildcardSuperType) {
                                    TypeDecl W2 = ((WildcardSuperType) X).getAccess().type();
                                    convertibleTo(W2, T);
                                }
                            }
                        }
                    } else if (T instanceof WildcardExtendsType) {
                        TypeDecl U2 = ((WildcardExtendsType) T).getAccess().type();
                        if (G.instanceOf(H)) {
                            if (H != G) {
                                Iterator iter2 = parameterizedSupertypes(F).iterator();
                                while (iter2.hasNext()) {
                                    TypeDecl V2 = (TypeDecl) iter2.next();
                                    if (!V2.isRawType() && ((ParTypeDecl) V2).genericDecl() == H) {
                                        ArrayList list = new ArrayList();
                                        for (int j = 0; j < ((ParTypeDecl) V2).getNumArgument(); j++) {
                                            list.add(((ParTypeDecl) V2).getArgument(j).type().asWildcardExtends());
                                        }
                                        convertibleFrom(A, ((GenericTypeDecl) H).lookupParTypeDecl(list));
                                    }
                                }
                            } else if (PF.getNumArgument() == PA.getNumArgument()) {
                                TypeDecl X2 = PA.getArgument(i2).type();
                                if (X2 instanceof WildcardExtendsType) {
                                    TypeDecl W3 = ((WildcardExtendsType) X2).getAccess().type();
                                    convertibleFrom(W3, U2);
                                }
                            }
                        }
                    } else if (T instanceof WildcardSuperType) {
                        TypeDecl U3 = ((WildcardSuperType) T).getAccess().type();
                        if (G.instanceOf(H)) {
                            if (H != G) {
                                Iterator iter3 = parameterizedSupertypes(F).iterator();
                                while (iter3.hasNext()) {
                                    TypeDecl V3 = (TypeDecl) iter3.next();
                                    if (!V3.isRawType() && ((ParTypeDecl) V3).genericDecl() == H) {
                                        ArrayList list2 = new ArrayList();
                                        for (int j2 = 0; j2 < ((ParTypeDecl) V3).getNumArgument(); j2++) {
                                            list2.add(((ParTypeDecl) V3).getArgument(j2).type().asWildcardExtends());
                                        }
                                        convertibleFrom(A, ((GenericTypeDecl) H).lookupParTypeDecl(list2));
                                    }
                                }
                            } else if (PF.getNumArgument() == PA.getNumArgument()) {
                                TypeDecl X3 = PA.getArgument(i2).type();
                                if (X3 instanceof WildcardSuperType) {
                                    TypeDecl W4 = ((WildcardSuperType) X3).getAccess().type();
                                    convertibleTo(W4, U3);
                                }
                            }
                        }
                    }
                }
            }
        } else if (F.isRawType()) {
            this.rawAccess = true;
        }
    }

    public void constraintEqual(TypeDecl A, TypeDecl F) {
        if (!F.involvesTypeParameters() || A.isNull()) {
            return;
        }
        if (F instanceof TypeVariable) {
            if (this.typeVariables.contains(F)) {
                addEqualConstraint(F, A);
            }
        } else if (F.isArrayDecl()) {
            TypeDecl U = ((ArrayDecl) F).componentType();
            if (A.isArrayDecl()) {
                TypeDecl V = ((ArrayDecl) A).componentType();
                constraintEqual(V, U);
            } else if (A.isTypeVariable()) {
                TypeVariable t = (TypeVariable) A;
                for (int i = 0; i < t.getNumTypeBound(); i++) {
                    TypeDecl typeBound = t.getTypeBound(i).type();
                    if (typeBound.isArrayDecl() && ((ArrayDecl) typeBound).componentType().isReferenceType()) {
                        TypeDecl V2 = ((ArrayDecl) typeBound).componentType();
                        constraintEqual(V2, U);
                    }
                }
            }
        } else if ((F instanceof ParTypeDecl) && !F.isRawType() && (A instanceof ParTypeDecl)) {
            ParTypeDecl PF = (ParTypeDecl) F;
            ParTypeDecl PA = (ParTypeDecl) A;
            if (PF.genericDecl() == PA.genericDecl()) {
                if (A.isRawType()) {
                    this.rawAccess = true;
                    return;
                }
                for (int i2 = 0; i2 < PF.getNumArgument(); i2++) {
                    TypeDecl T = PF.getArgument(i2).type();
                    if (T.involvesTypeParameters()) {
                        if (!T.isWildcard()) {
                            TypeDecl V3 = PA.getArgument(i2).type();
                            constraintEqual(V3, T);
                        } else if (T instanceof WildcardExtendsType) {
                            TypeDecl U2 = ((WildcardExtendsType) T).getAccess().type();
                            TypeDecl S = PA.getArgument(i2).type();
                            if (S instanceof WildcardExtendsType) {
                                TypeDecl V4 = ((WildcardExtendsType) S).getAccess().type();
                                constraintEqual(V4, U2);
                            }
                        } else if (T instanceof WildcardSuperType) {
                            TypeDecl U3 = ((WildcardSuperType) T).getAccess().type();
                            TypeDecl S2 = PA.getArgument(i2).type();
                            if (S2 instanceof WildcardSuperType) {
                                TypeDecl V5 = ((WildcardSuperType) S2).getAccess().type();
                                constraintEqual(V5, U3);
                            }
                        }
                    }
                }
            }
        }
    }
}
