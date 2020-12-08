package message.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Crypto {
    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        String user = "jia";
        String pwd = MD5(user);
        System.out.println(pwd);
    }
}
