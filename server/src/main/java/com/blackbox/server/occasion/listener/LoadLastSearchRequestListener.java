package com.blackbox.server.occasion.listener;

import com.blackbox.search.ExploreRequest;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.occasion.event.LoadLastExplorerEvent;
import org.yestech.cache.ICacheManager;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents({ LoadLastExplorerEvent.class})
public class LoadLastSearchRequestListener extends BaseBlackboxListener<LoadLastExplorerEvent, ExploreRequest> {

    @Resource(name = "eventLastExploreRequestCache")
    ICacheManager<String, ExploreRequest> eventLastExploreRequestCache;

    public ICacheManager<String, ExploreRequest> getEventLastExploreRequestCache() {
        return eventLastExploreRequestCache;
    }

    public void setEventLastExploreRequestCache(ICacheManager<String, ExploreRequest> eventLastExploreRequestCache) {
        this.eventLastExploreRequestCache = eventLastExploreRequestCache;
    }

    /**
     * Called by the {@link org.yestech.event.multicaster.IEventMulticaster} when the Event is fired.
     *
     * @param loadLastExplorerEvent  Event registered
     * @param result The result to return
     */
    @Override
    public void handle(LoadLastExplorerEvent loadLastExplorerEvent, ResultReference<ExploreRequest> result) {
        String guid = loadLastExplorerEvent.getType();
        result.setResult(eventLastExploreRequestCache.get(guid));
    }
}
