<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="postBean" class="message.entities.Post" scope="session" />
<html>
<head>
    <title>View a Post (Template View)</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<h1>View a Post (Template View)</h1>
<br>
<table>
    <tr>
        <td>Author: </td>
        <td>
            <jsp:getProperty name="postBean" property="postedBy"/>
        </td>
    </tr>
    <tr>
        <td>Content: </td>
        <td>
            <jsp:getProperty name="postBean" property="content"/>
        </td>
    </tr>
    <tr>
        <td>Post at: </td>
        <td>
            <jsp:getProperty name="postBean" property="formattedPostedAt"/>
        </td>
    </tr>
    <tr>
        <td>Hashtag: </td>
        <td>
            <jsp:getProperty name="postBean" property="formattedTags"/>
        </td>
    </tr>
    <tr>
        <td>Group: </td>
        <td>
            <jsp:getProperty name="postBean" property="groupName"/>
        </td>
    </tr>
</table>
<a href="./posts?postedBy=&from=&to=&hashTag=&action=recent">Go Back</a>
<div>
    <a href="./download?id=<jsp:getProperty name="postBean" property="id"/>" method="GET">Download</a>
</div>
</body>
</html>
