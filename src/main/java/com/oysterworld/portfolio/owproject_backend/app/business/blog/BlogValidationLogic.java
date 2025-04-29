package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import com.oysterworld.portfolio.owproject_backend.exception.OwBadRequestException;
import com.oysterworld.portfolio.owproject_backend.exception.OwInternalServerErrorException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogValidationLogic {
    public void validateBlankNull(String val) {
        if (val == null || val.trim().isEmpty()) {
            throw new OwBadRequestException("input parameter is empty.",
                    val);
        }
    }

    public void validateTitleExists(List<Blog> blogs, String title) {
        if (blogs.stream().anyMatch((blog) -> blog.getTitle().equals(title))) {
            throw new OwBadRequestException("input title already exists.", title);
        }
    }

    public void validateDuplicateHashedId(List<Blog> blogs, String hashedId) {
        if (blogs.stream().anyMatch((blog) -> blog.getId().equals(hashedId))) {
            throw new OwInternalServerErrorException("Duplicate id detected.");
        }
    }
}
