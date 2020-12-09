package message.services;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UserMngFactory {
    private static UserManager instance;
    private UserMngFactory() {}
    // a Factory pattern which is implemented as a singleton
    public static UserManager getInstance(String classname, InputStream groups, InputStream membership, InputStream users, String adminGroup) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if(instance == null){
            synchronized (UserManager.class) {
                if(instance == null) {
                    Class<?> cl = Class.forName(classname);
                    Class<?>[] type = {InputStream.class, InputStream.class, InputStream.class, String.class};
                    Constructor<?> constructor = cl.getConstructor(type);
                    Object[] obj = {groups, membership, users, adminGroup};
                    Object nIns = constructor.newInstance(obj);
                    instance = (UserManager) nIns;
                }
            }
        }
            return instance;
    }
}
