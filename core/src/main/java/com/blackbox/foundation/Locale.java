package com.blackbox.foundation;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "locale")
public class Locale implements Serializable {

    private String countryCode;
    private String languageCode;
    private String variant;

    public Locale() {
    }

    public Locale(String languageCode, String countryCode) {
        this.languageCode = languageCode;
        this.countryCode = countryCode;
    }

    public Locale(String languageCode, String countryCode, String variant) {
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.variant = variant;
    }

    public java.util.Locale toJavaLocale() {
        return new java.util.Locale(languageCode, countryCode, variant);
    }

    public static Locale fromJavaLocale(java.util.Locale locale) {
        return new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Locale locale = (Locale) o;

        if (countryCode != null ? !countryCode.equals(locale.countryCode) : locale.countryCode != null) return false;
        if (languageCode != null ? !languageCode.equals(locale.languageCode) : locale.languageCode != null)
            return false;
        //noinspection RedundantIfStatement
        if (variant != null ? !variant.equals(locale.variant) : locale.variant != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + (languageCode != null ? languageCode.hashCode() : 0);
        result = 31 * result + (variant != null ? variant.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Locale{" +
                "countryCode='" + countryCode + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", variant='" + variant + '\'' +
                '}';
    }
}


