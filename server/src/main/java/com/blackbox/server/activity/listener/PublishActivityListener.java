package com.blackbox.server.activity.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.event.PublishActivityEvent;
import com.blackbox.Utils;
import com.blackbox.activity.Activity;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.publish.client.IPublishBridge;

import javax.annotation.Resource;
import java.io.File;

/**
 *
 *
 */
@ListenedEvents(PublishActivityEvent.class)
@AsyncListener
public class PublishActivityListener extends BaseBlackboxListener<PublishActivityEvent, Activity> {

    @Resource(name = "mediaPublishBridge")
    private IPublishBridge bridge;

    @Resource(name = "publishTempDir")
    private File publishTempDir;

    public void setBridge(IPublishBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void handle(PublishActivityEvent event, ResultReference<Activity> result) {
        final Activity activity = event.getType();

//        File file = new File(publishTempDir, activity.getGuid());
//        assert file.exists();

//        DefaultFileArtifact<Activity, String> publisherActivity = new DefaultFileArtifact<Activity, String>();
//        publisherActvity.setFile(file);
//        publisherActivity.setArtifactMetaData(activity);
        final String guid = Utils.generateGuid();
        activity.setArtifactIdentifier(guid);
        bridge.publish(activity);
    }

}