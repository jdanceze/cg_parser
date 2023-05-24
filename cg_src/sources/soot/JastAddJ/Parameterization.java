package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Parameterization.class */
public interface Parameterization {
    boolean isRawType();

    TypeDecl substitute(TypeVariable typeVariable);
}
