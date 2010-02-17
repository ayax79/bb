/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.foundation.activity;

import static com.google.common.collect.Lists.newArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author boo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ActivityStream implements IActivityStream, Serializable {

    private static final long serialVersionUID = 4411121687729749238L;
    private List<ActivityThread> threads = newArrayList();
    private int totalThreads;

    public ActivityStream() {
    }

    public int getTotalThreads() {
        return totalThreads;
    }

    @Override
    public List<ActivityThread> getThreads() {
        return threads;
    }

    public void setThreads(List<ActivityThread> threads) {
        if (threads != null) {
            this.threads = threads;
            totalThreads = threads.size();
        }
    }

    public void addThread(ActivityThread thread) {
        threads.add(thread);
        ++totalThreads;
    }
}
