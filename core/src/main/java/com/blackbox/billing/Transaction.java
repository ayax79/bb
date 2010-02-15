package com.blackbox.billing;

import static com.blackbox.billing.Transaction.TransactionStatus.PENDING;
import com.blackbox.user.User;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class Transaction implements Serializable {

    public static enum TransactionStatus {
        // please do not change the order, since the ordinal is used by hibernate
        PENDING,
        APPROVED,
        DENIED
    }

    protected Long id;
    protected Integer version;
    protected DateTime created;
    protected Money amount;
    protected TransactionStatus status = PENDING;
    protected User user;

    public Transaction() {
        this.created = new DateTime();
    }

    public Transaction(Money amount) {
        this.amount = amount;
        this.created = new DateTime();
    }

    public Transaction(Money amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        //noinspection RedundantIfStatement
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", version=" + version +
                ", created=" + created +
                ", amount=" + amount +
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}
