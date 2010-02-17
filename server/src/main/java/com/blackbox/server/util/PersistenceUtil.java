/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.util;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.IStatusable;
import com.blackbox.foundation.Utils;
import static com.blackbox.foundation.Utils.getCurrentDateTime;

import com.blackbox.foundation.BBPersistentObject;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
final public class PersistenceUtil {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceUtil.class);


    private static boolean testing = false;

    private PersistenceUtil() {
    }

    /**
     * Just to turn off behavior of some things for unit testing, i know this is ugly :(
     */
    public static void setTestOverride(boolean test) {
        testing = test;
    }

    /**
     * determines which dates need to be initialized with now time stamp.  it only sets the created date if
     * {@link com.blackbox.foundation.BBPersistentObject#getVersion()} is null.  if you always want to set both dates
     * then use {@link #setDatesAlways(com.blackbox.foundation.BBPersistentObject)}.
     *
     * @param entity to affect
     */
    public static void setDates(BBPersistentObject entity) {
        DateTime now = getCurrentDateTime();
        if (entity.getVersion() == null) {
            entity.setCreated(now);
        }
        entity.setModified(now);
    }


    /**
     * it iterates through the collection and determines which dates need to be initialized with now time stamp.  it only sets the created date if
     * {@link com.blackbox.foundation.BBPersistentObject#getVersion()}  is null.  if you always want to set both dates
     * then use {@link #setDatesAlways(com.blackbox.foundation.BBPersistentObject)}.
     *
     * @param entities to affect
     */
    public static void setDates(Collection<? extends BBPersistentObject> entities) {
        for (BBPersistentObject entity : entities) {
            setDates(entity);
        }
    }

    /**
     * it iterates through the array and determines which dates need to be initialized with now time stamp.  it only sets the created date if
     * {@link com.blackbox.foundation.BBPersistentObject#getVersion()}  is null.  if you always want to set both dates
     * then use {@link #setDatesAlways(com.blackbox.foundation.BBPersistentObject)}.
     *
     * @param entities to affect
     */
    public static void setDates(BBPersistentObject... entities) {
        for (BBPersistentObject entity : entities) {
            setDates(entity);
        }
    }

    /**
     * always sets created and modified dates on the object to now.
     *
     * @param entity to affect
     */
    public static void setDatesAlways(BBPersistentObject entity) {
        DateTime now = getCurrentDateTime();
        entity.setCreated(now);
        entity.setModified(now);
    }

    /**
     * Updates the status and sets the modified date to now
     *
     * @param entity to save
     * @param status to set
     */
    public static void updateStatus(IStatusable entity, Status status) {
        entity.setStatus(status);
        entity.setModified(getCurrentDateTime());
    }

    /**
     * Updates the status and sets the modified date to now, plus executes the call
     *
     * @param entity   to save
     * @param status   to set
     * @param template
     */
    public static void updateStatus(IStatusable entity, Status status, HibernateOperations template) {
        updateStatus(entity, status);
        template.update(entity);
    }

    public static int insert(BBPersistentObject object, SqlSessionOperations template, String insert) {
        if (object.getGuid() == null) {
            Utils.applyGuid(object);
        }
        setDates(object);
        object.setVersion(0L);
        int i = template.insert(insert, object);
        assert i > 0;
        return i;
    }

    public static int update(BBPersistentObject object, SqlSessionOperations template, String update) {
        setDates(object);
        Long version = object.getVersion();
        object.setVersion(++version);
        return template.update(update, object);
    }

    /**
     * Either performs an insert of save on the specified object.
     *
     * @param object   object to insert or save.
     * @param template The ibatis sqlmap template.
     * @param insert   The name of the insert mapping.
     * @param update   The name of the save mapping.
     * @return true if insert, false if save
     */
    public static Info insertOrUpdate(BBPersistentObject object, SqlSessionOperations template, String insert, String update) {
        if (object.getVersion() == null && insert != null) {
            int rows = insert(object, template, insert);
            return new Info(Info.OperationType.INSERT, rows);
        } else if (update != null) {
            int rows = update(object, template, update);
            return new Info(Info.OperationType.UPDATE, rows);
        }
        return new Info(Info.OperationType.NONE, 0);
    }

    public static class Info implements Serializable {
        public static enum OperationType {
            INSERT,
            UPDATE,
            NONE
        }

        private int rows;
        private OperationType operation;

        public Info(OperationType operation, int rows) {
            this.operation = operation;
            this.rows = rows;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public OperationType getOperation() {
            return operation;
        }

        public void setOperation(OperationType operation) {
            this.operation = operation;
        }
    }

}