package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oysterworld.portfolio.owproject_backend.app.service.blog.BlogService;

@Service
public class BlogBusinessLogic {

    private final BlogService blogService;

    @Autowired
    public BlogBusinessLogic(BlogService blogService) {
        this.blogService = blogService;
    }

    public List<Blog.Response> getContentByTitle(String title) {
        return this.blogService.getContentByTitle(title);
    }

    public Blog.Response getContentById(String id) {
        return this.blogService.getContentById(id);
    }

    public List<Blog.Response> getAllContents() {
        return this.blogService.getAllContents();
    }

    public Blog.Response postContent(Blog blog) {
        return this.blogService.postContent(blog);
    }
}
