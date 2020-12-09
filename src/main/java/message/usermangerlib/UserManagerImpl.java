package message.usermangerlib;

import message.entities.User;
import message.services.UserManager;
import message.utils.Crypto;
import message.utils.LinkedProperities;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UserManagerImpl implements UserManager {

    private final List<User> USERS = new ArrayList<>();
    private final Map<String, Set<String>> groupTree = new HashMap<>();
    private final Map<String, Set<String>> membership = new HashMap<>();
    private String adminGroup;


    public UserManagerImpl() {

    }
    public UserManagerImpl(InputStream groups, InputStream membership, InputStream users, String adminGroup) throws IOException {
        this.adminGroup = adminGroup;
        init(groups, membership, users);
    }

    @Override
    public User login(String username, String password) {
        String passMD5 = Crypto.MD5(password);
        for (User user : USERS) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(passMD5)) {
                    return user;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public void init(InputStream groups, InputStream membership, InputStream users) throws IOException {
        LinkedProperities properties = new LinkedProperities();
        properties.loadFromXML(users);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            addUser((String) entry.getKey(), (String) entry.getValue());
        }
        properties = new LinkedProperities();
        properties.loadFromXML(groups);
        for (Object key : properties.orderStringPropertyNames()) {
            String group = key.toString();
            String parent = properties.get(key).toString();
            if (parent.isEmpty()) {
                addGroup(group);
            } else {
                addGroup(group, parent);
            }
        }
        properties = new LinkedProperities();
        properties.loadFromXML(membership);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] groupArr = entry.getValue().toString().split(",");
            for (String group : groupArr) {
                group = group.trim();
                addMembership(entry.getKey().toString(), group);
                for (String group2 : groupTree.get(group)) {
                    addMembership(entry.getKey().toString(), group2);
                }
            }
        }
    }

    public void addGroup(String group, String parent) {
        if (group == null || parent == null || containsGroup(group) || !containsGroup(parent)) {
            throw new IllegalArgumentException("group or parent is invalid");
        }
        groupTree.put(group, new HashSet<>());
        groupTree.get(parent).add(group);
    }

    public void addGroup(String group) {
        if (group == null || containsGroup(group)) {
            throw new IllegalArgumentException("group or parent is invalid");
        }
        if (!groupTree.containsKey(adminGroup)) {
            groupTree.put(adminGroup, new HashSet<>());
        }
        if (!group.equals(adminGroup)) {
            groupTree.get(adminGroup).add(group);
        }
        groupTree.put(group, new HashSet<>());
    }

    public boolean containsGroup(String group) {
        if (group == null) {
            throw new IllegalArgumentException("group is null");
        }
        return groupTree.containsKey(group);
    }

    public boolean containsUser(String user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        for (User user1 : USERS) {
            if (user.equals(user1.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void addMembership(String user, String group) {
        if (!containsUser(user)) {
            throw new IllegalArgumentException("user is not exists.");
        }
        if (!containsGroup(group)) {
            throw new IllegalArgumentException("group is not exists.");
        }
        membership.computeIfAbsent(user, s -> new HashSet<>()).add(group);
    }

    public void addUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        }
        USERS.add(new User(username, password));
    }

    @Override
    public Set<String> getGroups(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        Set<String> groups = membership.get(username);
        if (groups == null) {
            return new HashSet<>();
        }
        Set<String> result = new HashSet<>();
        for (String group : groups) {
            getAllSubGroups(group, result);
        }
        return result;
    }

    @Override
    public void setAdminGroup(String adminGroup) {
        this.adminGroup = adminGroup;
    }

    @Override
    public String getAdminGroup() {
        return adminGroup;
    }

    @Override
    public boolean isAdmin(String username) {
        return getGroups(username).contains(getAdminGroup());
    }

    private void getAllSubGroups(String group, Set<String> groups) {
        if (group == null) {
            return;
        }
        groups.add(group);
        for (String child : groupTree.get(group)) {
            getAllSubGroups(child, groups);
        }
    }

}
