package message.entities;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Post {
    private int id;
    private String postedBy;
    private LocalDateTime postedAt;
    private boolean updated = false;
    private String content;
    private List<String> hashTags = new ArrayList<>();;
    private Path attachment;
    private String attachmentName;
    private String groupName;

    public Post() {
    }

    public Post(String postedBy, LocalDateTime postedAt, String content) {
        this.postedBy = postedBy;
        this.postedAt = postedAt;
        this.content = content;
    }

    public static Post from(ResultSet result) throws SQLException {
        Post post = new Post();
        post.setId(result.getInt("ID"));
        post.setPostedBy(result.getString("POSTED_BY"));
        post.setPostedAt(LocalDateTime.ofEpochSecond(result.getLong("POSTED_AT"), 0, ZoneOffset.UTC));
        post.setUpdated(result.getBoolean("UPDATED"));
        post.setContent(result.getString("CONTENT"));
        String[] hash_tags = result.getString("HASH_TAG").split(",");
        if (hash_tags.length > 0) {
            post.hashTags.addAll(Arrays.asList(hash_tags));
        }

        String attachment = result.getString("ATTACHMENT");
        if (attachment != null) {
            post.setAttachment(Paths.get(attachment));
        }
        post.setAttachmentName(result.getString("ATTACHMENT_NAME"));
        post.setGroupName(result.getString("GROUP_NAME"));
        return post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public String getFormattedPostedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return postedAt.format(formatter);
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public String getFormattedTags() {
        return this.hashTags.stream().collect(Collectors.joining(",", "[", "]"));
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Path getAttachment() {
        return attachment;
    }

    public void setAttachment(Path attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
