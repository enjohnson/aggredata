package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml"
})
public class GroupDAOTest {

    @Autowired
    GroupDAO groupDAO;

    @Autowired
    UserDAO userDAO;

    
    User groupOwner1 = null;
    User groupOwner2 = null;

    @Before
    public void setupUser()
    {

        groupOwner1 = new User();
        groupOwner1.setUsername(TestUtil.getUniqueKey());
        groupOwner1.setActivationKey(TestUtil.getUniqueKey());
        groupOwner1.setRole("ROLE_USER");
        groupOwner1.setAccountState(User.STATE_ENABLED);

        groupOwner2 = new User();
        groupOwner2.setUsername(TestUtil.getUniqueKey());
        groupOwner2.setActivationKey(TestUtil.getUniqueKey());
        groupOwner2.setRole("ROLE_USER");
        groupOwner2.setAccountState(User.STATE_ENABLED);

        groupOwner1 = userDAO.create(groupOwner1);
        groupOwner2 = userDAO.create(groupOwner2);
        Assert.assertNotNull(groupOwner1);
        Assert.assertNotNull(groupOwner2);


    }

    @After
    public void disableUser()
    {
        groupOwner1.setAccountState(User.STATE_ENABLED);
        groupOwner1.setRole("ROLE_NONE");
        userDAO.save(groupOwner1);

        groupOwner2.setAccountState(User.STATE_ENABLED);
        groupOwner2.setRole("ROLE_NONE");
        userDAO.save(groupOwner2);

    }

    @Test
    public void testGROUPDAO()
    {

        String groupName2 = TestUtil.getUniqueKey();
        String groupName1 = TestUtil.getUniqueKey();


        //Create the group
        Group newGroup1 = groupDAO.create(groupOwner1, groupName1);
        Group newGroup2 = groupDAO.create(groupOwner1, groupName2);

        //Check the load group by description
        Group createdGroup1 = groupDAO.getOwnedGroup(groupOwner1, groupName1);
        Group createdGroup2 = groupDAO.getOwnedGroup(groupOwner1, groupName2);

        groupDAO.addGroupMembership(groupOwner2, createdGroup1, Group.Role.MEMBER);

        Assert.assertNotNull(createdGroup1);
        Assert.assertEquals(createdGroup1.getDescription(), groupName1);
        Assert.assertEquals(createdGroup1.getOwnerUserId(), groupOwner1.getId());
        Assert.assertNotSame(createdGroup1.getDescription(), newGroup2.getDescription());

        //Check load group negative (bad description)
        Group group2 = groupDAO.getOwnedGroup(groupOwner1, TestUtil.getUniqueKey());
        Assert.assertNull(group2);

        //Check load group negative (bad owner)
        Group group3 = groupDAO.getOwnedGroup(groupOwner2, groupName1);
        Assert.assertNull(group3);

        //check group memberships
        Assert.assertEquals(groupDAO.findGroupsByUser(groupOwner1).size(), 2);
        Assert.assertEquals(groupDAO.findGroupsByUser(groupOwner2).size(), 1);

        groupDAO.removeGroupMembership(groupOwner2, createdGroup1);
        Assert.assertEquals(groupDAO.findGroupsByUser(groupOwner2).size(), 0);


        //Delete the groups
        groupDAO.delete(createdGroup1);
        groupDAO.delete(createdGroup2);

        Assert.assertNull(groupDAO.getOwnedGroup(groupOwner1, groupName1));
        Assert.assertNull(groupDAO.getOwnedGroup(groupOwner1, groupName2));
        Assert.assertEquals(groupDAO.findGroupsByUser(groupOwner1).size(), 0);

        userDAO.delete(groupOwner1);
        userDAO.delete(groupOwner2);

    }
}
