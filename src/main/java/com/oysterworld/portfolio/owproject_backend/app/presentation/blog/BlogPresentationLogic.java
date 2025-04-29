package com.oysterworld.portfolio.owproject_backend.app.presentation.blog;

import com.oysterworld.portfolio.owproject_backend.app.business.blog.Blog;
import com.oysterworld.portfolio.owproject_backend.app.business.blog.BlogBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPresentationLogic {
    private final BlogBusinessLogic blogBusinessLogic;

    @Autowired
    public BlogPresentationLogic(BlogBusinessLogic blogBusinessLogic) {
        this.blogBusinessLogic = blogBusinessLogic;
    }

    public List<BlogResponse> getContentByTitle(String title) {
        return this.blogBusinessLogic.getContentByTitle(title)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BlogResponse getContentById(String id) {
        return this.toResponse(this.blogBusinessLogic.getContentById(id));
    }

    public List<BlogResponse> getAllContents() {
        return this.blogBusinessLogic.getAllContents()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BlogResponse postContent(BlogRequest blogReq) {
        return this.toResponse(this.blogBusinessLogic.postContent(this.fromRequest(blogReq)));
    }

    public Blog fromRequest(BlogRequest req) {
        return new Blog(null, req.getTitle(), req.getContent(), null);
    }

    public BlogResponse toResponse(Blog blog) {
        return new BlogResponse(blog.getId(), blog.getTitle(), blog.getContent());
    }
}
