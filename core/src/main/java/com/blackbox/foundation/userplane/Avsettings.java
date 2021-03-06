//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.01.28 at 03:34:04 PM PST 
//


package com.blackbox.foundation.userplane;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for avsettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="avsettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="audioSend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="videoSend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="audioReceive" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="videoReceive" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="audiokbps" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="videokbps">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;enumeration value="10"/>
 *               &lt;enumeration value="16"/>
 *               &lt;enumeration value="22"/>
 *               &lt;enumeration value="44"/>
 *               &lt;enumeration value="88"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="videofps" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="videosize">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="videoDisplaySize">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "avsettings", propOrder = {
    "avEnabled",
    "audioSend",
    "videoSend",
    "audioReceive",
    "videoReceive",
    "audiokbps",
    "videokbps",
    "videofps",
    "videosize",
    "videoDisplaySize"
})
public class Avsettings {

    protected boolean avEnabled;
    protected boolean audioSend;
    protected boolean videoSend;
    protected boolean audioReceive;
    protected boolean videoReceive;
    protected int audiokbps;
    protected int videokbps;
    protected int videofps;
    protected int videosize;
    protected int videoDisplaySize;

    /**
     * Gets the value of the avEnabled property.
     * 
     */
    public boolean isAvEnabled() {
        return avEnabled;
    }

    /**
     * Sets the value of the avEnabled property.
     * 
     */
    public void setAvEnabled(boolean value) {
        this.avEnabled = value;
    }

    /**
     * Gets the value of the audioSend property.
     * 
     */
    public boolean isAudioSend() {
        return audioSend;
    }

    /**
     * Sets the value of the audioSend property.
     * 
     */
    public void setAudioSend(boolean value) {
        this.audioSend = value;
    }

    /**
     * Gets the value of the videoSend property.
     * 
     */
    public boolean isVideoSend() {
        return videoSend;
    }

    /**
     * Sets the value of the videoSend property.
     * 
     */
    public void setVideoSend(boolean value) {
        this.videoSend = value;
    }

    /**
     * Gets the value of the audioReceive property.
     * 
     */
    public boolean isAudioReceive() {
        return audioReceive;
    }

    /**
     * Sets the value of the audioReceive property.
     * 
     */
    public void setAudioReceive(boolean value) {
        this.audioReceive = value;
    }

    /**
     * Gets the value of the videoReceive property.
     * 
     */
    public boolean isVideoReceive() {
        return videoReceive;
    }

    /**
     * Sets the value of the videoReceive property.
     * 
     */
    public void setVideoReceive(boolean value) {
        this.videoReceive = value;
    }

    /**
     * Gets the value of the audiokbps property.
     * 
     */
    public int getAudiokbps() {
        return audiokbps;
    }

    /**
     * Sets the value of the audiokbps property.
     * 
     */
    public void setAudiokbps(int value) {
        this.audiokbps = value;
    }

    /**
     * Gets the value of the videokbps property.
     * 
     */
    public int getVideokbps() {
        return videokbps;
    }

    /**
     * Sets the value of the videokbps property.
     * 
     */
    public void setVideokbps(int value) {
        this.videokbps = value;
    }

    /**
     * Gets the value of the videofps property.
     * 
     */
    public int getVideofps() {
        return videofps;
    }

    /**
     * Sets the value of the videofps property.
     * 
     */
    public void setVideofps(int value) {
        this.videofps = value;
    }

    /**
     * Gets the value of the videosize property.
     * 
     */
    public int getVideosize() {
        return videosize;
    }

    /**
     * Sets the value of the videosize property.
     * 
     */
    public void setVideosize(int value) {
        this.videosize = value;
    }

    /**
     * Gets the value of the videoDisplaySize property.
     * 
     */
    public int getVideoDisplaySize() {
        return videoDisplaySize;
    }

    /**
     * Sets the value of the videoDisplaySize property.
     * 
     */
    public void setVideoDisplaySize(int value) {
        this.videoDisplaySize = value;
    }

}
