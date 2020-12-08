package message.servlets;

import message.entities.User;
import message.services.UserManager;
import message.utils.UserManagerFactory;

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
    public static UserManager userManager;

    public UserLoginServlet() {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        InputStream users = context.getResourceAsStream("/WEB-INF/USERS.xml");
        InputStream groups = context.getResourceAsStream("/WEB-INF/GROUPS.xml");
        InputStream membership = context.getResourceAsStream("/WEB-INF/MEMBERSHIP.xml");
        try {
            String type = config.getServletContext().getInitParameter("userManager");
            String adminGroup = config.getServletContext().getInitParameter("adminGroup");
            userManager = UserManagerFactory.getUserManager(type, groups, membership, users, adminGroup);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
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
        User user = userManager.login(username, password);

        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("username", user.getUsername());
            StringBuilder membership = new StringBuilder();
            for (String group : userManager.getGroups(user.getUsername())) {
                membership.append(group).append(",");
            }
            membership.deleteCharAt(membership.length() - 1);
            session.setAttribute("membership", membership.toString());
            resp.sendRedirect(req.getContextPath() + "/posts");
        } else {
            req.setAttribute("errorMessage", "Your username or password is not correct. Please try again.");
            req.getRequestDispatcher(req.getContextPath() + "/login.jsp").forward(req, resp);
        }
    }

}
