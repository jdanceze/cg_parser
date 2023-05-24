package soot.JastAddJ;

import java.util.ArrayList;
import soot.JastAddJ.Signatures;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/GenericTypeDecl.class */
public interface GenericTypeDecl {
    TypeDecl original();

    int getNumTypeParameter();

    TypeVariable getTypeParameter(int i);

    List getTypeParameterList();

    String fullName();

    String typeName();

    TypeDecl makeGeneric(Signatures.ClassSignature classSignature);

    SimpleSet addTypeVariables(SimpleSet simpleSet, String str);

    List createArgumentList(ArrayList arrayList);

    boolean isGenericType();

    TypeDecl rawType();

    TypeDecl lookupParTypeDecl(ParTypeAccess parTypeAccess);

    TypeDecl lookupParTypeDecl(ArrayList arrayList);
}
