package message.servlets;

import message.entities.Post;
import message.services.IPostService;
import message.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewPostServlet")
public class ViewPostServlet extends HttpServlet {
    private final IPostService postService;

    public ViewPostServlet() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Post post = postService.find(Integer.parseInt(id));
        request.setAttribute("postBean", post);
        getServletContext().getRequestDispatcher("/templateview.jsp").forward(request,response);

    }
}
