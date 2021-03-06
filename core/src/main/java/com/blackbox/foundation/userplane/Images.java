//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.01.28 at 03:34:04 PM PST 
//


package com.blackbox.foundation.userplane;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for images complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="images">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="watermark" type="{}watermark" minOccurs="0"/>
 *         &lt;element name="icon" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="thumbnail" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="fullsize" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "images", propOrder = {
    "watermark",
    "icon",
    "thumbnail",
    "fullsize"
})
public class Images {

    protected Watermark watermark;
    @XmlSchemaType(name = "anyURI")
    protected String icon;
    @XmlSchemaType(name = "anyURI")
    protected String thumbnail;
    @XmlSchemaType(name = "anyURI")
    protected String fullsize;

    /**
     * Gets the value of the watermark property.
     * 
     * @return
     *     possible object is
     *     {@link Watermark }
     *     
     */
    public Watermark getWatermark() {
        return watermark;
    }

    /**
     * Sets the value of the watermark property.
     * 
     * @param value
     *     allowed object is
     *     {@link Watermark }
     *     
     */
    public void setWatermark(Watermark value) {
        this.watermark = value;
    }

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcon(String value) {
        this.icon = value;
    }

    /**
     * Gets the value of the thumbnail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the value of the thumbnail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThumbnail(String value) {
        this.thumbnail = value;
    }

    /**
     * Gets the value of the fullsize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullsize() {
        return fullsize;
    }

    /**
     * Sets the value of the fullsize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullsize(String value) {
        this.fullsize = value;
    }

}
