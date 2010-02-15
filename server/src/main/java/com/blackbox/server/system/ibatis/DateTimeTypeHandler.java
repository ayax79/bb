package com.blackbox.server.system.ibatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;

import java.sql.*;

// hmm. right now there's no merely dates in db, they all are time stamp so this will work....
public class DateTimeTypeHandler implements TypeHandler {

    @Override
    public void setParameter(PreparedStatement setter, int i, Object obj, JdbcType jdbcType) throws SQLException {
        if (obj == null) {
            setter.setNull(i, jdbcType != null ? jdbcType.TYPE_CODE : Types.DATE);
        } else if (obj instanceof DateTime) {

            setter.setTimestamp(i, new Timestamp(((DateTime) obj).getMillis()));
        } else {
            throw new IllegalArgumentException("Illegal Date object");
        }
    }

    @Override
    public Object getResult(ResultSet rs, String s) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(s);
        if (timestamp != null) {
            return new DateTime(timestamp);
        }
        return null;
    }

    @Override
    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        Timestamp timestamp = callableStatement.getTimestamp(i);
        if (timestamp != null) {
            return new DateTime(timestamp);
        }
        return null;
    }

}