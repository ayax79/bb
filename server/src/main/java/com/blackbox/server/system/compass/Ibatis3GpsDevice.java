package com.blackbox.server.system.compass;

import org.apache.ibatis.exceptions.IbatisException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.*;
import org.compass.core.CompassException;
import org.compass.core.CompassSession;
import org.compass.core.mapping.ResourceMapping;
import org.compass.gps.CompassGpsException;
import org.compass.gps.device.ibatis.IndexStatement;
import org.compass.gps.device.ibatis.SqlMapGpsDeviceException;
import org.compass.gps.device.ibatis.SqlMapIndexEntity;
import org.compass.gps.device.support.parallel.AbstractParallelGpsDevice;
import org.compass.gps.device.support.parallel.IndexEntitiesIndexer;
import org.compass.gps.device.support.parallel.IndexEntity;
import org.compass.gps.spi.CompassGpsInterfaceDevice;

/**
 * A <code>SqlMapClient</code> device, provides support for iBatis 2 and the
 * <code>index</code> operation. The device holds a list of iBatis select
 * statements ids, executes them, and index the result.
 *
 * <p>The device must be initialized with a <code>SqlMapClient</code> instance.
 * When indexing the data, a <code>SqlMapSession</code> will be opened, and a
 * transaction will be started. The device will then execute the select
 * statement id, and use the iBatis <code>PaginatedList</code> to index the
 * data.
 *
 * <p>The page size for the <code>PaginatedList</code> can be controlled using
 * the <code>pageSize</code> property.
 *
 * <p>The select statment can have a parameter object associated with it. If one of
 * the select statements requires a parameter object, then the
 * <code>statementsParameterObjects</code> property must be set. It must have
 * the same size as the <code>selectStatementsIds</code>, and the matching
 * index of the <code>selectStatementsIds</code> should be set at the
 * <code>statementsParameterObjects</code> property.
 *
 * <p>As a replacement, the {@link #setIndexStatements(org.compass.gps.device.ibatis.IndexStatement[])} can be used
 * which combines both a select statement id and its optional parameter.
 *
 * @author kimchy
 */
public class Ibatis3GpsDevice extends AbstractParallelGpsDevice {

    private SqlSessionFactory sqlSessionFactory;

    private String[] selectStatementsIds;

    private Object[] statementsParameterObjects;

    private int pageSize = 200;

    public Ibatis3GpsDevice() {

    }

    public Ibatis3GpsDevice(String deviceName, SqlSessionFactory sqlSessionFactory, IndexStatement... statements) {
        setName(deviceName);
        this.sqlSessionFactory = sqlSessionFactory;
        setIndexStatements(statements);
    }

    public Ibatis3GpsDevice(String deviceName, SqlSessionFactory sqlSessionFactory, String... selectStatementsIds) {
        this(deviceName, sqlSessionFactory, selectStatementsIds, null);
    }

    public Ibatis3GpsDevice(String deviceName, SqlSessionFactory sqlSessionFactory, String[] selectStatementsIds,
                                 Object[] statementsParameterObjects) {
        setName(deviceName);
        this.sqlSessionFactory = sqlSessionFactory;
        this.selectStatementsIds = selectStatementsIds;
        this.statementsParameterObjects = statementsParameterObjects;
    }

    protected void doStart() throws CompassGpsException {
        if (sqlSessionFactory == null) {
            throw new IllegalArgumentException(buildMessage("Must set sqlMapClaient property"));
        }
        if (selectStatementsIds == null) {
            throw new IllegalArgumentException(buildMessage("Must set selectStatementsIds property"));
        }
        if (selectStatementsIds.length == 0) {
            throw new IllegalArgumentException(
                    buildMessage("selectStatementsIds property must have at least one entry"));
        }
        if (statementsParameterObjects != null && statementsParameterObjects.length != selectStatementsIds.length) {
            throw new IllegalArgumentException(
                    buildMessage("Once the statementsParameterObjects property is set, it must have the same length as the selectStatementsIds property"));
        }
    }

