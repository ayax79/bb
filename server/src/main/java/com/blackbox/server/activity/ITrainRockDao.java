package com.blackbox.server.activity;

import org.springframework.transaction.annotation.Transactional;
import com.blackbox.foundation.activity.TrainRockEntry;
import com.blackbox.foundation.activity.TrainRockVote;

import java.util.List;

public interface ITrainRockDao {
    @Transactional
    void save(TrainRockEntry entry);

    @Transactional
    TrainRockEntry loadTrainRockEntry(String guid);

    void save(TrainRockVote vote);

    Boolean loadUserVote(String userGuid);

    List<TrainRockEntry> loadTrainRockEntries(int startIndex, int maxResults);
}
