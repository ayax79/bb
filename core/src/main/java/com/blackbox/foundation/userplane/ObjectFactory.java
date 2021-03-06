//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.01.28 at 03:34:04 PM PST 
//


package com.blackbox.foundation.userplane;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.blackbox.foundation.userplane package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Communicationsuite_QNAME = new QName("", "communicationsuite");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.blackbox.foundation.userplane
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Communicationsuite }
     * 
     */
    public Communicationsuite createCommunicationsuite() {
        return new Communicationsuite();
    }

    /**
     * Create an instance of {@link Userdata }
     * 
     */
    public Userdata createUserdata() {
        return new Userdata();
    }

    /**
     * Create an instance of {@link Lobby }
     * 
     */
    public Lobby createLobby() {
        return new Lobby();
    }

    /**
     * Create an instance of {@link Userlist }
     * 
     */
    public Userlist createUserlist() {
        return new Userlist();
    }

    /**
     * Create an instance of {@link Roomlist }
     * 
     */
    public Roomlist createRoomlist() {
        return new Roomlist();
    }

    /**
     * Create an instance of {@link Chat }
     * 
     */
    public Chat createChat() {
        return new Chat();
    }

    /**
     * Create an instance of {@link Buddylist }
     * 
     */
    public Buddylist createBuddylist() {
        return new Buddylist();
    }

    /**
     * Create an instance of {@link Userdatavalues }
     * 
     */
    public Userdatavalues createUserdatavalues() {
        return new Userdatavalues();
    }

    /**
     * Create an instance of {@link Minichat }
     * 
     */
    public Minichat createMinichat() {
        return new Minichat();
    }

    /**
     * Create an instance of {@link Initialroom }
     * 
     */
    public Initialroom createInitialroom() {
        return new Initialroom();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Images }
     * 
     */
    public Images createImages() {
        return new Images();
    }

    /**
     * Create an instance of {@link Blocklist }
     * 
     */
    public Blocklist createBlocklist() {
        return new Blocklist();
    }

    /**
     * Create an instance of {@link MaxUsers }
     * 
     */
    public MaxUsers createMaxUsers() {
        return new MaxUsers();
    }

    /**
     * Create an instance of {@link Adminrooms }
     * 
     */
    public Adminrooms createAdminrooms() {
        return new Adminrooms();
    }

    /**
     * Create an instance of {@link Watermark }
     * 
     */
    public Watermark createWatermark() {
        return new Watermark();
    }

    /**
     * Create an instance of {@link RestrictedRooms }
     * 
     */
    public RestrictedRooms createRestrictedRooms() {
        return new RestrictedRooms();
    }

    /**
     * Create an instance of {@link Reportabuse }
     * 
     */
    public Reportabuse createReportabuse() {
        return new Reportabuse();
    }

    /**
     * Create an instance of {@link AllowModeratedRooms }
     * 
     */
    public AllowModeratedRooms createAllowModeratedRooms() {
        return new AllowModeratedRooms();
    }

    /**
     * Create an instance of {@link Room }
     * 
     */
    public Room createRoom() {
        return new Room();
    }

    /**
     * Create an instance of {@link Domain }
     * 
     */
    public Domain createDomain() {
        return new Domain();
    }

    /**
     * Create an instance of {@link Labels }
     * 
     */
    public Labels createLabels() {
        return new Labels();
    }

    /**
     * Create an instance of {@link Avsettings }
     * 
     */
    public Avsettings createAvsettings() {
        return new Avsettings();
    }

    /**
     * Create an instance of {@link Gui }
     * 
     */
    public Gui createGui() {
        return new Gui();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Communicationsuite }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "communicationsuite")
    public JAXBElement<Communicationsuite> createCommunicationsuite(Communicationsuite value) {
        return new JAXBElement<Communicationsuite>(_Communicationsuite_QNAME, Communicationsuite.class, null, value);
    }

}
