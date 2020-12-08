package message.servlets;

import message.entities.Post;
import message.services.IPostService;
import message.services.PostService;
import message.utils.XMLTransformer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;

public class DownServlet extends HttpServlet {
    private final IPostService postService;

    public DownServlet() {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Post post = postService.find(Integer.parseInt(id));
        resp.addHeader("Content-Type", "text/xml; charset=utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=post1.xml");
        try (PrintStream out = new PrintStream(resp.getOutputStream())) {
            out.println(XMLTransformer.transform(post));
        }
    }
}
