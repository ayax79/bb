package com.blackbox.server.user;

import com.blackbox.foundation.user.ViewedBy;
import java.util.List;

/**
 *
 *
 */
public interface IViewedByDao {
    void insert(ViewedBy viewedBy);

    List<ViewedBy> loadViewersByDestGuid(String destGuid);

    int loadViewNumByDestGuid(String destGuid);
}
