package com.blackbox.server.user;

import com.blackbox.user.Profile;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Repository("profileDao")
public class IbatisProfileDao implements IProfileDao {

    @Resource
    SqlSessionOperations template;

    @Override
    public Profile loadProfileByUserGuid(String guid) {
        return (Profile) template.selectOne("Profile.loadByUserGuid", guid);
    }

}
