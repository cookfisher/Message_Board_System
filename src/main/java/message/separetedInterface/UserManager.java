package message.separetedInterface;

import java.io.File;
import java.util.List;

public interface UserManager {
    public boolean isAuthenticated(String username, String password, File file);
    //get group by username
    public String isBelongTo(String username, File file);
    //get groups by group
    public List<String> getGroupView(String group);
}
