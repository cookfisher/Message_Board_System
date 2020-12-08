package message.utils;

import message.entities.Post;

public class XMLTransformer {

    public static String transform(Post post) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<post>\n");
        xml.append(String.format("<id>%s</id>\n", post.getId()));
        xml.append(String.format("<postedBy>%s</postedBy>\n", post.getPostedBy()));
        xml.append(String.format("<postedAt>%s</postedAt>\n", post.getPostedAt()));
        xml.append(String.format("<updated>%s</updated>\n", post.isUpdated()));
        xml.append(String.format("<content>%s</content>\n", post.getContent()));
        xml.append("<hashTags>\n");
        for (String hashTag : post.getHashTags()) {
            xml.append(String.format("<hashTag>%s</hashTag>\n", hashTag));
        }
        xml.append("</hashTags>\n");
//        xml.append(String.format("<attachment>%s</attachment>\n", post.getAttachment()));
        xml.append(String.format("<attachmentName>%s</attachmentName>\n", post.getAttachmentName()));
        xml.append(String.format("<groupName>%s</groupName>\n", post.getGroupName()));
        xml.append("</post>\n");
        return xml.toString();
    }

}
