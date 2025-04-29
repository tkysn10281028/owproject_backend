package com.oysterworld.portfolio.owproject_backend.app.database.blog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {
    public List<BlogModel> getContentByTitle(@Param("title") String title);

    public List<BlogModel> getContentById(@Param("id") String id);

    public List<BlogModel> getAllContents();

    public void postContent(BlogModel model);
}
