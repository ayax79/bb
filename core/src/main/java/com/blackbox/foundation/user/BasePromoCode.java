package com.blackbox.foundation.user;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.util.JodaDateTimeXmlAdapter;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement
public abstract class BasePromoCode extends BBPersistentObject {

    private static final long serialVersionUID = -7715627278688004819L;

    public static enum PromoCodeType {
        MULTI_USE,
        SINGLE_USE

    }

    protected String code;
    protected User master;
    protected Integer evaluationPeriod;
    protected DateTime expirationDate;
    protected User.UserType userType = User.UserType.NORMAL;
    protected PromoCodeType type;

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Number of months to evaluate the app. Null for unlimited.
     *
     * @return Returns number of months to evaluate the app. Null for unlimited.
     */
    public Integer getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public void setEvaluationPeriod(Integer evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

    @XmlJavaTypeAdapter(JodaDateTimeXmlAdapter.class)
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public PromoCodeType getType() {
        return type;
    }

    public void setType(PromoCodeType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BasePromoCode that = (BasePromoCode) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (evaluationPeriod != null ? !evaluationPeriod.equals(that.evaluationPeriod) : that.evaluationPeriod != null)
            return false;
        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
            return false;
        if (master != null ? !master.equals(that.master) : that.master != null) return false;
        if (type != that.type) return false;
        //noinspection RedundantIfStatement
        if (userType != that.userType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (master != null ? master.hashCode() : 0);
        result = 31 * result + (evaluationPeriod != null ? evaluationPeriod.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "BasePromoCode{" +
                "code='" + code + '\'' +
                ", master=" + master +
                ", evaluationPeriod=" + evaluationPeriod +
                ", expirationDate=" + expirationDate +
                ", userType=" + userType +
                ", type=" + type +
                '}';
    }
}
