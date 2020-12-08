package message.services;

import message.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface UserManager {
    User login(String username, String password);

    void init(InputStream groups, InputStream membership, InputStream users) throws IOException;

    Set<String> getGroups(String username);

    void setAdminGroup(String adminGroup);

    String getAdminGroup();

    boolean isAdmin(String username);
}
