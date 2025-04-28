package com.oysterworld.portfolio.owproject_backend.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oysterworld.portfolio.owproject_backend.database.model.BlogModel;

@Mapper
public interface BlogMapper {
    public List<BlogModel> getContentByTitle(@Param("title") String title);

    public List<BlogModel> getContentById(@Param("id") String id);

    public List<BlogModel> getAllContents();

    public void postContent(BlogModel model);
}
