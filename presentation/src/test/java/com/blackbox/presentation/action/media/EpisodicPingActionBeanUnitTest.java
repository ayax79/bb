package com.blackbox.presentation.action.media;

import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.yestech.episodic.EpisodicService;
import org.yestech.episodic.objectmodel.Episode;
import org.yestech.episodic.objectmodel.Episodes;
import org.yestech.episodic.objectmodel.Player;
import org.yestech.episodic.objectmodel.Players;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class EpisodicPingActionBeanUnitTest {

    @Mock
    EpisodicService episodicService;

    @Mock
    IMediaManager mediaManager;

    EpisodicPingActionBean actionBean;

    @Before
    public void setup() {
        actionBean = new EpisodicPingActionBean();
        actionBean.mediaManager = mediaManager;
        actionBean.episodicService = episodicService;
    }

    @Test
    public void testHandleDoneStatus() throws Exception {
        String episodeId = "sldkfjd";

        MediaMetaData mmd = MediaMetaData.createMediaMetaData();
        when(mediaManager.loadMediaMetaDataByEpisodeId(episodeId)).thenReturn(mmd);

        Episodes episodes = new Episodes();
        episodes.setTotal(1);
        Episode episode = new Episode();
        episodes.setEpisode(newArrayList(episode));
        Players players = new Players();
        episode.setPlayers(players);
        Player player = new Player();
        episode.getPlayers().setPlayers(newArrayList(player));
        player.setDefaultPlayer(true);
        player.setEmbedCode("sdalkfasdjfkasdf");
        when(episodicService.getEpisodes(new String[] { episodeId } , null, null, null, null, null, null,
                null, null, null, null, null, null)).thenReturn(episodes);

        actionBean.setEpisode_id(episodeId);
        actionBean.setStatus("done");
        assertNull(actionBean.handle());
        assertEquals(player.getEmbedCode(), mmd.getLocation());
    }

    @Test
    public void testOtherStatus() throws JSONException {
        actionBean.setEpisode_id("sldfjkalfds");
        actionBean.setStatus("sldkfs");
        assertNull(actionBean.handle());

        verifyZeroInteractions(mediaManager);
        verifyZeroInteractions(episodicService);
    }


}
