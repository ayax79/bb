package com.blackbox.presentation.action.search;

import com.blackbox.occasion.IOccasionManager;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.user.IUserManager;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

/**
 * Creates an {@link ISearcher}
 */
public class SearcherFactory {
    final private static SearcherFactory INSTANCE = new SearcherFactory();
    private IUserManager userManager;
    private IOccasionManager occasionManager;


    public static ISearcher create(BlackBoxContext context) {
        String eventName = context.getEventName();

        ISearcher searcher = new NoOpSearcher();
        if (equalsIgnoreCase("user", eventName) || equalsIgnoreCase("people", eventName)) {
            UserSearcher typedsearcher = new UserSearcher();
            typedsearcher.setManager(INSTANCE.userManager);
            searcher = typedsearcher;
        } else if (equalsIgnoreCase("event", eventName) || equalsIgnoreCase("events", eventName)) {
            OccasionSearcher typedSearcher = new OccasionSearcher();
            typedSearcher.setManager(INSTANCE.occasionManager);
            searcher = typedSearcher;
        }
        return searcher;
    }

    public static void init(IUserManager userManager, IOccasionManager occasionManager) {
        INSTANCE.userManager = userManager;
        INSTANCE.occasionManager = occasionManager;
    }
}
