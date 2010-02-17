package com.blackbox.foundation.social;

import static com.blackbox.foundation.social.Relationship.RelationStatus;
import com.blackbox.foundation.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class Connection implements Serializable {

    public static enum ConnectionType {
        ALL, // use to display all results, shouldn't be seen in the results
        FRIEND,
        FOLLOWING,
        FOLLOWED,
        MUTUAL_FOLLOWING;

        public static ConnectionType fromWeight(int weight) {
            Relationship.RelationStatus status = RelationStatus.getClosestStatusForWeight(weight);
            if (status == RelationStatus.FRIEND || status == RelationStatus.IN_RELATIONSHIP_PENDING || status == RelationStatus.IN_RELATIONSHIP) {
                return FRIEND;
            } else if (status == RelationStatus.FOLLOW || status == RelationStatus.FRIEND_PENDING) {
                return FOLLOWING;
            }
            throw new IllegalArgumentException("unknown weight");
        }
    }

    private User user;
    private ConnectionType type;

    public Connection() {
    }

    public Connection(User user, ConnectionType type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }
}
