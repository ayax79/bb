package com.blackbox.server.user;

import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.user.ExternalCredentials;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("externalCredentialsDao")
public class IbatisExternalCredentialsDao implements IExternalCredentialsDao {

    @Resource
    SqlSessionOperations template;

    @Transactional
    public ExternalCredentials save(ExternalCredentials cred) {
        PersistenceUtil.insertOrUpdate(cred, template, "ExternalCredentials.insert", "ExternalCredentials.update");
        return cred;
    }

    public ExternalCredentials loadByOwnerAndCredType(String owner, ExternalCredentials.CredentialType type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("guid", owner);
        params.put("type", type);
        return (ExternalCredentials) template.selectOne("ExternalCredentials.loadByOwnerAndCredType", params);
    }

    @SuppressWarnings({"unchecked"})
    public List<ExternalCredentials> loadByOwner(String owner) {
        return template.selectList("ExternalCredentials.loadByOwner", owner);
    }


}