    protected IndexEntity[] doGetIndexEntities() throws CompassGpsException {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        IndexEntity[] entities = new IndexEntity[selectStatementsIds.length];
        for (int i = 0; i < selectStatementsIds.length; i++) {
            String statementId = selectStatementsIds[i];
            MappedStatement statement = configuration.getMappedStatement(statementId);
            if (statement == null) {
                throw new IllegalArgumentException("Failed to find statement for [" + statementId + "]");
            }
            Class resultClass = statement.getResultMaps().get(0).getType();
            ResourceMapping resourceMapping = ((CompassGpsInterfaceDevice) getGps()).getMappingForEntityForIndex(resultClass);
            if (resourceMapping == null) {
                throw new IllegalArgumentException("Failed to find mapping for class [" + resultClass.getName() + "]");
            }
            Object parameterObject = null;
            if (statementsParameterObjects != null) {
                parameterObject = statementsParameterObjects[i];
            }
            entities[i] = new SqlMapIndexEntity(resultClass.getName(), resourceMapping.getSubIndexHash().getSubIndexes(),
                    statementId, parameterObject);
        }
        return entities;
    }

    protected IndexEntitiesIndexer doGetIndexEntitiesIndexer() {
        return new SqlMapIndexer();
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Sets the given index statements that will be used. An index statement is a combination of the
     * statement id and a possible parameter.
     *
     * <p>Note, this method is used to replace the combination of {@link #setSelectStatementsIds(String[])} and
     * {@link #setStatementsParameterObjects(Object[])}.
     */
    public void setIndexStatements(IndexStatement... statements) {
        selectStatementsIds = new String[statements.length];
        statementsParameterObjects = new Object[statements.length];
        for (int i = 0; i < statements.length; i++) {
            selectStatementsIds[i] = statements[i].getStatementId();
            statementsParameterObjects[i] = statements[i].getParam();
        }
    }

    /**
     * Sets the select statement ids that will be used to fetch data to be indexed. If parameters are required
     * for some of the statements, they can be passed using {@link #setStatementsParameterObjects(Object[])} with
     * the order similar to the statement ids.
     *
     * <p>Note, this method can be replaced with {@link #setIndexStatements(IndexStatement[])}.
     */
    public void setSelectStatementsIds(String... statementsNames) {
        this.selectStatementsIds = statementsNames;
    }

    /**
     * Sets the select statement parameters for each select statment. The order is important and must match the
     * {@link #setSelectStatementsIds(String[])} order.
     *
     * <p>Note, the {@link #setIndexStatements(IndexStatement[])} can replce the combination of
     * {@link #setSelectStatementsIds(String[])} and {@link #setStatementsParameterObjects(Object[])}.
     */
    public void setStatementsParameterObjects(Object[] statementsParameterObjects) {
        this.statementsParameterObjects = statementsParameterObjects;
    }

    /**
     * Sets the pagination/fetch size when iterating through the result set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private class SqlMapIndexer implements IndexEntitiesIndexer {

        public void performIndex(CompassSession session, IndexEntity[] entities) throws CompassException {
            for (IndexEntity entity : entities) {
                SqlMapIndexEntity indexEntity = (SqlMapIndexEntity) entity;

                SqlSession sqlMapSession = getSqlSessionFactory().openSession();
                try {
                    if (log.isDebugEnabled()) {
                        log.debug(buildMessage("Indexing select statement id [" + indexEntity.getStatementId() + "]"));
                    }
                    sqlMapSession.select(indexEntity.getStatementId(), indexEntity.getParam(),
                            new SqlMapClientGpsDeviceRowHandler(indexEntity, session, pageSize));
                    session.evictAll();
                    sqlMapSession.commit();
                } catch (IbatisException e) {
                    throw new SqlMapGpsDeviceException("Failed to fetch paginated list for statement [" + indexEntity.getStatementId() + "]", e);
                } finally {
                    try {
                        try {
                            sqlMapSession.rollback();
                        } catch (Exception e) {
                            log.warn(buildMessage("Failed to close sqlMap session, ignoring"), e);
                        }
                    } finally {
                        sqlMapSession.close();
                    }
                }
            }
        }
    }

    public class SqlMapClientGpsDeviceRowHandler implements ResultHandler {

        private SqlMapIndexEntity indexEntity;

        private CompassSession session;

        private int pageSize;

        private int pageCount = 0;

        private int currentItem = 1;

        public SqlMapClientGpsDeviceRowHandler(SqlMapIndexEntity indexEntity, CompassSession session, int pageSize) {
            this.session = session;
            this.pageSize = pageSize;
            this.indexEntity = indexEntity;
        }


        @Override
        public void handleResult(ResultContext context) {
            Object o = context.getResultObject();
            session.create(o);
            if (currentItem == pageSize) {
                if (log.isTraceEnabled()) {
                    log.trace(buildMessage("Indexing [" + indexEntity.getName() + "] page number [" + pageCount + "]"));
                }
                session.evictAll();
                pageCount++;
                currentItem = 0;
            }
            currentItem++;
        }
    }
}
