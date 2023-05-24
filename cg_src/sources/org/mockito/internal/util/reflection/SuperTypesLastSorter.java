package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/SuperTypesLastSorter.class */
public class SuperTypesLastSorter {
    private static final Comparator<Field> compareFieldsByName = new Comparator<Field>() { // from class: org.mockito.internal.util.reflection.SuperTypesLastSorter.1
        @Override // java.util.Comparator
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private SuperTypesLastSorter() {
    }

    public static List<Field> sortSuperTypesLast(Collection<? extends Field> unsortedFields) {
        List<Field> fields = new ArrayList<>(unsortedFields);
        Collections.sort(fields, compareFieldsByName);
        int i = 0;
        while (i < fields.size() - 1) {
            Field f = fields.get(i);
            Class<?> ft = f.getType();
            int newPos = i;
            for (int j = i + 1; j < fields.size(); j++) {
                Class<?> t = fields.get(j).getType();
                if (ft != t && ft.isAssignableFrom(t)) {
                    newPos = j;
                }
            }
            if (newPos == i) {
                i++;
            } else {
                fields.remove(i);
                fields.add(newPos, f);
            }
        }
        return fields;
    }
}
