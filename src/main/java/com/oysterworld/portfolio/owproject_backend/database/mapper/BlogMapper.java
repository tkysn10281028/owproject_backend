package com.oysterworld.portfolio.owproject_backend.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oysterworld.portfolio.owproject_backend.app.business.blog.Blog;

@Mapper
public interface BlogMapper {
    public List<Blog.Model> getContentByTitle(@Param("title") String title);

    public List<Blog.Model> getContentById(@Param("id") String id);

    public List<Blog.Model> getAllContents();

    public void postContent(Blog.Model model);
}
