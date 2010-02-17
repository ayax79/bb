package com.blackbox.foundation;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class SearchResults implements Serializable {

    private List<SearchResult<User>> users;
    private List<SearchResult<Occasion>> occasions;

    public List<SearchResult<User>> getUsers() {
        return users;
    }

    public void setUsers(List<SearchResult<User>> users) {
        this.users = users;
    }

    public List<SearchResult<Occasion>> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<SearchResult<Occasion>> occasions) {
        this.occasions = occasions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResults that = (SearchResults) o;

        if (occasions != null ? !occasions.equals(that.occasions) : that.occasions != null) return false;
        //noinspection RedundantIfStatement
        if (users != null ? !users.equals(that.users) : that.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = users != null ? users.hashCode() : 0;
        result = 31 * result + (occasions != null ? occasions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "users=" + users +
                ", occasions=" + occasions +
                '}';
    }
}
