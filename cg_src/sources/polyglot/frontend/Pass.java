package polyglot.frontend;

import android.provider.MediaStore;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Pass.class */
public interface Pass {
    public static final ID PARSE = new ID("parse");
    public static final ID BUILD_TYPES = new ID("build-types");
    public static final ID BUILD_TYPES_ALL = new ID("build-types-barrier");
    public static final ID CLEAN_SUPER = new ID("clean-super");
    public static final ID CLEAN_SUPER_ALL = new ID("clean-super-barrier");
    public static final ID CLEAN_SIGS = new ID("clean-sigs");
    public static final ID ADD_MEMBERS = new ID("add-members");
    public static final ID ADD_MEMBERS_ALL = new ID("add-members-barrier");
    public static final ID DISAM = new ID("disam");
    public static final ID DISAM_ALL = new ID("disam-barrier");
    public static final ID TYPE_CHECK = new ID("type-check");
    public static final ID SET_EXPECTED_TYPES = new ID("set-expected-types");
    public static final ID EXC_CHECK = new ID("exc-check");
    public static final ID FOLD = new ID("fold");
    public static final ID INIT_CHECK = new ID("init-check");
    public static final ID CONSTRUCTOR_CHECK = new ID("constructor-check");
    public static final ID FWD_REF_CHECK = new ID("fwd-reference-check");
    public static final ID REACH_CHECK = new ID("reach-check");
    public static final ID EXIT_CHECK = new ID("exit-check");
    public static final ID DUMP = new ID("dump");
    public static final ID PRE_OUTPUT_ALL = new ID("pre-output-barrier");
    public static final ID SERIALIZE = new ID("serialization");
    public static final ID OUTPUT = new ID(MediaStore.EXTRA_OUTPUT);
    public static final ID FIRST_BARRIER = BUILD_TYPES_ALL;

    ID id();

    String name();

    boolean run();

    void resetTimers();

    void toggleTimers(boolean z);

    long inclusiveTime();

    long exclusiveTime();

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Pass$ID.class */
    public static class ID extends Enum {
        public ID(String name) {
            super(name);
        }
    }
}
