package com.oysterworld.portfolio.owproject_backend.app.controller.blog;

import com.oysterworld.framework.http.process.handler.ResponseHandler;
import com.oysterworld.framework.http.process.handler.ResponseHandlerFactory;
import com.oysterworld.framework.http.process.writer.httpservlet.HttpServletResponseWriterFactory;
import com.oysterworld.portfolio.owproject_backend.app.business.blog.Blog;
import com.oysterworld.portfolio.owproject_backend.app.business.blog.BlogBusinessLogic;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}")
public class BlogController {
    private final BlogBusinessLogic blogBusinessLogic;
    private final HttpServletResponse response;
    private final ResponseHandler handler;

    @Autowired
    public BlogController(BlogBusinessLogic blogBusinessLogic, HttpServletResponse response) {
        this.blogBusinessLogic = blogBusinessLogic;
        this.response = response;
        this.handler = ResponseHandlerFactory
                .createInstance(HttpServletResponseWriterFactory.createInstance(this.response));
    }

    @GetMapping("/blog/title")
    public void getContentByTitle(
            @RequestParam(value = "val", required = false) String val) {
        this.handler.run((title) -> {
            return blogBusinessLogic.getContentByTitle(title);
        }, val);
    }

    @GetMapping("/blog/id")
    public void getContentById(@RequestParam(value = "val", required = false) String val) {
        this.handler.run((id) -> {
            return blogBusinessLogic.getContentById(val);
        }, val);
    }

    @GetMapping("/blog")
    public void getAllContents() {
        this.handler.run(() -> {
            return blogBusinessLogic.getAllContents();
        });
    }

    @PostMapping("/blog")
    public void postContent(@RequestBody Blog.Request req) {
        this.handler.run((blog) -> {
            return blogBusinessLogic.postContent(blog);
        }, Blog.fromRequest(req));
    }
}
