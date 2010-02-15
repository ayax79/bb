package com.blackbox.presentation.action.media;

import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.episodic.EpisodicService;
import org.yestech.episodic.objectmodel.Episode;
import org.yestech.episodic.objectmodel.Episodes;

import java.util.List;

/**
 * @author A.J. Wright
 */
@SuppressWarnings({"UnusedDeclaration"})
public class EpisodicPingActionBean implements ActionBean {

    private static final Logger logger = LoggerFactory.getLogger(EpisodicPingActionBean.class);

    @SpringBean("mediaManager")
    IMediaManager mediaManager;

    @SpringBean("episodicService")
    EpisodicService episodicService;

    private String episode_id;
    private String status;
    private ActionBeanContext context;


    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution handle() throws JSONException {
        if ("done".equals(status)) {

            MediaMetaData data = mediaManager.loadMediaMetaDataByEpisodeId(episode_id);
            if (data != null) {
                Episodes episodes = episodicService.getEpisodes(new String[]{episode_id}, null, null, null, null, null,
                        null, null, null, null, null, null, null);
                assert episodes != null;
                assert episodes.getTotal() == 1;
                List<Episode> list = episodes.getEpisode();
                Episode episode = list.get(0);
                String embedCode = episode.getDefaultPlayer().getEmbedCode();
                assert embedCode != null;
                data.setLocation(embedCode);
                mediaManager.save(data);
            }
        }
        else {
            logger.warn(String.format("Episodic returns status %s for episode %s", episode_id, status));
        }

        return null;
    }

}
