package com.blackbox.server.system.ibatis;

import com.blackbox.Utils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author A.J. Wright
 */
public class OrdinalEnumTypeHandler extends BaseTypeHandler {

    private Class<Enum<?>> type;

    @SuppressWarnings({"unchecked"})
    protected OrdinalEnumTypeHandler(Class type) {
        Assert.state(type.isEnum(), "That type must be an enumeration");
        this.type = type;
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setInt(i, Utils.enumOridinal((Enum) parameter));
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object o = rs.getObject(columnName);
        if (o != null) {
            Integer i = (Integer) o;
            return Utils.enumOrdinalValueOf(type, i);
        }

        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object o = cs.getObject(columnIndex);
        if (o != null) {
            Integer i = (Integer) o;
            return Utils.enumOrdinalValueOf(type, i);
        }

        return null;
    }

}
