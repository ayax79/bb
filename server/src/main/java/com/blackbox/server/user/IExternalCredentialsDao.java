package com.blackbox.server.user;

import com.blackbox.foundation.user.ExternalCredentials;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface IExternalCredentialsDao {
    @Transactional
    ExternalCredentials save(ExternalCredentials cred);

    ExternalCredentials loadByOwnerAndCredType(String owner, ExternalCredentials.CredentialType type);

    @SuppressWarnings({"unchecked"})
    List<ExternalCredentials> loadByOwner(String owner);
}
