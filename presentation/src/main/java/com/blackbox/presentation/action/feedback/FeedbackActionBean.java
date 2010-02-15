/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.feedback;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@UrlBinding("/action/ajax/feedback")
public class FeedbackActionBean extends BaseBlackBoxActionBean {
    final private static Logger _logger = LoggerFactory.getLogger(FeedbackActionBean.class);

    private String subject;
    private String description;
    private static final String LG_URL = "http://blackbox.lighthouseapp.com/projects/37076-testing/tickets.xml?_token=ce8a6f4be8a37bc7c8116cd81ecfedeee2c0f729";
    
    @DefaultHandler
    public Resolution begin() {        
        return new ForwardResolution("/ajax/feedback/feedback.jsp");
    }
    
    public Resolution feedback() throws Exception {
        _logger.info("calling feedback");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();  
        Element root = (Element) document.createElement("ticket");
        document.appendChild(root);

        Element titleEle = document.createElement("title");
        titleEle.setTextContent(subject + "; by:" + getContext().getUser().getUsername());
        root.appendChild(titleEle);

        Element bodyEle = document.createElement("body");
        bodyEle.setTextContent(description);
        root.appendChild(bodyEle);

        Element stateEle = document.createElement("state");
        stateEle.setTextContent("new");
        root.appendChild(stateEle);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(root);
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();
        _logger.info(xmlString);

        HttpClient httpC = new HttpClient();
        PostMethod post = new PostMethod(LG_URL);

        RequestEntity entity = new StringRequestEntity(xmlString, "application/xml", "ISO-8859-1");
        post.setRequestEntity(entity);
        int rtn = httpC.executeMethod(post);
        _logger.info("rtn is:" + rtn ); // + "; " + post.getResponseBodyAsString());

        // getContext().getMessages().add(new LocalizableMessage("feedback.message"));
        if (rtn == 201) {
            return PresentationUtil.createResolutionWithText(getContext(), "success");
        }
        else {
            return PresentationUtil.createResolutionWithText(getContext(), "fail");
        }
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
}
