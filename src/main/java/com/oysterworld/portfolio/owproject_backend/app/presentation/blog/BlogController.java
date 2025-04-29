package com.oysterworld.portfolio.owproject_backend.app.presentation.blog;

import com.oysterworld.framework.http.process.handler.ResponseHandler;
import com.oysterworld.framework.http.process.handler.ResponseHandlerFactory;
import com.oysterworld.framework.http.process.writer.httpservlet.HttpServletResponseWriterFactory;
import com.oysterworld.portfolio.owproject_backend.app.business.blog.BlogBusinessLogic;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}")
public class BlogController {
    private final BlogPresentationLogic blogPresentationLogic;
    private final HttpServletResponse response;
    private final ResponseHandler handler;

    @Autowired
    public BlogController(BlogPresentationLogic blogPresentationLogic, HttpServletResponse response) {
        this.blogPresentationLogic = blogPresentationLogic;
        this.response = response;
        this.handler = ResponseHandlerFactory
                .createInstance(HttpServletResponseWriterFactory.createInstance(this.response));
    }

    @GetMapping("/blog/title")
    public void getContentByTitle(
            @RequestParam(value = "val", required = false) String val) {
        this.handler.run(blogPresentationLogic::getContentByTitle, val);
    }

    @GetMapping("/blog/id")
    public void getContentById(@RequestParam(value = "val", required = false) String val) {
        this.handler.run(blogPresentationLogic::getContentById, val);
    }

    @GetMapping("/blog")
    public void getAllContents() {
        this.handler.run(blogPresentationLogic::getAllContents);
    }

    @PostMapping("/blog")
    public void postContent(@RequestBody BlogRequest req) {
        this.handler.run(blogPresentationLogic::postContent, req);
    }
}
