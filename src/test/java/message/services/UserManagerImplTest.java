package message.services;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserManagerImplTest {


    @Test(expected = IllegalArgumentException.class)
    public void test1() {
        UserManagerImpl userManagerImpl = new UserManagerImpl();
        userManagerImpl.addGroup("group1", "group2");
        userManagerImpl.addGroup("a", null);
        userManagerImpl.addGroup("b", "a");
    }

    @Test
    public void test2() {
        UserManagerImpl userManagerImpl = new UserManagerImpl();
        userManagerImpl.addGroup("a");
        userManagerImpl.addGroup("b", "a");
        userManagerImpl.addUser("user", "user");
        userManagerImpl.addMembership("user","a");
        assertEquals("[a, b]",userManagerImpl.getGroups("user").toString());
    }

    @Test
    public void test3() {
        UserManagerImpl userManagerImpl = new UserManagerImpl();
        userManagerImpl.setAdminGroup("admins");
        userManagerImpl.addUser("john", "john");
        userManagerImpl.addUser("jane", "jane");
        userManagerImpl.addUser("jack123", "jack123");
        userManagerImpl.addGroup("admins");
        userManagerImpl.addGroup("concordia");
        userManagerImpl.addGroup("encs", "concordia");
        userManagerImpl.addGroup("comp", "encs");
        userManagerImpl.addGroup("soen", "encs");
        userManagerImpl.addMembership("john","admins");
        userManagerImpl.addMembership("john", "concordia");
        userManagerImpl.addMembership("jane","encs");
        userManagerImpl.addMembership("jack123","soen");
        assertEquals("[concordia, soen, comp, encs, admins]", userManagerImpl.getGroups("john").toString());
        assertEquals("[soen, comp, encs]", userManagerImpl.getGroups("jane").toString());
        assertEquals("[soen]", userManagerImpl.getGroups("jack123").toString());
        assertTrue(userManagerImpl.isAdmin("john"));
        assertFalse(userManagerImpl.isAdmin("jane"));
        assertFalse(userManagerImpl.isAdmin("jack123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test4(){
        UserManagerImpl userManagerImpl = new UserManagerImpl();
        userManagerImpl.addGroup("group1", "group2");
    }

}