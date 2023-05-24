package fj.control.db;

import java.sql.Connection;
import java.sql.SQLException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/db/Connector.class */
public abstract class Connector {
    public abstract Connection connect() throws SQLException;
}
