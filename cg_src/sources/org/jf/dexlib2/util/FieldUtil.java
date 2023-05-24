package org.jf.dexlib2.util;

import com.google.common.base.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.iface.Field;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/FieldUtil.class */
public final class FieldUtil {
    public static Predicate<Field> FIELD_IS_STATIC = new Predicate<Field>() { // from class: org.jf.dexlib2.util.FieldUtil.1
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Field input) {
            return input != null && FieldUtil.isStatic(input);
        }
    };
    public static Predicate<Field> FIELD_IS_INSTANCE = new Predicate<Field>() { // from class: org.jf.dexlib2.util.FieldUtil.2
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Field input) {
            return (input == null || FieldUtil.isStatic(input)) ? false : true;
        }
    };

    public static boolean isStatic(@Nonnull Field field) {
        return AccessFlags.STATIC.isSet(field.getAccessFlags());
    }

    private FieldUtil() {
    }
}
