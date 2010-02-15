package com.blackbox.server.occasion.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.occasion.event.LoadOccasionForSceneEvent;
import com.blackbox.server.occasion.IOccasionDao;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionRequest;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@ListenedEvents({LoadOccasionForSceneEvent.class})
public class LoadOccasionsForSceneListener extends BaseBlackboxListener<LoadOccasionForSceneEvent, List<Occasion>> {

    @Resource(name = "occasionDao")
    private IOccasionDao occasionDao;

    public IOccasionDao getOccasionDao() {
        return occasionDao;
    }

    public void setOccasionDao(IOccasionDao occasionDao) {
        this.occasionDao = occasionDao;
    }

    /**
     * Called by the {@link org.yestech.event.multicaster.IEventMulticaster} when the Event is fired.
     *
     * @param loadOccasionForSceneEvent  Event registered
     * @param result The result to return
     */
    @Override
    public void handle(LoadOccasionForSceneEvent loadOccasionForSceneEvent, ResultReference<List<Occasion>> result) {
        OccasionRequest request = loadOccasionForSceneEvent.getType();
        List<Occasion> occasions = occasionDao.loadOccasionsForScene(request);
        result.setResult(occasions);
    }
}