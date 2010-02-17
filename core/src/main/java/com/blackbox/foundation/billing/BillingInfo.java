package com.blackbox.foundation.billing;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.user.User;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement
public class BillingInfo extends BBPersistentObject {


    public static enum BillingProvider {
        INVALID,
        EPX,
        LEECH
    }

    protected User user;
    protected Address address;
    protected String providerGuid;
    protected String billingPhone;
    protected BillingProvider provider;
    protected DateTime lastBilled;
    protected DateTime lastExpirationDate;
    protected DateTime nextBillDate;
    protected String lastResponse;
    protected String authResponse;
    protected String lastAmount;
    protected String lastCardType;
    protected String lastCardNum;
    protected String firstName;
    protected String lastName;

    public BillingInfo() {
        address = new Address();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getProviderGuid() {
        return providerGuid;
    }

    public void setProviderGuid(String providerGuid) {
        this.providerGuid = providerGuid;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getLastBilled() {
        return lastBilled;
    }

    public void setLastBilled(DateTime lastBilled) {
        this.lastBilled = lastBilled;
    }

    public BillingProvider getProvider() {
        return provider;
    }

    public void setProvider(BillingProvider provider) {
        this.provider = provider;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getLastExpirationDate() {
        return lastExpirationDate;
    }

    public void setLastExpirationDate(DateTime lastExpirationDate) {
        this.lastExpirationDate = lastExpirationDate;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(String authResponse) {
        this.authResponse = authResponse;
    }

    public String getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(String lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getLastCardType() {
        return lastCardType;
    }

    public void setLastCardType(String lastCardType) {
        this.lastCardType = lastCardType;
    }

    public String getLastCardNum() {
        return lastCardNum;
    }

    public void setLastCardNum(String lastCardNum) {
        this.lastCardNum = lastCardNum;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getNextBillDate() {
        return nextBillDate;
    }

    public void setNextBillDate(DateTime nextBillDate) {
        this.nextBillDate = nextBillDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BillingInfo that = (BillingInfo) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (authResponse != null ? !authResponse.equals(that.authResponse) : that.authResponse != null) return false;
        if (billingPhone != null ? !billingPhone.equals(that.billingPhone) : that.billingPhone != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastAmount != null ? !lastAmount.equals(that.lastAmount) : that.lastAmount != null) return false;
        if (lastBilled != null ? !lastBilled.equals(that.lastBilled) : that.lastBilled != null) return false;
        if (lastCardNum != null ? !lastCardNum.equals(that.lastCardNum) : that.lastCardNum != null) return false;
        if (lastCardType != null ? !lastCardType.equals(that.lastCardType) : that.lastCardType != null) return false;
        if (lastExpirationDate != null ? !lastExpirationDate.equals(that.lastExpirationDate) : that.lastExpirationDate != null)
            return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (lastResponse != null ? !lastResponse.equals(that.lastResponse) : that.lastResponse != null) return false;
        if (nextBillDate != null ? !nextBillDate.equals(that.nextBillDate) : that.nextBillDate != null) return false;
        if (provider != that.provider) return false;
        if (providerGuid != null ? !providerGuid.equals(that.providerGuid) : that.providerGuid != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (providerGuid != null ? providerGuid.hashCode() : 0);
        result = 31 * result + (billingPhone != null ? billingPhone.hashCode() : 0);
        result = 31 * result + (provider != null ? provider.hashCode() : 0);
        result = 31 * result + (lastBilled != null ? lastBilled.hashCode() : 0);
        result = 31 * result + (lastExpirationDate != null ? lastExpirationDate.hashCode() : 0);
        result = 31 * result + (nextBillDate != null ? nextBillDate.hashCode() : 0);
        result = 31 * result + (lastResponse != null ? lastResponse.hashCode() : 0);
        result = 31 * result + (authResponse != null ? authResponse.hashCode() : 0);
        result = 31 * result + (lastAmount != null ? lastAmount.hashCode() : 0);
        result = 31 * result + (lastCardType != null ? lastCardType.hashCode() : 0);
        result = 31 * result + (lastCardNum != null ? lastCardNum.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BillingInfo{" +
                "user=" + user +
                ", address=" + address +
                ", providerGuid='" + providerGuid + '\'' +
                ", billingPhone='" + billingPhone + '\'' +
                ", provider=" + provider +
                ", lastBilled=" + lastBilled +
                ", lastExpirationDate=" + lastExpirationDate +
                ", nextBillDate=" + nextBillDate +
                ", lastResponse='" + lastResponse + '\'' +
                ", authResponse='" + authResponse + '\'' +
                ", lastAmount='" + lastAmount + '\'' +
                ", lastCardType='" + lastCardType + '\'' +
                ", lastCardNum='" + lastCardNum + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
