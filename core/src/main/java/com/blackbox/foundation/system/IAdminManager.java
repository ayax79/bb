package com.blackbox.foundation.system;

import java.util.List;

public interface IAdminManager {

    String ping(String text);

    String ping();

    void flushCacheWithName(String name);

    void reindex(String simpleName);

    List<CacheName> getCacheNames();
}
