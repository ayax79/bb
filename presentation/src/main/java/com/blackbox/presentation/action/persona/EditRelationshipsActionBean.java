package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.EntityReference;

import static com.blackbox.foundation.EntityType.USER;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;

import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;

import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Relationship;

import static com.blackbox.foundation.social.Relationship.RelationStatus.IN_RELATIONSHIP;

import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.IUserManager;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONException;

import java.util.Collection;


public class EditRelationshipsActionBean extends BaseBlackBoxActionBean {

    @SpringBean("socialManager")
    ISocialManager socialManager;

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("personaHelper")
    PersonaHelper personaHelper;


    private Collection<Relationship> relationships;
    private Relationship relationship;


    @Validate(required = true, on = {"add", "delete"})
    private String guid;
    private User user;
    private String description;

    @DefaultHandler
    public Resolution begin() {
        String currentUserGuid = getCurrentUser().getGuid();
        relationships = socialManager.loadRelationships(currentUserGuid, currentUserGuid);
        populateUsers(relationships);
        return new ForwardResolution("/ajax/relationships/edit-relationships.jspf");
    }

    public Resolution loadRelationships() {
        String currentUserGuid = getCurrentUser().getGuid();
        relationships = socialManager.loadRelationships(currentUserGuid, currentUserGuid);
        populateUsers(relationships);

        return new ForwardResolution("/ajax/relationships/list-relationships.jspf");
    }

    private void populateUsers(Collection<Relationship> list) {
        for (Relationship r : list) {
            r.setToEntityObject(userManager.loadUserByGuid(r.getToEntity().getGuid()));
        }
    }

    public Resolution add() throws JSONException {
        user = userManager.loadUserByGuid(guid);
        relationship = Relationship.createRelationship(getCurrentUser().toEntityReference(),
                new EntityReference(USER, guid), IN_RELATIONSHIP);
        relationship.setDescription(description);
        relationship.setToEntityObject(user);

        socialManager.relate(relationship);

        return new ForwardResolution("/ajax/relationships/edit-relationships-relationship.jspf");
    }

    public Resolution delete() {

        Relationship r = socialManager.loadRelationshipByEntities(getCurrentUser().getGuid(), guid);
        if (r != null) {
            r.setWeight(Relationship.RelationStatus.FRIEND.getWeight());
            r.setDescription(null);
        }
        socialManager.relate(r);
        return createResolutionWithText(getContext(), "success");
    }

    public void setSocialManager(ISocialManager socialManager) {
        this.socialManager = socialManager;
    }

    public Collection<Relationship> getRelationships() {
        return relationships;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
}