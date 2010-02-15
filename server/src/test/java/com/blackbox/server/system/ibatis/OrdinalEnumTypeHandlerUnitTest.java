package com.blackbox.server.system.ibatis;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.blackbox.Status;
import static junit.framework.Assert.assertEquals;

/**
 * @author A.J. Wright
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OrdinalEnumTypeHandlerUnitTest {

    @Mock
    PreparedStatement ps;

    @Mock
    ResultSet rs;

    @Test               
    public void setNonNullParameterTest() throws SQLException {
        OrdinalEnumTypeHandler handler = new OrdinalEnumTypeHandler(Status.class);
        handler.setNonNullParameter(ps, 1, Status.ENABLED, JdbcType.INTEGER);
        verify(ps).setInt(1, 0);
    }

    @Test
    public void getNullableResult() throws SQLException {
        when(rs.getObject("foo")).thenReturn(0);

        OrdinalEnumTypeHandler handler = new OrdinalEnumTypeHandler(Status.class);
        Status val = (Status) handler.getNullableResult(rs, "foo");
        assertEquals(Status.ENABLED, val);
    }

}
