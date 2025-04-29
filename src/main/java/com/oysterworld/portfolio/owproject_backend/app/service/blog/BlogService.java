package com.oysterworld.portfolio.owproject_backend.app.service.blog;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oysterworld.portfolio.owproject_backend.app.business.blog.Blog;
import com.oysterworld.portfolio.owproject_backend.common.IdHashUtils;
import com.oysterworld.portfolio.owproject_backend.database.mapper.BlogMapper;

@Service
public class BlogService {
    private final BlogValidationService blogValidationService;
    private final BlogMapper blogMapper;

    @Autowired
    public BlogService(BlogValidationService blogValidationService, BlogMapper blogMapper) {
        this.blogValidationService = blogValidationService;
        this.blogMapper = blogMapper;
    }

    public List<Blog.Response> getContentByTitle(String title) {
        this.blogValidationService.validateBlankNull(title);
        return this.blogMapper.getContentByTitle(title).stream()
                .map(Blog::fromModel)
                .map(Blog::toResponse)
                .collect(Collectors.toList());
    }

    public Blog.Response getContentById(String id) {
        this.blogValidationService.validateBlankNull(id);
        return this.blogMapper.getContentById(id).stream()
                .map(Blog::fromModel)
                .max(Comparator.comparing(blog -> Integer.parseInt(blog.getInnerId())))
                .map(Blog::toResponse)
                .orElse(new Blog().toResponse());
    }

    public List<Blog.Response> getAllContents() {
        return this.blogMapper.getAllContents().stream()
                .map(Blog::fromModel)
                .map(Blog::toResponse)
                .collect(Collectors.toList());
    }

    public Blog.Response postContent(Blog.Request blogFromReq) {
        this.blogValidationService.validateBlankNull(blogFromReq.getTitle());
        var blogs = this.blogMapper.getAllContents().stream()
                .map(Blog::fromModel)
                .collect(Collectors.toList());
        this.blogValidationService.validateTitleExists(blogs, blogFromReq.getTitle());
        var newestId = blogs.stream()
                .max(Comparator.comparing(blog -> Integer.parseInt(blog.getInnerId())))
                .map((blog) -> String.valueOf(Integer.parseInt(blog.getInnerId()) + 1))
                .orElse("1");
        var hashedId = IdHashUtils.hashId(newestId);
        this.blogValidationService.validateDuplicateHashedId(blogs, hashedId);
        var blog = new Blog(hashedId, blogFromReq.getTitle(), blogFromReq.getContent(), newestId);
        this.blogMapper.postContent(blog.toModel());
        return blog.toResponse();
    }
}
