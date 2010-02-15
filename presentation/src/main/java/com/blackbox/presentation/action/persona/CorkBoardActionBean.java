/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.media.CorkboardImage;
import com.blackbox.media.CorkboardPublish;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.user.User;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.blackbox.presentation.action.util.JSONUtil.*;
import static com.blackbox.presentation.action.util.PresentationUtil.*;

@UrlBinding("/corkboard/{$event}/{guid}")
public class CorkBoardActionBean extends BaseBlackBoxActionBean {

    private static final Logger logger = LoggerFactory.getLogger(CorkBoardActionBean.class);

    @SpringBean("mediaManager") IMediaManager mediaManager;
    @SpringBean("corkboardCache") ICacheManager<String, String> corkboardCache;

    private FileBean fileData;
    private JSONArray json;
    private String guid;

    @Before(on = {"save", "upload"})
    public Resolution securityCheck() throws JSONException {
        if (!getCurrentUser().getGuid().equals(guid)) {
            JSONObject error = new JSONObject();
            error.put("errorMessage", "Only the owner can edit their corkboard");
            createResolutionWithJson(getContext(), error);
        }
        return null;
    }

    @DefaultHandler
    public Resolution load() throws JSONException {

        String json = corkboardCache.get(guid);

        if (json == null) {
            List<CorkboardImage> images = mediaManager.loadCorkboard(guid);

            if (images != null && !images.isEmpty()) {
                JSONArray array = new JSONArray();
                for (CorkboardImage image : images) {
                    array.put(toJSON(image));
                }
                json = array.toString();
            }
            else {
                json =  "[]";
            }
            corkboardCache.put(guid, json);
        }
        return createResolutionWithText(getContext(), json);
    }

     public Resolution save() throws JSONException {
        guid = getCurrentUser().getGuid();
         
        List<CorkboardImage> images = mediaManager.loadCorkboard(guid);
        Map<String, CorkboardImage> imageMap = Maps.uniqueIndex(images, new Function<CorkboardImage, String>() {
            @Override
            public String apply(CorkboardImage from) {
                return from.getGuid();
            }
        });

        ArrayList<CorkboardImage> updateList = new ArrayList<CorkboardImage>(images.size());
        for (int i = 0, size = json.length(); i < size; i++) {
            JSONObject jo = json.getJSONObject(i);
            CorkboardImage image = imageMap.get(jo.getString("guid"));
            if (image != null) {
                updateValues(image, jo);
                updateList.add(image);
            }
        }

        mediaManager.bulkUpdate(updateList);                          
        corkboardCache.flush(guid);
        return createResolutionWithText(getContext(), "success=true");
    }

    public Resolution upload() throws IOException, JSONException {
        User owner = getCurrentUser();

        MediaMetaData mmd = PresentationUtil.buildMediaMetaData(fileData, owner, null);
        mmd.setRecipientDepth(NetworkTypeEnum.SELF);
        CorkboardImage ci = CorkboardImage.createCorkboadImage(owner, mmd);
        CorkboardPublish publish = new CorkboardPublish();
        publish.setCorkboardImage(ci);
        ci = mediaManager.publishCorkboardImage(new CorkboardPublish(ci, mmd, buildByteArray(fileData)));

        JSONObject jo = new JSONObject();
        jo.put("location", ci.getLocation());
        jo.put("guid", ci.getGuid());
        jo.put("fileName", ci.getFileName());

        JSONArray array = new JSONArray();
        array.put(jo);
        corkboardCache.flush(owner.getGuid());
        return createResolutionWithJsonArray(getContext(), array);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public JSONArray getJson() {
        return json;
    }

    public void setJson(JSONArray json) {
        this.json = json;
    }

    public FileBean getFileData() {
        return fileData;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public void updateValues(CorkboardImage ci, JSONObject jo) throws JSONException {

        ci.setX(jsonInt(jo, "x"));
        ci.setY(jsonInt(jo, "y"));
        ci.setZ(jsonInt(jo, "z"));
        ci.setRotation(jsonInt(jo, "rotation"));
        ci.setScale(jsonDouble(jo, "scale"));
    }

    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }
}
