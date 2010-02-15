package com.blackbox.message;

import static com.blackbox.message.MailboxRequest.MailboxFilter.ALL;
import static com.blackbox.message.MailboxRequest.MailboxFolder.INBOX_FOLDER;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class MailboxRequest implements Serializable {

    public static enum MailboxFilter {
        ALL, // Same as null
        FRIENDS,
        FOLLWERS,
        FOLLOWING,
        WISHED
    }

    public static enum MailboxFolder {
        INBOX_FOLDER,
        SENT_FOLDER,
        ARCHIVED_FOLDER,
        DRAFTS_FOLDER
    }

    public static enum ReadState {
        READ,
        UNREAD,
        EITHER
    }

    private String userGuid;
    private MailboxFolder folder = INBOX_FOLDER;
    private MailboxFilter filter = ALL;
    private int startIndex = 0;
    private int maxResults = 10;
    private ReadState readState = ReadState.EITHER;

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public MailboxFolder getFolder() {
        return folder;
    }

    public void setFolder(MailboxFolder folder) {
        this.folder = folder;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public MailboxFilter getFilter() {
        return filter;
    }

    public void setFilter(MailboxFilter filter) {
        this.filter = filter;
    }

    public ReadState getReadState() {
        return readState;
    }

    public void setReadState(ReadState readState) {
        this.readState = readState;
    }
}
