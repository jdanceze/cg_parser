package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParTypeDecl.class */
public interface ParTypeDecl extends Parameterization {
    int getNumArgument();

    Access getArgument(int i);

    String typeName();

    SimpleSet localFields(String str);

    HashMap localMethodsSignatureMap();

    @Override // soot.JastAddJ.Parameterization
    TypeDecl substitute(TypeVariable typeVariable);

    int numTypeParameter();

    TypeVariable typeParameter(int i);

    Access substitute(Parameterization parameterization);

    Access createQualifiedAccess();

    void transformation();

    boolean isParameterizedType();

    @Override // soot.JastAddJ.Parameterization
    boolean isRawType();

    boolean sameArgument(ParTypeDecl parTypeDecl);

    boolean sameSignature(Access access);

    boolean sameSignature(ArrayList arrayList);

    String nameWithArgs();

    TypeDecl genericDecl();
}
