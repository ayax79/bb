package com.blackbox.presentation.action.userplane;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.JspFunctions;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.SexEnum;
import com.blackbox.foundation.userplane.*;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings({"UnusedDeclaration"})
@UrlBinding("/chat/{$event}")

public class UserPlaneActionBean extends BaseBlackBoxActionBean implements ActionBean {

	private ActionBeanContext context;
    private static final Logger logger = LoggerFactory.getLogger(UserPlaneActionBean.class);
    private static JAXBContext jaxbContext;

	static {
        try {
            jaxbContext = JAXBContext.newInstance(Communicationsuite.class);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public enum Function {
        getUser,
        getDomainPreferences
    }

    @SpringBean("userManager")
    IUserManager userManager;

    private Function function;
    private String sessionGUID;
    private String domainID;

    @DefaultHandler
	@DontValidate
	public Resolution load() {
		return new ForwardResolution("/ajax/dash/lounge.jsp");
	}

	@HandlesEvent("csxml")
	@DontValidate
    public Resolution execute() throws JAXBException, DatatypeConfigurationException {

        if (Function.getDomainPreferences == function) {
            return getDomainPreferences();
        } else if (Function.getUser == function) {
            return getUser();
        } else {
			return getEmptyResponse();
		}

    }

	@DontValidate
    public Resolution getEmptyResponse() throws DatatypeConfigurationException, JAXBException {
		Communicationsuite suite = new Communicationsuite();
		suite.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		return createResolution(suite);		
	}

	@DontValidate
    public Resolution getDomainPreferences() throws JAXBException, DatatypeConfigurationException {
        Communicationsuite suite = new Communicationsuite();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        suite.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));

        Domain domain = new Domain();
        suite.setDomain(domain);
        domain.setMaxxmlretries(5);
		domain.setAvenabled(true);

        Chat chat = new Chat();
        domain.setChat(chat);
        chat.setBanNotification("<![CDATA[<b>[[NAME]] was banned</b>]]>");
		chat.setUserroomcreate(false);
		chat.setMaxroomusers(1000000);
		chat.setMaxdockitems(7);

        AllowModeratedRooms allowModeratedRooms = new AllowModeratedRooms();
        allowModeratedRooms.setAutoOn(false);
        allowModeratedRooms.setValue(false);

        Gui gui = new Gui();
        chat.setGui(gui);
        gui.setAddfriend(false);
		gui.setDockMinimized(false);
		gui.setViewprofile(true);
		gui.setInstantcommunicator(true);
		gui.setHelp(true);

		gui.setTitleBarColor("333333");
		gui.setUiFontColor("AAAAAA");
		gui.setButtonColor("111111");
		gui.setInnerBackgroundColor("FFFFFF");
		gui.setOuterBackgroundColor("FFFFFF");

		Lobby lobby = new Lobby();
		lobby.setName("Lounge");
		lobby.setDescription("The place to chillax");
		Labels labels = new Labels();
		labels.setLobby(lobby);
		Userdata userData = new Userdata();
		labels.setUserdata(userData);
		userData.getLine().add("Age/Gender");
		userData.getLine().add("Currently In");
		userData.getLine().add("Living In");
		chat.setLabels(labels);
        return createResolution(suite);
    }

	@DontValidate
    public Resolution getUser() throws DatatypeConfigurationException, JAXBException {

        // todo for now we are just using the user's guid for the session id.
        // later in the future we should replace it with something else.

        Communicationsuite suite = new Communicationsuite();
        suite.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar());

        com.blackbox.foundation.userplane.User userType = new com.blackbox.foundation.userplane.User();
        suite.setUser(userType);
        com.blackbox.foundation.user.User user = userManager.loadUserByGuid(sessionGUID);

		if (user == null) {
            userType.setUserid("INVALID");
        }
        else {

            userType.setUserid(user.getGuid());
            userType.setDisplayname(user.getUsername());
            userType.setAdmin(user.getType() == com.blackbox.foundation.user.User.UserType.BLACKBOX_ADMIN);

			Images images = new Images();
			userType.setImages(images);
			String avatarUrl = user.getProfile().getAvatarUrl();
			images.setIcon(avatarUrl);
			images.setThumbnail(avatarUrl);
			images.setFullsize(avatarUrl);

			Chat chat = new Chat();
			userType.setChat(chat);
			Userdatavalues userdatavalues = new Userdatavalues();
			chat.setUserdatavalues(userdatavalues);
			chat.setSessionTimeout(-1);
			chat.setMaxdockitems(7);

			StringBuilder line1 = new StringBuilder();

			if(!user.getProfile().isBirthdayInVisible()) {
				line1.append(JspFunctions.age(user.getProfile().getBirthday()));
			}

			if(user.getProfile().getSex() == SexEnum.MALE) {
				line1.append("M");
			} else if(user.getProfile().getSex() == SexEnum.FEMALE) {
				line1.append("F");
			}

			userdatavalues.getLine().add(line1.toString());
			userdatavalues.getLine().add(user.getProfile().getCurrentAddress().getCity());
			userdatavalues.getLine().add(user.getProfile().getLocation().getCity());

        }

		Avsettings avsettings = new Avsettings();
		userType.setAvsettings(avsettings);
		avsettings.setAvEnabled(true);
		avsettings.setAudioSend(true);
		avsettings.setVideoSend(true);
		avsettings.setAudioReceive(true);
		avsettings.setVideoReceive(true);
		avsettings.setVideofps(15);
		avsettings.setAudiokbps(16);
		avsettings.setVideokbps(100);
		avsettings.setVideoDisplaySize(1);
		
        return createResolution(suite);
    }

	@DontValidate
    private Resolution createResolution(Communicationsuite suite) throws JAXBException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(suite, out);
        return new StreamingResolution("text/xml", new ByteArrayInputStream(out.toByteArray()));
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setSessionGUID(String sessionGUID) {
        this.sessionGUID = sessionGUID;
    }

    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }
//
//    @Override
//    public void setContext(ActionBeanContext context) {
//        this.context = context;
//    }
//
//    @Override
//    public ActionBeanContext getContext() {
//        return context;
//    }
}
