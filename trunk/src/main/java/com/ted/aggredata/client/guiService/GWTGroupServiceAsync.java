package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.List;

public interface GWTGroupServiceAsync {
    void findGroups(User user, AsyncCallback<List<Group>> async);

    /**
     * Creates a new group
     *
     * @param user        The user creating the group. This user will be added as a OWNER of the group.
     * @param description
     */
    void createGroup(User user, String description, AsyncCallback<Group> async);

    /**
     * Saves a group
     *
     * @param group
     */
    void saveGroup(Group group, AsyncCallback<Group> async);
}
