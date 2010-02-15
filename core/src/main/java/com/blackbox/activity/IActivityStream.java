package com.blackbox.activity;

import com.blackbox.util.AnyTypeAdapter;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 *
 *
 */
@XmlSeeAlso({ActivityStream.class})
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface IActivityStream {
    List<ActivityThread> getThreads();

    void setThreads(List<ActivityThread> threads);

    int getTotalThreads();

    void addThread(ActivityThread thread);
}
