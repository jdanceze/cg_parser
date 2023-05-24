package javax.management.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleUnresolvedList.class */
public class RoleUnresolvedList extends ArrayList {
    private static final long serialVersionUID = 4054902803091433324L;

    public RoleUnresolvedList() {
    }

    public RoleUnresolvedList(int i) {
        super(i);
    }

    public RoleUnresolvedList(List list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        int i = 0;
        for (Object obj : list) {
            if (!(obj instanceof RoleUnresolved)) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("An element is not a RoleUnresolved at index ");
                stringBuffer.append(i);
                throw new IllegalArgumentException(stringBuffer.toString());
            }
            i++;
            super.add((RoleUnresolvedList) obj);
        }
    }

    public void add(RoleUnresolved roleUnresolved) throws IllegalArgumentException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.add((RoleUnresolvedList) roleUnresolved);
    }

    public void add(int i, RoleUnresolved roleUnresolved) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.add(i, (int) roleUnresolved);
    }

    public void set(int i, RoleUnresolved roleUnresolved) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.set(i, (int) roleUnresolved);
    }

    public boolean addAll(RoleUnresolvedList roleUnresolvedList) throws IndexOutOfBoundsException {
        if (roleUnresolvedList == null) {
            return true;
        }
        return super.addAll((Collection) roleUnresolvedList);
    }

    public boolean addAll(int i, RoleUnresolvedList roleUnresolvedList) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (roleUnresolvedList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        return super.addAll(i, (Collection) roleUnresolvedList);
    }
}
