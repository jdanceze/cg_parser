package android.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug.class */
public class ViewDebug {
    @Deprecated
    public static final boolean TRACE_HIERARCHY = false;
    @Deprecated
    public static final boolean TRACE_RECYCLER = false;

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$CapturedViewProperty.class */
    public @interface CapturedViewProperty {
        boolean retrieveReturn() default false;
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$ExportedProperty.class */
    public @interface ExportedProperty {
        boolean resolveId() default false;

        IntToString[] mapping() default {};

        IntToString[] indexMapping() default {};

        FlagToString[] flagMapping() default {};

        boolean deepExport() default false;

        String prefix() default "";

        String category() default "";
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$FlagToString.class */
    public @interface FlagToString {
        int mask();

        int equals();

        String name();

        boolean outputIf() default true;
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$HierarchyTraceType.class */
    public enum HierarchyTraceType {
        BUILD_CACHE,
        DRAW,
        INVALIDATE,
        INVALIDATE_CHILD,
        INVALIDATE_CHILD_IN_PARENT,
        ON_LAYOUT,
        ON_MEASURE,
        REQUEST_LAYOUT
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$IntToString.class */
    public @interface IntToString {
        int from();

        String to();
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewDebug$RecyclerTraceType.class */
    public enum RecyclerTraceType {
        BIND_VIEW,
        MOVE_FROM_ACTIVE_TO_SCRAP_HEAP,
        MOVE_TO_SCRAP_HEAP,
        NEW_VIEW,
        RECYCLE_FROM_ACTIVE_HEAP,
        RECYCLE_FROM_SCRAP_HEAP
    }

    public ViewDebug() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void trace(View view, RecyclerTraceType type, int... parameters) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void startRecyclerTracing(String prefix, View view) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void stopRecyclerTracing() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void trace(View view, HierarchyTraceType type) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void startHierarchyTracing(String prefix, View view) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void stopHierarchyTracing() {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCapturedView(String tag, Object view) {
        throw new RuntimeException("Stub!");
    }
}
