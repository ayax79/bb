if (typeof window.ActivityFeed == 'undefined') window.ActivityFeed = {};
ActivityFeed.Feeder = function(config)
{
    return this.construct(config);
};

ActivityFeed.Feeder.prototype = (function()
{

    // Status flags to control processing.
    var STATUS_READY = 0,
            STATUS_RUNNING = 1,
            STATUS_PAUSED = 2,
            STATUS_STOPPED = 3;

    return {
        defaults:
        {
            main:        null,            
            start_time:  new Date(new Date() - (30 * 60 * 1000)),
            check_delay: 5 * 1000,
            max_entries: 50,
            time_factor: 10
        },

        /**
         * Construct an instance of this object.
         */
        construct: function(config)
        {
            this.config = $.extend(this.defaults, config || {});
            this.last_time = this.config.start_time;
            this.status = STATUS_READY;
        },

        /**
         * Start this feeder running.
         */
        start: function()
        {
            this.status = STATUS_RUNNING;
            this.checkFeed();
        },

        /**
         * Pause this feeder temporarily.
         */
        pause: function()
        {
            this.status = STATUS_PAUSED;
        },

        /**
         * Pause this feeder temporarily.
         */
        resume: function()
        {
            this.status = STATUS_RUNNING;
        },

        /**
         * Stop this feeder permanently.
         */
        stop: function()
        {
            this.status = STATUS_STOPPED;
        },

        checkFeed: function() {
            if (this.status == STATUS_STOPPED) return;

            $('body').attr('class', 'loading');

             $.getJSON(
                this.config.activity_url,
                $.hitch(this, function(data) {
                    this.entries = data;
                    this.entries.sort(function(b,a) {
                        return (b.created < a.created) -
                            (a.created < b.created);
                    });
                    this.last_time = this.entries[0].created;
                    $('body').attr('class', 'playing');
                    this.processNextEntry();
                })
            );

        },


        /**
         * Process the next available entry.
         */
        processNextEntry: function() {
            if (this.status == STATUS_STOPPED) return;

            if (this.status == STATUS_PAUSED) {
                // If we're paused, defer processing of entry for a second.
                return $.delay(this, this.processNextEntry, 1000);
            }

            var entry = this.entries.pop();
            if (!entry) {
                return $.delay(
                    this, this.checkFeed,
                    this.config.check_delay
                );
            }

            var updated = $.parseISO8601(entry.created);
            if (updated < this.last_time) {
                // This entry has probably already been displayed.
                return $.delay(this, this.processNextEntry, 1);
            }

            // Update the time of the latest entry loaded for this session.
            this.last_time = updated;
            $('#last-time').text( ''+updated ); //$.dateToISO8601(updated) );

            // Get the tags and add the entry to the page.
            //var tags = this.extractTagsFromEntry(entry);
            var tags = [];
            this.addEntryToPage(entry, tags);

            // Schedule the next entry to be processed.
            var wait_next_entry =
                parseInt(this.calculateNextEntryDelay(entry) + 1);

            // this.updateStatusDisplay();

            return $.delay(this, this.processNextEntry, wait_next_entry);
        },


        /**
         * Add the given entry to the page layout.
         */
        addEntryToPage: function(entry, tags)
        {

            var entry_updated = $.parseISO8601(entry.created);


            // Clone and populate a new entry.
            var new_item = $('#feed-items .entry:last')
                .clone()
                .find('.subject')
                    .text(entry.subject)
                .end()
                .find('.updated')
                    .text(entry_updated.strftime('%+'))
                    .timeago()
                .end()
                .find('.owner')
                    .text(entry.owner.username || 'n/a')
                .end()
                .prependTo('#feed-items')
                .hide();

            new_item.show('fast');

            // If there are too many entries on the page, remove from the end.
            $('#feed-items .entry:not(.template):gt(' + (this.config.max_entries-1) + ')')
                .remove();
        },
        
        /**
         * Calculate the wait between displaying the given entry and the next
         * in the stack.
         */
        calculateNextEntryDelay: function(entry) {
             // Zero or negative time factor treated as instantaneous.
             if (this.config.time_factor < 1) return 0;

            var wait_next_entry = 0;
            if (this.entries.length) {
                var time_now  = $.parseISO8601(entry.created).getTime();
                var time_next = $.parseISO8601(this.entries[this.entries.length-1].created).getTime();
                wait_next_entry = ( time_next - time_now ) / this.config.time_factor;
            }
            return wait_next_entry;
        }

    };


})();

ActivityFeed.Main = function()
{
};

ActivityFeed.Main.prototype = (function() {

    return {
        /**
         * Initialize the code package on load.
         */
        init: function(activity_url) {

            $(document).ready($.hitch(this, function() {

                // Attempt to set a start time scraped from the last static
                // entry on the page.  Defaults to now, if none found.
                var start_time = new Date();
                $('#feed-items')
                    .find('.entry:not(.template):last .updated .timeago')
                    .each(function() {
                        start_time = $.parseISO8601(this.getAttribute('title'));
                    });

                this.wireUpFeedItems();

                this.feeder = new ActivityFeed.Feeder({
                    main:           this,
                    start_time:     start_time,
                    activity_url:   activity_url
                });
                this.feeder.start();
            }));

            return this;
        },

        /**
         * Set up interactivity with the feed items list.
         */
        wireUpFeedItems: function() {
            var that = this;

            $('#feed-items')
                // Remove all the static items to make way for dynamic fetch.
                .find('li:not(.template)').remove().end()
                // Set up handlers to pause the flow on mouse-over.
                .hover(
                    function() {
                        $(this).addClass('paused');
                        that.feeder.pause();
                        that.paused_class = $('body').attr('class');
                        $('body').attr('class', 'paused');
                    },
                    function() {
                        $(this).removeClass('paused');
                        that.feeder.resume();
                        $('body').attr('class', that.paused_class);
                    }
                );

            // Eschew timeago's built-in per-call interval call and setup a
            // global periodic time refresh.
            $.timeago.settings.refreshMillis = 0;
            (function() {
                $('*[class*=timeago]').timeago();
                setTimeout(arguments.callee, 60 * 1000);
            })();
        }
    };
    
})();
