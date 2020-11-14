package message.servlets;

import message.entities.User;
import message.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

public class UserLoginServlet extends HttpServlet {
    private final UserService userService;

    public UserLoginServlet() {
        userService = new UserService();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        InputStream resourceUrl = context.getResourceAsStream("/WEB-INF/USERS.xml");

        try {
            userService.init(resourceUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);

        if (user != null) {

            HttpSession session = req.getSession();
            session.setAttribute("username", user.getUsername());

            resp.sendRedirect(req.getContextPath() + "/posts");
        } else {
            req.setAttribute("errorMessage", "Your username or password is not correct. Please try again.");
            req.getRequestDispatcher(req.getContextPath() + "/login.jsp").forward(req, resp);
        }
    }

}
