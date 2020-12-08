package message.utils;

import message.services.UserManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UserManagerFactory {

    private static UserManager userManager;

    private UserManagerFactory() {

    }

    public static synchronized UserManager getUserManager(String type, InputStream groups,
                                                          InputStream membership, InputStream users, String adminGroup)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if (userManager == null) {
            Class<?> clazz = Class.forName(type);
            Constructor<?> constructor = clazz.getConstructor(InputStream.class, InputStream.class,
                    InputStream.class, String.class);
            userManager = (UserManager) constructor.newInstance(groups, membership, users, adminGroup);
        }
        return userManager;
    }

}
