/*
 * Copyright (c) 2009. Blackbox, LLC 
 * All rights reserved.
 */

package com.blackbox.foundation.user;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.Status;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.social.Vouch;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.joda.time.DateTime;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.InvocationTargetException;

import static com.blackbox.foundation.EntityType.USER;
import static org.apache.commons.beanutils.BeanUtils.cloneBean;

/**
 * Represents a basic user entity in the system the blackbox application.
 *
 * @see com.blackbox.foundation.user.IUserManager
 * @see com.blackbox.foundation.user.Profile
 */
@XmlRootElement(name = "user")
@InstrumentedClass
@Searchable
public class User extends BaseEntity implements IUser {
    private static final long serialVersionUID = 5837206872855727313L;

    @XmlRootElement
    public static enum UserType {
        /**
         * The $5 user
         */
        LIMITED,
        /**
         * The $25 user
         */
        NORMAL,
        PROMOTER,
        VENDOR,
        FOUNDER,
        BLACKBOX_EMPLOYEE,
        BLACKBOX_ADMIN,
        AFFILIATE,
        BLACKBOX_MARKETING
    }

    private String email;
    @SearchableProperty
    private String username;
    private String password;
    @SearchableProperty
    private UserType userType = UserType.NORMAL;
    @SearchableComponent(prefix = "profile_")
    private Profile profile;
    @SearchableProperty
    private String lastname;
    @SearchableProperty
    private DateTime lastOnline;
    private String epxId;
    private Integer maxPermanentVouches;
    private Integer maxWishes;
    private Integer maxTemporaryVouches;


    public User(String username, String guid, String name, String lastname, String city, DateTime birthday) {
        this(username, guid, name, lastname);
        profile.setBirthday(birthday);
        profile.getLocation().setCity(city);
    }

    public User(String username, String guid, String name, String lastname) {
        this();
        this.username = username;
        this.guid = guid;
        setName(name);
        this.lastname = lastname;
    }

    public User(String username, String guid, String name, String lastname, UserType type) {
        this(username, guid, name, lastname);
        this.userType = type;
    }

    public User(String username, String guid, String password, Status status, UserType type) {
        this(username, guid, null, (String) null, type);
        this.password = password;
        setStatus(status);
    }

    /**
     * Creates a new instance of the user.
     * <p/>
     * If you are wanting to actual create a new user to the system use the {@link #createUser()} method as it will
     * populate the user with a new guid.
     */
    public User() {
        super(USER);
        setStatus(Status.UNVERIFIED);
        setProfile(new Profile());
    }

    public User(String guid) {
        this();
        setGuid(guid);
    }

    /**
     * Returns the email address of the user.
     *
     * @return Returns the email address for the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the username of the user. This username is much like a vanity name in facebook.  must be unique as we load
     * profiles and miniprofiles by usernam, so has to be unique
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * ** Internal Use Only **
     * <p/>
     * The password hash of the user. Note, that generally this will be empty.
     * <p/>
     * This field is only used by the underlying system and should be ignored.
     *
     * @return The password has of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * The password has for the user.
     *
     * @param password The password has of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return userType;
    }

    public void setType(UserType userType) {
        this.userType = userType;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(DateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    /**
     * Returns the value of the last EPX transaction GUID if one exists
     *
     * @return Returns the user's last EPX transaction GUID
     */
    public String getEpxId() {
        return epxId;
    }

    /**
     * Sets the most recent EPX transaction GUID
     *
     * @param epxId An EPX transaction GUID
     */
    public void setEpxId(String epxId) {
        this.epxId = epxId;
    }


    @XmlTransient
    public String getFirstname() {
        return getName();
    }

    public void setFirstname(String firstname) {
        setName(firstname);
    }


    public Integer getMaxWishes() {
        if (maxWishes == null) {
            if (userType == UserType.LIMITED) {
                return Bookmark.MAX_LIMITED_USER_WISHES;
            } else if (userType == UserType.BLACKBOX_ADMIN || userType == UserType.BLACKBOX_EMPLOYEE) {
                return Integer.MAX_VALUE;
            } else {
                return Bookmark.MAX_UNLIMITED_USER_WISHES;
            }
        }
        return maxWishes;
    }

    public void setMaxWishes(Integer maxWishes) {
        this.maxWishes = maxWishes;
    }

    public Integer getMaxPermanentVouches() {
        if (maxPermanentVouches == null) {
            if (userType == UserType.LIMITED) {
                return Vouch.MAX_LIMITED_USER_PERMANENT_VOUCHES;
            } else if (userType == UserType.BLACKBOX_ADMIN || userType == UserType.BLACKBOX_EMPLOYEE) {
                return Integer.MAX_VALUE;
            } else {
                return Vouch.MAX_UNLIMITED_USER_PERMANENT_VOUCHES;
            }
        }


        return maxPermanentVouches;
    }

    public void setMaxPermanentVouches(Integer maxPermanentVouches) {
        this.maxPermanentVouches = maxPermanentVouches;
    }

    public Integer getMaxTemporaryVouches() {
        if (maxTemporaryVouches == null) {
            if (userType == UserType.LIMITED) {
                return Vouch.MAX_LIMITED_USER_TEMPORARY_VOUCHES;
            } else if (userType == UserType.BLACKBOX_ADMIN || userType == UserType.BLACKBOX_EMPLOYEE) {
                return Integer.MAX_VALUE;
            } else {
                return Vouch.MAX_UNLIMITED_USER_TEMPORARY_VOUCHES;
            }
        }

        return maxTemporaryVouches;
    }

    public void setMaxTemporaryVouches(Integer maxTemporaryVouches) {
        this.maxTemporaryVouches = maxTemporaryVouches;
    }

    /**
     * Creates a new user with the guid populated. This should be used with new user creation.
     *
     * @return A new user with the guid populated.
     */
    public static User createUser() {
        User user = new User();
        Utils.applyGuid(user);
        return user;
    }

    public static User cloneUser(User u) {
        try {
            return (User) cloneBean(u);
        } catch (IllegalAccessException e) {
            throw new BlackBoxException(e);
        } catch (InstantiationException e) {
            throw new BlackBoxException(e);
        } catch (InvocationTargetException e) {
            throw new BlackBoxException(e);
        } catch (NoSuchMethodException e) {
            throw new BlackBoxException(e);
        }
    }

    public String fullName() {
        return getName() + " " + getLastname();
    }

    @Override
    public String toString() {
        return "User{" +
                "guid='" + guid + '\'' +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", profile=" + profile +
                ", lastname='" + lastname + '\'' +
                ", lastOnline=" + lastOnline +
                ", epxId='" + epxId + '\'' +
                ", maxPermanentVouches=" + maxPermanentVouches +
                ", maxWishes=" + maxWishes +
                ", maxTemporaryVouches=" + maxTemporaryVouches +
                "}" + super.toString();
    }
}
