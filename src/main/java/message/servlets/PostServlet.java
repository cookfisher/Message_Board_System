package message.servlets;

import message.entities.Post;
import message.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
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
        this.postService.init(jdbcUrl, jdbcUsername, jdbcPassword);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        String action = req.getParameter("action");
        String content = req.getParameter("post");
        String id = req.getParameter("id");
        switch (action) {
            case "add":
                if (content == null || content.isEmpty()) {
                    req.setAttribute("errorMessage", "Empty post is not allowed.");
                    doGet(req, resp);
                    return;
                }
                postService.create(username, content);
                break;
            case "edit":
                if (content == null || content.isEmpty()) {
                    req.setAttribute("errorMessage", "Empty post is not allowed.");
                    doGet(req, resp);
                    return;
                }
                postService.update(Integer.parseInt(id), content);
                break;
            case "delete":
                postService.delete(Integer.parseInt(id));
                break;


            default:

        }

        resp.sendRedirect(req.getContextPath() + "/posts");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
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
                postStream = postService.search(postedBy, from, to, hashTag)
                        .sorted(Comparator.comparing(Post::getPostedAt).reversed());
                break;
            case "recent":
            default:
                postStream = postService.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Post::getPostedAt).reversed())
                        .limit(Long.parseLong(getServletContext().getInitParameter("pageSize")));
        }

        List<Post> posts = postStream.collect(Collectors.toList());

        req.setAttribute("posts", posts);
        req.getRequestDispatcher("/posts.jsp").forward(req, resp);
    }
}
