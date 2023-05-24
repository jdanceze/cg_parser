package soot.jimple.spark.geom.geomPA;

import soot.RefType;
import soot.Scene;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/Constants.class */
public class Constants {
    public static final String geomE = "Geom";
    public static final String heapinsE = "HeapIns";
    public static final String ptinsE = "PtIns";
    public static final int eval_nothing = 0;
    public static final int eval_basicInfo = 1;
    public static final int eval_simpleClients = 2;
    public static final int NEW_CONS = 0;
    public static final int ASSIGN_CONS = 1;
    public static final int LOAD_CONS = 2;
    public static final int STORE_CONS = 3;
    public static final int FIELD_ADDRESS = 4;
    public static final int SUPER_MAIN = 0;
    public static final int UNKNOWN_FUNCTION = -1;
    public static final long MAX_CONTEXTS = 9223372036854775806L;
    public static final RefType exeception_type = Scene.v().getBaseExceptionType();
    public static final int seedPts_allUser = 15;
    public static final int seedPts_all = Integer.MAX_VALUE;
}
