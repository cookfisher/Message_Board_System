package message.services;

import message.entities.User;
import message.utils.Crypto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UserService {
    private final List<User> USERS = new ArrayList<>();

    public User login(String username, String password) {
        String passMD5 = Crypto.MD5(password);
        for (User user : USERS) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(passMD5)) {
                    return user;
                }
                else {
                    return null;
                }
            }
        }
        return null;
    }


    public void init(InputStream resourceUrl) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(resourceUrl);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            USERS.add(new User((String) entry.getKey(), (String) entry.getValue()));
        }
    }
}
