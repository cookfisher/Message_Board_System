package message.services;

import message.entities.Post;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PostService implements IPostService {
    private static final PostService INSTANCE = new PostService();

    public static PostService getInstance() {
        return INSTANCE;
    }

    private PostService() {

    }

    @Override
    public synchronized void init(String jdbcUrl, String jdbcUsername, String jdbcPassword, UserManager userManager) {
        if (init) {
            return;
        }
        this.init = true;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.userManager = userManager;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM POST");
            while (result.next()) {
                posts.add(Post.from(result));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private UserManager userManager;


    private boolean init = false;
    private final List<Post> posts = new ArrayList<>();

    @Override
    public Post create(String username, String content, String groupName) {
        Post post = new Post(username, LocalDateTime.now(), content);
        post.setGroupName(groupName);
        post.setHashTags(hashTags(content));
        posts.add(post);

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO POST VALUES (NULL, ?, ?, 0, ?, ?, NULL, NULL, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setLong(2, post.getPostedAt().toEpochSecond(ZoneOffset.UTC));
            preparedStatement.setString(3, content);
            preparedStatement.setString(4, String.join(",", post.getHashTags()));
            preparedStatement.setString(5, groupName);
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            resultSet.next();
            post.setId(resultSet.getInt(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return post;
    }

    private void save(Post post) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE POST SET UPDATED = ?, CONTENT = ?, HASH_TAG = ?, ATTACHMENT = ?, ATTACHMENT_NAME = ? WHERE ID = ?");
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setString(3, String.join(",", post.getHashTags()));
            if (post.getAttachment() != null) {
                preparedStatement.setString(4, post.getAttachment().toString());
            } else {
                preparedStatement.setString(4, null);
            }
            preparedStatement.setString(5, post.getAttachmentName());
            preparedStatement.setInt(6, post.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> findAll(String username) {
        if (isAdmin(username)) {
            return Collections.unmodifiableList(posts);
        }
        List<Post> result = new ArrayList<>();
        Set<String> groups = userManager.getGroups(username);
        for (Post post : posts) {
            if (post.getGroupName().isEmpty() || groups.contains(post.getGroupName())) {
                result.add(post);
            }
        }
        return result;
    }

    @Override
    public Post find(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    private boolean isAdmin(String username) {
        return userManager.isAdmin(username);
    }

    @Override
    public void update(String username, int id, String content) {
        if (!isAdmin(username) && !find(id).getPostedBy().equals(username)) {
            return;
        }
        for (Post post : posts) {
            if (post.getId() == id) {
                post.setContent(content);
                post.setUpdated(true);
                post.setHashTags(hashTags(content));
                save(post);
            }
        }
    }

    @Override
    public void delete(String username, int id) {
        if (!isAdmin(username) && !find(id).getPostedBy().equals(username)) {
            return;
        }
        posts.removeIf(post -> post.getId() == id);
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM POST WHERE ID = ?");
            preparedStatement.setBoolean(1, true);

            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Stream<Post> search(String username, String postedBy, String from, String to, String hashTag) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Set<String> groups = userManager.getGroups(username);
        return posts.stream()
                .filter(post -> postedBy == null || postedBy.isEmpty() || post.getPostedBy().equals(postedBy))
                .filter(post -> from == null || from.isEmpty() || post.getPostedAt().isAfter(LocalDate.parse(from, formatter).atStartOfDay()))
                .filter(post -> to == null || to.isEmpty() || post.getPostedAt().isBefore(LocalDate.parse(to, formatter).plusDays(1).atStartOfDay()))
                .filter(post -> hashTag == null || hashTag.isEmpty() || post.getHashTags().contains(hashTag))
                .filter(post -> post.getGroupName().isEmpty() || groups.contains(post.getGroupName()));
    }

    private List<String> hashTags(String content) {
        int index = content.indexOf('#');
        List<String> result = new ArrayList<>();
        while (index != -1) {
            index += 1;
            int spaceIdx = content.indexOf(' ', index);
            if (spaceIdx == -1) {
                result.add(content.substring(index));
                break;
            } else {
                result.add(content.substring(index, spaceIdx));
            }

            index = content.indexOf('#', index);
        }

        return result;
    }

    @Override
    public void upload(int id, Part filePart) throws IOException {
        for (Post post : posts) {
            if (post.getId() == id) {
                post.setUpdated(true);
                Path tempFile = Files.createTempFile(null, null);
                post.setAttachmentName(filePart.getSubmittedFileName());
                Files.copy(filePart.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
                post.setAttachment(tempFile);
                save(post);
            }
        }
    }

    @Override
    public void remove(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                post.setUpdated(true);
                post.setAttachmentName(null);
                post.setAttachment(null);
                save(post);
            }
        }

    }
}
