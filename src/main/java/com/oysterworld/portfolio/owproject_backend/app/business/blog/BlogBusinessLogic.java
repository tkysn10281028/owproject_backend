package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import com.oysterworld.portfolio.owproject_backend.app.database.blog.BlogDatabaseLogic;
import com.oysterworld.portfolio.owproject_backend.app.database.blog.BlogMapper;
import com.oysterworld.portfolio.owproject_backend.common.IdHashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogBusinessLogic {
    private final BlogDatabaseLogic blogDatabaseLogic;
    private final BlogValidationLogic blogValidationLogic;

    @Autowired
    public BlogBusinessLogic(BlogDatabaseLogic blogDatabaseLogic, BlogValidationLogic blogValidationLogic) {
        this.blogDatabaseLogic = blogDatabaseLogic;
        this.blogValidationLogic = blogValidationLogic;
    }

    public List<Blog> getContentByTitle(String title) {
        this.blogValidationLogic.validateBlankNull(title);
        return this.blogDatabaseLogic.getContentByTitle(title);
    }

    public Blog getContentById(String id) {
        this.blogValidationLogic.validateBlankNull(id);
        return this.blogDatabaseLogic.getContentById(id)
                .stream()
                .max(Comparator.comparing(el -> Integer.parseInt(el.getInnerId())))
                .orElse(new Blog());
    }

    public List<Blog> getAllContents() {
        return this.blogDatabaseLogic.getAllContents();
    }

    public Blog postContent(Blog blog) {
        this.blogValidationLogic.validateBlankNull(blog.getTitle());
        var blogs = this.blogDatabaseLogic.getAllContents();
        this.blogValidationLogic.validateTitleExists(blogs, blog.getTitle());
        var newestId = blogs.stream()
                .max(Comparator.comparing(el -> Integer.parseInt(el.getInnerId())))
                .map((el) -> String.valueOf(Integer.parseInt(el.getInnerId()) + 1))
                .orElse("1");
        var hashedId = IdHashUtils.hashId(newestId);
        this.blogValidationLogic.validateDuplicateHashedId(blogs, hashedId);
        var newBlog = new Blog(hashedId, blog.getTitle(), blog.getContent(), newestId);
        this.blogDatabaseLogic.postContent(newBlog);
        return newBlog;
    }
}
