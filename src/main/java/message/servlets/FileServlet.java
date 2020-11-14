package message.servlets;

import message.entities.Post;
import message.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@MultipartConfig
public class FileServlet extends HttpServlet {
    private final PostService postService;

    public FileServlet() {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Post post = postService.find(Integer.parseInt(id));

        if (post.getAttachmentName() == null) {
            resp.sendError(404, "No attachment found.");
            return;
        }
        String mime = Files.probeContentType(post.getAttachment());
        resp.addHeader("Content-Type", mime);
        resp.addHeader("Expires", "0");
        resp.addHeader("Content-Disposition", "attachment; filename=\"" + post.getAttachmentName() + "\"");

        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(post.getAttachment(), out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        switch (action) {
            case "Upload":
                Part filePart = req.getPart("file");
                if (filePart.getSize() > 0) {
                    postService.upload(Integer.parseInt(id), filePart);
                }
                break;

            case "Remove Attachment":
                postService.remove(Integer.parseInt(id));
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/posts");

    }
}
