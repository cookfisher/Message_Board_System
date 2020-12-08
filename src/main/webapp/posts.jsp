<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Posts</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<style>
    body  {
        filter: invert(100%);
    }
    body {
        background-color: black;
    }
</style>
<body>
<div class="box d-flex align-items-center">
    <div class="container">
        <div class="row justify-content-end">
            <div class="col-3">
                <form action="<%= response.encodeURL("LogoutServlet") %>" method="post" class="mt-2">
                    <button type="submit" name="request" value="Logout" class="btn btn-primary">
                        Logout
                    </button>
                </form>
            </div>
        </div>

        <nav>
            <form class="form-inline" method="get" action="${pageContext.request.contextPath}/posts">
                <div class="form-row">
                    <div class="form-group mb-2">
                        <label for="postedBy" style="margin-right: .5rem;">Posted by</label>
                        <input type="text" class="form-control" id="postedBy" name="postedBy">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="from" style="margin-right: .5rem;">From</label>
                        <input type="date" class="form-control" id="from" name="from">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="to" style="margin-right: .5rem;">To</label>
                        <input type="date" class="form-control" id="to" name="to">
                    </div>
                    <div class="form-group mb-2">
                        <label for="hashTag" style="margin-right: .5rem;">Hash Tag</label>
                        <input type="text" class="form-control" id="hashTag" name="hashTag">
                    </div>
                    <button type="submit" style="margin-right: .5rem;" class="btn btn-primary mb-2" name="action"
                            value="search">Search
                    </button>
                    <button type="submit" class="btn btn-primary mb-2" name="action" value="recent">View recent posts
                    </button>
                </div>
            </form>

        </nav>

        <table class="table-responsive">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Content</th>
                <th scope="col">Posted At</th>
                <th scope="col">Posted By</th>
                <th scope="col">Group Name</th>
                <th scope="col">Updated</th>
                <th scope="col">Hash Tags</th>
                <th scope="col">Attachment</th>
                <th scope="col">Attach</th>
                <th scope="col">Actions</th>
                <th scope="col">View a Post</th>
            </tr>
            </thead>
            <tbody>
            <c:set var="username" scope="session" value="${sessionScope.username}"/>
            <c:choose>
                <c:when test="${sessionScope.membership.contains('admins')}">
                    <c:forEach var="post" items="${requestScope.posts}">
                        <tr>
                            <th scope="row">${post.id}</th>
                            <td>
                                <label class="sr-only" for="${post.id}_content"></label>
                                <textarea id="${post.id}_content" type="text" form="${post.id}_form" name="post" row="1">
                                        ${post.content}</textarea>
                            </td>
                            <td>${post.formattedPostedAt}</td>
                            <td>${post.postedBy}</td>
                            <td>${post.groupName}</td>
                            <td>${post.updated}</td>
                            <td>${post.formattedTags}</td>
                            <td>
                                <c:if test="${post.attachmentName != null}"><a type="button" class="btn btn-secondary" href="${pageContext.request.contextPath}/file?id=${post.id}">${post.attachmentName}</a></c:if>
                            </td>
                            <td>
                                <div id="modal_${post.id}" class="modal fade">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Attachment</h5>
                                                <div type="button" class="close">
                                                    <span class="button" data-dismiss="modal" aria-hidden="true">&times;</span>
                                                </div>
                                            </div>
                                            <div class="modal-body">
                                                <form enctype="multipart/form-data" id="form_upload_${post.id}"
                                                      action="${pageContext.request.contextPath}/file"
                                                      method="post">
                                                    <input type="hidden" value="${post.id}" name="id"/>
                                                    <input type="file" id="${post.id}_file" name="file">
                                                    <c:if test="${post.attachmentName != null}"><a type="button" class="btn btn-secondary" href="${pageContext.request.contextPath}/file?id=${post.id}">Download</a></c:if>
                                                    <input type="submit" class="btn btn-primary" name="action" value="Upload">
                                                    <input type="submit" class="btn btn-danger" name="action"
                                                           value="Remove Attachment">
                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <div type="button" class="btn btn-secondary" data-dismiss="modal">Close</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="btn btn-success" data-toggle="modal" data-target="#modal_${post.id}">
                                    Upload
                                </div>
                            </td>
                            <td>
                                <form id="${post.id}_form" class="form-inline" action="${pageContext.request.contextPath}/posts"
                                      method="post">
                                    <input type="hidden" value="${post.id}" name="id"/>
                                    <button class="btn btn-secondary" name="action" value="edit">Update</button>
                                    <button class="btn btn-danger" name="action" value="delete">Delete</button>
                                </form>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/view?id=${post.id}" class="btn btn-primary">View</a></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach var="post" items="${requestScope.posts}">
                        <tr>
                            <th scope="row">${post.id}</th>
                            <td>
                                <label class="sr-only" for="${post.id}_content"></label>
                                <c:choose>
                                    <c:when test="${post.postedBy == username}">
                                         <textarea id="${post.id}_content" type="text" form="${post.id}_form" name="post" row="1">
                                                 ${post.content}</textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <p id="${post.id}_content" type="text" form="${post.id}_form" name="post">${post.content}</p>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${post.formattedPostedAt}</td>
                            <td>${post.postedBy}</td>
                            <td>${post.groupName}</td>
                            <td>${post.updated}</td>
                            <td>${post.formattedTags}</td>
                            <td>
                                <c:if test="${post.attachmentName != null}"><a type="button" class="btn btn-secondary" href="${pageContext.request.contextPath}/file?id=${post.id}">${post.attachmentName}</a></c:if>
                            </td>
                            <td>
                                <div id="modal_${post.id}" class="modal fade">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Attachment</h5>
                                                <div type="button" class="close">
                                                    <span class="button" data-dismiss="modal" aria-hidden="true">&times;</span>
                                                </div>
                                            </div>
                                            <div class="modal-body">
                                                <form enctype="multipart/form-data" id="form_upload_${post.id}"
                                                      action="${pageContext.request.contextPath}/file"
                                                      method="post">
                                                    <input type="hidden" value="${post.id}" name="id"/>
                                                    <input type="file" id="${post.id}_file" name="file">
                                                    <c:if test="${post.attachmentName != null}"><a type="button" class="btn btn-secondary" href="${pageContext.request.contextPath}/file?id=${post.id}">Download</a></c:if>
                                                    <input type="submit" class="btn btn-primary" name="action" value="Upload">
                                                    <input type="submit" class="btn btn-danger" name="action"
                                                           value="Remove Attachment">
                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <div type="button" class="btn btn-secondary" data-dismiss="modal">Close</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                                            <c:if test="${post.postedBy == username}">
                                <div class="btn btn-success" data-toggle="modal" data-target="#modal_${post.id}">
                                    Upload
                                </div>
                                                            </c:if>
                            </td>
                            <td>
                                                            <c:if test="${post.postedBy == username}">
                                <form id="${post.id}_form" class="form-inline" action="${pageContext.request.contextPath}/posts"
                                      method="post">
                                    <input type="hidden" value="${post.id}" name="id"/>
                                    <button class="btn btn-secondary" name="action" value="edit">Update</button>
                                    <button class="btn btn-danger" name="action" value="delete">Delete</button>
                                </form>
                                                        </c:if>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/view?id=${post.id}" class="btn btn-primary">View</a></td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

            </tbody>
        </table>

        <ul class="list-group">
        </ul>

        <p style="color: red">
            ${requestScope.errorMessage}
        </p>
        <form method="post" action="${pageContext.request.contextPath}/posts">
            <div class="form-group">
                <label for="post">Post</label>
                <textarea class="form-control" id="post" name="post" rows="1"></textarea>
            </div>
            <div class="form-group">
                <label for="group_name">Group Name(${sessionScope.get("membership")}):</label>
                <input type="text" class="form-control" id="group_name" name="group_name">
            </div>
            <button type="submit" class="btn btn-primary" name="action" value="add">Post</button>
        </form>

    </div>
</div>


<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>
</body>
</html>
