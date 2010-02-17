package com.blackbox.server.user;

import com.blackbox.foundation.user.ViewedBy;
import com.blackbox.server.util.PersistenceUtil;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Repository("viewedByDao")
public class IbatisViewedByDao implements IViewedByDao {

    @Resource
    SqlSessionOperations template;


    @Transactional
    public void insert(ViewedBy viewedBy) {
        PersistenceUtil.insert(viewedBy, template, "ViewedBy.insert");
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<ViewedBy> loadViewersByDestGuid(final String destGuid) {
        return template.selectList("ViewedBy.loadViewersByDestGuid", destGuid);
    }

    @Override
    public int loadViewNumByDestGuid(String destGuid) {
        return (Integer) template.selectOne("ViewedBy.countViewNumByDestGuid", destGuid);
    }


}
