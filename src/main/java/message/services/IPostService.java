package message.services;

import message.entities.Post;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface IPostService {
    Post create(String username, String content, String groupName);

    List<Post> findAll(String username);

    Post find(int id);

    void update(String username, int id, String content);

    void delete(String username, int id);

    Stream<Post> search(String username, String postedBy, String from, String to, String hashTag);

    void upload(int id, Part filePart) throws IOException;

    void remove(int id);

    void init(String jdbcUrl, String jdbcUsername, String jdbcPassword, UserManager userManager);
}
