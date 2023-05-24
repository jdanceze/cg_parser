package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/GLBTypeFactory.class */
public class GLBTypeFactory {
    public static TypeDecl glb(ArrayList typeList) {
        TypeDecl retType = ((TypeDecl) typeList.get(0)).unknownType();
        TypeDecl cls = mostSpecificSuperClass(typeList);
        if (cls != null) {
            ArrayList intersectInterfaceList = new ArrayList();
            ArrayList allInterfaceList = new ArrayList();
            Iterator itera = typeList.iterator();
            while (itera.hasNext()) {
                TypeDecl typeDecl = (TypeDecl) itera.next();
                addInterfaces(intersectInterfaceList, allInterfaceList, typeDecl);
            }
            greatestLowerBounds(intersectInterfaceList);
            if (checkInterfaceCompatibility(allInterfaceList) && checkClassInterfaceCompatibility(cls, intersectInterfaceList)) {
                greatestLowerBounds(typeList);
                retType = typeList.size() == 1 ? (TypeDecl) typeList.iterator().next() : retType.lookupGLBType(typeList);
            }
        }
        return retType;
    }

    private static void addInterfaces(ArrayList intersectInterfaceList, ArrayList allInterfaceList, TypeDecl typeDecl) {
        if (typeDecl.isInterfaceDecl()) {
            intersectInterfaceList.add((InterfaceDecl) typeDecl);
            allInterfaceList.add((InterfaceDecl) typeDecl);
        } else if (typeDecl instanceof TypeVariable) {
            TypeVariable varTD = (TypeVariable) typeDecl;
            intersectInterfaceList.add(varTD.toInterface());
            allInterfaceList.addAll(varTD.implementedInterfaces());
        } else if (typeDecl instanceof LUBType) {
            allInterfaceList.addAll(typeDecl.implementedInterfaces());
        } else if (typeDecl instanceof GLBType) {
            allInterfaceList.addAll(typeDecl.implementedInterfaces());
        }
    }

    public static final TypeDecl mostSpecificSuperClass(ArrayList types) {
        ArrayList csList = new ArrayList();
        Iterator iter = types.iterator();
        while (iter.hasNext()) {
            csList.add(mostSpecificSuperClass((TypeDecl) iter.next()));
        }
        greatestLowerBounds(csList);
        if (csList.size() == 1) {
            return (TypeDecl) csList.get(0);
        }
        return null;
    }

    private static final TypeDecl mostSpecificSuperClass(TypeDecl t) {
        HashSet superTypes = new HashSet();
        addSuperClasses(t, superTypes);
        if (superTypes.isEmpty()) {
            return t.typeObject();
        }
        ArrayList result = new ArrayList(superTypes.size());
        result.addAll(superTypes);
        greatestLowerBounds(result);
        if (result.size() == 1) {
            return (TypeDecl) result.get(0);
        }
        return t.typeObject();
    }

    private static final void addSuperClasses(TypeDecl t, HashSet result) {
        if (t == null) {
            return;
        }
        if (t.isClassDecl() && !result.contains(t)) {
            result.add((ClassDecl) t);
        } else if (t.isTypeVariable()) {
            TypeVariable var = (TypeVariable) t;
            for (int i = 0; i < var.getNumTypeBound(); i++) {
                addSuperClasses(var.getTypeBound(i).type(), result);
            }
        } else if ((t instanceof LUBType) || (t instanceof GLBType)) {
            result.add(t);
        } else if (t.isInterfaceDecl()) {
            result.add((ClassDecl) t.typeObject());
        }
    }

    private static boolean checkInterfaceCompatibility(ArrayList ifaceList) {
        ParInterfaceDecl pj;
        for (int i = 0; i < ifaceList.size(); i++) {
            HashSet superISet_i = Constraints.parameterizedSupertypes((TypeDecl) ifaceList.get(i));
            Iterator iter1 = superISet_i.iterator();
            while (iter1.hasNext()) {
                InterfaceDecl superIface_i = (InterfaceDecl) iter1.next();
                if (superIface_i instanceof ParInterfaceDecl) {
                    ParInterfaceDecl pi = (ParInterfaceDecl) superIface_i;
                    for (int j = i + 1; j < ifaceList.size(); j++) {
                        HashSet superISet_j = Constraints.parameterizedSupertypes((TypeDecl) ifaceList.get(j));
                        Iterator iter2 = superISet_j.iterator();
                        while (iter2.hasNext()) {
                            InterfaceDecl superIface_j = (InterfaceDecl) iter2.next();
                            if ((superIface_j instanceof ParInterfaceDecl) && pi != (pj = (ParInterfaceDecl) superIface_j) && pi.genericDecl() == pj.genericDecl() && !pi.sameArgument(pj)) {
                                return false;
                            }
                        }
                    }
                    continue;
                }
            }
        }
        return true;
    }

    private static boolean checkClassInterfaceCompatibility(TypeDecl cls, ArrayList ifaceList) {
        ParInterfaceDecl parIface;
        HashSet implementedInterfaces = cls.implementedInterfaces();
        Iterator iter1 = implementedInterfaces.iterator();
        while (iter1.hasNext()) {
            InterfaceDecl impInterface = (InterfaceDecl) iter1.next();
            if (impInterface instanceof ParInterfaceDecl) {
                ParInterfaceDecl impParIface = (ParInterfaceDecl) impInterface;
                Iterator iter2 = ifaceList.iterator();
                while (iter2.hasNext()) {
                    InterfaceDecl iface = (InterfaceDecl) iter2.next();
                    if ((iface instanceof ParInterfaceDecl) && (parIface = (ParInterfaceDecl) iface) != impParIface && parIface.genericDecl() == impParIface.genericDecl() && !parIface.sameArgument(impParIface)) {
                        return false;
                    }
                }
                continue;
            }
        }
        return true;
    }

    public static final void greatestLowerBounds(ArrayList types) {
        for (int i = 0; i < types.size(); i++) {
            TypeDecl U = (TypeDecl) types.get(i);
            for (int j = i + 1; j < types.size(); j++) {
                TypeDecl V = (TypeDecl) types.get(j);
                if (U != null && V != null) {
                    if (U.instanceOf(V)) {
                        types.set(j, null);
                    } else if (V.instanceOf(U)) {
                        types.set(i, null);
                    }
                }
            }
        }
        removeNullValues(types);
    }

    public static final void removeNullValues(ArrayList types) {
        ArrayList filter = new ArrayList(1);
        filter.add(null);
        types.removeAll(filter);
    }
}
