package message.servlets;

import message.entities.Post;
import message.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostServlet extends HttpServlet {
    private final PostService postService;

    public PostServlet() {
        this.postService = PostService.getInstance();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String jdbcUrl = config.getServletContext().getInitParameter("jdbcUrl");
        String jdbcUsername = config.getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = config.getServletContext().getInitParameter("jdbcPassword");
        this.postService.init(jdbcUrl, jdbcUsername, jdbcPassword, UserLoginServlet.userManager);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        String action = req.getParameter("action");
        String content = req.getParameter("post");
        String id = req.getParameter("id");
        String groupName = req.getParameter("group_name");
        switch (action) {
            case "add":
                if (content == null || content.isEmpty()) {
                    req.setAttribute("errorMessage", "Empty post is now allowed.");
                    doGet(req, resp);
                    return;
                }
                if (groupName != null && !groupName.isEmpty()
                        && !UserLoginServlet.userManager.getGroups(username).contains(groupName)) {
                    req.setAttribute("errorMessage", "invalid group name.");
                    doGet(req, resp);
                    return;
                }
                postService.create(username, content, groupName);
                break;
            case "edit":
                if (content == null || content.isEmpty()) {
                    req.setAttribute("errorMessage", "Empty post is now allowed.");
                    doGet(req, resp);
                    return;
                }
                postService.update(username, Integer.parseInt(id), content);
                break;
            case "delete":
                postService.delete(username, Integer.parseInt(id));
                break;


            default:

        }

        resp.sendRedirect(req.getContextPath() + "/posts");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        if (action == null) {
            action = "recent";
        }
        Stream<Post> postStream;
        switch (action) {
            case "search":
                String postedBy = req.getParameter("postedBy");
                String from = req.getParameter("from");
                String to = req.getParameter("to");
                String hashTag = req.getParameter("hashTag");
                postStream = postService.search(username, postedBy, from, to, hashTag)
                        .sorted(Comparator.comparing(Post::getPostedAt).reversed());
                break;
            case "recent":
            default:
                postStream = postService.findAll(username)
                        .stream()
                        .sorted(Comparator.comparing(Post::getPostedAt).reversed())
                        .limit(Long.parseLong(getServletContext().getInitParameter("pageSize")));
        }

        List<Post> posts = postStream.collect(Collectors.toList());

        req.setAttribute("posts", posts);
        req.getRequestDispatcher("/posts.jsp").forward(req, resp);
    }
}
