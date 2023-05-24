package javax.management.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleList.class */
public class RoleList extends ArrayList {
    private static final long serialVersionUID = 5568344346499649313L;

    public RoleList() {
    }

    public RoleList(int i) {
        super(i);
    }

    public RoleList(List list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        int i = 0;
        for (Object obj : list) {
            if (!(obj instanceof Role)) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("An element is not a Role at index ");
                stringBuffer.append(i);
                throw new IllegalArgumentException(stringBuffer.toString());
            }
            i++;
            super.add((RoleList) obj);
        }
    }

    public void add(Role role) throws IllegalArgumentException {
        if (role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.add((RoleList) role);
    }

    public void add(int i, Role role) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.add(i, (int) role);
    }

    public void set(int i, Role role) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        super.set(i, (int) role);
    }

    public boolean addAll(RoleList roleList) throws IndexOutOfBoundsException {
        if (roleList == null) {
            return true;
        }
        return super.addAll((Collection) roleList);
    }

    public boolean addAll(int i, RoleList roleList) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (roleList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        return super.addAll(i, (Collection) roleList);
    }
}
