package message.services;

import message.usermangagerlib.UserMangerImpl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class UserManagerFactory {
    private static UserMangerImpl instance;
    private UserManagerFactory() {}
    // a Factory pattern which is implemented as a singleton
    public static UserMangerImpl getInstance(String path, String user, String pwd, File file) {
        path = "message.usermangagerlib.UserMangerImpl";
        try {
            if (instance == null) {
                synchronized (UserMangerImpl.class) {
                    if (instance == null) {
                        Class<?> cl = Class.forName(path);
                        Class<?>[] type = {String.class, String.class, File.class};

                        Constructor<?> cons = cl.getConstructor(type);
                        Object[] obj = {user, pwd, file};
                        Object nIns = cons.newInstance(obj);

                        instance = (UserMangerImpl) nIns;
                    }
                }
            }
            return instance;
        }catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException exception){
            exception.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //delete this one
    public static UserMangerImpl createUserManager(String path, String user, String pwd, File file) {
        path = "message.usermangagerlib.UserMangerImpl";
        try {
//            Class<?> cl = Class.forName("message.usermangagerlib.UserMangerImpl");
                Class<?> cl = Class.forName(path);
                Class<?>[] type = {String.class, String.class, File.class};

                Constructor<?> cons = cl.getConstructor(type);
                Object[] obj = {user, pwd, file};
                Object nIns = cons.newInstance(obj);

                UserMangerImpl umg = (UserMangerImpl) nIns;

            return umg;
        }catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException exception){
            exception.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        String user = "jane";
        String pwd = "WEShXnZWP+3RGED9b0Dqew==";
        String group = "encs";
        File file = new File("src/main/webapp/WEB-INF/userinfo1.xml");
        File file1 = new File("src/main/webapp/WEB-INF/membership.xml");
        String path = "message.usermangagerlib.UserMangerImpl";
        UserMangerImpl userManger = UserManagerFactory.getInstance(path, user,pwd,file);
        System.out.println(userManger.isAuthenticated(user,pwd,file));
        System.out.println(userManger.isBelongTo(user,file1));
        System.out.println(userManger.getGroupView(group));

    }
}
