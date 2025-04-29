package com.oysterworld.portfolio.owproject_backend.app.database.blog;

import com.oysterworld.portfolio.owproject_backend.app.business.blog.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogDatabaseLogic {
    private final BlogMapper blogMapper;

    @Autowired
    public BlogDatabaseLogic(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    public List<Blog> getContentByTitle(String title) {
        return blogMapper.getContentByTitle(title)
                .stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

    public List<Blog> getContentById(String id) {
        return blogMapper.getContentById(id)
                .stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

    public List<Blog> getAllContents() {
        return blogMapper.getAllContents()
                .stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

    public void postContent(Blog blog) {
        blogMapper.postContent(this.toModel(blog));
    }

    public Blog fromModel(BlogModel blogModel) {
        return new Blog(blogModel.getId(), blogModel.getTitle(),
                blogModel.getContent(), blogModel.getInnerId());
    }

    public BlogModel toModel(Blog blog) {
        return new BlogModel(blog.getId(), blog.getTitle(), blog.getContent(), blog.getInnerId());
    }
}
